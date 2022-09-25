package com.hubert.downloader;

import com.hubert.downloader.external.coreapplication.requestsgson.async.PasswordRequiredException;
import com.hubert.downloader.external.pl.kubikon.chomikmanager.api.AndroidApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DownloaderApplication {

    public static void main(String[] args) throws PasswordRequiredException, Exception {
        SpringApplication.run(DownloaderApplication.class, args);

        AndroidApi.login("allegro.lokalnie0251", "NieZmieniacH4sla");
    }
}
