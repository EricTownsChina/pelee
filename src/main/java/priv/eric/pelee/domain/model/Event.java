package priv.eric.pelee.domain.model;

import lombok.Getter;

import java.util.Map;

/**
 * Description: 数据事件载体，用于在流水线中传递数据
 *
 * @author EricTowns
 * @date 2026/1/26 21:01
 */
@Getter
public class Event<T> {

    private final T data;

    private final Map<String, Object> meta;

    public Event(T data, Map<String, Object> meta) {
        this.data = data;
        this.meta = meta;
    }

}