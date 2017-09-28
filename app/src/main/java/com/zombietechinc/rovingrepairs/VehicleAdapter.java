package com.zombietechinc.rovingrepairs;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by User on 9/1/2017.
 */

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>{



    public static class VehicleViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rv;
        TextView vehicleTV;
        TextView mileageTV;
        TextView vinTV;
        ImageView vehicleimg;


        VehicleViewHolder(View itemView) {
            super(itemView);
            rv = (RecyclerView) itemView.findViewById(R.id.vehicle_rv);
            vehicleTV = (TextView)itemView.findViewById(R.id.vehicle_tv);
            mileageTV = (TextView)itemView.findViewById(R.id.mileage_tv);
            vehicleimg = (ImageView)itemView.findViewById(R.id.vehicleimg);
            vinTV =(TextView)itemView.findViewById(R.id.vin_tv);

        }




    }

    List<Vehicle> vehicles;
    List<Appointment> appointments;
    private LayoutInflater inflater;
    Vehicle mVehicle;
    Appointment mAppointment;
    String vehicleType;
    OkHttpClient client = new OkHttpClient();
    String vehicleImgURL = "http://www.carimagery.com/api.asmx/GetImageUrl?searchTerm=";
    String imgURL = "";

    VehicleAdapter(Context context, List<Vehicle> vehicles){
        inflater = LayoutInflater.from(context);
        this.vehicles = vehicles;
        mVehicle = new Vehicle();
    }

    VehicleAdapter(Context context, List<Vehicle> vehicles, List<Appointment> appointments){
        inflater = LayoutInflater.from(context);
        this.vehicles = vehicles;
        this.appointments = appointments;
        mVehicle = new Vehicle();
        mAppointment = new Appointment();

    }


    @Override
    public VehicleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.vehicle_card, parent, false);
        VehicleViewHolder holder = new VehicleViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(VehicleViewHolder holder, int position) {
        mVehicle = vehicles.get(position);
        vehicleType = String.valueOf(mVehicle.getYear()) + " " + mVehicle.getMake()
                + " " + mVehicle.getModel();
        vehicleImgURL += mVehicle.getMake() + "+" + mVehicle.getModel();
        holder.vehicleTV.setText(vehicleType);
        holder.vehicleimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Vehicle img touched: ", mVehicle.getModel());
            }
        });

        final Request request = new Request.Builder()
                .url(vehicleImgURL)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    imgURL = response.body().string();
                    //Log.d("IMG LOCATION: ", imgURL);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }


}