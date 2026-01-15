# 对话记录转换系统

## 概述

这是一个基于领域驱动设计（DDD）的对话记录转换系统，支持通过配置文件灵活地转换对话记录的结构。

## 核心组件

### 1. 实体层 (Entity)
- `ConversationRecord`: 对话记录实体，包含ID、内容、元数据和时间戳

### 2. 值对象 (Value Object)
- `TransformationConfig`: 转换配置，定义转换规则
- `FieldMapping`: 字段映射配置，定义字段来源、目标和转换规则

### 3. 服务层 (Service)
- `TransformerService`: 基础转换服务接口
- `ConversationTransformerService`: 对话记录转换服务接口
- `TransformationConfigService`: 转换配置管理服务接口

### 4. 服务实现层 (Service Implementation)
- `TransformerServiceImpl`: 转换服务实现
- `ConversationTransformerServiceImpl`: 对话记录转换服务实现
- `TransformationConfigServiceImpl`: 配置管理服务实现
- `DomainTransformationOrchestrator`: 领域转换编排服务

### 5. 工厂层 (Factory)
- `TransformationConfigFactory`: 转换配置工厂，提供创建配置的便捷方法

### 6. 仓库层 (Repository)
- `TransformationConfigRepository`: 配置仓库接口
- `InMemoryTransformationConfigRepositoryImpl`: 内存配置仓库实现

### 7. 工具层 (Util)
- `PathExtractor`: 路径提取工具，支持嵌套路径访问
- `ConfigParser`: 配置解析器
- `JacksonConfigParser`: 基于Jackson的配置解析器

### 8. 事件层 (Event)
- `TransformationCompletedEvent`: 转换完成事件

## 使用示例

### 1. 基本转换
```java
// 创建转换服务
ConversationTransformerServiceImpl transformerService = new ConversationTransformerServiceImpl();

// 创建转换配置
FieldMapping userIdMapping = new FieldMapping("user.id", "user.id", "userId", null, false);
userIdMapping.setKeepOriginal(false); // 不保留原字段
TransformationConfig config = TransformationConfigFactory.createFullConfig(
    Arrays.asList(userIdMapping), 
    Arrays.asList("internal_data")
);

// 执行转换
ConversationRecord transformedRecord = transformerService.transformRecord(sourceRecord, config);
```

### 2. 配置文件驱动转换
```java
// 从资源文件加载配置
TransformationConfig config = ConfigParser.parseFromResource("/transformation-config.json");

// 使用配置转换记录
ConversationRecord transformedRecord = transformerService.transformRecord(sourceRecord, config);
```

### 3. 事件驱动转换
```java
// 创建编排服务
DomainTransformationOrchestrator orchestrator = new DomainTransformationOrchestrator();

// 设置事件监听器
orchestrator.setEventListener(event -> {
    System.out.println("转换完成: " + event.getConfigName());
});

// 执行转换（会触发事件）
ConversationRecord result = orchestrator.transformWithNamedConfig(record, "my-config");
```

## 配置格式

配置文件采用类似filebeat的格式，支持以下处理器：

### 重命名字段 (rename_fields)
```json
{
  "rename_fields": {
    "fields": [
      {"from": "user.id", "to": "userId", "keep_original": false},
      {"from": "message.content", "to": "textContent", "keep_original": true}
    ],
    "ignore_missing": true
  }
}
```

### 提取字段 (extract_fields)
```json
{
  "extract_fields": {
    "fields": [
      {"from": "user.profile.name", "to": "userName", "default_value": "Anonymous", "keep_original": false},
      {"from": "metadata.tags", "to": "tags", "keep_original": true}
    ]
  }
}
```

### 删除字段 (drop_fields)
```json
{
  "drop_fields": {
    "fields": ["internal_data", "temp_field", "debug_info"]
  }
}
```

## 特性

1. **灵活配置**: 支持通过JSON配置文件定义转换规则
2. **路径提取**: 支持嵌套路径访问，如 "user.profile.name"
3. **字段重命名**: 支持将字段重命名为新的名称
4. **保留原字段**: 支持选择是否保留原始字段，默认为false（不保留）
5. **默认值**: 支持为缺失字段设置默认值
6. **字段排除**: 支持排除不需要的字段
7. **事件驱动**: 支持转换完成事件
8. **配置管理**: 支持配置的保存、查询和删除
9. **批量处理**: 支持批量转换多个记录