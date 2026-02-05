package priv.eric.pelee.application.registry;

import org.springframework.stereotype.Component;
import priv.eric.pelee.domain.model.filter.PipeFilter;
import priv.eric.pelee.plugin.processor.remove.RemoveFieldPipeFilter;
import priv.eric.pelee.plugin.processor.rename.RenameFieldPipeFilter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: 过滤器注册表，管理所有可用的过滤器
 * 使用组合模式，避免继承复杂性
 *
 * @author EricTowns
 * @date 2026/2/4 22:30
 */
@Component
public class FilterRegistry {

    private final Map<String, FilterFactory> filterFactories = new ConcurrentHashMap<>();

    /**
     * 注册过滤器工厂
     */
    public void registerFilter(String type, FilterFactory factory) {
        filterFactories.put(type, factory);
    }

    /**
     * 创建过滤器实例
     */
    public PipeFilter createFilter(String type, Object config) {
        FilterFactory factory = filterFactories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Unknown filter type: " + type);
        }
        return factory.create(config);
    }

    /**
     * 获取所有可用的过滤器类型
     */
    public List<String> getAvailableFilterTypes() {
        return List.copyOf(filterFactories.keySet());
    }

    /**
     * 检查过滤器类型是否支持
     */
    public boolean isFilterSupported(String type) {
        return filterFactories.containsKey(type);
    }

    /**
     * 过滤器工厂接口
     */
    @FunctionalInterface
    public interface FilterFactory {
        PipeFilter create(Object config);
    }

    /**
     * 初始化默认过滤器
     */
    public void initializeDefaultFilters() {
        // 注册重命名字段过滤器
        registerFilter("rename", config -> {
            if (config instanceof RenameFieldPipeFilter.RenameFieldConfig renameConfig) {
                return new RenameFieldPipeFilter(renameConfig);
            }
            throw new IllegalArgumentException("Invalid config for rename filter");
        });

        // 注册删除字段过滤器
        registerFilter("remove", config -> {
            if (config instanceof RemoveFieldPipeFilter.RemoveFieldConfig removeConfig) {
                return new RemoveFieldPipeFilter(removeConfig);
            }
            throw new IllegalArgumentException("Invalid config for remove filter");
        });
    }
}