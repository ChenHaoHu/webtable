package top.hcy.webtable.kvdb;

import org.iq80.leveldb.*;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;


import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.ReadOptions;
import org.iq80.leveldb.Snapshot;
import org.iq80.leveldb.WriteBatch;
import org.iq80.leveldb.WriteOptions;


/**
 * @ClassName: LevelDBDemoTest
 * @Author: hcy
 * @Description:
 * @Date: 2020-01-20 21:43
 * @Version: 1.0
 **/
public class LevelDBDemoTest {



    private static final String PATH = "leveldb";
    private static final Charset CHARSET = Charset.forName("utf-8");
    private static final File FILE = new File(PATH);

    @Test
    public void putTest() {
        DBFactory factory = new Iq80DBFactory();
        // 默认如果没有则创建
        Options options = new Options();
        File file = new File(PATH);
        DB db = null;
        try {
            db = factory.open(file, options);
            byte[] keyByte1 = "key-01".getBytes(CHARSET);
            byte[] keyByte2 = "key-02".getBytes(CHARSET);
            // 会写入磁盘中
            db.put(keyByte1, "value-01".getBytes(CHARSET));
            db.put(keyByte2, "value-02".getBytes(CHARSET));
            String value1 = new String(db.get(keyByte1), CHARSET);
            System.out.println(value1);
            System.out.println(new String(db.get(keyByte2), CHARSET));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void readFromSnapshotTest() {
        DBFactory factory = new Iq80DBFactory();
        File file = new File(PATH);
        Options options = new Options();
        DB db = null;
        try {
            db = factory.open(file, options);
            // 读取当前快照，重启服务仍能读取，说明快照持久化至磁盘，
            Snapshot snapshot = db.getSnapshot();
            // 读取操作
            ReadOptions readOptions = new ReadOptions();
            // 遍历中swap出来的数据，不应该保存在memtable中。
            readOptions.fillCache(false);
            // 默认snapshot为当前
            readOptions.snapshot(snapshot);

            DBIterator it = db.iterator(readOptions);
            while (it.hasNext()) {
                Map.Entry<byte[], byte[]> entry = (Map.Entry<byte[], byte[]>) it
                        .next();
                String key = new String(entry.getKey(), CHARSET);
                String value = new String(entry.getValue(), CHARSET);
                System.out.println("key: " + key + " value: " + value);
                if (key.equals("key-01")) {
                    System.out.println("".equals(value));
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void snapshotTest() {
        DBFactory factory = new Iq80DBFactory();
        Options options = new Options();
        DB db = null;
        try {
            db = factory.open(FILE, options);

            db.put("key-04".getBytes(CHARSET), "value-04".getBytes(CHARSET));
            // 只能之前到getSnapshot之前put的值，之后的无法获取，即读取期间数据的变更，不会反应出来
            Snapshot snapshot = db.getSnapshot();
            db.put("key-05".getBytes(CHARSET), "value-05".getBytes(CHARSET));
            ReadOptions readOptions = new ReadOptions();
            readOptions.fillCache(false);
            readOptions.snapshot(snapshot);
            DBIterator it = db.iterator(readOptions);
            while (it.hasNext()) {
                Map.Entry<byte[],byte[]> entry = (Map.Entry<byte[],byte[]>) it
                        .next();
                String key = new String(entry.getKey(), CHARSET);
                String value = new String(entry.getValue(), CHARSET);
                System.out.println("key: " + key + " value: " + value);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void writeOptionsTest() {
        DBFactory factory = new Iq80DBFactory();
        Options options = new Options();
        DB db = null;
        try {
            db = factory.open(FILE, options);
            WriteOptions writeOptions = new WriteOptions().sync(true);	// 线程安全
            // 没有writeOptions时，会new一个，所以猜测这里添加了这个参数的意义就是可以设置sync和snapshot参数，建议采用这种方式
            db.put("key-06".getBytes(CHARSET), "value-06".getBytes(CHARSET), writeOptions);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void deleteTest() {
        DBFactory factory = new Iq80DBFactory();
        Options options = new Options();
        DB db = null;
        try {
            db = factory.open(FILE, options);
            // 存在会删除，之后查询不出，根据说明可能不是真删除，而是添加一个标记，待测试（大量数据之后删除，文件大小是否明显变化）
            db.delete("key-02".getBytes(CHARSET));
            // 不存在不会报错
            db.delete("key02".getBytes(CHARSET));

            Snapshot snapshot = db.getSnapshot();
            ReadOptions readOptions = new ReadOptions();
            readOptions.fillCache(false);
            readOptions.snapshot(snapshot);

            DBIterator it = db.iterator(readOptions);
            while (it.hasNext()) {
                Map.Entry<byte[], byte[]> entry = (Map.Entry<byte[], byte[]>) it
                        .next();
                String key = new String(entry.getKey(), CHARSET);
                String value = new String(entry.getValue(), CHARSET);
                System.out.println("key: " + key + " value: " + value);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void writeBatchTest() {
        DBFactory factory = Iq80DBFactory.factory;
        Options options = new Options();
        DB db = null;
        try {
            db = factory.open(FILE, options);
            // 批量保存，批量修改
            WriteBatch writeBatch = db.createWriteBatch();
            writeBatch.put("key-07".getBytes(CHARSET), "value-07".getBytes(CHARSET));
            writeBatch.put("key-08".getBytes(CHARSET), "value-08".getBytes(CHARSET));
            writeBatch.put("key-09".getBytes(CHARSET), "value-09".getBytes(CHARSET));
            // 这里也可以添加writeOptions
            db.write(writeBatch);

            DBIterator it = db.iterator();
            while (it.hasNext()) {
                Map.Entry<byte[], byte[]> entry = (Map.Entry<byte[], byte[]>) it
                        .next();
                String key = new String(entry.getKey(), CHARSET);
                String value = new String(entry.getValue(), CHARSET);
                System.out.println("key: " + key + " value: " + value);
            }
            it.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void writeBatchDeleteTest() {
        DBFactory factory = Iq80DBFactory.factory;
        Options options = new Options();
        DB db = null;
        try {
            db = factory.open(FILE, options);
            WriteBatch writeBatch = db.createWriteBatch();
            writeBatch.put("key-10".getBytes(CHARSET), "value-10".getBytes(CHARSET));
            writeBatch.put("key-11".getBytes(CHARSET), "value-11".getBytes(CHARSET));
            // 会将key-01的value置为""
            writeBatch.delete("key-01".getBytes(CHARSET));
            db.write(writeBatch);
            writeBatch.close();
            DBIterator it = db.iterator();
            while (it.hasNext()) {
                Map.Entry<byte[], byte[]> entry = (Map.Entry<byte[], byte[]>) it
                        .next();
                String key = new String(entry.getKey(), CHARSET);
                String value = new String(entry.getValue(), CHARSET);
                System.out.println("key: " + key + " value: " + value);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

}
