package priv.eric.pelee.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import priv.eric.pelee.application.init.PipelineInitializer;
import priv.eric.pelee.domain.model.Event;
import priv.eric.pelee.domain.model.Pipeline;

/**
 * Description: 流水线执行服务，负责根据ID执行预定义的流水线处理
 *
 * @author EricTowns
 * @date 2026/1/27 16:00
 */
@Service
public class PipelineExecutionService {

    private final PipelineInitializer pipelineInitializer;

    public PipelineExecutionService(PipelineInitializer pipelineInitializer) {
        this.pipelineInitializer = pipelineInitializer;
    }

    public Object executePipeline(String pipelineId, JsonNode data) {
        Pipeline pipeline = pipelineInitializer.get(pipelineId);
        if (pipeline == null) {
            throw new IllegalArgumentException("Pipeline not found: " + pipelineId);
        }
        // 创建事件并执行流水线
        Event event = new Event(data);
        pipeline.execute(event);
        return event.get();
    }

    public boolean exists(String pipelineId) {
        return pipelineInitializer.contains(pipelineId);
    }

}