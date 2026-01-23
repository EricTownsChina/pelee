package priv.eric.pelee.domain.dialogrecord.model.fieldprocessor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import priv.eric.pelee.domain.dialogrecord.model.FieldProcessType;

import java.util.Objects;

/**
 * Description: 移动字段处理器
 *
 * @author EricTowns
 * @date 2026/1/22 21:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MoveFieldProcessor extends FieldProcessor {

    private String sourcePath;

    private String source;

    private String destPath;

    private String dest;

    private Boolean keepSource;

    public MoveFieldProcessor() {
        // 默认构造函数
        this.sourcePath = "/";
        this.destPath = "/";
        this.keepSource = false;
    }

    public MoveFieldProcessor(String sourcePath, String source, String destPath, String dest, Boolean keepSource) {
        this.sourcePath = sourcePath;
        this.source = source;
        this.destPath = destPath;
        this.dest = dest;
        this.keepSource = keepSource;
    }

    @Override
    public FieldProcessType getType() {
        return FieldProcessType.MOVE;
    }

    @Override
    FieldProcessType type() {
        return FieldProcessType.MOVE;
    }

    @Override
    public void process(JsonNode dialogRecord) {
        if (Objects.equals(sourcePath, destPath)) {
            return;
        }
        final JsonNode sourcePathNode = dialogRecord.at(sourcePath);
        if (!nodeIsObjectNode(sourcePathNode)) {
            return;
        }
        final JsonNode sourceNode = sourcePathNode.at(source);
        if (sourceNode.isMissingNode()) {
            return;
        }
        final JsonNode destPathNode = dialogRecord.at(destPath);
        final ObjectNode destPathObjectNode = objectNode(destPathNode);
        if (null == destPathObjectNode) {
            return;
        }
        destPathObjectNode.set(dest, sourceNode);
        // 如果不保留源字段，则删除源字段
        if (Boolean.FALSE.equals(keepSource)) {
            ObjectNode sourcePathObjectNode = objectNode(sourcePathNode);
            if (null != sourcePathObjectNode) {
                sourcePathObjectNode.remove(source);
            }
        }
    }

}