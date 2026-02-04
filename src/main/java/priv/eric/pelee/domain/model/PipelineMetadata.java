package priv.eric.pelee.domain.model;

import java.util.Map;
import java.util.Optional;

/**
 * Description: 流水线元数据，包含流水线的配置信息
 * 纯数据容器，遵循DDD值对象设计原则
 *
 * @author EricTowns
 * @date 2026/2/4 21:58
 */
public class PipelineMetadata {

    private final String name;
    private final String description;
    private final Map<String, Object> properties;
    private final long createdAt;
    private final String version;

    /**
     * 构造函数
     */
    public PipelineMetadata(String name, String description, 
                         Map<String, Object> properties, String version) {
        this.name = name;
        this.description = description;
        this.properties = properties;
        this.version = version;
        this.createdAt = System.currentTimeMillis();
    }

    /**
     * 创建元数据
     */
    public static PipelineMetadata of(String name, String description) {
        return new PipelineMetadata(name, description, Map.of(), "1.0");
    }

    /**
     * 创建完整元数据
     */
    public static PipelineMetadata of(String name, String description, 
                                   Map<String, Object> properties, String version) {
        return new PipelineMetadata(name, description, properties, version);
    }

    /**
     * 获取名称
     */
    public String getName() {
        return name;
    }

    /**
     * 获取描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 获取属性
     */
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * 获取指定属性
     */
    public Optional<Object> getProperty(String key) {
        return Optional.ofNullable(properties.get(key));
    }

    /**
     * 获取指定属性，带默认值
     */
    @SuppressWarnings("unchecked")
    public <T> T getProperty(String key, T defaultValue) {
        Object value = properties.get(key);
        return value != null ? (T) value : defaultValue;
    }

    /**
     * 获取版本
     */
    public String getVersion() {
        return version;
    }

    /**
     * 获取创建时间
     */
    public long getCreatedAt() {
        return createdAt;
    }

    /**
     * 创建新元数据，添加属性
     */
    public PipelineMetadata withProperty(String key, Object value) {
        Map<String, Object> newProperties = Map.copyOf(properties);
        // Java的Map是不可变的，需要创建新Map
        java.util.HashMap<String, Object> mutableProperties = new java.util.HashMap<>(properties);
        mutableProperties.put(key, value);
        return new PipelineMetadata(name, description, Map.copyOf(mutableProperties), version);
    }

    @Override
    public String toString() {
        return "PipelineMetadata{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", properties=" + properties +
                ", version='" + version + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}