package priv.eric.pelee.interfaces.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import priv.eric.pelee.interfaces.entity.Resp;

import java.util.Map;

/**
 * Description: 对话记录处理控制器，基于新的DDD架构设计
 * 提供对话记录的HTTP接口，使用Event-Filter模式
 *
 * @author EricTowns
 * @date 2026/1/22 20:27
 */
@RestController
@RequestMapping("/dialog_record")
public class DialogRecordController {

    /**
     * 简单处理对话记录
     * @param dialogRecord 对话记录数据
     * @return 处理结果
     */
    @PostMapping("/handle")
    public Resp<String> process(@RequestBody Object dialogRecord) {
        return Resp.ok("Simple processing completed");
    }

    /**
     * 使用指定流水线处理对话记录
     * @param pipelineId 流水线ID
     * @param dialogRecord 对话记录数据
     * @return 处理结果
     */
    @PostMapping("/process-by-pipeline-id")
    public Resp<String> processWithPipeline(
            @RequestParam String pipelineId,
            @RequestBody Object dialogRecord) {
        try {
            return Resp.ok();
        } catch (IllegalArgumentException e) {
            return Resp.error(e.getMessage());
        } catch (Exception e) {
            return Resp.error("Pipeline execution failed: " + e.getMessage());
        }
    }

    /**
     * 获取可用的流水线列表
     * @return 流水线信息
     */
    @GetMapping("/available-pipelines")
    public Resp<Map<String, Object>> getAvailablePipelines() {
        try {
            return Resp.ok();
        } catch (Exception e) {
            return Resp.error("Failed to get available pipelines: " + e.getMessage());
        }
    }

}