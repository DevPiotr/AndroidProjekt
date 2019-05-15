package com.example.projekt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class AddNewDietActivity extends AppCompatActivity {

    EditText newDietNewDiet;
    ArrayList<String> nutriValues;
    ListView listView;
    SimpleAdapter simpleAdapter;
    Button newDietAddButton;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_diet);

        final NutriDatabaseHelper db = new NutriDatabaseHelper(this);

        //ListView configuration
        listView = findViewById(R.id.newDietListView);

        ArrayList<String> mealNames = db.getMealNames();
        for (int i=0; i<mealNames.size();i++){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("name",mealNames.get(i));
            arrayList.add(hashMap);
        }

        String[] from = {"name"};
        int[] to = {R.id.textView};
        simpleAdapter =
                new SimpleAdapter(this,arrayList,R.layout.list_view_items,from,to);
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
    }
}
