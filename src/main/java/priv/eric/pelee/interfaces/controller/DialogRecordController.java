package priv.eric.pelee.interfaces.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.eric.pelee.domain.dialogrecord.service.ProcessService;
import priv.eric.pelee.infrastructure.util.JsonUtil;
import priv.eric.pelee.interfaces.entity.Resp;

/**
 * Description: todo
 *
 * @author EricTowns
 * @date 2026/1/22 20:27
 */
@RestController
@RequestMapping("/dialog_record")
public class DialogRecordController {

    private final ProcessService processService;

    public DialogRecordController(ProcessService processService) {
        this.processService = processService;
    }

    @PostMapping("/process")
    public Resp<String> process(@RequestBody Object dialogRecord) {
        JsonNode node = JsonUtil.parseToJsonNode(JsonUtil.toJson(dialogRecord));
        processService.process(node);
        return Resp.ok(JsonUtil.toJson(node));
    }

}
