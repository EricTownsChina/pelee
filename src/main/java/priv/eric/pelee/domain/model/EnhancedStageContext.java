package priv.eric.pelee.domain.model;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

/**
 * Description: 增强的阶段上下文，提供更丰富的管理能力
 *
 * @author EricTowns
 * @date 2026/02/04 00:00
 */
public interface EnhancedStageContext extends StageContext {

    /**
     * 获取流水线级别的共享状态
     * @return 流水线状态存储
     */
    Map<String, Object> getPipelineState();

    /**
     * 获取阶段级别的状态
     * @return 阶段状态存储
     */
    Map<String, Object> getStageState();

    /**
     * 获取当前执行的阶段信息
     * @return 当前阶段信息
     */
    Optional<StageInfo> getCurrentStage();

    /**
     * 获取流水线执行ID
     * @return 执行ID
     */
    String getExecutionId();

    /**
     * 获取流水线开始时间
     * @return 开始时间
     */
    Instant getStartTime();

    /**
     * 记录执行指标
     * @param metricName 指标名称
     * @param value 指标值
     */
    void recordMetric(String metricName, Object value);

    /**
     * 获取执行指标
     * @param metricName 指标名称
     * @return 指标值
     */
    Optional<Object> getMetric(String metricName);

    /**
     * 设置用户自定义属性
     * @param key 属性键
     * @param value 属性值
     */
    void setProperty(String key, Object value);

    /**
     * 获取用户自定义属性
     * @param key 属性键
     * @return 属性值
     */
    Optional<Object> getProperty(String key);

    /**
     * 检查是否应该停止处理
     * @return 是否停止
     */
    boolean shouldStop();

    /**
     * 请求停止处理
     * @param reason 停止原因
     */
    void requestStop(String reason);

    /**
     * 获取停止原因
     * @return 停止原因
     */
    Optional<String> getStopReason();

    /**
     * 向特定输出发送数据（支持多路输出）
     * @param outputName 输出名称
     * @param event 数据事件
     */
    default void sendToOutput(String outputName, Event<?> event) {
        throw new UnsupportedOperationException("Multiple outputs not supported");
    }

    /**
     * 设置定时器
     * @param timerName 定时器名称
     * @param fireTime 触发时间
     */
    default void setTimer(String timerName, Instant fireTime) {
        throw new UnsupportedOperationException("Timer not supported");
    }

    /**
     * 获取当前事件时间
     * @return 事件时间
     */
    default Instant getEventTime() {
        return Instant.now();
    }

    /**
     * 阶段信息内部类
     */
    interface StageInfo {
        String getName();
        int getIndex();
        Map<String, Object> getProperties();
    }
}