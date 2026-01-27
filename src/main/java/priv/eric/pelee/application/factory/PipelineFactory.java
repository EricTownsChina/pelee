package priv.eric.pelee.application.factory;

import com.fasterxml.jackson.databind.JsonNode;
import priv.eric.pelee.domain.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description: 应用层流水线加载器，复用domain层StageFactory和Pipeline
 *
 * @author EricTowns
 * @date 2026/1/27 17:35
 */
public class PipelineFactory {

    public Pipeline create(JsonNode configNode) {
        // 直接使用配置创建流水线阶段
        JsonNode processorsNode = configNode.get("processors");
        final List<Stage> stages = new ArrayList<>();
        if (processorsNode != null && processorsNode.isArray() && !processorsNode.isEmpty()) {
            for (JsonNode node : processorsNode) {
                Stage stage = StageFactory.getInstance().create(node);
                stages.add(stage);
            }
        }

        // 上下文选择
        JsonNode contextNode = configNode.get("context");
        String contextCode = "sequence";
        if (contextNode != null && contextNode.isTextual()) {
            contextCode = contextNode.asText();
        }
        final StageContext context = StageContextFactory.getInstance().get(contextCode);
        if (context == null) {
            throw new IllegalArgumentException("Unknown context code: " + contextCode);
        }

        // 直接返回domain层的Pipeline
        return new Pipeline(Map.of(), stages, context);
    }
}