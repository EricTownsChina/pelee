package priv.eric.plugin.filter.remove;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import priv.eric.pelee.domain.model.Filter;
import priv.eric.pelee.domain.model.FilterContext;
import priv.eric.pelee.domain.model.PipelineEvent;

import java.util.List;

/**
 * Description: 删除字段过滤器，移除指定的字段
 * 使用组合模式实现Filter接口
 *
 * @author EricTowns
 * @date 2026/2/4 22:10
 */
public class RemoveFieldFilter implements Filter {

    private final RemoveFieldConfig config;

    /**
     * 构造函数
     */
    public RemoveFieldFilter(RemoveFieldConfig config) {
        this.config = config;
    }

    /**
     * 处理事件，执行字段删除
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
        List<String> fieldsToRemove = config.getFields();

        // 删除指定字段
        for (String field : fieldsToRemove) {
            if (objectNode.has(field)) {
                objectNode.remove(field);
                
                // 可选：记录删除操作到上下文
                if (config.isEnableRemoveLog() && context.hasState("removeLog")) {
                    @SuppressWarnings("unchecked")
                    java.util.List<String> removeLog = (java.util.List<String>) context.getState("removeLog");
                    removeLog.add("Removed field: " + field);
                }
            }
        }

        return event.withData(objectNode);
    }

    /**
     * 删除字段配置
     */
    @Data
    public static class RemoveFieldConfig {
        private List<String> fields;
        private boolean enableRemoveLog = false;
    }
}