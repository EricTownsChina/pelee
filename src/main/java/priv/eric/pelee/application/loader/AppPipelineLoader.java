package priv.eric.pelee.application.loader;

import com.fasterxml.jackson.databind.JsonNode;
import priv.eric.pelee.application.factory.StageFactory;
import priv.eric.pelee.application.registry.PluginRegistry;
import priv.eric.pelee.domain.model.Pipeline;
import priv.eric.pelee.domain.model.Stage;
import priv.eric.pelee.domain.model.StageContext;
import priv.eric.pelee.infrastructure.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description: 应用层流水线加载器，负责将配置转换为Pipeline领域对象
 *
 * @author EricTowns
 * @date 2026/1/27 17:35
 */
public class AppPipelineLoader {

    private static final String DEFAULT_CONTEXT_CODE = "sequence";

    public Pipeline loadPipeline(String pipelineConfig) {
        JsonNode configNode = JsonUtil.parseToJsonNode(pipelineConfig);
        
        // 创建Stage工厂
        StageFactory factory = createStageFactory();
        
        // 解析阶段列表
        List<Stage> stages = parseStages(configNode, factory);
        
        // 解析上下文
        StageContext context = parseContext(configNode);

        // 创建并返回Pipeline领域对象
        return new Pipeline(Map.of(), stages, context);
    }

    /**
     * 创建Stage工厂
     */
    private StageFactory createStageFactory() {
        List<priv.eric.pelee.domain.model.StageDescriptor> descriptors = PluginRegistry.getAllStageDescriptors();
        return new StageFactory(descriptors);
    }

    /**
     * 解析阶段列表
     */
    private List<Stage> parseStages(JsonNode configNode, StageFactory factory) {
        List<Stage> stages = new ArrayList<>();
        JsonNode processorsNode = configNode.get("processors");
        
        if (processorsNode != null) {
            for (JsonNode node : processorsNode) {
                stages.add(factory.create(node));
            }
        }
        
        return stages;
    }

    /**
     * 解析上下文
     */
    private StageContext parseContext(JsonNode configNode) {
        String contextCode = DEFAULT_CONTEXT_CODE;
        if (configNode.has("context")) {
            contextCode = configNode.get("context").asText();
        }
        
        StageContext context = PluginRegistry.getStageContextByCode(contextCode);
        if (context == null) {
            throw new IllegalArgumentException("Unknown context code: " + contextCode);
        }
        
        return context;
    }
}