package com.chillpill.zhospar.controller;

import com.chillpill.zhospar.controller.dto.AddProjectRequest;
import com.chillpill.zhospar.repository.dto.*;
import com.chillpill.zhospar.service.AccountDetailsService;
import com.chillpill.zhospar.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectsController {
    private final AccountDetailsService accountDetailsService;
    private final ProjectService projectService;

    @Autowired
    public ProjectsController(AccountDetailsService accountDetailsService, ProjectService projectService) {
        this.accountDetailsService = accountDetailsService;
        this.projectService = projectService;
    }

    @GetMapping
    public String getProjects(Model model, HttpServletRequest request) {
        Account account = ((Account)(request.getSession().getAttribute("user")));
        long userId = account.getAccountId();
        List<ProjectMembership> memberships = accountDetailsService.getAllMembershipsByAccountId(userId);
        List<Project> projects = new ArrayList<>();
        for (ProjectMembership membership:
             memberships) {
            projects.add(membership.getProject());
        }
        model.addAttribute("projects", projects);
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
        Account user = (Account)request.getSession().getAttribute("user");
        Project project = new Project();
        project.setProjectName(projectRequest.getProjectName());
        project.setProjectDescription(projectRequest.getDescription());
        project = projectService.createProject(project);
        ProjectRole role = projectService.getProjectRoleByName("Maintainer");
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
        Account user = (Account)request.getSession().getAttribute("user");
        ProjectMembership membership = projectService.getProjectMembershipByAccountAndProject(user, project);
        if (membership == null) {
            model.addAttribute("error", "Membership not found");
            return "error";
        }
        List<TaskStatus> taskStatusList = project.getTaskStatuses();
        model.addAttribute("project", project);
        model.addAttribute("taskStatusList", taskStatusList);
        return "dashboard";
    }
}
