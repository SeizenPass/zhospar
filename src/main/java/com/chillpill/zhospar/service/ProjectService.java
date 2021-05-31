package com.chillpill.zhospar.service;

import com.chillpill.zhospar.repository.AccountRepository;
import com.chillpill.zhospar.repository.ProjectMembershipRepository;
import com.chillpill.zhospar.repository.ProjectRepository;
import com.chillpill.zhospar.repository.ProjectRoleRepository;
import com.chillpill.zhospar.repository.dto.Account;
import com.chillpill.zhospar.repository.dto.Project;
import com.chillpill.zhospar.repository.dto.ProjectMembership;
import com.chillpill.zhospar.repository.dto.ProjectRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectRoleRepository projectRoleRepository;
    private final ProjectMembershipRepository membershipRepository;
    private final AccountRepository accountRepository;

    public static final String ROLE_MAINTAINER = "Maintainer";
    public static final String ROLE_VIEWER = "Viewer";

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ProjectRoleRepository projectRoleRepository, ProjectMembershipRepository membershipRepository, AccountRepository accountRepository) {
        this.projectRepository = projectRepository;
        this.projectRoleRepository = projectRoleRepository;
        this.membershipRepository = membershipRepository;
        this.accountRepository = accountRepository;
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public ProjectRole getProjectRoleByName(String projectRoleName) {
        return projectRoleRepository.findProjectRoleByRoleName(projectRoleName);
    }

    public ProjectMembership createProjectMembership(ProjectMembership projectMembership) {
        return membershipRepository.save(projectMembership);
    }

    public Project getProjectById(long id) {
        return projectRepository.getOne(id);
    }

    public ProjectMembership getProjectMembershipByAccountAndProject(Account account, Project project) {
        return membershipRepository.findFirstByAccountAndProject(account, project);
    }

    public List<ProjectMembership> getProjectMembershipsByProject(Project project) {
        return membershipRepository.findAllByProject(project);
    }

    public List<ProjectMembership>
    getProjectMembershipsByAccountAndProjectRole(Account account, ProjectRole role) {
        return membershipRepository.findAllByAccountAndProjectRole(account, role);
    }

    public ProjectMembership addMembershipByAccountIdAndProjectId(long accountId, long projectId) {
        Account account = accountRepository.getOne(accountId);
        Project project = projectRepository.getOne(projectId);
        ProjectMembership membership = new ProjectMembership();
        membership.setProjectRole(getProjectRoleByName(ROLE_VIEWER));
        membership.setProject(project);
        membership.setAccount(account);
        return membershipRepository.save(membership);
    }
}
