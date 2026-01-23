package priv.eric.pelee.domain.dialogrecord.model.fieldprocessor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import priv.eric.pelee.domain.dialogrecord.model.FieldProcessType;
import priv.eric.pelee.domain.dialogrecord.model.spi.FieldProcessConfig;
import priv.eric.pelee.domain.dialogrecord.model.spi.FieldProcessorSPI;

import java.util.Objects;

/**
 * Description: 重命名字段处理器
 *
 * @author EricTowns
 * @date 2026/1/18 20:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RenameFieldProcessor extends FieldProcessor implements FieldProcessorSPI {

    private String path;

    private String source;

    private String dest;

    public RenameFieldProcessor(String path, String source, String dest) {
        this.path = path;
        this.source = source;
        this.dest = dest;
    }

    @Override
    public FieldProcessType getType() {
        return FieldProcessType.RENAME;
    }

    @Override
    FieldProcessType type() {
        return FieldProcessType.RENAME;
    }

    @Override
    public void process(JsonNode dialogRecord) {
        if (Objects.equals(source, dest)) {
            return;
        }
        final JsonNode pathNode = dialogRecord.at(path);
        if (null == pathNode || pathNode.isMissingNode() || !(pathNode instanceof ObjectNode pathObjNode)) {
            // 如果路径不存在或不是一个对象，则返回
            return;
        }
        JsonNode sourceNode = pathNode.get(source);
        if (null == sourceNode) {
            // 如果源字段不存在，则返回
            return;
        }
        pathObjNode.set(dest, sourceNode);
        pathObjNode.remove(source);
    }

    @Override
    public void process(JsonNode dialogRecord, FieldProcessConfig config) {
        this.path = config.getPath();
        this.source = config.getSource();
        this.dest = config.getDest();
        process(dialogRecord);
    }

}