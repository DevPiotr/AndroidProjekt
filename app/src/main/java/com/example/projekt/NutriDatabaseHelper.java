package com.example.projekt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class NutriDatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NUTRI = "NUTRI";
    private static final String TABLE_DIET = "DIET";
    private static final String TABLE_USER = "USERS";

    private static final String CREATE_TABLE_NUTRI=
            "CREATE TABLE  IF NOT EXISTS "+ TABLE_NUTRI +"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Name TEXT UNIQUE," +
                    "Kcal INTEGER," +
                    "Fat INTEGER," +
                    "Carbohydrates INTEGER," +
                    "Protein INTEGER," +
                    "Description LONGTEXT," +
                    "Ingredients LONGTEXT," +
                    "Preparation LONGTEXT)";


    private static final String CREATE_TABLE_DIET =
            "CREATE TABLE  IF NOT EXISTS "+ TABLE_DIET +"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Name TEXT UNIQUE," +
                    "Meals LONGTEXT)";

    private static final String CREATE_TABLE_USER =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_USER +"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Login TEXT UNIQUE," +
                    "Password TEXT)";



    public String[] meals;
    private String[] nutriValues;
    private static final String DBNAME="NutriValue";
    private static final int DBVER = 1;

    public NutriDatabaseHelper(Context context) {
        super(context,DBNAME,null,DBVER);

        meals = context.getResources().getStringArray(R.array.Meals);
        nutriValues = context.getResources().getStringArray(R.array.NutriValues);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_NUTRI);
        db.execSQL(CREATE_TABLE_DIET);
        db.execSQL(CREATE_TABLE_USER);


        ContentValues contentValues = new ContentValues();
        String[] tmp;
        for(int i=0; i < meals.length;i++){
            contentValues.clear();

            contentValues.put("Name",meals[i]);
            tmp = nutriValues[i].split(";");
            contentValues.put("Kcal",tmp[0]);
            contentValues.put("Fat",tmp[1]);
            contentValues.put("Carbohydrates",tmp[2]);
            contentValues.put("Protein",tmp[3]);
            contentValues.put("Description",tmp[4]);
            contentValues.put("Ingredients",tmp[5]);
            contentValues.put("Preparation",tmp[6]);

            db.insert("NUTRI",null,contentValues);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_NUTRI + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_DIET + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_USER + "'");
        onCreate(db);
    }

    public ArrayList<String> getMealNames(){
        ArrayList<String> ret = new ArrayList<>();
        try{
            Cursor cursor = getReadableDatabase().query(TABLE_NUTRI,
                    new String[]{"Name"},
                    null,
                    null,
                    null,null,null);
            if(cursor.moveToFirst()) {
                do {
                    ret.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch(SQLiteException exp){
            System.out.println(exp.getMessage());
        }
        return ret;
    }

    public ArrayList<String> getDietNames(){
        ArrayList<String> ret = new ArrayList<>();
        try{
            Cursor cursor = getReadableDatabase().query(TABLE_DIET,
                    new String[]{"Name"},
                    null,
                    null,
                    null,null,null);
            if(cursor.moveToFirst()) {
                do {
                    ret.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }

            cursor.close();
        }catch(SQLiteException exp){
            System.out.println(exp.getMessage());
        }
        return ret;
    }
    public boolean dietExist(){
        boolean ret = false;

        try{
            Cursor cursor = getReadableDatabase().query(TABLE_DIET,
                    new String[]{"Name"},
                    null,
                    null,
                    null,null,null);
            if(cursor.moveToFirst()) {
                ret = true;
            }
            cursor.close();
        }catch(SQLiteException exp){
            System.out.println(exp.getMessage());
        }
        return ret;
    }

    public ArrayList<String> getNutriValuesByName(String mealName) {
        ArrayList<String> ret = new ArrayList<>();

        try{
            Cursor cursor = getReadableDatabase().query(TABLE_NUTRI,
                    new String[]{"Kcal","Fat","Carbohydrates","Protein"},
                    "Name = ?",
                    new String[]{mealName},
                    null,null,null);
            if(cursor.moveToFirst()) {
                for (int i = 0; i < 4; i++) {
                    ret.add(cursor.getString(i));
                }
            }
            cursor.close();
        }catch(SQLiteException exp){
            System.out.println(exp.getMessage());
        }

        return ret;
    }


    public ArrayList<String> getDietMeals(String dietName) {
        ArrayList<String> ret = new ArrayList<>();

        String dietMealsName = "";
        try{
            Cursor cursor = getReadableDatabase().query(TABLE_DIET,
                    new String[]{"Meals"},
                    "Name = ?",
                    new String[]{dietName},
                    null,null,null);
            if(cursor.moveToFirst()) {
                do {
                    dietMealsName = cursor.getString(0);
                } while (cursor.moveToNext());
            }
            cursor.close();

            String[] argsSelection = dietMealsName.split(";");
            String whereSelection = "";
            for(int i=0;i<argsSelection.length;i++){
                whereSelection += "Name = ? OR ";
            }
            whereSelection = whereSelection.substring(0,whereSelection.length()-4);

            cursor = getReadableDatabase().query(TABLE_NUTRI,
                    new String[]{"Name"},
                    whereSelection,
                    argsSelection,
                    null,null,null);
            if(cursor.moveToFirst()) {
                do {
                    ret.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch(SQLiteException exp){
            System.out.println(exp.getMessage());
        }

        return ret;
    }

    public boolean userExist(String login) {
        Cursor cursor = getReadableDatabase().query(TABLE_USER,
                new String[]{login},
                "Login = ?", null,null,null,null);
        return cursor.getCount() >= 1;
    }

    public void createNewUser(String login,String password) {
        ContentValues contentValue = new ContentValues() ;

        contentValue.put("Login",login);
        contentValue.put("Password",password);
        getWritableDatabase().insert(TABLE_USER,null,contentValue);
    }
}
