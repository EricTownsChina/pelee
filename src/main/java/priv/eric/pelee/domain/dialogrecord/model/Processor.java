package priv.eric.pelee.domain.dialogrecord.model;

import priv.eric.pelee.domain.dialogrecord.model.fieldprocessor.FieldProcessor;

import java.util.List;

/**
 * Description: TODO
 *
 * @author EricTowns
 * @date 2026/1/18 22:27
 */
public interface Processor {

    List<? extends FieldProcessor> getFieldProcessors();

}
