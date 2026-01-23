package priv.eric.pelee.application.dialogrecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import priv.eric.pelee.domain.dialogrecord.model.fieldprocessor.FieldProcessor;
import priv.eric.pelee.domain.dialogrecord.model.Processor;
import priv.eric.pelee.infrastructure.factory.dialogrecord.FieldProcessorFactory;
import priv.eric.pelee.infrastructure.factory.dialogrecord.FieldProcessorFactoryRegistry;
import priv.eric.pelee.infrastructure.pojo.dialogrecord.ProcessorPO;
import priv.eric.pelee.infrastructure.repository.FileDataRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: todo
 *
 * @author EricTowns
 * @date 2026/1/20 20:48
 */
@Service
public class ProcessorLoader implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorLoader.class);

    private final FileDataRepository fileDataRepository;
    private final ProcessorProperties processorProperties;
    private final FieldProcessorFactoryRegistry registry;
    private final List<FieldProcessorFactory> fieldProcessFactories;

    private ProcessorPO processorPO;
    private List<FieldProcessor> fieldProcessors;

    public ProcessorLoader(FileDataRepository fileDataRepository,
                           ProcessorProperties processorProperties,
                           FieldProcessorFactoryRegistry registry,
                           List<FieldProcessorFactory> fieldProcessFactories) {
        this.fileDataRepository = fileDataRepository;
        this.processorProperties = processorProperties;
        this.registry = registry;
        this.fieldProcessFactories = fieldProcessFactories;
        this.processorPO = ProcessorPO.empty();
    }


    @PostConstruct
    public void load() throws IOException {
        fieldProcessFactories.forEach(registry::register);
        if (processorProperties.enable()) {
            final Path filePath = Paths.get(processorProperties.getPath());
            this.processorPO = fileDataRepository.loadJsonFile(filePath, ProcessorPO.class);
            this.fieldProcessors = this.processorPO.getFields().stream().map(registry::create).collect(Collectors.toList());
        }
    }

    @Override
    public List<FieldProcessor> getFieldProcessors() {
        return this.fieldProcessors;
    }

}
