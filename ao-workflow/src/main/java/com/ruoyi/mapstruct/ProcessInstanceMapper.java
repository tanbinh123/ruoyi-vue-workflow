package com.ruoyi.mapstruct;

import com.ruoyi.entity.vo.ProcessInstanceVo;
import org.activiti.api.process.model.ProcessInstance;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring" ,unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProcessInstanceMapper {


    ProcessInstanceVo toVo(ProcessInstance processInstance);


    List<ProcessInstanceVo> toVo(List<ProcessInstance> processInstances);

}
