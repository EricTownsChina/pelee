package priv.eric.pelee.domain.repository;

import priv.eric.pelee.domain.vo.TransformationConfig;

/**
 * 转换配置仓库接口
 */
public interface TransformationConfigRepository {
    
    /**
     * 保存转换配置
     */
    void save(String configName, TransformationConfig config);
    
    /**
     * 根据名称获取转换配置
     */
    TransformationConfig findByName(String configName);
    
    /**
     * 删除转换配置
     */
    void delete(String configName);
    
    /**
     * 检查配置是否存在
     */
    boolean exists(String configName);
}