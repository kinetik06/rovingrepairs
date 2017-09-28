package com.zombietechinc.rovingrepairs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by User on 9/18/2017.
 */

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>{

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rv;
        TextView serviceName;
        TextView serviceTime;


        AppointmentViewHolder(View itemView) {
            super(itemView);
            rv = (RecyclerView) itemView.findViewById(R.id.appointment_rv);
            serviceName = (TextView)itemView.findViewById(R.id.service_name_tv);
            serviceTime = (TextView)itemView.findViewById(R.id.service_time_tv);

        }
    }

    List<Appointment> appointments;
    private LayoutInflater inflater;
    Appointment mAppointment;
    String serviceType;


    AppointmentAdapter(Context context, List<Appointment> appointments){
        inflater = LayoutInflater.from(context);
        this.appointments = appointments;
        mAppointment = new Appointment();
    }
    @Override
    public AppointmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.appointment_card, parent, false);
        AppointmentViewHolder holder = new AppointmentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AppointmentViewHolder holder, int position) {
        mAppointment = appointments.get(position);
        RFC3339Date rfc3339Date = new RFC3339Date();
        try {
            Date parsedTime = rfc3339Date.parseRFC3339Date(mAppointment.getStartTime());
            SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy", Locale.US);
            SimpleDateFormat format1 = new SimpleDateFormat("H:mm a", Locale.US);
            String formattedDate = format.format(parsedTime);
            String formattedTime = format1.format(parsedTime);
            holder.serviceName.setText(mAppointment.getProductName());
            holder.serviceTime.setText(formattedDate + " at " + formattedTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }




    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }


}
