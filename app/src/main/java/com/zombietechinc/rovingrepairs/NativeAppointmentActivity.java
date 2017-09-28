package com.zombietechinc.rovingrepairs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NativeAppointmentActivity extends AppCompatActivity {

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
    ArrayList<Service> serviceList;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    private LinearLayoutManager manager;
    String vehicleKey;
    String ymm;
    String customer = "customers?";
    String webHooks = "webhooks?";
    String webHookURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_appointment);
        Intent intent = getIntent();
        vehicleKey = intent.getStringExtra("key");
        ymm = intent.getStringExtra("ymm");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ", Locale.US);
        Date date = new Date();
        Date date2 = new Date(date.getTime() + 604800000L);
        RequestAvailableSlots availableSlots = new RequestAvailableSlots();

        final String first = String.valueOf(simpleDateFormat.format(date));
        final String second = String.valueOf(simpleDateFormat.format(date2));
        tryPost = availableSlots.getavailableslots(diagID,first,second);
        diagFullURL = getString(R.string.BOOKEO_URL) + matchingSlots + "&secretKey=" + getString(R.string.BOOKEO_SECRET_KEY) + "&apiKey=" + getString(R.string.BOOKEO_API_KEY_TWO);
        servicesFullURL = getString(R.string.BOOKEO_URL) + services + "&secretKey=" + getString(R.string.BOOKEO_SECRET_KEY) + "&apiKey=" + getString(R.string.BOOKEO_API_KEY_TWO);
        String customerFullURL = getString(R.string.BOOKEO_URL) + customer + "&secretKey=" + getString(R.string.BOOKEO_SECRET_KEY) + "&apiKey=" + getString(R.string.BOOKEO_API_KEY_TWO);
        webHookURL = getString(R.string.BOOKEO_URL) + webHooks + "&secretKey=" + getString(R.string.BOOKEO_SECRET_KEY) + "&apiKey=" + getString(R.string.BOOKEO_API_KEY_TWO);
        String customerwebhook = "{\"id\": \"string\",\"url\": \"https://us-central1-roving-repairs-ac772.cloudfunctions.net/helloWorld\",\"domain\": \"customers\",\"type\": \"created\",\"blockedTime\": \"\",\"blockedReason\": \"\"}";
        /*try {
            String post = post(webHookURL, customerwebhook);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Log.d("Customers: ", customerFullURL);
        //        https://api.bookeo.com/v2/availability/matchingslots?&search=MatchingSlotsSearchParameters{}&startTime=2017-09-06T11:06:37.046-04:00&secretKey=ndtknLQ1AFYOg38OBqU1FfYH7xMRLohJ&productId=41564RYA3XN15E53A5319E_UEXXJKKY&endTime=2017-09-07T11:06:37.046-04:00&apiKey=APTHC7639FC7UJ9XKYFXE41564PPF9JK15E53A1D0A5
        Log.d("WebHookURL: ", webHookURL);
        final String jsonHeader = "GET " + diagFullURL + " " + "HTTP/1.1\n" +
                "Host: api.bookeo.com\n" +
                "Connection: Keep-Alive\n" +
                "Accept-Encoding: gzip,deflate\n" +
                "\n" +
                "HTTP/1.1 200 OK" + "\n" + "Date: " + String.valueOf(date) + "\n" + "Expires: Fri, 01 Jan 1990 00:00:00 GMT\n" +
                "Cache-Control: private, max-age=0, no-cache, no-store\n" +
                "Pragma: no-cache\n" +
                "Content-Language: en-US\n" +
                "Content-Type: application/json\n" +
                "Vary: Accept-Encoding, User-Agent\n" +
                "Content-Length: " + tryPost.length() +
                "\n" + tryPost;
        mRecyclerView = (RecyclerView)findViewById(R.id.services_rv);


        getServices(servicesFullURL);

        /*try {
            String response = post(diagFullURL, tryPost);

        } catch (IOException e) {
            e.printStackTrace();

        }*/

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
            }
        });
        return data;
    }

    String getServices(String url) {
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
                    Log.d("Services! ", data);
                    if (response.isSuccessful()){
                        JSONObject object = new JSONObject(data);
                        JSONArray serviceData = object.getJSONArray("data");
                        Log.d("Data", String.valueOf(serviceData));
                        serviceList = new ArrayList<Service>();
                        for (int i = 0; i < serviceData.length(); i++){
                            JSONObject jsonObject = serviceData.getJSONObject(i);
                            String name = jsonObject.getString("name");
                            String description = jsonObject.getString("description");
                            String productId = jsonObject.getString("productId");
                            /*String price = "40";
                            String duration = "1 hour";*/

                            JSONArray rates = jsonObject.getJSONArray("defaultRates");
                            String price = getprice(rates);
                            JSONObject times = jsonObject.getJSONObject("duration");
                            String duration = getduration(times);

                            Service service = new Service(name, description, productId, price, duration);
                            Log.d("Service name: ", service.getName());
                            Log.d("Service Price: ", service.getPrice());
                            serviceList.add(service);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    manager = new LinearLayoutManager(NativeAppointmentActivity.this);
                                    mRecyclerView.setLayoutManager(manager);
                                    mAdapter = new ServiceAdapter(NativeAppointmentActivity.this, serviceList);

                                    mRecyclerView.setAdapter(mAdapter);
                                    mAdapter.notifyDataSetChanged();

                                }
                            });
                            ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                                @Override
                                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                    Service service1 = serviceList.get(position);
                                    String id = service1.getProductId();
                                    Log.d("Service clicked: ", id + " " + service1.getName());
                                    Intent intent = new Intent(NativeAppointmentActivity.this, DateTimeActivity.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("name", service1.getName());
                                    intent.putExtra("key",vehicleKey);
                                    intent.putExtra("ymm", ymm);
                                    startActivity(intent);
                                }
                            });


                        }



                    }
                }catch (IOException e){
                    Log.e("Error:", "#", e);
                } catch (JSONException e) {
                    Log.e("Error:", "#", e);
                }


            }
        });
        return data;
    }

    private String getprice(JSONArray jsonArray) throws JSONException {
        String price = "";
        for (int a = 0; a < jsonArray.length(); a++){
            JSONObject newobject = jsonArray.getJSONObject(a);
            Log.d("Json price: ", String.valueOf(newobject));
            JSONObject price1 = newobject.getJSONObject("price");
            Log.d("PRICE", String.valueOf(price1));
            price = price1.getString("amount");
            Log.d("FINAL PRICE:  ", price);
        }
        return price;
    }

    private String getduration(JSONObject jsonObject) throws JSONException {
        String duration = "";
        int hours = jsonObject.getInt("hours");
        int minutes = jsonObject.getInt("minutes");

            duration = String.valueOf(hours) + "hours " + String.valueOf(minutes);

        return duration;
    }
}
