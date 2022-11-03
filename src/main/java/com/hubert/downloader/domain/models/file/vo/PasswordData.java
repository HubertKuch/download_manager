package com.hubert.downloader.domain.models.file.vo;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

@AllArgsConstructor
@Data
@Getter
@Setter
@NoArgsConstructor
public class PasswordData {
    @Field(write = Field.Write.ALWAYS)
    Boolean hasPassword;
    @Field(write = Field.Write.ALWAYS)
    String folderPassword;
    @Field(write = Field.Write.ALWAYS)
    String hamsterPassword;

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
