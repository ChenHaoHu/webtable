package top.hcy.webtable.wsql.structured.factory;

import top.hcy.webtable.wsql.structured.*;
import top.hcy.webtable.wsql.structured.impl.mysql.*;

public class WSQLFactory {

    public static WSelectSql getWSelectSql(WSQLDBType wsqlDBType){
        WSelectSql wSelectSql = null;
        switch (wsqlDBType){
            case MYSQL:
                wSelectSql = new WMySQLSelectSql();
                break;
            case ElasticSearch:
                break;
            case SQLServer:
                break;
        }
        return wSelectSql;
    }

    public static WInsertSql getWInsertSql(WSQLDBType wsqlDBType){
        WInsertSql wInsertSql = null;
        switch (wsqlDBType){
            case MYSQL:
                wInsertSql = new WMySQLInsertSql();
                break;
            case ElasticSearch:
                break;
            case SQLServer:
                break;
        }
        return wInsertSql;
    }

    public static WDeleteSql getWDeleteSql(WSQLDBType wsqlDBType){
        WDeleteSql wDeleteSql = null;
        switch (wsqlDBType){
            case MYSQL:
                wDeleteSql = new WMySQLDeleteSql();
                break;
            case ElasticSearch:
                break;
            case SQLServer:
                break;
        }
        return wDeleteSql;
    }

    public static WUpdateSql getWUpdateSql(WSQLDBType wsqlDBType){
        WUpdateSql wUpdateSql = null;
        switch (wsqlDBType){
            case MYSQL:
                wUpdateSql = new WMySQLUpdateSql();
                break;
            case ElasticSearch:
                break;
            case SQLServer:
                break;
        }
        return wUpdateSql;
    }

    public static WTableData getWTableData(WSQLDBType wsqlDBType){
        WTableData wTableData = null;
        switch (wsqlDBType){
            case MYSQL:
                wTableData = new WMySQLTableData();
                break;
            case ElasticSearch:
                break;
            case SQLServer:
                break;
        }
        return wTableData;
    }

}