package top.hcy.webtable.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseEntity {
    private int code;
    private String msg;
    private Object data;

    public ResponseEntity(RespCode respCode) {
        this.code = respCode.getCode();
        this.msg = respCode.getMsg();
    }

    public ResponseEntity(RespCode respCode, Object data) {
        this(respCode);
        this.data = data;
    }

}