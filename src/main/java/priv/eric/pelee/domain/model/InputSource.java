package priv.eric.pelee.domain.model;

import java.util.concurrent.CompletableFuture;

/**
 * Description: 输入源接口，定义数据输入的抽象
 * 与Filter平级，负责数据获取
 *
 * @author EricTowns
 * @date 2026/2/4 21:50
 */
@FunctionalInterface
public interface InputSource {

    /**
     * 获取输入数据
     * 
     * @return 完成的Future，包含PipelineEvent数据
     */
    CompletableFuture<PipelineEvent> getInput();

}