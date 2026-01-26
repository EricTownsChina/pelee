package priv.eric.plugin.remove;

import com.fasterxml.jackson.databind.JsonNode;
import priv.eric.pelee.domain.model.Event;
import priv.eric.pelee.domain.model.Stage;
import priv.eric.pelee.domain.model.StageContext;

import java.util.List;

/**
 * Description: TODO
 *
 * @author EricTowns
 * @date 2026/1/27 01:03
 */
public class RemoveStage implements Stage<JsonNode> {

    private final List<String> fields;

    public RemoveStage(RemoveConfig config) {
        this.fields = config.getFields();
    }

    @Override
    public void process(Event<JsonNode> event, StageContext<JsonNode> context) {

        context.next(event);
    }
}
