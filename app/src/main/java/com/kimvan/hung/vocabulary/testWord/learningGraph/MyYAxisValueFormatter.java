package com.kimvan.hung.vocabulary.testWord.learningGraph;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by h on 22/02/2018.
 */

class MyYAxisValueFormatter implements IAxisValueFormatter {
    DecimalFormat format;

    public MyYAxisValueFormatter() {
        format = new DecimalFormat();
    }

    @Override
    public String getFormattedValue(float v, AxisBase axisBase) {
        return format.format(v)+"%";
    }
}
