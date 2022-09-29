package com.hubert.downloader.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubert.downloader.domain.exceptions.InvalidAccessCode;
import com.hubert.downloader.domain.models.tokens.Token;
import com.hubert.downloader.domain.models.tokens.TokenResponse;
import com.hubert.downloader.domain.models.user.AccessCodeDTO;
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.services.TokenService;
import com.hubert.downloader.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final TokenService tokenService;
    private final UserService userService;

    @PostMapping("/login/")
    public void login(@RequestBody AccessCodeDTO accessCode, HttpServletResponse response) throws IOException, InvalidAccessCode {
        User user = userService.findByAccessCode(accessCode);

        if (user == null) {
            throw new InvalidAccessCode(String.format("Access code - %s -is invalid", accessCode.accessCode().toString()));
        }

        Token token = tokenService.sign(accessCode);

        response.getOutputStream().write(
                new ObjectMapper()
                        .writeValueAsString(new TokenResponse(token)).getBytes()
        );

        response.setHeader("Content-Type", "application/json");
    }
}
