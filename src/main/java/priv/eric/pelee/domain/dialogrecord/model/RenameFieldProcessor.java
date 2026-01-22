package priv.eric.pelee.domain.dialogrecord.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;

/**
 * Description: 重命名字段处理器
 *
 * @author EricTowns
 * @date 2026/1/18 20:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RenameFieldProcessor extends FieldProcessor {

    private final String path;

    private final String source;

    private final String dest;

    public RenameFieldProcessor(String path, String source, String dest) {
        this.path = path;
        this.source = source;
        this.dest = dest;
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

}