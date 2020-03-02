package top.hcy.webtable.tools;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CommonUtilsTest {

    @Test//k5qxvry9
    void getShortString() {
        String shortURI = CommonUtils.getShortStr(25);
        Assert.assertNotNull(shortURI);
    }

    @Test
    void generateShareAccount() {
        JSONObject map = CommonUtils.generateShareAccount();
        System.out.println(map);
        Assert.assertNotNull(map);
    }

    @Test
    void getTodayStartTime() {
        System.out.println(CommonUtils.getTodayStartTime());
    }
}