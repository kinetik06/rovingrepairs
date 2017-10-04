package com.zombietechinc.rovingrepairs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView pickvehicle;
    ListView lv;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("vehicles");
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    List <Vehicle> mVehicleList;
    private LinearLayoutManager manager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    String vehicleKey;
    String userID;
    DatabaseReference ref2 = database.getReference("users");
    String bookeoID = "";
    String customers = "customers/";
    String bookings = "/bookings?";
    String allBookingsURL;
    OkHttpClient client = new OkHttpClient();
    String customerBookings;
    ArrayList<Appointment> mAppointments;
    FirebaseRecyclerAdapter firebaseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVehicleList = new ArrayList<>();

        mRecyclerView = (RecyclerView)findViewById(R.id.vehicle_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null ) {
            FirebaseUser user = mAuth.getCurrentUser();
            userID = user.getUid();
            ref = database.getReference("vehicles/" + userID);


            Log.d("From base code: ", user.getUid());
        }else {

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        //Set up the listener to see if user is logged in
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    userID = user.getUid();
                    Log.d("From listener: ", user.getEmail());
                    ref = database.getReference("vehicles/" + userID);

                    firebaseAdapter = new FirebaseRecyclerAdapter <Vehicle, VehicleHolder>
                            (Vehicle.class, R.layout.vehicle_card, VehicleHolder.class, ref ) {

                        @Override
                        protected void populateViewHolder(VehicleHolder holder, final Vehicle vehicle, int position) {
                            holder.vehicleTV.setText(String.valueOf(vehicle.getYear()) + " " + vehicle.getMake()
                                    + " " + vehicle.getModel());
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference vehicleImgRef = storage.getReference("vehicles/" + vehicle.getUniqueKey() + ".jpg");
                            holder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.d("Vehicle Touched: ", vehicle.getModel());
                                    vehicleKey = vehicle.getUniqueKey();
                                    String name = vehicle.getModel();
                                    Toast.makeText(MainActivity.this, vehicleKey + " " + name, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, IndividualVehicleActivity.class);
                                    intent.putExtra("key", vehicleKey);
                                    intent.putExtra("id", userID);
                                    startActivity(intent);
                                }
                            });
                            Glide.with(MainActivity.this).using(new FirebaseImageLoader()).load(vehicleImgRef).error(R.drawable.car_holder).into(holder.vehicleimg);

                        }
                    };

                    mRecyclerView.setAdapter(firebaseAdapter);

                    ref2 = database.getReference("users/" + userID);
                    ref2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()){
                                Toast.makeText(MainActivity.this, "Let's get some basic information first.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                startActivity(intent);
                            }else {
                                User user = dataSnapshot.getValue(User.class);
                                bookeoID = user.getBookeoId();
                                Log.d("Bookio ID:  ", bookeoID);
                                allBookingsURL = getString(R.string.BOOKEO_URL) + customers + bookeoID + bookings + "&secretKey=" + getString(R.string.BOOKEO_SECRET_KEY) + "&apiKey=" + getString(R.string.BOOKEO_API_KEY_TWO);
                                String allbookings = getCustomerBookings(allBookingsURL);



                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        /*FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        ref = database.getReference("users/" + userID + "/vehicles");*/



                    mVehicleList.clear();
                    /*ChildEventListener childEventListener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (dataSnapshot.getValue() != null) {
                                Vehicle vehicle = dataSnapshot.getValue(Vehicle.class);
                                Log.d("stuff", vehicle.getMake());
                                Log.d("stuff", vehicle.getModel());
                                Log.d("stuff", String.valueOf(vehicle.getYear()));

                                mVehicleList.add(vehicle);
                                manager = new LinearLayoutManager(MainActivity.this);

                                mAdapter = new VehicleAdapter(MainActivity.this, mVehicleList);

                                mRecyclerView.setLayoutManager(manager);
                                mRecyclerView.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                                ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                                    @Override
                                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                        Vehicle vehicle1 = mVehicleList.get(position);
                                        vehicleKey = vehicle1.getUniqueKey();
                                        String name = vehicle1.getModel();
                                        Toast.makeText(MainActivity.this, vehicleKey + " " + name, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, IndividualVehicleActivity.class);
                                        intent.putExtra("key", vehicleKey);
                                        intent.putExtra("id", userID);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            mAdapter.notifyDataSetChanged();
                            Log.d("On Change: ", dataSnapshot.getKey());
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
                    };*/
        /*ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Vehicle vehicle = dataSnapshot.getValue(Vehicle.class);
                Log.d("stuff" , vehicle.getMake() );
                mVehicleList.add(dataSnapshot.getValue(Vehicle.class));

                manager = new LinearLayoutManager(MainActivity.this);

                mAdapter = new AppointmentSlotAdapter(MainActivity.this, mVehicleList);
                mRecyclerView.setLayoutManager(manager);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Vehicle vehicle1 = mVehicleList.get(position);

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };*/
                    //ref.addChildEventListener(childEventListener);


                    //User is signed in stay on MainActivity
                }else {
                    //User is signed out - start LoginActivity

                    /*Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);*/
                }
            }
        };

        //mAuth.addAuthStateListener(mAuthStateListener);


        pickvehicle = (TextView)findViewById(R.id.pick_vehicle);
        pickvehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SelectVehicleActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mVehicleList.clear();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.signout:
                mAuth.signOut();
                mVehicleList.clear();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.editprofile:
                Intent intent1 = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent1);
                return true;
            case R.id.make_appointment:
                Intent intent2 = new Intent(MainActivity.this, NativeAppointmentActivity.class);
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    String getCustomerBookings (String url) {

        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                customerBookings = response.body().string();
                Log.d("Customer Booking: ", customerBookings);
                if (response.isSuccessful()){

                    try {
                        JSONObject jsonObject = new JSONObject(customerBookings);
                        JSONArray bookingArray = jsonObject.getJSONArray("data");
                        mAppointments = new ArrayList<Appointment>();
                        for (int i = 0; i < bookingArray.length(); i++){
                            JSONObject jsonObject1 = bookingArray.getJSONObject(i);
                            Appointment appointment = new Appointment();
                            appointment.setBookingNumber(jsonObject1.getString("bookingNumber"));
                            appointment.setStartTime(jsonObject1.getString("startTime"));
                            appointment.setTitle(jsonObject1.getString("title"));
                            appointment.setProductName(jsonObject1.getString("productName"));

                            Log.d("Apppointment Stuff :  ", appointment.getBookingNumber() + appointment.getProductName());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return customerBookings;
    }

    }


