package com.hubert.downloader.models;

import com.hubert.downloader.domain.Transfer;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Getter
@Setter
@Document
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @MongoId
    private String id;
    @Field
    private String username;
    @Field
    private String email;
    @Field
    private String passwordHash;
    @Field
    private Transfer transfer;
    @Field
    private List<File> files = List.of();

    public Float compareFileSizeWithUserTransfer(final File file) {
        return transfer.getTransfer().size() - file.getSize().size();
    }

    public boolean isUserCanDownloadAFile(final File file) {
        return compareFileSizeWithUserTransfer(file) > 0;
    }
}
