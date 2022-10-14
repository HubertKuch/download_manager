package com.hubert.downloader.utils;

import com.hubert.downloader.domain.exceptions.FolderRequiresPasswordException;
import com.hubert.downloader.domain.exceptions.HamsterFolderLinkIsInvalid;
import com.hubert.downloader.domain.exceptions.InvalidPasswordDataException;
import com.hubert.downloader.domain.models.file.Folder;
import com.hubert.downloader.domain.models.file.IncomingDataRequestWithPassword;
import com.hubert.downloader.domain.models.file.vo.PasswordData;
import com.hubert.downloader.domain.models.user.HamsterUser;
import com.hubert.downloader.external.coreapplication.requestsgson.async.PasswordRequiredException;
import com.hubert.downloader.external.pl.kubikon.chomikmanager.api.*;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;

import org.openqa.selenium.NoSuchElementException;

@Getter
@Setter
public class HamsterFolderPage {

    private String url;
    private Document document;
    private final String INVALID_LINK_EXCEPTION_FORMAT = "`%s` link doesnt contains folder";
    private final String BASE_HAMSTER_PAGE = "https://chomikuj.pl/";
    private final WebDriver webDriver;

    public HamsterFolderPage(
            IncomingDataRequestWithPassword incomingDataRequestWithPassword,
            WebDriver driver
    ) throws Exception {
        this.url = incomingDataRequestWithPassword.getUrl();
        this.webDriver = driver;
        webDriver.get(url);

        try {
            Optional<HamsterUser> optionalHamsterUser = HamsterUtils.getAccountByName(getAccountName());

            if (optionalHamsterUser.isEmpty()) {
                throwInvalidLink();
            }

            HamsterUser hamsterUser = optionalHamsterUser.get();

            if (isSecureHamsterPage()) {
                provideHamsterPassword(incomingDataRequestWithPassword);
            }

            if (isSecureHamsterPage()) {
                throw new InvalidPasswordDataException();
            }

            if (isSecuredFolderPage()) {
                provideFolderPassword(incomingDataRequestWithPassword.getPasswordData(), hamsterUser);
            }

        } catch (CopyingForbiddenException | ReloginRequiredException | TryAgainException | TooFastRequestsException | PasswordRequiredException e) {
            throw new RuntimeException(e);
        }
    }

    private void provideHamsterPassword(IncomingDataRequestWithPassword incomingDataRequestWithPassword) throws
            Exception,
            CopyingForbiddenException,
            ReloginRequiredException,
            TryAgainException,
            TooFastRequestsException,
            PasswordRequiredException {

        WebApi.postUserPassword(getAccountName(), incomingDataRequestWithPassword.getPasswordData().hamsterPassword());
        AndroidApi.postUserPassword(getAccountName(), incomingDataRequestWithPassword.getPasswordData().hamsterPassword());

        provideHamsterPassword(incomingDataRequestWithPassword.getPasswordData());
        submitHamsterLoginPage();
    }

    private void provideFolderPassword(PasswordData passwordData, HamsterUser hamsterUser) throws InvalidPasswordDataException {
        try {
            AndroidApi.postFolderPassword(hamsterUser.accountId(), getFolderId(), passwordData.folderPassword());
            WebApi.postFolderPassword(hamsterUser.accountId(), getFolderId(), getFolderName(), passwordData.folderPassword());
        } catch (Exception | CopyingForbiddenException | ReloginRequiredException | TryAgainException | TooFastRequestsException ignored) {
            throw new InvalidPasswordDataException();
        } catch (PasswordRequiredException e) {
            throw new RuntimeException(e);
        }
    }

    private void provideHamsterPassword(PasswordData passwordData) {
        webDriver.findElement(By.cssSelector("#Password")).sendKeys(passwordData.hamsterPassword());
    }

    private void submitHamsterLoginPage() {
        webDriver.findElement(By.cssSelector(".loginForm form")).submit();
    }

    private Boolean isSecureHamsterPage() {
        try {
            webDriver.findElement(By.cssSelector("#userMustLogin"));
            return true;
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }

    private Boolean isSecuredFolderPage() {
        try {
            webDriver.findElement(By.cssSelector(".LoginToFolderForm"));
            return true;
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }

    private String getHamsterId() throws HamsterFolderLinkIsInvalid {
        try {
            String accountName = webDriver.findElement(By.cssSelector("[name=__accname]")).getAttribute("value");

            return AndroidApi.searchForAccount(accountName).getAccountId();
        } catch (NoSuchElementException ignore) {
            throwInvalidLink();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "";
    }

    private void throwInvalidLink() throws HamsterFolderLinkIsInvalid {
        throw new HamsterFolderLinkIsInvalid(String.format(INVALID_LINK_EXCEPTION_FORMAT, url));
    }

    public String getFolderId() throws HamsterFolderLinkIsInvalid {
        try {
            WebElement folderId = webDriver.findElement(By.cssSelector("#TreeForm input[name=FolderId]"));

            return folderId.getAttribute("value");
        } catch (NoSuchElementException ignored) {
            throwInvalidLink();
        }

        return "";
    }

    public String getAccountName() throws HamsterFolderLinkIsInvalid {
        try {
            return webDriver.findElement(By.cssSelector(".chomikName")).getText().trim();
        } catch (NoSuchElementException e) {
            throwInvalidLink();
        }

        return "";
    }

    public String getFolderName() throws HamsterFolderLinkIsInvalid {
        try {
            WebElement folderNameElement = webDriver.findElement(By.cssSelector("[name=\"_metaPageTitle\"]"));

            return folderNameElement.getAttribute("value");
        } catch (NoSuchElementException ignored) {
            throw new HamsterFolderLinkIsInvalid(String.format(INVALID_LINK_EXCEPTION_FORMAT, url));
        }
    }

    public Folder getFolder() throws HamsterFolderLinkIsInvalid, FolderRequiresPasswordException {
        return new Folder(
                getFolderId(),
                url,
                getAccountName(),
                getFolderName(),
                new ArrayList<>(),
                new Timestamp(System.currentTimeMillis())
                //PasswordData.withoutPassword()
        );
    }
}
