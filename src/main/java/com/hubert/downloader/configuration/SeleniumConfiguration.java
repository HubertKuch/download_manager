package com.hubert.downloader.configuration;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class SeleniumConfiguration {

    @PostConstruct
    public void init() {
        System.setProperty("webdriver.chrome.driver", "/var/www/chromedriver");
    }

    @Bean
    public ChromeDriver chromeDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();

        chromeOptions.addArguments("--headless");

        return new ChromeDriver(chromeOptions);
    }
}
