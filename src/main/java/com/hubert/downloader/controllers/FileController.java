package com.hubert.downloader.controllers;

import com.hubert.downloader.domain.exceptions.UserCantDownloadFile;
import com.hubert.downloader.domain.models.file.File;
import com.hubert.downloader.domain.models.file.dto.FileIncomingDTO;
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.services.FileService;
import com.hubert.downloader.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    @PostMapping("/add/")
    public User addFile(@RequestBody FileIncomingDTO fileIncomingDTO) throws UserCantDownloadFile {
        File requestedFile = fileService.getRequestedFile(fileIncomingDTO);
        User user = userService.findByToken(fileIncomingDTO.token());

        return fileService.downloadFile(user, requestedFile);
    }
}
