package priv.eric.pelee.domain.util;

import java.util.Map;

/**
 * 路径提取工具类
 * 支持从嵌套结构中按路径提取值
 */
public class PathExtractor {
    
    /**
     * 从Map结构中按点分隔的路径提取值
     * 例如: 从 {"user": {"profile": {"name": "John"}}} 中提取 "user.profile.name" 得到 "John"
     *
     * @param data 源数据Map
     * @param path 点分隔的路径，如 "user.profile.name"
     * @return 提取的值，如果路径不存在则返回null
     */
    @SuppressWarnings("unchecked")
    public static Object extractByPath(Map<String, Object> data, String path) {
        if (data == null || path == null || path.trim().isEmpty()) {
            return null;
        }
        
        String[] pathParts = path.split("\\.");
        Object currentValue = data;
        
        for (String part : pathParts) {
            if (currentValue == null || !(currentValue instanceof Map)) {
                return null;
            }
            
            Map<String, Object> currentMap = (Map<String, Object>) currentValue;
            currentValue = currentMap.get(part);
        }
        
        return currentValue;
    }
    
    /**
     * 安全地将值转换为目标类型
     */
    public static <T> T convertValue(Object value, Class<T> targetType) {
        if (value == null) {
            return null;
        }
        
        if (targetType.isInstance(value)) {
            return targetType.cast(value);
        }
        
        // 尝试基本类型转换
        if (targetType == String.class) {
            return targetType.cast(value.toString());
        } else if (targetType == Integer.class || targetType == int.class) {
            if (value instanceof Number) {
                return targetType.cast(((Number) value).intValue());
            } else {
                try {
                    return targetType.cast(Integer.valueOf(value.toString()));
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        } else if (targetType == Long.class || targetType == long.class) {
            if (value instanceof Number) {
                return targetType.cast(((Number) value).longValue());
            } else {
                try {
                    return targetType.cast(Long.valueOf(value.toString()));
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        } else if (targetType == Double.class || targetType == double.class) {
            if (value instanceof Number) {
                return targetType.cast(((Number) value).doubleValue());
            } else {
                try {
                    return targetType.cast(Double.valueOf(value.toString()));
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            if (value instanceof Boolean) {
                return targetType.cast(value);
            } else {
                return targetType.cast(Boolean.valueOf(value.toString()));
            }
        }
        
        return null;
    }
    
    /**
     * 检查路径是否存在
     */
    public static boolean hasPath(Map<String, Object> data, String path) {
        return extractByPath(data, path) != null;
    }
}