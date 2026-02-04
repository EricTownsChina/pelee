package priv.eric.pelee.domain.model;

/**
 * Description: 阶段, 流水线运行最小单位
 *
 * @author EricTowns
 * @date 2026/1/26 20:59
 */
public interface Stage {

    void process(Event<?> event, StageContext context);

}
