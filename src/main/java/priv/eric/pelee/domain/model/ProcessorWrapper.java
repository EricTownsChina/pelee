package priv.eric.pelee.domain.model;

/**
 * desc:
 *
 * @author EricTownsChina@outlook.com
 * @date 2026-02-06 14:51
 */
public class ProcessorWrapper<C> {

    private final Processor processor;

    private final C config;

    public ProcessorWrapper(Processor processor, C config) {
        this.processor = processor;
        this.config = config;
    }

    public void process(Event event) {
        processor.process(event);
    }

}
