package priv.eric.pelee.domain.aggregate;

import priv.eric.pelee.domain.entity.ConversationRecord;

import java.util.List;

/**
 * 对话聚合根
 * 管理一组相关的对话记录
 */
public class ConversationAggregate {
    private String aggregateId;
    private List<ConversationRecord> conversationRecords;
    private Long createdAt;
    private Long updatedAt;

    public ConversationAggregate() {
    }

    public ConversationAggregate(String aggregateId, List<ConversationRecord> conversationRecords) {
        this.aggregateId = aggregateId;
        this.conversationRecords = conversationRecords;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    /**
     * 添加对话记录
     */
    public void addConversationRecord(ConversationRecord record) {
        if (this.conversationRecords == null) {
            this.conversationRecords = new java.util.ArrayList<>();
        }
        this.conversationRecords.add(record);
        this.updatedAt = System.currentTimeMillis();
    }

    /**
     * 移除对话记录
     */
    public boolean removeConversationRecord(String recordId) {
        if (this.conversationRecords != null) {
            boolean removed = this.conversationRecords.removeIf(record -> 
                record.getId() != null && record.getId().equals(recordId));
            if (removed) {
                this.updatedAt = System.currentTimeMillis();
            }
            return removed;
        }
        return false;
    }

    // Getters and Setters
    public String getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public List<ConversationRecord> getConversationRecords() {
        return conversationRecords;
    }

    public void setConversationRecords(List<ConversationRecord> conversationRecords) {
        this.conversationRecords = conversationRecords;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}