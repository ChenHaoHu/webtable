package top.hcy.webtable.wsql.druid;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import top.hcy.webtable.tools.CommonUtils;

public class DBDataMain {

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

    public static void close(Connection conn, Statement state, ResultSet result) {

        try {
            if (result != null) {
                result.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (state != null) {
                        state.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Connection connection = getConnection();
        DatabaseMetaData d = connection.getMetaData();
        ResultSet ttt = d.getPrimaryKeys(connection.getCatalog(), null, "tt");
        ArrayList<HashMap<String, Object>> h = CommonUtils.convertResultSetToList(ttt);
        System.out.println(h);
        return;
    }
}