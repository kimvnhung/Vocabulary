package com.kimvan.hung.vocabulary.testWord.learningGraph;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

public class MonthPagerFragment extends Fragment {
    LineChart lineChart;

    Spinner spinnerYear;
    ArrayAdapter<String> adapterYear;
    String[] listYear ;

    DataBaseHandler dbHandler;
    Game1Handler game1Handler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_monthpagergraph,container,false);



        dbHandler = new DataBaseHandler(getContext(), MainActivity.DATABASE_NAME,null,1);
        game1Handler = new Game1Handler(dbHandler);
        lineChart = (LineChart)layout.findViewById(R.id.line_chart_on_month);

        setUpSpinner(layout);



        return layout;
    }

    private void setUpSpinner(View layout) {
        spinnerYear = (Spinner)layout.findViewById(R.id.spinner_in_month_pager_graph);
        setListYear();
        adapterYear = new ArrayAdapter<String>(
                getContext(),R.layout.spinner_add,R.id.text_view_spinner_item,listYear
        );
        spinnerYear.setAdapter(adapterYear);
        int lastPositionYear = new Date().getYear()-118;
        spinnerYear.setSelection(lastPositionYear);
        spinnerYear.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        getLineChart(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
    }

    private void setListYear() {
        listYear = new String[12];
        for (int i=0;i<12;i++){
            listYear[i] = "Năm "+(2018+i);
        }
    }

    private void getLineChart(int position) {

        try {
            ArrayList<String> xAxis = new ArrayList<>();
            ArrayList<Entry> yAxis1 = new ArrayList<>();
            ArrayList<Entry> yAxis2 = new ArrayList<>();

            int x = 0;
            //tìm giá trị khác 0 đầu tiên
            int[] listValue1 = game1Handler.getValuesOfMonths(position+118)[0];
            int[] listValue2 = game1Handler.getValuesOfMonths(position+118)[1];
            int numberPoint = listValue1.length;
            int firstValue1InYear = 0;
            int firstValue2InYear = 0;
            int index=0;
            while (listValue1[index]==0){
                index++;
            }
            firstValue1InYear = listValue1[index];

            index=0;
            while (listValue2[index]==0){
                index++;
            }
            firstValue2InYear = listValue2[index];


            for (int i=0;i<numberPoint;i++){
                float function1 = (float) ((int)(listValue1[i]*100/firstValue1InYear+0.5));
                float function2 = (float) ((int)(listValue2[i]*100/firstValue2InYear+0.5));
                x++;
                yAxis1.add(new Entry(i+1,function1));
                yAxis2.add(new Entry(i+1,function2));
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
            lineChart.setVisibleXRangeMaximum(12f);

            lineChart.getAxisRight().setValueFormatter(new MyYAxisValueFormatter());
            lineChart.getAxisLeft().setValueFormatter(new MyYAxisValueFormatter());

            lineChart.getXAxis().setValueFormatter(new MyXAxisValueFormatterForMonth(xaxis));

            lineChart.invalidate();

        }catch (Exception e){
            e.getMessage();
            //Toast.makeText(getContext(),"Chưa có thông tin để hiển thị",Toast.LENGTH_SHORT).show();
        }
    }
}
