package priv.eric.plugin.stage.rename;

import com.fasterxml.jackson.databind.JsonNode;
import priv.eric.pelee.domain.model.Stage;
import priv.eric.pelee.domain.model.StageDescriptor;

/**
 * Description: todo
 *
 * @author EricTowns
 * @date 2026/1/27 10:36
 */
public class RenameStageDescriptor implements StageDescriptor {

    @Override
    public String code() {
        return "rename";
    }

    @Override
    public String desc() {
        return "字段重命名";
    }

    @Override
    public Class<?> configClass() {
        return RenameConfig.class;
    }

    @Override
    public Stage create(Object config) {
        return new RenameStage((RenameConfig) config);
    }

}
