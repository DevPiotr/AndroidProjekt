package com.example.projekt;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView simpleListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final NutriDatabaseHelper db = new NutriDatabaseHelper(this);
        simpleListView = findViewById(R.id.simpleListView);

        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();

        for (int i=0; i<db.meals.length;i++){
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

        simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
    }

}
