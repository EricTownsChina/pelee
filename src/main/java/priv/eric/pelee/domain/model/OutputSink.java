package priv.eric.pelee.domain.model;

import java.util.concurrent.CompletableFuture;

/**
 * Description: 输出接收器接口，定义数据输出的抽象
 * 与Filter平级，负责数据发送
 *
 * @author EricTowns
 * @date 2026/2/4 21:52
 */
@FunctionalInterface
public interface OutputSink {

    /**
     * 发送处理后的数据
     * 
     * @param event 处理后的事件数据
     * @return 完成的Future，可用于确认发送结果
     */
    CompletableFuture<Void> sendOutput(PipelineEvent event);

}