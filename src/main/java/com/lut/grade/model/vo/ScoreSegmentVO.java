package com.lut.grade.model.vo;

/** 分数段统计项：名称 + 人数 + 占比。 */
public class ScoreSegmentVO {

    private String name;
    private int count;
    private double ratio;
    /** 对应 Bootstrap 背景色 class，页面直接取用。 */
    private String color;

    public ScoreSegmentVO() {
    }

    public ScoreSegmentVO(String name, int count, String color) {
        this.name = name;
        this.count = count;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    /** 百分比整数部分，进度条宽度使用。 */
    public int getPercent() {
        return (int) Math.round(ratio * 100);
    }

    /** 该档成绩对应的分数区间，页面直接展示。 */
    public String getRange() {
        if (name == null) {
            return "";
        }
        switch (name) {
            case "优":
                return "90 ~ 100";
            case "良":
                return "80 ~ 89";
            case "中":
                return "70 ~ 79";
            case "及格":
                return "60 ~ 69";
            case "不及格":
                return "0 ~ 59";
            default:
                return "";
        }
    }
}
