package priv.eric.pelee.domain.util;

import priv.eric.pelee.domain.vo.TransformationConfig;

import java.io.IOException;

/**
 * 配置解析器 - 统一入口
 */
public class ConfigParser {
    
    /**
     * 从JSON字符串解析转换配置
     */
    public static TransformationConfig parseFromJson(String jsonConfig) {
        try {
            return JacksonConfigParser.parseFromJson(jsonConfig);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse transformation config from JSON", e);
        }
    }
    
    /**
     * 从资源文件解析转换配置
     */
    public static TransformationConfig parseFromResource(String resourcePath) {
        try {
            return JacksonConfigParser.parseFromResource(resourcePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse transformation config from resource: " + resourcePath, e);
        }
    }
}