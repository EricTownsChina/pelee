package priv.eric.pelee.domain.model;

/**
 * Description: 数据事件载体，用于在流水线中传递数据
 *
 * @author EricTowns
 * @date 2026/1/26 21:01
 */
public class Event<T> {

    private T data;

    public Event(T data) {
        this.data = data;
    }

    public T get() {
        return this.data;
    }

    public void set(T data) {
        this.data = data;
    }

}