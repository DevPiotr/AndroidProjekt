package com.example.projekt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class AddNewDietActivity extends AppCompatActivity {

    EditText newDietNewDiet;
    ArrayList<String> nutriValues;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_diet);
    }
}
