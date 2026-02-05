package priv.eric.pelee.application.factory;

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


}
