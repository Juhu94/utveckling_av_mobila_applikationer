package com.example.julian.da345a_mobila_applikationer_p1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * The mainActivity.
 * Checks to see if a user is already registered,
 * if a user is already registered, the activity proceeds to the mainMenu.
 *
 * If no user is registered, prompting the user to register a name to use.
 */
public class MainActivity extends AppCompatActivity {

    private EditText etFirstname;
    private EditText etSurname;
    private Button btnAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences mSharedPreferences = getSharedPreferences("MainActivity", AppCompatActivity.MODE_PRIVATE);
        String savedFirstname = mSharedPreferences.getString("Firstname", "");

        //Kollar ifall en användaren är sparad
        //Om det finns en användare, hämtar för-efternamn och startar meny activity
        //Annars initiera allt som vanligt
        if(!savedFirstname.equals("")){
            String savedSurname = mSharedPreferences.getString("Surname", null);
            Intent intent = new Intent(this, MenuActivity.class);
            intent.putExtra("Firstname", savedFirstname);
            intent.putExtra("Surname", savedSurname);
            startActivity(intent);
            finish();
        }

        etFirstname = findViewById(R.id.etFirstname);
        etSurname = findViewById(R.id.etSurname);
        btnAddUser = findViewById(R.id.btnAddUser);

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });
    }

    private void addUser(){
        String firstname = etFirstname.getText().toString();
        String surname = etSurname.getText().toString();

        //Spara för-efternamn i sharedpreferences
        SharedPreferences mSharedPreferences = getSharedPreferences("MainActivity", AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("Firstname", firstname);
        editor.putString("Surname", surname);
        editor.apply();

        //Starta meny activity och skicka med för-efternamn i intent
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("Firstname", firstname);
        intent.putExtra("Surname", surname);
        startActivity(intent);
        finish();
    }
}
