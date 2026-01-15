package priv.eric.pelee.domain.service;

import priv.eric.pelee.domain.entity.ConversationRecord;
import priv.eric.pelee.domain.vo.TransformationConfig;

/**
 * 转换器服务接口
 */
public interface TransformerService {
    /**
     * 根据配置转换对话记录
     *
     * @param sourceRecord 源对话记录
     * @param config       转换配置
     * @return 转换后的对话记录
     */
    ConversationRecord transform(ConversationRecord sourceRecord, TransformationConfig config);
}