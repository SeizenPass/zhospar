package com.chillpill.zhospar.service;

import com.chillpill.zhospar.repository.InviteRepository;
import com.chillpill.zhospar.repository.dto.Invite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InviteService {
    private final InviteRepository inviteRepository;

    @Autowired
    public InviteService(InviteRepository inviteRepository) {
        this.inviteRepository = inviteRepository;
    }

    public Invite saveInvite(Invite invite) {
        return inviteRepository.save(invite);
    }
}
