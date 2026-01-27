package priv.eric.pelee.domain.model;

/**
 * Description: 事件
 *
 * @author EricTowns
 * @date 2026/1/26 21:01
 */
public class Event {

    public Object data;

    public Event(Object data) {
        this.data = data;
    }

    public Object get() {
        return this.data;
    }

    public void set(Object data) {
        this.data = data;
    }

}