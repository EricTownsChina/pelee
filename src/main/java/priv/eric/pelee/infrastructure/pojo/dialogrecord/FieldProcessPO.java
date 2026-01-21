package priv.eric.pelee.infrastructure.pojo.dialogrecord;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import priv.eric.pelee.domain.dialogrecord.model.FieldProcessType;

/**
 * Description: 字段处理映射
 *
 * @author EricTowns
 * @date 2026/1/18 20:32
 */
@Data
public class FieldProcessPO {

    /**
     * 处理类型
     */
    @JsonProperty("type")
    private FieldProcessType type;
    /**
     * 处理路径
     */
    @JsonProperty("path")
    private String path;
    /**
     * 源字段名称
     */
    @JsonProperty("source")
    private String source;
    /**
     * 目标字段名称
     */
    @JsonProperty("dest")
    private String dest;
    /**
     * 补充信息
     */
    @JsonProperty("addition")
    private FieldProcessAdditionPO addition;

}
