package com.kimvan.hung.vocabulary.testWord.orderYourLetter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kimvan.hung.vocabulary.R;

/**
 * Created by h on 14/07/2018.
 */

public abstract class LetterView extends android.support.v7.widget.AppCompatTextView {
    private boolean isSelected;
    private int sttStart;
    private int sttSeleted;
    private Attrs attrs;

    public LetterView(Context context, ViewGroup viewGroup, String content, final int stt, int totalNum) {
        super(context);
        attrs = new Attrs(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, content, 20, Typeface.BOLD, Attrs.marginTopLine1);
        this.sttStart = stt ;
        this.isSelected = false;
        setAtrrsForView(viewGroup,totalNum);
        setBackground(ContextCompat.getDrawable(context, R.drawable.border_rectang_conner));
        setGravity(Gravity.CENTER);


        setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isSelected = !isSelected;
                        if (isSelected){
                            moveView();
                        }else{
                            returnView();
                        }

                    }
                }
        );
    }

    private void setAtrrsForView(ViewGroup contentLayout,int totalNum) {
        //width, height
        RelativeLayout.LayoutParams addingRules = new RelativeLayout.LayoutParams(
                getAttrs().getWidth(),
                getAttrs().getHeight()
        );
        //text
        setText(getAttrs().getText());
        //textSize
        setTextSize(getAttrs().getTextSize());
        //textStyle
        setTypeface(getTypeface(),getAttrs().getTextStyle());
        //textColor
        setTextColor(Attrs.defaultTextColor);

        //marginTop
        this.setLayoutParams(addingRules);
        RelativeLayout.LayoutParams more = (RelativeLayout.LayoutParams) getLayoutParams();


        //position the views
        Resources r = this.getResources();


        int x,y;
        x = ConvertUnit.dpToPx((int) (150-Math.pow(-1,getSttStart()%2)*((getSttStart()%(totalNum/2+1)+1)/2)*27),r);
        y = ConvertUnit.dpToPx(250+getSttStart()/(totalNum/2+1)*50,r);
        more.setMargins(x,y,more.rightMargin,more.bottomMargin);


        this.setLayoutParams(more);



        contentLayout.addView(this);
    }


    public LetterView(Context context) {
        super(context);
    }

    public LetterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LetterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected abstract void returnView();

    protected abstract void moveView();

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Attrs getAttrs() {
        return attrs;
    }

    public void setSttStart(int sttStart) {
        this.sttStart = sttStart;
    }

    public int getSttStart() {
        return sttStart;
    }

    public int getSttSeleted() {
        return sttSeleted;
    }

    public void setSttSeleted(int sttSeleted) {
        this.sttSeleted = sttSeleted;
    }
}
