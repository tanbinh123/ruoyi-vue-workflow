package com.ruoyi.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class HistoricTaskInstanceTest {

    @Autowired
    private HistoryService historyService;

    /**
     *  根据用户名查询历史记录
     */
    @Test
    public void testHistoricTaskInstanceByUser() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee("")
                .list();
        for (HistoricTaskInstance historicTaskInstance : list) {
            System.err.println(historicTaskInstance.getId());
            System.err.println(historicTaskInstance.getProcessInstanceId());
            System.err.println(historicTaskInstance.getName());
        }
    }

    /**
     *  通过流程实例ID查询历史任务
     */
    @Test
    public void testHistoricTaskInstanceByPiId() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId("")
                .list();
        for (HistoricTaskInstance historicTaskInstance : list) {
            System.err.println(historicTaskInstance.getId());
            System.err.println(historicTaskInstance.getProcessInstanceId());
            System.err.println(historicTaskInstance.getName());
        }
    }
}
