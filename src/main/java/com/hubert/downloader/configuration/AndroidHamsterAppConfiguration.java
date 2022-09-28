package com.hubert.downloader.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "android.hamster.credentials")
public class AndroidHamsterAppConfiguration {
    private String username;
    private String password;
}
