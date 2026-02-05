package priv.eric.pelee.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import priv.eric.pelee.application.factory.ProcessorFactory;
import priv.eric.pelee.domain.model.Pipeline;
import priv.eric.pelee.domain.model.Processor;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * desc:
 *
 * @author EricTownsChina@outlook.com
 * @date 2026-02-05 20:25
 */
@Component
public class PipelineManager {

    private final ProcessorFactory processorFactory;

    @Value("${processor.dialog-record.path:}")
    private String baseDir;

    public PipelineManager(ProcessorFactory processorFactory) {
        this.processorFactory = processorFactory;
    }

    @PostConstruct
    public void init() throws IOException {
        Path basePath = Paths.get(baseDir);
        try (Stream<Path> paths = Files.walk(basePath)) {
            paths.filter(p -> {
                String pathName = p.getFileName().toString();
                return pathName.endsWith(".json");
            })
        }
    }


    public Pipeline create(ArrayNode node) {
        List<Processor<?>> processors = new ArrayList<>(node.size());
        for (JsonNode jsonNode : node) {
            if (jsonNode instanceof ObjectNode configNode) {
                Processor<?> processor = processorFactory.create(configNode);
                processors.add(processor);
            }
        }
        return new Pipeline(processors);
    }

}
