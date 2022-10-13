package com.hubert.downloader.domain.models.file.vo;

public record PasswordData(
        Boolean hasPassword,
        String folderPassword,
        String hamsterPassword
) {
    public static PasswordData withoutPassword() {
        return new PasswordData(false, null, null);
    }

    public static PasswordData withFolderPassword(String folderPassword) {
        return new PasswordData(true, folderPassword, null);
    }

    public static PasswordData withPassword(String folderPassword, String hamsterPassword) {
        return new PasswordData(true, folderPassword, hamsterPassword);
    }
}
