package com.example.projekt;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    //region  INTENTCODE
    public static final int CODE_ADDMEAL = 1;

    public static final int CODE_LOGIN = 11;
    //endregion

    //region global variable
    ListView simpleListView;
    SimpleAdapter simpleAdapter;

    public static Context mContext;

    public NutriDatabaseHelper db;

    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();

    private int loggedUserId = 0;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        deleteDatabase("NutriValue");

        setContentView(R.layout.activity_main);
        mContext = this;
        db = new NutriDatabaseHelper(this);

        SharedPreferences shared = getSharedPreferences("shared", MODE_PRIVATE);

        //region listView populate
        simpleListView = findViewById(R.id.simpleListView);

        ArrayList<String> mealNames = db.getMealNames();
        for (int i=0; i<mealNames.size();i++){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("name",mealNames.get(i));
            arrayList.add(hashMap);
        }

        String[] from = {"name"};
        int[] to = {R.id.textView};
        simpleAdapter = new SimpleAdapter(this,arrayList,R.layout.list_view_items,from,to);

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
        //endregion

        if(shared.contains("userId")) {
            loggedUserId  = shared.getInt("userId",0);
            setHelloText();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.popup_menu,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        invalidateOptionsMenu();
        if(userLogged()){
            menu.findItem(R.id.logInMenuButton).setVisible(false);
            menu.findItem(R.id.showDietsMenuButton).setVisible(true);
            menu.findItem(R.id.addMealMenuButton).setVisible(true);
            menu.findItem(R.id.logOutMenuButton).setVisible(true);
        }else{
            menu.findItem(R.id.logInMenuButton).setVisible(true);
            menu.findItem(R.id.showDietsMenuButton).setVisible(false);
            menu.findItem(R.id.addMealMenuButton).setVisible(false);
            menu.findItem(R.id.logOutMenuButton).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        switch(item.getItemId()){
            case R.id.logInMenuButton:
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent,CODE_LOGIN);
                return true;
            case R.id.addMealMenuButton:
                intent = new Intent(MainActivity.this,AddNewMealActivity.class);
                startActivityForResult(intent,CODE_ADDMEAL);
                return true;
            case R.id.showDietsMenuButton:
                intent = new Intent(MainActivity.this,DietActivity.class);
                intent.putExtra("userId",loggedUserId);
                startActivity(intent);
                return true;
            case R.id.logOutMenuButton:
                loggedUserId = 0;
                clearRememberUser();
                recreate();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearRememberUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared",MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
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
            case CODE_LOGIN: {
                if(resultCode == RESULT_OK) {
                    loggedUserId = (int)data.getExtras().get("userId");
                    setHelloText();
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    private void setHelloText() {
        String helloText = "Witaj " + db.getUserNameById(loggedUserId) + " !";
        ((TextView)findViewById(R.id.helloText)).setText(helloText);
    }

    private boolean userLogged(){
        return loggedUserId != 0;
    }

}
