package com.hubert.downloader.utils;

import com.hubert.downloader.domain.exceptions.HamsterFolderLinkIsInvalid;
import com.hubert.downloader.domain.models.file.Folder;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

@Getter
@Setter
public class HamsterFolderPage {

    private String url;
    private Document document;
    private final String INVALID_LINK_EXCEPTION_FORMAT = "`%s` link doesnt contains folder";

    private HamsterFolderPage(String url) {
        this.url = url;
    }

    public static HamsterFolderPage from(String url) throws HamsterFolderLinkIsInvalid {
        HamsterFolderPage hamsterFolderPage = new HamsterFolderPage(url);


        try {
            Connection connection = Jsoup.connect(url);
            hamsterFolderPage.document = connection.get();
        } catch (IOException|IllegalArgumentException e) {
            throw new HamsterFolderLinkIsInvalid(String.format(hamsterFolderPage.INVALID_LINK_EXCEPTION_FORMAT, url));
        }

        return hamsterFolderPage;
    }

    public String getFolderId() throws HamsterFolderLinkIsInvalid {
        Elements fileListInputs = this.document.select("#FileListForm input[name=\"folderId\"]");

        if (fileListInputs.isEmpty()) {
            throw new HamsterFolderLinkIsInvalid(String.format(INVALID_LINK_EXCEPTION_FORMAT, url));
        }

        return fileListInputs.val();
    }

    public String getAccountName() throws HamsterFolderLinkIsInvalid {
        Elements accountNameElement = this.document.select(".chomikName");

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

    public Folder getFolder() throws HamsterFolderLinkIsInvalid {
        return new Folder(
                getFolderId(),
                url,
                getAccountName(),
                getFolderName(),
                new ArrayList<>(),
                new Timestamp(System.currentTimeMillis())
        );
    }
}
