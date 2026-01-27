package priv.eric.pelee.application.init;

import priv.eric.pelee.domain.model.StageContext;

import java.util.Collection;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Description: 加载所有StageContext实现类
 *
 * @author EricTowns
 * @date 2026/1/27 14:14
 */
public class StageContextLoader {

    private static volatile StageContextLoader instance;
    private final Map<String, StageContext> stageContexts;

    private StageContextLoader() {
        this.stageContexts = loadAllStageContexts();
    }

    public static StageContextLoader getInstance() {
        if (instance == null) {
            synchronized (StageContextLoader.class) {
                if (instance == null) {
                    instance = new StageContextLoader();
                }
            }
        }
        return instance;
    }

    private Map<String, StageContext> loadAllStageContexts() {
        ServiceLoader<StageContext> loader = ServiceLoader.load(StageContext.class);
        Map<String, StageContext> contexts = new ConcurrentHashMap<>();
        for (StageContext context : loader) {
            contexts.put(context.code(), context);
        }
        return contexts;
    }

    public Collection<StageContext> getAllStageContexts() {
        return stageContexts.values();
    }

    public StageContext getStageContextByCode(String code) {
        return stageContexts.get(code);
    }

}