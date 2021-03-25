package com.ruoyi.mapstruct;


import com.ruoyi.entity.vo.HistoricTaskInstanceVo;
import org.activiti.engine.history.HistoricTaskInstance;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring" ,unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HistoricTaskInstanceMapper {

    HistoricTaskInstanceVo toVo(HistoricTaskInstance historicTaskInstance);

    List<HistoricTaskInstanceVo> toVo(List<HistoricTaskInstance> historicTaskInstanceList);

}
