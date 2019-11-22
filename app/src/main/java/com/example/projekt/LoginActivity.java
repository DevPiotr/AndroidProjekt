package com.example.projekt;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEditText;
    private EditText passwordEditText;
    private Button logInButton;

    public NutriDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new NutriDatabaseHelper(this);

        loginEditText = findViewById(R.id.loginEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        logInButton = findViewById(R.id.logInButton);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loginEditText.equals("") && passwordEditText.equals("")){
                    Toast.makeText(LoginActivity.this,"Pola nie mogą być puste",Toast.LENGTH_SHORT).show();
                }else{

                    try {
                        if (db.userExist(loginEditText.getText().toString())) {
                            logInUser(loginEditText.getText().toString(), passwordEditText.getText().toString());
                        } else {
                            db.createNewUser(loginEditText.getText().toString(), passwordEditText.getText().toString());
                            logInUser(loginEditText.getText().toString(), passwordEditText.getText().toString());
                        }
                    }catch (Exception exp){
                        System.out.println(exp.getMessage());
                    }
                }
            }
        });
    }

    private void logInUser(String login, String password) throws Exception {
        Cursor cursor = db.getReadableDatabase().query("USERS",
                new String[]{login},
                "Login = ?", null,null,null,null);

        if(cursor.getCount() > 1) throw new Exception("Dwóch użytkowników o takim samym loginie");

        cursor.moveToFirst();
        if(cursor.getString(1).equals(password)){

        }else{
            Toast.makeText(LoginActivity.this,"Hasło nie jest poprawne",Toast.LENGTH_SHORT).show();
        }
    }
}
