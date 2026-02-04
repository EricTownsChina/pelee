package priv.eric.pelee.domain.model;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Description: 数据流水线，基于Logstash模式的DDD设计
 * 负责编排InputSource、FilterChain、OutputSink的执行
 *
 * @author EricTowns
 * @date 2026/2/4 21:55
 */
public class DataPipeline {

    private final String id;
    private final List<InputSource> inputSources;
    private final FilterChain filterChain;
    private final List<OutputSink> outputSinks;
    private final PipelineMetadata metadata;

    /**
     * 构造函数
     */
    public DataPipeline(String id, List<InputSource> inputSources, 
                     FilterChain filterChain, List<OutputSink> outputSinks, 
                     PipelineMetadata metadata) {
        this.id = id;
        this.inputSources = inputSources;
        this.filterChain = filterChain;
        this.outputSinks = outputSinks;
        this.metadata = metadata;
    }

    /**
     * 执行流水线处理
     * 
     * @return 完成的Future，表示处理结果
     */
    public CompletableFuture<Void> execute() {
        CompletableFuture<Void> result = new CompletableFuture<>();
        
        // 处理所有输入源
        List<CompletableFuture<PipelineEvent>> inputFutures = inputSources.stream()
                .map(InputSource::getInput)
                .toList();

        // 当所有输入完成后，进行处理
        CompletableFuture.allOf(inputFutures.toArray(new CompletableFuture[0]))
                .thenCompose(v -> {
                    // 处理每个输入事件
                    List<CompletableFuture<Void>> outputFutures = inputFutures.stream()
                            .map(inputFuture -> inputFuture.thenCompose(event -> {
                                if (event != null) {
                                    // 通过过滤器链处理事件
                                    PipelineEvent processedEvent = filterChain.process(event);
                                    
                                    // 发送到所有输出接收器
                                    return sendToAllOutputs(processedEvent);
                                }
                                return CompletableFuture.completedFuture(null);
                            }))
                            .toList();

                    // 等待所有输出完成
                    return CompletableFuture.allOf(outputFutures.toArray(new CompletableFuture[0]));
                })
                .thenRun(() -> result.complete(null))
                .exceptionally(throwable -> {
                    result.completeExceptionally(throwable);
                    return null;
                });

        return result;
    }

    /**
     * 发送事件到所有输出接收器
     */
    private CompletableFuture<Void> sendToAllOutputs(PipelineEvent event) {
        if (event == null) {
            return CompletableFuture.completedFuture(null);
        }

        List<CompletableFuture<Void>> outputFutures = outputSinks.stream()
                .map(outputSink -> outputSink.sendOutput(event))
                .toList();

        return CompletableFuture.allOf(outputFutures.toArray(new CompletableFuture[0]));
    }

    /**
     * 获取流水线ID
     */
    public String getId() {
        return id;
    }

    /**
     * 获取输入源列表
     */
    public List<InputSource> getInputSources() {
        return inputSources;
    }

    /**
     * 获取过滤器链
     */
    public FilterChain getFilterChain() {
        return filterChain;
    }

    /**
     * 获取输出接收器列表
     */
    public List<OutputSink> getOutputSinks() {
        return outputSinks;
    }

    /**
     * 获取元数据
     */
    public PipelineMetadata getMetadata() {
        return metadata;
    }

    /**
     * 判断是否为空流水线
     */
    public boolean isEmpty() {
        return inputSources.isEmpty() || outputSinks.isEmpty();
    }
}