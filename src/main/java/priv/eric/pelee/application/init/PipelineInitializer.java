package priv.eric.pelee.application.init;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import priv.eric.pelee.application.factory.PipelineFactory;
import priv.eric.pelee.domain.model.Pipeline;
import priv.eric.pelee.infrastructure.util.JsonUtil;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Description: 流水线注册表，负责加载和管理所有预定义的流水线
 *
 * @author EricTowns
 * @date 2026/1/27 18:30
 */
@Component
public class PipelineInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PipelineInitializer.class);
    private final Map<String, Pipeline> pipelineMap = new ConcurrentHashMap<>();
    private final PipelineFactory pipelineFactory = new PipelineFactory();

    @Value("${pipeline.base-dir:}")
    private String pipelineBaseDir;

    @PostConstruct
    public void initializePipelines() throws IOException {
        Path pipelineDir = Paths.get(pipelineBaseDir);
        try (Stream<Path> paths = Files.walk(pipelineDir)) {
            paths.filter(path -> path.getFileName().toString().endsWith(".json")).forEach(this::loadPipelineFromFile);
        }
    }

    private void loadPipelineFromFile(Path filePath) {
        try {
            String fileName = filePath.getFileName().toString();
            String pipelineId = fileName.substring(0, fileName.lastIndexOf('.')); // 移除扩展名作为ID
            String pipelineConfig = Files.readString(filePath);

            JsonNode node = JsonUtil.parseToJsonNode(pipelineConfig);
            Pipeline pipeline = pipelineFactory.create(node);
            pipelineMap.put(pipelineId, pipeline);

            LOGGER.info("Loaded pipeline id: {}, path: {}", pipelineId, filePath);
        } catch (Exception e) {
            LOGGER.error("Failed to load pipeline from file: {}, error: {}", filePath, e.getMessage(), e);
        }
    }

    public Pipeline get(String id) {
        return pipelineMap.get(id);
    }

    public boolean contains(String id) {
        return pipelineMap.containsKey(id);
    }

}