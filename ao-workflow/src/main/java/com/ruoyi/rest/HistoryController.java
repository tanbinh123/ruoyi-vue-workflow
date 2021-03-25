package com.ruoyi.rest;

import com.ruoyi.entity.vo.HistoricTaskInstanceVo;
import com.ruoyi.entity.vo.SequenceFlowVo;
import lombok.RequiredArgsConstructor;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.apache.commons.lang3.StringUtils;
import com.ruoyi.mapstruct.HistoricTaskInstanceMapper;
import com.ruoyi.mapstruct.SequenceFlowMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/activiti7/historic")
public class HistoryController {

    private final HistoryService historyService;

    private final RepositoryService repositoryService;

    private final SequenceFlowMapper sequenceFlowMapper;

    private final HistoricTaskInstanceMapper historicTaskInstanceMapper;

    // 用户历史任务
    @GetMapping("/userHistoricTask")
    public ResponseEntity<Object> userHistoricTask(Integer current, Integer pageSize, @RequestParam(required = false) String assignee) {

        String username = assignee;
        if(StringUtils.isEmpty(assignee)) {
            username = SecurityContextHolder.getContext().getAuthentication().getName();
        }

        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(username)
                .listPage(current, pageSize);

        List<HistoricTaskInstanceVo> historicTaskInstanceVos = historicTaskInstanceMapper.toVo(historicTaskInstances);

        Long count = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(username)
                .count();

        return ResponseEntity.ok(new HashMap<>(){
            {
                put("data", historicTaskInstanceVos);
                put("total", count);
            }
        });
    }

    // 根据流程实例ID查询任务
    @GetMapping("/historicByInstanceId")
    public ResponseEntity<Object> historicByInstanceId(String processInstanceId) {

        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime()
                .processInstanceId(processInstanceId)
                .list();

        return ResponseEntity.ok(historicTaskInstanceMapper.toVo(historicTaskInstances));
    }

    // 高亮显示流程历史
    @Deprecated
    @GetMapping("/highLine")
    public ResponseEntity<Object> highLine(String processInstanceId) {

        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();

        HistoricTaskInstance historicTaskInstance = historicTaskInstanceList.get(0);

        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicTaskInstance.getProcessDefinitionId());
        Process process = bpmnModel.getProcesses().get(0);
        Collection<FlowElement> flowElements = process.getFlowElements();

        List<SequenceFlowVo> sequenceFlowVos = new ArrayList<>();
        for (FlowElement flowElement : flowElements) {
            if(flowElement instanceof SequenceFlow) {
                SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                String sourceRef = sequenceFlow.getSourceRef();    // 流程线前置节点key
                String targetRef = sequenceFlow.getTargetRef();    // 后置节点key
                String lineKey = sequenceFlow.getId();    // 线条key
                sequenceFlowVos.add(sequenceFlowMapper.toVo(sequenceFlow));
            }
        }

        // 已经走过的历史节点
        List<HistoricActivityInstance> historicList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();

        List<String> histories = historicList.parallelStream().map(HistoricActivityInstance::getActivityId).collect(Collectors.toList());

        sequenceFlowVos.parallelStream().forEach(sequenceFlowVo -> {
            if(histories.contains(sequenceFlowVo.getTargetRef())) {
                sequenceFlowVo.setHistoric(true);
            } else {
                sequenceFlowVo.setHistoric(false);
            }
        });


        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 当前用户完成的任务
        List<HistoricTaskInstance> finishedTasks = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(principal.getUsername())
                .processInstanceId(processInstanceId)
                .finished()
                .list();

        List<String> finishedList = finishedTasks.parallelStream().map(HistoricTaskInstance::getTaskDefinitionKey).collect(Collectors.toList());

        sequenceFlowVos.parallelStream().forEach(sequenceFlowVo -> {
            if(finishedList.contains(sequenceFlowVo.getSourceRef())) {
                sequenceFlowVo.setFinishedByCurrent(true);
            } else {
                sequenceFlowVo.setFinishedByCurrent(false);
            }
        });

        //获取流程实例 历史节点（已完成）
        List<HistoricActivityInstance> listFinished = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .list();
        //高亮节点ID
        List<String> highPoint = listFinished.parallelStream().map(s -> s.getActivityId()).collect(Collectors.toList());
        sequenceFlowVos.parallelStream().forEach(sequenceFlowVo -> {
            if(highPoint.contains(sequenceFlowVo.getSourceRef())) {
                sequenceFlowVo.setFinished(true);
            } else {
                sequenceFlowVo.setFinished(false);
            }
        });

