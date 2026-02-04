package priv.eric.pelee.domain.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 过滤器链，使用组合模式管理多个过滤器的执行
 * 负责协调过滤器的执行顺序和上下文传递
 *
 * @author EricTowns
 * @date 2026/2/4 21:45
 */
public class FilterChain {

    private final List<Filter> filters;
    private final Map<String, Object> globalConfig;
    private final Map<String, Object> runtimeState;

    /**
     * 构造函数
     */
    public FilterChain(List<Filter> filters) {
        this(filters, new HashMap<>(), new HashMap<>());
    }

    /**
     * 完整构造函数
     */
    public FilterChain(List<Filter> filters, Map<String, Object> globalConfig, Map<String, Object> runtimeState) {
        this.filters = new ArrayList<>(filters);
        this.globalConfig = new HashMap<>(globalConfig);
        this.runtimeState = new HashMap<>(runtimeState);
    }

    /**
     * 处理事件，通过整个过滤器链
     * 
     * @param event 输入事件
     * @return 处理后的事件
     */
    public PipelineEvent process(PipelineEvent event) {
        PipelineEvent currentEvent = event;

        for (int i = 0; i < filters.size(); i++) {
            Filter filter = filters.get(i);
            String filterId = "filter-" + i;
            
            FilterContext context = new FilterContext(
                globalConfig, runtimeState, filterId, i
            );

            PipelineEvent result = filter.filter(currentEvent, context);
            
            // 如果过滤器返回null，停止处理
            if (result == null) {
                break;
            }
            
            currentEvent = result;
        }

        return currentEvent;
    }

    /**
     * 获取过滤器数量
     */
    public int size() {
        return filters.size();
    }

    /**
     * 判断是否为空
     */
    public boolean isEmpty() {
        return filters.isEmpty();
    }

    /**
     * 添加过滤器到链末尾
     */
    public FilterChain addFilter(Filter filter) {
        List<Filter> newFilters = new ArrayList<>(this.filters);
        newFilters.add(filter);
        return new FilterChain(newFilters, this.globalConfig, this.runtimeState);
    }

    /**
     * 在指定位置插入过滤器
     */
    public FilterChain addFilter(int index, Filter filter) {
        List<Filter> newFilters = new ArrayList<>(this.filters);
        newFilters.add(index, filter);
        return new FilterChain(newFilters, this.globalConfig, this.runtimeState);
    }

    /**
     * 移除指定位置的过滤器
     */
    public FilterChain removeFilter(int index) {
        List<Filter> newFilters = new ArrayList<>(this.filters);
        newFilters.remove(index);
        return new FilterChain(newFilters, this.globalConfig, this.runtimeState);
    }

    /**
     * 设置全局配置
     */
    public FilterChain withGlobalConfig(String key, Object value) {
        Map<String, Object> newGlobalConfig = new HashMap<>(this.globalConfig);
        newGlobalConfig.put(key, value);
        return new FilterChain(this.filters, newGlobalConfig, this.runtimeState);
    }

    /**
     * 设置运行时状态
     */
    public FilterChain withRuntimeState(String key, Object value) {
        Map<String, Object> newRuntimeState = new HashMap<>(this.runtimeState);
        newRuntimeState.put(key, value);
        return new FilterChain(this.filters, newRuntimeState);
    }

    /**
     * 获取所有过滤器的只读副本
     */
    public List<Filter> getFilters() {
        return new ArrayList<>(filters);
    }

    /**
     * 获取全局配置的只读副本
     */
    public Map<String, Object> getGlobalConfig() {
        return new HashMap<>(globalConfig);
    }

    /**
     * 获取运行时状态的只读副本
     */
    public Map<String, Object> getRuntimeState() {
        return new HashMap<>(runtimeState);
    }
}