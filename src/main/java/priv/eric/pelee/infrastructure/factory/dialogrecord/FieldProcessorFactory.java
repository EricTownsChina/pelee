package priv.eric.pelee.infrastructure.factory.dialogrecord;

import priv.eric.pelee.domain.dialogrecord.model.FieldProcessType;
import priv.eric.pelee.domain.dialogrecord.model.FieldProcessor;
import priv.eric.pelee.infrastructure.pojo.dialogrecord.FieldProcessPO;

/**
 * Description: 字段处理工厂
 *
 * @author EricTowns
 * @date 2026/1/22 14:51
 */
public interface FieldProcessorFactory {

    /**
     * 获取字段处理类型
     *
     * @return 处理类型
     */
    FieldProcessType type();

    /**
     * 创建字段处理器
     *
     * @param fieldProcessPO 字段处理映射
     * @return 字段处理器
     */
    FieldProcessor create(FieldProcessPO fieldProcessPO);

}
