package priv.eric.pelee.domain.model;

/**
 * Description: todo
 *
 * @author EricTowns
 * @date 2026/1/27 17:16
 */
public interface DataCarrier {

    <T> T getData();

    void setData(Object data);

    Object getRaw();

}
