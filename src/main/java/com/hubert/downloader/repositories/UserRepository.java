package com.hubert.downloader.repositories;

import com.hubert.downloader.models.User;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface UserRepository extends Repository<User, String> {
    User save(User user);
    List<User> findAll();
}
