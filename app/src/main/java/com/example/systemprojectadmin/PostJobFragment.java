package com.example.systemprojectadmin;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


public class PostJobFragment extends Fragment {
    EditText job_title, last_date, job_type;
    String date2 = "";
    String item ="";
    Button post, close;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_job, container, false);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        job_title = view.findViewById(R.id.job_title);
        job_type = view.findViewById(R.id.job_typ);
        post = view.findViewById(R.id.postb);
        close = view.findViewById(R.id.closeb);

        List<String> status2 = new ArrayList<>();
        status2.add(0,"Choose Category");
        status2.add("IT");
        status2.add("Business & Management");
        status2.add("Engineering");
        status2.add("Biology & Medical Science");
        status2.add("Others");
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, status2);
        spinner.setAdapter(arrayAdapter2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#7ACAEBFF"));
                if (parent.getItemAtPosition(position).equals("Choose Category")){
                }else {
                    item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(),"Selected: " +item, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //set date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        last_date = view.findViewById(R.id.last_date);

        last_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "-" + month + "-" + year;
                        last_date.setText(date);
                        date2 = date;
                    }
                }, year, month, day);
                dialog.show();
            }
        });
        SharedPreferences pref = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        String companyName = pref.getString("CompanyName","");
        Toast.makeText(getActivity(),"Name "+companyName,Toast.LENGTH_SHORT).show();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String job_name = job_title.getText().toString();
                String job_typ = job_type.getText().toString();
                String uid = user.getUid();


                if (job_name != null && job_typ != null)
                {
                    reference.child("JobVacancy").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long total = snapshot.child("Category").child(item).getChildrenCount();
                            String pos = Objects.toString(total,"");
                            reference.child("JobVacancy").child("Category").child(item).child(pos).child("Company").setValue(companyName);
                            reference.child("JobVacancy").child("Category").child(item).child(pos).child("Location").setValue(job_typ);
                            reference.child("JobVacancy").child("Category").child(item).child(pos).child("Post").setValue(job_name);
                            reference.child("JobVacancy").child("Category").child(item).child(pos).child("Last Date").setValue(date2);

                            reference.child("CompanyJob").child(uid).child(pos).child("Company").setValue(companyName);
                            reference.child("CompanyJob").child(uid).child(pos).child("Location").setValue(job_typ);
                            reference.child("CompanyJob").child(uid).child(pos).child("Category").setValue(item);
                            reference.child("CompanyJob").child(uid).child(pos).child("Post").setValue(job_name);
                            reference.child("CompanyJob").child(uid).child(pos).child("Last Date").setValue(date2);


                            Toast.makeText(getActivity(),"Job Posted",Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JobFragment job = new JobFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.container, job ,"null");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }
}