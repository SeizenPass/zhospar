package com.chillpill.zhospar.controller;

import com.chillpill.zhospar.repository.dto.Account;
import com.chillpill.zhospar.service.AccountDetailsService;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final AccountDetailsService accountDetailsService;

    @Autowired
    public ProfileController(AccountDetailsService accountDetailsService) {
        this.accountDetailsService = accountDetailsService;
    }

    @GetMapping("/{id}")
    public String getProfile(Model model, @PathVariable("id") long profileId, HttpServletRequest request) {
        Account user = accountDetailsService.getAccountById((Long)request.getSession().getAttribute("userId"));
        Account profile = accountDetailsService.getAccountById(profileId);
        boolean isUser = false;
        if (user.getAccountId() == profile.getAccountId()) isUser = true;
        model.addAttribute("isUser", isUser);
        model.addAttribute("profile", profile);
        model.addAttribute("user", user);
        return "profile";
    }
}
