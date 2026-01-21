package priv.eric.pelee.infrastructure.pojo.dialogrecord;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 对话记录处理映射
 *
 * @author EricTowns
 * @date 2026/1/20 20:36
 */
@Data
public class ProcessorPO {

    private static final ProcessorPO EMPTY;

    static {
        EMPTY = new ProcessorPO();
        EMPTY.setFields(new ArrayList<>(0));
    }

    public static ProcessorPO empty() {
        return EMPTY;
    }

    @JsonProperty("fields")
    private List<FieldProcessPO> fields;

}
