package com.ruoyi.activiti;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TaskTest {

    @Autowired
    private TaskService taskService;

    /**
     *  查询所有任务
     */
    @Test
    public void testQueryTasks() {
        List<Task> list = taskService.createTaskQuery().list();
        for (Task task : list) {
            System.err.println(task.getAssignee());
            System.err.println(task.getId());
            System.err.println(task.getName());
            System.err.println("--------------------");
        }
    }

    /**
     *  查询待办任务
     */
    @Test
    public void testQueryTaskByAssignee() {
        List<Task> list = taskService.createTaskQuery().taskAssignee("")
                .list();

        for (Task task : list) {
            System.err.println(task.getAssignee());
            System.err.println(task.getId());
            System.err.println(task.getName());
            System.err.println("--------------------");
        }
    }

    /**
     *  执行任务
     */
    @Test
    public void testCompleteTask() {
        String taskId = "";
        taskService.complete(taskId);
    }

    /**
     *  认领任务
     */
    @Test
    public void testClaimTask() {
        List<Task> list = taskService.createTaskQuery()
                .taskCandidateUser("").list();
        String userId = "";
        for (Task task : list) {
            taskService.claim(task.getId(), userId);
        }
    }

    /**
     *  设置执行人
     */
    @Test
    public void testTaskAssignee() {
        String taskId = "";
        String userId = "";
        taskService.setAssignee(taskId, userId);
    }

}
