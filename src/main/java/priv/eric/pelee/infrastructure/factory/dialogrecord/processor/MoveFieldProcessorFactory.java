package priv.eric.pelee.infrastructure.factory.dialogrecord.processor;

import org.springframework.stereotype.Component;
import priv.eric.pelee.domain.dialogrecord.model.FieldProcessType;
import priv.eric.pelee.domain.dialogrecord.model.fieldprocessor.FieldProcessor;
import priv.eric.pelee.domain.dialogrecord.model.fieldprocessor.MoveFieldProcessor;
import priv.eric.pelee.infrastructure.pojo.dialogrecord.FieldProcessPO;

/**
 * Description: todo
 *
 * @author EricTowns
 * @date 2026/1/23 17:11
 */
@Component
public class MoveFieldProcessorFactory extends DefaultFieldProcessorFactory {

    @Override
    public FieldProcessType type() {
        return FieldProcessType.MOVE;
    }

    @Override
    public FieldProcessor doCreate(FieldProcessPO fieldProcessPO) {
        MoveFieldProcessor processor = new MoveFieldProcessor();
        processor.setSourcePath(fieldProcessPO.getPath());
        processor.setSource(fieldProcessPO.getSource());
        processor.setDestPath(fieldProcessPO.getPath());
        processor.setDest(fieldProcessPO.getDest());
        if (fieldProcessPO.getAddition() != null) {
            processor.setKeepSource(fieldProcessPO.getAddition().getKeepSource());
        }
        return processor;
    }

    @Override
    boolean validate(FieldProcessPO fieldProcessPO) {
        if (null == fieldProcessPO) {
            return false;
        }
        String source = fieldProcessPO.getSource();
        String dest = fieldProcessPO.getDest();
        return org.springframework.util.StringUtils.hasText(source) && org.springframework.util.StringUtils.hasText(dest);
    }
}
