package com.hubert.downloader.domain.models.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hubert.downloader.domain.InformationSize;
import com.hubert.downloader.domain.InformationUnit;
import com.hubert.downloader.domain.Transfer;
import com.hubert.downloader.domain.exceptions.InvalidRequestDataException;
import com.hubert.downloader.domain.models.file.File;
import com.hubert.downloader.domain.models.file.Folder;
import com.hubert.downloader.domain.models.file.dto.FolderWithFilesWithoutPaths;
import com.hubert.downloader.domain.models.history.History;
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
import java.util.stream.Collectors;

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
    private List<Folder> folders;
    @Field
    private UserRole role;
    @Field
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expiringDate;
    @Field
    private Boolean hasActiveAccount;
    @Field(name = "histories", write = Field.Write.ALWAYS)
    private List<History> histories;

    public User(Transfer transfer, UserRole role, Date expiringDate) {
        this.transfer = transfer;
        this.role = role;
        this.expiringDate = expiringDate;
        this.accessCode = UUID.randomUUID().toString();
        this.histories = new ArrayList<>();
    }

    public User(String accessCode, Transfer transfer, List<Folder> folders, UserRole role, Date expiringDate) {
        this.transfer = transfer;
        this.accessCode = accessCode;
        this.folders = folders == null ? new ArrayList<>() : folders;
        this.role = role;
        this.expiringDate = expiringDate;
        this.histories = new ArrayList<>();
    }

    public static User fromDTO(NewUserDTO userDTO) {
        User user = new User(
                userDTO.transfer(),
                userDTO.role(),
                userDTO.expiringDate()
        );

        user.setTransfer(userDTO.transfer());
        user.setAccessCode(UUID.randomUUID().toString());
        user.setFolders(List.of());

        return user;
    }

    public Long compareFileSizeWithUserTransfer(final File file) {
        return transfer.getTransfer().parseTo(InformationUnit.BYTE).size() - file.getSize().parseTo(InformationUnit.BYTE).size();
    }

    public void addFile(Folder folder, File file) {
        if (folders == null) folders = List.of();

        List<Folder> matchedFolders = this.folders.stream()
                .filter(userFolder -> userFolder.name().equals(folder.name()))
                .toList();

        if (matchedFolders.isEmpty()) {
            this.folders.add(folder);

            addFile(folder, file);

            return;
        }

        matchedFolders.get(0).files().add(file);
    }

    public boolean isUserCanDownloadAFile(final File file) {
        return compareFileSizeWithUserTransfer(file) > 0;
    }

    public boolean isUserCanDownloadAFolder(final Folder folder) {
        long diff = folder.files().stream()
                .map(this::compareFileSizeWithUserTransfer)
                .reduce(0L, Long::sum);

        return diff > 0;
    }

    public void deactivateAccount() {
        this.hasActiveAccount = false;
    }

    public UserWithoutPathInFilesDTO parseToDto() {
        if (folders == null) folders = List.of();

        List<FolderWithFilesWithoutPaths> parsedFolders = this
                .folders
                .stream()
                .map(Folder::parseExcludingPaths)
                .toList();

        return new UserWithoutPathInFilesDTO(
                id,
                accessCode,
                transfer,
                List.of(
                        new Transfer(
                                new InformationSize(InformationUnit.GIGA_BYTE, transfer.getTransfer().parseTo(InformationUnit.GIGA_BYTE).size()),
                                new InformationSize(InformationUnit.GIGA_BYTE, transfer.getStartTransfer().parseTo(InformationUnit.GIGA_BYTE).size())
                        )
                ),
                parsedFolders,
                role,
                expiringDate,
                hasActiveAccount,
                histories
        );
    }

    public void addHistoryOfDownloadedFiles(List<File> downloadedFiles) {
        if (this.histories == null) this.histories = new ArrayList<>();

        this.histories.add(History.ofDownloadedFiles(downloadedFiles));
    }

    public void addHistory(History history) {
        if (this.histories == null) this.histories = new ArrayList<>();

        this.histories.add(history);
    }

    public void removeFolder(final Folder folderToDelete) {
        this.folders = folders.stream()
                .filter(folder -> !folder.id().equals(folderToDelete.id()))
                .collect(Collectors.toList());
    }

    public void removeFile(final Folder folder, final File file) throws InvalidRequestDataException {
        boolean isFolderHasThatFile = folder.files().stream().anyMatch(matchingFile -> matchingFile.getId().equals(file.getId()));

        if (!isFolderHasThatFile) {
            throw new InvalidRequestDataException("Invalid file or folder id");
        }

        List<File> mappedFiles = folder.files().stream()
                .filter(checkingFile -> !checkingFile.getId().equals(file.getId()))
                .toList();

        Folder newFolder = new Folder(
                folder.id(),
                folder.url(),
                folder.account(),
                folder.name(),
                mappedFiles,
                folder.addedAt()
        );

        this.folders = folders
                .stream()
                .map(userFolder -> userFolder.id().equals(folder.id()) ? newFolder : userFolder)
                .collect(Collectors.toList());
    }
}
