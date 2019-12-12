package com.example.projekt;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AddNewDietActivity extends AppCompatActivity {

    EditText newDietName;
    Date beginDate,endDate;
    TextView mBeginDateText,mEndDateText;
    private DatePickerDialog.OnDateSetListener mBeginListener,mEndListener;

    ArrayList<TextView> nutriValues = new ArrayList<>();
    ArrayList<String> mealNames;
    ArrayList<String> selectedMeals = new ArrayList<>();

    ListView listView;
    SimpleAdapter simpleAdapter;


    Button newDietAddButton, beginDateInput, endDateInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_diet);

        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();

        final NutriDatabaseHelper db = new NutriDatabaseHelper(this);

        newDietName = findViewById(R.id.newDietName);
        newDietAddButton = findViewById(R.id.newDietAddButton);
        beginDateInput = findViewById(R.id.beginDateInput);
        endDateInput = findViewById(R.id.endDateInput);
        mBeginDateText = findViewById(R.id.beginDateText);
        mEndDateText = findViewById(R.id.endDateText);

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

        //region populateList
        String[] from = {"name"};
        int[] to = {R.id.usernameList};
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
//endregion

        //region AddDietFunc
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
                if(mBeginDateText.getText().toString().equals("Data rozpoczęcia") || mEndDateText.getText().toString().equals("Data zakończenia"))
                {
                    Toast.makeText(AddNewDietActivity.this,"Uzupełnij daty",Toast.LENGTH_SHORT).show();
                }
                else if(beginDate.getTime() > endDate.getTime())
                {
                    Toast.makeText(AddNewDietActivity.this,"Data zakończenia powinna być w później niż data rozpoczęcia",Toast.LENGTH_LONG).show();
                }
                else if(newDietName.getText().toString().equals("")){
                    Toast.makeText(AddNewDietActivity.this,"Podaj nazwę diety",Toast.LENGTH_SHORT).show();
                }
                else if(dietExist) {
                    Toast.makeText(AddNewDietActivity.this,"Już istnieje taka nazwa diety",Toast.LENGTH_SHORT).show();
                }
                else {

                    contentValues.put("Name", newDietName.getText().toString());

                    for(String meals: selectedMeals){
                        tmpMealNames += meals +";";
                    }

                    tmpMealNames = tmpMealNames.substring(0,tmpMealNames.length()-1);

                    contentValues.put("Meals",tmpMealNames);

                    contentValues.put("Kcal",nutriValues.get(0).getText().toString());
                    contentValues.put("Fat",nutriValues.get(1).getText().toString());
                    contentValues.put("Carbohydrates",nutriValues.get(2).getText().toString());
                    contentValues.put("Protein",nutriValues.get(3).getText().toString());

                    contentValues.put("beginDate",beginDate.getTime());
                    contentValues.put("endDate",endDate.getTime());

                    backIntent.putExtra("contentValues", contentValues);
                    setResult(RESULT_OK, backIntent);
                    finish();
                }
            }
        });
        //endregion



        beginDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBeginDateDialog();
            }
        });

        endDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDateDialog();
            }
        });

        mBeginListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                beginDate = new Date(year,month,dayOfMonth);
                mBeginDateText.setText(date);
            }
        };

        mEndListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                endDate = new Date(year,month,dayOfMonth);
                mEndDateText.setText(date);
            }
        };
    }

    private void showEndDateDialog() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                AddNewDietActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mEndListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void showBeginDateDialog() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                AddNewDietActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mBeginListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }



}
