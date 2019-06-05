package com.example.projekt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class DietDetailActivity extends AppCompatActivity {

    ListView mealListView;
    SimpleAdapter simpleAdapter;

    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();

    NutriDatabaseHelper db = new NutriDatabaseHelper(MainActivity.mContext);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        String dietName = getIntent().getStringExtra("dietName");
        ((TextView)findViewById(R.id.textView5)).setText(dietName);
        mealListView = findViewById(R.id.dietListView);

        ArrayList<String> dietMeals = db.getDietMeals(dietName);

        for (int i=0; i<dietMeals.size();i++){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("name",dietMeals.get(i));
            arrayList.add(hashMap);
        }

        String[] from = {"name"};
        int[] to = {R.id.textView};
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

    }
}

