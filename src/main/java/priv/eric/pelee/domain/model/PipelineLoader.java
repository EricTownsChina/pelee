package priv.eric.pelee.domain.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: TODO
 *
 * @author EricTowns
 * @date 2026/1/27 00:19
 */
public class PipelineLoader {

    private final StageFactory stageFactory;

    public PipelineLoader(StageFactory stageFactory) {
        this.stageFactory = stageFactory;
    }

    public List<Stage> load(JsonNode root) {
        List<Stage> stages = new ArrayList<>();
        for (JsonNode node : root.get("_processors")) {
            stages.add(stageFactory.create(node));
        }
        return stages;
    }

}
