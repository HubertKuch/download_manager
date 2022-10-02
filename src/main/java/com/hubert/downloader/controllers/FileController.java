package com.hubert.downloader.controllers;

import com.hubert.downloader.domain.exceptions.FileNotFoundException;
import com.hubert.downloader.domain.exceptions.UserCantDownloadFile;
import com.hubert.downloader.domain.models.file.File;
import com.hubert.downloader.domain.models.file.dto.FileIncomingDTO;
import com.hubert.downloader.domain.models.file.dto.FileWithoutPath;
import com.hubert.downloader.domain.models.tokens.Token;
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.services.FileService;
import com.hubert.downloader.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    @PostMapping("/")
    public User addFile(
            @RequestBody FileIncomingDTO fileIncomingDTO,
            @RequestHeader(name = "Authorization") String token
    ) throws UserCantDownloadFile {
        File requestedFile = fileService.getRequestedFile(fileIncomingDTO);
        requestedFile.setId(UUID.randomUUID());
        User user = userService.findByToken(new Token(token));

        return fileService.addFile(user, requestedFile);
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
