package com.chillpill.zhospar.controller;

import com.chillpill.zhospar.controller.dto.UpdateProfileRequest;
import com.chillpill.zhospar.repository.dto.Account;
import com.chillpill.zhospar.repository.dto.Project;
import com.chillpill.zhospar.repository.dto.ProjectMembership;
import com.chillpill.zhospar.service.AccountDetailsService;
import com.chillpill.zhospar.service.ImageService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final AccountDetailsService accountDetailsService;
    private final ImageService imageService;

    @Autowired
    public ProfileController(AccountDetailsService accountDetailsService, ImageService imageService) {
        this.accountDetailsService = accountDetailsService;
        this.imageService = imageService;
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
        if (!isUser) {
            List<ProjectMembership> memberships = profile.getMemberships();
            List<Project> result = new ArrayList<>();
            List<Long> userProjects = new ArrayList<>();
            user.getMemberships().forEach(o -> userProjects.add(o.getProject().getProjectId()));
            for (ProjectMembership pm:
                 memberships) {
                Project pr = pm.getProject();
                if (userProjects.contains(pr.getProjectId())) result.add(pr);
            }
            model.addAttribute("projects", result);
        }
        return "profile";
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updateProfile(Model model, HttpServletRequest request,
                                @RequestPart("image")MultipartFile imagePart,
                                @RequestPart("fullName") String fullName) {
        Account user = accountDetailsService.getAccountById((Long)request.getSession().getAttribute("userId"));
        byte[] content = {};
        try {
            content = imagePart.getBytes();
        } catch (IOException e) {
            model.addAttribute("error", e.getCause());
            return "error";
        }
        UUID uuid = UUID.randomUUID();
        File file = new File("filesystem/" + uuid.toString());
        try (FileOutputStream stream = new FileOutputStream(file)) {
            if (!file.exists()) file.createNewFile();
            stream.write(content);
            stream.flush();
        } catch (IOException e) {
            model.addAttribute("error", e.getCause());
            return "error";
        }
        user.setAvatarId(uuid);
        user.setFullName(fullName);
        accountDetailsService.updateAccount(user);
        return "redirect:/profile/"+user.getAccountId();
    }
}
