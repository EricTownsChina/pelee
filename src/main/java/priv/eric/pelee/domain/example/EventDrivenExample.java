package priv.eric.pelee.domain.example;

import priv.eric.pelee.domain.entity.ConversationRecord;
import priv.eric.pelee.domain.event.TransformationCompletedEvent;
import priv.eric.pelee.domain.factory.TransformationConfigFactory;
import priv.eric.pelee.domain.service.impl.DomainTransformationOrchestrator;
import priv.eric.pelee.domain.vo.field.FieldMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 事件驱动示例
 */
public class EventDrivenExample {
    
    public static void main(String[] args) {
        // 创建编排服务
        DomainTransformationOrchestrator orchestrator = new DomainTransformationOrchestrator();
        
        // 设置事件监听器
        orchestrator.setEventListener(new Consumer<TransformationCompletedEvent>() {
            @Override
            public void accept(TransformationCompletedEvent event) {
                System.out.println("事件触发 - 配置: " + event.getConfigName());
                System.out.println("原始记录ID: " + event.getOriginalRecord().getId());
                System.out.println("转换后记录ID: " + event.getTransformedRecord().getId());
                System.out.println("转换时间: " + event.getTimestamp());
                System.out.println("---");
            }
        });
        
        // 创建示例配置
        FieldMapping userIdMapping = new FieldMapping("user.id", "user.id", "userId", null, false);
        FieldMapping userNameMapping = new FieldMapping("user.profile.name", "user.profile.name", "userName", "Anonymous", false);
        FieldMapping contentMapping = new FieldMapping("message.content", "message.content", "textContent", null, false);
        
        var config = TransformationConfigFactory.createFullConfig(
            Arrays.asList(userIdMapping, userNameMapping, contentMapping),
            Arrays.asList("internal_data", "temp_field")
        );
        
        // 保存配置
        orchestrator.saveConfig("event-driven-rule", config);
        
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
        
        System.out.println("开始转换...");
        
        // 使用命名配置进行转换，这将触发事件
        ConversationRecord transformedRecord = orchestrator.transformWithNamedConfig(sourceRecord, "event-driven-rule");
        
        System.out.println("转换完成");
        System.out.println("转换后元数据: " + transformedRecord.getMetadata());
    }
}