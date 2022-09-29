package com.hubert.downloader.configuration;

import com.hubert.downloader.auth.AuthFilter;
import com.hubert.downloader.auth.CorsFilter;
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
        authFilter.setOrder(2);

        return authFilter;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterFilterRegistrationBean() {
        FilterRegistrationBean<CorsFilter> corsFilter = new FilterRegistrationBean<>();

        corsFilter.setFilter(new CorsFilter());
        corsFilter.setOrder(1);

        return corsFilter;
    }
}
