package top.hcy.webtable.wsql.structured.impl.mysql;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WGlobal;
import top.hcy.webtable.wsql.structured.DBUtils;
import top.hcy.webtable.tools.CommonUtils;
import top.hcy.webtable.wsql.structured.factory.WDataSource;
import top.hcy.webtable.wsql.structured.factory.WSQLDBType;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


@Slf4j
public class MySQLDBUtils implements DBUtils {


    public static Connection getConnection() {
        WebTableContext ctx = WGlobal.ctxThreadLocal.get();
        WSQLDBType wsqldbType = ctx.getWsqldbType();
        String dbname = ctx.getDbname();
        Connection connection = null;

        DataSource dataSource = (DataSource)WDataSource.getDataSource(wsqldbType, dbname);

      if (dataSource == null){

          log.error("datasource can not be null " +" WsqldbType:"+wsqldbType+" dbname: "+dbname);
          throw new NullPointerException();
      }

        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public static ArrayList<HashMap<String,Object>> find(String sql,ArrayList<String> conditionValues) {
        ArrayList<HashMap<String,Object>> list = new ArrayList<>();
        Connection connection = null;

        boolean executedSuccess = true;
        String error = null;
        long startTime = 0;
        long executedTime = 0;
        try{
            int size = conditionValues.size();
            connection  = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < size; i++) {
                preparedStatement.setString(i+1,conditionValues.get(i));
            }
            startTime =  System.currentTimeMillis();
            ResultSet rs = preparedStatement.executeQuery();
            executedTime = System.currentTimeMillis() - startTime;
            list = CommonUtils.convertResultSetToList(rs);
            connection.close();
        }catch (Exception e){
            // e.printStackTrace();
            executedTime = System.currentTimeMillis() - startTime;
            error = e.getMessage();
            executedSuccess = false;
            log.error(sql+"  "+ conditionValues+ "  "+ e.getMessage());
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        recordSQLData(sql,conditionValues,executedSuccess,error,executedTime);
        return list;
    }

    public static int insert(String sql,ArrayList<ArrayList<String>> values) {
        int execute = 0;
        Connection connection = null;

        boolean executedSuccess = true;
        String error = null;
        long startTime = 0;
        long executedTime = 0;
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
            startTime = System.currentTimeMillis();
            execute = preparedStatement.executeUpdate();
            executedTime = System.currentTimeMillis() - startTime;
        }catch (Exception e){
            //e.printStackTrace();
            executedTime = System.currentTimeMillis() - startTime;
            error = e.getMessage();
            executedSuccess = false;
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
        recordSQLData(sql,values,executedSuccess,error,executedTime);
        return execute;
    }

    public static int update(String sql,ArrayList<String> values,ArrayList<String> conditionValues) {
        int execute = 0;
        Connection connection = null;
        long startTime = 0;
        long executedTime = 0;
        boolean executedSuccess = true;
        String error = null;
        try{
            connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int size = values.size();
            for (int i = 0; i < size; i++) {
                preparedStatement.setString(i+1,values.get(i));
            }
            int len = conditionValues.size();
            for (int i = 0; i < len; i++) {
                preparedStatement.setString(size+i+1,conditionValues.get(i));
            }
            startTime = System.currentTimeMillis();
            execute = preparedStatement.executeUpdate();
            executedTime = System.currentTimeMillis() - startTime;

        }catch (Exception e){
            //e.printStackTrace();
            error = e.getMessage();
            executedSuccess = false;
            executedTime = System.currentTimeMillis() - startTime;
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
        recordSQLData(sql,values,conditionValues,executedSuccess,error,executedTime);
        return execute;
    }



    public static int delete(String sql,ArrayList<String> conditionValues) {
        int execute = 0;
        Connection connection = null;
        boolean executedSuccess = true;
        String error = null;
        long startTime = 0;
        long executedTime = 0;

        try{
            connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int size = conditionValues.size();
            for (int i = 0; i < size; i++) {
                preparedStatement.setString(i+1,conditionValues.get(i));
            }
            startTime = System.currentTimeMillis();
            execute = preparedStatement.executeUpdate();
            executedTime = System.currentTimeMillis() - startTime;

        }catch (Exception e){
            //e.printStackTrace();
            error = e.getMessage();
            executedSuccess = false;
            executedTime = System.currentTimeMillis() - startTime;
            log.error(sql+"  "+ JSON.toJSONString(conditionValues)+ "  "+ e.getMessage());
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        recordSQLData(sql,conditionValues,executedSuccess,error,executedTime);
        return execute;
    }



    public static ArrayList<String> getPrimayKey(String table) {
        ArrayList<String> list = new ArrayList<>();
        Connection connection = null;
        try{
            connection = getConnection();
            DatabaseMetaData d = connection.getMetaData();
            ResultSet rs = d.getPrimaryKeys(connection.getCatalog(), null, table);
            ArrayList<HashMap<String, Object>> h = CommonUtils.convertResultSetToList(rs);
            int size = h.size();
            for (int i = 0; i < size; i++) {
                list.add(h.get(i).get("COLUMN_NAME").toString());
            }

        }catch (Exception e){
            //e.printStackTrace();
            log.error(table+ "  "+ e.getMessage());
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



    private static void recordSQLData(String sql, ArrayList<String> values, ArrayList<String> conditionValues, boolean executedSuccess, String error,long executedTime) {
        WebTableContext ctx = WGlobal.ctxThreadLocal.get();
        if(ctx==null){
            return;
        }
        JSONArray executedSQLs = ctx.getExecutedSQLs();
        if (executedSQLs == null){
            executedSQLs = new JSONArray();
        }
        JSONObject data = new JSONObject();
        data.put("sql",sql);
        data.put("state",executedSuccess);
        data.put("error",error);
        data.put("executedTime",executedTime);
        JSONArray vs =  new JSONArray();
        vs.add(values);
        vs.add(conditionValues);
        data.put("values",vs);
        executedSQLs.add(data);
        ctx.setExecutedSQLs(executedSQLs);
    }

    private static void recordSQLData(String sql, ArrayList values, boolean executedSuccess, String error,long executedTime) {
        WebTableContext ctx = WGlobal.ctxThreadLocal.get();
        JSONArray executedSQLs = ctx.getExecutedSQLs();
        if (executedSQLs == null){
            executedSQLs = new JSONArray();
        }
        JSONObject data = new JSONObject();
        data.put("sql",sql);
        data.put("state",executedSuccess);
        data.put("error",error);
        data.put("executedTime",executedTime);
        JSONArray vs =  new JSONArray();
        vs.add(values);
        data.put("values",vs);
        executedSQLs.add(data);
        ctx.setExecutedSQLs(executedSQLs);
    }

//    private static void recordSQLData(String sql, ArrayList<ArrayList<String>> values, boolean executedSuccess, String error,long executedTime) {
//        WebTableContext ctx = WGlobal.ctxThreadLocal.get();
//        JSONArray executedSQLs = ctx.getExecutedSQLs();
//        if (executedSQLs == null){
//            executedSQLs = new JSONArray();
//        }
//        JSONObject data = new JSONObject();
//        data.put("sql",sql);
//        data.put("state",executedSuccess);
//        data.put("error",error);
//        data.put("executedTime",executedTime);
//        JSONArray vs =  new JSONArray();
//        vs.add(values);
//        data.put("values",vs);
//        executedSQLs.add(data);
//        ctx.setExecutedSQLs(executedSQLs);
//    }
}