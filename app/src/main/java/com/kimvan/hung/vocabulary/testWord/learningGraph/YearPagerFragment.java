package com.kimvan.hung.vocabulary.testWord.learningGraph;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.kimvan.hung.vocabulary.MainActivity;
import com.kimvan.hung.vocabulary.R;
import com.kimvan.hung.vocabulary.dataBase.DataBaseHandler;
import com.kimvan.hung.vocabulary.testWord.fourAnswerGame.Game1Handler;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by h on 22/02/2018.
 */

public class YearPagerFragment extends Fragment {
    LineChart lineChart;

    TextView testText;
    DataBaseHandler dbHandler;
    Game1Handler game1Handler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_yearpagergraph,container,false);
        testText = (TextView)layout.findViewById(R.id.title_in_graph_year_pager_graph);
        testText.setText("Thế Kỉ "+((new Date().getYear()+1900)/100));

        dbHandler = new DataBaseHandler(getContext(), MainActivity.DATABASE_NAME,null,1);
        game1Handler = new Game1Handler(dbHandler);
        setupLineChart(layout);

        return layout;
    }

    private void setupLineChart(View layout) {
        lineChart = (LineChart)layout.findViewById(R.id.line_chart_on_year);

        try {
            ArrayList<String> xAxis = new ArrayList<>();
            ArrayList<Entry> yAxis1 = new ArrayList<>();
            ArrayList<Entry> yAxis2 = new ArrayList<>();

            int x = 0;
            int size = game1Handler.getListYearSize();
            //tìm giá trị khác 0 đầu tiên
            int[] listValueNumQuestion = new int[size<12?12:size];
            int[] listValueBestInSth = new int[size<12?12:size];
            for (int i=0;i<listValueNumQuestion.length;i++){
                if (i<size){
                    listValueNumQuestion[i]=game1Handler.getValuesOfYears()[0][i];
                    listValueBestInSth[i]=game1Handler.getValuesOfYears()[1][i];
                }else {
                    listValueNumQuestion[i]=0;
                    listValueBestInSth[i]=0;
                }
            }
            int numberPoint = listValueNumQuestion.length;

            int firstValueNumQuestionInYears = 0;
            int firstValueBestInSthInYears = 0;

            int index=0;
            while (listValueNumQuestion[index]==0){
                index++;
            }
            firstValueNumQuestionInYears = listValueNumQuestion[index];

            index=0;
            while (listValueBestInSth[index]==0){
                index++;
            }
            firstValueBestInSthInYears = listValueBestInSth[index];


            for (int i=0;i<numberPoint;i++){
                float function1 = (float) ((int)(listValueNumQuestion[i]*100/firstValueNumQuestionInYears+0.5));
                float function2 = (float) ((int)(listValueBestInSth[i]*100/firstValueBestInSthInYears+0.5));
                x++;
                yAxis1.add(new Entry(i,function1));
                yAxis2.add(new Entry(i,function2));
                xAxis.add(i,String.valueOf(x));
            }

            String[] xaxis = new String[xAxis.size()];
            for (int i=0;i<xAxis.size();i++){
                xaxis[i]=xAxis.get(i);
            }

            ArrayList<ILineDataSet> lineDataSet = new ArrayList<>();

            LineDataSet lineDataSet1 = new LineDataSet(yAxis1,"Câu hỏi đã chơi!");
            lineDataSet1.setDrawCircles(false);
            lineDataSet1.setColor(Color.RED);
            lineDataSet1.setDrawValues(false);

            LineDataSet lineDataSet2 = new LineDataSet(yAxis2,"Best Score");
            lineDataSet2.setDrawCircles(false);
            lineDataSet2.setColor(Color.BLUE);
            lineDataSet2.setDrawValues(false);

            lineDataSet.add(lineDataSet1);
            lineDataSet.add(lineDataSet2);

            lineChart.setData(new LineData(lineDataSet));
            lineChart.setVisibleXRangeMaximum(size<12?12f:(float) size);

            lineChart.getAxisRight().setValueFormatter(new MyYAxisValueFormatter());
            lineChart.getAxisLeft().setValueFormatter(new MyYAxisValueFormatter());

            lineChart.getXAxis().setValueFormatter(new MyXAxisValueFormatterForYear(xaxis));
        }catch (Exception e){
            e.getMessage();
            //Toast.makeText(getContext(),"Chưa có thông tin để hiển thị",Toast.LENGTH_SHORT).show();
        }
    }
}
