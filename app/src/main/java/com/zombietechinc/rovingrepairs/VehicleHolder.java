package com.zombietechinc.rovingrepairs;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by User on 10/2/2017.
 */

public class VehicleHolder extends RecyclerView.ViewHolder {

    TextView vehicleTV;
    TextView mileageTV;
    TextView vinTV;
    ImageView vehicleimg;
    View mView;


    public VehicleHolder(View itemView) {
        super(itemView);
        mView = itemView;
        vehicleTV = (TextView)itemView.findViewById(R.id.vehicle_tv);
        //mileageTV = (TextView)itemView.findViewById(R.id.mileage_tv);
        vehicleimg = (ImageView)itemView.findViewById(R.id.vehicleimg);
        //vinTV =(TextView)itemView.findViewById(R.id.vin_tv);
    }

}