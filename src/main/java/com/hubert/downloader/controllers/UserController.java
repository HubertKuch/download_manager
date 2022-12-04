package com.hubert.downloader.controllers;

import com.hubert.downloader.domain.models.file.dto.FolderWithFilesWithoutPaths;
import com.hubert.downloader.domain.models.history.History;
import com.hubert.downloader.domain.models.tokens.Token;
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.domain.models.user.dto.NewUserDTO;
import com.hubert.downloader.domain.models.user.dto.UserWithoutPathInFilesDTO;
import com.hubert.downloader.domain.models.user.responses.UserDataEntity;
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

    @GetMapping("/logged/histories/")
    public List<History> histories(@RequestHeader(name = "Authorization") String token) {
        User user = userService.findByToken(new Token(token));

        return user.getHistories();
    }

    @GetMapping("/logged/folders/")
    public List<FolderWithFilesWithoutPaths> folders(@RequestHeader(name = "Authorization") String token) {
        User user = userService.findByToken(new Token(token));

        return user.parseToDto().folders();
    }

    @GetMapping("/logged/")
    public UserDataEntity getLoggedInUser(@RequestHeader(name = "Authorization") String token) {
        return userService.findByToken(new Token(token.replace("Bearer ", ""))).toDataEntity();
    }

    @PostMapping("/")
    public User saveUser(@RequestBody NewUserDTO user) {
        return userService.saveUser(User.fromDTO(user));
    }
}
