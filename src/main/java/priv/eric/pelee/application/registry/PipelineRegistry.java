package priv.eric.pelee.application.registry;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import priv.eric.pelee.application.loader.AppPipelineLoader;
import priv.eric.pelee.domain.model.Pipeline;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: 流水线注册表，负责加载和管理所有预定义的流水线
 *
 * @author EricTowns
 * @date 2026/1/27 18:30
 */
@Component
public class PipelineRegistry {

    private final Map<String, Pipeline> pipelines = new ConcurrentHashMap<>();
    private final AppPipelineLoader pipelineLoader = new AppPipelineLoader();

    @PostConstruct
    public void initializePipelines() {
        try {
            Path pipelineDir = Paths.get("src", "main", "resources", "pipeline");
            if (Files.exists(pipelineDir)) {
                Files.walk(pipelineDir)
                        .filter(path -> path.toString().endsWith(".json"))
                        .forEach(this::loadPipelineFromFile);
            }
            
            System.out.println("Loaded " + pipelines.size() + " pipelines: " + pipelines.keySet());
        } catch (Exception e) {
            System.err.println("Failed to initialize pipelines: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadPipelineFromFile(Path filePath) {
        try {
            String fileName = filePath.getFileName().toString();
            String pipelineId = fileName.substring(0, fileName.lastIndexOf('.'));
            String pipelineConfig = Files.readString(filePath);
            
            Pipeline pipeline = pipelineLoader.loadPipeline(pipelineConfig);
            pipelines.put(pipelineId, pipeline);
            
            System.out.println("Loaded pipeline: " + pipelineId + " from " + filePath);
        } catch (Exception e) {
            System.err.println("Failed to load pipeline from file: " + filePath + ", error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Pipeline getPipelineById(String pipelineId) {
        return pipelines.get(pipelineId);
    }

    public boolean containsPipeline(String pipelineId) {
        return pipelines.containsKey(pipelineId);
    }

    public Map<String, Pipeline> getAllPipelines() {
        return new HashMap<>(pipelines);
    }

    public void registerPipeline(String pipelineId, Pipeline pipeline) {
        pipelines.put(pipelineId, pipeline);
    }

}