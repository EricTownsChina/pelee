package priv.eric.pelee.domain.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

/**
 * Description: todo
 *
 * @author EricTowns
 * @date 2026/1/26 21:28
 */
public class DefaultStageContext<T> implements StageContext<T> {

    private final List<Stage<T>> stages;

    private int index = 0;

    public DefaultStageContext(List<Stage<T>> stages) {
        this.stages = stages;
    }

    @Override
    public void next(Event<T> event) {
        if (index < stages.size()) {
            stages.get(index++).process(event, this);
        }
    }

}
