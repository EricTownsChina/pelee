package priv.eric.pelee.plugin.processor;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;
import priv.eric.pelee.domain.model.Processor;
import priv.eric.pelee.domain.model.ProcessorDescriptor;

/**
 * desc:
 *
 * @author EricTownsChina@outlook.com
 * @date 2026-02-05 20:08
 */
@Component
public class RemoveDescriptor implements ProcessorDescriptor<ObjectNode> {

    @Override
    public String type() {
        return "remove";
    }

    @Override
    public String desc() {
        return "删除字段";
    }

    @Override
    public Class<?> configClass() {
        return RemoveConfig.class;
    }

    @Override
    public Processor<ObjectNode> create(Object config) {
        RemoveConfig removeConfig = (RemoveConfig) config;
        return new RemoveProcessor(removeConfig.getFields());
    }
}
