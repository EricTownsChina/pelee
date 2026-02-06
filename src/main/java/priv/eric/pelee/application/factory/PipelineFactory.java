package priv.eric.pelee.application.factory;

import priv.eric.pelee.domain.model.Pipeline;
import priv.eric.pelee.domain.model.ProcessorDescriptor;

import java.util.List;

/**
 * desc:
 *
 * @author EricTownsChina@outlook.com
 * @date 2026-02-05 21:19
 */
public class PipelineFactory {

    private ProcessorFactory processorFactory;

    public PipelineFactory(ProcessorFactory processorFactory) {
        this.processorFactory = processorFactory;
    }

    public Pipeline create() {

        return null;


    }

}
