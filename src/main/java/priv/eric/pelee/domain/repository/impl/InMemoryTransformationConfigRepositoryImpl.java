package priv.eric.pelee.domain.repository.impl;

import priv.eric.pelee.domain.repository.TransformationConfigRepository;
import priv.eric.pelee.domain.vo.TransformationConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存转换配置仓库实现
 */
public class InMemoryTransformationConfigRepositoryImpl implements TransformationConfigRepository {
    
    private final Map<String, TransformationConfig> configStore = new ConcurrentHashMap<>();
    
    @Override
    public void save(String configName, TransformationConfig config) {
        if (configName == null || configName.trim().isEmpty()) {
            throw new IllegalArgumentException("Config name cannot be null or empty");
        }
        configStore.put(configName, config);
    }
    
    @Override
    public TransformationConfig findByName(String configName) {
        if (configName == null || configName.trim().isEmpty()) {
            return null;
        }
        return configStore.get(configName);
    }
    
    @Override
    public void delete(String configName) {
        if (configName != null) {
            configStore.remove(configName);
        }
    }
    
    @Override
    public boolean exists(String configName) {
        return configName != null && configStore.containsKey(configName);
    }
}