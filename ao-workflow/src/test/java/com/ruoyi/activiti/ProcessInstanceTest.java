package com.ruoyi.activiti;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ProcessInstanceTest {
    @Autowired
    private RuntimeService runtimeService;

    /**
     *  创建流程实例
     */
    @Test
    public void testInitProcessInstance() {
        // business_key 是相关业务表的主键，把业务数据和activiti流程数据关联。
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("deployment_key", "business_key");
        System.err.println("processInstance id: " + processInstance.getProcessDefinitionId());
    }

    /**
     *  获取流程实例列表
     */
    @Test
    public void queryProcessInstances() {
        List<ProcessInstance> list =
                runtimeService.createProcessInstanceQuery().list();
        for (ProcessInstance processInstance : list) {
            System.err.println(processInstance.getId());
            System.err.println(processInstance.getProcessDefinitionId());
            System.err.println(processInstance.getName());
            System.err.println(processInstance.getDeploymentId());
            System.err.println(processInstance.getBusinessKey());
            System.err.println(processInstance.getProcessDefinitionKey());
            System.err.println(processInstance.isEnded());
            System.err.println("--------------------------");
        }
    }

    /**
     *  暂停或激活流程实例
     */
    @Test
    public void testActiveProcessInstance() {
        String processInstanceId = "";
        // 挂起流程实例
        runtimeService.suspendProcessInstanceById(processInstanceId);
        // 激活流程实例
        runtimeService.activateProcessInstanceById(processInstanceId);
    }

    /**
     *  删除流程实例
     */
    @Test
    public void testDelProcessInstance() {
        String processInstanceId = "";
        String reason = "";
        runtimeService.deleteProcessInstance(processInstanceId, reason);
    }
}
