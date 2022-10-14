package com.hubert.downloader.controllers;

import com.hubert.downloader.builders.HamsterPageBuilder;
import com.hubert.downloader.domain.exceptions.FileNotFoundException;
import com.hubert.downloader.domain.exceptions.FolderRequiresPasswordException;
import com.hubert.downloader.domain.exceptions.HamsterFolderLinkIsInvalid;
import com.hubert.downloader.domain.exceptions.UserCantDownloadFile;
import com.hubert.downloader.domain.models.file.File;
import com.hubert.downloader.domain.models.file.Folder;
import com.hubert.downloader.domain.models.file.IncomingDataRequestWithPassword;
import com.hubert.downloader.domain.models.file.dto.*;
import com.hubert.downloader.domain.models.tokens.Token;
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.domain.models.user.dto.UserWithoutPathInFilesDTO;
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

        user.addHistoryOfAddedFiles(List.of(requestedFile));

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

        user.addHistoryOfAddedFiles(requestedFolder.files());

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

        user.addHistoryOfDownloadedFiles(List.of(file));

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

        user.addHistoryOfDownloadedFiles(folder.files());

        return fileService.downloadFolder(user, folder);
    }
}
