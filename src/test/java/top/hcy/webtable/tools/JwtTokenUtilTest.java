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
        String s = JwtTokenUtil.generateToken("156165156165");
        System.out.println(s);
    }

    @Test
    void validateToken() {
     //   Boolean c = JwtTokenUtil.validateToken("eyJhbGciyyuOiJIUzUxMiJ9.eyJzdWIiOiJoY3kiLCJleHAiOjE1NzkyODA5MzMsImlhdCI6MTU3OTI4MDg3M30.acVRlOouzRhbvp4YjuNV5kTLnYEEsPpheRHoE0N0lvxwAVQOvOZNd2vHVOZLIXzcOvN6LSX8kIWvq2OUY1Nf1A", "hcy");
      //  System.out.println(c);
    }

    @Test
    void getDataFromToken() {
      //  Date d = JwtTokenUtil.getExpirationDateFromToken("eyJhbGciOi1111JIUzUxMiJ9.eyJzdWIiOiJoY3kiLCJleHAiOjE1NzkyODA5MzMsImlhdCI6MTU3OTI4MDg3M30.acVRlOouzRhbvp4YjuNV5kTLnYEEsPpheRHoE0N0lvxwAVQOvOZNd2vHVOZLIXzcOvN6LSX8kIWvq2OUY1Nf1A");

     //   SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
       // String  dString = formatter.format(d);
       // System.out.println(dString);
    }
}