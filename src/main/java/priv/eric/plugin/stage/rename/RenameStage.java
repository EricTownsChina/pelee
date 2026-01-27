package priv.eric.plugin.stage.rename;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import priv.eric.pelee.domain.model.Event;
import priv.eric.pelee.domain.model.Stage;
import priv.eric.pelee.domain.model.StageContext;

import java.util.Map;

/**
 * Description: 重命名字段阶段
 *
 * @author EricTowns
 * @date 2026/1/27 10:40
 */
public class RenameStage implements Stage {

    private final Map<String, String> mappings;

    public RenameStage(RenameConfig config) {
        this.mappings = config.getMappings();
    }

    @Override
    public void process(Event event, StageContext context) {
        if (event.get() != null) {
            ObjectNode objectNode = (ObjectNode) event.get();
            // 遍历映射关系，重命名字段
            for (Map.Entry<String, String> entry : mappings.entrySet()) {
                String oldFieldName = entry.getKey();
                String newFieldName = entry.getValue();

                if (objectNode.has(oldFieldName)) {
                    // 获取旧字段的值
                    JsonNode value = objectNode.get(oldFieldName);
                    // 移除旧字段
                    objectNode.remove(oldFieldName);
                    // 添加新字段
                    objectNode.set(newFieldName, value);
                }
            }
        }
        // 继续执行下一个阶段
        context.next(event);
    }
}