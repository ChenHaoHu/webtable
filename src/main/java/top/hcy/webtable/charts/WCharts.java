package top.hcy.webtable.charts;

import lombok.Data;

import java.util.ArrayList;


@Data
public class WCharts {

    public static int LineChart1 = 1;
    public static int LineChart2 = 2;
    public static int BarChart = 3;
    public static int PieChart1 = 4;
    public static int PieChart2 = 5;
    public static int PieChart3 = 6;

    private String title = "";
    private ArrayList<String> xvalues = null;
    private ArrayList<Integer> yvalues = null;
    public String xname = null;
    public String yname = null;
    private int type;

    public WCharts(ArrayList<String> xValues, ArrayList<Integer> yValues, int type) {
        this.xvalues = xValues;
        this.yvalues = yValues;
        this.type = type;
    }

    public WCharts(ArrayList<String> xValues, ArrayList<Integer> yValues, String xname, String yname, String title, int type) {
        this.xvalues = xValues;
        this.yvalues = yValues;
        this.xname = xname;
        this.yname = yname;
        this.title = title;
        this.type = type;
    }
}