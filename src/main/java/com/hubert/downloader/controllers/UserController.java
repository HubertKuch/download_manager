package com.hubert.downloader.controllers;

import com.hubert.downloader.domain.models.tokens.Token;
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.domain.models.user.dto.NewUserDTO;
import com.hubert.downloader.domain.models.user.dto.UserWithoutPathInFilesDTO;
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

    @GetMapping("/")
    public List<UserWithoutPathInFilesDTO> getUsers() {

        return userService.getUsers().stream().map(User::parseToDto).toList();
    }

    @GetMapping("/logged/")
    public UserWithoutPathInFilesDTO getLoggedInUser(@RequestHeader(name = "Authorization") String token) {

        return userService.findByToken(new Token(token.replace("Bearer ", ""))).parseToDto();
    }

    @PostMapping("/")
    public User saveUser(@RequestBody NewUserDTO user) {

        return userService.saveUser(User.fromDTO(user));
    }
}
