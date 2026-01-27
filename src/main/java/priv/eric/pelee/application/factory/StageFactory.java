package priv.eric.pelee.application.factory;

import com.fasterxml.jackson.databind.JsonNode;
import priv.eric.pelee.domain.model.Stage;
import priv.eric.pelee.domain.model.StageDescriptor;
import priv.eric.pelee.infrastructure.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Description: 加载所有插件实现类，包括StageDescriptor等
 *
 * @author EricTowns
 * @date 2026/1/27 15:00
 */
public class StageFactory {

    private static volatile StageFactory instance;

    private final Map<String, StageDescriptor> stageDescriptorMap;

    private StageFactory() {
        this.stageDescriptorMap = load();
    }

    public static StageFactory getInstance() {
        if (instance == null) {
            synchronized (StageFactory.class) {
                if (instance == null) {
                    instance = new StageFactory();
                }
            }
        }
        return instance;
    }

    public Stage create(JsonNode stageNode) {
        String code = stageNode.get("code").asText();
        StageDescriptor descriptor = this.stageDescriptorMap.get(code);
        if (descriptor == null) {
            throw new IllegalArgumentException("Invalid stage code: " + code);
        }
        Object configClass = JsonUtil.convertValue(stageNode, descriptor.configClass());
        return descriptor.create(configClass);
    }

    public Stage create(StageDescriptor descriptor, JsonNode stageNode) {
        Object configClass = JsonUtil.convertValue(stageNode, descriptor.configClass());
        return descriptor.create(configClass);
    }

    private StageDescriptor get(String code) {
        return this.stageDescriptorMap.get(code);
    }

    private Map<String, StageDescriptor> load() {
        ServiceLoader<StageDescriptor> loader = ServiceLoader.load(StageDescriptor.class);
        final Map<String, StageDescriptor> descriptorMap = new HashMap<>();
        for (StageDescriptor descriptor : loader) {
            String code = descriptor.code();
            if (null == code || code.isEmpty()) {
                continue;
            }
            descriptorMap.put(code, descriptor);
        }
        return descriptorMap;
    }

}