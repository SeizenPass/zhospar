package com.chillpill.zhospar.repository;

import com.chillpill.zhospar.repository.dto.Account;
import com.chillpill.zhospar.repository.dto.Project;
import com.chillpill.zhospar.repository.dto.ProjectMembership;
import com.chillpill.zhospar.repository.dto.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMembershipRepository extends JpaRepository<ProjectMembership, Long> {
    ProjectMembership findFirstByAccountAndProject(Account account, Project project);
    List<ProjectMembership> findAllByProject(Project project);
    List<ProjectMembership> findAllByAccountAndProjectRole(Account account, ProjectRole role);
}
