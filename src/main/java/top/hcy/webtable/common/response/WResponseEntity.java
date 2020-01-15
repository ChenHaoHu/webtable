package top.hcy.webtable.common.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.hcy.webtable.common.enums.WRespCode;

@Data
@NoArgsConstructor
public class WResponseEntity {
    private int code;
    private String msg;
    private Object data;

    public WResponseEntity(WRespCode WRespCode) {
        this.code = WRespCode.getCode();
        this.msg = WRespCode.getMsg();
    }

    public WResponseEntity(WRespCode WRespCode, Object data) {
        this(WRespCode);
        this.data = data;
    }

}