        // 获取流程实例 历史节点（待办节点）
        List<HistoricActivityInstance> listUnFinished = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .unfinished()
                .list();
        List<String> listUnFinishedPoint = listUnFinished.parallelStream().map(s -> s.getActivityId()).collect(Collectors.toList());
        sequenceFlowVos.parallelStream().forEach(sequenceFlowVo -> {
            if(listUnFinishedPoint.contains(sequenceFlowVo.getSourceRef())) {
                sequenceFlowVo.setUnFinished(true);
            } else {
                sequenceFlowVo.setUnFinished(false);
            }
        });


        return ResponseEntity.ok(sequenceFlowVos);
    }


    //流程图高亮
    @GetMapping("/getHighLine")
    public ResponseEntity<Object> getHighLine(@RequestParam("instanceId") String instanceId) {
        try {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(instanceId).singleResult();
            //获取bpmnModel对象
            BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
            //因为我们这里只定义了一个Process 所以获取集合中的第一个即可
            Process process = bpmnModel.getProcesses().get(0);
            //获取所有的FlowElement信息
            Collection<FlowElement> flowElements = process.getFlowElements();

            Map<String, String> map = new HashMap<>();
            for (FlowElement flowElement : flowElements) {
                //判断是否是连线
                if (flowElement instanceof SequenceFlow) {
                    SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                    String ref = sequenceFlow.getSourceRef();
                    String targetRef = sequenceFlow.getTargetRef();
                    map.put(ref + targetRef, sequenceFlow.getId());
                }
            }

            //获取流程实例 历史节点(全部)
            List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(instanceId)
                    .list();
            //各个历史节点   两两组合 key
            Set<String> keyList = new HashSet<>();
            for (HistoricActivityInstance i : list) {
                for (HistoricActivityInstance j : list) {
                    if (i != j) {
                        keyList.add(i.getActivityId() + j.getActivityId());
                    }
                }
            }
            //高亮连线ID
            Set<String> highLine = new HashSet<>();
            keyList.forEach(s -> highLine.add(map.get(s)));


            //获取流程实例 历史节点（已完成）
            List<HistoricActivityInstance> listFinished = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(instanceId)
                    .finished()
                    .list();
            //高亮节点ID
            Set<String> highPoint = new HashSet<>();
            listFinished.forEach(s -> highPoint.add(s.getActivityId()));

            //获取流程实例 历史节点（待办节点）
            List<HistoricActivityInstance> listUnFinished = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(instanceId)
                    .unfinished()
                    .list();

            //需要移除的高亮连线
            Set<String> set = new HashSet<>();
            //待办高亮节点
            Set<String> waitingToDo = new HashSet<>();
            listUnFinished.forEach(s -> {
                waitingToDo.add(s.getActivityId());

                for (FlowElement flowElement : flowElements) {
                    //判断是否是 用户节点
                    if (flowElement instanceof UserTask) {
                        UserTask userTask = (UserTask) flowElement;

                        if (userTask.getId().equals(s.getActivityId())) {
                            List<SequenceFlow> outgoingFlows = userTask.getOutgoingFlows();
                            //因为 高亮连线查询的是所有节点  两两组合 把待办 之后  往外发出的连线 也包含进去了  所以要把高亮待办节点 之后 即出的连线去掉
                            if (outgoingFlows != null && outgoingFlows.size() > 0) {
                                outgoingFlows.forEach(a -> {
                                    if (a.getSourceRef().equals(s.getActivityId())) {
                                        set.add(a.getId());
                                    }
                                });
                            }
                        }
                    }
                }
            });

            highLine.removeAll(set);
            //获取当前用户
            //User sysUser = getSysUser();
            Set<String> iDo = new HashSet<>(); //存放 高亮 我的办理节点
            //当前用户已完成的任务

            String assigneeName = "alice";    // 获取当前用户
            /*if (GlobalConfig.Test) {
                AssigneeName = "bajie";
            } else {
                AssigneeName = UuserInfoBean.getUsername();
            }*/

            List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery()
                    .taskAssignee(assigneeName)
                    .finished()
                    .processInstanceId(instanceId).list();

            taskInstanceList.forEach(a -> iDo.add(a.getTaskDefinitionKey()));

            Map<String, Object> reMap = new HashMap<>();
            reMap.put("highPoint", highPoint);
            reMap.put("highLine", highLine);
            reMap.put("waitingToDo", waitingToDo);
            reMap.put("iDo", iDo);
            return ResponseEntity.ok(reMap);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("渲染历史流程失败");
        }
    }


}
