package priv.eric.pelee.interfaces.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Description: 统一返回
 *
 * @author EricTowns
 * @date 2026/1/22 20:28
 */
@Data
public class Resp<T> {

    @JsonProperty("status_code")
    private Integer statusCode;

    @JsonProperty("status_msg")
    private String statusMsg;

    @JsonProperty("data")
    private T data;

    public Resp(Builder<T> builder) {
        this.statusCode = builder.statusCode;
        this.statusMsg = builder.statusMsg;
        this.data = builder.data;
    }

    public static <T> Resp<T> ok() {
        return new Builder<T>()
                .setStatusCode(0)
                .setStatusMsg("ok")
                .build();
    }

    public static <T> Resp<T> ok(T data) {
        return new Builder<T>()
                .setStatusCode(0)
                .setStatusMsg("ok")
                .setData(data)
                .build();
    }

    public static <T> Resp<T> error(Integer statusCode, String statusMsg) {
        return new Builder<T>()
                .setStatusCode(statusCode)
                .setStatusMsg(statusMsg)
                .build();
    }

    public static <T> Resp<T> error(String statusMsg) {
        return new Builder<T>()
                .setStatusCode(-1)
                .setStatusMsg(statusMsg)
                .build();
    }

    private static class Builder<T> {
        private Integer statusCode;
        private String statusMsg;
        private T data;

        public Builder<T> setStatusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder<T> setStatusMsg(String statusMsg) {
            this.statusMsg = statusMsg;
            return this;
        }

        public Builder<T> setData(T data) {
            this.data = data;
            return this;
        }

        public Resp<T> build() {
            return new Resp<>(this);
        }
    }

}
