package com.hubert.downloader.controllers;

import com.hubert.downloader.domain.InformationSize;
import com.hubert.downloader.domain.InformationUnit;
import com.hubert.downloader.domain.exceptions.UserCantDownloadFile;
import com.hubert.downloader.models.File;
import com.hubert.downloader.models.User;
import com.hubert.downloader.services.FileService;
import com.hubert.downloader.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    @PostMapping("/download/")
    public User downloadFile() throws UserCantDownloadFile {
        File file = new File("test", "testfile.txt", new InformationSize(InformationUnit.MEGA_BYTE, 500F));
        User user = userService.getUsers().get(0);

        return fileService.downloadFile(user, file);
    }
}
