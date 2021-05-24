package com.chillpill.zhospar.service;

import com.chillpill.zhospar.repository.TaskRepository;
import com.chillpill.zhospar.repository.TaskStatusRepository;
import com.chillpill.zhospar.repository.dto.Project;
import com.chillpill.zhospar.repository.dto.Task;
import com.chillpill.zhospar.repository.dto.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskStatusRepository statusRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskStatusRepository statusRepository, TaskRepository taskRepository) {
        this.statusRepository = statusRepository;
        this.taskRepository = taskRepository;
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
        return taskRepository.getTaskByTaskId(id);
    }
}
