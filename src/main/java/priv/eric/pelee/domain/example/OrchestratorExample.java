package priv.eric.pelee.domain.example;

import priv.eric.pelee.domain.entity.ConversationRecord;
import priv.eric.pelee.domain.factory.TransformationConfigFactory;
import priv.eric.pelee.domain.service.impl.DomainTransformationOrchestrator;
import priv.eric.pelee.domain.vo.field.FieldMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 编排服务示例
 */
public class OrchestratorExample {
    
    public static void main(String[] args) {
        // 创建编排服务
        DomainTransformationOrchestrator orchestrator = new DomainTransformationOrchestrator();
        
        // 创建示例配置
        FieldMapping userIdMapping = new FieldMapping("user.id", "user.id", "userId", null, false);
        FieldMapping userNameMapping = new FieldMapping("user.profile.name", "user.profile.name", "userName", "Anonymous", false);
        FieldMapping contentMapping = new FieldMapping("message.content", "message.content", "textContent", null, false);
        
        var config = TransformationConfigFactory.createFullConfig(
            Arrays.asList(userIdMapping, userNameMapping, contentMapping),
            Arrays.asList("internal_data", "temp_field")
        );
        
        // 保存配置
        orchestrator.saveConfig("conversation-transform-rule", config);
        
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
        
        // 使用命名配置进行转换
        ConversationRecord transformedRecord = orchestrator.transformWithNamedConfig(sourceRecord, "conversation-transform-rule");
        
        System.out.println("\n转换后记录: " + transformedRecord.getId());
        System.out.println("转换后元数据: " + transformedRecord.getMetadata());
        
        // 检查是否配置存在
        System.out.println("\n配置是否存在: " + orchestrator.hasConfig("conversation-transform-rule"));
        
        // 删除配置
        orchestrator.removeConfig("conversation-transform-rule");
        System.out.println("配置是否仍然存在: " + orchestrator.hasConfig("conversation-transform-rule"));
    }
}