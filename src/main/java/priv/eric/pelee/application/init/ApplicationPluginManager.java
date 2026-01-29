package priv.eric.pelee.application.init;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Description: 应用插件管理器，负责初始化所有插件加载器
 *
 * @author EricTowns
 * @date 2026/1/27 15:30
 */
@Component
public class ApplicationPluginManager {

    @PostConstruct
    public void initializePlugins() {
        // 初始化StageContext加载器
        StageContextLoader.getInstance();
        
        // 初始化StageDescriptor加载器
        PluginLoader.getInstance();
        
        System.out.println("Application plugins initialized successfully.");
    }

}