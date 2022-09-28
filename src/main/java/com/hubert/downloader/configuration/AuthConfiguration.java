package com.hubert.downloader.configuration;

import com.hubert.downloader.auth.AuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfiguration {

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter() {
        FilterRegistrationBean<AuthFilter> authFilter = new FilterRegistrationBean<>();

        authFilter.setFilter(new AuthFilter());
        authFilter.addUrlPatterns("/api/v1/files/*");

        return authFilter;
    }
}
