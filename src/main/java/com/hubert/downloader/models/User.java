package com.hubert.downloader.models;

import com.hubert.downloader.domain.InformationUnit;
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
        System.out.println(transfer.getTransfer().parseTo(InformationUnit.BYTE));
        System.out.println(file.getSize().parseTo(InformationUnit.BYTE));
        return transfer.getTransfer().parseTo(InformationUnit.BYTE).size() - file.getSize().parseTo(InformationUnit.BYTE).size();
    }

    public boolean isUserCanDownloadAFile(final File file) {
        return compareFileSizeWithUserTransfer(file) > 0;
    }
}
