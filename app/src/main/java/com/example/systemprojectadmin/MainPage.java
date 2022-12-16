package com.example.systemprojectadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPage extends AppCompatActivity {
    BottomNavigationView bnView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        bnView = findViewById(R.id.bottomNavigation);
        bnView.setItemIconTintList(null);
    }
}