package com.zombietechinc.rovingrepairs;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.RunnableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SelectVehicleActivity extends AppCompatActivity {

    TextView data;
    ListView lv;
    ArrayAdapter<String> arrayAdapter;
    List<String> smallMakeList;
    String[] arrayofSmallMakes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_vehicle);
        lv = (ListView) findViewById(R.id.make_list);

        Resources resources = getResources();

        final List<String> newVehicleMakes = Arrays.asList(getResources().getStringArray(R.array.vehicle_makes));

        /*for (int i = 0; i < arrayofSmallMakes.length; i++){
            Log.d("Short Vehicle makes", arrayofSmallMakes[i]);
            smallMakeList.add(arrayofSmallMakes[i]);
        };*/
        /*SortArrayList sortArrayList = new SortArrayList(smallMakeList);
        smallMakeList = sortArrayList.sortDescending();*/
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, newVehicleMakes);
        lv.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        final List<String> finalMakes = smallMakeList;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String make = newVehicleMakes.get(i);
                Log.d("Vehicle Year: ", String.valueOf(make));
                Intent intent1 = new Intent(SelectVehicleActivity.this, SelectYearActivity.class);
                String yearString = String.valueOf(make);
                intent1.putExtra("make", make);
                startActivity(intent1);
            }
        });
        //getVehiclesMakes();



    }

    private void getVehiclesMakes() {

        String makesUrl = "https://vpic.nhtsa.dot.gov/api/vehicles/getallmakes?format=json";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(makesUrl).build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                alertUserAboutError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    Log.v("THis is the data", jsonData);
                    if (response.isSuccessful()) {
                        JSONObject results = new JSONObject(jsonData);
                        JSONArray makes = results.getJSONArray("Results");
                        //Log.d("some makes", makes.toString());

                        ArrayList<String>makelist = new ArrayList<String>();
                        for (int i = 0; i < makes.length(); i++) {
                            JSONObject json = makes.getJSONObject(i);
                            int id = json.getInt("Make_ID");
                            String make = json.getString("Make_Name");
                            makelist.add(make);
                            Log.d("Makes", make);
                            SortArrayList sortArrayList = new SortArrayList(makelist);
                            makelist = sortArrayList.sortAscending();



                        }

                        arrayAdapter = new ArrayAdapter<String>(SelectVehicleActivity.this, android.R.layout.simple_list_item_1, makelist);

                        final ArrayList<String> finalMakelist = makelist;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                lv.setAdapter(arrayAdapter);
                                arrayAdapter.notifyDataSetChanged();

                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        String vehicleMake = finalMakelist.get(i);
                                        Log.d("Vehicle Make: ", vehicleMake);
                                        Intent intent = new Intent(SelectVehicleActivity.this, SelectYearActivity.class);
                                        intent.putExtra("make", vehicleMake);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });


                    }else{
                        alertUserAboutError();
                    }


                }catch (IOException e) {
                    Log.e("Error:","#", e);
                } catch (JSONException e) {
                    Log.e("Error:", "#", e);
                }
            }
        });


    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }
}



