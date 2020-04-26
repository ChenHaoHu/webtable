package top.hcy.webtable.wsql.structured;

import java.util.ArrayList;
import java.util.HashMap;

public interface WSelectSql {
    WSelectSql table(String tableName);

    WSelectSql fields(String... fields);

    WSelectSql fieldsPk(String pk);

    WSelectSql count();

    WSelectSql limit(int limit, int offset);

    WSelectSql page(int pageNum, int pageSize);

    WSelectSql where();

    WSelectSql where(String condition);

    WSelectSql and(String andStr);

    WSelectSql greater(String andStr);

    WSelectSql less(String andStr);

    WSelectSql greaterAndequals(String andStr);

    WSelectSql lessAndequals(String andStr);

    WSelectSql or(String orStr);

    WSelectSql like(String likeStr);

    WSelectSql orderBy(String field, Boolean desc);

    WSelectSql orderBy(String field);

    ArrayList<HashMap<String,Object>> executeQuery(String... values);

    String getSql();
}
