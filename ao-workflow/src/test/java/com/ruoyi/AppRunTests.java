package com.ruoyi;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class AppRunTests {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Test
    void contextLoads() {
        /*Map<String, Object> variables = new HashMap<>(){
            {
                put("isBack", 0);
            }
        };*/
        //taskService.complete("c6a7aab0-5353-11eb-99f1-de1cc276926b", variables);

        Execution execution = runtimeService.createExecutionQuery()
                .messageEventSubscriptionName("Message_1vh6u34")
                .processInstanceId("52fec003-5417-11eb-b896-5a4176db00e8")
                .singleResult();

        runtimeService.messageEventReceived("Message_1vh6u34", execution.getId());

    }

}
