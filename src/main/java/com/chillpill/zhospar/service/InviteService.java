package com.chillpill.zhospar.service;

import com.chillpill.zhospar.repository.AccountRepository;
import com.chillpill.zhospar.repository.InviteRepository;
import com.chillpill.zhospar.repository.dto.Account;
import com.chillpill.zhospar.repository.dto.Invite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InviteService {
    private final InviteRepository inviteRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public InviteService(InviteRepository inviteRepository, AccountRepository accountRepository) {
        this.inviteRepository = inviteRepository;
        this.accountRepository = accountRepository;
    }

    public Invite saveInvite(Invite invite) {
        return inviteRepository.save(invite);
    }

    public List<Invite> getInvitesByInvitedAccountId(long id) {
        Account account = accountRepository.getOne(id);
        return inviteRepository.findAllByInvitedAccountAndExpired(account, false);
    }

    public Invite getInviteById(long id) {
        return inviteRepository.getOne(id);
    }
}
