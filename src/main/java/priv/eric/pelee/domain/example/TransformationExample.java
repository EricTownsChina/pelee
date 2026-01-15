package priv.eric.pelee.domain.example;

import priv.eric.pelee.domain.entity.ConversationRecord;
import priv.eric.pelee.domain.service.impl.ConversationTransformerServiceImpl;
import priv.eric.pelee.domain.util.ConfigParser;
import priv.eric.pelee.domain.vo.TransformationConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 转换示例
 */
public class TransformationExample {
    
    public static void main(String[] args) {
        // 创建转换服务
        ConversationTransformerServiceImpl transformerService = new ConversationTransformerServiceImpl();
        
        // 创建示例对话记录
        Map<String, Object> metadata = new HashMap<>();
        Map<String, Object> user = new HashMap<>();
        user.put("id", "user123");
        user.put("profile", Map.of("name", "John Doe", "email", "john@example.com"));
        
        Map<String, Object> message = new HashMap<>();
        message.put("content", "Hello, how are you?");
        message.put("type", "text");
        
        metadata.put("user", user);
        metadata.put("message", message);
        metadata.put("conversation_type", "support");
        metadata.put("internal_data", "sensitive_info");
        metadata.put("temp_field", "temporary_value");
        
        ConversationRecord sourceRecord = new ConversationRecord(
            "record001", 
            "Original content", 
            metadata, 
            System.currentTimeMillis()
        );
        
        System.out.println("原始记录: " + sourceRecord.getId());
        System.out.println("原始元数据: " + sourceRecord.getMetadata());
        
        try {
            // 从资源文件加载配置
            TransformationConfig config = ConfigParser.parseFromResource("/transformation-config.json");
            
            // 执行转换
            ConversationRecord transformedRecord = transformerService.transformRecord(sourceRecord, config);
            
            System.out.println("\n转换后记录: " + transformedRecord.getId());
            System.out.println("转换后元数据: " + transformedRecord.getMetadata());
            
        } catch (Exception e) {
            System.err.println("转换过程中发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}