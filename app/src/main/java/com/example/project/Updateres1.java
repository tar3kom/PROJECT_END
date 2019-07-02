package com.example.project;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.*;
import com.google.firebase.database.*;

import java.util.Map;

public class Updateres1 extends Activity {
    public DatabaseReference testapp;
    private TextView ntextview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateres);
        ntextview = findViewById(R.id.response);
        final EditText newres = findViewById(R.id.newres);
        final Button butup= findViewById(R.id.button_update);
        final Spinner resspin = findViewById(R.id.res_select);

        final String[] reslist = getResources().getStringArray(R.array.scplace);
        final ArrayAdapter<String> adapterres = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, reslist);
        resspin.setAdapter(adapterres);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        testapp = database.getReference();

        resspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                testapp.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map map = (Map)dataSnapshot.child(reslist[position]).getValue();
                        String username = String.valueOf(map.get("res"));
                        ntextview.setText(username);
                        newres.setText(username);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        ntextview.setText("Can't Read");

                    }
                });

                butup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference mUsersRef = mRootRef.child(reslist[position]);
                        String newresponse = newres.getText().toString();
                        mUsersRef.child("res").setValue(newresponse);
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });













    }
}
