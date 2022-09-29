package com.hubert.downloader.services;

import com.hubert.downloader.domain.InformationSize;
import com.hubert.downloader.domain.InformationUnit;
import com.hubert.downloader.domain.Transfer;
import com.hubert.downloader.domain.models.tokens.Token;
import com.hubert.downloader.domain.models.user.AccessCodeDTO;
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final TokenService tokenService;

    public User saveUser(User user) {
        Transfer transferInBytes = new Transfer(
                new InformationSize(InformationUnit.BYTE, user.getTransfer().getTransfer().parseTo(InformationUnit.BYTE).size()),
                new InformationSize(InformationUnit.BYTE, user.getTransfer().getStartTransfer().parseTo(InformationUnit.BYTE).size())
        );
        user.setTransfer(transferInBytes);

        return repository.save(user);
    }

    public List<User> getUsers() {
        return repository.findAll();
    }

    public User findByAccessCode(AccessCodeDTO accessCodeDTO) {
        return repository.findFirstByAccessCode(accessCodeDTO.accessCode().toString());
    }

    public User findByToken(Token token) {
        AccessCodeDTO accessCodeDTO = tokenService.decode(token);
        return findByAccessCode(accessCodeDTO);
    }
}
