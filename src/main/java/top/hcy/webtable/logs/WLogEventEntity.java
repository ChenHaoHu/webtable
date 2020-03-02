package top.hcy.webtable.logs;


import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WLogEventEntity {

    private int level  = 0; //0- info, 1- warn , 2- error
    private String username;
    private String role;
    private String uri;
    private String ip;
    private String requestdesc;
    private String params;
    private String response;
    private Long requestTime;
    private Long responseTime;
    private JSONArray sqlRecords;

}