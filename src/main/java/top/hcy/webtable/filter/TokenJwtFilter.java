package top.hcy.webtable.filter;

import lombok.Data;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.common.constant.WGlobal;
import top.hcy.webtable.common.constant.WTokenType;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.tools.JwtTokenUtils;

import java.util.Date;


@Data
public class TokenJwtFilter implements WHandleFilter {

    @Override
    public void doFilter(WebTableContext ctx) {
        //token 存在 heard里的token字段下
        String token = ctx.getRequest().getHeader("token");

        if (token == null ){
            ctx.setWRespCode(WRespCode.REQUEST_TOKEN_LOST);
            ctx.setError(true);
            return;
        }


        //检查uri白名单
        int len = WConstants.NO_TOKEN_URI.length;
        String uri = ctx.getRealUri();
        for (int i = 0; i < len; i++) {
            if(WConstants.NO_TOKEN_URI[i].equals(uri)){
                return;
            }
        }


        //检查万能token
         len = WGlobal.TOKEN_POWER.length;
        for (int i = 0; i < len; i++) {
            if(WGlobal.TOKEN_POWER[i].equals(token)){
                ctx.setTokenKey("admin"+WConstants.TOKEN_SPLIT+"admin");
                ctx.setRole("admin");
                return;
            }
        }


        //检查token
        ctx.setToken(token);
        if ( !JwtTokenUtils.isTokenExpired(token)){
            String data = JwtTokenUtils.getDataFromToken(token);
            ctx.setTokenKey(data);
            //检查是否需要刷新token
            //todo: 该用户是否需要刷新token 这里暂时默认刷新默认时长的token
            Date expirationDate = JwtTokenUtils.getExpirationDateFromToken(token);
            Date now = new Date();
            if(expirationDate.getTime() - now.getTime() > 0 && expirationDate.getTime() - now.getTime()  < WGlobal.TOKEN_REFRESH_TIME){
                String s = JwtTokenUtils.generateToken(data, WTokenType.DEFAULT_TOKE);
                ctx.setNewToken(s);
                ctx.setRefreshToken(true);
            }
        }else{
            //失效token
            ctx.setWRespCode(WRespCode.REQUEST_TOKEN_ERROR);
            ctx.setError(true);
        }
    }
}
