package com.chillpill.zhospar.repository;

import com.chillpill.zhospar.repository.dto.Account;
import com.chillpill.zhospar.repository.dto.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InviteRepository extends JpaRepository<Invite, Long> {
    List<Invite> findAllByInvitedAccountAndExpired(Account account, boolean expired);
}
