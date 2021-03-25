package com.ruoyi.rest;

import com.ruoyi.config.security.SecurityUtil;
import com.ruoyi.entity.vo.TaskVo;
import lombok.RequiredArgsConstructor;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import com.ruoyi.mapstruct.TaskMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/activiti7/task")
public class TaskController {

    private final TaskRuntime taskRuntime;

    private final TaskService taskService;

    private final ProcessRuntime processRuntime;

    private final TaskMapper taskMapper;

    private final RepositoryService repositoryService;

    private final RuntimeService runtimeService;

    private final SecurityUtil securityUtil;

    // 1 获取我的待办任务
    @GetMapping("/taskByUser")
    public ResponseEntity<Object> taskByUser(Integer current, Integer pageSize) {

        // securityUtil.logInAs("alice");

        /*UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(principal.getUsername()).listPage(current, pageSize);

        List<TaskVo> taskVos = taskMapper.toVo(tasks);*/

        Page<Task> tasks = taskRuntime.tasks(Pageable.of(current, pageSize));

        List<Task> content = tasks.getContent();

        // List<TaskVo> taskVos = taskMapper.toVo(content);

        List<TaskVo> taskVos = taskMapper.toVo(content).parallelStream().map(k -> {
            if(k.getAssignee() == null) {
                k.setAssignee("待领取任务");
            }

            ProcessInstance processInstance = processRuntime.processInstance(k.getProcessInstanceId());

            k.setProcessInstanceName(processInstance.getName());

            return k;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(new HashMap<>(){
            {
                put("data", taskVos);
                put("total", tasks.getTotalItems());
            }
        });
    }

    // 2 完成任务
    @PostMapping("/completeTask/{taskId}")
    public ResponseEntity<Object> completeTask(@PathVariable("taskId") String taskId,
                                               @RequestBody Map<String, Object> variables) {

        org.activiti.api.task.model.Task task = taskRuntime.task(taskId);
        if(task.getAssignee() == null) {
            taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(taskId).build());
        }

        org.activiti.api.task.model.Task complete = taskRuntime.complete(TaskPayloadBuilder.complete()
                .withTaskId(taskId)
                .withVariables(variables)
                .build());

        return ResponseEntity.ok(complete.getId());
    }

    // 3 渲染动态表单
    @GetMapping("/renderForm")
    public ResponseEntity<Object> renderForm(@RequestParam("taskId") String taskId) {

        /*Task task = taskRuntime.task(taskId);

        if(task.getAssignee() == null) {
            taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(taskId).build());
        }

        UserTask userTask = (UserTask) repositoryService.getBpmnModel(task.getProcessDefinitionId())
                .getFlowElement(task.getFormKey());

        if(userTask == null) {
            return ResponseEntity.ok().build();
        }


        List<FormProperty> formProperties = userTask.getFormProperties();
        formProperties.parallelStream().forEach(formProperty -> {

        });*/

        return ResponseEntity.ok().build();
    }

    // 4 保存动态表单

}
