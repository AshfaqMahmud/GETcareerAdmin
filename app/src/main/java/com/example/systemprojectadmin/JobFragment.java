package com.example.systemprojectadmin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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


public class JobFragment extends Fragment implements RecyclerViewInterface {

    MyAdapter myAdapter;
    RecyclerView recyclerView;
    ArrayList<Job> list;
    DatabaseReference databaseReference,dbref;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ProgressDialog dialog;
    TextView job_post;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_job, container, false);
        recyclerView=view.findViewById(R.id.recycler2);
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Loading available jobs\nThis may take some time");
        dialog.show();

        databaseReference= FirebaseDatabase.getInstance().getReference();

        job_post=view.findViewById(R.id.post_job);
        job_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostJobFragment companyDetails = new PostJobFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.container, companyDetails ,"null");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myAdapter=new MyAdapter(getActivity(),list,  this);
        recyclerView.setAdapter(myAdapter);

        //Toast.makeText(getActivity(),uid+" uid",Toast.LENGTH_SHORT).show();
        String uid = user.getUid();

        databaseReference.child("CompanyJob").child(uid).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    String uid2 =dataSnapshot.child(uid).getKey();
                    String company = dataSnapshot.child("Location").getValue(String.class);
                    String typ = dataSnapshot.child("Category").getValue(String.class);
                    String date = dataSnapshot.child("Last Date").getValue(String.class);
                    String name = dataSnapshot.child("Post").getValue(String.class);

                    date = "Last application date: " + date;
                    //Toast.makeText(getActivity(),uid2,Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getActivity(),company+typ+date+name,Toast.LENGTH_LONG).show();
                    Job job = new Job(name,typ,company,date);
                    list.add(job);
                    dialog.dismiss();
                }
                myAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity()," "+error.toString(),Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    @Override
    public void onItemClick(int position) {

    }
}