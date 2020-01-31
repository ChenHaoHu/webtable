package top.hcy.webtable.entity;

import lombok.Data;
import top.hcy.webtable.annotation.field.*;
import top.hcy.webtable.annotation.method.WDeleteTrigger;
import top.hcy.webtable.annotation.method.WInsertTrigger;
import top.hcy.webtable.annotation.method.WSelectTrigger;
import top.hcy.webtable.annotation.method.WUpdateTrigger;
import top.hcy.webtable.annotation.table.WEnadbleDelete;
import top.hcy.webtable.annotation.table.WEnadbleInsert;
import top.hcy.webtable.annotation.table.WEnadbleUpdate;
import top.hcy.webtable.annotation.table.WTable;
import top.hcy.webtable.common.enums.WebFieldType;

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
@WTable(aliasName = "用户信息",tableName = "data2")
@WEnadbleDelete
@WEnadbleInsert
@WEnadbleUpdate
public class Data2 {
 @WField(aliasName = "编号")
 @WUpdateField
 private long id;
 @WInsertField
 @WField(aliasName = "任务")
 private String job;
 @WInsertField
 @WUpdateField
 @WField(aliasName = "数值",columnName = "num")
 private int numNumber;
 @WInsertField
 @WUpdateField
 @WField(aliasName = "密码",fieldType = WebFieldType.Number)
 private String tip;
 @WField(aliasName = "数据一")
 private String data1;
 @WField(aliasName = "数据二")
 private String data2;
 private String data3;
 private String data4;


 @WFieldToPersistence("tip")
 public String sss(Object a){
  System.out.println("----- tip - update ------");
  return "tiptiptip";
 }

 @WSelectTrigger
 public void selectData1Trigger(){
  System.out.println("------------- 查找触发器 --------------");
 }

 @WInsertTrigger
 public void insertData1Trigger(){
  System.out.println("------------- 插入触发器 --------------");
 }

 @WUpdateTrigger
 public void updateData1Trigger(){
  System.out.println("------------- 更新触发器 --------------");
 }

 @WDeleteTrigger
 public void deleteData1Trigger(){
  System.out.println("------------- 更新触发器 --------------");
 }
}