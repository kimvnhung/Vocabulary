package com.kimvan.hung.vocabulary.testWord.fourAnswerGame;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kimvan.hung.vocabulary.MainActivity;
import com.kimvan.hung.vocabulary.R;
import com.kimvan.hung.vocabulary.dataBase.DataBaseHandler;
import com.kimvan.hung.vocabulary.dataBase.NormalWordMeaning;
import com.kimvan.hung.vocabulary.dataBase.NouveauMot;

import java.util.ArrayList;

public class FourAnswerGameActivity extends AppCompatActivity {

    LinearLayout questionLayout;

    int SIZE_QUESTION ;

    TextView show_le_mot;
    TextView answer1;
    TextView answer2;
    TextView answer3;
    TextView answer4;

    TextView cur_score_txt_view;
    TextView best_score_txt_view;
    int _cur_score;
    int _best_score;
    int _best_score_in_day;
    int _num_question_in_this;

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

        //kiểm tra xem ngày hôm nay đã chơi chưa
        MainActivity.isPlayed = game1Handler.isPlayed();

        //lấy bestScoreInDay
        if (MainActivity.isPlayed){
            _best_score_in_day = game1Handler.getBestScoreInSth(game1Handler.DATE_TABLE_NAME,game1Handler.getTimeOnTodayBegining());
        }else {
            _best_score_in_day=0;
        }

        _num_question_in_this = 0;
        _best_score=game1Handler.getBestScore();
        _cur_score=0;
        setScore(_cur_score);
    }

    //kiểm tra câu hỏi trùng trong 4 câu gần nhất
    public boolean checkDoubleAsk(NouveauMot nouveauMot){
        for (NouveauMot x:checkDoubleAskArr){
            if (x.equals(nouveauMot)){
                return true;
            }
        }
        //số từ giới hạn 1 lần lặp lại
        double sPercent = dbHandler.getTableCount(dbHandler.TABLE_NAME_VOCABULARY)*0.1;
        int distanceRepeat = (int) (sPercent>3?sPercent:3);
        if (checkDoubleAskArr.size()==distanceRepeat){
            checkDoubleAskArr.remove(0);
        }
        checkDoubleAskArr.add(nouveauMot);
        return false;
    }

    // set up nội dung câu hỏi
    public void setContentAsking(){
        setClickableAnswer(true);
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
        NouveauMot.setColorExpert(show_le_mot,nouveauMot.get_expert_point());
        setSizeByScore(show_le_mot);

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

        questionLayout = (LinearLayout)findViewById(R.id.layout_question_4answer_game);
        questionLayout.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        game1Handler.setAnglaisouvietnamien(!game1Handler.getAnglaisOuVietnamien());
                        changeAnswerToAnotherMeaning();
                        Toast.makeText(getApplicationContext(),"it's ok",Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
        );

        show_le_mot = (TextView)findViewById(R.id.show_le_mot_txt_4answer_game);
        answer1 = (TextView)findViewById(R.id.answer_1_4answer_game);
        answer2 = (TextView)findViewById(R.id.answer_2_4answer_game);
        answer3 = (TextView)findViewById(R.id.answer_3_4answer_game);
        answer4 = (TextView)findViewById(R.id.answer_4_4answer_game);

        setOnClickAnswer(answer1);
        setOnClickAnswer(answer2);
        setOnClickAnswer(answer3);
        setOnClickAnswer(answer4);


        cur_score_txt_view = (TextView)findViewById(R.id.score_now_4answer_game);
        best_score_txt_view = (TextView)findViewById(R.id.best_score_4answer_game);
    }

    //cài  đặt phím trả lời
    private void setOnClickAnswer(final TextView answer){
        answer.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setClickableAnswer(false);
                        handlerAfterClickAnswer(answer);
                    }
                }
        );
    }

    //vô hiệu hóa phím trả lời
    private void setClickableAnswer(boolean onOff){
        answer1.setClickable(onOff);
        answer2.setClickable(onOff);
        answer3.setClickable(onOff);
        answer4.setClickable(onOff);
    }
    //xử lý hiệu ứng + cộng trừ điểm sau click
    private void handlerAfterClickAnswer(TextView view){
        boolean para = checkAnswer(view.getText().toString());
        int point = nouveauMot.get_expert_point();
        if (para){
            point-=3;
            _cur_score++;
            setScore(_cur_score);
        }else {
            _cur_score=0;
            setScore(_cur_score);
            point++;
        }
        dbHandler.updateWord(new NouveauMot(nouveauMot.get_id(),nouveauMot.get_leMot(),
              nouveauMot.get_type_word(),nouveauMot.get_lv2(),point>0?point:1));
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
        _num_question_in_this++;
        String checkAnswer = "";
        if (game1Handler.getAnglaisOuVietnamien()){
            checkAnswer = normalWordMeaning.get_enVietnamien();
        }else {
            checkAnswer = normalWordMeaning.get_enAnglais();
        }
        if (answerTxt.equals(checkAnswer)){
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
        animation.setDuration(200); // duration - 200ms
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


    //khởi tạo điểm
    public void setScore(int curScore){
        if (curScore>=_best_score_in_day){//kiểm tra xem đã vượt quá bestScoreInday hay chưa
            _best_score_in_day = curScore;
            if (curScore>=_best_score){//kiểm tra xem đã vượt quá bestScore chưa
                _best_score=curScore;
                game1Handler.updateScore(_best_score);
            }
        }
        //hiển thị best_score
        best_score_txt_view.setText(Integer.toString(_best_score));
        NouveauMot.setColorExpert(best_score_txt_view,_best_score);

        //hiển thị curScore
        cur_score_txt_view.setText(""+curScore);

        //tạo màu nền cho curScore
        if (_best_score==0){//trường hợp chưa có bestScore
            NouveauMot.setColorExpert(cur_score_txt_view,100);
        }else {
            NouveauMot.setColorExpert(cur_score_txt_view,100-(curScore*100/_best_score));
        }


    }

    //tạo size câu hỏi theo điểm hiện tại
    public void setSizeByScore(TextView view){
        //đặt size_question theo độ dài từ
        /*String question = nouveauMot.get_leMot();
        SIZE_QUESTION = 50*12/question.length();
        //1 là đơn vị dp
        int percent;
        if(_best_score>=50){
            percent = _cur_score*100/_best_score;
        }else {
            percent=_cur_score*2;
        }*/

        view.setTextSize(1,24);
    }

    //thay đổi câu trả lời trên dao diện
    private void changeAnswerToAnotherMeaning(){
        String oldMeaning = answer1.getText().toString();
        answer1.setText(game1Handler.getSameMeaning(oldMeaning));

        oldMeaning = answer2.getText().toString();
        answer2.setText(game1Handler.getSameMeaning(oldMeaning));

        oldMeaning = answer3.getText().toString();
        answer3.setText(game1Handler.getSameMeaning(oldMeaning));

        oldMeaning = answer4.getText().toString();
        answer4.setText(game1Handler.getSameMeaning(oldMeaning));

    }

    //cập nhật số câu hỏi và bestScore
    @Override
    protected void onStop() {
        super.onStop();
        game1Handler.insertValueInDate(_num_question_in_this,_best_score_in_day);
    }
}
