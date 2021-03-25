package com.ruoyi.activiti;

import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProcessRuntimeTest {

    @Autowired
    private ProcessRuntime processRuntime;

    /**
     *  创建流程实例
     */
    @Test
    public void testInitProcessInstance() {
        // business_key 是相关业务表的主键，把业务数据和activiti流程数据关联。

    }

    /**
     *  获取流程实例列表
     */
    @Test
    public void queryProcessInstances() {

        /*for (ProcessInstance processInstance : list) {
            System.err.println(processInstance.getId());
            System.err.println(processInstance.getProcessDefinitionId());
            System.err.println(processInstance.getName());
            System.err.println(processInstance.getDeploymentId());
            System.err.println(processInstance.getBusinessKey());
            System.err.println(processInstance.getProcessDefinitionKey());
            System.err.println(processInstance.isEnded());
            System.err.println("--------------------------");
        }*/
    }

    /**
     *  暂停或激活流程实例
     */
    @Test
    public void testActiveProcessInstance() {
        String processInstanceId = "";

    }

    /**
     *  删除流程实例
     */
    @Test
    public void testDelProcessInstance() {
        String processInstanceId = "";
        String reason = "";

    }

}
