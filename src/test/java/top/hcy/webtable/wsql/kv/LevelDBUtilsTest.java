package top.hcy.webtable.wsql.kv;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
    String key2 = "test2";



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
        boolean test = kvDBUtils.setValue(key, value1, WKVType.T_MAP);
        JSONObject value = (JSONObject)kvDBUtils.getValue(key, WKVType.T_MAP);
        Assert.assertEquals(value1,value);
    }

    @Test
    void setValue() {
        boolean t = kvDBUtils.setValue(key, value2, WKVType.T_STRING);
        Assert.assertTrue(t);
        String value = (String)kvDBUtils.getValue(key, WKVType.T_STRING);
        Assert.assertEquals(value2,value);
    }


    @Test
    void updateKey() {
        boolean b = kvDBUtils.updateKey(key, value3, WKVType.T_LIST);
        Assert.assertTrue(b);
        JSONArray value = (JSONArray)kvDBUtils.getValue(key, WKVType.T_LIST);
        //    System.out.println(value);
        Assert.assertEquals(value, JSON.parseArray(JSON.toJSONString(value3)));
    }


    @Test
    void copyKey() {
        boolean t = kvDBUtils.copyKey(key2, key);
        Assert.assertTrue(t);
        //  JSONObject ttt = (JSONObject)kvDBUtils.getValue("ttt", WKVType.T_MAP);
    }

    @Test
    void deleKey() {
        boolean b = kvDBUtils.deleKey(key);
        Assert.assertTrue(b);
        Object value = kvDBUtils.getValue(key, WKVType.T_STRING);
        Assert.assertEquals(value,null);
    }


    @Test
    void getAllKeys() {
        ArrayList<String> allKeys = kvDBUtils.getAllKeys();
    }


    @AfterAll
    void deleteData(){
        System.out.println(" ----- deleteData ----- ");
        kvDBUtils.deleKey(key);
        kvDBUtils.deleKey(key2);
    }

}