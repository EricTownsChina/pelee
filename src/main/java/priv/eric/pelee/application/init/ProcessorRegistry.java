package priv.eric.pelee.application.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import priv.eric.pelee.domain.model.Processor;
import priv.eric.pelee.plugin.ProcessorMetadata;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * desc:
 *
 * @author EricTownsChina@outlook.com
 * @date 2026-02-06 14:02
 */
@Component
public class ProcessorRegistry implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorRegistry.class);

    private final List<Processor> processors;

    private final Map<String, Processor> processorRegistry = new ConcurrentHashMap<>(0);

    private final Map<String, ProcessorMetadata> metadataRegistry = new ConcurrentHashMap<>(0);

    public ProcessorRegistry(List<Processor> processors) {
        this.processors = processors;
    }

    @Override
    public void afterPropertiesSet() {
        processors.forEach(p -> {
            final ProcessorMetadata metadata = new ProcessorMetadata(p);
            final String type = metadata.getType();
            processorRegistry.put(type, p);
            metadataRegistry.put(type, metadata);
            LOGGER.info("===== register processor: {}", type);
        });
        if (metadataRegistry.isEmpty()) {
            LOGGER.warn("===== registry is empty");
        }
    }

    public Processor getProcessor(String type) {
        Processor processor = processorRegistry.get(type);
        if (null == processor) {
            throw new IllegalArgumentException("未知的处理器: " + type);
        }
        return processor;
    }

    public ProcessorMetadata getMeta(String type) {
        ProcessorMetadata metadata = metadataRegistry.get(type);
        if (null == metadata) {
            throw new IllegalArgumentException("未知的处理器描述: " + type);
        }
        return metadata;
    }

}
