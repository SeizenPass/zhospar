package com.chillpill.zhospar.service;

import com.chillpill.zhospar.repository.AccountRepository;
import com.chillpill.zhospar.repository.dto.Account;
import com.chillpill.zhospar.repository.dto.ProjectMembership;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountDetailsService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByUsername(s);
        if (account == null) {
            throw new UsernameNotFoundException("Account with such username doesn't exist");
        }
        return account;
    }

    public Account findByUsername(String s) {
        return accountRepository.findAccountByUsername(s);
    }

    public Account loadByEmail(String email) {
        return accountRepository.findAccountByEmail(email);
    }

    public Account saveAccount(Account account) {
        if (!account.getFullName().trim().isEmpty() || !account.getPassword().trim().isEmpty() ||
        !account.getUsername().trim().isEmpty() || !account.getEmail().trim().isEmpty()) {
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            accountRepository.save(account);
            return account;
        }
        else {
            return null;
        }
    }

    public Account getAccountById(long id) {
        return accountRepository.getOne(id);
    }

    public List<ProjectMembership> getAllMembershipsByAccountId(long accountId) {
        return accountRepository.getOne(accountId).getMemberships();
    }
}
