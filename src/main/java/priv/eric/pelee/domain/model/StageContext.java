package priv.eric.pelee.domain.model;

import java.util.List;

/**
 * Description: 阶段上下文，管理阶段的执行流程
 *
 * @author EricTowns
 * @date 2026/1/26 21:03
 */
public interface StageContext {

    String code();

    void next(Event<?> event);

    /**
     * 设置要执行的阶段列表（可选操作）
     * @param stages 阶段列表
     * @throws UnsupportedOperationException 如果该上下文不支持设置阶段列表
     */
    default void setStages(List<Stage> stages) {
        throw new UnsupportedOperationException("This context does not support setting stages");
    }

}
