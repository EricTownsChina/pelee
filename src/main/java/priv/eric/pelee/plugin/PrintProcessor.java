package priv.eric.pelee.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import priv.eric.pelee.domain.model.Event;
import priv.eric.pelee.domain.model.Processor;
import priv.eric.pelee.infrastructure.util.JsonUtil;

/**
 * desc:
 *
 * @author EricTownsChina@outlook.com
 * @date 2026-02-06 15:11
 */
@ProcessorDescriptor(
        type = "print",
        description = "打印处理器",
        author = "Eric"
)
public class PrintProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintProcessor.class);

    @Override
    public void process(Event event) {
        Object data = event.getData();
        LOGGER.info("===== print event: {}", JsonUtil.toJson(data));
    }

}
