package com.kimvan.hung.vocabulary.addWord;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.api.GoogleAPIException;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;
import com.google.api.translate.TranslateV2;
import com.google.api.translate.TranslatorFrame;
import com.kimvan.hung.vocabulary.MainActivity;
import com.kimvan.hung.vocabulary.R;
import com.kimvan.hung.vocabulary.dataBase.DataBaseHandler;
import com.kimvan.hung.vocabulary.dataBase.NormalWordMeaning;
import com.kimvan.hung.vocabulary.dataBase.NouveauMot;
import com.kimvan.hung.vocabulary.dataBase.VerbWordMeaning;

public class AddWordActivity extends AppCompatActivity {

    Intent intent;

    String[] type_word;
    String[] type_verb;
    String[] type_sex;

    EditText nouveau_mot;
    EditText new_word;
    EditText tu_moi;

    Toolbar toolbar_add_word;


    ArrayAdapter<String> adapter;
    Spinner spinner_type_word;
    Spinner spinner_type_lv2;
    Boolean spinner_lv2_check;

    DataBaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        // cài đặt khởi tạo giá trị,ID
        setUpPrimary();


    }


    //kiểm tra từ có dấu cách hay ko
    private boolean haveSpace(String s){
        for (int i=0;i<s.length();i++){
            if ((s.charAt(i)+"").equals(" ")){
                return true;
            }
        }
        return false;
    }

    //save le mot click
    public void saveAddClicked(View view){
        String nouveau_mot_para = nouveau_mot.getText().toString();
        String new_word_para = new_word.getText().toString();
        String tu_moi_para = tu_moi.getText().toString();
        String type_word_para = spinner_type_word.getSelectedItem().toString();
        String lv2_para = spinner_type_lv2.getSelectedItem().toString();
        Cursor c = new Cursor() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public int getPosition() {
                return 0;
            }

            @Override
            public boolean move(int offset) {
                return false;
            }

            @Override
            public boolean moveToPosition(int position) {
                return false;
            }

            @Override
            public boolean moveToFirst() {
                return false;
            }

            @Override
            public boolean moveToLast() {
                return false;
            }

            @Override
            public boolean moveToNext() {
                return false;
            }

            @Override
            public boolean moveToPrevious() {
                return false;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean isBeforeFirst() {
                return false;
            }

            @Override
            public boolean isAfterLast() {
                return false;
            }

            @Override
            public int getColumnIndex(String columnName) {
                return 0;
            }

            @Override
            public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
                return 0;
            }

            @Override
            public String getColumnName(int columnIndex) {
                return null;
            }

            @Override
            public String[] getColumnNames() {
                return new String[0];
            }

            @Override
            public int getColumnCount() {
                return 0;
            }

            @Override
            public byte[] getBlob(int columnIndex) {
                return new byte[0];
            }

            @Override
            public String getString(int columnIndex) {
                return null;
            }

            @Override
            public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {

            }

            @Override
            public short getShort(int columnIndex) {
                return 0;
            }

            @Override
            public int getInt(int columnIndex) {
                return 0;
            }

            @Override
            public long getLong(int columnIndex) {
                return 0;
            }

            @Override
            public float getFloat(int columnIndex) {
                return 0;
            }

            @Override
            public double getDouble(int columnIndex) {
                return 0;
            }

            @Override
            public int getType(int columnIndex) {
                return 0;
            }

            @Override
            public boolean isNull(int columnIndex) {
                return false;
            }

            @Override
            public void deactivate() {

            }

            @Override
            public boolean requery() {
                return false;
            }

            @Override
            public void close() {

            }

            @Override
            public boolean isClosed() {
                return false;
            }

            @Override
            public void registerContentObserver(ContentObserver observer) {

            }

            @Override
            public void unregisterContentObserver(ContentObserver observer) {

            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void setNotificationUri(ContentResolver cr, Uri uri) {

            }

            @Override
            public Uri getNotificationUri() {
                return null;
            }

            @Override
            public boolean getWantsAllOnMoveCalls() {
                return false;
            }

            @Override
            public void setExtras(Bundle extras) {

            }

            @Override
            public Bundle getExtras() {
                return null;
            }

            @Override
            public Bundle respond(Bundle extras) {
                return null;
            }
        };
        try {
            c = dbHandler.getWritableDatabase().rawQuery(
                    "SELECT * FROM "+dbHandler.TABLE_NAME_VOCABULARY+" WHERE "+dbHandler.COLUMN_LE_MOT+"=\""+
                            nouveau_mot_para+"\"",null);

        }catch (Exception e){
            e.getMessage();
        }

        if (nouveau_mot.getText().toString().equals("")){
            //kiểm tra xem đã nhập từ hay chưa
            Toast.makeText(this,"Chưa điền từ mới",Toast.LENGTH_SHORT).show();
        }else if (c.getCount()>0){
            Toast.makeText(this,"Từ đã tồn tại",Toast.LENGTH_SHORT).show();
        }else if (haveSpace(nouveau_mot_para)){
            Toast.makeText(getApplicationContext(),"Không được để dấu cách!",Toast.LENGTH_SHORT).show();
        }else {


            try {
                //kiểm tra xem đã tạo bảng meaning hay chưa
                c = dbHandler.getWritableDatabase().rawQuery("SELECT * FROM "+nouveau_mot_para+" WHERE 1",null);
            }catch (Exception e){
                //tạo bảng meaning hoặc conjugation khi chưa có
                dbHandler.createAnotherTable(nouveau_mot_para,type_word_para);
            }

            if (spinner_lv2_check){
                dbHandler.addWord(new NouveauMot(nouveau_mot_para,type_word_para,lv2_para));
            }else {
                dbHandler.addWord(new NouveauMot(nouveau_mot_para,type_word_para,""));
            }
            if (new_word_para.length()>0 && tu_moi_para.length()>0){
                dbHandler.addMeaning(new NormalWordMeaning(nouveau_mot_para,new_word_para,tu_moi_para));
            }


            if (1==0/*spinner_type_word.getSelectedItem().toString().equals("le verbe")*/){
                intent = new Intent(this,ConjugationInput.class);
                intent.putExtra("leMot",nouveau_mot_para);
                intent.putExtra("enAnglais",new_word_para);
                intent.putExtra("enVietnamien",tu_moi_para);
                startActivity(intent);
                Toast.makeText(this,"Chia động từ",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,R.string.add_complete,Toast.LENGTH_SHORT).show();
            }
            nouveau_mot.setText("");
            new_word.setText("");
            tu_moi.setText("");
        }

    }
    //copy txt to clipboard to hear sound
    public void soundEnglishWordClicked(View view){
        String txt = new_word.getText().toString();

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(txt);
    }
    public void soundFranceWordClicked(View view){
        String text = nouveau_mot.getText().toString();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setText(text);
    }
    public void unAutreClicked(View view){
        if (nouveau_mot.getText().toString().equals("")){
            Toast.makeText(this,"Chưa điền từ mới!",Toast.LENGTH_SHORT).show();
        }else if (new_word.getText().toString().equals("")|| tu_moi.getText().toString().equals("")){
            Toast.makeText(this,"Chưa nhập đủ nghĩa!",Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                Cursor c = dbHandler.getWritableDatabase().rawQuery("SELECT * FROM "+nouveau_mot.getText().toString()+" WHERE 1",null);

            }catch (Exception e){
                dbHandler.createAnotherTable(nouveau_mot.getText().toString(),null);
                e.getMessage();
            }
            dbHandler.addMeaning(new NormalWordMeaning(nouveau_mot.getText().toString(),
                    new_word.getText().toString(),tu_moi.getText().toString()));
            new_word.setText("");
            tu_moi.setText("");
        }
    }


    private void setSpinner_type_lv2(String name){
        switch (name){
            case "le nom":
                adapter = new ArrayAdapter<String>(
                        this,R.layout.spinner_add,R.id.text_view_spinner_item,type_sex
                );
                spinner_type_lv2.setVisibility(Spinner.VISIBLE);
                spinner_lv2_check = true;
                spinner_type_lv2.setAdapter(adapter);
                break;
            case "le verbe":
                adapter = new ArrayAdapter<String>(
                    this,R.layout.spinner_add,R.id.text_view_spinner_item,type_verb
                );
                spinner_type_lv2.setVisibility(Spinner.VISIBLE);
                spinner_lv2_check=true;
                spinner_type_lv2.setAdapter(adapter);
                break;
            case "le adj":
                adapter = new ArrayAdapter<String>(
                    this,R.layout.spinner_add,R.id.text_view_spinner_item,type_sex
                );
                spinner_type_lv2.setVisibility(Spinner.VISIBLE);
                spinner_lv2_check=true;
                spinner_type_lv2.setAdapter(adapter);
                break;
            default:
                spinner_lv2_check=false;
                spinner_type_lv2.setVisibility(Spinner.INVISIBLE);
        }
    }

    public void setUpPrimary(){
        type_word = new String[]{"le nom","le pronom","le verbe","le adj","le article","le conjonction"};
        type_verb = new String[]{"Group 1","Group 2","Group 3"};
        type_sex =  new String[]{"male","female","tous"};

        toolbar_add_word = (Toolbar)findViewById(R.id.toolbar_add_word);
        setSupportActionBar(toolbar_add_word);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar_add_word.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        backToHome();
                    }
                }
        );

        tu_moi = (EditText) findViewById(R.id.edit_text_en_vietnamien_add);
        new_word = (EditText) findViewById(R.id.edit_text_en_anglais_add);
        nouveau_mot = (EditText) findViewById(R.id.edit_text_add_new_add);
        spinner_type_word = (Spinner) findViewById(R.id.spinner_type_word_add);
        spinner_type_lv2 = (Spinner) findViewById(R.id.spinner_type_word_lv2_add);


        // set up spinner type word
        adapter = new ArrayAdapter<String>(
                this,R.layout.spinner_add,R.id.text_view_spinner_item,type_word
        );
        spinner_type_word.setAdapter(adapter);


        //set up spinner type word 2
        adapter = new ArrayAdapter<String>(
                this,R.layout.spinner_add,R.id.text_view_spinner_item,type_sex
        );
        spinner_type_lv2.setAdapter(adapter);
        spinner_lv2_check = true;
        spinner_type_word.setOnItemSelectedListener(
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

        dbHandler = new DataBaseHandler(this,MainActivity.DATABASE_NAME,null,1);

        setupUI(findViewById(R.id.layoutparent_add_word_add));
    }

    public void backToHome(){
        try {
            String tableDust = nouveau_mot.getText().toString();
            Cursor c = dbHandler.getWritableDatabase().rawQuery("SELECT * FROM "+dbHandler.TABLE_NAME_VOCABULARY+" WHERE "+
                    dbHandler.COLUMN_LE_MOT+"=\""+tableDust+"\"",null);
            if (c.getCount()==0){
                dbHandler.deleteTable(tableDust);
                if (!tableDust.equals("")){
                    Toast.makeText(getApplicationContext(),"Chưa lưu từ "+tableDust,Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            e.getMessage();
        }
        intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.homeActivity)
            finish();
        return super.onOptionsItemSelected(item);
    }*/



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
                    hideSoftKeyboard(AddWordActivity.this);
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
