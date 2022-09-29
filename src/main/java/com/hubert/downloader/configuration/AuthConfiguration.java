package com.hubert.downloader.configuration;

import com.hubert.downloader.auth.AuthFilter;
import com.hubert.downloader.services.TokenService;
import com.hubert.downloader.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "auth")
public class AuthConfiguration {

    private String secret;
    private final TokenService tokenService;
    private final UserService userService;

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter() {
        FilterRegistrationBean<AuthFilter> authFilter = new FilterRegistrationBean<>();

        authFilter.setFilter(new AuthFilter(tokenService, userService));
        authFilter.addUrlPatterns("/api/v1/files/*");

        return authFilter;
    }
}
