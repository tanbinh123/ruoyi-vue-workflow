package com.ruoyi.activiti;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class UELTest {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    /**
     *  启动流程实例
     */
    @Test
    public void testInitProcessInstanceWithArgs() {
        // 流程变量 ${assigneeUser}:在定义bpmn文件时指定执行人(assignee)为参数${assigneeUser}
        Map<String, Object> variables = new HashMap<>();

        variables.put("assigneeUser", "admin");

        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey("deployment_key", "business_key", variables);
    }

    /**
     *  完成任务时使用参数
     */
    @Test
    public void testCompleteTaskWithArgs() {

        // 排他网关： ${pay <= 100}
        Map<String, Object> variables = new HashMap<>();
        variables.put("pay", 100);
        String taskId = "";
        taskService.complete(taskId, variables);
    }

    /**
     *  启动流程实例、使用对象参数
     */
    @Test
    public void testInitProcessInstanceWithObjArgs() {
        // 流程变量 使用实体类参数 ${assigneeUser.userName}
        Map<String, Object> variables = new HashMap<>();

        Object object = new Object();
        variables.put("assigneeUser", object);

        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey("deployment_key", "business_key", variables);
    }

    /**
     *  任务完成时指定参数，指定多个候选人
     */
    @Test
    public void testInitProcessInstanceWithCandiDateArgs() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("candidates", "user1,user2");
        String taskId = "";
        taskService.complete(taskId, variables);
    }


    /**
     *  直接指定流程变量
     */
    @Test
    public void testOtherArgs() {
        String processInstanceId = "";
        String taskId = "";
        runtimeService.setVariable(processInstanceId, "varName", "value");
        taskService.setVariable(taskId, "varName", "value");
    }

    /**
     *  局部变量
     */
    @Test
    public void testLocalArgs() {
        String processInstanceId = "";
        String taskId = "";
        runtimeService.setVariableLocal(processInstanceId, "varName", "value");
        taskService.setVariableLocal(taskId, "varName", "value");
    }
}
