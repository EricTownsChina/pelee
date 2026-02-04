package priv.eric.pelee.domain.model;

import java.util.Map;
import java.util.Optional;

/**
 * Description: 过滤器上下文，提供过滤器和环境之间的交互
 * 使用组合模式，避免继承带来的复杂性
 *
 * @author EricTowns
 * @date 2026/2/4 21:35
 */
public class FilterContext {

    private final Map<String, Object> globalConfig;
    private final Map<String, Object> runtimeState;
    private final String filterId;
    private final int filterIndex;

    /**
     * 构造函数
     */
    public FilterContext(Map<String, Object> globalConfig, Map<String, Object> runtimeState, 
                       String filterId, int filterIndex) {
        this.globalConfig = globalConfig;
        this.runtimeState = runtimeState;
        this.filterId = filterId;
        this.filterIndex = filterIndex;
    }

    /**
     * 获取全局配置
     */
    public Optional<Object> getConfig(String key) {
        return Optional.ofNullable(globalConfig.get(key));
    }

    /**
     * 获取全局配置，带默认值
     */
    @SuppressWarnings("unchecked")
    public <T> T getConfig(String key, T defaultValue) {
        Object value = globalConfig.get(key);
        return value != null ? (T) value : defaultValue;
    }

    /**
     * 获取运行时状态
     */
    public Optional<Object> getState(String key) {
        return Optional.ofNullable(runtimeState.get(key));
    }

    /**
     * 设置运行时状态
     */
    public void setState(String key, Object value) {
        runtimeState.put(key, value);
    }

    /**
     * 获取运行时状态，带默认值
     */
    @SuppressWarnings("unchecked")
    public <T> T getState(String key, T defaultValue) {
        Object value = runtimeState.get(key);
        return value != null ? (T) value : defaultValue;
    }

    /**
     * 获取过滤器ID
     */
    public String getFilterId() {
        return filterId;
    }

    /**
     * 获取过滤器索引
     */
    public int getFilterIndex() {
        return filterIndex;
    }

    /**
     * 创建子上下文（用于复杂过滤器内部处理）
     */
    public FilterContext createChild(String childFilterId) {
        return new FilterContext(globalConfig, runtimeState, childFilterId, filterIndex);
    }

    /**
     * 判断是否包含指定的配置
     */
    public boolean hasConfig(String key) {
        return globalConfig.containsKey(key);
    }

    /**
     * 判断是否包含指定的状态
     */
    public boolean hasState(String key) {
        return runtimeState.containsKey(key);
    }
}