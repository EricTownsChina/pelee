package priv.eric.pelee.domain.service.impl;

import priv.eric.pelee.domain.entity.ConversationRecord;
import priv.eric.pelee.domain.service.ConversationTransformerService;
import priv.eric.pelee.domain.service.TransformerService;
import priv.eric.pelee.domain.vo.TransformationConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 对话记录转换服务实现
 */
public class ConversationTransformerServiceImpl implements ConversationTransformerService {
    
    private final TransformerService transformerService;
    
    public ConversationTransformerServiceImpl() {
        this.transformerService = new TransformerServiceImpl();
    }
    
    public ConversationTransformerServiceImpl(TransformerService transformerService) {
        this.transformerService = transformerService;
    }
    
    @Override
    public ConversationRecord transformRecord(ConversationRecord record, TransformationConfig config) {
        if (record == null) {
            return null;
        }
        
        if (config == null) {
            // 如果没有配置，直接返回原记录的副本
            return copyRecord(record);
        }
        
        return transformerService.transform(record, config);
    }
    
    @Override
    public List<ConversationRecord> transformRecords(List<ConversationRecord> records, TransformationConfig config) {
        if (records == null) {
            return null;
        }
        
        List<ConversationRecord> result = new ArrayList<>();
        for (ConversationRecord record : records) {
            result.add(transformRecord(record, config));
        }
        
        return result;
    }
    
    @Override
    public boolean validateConfig(TransformationConfig config) {
        // 验证配置的基本有效性
        if (config == null) {
            return true; // null配置表示无转换
        }
        
        // 检查字段映射配置
        if (config.getFieldMappings() != null) {
            for (int i = 0; i < config.getFieldMappings().size(); i++) {
                var mapping = config.getFieldMappings().get(i);
                if (mapping == null) {
                    return false;
                }
                
                // 验证字段名不为空（除非是特殊用途）
                if (mapping.getSourceField() == null || mapping.getSourceField().trim().isEmpty()) {
                    return false;
                }
            }
        }
        
        // 检查排除字段配置
        if (config.getExcludedFields() != null) {
            for (String field : config.getExcludedFields()) {
                if (field == null || field.trim().isEmpty()) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * 复制对话记录
     */
    private ConversationRecord copyRecord(ConversationRecord original) {
        if (original == null) {
            return null;
        }
        
        ConversationRecord copy = new ConversationRecord();
        copy.setId(original.getId());
        copy.setContent(original.getContent());
        copy.setTimestamp(original.getTimestamp());
        
        if (original.getMetadata() != null) {
            copy.setMetadata(new java.util.HashMap<>(original.getMetadata()));
        }
        
        return copy;
    }
}