package top.hcy.webtable.wsql.structured;

public interface WInsertSql {
    WInsertSql table(String tableName);

    WInsertSql fields(String... fields);

    WInsertSql values(String... values);

    int executeInsert();

    String getSql();
}
