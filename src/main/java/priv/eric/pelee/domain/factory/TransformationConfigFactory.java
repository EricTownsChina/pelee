package priv.eric.pelee.domain.factory;

import priv.eric.pelee.domain.vo.FieldMapping;
import priv.eric.pelee.domain.vo.TransformationConfig;

import java.util.Arrays;
import java.util.List;

/**
 * 转换配置工厂
 */
public class TransformationConfigFactory {
    
    /**
     * 创建基本字段重命名配置
     */
    public static TransformationConfig createRenameConfig(List<FieldMapping> fieldMappings) {
        TransformationConfig config = new TransformationConfig();
        config.setFieldMappings(fieldMappings);
        return config;
    }
    
    /**
     * 创建字段排除配置
     */
    public static TransformationConfig createExcludeConfig(List<String> excludedFields) {
        TransformationConfig config = new TransformationConfig();
        config.setExcludedFields(excludedFields);
        return config;
    }
    
    /**
     * 创建完整转换配置
     */
    public static TransformationConfig createFullConfig(List<FieldMapping> fieldMappings, List<String> excludedFields) {
        TransformationConfig config = new TransformationConfig();
        config.setFieldMappings(fieldMappings);
        config.setExcludedFields(excludedFields);
        return config;
    }
    
    /**
     * 创建对话记录转换配置的便捷方法
     */
    public static TransformationConfig createConversationRecordConfig() {
        // 示例配置：将嵌套的用户信息提取到顶层，并重命名一些字段
        FieldMapping userIdMapping = new FieldMapping("user.id", "user.id", "userId", null, false);
        userIdMapping.setKeepOriginal(false); // 不保留原字段
        FieldMapping userNameMapping = new FieldMapping("user.name", "user.name", "userName", "Anonymous", false);
        userNameMapping.setKeepOriginal(false); // 不保留原字段
        FieldMapping contentMapping = new FieldMapping("message.content", "message.content", "textContent", null, false);
        contentMapping.setKeepOriginal(true); // 保留原字段
        
        List<FieldMapping> mappings = Arrays.asList(userIdMapping, userNameMapping, contentMapping);
        List<String> exclusions = Arrays.asList("internal_data", "temp_field"); // 排除某些字段
        
        return createFullConfig(mappings, exclusions);
    }
    
    /**
     * 创建保留原字段的对话记录转换配置
     */
    public static TransformationConfig createConversationRecordConfigWithOriginal() {
        // 示例配置：保留原字段的版本
        FieldMapping userIdMapping = new FieldMapping("user.id", "user.id", "userId", null, false);
        userIdMapping.setKeepOriginal(true); // 保留原字段
        FieldMapping userNameMapping = new FieldMapping("user.name", "user.name", "userName", "Anonymous", false);
        userNameMapping.setKeepOriginal(true); // 保留原字段
        FieldMapping contentMapping = new FieldMapping("message.content", "message.content", "textContent", null, false);
        contentMapping.setKeepOriginal(true); // 保留原字段
        
        List<FieldMapping> mappings = Arrays.asList(userIdMapping, userNameMapping, contentMapping);
        List<String> exclusions = Arrays.asList("internal_data", "temp_field"); // 排除某些字段
        
        return createFullConfig(mappings, exclusions);
    }
}