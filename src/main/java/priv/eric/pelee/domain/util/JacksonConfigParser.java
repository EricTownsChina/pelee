package priv.eric.pelee.domain.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import priv.eric.pelee.domain.vo.FieldMapping;
import priv.eric.pelee.domain.vo.TransformationConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 使用Jackson的配置解析器
 */
public class JacksonConfigParser {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 从JSON字符串解析转换配置
     */
    public static TransformationConfig parseFromJson(String jsonConfig) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonConfig);
        JsonNode processorsNode = rootNode.get("processors");
        
        List<FieldMapping> fieldMappings = new ArrayList<>();
        List<String> excludedFields = new ArrayList<>();
        
        if (processorsNode != null && processorsNode.isArray()) {
            for (JsonNode processorNode : processorsNode) {
                JsonNode processor = processorNode.elements().next(); // 获取处理器类型
                
                String processorType = processorNode.fieldNames().next();
                
                switch (processorType) {
                    case "rename_fields":
                        parseRenameFields(processor, fieldMappings);
                        break;
                    case "extract_fields":
                        parseExtractFields(processor, fieldMappings);
                        break;
                    case "drop_fields":
                        parseDropFields(processor, excludedFields);
                        break;
                }
            }
        }
        
        TransformationConfig config = new TransformationConfig();
        config.setFieldMappings(fieldMappings);
        config.setExcludedFields(excludedFields);
        
        return config;
    }
    
    /**
     * 从资源文件解析转换配置
     */
    public static TransformationConfig parseFromResource(String resourcePath) throws IOException {
        InputStream inputStream = JacksonConfigParser.class.getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }
        
        String jsonConfig = new String(inputStream.readAllBytes());
        return parseFromJson(jsonConfig);
    }
    
    private static void parseRenameFields(JsonNode processor, List<FieldMapping> fieldMappings) {
        JsonNode fieldsNode = processor.get("fields");
        boolean ignoreMissing = processor.has("ignore_missing") && 
                                processor.get("ignore_missing").asBoolean();
        
        if (fieldsNode != null && fieldsNode.isArray()) {
            for (JsonNode fieldNode : fieldsNode) {
                String from = fieldNode.get("from").asText();
                String to = fieldNode.get("to").asText();
                boolean keepOriginal = fieldNode.has("keep_original") && 
                                     fieldNode.get("keep_original").asBoolean(false); // 默认为false
                
                FieldMapping mapping = new FieldMapping();
                mapping.setSourceField(from);
                mapping.setTargetField(to);
                mapping.setRenameTo(to); // 重命名
                mapping.setRequired(!ignoreMissing);
                mapping.setKeepOriginal(keepOriginal);
                
                fieldMappings.add(mapping);
            }
        }
    }
    
    private static void parseExtractFields(JsonNode processor, List<FieldMapping> fieldMappings) {
        JsonNode fieldsNode = processor.get("fields");
        
        if (fieldsNode != null && fieldsNode.isArray()) {
            for (JsonNode fieldNode : fieldsNode) {
                String from = fieldNode.get("from").asText();
                String to = fieldNode.has("to") ? fieldNode.get("to").asText() : from;
                String defaultValue = fieldNode.has("default_value") ? 
                                    fieldNode.get("default_value").asText() : null;
                boolean keepOriginal = fieldNode.has("keep_original") && 
                                     fieldNode.get("keep_original").asBoolean(false); // 默认为false
                
                FieldMapping mapping = new FieldMapping();
                mapping.setSourceField(from);
                mapping.setTargetField(to);
                mapping.setRenameTo(to);
                mapping.setDefaultValue(defaultValue);
                mapping.setKeepOriginal(keepOriginal);
                
                fieldMappings.add(mapping);
            }
        }
    }
    
    private static void parseDropFields(JsonNode processor, List<String> excludedFields) {
        JsonNode fieldsNode = processor.get("fields");
        
        if (fieldsNode != null && fieldsNode.isArray()) {
            for (JsonNode fieldNode : fieldsNode) {
                excludedFields.add(fieldNode.asText());
            }
        }
    }
}