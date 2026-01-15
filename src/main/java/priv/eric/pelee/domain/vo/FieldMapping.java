package priv.eric.pelee.domain.vo;

/**
 * 字段映射配置
 */
public class FieldMapping {
    private String sourceField;      // 源字段路径，支持嵌套路径，如 "user.name"
    private String targetField;      // 目标字段名
    private String renameTo;         // 重命名目标
    private String defaultValue;     // 默认值
    private boolean required;        // 是否必需
    private boolean keepOriginal = false; // 是否保留原字段，默认为false

    public FieldMapping() {
    }

    public FieldMapping(String sourceField, String targetField, String renameTo, String defaultValue, boolean required) {
        this.sourceField = sourceField;
        this.targetField = targetField;
        this.renameTo = renameTo;
        this.defaultValue = defaultValue;
        this.required = required;
    }

    // Getters and Setters
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

    public String getRenameTo() {
        return renameTo;
    }

    public void setRenameTo(String renameTo) {
        this.renameTo = renameTo;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isKeepOriginal() {
        return keepOriginal;
    }

    public void setKeepOriginal(boolean keepOriginal) {
        this.keepOriginal = keepOriginal;
    }
}