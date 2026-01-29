package priv.eric.pelee.application.loader;

import com.fasterxml.jackson.databind.JsonNode;
import priv.eric.pelee.application.util.PluginRegistry;
import priv.eric.pelee.domain.model.Pipeline;
import priv.eric.pelee.domain.model.StageContext;
import priv.eric.pelee.domain.model.StageFactory;
import priv.eric.pelee.infrastructure.util.JsonUtil;

import java.util.List;
import java.util.Map;

/**
 * Description: 应用层流水线加载器，复用domain层StageFactory和Pipeline
 *
 * @author EricTowns
 * @date 2026/1/27 17:35
 */
public class AppPipelineLoader {

    public Pipeline loadPipeline(String pipelineConfig) {
        JsonNode configNode = JsonUtil.parseToJsonNode(pipelineConfig);
        
        // 获取所有可用的描述符，复用PluginRegistry
        List<priv.eric.pelee.domain.model.StageDescriptor> descriptors = PluginRegistry.getAllStageDescriptors();
        
        // 复用domain层的StageFactory
        StageFactory factory = new StageFactory(descriptors);

        // 直接使用配置创建流水线阶段
        JsonNode processorsNode = configNode.get("processors");
        java.util.List<priv.eric.pelee.domain.model.Stage> stages =
            new java.util.ArrayList<>();
        
        if (processorsNode != null) {
            for (JsonNode node : processorsNode) {
                stages.add(factory.create(node));
            }
        }

        // 获取上下文类型，默认为sequence
        String contextCode = "sequence"; // 默认上下文
        if (configNode.has("context")) {
            contextCode = configNode.get("context").asText();
        }
        
        // 从PluginRegistry获取指定的StageContext实现
        StageContext context = PluginRegistry.getStageContextByCode(contextCode);
        if (context == null) {
            throw new IllegalArgumentException("Unknown context code: " + contextCode);
        }

        // 直接返回domain层的Pipeline
        return new Pipeline(Map.of(), stages, context);
    }
}