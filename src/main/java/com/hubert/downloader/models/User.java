package com.hubert.downloader.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

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
}
