package com.zombietechinc.rovingrepairs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static java.security.AccessController.getContext;

public class VehicleConfirmationActivity extends AppCompatActivity {

    private TextView questiontv;
    private String year;
    private String make;
    private String model;
    private String question;
    private Button buttonNo;
    private Button buttonYes;
    Vehicle mVehicle;
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String userID;
    private String vehicleKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_confirmation);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("vehicles/userID");
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                userID = user.getUid();
                mDatabaseReference = mFirebaseDatabase.getReference("vehicles/" + userID );
            }
        };



        questiontv = (TextView) findViewById(R.id.questiontv);
        Intent intent = getIntent();
        year = intent.getStringExtra("year");
        make = intent.getStringExtra("make");
        model = intent.getStringExtra("model");

        question = "So you've got a " + year + " " + make + " " + model + "?";
        questiontv.setText(question);

        buttonNo = (Button) findViewById(R.id.buttonNo);
        buttonYes = (Button) findViewById(R.id.buttonYes);

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertUserSuccess();
                vehicleKey = mDatabaseReference.push().getKey();
                mVehicle = new Vehicle(make, Integer.parseInt(year), model, vehicleKey);
                mDatabaseReference = mFirebaseDatabase.getReference("vehicles/" + userID + "/" + vehicleKey);
                mDatabaseReference.setValue(mVehicle);


            }
        });
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(VehicleConfirmationActivity.this, "Let's try that again...", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(VehicleConfirmationActivity.this, SelectVehicleActivity.class);
                startActivity(intent1);

            }
        });

    }

    private void alertUserSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Great!")
                .setMessage("Let's Put that in your Garage!")
                .setPositiveButton(this.getString(R.string.error_ok_button_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent(VehicleConfirmationActivity.this, MainActivity.class);
                            startActivity(intent);

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}
