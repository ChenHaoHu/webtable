package top.hcy.webtable.db.mysql;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import top.hcy.webtable.db.DBUtils;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
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
@Slf4j
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
        Connection connection = null;
        try{
            int size = values.length;
            connection  = getConnection();
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
            // e.printStackTrace();
            log.error(sql+"  "+ values+ "  "+ e.getMessage());
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static int insert(String sql,ArrayList<ArrayList<String>> values) {
        int execute = 0;
        Connection connection = null;
        try{
            connection = getConnection();
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
            //e.printStackTrace();
            log.error(sql+"  "+ values+ "  "+ e.getMessage());
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return execute;
    }

    public static int update(String sql,ArrayList<String> values,String... conditionValues) {
        int execute = 0;
        Connection connection = null;
        try{
            connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int size = values.size();
            for (int i = 0; i < size; i++) {
                preparedStatement.setString(i+1,values.get(i));
            }
            int len = conditionValues.length;
            for (int i = 0; i < len; i++) {
                preparedStatement.setString(size+i+1,conditionValues[i]);
            }
            execute = preparedStatement.executeUpdate();
        }catch (Exception e){
            //e.printStackTrace();
            log.error(sql+"  "+ values+ "  "+ e.getMessage());
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return execute;
    }

    public static int delete(String sql,String... values) {
        int execute = 0;
        Connection connection = null;
        try{
            connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int size = values.length;
            for (int i = 0; i < size; i++) {
                preparedStatement.setString(i+1,values[i]);
            }
            execute = preparedStatement.executeUpdate();
        }catch (Exception e){
            //e.printStackTrace();
            log.error(sql+"  "+ values+ "  "+ e.getMessage());
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return execute;
    }
}