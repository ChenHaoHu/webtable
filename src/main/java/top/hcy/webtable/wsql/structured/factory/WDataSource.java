package top.hcy.webtable.wsql.structured.factory;


import java.util.HashMap;

public class WDataSource {

    private  static  HashMap<WSQLDBType,HashMap<String,Object>>  datasources = new HashMap<>();

    public static void addDataSource(WSQLDBType dbType, String name, Object datasource){
        HashMap<String, Object> map = datasources.get(dbType);
        if (map == null){
            map = new HashMap<String,Object>();
            datasources.put(dbType,map);
        }
        map.put(name,datasource);
    }

    public static void addDefaulteDataSource(WSQLDBType dbType, Object datasource){
        String  name = "default";
        addDataSource( dbType, name, datasource);
    }

    public static void addDefaulteMySQLDataSource(Object datasource){
        String  name = "default";
        WSQLDBType dbType = WSQLDBType.MYSQL;
        addDataSource( dbType, name, datasource);
    }

    public static  Object getDefaulteMySQLDataSource(){
        return getDataSource(WSQLDBType.MYSQL,"default");
    }

    public static  Object getDefaulteDataSource(WSQLDBType dbType){
        return getDataSource(dbType,"default");
    }

    public static  Object getDataSource(WSQLDBType dbType,String name){
        HashMap<String, Object> datasource = datasources.get(dbType);
        if (datasource!=null){
            return datasource.get(name);
        }
        return null;
    }
}