package com.hubert.downloader.controllers;

import com.hubert.downloader.domain.exceptions.UserCantDownloadFile;
import com.hubert.downloader.domain.models.file.File;
import com.hubert.downloader.domain.models.file.dto.FileIncomingDTO;
import com.hubert.downloader.domain.models.tokens.Token;
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.services.FileService;
import com.hubert.downloader.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    @PostMapping("/")
    public User addFile(@RequestBody FileIncomingDTO fileIncomingDTO, @RequestHeader(name = "Authorization") String token) throws UserCantDownloadFile {
        File requestedFile = fileService.getRequestedFile(fileIncomingDTO);
        requestedFile.setId(UUID.randomUUID());
        User user = userService.findByToken(new Token(token));

        return fileService.downloadFile(user, requestedFile);
    }

    @GetMapping("/")
    public List<File> getFiles(@RequestHeader(name = "Authorization") String token) {
        User user = userService.findByToken(new Token(token));

        return user.getFiles();
    }
}
