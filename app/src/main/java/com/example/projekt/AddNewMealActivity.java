package com.example.projekt;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class AddNewMealActivity extends AppCompatActivity {

    final static int RESULT_LOAD_IMAGE_GALLERY = 11;

    ArrayList<EditText> newMealDetails = new ArrayList<>();

    Button addMealAddButton;

    String localImagePath = "";
    String savedImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_meal);
        final NutriDatabaseHelper db = new NutriDatabaseHelper(this);
        addMealAddButton = findViewById(R.id.addMealAddButton);
        Button addImageFromGallery = findViewById(R.id.addImageFromGalleryButton);

        newMealDetails.add((EditText)findViewById(R.id.addMealName));
        newMealDetails.add((EditText)findViewById(R.id.addMealKcal));
        newMealDetails.add((EditText)findViewById(R.id.addMealFat));
        newMealDetails.add((EditText)findViewById(R.id.addMealCarbohydrates));
        newMealDetails.add((EditText)findViewById(R.id.addMealProtein));
        newMealDetails.add((EditText)findViewById(R.id.addMealContext));
        newMealDetails.add((EditText)findViewById(R.id.addMealIngredients));
        newMealDetails.add((EditText)findViewById(R.id.addMealPreparation));

        addMealAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent();
                ArrayList<String>  mealNames = db.getMealNames();
                ContentValues contentValues =  new ContentValues();
                Boolean mealExist = false;

                for(String meal: mealNames){
                    if(meal.toLowerCase().equals(newMealDetails.get(0).getText().toString().toLowerCase())){
                        mealExist = true;
                        break;
                    }
                }
                contentValues.clear();
                if(!mealExist) {
                    contentValues.put("Name", newMealDetails.get(0).getText().toString());
                    contentValues.put("Kcal", newMealDetails.get(1).getText().toString());
                    contentValues.put("Fat", newMealDetails.get(2).getText().toString());
                    contentValues.put("Carbohydrates", newMealDetails.get(3).getText().toString());
                    contentValues.put("Protein", newMealDetails.get(4).getText().toString());
                    contentValues.put("Description", newMealDetails.get(5).getText().toString());
                    contentValues.put("Ingredients", newMealDetails.get(6).getText().toString());
                    contentValues.put("Preparation", newMealDetails.get(7).getText().toString());

                    if(!localImagePath.equals("")){
                        contentValues.put("ImagePath",localImagePath);
                    }
                    else{
                        contentValues.put("ImagePath","");
                    }

                    backIntent.putExtra("contentValues", contentValues);

                    setResult(RESULT_OK, backIntent);
                    finish();
                }
                else {
                    Toast.makeText(AddNewMealActivity.this,"Już istnieje taka nazwa posiłku",Toast.LENGTH_SHORT).show();
                }
            }
        });

        addImageFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE_GALLERY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case RESULT_LOAD_IMAGE_GALLERY:
                if(resultCode == RESULT_OK || data != null){

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    localImagePath = cursor.getString(columnIndex);
                    cursor.close();

                    ImageView imageView = findViewById(R.id.mealImage);
                    imageView.setVisibility(View.VISIBLE);

                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                        imageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                }
        }
    }
}
