package priv.eric.pelee.domain.model;

import java.util.List;
import java.util.Map;

/**
 * Description: todo
 *
 * @author EricTowns
 * @date 2026/1/26 21:50
 */
public abstract class Pipeline<T> {

    private final Map<String, Object> meta;

    private final List<Stage<T>> stages;

    public Pipeline(Map<String, Object> meta, List<Stage<T>> stages) {
        this.meta = meta;
        this.stages = stages;
    }

    abstract Map<String, Object> meta();

    public void execute(Event<T> event) {
        new DefaultStageContext<>(stages).next(event);
    }
}
