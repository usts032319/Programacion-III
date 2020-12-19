package com.example.reposicion.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.reposicion.Models.Producto;
import com.example.reposicion.R;

import java.util.ArrayList;

public class listViewProductoAdapter extends BaseAdapter {
    Context context;
    ArrayList<Producto> productoData;
    LayoutInflater layoutInflater;
    Producto productoModal;

    public listViewProductoAdapter(Context context, ArrayList<Producto> productoData) {
        this.context = context;
        this.productoData = productoData;
        layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
    }

    @Override
    public int getCount() {
        return productoData.size();
    }

    @Override
    public Object getItem(int i) {
        return productoData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View rowView = convertView;
        if (rowView == null){
            rowView = layoutInflater.inflate(R.layout.lista_carro,
                    null,
                    true);
        }
        //enlaza la vista
        TextView marca = (TextView) rowView.findViewById(R.id.marca);
        TextView modelo = (TextView) rowView.findViewById(R.id.modelo);
        TextView year = (TextView) rowView.findViewById(R.id.year);
        TextView motor = (TextView) rowView.findViewById(R.id.motor);
        TextView chasis = (TextView) rowView.findViewById(R.id.chasis);

        productoModal = productoData.get(i);
        marca.setText(productoModal.getMarca());
        modelo.setText(productoModal.getModelo());
        year.setText(productoModal.getYear());
        motor.setText(productoModal.getMotor());
        chasis.setText(productoModal.getChasis());

        return rowView;
    }
}
