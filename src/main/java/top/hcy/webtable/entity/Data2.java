package top.hcy.webtable.entity;

import lombok.Data;
import top.hcy.webtable.annotation.charts.WChart;
import top.hcy.webtable.annotation.field.*;
import top.hcy.webtable.annotation.method.WDeleteTrigger;
import top.hcy.webtable.annotation.method.WInsertTrigger;
import top.hcy.webtable.annotation.method.WSelectTrigger;
import top.hcy.webtable.annotation.method.WUpdateTrigger;
import top.hcy.webtable.annotation.table.WEnadbleDelete;
import top.hcy.webtable.annotation.table.WEnadbleUpdate;
import top.hcy.webtable.annotation.table.WTable;
import top.hcy.webtable.charts.WCharts;
import top.hcy.webtable.common.WebTableContext;
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
@WTable(aliasName = "工作信息",tableName = "data2")
@WEnadbleDelete
//@WEnadbleInsert
@WEnadbleUpdate
public class Data2 {
 @WField(aliasName = "编号")
 @WUpdateField
 @WInsertField
 private long id;
 @WInsertField
 @WField(aliasName = "任务")
 private String job;
 @WInsertField
 @WUpdateField
 @WFindField
 @WField(aliasName = "数值",columnName = "num")
 private int numNumber;
 @WInsertField
 @WUpdateField
 @WField(aliasName = "密码",fieldType = WebFieldType.NUMBER)
 private String tip;
 @WField(aliasName = "BASE64照片",fieldType = WebFieldType.IMAGEBASE64)
 @WInsertField
 private String data1;
 @WField(aliasName = "数据二")
 @WFindField
 private String data2;
 @WField(aliasName = "选择项",fieldType = WebFieldType.SELECT)
 @WSelectsField(selects = {"选择一","AAA","选择二","BBB","选择三","CCC"} )
 @WInsertField
 @WUpdateField
 private String data3;
 private String data4;

 @WAbstractField(aliasName = "虚拟字段1")
 public String aa(){
  return "虚拟字段1";
 }

 @WAbstractField(aliasName = "虚拟字段2")
 public String bb(){
  return "虚拟字段2";
 }


 @WFieldToPersistence("tip")
 public String sss(Object a){
  System.out.println("----- tip - update ------");
  return "tiptiptip";
 }

 @WSelectTrigger
 public void selectData1Trigger(WebTableContext ctx){
  System.out.println("------------- 查找触发器 --------------");
 }

 @WInsertTrigger
 public void insertData1Trigger(WebTableContext ctx){
  System.out.println("------------- 插入触发器 --------------");
 }

 @WDeleteTrigger
 public void deleteData1Trigger(WebTableContext ctx){
  System.out.println("------------- 删除触发器 --------------");
 }

 @WUpdateTrigger
 public void updateData1Trigger(WebTableContext ctx){
  System.out.println("------------- 更新触发器 --------------");
 }

 @WChart(value = "部门业绩表",showDashboard = true)
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




 @WChart(value = "部门绩效汇总",showDashboard = true)
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

  WCharts wBarChart = new WCharts(x, y, WCharts.PieChart3);
  wBarChart.setXname("测试横坐标");
  wBarChart.setYname("测试竖坐标");
  return wBarChart;
 }
}

