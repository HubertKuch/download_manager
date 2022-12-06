package com.hubert.downloader.utils;

import com.hubert.downloader.domain.exceptions.FolderRequiresPasswordException;
import com.hubert.downloader.domain.models.file.File;
import com.hubert.downloader.domain.models.file.Folder;
import com.hubert.downloader.domain.models.file.vo.PasswordData;
import com.hubert.downloader.external.coreapplication.models.AccountsListItem;
import com.hubert.downloader.external.coreapplication.requestsgson.async.PasswordRequiredException;
import com.hubert.downloader.external.pl.kubikon.chomikmanager.api.AndroidApi;

public final class HamsterPassword   {

    public static void provideUserPassword(String accountId, PasswordData passwordData) throws FolderRequiresPasswordException {
        try {
            System.out.println();
            System.out.println(accountId);
            System.out.println(passwordData);
            System.out.println();
            AndroidApi.postPassword(accountId, "0", passwordData.getHamsterPassword());
        } catch (Exception | PasswordRequiredException e) {
            System.out.println(e);
            throw new FolderRequiresPasswordException("");
        }
    }

    public static void provideFolderPassword(String accountId, String folderId, PasswordData passwordData) throws FolderRequiresPasswordException {
        try {
            System.out.println();
            System.out.println(accountId);
            System.out.println(folderId);
            System.out.println(passwordData);
            System.out.println();
            AndroidApi.postPassword(accountId, folderId, passwordData.getFolderPassword());
        } catch (Exception | PasswordRequiredException e) {
            System.out.println(e);
            throw new FolderRequiresPasswordException("");
        }
    }

    public static void operateOnFile(File file, Folder folder, AccountsListItem accountsListItem) throws FolderRequiresPasswordException {
        operateOnFile(file.getPasswordData(), folder.id(), accountsListItem.getAccountId());
    }

    public static void operateOnFile(PasswordData passwordData, String folderId, String accountId) throws FolderRequiresPasswordException {
        if (passwordData.getHasPassword()) {
            if (passwordData.getHamsterPassword() != null) {
                provideUserPassword(accountId, passwordData);
            }

            if (passwordData.getFolderPassword() != null) {
                provideFolderPassword(accountId, folderId, passwordData);
            }
        }
    }
}
