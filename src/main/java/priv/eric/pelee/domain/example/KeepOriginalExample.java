package priv.eric.pelee.domain.example;

import priv.eric.pelee.domain.entity.ConversationRecord;
import priv.eric.pelee.domain.factory.TransformationConfigFactory;
import priv.eric.pelee.domain.service.impl.ConversationTransformerServiceImpl;
import priv.eric.pelee.domain.vo.field.FieldMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 演示keepOriginal功能的示例
 */
public class KeepOriginalExample {
    
    public static void main(String[] args) {
        // 创建转换服务
        ConversationTransformerServiceImpl transformerService = new ConversationTransformerServiceImpl();
        
        // 创建示例对话记录
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("original_field", "original_value");
        metadata.put("user_id", "user123");
        metadata.put("message_content", "Hello, how are you?");
        
        ConversationRecord sourceRecord = new ConversationRecord(
            "record001", 
            "Original content", 
            metadata, 
            System.currentTimeMillis()
        );
        
        System.out.println("原始记录: " + sourceRecord.getId());
        System.out.println("原始元数据: " + sourceRecord.getMetadata());
        
        // 创建转换配置 - 第一个配置保留原字段
        FieldMapping fieldMapping1 = new FieldMapping("original_field", "original_field", "renamed_field", null, false);
        fieldMapping1.setKeepOriginal(true); // 保留原字段
        
        // 创建转换配置 - 第二个配置不保留原字段
        FieldMapping fieldMapping2 = new FieldMapping("user_id", "user_id", "userId", null, false);
        fieldMapping2.setKeepOriginal(false); // 不保留原字段
        
        var config = TransformationConfigFactory.createFullConfig(
            Arrays.asList(fieldMapping1, fieldMapping2),
            Arrays.asList()
        );
        
        // 执行转换
        ConversationRecord transformedRecord = transformerService.transformRecord(sourceRecord, config);
        
        System.out.println("\n转换后记录: " + transformedRecord.getId());
        System.out.println("转换后元数据: " + transformedRecord.getMetadata());
        
        // 验证结果
        System.out.println("\n验证结果:");
        System.out.println("原字段 'original_field' 是否存在: " + transformedRecord.getMetadata().containsKey("original_field"));
        System.out.println("重命名字段 'renamed_field' 是否存在: " + transformedRecord.getMetadata().containsKey("renamed_field"));
        System.out.println("原字段 'user_id' 是否存在: " + transformedRecord.getMetadata().containsKey("user_id"));
        System.out.println("重命名字段 'userId' 是否存在: " + transformedRecord.getMetadata().containsKey("userId"));
    }
}