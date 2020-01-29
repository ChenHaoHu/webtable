package top.hcy.webtable.service;

import com.alibaba.fastjson.JSONObject;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.common.constant.WGlobal;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.db.kv.WKVType;
import top.hcy.webtable.tools.JwtTokenUtils;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.service
 * @ClassName: LoginService
 * @Author: hcy
 * @Description: 登录处理
 * @Date: 20-1-25 22:07
 * @Version: 1.0
 **/
public class LoginService implements WService {
    @Override
    public void verifyParams(WebTableContext ctx) {
        JSONObject params = ctx.getParams();
        String username = (String)params.get("username");
        String passwd = (String)params.get("passwd");
        if (username!=null && passwd!=null && !username.isEmpty()&&!passwd.isEmpty()){
            return;
        }else{
            ctx.setWRespCode(WRespCode.REQUEST_PARAM_ERROR);
            ctx.setError(true);
            return;
        }
    }

    @Override
    public void doService(WebTableContext ctx) {
        JSONObject params = ctx.getParams();
        String username = (String)params.get("username");
        String passwd = (String)params.get("passwd");
        String password = (String)WGlobal.kvDBUtils.getValue(WConstants.PREFIX_ACCOUNTS + username, WKVType.T_STRING);
        if (passwd.equals(password)){
            ctx.setWRespCode(WRespCode.LOGIN_SUCCESS);
            String s = JwtTokenUtils.generateToken(username);
            ctx.setNewToken(s);
            ctx.setRefreshToken(true);
        }else{
            ctx.setWRespCode(WRespCode.LOGIN_FAILE);
        }

    }
}