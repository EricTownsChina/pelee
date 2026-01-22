package priv.eric.pelee.infrastructure.factory.dialogrecord;

import org.springframework.stereotype.Component;
import priv.eric.pelee.domain.dialogrecord.model.FieldProcessType;
import priv.eric.pelee.domain.dialogrecord.model.FieldProcessor;
import priv.eric.pelee.infrastructure.pojo.dialogrecord.FieldProcessPO;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: todo
 *
 * @author EricTowns
 * @date 2026/1/22 16:02
 */
@Component
public class FieldProcessorFactoryRegistry {

    private final Map<FieldProcessType, FieldProcessorFactory> factories;

    public FieldProcessorFactoryRegistry() {
        this.factories = new HashMap<>();
    }

    public void register(FieldProcessorFactory factory) {
        this.factories.put(factory.type(), factory);
    }

    public FieldProcessor create(FieldProcessPO fieldProcessPO) {
        FieldProcessorFactory factory = this.factories.get(fieldProcessPO.getType());
        if (null == factory) {
            return null;
        }
        return factory.create(fieldProcessPO);
    }

}
