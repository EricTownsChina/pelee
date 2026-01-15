package priv.eric.pelee.domain.event;

import priv.eric.pelee.domain.entity.ConversationRecord;

/**
 * 转换完成事件
 */
public class TransformationCompletedEvent {
    private final ConversationRecord originalRecord;
    private final ConversationRecord transformedRecord;
    private final String configName;
    private final long timestamp;
    
    public TransformationCompletedEvent(ConversationRecord originalRecord, 
                                      ConversationRecord transformedRecord, 
                                      String configName) {
        this.originalRecord = originalRecord;
        this.transformedRecord = transformedRecord;
        this.configName = configName;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters
    public ConversationRecord getOriginalRecord() {
        return originalRecord;
    }
    
    public ConversationRecord getTransformedRecord() {
        return transformedRecord;
    }
    
    public String getConfigName() {
        return configName;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return String.format("TransformationCompletedEvent{configName='%s', timestamp=%d}", 
                           configName, timestamp);
    }
}