package com.kimvan.hung.vocabulary.editWord;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kimvan.hung.vocabulary.Information.InformationActivity;
import com.kimvan.hung.vocabulary.MainActivity;
import com.kimvan.hung.vocabulary.R;
import com.kimvan.hung.vocabulary.addWord.AddWordActivity;
import com.kimvan.hung.vocabulary.dataBase.DataBaseHandler;
import com.kimvan.hung.vocabulary.dataBase.NormalWordMeaning;
import com.kimvan.hung.vocabulary.dataBase.NouveauMot;

import java.util.ArrayList;

public class EditWordActivity extends AppCompatActivity {

    TextView saveToolbar;
    TextView exitToolbar;
    TextView expertPoint;
    TextView shapeExpert;

    EditText leMot;
    EditText enAnglais;
    int posMeaningOnChanging=0;
    Boolean txtIsChanged=false;
    EditText enVietnamien;

    Spinner spinnerTypeWord;
    Spinner spinnerLv2;
    Boolean spinner_lv2_check=true;
    RelativeLayout layout_lv2;

    Toolbar toolbar;

    Intent intent;
    NouveauMot nouveauMotPara;
    NouveauMot nouveauMotAfter;
    ArrayList<NormalWordMeaning> normalWordMeaning;
    DataBaseHandler dbHandler;

