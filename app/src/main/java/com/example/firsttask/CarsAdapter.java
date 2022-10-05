package com.example.firsttask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CarsAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Cars> objects;

    CarsAdapter(Context context, ArrayList<Cars> objects){
        this.context = context;
        this.objects = objects;
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.activity_forlistview, parent, false);
        }
        Cars c = getCar(position);

        ((TextView) view.findViewById(R.id.showCarName)).setText(c.nameCar);
        ((TextView) view.findViewById(R.id.showCarColor)).setText(c.colorCar);
        ((TextView) view.findViewById(R.id.showCarPrice)).setText(c.priceCar);

        return view;
    }

    Cars getCar(int position){
       return ((Cars) getItem(position));
   }


}
