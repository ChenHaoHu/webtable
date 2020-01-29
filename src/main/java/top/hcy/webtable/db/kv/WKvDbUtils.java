package top.hcy.webtable.db.kv;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

/**
 * @ClassName: WKvDbUtils
 * @Author: hcy
 * @Description:
 * @Date: 2020-01-20 22:41
 * @Version: 1.0
 **/
public interface WKvDbUtils {

    Object getValue(String key,WKVType t);

    boolean setValue(String key,Object ob,WKVType t);

    boolean deleKey(String key);

    boolean updateKey(String key,Object ob,WKVType t);

    boolean copyKey(String target,String key);

    ArrayList<String> getAllKeys();


}
