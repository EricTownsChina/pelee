package priv.eric.pelee.plugin.processor;

import com.fasterxml.jackson.databind.node.ObjectNode;
import priv.eric.pelee.domain.model.Event;
import priv.eric.pelee.domain.model.Processor;
import priv.eric.pelee.plugin.ProcessorDescriptor;

import java.util.List;

/**
 * desc:
 *
 * @author EricTownsChina@outlook.com
 * @date 2026-02-05 19:42
 */
@ProcessorDescriptor(
        type = "remove",
        description = "重命名处理器",
        author = "Eric",
        configClass = RemoveConfig.class
)
public class RemoveProcessor implements Processor<ObjectNode> {

    private final List<String> fields;

    public RemoveProcessor(List<String> fields) {
        this.fields = fields;
    }

    @Override
    public void process(Event<ObjectNode> event) {
        ObjectNode data = event.getData();
        data.remove(fields);
    }

}
