package com.zombietechinc.rovingrepairs;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConfirmAppointmentActivity extends AppCompatActivity {

    String ymm;
    String vehicleKey;
    String serviceID;
    String serviceTime;
    String serviceName;
    TextView confirmTV;
    TextView serviceNameTV;
    TextView serviceTimeTV;
    Date parsedTime;
    String formattedTime;
    String formattedDate;
    Button buttonYes;
    Button buttonNo;
    String bookings = "bookings?";
    String bookingURL;
    String data;
    String userEmail;
    String eventId;
    String firstName;
    String lastName;
    String userID;
    String address;
    String phoneNumber;
    String postBody;
    String bookioID;
    String bookingNumber;
    String singleBookingIDURL;
    String bookings2 = "bookings/";
    String response = "";
    String bookingDetail = "";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("users");
    DatabaseReference ref2 = database.getReference("vehicles");
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_appointment);

        Intent intent = getIntent();
        bookingURL = getString(R.string.BOOKEO_URL) + bookings + "&secretKey=" + getString(R.string.BOOKEO_SECRET_KEY) + "&apiKey=" + getString(R.string.BOOKEO_API_KEY_TWO) + "&notifyUsers=true&notifyCustomer=true";

        ymm = intent.getStringExtra("ymm");
        vehicleKey = intent.getStringExtra("key");
        serviceID = intent.getStringExtra("id");
        serviceTime = intent.getStringExtra("time");
        serviceName = intent.getStringExtra("name");
        eventId = intent.getStringExtra("eventId");
        RFC3339Date rfc3339Date = new RFC3339Date();
        try {
            parsedTime = rfc3339Date.parseRFC3339Date(serviceTime);
            SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy", Locale.US);
            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm a", Locale.US);
            formattedDate = format.format(parsedTime);
            formattedTime = format1.format(parsedTime);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        confirmTV = (TextView)findViewById(R.id.confirmtv);
        confirmTV.setText("Would you like to confirm your appointment for a " + serviceName + " on your " + ymm + " on " + formattedDate + " at " + formattedTime + "?" );
        buttonNo = (Button)findViewById(R.id.buttonNo);
        buttonYes = (Button)findViewById(R.id.buttonYes);



        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    userID = user.getUid();

                    ref = database.getReference("users/" + userID);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user1 = dataSnapshot.getValue(User.class);
                            firstName = user1.getName();
                            lastName = user1.getLastName();
                            userEmail = user1.getEmailAddress();
                            address = user1.getAddress();
                            phoneNumber = user1.getContactnumber();
                            bookioID = user1.getBookeoId();
                            if (bookioID == null) {
                                Toast.makeText(ConfirmAppointmentActivity.this, "Let's get you registered first", Toast.LENGTH_SHORT);
                                Intent intent1 = new Intent(ConfirmAppointmentActivity.this, ProfileActivity.class);
                                startActivity(intent1);
                            }
                            BookAppointment bookAppointment = new BookAppointment();
                            //postBody = bookAppointment.getPostBody(eventId, firstName, lastName, userEmail, phoneNumber, serviceID, address );
                            postBody = bookAppointment.getPostwithID(eventId, serviceID, bookioID);
                            Log.d("Post Body: ", postBody);
                            buttonNo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(ConfirmAppointmentActivity.this, "No Problem, Let's head back to the garage!", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(ConfirmAppointmentActivity.this, MainActivity.class);
                                    startActivity(intent1);
                                }
                            });

                            buttonYes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        response = post(bookingURL, postBody);


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(ConfirmAppointmentActivity.this, "Great! We'll see you on " + formattedDate + " at " + formattedTime + "!", Toast.LENGTH_LONG).show();
                                    Intent intent1 = new Intent(ConfirmAppointmentActivity.this, MainActivity.class);
                                    startActivity(intent1);
                                    Log.d("The Booking URL", bookingURL);

                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else {
                    Intent intent = new Intent(ConfirmAppointmentActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };



    }

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    data = response.body().string();
                    String header = response.headers().toString();

                    String location = response.header("Location");
                    bookingNumber = location.substring(35);
                    Log.d("Response Bitch!", header + bookingNumber);
                    singleBookingIDURL = getString(R.string.BOOKEO_URL) + bookings2 + bookingNumber + "?" + "&secretKey=" + getString(R.string.BOOKEO_SECRET_KEY) + "&apiKey=" + getString(R.string.BOOKEO_API_KEY_TWO) + "&notifyUsers=true&notifyCustomer=true";
                    getSingleBooking(singleBookingIDURL);

                }
            }
        });
        return bookingNumber;
    }

    String getSingleBooking (String bookingURL){
        Request request = new Request.Builder()
                .url(bookingURL)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String bookingDetail = response.body().string();
                    Log.d("Booking Details : ", bookingDetail);
                    ref2 = database.getReference("appointments/" + userID + "/" + vehicleKey);
                    try {
                        JSONObject jsonObject = new JSONObject(bookingDetail);
                        Appointment appointment = new Appointment();
                        appointment.setBookingNumber(jsonObject.getString("bookingNumber"));
                        appointment.setProductName(jsonObject.getString("productName"));
                        appointment.setTitle(jsonObject.getString("title"));
                        appointment.setStartTime(jsonObject.getString("startTime"));

                        ref2.push().setValue(appointment);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        return bookingDetail;
    }


    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
