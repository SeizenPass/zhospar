package com.chillpill.zhospar.service;

import com.chillpill.zhospar.repository.TaskExecutionRepository;
import com.chillpill.zhospar.repository.TaskRepository;
import com.chillpill.zhospar.repository.TaskStatusRepository;
import com.chillpill.zhospar.repository.dto.Project;
import com.chillpill.zhospar.repository.dto.Task;
import com.chillpill.zhospar.repository.dto.TaskExecution;
import com.chillpill.zhospar.repository.dto.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskStatusRepository statusRepository;
    private final TaskRepository taskRepository;
    private final TaskExecutionRepository executionRepository;

    @Autowired
    public TaskService(TaskStatusRepository statusRepository, TaskRepository taskRepository, TaskExecutionRepository executionRepository) {
        this.statusRepository = statusRepository;
        this.taskRepository = taskRepository;
        this.executionRepository = executionRepository;
    }

    public List<TaskStatus> getTaskStatusesByProject(Project project) {
        return statusRepository.findAllByProject(project);
    }

    public TaskStatus createTaskStatus(TaskStatus taskStatus) {
        return statusRepository.save(taskStatus);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public TaskStatus getTaskStatusByStatusId(long id) {
        return statusRepository.getTaskStatusByStatusId(id);
    }

    public Task getTask(long id){
        return taskRepository.getOne(id);
    }

    public void deleteTask(long id) {
        Task task = getTask(id);
        taskRepository.deleteAllByParentTask(task);
        taskRepository.delete(task);
    }

    public TaskExecution createTaskExecution(TaskExecution taskExecution) {
        return executionRepository.save(taskExecution);
    }
}
