package com.hubert.downloader.configuration;

import com.hubert.downloader.external.coreapplication.requestsgson.async.PasswordRequiredException;
import com.hubert.downloader.external.pl.kubikon.chomikmanager.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class LoginConfiguration {

    @Value("${android.hamster.credentials.username}")
    private String username;
    @Value("${android.hamster.credentials.password}")
    private String password;

    @PostConstruct
    public void login() throws
            CopyingForbiddenException,
            ReloginRequiredException,
            TryAgainException,
            TooFastRequestsException,
            Exception,
            PasswordRequiredException
    {
        AndroidApi.login(username, password);
        WebApi.login(username, password);
        WebApi.getRequestVerificationToken();
    }
}
