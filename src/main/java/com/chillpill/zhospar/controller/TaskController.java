package com.chillpill.zhospar.controller;

import com.chillpill.zhospar.controller.dto.AddTaskRequest;
import com.chillpill.zhospar.repository.dto.Account;
import com.chillpill.zhospar.repository.dto.Project;
import com.chillpill.zhospar.repository.dto.ProjectMembership;
import com.chillpill.zhospar.repository.dto.Task;
import com.chillpill.zhospar.service.ProjectService;
import com.chillpill.zhospar.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/task")
public class TaskController {
    private final ProjectService projectService;
    private final TaskService taskService;
    @Autowired
    public TaskController(ProjectService projectService, TaskService taskService) {
        this.projectService = projectService;
        this.taskService = taskService;
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
        task.setDeadline(taskRequest.getDeadline());
        task.setDescription(taskRequest.getTaskName());
        task.setParentTask(taskService.getTask(taskRequest.getParentId()));
        task.setProject(projectService.getProjectById(taskRequest.getProjectId()));
        task.setStatus(taskService.getTaskStatusByStatusId(taskRequest.getStatusId()));
        taskService.createTask(task);
        return "redirect:/projects/"+taskRequest.getProjectId();
    }
}
