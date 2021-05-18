package com.chillpill.zhospar.service;

import com.chillpill.zhospar.repository.ProjectMembershipRepository;
import com.chillpill.zhospar.repository.ProjectRepository;
import com.chillpill.zhospar.repository.ProjectRoleRepository;
import com.chillpill.zhospar.repository.dto.Account;
import com.chillpill.zhospar.repository.dto.Project;
import com.chillpill.zhospar.repository.dto.ProjectMembership;
import com.chillpill.zhospar.repository.dto.ProjectRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectRoleRepository projectRoleRepository;
    private final ProjectMembershipRepository membershipRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ProjectRoleRepository projectRoleRepository, ProjectMembershipRepository membershipRepository) {
        this.projectRepository = projectRepository;
        this.projectRoleRepository = projectRoleRepository;
        this.membershipRepository = membershipRepository;
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
}
