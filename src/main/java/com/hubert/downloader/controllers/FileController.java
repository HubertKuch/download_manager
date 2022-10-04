package com.hubert.downloader.controllers;

import com.hubert.downloader.domain.exceptions.FileNotFoundException;
import com.hubert.downloader.domain.exceptions.HamsterFolderLinkIsInvalid;
import com.hubert.downloader.domain.exceptions.UserCantDownloadFile;
import com.hubert.downloader.domain.models.file.File;
import com.hubert.downloader.domain.models.file.Folder;
import com.hubert.downloader.domain.models.file.dto.*;
import com.hubert.downloader.domain.models.tokens.Token;
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.domain.models.user.dto.UserWithoutPathInFilesDTO;
import com.hubert.downloader.services.FileService;
import com.hubert.downloader.services.UserService;
import com.hubert.downloader.utils.HamsterFolderPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    @PostMapping("/")
    public UserWithoutPathInFilesDTO addFile(
            @RequestBody NewFileDTO fileIncomingDTO,
            @RequestHeader(name = "Authorization") String token
    ) throws UserCantDownloadFile, HamsterFolderLinkIsInvalid {
        HamsterFolderPage hamsterFolderPage = HamsterFolderPage.from(fileIncomingDTO.url());

        FileIncomingDTO incomingFile = new FileIncomingDTO(
                fileIncomingDTO.fileName(),
                hamsterFolderPage.getFolderId(),
                hamsterFolderPage.getAccountName()
        );

        File requestedFile = fileService.getRequestedFile(incomingFile);
        User user = userService.findByToken(new Token(token));
        user = fileService.addFile(user, requestedFile, hamsterFolderPage.getFolder());

        return user.parseToDto();
    }

    @PostMapping("/whole-folder/")
    public UserWithoutPathInFilesDTO addFileFromWholeFolder(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody FileWithOnlyUrlDTO incomingFile
    ) throws HamsterFolderLinkIsInvalid {
        User user = userService.findByToken(new Token(token));

        HamsterFolderPage hamsterFolderPage = HamsterFolderPage.from(incomingFile.url());
        Folder requestedFolder = fileService.getRequestedFolder(new IncomingFolderDTO(
                incomingFile.url(),
                hamsterFolderPage.getAccountName(),
                hamsterFolderPage.getFolderName(),
                hamsterFolderPage.getFolderId()
        ));

        requestedFolder.files().forEach(file -> {
            try {
                fileService.addFile(user, file, hamsterFolderPage.getFolder());
            } catch (UserCantDownloadFile | HamsterFolderLinkIsInvalid e) {
                throw new RuntimeException(e);
            }
        });

        return user.parseToDto();
    }

    @GetMapping("/")
    public List<FolderWithFilesWithoutPaths> getFiles(@RequestHeader(name = "Authorization") String token) {
        User user = userService.findByToken(new Token(token));

        return user
                .getFolders()
                .stream()
                .map(Folder::parseExcludingPaths)
                .toList();
    }

    @GetMapping("/resource/{id}/")
    public File downloadFile(
            @PathVariable String id,
            @RequestHeader(name = "Authorization") String token
    ) throws FileNotFoundException, UserCantDownloadFile {
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

        return fileService.downloadFolder(user, folder);
    }
}
