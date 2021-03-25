package com.ruoyi.activiti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ProcessDefinitionTest {

    @Autowired
    private RepositoryService repositoryService;

    /**
     *  查询流程定义
     */
    @Test
    public void queryDefinitions() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .list();

        for (ProcessDefinition processDefinition : list) {
            System.err.println(processDefinition.getId());
            System.err.println(processDefinition.getKey());
            System.err.println(processDefinition.getName());
            System.err.println(processDefinition.getDeploymentId());
            System.err.println(processDefinition.getVersion());
            System.err.println("----------------------------");
        }

    }

    /**
     *  删除流程定义
     */
    @Test
    public void delDefinitions() {
        String deploymentId = "";
        repositoryService.deleteDeployment(deploymentId, false);
    }
}
