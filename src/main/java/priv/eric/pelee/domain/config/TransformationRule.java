package priv.eric.pelee.domain.config;

import java.util.List;
import java.util.Map;

/**
 * 转换规则配置 - 参考filebeat风格
 * 示例配置:
 * {
 *   "processors": [
 *     {
 *       "convert": {
 *         "fields": [
 *           {"from": "original.field", "to": "new.field", "rename": true}
 *         ]
 *       }
 *     },
 *     {
 *       "drop_fields": {
 *         "fields": ["field1", "field2"]
 *       }
 *     }
 *   ]
 * }
 */
public class TransformationRule {
    private List<Processor> processors;

    public TransformationRule() {
    }

    public TransformationRule(List<Processor> processors) {
        this.processors = processors;
    }

    // Getters and Setters
    public List<Processor> getProcessors() {
        return processors;
    }

    public void setProcessors(List<Processor> processors) {
        this.processors = processors;
    }

    /**
     * 处理器抽象类
     */
    public static class Processor {
        private String name;
        private Map<String, Object> config;

        public Processor() {
        }

        public Processor(String name, Map<String, Object> config) {
            this.name = name;
            this.config = config;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<String, Object> getConfig() {
            return config;
        }

        public void setConfig(Map<String, Object> config) {
            this.config = config;
        }
    }
}