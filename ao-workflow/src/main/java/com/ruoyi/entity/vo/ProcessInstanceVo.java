package com.ruoyi.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ProcessInstanceVo {

    private String id;
    private String name;
    private String processDefinitionId;
    private String processDefinitionKey;
    private String initiator;
    private Date startDate;
    private String businessKey;
    private String parentId;
    private Integer processDefinitionVersion;


    private String deploymentId;

    private String resourceName;

}