    String[] type_word;
    String[] type_verb;
    String[] type_sex;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word);

        setPrimary();


    }

    public void setPrimary(){
        toolbar = (Toolbar)findViewById(R.id.toolbar_edit_word);
        setSupportActionBar(toolbar);


        dbHandler = new DataBaseHandler(this, MainActivity.DATABASE_NAME,null,1);
        Bundle data = getIntent().getExtras();
        nouveauMotPara = data.getParcelable("leMot");
        nouveauMotAfter=nouveauMotPara;

        expertPoint = (TextView)findViewById(R.id.expert_point_txt_edit_word);
        shapeExpert = (TextView)findViewById(R.id.shape_expert_point_edit_word);
        saveToolbar = (TextView)findViewById(R.id.toolbar_save_edit_word);
        exitToolbar = (TextView)findViewById(R.id.toolbar_exit_edit_word);
        leMot = (EditText)findViewById(R.id.le_mot_edit_txt_edit_word);
        enVietnamien = (EditText)findViewById(R.id.en_vietnamien_edit_txt_edit_word);
        enAnglais = (EditText)findViewById(R.id.en_englais_edit_txt_edit_word);
        spinnerTypeWord = (Spinner)findViewById(R.id.spinner_type_word_edit);
        spinnerLv2 = (Spinner)findViewById(R.id.spinner_type_word_lv2_edit_word);
        layout_lv2 = (RelativeLayout)findViewById(R.id.layout_lv2_edit_word);



        onClickToolbar();

        createSpinner();

        setUpTxt();
        try {
            setupUI(findViewById(R.id.layout_parent_edit_word));
        }catch (Exception e){
            e.getMessage();
        }
    }

    public void nextClicked(View view){
        saveState();
        posMeaningOnChanging=(posMeaningOnChanging+1)%normalWordMeaning.size();
        enAnglais.setText(normalWordMeaning.get(posMeaningOnChanging).get_enAnglais());
        enVietnamien.setText(normalWordMeaning.get(posMeaningOnChanging).get_enVietnamien());
    }

    private void saveState(){
        nouveauMotAfter.set_leMot(leMot.getText().toString());
        nouveauMotAfter.set_type_word(spinnerTypeWord.getSelectedItem().toString());
        if (spinner_lv2_check){
            nouveauMotAfter.set_lv2(spinnerLv2.getSelectedItem().toString());
        }else {
            nouveauMotAfter.set_lv2("");
        }

        normalWordMeaning.remove(posMeaningOnChanging);
        normalWordMeaning.add(posMeaningOnChanging,new NormalWordMeaning(nouveauMotPara.get_leMot(),enAnglais.getText().toString(),
                enVietnamien.getText().toString()));

    }

    private int getPositionSpinner(String value,String[] t){
        for (int i=0;i<t.length;i++){
            if (value.equals(t[i])){
                return i;
            }
        }
        return -1;
    }
    private void setSpinner_type_lv2(String name){
        switch (name){
            case "le nom":
                adapter = new ArrayAdapter<String>(
                        this,R.layout.spinner_add,R.id.text_view_spinner_item,type_sex
                );
                layout_lv2.setVisibility(RelativeLayout.VISIBLE);
                spinnerLv2.setVisibility(Spinner.VISIBLE);
                spinner_lv2_check = true;
                spinnerLv2.setAdapter(adapter);
                break;
            case "le verbe":
                adapter = new ArrayAdapter<String>(
                        this,R.layout.spinner_add,R.id.text_view_spinner_item,type_verb
                );
                layout_lv2.setVisibility(RelativeLayout.VISIBLE);
                spinnerLv2.setVisibility(Spinner.VISIBLE);
                spinner_lv2_check=true;
                spinnerLv2.setAdapter(adapter);
                break;
            case "le adj":
                adapter = new ArrayAdapter<String>(
                        this,R.layout.spinner_add,R.id.text_view_spinner_item,type_sex
                );
                layout_lv2.setVisibility(RelativeLayout.VISIBLE);
                spinnerLv2.setVisibility(Spinner.VISIBLE);
                spinner_lv2_check=true;
                spinnerLv2.setAdapter(adapter);
                break;
            default:
                layout_lv2.setVisibility(RelativeLayout.INVISIBLE);
                spinner_lv2_check=false;
                spinnerLv2.setVisibility(Spinner.INVISIBLE);
        }
    }

    private void setUpTxt(){
        leMot.setText(nouveauMotPara.get_leMot());
        normalWordMeaning = dbHandler.getNormalWordMeaning(nouveauMotPara.get_leMot());
        enAnglais.setText(normalWordMeaning.get(0).get_enAnglais());
        enVietnamien.setText(normalWordMeaning.get(0).get_enVietnamien());
        expertPoint.setText(String.valueOf(nouveauMotPara.get_expert_point()));
        nouveauMotPara.setColorExpert(shapeExpert);

    }

    private void createSpinner(){
        // create Spinner
        type_word = new String[]{"le nom","le pronom","le verbe","le adj","le article","le conjonction"};
        type_verb = new String[]{"Group 1","Group 2","Group 3"};
        type_sex =  new String[]{"male","female","tous"};

        // set up spinner type word
        adapter = new ArrayAdapter<String>(
                this,R.layout.spinner_add,R.id.text_view_spinner_item,type_word
        );
        spinnerTypeWord.setAdapter(adapter);

        //set up spinner type word 2
        adapter = new ArrayAdapter<String>(
                this,R.layout.spinner_add,R.id.text_view_spinner_item,type_sex
        );
        spinnerLv2.setAdapter(adapter);
        spinnerTypeWord.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        setSpinner_type_lv2((String)parent.getItemAtPosition(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
        spinnerTypeWord.setSelection(getPositionSpinner(nouveauMotPara.get_type_word(),type_word));
        try {
            if (spinnerLv2.getCount()==3){
                spinnerLv2.setSelection(getPositionSpinner(nouveauMotPara.get_lv2(),type_verb));
            }else {
                spinnerLv2.setSelection(getPositionSpinner(nouveauMotPara.get_lv2(),type_sex));
            }
        }catch (Exception e){
            e.getMessage();
        }
    }

    private void onClickToolbar(){
        saveToolbar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveState();
                        dbHandler.deleteTable(nouveauMotPara.get_leMot());
                        dbHandler.createAnotherTable(leMot.getText().toString(),nouveauMotAfter.get_type_word());

                        for (NormalWordMeaning x:normalWordMeaning){
                            x.setNameWord(leMot.getText().toString());
                            dbHandler.addMeaning(x);
                        }
                        dbHandler.updateWord(nouveauMotAfter);


                        Toast.makeText(getApplicationContext(),"Từ đã được thay đổi!",Toast.LENGTH_SHORT).show();
                        intent = new Intent(EditWordActivity.this,InformationActivity.class);
                        startActivity(intent);

                    }
                }
        );

        exitToolbar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(EditWordActivity.this, InformationActivity.class);
                        startActivity(intent);
                    }
                }
        );
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
        try {
            if (!(view instanceof EditText)) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        hideSoftKeyboard(EditWordActivity.this);
                        return false;
                    }
                });
            }
        }catch (Exception e){
            e.getMessage();
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
