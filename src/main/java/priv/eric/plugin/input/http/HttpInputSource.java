package priv.eric.plugin.input.http;

import com.fasterxml.jackson.databind.JsonNode;
import priv.eric.pelee.domain.model.InputSource;
import priv.eric.pelee.domain.model.PipelineEvent;
import priv.eric.pelee.infrastructure.util.JsonUtil;

import java.util.concurrent.CompletableFuture;

/**
 * Description: HTTP输入源，通过HTTP接收数据
 *
 * @author EricTowns
 * @date 2026/2/4 22:15
 */
public class HttpInputSource implements InputSource {

    private final JsonNode data;

    /**
     * 构造函数
     */
    public HttpInputSource(JsonNode data) {
        this.data = data;
    }

    /**
     * 构造函数，接收字符串数据
     */
    public HttpInputSource(String jsonData) {
        this.data = JsonUtil.parseToJsonNode(jsonData);
    }

    /**
     * 构造函数，接收对象数据
     */
    public HttpInputSource(Object objectData) {
        this.data = JsonUtil.parseToJsonNode(JsonUtil.toJson(objectData));
    }

    /**
     * 获取输入数据
     */
    @Override
    public CompletableFuture<PipelineEvent> getInput() {
        return CompletableFuture.completedFuture(PipelineEvent.of(data));
    }

    /**
     * 创建静态工厂方法
     */
    public static HttpInputSource of(Object data) {
        return new HttpInputSource(data);
    }
}