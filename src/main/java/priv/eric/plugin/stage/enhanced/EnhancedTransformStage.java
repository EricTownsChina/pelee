package priv.eric.plugin.stage.enhanced;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import priv.eric.pelee.domain.model.EnhancedStage;
import priv.eric.pelee.domain.model.Event;
import priv.eric.pelee.domain.model.EnhancedStageContext;
import priv.eric.pelee.infrastructure.util.JsonUtil;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 增强的数据转换Stage，展示新架构的能力
 *
 * @author EricTowns
 * @date 2026/02/04 00:00
 */
public class EnhancedTransformStage implements EnhancedStage {

    private String stageName;
    private Map<String, Object> configuration;
    private Map<String, Object> fieldMappings;
    private boolean enableValidation;
    private int processedCount = 0;
    private int errorCount = 0;

    @Override
    public void initialize(Map<String, Object> config, EnhancedStageContext context) 
            throws StageInitializationException {
        this.configuration = new HashMap<>(config);
        this.stageName = (String) config.getOrDefault("stageName", "EnhancedTransform");
        this.fieldMappings = (Map<String, Object>) config.getOrDefault("fieldMappings", Map.of());
        this.enableValidation = (Boolean) config.getOrDefault("enableValidation", true);

        // 记录初始化指标
        context.recordMetric(stageName + ".initializedAt", Instant.now().toString());
        context.setProperty(stageName + ".fieldCount", fieldMappings.size());
        
        // 在流水线状态中设置共享配置
        context.getPipelineState().put("transformStage", stageName);
    }

    @Override
    public void validate(Event<?> event, EnhancedStageContext context) 
            throws StageValidationException {
        if (event == null || event.get() == null) {
            throw new StageValidationException("Event or event data cannot be null");
        }

        if (!(event.get() instanceof JsonNode)) {
            throw new StageValidationException("Event data must be JsonNode");
        }

        JsonNode data = (JsonNode) event.get();
        if (enableValidation && !data.isObject()) {
            throw new StageValidationException("Event data must be a JSON object");
        }

        // 记录验证指标
        context.recordMetric(stageName + ".validations", 
            (context.getMetric(stageName + ".validations").map(v -> (Integer) v + 1).orElse(1)));
    }

    @Override
    public void processEnhanced(Event<?> event, EnhancedStageContext context) 
            throws StageProcessingException {
        try {
            JsonNode data = (JsonNode) event.get();
            ObjectNode result = transformData((ObjectNode) data, context);
            
            event.set(result);
            processedCount++;

            // 更新处理指标
            context.recordMetric(stageName + ".processedCount", processedCount);
            context.getStageState().put("lastProcessedAt", Instant.now().toString());
            
            // 检查是否需要停止处理
            if (processedCount >= getMaxProcessCount(context)) {
                context.requestStop("Maximum process count reached: " + getMaxProcessCount(context));
            }

            // 继续执行下一个阶段
            context.next(event);

        } catch (Exception e) {
            errorCount++;
            context.recordMetric(stageName + ".errorCount", errorCount);
            throw new StageProcessingException("Data transformation failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void flush(EnhancedStageContext context) throws StageFlushException {
        // 批处理场景下的刷新操作
        context.recordMetric(stageName + ".flushedAt", Instant.now().toString());
        context.getStageState().put("totalProcessed", processedCount);
        context.getStageState().put("totalErrors", errorCount);
    }

    @Override
    public void cleanup(EnhancedStageContext context) throws StageCleanupException {
        // 清理资源和记录最终统计
        Map<String, Object> finalStats = new HashMap<>();
        finalStats.put("processedCount", processedCount);
        finalStats.put("errorCount", errorCount);
        finalStats.put("errorRate", processedCount > 0 ? (double) errorCount / processedCount : 0.0);
        finalStats.put("cleanedUpAt", Instant.now().toString());

        context.recordMetric(stageName + ".finalStats", finalStats);
        
        // 从流水线状态中移除
        context.getPipelineState().remove("transformStage");
    }

    @Override
    public StageHealth getHealth(EnhancedStageContext context) {
        double errorRate = processedCount > 0 ? (double) errorCount / processedCount : 0.0;
        
        if (errorRate > 0.1) { // 错误率超过10%
            return StageHealth.UNHEALTHY;
        } else if (errorRate > 0.05) { // 错误率超过5%
            return StageHealth.DEGRADED;
        } else {
            return StageHealth.HEALTHY;
        }
    }

    @Override
    public Map<String, Object> getStatistics(EnhancedStageContext context) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("processedCount", processedCount);
        stats.put("errorCount", errorCount);
        stats.put("errorRate", processedCount > 0 ? (double) errorCount / processedCount : 0.0);
        stats.put("stageName", stageName);
        stats.put("configuration", configuration);
        
        // 添加上下文中的指标
        context.getMetric(stageName + ".processedCount").ifPresent(v -> stats.put("contextProcessedCount", v));
        context.getMetric(stageName + ".errorCount").ifPresent(v -> stats.put("contextErrorCount", v));
        
        return stats;
    }

    @Override
    public String getStageName() {
        return stageName;
    }

    /**
     * 执行数据转换
     */
    private ObjectNode transformData(ObjectNode data, EnhancedStageContext context) {
        ObjectNode result = data.deepCopy();

        // 执行字段映射
        for (Map.Entry<String, Object> mapping : fieldMappings.entrySet()) {
            String sourceField = mapping.getKey();
            Object targetInfo = mapping.getValue();

            if (targetInfo instanceof String) {
                String targetField = (String) targetInfo;
                if (result.has(sourceField)) {
                    JsonNode value = result.get(sourceField);
                    result.remove(sourceField);
                    result.set(targetField, value);
                }
            } else if (targetInfo instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> complexMapping = (Map<String, Object>) targetInfo;
                applyComplexMapping(result, sourceField, complexMapping, context);
            }
        }

        // 添加元数据
        ObjectNode metadata = result.putObject("_metadata");
        metadata.put("processedBy", stageName);
        metadata.put("processedAt", Instant.now().toString());
        metadata.put("executionId", context.getExecutionId());

        return result;
    }

    /**
     * 应用复杂映射
     */
    @SuppressWarnings("unchecked")
    private void applyComplexMapping(ObjectNode data, String sourceField, 
                                   Map<String, Object> mapping, EnhancedStageContext context) {
        String targetField = (String) mapping.get("target");
        String transformType = (String) mapping.getOrDefault("transform", "copy");

        if (!data.has(sourceField)) {
            return;
        }

        JsonNode value = data.get(sourceField);

        switch (transformType) {
            case "copy":
                data.set(targetField, value);
                break;
            case "uppercase":
                if (value.isTextual()) {
                    data.put(targetField, value.asText().toUpperCase());
                }
                break;
            case "lowercase":
                if (value.isTextual()) {
                    data.put(targetField, value.asText().toLowerCase());
                }
                break;
            case "jsonString":
                data.put(targetField, JsonUtil.toJson(value));
                break;
            default:
                data.set(targetField, value);
        }

        if (!sourceField.equals(targetField)) {
            data.remove(sourceField);
        }
    }

    /**
     * 获取最大处理次数
     */
    private int getMaxProcessCount(EnhancedStageContext context) {
        return context.getProperty("maxProcessCount")
                .map(v -> Integer.parseInt(v.toString()))
                .orElse(Integer.MAX_VALUE);
    }
}