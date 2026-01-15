package priv.eric.pelee.domain.service.impl;

import priv.eric.pelee.domain.entity.ConversationRecord;
import priv.eric.pelee.domain.event.TransformationCompletedEvent;
import priv.eric.pelee.domain.service.ConversationTransformerService;
import priv.eric.pelee.domain.service.TransformationConfigService;
import priv.eric.pelee.domain.vo.TransformationConfig;

import java.util.List;
import java.util.function.Consumer;

/**
 * 领域转换编排服务
 * 协调配置管理和记录转换操作
 */
public class DomainTransformationOrchestrator {
    
    private final TransformationConfigService configService;
    private final ConversationTransformerService transformerService;
    private Consumer<TransformationCompletedEvent> eventListener;
    
    public DomainTransformationOrchestrator() {
        this.configService = new TransformationConfigServiceImpl();
        this.transformerService = new ConversationTransformerServiceImpl();
    }
    
    public DomainTransformationOrchestrator(TransformationConfigService configService, 
                                          ConversationTransformerService transformerService) {
        this.configService = configService;
        this.transformerService = transformerService;
    }
    
    /**
     * 设置事件监听器
     */
    public void setEventListener(Consumer<TransformationCompletedEvent> eventListener) {
        this.eventListener = eventListener;
    }
    
    /**
     * 使用命名配置转换单个记录
     */
    public ConversationRecord transformWithNamedConfig(ConversationRecord record, String configName) {
        TransformationConfig config = configService.getConfig(configName);
        if (config == null) {
            throw new IllegalArgumentException("Config not found: " + configName);
        }
        
        ConversationRecord originalRecord = cloneRecord(record);
        ConversationRecord transformedRecord = transformerService.transformRecord(record, config);
        
        // 发布转换完成事件
        if (eventListener != null) {
            eventListener.accept(new TransformationCompletedEvent(originalRecord, transformedRecord, configName));
        }
        
        return transformedRecord;
    }
    
    /**
     * 使用命名配置批量转换记录
     */
    public List<ConversationRecord> transformBatchWithNamedConfig(List<ConversationRecord> records, String configName) {
        TransformationConfig config = configService.getConfig(configName);
        if (config == null) {
            throw new IllegalArgumentException("Config not found: " + configName);
        }
        return transformerService.transformRecords(records, config);
    }
    
    /**
     * 验证配置并保存
     */
    public void saveConfig(String configName, TransformationConfig config) {
        if (!transformerService.validateConfig(config)) {
            throw new IllegalArgumentException("Invalid configuration: " + configName);
        }
        configService.saveConfig(configName, config);
    }
    
    /**
     * 获取配置
     */
    public TransformationConfig getConfig(String configName) {
        return configService.getConfig(configName);
    }
    
    /**
     * 检查配置是否存在
     */
    public boolean hasConfig(String configName) {
        return configService.configExists(configName);
    }
    
    /**
     * 删除配置
     */
    public void removeConfig(String configName) {
        configService.deleteConfig(configName);
    }
    
    /**
     * 克隆记录
     */
    private ConversationRecord cloneRecord(ConversationRecord record) {
        if (record == null) {
            return null;
        }
        
        ConversationRecord clone = new ConversationRecord();
        clone.setId(record.getId());
        clone.setContent(record.getContent());
        clone.setTimestamp(record.getTimestamp());
        
        if (record.getMetadata() != null) {
            clone.setMetadata(new java.util.HashMap<>(record.getMetadata()));
        }
        
        return clone;
    }
}