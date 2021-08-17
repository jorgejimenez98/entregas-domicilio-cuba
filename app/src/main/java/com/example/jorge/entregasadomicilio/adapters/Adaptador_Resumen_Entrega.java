package com.example.jorge.entregasadomicilio.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Entrega;
import com.example.jorge.entregasadomicilio.model.Producto;

import org.mapsforge.map.rendertheme.renderinstruction.Line;

import java.util.ArrayList;

public class Adaptador_Resumen_Entrega extends RecyclerView.Adapter<Adaptador_Resumen_Entrega.MyHolder> {
    Adaptador_Resumen_Entrega.RecyclerTouchListener listener;

    public interface RecyclerTouchListener {
        void onClickItem(View v, int position);
    }

    Context context;
    LayoutInflater inflater;
    ArrayList<Entrega> model;
    BaseDeDatos bd;

    public Adaptador_Resumen_Entrega(Context context, ArrayList<Entrega> model) {
        bd = new BaseDeDatos(context);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
    }

    public int getItemCount() {
        return model.size();
    }

    @Override
    public Adaptador_Resumen_Entrega.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_resumen_entregas, parent, false);
        return new Adaptador_Resumen_Entrega.MyHolder(v);
    }

    @Override
    public void onBindViewHolder(final Adaptador_Resumen_Entrega.MyHolder holder, final int position) {
        Entrega entrega = model.get(position);
        String nombreEntrega = String.valueOf(position + 1) + " - " + entrega.getBarrioPrincipal();
        holder.txt_nombreEntregaRes.setText(nombreEntrega);
        String sePagoDomicilio = "Si se pagó domicilio (" + entrega.getPrecioDomicilio() + " cup)";
        if (entrega.getSePagoDomicilio().equals("NO")) {
            sePagoDomicilio = "No se pagó domicilio";
        }
        holder.txt_se_pagoDomicilio.setText(sePagoDomicilio);
        ArrayList<Producto> productoList = bd.obtenerProductosPorEntrega(entrega.getId());
        String nombresProductoYCantidadDIO = "";
        String cantProductosSobraron = "";
        int sumaTotalPreciosProductos = 0;
        String nombresProductosVendidosCantidad = "";
        String preciosProductosVendidos = "";
        boolean seVendioAlgo = false;
        for (Producto producto : productoList) {
            nombresProductoYCantidadDIO += producto.getNombre() + "(" + producto.getCantidad() + ")";
            int diferencia = producto.getCantidad() - producto.getCantidadAVender();
            if (diferencia == 0) {
                cantProductosSobraron += "Se vendió todo";
            } else {
                cantProductosSobraron += String.valueOf(diferencia);
            }
            if (diferencia < producto.getCantidad()) {
                seVendioAlgo = true;
                nombresProductosVendidosCantidad += producto.getNombre() + "(" + producto.getCantidadAVender() + ")";
                preciosProductosVendidos += producto.getPrecio() + " x " + producto.getCantidadAVender() + " = " + producto.getPrecioPorCantidad() + " cup";
                sumaTotalPreciosProductos += producto.getPrecioPorCantidad();
            }
            if (productoList.indexOf(producto) != productoList.size() - 1) {
                nombresProductoYCantidadDIO += "\n";
                cantProductosSobraron += "\n";
                nombresProductosVendidosCantidad += "\n";
                preciosProductosVendidos += "\n";
            }
        }
        holder.txt_nombresProductoYCantidadDIO.setText(nombresProductoYCantidadDIO);
        holder.txt_prdocutosSobrantes.setText(cantProductosSobraron);
        holder.txt_nombreProductoVendido.setText(nombresProductosVendidosCantidad);
        holder.txt_precioProductoVendido.setText(preciosProductosVendidos);
        String sumaTotal = "Total: " + sumaTotalPreciosProductos + "cup";
        if (seVendioAlgo) {
            holder.precioTotalEntregas.setText(sumaTotal);
            holder.layoutTituloCosasVendidas.setVisibility(View.VISIBLE);
        } else {
            String seVendio = "No se vendió nada, 0 cup";
            holder.precioTotalEntregas.setText(seVendio);
            holder.layoutTituloCosasVendidas.setVisibility(View.GONE);
        }
    }

    public void setClickListener(Adaptador_Resumen_Entrega.RecyclerTouchListener value) {
        this.listener = value;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutTituloCosasVendidas;
        TextView txt_nombreEntregaRes, txt_se_pagoDomicilio, txt_nombresProductoYCantidadDIO, precioTotalEntregas;
        TextView txt_prdocutosSobrantes, txt_nombreProductoVendido, txt_precioProductoVendido;

        public MyHolder(final View view) {
            super(view);
            txt_nombreEntregaRes = (TextView) view.findViewById(R.id.txt_nombreEntregaRes);
            txt_se_pagoDomicilio = (TextView) view.findViewById(R.id.txt_se_pagoDomicilio);
            txt_nombresProductoYCantidadDIO = (TextView) view.findViewById(R.id.txt_nombresProductoYCantidadDIO);
            txt_prdocutosSobrantes = (TextView) view.findViewById(R.id.txt_prdocutosSobrantes);
            txt_nombreProductoVendido = (TextView) view.findViewById(R.id.txt_nombreProductoVendido);
            txt_precioProductoVendido = (TextView) view.findViewById(R.id.txt_precioProductoVendido);
            precioTotalEntregas = (TextView) view.findViewById(R.id.precioTotalEntregas);
            layoutTituloCosasVendidas = (LinearLayout) view.findViewById(R.id.layoutTituloCosasVendidas);

            view.setTag(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onClickItem(view, getAdapterPosition());
                }
            });
        }
    }

}
