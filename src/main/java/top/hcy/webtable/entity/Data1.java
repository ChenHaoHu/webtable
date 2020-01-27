package top.hcy.webtable.entity;

import lombok.Data;
import top.hcy.webtable.annotation.WebTable;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.entity
 * @ClassName: Data1
 * @Author: hcy
 * @Description: 测试实例 1
 * @Date: 20-1-27 17:35
 * @Version: 1.0
 **/
@Data
@WebTable(aliasName = "数据集一",tableName = "data1")
public class Data1 {
   private long id;
   private String name;
   private int age;
   private String passwd;
   private String data1;
   private String data2;
   private String data3;
   private String data4;
}