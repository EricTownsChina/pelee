package priv.eric.pelee.application.init;

import org.springframework.stereotype.Component;
import priv.eric.pelee.domain.model.Pipeline;
import priv.eric.pelee.domain.model.Processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * desc:
 *
 * @author EricTownsChina@outlook.com
 * @date 2026-02-06 17:14
 */
@Component
public class PipelineRegistry {

    private Map<String, Pipeline> pipelineRegistry;

    public PipelineRegistry() {
        pipelineRegistry = new ConcurrentHashMap<>();
    }

    public Pipeline get(String id) {
        return pipelineRegistry.get(id);
    }

    public void put(String id, Pipeline pipeline) {
        this.pipelineRegistry.put(id, pipeline);
    }

}
