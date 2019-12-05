package com.example.projekt;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEditText;
    private EditText passwordEditText;
    private Button logInButton;

    NutriDatabaseHelper db = new NutriDatabaseHelper(MainActivity.mContext);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        loginEditText = findViewById(R.id.loginEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        logInButton = findViewById(R.id.logInMenuButton);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("login:" + loginEditText.getText());
                System.out.println("pass: " + passwordEditText.getText());

                if(loginEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")){
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
                null,
                "Login = ?",
                new String[]{login},null,null,null);

        cursor.moveToFirst();

        if(cursor.getCount() > 1) throw new Exception("Dwóch użytkowników o takim samym loginie");

        System.out.println();
        if(cursor.getString(2).equals(password)){
            Intent backIntent = new Intent();

            backIntent.putExtra("userId",cursor.getInt(0));
            setResult(RESULT_OK,backIntent);
            finish();

        }else{
            Toast.makeText(LoginActivity.this,"Hasło nie jest poprawne",Toast.LENGTH_SHORT).show();
        }
    }
}
