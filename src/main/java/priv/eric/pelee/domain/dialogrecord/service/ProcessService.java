package priv.eric.pelee.domain.dialogrecord.service;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Description: 对话记录处理service
 *
 * @author EricTowns
 * @date 2026/1/22 19:54
 */
public interface ProcessService {

    void process(JsonNode dialogRecord);

}
