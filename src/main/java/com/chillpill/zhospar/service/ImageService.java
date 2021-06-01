package com.chillpill.zhospar.service;

import com.chillpill.zhospar.repository.AccountRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class ImageService {
    private final AccountRepository accountRepository;

    public ImageService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public byte[] getImageById(long id) {
        UUID uuid = accountRepository.getOne(id).getAvatarId();
        File file = new File("filesystem/"+uuid.toString());
        byte[] content = {};
        try {
            content = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
