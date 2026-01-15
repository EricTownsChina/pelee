package priv.eric.pelee.domain.service.impl;

import priv.eric.pelee.domain.entity.ConversationRecord;
import priv.eric.pelee.domain.service.TransformerService;
import priv.eric.pelee.domain.util.PathExtractor;
import priv.eric.pelee.domain.vo.FieldMapping;
import priv.eric.pelee.domain.vo.TransformationConfig;

import java.util.*;

/**
 * 转换器服务实现
 */
public class TransformerServiceImpl implements TransformerService {

    @Override
    public ConversationRecord transform(ConversationRecord sourceRecord, TransformationConfig config) {
        // 创建新的对话记录
        ConversationRecord result = new ConversationRecord();
        
        // 复制基本字段
        result.setId(sourceRecord.getId());
        result.setContent(sourceRecord.getContent());
        result.setTimestamp(sourceRecord.getTimestamp());
        
        // 初始化元数据映射
        Map<String, Object> resultMetadata = new HashMap<>();
        
        // 如果源记录有元数据，先复制所有字段
        if (sourceRecord.getMetadata() != null) {
            resultMetadata.putAll(sourceRecord.getMetadata());
        }
        
        // 处理字段映射
        if (config.getFieldMappings() != null) {
            for (FieldMapping mapping : config.getFieldMappings()) {
                processFieldMapping(resultMetadata, sourceRecord, mapping);
            }
        }
        
        // 处理排除字段
        if (config.getExcludedFields() != null) {
            for (String excludedField : config.getExcludedFields()) {
                resultMetadata.remove(excludedField);
            }
        }
        
        result.setMetadata(resultMetadata);
        return result;
    }
    
    /**
     * 处理单个字段映射
     */
    private void processFieldMapping(Map<String, Object> resultMetadata, ConversationRecord sourceRecord, FieldMapping mapping) {
        String sourcePath = mapping.getSourceField();
        String targetField = mapping.getTargetField() != null ? mapping.getTargetField() : mapping.getSourceField();
        String renameTo = mapping.getRenameTo() != null ? mapping.getRenameTo() : targetField;
        
        // 从源数据中提取值
        Object value = extractValueFromPath(sourceRecord, sourcePath);
        
        // 如果没有找到值且有默认值，则使用默认值
        if (value == null && mapping.getDefaultValue() != null) {
            value = mapping.getDefaultValue();
        }
        
        // 如果是必需字段但没有值，则抛出异常（或者记录错误）
        if (value == null && mapping.isRequired()) {
            throw new RuntimeException("Required field " + sourcePath + " is missing");
        }
        
        // 设置到结果中
        if (value != null) {
            resultMetadata.put(renameTo, value);
            
            // 如果不保留原字段，则从结果中删除原字段
            if (!mapping.isKeepOriginal()) {
                // 删除原始路径对应的字段
                removeOriginalField(resultMetadata, sourcePath);
            }
        }
    }
    
    /**
     * 从路径提取值，支持嵌套路径，如 "user.profile.name"
     */
    private Object extractValueFromPath(ConversationRecord record, String path) {
        // 首先检查基本字段
        switch (path) {
            case "id":
                return record.getId();
            case "content":
                return record.getContent();
            case "timestamp":
                return record.getTimestamp();
        }
        
        // 检查metadata中的路径
        if (record.getMetadata() != null) {
            return PathExtractor.extractByPath(record.getMetadata(), path);
        }
        
        return null;
    }
    
    /**
     * 删除原始字段，如果不需要保留的话
     */
    private void removeOriginalField(Map<String, Object> resultMetadata, String sourcePath) {
        String[] pathParts = sourcePath.split("\\.", -1); // 使用-1确保保留尾随空字符串
        
        if (pathParts.length == 1) {
            // 简单情况：直接删除顶层字段
            resultMetadata.remove(pathParts[0]);
        } else {
            // 复杂情况：删除嵌套字段
            removeNestedField(resultMetadata, pathParts, 0);
        }
    }
    
    /**
     * 递归删除嵌套字段
     */
    @SuppressWarnings("unchecked")
    private void removeNestedField(Map<String, Object> currentMap, String[] pathParts, int depth) {
        if (depth >= pathParts.length) {
            return; // 达到路径末尾
        }
        
        String currentKey = pathParts[depth];
        Object currentValue = currentMap.get(currentKey);
        
        if (currentValue == null) {
            return; // 路径不存在
        }
        
        if (depth == pathParts.length - 1) {
            // 到达路径末尾，删除当前键
            currentMap.remove(currentKey);
        } else if (currentValue instanceof Map) {
            // 继续深入嵌套结构
            Map<String, Object> nestedMap = (Map<String, Object>) currentValue;
            removeNestedField(nestedMap, pathParts, depth + 1);
            
            // 检查嵌套Map是否变空，如果变空可以考虑删除它
            if (nestedMap.isEmpty()) {
                currentMap.remove(currentKey);
            }
        } else {
            // 如果路径中间的节点不是Map，说明路径不正确
            // 这种情况通常不应该发生，但为了健壮性处理
            return;
        }
    }
}