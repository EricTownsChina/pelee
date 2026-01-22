package priv.eric.pelee.infrastructure.factory.dialogrecord.processor;

import org.springframework.stereotype.Component;
import priv.eric.pelee.domain.dialogrecord.model.FieldProcessType;
import priv.eric.pelee.domain.dialogrecord.model.FieldProcessor;
import priv.eric.pelee.infrastructure.factory.dialogrecord.FieldProcessorFactoryRegistry;
import priv.eric.pelee.infrastructure.factory.dialogrecord.FieldProcessorFactory;
import priv.eric.pelee.infrastructure.pojo.dialogrecord.FieldProcessPO;

/**
 * Description: todo
 *
 * @author EricTowns
 * @date 2026/1/22 15:02
 */
public abstract class DefaultFieldProcessorFactory implements FieldProcessorFactory {

    @Override
    public abstract FieldProcessType type();

    @Override
    public abstract FieldProcessor create(FieldProcessPO fieldProcessPO);

    abstract boolean validate(FieldProcessPO fieldProcessPO);

    void pre (FieldProcessPO fieldProcessPO) {
        //
    }

    void post(FieldProcessPO fieldProcessPO, FieldProcessor fieldProcessor) {
        //
    }

    public void register(FieldProcessorFactoryRegistry registry) {
        registry.register(this);
    }

}
