package com.example.projekt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.UUID;

public class StatsActivity extends AppCompatActivity {

    NutriDatabaseHelper db = new NutriDatabaseHelper(MainActivity.mContext);

    private PieChart pieChart;
    private PieDataSet dataSet;
    private ArrayList<PieEntry> entries = new ArrayList<>();
    private int userId;
    int[] newSetValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pieChart = new PieChart(getApplicationContext());
        userId = getIntent().getIntExtra("userId",1);

        int[] values = db.getFullUserDietValuesById(userId);
        if(values == null){
            Toast.makeText(StatsActivity.this,"Brak danych",Toast.LENGTH_LONG).show();
            finish();
        }else {

            pieChart.getLegend().setEnabled(false);
            pieChart.setCenterTextSize(20);

            Description description = pieChart.getDescription();

            description.setText("Średnie wartości odżywcze z wszystkich diet");
            description.setTextSize(17);

            pieChart.setDescription(description);

            pieChart.animateXY(800, 800);

            setPieChartData(values);

            setContentView(pieChart);
        }
    }

    private void setPieChartData(int[] values) {
        entries.clear();
        entries.add(new PieEntry(values[1],"Tłuszcze"));
        entries.add(new PieEntry(values[2],"Węglowodany"));
        entries.add(new PieEntry(values[3],"Białko"));

        dataSet = new PieDataSet(entries,"Wartości odżywcze");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(20);

        PieData data = new PieData(dataSet);

        if(pieChart.getData() != null)
            pieChart.clearValues();
        pieChart.setData(data);
        pieChart.setCenterText("Kcal \n" + values[0]);
        pieChart.animateXY(800,800);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.piechart_menu,menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch(item.getItemId()){
            case R.id.pieAvg:
                recreate();
                return true;
            case R.id.pieRange:
                final EditText taskEditText = new EditText(StatsActivity.this);
                AlertDialog dialog = new AlertDialog.Builder(StatsActivity.this)
                        .setTitle("Zakres diet")
                        .setMessage("Ilośc ostatnich diet do zliczenia")
                        .setView(taskEditText)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int count = Integer.valueOf(taskEditText.getText().toString());
                                if(count <= db.howMuchDietUserHas(userId)) {
                                    setValuesTable(count);
                                    setPieChartData(newSetValue);
                                    pieChart.getDescription().setTextSize(20);
                                    pieChart.getDescription().setText("Wartości odżywcze z " + count + " ostatnich diet");
                                }else
                                    Toast.makeText(StatsActivity.this,"Nie posiadasz tylu diet",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();

                return true;
            case R.id.pieSavePhoto:
                if(pieChart.saveToGallery(UUID.randomUUID().toString()))
                    Toast.makeText(StatsActivity.this,"Zdjęcie zapisano poprawnie",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(StatsActivity.this,"Błąd podczas zapisywania zdjęcia",Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setValuesTable(int count) {
        newSetValue = db.getSpecificDietNutriValuesCount(userId,count);
    }


}
