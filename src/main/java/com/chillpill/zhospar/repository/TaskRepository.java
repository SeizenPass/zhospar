package com.chillpill.zhospar.repository;

import com.chillpill.zhospar.repository.dto.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Task getTaskByTaskId(long id);
    @Transactional
    void deleteAllByParentTask(Task task);
    List<Task> findAllByParentTask(Task task);
}
