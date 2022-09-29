package com.hubert.downloader.domain.models.tokens;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RequestWithTokenDTO(
        Token token
) {}
