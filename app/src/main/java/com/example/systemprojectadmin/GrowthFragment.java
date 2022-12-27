package com.example.systemprojectadmin;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.NumberFormat;
import java.text.ParseException;

public class GrowthFragment extends Fragment {
    LinearLayout layout;
    FirebaseUser user;
    long n = 0;
    int year, value;

    GraphView graphView;
    LineGraphSeries series;
    FirebaseDatabase node = FirebaseDatabase.getInstance();
    DatabaseReference reference = node.getReference("CompanyDB");

    @Override
    public void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
        String cid = user.getUid();
        reference.child(cid).child("Economy").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataPointInterface[] dp = new DataPoint[(int) snapshot.getChildrenCount()];
                int index = 0;
                for(DataSnapshot snapshot1: snapshot.getChildren())
                {
                    PointValue pointValue = snapshot1.getValue(PointValue.class);
                    dp[index]=new DataPoint(pointValue.getxYear(),pointValue.getyVal());
                    index++;
                }
                series.resetData(dp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_growth, container, false);


        TextView tname, tmail, ttype, tabout, tloc, tsize;
        EditText tyear, tvalue;

        tyear = view.findViewById(R.id.eyear);
        tvalue = view.findViewById(R.id.evalue);
        layout = view.findViewById(R.id.hiddenlayout);
        Button edit, enter, close;
        edit = view.findViewById(R.id.editbutton);
        enter = view.findViewById(R.id.postb);
        close = view.findViewById(R.id.closeb);
        series = new LineGraphSeries();
        graphView = view.findViewById(R.id.graph);
        graphView.addSeries(series);
        user = FirebaseAuth.getInstance().getCurrentUser();
        String cid = user.getUid();


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.VISIBLE);
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String year = tyear.getText().toString();
                String value = tvalue.getText().toString();
                String id = reference.push().getKey();
               /* reference.child(cid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        n = snapshot.child("Economy").getChildrenCount() + 1;
                        reference.child(cid).child("Economy").child(String.valueOf(n)).child("Year").setValue(year);
                        reference.child(cid).child("Economy").child(String.valueOf(n)).child("Value").setValue(value);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/
                int x = Integer.parseInt(year);
                float y = Float.parseFloat(value);
                PointValue pointValue = new PointValue(x, y);
                assert id != null;
                reference.child(cid).child("Economy").child(id).setValue(pointValue);
                //reference
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
            }
        });


        return view;
    }
}