package com.example.projekt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class UserManagementActivity extends AppCompatActivity {

    ListView userListView;
    SimpleAdapter simpleAdapter;


    NutriDatabaseHelper db = new NutriDatabaseHelper(MainActivity.mContext);

    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        //region listView populate
        userListView = findViewById(R.id.userListView);

        ArrayList<String> userNames = db.getUserNames();
        for (int i=0; i<userNames.size();i++){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("name",userNames.get(i));
            hashMap.put("count",String.valueOf(db.howMuchDietUserHas(userNames.get(i))));
            arrayList.add(hashMap);
        }

        String[] from = {"name","count"};
        int[] to = {R.id.usernameList,R.id.dietCount};
        simpleAdapter = new SimpleAdapter(this,arrayList,R.layout.user_view_list,from,to);

        userListView.setAdapter(simpleAdapter);

        /*userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("mealName",arrayList.get((int)id).get("name"));
                startActivity(intent);
            }
        });*/

        userListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int mealId = (int)id;
                new AlertDialog.Builder(UserManagementActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Usuwanie użytkownika")
                        .setMessage("Czy chcesz usunąć tego użytkownika?")
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String login =arrayList.get(mealId).get("name");

                                db.getWritableDatabase().delete("DIET",
                                        "userId = ?",
                                        new String[]{String.valueOf(db.getUserIdByName(login))});

                                db.getWritableDatabase().delete("USERS",
                                        "Login = ?",
                                        new String[]{login});

                                arrayList.remove(mealId);
                                simpleAdapter.notifyDataSetChanged();
                            }

                        })
                        .setNegativeButton("Nie", null)
                        .show();

                return true;
            }
        });
        //endregion
    }
}
