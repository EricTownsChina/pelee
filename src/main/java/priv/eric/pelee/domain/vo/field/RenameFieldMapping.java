package priv.eric.pelee.domain.vo.field;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Description: TODO
 *
 * @author EricTowns
 * @date 2026/1/15 22:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RenameFieldMapping extends FieldMapping {

    /**
     * 目标字段
     */
    private String dest;

}
