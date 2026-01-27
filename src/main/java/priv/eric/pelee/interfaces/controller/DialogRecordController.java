package priv.eric.pelee.interfaces.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import priv.eric.pelee.application.service.PipelineExecutionService;
import priv.eric.pelee.interfaces.entity.Resp;

import java.util.Map;

/**
 * Description: 对话记录处理控制器
 *
 * @author EricTowns
 * @date 2026/1/22 20:27
 */
@RestController
@RequestMapping("/dialog_record")
public class DialogRecordController {

    @Autowired
    private PipelineExecutionService pipelineExecutionService;

    @PostMapping("/handle")
    public Resp<String> process(@RequestBody Object dialogRecord) {
        // 原有的简单处理逻辑
        return Resp.ok("Simple processing completed");
    }

    @PostMapping("/process-by-pipeline-id")
    public Resp<Object> processWithPipeline(
            @RequestParam String pipelineId,
            @RequestBody Object dialogRecord) {
        try {
            if (!pipelineExecutionService.exists(pipelineId)) {
                return Resp.error("Pipeline not found: " + pipelineId);
            }
            
            JsonNode inputData = priv.eric.pelee.infrastructure.util.JsonUtil.parseToJsonNode(
                priv.eric.pelee.infrastructure.util.JsonUtil.toJson(dialogRecord)
            );
            
            Object result = pipelineExecutionService.executePipeline(pipelineId, inputData);
            
            return Resp.ok(priv.eric.pelee.infrastructure.util.JsonUtil.toJson(result));
        } catch (Exception e) {
            return Resp.error("Pipeline execution failed: " + e.getMessage());
        }
    }

}