package priv.eric.pelee.application.loader;

import com.fasterxml.jackson.databind.JsonNode;
import priv.eric.pelee.application.factory.StageFactory;
import priv.eric.pelee.domain.model.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 流水线阶段加载器，负责从配置中加载阶段列表
 *
 * @author EricTowns
 * @date 2026/1/27 00:19
 */
public class PipelineStageLoader {

    private final StageFactory stageFactory;

    public PipelineStageLoader(StageFactory stageFactory) {
        this.stageFactory = stageFactory;
    }

    public List<Stage> loadStages(JsonNode root) {
        List<Stage> stages = new ArrayList<>();
        for (JsonNode node : root.get("_processors")) {
            stages.add(stageFactory.create(node));
        }
        return stages;
    }

}