package com.chillpill.zhospar.repository;

import com.chillpill.zhospar.repository.dto.Account;
import com.chillpill.zhospar.repository.dto.Project;
import com.chillpill.zhospar.repository.dto.ProjectMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMembershipRepository extends JpaRepository<ProjectMembership, Long> {
    ProjectMembership findFirstByAccountAndProject(Account account, Project project);
}
