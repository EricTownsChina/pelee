package priv.eric.pelee.infrastructure.factory.dialogrecord.processor;

import org.springframework.util.StringUtils;
import priv.eric.pelee.domain.dialogrecord.model.FieldProcessType;
import priv.eric.pelee.domain.dialogrecord.model.FieldProcessor;
import priv.eric.pelee.domain.dialogrecord.model.RenameFieldProcessor;
import priv.eric.pelee.infrastructure.factory.dialogrecord.FieldProcessorFactoryRegistry;
import priv.eric.pelee.infrastructure.pojo.dialogrecord.FieldProcessPO;

/**
 * Description: 重命名字段处理器工厂
 *
 * @author EricTowns
 * @date 2026/1/22 14:53
 */
public class RenameFieldProcessorFactory extends DefaultFieldProcessorFactory {

    private static final String ROOT_PATH = "/";

    public RenameFieldProcessorFactory(FieldProcessorFactoryRegistry registry) {
        super(registry);
    }

    @Override
    public FieldProcessType type() {
        return FieldProcessType.RENAME;
    }

    @Override
    public FieldProcessor create(FieldProcessPO fieldProcessPO) {
        String path = fieldProcessPO.getPath();
        String source = fieldProcessPO.getSource();
        String dest = fieldProcessPO.getDest();
        return new RenameFieldProcessor(path, source, dest);
    }

    @Override
    boolean validate(FieldProcessPO fieldProcessPO) {
        if (null == fieldProcessPO) {
            return false;
        }
        String source = fieldProcessPO.getSource();
        String dest = fieldProcessPO.getDest();
        return StringUtils.hasText(source) && StringUtils.hasText(dest);
    }

    @Override
    void pre(FieldProcessPO fieldProcessPO) {
        String path = fieldProcessPO.getPath();
        if (ROOT_PATH.equals(path)) {
            fieldProcessPO.setPath(null);
        }
    }
}
