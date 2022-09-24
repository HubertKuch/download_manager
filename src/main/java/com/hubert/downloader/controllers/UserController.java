package com.hubert.downloader.controllers;

import com.hubert.downloader.models.User;
import com.hubert.downloader.services.FileService;
import com.hubert.downloader.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final FileService fileService;

    @GetMapping("/")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping("/")
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
}
