package com.zombietechinc.rovingrepairs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http2.Header;

public class ProfileActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("users");
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    String userID;
    String name;
    String address;
    String number;
    EditText nameEt;
    EditText addressEt;
    EditText numberEt;
    Button savebtn;
    User mUser;
    TextView userIDTV;
    EditText lastNameET;
    String lastName;
    String customer = "customers?";
    String bookeoId;
    String data;
    String customerFullURL;
    ArrayList<String> idArrayList;
    String location;
    String location2;

    String headertext = "";
    String updateCustomerURL;
    OkHttpClient client2 = new OkHttpClient();


    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //client = new OkHttpClient.Builder().addNetworkInterceptor(new LoggingInterceptor()).build();
        mUser = new User();

        numberEt = (EditText)findViewById(R.id.number_et);
        nameEt = (EditText)findViewById(R.id.name_et);
        addressEt = (EditText)findViewById(R.id.address_et);
        savebtn = (Button)findViewById(R.id.savebtn);
        userIDTV = (TextView)findViewById(R.id.userid_tv);
        lastNameET = (EditText)findViewById(R.id.lastname_et);
        customerFullURL = getString(R.string.BOOKEO_URL) + customer + "&secretKey=" + getString(R.string.BOOKEO_SECRET_KEY) + "&apiKey=" + getString(R.string.BOOKEO_API_KEY_TWO);
        updateCustomerURL = getString(R.string.BOOKEO_URL) + "customers/"+ bookeoId + "?" + "/" + "&secretKey=" + getString(R.string.BOOKEO_SECRET_KEY) + "&apiKey=" + getString(R.string.BOOKEO_API_KEY_TWO);

        Log.d("Customers: ", customerFullURL);

        mAuth = FirebaseAuth.getInstance();
        Log.d("Current User: ", mAuth.getCurrentUser().getEmail());
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String userID = user.getUid();
                    Log.d("From listener: ", user.getEmail());
                    ref = database.getReference("users/" + userID);

                    //User is signed in stay on MainActivity
                }else {
                    //User is signed out - start LoginActivity

                    /*Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);*/
                }
            }
        };

        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        ref = database.getReference("users/" + userID);
        userIDTV.setText("User ID: " + userID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User newUser = dataSnapshot.getValue(User.class);
                    nameEt.setText(newUser.getName());
                    numberEt.setText(newUser.getContactnumber());
                    addressEt.setText(newUser.getAddress());
                    lastNameET.setText(newUser.getLastName());
                    bookeoId = newUser.getBookeoId();

                }else {
                    nameEt.setText("");
                    numberEt.setText("");
                    addressEt.setText("");
                    lastNameET.setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = String.valueOf(nameEt.getText());
                address = String.valueOf(addressEt.getText());
                number = String.valueOf(numberEt.getText());
                lastName = String.valueOf(lastNameET.getText());
                if (name.isEmpty() || address.isEmpty() || number.isEmpty() || lastName.isEmpty()){
                    Toast.makeText(ProfileActivity.this, "Please ensure all forms are filled!", Toast.LENGTH_SHORT).show();
                }else {
                    mUser.setName(name);
                    mUser.setAddress(address);
                    mUser.setContactnumber(number);
                    mUser.setLastName(lastName);
                    mUser.setEmailAddress(user.getEmail());
                    CreateCustomer createCustomer = new CreateCustomer();
                    if (bookeoId == "" || bookeoId == null) {

                        String createCustomerPost = createCustomer.createCustomerPost(mUser);
                        //Log.d("BookeoID before post: ", bookeoId);

                        try {

                            String response = post(customerFullURL, createCustomerPost);
                            //Log.d("Server response!!  ", response);
                            Log.d("Create Customer Post!! ", createCustomerPost);

                            Toast.makeText(ProfileActivity.this, "User Information Updated. Thank You!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                            startActivity(intent);
                            //String customers = getCustomers(customerFullURL);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } else {
                        Log.d("Bookio ID: ", bookeoId);
                        updateCustomerURL = getString(R.string.BOOKEO_URL) + "customers/"+ bookeoId + "?" + "/" + "&secretKey=" + getString(R.string.BOOKEO_SECRET_KEY) + "&apiKey=" + getString(R.string.BOOKEO_API_KEY_TWO);
                        UpdateCustomer updateCustomer = new UpdateCustomer();
                        String customerUpdatePost = updateCustomer.updateCustomerPost(mUser);
                        try {
                            Log.d("Update Customer Post  ", updateCustomerURL + " " + customerUpdatePost);
                            String response = put(updateCustomerURL, customerUpdatePost);
                            Toast.makeText(ProfileActivity.this, "User Information Updated. Thank You!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });



    }




        String getCustomers(String url){
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
                    try {
                        data = response.body().string();
                        Log.d("Customers! ", data);
                        if (response.isSuccessful()) {
                            JSONObject object = new JSONObject(data);
                            JSONArray customerData = object.getJSONArray("data");
                            idArrayList = new ArrayList<String>();
                            for (int i = 0; i < customerData.length(); i++){
                                JSONObject jsonObject = customerData.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                idArrayList.add(id);
                                bookeoId = idArrayList.get(0);
                                Log.d("User Bookeo ID:!! ", bookeoId);
                            }
                            Log.d("Array of ID! ", String.valueOf(idArrayList));

                                if (idArrayList.contains(bookeoId)){
                                    //Customer exists in bookeo
                                };

                        }


                    } catch (IOException e) {
                        Log.e("Error:", "#", e);
                    } catch (JSONException e) {
                        Log.e("Error:", "#", e);
                    }


                }
            });
            return data;
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

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "27839720-3ff4-6f11-5f1b-799e826cdd75")
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Response response = client.newCall(request).execute();
                response = client.newCall(request).execute();
                data = response.body().string();
                data = response.headers().toString();
                location = response.header("Location");
                location2 = response.header("Location", "URI");
                String other = response.request().url().toString();

                Headers headers = response.headers();
                ArrayList<Pair<String, String>> headersList = new ArrayList<>();
                for (int i = 0, j = headers.size(); i < j; i++) {
                    headersList.add(Pair.create(headers.name(i), headers.value(i)));
                }

                Log.d("Data from header", data);
                Log.d("Header stuff  :", String.valueOf(headersList));
                bookeoId = location.substring(36);
                mUser.setBookeoId(bookeoId);
                Log.d("Bookio ID: ", bookeoId);
                Log.d("THIS IS USER INFO: ", mUser.getName() + mUser.getBookeoId());
                ref.setValue(mUser);







                Log.d("Response Bitch!", location + " " + bookeoId);
            }
        });
        return data;
    }

    String put(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        final Request request = new Request.Builder()
                .url(url)
                .put(body)
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
                data = response.body().string();
                data = response.headers().toString();
                Log.d("Customer Updated: ", data + response.code() + response.body().toString());
                mUser.setBookeoId(bookeoId);
                ref.setValue(mUser);
            }
        });
        return data;
    }
}
