package priv.eric.pelee.domain.service.impl;

import priv.eric.pelee.domain.repository.TransformationConfigRepository;
import priv.eric.pelee.domain.repository.impl.InMemoryTransformationConfigRepositoryImpl;
import priv.eric.pelee.domain.service.TransformationConfigService;
import priv.eric.pelee.domain.vo.TransformationConfig;

/**
 * 转换配置服务实现
 */
public class TransformationConfigServiceImpl implements TransformationConfigService {
    
    private final TransformationConfigRepository repository;
    
    public TransformationConfigServiceImpl() {
        this.repository = new InMemoryTransformationConfigRepositoryImpl();
    }
    
    public TransformationConfigServiceImpl(TransformationConfigRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public void saveConfig(String configName, TransformationConfig config) {
        validateConfigName(configName);
        validateConfig(config);
        repository.save(configName, config);
    }
    
    @Override
    public TransformationConfig getConfig(String configName) {
        validateConfigName(configName);
        return repository.findByName(configName);
    }
    
    @Override
    public void deleteConfig(String configName) {
        validateConfigName(configName);
        repository.delete(configName);
    }
    
    @Override
    public boolean configExists(String configName) {
        validateConfigName(configName);
        return repository.exists(configName);
    }
    
    @Override
    public void updateConfig(String configName, TransformationConfig config) {
        validateConfigName(configName);
        validateConfig(config);
        if (!repository.exists(configName)) {
            throw new IllegalArgumentException("Config does not exist: " + configName);
        }
        repository.save(configName, config);
    }
    
    private void validateConfigName(String configName) {
        if (configName == null || configName.trim().isEmpty()) {
            throw new IllegalArgumentException("Config name cannot be null or empty");
        }
    }
    
    private void validateConfig(TransformationConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null");
        }
    }
}