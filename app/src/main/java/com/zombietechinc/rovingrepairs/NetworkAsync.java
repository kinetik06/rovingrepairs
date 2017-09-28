package com.zombietechinc.rovingrepairs;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by User on 9/12/2017.
 */

public class NetworkAsync extends AsyncTask<String, String, String> {

    private String resp;
    ProgressDialog progressDialog;

    @Override
    protected String doInBackground(String... params) {
        String headertext = "";
        OkHttpClient client2 = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{ \"firstName\" : \"Bob \",\r\n\t\"lastName\" : \" Ross\" , \r\n    \"emailAddress\" : \"rovingrepairs@gmail.com\" , \r\n \"streetAdress\": [{ \"address1\": \"3331 The Loop Rd. 23231\"}], \r\n\"phoneNumbers\" : [{ \"number\" : \"7849365\" , \"type\" : \"mobile\" }] } ");
        Request request = new Request.Builder()
                .url("https://api.bookeo.com/v2/customers?=&secretKey=ndtknLQ1AFYOg38OBqU1FfYH7xMRLohJ&apiKey=AMRPTYEF9FC7UJ9XKYFXE41564PPF9JK15E53A1D0A5")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "27839720-3ff4-6f11-5f1b-799e826cdd75")
                .build();

        try {
            Response response = client2.newCall(request).execute();
            headertext = response.headers().toString();
            Log.d("Headers:  ", headertext);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return headertext;
    }


    @Override
    protected void onPostExecute(String result) {


    }


    @Override
    protected void onPreExecute() {


    }


    @Override
    protected void onProgressUpdate(String... text) {


    }
}

