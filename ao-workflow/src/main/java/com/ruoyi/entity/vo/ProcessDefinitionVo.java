package com.ruoyi.entity.vo;

import lombok.Data;

@Data
public class ProcessDefinitionVo {
    private String id;
    protected String name;
    protected String description;
    protected String key;
    protected int version;
    protected String category;
    protected String deploymentId;
    private String processDefinitionId;
    protected String resourceName;
}
