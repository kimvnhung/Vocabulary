package com.kimvan.hung.vocabulary.Information;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.support.v7.widget.Toolbar;
import android.support.v7.recyclerview.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.app.ActionBar;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kimvan.hung.vocabulary.MainActivity;
import com.kimvan.hung.vocabulary.R;
import com.kimvan.hung.vocabulary.addWord.AddWordActivity;
import com.kimvan.hung.vocabulary.dataBase.DataBaseHandler;
import com.kimvan.hung.vocabulary.dataBase.NouveauMot;
import com.kimvan.hung.vocabulary.editWord.EditWordActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InformationActivity extends AppCompatActivity {

    DataBaseHandler dbHandler;
    Intent intent;
    EditText entrerMot;
    TextView numberListItem;

    List<NouveauMot> nouveauMotList = new ArrayList<>();
    ListView recyclerView;
    WordPropertiesAdapter wordPropertiesAdapter;

    Toolbar toolbar_information;
    RelativeLayout layoutInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        setPrimary();

        setupUI(findViewById(R.id.layout_content_information));
    }

    public void setPrimary(){
        dbHandler = new DataBaseHandler(this,MainActivity.DATABASE_NAME,null,1);

        layoutInformation = (RelativeLayout)findViewById(R.id.layout_content_information);
        layoutInformation.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //chứa các bảng đã chọn
                        final ArrayList<String> listItemsSelected = new ArrayList<>();
                        //chứa tất cả các bảng
                        final String[] listTable = dbHandler.getListTableInDatabase();

                        final AlertDialog.Builder builder = new AlertDialog.Builder(InformationActivity.this);

                        builder.setTitle("Reset all")
                                .setMultiChoiceItems(
                                        listTable, null,
                                        new DialogInterface.OnMultiChoiceClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                                //uncheck tất cả khi check vào delete all
                                                if (which==listTable.length-1 && isChecked==true){
                                                    for (int i=0;i<which;i++){
                                                        ((AlertDialog)dialog).getListView().setItemChecked(i,false);
                                                    }
                                                    listItemsSelected.clear();
                                                    listItemsSelected.add(listTable[which]);
                                                }else {
                                                    //nếu đã check delete all thì bỏ check
                                                    if (listItemsSelected.contains(listTable[listTable.length-1])){
                                                        ((AlertDialog)dialog).getListView().setItemChecked(listTable.length-1,false);
                                                    }
                                                    if (isChecked){
                                                        //thêm vào list item đc check
                                                        listItemsSelected.add(listTable[which]);
                                                    }else if (listItemsSelected.contains(listTable[which])){
                                                        //xóa item khi bị uncheck
                                                        listItemsSelected.remove(listItemsSelected.indexOf(listTable[which]));
                                                    }
                                                }

                                            }
                                        }
                                ).setPositiveButton(
                                " Delete...!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (listItemsSelected.size()==1 && listItemsSelected.get(0)== getString(R.string.delete_all)){
                                            AlertDialog.Builder builder2 = new AlertDialog.Builder(InformationActivity.this);
                                            builder2.setTitle("Delete All!").setMessage("Are you sure?").
                                                    setPositiveButton(
                                                            "Yes!", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dbHandler.resetAllData();
                                                                    dialog.dismiss();
                                                                    Toast.makeText(getApplicationContext(),"Đã xóa tất cả",Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                    ).setNegativeButton(
                                                    "Cancel!", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    }
                                            );
                                            builder2.create();
                                            builder2.show();

                                        }else {
                                            for (int i=0;i<listItemsSelected.size();i++){
                                                dbHandler.dropTable(listItemsSelected.get(i));
                                            }
                                            if (listItemsSelected.contains(dbHandler.TABLE_NAME_VOCABULARY)){
                                                dbHandler.onCreate(dbHandler.getWritableDatabase());
                                            }
                                            Toast.makeText(getApplicationContext(),"Đã xóa các bảng đã chọn",Toast.LENGTH_SHORT).show();
                                        }
                                        dialog.dismiss();

                                    }
                                }
                        ).setNegativeButton(
                                "Cancel ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }
                        );

                        builder.create();
                        builder.show();
                        return true;
                    }
                }
        );

        toolbar_information = (Toolbar)findViewById(R.id.toolbar_information);
        setSupportActionBar(toolbar_information);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar_information.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        backToHome();
                    }
                }
        );

        recyclerView =  findViewById(R.id.information_recycler_view);
        wordPropertiesAdapter = new WordPropertiesAdapter(nouveauMotList,getApplicationContext()) {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                wordPropertiesAdapter.setCurPosition(position);
                return true;
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                numberListItem.setText((position+1)+"/"+nouveauMotList.size());
            }
        };
        /*RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);*/
        recyclerView.setAdapter(wordPropertiesAdapter);
        registerForContextMenu(recyclerView);
        /*recyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        numberListItem.setText((position+1)+"/"+nouveauMotList.size());
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        wordPropertiesAdapter.setCurPosition(position);
                    }
                })
        );*/




        entrerMot = (EditText)findViewById(R.id.mot_pour_trouver_edittxt);
        entrerMot.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                prepareData(s.toString());
                numberListItem.setText("0/"+nouveauMotList.size());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        numberListItem = (TextView)findViewById(R.id.number_list_item_information);
        GradientDrawable gd = (GradientDrawable)numberListItem.getBackground().getCurrent();
        gd.setColor(Color.WHITE);
        prepareData("");
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int positionMot = wordPropertiesAdapter.getCurPosition();
        int positonItem =-1;
        try {
            positonItem = item.getItemId();
        }catch (Exception e){
            return super.onContextItemSelected(item);
        }

        switch (positonItem){
            case 0:
                dbHandler.deleteWord(nouveauMotList.get(positionMot).get_id());

                prepareData(entrerMot.getText().toString());
                break;
            default:
                intent = new Intent(this, EditWordActivity.class);
                intent.putExtra("leMot",nouveauMotList.get(positionMot));
                startActivity(intent);
                break;

        }
        return super.onContextItemSelected(item);
    }

    public void backToHome(){
       finish();

    }

    public void prepareData(String searchWord){
         nouveauMotList.clear();
         ArrayList<NouveauMot> para = dbHandler.searchNouveauMot(searchWord,dbHandler.COLUMN_LE_MOT);
         for (NouveauMot x:para) {
             nouveauMotList.add(x);
         }
         if (nouveauMotList.size()==0){
             Toast.makeText(this,"La liste est vide!",Toast.LENGTH_SHORT).show();
         }
         wordPropertiesAdapter.notifyDataSetChanged();
        numberListItem.setText("0/"+nouveauMotList.size());
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
                    hideSoftKeyboard(InformationActivity.this);
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
