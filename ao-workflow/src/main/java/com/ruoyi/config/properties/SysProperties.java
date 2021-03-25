package com.ruoyi.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "workflow")
public class SysProperties {


    private String bpmnLocation = "/Users/lwt2710/Public/code/BPMN/test/";

}
