package com.kimvan.hung.vocabulary.testWord.orderYourLetter;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by h on 14/07/2018.
 */

class ConvertUnit {
    public static int pxToDp(int px,Resources r){
        return 0;
    }

    public static int dpToPx(int dp,Resources r){

        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );

        return px;
    }

    public static int spToPx(int sp,Resources r){
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                r.getDisplayMetrics()
        );

        return px;
    }
}
