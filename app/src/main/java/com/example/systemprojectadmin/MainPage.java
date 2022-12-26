package com.example.systemprojectadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainPage extends AppCompatActivity {
    BottomNavigationView bnView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        bnView = findViewById(R.id.bottomNavigation);
        bnView.setItemIconTintList(null);

        String uid = user.getUid();

        reference.child("CompanyDB").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String uid2 = dataSnapshot.getKey();
                    if(uid2.equals(uid))
                    {
                        String companyName = snapshot.child(uid).child("name").getValue(String.class);
                        //Toast.makeText(MainPage.this,"In intent "+companyName,Toast.LENGTH_SHORT).show();
                        /*SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = pref.edit();
                        //myEdit.putString("category",cat);
                        myEdit.putString("usercompany",companyName);
                        myEdit.apply();*/
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        HomeFragment fragment=new HomeFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment,"");
        fragmentTransaction.commit();
        bnView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.home:

                        HomeFragment fragment = new HomeFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container, fragment, "");
                        fragmentTransaction.commit();
                        return true;

                    case R.id.job:

                        JobFragment fragment1 = new JobFragment();
                        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction1.replace(R.id.container, fragment1);
                        fragmentTransaction1.commit();
                        return true;

                    case R.id.review:

                        ShowReviews fragment2 = new ShowReviews();
                        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.container, fragment2);
                        fragmentTransaction2.commit();
                        return true;

                    case R.id.growth:

                        GrowthFragment fragment3 = new GrowthFragment();
                        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction3.replace(R.id.container, fragment3);
                        fragmentTransaction3.commit();
                        return true;
                }
                return false;
            }
        });
    }
}