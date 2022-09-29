package com.hubert.downloader.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubert.downloader.domain.models.tokens.Token;
import com.hubert.downloader.domain.models.user.AccessCodeDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Service
public class TokenService {
    @Value("${auth.secret}")
    private String secret;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    public Token sign(AccessCodeDTO accessCode) {
        final Algorithm algorithm = getAlgorithm();

        return new Token(JWT.create()
                .withPayload(Map.of("accessCode", accessCode.accessCode().toString()))
                .sign(algorithm));
    }

    public AccessCodeDTO decode(Token token) {
        final Algorithm algorithm = getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token.value());

        try {
            return new ObjectMapper().readValue(Base64.getUrlDecoder().decode(jwt.getPayload()), AccessCodeDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
