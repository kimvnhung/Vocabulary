package com.kimvan.hung.vocabulary.addWord;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kimvan.hung.vocabulary.MainActivity;
import com.kimvan.hung.vocabulary.R;
import com.kimvan.hung.vocabulary.dataBase.Conjugation;
import com.kimvan.hung.vocabulary.dataBase.DataBaseHandler;
import com.kimvan.hung.vocabulary.dataBase.VerbWordMeaning;

import java.util.ArrayList;

public class ConjugationInput extends AppCompatActivity implements GestureDetector.OnDoubleTapListener,
        GestureDetector.OnGestureListener{

    ArrayList<Conjugation> conjugationArrayList = new ArrayList<>();
    TextView title_conjugation_input;
    TextView le_temp;

    EditText je,tu,ilElle,nous,vous,ilsElles;
    VerbWordMeaning verbWordMeaning ;
    DataBaseHandler dbHandler ;
    Intent intent;

    String[] lesTemps = {
            "Présent",
            "Passé composé",
            "Imparfait",
            "Plus-que-parfait",
            "Futur simple",
            "Futur antérieur"
    };

    private GestureDetectorCompat gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conjugation_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_conjugation_input);
        setSupportActionBar(toolbar);

        setUpPrimary();

        Bundle mData = getIntent().getExtras();

        verbWordMeaning = new VerbWordMeaning(mData.getString("leMot"),mData.getString("enAnlais"),
                mData.getString("enVietnamien"));

        setupUI(findViewById(R.id.layout_conjugation_input));


    }


    public void setUpPrimary(){

        dbHandler = new DataBaseHandler(this, MainActivity.DATABASE_NAME,null,1);
        gestureDetector = new GestureDetectorCompat(this,this);
        gestureDetector.setOnDoubleTapListener(this);

        le_temp = (TextView)findViewById(R.id.le_temps_txt);
        le_temp.setText(lesTemps[0]);

        je = (EditText)findViewById(R.id.input_je_conjugation_edittxt);
        tu = (EditText)findViewById(R.id.input_tu_conjugation_edittxt);
        ilElle = (EditText)findViewById(R.id.input_il_elle_conjugation_edittxt);
        nous = (EditText)findViewById(R.id.input_nous_conjugation_edittxt);
        vous = (EditText)findViewById(R.id.input_vous_conjugation_edittxt);
        ilsElles = (EditText)findViewById(R.id.input_ils_elles_conjugation_edittxt);

        title_conjugation_input = (TextView)findViewById(R.id.title_conjugation_input);
    }

    public void saveConjugatonClicked(View view){
        verbWordMeaning.setLesTemps(conjugationArrayList);
        intent = new Intent(this,AddWordActivity.class);
        startActivity(intent);
        Toast.makeText(this,R.string.add_complete,Toast.LENGTH_SHORT).show();
    }



    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        final String curTemp = le_temp.getText().toString();
        if (conjugationArrayList.size()<9){
            conjugationArrayList.add(new Conjugation(curTemp,je.getText().toString(),tu.getText().toString(),
                    ilElle.getText().toString(),nous.getText().toString(),vous.getText().toString(),ilsElles.getText().toString()));
        }else {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            for (int i=0;i<conjugationArrayList.size();i++){
                                if (curTemp.equals(conjugationArrayList.get(i).get_leTemps())){
                                    conjugationArrayList.remove(i);
                                    conjugationArrayList.add(i,new Conjugation(curTemp,je.getText().toString(),
                                            tu.getText().toString(),ilElle.getText().toString(),nous.getText().toString(),
                                            vous.getText().toString(),ilsElles.getText().toString()));
                                }
                            }
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            le_temp.setText(curTemp);
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.ghi_de).setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
        for (int i=0;i<lesTemps.length;i++){
            if (curTemp.equals(lesTemps[i])){
                le_temp.setText(lesTemps[(i+1)%lesTemps.length]);
                break;
            }
        }
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    // hide keyboard when tap outside of EditText
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(ConjugationInput.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
}
