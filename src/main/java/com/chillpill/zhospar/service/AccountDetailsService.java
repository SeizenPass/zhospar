package com.chillpill.zhospar.service;

import com.chillpill.zhospar.repository.AccountRepository;
import com.chillpill.zhospar.repository.dto.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByUsername(s);
        if (account == null) {
            throw new UsernameNotFoundException("Account with such username doesn't exist");
        }
        return account;
    }
}
