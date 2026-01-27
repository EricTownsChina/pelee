package priv.eric.pelee.domain.model;

/**
 * Description: todo
 *
 * @author EricTowns
 * @date 2026/1/26 21:08
 */
public interface StageDescriptor {

    String code();

    String desc();

    Class<?> configClass();

    Stage create(Object config);

}
