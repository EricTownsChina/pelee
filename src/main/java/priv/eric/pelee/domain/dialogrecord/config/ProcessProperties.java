package priv.eric.pelee.domain.dialogrecord.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Description: 处理器配置
 *
 * @author EricTowns
 * @date 2026/1/18 20:33
 */
@Data
public class ProcessProperties {

    /**
     * 字段处理器
     */
    @JsonProperty("fields")
    private List<FieldProcessProperties> fields;

}
