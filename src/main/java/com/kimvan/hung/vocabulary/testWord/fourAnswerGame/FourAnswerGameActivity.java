package com.kimvan.hung.vocabulary.testWord.fourAnswerGame;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.kimvan.hung.vocabulary.MainActivity;
import com.kimvan.hung.vocabulary.R;
import com.kimvan.hung.vocabulary.dataBase.DataBaseHandler;
import com.kimvan.hung.vocabulary.dataBase.NormalWordMeaning;
import com.kimvan.hung.vocabulary.dataBase.NouveauMot;

import java.util.ArrayList;

public class FourAnswerGameActivity extends AppCompatActivity {

    TextView show_le_mot;
    TextView answer1;
    TextView answer2;
    TextView answer3;
    TextView answer4;

    DataBaseHandler dbHandler;
    Game1Handler game1Handler;

    NouveauMot nouveauMot;
    ArrayList<NouveauMot> checkDoubleAskArr = new ArrayList<>();
    NormalWordMeaning normalWordMeaning;
    String[] answer = new String[4];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_answer_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_four_answer_game);
        setSupportActionBar(toolbar);

        dbHandler = new DataBaseHandler(this, MainActivity.DATABASE_NAME,null,1);
        game1Handler = new Game1Handler(dbHandler);

        setId();

        setContentAsking();
    }

    //kiểm tra câu hỏi trùng trong 4 câu gần nhất
    public boolean checkDoubleAsk(NouveauMot nouveauMot){
        for (NouveauMot x:checkDoubleAskArr){
            if (x.get_leMot().equals(nouveauMot.get_leMot())){
                return true;
            }
        }
        if (checkDoubleAskArr.size()==4){
            checkDoubleAskArr.remove(0);
        }
        checkDoubleAskArr.add(nouveauMot);
        return false;
    }

    // set up nội dung câu hỏi
    public void setContentAsking(){
        do {
            answer = game1Handler.getAnswer();
            nouveauMot = game1Handler.getNouveauMotSelected();
            normalWordMeaning = game1Handler.getNormalWordMeaningSelected();
        }while (checkDoubleAsk(nouveauMot));


        String[] para = mixAnswer(answer);
        answer1.setText(para[0]);
        answer2.setText(para[1]);
        answer3.setText(para[2]);
        answer4.setText(para[3]);

        show_le_mot.setText(nouveauMot.get_leMot());
        nouveauMot.setColorExpert(show_le_mot);

    }

    //nghe phát âm
    public void soundWordTestClicked(View view){
        String txt = show_le_mot.getText().toString();

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(txt);
    }

    //trộn câu trả lời
    private String[] mixAnswer(String[] answer){
        String para = answer[0];
        int random = (int)(Math.random()*4);
        answer[0]=answer[random];
        answer[random]=para;
        para = answer[1];
        random = (int)(Math.random()*4);
        answer[1] = answer[random];
        answer[random]=para;

        return answer;
    }

    private void setId(){
        show_le_mot = (TextView)findViewById(R.id.show_le_mot_txt_4answer_game);
        answer1 = (TextView)findViewById(R.id.answer_1_4answer_game);
        answer2 = (TextView)findViewById(R.id.answer_2_4answer_game);
        answer3 = (TextView)findViewById(R.id.answer_3_4answer_game);
        answer4 = (TextView)findViewById(R.id.answer_4_4answer_game);

        setOnClickAnswer(answer1);
        setOnClickAnswer(answer2);
        setOnClickAnswer(answer3);
        setOnClickAnswer(answer4);
    }

    //cài  đặt phím trả lời
    private void setOnClickAnswer(final TextView answer){
        answer.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handlerAfterClickAnswer(answer);
                    }
                }
        );
    }

    //xử lý hiệu ứng + cộng trừ điểm sau click
    private void handlerAfterClickAnswer(TextView view){
        boolean para = checkAnswer(view.getText().toString());
        int point = nouveauMot.get_expert_point();
        if (para){
            point-=3;
        }else {
            point++;
        }
        dbHandler.updateWord(new NouveauMot(nouveauMot.get_id(),nouveauMot.get_leMot(),
              nouveauMot.get_type_word(),nouveauMot.get_lv2(),point));
        setClickAnswerAnimation(view,para);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 2s = 2000ms
                setContentAsking();
                setDefaultAnswer();

            }
        }, 2000);
    }

    //kiểm tra câu trả lời đúng sai?
    private boolean checkAnswer(String answerTxt){
        if (answerTxt.equals(normalWordMeaning.get_enVietnamien())){
            return true;
        }
        return false;
    }

    //animation sau click
    private void setClickAnswerAnimation(View view,Boolean isTrue){
        GradientDrawable gd = (GradientDrawable)view.getBackground().getCurrent();

        final View paraView = view;
        if (isTrue){
            gd.setColor(getResources().getColor(R.color.colorClicked));

        }else {
            gd.setColor(getResources().getColor(R.color.colorBeginner));
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 0,5s = 500ms
                colorAnswerAnimation(paraView);

            }
        }, 500);
    }

    //animation
    private void colorAnswerAnimation(View view){
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(200); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(5); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        view.startAnimation(animation);


    }

    //khởi tạo lại dao diện mặc định
    private void setDefaultAnswer(){
        GradientDrawable gd = (GradientDrawable)answer1.getBackground().getCurrent();
        gd.setColor(getResources().getColor(R.color.colorTitileMain));
        gd.setStroke(1,getResources().getColor(android.R.color.holo_blue_dark));

        gd = (GradientDrawable)answer2.getBackground().getCurrent();
        gd.setColor(getResources().getColor(R.color.colorTitileMain));
        gd.setStroke(1,getResources().getColor(android.R.color.holo_blue_dark));

        gd = (GradientDrawable)answer3.getBackground().getCurrent();
        gd.setColor(getResources().getColor(R.color.colorTitileMain));
        gd.setStroke(1,getResources().getColor(android.R.color.holo_blue_dark));

        gd = (GradientDrawable)answer4.getBackground().getCurrent();
        gd.setColor(getResources().getColor(R.color.colorTitileMain));
        gd.setStroke(1,getResources().getColor(android.R.color.holo_blue_dark));

    }

}
