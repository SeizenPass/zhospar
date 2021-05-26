package com.chillpill.zhospar.controller;

import com.chillpill.zhospar.controller.dto.AddTaskRequest;
import com.chillpill.zhospar.repository.dto.Account;
import com.chillpill.zhospar.repository.dto.ProjectMembership;
import com.chillpill.zhospar.repository.dto.Task;
import com.chillpill.zhospar.repository.dto.TaskExecution;
import com.chillpill.zhospar.service.AccountDetailsService;
import com.chillpill.zhospar.service.ProjectService;
import com.chillpill.zhospar.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/task")
public class TaskController {
    private final ProjectService projectService;
    private final TaskService taskService;
    private final AccountDetailsService accountDetailsService;
    @Autowired
    public TaskController(ProjectService projectService, TaskService taskService, AccountDetailsService accountDetailsService) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.accountDetailsService = accountDetailsService;
    }

    @GetMapping("/add")
    public String getCreateTask(Model model, HttpServletRequest request) {
        Account account = ((Account)(request.getSession().getAttribute("user")));
        List<ProjectMembership> memberships = account.getMemberships();
        model.addAttribute("memberships", memberships);
        return "addTask";
    }

    @PostMapping("/add")
    public String createTask(AddTaskRequest taskRequest, HttpServletRequest request) {
        Account account = ((Account)(request.getSession().getAttribute("user")));
        Task task = new Task();
        task.setCreator(account);
        task.setDescription(taskRequest.getTaskName());
        if (taskRequest.getParentId() != 0) {
            Task parentTask = taskService.getTask(taskRequest.getParentId());
            task.setParentTask(parentTask);
            task.setStatus(parentTask.getStatus());
        } else {
            task.setStatus(taskService.getTaskStatusByStatusId(taskRequest.getStatusId()));
        }
        task.setProject(projectService.getProjectById(taskRequest.getProjectId()));
        taskService.createTask(task);
        if (taskRequest.getDeadline() != null && !taskRequest.getDeadline().trim().isEmpty()) {
            task.setDeadline(Date.valueOf(taskRequest.getDeadline()));
        }
        if (taskRequest.getExecutors() != null) {
            for (long id:
                    taskRequest.getExecutors()) {
                TaskExecution execution = new TaskExecution();
                execution.setTask(task);
                execution.setAccount(accountDetailsService.getAccountById(id));
                taskService.createTaskExecution(execution);
            }
        }
        return "redirect:/projects/"+taskRequest.getProjectId();
    }

    @GetMapping("{id}")
    public String getTask(Model model, @PathVariable("id") long taskid) {
        Task task = taskService.getTask(taskid);
        model.addAttribute("task", task);
        return "taskView";
    }
}
