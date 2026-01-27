package priv.eric.plugin.stage.remove;

import com.fasterxml.jackson.databind.JsonNode;
import priv.eric.pelee.domain.model.Stage;
import priv.eric.pelee.domain.model.StageDescriptor;

/**
 * Description: TODO
 *
 * @author EricTowns
 * @date 2026/1/27 00:52
 */
public class RemoveStageDescriptor implements StageDescriptor {
    @Override
    public String code() {
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
    public Stage create(Object config) {
        return new RemoveStage((RemoveConfig) config);
    }
}
