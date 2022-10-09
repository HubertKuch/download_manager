package com.hubert.downloader.utils;

import com.hubert.downloader.domain.models.user.HamsterUser;
import com.hubert.downloader.external.coreapplication.models.AccountsListItem;
import com.hubert.downloader.external.pl.kubikon.chomikmanager.api.AndroidApi;

import java.util.Optional;

public class HamsterUtils {

    public static Optional<HamsterUser> getAccountByName(String name) {
        try {
            AccountsListItem accountsListItem = AndroidApi.searchForAccount(name);

            if (accountsListItem.getAccountName() == null) {
                return Optional.empty();
            }

            return Optional.of(new HamsterUser(accountsListItem.getAccountId(), name));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
