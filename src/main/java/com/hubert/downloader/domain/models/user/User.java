package com.hubert.downloader.domain.models.user;

import com.hubert.downloader.domain.InformationUnit;
import com.hubert.downloader.domain.Transfer;
import com.hubert.downloader.domain.models.file.File;
import com.hubert.downloader.domain.models.user.dto.NewUserDTO;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Document
public class User {
    @MongoId
    private ObjectId id;
    @Field
    private String accessCode;
    @Field
    private Transfer transfer;
    @Field
    private List<File> files;

    public User(Transfer transfer) {
        this.transfer = transfer;
        this.accessCode = UUID.randomUUID().toString();
    }

    public User(String accessCode, Transfer transfer, List<File> files) {
        this.transfer= transfer;
        this.accessCode = accessCode;
        this.files = files == null ? new ArrayList<>() : files;
    }

    public static User fromDTO(NewUserDTO userDTO) {
        User user = new User(
                userDTO.transfer()
        );

        user.setTransfer(userDTO.transfer());
        user.setAccessCode(UUID.randomUUID().toString());
        user.setFiles(List.of());

        return user;
    }

    public Float compareFileSizeWithUserTransfer(final File file) {
        return transfer.getTransfer().parseTo(InformationUnit.BYTE).size() - file.getSize().parseTo(InformationUnit.BYTE).size();
    }

    public void addFile(File file) {
        this.files.add(file);
    }

    public boolean isUserCanDownloadAFile(final File file) {
        return compareFileSizeWithUserTransfer(file) > 0;
    }
}
