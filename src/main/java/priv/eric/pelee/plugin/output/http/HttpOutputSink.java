package priv.eric.pelee.plugin.output.http;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import priv.eric.pelee.domain.model.Codec;
import priv.eric.pelee.domain.model.io.OutputSink;
import priv.eric.pelee.domain.model.core.PipelineEvent;
import priv.eric.pelee.infrastructure.codec.JsonCodec;
import priv.eric.pelee.infrastructure.util.JsonUtil;

import java.util.concurrent.CompletableFuture;

/**
 * Description: HTTP输出接收器，将处理后的数据转换为JSON字符串
 * 支持使用Codec进行数据编解码
 *
 * @author EricTowns
 * @date 2026/2/4 22:18
 */
@Slf4j
public class HttpOutputSink implements OutputSink {

    private final OutputConfig config;
    private Codec codec;

    /**
     * 构造函数 - 使用默认JSON编解码器
     */
    public HttpOutputSink() {
        this(new OutputConfig(), new JsonCodec());
    }

    /**
     * 构造函数 - 使用默认JSON编解码器
     */
    public HttpOutputSink(OutputConfig config) {
        this(config, new JsonCodec());
    }

    /**
     * 构造函数 - 指定编解码器
     */
    public HttpOutputSink(OutputConfig config, Codec codec) {
        this.config = config != null ? config : new OutputConfig();
        this.codec = codec != null ? codec : new JsonCodec();
    }

    /**
     * 发送处理后的数据
     * 使用codec将PipelineEvent转换为输出格式
     */
    @Override
    public CompletableFuture<Void> sendOutput(PipelineEvent event) {
        try {
            String result;
            if (codec != null) {
                // 使用codec格式化事件
                result = codec.formatEvent(event);
            } else {
                // 默认使用JSON转换
                JsonNode data = event.getData();
                result = config.isPrettyPrint() ? JsonUtil.toPrettyJson(data) : JsonUtil.toJson(data);
            }
            
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
     * 获取编解码器
     */
    @Override
    public Codec getCodec() {
        return codec;
    }

    /**
     * 设置编解码器
     */
    @Override
    public void setCodec(Codec codec) {
        this.codec = codec;
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