package com.zombietechinc.rovingrepairs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SelectModelActivity extends AppCompatActivity {

    String make;
    String year;
    ArrayAdapter<String> arrayAdapter;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_model);
        Intent intent = getIntent();
        make = intent.getStringExtra("make");
        year = intent.getStringExtra("year");
        lv = (ListView) findViewById(R.id.model_list);
        getModels();




    }

    private void getModels(){
        String newMake = make.replaceAll("\\s+","");
        String modelsUrl = "https://vpic.nhtsa.dot.gov/api/vehicles/getmodelsformakeyear/make/" + newMake + "/modelyear/" + year + "?format=json";

        Log.d("new make: ", newMake);
        Log.d("url:", modelsUrl);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(modelsUrl).build();
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
                        JSONArray models = results.getJSONArray("Results");
                        //Log.d("some models", models.toString());

                        ArrayList<String> modelList = new ArrayList<String>();
                        for (int i = 0; i < models.length(); i++) {
                            JSONObject json = models.getJSONObject(i);
                            int id = json.getInt("Model_ID");
                            String model = json.getString("Model_Name");
                            modelList.add(model);
                            Log.d("Models", model);
                            SortArrayList sortArrayList = new SortArrayList(modelList);
                            modelList = sortArrayList.sortAscending();



                        }

                        arrayAdapter = new ArrayAdapter<String>(SelectModelActivity.this, android.R.layout.simple_list_item_1, modelList);

                        final ArrayList<String> finalModellist = modelList;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                lv.setAdapter(arrayAdapter);
                                arrayAdapter.notifyDataSetChanged();

                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        String vehicleModel = finalModellist.get(i);
                                        Log.d("Vehicle Model: ", vehicleModel);
                                        Intent intent = new Intent(SelectModelActivity.this, VehicleConfirmationActivity.class);
                                        intent.putExtra("make", make);
                                        intent.putExtra("year", year);
                                        intent.putExtra("model", vehicleModel);
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
