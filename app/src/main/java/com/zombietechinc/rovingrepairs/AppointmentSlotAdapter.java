package com.zombietechinc.rovingrepairs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zombietechinc.rovingrepairs.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AppointmentSlotAdapter extends RecyclerView.Adapter<AppointmentSlotAdapter.VehicleViewHolder>{

    public static class VehicleViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rv;
        TextView startTimeTV;
        TextView endTimeTV;
        TextView dayTV;
        TextView monthTV;


        VehicleViewHolder(View itemView) {
            super(itemView);
            rv = (RecyclerView) itemView.findViewById(R.id.datetime_rv);
            startTimeTV = (TextView)itemView.findViewById(R.id.startTime_tv);
            endTimeTV= (TextView)itemView.findViewById(R.id.endTime_tv);
            dayTV = (TextView) itemView.findViewById(R.id.day_tv);
            monthTV =(TextView)itemView.findViewById(R.id.month_tv);
        }
    }

    List<AppointmentSlot> mAppointmentSlots;
    private LayoutInflater inflater;
    AppointmentSlot mSlot;
    String ApptType;


    AppointmentSlotAdapter(Context context, List<AppointmentSlot> appointmentSlots){
        inflater = LayoutInflater.from(context);
        this.mAppointmentSlots = appointmentSlots;
        mSlot = new AppointmentSlot();
    }
    @Override
    public VehicleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.datetime_card, parent, false);
        VehicleViewHolder holder = new VehicleViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(VehicleViewHolder holder, int position) {
        mSlot = mAppointmentSlots.get(position);
        holder.startTimeTV.setText(mSlot.getStartTime());
        holder.endTimeTV.setText(mSlot.getEndTime());
        SimpleDateFormat format = new SimpleDateFormat("h:mm a", Locale.US);
        SimpleDateFormat format1 = new SimpleDateFormat("d", Locale.US);
        SimpleDateFormat format2 = new SimpleDateFormat("M", Locale.US);
        try {

            RFC3339Date rfc3339Date = new RFC3339Date();


            Date startDate = rfc3339Date.parseRFC3339Date(mSlot.getStartTime());
            Date endDate = rfc3339Date.parseRFC3339Date(mSlot.getEndTime());
            SimpleDateFormat parser = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
            Date startTime = parser.parse(String.valueOf(startDate));
            Date endTime = parser.parse(String.valueOf(endDate));
            String day = format1.format(startTime);
            String month = format2.format(startTime);

            String laststart = format.format(startTime);
            String lastend = format.format(endTime);
            holder.startTimeTV.setText(laststart);
            holder.endTimeTV.setText(lastend);
            holder.dayTV.setText(day + '/' + month);
            holder.monthTV.setText("");

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mAppointmentSlots.size();
    }


}
