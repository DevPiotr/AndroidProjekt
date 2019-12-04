package com.example.projekt;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static final int CODE_ADDMEAL = 1;
    public static final int CODE_ADDDIET = 10;
    ListView simpleListView;
    SimpleAdapter simpleAdapter;

    Button mainActivityDiets;

    public static Context mContext;

    public NutriDatabaseHelper db;

    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //deleteDatabase("NutriValue");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_new_diet );

        mContext = this;

        final Button mainActivityMenuButton = findViewById(R.id.menuButton);

        Button mainActivityAddMealButton = findViewById(R.id.mainActivityAddMealButton);
        Button mainActivityNewDietButton = findViewById(R.id.mainActivityNewDietButton);
        mainActivityDiets = findViewById(R.id.mainActivityDiets);

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

        //Obsługa przycisku mainActivityDiets
        mainActivityDiets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,DietActivity.class);
                startActivity(intent);
            }
        });

        mainActivityMenuButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, mainActivityMenuButton);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        //TODO zarzadzanie wcisnieciami

                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });//closing the setOnClickListener method

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case CODE_ADDMEAL: {
                if(resultCode == RESULT_OK) {
                    db.getWritableDatabase().insert("NUTRI", null,  (ContentValues) data.getExtras().get("contentValues"));

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("name", ((ContentValues) data.getExtras().get("contentValues")).get("Name").toString());
                    arrayList.add(hashMap);

                    simpleAdapter.notifyDataSetChanged();
                }
                break;
            }
            case CODE_ADDDIET:
            {
                if(resultCode == RESULT_OK){
                    db.getWritableDatabase().insert("DIET",null,(ContentValues) data.getExtras().get("contentValues"));
                }
                break;
            }
            default: {
                break;
            }
        }
    }

}
