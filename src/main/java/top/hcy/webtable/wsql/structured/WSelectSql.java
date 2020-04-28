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

//    WSelectSql where(String condition);

    WSelectSql and(String andField,String andValue);

    WSelectSql greater(String gteField,String gteValue);

    WSelectSql less(String lessField,String lessValue);

    WSelectSql greaterAndequals(String gteAndEqualsField,String gteAndEqualsValue);

    WSelectSql lessAndequals(String lessAndEqualsField,String lessAndEqualsValue);

    WSelectSql or(String orField,String orValue);

    WSelectSql like(String likeField,String likeValue);

    WSelectSql orderBy(String orderByField, Boolean desc);

    WSelectSql orderBy(String orderByField);

    ArrayList<HashMap<String,Object>> executeQuery();

    String getSql();
}
