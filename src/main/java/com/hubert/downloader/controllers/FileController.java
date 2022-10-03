package com.hubert.downloader.controllers;

import com.hubert.downloader.domain.exceptions.FileNotFoundException;
import com.hubert.downloader.domain.exceptions.HamsterFolderLinkIsInvalid;
import com.hubert.downloader.domain.exceptions.UserCantDownloadFile;
import com.hubert.downloader.domain.models.file.File;
import com.hubert.downloader.domain.models.file.dto.FileIncomingDTO;
import com.hubert.downloader.domain.models.file.dto.FileWithoutPath;
import com.hubert.downloader.domain.models.file.dto.NewFileDTO;
import com.hubert.downloader.domain.models.tokens.Token;
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.domain.models.user.dto.UserWithoutPathInFilesDTO;
import com.hubert.downloader.services.FileService;
import com.hubert.downloader.services.UserService;
import com.hubert.downloader.utils.HamsterFolderPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.io.*;
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
        user = fileService.addFile(user, requestedFile);

        return user.parseToDto();
    }

    @GetMapping("/")
    public List<FileWithoutPath> getFiles(@RequestHeader(name = "Authorization") String token) {
        User user = userService.findByToken(new Token(token));

        return user
                .getFiles()
                .stream()
                .map(file -> new FileWithoutPath(
                        file.getId(),
                        file.getName(),
                        file.getExtension(),
                        file.getSize()
                ))
                .toList();
    }

    @GetMapping("/resource/{id}/")
    public File downloadFile(
            @PathVariable String id,
            @RequestHeader(name = "Authorization") String token
    ) throws FileNotFoundException, UserCantDownloadFile, IOException {
        User user = userService.findByToken(new Token(token.replace("Bearer ", "")));

        List<File> matchedFiles = user
                .getFiles()
                .stream()
                .filter(file -> file.getId().toString().equals(id))
                .toList();

        if (matchedFiles.isEmpty()) {
            throw new FileNotFoundException(String.format("File with id - %s - not found.", id));
        }

        File file = matchedFiles.get(0);

        return fileService.downloadFile(user, file);
    }
}
