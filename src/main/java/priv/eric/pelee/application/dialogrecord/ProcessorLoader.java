package priv.eric.pelee.application.dialogrecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import priv.eric.pelee.domain.dialogrecord.model.FieldProcessor;
import priv.eric.pelee.domain.dialogrecord.model.Processor;
import priv.eric.pelee.infrastructure.pojo.dialogrecord.ProcessorPO;
import priv.eric.pelee.infrastructure.repository.FileDataRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

    public ProcessorLoader(FileDataRepository fileDataRepository,
                           ProcessorProperties processorProperties) {
        this.fileDataRepository = fileDataRepository;
        this.processorProperties = processorProperties;
    }


    @PostConstruct
    public ProcessorPO load() throws IOException {
        if (processorProperties.enable()) {
            final String path = processorProperties.getPath();
            final Path filePath = Paths.get(path);
            ProcessorPO processorPO = fileDataRepository.loadJsonFile(filePath, ProcessorPO.class);

            LOGGER.info(".");
        }
        return ProcessorPO.empty();
    }

    @Override
    public List<FieldProcessor> getFieldProcessors() {
        return null;
    }

}
