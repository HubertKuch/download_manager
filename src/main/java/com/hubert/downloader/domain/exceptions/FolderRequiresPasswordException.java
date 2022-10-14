package com.hubert.downloader.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class FolderRequiresPasswordException extends Exception {

    public FolderRequiresPasswordException(String message) {
        super(message);
    }
}
