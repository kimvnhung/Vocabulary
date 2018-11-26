package com.kimvan.hung.vocabulary.testWord;

import android.content.Intent;
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
import com.kimvan.hung.vocabulary.testWord.learningGraph.GraphActivity;
import com.kimvan.hung.vocabulary.testWord.orderYourLetter.OrderYourWordActivity;

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
        recyclerView = findViewById(R.id.test_word_recycler_view);

        listGameAdapter =  new ListGameAdapter(gameList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.HORIZONTAL));
        recyclerView.setAdapter(listGameAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(getApplicationContext(), recyclerView,
                        new RecyclerTouchListener.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                switch (position){
                                    case 0:
                                        try {
                                            int c=dbHandler.getTableCount(dbHandler.TABLE_NAME_VOCABULARY);
                                            if (c<=4){
                                                Toast.makeText(getApplicationContext(),"Dữ liệu phải nhiều hơn 4 từ!!",Toast.LENGTH_SHORT).show();
                                            }else {
                                                intent = new Intent(TestWordActivity.this,FourAnswerGameActivity.class);
                                                startActivity(intent);
                                            }
                                        }catch (Exception e){
                                            e.getMessage();
                                        }
                                        break;
                                    case 1:
                                        try {
                                            intent = new Intent(getApplicationContext(), OrderYourWordActivity.class);
                                            startActivity(intent);
                                        }catch (Exception e){
                                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    case 2:

                                        break;
                                    default:
                                        intent = new Intent(getApplicationContext(), GraphActivity.class);
                                        startActivity(intent);
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

        game = new GameProperties("Xếp Chữ","Xếp chữ đúng từ");
        gameList.add(game);

        game = new GameProperties("On Updating...","Đang phát triển...");
        gameList.add(game);

        game = new GameProperties("Biểu Đồ Học Tập","xem điểm qua các ngày");
        gameList.add(game);

        listGameAdapter.notifyDataSetChanged();
    }
}
