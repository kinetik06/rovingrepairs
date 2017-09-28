package com.zombietechinc.rovingrepairs;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class IndividualVehicleActivity extends AppCompatActivity {

    String vehicleKey;
    String userID;
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mDatabaseReference;
    public DatabaseReference ref;
    TextView ymmTV;
    TextView appointmentTV;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    ArrayList<Appointment> mAppointments;
    private LinearLayoutManager manager;
    ImageView vehicleIMG;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int MY_PERMISSIONS_REQUEST_WRITE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_vehicle);
        vehicleIMG = (ImageView)findViewById(R.id.vehicle_iv);
        mRecyclerView = (RecyclerView)findViewById(R.id.appointment_rv);
        ymmTV = (TextView)findViewById(R.id.ymm_tv);
        appointmentTV = (TextView)findViewById(R.id.appointment_tv);
        Intent intent = getIntent();
        vehicleKey = intent.getStringExtra("key");
        userID = intent.getStringExtra("id");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("vehicles/" + userID + "/" + vehicleKey);
        ref = mFirebaseDatabase.getReference("appointments/" + userID + "/" + vehicleKey);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Vehicle vehicle = dataSnapshot.getValue(Vehicle.class);
                Log.d("vehicle: ", vehicle.getModel());
                final String ymm = vehicle.getYear() + " " + vehicle.getMake() + " " + vehicle.getModel();
                ymmTV.setText(ymm);
                appointmentTV.setText("Make an Appointment");
                appointmentTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent1 = new Intent(IndividualVehicleActivity.this, NativeAppointmentActivity.class);
                        intent1.putExtra("key", vehicleKey);
                        intent1.putExtra("ymm", ymm);
                        startActivity(intent1);
                    }
                });

                vehicleIMG.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Here, thisActivity is the current activity
                        if (ContextCompat.checkSelfPermission(IndividualVehicleActivity.this,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {

                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(IndividualVehicleActivity.this,
                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                                // Show an explanation to the user *asynchronously* -- don't block
                                // this thread waiting for the user's response! After the user
                                // sees the explanation, try again to request the permission.

                            } else {

                                // No explanation needed, we can request the permission.

                                ActivityCompat.requestPermissions(IndividualVehicleActivity.this,
                                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_WRITE);

                                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                // app-defined int constant. The callback method gets the
                                // result of the request.


                            }
                        } else {
                            dispatchTakePictureIntent();
                            galleryAddPic();

                        }

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabaseReference.addValueEventListener(valueEventListener);
        mAppointments = new ArrayList<>();
        mAppointments.clear();
        manager = new LinearLayoutManager(IndividualVehicleActivity.this);
        mAdapter = new AppointmentAdapter(IndividualVehicleActivity.this, mAppointments);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    Appointment appointment = dataSnapshot.getValue(Appointment.class);
                    Log.d("onChildadded called: ", appointment.productName + " " + appointment.getBookingNumber());
                    mAppointments.add(appointment);

                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAppointments.clear();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.d("File created : ", "true");
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("File not created : ", ex.toString() );
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.zombietechinc.rovingrepairs.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                    vehicleIMG.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("error: ", e.toString());
                }
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                    galleryAddPic();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
