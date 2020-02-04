package top.hcy.webtable.tools;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CommonUtilsTest {

    @Test//k5qxvry9
    void getShortString() {
        String shortURI = CommonUtils.getShortStr();
        Assert.assertNotNull(shortURI);
    }

    @Test
    void generateShareAccount() {
        JSONObject map = CommonUtils.generateShareAccount();
        Assert.assertNotNull(map);
    }
}