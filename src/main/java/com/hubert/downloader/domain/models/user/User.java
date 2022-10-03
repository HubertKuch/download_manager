package com.hubert.downloader.domain.models.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hubert.downloader.domain.InformationUnit;
import com.hubert.downloader.domain.Transfer;
import com.hubert.downloader.domain.models.file.File;
import com.hubert.downloader.domain.models.file.dto.FileWithoutPath;
import com.hubert.downloader.domain.models.user.dto.NewUserDTO;
import com.hubert.downloader.domain.models.user.dto.UserWithoutPathInFilesDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;
    @Field
    private String accessCode;
    @Field
    private Transfer transfer;
    @Field
    private List<File> files;
    @Field
    private UserRole role;
    @Field
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date expiringDate;
    @Field
    private Boolean hasActiveAccount;

    public User(Transfer transfer, UserRole role, Date expiringDate) {
        this.transfer = transfer;
        this.role = role;
        this.expiringDate = expiringDate;
        this.accessCode = UUID.randomUUID().toString();
    }

    public User(String accessCode, Transfer transfer, List<File> files, UserRole role, Date expiringDate) {
        this.transfer= transfer;
        this.accessCode = accessCode;
        this.files = files == null ? new ArrayList<>() : files;
        this.role = role;
        this.expiringDate = expiringDate;
    }

    public static User fromDTO(NewUserDTO userDTO) {
        User user = new User(
                userDTO.transfer(),
                userDTO.role(),
                userDTO.expiringDate()
        );

        user.setTransfer(userDTO.transfer());
        user.setAccessCode(UUID.randomUUID().toString());
        user.setFiles(List.of());

        return user;
    }

    public Long compareFileSizeWithUserTransfer(final File file) {
        return transfer.getTransfer().parseTo(InformationUnit.BYTE).size() - file.getSize().parseTo(InformationUnit.BYTE).size();
    }

    public void addFile(File file) {
        this.files.add(file);
    }

    public boolean isUserCanDownloadAFile(final File file) {
        return compareFileSizeWithUserTransfer(file) > 0;
    }

    public void deactivateAccount() {
        this.hasActiveAccount = false;
    }

    public UserWithoutPathInFilesDTO parseToDto() {
        List<FileWithoutPath> parsedFiles = this.files.stream().map(file -> new FileWithoutPath(file.getId(), file.getName(), file.getExtension(), file.getSize())).toList();

        return new UserWithoutPathInFilesDTO(
                id,
                accessCode,
                transfer,
                parsedFiles,
                role,
                expiringDate,
                hasActiveAccount
        );
    }
}
