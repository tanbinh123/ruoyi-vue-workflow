package com.ruoyi.activiti.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

@Slf4j
public class SetAssignListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
      log.debug("上一个任务的执行人assigneeUser: {}", delegateTask.getVariable("assigneeUser"));
      //
    }
}
