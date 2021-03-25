package com.ruoyi.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class HistoricTaskInstanceVo {
    private String id;
    protected String executionId;
    protected String name;
    protected String localizedName;
    protected String parentTaskId;
    protected String description;
    protected String localizedDescription;
    protected String owner;
    protected String assignee;
    protected String taskDefinitionKey;
    protected String formKey;
    protected int priority;
    protected Date dueDate;
    protected Date claimTime;
    protected String category;

    protected String processInstanceId;
    protected String processDefinitionId;
    protected Date startTime;
    protected Date endTime;
    protected Long durationInMillis;
    protected String deleteReason;
}
