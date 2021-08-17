package com.example.jorge.entregasadomicilio.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Entrega;
import com.example.jorge.entregasadomicilio.model.Jefe;
import com.example.jorge.entregasadomicilio.otros.dialogos.Dialogo_Tipo_Llamada;

import java.util.ArrayList;

public class Adaptador_Jefes extends RecyclerView.Adapter<Adaptador_Jefes.MyHolder> {
    RecyclerTouchListener listener;

    public interface RecyclerTouchListener {
        void onClickItem(View v, int position);
    }

    Context context;
    LayoutInflater inflater;
    ArrayList<Jefe> model;
    BaseDeDatos bd;
    Activity activity;
    Resources resources;

    public Adaptador_Jefes(Context context, ArrayList<Jefe> model, BaseDeDatos bd, Activity activity, Resources resources) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
        this.bd = bd;
        this.activity = activity;
        this.resources = resources;
    }

    public int getItemCount() {
        return model.size();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_jefes, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        final Jefe jefe = model.get(position);
        holder.nombreJefe.setText(jefe.getNombre());
        holder.fotoPerfil.setImageBitmap(jefe.getFotoPerfil());

        holder.telefonoLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogo_Tipo_Llamada dialogo_tipo_llamada = new Dialogo_Tipo_Llamada(context, activity, jefe.getTelefono());
                dialogo_tipo_llamada.mostrarDialogoTipoLlamada();
            }
        });

        int cantEntregasPorHacer = 0;
        ArrayList<Entrega> listaEntregas = bd.obtenerEntregasPorJefe(jefe.getId());
        for (Entrega entrega : listaEntregas) {
            if (entrega.getSeRealizoEntrega().equals("NO")) {
                cantEntregasPorHacer += 1;
            }
        }
        if (cantEntregasPorHacer == 0) {
            holder.cardBorde.setCardBackgroundColor(resources.getColor(R.color.light_green_400));
            holder.txtEntregasPorHacer.setBackgroundColor(resources.getColor(R.color.light_green_400));
        } else {
            holder.cardBorde.setCardBackgroundColor(resources.getColor(R.color.light_blue_400));
            holder.txtEntregasPorHacer.setBackgroundColor(resources.getColor(R.color.light_blue_400));
        }

        if (listaEntregas.size() == 0) {
            holder.cardBorde.setCardBackgroundColor(resources.getColor(R.color.light_green_400));
        } else {
            holder.cardBorde.setCardBackgroundColor(resources.getColor(R.color.light_blue_400));
        }
        holder.txtEntregasPorHacer.setText(String.valueOf(cantEntregasPorHacer));

    }

    public void setClickListener(RecyclerTouchListener value) {
        this.listener = value;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView fotoPerfil;
        TextView nombreJefe, txtEntregasPorHacer;
        ImageButton telefonoLlamar;
        CardView cardBorde;

        public MyHolder(final View view) {
            super(view);
            fotoPerfil = (ImageView) view.findViewById(R.id.imageItemFotoPerfil);
            nombreJefe = (TextView) view.findViewById(R.id.itemNombreJefe);
            telefonoLlamar = (ImageButton) view.findViewById(R.id.llamarJefeBoton);
            txtEntregasPorHacer = (TextView) view.findViewById(R.id.txtEntregasPorHacer);
            cardBorde = (CardView) view.findViewById(R.id.cardBorde);

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
