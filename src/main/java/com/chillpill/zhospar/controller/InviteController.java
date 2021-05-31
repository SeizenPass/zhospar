package com.chillpill.zhospar.controller;

import com.chillpill.zhospar.controller.dto.AddInviteRequest;
import com.chillpill.zhospar.repository.dto.*;
import com.chillpill.zhospar.service.AccountDetailsService;
import com.chillpill.zhospar.service.InviteService;
import com.chillpill.zhospar.service.ProjectService;
import com.chillpill.zhospar.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/invite")
public class InviteController {
    private final AccountDetailsService accountDetailsService;
    private final ProjectService projectService;
    private final InviteService inviteService;
    private final Converter converter;

    @Autowired
    public InviteController(AccountDetailsService accountDetailsService, ProjectService projectService, InviteService inviteService, Converter converter) {
        this.accountDetailsService = accountDetailsService;
        this.projectService = projectService;
        this.inviteService = inviteService;
        this.converter = converter;
    }

    @GetMapping
    public String getInvite(Model model, HttpServletRequest request) {
        Account user = accountDetailsService.getAccountById((Long)request.getSession().getAttribute("userId"));
        ProjectRole role = projectService.getProjectRoleByName(ProjectService.ROLE_MAINTAINER);
        List<ProjectMembership> memberships = projectService.getProjectMembershipsByAccountAndProjectRole(user, role);
        model.addAttribute("memberships", memberships);
        return "invite";
    }

    @PostMapping
    public String createInvite(Model model, HttpServletRequest request, AddInviteRequest inviteRequest) {
        Account user = accountDetailsService.getAccountById((Long)request.getSession().getAttribute("userId"));
        Project project = projectService.getProjectById(inviteRequest.getProjectId());
        if (project == null) {
            model.addAttribute("error", "Project not found");
            return "error";
        }
        ProjectMembership membership = projectService.getProjectMembershipByAccountAndProject(user, project);
        if (membership == null) {
            model.addAttribute("error", "Membership not found");
            return "error";
        }
        if (!membership.getProjectRole().getRoleName().equals(ProjectService.ROLE_MAINTAINER)) {
            model.addAttribute("error", "You are not privileged enough");
            return "error";
        }
        Account invited = accountDetailsService.findByUsername(inviteRequest.getCredentials());
        if (invited == null) {
            invited = accountDetailsService.loadByEmail(inviteRequest.getCredentials());
        }
        if (invited == null) {
            model.addAttribute("error", "No such user");
            return "error";
        }
        Invite invite = new Invite();
        invite.setInvitedAccount(invited);
        invite.setInviterAccount(user);
        invite.setProject(project);
        inviteService.saveInvite(invite);
        return "redirect:/projects/"+inviteRequest.getProjectId();
    }
    @GetMapping("/accept/{id}")
    public String accept(Model model, @PathVariable("id") long inviteId, HttpServletRequest request) {
        Account user = accountDetailsService.getAccountById((Long)request.getSession().getAttribute("userId"));
        Invite invite = inviteService.getInviteById(inviteId);
        if (invite.isExpired()) {
            model.addAttribute("error", "Invite is expired");
            return "error";
        }
        if (!isAccessible(user, invite)) {
            model.addAttribute("error", "No such user");
            return "error";
        }
        invite.setAccepted(true);
        invite.setExpired(true);
        inviteService.saveInvite(invite);
        projectService.addMembershipByAccountIdAndProjectId(user.getAccountId(), invite.getProject().getProjectId());
        return "redirect:/projects";
    }

    @GetMapping("/decline/{id}")
    public String decline(Model model, @PathVariable("id") long inviteId, HttpServletRequest request) {
        Account user = accountDetailsService.getAccountById((Long)request.getSession().getAttribute("userId"));
        Invite invite = inviteService.getInviteById(inviteId);
        if (invite.isExpired()) {
            model.addAttribute("error", "Invite is expired");
            return "error";
        }
        if (!isAccessible(user, invite)) {
            model.addAttribute("error", "No such user");
            return "error";
        }
        invite.setAccepted(false);
        invite.setExpired(true);
        inviteService.saveInvite(invite);
        return "redirect:/projects";
    }

    private boolean isAccessible(Account account, Invite invite) {
        return invite.getInvitedAccount().getAccountId() == account.getAccountId();
    }
}
