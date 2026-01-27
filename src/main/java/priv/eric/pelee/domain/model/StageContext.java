package priv.eric.pelee.domain.model;

/**
 * Description: todo
 *
 * @author EricTowns
 * @date 2026/1/26 21:03
 */
public interface StageContext {

    String code();

    void next(Event event);

}
