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

import com.github.mikephil.charting.data.PieEntry;

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
                    "Preparation LONGTEXT," +
                    "ImagePath TEXT DEFAULT '')";


    private static final String CREATE_TABLE_DIET =
            "CREATE TABLE  IF NOT EXISTS "+ TABLE_DIET +"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Name TEXT UNIQUE," +
                    "Meals LONGTEXT," +
                    "userId INTEGER REFERENCES USERS(_id) ON DELETE CASCADE ON UPDATE CASCADE," +
                    "Kcal INTEGER," +
                    "Fat INTEGER," +
                    "Carbohydrates INTEGER," +
                    "Protein INTEGER," +
                    "beginDate INTEGER," +
                    "endDate INTEGER)";

    private static final String CREATE_TABLE_USER =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_USER +"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Login TEXT UNIQUE," +
                    "Password TEXT," +
                    "isAdmin INTEGER)";



    public String[] meals;
    private String[] nutriValues;
    private static final String DBNAME="NutriValue";
    private static final int DBVER = 4;

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

        //First user

        contentValues.clear();
        contentValues.put("Login","Admin");
        contentValues.put("Password","Admin");
        contentValues.put("isAdmin",1);
        db.insert(TABLE_USER,null,contentValues);
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

    public ArrayList<String> getUserNames() {
        ArrayList<String> ret = new ArrayList<>();
        try{
            Cursor cursor = getReadableDatabase().query(TABLE_USER,
                    new String[]{"Login","isAdmin"},
                    null,
                    null,
                    null,null,null);
            if(cursor.moveToFirst()) {
                do {
                    if(cursor.getInt(1) == 0)
                        ret.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }

            cursor.close();
        }catch(SQLiteException exp){
            System.out.println(exp.getMessage());
        }
        return ret;

    }

    public ArrayList<String> getAllDietNames() {

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

    public ArrayList<String> getUserDietNames(int userId) {

        ArrayList<String> ret = new ArrayList<>();
        try{
            Cursor cursor = getReadableDatabase().query(TABLE_DIET,
                    new String[]{"Name"},
                    "userId = ?",
                    new String[]{String.valueOf(userId)},
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

    public boolean userExist(String userLogin) {
        Cursor cursor;
        try {
            cursor = getReadableDatabase().query(TABLE_USER,
                    null,
                    "Login = ?",
                    new String[]{userLogin}, null, null, null);

            return cursor.moveToFirst();
        }catch(SQLiteException exp){
            System.out.println(exp.getMessage());
        }
         return false;
    }

    public void createNewUser(String login,String password) {
        ContentValues contentValue = new ContentValues() ;

        contentValue.put("Login",login);
        contentValue.put("Password",password);
        contentValue.put("isAdmin",0);
        getWritableDatabase().insert(TABLE_USER,null,contentValue);
    }

    public String getUserNameById(int loggedUserId) {

        Cursor cursor = getReadableDatabase().query(TABLE_USER,
                new String[]{"Login"},
                "_id = ?",
                new String[]{String.valueOf(loggedUserId)},null,null,null);

        cursor.moveToFirst();

        return cursor.getString(0);
    }

    public void showAllUsers() {

        try {
            Cursor cursor = getReadableDatabase().query(TABLE_USER,
                    null,
                    null,
                    null, null, null, null);


            if(cursor.moveToFirst()){
                do {
                    System.out.println(cursor.getInt(0) + " ; " + cursor.getString(1) + " ; " + cursor.getString(2));
                } while (cursor.moveToNext());
            }
        }catch(SQLiteException exp){
            System.out.println(exp.getMessage());
        }

    }

    public ArrayList<String> getDietNutriValuesByName(String dietName) {

        ArrayList<String> ret = new ArrayList<>();

        try{
            Cursor cursor = getReadableDatabase().query(TABLE_DIET,
                    new String[]{"Kcal","Fat","Carbohydrates","Protein"},
                    "Name = ?",
                    new String[]{dietName},
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

    public int[] getDietDatesByName(String dietName) {
        int[] ret = new int[2];
        try{
            Cursor cursor = getReadableDatabase().query(TABLE_DIET,
                    new String[]{"beginDate","endDate"},
                    "Name = ?",
                    new String[]{dietName},
                    null,null,null);
            if(cursor.moveToFirst()) {
                for (int i = 0; i < 2; i++) {
                    ret[i] = cursor.getInt(i);
                }
            }
            cursor.close();
        }catch(SQLiteException exp){
            System.out.println(exp.getMessage());
        }

        return ret;
    }


    public int[] getFullUserDietValuesById(int userId) {
        int[] ret = new int[]{0,0,0,0};
        int dietCount=0;
        try{
            Cursor cursor = getReadableDatabase().query(TABLE_DIET,
                    new String[]{"Kcal","Fat","Carbohydrates","Protein"},
                    "userId = ?",
                    new String[]{String.valueOf(userId)},
                    null,null,null);
            if(cursor.moveToFirst()){
              do{
                  dietCount++;
                for(int i=0;i<4;i++){
                    ret[i] += cursor.getInt(i);
                }
              }while(cursor.moveToNext());

                ret[0] = ret[0]/dietCount;
                ret[1] = ret[1]/dietCount;
                ret[2] = ret[2]/dietCount;
                ret[3] = ret[3]/dietCount;
            }
            else {
                cursor.close();
                ret = null;
            }
        }catch (SQLiteException exp){
            exp.printStackTrace();
        }

        return ret;
    }

    public int[] getSpecificDietNutriValuesCount(int userId, int count) {
        int[] ret = new int[]{0,0,0,0};
        int dietCount=0;
        try{
            Cursor cursor = getReadableDatabase().query(TABLE_DIET,
                    new String[]{"Kcal","Fat","Carbohydrates","Protein"},
                    "userId = ?",
                    new String[]{String.valueOf(userId)},
                    null,null,"beginDate DESC",
                    String.valueOf(count));
            if(cursor.moveToFirst()){
                do{
                    dietCount++;
                    for(int i=0;i<4;i++){
                        ret[i] += cursor.getInt(i);
                    }
                }while(cursor.moveToNext());
            }
            else {
                cursor.close();
                ret = null;
            }
        }catch (SQLiteException exp){
            exp.printStackTrace();
        }
        ret[0] = ret[0]/dietCount;
        ret[1] = ret[1]/dietCount;
        ret[2] = ret[2]/dietCount;
        ret[3] = ret[3]/dietCount;
        return ret;
    }

    public int howMuchDietUserHas(int userId) {
        int countRet = 0;

        try{
            Cursor cursor = getReadableDatabase().query(TABLE_DIET,
                    null,
                    "userId = ?",
                    new String[]{String.valueOf(userId)},
                    null,null,null);

            countRet = cursor.getCount();
            cursor.close();
        }catch (SQLiteException exp){
            exp.printStackTrace();
        }

        return countRet;
    }

    public int howMuchDietUserHas(String login) {
        int countRet = 0;

        int userId = getUserIdByName(login);
        try{
            Cursor cursor = getReadableDatabase().query(TABLE_DIET,
                    null,
                    "userId = ?",
                    new String[]{String.valueOf(userId)},
                    null,null,null);

            countRet = cursor.getCount();
            cursor.close();
        }catch (SQLiteException exp){
            exp.printStackTrace();
        }

        return countRet;
    }

    public int getUserIdByName(String login) {
        int userId = 0;
        try{
            Cursor cursor = getReadableDatabase().query(TABLE_USER,
                    new String[]{"_id"},
                    "Login = ?",
                    new String[]{String.valueOf(login)},
                    null,null,null);
            if(cursor.moveToFirst()){
                userId = cursor.getInt(0) ;
            }
            cursor.close();
        }catch (SQLiteException exp){
            exp.printStackTrace();
        }

        return userId;
    }

    public boolean userIsAdmin(int loggedUserId) {

        boolean ret = false;
        try{
            Cursor cursor = getReadableDatabase().query(TABLE_USER,
                    new String[]{"isAdmin"},
                    "_id = ?",
                    new String[]{String.valueOf(loggedUserId)},
                    null,null,null);

            if(cursor.moveToFirst()){
                if(cursor.getInt(0) == 1){
                    ret = true;
                }
            }
            cursor.close();
        }catch (SQLiteException exp){
            exp.printStackTrace();
        }

        return ret;
    }
}
