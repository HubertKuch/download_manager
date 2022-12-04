package com.hubert.downloader.utils;

import com.hubert.downloader.domain.InformationSize;
import com.hubert.downloader.domain.InformationUnit;
import com.hubert.downloader.domain.models.file.File;
import com.hubert.downloader.domain.models.file.Folder;
import com.hubert.downloader.domain.models.file.vo.PasswordData;
import com.hubert.downloader.external.coreapplication.models.AccountsListItem;
import com.hubert.downloader.external.pl.kubikon.chomikmanager.api.AndroidApi;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HamsterPasswordTest {

    private Folder mockedFolder;
    private File mockedFile;
    private AccountsListItem account;

    @BeforeEach
    public void setUp() throws Exception {
        this.mockedFile = new File(UUID.randomUUID(),
                                   5944665607L,
                                   "Warhammer.40000.Dawn.of.War.III.v.4.0.0.16278.Crack.by.BALDMAN.and.Voksi.rar",
                                   "",
                                   null,
                                   new InformationSize(InformationUnit.BYTE, 231L),
                                   PasswordData.withPassword("123", "szpital"));

        this.mockedFolder = new Folder("522",

                                       "https://chomikuj.pl/maxpayner_priv/*e2*9d*97+*f0*9f*92*8e+FILMOTEKA+SPORTOWE*f0*9f*92*8e*e2*9d*97/Babilon+MMA+16+(25.09.2020)+PL",
                                       "maxpayner_priv",
                                       "Babilon MMA 16 (25.09.2020) PL",
                                       List.of(mockedFile),
                                       new Date(System.currentTimeMillis()),
                                       PasswordData.withPassword("123", "szpital"));

        this.account = AndroidApi.searchForAccount(this.mockedFolder.account());
    }

    @AfterEach
    public void teardown() {
        this.mockedFolder = null;
        this.mockedFile = null;
        this.account = null;
    }

    @Test
    void provideUserPassword() {
        assertDoesNotThrow(() -> HamsterPassword.provideUserPassword(this.account.getAccountId(), this.mockedFolder.passwordData()));
    }

    @Test
    void provideFolderPassword() {
        assertDoesNotThrow(() -> HamsterPassword.provideFolderPassword(
                this.account.getAccountId(),
                this.mockedFolder.id(),
                this.mockedFolder.passwordData()
        ));
    }

    @Test
    void operateOnFile() {
        assertDoesNotThrow(() -> HamsterPassword.operateOnFile(this.mockedFile, this.mockedFolder, this.account));
    }
}