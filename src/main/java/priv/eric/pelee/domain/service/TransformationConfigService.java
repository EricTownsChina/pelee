package priv.eric.pelee.domain.service;

import priv.eric.pelee.domain.vo.TransformationConfig;

/**
 * 转换配置服务
 */
public interface TransformationConfigService {
    
    /**
     * 保存转换配置
     */
    void saveConfig(String configName, TransformationConfig config);
    
    /**
     * 获取转换配置
     */
    TransformationConfig getConfig(String configName);
    
    /**
     * 删除转换配置
     */
    void deleteConfig(String configName);
    
    /**
     * 验证配置名称是否存在
     */
    boolean configExists(String configName);
    
    /**
     * 更新转换配置
     */
    void updateConfig(String configName, TransformationConfig config);
}