package priv.eric.pelee.domain.model;

import com.fasterxml.jackson.databind.JsonNode;
import priv.eric.pelee.infrastructure.util.JsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: Stage工厂
 *
 * @author EricTowns
 * @date 2026/1/26 21:14
 */
public class StageFactory {

    private final Map<String, StageDescriptor> registry = new HashMap<>();

    public StageFactory(List<StageDescriptor> descriptors) {
        for (StageDescriptor descriptor : descriptors) {
            registry.put(descriptor.type(), descriptor);
        }
    }

    public Stage create(JsonNode stageNode) {
        String type = stageNode.get("type").asText();
        StageDescriptor descriptor = registry.get(type);
        if (descriptor == null) {
            throw new IllegalArgumentException("Invalid stage type: " + type);
        }
        Object configClass = JsonUtil.convertValue(stageNode, descriptor.configClass());
        return descriptor.create(configClass);
    }
}
