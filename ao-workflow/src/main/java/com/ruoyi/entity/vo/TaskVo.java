package com.ruoyi.entity.vo;

import lombok.Data;
import org.activiti.api.task.model.Task;

import java.util.Date;

@Data
public class TaskVo {

    private String id;
    private String name;
    private Task.TaskStatus status;
    private String owner;
    private String assignee;
    private String description;
    private Date createdDate;
    private Date claimedDate;
    private Date dueDate;
    private int priority;
    private String processDefinitionId;
    private String processInstanceId;
    private String parentTaskId;
    private String formKey;
    private Date completedDate;
    private Long duration;
    private Integer processDefinitionVersion;
    private String businessKey;

    private String processInstanceName;

}
