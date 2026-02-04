package priv.eric.plugin.output.http;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import priv.eric.pelee.domain.model.OutputSink;
import priv.eric.pelee.domain.model.PipelineEvent;
import priv.eric.pelee.infrastructure.util.JsonUtil;

import java.util.concurrent.CompletableFuture;

/**
 * Description: HTTP输出接收器，将处理后的数据转换为JSON字符串
 *
 * @author EricTowns
 * @date 2026/2/4 22:18
 */
@Slf4j
public class HttpOutputSink implements OutputSink {

    private final OutputConfig config;

    /**
     * 构造函数
     */
    public HttpOutputSink() {
        this(new OutputConfig());
    }

    /**
     * 构造函数
     */
    public HttpOutputSink(OutputConfig config) {
        this.config = config;
    }

    /**
     * 发送处理后的数据
     */
    @Override
    public CompletableFuture<Void> sendOutput(PipelineEvent event) {
        try {
            JsonNode data = event.getData();
            String result = config.isPrettyPrint() ? 
                JsonUtil.toPrettyJson(data) : JsonUtil.toJson(data);
            
            if (config.isEnableLog()) {
                log.info("Output data: {}", result);
            }
            
            // 在实际应用中，这里可能需要发送到其他HTTP端点
            // 目前只是返回成功，表示数据已准备好发送
            return CompletableFuture.completedFuture(null);
            
        } catch (Exception e) {
            log.error("Failed to send output", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * 输出配置
     */
    @Data
    public static class OutputConfig {
        private boolean enableLog = false;
        private boolean prettyPrint = false;
        private String targetUrl;
    }
}