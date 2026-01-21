package priv.eric.pelee.domain.dialogrecord.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 * Description: TODO
 *
 * @author EricTowns
 * @date 2026/1/18 20:43
 */
@Data
public abstract class FieldProcessor {

    public final FieldProcessType type;

    public FieldProcessor() {
       this.type = type();
    }

    abstract FieldProcessType type();

    abstract void process(JsonNode dialogRecord);

}
