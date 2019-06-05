package com.example.projekt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class DietActivity extends AppCompatActivity {

    ListView dietListView;
    SimpleAdapter simpleAdapter;

    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();


    NutriDatabaseHelper db = new NutriDatabaseHelper(MainActivity.mContext);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        dietListView = findViewById(R.id.dietListView);

        ArrayList<String> dietNames = db.getDietNames();

        for (int i=0; i<dietNames.size();i++){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("name",dietNames.get(i));
            arrayList.add(hashMap);
        }

        String[] from = {"name"};
        int[] to = {R.id.textView};
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
                                db.getWritableDatabase().delete("NUTRI",
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
}
