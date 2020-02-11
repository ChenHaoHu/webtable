package top.hcy.webtable.charts;

import lombok.Data;

import java.util.ArrayList;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable
 * @ClassName: WCharts
 * @Author: hcy
 * @Description:
 * @Date: 20-2-10 23:54
 * @Version: 1.0
 **/
@Data
public class WCharts {

    public static int LineChart1 = 1;
    public static int LineChart2 = 2;
    public static int BarChart = 3;
    public static int PieChart1 = 4;
    public static int PieChart2 = 5;
    public static int PieChart3 = 6;

    private ArrayList<String> xValues = null;
    private ArrayList<Integer> yValues = null;
    public String xname = null;
    public String yname = null;
    public String title = null;
    private int type;

    public WCharts(ArrayList<String> xValues, ArrayList<Integer> yValues, int type) {
        this.xValues = xValues;
        this.yValues = yValues;
        this.type = type;
    }

    public WCharts(ArrayList<String> xValues, ArrayList<Integer> yValues, String xname, String yname, String title, int type) {
        this.xValues = xValues;
        this.yValues = yValues;
        this.xname = xname;
        this.yname = yname;
        this.title = title;
        this.type = type;
    }
}