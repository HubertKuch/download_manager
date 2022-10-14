package com.hubert.downloader.controllers;

import com.hubert.downloader.builders.HamsterPageBuilder;
import com.hubert.downloader.domain.exceptions.*;
import com.hubert.downloader.domain.models.file.File;
import com.hubert.downloader.domain.models.file.Folder;
import com.hubert.downloader.domain.models.file.IncomingDataRequestWithPassword;
import com.hubert.downloader.domain.models.file.dto.*;
import com.hubert.downloader.domain.models.history.History;
import com.hubert.downloader.domain.models.tokens.Token;
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.domain.models.user.dto.UserWithoutPathInFilesDTO;
import com.hubert.downloader.domain.responses.RemovedFileReponse;
import com.hubert.downloader.domain.responses.RemovedFolderResponse;
import com.hubert.downloader.external.coreapplication.modelsgson.GetDownloadUrl;
import com.hubert.downloader.external.coreapplication.requestsgson.async.PasswordRequiredException;
import com.hubert.downloader.external.pl.kubikon.chomikmanager.api.*;
import com.hubert.downloader.services.FileService;
import com.hubert.downloader.services.UserService;
import com.hubert.downloader.utils.HamsterFolderPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {
    private final FileService fileService;
    private final UserService userService;
    private final HamsterPageBuilder hamsterPageBuilder;

    @PostMapping("/")
    public UserWithoutPathInFilesDTO addFile(
            @RequestBody NewFileDTO fileIncomingDTO,
            @RequestHeader(name = "Authorization") String token
    ) throws PasswordRequiredException, Exception {
        HamsterFolderPage hamsterFolderPage = getHamsterPagePreventsPasswordIssues(fileIncomingDTO);

        FileIncomingDTO incomingFile = new FileIncomingDTO(
                fileIncomingDTO.fileName(),
                hamsterFolderPage.getFolderId(),
                hamsterFolderPage.getAccountName()
        );

        File requestedFile = fileService.getRequestedFile(incomingFile);
        User user = userService.findByToken(new Token(token));

        user.addHistory(History.ofAddedFiles(requestedFile));

        user = fileService.addFile(user, requestedFile, hamsterFolderPage.getFolder());

        userService.saveUser(user);

        return user.parseToDto();
    }

    @PostMapping("/whole-folder/")
    public UserWithoutPathInFilesDTO addFileFromWholeFolder(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody FileWithUrlAndPasswordInfo incomingFile
    ) throws Exception, PasswordRequiredException {
        User user = userService.findByToken(new Token(token));
        HamsterFolderPage hamsterFolderPage = getHamsterPagePreventsPasswordIssues(incomingFile);

        Folder requestedFolder = fileService.getRequestedFolder(new IncomingFolderDTO(
                incomingFile.url(),
                hamsterFolderPage.getAccountName(),
                hamsterFolderPage.getFolderName(),
                hamsterFolderPage.getFolderId(),
                incomingFile.passwordData()
        ));

        user.addHistory(History.ofAddedFiles(requestedFolder.files()));

        requestedFolder.files().forEach(file -> {
            try {
                fileService.addFile(user, file, hamsterFolderPage.getFolder());
            } catch (UserCantDownloadFile | HamsterFolderLinkIsInvalid | FolderRequiresPasswordException e) {
                throw new RuntimeException(e);
            }
        });

        userService.saveUser(user);

        return user.parseToDto();
    }

    private HamsterFolderPage getHamsterPagePreventsPasswordIssues(
            IncomingDataRequestWithPassword passwordRequest
    ) throws Exception, PasswordRequiredException {
        return hamsterPageBuilder.buildPage(passwordRequest);
    }

    @GetMapping("/")
    public List<FolderWithFilesWithoutPaths> getFiles(@RequestHeader(name = "Authorization") String token) {
        User user = userService.findByToken(new Token(token));

        return user
                .getFolders()
                .stream()
                .map(Folder::parseExcludingPaths)
                .sorted(Comparator.comparingLong(o -> o.addedAt().getTime()))
                .toList();
    }

    @GetMapping("/resource/{id}/")
    public File downloadFile(
            @PathVariable String id,
            @RequestHeader(name = "Authorization") String token
    ) throws Exception, PasswordRequiredException {
        User user = userService.findByToken(new Token(token.replace("Bearer ", "")));

        List<File> matchedFiles = user
                .getFolders()
                .stream()
                .map(Folder::files)
                .flatMap(Collection::stream)
                .filter(file -> file.getId().toString().equals(id))
                .toList();

        if (matchedFiles.isEmpty()) {
            throw new FileNotFoundException(String.format("File with id - %s - not found.", id));
        }

        File file = matchedFiles.get(0);

        GetDownloadUrl url = AndroidApi.getDownloadUrl(file.getHamsterId());

        user.addHistory(History.ofDownloadedFiles(file));

        file.setPath(url.fileUrl);

        return fileService.downloadFile(user, file);
    }

    @GetMapping("/resource/whole-folder/{id}/")
    public Folder downloadFolder(
            @PathVariable String id,
            @RequestHeader(name = "Authorization") String token
    ) throws FileNotFoundException, UserCantDownloadFile {
        User user = userService.findByToken(new Token(token.replace("Bearer ", "")));

        List<Folder> matchedFolders = user
                .getFolders()
                .stream()
                .filter(folder -> folder.id().equals(id))
                .toList();

        if (matchedFolders.isEmpty()) {
            throw new FileNotFoundException(String.format("Folder with id - %s - not found.", id));
        }

        Folder folder = matchedFolders.get(0);

        folder.files().forEach(file -> {
            GetDownloadUrl url = null;

            try {
                url = AndroidApi.getDownloadUrl(file.getHamsterId());
            } catch (Exception | PasswordRequiredException e) {
                throw new RuntimeException(e);
            }

            file.setPath(url.fileUrl);
        });

        user.addHistory(History.ofDownloadedFiles(folder.files()));

        userService.saveUser(user);

        return fileService.downloadFolder(user, folder);
    }

    @DeleteMapping("/file/")
    public RemovedFileReponse removeFile(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody FileToDeleteDTO fileToDeleteDTO
    ) throws InvalidRequestDataException {
        User user = userService.findByToken(new Token(token));
        Optional<Folder> optionalFolder = fileService.findFolderById(user, fileToDeleteDTO.folderId());
        Optional<File> optionalFile = fileService.findFileById(user, fileToDeleteDTO.fileId());

        if (optionalFolder.isEmpty() || optionalFile.isEmpty()) {
            throw new InvalidRequestDataException("Invalid request data. File or optionalFolder id are incorrect.");
        }

        Folder folder = optionalFolder.get();
        File file = optionalFile.get();

        fileService.removeFile(user, folder, file);
        user.addHistory(History.ofDeletedFile(file));

        userService.saveUser(user);

        return new RemovedFileReponse("File removed successfully.", file);
    }

    @DeleteMapping("/folder/")
    public RemovedFolderResponse removeFolder(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody FolderToRemoveDTO fileToDeleteDTO
    ) throws InvalidRequestDataException {
        User user = userService.findByToken(new Token(token));
        Optional<Folder> optionalFolder = fileService.findFolderById(user, fileToDeleteDTO.folderId());

        if (optionalFolder.isEmpty()) {
            throw new InvalidRequestDataException("Invalid request data. File or optionalFolder id are incorrect.");
        }

        Folder folder = optionalFolder.get();

        fileService.removeFolder(user, folder);
        user.addHistory(History.ofDeletedFolder(folder));

        userService.saveUser(user);

        return new RemovedFolderResponse("Folder removed successfully.", folder);
    }
}
