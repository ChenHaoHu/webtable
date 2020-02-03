package top.hcy.webtable.entity;

import lombok.Data;
import top.hcy.webtable.annotation.field.*;
import top.hcy.webtable.annotation.method.WDeleteTrigger;
import top.hcy.webtable.annotation.method.WInsertTrigger;
import top.hcy.webtable.annotation.method.WUpdateTrigger;
import top.hcy.webtable.annotation.table.*;
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
@WTable(aliasName = "用户信息",tableName = "data1")
@WEnadbleDelete
@WEnadbleInsert
@WEnadbleUpdate
public class Data1 {
   @WField(aliasName = "编号")
   private long id;
   @WInsertField
   @WField(aliasName = "姓名")
   private String name;
   @WInsertField
   @WUpdateField
   @WField(aliasName = "年龄",columnName = "age")
   private int age2;
   @WInsertField
   @WUpdateField
   @WField(aliasName = "密码",fieldType = WebFieldType.NUMBER)
   private String passwd;
   @WField(aliasName = "照片显示",fieldType = WebFieldType.IMAGEURL)
   private String data1;
   @WField(aliasName = "数据二")
   private String data2;
   @WSelectField(select = {"1","aaa","2","bbbb"})
   private String data3;
   private String data4;



   @WFieldToShow("name")
   public void wFieldToShowName(){
      System.out.println("------------- 展示时，调整name值 ------------- ");
      this.name = "hello"+this.name;
   }

   @WFieldToShow("age2")
   public void wFieldToShowAge(){
      System.out.println("------------- 展示时，调整age值 ------------- ");
      this.age2 = this.age2 + 1000;
   }

   @WFieldToPersistence("age")
   public void wFieldToPersistenceAge(){
      System.out.println("------------- 存储时，调整age值 ------------- ");
      this.age2 = -1;
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
      System.out.println("------------- 删除触发器 --------------");
   }
}