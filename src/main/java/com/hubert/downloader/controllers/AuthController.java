package com.hubert.downloader.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubert.downloader.domain.models.tokens.Token;
import com.hubert.downloader.domain.models.tokens.TokenResponse;
import com.hubert.downloader.domain.models.user.AccessCodeDTO;
import com.hubert.downloader.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final TokenService tokenService;

    @PostMapping("/login/")
    public void login(@RequestBody AccessCodeDTO accessCode, HttpServletResponse response) throws IOException {
        Token token = tokenService.sign(accessCode);

        response.getOutputStream().write(
                new ObjectMapper()
                        .writeValueAsString(new TokenResponse(token)).getBytes()
        );

        response.setHeader("Content-Type", "application/json");
    }
}
