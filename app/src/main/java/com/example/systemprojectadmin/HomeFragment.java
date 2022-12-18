package com.example.systemprojectadmin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //declare variables
        FirebaseDatabase rootnode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootnode.getReference();
        TextView tname, tmail, tphone, toverview, tweb, ttype, tsize, thq, ttype2;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        Button edit;

        //get id of textviews
        tname = view.findViewById(R.id.company_name);
        tmail = view.findViewById(R.id.usermail);
        tphone = view.findViewById(R.id.userconct);
        toverview = view.findViewById(R.id.about);
        tweb = view.findViewById(R.id.web);
        ttype = view.findViewById(R.id.usertype);
        tsize = view.findViewById(R.id.size);
        thq = view.findViewById(R.id.headquarter);
        ttype2 = view.findViewById(R.id.type2);
        edit = view.findViewById(R.id.edit);

        reference.child("CompanyDB").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //assert user != null;
                String uid = user.getUid();
                String name = snapshot.child(uid).child("name").getValue(String.class);
                String mail = snapshot.child(uid).child("email").getValue(String.class);
                String phone = snapshot.child(uid).child("phone").getValue(String.class);
                String overview = snapshot.child(uid).child("about").getValue(String.class);
                String web = snapshot.child(uid).child("web").getValue(String.class);
                String utype = snapshot.child(uid).child("category").getValue(String.class);
                String utype2 = snapshot.child(uid).child("Type").getValue(String.class);
                String size = snapshot.child(uid).child("Size").getValue(String.class);
                String hq = snapshot.child(uid).child("Location").getValue(String.class);
                //Picasso.with(getActivity()).load(image).into(userimage);

                SharedPreferences preferences = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor myEdit = preferences.edit();
                myEdit.putString("CompanyName",name);
                myEdit.apply();

                tname.setText(name);
                tmail.setText(mail);
                tphone.setText(phone);
                toverview.setText(overview);
                tweb.setText(web);
                ttype.setText(utype);
                ttype2.setText(utype2);
                tsize.setText(size);
                thq.setText(hq);
                ttype2.setText(utype2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}