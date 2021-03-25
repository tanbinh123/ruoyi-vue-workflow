package com.ruoyi.rest;

import com.ruoyi.config.security.SecurityUtil;
import com.ruoyi.entity.vo.ProcessInstanceVo;
import lombok.RequiredArgsConstructor;
import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import com.ruoyi.mapstruct.ProcessInstanceMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  流程实例controller
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/activiti7/processInstance")
public class ProcessInstanceController {

    private final RepositoryService repositoryService;

    private final ProcessRuntime processRuntime;

    private final ProcessInstanceMapper processInstanceMapper;

    private final SecurityUtil securityUtil;

    /**
     *  获取流程实例列表
     * @return ResponseEntity<Object>
     */
    @GetMapping("/processInstanceList")
    public ResponseEntity<Object> processInstances(Integer current, Integer pageSize) {

        // securityUtil.logInAs("alice");

        Page<ProcessInstance> processInstancePage =
                processRuntime.processInstances(Pageable.of(current, pageSize));

        List<ProcessInstance> processInstances = processInstancePage.getContent();

        List<ProcessInstanceVo> processInstanceVos = processInstanceMapper.toVo(processInstances);

        List<ProcessInstanceVo> results = processInstanceVos.parallelStream().map(k -> {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(k.getProcessDefinitionId()).singleResult();

            k.setDeploymentId(processDefinition.getDeploymentId());
            k.setResourceName(processDefinition.getResourceName());

            return k;
        }).collect(Collectors.toList());

        int total = processInstancePage.getTotalItems();


        return ResponseEntity.ok(new HashMap<>(){
            {
                put("data", results);
                put("total", total);
            }
        });
    }

    /**
     *  启动流程实例
     * @param processDefKey  流程定义key
     * @param instanceName  流程实例名称
     * @return ResponseEntity<Object>
     */
    @GetMapping("/startProcess")
    public ResponseEntity<Object> startProcess(@RequestParam("processDefKey") String processDefKey,
                                               @RequestParam("instanceName") String instanceName) {
        ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder.start()
                .withProcessDefinitionKey(processDefKey)
                .withName(instanceName).build());

        return ResponseEntity.ok(processInstance);
    }

    /**
     *  挂起流程实例
     * @param instanceId 流程实例ID
     * @return ResponseEntity<Object>
     */
    @GetMapping("/suspendInstance")
    public ResponseEntity<Object> suspendInstance(@RequestParam("instanceId") String instanceId) {

        ProcessInstance processInstance = processRuntime.suspend(ProcessPayloadBuilder.suspend()
                .withProcessInstanceId(instanceId).build());

        return ResponseEntity.ok().build();
    }

    /**
     *  激活流程实例
     * @param instanceId 流程实例ID
     * @return ResponseEntity<Object>
     */
    @GetMapping("/resumeInstance")
    public ResponseEntity<Object> resumeInstance(@RequestParam("instanceId") String instanceId) {

        ProcessInstance processInstance = processRuntime.resume(ProcessPayloadBuilder.resume()
                .withProcessInstanceId(instanceId).build());

        return ResponseEntity.ok().build();
    }

    /**
     *  删除流程实例
     * @param instanceId 流程实例ID
     * @return ResponseEntity<Object>
     */
    @DeleteMapping("/delInstance")
    public ResponseEntity<Object> delInstance(@RequestParam("instanceId") String instanceId) {

        ProcessInstance processInstance = processRuntime.delete(ProcessPayloadBuilder.delete()
                .withProcessInstanceId(instanceId).build());

        return ResponseEntity.ok().build();
    }

    /**
     *  查询流程实例参数
     * @param instanceId 流程实例ID
     * @return ResponseEntity<Object>
     */
    @DeleteMapping("/variables")
    public ResponseEntity<Object> variables(@RequestParam("instanceId") String instanceId) {

        List<VariableInstance> variables = processRuntime.variables(ProcessPayloadBuilder.variables()
                .withProcessInstanceId(instanceId).build());

        return ResponseEntity.ok(variables);
    }

}
