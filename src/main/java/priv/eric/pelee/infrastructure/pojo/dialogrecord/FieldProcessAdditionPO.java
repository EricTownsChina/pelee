package priv.eric.pelee.infrastructure.pojo.dialogrecord;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Description: 字段处理附加配置映射
 *
 * @author EricTowns
 * @date 2026/1/20 17:26
 */
@Data
public class FieldProcessAdditionPO {

    @JsonProperty("keep_source")
    private Boolean keepSource;

    @JsonProperty("default_value")
    private Object defaultValue;

}
