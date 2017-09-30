package com.kimvan.hung.vocabulary;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kimvan.hung.vocabulary.Information.InformationActivity;
import com.kimvan.hung.vocabulary.addWord.AddWordActivity;
import com.kimvan.hung.vocabulary.editWord.EditWordActivity;
import com.kimvan.hung.vocabulary.testWord.TestWordActivity;

public class MainActivity extends AppCompatActivity {

    public static String DATABASE_NAME = "Vocabularys.db";
    Button button_add_main;
    Button information_main;
    Button test_word_main;
    Button quite_main;

    Drawable para_button;

    TextView dbModeTxt ;

    RelativeLayout homeActivity;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setWidgetId();

    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int positonItem =-1;
        try {
            positonItem = item.getItemId();
        }catch (Exception e){
            return super.onContextItemSelected(item);
        }

        switch (positonItem){
            case 0:
                MainActivity.DATABASE_NAME="Vocabularys.db";
                break;
            default:
                MainActivity.DATABASE_NAME="Demo.db";
                break;

        }
        return super.onContextItemSelected(item);
    }

    public void addMainClicked(View view){
        button_add_main.setBackgroundColor(getResources().getColor(R.color.colorClicked));
        intent = new Intent(this, AddWordActivity.class);
        startActivity(intent);
    }

    public void testMainClicked(View view){
        test_word_main.setBackgroundColor(getResources().getColor(R.color.colorClicked));
        intent = new Intent(this, TestWordActivity.class);
        startActivity(intent);
    }

    public void informationClicked(View view){
        information_main.setBackgroundColor(getResources().getColor(R.color.colorClicked));
        intent = new Intent(this,InformationActivity.class);
        startActivity(intent);
    }


    public void quitterClicked(View view){
        quite_main.setBackgroundColor(getResources().getColor(R.color.colorClicked));
        finish();
        System.exit(0);
    }


    //
    public void setWidgetId(){


        dbModeTxt = (TextView)findViewById(R.id.database_mode_txt);
        homeActivity = (RelativeLayout)findViewById(R.id.homeActivity);
        test_word_main = (Button)findViewById(R.id.button_test_main);
        quite_main = (Button)findViewById(R.id.button_quite_main);
        information_main = (Button)findViewById(R.id.button_information_main);
        button_add_main = (Button) findViewById(R.id.button_add_main);

        para_button = button_add_main.getBackground();
        dbModeTxt.setText(MainActivity.DATABASE_NAME);

        homeActivity.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (dbModeTxt.getText().toString().equals("Vocabularys.db")){
                            dbModeTxt.setText("Demo.db");
                        }else {
                            dbModeTxt.setText("Vocabularys.db");
                        }
                        MainActivity.DATABASE_NAME = dbModeTxt.getText().toString();
                        Toast.makeText(getApplicationContext(),"Mode db has changed!",Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
        );
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        button_add_main.setBackgroundColor(getResources().getColor(R.color.colorBackgroundButton));
        test_word_main.setBackgroundColor(getResources().getColor(R.color.colorBackgroundButton));
        information_main.setBackgroundColor(getResources().getColor(R.color.colorBackgroundButton));
        quite_main.setBackgroundColor(getResources().getColor(R.color.colorBackgroundButton));
    }
}
