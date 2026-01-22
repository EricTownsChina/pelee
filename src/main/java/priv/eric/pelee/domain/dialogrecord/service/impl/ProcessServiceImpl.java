package priv.eric.pelee.domain.dialogrecord.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import priv.eric.pelee.domain.dialogrecord.model.FieldProcessor;
import priv.eric.pelee.domain.dialogrecord.model.Processor;
import priv.eric.pelee.domain.dialogrecord.service.ProcessService;

import java.util.List;

/**
 * Description: todo
 *
 * @author EricTowns
 * @date 2026/1/22 19:59
 */
@Service
public class ProcessServiceImpl implements ProcessService {

    private final Processor processor;

    public ProcessServiceImpl(Processor processor) {
        this.processor = processor;
    }

    @Override
    public void process(JsonNode dialogRecord) {

        List<? extends FieldProcessor> fieldProcessors = processor.getFieldProcessors();
        for (FieldProcessor fieldProcessor : fieldProcessors) {
            fieldProcessor.process(dialogRecord);
        }
    }

}
