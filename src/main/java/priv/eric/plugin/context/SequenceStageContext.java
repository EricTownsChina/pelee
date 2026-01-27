package priv.eric.plugin.context;

import priv.eric.pelee.domain.model.Event;
import priv.eric.pelee.domain.model.Stage;
import priv.eric.pelee.domain.model.StageContext;

import java.util.List;

/**
 * Description: 顺序执行
 *
 * @author EricTowns
 * @date 2026/1/26 21:28
 */
public class SequenceStageContext implements StageContext {

    private List<Stage> stages;
    private int index = 0;

    public SequenceStageContext() {
        // 无参构造函数，用于SPI加载
    }

    public SequenceStageContext(List<Stage> stages) {
        this.stages = stages;
    }

    @Override
    public String code() {
        return "sequence";
    }

    @Override
    public void next(Event event) {
        if (stages != null && index < stages.size()) {
            stages.get(index++).process(event, this);
        }
    }

    public void setStages(List<Stage> stages) {
        this.stages = stages;
        this.index = 0; // 重置索引
    }

    public List<Stage> getStages() {
        return this.stages;
    }
}