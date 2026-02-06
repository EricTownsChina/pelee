package priv.eric.pelee.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import priv.eric.pelee.application.factory.ProcessorFactory;
import priv.eric.pelee.domain.model.Pipeline;
import priv.eric.pelee.infrastructure.util.JsonUtil;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            }).toList();
        }
    }

    public JsonNode readConfig(Path path) throws IOException {
        String config = Files.readString(path);
        JsonNode node = JsonUtil.convertValue(config, JsonNode.class);

    }





}
