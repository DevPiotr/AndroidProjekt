package com.example.projekt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class DietDetailActivity extends AppCompatActivity {

    ListView mealListView;
    SimpleAdapter simpleAdapter;

    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    ArrayList<TextView> nutriValuesViews = new ArrayList<>();

    NutriDatabaseHelper db = new NutriDatabaseHelper(MainActivity.mContext);

    TextView mBeginDate,mEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_detail);

        mBeginDate = findViewById(R.id.dietDetailBeginDateText);
        mEndDate = findViewById(R.id.dietDetailEndDateText);
        nutriValuesViews.add((TextView)findViewById(R.id.dietDetailKcal));
        nutriValuesViews.add((TextView)findViewById(R.id.dietDetailFat));
        nutriValuesViews.add((TextView)findViewById(R.id.dietDetailCarbohydrates));
        nutriValuesViews.add((TextView)findViewById(R.id.dietDetailProtein));

        String dietName = getIntent().getStringExtra("dietName");
        ((TextView)findViewById(R.id.newDietName)).setText(dietName);


        //region populate mealList
        mealListView = findViewById(R.id.dietDetailListView);
        ArrayList<String> dietMeals = db.getDietMeals(dietName);
        for (int i=0; i<dietMeals.size();i++){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("name",dietMeals.get(i));
            arrayList.add(hashMap);
        }
        String[] from = {"name"};
        int[] to = {R.id.usernameList};
        simpleAdapter =
                new SimpleAdapter(this,arrayList,R.layout.list_view_items,from,to);
        mealListView.setAdapter(simpleAdapter);

        mealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DietDetailActivity.this,DetailActivity.class);
                intent.putExtra("mealName",arrayList.get((int)id).get("name"));
                startActivity(intent);
            }
        });
        //endregion

        ArrayList<String> nutriValues = db.getDietNutriValuesByName(dietName);

        for(int i=0; i<nutriValuesViews.size();i++){
            nutriValuesViews.get(i).setText(nutriValues.get(i));
        }

        int[] dates = db.getDietDatesByName(dietName);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        mBeginDate.setText(simpleDateFormat.format(new Date(dates[0])));
        mEndDate.setText(simpleDateFormat.format(new Date(dates[1])));

    }
}

