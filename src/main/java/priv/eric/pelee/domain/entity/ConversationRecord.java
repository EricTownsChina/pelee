package priv.eric.pelee.domain.entity;

import java.util.Map;

/**
 * 对话记录实体
 */
public class ConversationRecord {
    private String id;
    private String content;
    private Map<String, Object> metadata;
    private Long timestamp;

    public ConversationRecord() {
    }

    public ConversationRecord(String id, String content, Map<String, Object> metadata, Long timestamp) {
        this.id = id;
        this.content = content;
        this.metadata = metadata;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}