package com.hubert.downloader.controllers;

import com.hubert.downloader.domain.exceptions.UserCantDownloadFile;
import com.hubert.downloader.domain.models.file.dto.FileIncomingDTO;
import com.hubert.downloader.domain.models.file.File;
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.repositories.UserRepository;
import com.hubert.downloader.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {
    private final FileService fileService;
    private final UserRepository userRepository;

    @PostMapping("/add/")
    public void addFile(@RequestBody FileIncomingDTO fileIncomingDTO) throws UserCantDownloadFile {
        File requestedFile = fileService.getRequestedFile(fileIncomingDTO);
//        User user = userRepository.findAll().get(0);
//
//        return fileService.downloadFile(user, requestedFile);
    }
}
