package priv.eric.pelee.application.factory;

import org.springframework.stereotype.Component;
import priv.eric.pelee.application.init.ProcessorRegistry;
import priv.eric.pelee.domain.model.Processor;
import priv.eric.pelee.domain.model.ProcessorWrapper;
import priv.eric.pelee.infrastructure.util.JsonUtil;
import priv.eric.pelee.plugin.ProcessorMetadata;

/**
 * desc:
 *
 * @author EricTownsChina@outlook.com
 * @date 2026-02-05 19:58
 */
@Component
public class ProcessorFactory {

    private final ProcessorRegistry registry;

    public ProcessorFactory(ProcessorRegistry registry) {
        this.registry = registry;
    }

    public <C> ProcessorWrapper<C> create(String type, Object rawConfig) {
        Processor processor = registry.getProcessor(type);
        ProcessorMetadata metadata = registry.getMeta(type);

        C config = null;
        if (metadata.hasConfig() && rawConfig != null) {
            config = JsonUtil.convertValue(rawConfig, (Class<C>) metadata.getConfigClass());
        }
        return new ProcessorWrapper<>(processor, config);
    }

}
