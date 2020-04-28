package top.hcy.webtable.wsql.structured;

public interface WDeleteSql {
    WDeleteSql table(String tableName);

    WDeleteSql where();

//    WDeleteSql where(String condition);

    WDeleteSql and(String andField,String andValue);

    WDeleteSql or(String orField,String orValue);

    int executeDelete();

    String getSql();
}
