package com.ruoyi.mapstruct;

import com.ruoyi.entity.vo.SequenceFlowVo;
import org.activiti.bpmn.model.SequenceFlow;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring" ,unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SequenceFlowMapper {

    SequenceFlowVo toVo(SequenceFlow entity);


    List<SequenceFlowVo> toVo(List<SequenceFlow> entity);

}
