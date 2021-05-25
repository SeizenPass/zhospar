package com.chillpill.zhospar.util;

import com.chillpill.zhospar.controller.dto.TaskDto;
import com.chillpill.zhospar.controller.dto.TaskStatusDto;
import com.chillpill.zhospar.repository.dto.Task;
import com.chillpill.zhospar.repository.dto.TaskStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Converter {
    private final ModelMapper modelMapper;

    @Autowired
    public Converter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TaskStatusDto convertTaskStatusDto(TaskStatus taskStatus) {
        return modelMapper.map(taskStatus, TaskStatusDto.class);
    }

    public TaskDto convertTaskDto(Task task) {
        return modelMapper.map(task, TaskDto.class);
    }
}
