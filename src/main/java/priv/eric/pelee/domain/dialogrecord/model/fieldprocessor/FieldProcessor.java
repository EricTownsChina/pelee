package priv.eric.pelee.domain.dialogrecord.model.fieldprocessor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import priv.eric.pelee.domain.dialogrecord.model.FieldProcessType;

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

    public abstract void process(JsonNode dialogRecord);

    boolean nodeIsObjectNode(JsonNode node) {
        return null != node && !node.isMissingNode() && node instanceof ObjectNode;
    }

    ObjectNode objectNode(JsonNode node) {
        if (null != node && !node.isMissingNode() && node instanceof ObjectNode objectNode) {
            return objectNode;
        }
        return null;
    }

}
