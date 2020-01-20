package top.hcy.webtable.db.kv;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


import java.util.ArrayList;
import java.util.HashMap;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LevelDBUtilsTest
{
    HashMap<String,String> value1 = new HashMap<>();
    ArrayList<String> value3 = new ArrayList<>();
    String value2 = "testvalue";
    String key = "test";



    @BeforeAll
    void  init(){
        System.out.println(" ----- before ----- ");
        value1.put("one","1");
        value1.put("two","2");
        value1.put("three","3");

        value3.add("one");
        value3.add("two");
        value3.add("three");
    }


    @Test
    void getValue() {
        boolean test = kvDBUtils.setValue(key, value1,KVType.T_MAP);
        JSONObject value = (JSONObject)kvDBUtils.getValue(key,KVType.T_MAP);
        Assert.assertEquals(value1,value);
    }

    @Test
    void setValue() {
        boolean t = kvDBUtils.setValue(key, value2,KVType.T_STRING);
        Assert.assertTrue(t);
        String value = (String)kvDBUtils.getValue(key,KVType.T_STRING);
        Assert.assertEquals(value2,value);
    }


    @Test
    void updateKey() {
        boolean b = kvDBUtils.updateKey(key, value3,KVType.T_LIST);
        Assert.assertTrue(b);
        JSONArray value = (JSONArray)kvDBUtils.getValue(key,KVType.T_LIST);
        //    System.out.println(value);
        Assert.assertEquals(value, JSON.parseArray(JSON.toJSONString(value3)));
    }


    @Test
    void deleKey() {
        boolean b = kvDBUtils.deleKey(key);
        Assert.assertTrue(b);
        Object value = kvDBUtils.getValue(key,KVType.T_STRING);
        Assert.assertEquals(value,null);

    }
}