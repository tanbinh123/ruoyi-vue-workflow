package com.ruoyi.activiti.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

@Slf4j
public class SenderMsgListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
      log.debug("执行人: {}", delegateTask.getAssignee());
        delegateTask.setVariable("assigneeUser", delegateTask.getAssignee());
    }
}
