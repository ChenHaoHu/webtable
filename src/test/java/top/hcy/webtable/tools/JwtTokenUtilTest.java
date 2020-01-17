package top.hcy.webtable.tools;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import java.text.SimpleDateFormat;
import java.util.Date;

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


    @Test
    void generateToken() {
        String s = JwtTokenUtil.generateToken("hcy");
        System.out.println(s);
    }

    @Test
    void validateToken() {
        Boolean c = JwtTokenUtil.validateToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJoY3kiLCJleHAiOjE1NzkyNzEyMjMsImlhdCI6MTU3OTI2NzYyM30.lRTjM_BeRwmcduAktFeMZD_cXmi5yU2PPDJJa0sYO5NdUMUbuJfukiLqvT9X3YkXJchIiBV8ZBOGNmFef4lkvA", "hcy");
        System.out.println(c);
    }

    @Test
    void getDataFromToken() {
        Date d = JwtTokenUtil.getExpirationDateFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJoY3kiLCJleHAiOjE1NzkyNzEyMjMsImlhdCI6MTU3OTI2NzYyM30.lRTjM_BeRwmcduAktFeMZD_cXmi5yU2PPDJJa0sYO5NdUMUbuJfukiLqvT9X3YkXJchIiBV8ZBOGNmFef4lkvA");

        SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        String  dString = formatter.format(d);
        System.out.println(dString);
    }
}