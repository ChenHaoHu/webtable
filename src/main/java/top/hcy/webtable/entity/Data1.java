package top.hcy.webtable.entity;

import lombok.Data;
import top.hcy.webtable.annotation.charts.WChart;
import top.hcy.webtable.annotation.field.*;
import top.hcy.webtable.annotation.method.WDeleteTrigger;
import top.hcy.webtable.annotation.method.WInsertTrigger;
import top.hcy.webtable.annotation.method.WUpdateTrigger;
import top.hcy.webtable.annotation.table.*;
import top.hcy.webtable.charts.WCharts;
import top.hcy.webtable.common.enums.WebFieldType;

import java.util.ArrayList;

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
public class Data1 {
   @WField(aliasName = "编号")
   private long id;
   @WInsertField
   @WFindField
   @WField(aliasName = "姓名")
   private String name;
   @WInsertField
   @WUpdateField
   @WFindField
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
   @WSelectsField(selects = {"1","aaa","2","bbbb"})
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



   @WChart(value = "高层会议统计图",showDashboard = true)
   public Object chart1(){

      ArrayList<String> x = new ArrayList<>();
      ArrayList<Integer> y = new ArrayList<>();

      x.add("aaa");
      x.add("bbb");
      x.add("ccc");
      x.add("ddd");

      y.add(100);
      y.add(200);
      y.add(300);
      y.add(400);

      WCharts chart = new WCharts(x, y, WCharts.LineChart1);
      chart.setXname("测试横坐标2");
      chart.setYname("测试竖坐标2");
      chart.setTitle("高层会议统计图");

      return chart;
   }

   @WChart(value = "年终总结会议图",showDashboard = true)
   public Object chart2(){

      ArrayList<String> x = new ArrayList<>();
      ArrayList<Integer> y = new ArrayList<>();

      x.add("aaa");
      x.add("bbb");
      x.add("ccc");
      x.add("ddd");

      y.add(100);
      y.add(200);
      y.add(300);
      y.add(400);

      WCharts wBarChart = new WCharts(x, y, WCharts.BarChart);
      wBarChart.setXname("测试横坐标");
      wBarChart.setYname("测试竖坐标");

      return wBarChart;

   }

   @WChart(value = "交易总结",showDashboard = true)
   public Object chart3(){

      ArrayList<String> x = new ArrayList<>();
      ArrayList<Integer> y = new ArrayList<>();

      x.add("aaa");
      x.add("bbb");
      x.add("ccc");
      x.add("ddd");

      y.add(100);
      y.add(200);
      y.add(300);
      y.add(400);

      WCharts wBarChart = new WCharts(x, y, WCharts.PieChart1);
      wBarChart.setXname("测试横坐标");
      wBarChart.setYname("测试竖坐标");
      wBarChart.setTitle("交易总结图");
      return wBarChart;

   }
}