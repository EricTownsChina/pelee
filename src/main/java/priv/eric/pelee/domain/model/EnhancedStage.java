package priv.eric.pelee.domain.model;

import java.util.Map;
import java.util.Optional;

/**
 * Description: 增强的阶段接口，支持完整生命周期管理
 *
 * @author EricTowns
 * @date 2026/02/04 00:00
 */
public interface EnhancedStage extends Stage {

    /**
     * 初始化阶段
     * @param config 阶段配置
     * @param context 执行上下文
     * @throws StageInitializationException 初始化异常
     */
    default void initialize(Map<String, Object> config, EnhancedStageContext context) 
            throws StageInitializationException {
        // 默认空实现
    }

    /**
     * 处理前验证
     * @param event 数据事件
     * @param context 执行上下文
     * @throws StageValidationException 验证异常
     */
    default void validate(Event<?> event, EnhancedStageContext context) 
            throws StageValidationException {
        // 默认空实现
    }

    /**
     * 增强的处理方法
     * @param event 数据事件
     * @param context 增强上下文
     * @throws StageProcessingException 处理异常
     */
    void processEnhanced(Event<?> event, EnhancedStageContext context) 
            throws StageProcessingException;

    /**
     * 刷新阶段（用于批处理场景）
     * @param context 执行上下文
     * @throws StageFlushException 刷新异常
     */
    default void flush(EnhancedStageContext context) throws StageFlushException {
        // 默认空实现
    }

    /**
     * 清理阶段
     * @param context 执行上下文
     * @throws StageCleanupException 清理异常
     */
    default void cleanup(EnhancedStageContext context) throws StageCleanupException {
        // 默认空实现
    }

    /**
     * 获取阶段健康状态
     * @param context 执行上下文
     * @return 健康状态
     */
    default StageHealth getHealth(EnhancedStageContext context) {
        return StageHealth.HEALTHY;
    }

    /**
     * 获取阶段统计信息
     * @param context 执行上下文
     * @return 统计信息
     */
    default Map<String, Object> getStatistics(EnhancedStageContext context) {
        return Map.of();
    }

    /**
     * 获取阶段名称
     * @return 阶段名称
     */
    default String getStageName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 兼容原有接口
     */
    @Override
    default void process(Event<?> event, StageContext context) {
        if (context instanceof EnhancedStageContext enhancedContext) {
            try {
                validate(event, enhancedContext);
                processEnhanced(event, enhancedContext);
            } catch (StageValidationException | StageProcessingException e) {
                throw new RuntimeException("Stage processing failed", e);
            }
        } else {
            throw new IllegalArgumentException("EnhancedStage requires EnhancedStageContext");
        }
    }

    /**
     * 阶段健康状态枚举
     */
    enum StageHealth {
        HEALTHY, DEGRADED, UNHEALTHY, UNKNOWN
    }

    /**
     * 阶段异常基类
     */
    class StageException extends RuntimeException {
        public StageException(String message) {
            super(message);
        }
        
        public StageException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * 初始化异常
     */
    class StageInitializationException extends StageException {
        public StageInitializationException(String message) {
            super(message);
        }
        
        public StageInitializationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * 验证异常
     */
    class StageValidationException extends StageException {
        public StageValidationException(String message) {
            super(message);
        }
        
        public StageValidationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * 处理异常
     */
    class StageProcessingException extends StageException {
        public StageProcessingException(String message) {
            super(message);
        }
        
        public StageProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * 刷新异常
     */
    class StageFlushException extends StageException {
        public StageFlushException(String message) {
            super(message);
        }
        
        public StageFlushException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * 清理异常
     */
    class StageCleanupException extends StageException {
        public StageCleanupException(String message) {
            super(message);
        }
        
        public StageCleanupException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}