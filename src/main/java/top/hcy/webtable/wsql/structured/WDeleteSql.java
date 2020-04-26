package top.hcy.webtable.wsql.structured;

public interface WDeleteSql {
    WDeleteSql table(String tableName);

    WDeleteSql where();

    WDeleteSql where(String condition);

    WDeleteSql and(String andStr);

    WDeleteSql or(String orStr);

    int executeDelete(String... values);

    String getSql();
}
