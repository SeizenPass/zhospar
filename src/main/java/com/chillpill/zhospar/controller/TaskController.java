package com.chillpill.zhospar.controller;

import com.chillpill.zhospar.controller.dto.AddTaskRequest;
import com.chillpill.zhospar.controller.dto.UpdateTaskRequest;
import com.chillpill.zhospar.repository.dto.*;
import com.chillpill.zhospar.service.AccountDetailsService;
import com.chillpill.zhospar.service.ProjectService;
import com.chillpill.zhospar.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        Account account = accountDetailsService.getAccountById((Long)request.getSession().getAttribute("userId"));
        List<ProjectMembership> memberships = account.getMemberships();
        model.addAttribute("memberships", memberships);
        return "addTask";
    }

    @PostMapping("/add")
    public String createTask(AddTaskRequest taskRequest, HttpServletRequest request) {
        Account account = accountDetailsService.getAccountById((Long)request.getSession().getAttribute("userId"));
        Task task = new Task();
        task.setCreator(account);
        task.setTitle(taskRequest.getTaskName());
        task.setDescription(taskRequest.getDescription());
        if (taskRequest.getParentId() != 0) {
            Task parentTask = taskService.getTask(taskRequest.getParentId());
            task.setParentTask(parentTask);
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

    @GetMapping("/{id}")
    public String getTask(Model model, @PathVariable("id") long taskid, HttpServletRequest request) {
        Task task = taskService.getTask(taskid);
        model.addAttribute("task", task);
        boolean isUser = false;
        Account account = accountDetailsService.getAccountById((Long)request.getSession().getAttribute("userId"));
        if (account.getAccountId() == task.getCreator().getAccountId()) isUser = true;
        model.addAttribute("isUser", isUser);
        return "taskView";
    }

    @GetMapping("/{id}/delete")
    public ResponseEntity<Boolean> deleteTask(Model model, @PathVariable("id") long taskid, HttpServletRequest request) {
        Task task = taskService.getTask(taskid);
        Account account = accountDetailsService.getAccountById((Long)request.getSession().getAttribute("userId"));
        if (task.getCreator().getAccountId() != account.getAccountId()) {
            return ResponseEntity.ok(false);
        }
        taskService.deleteTask(taskid);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/{id}/status/{statusId}")
    public ResponseEntity<Boolean> changeStatus(@PathVariable("id") long taskId, @PathVariable("statusId") long statusId, HttpServletRequest request) {
        Task task = taskService.getTask(taskId);
        Account account = accountDetailsService.getAccountById((Long)request.getSession().getAttribute("userId"));
        if (task.getCreator().getAccountId() != account.getAccountId()) {
            return ResponseEntity.ok(false);
        }
        TaskStatus status = taskService.getTaskStatusByStatusId(statusId);
        task.setStatus(status);
        taskService.createTask(task);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/update")
    public String updateTask(Model model, UpdateTaskRequest taskRequest, HttpServletRequest request) {
        Task task = taskService.getTask(taskRequest.getTaskId());
        if (task == null) {
            model.addAttribute("error", "Not found");
            return "error";
        }
        Account account = accountDetailsService.getAccountById((Long)request.getSession().getAttribute("userId"));
        if (task.getCreator().getAccountId() != account.getAccountId()) {
            model.addAttribute("error", "You are not a creator of this task");
            return "error";
        }
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setDeadline(Date.valueOf(taskRequest.getDeadline()));
        taskService.createTask(task);
        return "redirect:/task/" + task.getTaskId();
    }
}
