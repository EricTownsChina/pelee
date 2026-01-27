package priv.eric.plugin.stage.remove;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Description: TODO
 *
 * @author EricTowns
 * @date 2026/1/27 01:01
 */
@Data
public class RemoveConfig {

    @JsonProperty("fields")
    private List<String> fields;

}
