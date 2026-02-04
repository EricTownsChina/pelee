package priv.eric.pelee.application.context;

import priv.eric.pelee.domain.model.EnhancedStageContext;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 增强阶段上下文的具体实现
 *
 * @author EricTowns
 * @date 2026/02/04 00:00
 */
public class DefaultEnhancedStageContext implements EnhancedStageContext {

    private final String executionId;
    private final String pipelineId;
    private final Instant startTime;
    private final Map<String, Object> pipelineState;
    private final Map<String, Object> stageState;
    private final Map<String, Object> properties;
    private final Map<String, Object> metrics;
    private final AtomicBoolean shouldStop = new AtomicBoolean(false);
    private volatile String stopReason;
    private final List<priv.eric.pelee.domain.model.Stage> stages;
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    public DefaultEnhancedStageContext(String executionId, String pipelineId, 
                                     List<priv.eric.pelee.domain.model.Stage> stages) {
        this.executionId = executionId;
        this.pipelineId = pipelineId;
        this.startTime = Instant.now();
        this.stages = new ArrayList<>(stages);
        this.pipelineState = new ConcurrentHashMap<>();
        this.stageState = new ConcurrentHashMap<>();
        this.properties = new ConcurrentHashMap<>();
        this.metrics = new ConcurrentHashMap<>();
    }

    @Override
    public String code() {
        return "enhanced-sequence";
    }

    @Override
    public void next(Event<?> event) {
        if (shouldStop.get()) {
            return;
        }

        int index = currentIndex.get();
        if (index < stages.size()) {
            priv.eric.pelee.domain.model.Stage stage = stages.get(index);
            currentIndex.incrementAndGet();
            
            try {
                stage.process(event, this);
            } catch (Exception e) {
                requestStop("Stage processing failed: " + e.getMessage());
                throw new RuntimeException("Pipeline execution stopped due to error", e);
            }
        }
    }

    @Override
    public Map<String, Object> getPipelineState() {
        return Collections.unmodifiableMap(pipelineState);
    }

    @Override
    public Map<String, Object> getStageState() {
        return Collections.unmodifiableMap(stageState);
    }

    @Override
    public Optional<StageInfo> getCurrentStage() {
        int index = currentIndex.get() - 1; // 当前正在执行的stage
        if (index >= 0 && index < stages.size()) {
            int finalIndex = index;
            return Optional.of(new StageInfo() {
                @Override
                public String getName() {
                    return stages.get(finalIndex).getClass().getSimpleName();
                }

                @Override
                public int getIndex() {
                    return finalIndex;
                }

                @Override
                public Map<String, Object> getProperties() {
                    return Map.of(
                        "stageClass", stages.get(finalIndex).getClass().getName(),
                        "pipelineId", pipelineId,
                        "executionId", executionId
                    );
                }
            });
        }
        return Optional.empty();
    }

    @Override
    public String getExecutionId() {
        return executionId;
    }

    @Override
    public Instant getStartTime() {
        return startTime;
    }

    @Override
    public void recordMetric(String metricName, Object value) {
        metrics.put(metricName, value);
    }

    @Override
    public Optional<Object> getMetric(String metricName) {
        return Optional.ofNullable(metrics.get(metricName));
    }

    @Override
    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    @Override
    public Optional<Object> getProperty(String key) {
        return Optional.ofNullable(properties.get(key));
    }

    @Override
    public boolean shouldStop() {
        return shouldStop.get();
    }

    @Override
    public void requestStop(String reason) {
        this.stopReason = reason;
        this.shouldStop.set(true);
    }

    @Override
    public Optional<String> getStopReason() {
        return Optional.ofNullable(stopReason);
    }

    /**
     * 获取流水线ID
     */
    public String getPipelineId() {
        return pipelineId;
    }

    /**
     * 获取所有阶段
     */
    public List<priv.eric.pelee.domain.model.Stage> getStages() {
        return Collections.unmodifiableList(stages);
    }

    /**
     * 获取当前阶段索引
     */
    public int getCurrentIndex() {
        return currentIndex.get();
    }

    /**
     * 重置执行状态（用于重试场景）
     */
    public void reset() {
        currentIndex.set(0);
        shouldStop.set(false);
        stopReason = null;
        stageState.clear();
        metrics.clear();
    }

    /**
     * 获取执行摘要
     */
    public Map<String, Object> getExecutionSummary() {
        return Map.of(
            "executionId", executionId,
            "pipelineId", pipelineId,
            "startTime", startTime.toString(),
            "totalStages", stages.size(),
            "completedStages", currentIndex.get(),
            "shouldStop", shouldStop.get(),
            "stopReason", stopReason,
            "metrics", new HashMap<>(metrics),
            "properties", new HashMap<>(properties)
        );
    }
}