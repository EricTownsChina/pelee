package priv.eric.pelee.application.factory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;
import priv.eric.pelee.domain.model.Processor;
import priv.eric.pelee.domain.model.ProcessorDescriptor;
import priv.eric.pelee.infrastructure.util.JsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * desc:
 *
 * @author EricTownsChina@outlook.com
 * @date 2026-02-05 19:58
 */
@Component
public class ProcessorFactory {

    private final Map<String, ProcessorDescriptor<?>> registry = new HashMap<>();

    public ProcessorFactory(List<ProcessorDescriptor<?>> descriptors) {
        for (ProcessorDescriptor<?> descriptor : descriptors) {
            registry.put(descriptor.type(), descriptor);
        }
    }

    public Processor<?> create(ObjectNode node) {
        String type = node.get("type").asText();
        ProcessorDescriptor<?> descriptor = registry.get(type);
        if (descriptor == null) {
            throw new IllegalArgumentException("Invalid stage type: " + type);
        }
        Object configClass = JsonUtil.convertValue(node, descriptor.configClass());
        return descriptor.create(configClass);
    }

}
