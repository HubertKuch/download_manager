package com.hubert.downloader.services;

import com.hubert.downloader.models.User;
import com.hubert.downloader.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public void saveUser(User user) {
        repository.save(user);
    }

    public List<User> getUsers() {
        return repository.findAll();
    }
}
