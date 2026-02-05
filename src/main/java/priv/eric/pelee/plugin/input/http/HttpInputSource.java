package priv.eric.pelee.plugin.input.http;

import com.fasterxml.jackson.databind.JsonNode;
import priv.eric.pelee.domain.model.Codec;
import priv.eric.pelee.domain.model.io.InputSource;
import priv.eric.pelee.domain.model.core.PipelineEvent;
import priv.eric.pelee.infrastructure.codec.JsonCodec;
import priv.eric.pelee.infrastructure.util.JsonUtil;

import java.util.concurrent.CompletableFuture;

/**
 * Description: HTTP输入源，通过HTTP接收数据
 * 支持使用Codec进行数据编解码
 *
 * @author EricTowns
 * @date 2026/2/4 22:15
 */
public class HttpInputSource implements InputSource {

    private final Object rawData;
    private Codec codec;

    /**
     * 构造函数 - 使用默认JSON编解码器
     */
    public HttpInputSource(JsonNode data) {
        this.rawData = data;
        this.codec = new JsonCodec();
    }

    /**
     * 构造函数，接收字符串数据 - 使用默认JSON编解码器
     */
    public HttpInputSource(String jsonData) {
        this.rawData = jsonData;
        this.codec = new JsonCodec();
    }

    /**
     * 构造函数，接收对象数据 - 使用默认JSON编解码器
     */
    public HttpInputSource(Object objectData) {
        this.rawData = objectData;
        this.codec = new JsonCodec();
    }

    /**
     * 构造函数，指定编解码器
     */
    public HttpInputSource(Object rawData, Codec codec) {
        this.rawData = rawData;
        this.codec = codec != null ? codec : new JsonCodec();
    }

    /**
     * 获取输入数据
     * 使用codec将原始数据转换为PipelineEvent
     */
    @Override
    public CompletableFuture<PipelineEvent> getInput() {
        JsonNode jsonNode;
        
        if (rawData instanceof JsonNode) {
            jsonNode = (JsonNode) rawData;
        } else if (codec != null) {
            // 使用codec编码数据
            jsonNode = codec.encode(rawData);
        } else {
            // 默认使用JSON转换
            jsonNode = JsonUtil.parseToJsonNode(JsonUtil.toJson(rawData));
        }
        
        return CompletableFuture.completedFuture(PipelineEvent.of(jsonNode));
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
     * 创建静态工厂方法 - 使用默认JSON编解码器
     */
    public static HttpInputSource of(Object data) {
        return new HttpInputSource(data);
    }

    /**
     * 创建静态工厂方法 - 指定编解码器
     */
    public static HttpInputSource of(Object data, Codec codec) {
        return new HttpInputSource(data, codec);
    }
}