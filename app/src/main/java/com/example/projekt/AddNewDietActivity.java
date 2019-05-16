package com.example.projekt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class AddNewDietActivity extends AppCompatActivity {

    EditText newDietNewDiet;

    ArrayList<TextView> nutriValues = new ArrayList<>();
    ArrayList<String> mealNames;
    ArrayList<String> selectedMeals = new ArrayList<>();

    ListView listView;
    SimpleAdapter simpleAdapter;

    Button newDietAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_diet);

        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();

        final NutriDatabaseHelper db = new NutriDatabaseHelper(this);

        //HashMap NutriValues Init
        nutriValues.add((TextView)findViewById(R.id.newDietKcal));
        nutriValues.add((TextView)findViewById(R.id.newDietFat));
        nutriValues.add((TextView)findViewById(R.id.newDietCarbohydrates));
        nutriValues.add((TextView)findViewById(R.id.newDietProtein));

        //ListView configuration
        listView = findViewById(R.id.newDietListView);

        mealNames = db.getMealNames();
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

                /*
                String mealName = mealNames.get((int)id);
                if(selectedMeals.contains(mealName)){

                    ArrayList<String> tmpValues = db.getNutriValuesByName(mealName);

                    for (int i = 0; i < nutriValues.size(); i++) {
                        nutriValues.get(i).setText(tmpValues.get(i));
                    }
                }
                */
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
