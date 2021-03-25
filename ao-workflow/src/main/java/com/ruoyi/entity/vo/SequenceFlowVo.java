package com.ruoyi.entity.vo;

import lombok.Data;


@Deprecated
@Data
public class SequenceFlowVo {

    private String id;
    private String name;
    protected String sourceRef;
    protected String targetRef;

    /** 是否已经走过, 前端根据该字段是否高亮显示 */
    private Boolean historic;

    /** 是否是当前用户完成, true表示sourceRef已由当前用户完成 **/
    private Boolean finishedByCurrent;

    /** sourceRef是否是已完成的节点，不论是谁完成 */
    private Boolean finished;

    /** 未完成的节点 **/
    private Boolean unFinished;

}
