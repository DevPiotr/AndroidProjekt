package com.example.projekt;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class DietActivity extends AppCompatActivity {

    public static final int CODE_ADDDIET = 10;

    ListView dietListView;
    SimpleAdapter simpleAdapter;

    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();


    NutriDatabaseHelper db = new NutriDatabaseHelper(MainActivity.mContext);

    int loggedUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        loggedUserId = getIntent().getIntExtra("userId",0);

        dietListView = findViewById(R.id.dietListView);

        ArrayList<String> dietNames ;

        if(db.userIsAdmin(loggedUserId))
            dietNames = db.getAllDietNames();
        else
            dietNames = db.getUserDietNames(loggedUserId);

        for (int i=0; i<dietNames.size();i++){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("name",dietNames.get(i));
            arrayList.add(hashMap);
        }

        String[] from = {"name"};
        int[] to = {R.id.usernameList};
        simpleAdapter =
                new SimpleAdapter(this,arrayList,R.layout.list_view_items,from,to);
        dietListView.setAdapter(simpleAdapter);

        dietListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DietActivity.this,DietDetailActivity.class);
                intent.putExtra("dietName",arrayList.get((int)id).get("name"));
                startActivity(intent);
            }
        });

        dietListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int dietId = (int)id;
                new AlertDialog.Builder(DietActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Usuwanie diety")
                        .setMessage("Czy chcesz usunąć tą dietę?")
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.getWritableDatabase().delete("DIET",
                                        "name = ?",
                                        new String[]{arrayList.get(dietId).get("name")});
                                arrayList.remove(dietId);
                                simpleAdapter.notifyDataSetChanged();
                            }

                        })
                        .setNegativeButton("Nie", null)
                        .show();

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.diet_popup_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.addDietMenuButton:
                Intent intent = new Intent(DietActivity.this,AddNewDietActivity.class);
                startActivityForResult(intent,CODE_ADDDIET);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case CODE_ADDDIET: {
                if (resultCode == RESULT_OK) {
                    ContentValues contentValues = (ContentValues) data.getExtras().get("contentValues");

                    contentValues.put("userId",loggedUserId);

                    db.getWritableDatabase().insert("DIET", null, contentValues);
                    recreate();
                }
                break;
            }
        }
    }


}
