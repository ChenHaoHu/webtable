package top.hcy.webtable.wsql.structured;

public interface WUpdateSql {
    WUpdateSql table(String tableName);

    WUpdateSql update(String field, String value);

    WUpdateSql where();

    WUpdateSql where(String condition);

    WUpdateSql and(String andStr);

    WUpdateSql or(String orStr);

    int executeUpdate(String... conditionValues);

    String getSql();
}
