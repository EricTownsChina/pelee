package priv.eric.pelee.domain.model;

import java.util.List;

/**
 * desc:
 *
 * @author EricTownsChina@outlook.com
 * @date 2026-02-05 19:33
 */
public class Pipeline {

    private final List<Processor<?>> processors;

    public Pipeline(List<Processor<?>> processors) {
        this.processors = processors;
    }

    public <T> void execute(Event<T> event) {
        for (Processor processor : processors) {
            processor.execute(event);
        }
    }

}
