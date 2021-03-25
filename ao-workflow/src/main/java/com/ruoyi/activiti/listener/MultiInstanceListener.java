package com.ruoyi.activiti.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MultiInstanceListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) {

        log.debug("MultiInstanceListener执行。。。");

        // 获取并行执行人列表
        List<String> assigneeList = new ArrayList<>() {
            {
                add("alice");
                add("john");
                add("郭靖");
                add("黄蓉");
            }
        };
        delegateExecution.setVariable("assigneeList", assigneeList);
        delegateExecution.setVariable("assigneeCount", assigneeList.size());
    }
}
