package com.ruoyi.activiti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DeploymentTest {

    @Autowired
    private RepositoryService repositoryService;


    /**
     *  部署流程
     */
    @Test
    public void initDeploymentBPMN() {
        String bpmnPath = "";

        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(bpmnPath)
                .name("流程部署测试")
                .deploy();

    }


    /**
     *  查询流程部署
     */
    @Test
    public void testQueryDeployments() {
        List<Deployment> list = repositoryService.createDeploymentQuery().list();
        for (Deployment deployment : list) {
            System.err.println(deployment.getId());
            System.err.println(deployment.getName());
            System.err.println(deployment.getDeploymentTime());
            System.err.println(deployment.getKey());
            System.err.println("---------------------------");
        }
    }

}
