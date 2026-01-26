package priv.eric.pelee.domain.model;

/**
 * Description: todo
 *
 * @author EricTowns
 * @date 2026/1/26 21:08
 */
public interface StageDescriptor<T> {

    String type();

    String desc();

    Class<?> configClass();

    Stage<T> create(Object config);

}
