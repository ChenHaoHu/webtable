package top.hcy.webtable.wsql.kv;

import java.util.ArrayList;


public interface WKvDbUtils {

    Object getValue(String key,WKVType t);

    boolean setValue(String key,Object ob,WKVType t);

    boolean deleKey(String key);

    boolean updateKey(String key,Object ob,WKVType t);

    boolean copyKey(String target,String key);

    ArrayList<String> getAllKeys();

}
