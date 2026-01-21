package priv.eric.pelee.domain.dialogrecord.model;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Description: 字段处理类型
 *
 * @author EricTowns
 * @date 2026/1/18 16:05
 */
public enum FieldProcessType {

    /**
     * 字段重命名
     */
    RENAME,
    /**
     * 字段值提取到新字段
     */
    EXTRACT,
    /**
     * 删除字段
     */
    DROP,
    /**
     * 自定义
     */
    CUSTOMIZE;

    @JsonCreator
    public static FieldProcessType from(String value) {
        return FieldProcessType.valueOf(value.toUpperCase());
    }

}
