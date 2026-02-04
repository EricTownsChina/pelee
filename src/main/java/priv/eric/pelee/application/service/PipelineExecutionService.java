package priv.eric.pelee.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.eric.pelee.domain.model.Event;
import priv.eric.pelee.domain.model.EnhancedPipeline;
import priv.eric.pelee.domain.model.Pipeline;
import priv.eric.pelee.domain.model.EnhancedStageContext;
import priv.eric.pelee.application.context.DefaultEnhancedStageContext;
import priv.eric.pelee.infrastructure.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

    /**
     * 执行指定ID的流水线处理
     * @param pipelineId 流水线ID
     * @param data 输入数据对象
     * @return 处理后的结果JSON字符串
     */
    public String executePipeline(String pipelineId, Object data) {
        Pipeline pipeline = pipelineRegistry.getPipelineById(pipelineId);
        if (pipeline == null) {
            throw new IllegalArgumentException("Pipeline not found: " + pipelineId);
        }
        
        // 转换为JsonNode
        JsonNode inputData = JsonUtil.parseToJsonNode(JsonUtil.toJson(data));
        
        // 创建事件并执行流水线
        Event<JsonNode> event = new Event<>(inputData);
        pipeline.execute(event);

        return JsonUtil.toJson(event.get());
    }

    /**
     * 执行增强流水线
     * @param pipelineId 流水线ID
     * @param data 输入数据对象
     * @param executionContext 执行上下文配置
     * @return 包含结果和执行信息的响应
     */
    public Map<String, Object> executeEnhancedPipeline(String pipelineId, Object data, 
                                                        Map<String, Object> executionContext) {
        Pipeline pipeline = pipelineRegistry.getPipelineById(pipelineId);
        if (pipeline == null) {
            throw new IllegalArgumentException("Pipeline not found: " + pipelineId);
        }

        // 转换为JsonNode
        JsonNode inputData = JsonUtil.parseToJsonNode(JsonUtil.toJson(data));
        Event<JsonNode> event = new Event<>(inputData);

        try {
            // 如果是增强流水线，使用增强执行
            if (pipeline instanceof EnhancedPipeline) {
                EnhancedPipeline enhancedPipeline = (EnhancedPipeline) pipeline;
                
                // 初始化流水线
                enhancedPipeline.initialize();
                
                // 执行流水线
                EnhancedPipeline.PipelineExecutionResult result = enhancedPipeline.execute(event);
                
                return Map.of(
                    "success", result.isSuccess(),
                    "result", result.isSuccess() ? JsonUtil.toJson(result.getResult()) : null,
                    "metrics", result.getMetrics(),
                    "endTime", result.getEndTime().toString(),
                    "error", result.getError() != null ? result.getError().getMessage() : null,
                    "executionContext", executionContext
                );
            } else {
                // 传统流水线执行
                pipeline.execute(event);
                return Map.of(
                    "success", true,
                    "result", JsonUtil.toJson(event.get()),
                    "executionContext", executionContext
                );
            }
        } catch (Exception e) {
            return Map.of(
                "success", false,
                "error", e.getMessage(),
                "executionContext", executionContext
            );
        }
    }

    /**
     * 异步执行增强流水线
     */
    public CompletableFuture<Map<String, Object>> executeEnhancedPipelineAsync(String pipelineId, Object data, 
                                                                               Map<String, Object> executionContext) {
        return CompletableFuture.supplyAsync(() -> 
            executeEnhancedPipeline(pipelineId, data, executionContext)
        );
    }

    /**
     * 创建增强流水线实例
     */
    public EnhancedPipeline createEnhancedPipeline(String pipelineId, String name, String description,
                                                  EnhancedPipeline.PipelineType type,
                                                  Map<String, Object> configuration) {
        Pipeline pipeline = pipelineRegistry.getPipelineById(pipelineId);
        if (pipeline == null) {
            throw new IllegalArgumentException("Pipeline not found: " + pipelineId);
        }

        String executionId = UUID.randomUUID().toString();
        EnhancedStageContext context = new DefaultEnhancedStageContext(
            executionId, pipelineId, pipeline.getStages()
        );

        return new EnhancedPipeline(pipelineId, name, description, type, 
                                   pipeline.getStages(), context, configuration);
    }

    public boolean isPipelineExists(String pipelineId) {
        return pipelineRegistry.containsPipeline(pipelineId);
    }

    public Map<String, Pipeline> getAllPipelines() {
        return pipelineRegistry.getAllPipelines();
    }

    /**
     * 获取可用流水线列表信息
     * @return 包含流水线ID和数量的信息
     */
    public Map<String, Object> getAvailablePipelinesInfo() {
        Map<String, Pipeline> allPipelines = getAllPipelines();
        Map<String, Object> result = new HashMap<>();
        result.put("pipelines", allPipelines.keySet());
        result.put("count", allPipelines.size());
        return result;
    }

}