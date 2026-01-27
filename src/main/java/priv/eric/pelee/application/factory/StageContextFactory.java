package priv.eric.pelee.application.factory;

import priv.eric.pelee.domain.model.StageContext;

import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Description: 加载所有StageContext实现类
 *
 * @author EricTowns
 * @date 2026/1/27 14:14
 */
public class StageContextFactory {

    private static volatile StageContextFactory instance;
    private final Map<String, StageContext> stageContexts;

    private StageContextFactory() {
        this.stageContexts = load();
    }

    public static StageContextFactory getInstance() {
        if (instance == null) {
            synchronized (StageContextFactory.class) {
                if (instance == null) {
                    instance = new StageContextFactory();
                }
            }
        }
        return instance;
    }

    private Map<String, StageContext> load() {
        ServiceLoader<StageContext> loader = ServiceLoader.load(StageContext.class);
        Map<String, StageContext> contexts = new ConcurrentHashMap<>();
        for (StageContext context : loader) {
            contexts.put(context.code(), context);
        }
        return contexts;
    }

    public StageContext get(String code) {
        return stageContexts.get(code);
    }

}