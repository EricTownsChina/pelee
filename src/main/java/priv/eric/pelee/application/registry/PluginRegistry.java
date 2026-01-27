package priv.eric.pelee.application.registry;

import priv.eric.pelee.application.init.PluginLoader;
import priv.eric.pelee.application.init.StageContextLoader;
import priv.eric.pelee.domain.model.StageContext;
import priv.eric.pelee.domain.model.StageDescriptor;

import java.util.Collection;
import java.util.List;

/**
 * Description: 插件注册表工具类，提供便捷的插件访问方法
 *
 * @author EricTowns
 * @date 2026/1/27 15:45
 */
public class PluginRegistry {

    /**
     * 获取所有StageContext实现
     */
    public static Collection<StageContext> getAllStageContexts() {
        return StageContextLoader.getInstance().getAllStageContexts();
    }

    /**
     * 根据代码获取特定的StageContext实现
     */
    public static StageContext getStageContextByCode(String code) {
        return StageContextLoader.getInstance().getStageContextByCode(code);
    }

    /**
     * 获取所有StageDescriptor实现
     */
    public static List<StageDescriptor> getAllStageDescriptors() {
        return PluginLoader.getInstance().getAllStageDescriptors();
    }

    /**
     * 根据类型获取特定的StageDescriptor实现
     */
    public static StageDescriptor getStageDescriptorByType(String type) {
        return PluginLoader.getInstance().getStageDescriptorByType(type);
    }

}