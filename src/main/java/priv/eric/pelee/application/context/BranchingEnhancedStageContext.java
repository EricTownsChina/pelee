package priv.eric.pelee.application.context;

import priv.eric.pelee.domain.model.Event;
import priv.eric.pelee.domain.model.EnhancedStageContext;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 支持条件分支和多路径输出的增强上下文
 *
 * @author EricTowns
 * @date 2026/02/04 00:00
 */
public class BranchingEnhancedStageContext implements EnhancedStageContext {

    private final String executionId;
    private final String pipelineId;
    private final Instant startTime;
    private final Map<String, Object> pipelineState;
    private final Map<String, Object> stageState;
    private final Map<String, Object> properties;
    private final Map<String, Object> metrics;
    private final AtomicBoolean shouldStop = new AtomicBoolean(false);
    private volatile String stopReason;
    private final Map<String, List<priv.eric.pelee.domain.model.Stage>> branches;
    private final AtomicInteger currentStageIndex = new AtomicInteger(0);
    private volatile String currentBranch = "default";
    private final Map<String, Event<?>> outputs = new ConcurrentHashMap<>();

    public BranchingEnhancedStageContext(String executionId, String pipelineId) {
        this.executionId = executionId;
        this.pipelineId = pipelineId;
        this.startTime = Instant.now();
        this.branches = new ConcurrentHashMap<>();
        this.pipelineState = new ConcurrentHashMap<>();
        this.stageState = new ConcurrentHashMap<>();
        this.properties = new ConcurrentHashMap<>();
        this.metrics = new ConcurrentHashMap<>();
        
        // 初始化默认分支
        this.branches.put("default", new ArrayList<>());
    }

    @Override
    public String code() {
        return "branching-enhanced";
    }

    @Override
    public void next(Event<?> event) {
        if (shouldStop.get()) {
            return;
        }

        List<priv.eric.pelee.domain.model.Stage> currentBranchStages = branches.get(currentBranch);
        if (currentBranchStages == null) {
            throw new IllegalStateException("Unknown branch: " + currentBranch);
        }

        int index = currentStageIndex.get();
        if (index < currentBranchStages.size()) {
            priv.eric.pelee.domain.model.Stage stage = currentBranchStages.get(index);
            currentStageIndex.incrementAndGet();
            
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
        List<priv.eric.pelee.domain.model.Stage> currentBranchStages = branches.get(currentBranch);
        if (currentBranchStages == null) {
            return Optional.empty();
        }

        int index = currentStageIndex.get() - 1;
        if (index >= 0 && index < currentBranchStages.size()) {
            int finalIndex = index;
            String finalCurrentBranch = currentBranch;
            return Optional.of(new StageInfo() {
                @Override
                public String getName() {
                    return currentBranchStages.get(finalIndex).getClass().getSimpleName();
                }

                @Override
                public int getIndex() {
                    return finalIndex;
                }

                @Override
                public Map<String, Object> getProperties() {
                    return Map.of(
                        "stageClass", currentBranchStages.get(finalIndex).getClass().getName(),
                        "pipelineId", pipelineId,
                        "executionId", executionId,
                        "branch", finalCurrentBranch
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

    @Override
    public void sendToOutput(String outputName, Event<?> event) {
        outputs.put(outputName, event);
    }

    /**
     * 获取特定输出的数据
     */
    public Optional<Event<?>> getOutput(String outputName) {
        return Optional.ofNullable(outputs.get(outputName));
    }

    /**
     * 获取所有输出
     */
    public Map<String, Event<?>> getAllOutputs() {
        return Collections.unmodifiableMap(outputs);
    }

    /**
     * 添加分支
     */
    public void addBranch(String branchName, List<priv.eric.pelee.domain.model.Stage> stages) {
        branches.put(branchName, new ArrayList<>(stages));
    }

    /**
     * 切换到指定分支
     */
    public void switchBranch(String branchName) {
        if (!branches.containsKey(branchName)) {
            throw new IllegalArgumentException("Unknown branch: " + branchName);
        }
        this.currentBranch = branchName;
        this.currentStageIndex.set(0);
    }

    /**
     * 获取当前分支
     */
    public String getCurrentBranch() {
        return currentBranch;
    }

    /**
     * 获取所有分支名称
     */
    public Set<String> getBranchNames() {
        return Collections.unmodifiableSet(branches.keySet());
    }

    /**
     * 条件执行分支
     */
    public boolean executeIf(String condition, Runnable action) {
        // 简单的条件实现，实际可以根据需要扩展
        boolean result = evaluateCondition(condition);
        if (result) {
            action.run();
        }
        return result;
    }

    private boolean evaluateCondition(String condition) {
        // 这里可以实现复杂的条件评估逻辑
        // 比如基于pipelineState、properties等
        return Boolean.parseBoolean(condition);
    }

    /**
     * 获取执行摘要
     */
    public Map<String, Object> getExecutionSummary() {
        return Map.of(
            "executionId", executionId,
            "pipelineId", pipelineId,
            "startTime", startTime.toString(),
            "currentBranch", currentBranch,
            "currentStageIndex", currentStageIndex.get(),
            "branches", branches.keySet(),
            "outputs", outputs.keySet(),
            "shouldStop", shouldStop.get(),
            "stopReason", stopReason,
            "metrics", new HashMap<>(metrics),
            "properties", new HashMap<>(properties)
        );
    }
}