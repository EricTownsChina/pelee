package priv.eric.pelee.domain.dialogrecord.config;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import priv.eric.pelee.domain.dialogrecord.entity.FieldProcessType;

/**
 * Description: 字段映射
 *
 * @author EricTowns
 * @date 2026/1/18 20:32
 */
@Data
public class FieldProcessProperties {

    @JsonProperty("type")
    private FieldProcessType type;

    @JsonProperty("source")
    @JsonAlias({"source", "source_path"})
    private String sourcePath;

    @JsonProperty("dest")
    @JsonAlias({"dest", "dest_path"})
    private String dest;

    @JsonProperty("keep_source")
    private Boolean keepSource;

    @JsonProperty("default_value")
    private Object defaultValue;

}
