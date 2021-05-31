package com.chillpill.zhospar.controller;

import com.chillpill.zhospar.controller.dto.RegistrationRequest;
import com.chillpill.zhospar.repository.dto.Account;
import com.chillpill.zhospar.service.AccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;

@Controller
public class AuthController {
    private final AccountDetailsService accountDetailsService;

    @Autowired
    public AuthController(AccountDetailsService accountDetailsService) {
        this.accountDetailsService = accountDetailsService;
    }

    // Login form
    @GetMapping("/login")
    public String getLogin(HttpServletRequest request) {
        if (request.getSession().getAttribute("userId") != null) {
            return "redirect:/projects";
        }
        return "login";
    }

    @GetMapping("/register")
    public String getRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(RegistrationRequest registrationRequest) {
        Account account = new Account();
        account.setEmail(registrationRequest.getEmail());
        account.setFullName(registrationRequest.getFullName());
        account.setPassword(registrationRequest.getPassword());
        account.setUsername(registrationRequest.getUsername());
        account.setRegistrationDate(new Date(new java.util.Date().getTime()));
        account = accountDetailsService.saveAccount(account);
        if (account != null) {
            return "registerSuccess";
        }
        else {
            return "/error";
        }
    }
}
