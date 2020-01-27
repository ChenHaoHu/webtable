//package top.hcy.webtable.db.druid;
//
//import java.io.InputStream;
//import java.sql.*;
//import java.util.Properties;
//import java.util.concurrent.*;
//
//import javax.sql.DataSource;
//
//import ch.qos.logback.classic.pattern.SyslogStartConverter;
//import com.alibaba.druid.pool.DruidDataSourceFactory;
//import com.alibaba.fastjson.JSON;
//
//public class DruidUtil {
//
//    private static Properties p;
//    private static DataSource dataSource;
//
//    static {
//        try {
//            ClassLoader loader = Thread.currentThread().getContextClassLoader();
//            InputStream inputStream = loader.getResourceAsStream("db.properties");
//            p = new Properties();
//            p.load(inputStream);
//            // 通过工厂类获取DataSource对象
//            dataSource = DruidDataSourceFactory.createDataSource(p);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static Connection getConnection() {
//        try {
//            return dataSource.getConnection();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static void close(Connection conn, Statement state, ResultSet result) {
//
//        try {
//            if (result != null) {
//                result.close();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (state != null) {
//                        state.close();
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public static void main(String[] args) throws Exception {
//
//        long s = System.currentTimeMillis();
//        CountDownLatch countDownLatch = new CountDownLatch(1);
//        ExecutorService executorService = Executors.newFixedThreadPool(1);
//        for (int i = 0; i < 1; i++) {
//            executorService.submit(() -> {
//                try {
//                    Connection connection = getConnection();
//                    PreparedStatement preparedStatement = connection.prepareStatement("select * from task");
//                    ResultSet resultSet = preparedStatement.executeQuery();
//                    ResultSetMetaData metaData = resultSet.getMetaData();
//
//                    resultSet.next();
//                    System.out.println(resultSet.getFetchSize());
//                    System.out.println(resultSet.getFetchDirection());
//                    System.out.println(resultSet.getType());
//                    System.out.println(resultSet.getString(1));
//                    System.out.println(resultSet.getString(2));
//                    System.out.println(resultSet.getString(3));
//                    System.out.println(resultSet.getString(4));
//
//                    connection.close();
//                    countDownLatch.countDown();
//                } catch (Exception e) {
//                    System.out.println(e);
//                }
//            });
//        }
//
//        countDownLatch.await();
//        long e = System.currentTimeMillis();
//        System.out.println((e-s)/1000);
//        return;
//    }
//}