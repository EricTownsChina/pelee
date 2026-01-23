package priv.eric.pelee.infrastructure.convert.dialogrecord;

import priv.eric.pelee.domain.dialogrecord.model.fieldprocessor.FieldProcessor;
import priv.eric.pelee.infrastructure.pojo.dialogrecord.FieldProcessPO;

/**
 * Description: todo
 *
 * @author EricTowns
 * @date 2026/1/22 12:02
 */
public abstract class FieldProcessorConvertor {

    abstract FieldProcessor convert(FieldProcessPO fieldProcessPO);

}
