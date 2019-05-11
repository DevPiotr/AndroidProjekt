package com.example.projekt;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView simpleListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NutriDatabaseHelper db = new NutriDatabaseHelper(this);
        simpleListView = findViewById(R.id.simpleListView);

        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();

        for (int i=0; i<db.howManyMeals();i++){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("name",db.meals[i]);

            arrayList.add(hashMap);
        }

        //Robimy adapter
        String[] from = {"name"};
        int[] to = {R.id.textView};
        SimpleAdapter simpleAdapter =
                new SimpleAdapter(this,arrayList,R.layout.list_view_items,from,to);
        simpleListView.setAdapter(simpleAdapter);


    }
}
