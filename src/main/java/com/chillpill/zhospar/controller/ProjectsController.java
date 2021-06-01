package com.chillpill.zhospar.controller;

import com.chillpill.zhospar.controller.dto.AccountDto;
import com.chillpill.zhospar.controller.dto.AddInviteRequest;
import com.chillpill.zhospar.controller.dto.AddProjectRequest;
import com.chillpill.zhospar.repository.dto.*;
import com.chillpill.zhospar.service.AccountDetailsService;
import com.chillpill.zhospar.service.InviteService;
import com.chillpill.zhospar.service.ProjectService;
import com.chillpill.zhospar.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/projects")
public class ProjectsController {
    private final AccountDetailsService accountDetailsService;
    private final ProjectService projectService;
    private final InviteService inviteService;
    private final Converter converter;

    @Autowired
    public ProjectsController(AccountDetailsService accountDetailsService, ProjectService projectService, InviteService inviteService, Converter converter) {
        this.accountDetailsService = accountDetailsService;
        this.projectService = projectService;
        this.inviteService = inviteService;
        this.converter = converter;
    }

    @GetMapping
    public String getProjects(Model model, HttpServletRequest request) {
        Account account = accountDetailsService.getAccountById((Long)request.getSession().getAttribute("userId"));
        long userId = account.getAccountId();
        List<ProjectMembership> memberships = accountDetailsService.getAllMembershipsByAccountId(userId);
        List<Project> projects = new ArrayList<>();
        for (ProjectMembership membership:
             memberships) {
            projects.add(membership.getProject());
        }
        List<Invite> inviteList = inviteService.getInvitesByInvitedAccountId(userId);
        model.addAttribute("projects", projects);
        model.addAttribute("invites", inviteList);
        return "projects";
    }

    @GetMapping("/add")
    public String getAddProject() {
        return "addProject";
    }

    @PostMapping("/add")
    public String postAddProject(AddProjectRequest projectRequest, HttpServletRequest request) {
        if (projectRequest.getProjectName().trim().isEmpty()) {
            //TODO error validation logic
        }
        Account user = accountDetailsService.getAccountById((Long)request.getSession().getAttribute("userId"));
        Project project = new Project();
        project.setProjectName(projectRequest.getProjectName());
        project.setProjectDescription(projectRequest.getDescription());
        project = projectService.createProject(project);
        ProjectRole role = projectService.getProjectRoleByName(ProjectService.ROLE_MAINTAINER);
        ProjectMembership membership = new ProjectMembership();
        membership.setProject(project);
        membership.setAccount(user);
        membership.setProjectRole(role);
        projectService.createProjectMembership(membership);
        return "redirect:/projects";
    }

    @GetMapping("/{id}")
    public String getProject(Model model, @PathVariable("id") long projectId, HttpServletRequest request) {
        Project project = projectService.getProjectById(projectId);
        if (project == null) {
            model.addAttribute("error", "Project not found");
            return "error";
        }
        Account user = accountDetailsService.getAccountById((Long)request.getSession().getAttribute("userId"));
        ProjectMembership membership = projectService.getProjectMembershipByAccountAndProject(user, project);
        if (membership == null) {
            model.addAttribute("error", "Membership not found");
            return "error";
        }
        List<ProjectMembership> mems = projectService.getProjectMembershipsByProject(project);
        List<TaskStatus> taskStatusList = project.getTaskStatuses();
        model.addAttribute("project", project);
        model.addAttribute("taskStatusList", taskStatusList);
        model.addAttribute("user", user);
        model.addAttribute("teamList", mems);
        return "dashboard";
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<AccountDto>> getUsers(@PathVariable("id") long id, HttpServletRequest request) {
        Project project = projectService.getProjectById(id);
        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Account user = accountDetailsService.getAccountById((Long)request.getSession().getAttribute("userId"));
        ProjectMembership membership = projectService.getProjectMembershipByAccountAndProject(user, project);
        if (membership == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<ProjectMembership> projectMemberships = projectService.getProjectMembershipsByProject(project);
        List<AccountDto> accountDtoList = projectMemberships.stream()
                .map(e-> converter.convertAccountDto(e.getAccount()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(accountDtoList);
    }


}
