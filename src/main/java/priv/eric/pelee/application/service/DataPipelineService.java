package priv.eric.pelee.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.eric.pelee.application.registry.FilterRegistry;
import priv.eric.pelee.domain.model.DataPipeline;
import priv.eric.pelee.domain.model.core.PipelineMetadata;
import priv.eric.pelee.domain.model.filter.PipeFilter;
import priv.eric.pelee.domain.model.filter.FilterChain;
import priv.eric.pelee.infrastructure.util.JsonUtil;
import priv.eric.pelee.plugin.processor.remove.RemoveFieldPipeFilter;
import priv.eric.pelee.plugin.processor.rename.RenameFieldPipeFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Description: 数据流水线服务，基于新的DDD架构设计
 * 负责流水线的创建、执行和管理
 *
 * @author EricTowns
 * @date 2026/2/4 22:25
 */
@Service
@Slf4j
public class DataPipelineService {

    @Autowired
    private FilterRegistry filterRegistry;

    /**
     * 执行流水线处理
     *
     * @param pipelineId 流水线ID
     * @param data       输入数据
     * @return 处理结果的JSON字符串
     */
    public String executePipeline(String pipelineId, Object data) {
        try {
            // 1. 获取流水线配置
            DataPipeline pipeline = createPipelineFromConfig(pipelineId);

            // 2. 创建输入源
            priv.eric.pelee.plugin.input.http.HttpInputSource inputSource = new priv.eric.pelee.plugin.input.http.HttpInputSource(data);

            // 3. 创建输出接收器
            priv.eric.pelee.plugin.output.http.HttpOutputSink outputSink = new priv.eric.pelee.plugin.output.http.HttpOutputSink();

            // 4. 构建新的数据流水线
            DataPipeline dataPipeline = new DataPipeline(
                    pipelineId,
                    List.of(inputSource),
                    pipeline.getFilterChain(),
                    List.of(outputSink),
                    pipeline.getMetadata()
            );

            // 5. 执行流水线并等待完成
            CompletableFuture<Void> execution = dataPipeline.execute();
            execution.join(); // 等待完成

            // 6. 返回结果
            return JsonUtil.toJson(data);

        } catch (Exception e) {
            log.error("Pipeline execution failed for pipeline: {}", pipelineId, e);
            throw new RuntimeException("Pipeline execution failed: " + e.getMessage(), e);
        }
    }

    /**
     * 从配置创建流水线
     */
    private DataPipeline createPipelineFromConfig(String pipelineId) {
        // 这里应该从配置加载，现在创建示例流水线
        FilterChain filterChain = createSampleFilterChain();
        PipelineMetadata metadata = PipelineMetadata.of(
                "dialog-record-pipeline",
                "对话记录处理流水线"
        );

        return new DataPipeline(
                pipelineId,
                new ArrayList<>(), // 输入源将在执行时创建
                filterChain,
                new ArrayList<>(), // 输出接收器将在执行时创建
                metadata
        );
    }

    /**
     * 创建示例过滤器链
     */
    private FilterChain createSampleFilterChain() {
        List<PipeFilter> pipeFilters = new ArrayList<>();

// 重命名字段过滤器
        Map<String, String> renameMappings = new HashMap<>();
        renameMappings.put("userName", "user_name");
        renameMappings.put("userAge", "user_age");

        RenameFieldPipeFilter.RenameFieldConfig renameConfig = new RenameFieldPipeFilter.RenameFieldConfig();
        renameConfig.setMappings(renameMappings);

        pipeFilters.add(new RenameFieldPipeFilter(renameConfig));

        // 删除字段过滤器
        RemoveFieldPipeFilter.RemoveFieldConfig removeConfig = new RemoveFieldPipeFilter.RemoveFieldConfig();
        removeConfig.setFields(List.of("tempField", "internalId"));
        removeConfig.setEnableRemoveLog(true);

        pipeFilters.add(new RemoveFieldPipeFilter(removeConfig));

        return new FilterChain(pipeFilters);
    }

    /**
     * 检查流水线是否存在
     */
    public boolean isPipelineExists(String pipelineId) {
        // 简化实现，总是返回true
        return true;
    }

    /**
     * 获取所有可用流水线
     */
    public Map<String, Object> getAvailablePipelinesInfo() {
        Map<String, Object> result = new HashMap<>();
        result.put("pipelines", List.of("dialog-record-pipeline"));
        result.put("count", 1);
        return result;
    }
}