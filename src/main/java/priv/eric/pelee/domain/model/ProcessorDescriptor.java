package priv.eric.pelee.domain.model;

/**
 * desc:
 *
 * @author EricTownsChina@outlook.com
 * @date 2026-02-05 20:00
 */
public interface ProcessorDescriptor<T> {

    String type();

    String desc();

    Class<?> configClass();

    Processor<T> create(Object config);

}
