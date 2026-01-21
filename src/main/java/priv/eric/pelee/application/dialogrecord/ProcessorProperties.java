package priv.eric.pelee.application.dialogrecord;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description: 处理器配置
 *
 * @author EricTowns
 * @date 2026/1/21 17:19
 */
@Data
@Component
@ConfigurationProperties(prefix = "processor.dialog-record")
public class ProcessorProperties {

    private Boolean enable;

    private String path;

    public boolean enable() {
        return enable != null && enable;
    }

}
