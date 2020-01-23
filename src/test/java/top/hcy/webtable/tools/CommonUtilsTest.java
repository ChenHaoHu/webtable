package top.hcy.webtable.tools;

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
        HashMap<String, String> map = CommonUtils.generateShareAccount();
        Assert.assertNotNull(map);
    }
}