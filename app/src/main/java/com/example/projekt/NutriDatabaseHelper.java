package com.example.projekt;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NutriDatabaseHelper extends SQLiteOpenHelper {

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
        String sqlString=
                "CREATE TABLE NUTRI(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "Name TEXT," +
                        "Kcal INTEGER," +
                        "Fat INTEGER," +
                        "Carbohydrates INTEGER," +
                        "Protein INTEGER," +
                        "Description TEXT)";
        db.execSQL(sqlString);

        ContentValues contentValues = new ContentValues();


        String[] tmp;
        for(int i=0; i < meals.length;i++){
            contentValues.clear();
            // Pobierz dane
            contentValues.put("Name",meals[i]);
            //Wrzuć do bazy danych
            tmp = nutriValues[i].split(";");
            contentValues.put("Kcal",tmp[0]);
            contentValues.put("Fat",tmp[1]);
            contentValues.put("Carbohydrates",tmp[2]);
            contentValues.put("Protein",tmp[3]);
            contentValues.put("Description",tmp[4]);

            db.insert("NUTRI",null,contentValues);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
