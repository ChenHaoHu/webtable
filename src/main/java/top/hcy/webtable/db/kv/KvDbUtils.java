package top.hcy.webtable.db.kv;

import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName: KvDbUtils
 * @Author: hcy
 * @Description:
 * @Date: 2020-01-20 22:41
 * @Version: 1.0
 **/
public interface KvDbUtils {

    Object getValue(String key,int t);

    boolean setValue(String key,Object ob,int t);

    boolean deleKey(String key);

    boolean updateKey(String key,Object ob,int t);

}
