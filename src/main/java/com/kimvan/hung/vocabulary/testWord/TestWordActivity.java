package com.kimvan.hung.vocabulary.testWord;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.kimvan.hung.vocabulary.Information.RecyclerTouchListener;
import com.kimvan.hung.vocabulary.MainActivity;
import com.kimvan.hung.vocabulary.R;
import com.kimvan.hung.vocabulary.dataBase.DataBaseHandler;
import com.kimvan.hung.vocabulary.testWord.fourAnswerGame.FourAnswerGameActivity;

import java.util.ArrayList;
import java.util.List;

public class TestWordActivity extends AppCompatActivity {

    private List<GameProperties> gameList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ListGameAdapter listGameAdapter;

    Intent intent;
    DataBaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_word);

        dbHandler = new DataBaseHandler(this, MainActivity.DATABASE_NAME,null,1);

        setUpRecyclerView();
    }

    private void setUpRecyclerView(){
        recyclerView = (RecyclerView)findViewById(R.id.test_word_recycler_view);

        listGameAdapter =  new ListGameAdapter(gameList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.HORIZONTAL));
        recyclerView.setAdapter(listGameAdapter);
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(getApplicationContext(), recyclerView,
                        new RecyclerTouchListener.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                switch (position){
                                    case 0:
                                        try {
                                            Cursor c = dbHandler.getWritableDatabase().rawQuery("SELECT * FROM "+dbHandler.TABLE_NAME_VOCABULARY+
                                                    " WHERE 1",null);
                                            if (c.getCount()<4){
                                                Toast.makeText(getApplicationContext(),"Dữ liệu phải nhiều hơn 4 từ!!",Toast.LENGTH_SHORT).show();
                                            }else {
                                                intent = new Intent(TestWordActivity.this,FourAnswerGameActivity.class);
                                                startActivity(intent);
                                            }
                                            c.close();
                                        }catch (Exception e){
                                            e.getMessage();
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }

                            @Override
                            public void onLongClick(View view, int position) {

                            }
                        })
        );

        prepareListGame();
    }

    private void prepareListGame(){
        GameProperties game = new GameProperties("Trắc Nghiệm","Chọn đáp án đúng");
        gameList.add(game);

        game = new GameProperties("example1","somthing");
        gameList.add(game);

        game = new GameProperties("Biểu Đồ Học Tập","xem điểm qua các ngày");
        gameList.add(game);

        listGameAdapter.notifyDataSetChanged();
    }
}
