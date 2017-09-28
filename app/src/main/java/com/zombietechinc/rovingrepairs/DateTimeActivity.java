package com.zombietechinc.rovingrepairs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DateTimeActivity extends AppCompatActivity {

    String serviceName;
    String serviceID;
    String BOOKEO_END_URL = "?secretKey=ndtknLQ1AFYOg38OBqU1FfYH7xMRLohJ&apiKey=A9KTCENYKFC7UJ9XKYFXE41564PPF9JK15E53A1D0A5";
    Button logButton;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    String diagID = "41564RYA3XN15E53A5319E_UEXXJKKY";
    String tryPost;
    String diagFullURL;
    String matchingSlots = "availability/matchingslots?";
    String data;
    String services = "settings/products?";
    String servicesFullURL;
    ArrayList<AppointmentSlot> slotArrayList;
    ListView timelv;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    private LinearLayoutManager manager;
    String vehicleKey;
    String ymm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);
        Intent intent = getIntent();
        serviceName = intent.getStringExtra("name");
        serviceID = intent.getStringExtra("id");
        vehicleKey = intent.getStringExtra("key");
        ymm = intent.getStringExtra("ymm");
        slotArrayList= new ArrayList<>();

        mRecyclerView = (RecyclerView)findViewById(R.id.datetime_rv);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ", Locale.US);
        Date date = new Date();
        Date date2 = new Date(date.getTime() + 604800000L);
        RequestAvailableSlots availableSlots = new RequestAvailableSlots();

        final String first = String.valueOf(simpleDateFormat.format(date));
        final String second = String.valueOf(simpleDateFormat.format(date2));
        tryPost = availableSlots.getavailableslots(serviceID,first,second);
        diagFullURL = getString(R.string.BOOKEO_URL) + matchingSlots + "&secretKey=" + getString(R.string.BOOKEO_SECRET_KEY) + "&apiKey=" + getString(R.string.BOOKEO_API_KEY_TWO);
        servicesFullURL = getString(R.string.BOOKEO_URL) + services + "&secretKey=" + getString(R.string.BOOKEO_SECRET_KEY) + "&apiKey=" + getString(R.string.BOOKEO_API_KEY_TWO);

        try {
            String response = post(diagFullURL, tryPost);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
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
                Log.d("Response Bitch!", data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    if (response.isSuccessful()){

                        JSONArray serviceData = jsonObject.getJSONArray("data");
                        Log.d("Data", String.valueOf(serviceData));
                        for (int i = 0; i < serviceData.length(); i++){
                            JSONObject jsonObject1 = serviceData.getJSONObject(i);
                            String startTime = jsonObject1.getString("startTime");
                            String endTime = jsonObject1.getString("endTime");
                            String eventId = jsonObject1.getString("eventId");

                            AppointmentSlot slot = new AppointmentSlot(startTime,endTime,eventId);
                            slotArrayList.add(slot);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    manager = new LinearLayoutManager(DateTimeActivity.this);
                                    mRecyclerView.setLayoutManager(manager);
                                    AppointmentSlotAdapter arrayAdapter = new AppointmentSlotAdapter(DateTimeActivity.this, slotArrayList);

                                    mRecyclerView.setAdapter(arrayAdapter);
                                    arrayAdapter.notifyDataSetChanged();
                                    mRecyclerView.setHasFixedSize(true);
                                }
                            });
                        }

                        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                            @Override
                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                AppointmentSlot slot = slotArrayList.get(position);
                                String eventId = slot.getEventId();
                                Log.d("Event ID: ", eventId);
                                String time = slot.getStartTime();
                                Intent intent = new Intent(DateTimeActivity.this, ConfirmAppointmentActivity.class);
                                intent.putExtra("time", time);
                                intent.putExtra("id", serviceID);
                                intent.putExtra("key", vehicleKey);
                                intent.putExtra("ymm", ymm);
                                intent.putExtra("name", serviceName);
                                intent.putExtra("eventId", eventId);
                                startActivity(intent);
                            }
                        });



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return data;
    }

}
