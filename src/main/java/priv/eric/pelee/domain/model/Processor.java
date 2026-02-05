package priv.eric.pelee.domain.model;

/**
 * desc:
 *
 * @author EricTownsChina@outlook.com
 * @date 2026-02-05 17:59
 */
public interface Processor<T> {

    void execute(Event<T> event);

}
