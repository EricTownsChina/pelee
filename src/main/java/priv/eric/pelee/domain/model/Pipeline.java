package priv.eric.pelee.domain.model;

import java.util.List;
import java.util.Map;

/**
 * Description: 流水线执行器
 *
 * @author EricTowns
 * @date 2026/1/26 21:50
 */
public class Pipeline {

    protected final Map<String, Object> meta;
    private final List<Stage> stages;
    private final StageContext context;

    public Pipeline(Map<String, Object> meta, List<Stage> stages, StageContext context) {
        this.meta = meta;
        this.stages = stages;
        this.context = context;
    }

public void execute(Event<?> event) {
        // 尝试设置上下文中的阶段列表
        try {
            context.setStages(stages);
        } catch (UnsupportedOperationException e) {
            // 上下文不支持设置阶段列表，忽略
        }
        context.next(event);
    }

    public Map<String, Object> meta() {
        return this.meta;
    }
    
    public List<Stage> getStages() {
        return this.stages;
    }
    
    public StageContext getContext() {
        return this.context;
    }
}