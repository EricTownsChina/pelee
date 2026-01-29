package priv.eric.pelee.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.eric.pelee.domain.model.Event;
import priv.eric.pelee.domain.model.Pipeline;

import java.util.Map;

/**
 * Description: 流水线执行服务，负责根据ID执行预定义的流水线处理
 *
 * @author EricTowns
 * @date 2026/1/27 16:00
 */
@Service
public class PipelineExecutionService {

    @Autowired
    private priv.eric.pelee.application.registry.PipelineRegistry pipelineRegistry;

    public Object executePipeline(String pipelineId, JsonNode data) {
        Pipeline pipeline = pipelineRegistry.getPipelineById(pipelineId);
        if (pipeline == null) {
            throw new IllegalArgumentException("Pipeline not found: " + pipelineId);
        }
        
        // 创建事件并执行流水线
        Event event = new Event(data);
        pipeline.execute(event);

        return event.get(); // 返回处理后的数据
    }

    public boolean isPipelineExists(String pipelineId) {
        return pipelineRegistry.containsPipeline(pipelineId);
    }

    public Map<String, Pipeline> getAllPipelines() {
        return pipelineRegistry.getAllPipelines();
    }

}