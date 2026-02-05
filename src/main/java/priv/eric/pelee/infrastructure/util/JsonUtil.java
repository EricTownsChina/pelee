package priv.eric.pelee.infrastructure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

/**
 * 高性能JSON工具类
 *
 * @author Eric Zhao
 */
public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 将对象转换为JSON字符串
     *
     * @param obj 待转换的对象
     * @return JSON字符串
     */
    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Object to JSON conversion failed", e);
        }
    }

    /**
     * 将对象转换为格式化（美观）的JSON字符串
     *
     * @param obj 待转换的对象
     * @return 格式化后的JSON字符串
     */
    public static String toPrettyJson(Object obj) {
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Object to pretty JSON conversion failed", e);
        }
    }

    /**
     * 将JSON字符串转换为指定类型的对象
     *
     * @param json  JSON字符串
     * @param clazz 目标类型
     * @param <T>   泛型参数
     * @return 指定类型的对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON to Object conversion failed", e);
        }
    }

    /**
     * 将JSON字符串转换为指定类型的对象（支持泛型）
     *
     * @param json          JSON字符串
     * @param typeReference 目标类型引用
     * @param <T>           泛型参数
     * @return 指定类型的对象
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON to Object conversion failed", e);
        }
    }

    /**
     * 将JSON字符串解析为JsonNode
     *
     * @param json JSON字符串
     * @return JsonNode对象
     */
    public static JsonNode parseToJsonNode(String json) {
        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON parsing to JsonNode failed", e);
        }
    }

    /**
     * 将JsonNode转换为对象
     *
     * @param node JsonNode
     * @return T 转换对象
     */
    public static <T> T convertValue(JsonNode node, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(node, clazz);
    }

    /**
     * 从JSON字符串中根据路径提取值
     *
     * @param json JSON字符串
     * @param path 路径表达式，如 "user.name" 或 "items[0].id"
     * @return 提取的值，如果路径不存在则返回Optional.empty()
     */
    public static Optional<String> extractValueByPath(String json, String path) {
        try {
            JsonNode rootNode = parseToJsonNode(json);
            JsonNode node = getByPath(rootNode, path);

            if (node != null && !node.isMissingNode()) {
                return Optional.of(node.asText());
            }
        } catch (Exception e) {
            // 如果解析失败或路径不存在，返回空Optional
        }

        return Optional.empty();
    }

    /**
     * 从JsonNode中根据路径提取值
     *
     * @param node JsonNode对象
     * @param path 路径表达式，如 "user.name" 或 "items[0].id"
     * @return 提取的值，如果路径不存在则返回Optional.empty()
     */
    public static Optional<String> extractValueByPath(JsonNode node, String path) {
        try {
            JsonNode resultNode = getByPath(node, path);

            if (resultNode != null && !resultNode.isMissingNode()) {
                return Optional.of(resultNode.asText());
            }
        } catch (Exception e) {
            // 如果解析失败或路径不存在，返回空Optional
        }

        return Optional.empty();
    }

    /**
     * 根据路径获取JsonNode
     *
     * @param node JsonNode对象
     * @param path 路径表达式，如 "user.name" 或 "items[0].id"
     * @return JsonNode对象，如果路径不存在则返回null
     */
    public static JsonNode getByPath(JsonNode node, String path) {
        if (node == null || path == null || path.trim().isEmpty()) {
            return null;
        }

        try {
            // 使用Jackson的at方法，支持JSON Pointer语法 (例如 /user/name 或 /items/0/id)
            // 但我们需要将点分隔的路径转换为JSON Pointer语法
            String jsonPointerPath = convertDotPathToJsonPointer(path);
            return node.at(jsonPointerPath);
        } catch (Exception e) {
            // 如果路径无效，返回null
            return null;
        }
    }

    /**
     * 将点分隔和数组索引路径转换为JSON指针语法
     * 例如: "user.name" -> "/user/name", "items[0].id" -> "/items/0/id"
     *
     * @param path 普通路径表达式
     * @return JSON指针路径
     */
    private static String convertDotPathToJsonPointer(String path) {
        if (path == null || path.isEmpty()) {
            return "/";
        }

        // 先处理数组索引: items[0].property -> items/0/property
        String converted = path.replaceAll("\\[(\\d+)\\]", "/$1");

        // 再将点替换为斜杠
        converted = "/" + converted.replace('.', '/');

        return converted;
    }

    /**
     * 检查JSON字符串格式是否有效
     *
     * @param jsonString 待验证的JSON字符串
     * @return 是否为有效的JSON格式
     */
    public static boolean isValidJson(String jsonString) {
        try {
            OBJECT_MAPPER.readTree(jsonString);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    /**
     * 获取ObjectMapper实例，用于自定义配置
     *
     * @return ObjectMapper实例
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
}