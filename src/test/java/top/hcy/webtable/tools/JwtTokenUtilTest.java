package top.hcy.webtable.tools;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import top.hcy.webtable.common.constant.WTokenType;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.tools
 * @ClassName: JwtTokenUtilTest
 * @Author: hcy
 * @Description:
 * @Date: 2020/1/17 21:02
 * @Version: 1.0
 */
//@SpringBootTest

class JwtTokenUtilTest {


    @Test//验证token功能正常
    void generateToken() {

        Random random = new Random();
        int i = random.nextInt(10000);
        String txt = String.valueOf(i);
        String s = JwtTokenUtil.generateToken(txt, WTokenType.DEFAULT_TOKE);
        String data = JwtTokenUtil.getDataFromToken(s);
        Assert.assertEquals(txt,data);
        Boolean aBoolean = JwtTokenUtil.validateToken(s, txt);
        Assert.assertEquals(aBoolean,true);
        Boolean tokenExpired = JwtTokenUtil.isTokenExpired(s);
        Assert.assertEquals(tokenExpired,false);
    }




}