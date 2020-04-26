package top.hcy.webtable.wsql.structured.factory;

import top.hcy.webtable.wsql.structured.WSelectSql;
import top.hcy.webtable.wsql.structured.impl.mysql.WMysqlSelectSql;

public class WsqlFactory {

    public static WSelectSql getWSelectSql(WsqlDBType wsqlDBType){
        WSelectSql wSelectSql = null;
        switch (wsqlDBType){
            case MYSQL:
                wSelectSql = new WMysqlSelectSql();
                break;
            case ElasticSearch:
                break;
            case SQLServer:
                break;
        }
        return wSelectSql;
    }

}