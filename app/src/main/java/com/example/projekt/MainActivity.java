package com.example.projekt;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static final int CODE_ADDMEAL = 01;
    public static final int CODE_ADDDIET = 10;
    ListView simpleListView;
    SimpleAdapter simpleAdapter;

    public NutriDatabaseHelper db;

    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mainActivityAddMealButton = findViewById(R.id.mainActivityAddMealButton);
        Button mainActivityNewDietButton = findViewById(R.id.mainActivityNewDietButton);

        db = new NutriDatabaseHelper(this);


        simpleListView = findViewById(R.id.simpleListView);

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
        simpleListView.setAdapter(simpleAdapter);

        simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("mealName",arrayList.get((int)id).get("name"));
                startActivity(intent);
            }
        });

        simpleListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int mealId = (int)id;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Usuwanie posiłku")
                        .setMessage("Czy chcesz usunąć ten posiłek?")
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.getWritableDatabase().delete("NUTRI",
                                                            "name = ?",
                                                                    new String[]{arrayList.get(mealId).get("name")});
                                arrayList.remove(mealId);
                                simpleAdapter.notifyDataSetChanged();
                            }

                        })
                        .setNegativeButton("Nie", null)
                        .show();

                return true;
            }
        });

        //Obsługa przycisku mainActivityAddMeal
        mainActivityAddMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddNewMealActivity.class);
                startActivityForResult(intent,CODE_ADDMEAL);

            }
        });

        //NewDietButton Configuration
        mainActivityNewDietButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddNewDietActivity.class);
                startActivityForResult(intent,CODE_ADDDIET);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case CODE_ADDMEAL: {
                if(resultCode == RESULT_OK) {
                    db.getWritableDatabase().insertWithOnConflict("NUTRI", null, (ContentValues) data.getExtras().get("contentValues"), SQLiteDatabase.CONFLICT_IGNORE);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("name", ((ContentValues) data.getExtras().get("contentValues")).get("Name").toString());
                    arrayList.add(hashMap);

                    simpleAdapter.notifyDataSetChanged();
                }
                break;
            }
            case CODE_ADDDIET:
            {
                //TODO::Dodawanie do bazy diet
            }
            default: {
                break;
            }
        }
    }

}
