package priv.eric.plugin.filter.rename;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import priv.eric.pelee.domain.model.Filter;
import priv.eric.pelee.domain.model.FilterContext;
import priv.eric.pelee.domain.model.PipelineEvent;

import java.util.Map;

/**
 * Description: 重命名字段过滤器，将指定字段按照映射关系重命名
 * 使用组合模式实现Filter接口
 *
 * @author EricTowns
 * @date 2026/2/4 22:05
 */
public class RenameFieldFilter implements Filter {

    private final RenameFieldConfig config;

    /**
     * 构造函数
     */
    public RenameFieldFilter(RenameFieldConfig config) {
        this.config = config;
    }

    /**
     * 处理事件，执行字段重命名
     */
    @Override
    public PipelineEvent filter(PipelineEvent event, FilterContext context) {
        if (!event.hasData()) {
            return event;
        }

        JsonNode data = event.getData();
        if (!data.isObject()) {
            return event; // 只处理对象类型
        }

        ObjectNode objectNode = (ObjectNode) data;
        Map<String, String> mappings = config.getMappings();

        // 遍历映射关系，重命名字段
        for (Map.Entry<String, String> entry : mappings.entrySet()) {
            String oldFieldName = entry.getKey();
            String newFieldName = entry.getValue();

            if (objectNode.has(oldFieldName)) {
                // 获取旧字段的值
                JsonNode value = objectNode.get(oldFieldName);
                // 移除旧字段
                objectNode.remove(oldFieldName);
                // 添加新字段
                objectNode.set(newFieldName, value);
                
                // 可选：记录重命名操作到上下文
                if (context.hasState("renameLog")) {
                    @SuppressWarnings("unchecked")
                    java.util.List<String> renameLog = (java.util.List<String>) context.getState("renameLog");
                    renameLog.add(String.format("Renamed '%s' to '%s'", oldFieldName, newFieldName));
                }
            }
        }

        return event.withData(objectNode);
    }

    /**
     * 重命名字段配置
     */
    @Data
    public static class RenameFieldConfig {
        private Map<String, String> mappings;
        private boolean enableRenameLog;
    }
}