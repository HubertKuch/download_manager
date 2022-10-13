package com.hubert.downloader.builders;

import com.hubert.downloader.domain.models.file.IncomingDataRequestWithPassword;
import com.hubert.downloader.external.coreapplication.requestsgson.async.PasswordRequiredException;
import com.hubert.downloader.utils.HamsterFolderPage;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HamsterPageBuilder {

    private final WebDriver driver;

    public HamsterFolderPage buildPage(
            IncomingDataRequestWithPassword incomingDataRequestWithPassword
    ) throws PasswordRequiredException, Exception {
        return new HamsterFolderPage(incomingDataRequestWithPassword, driver);
    }
}
