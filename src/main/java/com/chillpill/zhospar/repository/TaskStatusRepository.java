package com.chillpill.zhospar.repository;

import com.chillpill.zhospar.repository.dto.Project;
import com.chillpill.zhospar.repository.dto.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
    List<TaskStatus> findAllByProject(Project project);
}
