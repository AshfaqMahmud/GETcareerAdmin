package com.example.systemprojectadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent;
        if (user != null) {
            // User is signed in
            intent = new Intent(MainActivity.this, MainPage.class);
        } else {
            intent = new Intent(MainActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}