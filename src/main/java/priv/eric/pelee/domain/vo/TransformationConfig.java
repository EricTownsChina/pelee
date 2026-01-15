package priv.eric.pelee.domain.vo;

import java.util.List;

/**
 * 转换配置
 */
public class TransformationConfig {
    private List<FieldMapping> fieldMappings;
    private List<String> excludedFields;
    private String sourceField;
    private String targetField;

    public TransformationConfig() {
    }

    public TransformationConfig(List<FieldMapping> fieldMappings, List<String> excludedFields, String sourceField, String targetField) {
        this.fieldMappings = fieldMappings;
        this.excludedFields = excludedFields;
        this.sourceField = sourceField;
        this.targetField = targetField;
    }

    // Getters and Setters
    public List<FieldMapping> getFieldMappings() {
        return fieldMappings;
    }

    public void setFieldMappings(List<FieldMapping> fieldMappings) {
        this.fieldMappings = fieldMappings;
    }

    public List<String> getExcludedFields() {
        return excludedFields;
    }

    public void setExcludedFields(List<String> excludedFields) {
        this.excludedFields = excludedFields;
    }

    public String getSourceField() {
        return sourceField;
    }

    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }

    public String getTargetField() {
        return targetField;
    }

    public void setTargetField(String targetField) {
        this.targetField = targetField;
    }
}