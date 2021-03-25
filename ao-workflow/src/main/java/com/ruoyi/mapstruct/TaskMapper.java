package com.ruoyi.mapstruct;


import com.ruoyi.entity.vo.TaskVo;
import org.activiti.api.task.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring" ,unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    TaskVo toVo(Task task);

    List<TaskVo> toVo(List<Task> tasks);

}
