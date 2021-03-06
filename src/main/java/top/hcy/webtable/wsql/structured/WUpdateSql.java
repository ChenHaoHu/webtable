package top.hcy.webtable.wsql.structured;

public interface WUpdateSql {
    WUpdateSql table(String tableName);

    WUpdateSql update(String field, String value);

    WUpdateSql where();

//    WUpdateSql where(String condition);

    WUpdateSql and(String andField,String andValue);

    WUpdateSql or(String orField,String orValue);

    int executeUpdate();

    String getSql();
}
