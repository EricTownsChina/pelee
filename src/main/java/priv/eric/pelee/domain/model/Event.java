package priv.eric.pelee.domain.model;

/**
 * Description: 事件
 *
 * @author EricTowns
 * @date 2026/1/26 21:01
 */
public class Event<T> {

    private T data;

    public T get() {
        return this.data;
    }

}
