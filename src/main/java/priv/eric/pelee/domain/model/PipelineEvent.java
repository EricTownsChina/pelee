package priv.eric.pelee.domain.model;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Description: 流水线事件，封装对话记录数据和元数据
 * 使用不可变设计，每次操作返回新的事件实例
 *
 * @author EricTowns
 * @date 2026/2/4 21:30
 */
public class PipelineEvent {

    private final JsonNode data;
    private final JsonNode metadata;
    private final long timestamp;

    /**
     * 构造函数
     */
    public PipelineEvent(JsonNode data, JsonNode metadata, long timestamp) {
        this.data = data;
        this.metadata = metadata;
        this.timestamp = timestamp;
    }

    /**
     * 创建简单的事件实例（只有数据）
     */
    public static PipelineEvent of(JsonNode data) {
        return new PipelineEvent(data, null, System.currentTimeMillis());
    }

    /**
     * 创建带元数据的事件实例
     */
    public static PipelineEvent of(JsonNode data, JsonNode metadata) {
        return new PipelineEvent(data, metadata, System.currentTimeMillis());
    }

    /**
     * 获取事件数据
     */
    public JsonNode getData() {
        return data;
    }

    /**
     * 获取元数据
     */
    public JsonNode getMetadata() {
        return metadata;
    }

    /**
     * 获取时间戳
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * 创建新的事件实例，替换数据部分
     */
    public PipelineEvent withData(JsonNode newData) {
        return new PipelineEvent(newData, this.metadata, this.timestamp);
    }

    /**
     * 创建新的事件实例，添加或更新元数据
     */
    public PipelineEvent withMetadata(JsonNode newMetadata) {
        return new PipelineEvent(this.data, newMetadata, this.timestamp);
    }

    /**
     * 创建新的事件实例，更新时间戳
     */
    public PipelineEvent withTimestamp(long newTimestamp) {
        return new PipelineEvent(this.data, this.metadata, newTimestamp);
    }

    /**
     * 判断是否包含数据
     */
    public boolean hasData() {
        return data != null && !data.isNull();
    }

    /**
     * 判断是否包含元数据
     */
    public boolean hasMetadata() {
        return metadata != null && !metadata.isNull();
    }

    @Override
    public String toString() {
        return "PipelineEvent{" +
                "data=" + data +
                ", metadata=" + metadata +
                ", timestamp=" + timestamp +
                '}';
    }
}