package com.hubert.downloader.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserCantDownloadFile extends Exception {

    public UserCantDownloadFile(String message) {
        super(message);
    }
}
