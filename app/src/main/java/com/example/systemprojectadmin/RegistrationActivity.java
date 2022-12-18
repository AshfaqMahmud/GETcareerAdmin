package com.example.systemprojectadmin;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;

public class RegistrationActivity extends AppCompatActivity {
    String type="";
    String name = "";
    String id = "";
    String cat = "";
    String email = "";
    String pass = "";
    String cpass = "";
    String location = "";
    String size = "";
    String about = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
        setContentView(R.layout.activity_registration);
        //Declare variables
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        Button register = findViewById(R.id.rgstrb);
        Spinner spinner = (Spinner) findViewById(R.id.companytyp);
        Spinner spinner2 = (Spinner) findViewById(R.id.companysize);
        EditText tname, tmail, tpass, tid, tloc,tcpass, tabout;

        tname = findViewById(R.id.tname);
        tid = findViewById(R.id.tid);
        tmail = findViewById(R.id.tmail);
        tloc = findViewById(R.id.tloc);
        tabout = findViewById(R.id.tabout);
        tpass = findViewById(R.id.tpass);
        tcpass = findViewById(R.id.tcpass);

        //arraylist for spinner
        List<String> status = new ArrayList<>();
        status.add(0,"Choose Industry");
        status.add("Engineering & IT");
        status.add("Business & Management");
        status.add("Manufacturing");
        status.add("Biology & Medical Science");
        status.add("Others");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, status);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#80A29595"));
                if (parent.getItemAtPosition(position).equals("Choose Industry")) {
                } else {
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        List<String> status2 = new ArrayList<>();
        status2.add(0,"Company Size :");
        status2.add("less than 50");
        status2.add("50 to 100");
        status2.add("100 to 500");
        status2.add("500 to 1000");
        status2.add("1000 to less than 10000");
        status2.add("more than 10000");
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, status2);
        spinner2.setAdapter(arrayAdapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#80A29595"));
                if (parent.getItemAtPosition(position).equals("Company Size :")) {
                } else {
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        //register button clicl action
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(RegistrationActivity.this, "Register Button CLicked",Toast.LENGTH_SHORT).show();
                name = tname.getText().toString();
                id = tid.getText().toString();
                cat = spinner.getSelectedItem().toString();
                email = tmail.getText().toString();
                pass = tpass.getText().toString();
                cpass = tcpass.getText().toString();
                location = tloc.getText().toString();
                size = spinner2.getSelectedItem().toString();
                about = tabout.getText().toString();

                if(email!="" && pass!="")
                {
                    if(pass.length()<8)
                    {
                        tpass.setError("Password must be size of 8!");
                    }
                    else
                    {
                        if(pass.equals(cpass))
                        {
                            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String uid = user.getUid();
                                        reference.child("CompanyDB").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                reference.child("CompanyDB").child(uid).child("name").setValue(name);
                                                reference.child("CompanyDB").child(uid).child("id").setValue(id);
                                                reference.child("CompanyDB").child(uid).child("phone").setValue("0101010101");
                                                reference.child("CompanyDB").child(uid).child("category").setValue(cat);
                                                reference.child("CompanyDB").child(uid).child("email").setValue(email);
                                                reference.child("CompanyDB").child(uid).child("Location").setValue(location);
                                                reference.child("CompanyDB").child(uid).child("Size").setValue(size);
                                                reference.child("CompanyDB").child(uid).child("Type").setValue(type);
                                                reference.child("CompanyDB").child(uid).child("about").setValue(about);
                                                reference.child("CompanyDB").child(uid).child("web").setValue("http://www.bing.com");

                                                Toast.makeText(RegistrationActivity.this, "Registration success.",Toast.LENGTH_SHORT).show();
                                                //start login activity to login
                                                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }


                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                    else{
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegistrationActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else
                        {
                            tpass.setError("Password not matching");
                            tcpass.setError("Password not matching");
                        }
                    }

                }
            }
        });
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbutton1:
                if (checked) {
                    type="Global";
                }
                break;
            case R.id.rbutton2:
                if (checked) {
                    type="Multinational";
                }
                break;
            case R.id.rbutton3:
                if (checked) {
                    type="Local";
                }
                break;
        }
    }
}