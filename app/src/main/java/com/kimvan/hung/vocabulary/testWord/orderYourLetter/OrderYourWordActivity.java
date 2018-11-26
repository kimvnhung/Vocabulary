package com.kimvan.hung.vocabulary.testWord.orderYourLetter;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kimvan.hung.vocabulary.MainActivity;
import com.kimvan.hung.vocabulary.R;
import com.kimvan.hung.vocabulary.dataBase.DataBaseHandler;
import com.kimvan.hung.vocabulary.dataBase.NouveauMot;

import java.util.ArrayList;

public class OrderYourWordActivity extends AppCompatActivity {

    RelativeLayout contentLayout;
    TextView showMeaning;
    TextView point;
    TextView help;
    Button btCheck;

    DataBaseHandler  dbHandler;
    Game2Handler models;

    ArrayList<NouveauMot> checkDoubleAskArr;
    ArrayList<LetterView> listQuestion;
    ArrayList<LetterView> listAnswer;
    char[] listRadomChar;

    NouveauMot input;
    boolean touchable;
    float defaultTextSize ;
    int helpCounting;

    int numQuestionPlayed;
    int _best_score_in_day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_your_word);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_order_your_word);
        setSupportActionBar(toolbar);

        listQuestion = new ArrayList<>();
        listAnswer = new ArrayList<>();
        checkDoubleAskArr = new ArrayList<>();



        contentLayout = (RelativeLayout) findViewById(R.id.content_layout_order_ur_word);
        contentLayout.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        deleteSharedPreferencesData();
                        return false;
                    }
                }
        );

        showMeaning = (TextView) findViewById(R.id.question_content_order_ur_word);

        point = (TextView) findViewById(R.id.point_order_ur_word);
        point.setText(getCurrentScore());
        GradientDrawable gd = (GradientDrawable)point.getBackground().getCurrent();
        gd.setColor(Color.WHITE);


        defaultTextSize = ConvertUnit.dpToPx(Attrs.defaultTextSize,getResources());
        helpCounting = 0;
        numQuestionPlayed = 0;


        help = (TextView) findViewById(R.id.help_icon_order_ur_word);
        help.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        helpClicked();
                    }
                }
        );

        btCheck = (Button) findViewById(R.id.btCheck_order_ur_word);
        btCheck.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btCheckClicked();
                    }
                }
        );

        dbHandler = new DataBaseHandler(this, MainActivity.DATABASE_NAME,null,1);
        models = new Game2Handler(dbHandler);

        //set up lại câu hỏi cũ nếu có
        setLastQuestion();






        //kiểm tra xem ngày hôm nay đã chơi chưa
        MainActivity.isPlayed = models.isPlayed();

        //thiết đặt bestscore
        //lấy bestScoreInDay
        if (MainActivity.isPlayed){
            _best_score_in_day = models.getBestScoreInSth(models.DATE_TABLE_NAME,models.getTimeOnTodayBegining());
        }else {
            _best_score_in_day=0;
        }


    }

    private void deleteSharedPreferencesData() {
        SharedPreferences prefs = this.getSharedPreferences("orderYourWordData", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
        Toast.makeText(this,"delete complete",Toast.LENGTH_SHORT).show();
    }

    private void setLastQuestion() {
        try {
            //getting preferences
            SharedPreferences prefs = this.getSharedPreferences("orderYourWordData", Context.MODE_PRIVATE);

            if (prefs != null){
                if (prefs.getString("le_mot",null) == null){
                    changeQuestion();
                }else {
                    //lấy từ mới
                    String leMot = prefs.getString("le_mot", null);
                    input = dbHandler.getNouveauMot(leMot);
                    if (input == null){
                        throw new Exception("Từ đã bị xóa hoặc không tồn tại");
                    }
                    //lấy nghĩa
                    String meaning = prefs.getString("lastest_content_question", null);
                    if (meaning != null) {
                        showMeaning.setText(meaning);
                        showMeaning.setTextColor(Color.parseColor(NouveauMot.getColorExpert(input.get_expert_point())));
                    }

                    //tạo list letterview
                    char[] listOldRadomChar = new char[5];
                    for (int i = 0; i < 5; i++){
                        listOldRadomChar[i] = prefs.getString("radom_char"+i," ").charAt(0);
                    }
                    createView(getItems(listOldRadomChar));

                    //lấy listAnswer
                    int answerSelectedLength = prefs.getInt("answer_selected_length",0);
                    boolean stillCorrectChar = true;
                    for (int i = 0; i < answerSelectedLength; i++) {
                        String letter = prefs.getString("ans" + i, null);
                        //tìm trong listquestion để dưa letter lên

                        for (int j = 0; j < listQuestion.size(); j++) {

                            if (listQuestion.get(j).getText().toString().equals(letter)) {
                                listQuestion.get(j).moveView();
                                listQuestion.get(j).setSelected(true);

                                if (!listQuestion.get(j).getText().toString().equals(input.get_leMot().charAt(i)+"")){
                                    stillCorrectChar = false;
                                }

                                //nếu đã check thì sẽ gọi đến checkbt
                                if (prefs.getBoolean("ischeck" + i, false)) {
                                    btCheckClicked();
                                    if (stillCorrectChar){
                                        listQuestion.get(j).setClickable(false);
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            changeQuestion();
        }

    }

    // lưu các thông tin khi thoát game
    @Override
    protected void onStop() {
        super.onStop();
        saveData(Integer.parseInt(point.getText().toString()));
        int new_best = Math.max(_best_score_in_day,numQuestionPlayed);
        models.insertValueInDate(numQuestionPlayed,new_best );

        if (new_best > models.getBestScore()){
            models.updateScore(new_best);
        }

    }


    //lưu điểm, câu hỏi hiện tại vào bộ nhớ máy
    private void saveData(int score) {
        //setting preferences
        SharedPreferences prefs = this.getSharedPreferences("orderYourWordData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("score", score);
        editor.putString("le_mot",input.get_leMot());
        editor.putString("lastest_content_question",showMeaning.getText().toString());
        for (int i = 0; i < listAnswer.size(); i++){
            editor.putString("ans"+i,listAnswer.get(i).getText().toString());
            editor.putBoolean("ischeck"+i,listAnswer.get(i).getCurrentTextColor() == Color.RED
                    || listAnswer.get(i).getCurrentTextColor() == Color.GREEN);
        }
        for (int i = 0; i < 5 ; i++){
            editor.putString("radom_char"+i,listRadomChar[i]+"");
        }
        editor.putInt("answer_selected_length",listAnswer.size());
        editor.apply();
    }

    // lấy điểm trong bộ nhớ máy
    private String getCurrentScore() {
        //getting preferences
        SharedPreferences prefs = this.getSharedPreferences("orderYourWordData", Context.MODE_PRIVATE);
        int score ;
        if (prefs == null){
            score = 0;
        }else {
            score = prefs.getInt("score",0);
        }
        return String.valueOf(score);
    }

    //khi click trợ giúp
    private void helpClicked() {
        //nếu hết điểm thì ko được trợ giúp
        if (Integer.parseInt(point.getText().toString()) <= 0){
            Toast.makeText(this,"Hết sự trợ giúp!",Toast.LENGTH_SHORT).show();

        }else {
            try {
                //khi đã chọn kí tự
                if (listAnswer.size() > 0){
                    //khi chưa ấn check thì sẽ check
                    if (listAnswer.get(listAnswer.size()-1).getCurrentTextColor() == Attrs.defaultTextColor){
                        btCheckClicked();
                    }

                    int wrongidx = listAnswer.size();
                    //unclickable những kí tự đúng
                    for (int i = 0; i < listAnswer.size(); i++){
                        if (listAnswer.get(i).getText().toString().equals(input.get_leMot().charAt(i)+"")){
                            listAnswer.get(i).setClickable(false);
                            colorTextAnimation(listAnswer.get(i),"true");

                        }else {
                            wrongidx = i;
                            break;
                        }
                    }
                    //trả về các kí tự sai
                    int size = listAnswer.size();
                    for (int i = wrongidx ; i < size; i++){
                        listAnswer.get(wrongidx).setSelected(false);
                        listAnswer.get(wrongidx).returnView();
                    }

                    //khi kết quả chưa đúng thì sử dụng trợ giúp
                    if (listAnswer.size() != input.get_leMot().length()){
                        doingHelp();
                    }else {
                        //kết thúc câu hỏi
                        btCheckClicked();
                    }

                }else {
                    doingHelp();
                }

            }catch (Exception e){
                Toast.makeText(this,"Hết từ gợi ý!",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void doingHelp() {
        //select the next correct letter
        char nextLetter = input.get_leMot().charAt(listAnswer.size());
        for (int i = 0; i < listQuestion.size(); i++) {
            if (listQuestion.get(i).getText().toString().equals(nextLetter + "")
                    && !listQuestion.get(i).isSelected()) {
                listQuestion.get(i).moveView();//chuyển kí tự đúng lên đáp án
                listQuestion.get(i).setClickable(false);//unclickable
                listQuestion.get(i).setSelected(true);
                colorTextAnimation(listQuestion.get(i),"true");//chuyển sang màu xanh
                break;
            }
        }
        //minus point
        int minusPoint = Math.max(Integer.parseInt(point.getText().toString()) - 5,0);
        point.setText(String.valueOf(minusPoint));
        //adding to helpCounting
        helpCounting++;
    }


    // tách đáp án ra thành các kí tự
    private String[] getItems(char[] listRadomChar) {
        //save listRadom
        this.listRadomChar = listRadomChar;


        //mix list
        String[] output = new String[input.get_leMot().length()+5];

        StringBuilder mixString = new StringBuilder(input.get_leMot());
        for (int i = 0; i < 5; i++){
            //thêm kí tự ngẫu nhiên
            mixString.append(listRadomChar[i]);

        }
        for (int i = 0; i < mixString.length()/2; i++){
            int random = (int) (Math.random()*(mixString.length()-1));
            char para = mixString.charAt(random);
            char atI = mixString.charAt(i);
            mixString.replace(random,random+1, String.valueOf(atI));
            mixString.replace(i,i+1, String.valueOf(para));

        }

        for (int i = 0; i < output.length; i++){
            output[i] = mixString.charAt(i)+"";
        }

        return output;
    }

    private char getRadomChar() {
        int radom = (int) (97 + Math.random()*34);
        int result = 97;
        switch (radom){
            case 123 :
                result = 39;//"'"
                break;
            case 124 :
                result = 233;//"é"
                break;
            case 125 :
                result = 224;//"à"
                break;
            case 126 :
                result = 232;//"è"
                break;
            case 127 :
                result = 249;//"ù"
                break;
            case 128 :
                result = 226;//"â"
                break;
            case 129 :
                result = 234;//"ê"
                break;
            case 130 :
                result = 244;//"ô"
                break;
            case 131 :
                result = 32;//" "
            default:
                result = radom;
                break;
        }
        return (char) result;
    }

    //tạo các view kí tự có thể chọn
    private void createView(final String[] items) {

        for (int i = 0; i < items.length; i++){


            LetterView adding = new LetterView(this,contentLayout,items[i],i,items.length) {

                //trả view về vị trí xuất hiện
                @Override
                protected void returnView() {
                    RelativeLayout.LayoutParams more = (RelativeLayout.LayoutParams) getLayoutParams();
                    //position the views
                    Resources r = this.getResources();


                    int x,y;
                    x = ConvertUnit.dpToPx((int) (150-Math.pow(-1,getSttStart()%2)*((getSttStart()%(items.length/2+1)+1)/2)*27),r);
                    y = ConvertUnit.dpToPx(250+getSttStart()/(items.length/2+1)*50,r);

                    TransitionManager.beginDelayedTransition(contentLayout);


                    more.setMargins(x,y,more.rightMargin,more.bottomMargin);


                    this.setLayoutParams(more);

                    //set none selected
                    listAnswer.remove(getSttSeleted());


                    for (int i = getSttSeleted(); i < listAnswer.size(); i++){
                        LetterView viewEdit = listAnswer.get(i);
                        viewEdit.setSttSeleted(viewEdit.getSttSeleted()-1);
                        int xi,yi;
                        if (viewEdit.getSttSeleted() >=9 ){
                            xi = ConvertUnit.dpToPx(20+(viewEdit.getSttSeleted()%9)*25+(viewEdit.getSttSeleted()%9-1)*2,getResources());
                        }else {
                            xi = ConvertUnit.dpToPx(50+(viewEdit.getSttSeleted()%9)*25+(viewEdit.getSttSeleted()%9-1)*2,getResources());
                        }
                        yi = ConvertUnit.dpToPx( 120+viewEdit.getSttSeleted()/9*50,getResources());

                        TransitionManager.beginDelayedTransition(contentLayout);
                        //change the position
                        RelativeLayout.LayoutParams positionRule = (RelativeLayout.LayoutParams) viewEdit.getLayoutParams();

                        positionRule.setMargins(xi,yi,positionRule.rightMargin,positionRule.bottomMargin);

                        viewEdit.setLayoutParams(positionRule);
                    }

                    setSttSeleted(-1);
                    colorTextAnimation(this,"default");

                }


                //đưa view tới ví trí kế tiếp đc chọn trên dòng đáp án
                @Override
                protected void moveView() {
                    //set to answer or none
                    listAnswer.add(this);
                    setSttSeleted(listAnswer.size()-1);

                    int x,y;
                    if (getSttSeleted() >=9 ){
                        x = ConvertUnit.dpToPx(20+(getSttSeleted()%9)*25+(getSttSeleted()%9-1)*2,getResources());
                    }else {
                        x = ConvertUnit.dpToPx(50+(getSttSeleted()%9)*25+(getSttSeleted()%9-1)*2,getResources());
                    }
                    y = ConvertUnit.dpToPx( 120+getSttSeleted()/9*50,getResources());

                    TransitionManager.beginDelayedTransition(contentLayout);
                    //change the position
                    RelativeLayout.LayoutParams positionRule = (RelativeLayout.LayoutParams) getLayoutParams();

                    positionRule.setMargins(x,y,positionRule.rightMargin,positionRule.bottomMargin);

                    setLayoutParams(positionRule);



                }
            };

            listQuestion.add(adding);
        }
    }


    //khi click button check
    public void btCheckClicked(){
        if (listAnswer.get(listAnswer.size()-1).getCurrentTextColor() == Attrs.defaultTextColor){
            //minus point
            int pointAfterMinus = Integer.parseInt(point.getText().toString())-1;
            animatePointCounting();
            point.setText(pointAfterMinus+"");
        }


        StringBuilder answer = new StringBuilder();
        for (int i = 0; i < listAnswer.size(); i++){
            answer.append(listAnswer.get(i).getText().toString());
        }

        if (isCorrectedAnswer(answer)){
            //adding point
            double percentCorect =  1-(helpCounting/(double)input.get_leMot().length());
            final int pointEarning = percentCorect==0?1:(int)(percentCorect*4);
            point.setText(String.valueOf(Integer.parseInt(point.getText().toString())+pointEarning));

            //transition view to point circle
            for (int i = 0; i < listAnswer.size(); i++){
                destroyLetterView(listAnswer.get(i));
            }
            //animation point-counting
            animatePointCounting();
            //unclickable all view
            for (int i = 0; i < listQuestion.size(); i++){
                listQuestion.get(i).setClickable(false);
            }

            //delay and change the question
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 2s = 2000ms

                    //increase experpoint
                    NouveauMot update = input;
                    update.set_expert_point(input.get_expert_point()-(pointEarning==1?-2:pointEarning));
                    if (update.get_expert_point() <= 0){
                        update.set_expert_point(1);
                    }
                    dbHandler.updateWord(update);

                    //change the question
                    changeQuestion();
                    //return default size
                    point.setTextSize(defaultTextSize);

                }
            }, 200);
        }else {

        }


    }

    //animate tăng điểm
    private void animatePointCounting() {

        final float startSize = defaultTextSize; // Size in pixels
        final float endSize = ConvertUnit.dpToPx(20,getResources());
        long animationDuration = 200; // Animation duration in ms

        final ValueAnimator animator = ValueAnimator.ofFloat(startSize, endSize);
        animator.setDuration(animationDuration);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                point.setTextSize(animatedValue);
            }
        });

        animator.start();

        //delay and change the question
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after time
                //return default size
                //animator.end();
                point.setTextSize(defaultTextSize);

            }
        }, 200);
    }

    //chuyển hết đối tượng letter view về 1 góc và cho size = (1,1)
    private void destroyLetterView(LetterView letterView) {
        int x,y;
        x = ConvertUnit.dpToPx(25,getResources());
        y = ConvertUnit.dpToPx(25,getResources());

        TransitionManager.beginDelayedTransition(contentLayout);
        //change the position
        RelativeLayout.LayoutParams positionRule = (RelativeLayout.LayoutParams) letterView.getLayoutParams();

        positionRule.setMargins(positionRule.leftMargin,y,x,positionRule.bottomMargin);
        positionRule.addRule(RelativeLayout.ALIGN_PARENT_END,RelativeLayout.TRUE);
        //change size
        positionRule.width = 1;
        positionRule.height = 1;

        letterView.setLayoutParams(positionRule);
    }

    //kiểm tra từng kí tự trong câu trả lời
    private boolean isCorrectedAnswer(StringBuilder answer) {
        boolean result = false;

        //decorate the text
        StringBuilder correctedAnswer = new StringBuilder(input.get_leMot());
        for (int i = 0; i < answer.length(); i++){
            if (i < correctedAnswer.length() && answer.charAt(i) == correctedAnswer.charAt(i)){
                colorTextAnimation(listAnswer.get(i),"true");
            }else {

                colorTextAnimation(listAnswer.get(i),"false");
            }
        }
        //check entire answer
        if (answer.toString().equals(input.get_leMot())){
            result = true;
        }
        return result;
    }

    //thay đổi nội dung câu hỏi
    private void changeQuestion() {
        //increase number question we played
        numQuestionPlayed++;
        //xóa câu hỏi cũ
        for (LetterView x: listQuestion){
            contentLayout.removeView(x);
        }
        listQuestion.clear();
        listAnswer.clear();

        //reset helpCounting
        helpCounting = 0;
        //hiện câu hỏi mới
        do {
            showMeaning.setText(models.getContentQuestion());
            input = models.getNouveauMotSelected();
            //cài màu exper cho câu hỏi
            showMeaning.setTextColor(Color.parseColor(NouveauMot.getColorExpert(input.get_expert_point())));
            //kiểm tra xem có trùng câu hỏi
        }while (checkDoubleAsk(models.getNouveauMotSelected()));

        createView(getItems(getListRadomChar()));
    }

    private char[] getListRadomChar() {
        char[] result = new char[5];
        for (int i = 0; i < 5; i++ ){
            result[i] = getRadomChar();
        }
        return result;
    }

    //kiểm tra câu hỏi trùng trong (10% số lượng từ)  câu gần nhất
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


    //animation
    private void colorTextAnimation(LetterView view, String result){
        int color ;
        switch (result){
            case "true":
                color = Color.GREEN;
                break;
            case "false":
                color = Color.RED;
                break;
            default:
                color = Attrs.defaultTextColor;
                break;
        }
        ObjectAnimator colorAnim = ObjectAnimator.ofInt(view, "textColor",
                view.getCurrentTextColor(), color);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setDuration(100); // 100ms
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();
    }
}
