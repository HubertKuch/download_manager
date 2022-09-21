package com.hubert.downloader.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String username;
    private String email;
    private String passwordHash;
    private Float transfer;
}
