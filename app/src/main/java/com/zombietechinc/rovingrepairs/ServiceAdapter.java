package com.zombietechinc.rovingrepairs;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by User on 9/1/2017.
 */

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>{

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rv;
        TextView serviceTV;
        TextView descriptionTV;
        TextView priceTV;
        ImageView vehicleimg;
        TextView duration;

        ServiceViewHolder(View itemView) {
            super(itemView);
            rv = (RecyclerView) itemView.findViewById(R.id.services_rv);
            serviceTV = (TextView)itemView.findViewById(R.id.service_tv);
            descriptionTV = (TextView)itemView.findViewById(R.id.description_tv);
            //vehicleimg = (ImageView)itemView.findViewById(R.id.serviceimg);
            duration =(TextView)itemView.findViewById(R.id.duration_tv);
            priceTV = (TextView)itemView.findViewById(R.id.price_tv);
        }
    }

    List<Service> services;
    private LayoutInflater inflater;
    Service service;
    String serviceType;


    ServiceAdapter(Context context, List<Service> services){
        inflater = LayoutInflater.from(context);
        this.services = services;
        service = new Service();
    }
    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.service_card, parent, false);
        ServiceViewHolder holder = new ServiceViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ServiceViewHolder holder, int position) {
        service = services.get(position);
        serviceType = service.getName();

        holder.serviceTV.setText(serviceType);
        holder.descriptionTV.setText(service.getDescription());
        holder.priceTV.setText(service.getPrice());
        holder.duration.setText(service.getDuration());
    }

    @Override
    public int getItemCount() {
        return services.size();
    }


}