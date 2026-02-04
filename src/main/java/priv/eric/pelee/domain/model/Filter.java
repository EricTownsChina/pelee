package priv.eric.pelee.domain.model;

/**
 * Description: 过滤器接口，定义数据处理的基本单元
 * 采用组合模式，避免继承的复杂性
 *
 * @author EricTowns
 * @date 2026/2/4 21:40
 */
@FunctionalInterface
public interface Filter {

    /**
     * 处理流水线事件
     * 
     * @param event 输入事件
     * @param context 过滤器上下文
     * @return 处理后的事件，如果不应该继续传递则返回null
     */
    PipelineEvent filter(PipelineEvent event, FilterContext context);

}