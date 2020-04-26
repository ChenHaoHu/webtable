package top.hcy.webtable.wsql.structured;

import java.util.ArrayList;

public interface WTableData {
    WTableData table(String table);

    ArrayList<String> getPrimayKey();
}
