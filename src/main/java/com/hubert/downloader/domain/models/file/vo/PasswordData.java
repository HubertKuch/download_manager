package com.hubert.downloader.domain.models.file.vo;

public record PasswordData(
        Boolean hasPassword,
        String password
) {
    public static PasswordData withoutPassword() {
        return new PasswordData(false, null);
    }

    public static PasswordData withPassword(String password) {
        return new PasswordData(true, password);
    }
}
