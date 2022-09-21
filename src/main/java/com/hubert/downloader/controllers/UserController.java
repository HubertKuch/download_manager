package com.hubert.downloader.controllers;

import com.hubert.downloader.models.AllowedTransfer;
import com.hubert.downloader.models.User;
import com.hubert.downloader.repositories.UserRepository;
import com.hubert.downloader.services.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public List<User> testUser() {
        return userService.getUsers();
    }
}
