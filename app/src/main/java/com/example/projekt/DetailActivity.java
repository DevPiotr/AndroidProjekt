package com.example.projekt;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    ArrayList<TextView> mealDetails = new ArrayList<>();

    private Animator currentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int shortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //region Populate View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final NutriDatabaseHelper db = new NutriDatabaseHelper(this);

        mealDetails.add((TextView)findViewById(R.id.mealDetailName));
        mealDetails.add((TextView)findViewById(R.id.mealDetailKcal));
        mealDetails.add((TextView)findViewById(R.id.mealDetailFat));
        mealDetails.add((TextView)findViewById(R.id.mealDetailCarbohydrates));
        mealDetails.add((TextView)findViewById(R.id.mealDetailProtein));
        mealDetails.add((TextView)findViewById(R.id.mealDetailContext));
        mealDetails.add((TextView)findViewById(R.id.mealDetailIngredients));
        mealDetails.add((TextView)findViewById(R.id.mealDetailPreparation));

        ImageView mealImageView = findViewById(R.id.mealDetailImage);

        if(getIntent() != null) {

            String currentItemName = getIntent().getStringExtra("mealName");

            try{
                Cursor cursor = db.getReadableDatabase().query("NUTRI",
                        new String[]{"Name","Kcal","Fat","Carbohydrates","Protein","Description","Ingredients","Preparation","ImagePath"},
                        "Name = ?",
                        new String[]{currentItemName},
                        null,null,null);
                cursor.moveToFirst();
                mealDetails.get(0).setText(cursor.getString(0));
                for(int i=1;i<cursor.getColumnCount()-1;i++){
                    mealDetails.get(i).setText(cursor.getString(i));
                }

                String imagePath = cursor.getString(8);

                if(!imagePath.equals("")){
                    setImageView(mealImageView,imagePath);
                }
                else{
                    LinearLayout imageLayout= findViewById(R.id.mealDetailImageLayout);
                    imageLayout.setVisibility(View.GONE);
                }

                Toast.makeText(this,cursor.getString(8),Toast.LENGTH_SHORT).show();

                cursor.close();
            }catch(SQLiteException exp){
                System.out.println(exp.getMessage());
                Toast.makeText(this,"Problem z pobraniem danych z bazy",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            //TODO:: Zrób coś z tym że intent nie działa
        }

        //endregion

    }

    private void setImageView(ImageView mealImageView, String imagePath) {
        File imgFile = new File(imagePath);

        if(imgFile.exists()){
            mealImageView.setImageURI(Uri.fromFile(imgFile));
        }
        else{
            Log.d("Image","Nie istnieje");
        }
    }

}
