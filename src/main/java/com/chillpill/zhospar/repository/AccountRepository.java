package com.chillpill.zhospar.repository;

import com.chillpill.zhospar.repository.dto.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountByUsername(String username);
}
