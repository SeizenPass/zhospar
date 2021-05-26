package com.chillpill.zhospar.repository;

import com.chillpill.zhospar.repository.dto.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteRepository extends JpaRepository<Invite, Long> {
}
