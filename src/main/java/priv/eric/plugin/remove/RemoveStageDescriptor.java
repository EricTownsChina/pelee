package priv.eric.plugin.remove;

import com.fasterxml.jackson.databind.JsonNode;
import priv.eric.pelee.domain.model.Stage;
import priv.eric.pelee.domain.model.StageDescriptor;

/**
 * Description: TODO
 *
 * @author EricTowns
 * @date 2026/1/27 00:52
 */
public class RemoveStageDescriptor implements StageDescriptor<JsonNode> {
    @Override
    public String type() {
        return "remove";
    }

    @Override
    public String desc() {
        return "删除属性";
    }

    @Override
    public Class<?> configClass() {
        return RemoveConfig.class;
    }

    @Override
    public Stage<JsonNode> create(Object config) {
        return new RemoveStage((RemoveConfig) config);
    }
}
