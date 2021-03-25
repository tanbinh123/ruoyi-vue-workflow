package com.ruoyi.mapstruct;

import com.ruoyi.entity.vo.ProcessDefinitionVo;
import org.activiti.engine.repository.ProcessDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring" ,unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProcessDefinitionMapper {

    ProcessDefinitionVo toVo(ProcessDefinition entity);


    List<ProcessDefinitionVo> toVo(List<ProcessDefinition> entity);
}
