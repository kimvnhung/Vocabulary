package com.kimvan.hung.vocabulary.testWord.learningGraph;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by h on 22/02/2018.
 */

class MyXAxisValueFormatterForYear implements IAxisValueFormatter {
    private String[] Value;

    public MyXAxisValueFormatterForYear(String[] value) {
        this.Value = value;
    }

    @Override
    public String getFormattedValue(float v, AxisBase axisBase) {
        if ((int)v!=v){
            return "";
        }else{
            return String.valueOf(Integer.parseInt(Value[(int) v])+2017);
        }
    }
}
