package cn.com.jonpad.juc.pool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Jon Chan
 * @date 2020/4/24 23:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto implements Serializable {
    /**
     * 任务唯一标识ID
     */
    String taskId;
    /**
     * 任务实例ID
     */
    String taskInsId;
    /**
     * 采集服务唯一标识ID
     */
    String collectorId;
    /**
     * 采集服务实例ID，采用UUID
     */
    String collectorInstId;

    /**
     * 采集源ID列表，每个元素的格式，采集源_采集源版本,
     */
    String unitIds;

    /**
     * 模板Id
     */
    String templateId;

    /**
     * 模板数据版本
     */
    Integer tdVersion;

    /**
     * 任务启停动作，1：启动；2：停止(停止只针对常驻任务有效)
     */
    Integer action;

    /**
     * 采集方式，1-服务采集；2-指令采集
     */
    Integer collType;

}
