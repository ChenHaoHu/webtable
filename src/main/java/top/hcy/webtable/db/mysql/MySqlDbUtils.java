package top.hcy.webtable.db.mysql;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import top.hcy.webtable.db.DBUtils;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.db.mysql
 * @ClassName: MySqlDbUtils
 * @Author: hcy
 * @Description:
 * @Date: 20-1-26 15:49
 * @Version: 1.0
 **/
public class MySqlDbUtils implements DBUtils {

    private static Properties p;
    private static DataSource dataSource;

    static {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = loader.getResourceAsStream("db.properties");
            p = new Properties();
            p.load(inputStream);
            // 通过工厂类获取DataSource对象
            dataSource = DruidDataSourceFactory.createDataSource(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<HashMap<String,Object>> find(String sql,String... values) {
        ArrayList<HashMap<String,Object>> list = new ArrayList<>();
       try{
           int size = values.length;
           Connection connection = getConnection();
           PreparedStatement preparedStatement = connection.prepareStatement(sql);
           for (int i = 0; i < size; i++) {
               preparedStatement.setString(i+1,values[i]);
           }
           ResultSet rs = preparedStatement.executeQuery();
           ResultSetMetaData md = rs.getMetaData(); //获得结果集结构信息,元数据
           int columnCount = md.getColumnCount();   //获得列数
           while (rs.next()) {
               HashMap<String,Object> rowData = new HashMap<String,Object>();
               for (int i = 1; i <= columnCount; i++) {
                   rowData.put(md.getColumnName(i), rs.getObject(i));
               }
               list.add(rowData);
           }
           connection.close();
       }catch (Exception e){
            e.printStackTrace();
       }
        return list;
    }

    public static int insert(String sql,ArrayList<ArrayList<String>> values) {
        int execute = 0;
        try{
           Connection connection = getConnection();
           PreparedStatement preparedStatement = connection.prepareStatement(sql);

           int size = values.size();
            for (int i = 0; i < size; i++) {
                ArrayList<String> value = values.get(i);
                int valueSize = value.size();
                for (int j = 0; j < valueSize; j++) {
                    preparedStatement.setString(i*valueSize+j+1,value.get(j));
                }
            }


           execute = preparedStatement.executeUpdate();
       }catch (Exception e){
           e.printStackTrace();
           //log
       }
        return execute;
    }

    public static void update(String sql) {

    }

    public static void delete(String sql) {

    }
}