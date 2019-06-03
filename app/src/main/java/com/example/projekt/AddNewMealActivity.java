package com.example.projekt;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddNewMealActivity extends AppCompatActivity {

    ArrayList<EditText> newMealDetails = new ArrayList<>();

    Button addMealAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_meal);
        final NutriDatabaseHelper db = new NutriDatabaseHelper(this);
        addMealAddButton = findViewById(R.id.addMealAddButton);

        newMealDetails.add((EditText)findViewById(R.id.addMealName));
        newMealDetails.add((EditText)findViewById(R.id.addMealKcal));
        newMealDetails.add((EditText)findViewById(R.id.addMealFat));
        newMealDetails.add((EditText)findViewById(R.id.addMealCarbohydrates));
        newMealDetails.add((EditText)findViewById(R.id.addMealProtein));
        newMealDetails.add((EditText)findViewById(R.id.addMealContext));
        newMealDetails.add((EditText)findViewById(R.id.addMealIngredients));
        newMealDetails.add((EditText)findViewById(R.id.addMealPreparation));

        addMealAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent();
                ArrayList<String>  mealNames = db.getMealNames();
                ContentValues contentValues =  new ContentValues();
                Boolean mealExist = false;

                for(String meal: mealNames){
                    if(meal.toLowerCase().equals(newMealDetails.get(0).getText().toString().toLowerCase())){
                        mealExist = true;
                        break;
                    }
                }
                contentValues.clear();
                if(!mealExist) {
                    contentValues.put("Name", newMealDetails.get(0).getText().toString());
                    contentValues.put("Kcal", newMealDetails.get(1).getText().toString());
                    contentValues.put("Fat", newMealDetails.get(2).getText().toString());
                    contentValues.put("Carbohydrates", newMealDetails.get(3).getText().toString());
                    contentValues.put("Protein", newMealDetails.get(4).getText().toString());
                    contentValues.put("Description", newMealDetails.get(5).getText().toString());
                    contentValues.put("Ingredients", newMealDetails.get(6).getText().toString());
                    contentValues.put("Preparation", newMealDetails.get(7).getText().toString());

                    backIntent.putExtra("contentValues", contentValues);
                    setResult(RESULT_OK, backIntent);
                    finish();
                }
                else {
                    Toast.makeText(AddNewMealActivity.this,"Już istnieje taka nazwa posiłku",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
