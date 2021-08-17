package com.example.jorge.entregasadomicilio.adapters;


import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Entrega_Realizada;

import java.util.ArrayList;

public class Adaptador_Dias_Trabajo extends RecyclerView.Adapter<Adaptador_Dias_Trabajo.MyHolder> {
    Adaptador_Dias_Trabajo.RecyclerTouchListener listener;

    public interface RecyclerTouchListener {
        void onClickItem(View v, int position);
    }

    Context context;
    LayoutInflater inflater;
    ArrayList<String> model;
    BaseDeDatos baseDeDatos;
    Resources resources;

    public Adaptador_Dias_Trabajo(Context context, ArrayList<String> model, BaseDeDatos baseDeDatos, Resources resources) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
        this.baseDeDatos = baseDeDatos;
        this.resources = resources;
    }

    public int getItemCount() {
        return model.size();
    }

    @Override
    public Adaptador_Dias_Trabajo.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_dia_trabajo, parent, false);
        return new Adaptador_Dias_Trabajo.MyHolder(v);
    }

    @Override
    public void onBindViewHolder(final Adaptador_Dias_Trabajo.MyHolder holder, final int position) {
        final String fecha = model.get(position);
        ArrayList<Entrega_Realizada> entrega_realizadas = baseDeDatos.obtenerEntregasRealizadasHoy(fecha);
        int totalGanancias = 0;
        for (Entrega_Realizada entrega : entrega_realizadas) {
            totalGanancias += Integer.parseInt(entrega.getPrecioDomicilio());
        }
        holder.txt_fecha.setText(fecha);
        holder.txt_numeroEntregas.setText(String.valueOf(entrega_realizadas.size()));
        String gananciaString = String.valueOf(totalGanancias) + " cup";
        holder.txt_gananciaTotal.setText(gananciaString);

        if (totalGanancias <= 100) {
            holder.cardDiaTrabajo.setCardBackgroundColor(resources.getColor(R.color.red_600));
            holder.img_estado.setImageResource(R.drawable.bad);
            holder.img_estado.setColorFilter(resources.getColor(R.color.red_600));
        } else if (totalGanancias > 100 && totalGanancias <= 300) {
            holder.cardDiaTrabajo.setCardBackgroundColor(resources.getColor(R.color.light_blue_400));
            holder.img_estado.setImageResource(R.drawable.soso);
            holder.img_estado.setColorFilter(resources.getColor(R.color.light_blue_400));
        } else if (totalGanancias > 300) {
            holder.cardDiaTrabajo.setCardBackgroundColor(resources.getColor(R.color.light_green_400));
            holder.img_estado.setImageResource(R.drawable.ganancias);
            holder.img_estado.setColorFilter(resources.getColor(R.color.light_green_400));
        }

    }

    public void setClickListener(Adaptador_Dias_Trabajo.RecyclerTouchListener value) {
        this.listener = value;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txt_fecha, txt_numeroEntregas, txt_gananciaTotal;
        ImageView img_estado;
        CardView cardDiaTrabajo;


        public MyHolder(final View view) {
            super(view);
            txt_fecha = (TextView) view.findViewById(R.id.txt_fecha);
            txt_numeroEntregas = (TextView) view.findViewById(R.id.txt_numeroEntregas);
            txt_gananciaTotal = (TextView) view.findViewById(R.id.txt_gananciaTotal);
            img_estado = (ImageView) view.findViewById(R.id.img_estado);
            cardDiaTrabajo = (CardView) view.findViewById(R.id.cardDiaTrabajo);

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
