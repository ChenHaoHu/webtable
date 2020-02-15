package top.hcy.webtable.service.handle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import top.hcy.webtable.annotation.common.WHandleService;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.common.constant.WGlobal;
import top.hcy.webtable.common.enums.WHandlerType;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.db.kv.WKVType;
import top.hcy.webtable.service.WService;
import top.hcy.webtable.tools.JwtTokenUtils;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.service
 * @ClassName: LoginService
 * @Author: hcy
 * @Description: 登录处理
 * @Date: 20-1-25 22:07
 * @Version: 1.0
 **/
@WHandleService(WHandlerType.LoginRequest)
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
        String password = (String) kvDBUtils.getValue(WConstants.PREFIX_ACCOUNTS + username, WKVType.T_STRING);
        if (passwd.equals(password)){
            ctx.setWRespCode(WRespCode.LOGIN_SUCCESS);
            String s = JwtTokenUtils.generateToken(username+WConstants.TOKEN_SPLIT+"admin");
            ctx.setNewToken(s);
            ctx.setRefreshToken(true);
            ctx.setRole("admin");
            return;
        }
        else{
            //验证share账号
            JSONArray shareslist = (JSONArray) kvDBUtils.getValue("shareslist", WKVType.T_LIST);

            int size = shareslist.size();
            for (int i = 0; i < size; i++) {
                JSONObject share = (JSONObject) shareslist.get(i);
                String u = share.getString("username");
                if (u.equals(username) && share.getString("passwd").equals(passwd)){

                    Integer status = share.getInteger("status");
                    if(status == 1){
                        ctx.setWRespCode(WRespCode.LOGIN_SUCCESS);
                        String s = JwtTokenUtils.generateToken(username+WConstants.TOKEN_SPLIT+"share");
                        ctx.setNewToken(s);
                        ctx.setRefreshToken(true);
                        ctx.setRole("share");
                        return;
                    }else{
                        ctx.setWRespCode(WRespCode.LOGIN_SHAREFORBID);
                        return;
                    }


                }
            }
        }
        ctx.setWRespCode(WRespCode.LOGIN_FAILE);
    }
}