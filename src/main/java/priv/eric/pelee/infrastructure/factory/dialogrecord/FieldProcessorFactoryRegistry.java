package priv.eric.pelee.infrastructure.factory.dialogrecord;

import org.springframework.stereotype.Component;
import priv.eric.pelee.domain.dialogrecord.model.FieldProcessType;
import priv.eric.pelee.domain.dialogrecord.model.fieldprocessor.FieldProcessor;
import priv.eric.pelee.domain.dialogrecord.model.spi.FieldProcessConfig;
import priv.eric.pelee.domain.dialogrecord.model.spi.FieldProcessorSPI;
import priv.eric.pelee.infrastructure.pojo.dialogrecord.FieldProcessPO;
import priv.eric.pelee.infrastructure.spi.FieldProcessorSPILoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 字段处理器工厂注册表 - 现在结合SPI机制
 *
 * @author EricTowns
 * @date 2026/1/22 16:02
 */
@Component
public class FieldProcessorFactoryRegistry {

    private final Map<FieldProcessType, FieldProcessorFactory> factories;
    private final FieldProcessorSPILoader spiLoader;

    public FieldProcessorFactoryRegistry(FieldProcessorSPILoader spiLoader) {
        this.factories = new HashMap<>();
        this.spiLoader = spiLoader;
    }

    public void register(FieldProcessorFactory factory) {
        this.factories.put(factory.type(), factory);
    }

    public FieldProcessor create(FieldProcessPO fieldProcessPO) {
        // 优先使用传统工厂方式
        FieldProcessorFactory factory = this.factories.get(fieldProcessPO.getType());
        if (null != factory) {
            return factory.create(fieldProcessPO);
        }
        
        // 如果没有找到传统工厂，则尝试使用SPI机制
        FieldProcessorSPI spi = spiLoader.getImplementation(fieldProcessPO.getType());
        if (null != spi) {
            // 创建包装器将SPI实现转换为FieldProcessor
            return createFromSPI(spi, fieldProcessPO);
        }
        
        return null;
    }
    
    private FieldProcessor createFromSPI(FieldProcessorSPI spi, FieldProcessPO fieldProcessPO) {
        // 根据SPI类型创建相应的FieldProcessor实例
        FieldProcessConfig config = new FieldProcessConfig();
        config.setPath(fieldProcessPO.getPath());
        config.setSource(fieldProcessPO.getSource());
        config.setDest(fieldProcessPO.getDest());
        if (fieldProcessPO.getAddition() != null) {
            Map<String, Object> additionMap = new java.util.HashMap<>();
            additionMap.put("keepSource", fieldProcessPO.getAddition().getKeepSource());
            additionMap.put("defaultValue", fieldProcessPO.getAddition().getDefaultValue());
            config.setAddition(additionMap);
        } else {
            config.setAddition(new java.util.HashMap<>());
        }
        
        // 根据SPI类型创建对应的处理器
        switch (fieldProcessPO.getType()) {
            case RENAME:
                priv.eric.pelee.domain.dialogrecord.model.fieldprocessor.RenameFieldProcessor renameProcessor = 
                    new priv.eric.pelee.domain.dialogrecord.model.fieldprocessor.RenameFieldProcessor(
                        fieldProcessPO.getPath(), fieldProcessPO.getSource(), fieldProcessPO.getDest());
                return renameProcessor;
            case MOVE:
                priv.eric.pelee.domain.dialogrecord.model.fieldprocessor.MoveFieldProcessor moveProcessor = 
                    new priv.eric.pelee.domain.dialogrecord.model.fieldprocessor.MoveFieldProcessor();
                // 设置move处理器的属性
                Map<String, Object> addition = fieldProcessPO.getAddition() != null ? 
                    config.getAddition() : new java.util.HashMap<>();
                moveProcessor.setSourcePath((String) addition.getOrDefault("sourcePath", fieldProcessPO.getPath()));
                moveProcessor.setSource(fieldProcessPO.getSource());
                moveProcessor.setDestPath((String) addition.getOrDefault("destPath", fieldProcessPO.getPath()));
                moveProcessor.setDest(fieldProcessPO.getDest());
                moveProcessor.setKeepSource((Boolean) addition.getOrDefault("keepSource", false));
                return moveProcessor;
            case DROP:
                // TODO: 添加DropFieldProcessor
                return null;
            case CUSTOMIZE:
                // TODO: 添加CustomizeFieldProcessor
                return null;
            default:
                throw new UnsupportedOperationException("Unsupported field process type: " + fieldProcessPO.getType());
        }
    }

}
