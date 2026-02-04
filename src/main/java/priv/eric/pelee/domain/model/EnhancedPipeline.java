package priv.eric.pelee.domain.model;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description: 增强的流水线模型，支持更复杂的执行模式
 *
 * @author EricTowns
 * @date 2026/02/04 00:00
 */
public class EnhancedPipeline {

    private final String pipelineId;
    private final String name;
    private final String description;
    private final PipelineType type;
    private final List<Stage> stages;
    private final EnhancedStageContext context;
    private final Map<String, Object> configuration;
    private final ExecutorService executorService;
    private volatile boolean isInitialized = false;

    public enum PipelineType {
        SEQUENTIAL,      // 顺序执行
        PARALLEL,        // 并行执行
        CONDITIONAL,     // 条件分支
        BATCH           // 批处理模式
    }

    public EnhancedPipeline(String pipelineId, String name, String description,
                          PipelineType type, List<Stage> stages,
                          EnhancedStageContext context, Map<String, Object> configuration) {
        this.pipelineId = pipelineId;
        this.name = name;
        this.description = description;
        this.type = type;
        this.stages = new ArrayList<>(stages);
        this.context = context;
        this.configuration = new HashMap<>(configuration != null ? configuration : Map.of());
        this.executorService = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r, "enhanced-pipeline-" + pipelineId);
            t.setDaemon(true);
            return t;
        });
    }

    /**
     * 初始化流水线
     */
    public synchronized void initialize() throws Exception {
        if (isInitialized) {
            return;
        }

        for (Stage stage : stages) {
            if (stage instanceof EnhancedStage enhancedStage) {
                enhancedStage.initialize(configuration, context);
            }
        }

        isInitialized = true;
    }

    /**
     * 执行流水线
     */
    public PipelineExecutionResult execute(Event<?> event) {
        if (!isInitialized) {
            throw new IllegalStateException("Pipeline not initialized");
        }

        Instant startTime = Instant.now();
        context.recordMetric("startTime", startTime.toString());

        try {
            switch (type) {
                case SEQUENTIAL:
                    return executeSequential(event);
                case PARALLEL:
                    return executeParallel(event);
                case CONDITIONAL:
                    return executeConditional(event);
                case BATCH:
                    return executeBatch(event);
                default:
                    throw new UnsupportedOperationException("Unsupported pipeline type: " + type);
            }
        } catch (Exception e) {
            context.recordMetric("error", e.getMessage());
            return PipelineExecutionResult.failure(pipelineId, e, startTime);
        } finally {
            cleanup();
        }
    }

    /**
     * 异步执行流水线
     */
    public CompletableFuture<PipelineExecutionResult> executeAsync(Event<?> event) {
        return CompletableFuture.supplyAsync(() -> execute(event), executorService);
    }

    /**
     * 顺序执行
     */
    private PipelineExecutionResult executeSequential(Event<?> event) {
        context.recordMetric("executionType", "sequential");
        
        Event<?> currentEvent = event;
        for (Stage stage : stages) {
            if (context.shouldStop()) {
                break;
            }
            
            try {
                stage.process(currentEvent, context);
            } catch (Exception e) {
                context.recordMetric("failedStage", stage.getClass().getSimpleName());
                throw e;
            }
        }

        return PipelineExecutionResult.success(pipelineId, currentEvent, 
                                              Instant.now(), context);
    }

    /**
     * 并行执行
     */
    private PipelineExecutionResult executeParallel(Event<?> event) {
        context.recordMetric("executionType", "parallel");
        
        List<CompletableFuture<Event<?>>> futures = stages.stream()
            .map(stage -> CompletableFuture.supplyAsync(() -> {
                Event<?> stageEvent = event; // 每个stage获取原始数据的副本
                stage.process(stageEvent, context);
                return stageEvent;
            }, executorService))
            .toList();

        try {
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
            );
            allFutures.join(); // 等待所有stage完成

            // 收集所有结果（这里简化处理，实际可能需要合并策略）
            Event<?> resultEvent = futures.get(0).join(); // 使用第一个结果

            return PipelineExecutionResult.success(pipelineId, resultEvent, 
                                                  Instant.now(), context);

        } catch (Exception e) {
            throw new RuntimeException("Parallel execution failed", e);
        }
    }

    /**
     * 条件执行
     */
    private PipelineExecutionResult executeConditional(Event<?> event) {
        context.recordMetric("executionType", "conditional");
        
        // 这里可以实现复杂的条件逻辑
        // 简化实现：根据配置决定执行哪些stage
        List<Stage> enabledStages = determineEnabledStages();
        
        Event<?> currentEvent = event;
        for (Stage stage : enabledStages) {
            if (context.shouldStop()) {
                break;
            }
            
            stage.process(currentEvent, context);
        }

        return PipelineExecutionResult.success(pipelineId, currentEvent, 
                                              Instant.now(), context);
    }

    /**
     * 批处理执行
     */
    private PipelineExecutionResult executeBatch(Event<?> event) {
        context.recordMetric("executionType", "batch");
        
        // 假设event包含批量数据
        List<?> batchData = extractBatchData(event);
        List<Event<?>> results = new ArrayList<>();

        for (Object item : batchData) {
            if (context.shouldStop()) {
                break;
            }

            Event<?> itemEvent = new Event<>(item);
            for (Stage stage : stages) {
                stage.process(itemEvent, context);
            }
            results.add(itemEvent);
        }

        Event<?> batchResultEvent = new Event<>(results);
        return PipelineExecutionResult.success(pipelineId, batchResultEvent, 
                                              Instant.now(), context);
    }

    /**
     * 根据配置确定启用的stage
     */
    private List<Stage> determineEnabledStages() {
        // 简化实现：返回所有stage
        // 实际可以根据configuration中的条件来过滤
        return new ArrayList<>(stages);
    }

    /**
     * 提取批量数据
     */
    @SuppressWarnings("unchecked")
    private List<?> extractBatchData(Event<?> event) {
        Object data = event.get();
        if (data instanceof List) {
            return (List<?>) data;
        }
        return List.of(data);
    }

    /**
     * 清理资源
     */
    private void cleanup() {
        for (Stage stage : stages) {
            if (stage instanceof EnhancedStage enhancedStage) {
                try {
                    enhancedStage.cleanup(context);
                } catch (Exception e) {
                    // 记录清理异常但不中断流程
                    context.recordMetric("cleanupError_" + stage.getClass().getSimpleName(), 
                                      e.getMessage());
                }
            }
        }
    }

    /**
     * 关闭流水线
     */
    public void shutdown() {
        executorService.shutdown();
        cleanup();
    }

    // Getters
    public String getPipelineId() { return pipelineId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public PipelineType getType() { return type; }
    public List<Stage> getStages() { return Collections.unmodifiableList(stages); }
    public EnhancedStageContext getContext() { return context; }
    public Map<String, Object> getConfiguration() { 
        return Collections.unmodifiableMap(configuration); 
    }

    /**
     * 流水线执行结果
     */
    public static class PipelineExecutionResult {
        private final String pipelineId;
        private final boolean success;
        private final Event<?> result;
        private final Instant endTime;
        private final Exception error;
        private final Map<String, Object> metrics;

        private PipelineExecutionResult(String pipelineId, boolean success, 
                                      Event<?> result, Instant endTime, 
                                      Exception error, Map<String, Object> metrics) {
            this.pipelineId = pipelineId;
            this.success = success;
            this.result = result;
            this.endTime = endTime;
            this.error = error;
            this.metrics = metrics;
        }

        public static PipelineExecutionResult success(String pipelineId, Event<?> result, 
                                                   Instant endTime, EnhancedStageContext context) {
            return new PipelineExecutionResult(pipelineId, true, result, endTime, 
                                             null, new HashMap<>(context.getPipelineState()));
        }

        public static PipelineExecutionResult failure(String pipelineId, Exception error, 
                                                   Instant startTime) {
            return new PipelineExecutionResult(pipelineId, false, null, startTime, 
                                             error, Map.of());
        }

        // Getters
        public String getPipelineId() { return pipelineId; }
        public boolean isSuccess() { return success; }
        public Event<?> getResult() { return result; }
        public Instant getEndTime() { return endTime; }
        public Exception getError() { return error; }
        public Map<String, Object> getMetrics() { return metrics; }
    }
}