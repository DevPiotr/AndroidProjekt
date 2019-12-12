package com.example.projekt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {

    NutriDatabaseHelper db = new NutriDatabaseHelper(MainActivity.mContext);

    private PieChart pieChart;
    private PieDataSet dataSet;
    private ArrayList<PieEntry> entries = new ArrayList<>();
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pieChart = new PieChart(getApplicationContext());
        userId = getIntent().getIntExtra("userId",1);

        entries = db.getFullUserDietValuesById(userId);
        if(entries == null){
            Toast.makeText(StatsActivity.this,"Brak danych",Toast.LENGTH_LONG).show();
            finish();
        }

        dataSet = new PieDataSet(entries,"Wartości odżywcze");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(20);
        dataSet.

        PieData data = new PieData(dataSet);

        pieChart.setData(data);

        pieChart.getLegend().setEnabled(false);
        pieChart.setCenterText("Wartości odżywcze");

        setContentView(pieChart);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.piechart_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.pieAvg:

                return true;
            case R.id.pieRange:

                return true;
            case R.id.pieSavePhoto:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
