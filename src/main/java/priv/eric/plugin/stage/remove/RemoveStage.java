package priv.eric.plugin.stage.remove;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import priv.eric.pelee.domain.model.Event;
import priv.eric.pelee.domain.model.Stage;
import priv.eric.pelee.domain.model.StageContext;

import java.util.List;

/**
 * Description: 删除字段阶段
 *
 * @author EricTowns
 * @date 2026/1/27 01:03
 */
public class RemoveStage implements Stage {

    private final List<String> fields;

    public RemoveStage(RemoveConfig config) {
        this.fields = config.getFields();
    }

    @Override
    public void process(Event event, StageContext context) {
        if (event.get() != null) {
            ObjectNode objectNode = (ObjectNode) event.get();
            for (String field : fields) {
                objectNode.remove(field);
            }
        }
        // 继续执行下一个阶段
        context.next(event);
    }
}