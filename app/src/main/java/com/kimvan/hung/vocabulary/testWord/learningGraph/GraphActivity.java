package com.kimvan.hung.vocabulary.testWord.learningGraph;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.kimvan.hung.vocabulary.MainActivity;
import com.kimvan.hung.vocabulary.R;
import com.kimvan.hung.vocabulary.dataBase.DataBaseHandler;

public class GraphActivity extends AppCompatActivity {

    ViewPager viewPager;
    DataBaseHandler dataBaseHandler ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        setupId();
    }

    //set up id
    private void setupId() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new StaePagerGraphAdapter(getSupportFragmentManager()));
    }
}
