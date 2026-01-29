package priv.eric.pelee.application.init;

import priv.eric.pelee.domain.model.StageDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Description: 加载所有插件实现类，包括StageDescriptor等
 *
 * @author EricTowns
 * @date 2026/1/27 15:00
 */
public class PluginLoader {

    private static volatile PluginLoader instance;
    private final List<StageDescriptor> stageDescriptors;

    private PluginLoader() {
        this.stageDescriptors = loadAllStageDescriptors();
    }

    public static PluginLoader getInstance() {
        if (instance == null) {
            synchronized (PluginLoader.class) {
                if (instance == null) {
                    instance = new PluginLoader();
                }
            }
        }
        return instance;
    }

    private List<StageDescriptor> loadAllStageDescriptors() {
        ServiceLoader<StageDescriptor> loader = ServiceLoader.load(StageDescriptor.class);
        List<StageDescriptor> descriptors = new ArrayList<>();
        for (StageDescriptor descriptor : loader) {
            descriptors.add(descriptor);
        }
        return descriptors;
    }

    public List<StageDescriptor> getAllStageDescriptors() {
        return new ArrayList<>(stageDescriptors);
    }

    public StageDescriptor getStageDescriptorByType(String type) {
        return stageDescriptors.stream()
                .filter(descriptor -> descriptor.type().equals(type))
                .findFirst()
                .orElse(null);
    }

}