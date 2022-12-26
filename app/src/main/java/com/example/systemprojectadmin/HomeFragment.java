package com.example.systemprojectadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {
    ProgressDialog dialog;
    ImageView logout, edit, cancel;
    EditText editweb;
    Button save;

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
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Loading Home Screen\nThis may take some time");
        dialog.show();

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
        edit = view.findViewById(R.id.editbutton);
        cancel = view.findViewById(R.id.cancelbtn);
        logout = view.findViewById(R.id.logout);
        editweb= view.findViewById(R.id.eweb);
        save= view.findViewById(R.id.save);

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

                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(view, "Logout clicked", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editweb.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                tweb.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editweb.setVisibility(View.GONE);
                        cancel.setVisibility(View.GONE);
                        tweb.setVisibility(View.VISIBLE);
                        save.setVisibility(View.GONE);
                    }
                });
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String web = editweb.getText().toString();
                if(web.equals(""))
                {
                    Snackbar.make(getActivity().findViewById(android.R.id.content),
                            "Web address is null!!", Snackbar.LENGTH_LONG).show();
                }
                else
                {
                    reference.child("CompanyDB").child(user.getUid()).child("web").setValue(web);
                    editweb.setVisibility(View.GONE);
                    cancel.setVisibility(View.GONE);
                    tweb.setVisibility(View.VISIBLE);
                    save.setVisibility(View.GONE);
                    Snackbar.make(getActivity().findViewById(android.R.id.content),
                            "Edited successfully", Snackbar.LENGTH_LONG).show();
                }
            }
        });


        return view;
    }
}