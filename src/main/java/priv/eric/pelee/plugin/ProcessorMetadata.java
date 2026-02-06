package priv.eric.pelee.plugin;

import lombok.Getter;
import priv.eric.pelee.domain.model.Processor;

/**
 * desc:
 *
 * @author EricTownsChina@outlook.com
 * @date 2026-02-06 14:08
 */
@Getter
public class ProcessorMetadata {

    private final String type;

    private final String description;

    private final String author;

    private final Class<? extends Processor> processorClass;

    private final Class<?> configClass;

    public ProcessorMetadata(Processor processor) {
        ProcessorDescriptor descriptor = processor.getClass().getAnnotation(ProcessorDescriptor.class);
        if (null == descriptor) {
            throw new IllegalArgumentException(processor.getClass() + " 缺少@ProcessorDescriptor描述注解");
        }
        this.type = descriptor.type();
        this.description = descriptor.description();
        this.author = descriptor.author();
        this.processorClass = processor.getClass();
        this.configClass = descriptor.configClass();
    }

    public boolean hasConfig() {
        return configClass != Void.class;
    }

}
