package com.hubert.downloader.utils;

import com.hubert.downloader.domain.exceptions.FolderRequiresPasswordException;
import com.hubert.downloader.domain.exceptions.HamsterFolderLinkIsInvalid;
import com.hubert.downloader.domain.models.file.Folder;
import com.hubert.downloader.domain.models.file.IncomingDataRequestWithPassword;
import com.hubert.downloader.domain.models.file.vo.PasswordData;
import com.hubert.downloader.domain.models.user.HamsterUser;
import com.hubert.downloader.external.coreapplication.modelsgson.ApiError;
import com.hubert.downloader.external.coreapplication.requestsgson.async.PasswordRequiredException;
import com.hubert.downloader.external.pl.kubikon.chomikmanager.api.AndroidApi;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;

@Getter
@Setter
public class HamsterFolderPage {

    private String url;
    private Document document;
    private final String INVALID_LINK_EXCEPTION_FORMAT = "`%s` link doesnt contains folder";

    private String getPageContent(String appendUrl) {
        try {
            URL url = new URL(appendUrl);

            url.openConnection().connect();
            System.out.println(url.getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "";
    }

    public HamsterFolderPage(IncomingDataRequestWithPassword incomingDataRequestWithPassword) throws Exception, PasswordRequiredException {
        this.url = incomingDataRequestWithPassword.getUrl();

        getPageContent(url);

//        System.out.println(Jsoup.connect(url).execute().body());

        Connection connection = Jsoup
                .connect(url);

        try {
            this.document = connection
                    .userAgent("Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; fr) Presto/2.9.168 Version/11.52")
                    .get();

            System.out.println(incomingDataRequestWithPassword);

            if (incomingDataRequestWithPassword.getPasswordData().hasPassword()) {
                System.out.println("test");
                Optional<HamsterUser> optionalHamsterUser = HamsterUtils
                        .getAccountByName(getAccountNameFromPasswordPage());

                if (optionalHamsterUser.isEmpty()) {
                    throwInvalidLink();
                }

                HamsterUser hamsterUser = optionalHamsterUser.get();

                ApiError apiError = AndroidApi.postFolderPassword(
                        hamsterUser.accountId(),
                        "",
                        incomingDataRequestWithPassword.getPasswordData().password()
                );

                System.out.println(apiError.message);
                System.out.println(apiError.argExtra);
                System.out.println(apiError.code);
            }

        } catch (IllegalArgumentException e) {
            throwInvalidLink();
        }
    }

    private void throwInvalidLink() throws HamsterFolderLinkIsInvalid {
        throw new HamsterFolderLinkIsInvalid(String.format(INVALID_LINK_EXCEPTION_FORMAT, url));
    }

    public String getFolderId() throws HamsterFolderLinkIsInvalid, FolderRequiresPasswordException {
        Elements fileListInputs = this.document.select("#FileListForm input[name=\"folderId\"]");

        processRequiringPasswordIfNeeded();

        if (fileListInputs.isEmpty()) {
            throw new HamsterFolderLinkIsInvalid(String.format(INVALID_LINK_EXCEPTION_FORMAT, url));
        }

        return fileListInputs.val();
    }

    public String getAccountName() throws HamsterFolderLinkIsInvalid, FolderRequiresPasswordException {
        Elements accountNameElement = this.document.select(".chomikName");

        processRequiringPasswordIfNeeded();

        if (accountNameElement.isEmpty()) {
            throw new HamsterFolderLinkIsInvalid(String.format(INVALID_LINK_EXCEPTION_FORMAT, url));
        }

        return accountNameElement.text();
    }

    public String getFolderName() throws HamsterFolderLinkIsInvalid {
        Elements folderNameElements = this.document.select("[name=\"_metaPageTitle\"]");

        if (folderNameElements.isEmpty()) {
            throw new HamsterFolderLinkIsInvalid(String.format(INVALID_LINK_EXCEPTION_FORMAT, url));
        }

        return folderNameElements.val();
    }

    public Folder getFolder() throws HamsterFolderLinkIsInvalid, FolderRequiresPasswordException {
        return new Folder(
                getFolderId(),
                url,
                getAccountName(),
                getFolderName(),
                new ArrayList<>(),
                new Timestamp(System.currentTimeMillis()),
                PasswordData.withoutPassword()
        );
    }

    private Boolean folderRequiringPassword() {
        return !this.document.select("#userMustLogin").isEmpty();
    }

    private void processRequiringPasswordIfNeeded() throws FolderRequiresPasswordException {
        if (folderRequiringPassword()) {
            throw new FolderRequiresPasswordException("This folder requires password.");
        }
    }

    private String getAccountNameFromPasswordPage() {
        Elements nameElement = this.document.select(".chomikName h2");

        if (nameElement.isEmpty()) {
            return "";
        }

        return nameElement.text();
    }
}
