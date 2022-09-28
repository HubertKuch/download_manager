package com.hubert.downloader.repositories;

import com.hubert.downloader.domain.models.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {}
