package com.example.projekt;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;

public class DetailActivity extends AppCompatActivity {

    ArrayList<TextView> mealDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final NutriDatabaseHelper db = new NutriDatabaseHelper(this);

        mealDetails.add((TextView)findViewById(R.id.mealDetailName));
        mealDetails.add((TextView)findViewById(R.id.mealDetailKcal));
        mealDetails.add((TextView)findViewById(R.id.mealDetailFat));
        mealDetails.add((TextView)findViewById(R.id.mealDetailCarbohydrates));
        mealDetails.add((TextView)findViewById(R.id.mealDetailProtein));
        mealDetails.add((TextView)findViewById(R.id.mealDetailContext));

        if(getIntent() != null) {
            int id = (int) getIntent().getLongExtra("id", 0);

            String currentItemName = db.meals[id];
            System.out.println("Id posiłku :"+id);
            try{
                Cursor cursor = db.getReadableDatabase().query("NUTRI",
                        new String[]{"Name","Kcal","Fat","Carbohydrates","Protein","Description"},
                        "Name = ?",
                        new String[]{currentItemName},
                        null,null,null);
                cursor.moveToFirst();
                mealDetails.get(0).setText(cursor.getString(0));
                for(int i=1;i<cursor.getColumnCount();i++){
                    System.out.println(cursor.getInt(i));
                    mealDetails.get(i).setText(cursor.getString(i));
                }
            }catch(SQLiteException exp){
                System.out.println(exp.getMessage());
                Toast.makeText(this,"Problem z pobraniem danych z bazy",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            //TODO:: Zrób coś z tym że intent nie działa
        }


    }
}
