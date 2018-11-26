package com.kimvan.hung.vocabulary.testWord.orderYourLetter;

import android.graphics.Color;

/**
 * Created by h on 14/07/2018.
 */

class Attrs {
    public static final int marginTopLine1 = 250; //dp
    public static final int marginTopLine2 = 223; //dp
    public static final int defaultTextSize = 10; //dp
    public static final int defaultTextColor = Color.parseColor("#808080");


    private int id;
    private int width;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(int textStyle) {
        this.textStyle = textStyle;
    }

    public int getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    private int height;
    private String text;
    private int textSize;
    private int textStyle;
    private int marginTop;


    public Attrs(int width, int height, String text, int textSize, int textStyle, int marginTop) {
        this.width = width;
        this.height = height;
        this.text = text;
        this.textSize = textSize;
        this.textStyle = textStyle;
        this.marginTop = marginTop;
    }
}
