package top.hcy.webtable.db.kv;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.File;
import java.nio.charset.Charset;

/**
 * @ClassName: LevelDBUtils
 * @Author: hcy
 * @Description:
 * @Date: 2020-01-20 22:43
 * @Version: 1.0
 **/
@Slf4j
public class LevelDBUtils implements KvDbUtils {

    private static final String PATH = "leveldb";
    private static final Charset CHARSET = Charset.forName("utf-8");
    private static final File FILE = new File(PATH);
    DB db = null;

    public LevelDBUtils(){
        //初始化对象
        DBFactory factory = new Iq80DBFactory();
        // 默认如果没有则创建
        Options options = new Options();
        File file = new File(PATH);

        try {
            db = factory.open(file, options);
        }catch (Exception e){
            log.error(" LevelDBUtils start error :"+e);

        }
    }


    @Override
    public Object getValue(String key,int t) {
        if(db == null){
            log.error(" LevelDBUtils start error db is null");
            return null;
        }
        byte[] bytes = db.get(key.getBytes(CHARSET));
        if (bytes==null){
            return null;
        }

        if (t == KVType.T_STRING){
            return new String(bytes);
        }else  if(t == KVType.T_LIST){
            try{
                JSONArray array = JSONArray.parseArray(new String(bytes));
                return array;
            }catch (Exception e){
                log.error("bytes" + new String(bytes)+" to JSONArray error");
                return null;
            }
        }else if  (t == KVType.T_MAP){
            try{
                JSONObject object = JSONObject.parseObject(new String(bytes));
                return object;
            }catch (Exception e){
                log.error("bytes" + new String(bytes)+" to JSONObject error");
                return null;
            }
        }else{
            return null;
        }

    }

    @Override
    public boolean setValue(String key, Object ob,int t ){
        if(db == null){
            log.error(" LevelDBUtils start error db is null");
            return false;
        }
        String s = null;

        if(t != KVType.T_STRING){
            s = JSON.toJSONString(ob);
        }else{
            s = ob.toString();
        }

        db.put(key.getBytes(CHARSET),s.getBytes(CHARSET));
        return true;
    }

    @Override
    public boolean deleKey(String key) {
        if(db == null){
            log.error(" LevelDBUtils start error db is null");
            return false;
        }
        db.delete(key.getBytes(CHARSET));
        return true;
    }

    @Override
    public boolean updateKey(String key, Object ob,int t) {
        boolean b = setValue(key, ob, t);
        return b;
    }
}
