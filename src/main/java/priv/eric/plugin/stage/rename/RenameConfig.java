package priv.eric.plugin.stage.rename;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * Description: 重命名配置
 *
 * @author EricTowns
 * @date 2026/1/27 10:38
 */
@Data
public class RenameConfig {

    @JsonProperty("type")
    private String type;

    @JsonProperty("mappings")
    private Map<String, String> mappings;

}
