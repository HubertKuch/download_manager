package com.hubert.downloader.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubert.downloader.domain.exceptions.InvalidAccessCode;
import com.hubert.downloader.domain.models.responses.UserExpiredResponse;
import com.hubert.downloader.domain.models.tokens.Token;
import com.hubert.downloader.domain.models.responses.TokenResponse;
import com.hubert.downloader.domain.models.user.AccessCodeDTO;
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.services.TokenService;
import com.hubert.downloader.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@RestController

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final TokenService tokenService;
    private final UserService userService;

    @PostMapping("/login/")
    public void login(@RequestBody AccessCodeDTO accessCode, HttpServletResponse response) throws IOException, InvalidAccessCode {
        User user = userService.findByAccessCode(accessCode);
        response.setHeader("Content-Type", "application/json");

        if (user == null) {
            throw new InvalidAccessCode(String.format("Access code - %s -is invalid", accessCode.accessCode().toString()));
        }

        if (new Date(System.currentTimeMillis()).after(user.getExpiringDate())) {
            response.getOutputStream().write(
                    new ObjectMapper()
                            .writeValueAsString(new UserExpiredResponse("User expired")).getBytes()
            );

            response.setStatus(401);
            user.deactivateAccount();
            userService.saveUser(user);
            return;
        }

        Token token = tokenService.sign(accessCode);

        response.getOutputStream().write(
                new ObjectMapper()
                        .writeValueAsString(new TokenResponse(token)).getBytes()
        );
    }
}
