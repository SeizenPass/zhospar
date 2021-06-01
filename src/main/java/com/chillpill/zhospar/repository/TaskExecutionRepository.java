package com.chillpill.zhospar.repository;

import com.chillpill.zhospar.repository.dto.Task;
import com.chillpill.zhospar.repository.dto.TaskExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface TaskExecutionRepository extends JpaRepository<TaskExecution, Long> {
    @Transactional
    void deleteAllByTask(Task task);
}
