package com.chillpill.zhospar.controller;

import com.chillpill.zhospar.controller.dto.AddTaskStatusRequest;
import com.chillpill.zhospar.controller.dto.TaskDto;
import com.chillpill.zhospar.controller.dto.TaskStatusDto;
import com.chillpill.zhospar.repository.dto.Account;
import com.chillpill.zhospar.repository.dto.Project;
import com.chillpill.zhospar.repository.dto.ProjectMembership;
import com.chillpill.zhospar.repository.dto.TaskStatus;
import com.chillpill.zhospar.service.AccountDetailsService;
import com.chillpill.zhospar.service.ProjectService;
import com.chillpill.zhospar.service.TaskService;
import com.chillpill.zhospar.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/status")
public class TaskStatusController {
    private final AccountDetailsService accountDetailsService;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final Converter converter;

    @Autowired
    public TaskStatusController(AccountDetailsService accountDetailsService, ProjectService projectService,
                                TaskService taskService, Converter converter) {
        this.accountDetailsService = accountDetailsService;
        this.projectService = projectService;
        this.taskService = taskService;
        this.converter = converter;
    }

    @GetMapping("/add")
    public String getStatus(Model model, HttpServletRequest request) {
        Account account = accountDetailsService.getAccountById((Long)request.getSession().getAttribute("userId"));
        List<ProjectMembership> memberships = account.getMemberships();
        model.addAttribute("memberships", memberships);
        return "addStatus";
    }

    @PostMapping("/add")
    public String addStatus(AddTaskStatusRequest statusRequest) {
        TaskStatus status = new TaskStatus();
        status.setProject(projectService.getProjectById(statusRequest.getProjectId()));
        status.setStatusName(statusRequest.getStatusName());
        taskService.createTaskStatus(status);
        return "redirect:/projects/" + statusRequest.getProjectId();
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<List<TaskStatusDto>> getTaskStatuses(@PathVariable("id") long id) {
        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(
                project.getTaskStatuses().stream()
                .map(converter::convertTaskStatusDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<List<TaskDto>> getTasksByStatus(@PathVariable("id") long statusId) {
        TaskStatus status = taskService.getTaskStatusByStatusId(statusId);
        return ResponseEntity.ok(
                status.getTaskList().stream()
                .map(converter::convertTaskDto)
                .collect(Collectors.toList())
        );
    }
}
