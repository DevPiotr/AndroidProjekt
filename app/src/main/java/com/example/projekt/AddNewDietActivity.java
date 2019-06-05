package com.example.projekt;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class AddNewDietActivity extends AppCompatActivity {

    EditText newDietName;

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

        newDietName = findViewById(R.id.newDietName);
        newDietAddButton = findViewById(R.id.newDietAddButton);

        //ArrayList NutriValues Init
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

                String clickedMealName = mealNames.get((int)id);
                ArrayList<String> tmpValues = db.getNutriValuesByName(clickedMealName);

                if(!selectedMeals.contains(clickedMealName)){

                    for (int i = 0; i < nutriValues.size(); i++) {
                        int tmp = Integer.parseInt(nutriValues.get(i).getText().toString());
                        tmp += Integer.parseInt(tmpValues.get(i));
                        nutriValues.get(i).setText(String.valueOf(tmp));
                    }
                    selectedMeals.add(clickedMealName);
                    view.setBackgroundColor(Color.parseColor("#E57373"));
                }
                else{
                    for (int i = 0; i < nutriValues.size(); i++) {
                        int tmp = Integer.parseInt(nutriValues.get(i).getText().toString());
                        tmp -= Integer.parseInt(tmpValues.get(i));
                        nutriValues.get(i).setText(String.valueOf(tmp));
                    }
                    selectedMeals.remove(clickedMealName);
                    view.setBackgroundColor(Color.WHITE);
                }

            }
        });

        newDietAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent();
                ArrayList<String>  dietNames = db.getDietNames();
                ContentValues contentValues =  new ContentValues();
                boolean dietExist = false;
                String tmpMealNames = "";

                for(String diet: dietNames){
                    if(diet.toLowerCase().equals(newDietName.getText().toString().toLowerCase())){
                        dietExist = true;
                        break;
                    }
                }
                contentValues.clear();
                if(!dietExist) {
                    contentValues.put("Name", newDietName.getText().toString());

                    for(String meals: selectedMeals){
                        tmpMealNames += meals +";";
                    }

                    tmpMealNames = tmpMealNames.substring(0,tmpMealNames.length()-1);

                    System.out.println(tmpMealNames);

                    contentValues.put("Meals",tmpMealNames);
                    backIntent.putExtra("contentValues", contentValues);
                    setResult(RESULT_OK, backIntent);
                    finish();
                }
                else {
                    Toast.makeText(AddNewDietActivity.this,"Już istnieje taka nazwa diety",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
