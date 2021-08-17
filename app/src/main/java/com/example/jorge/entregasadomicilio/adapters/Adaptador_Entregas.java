package com.example.jorge.entregasadomicilio.adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.model.Entrega;
import com.example.jorge.entregasadomicilio.otros.dialogos.Dialogo_Tipo_Llamada;

import java.util.ArrayList;

public class Adaptador_Entregas extends RecyclerView.Adapter<Adaptador_Entregas.MyHolder> {
    Adaptador_Entregas.RecyclerTouchListener listener;

    public interface RecyclerTouchListener {
        void onClickItem(View v, int position);
    }

    Context context;
    LayoutInflater inflater;
    ArrayList<Entrega> model;
    Activity activity;

    public Adaptador_Entregas(Context context, ArrayList<Entrega> model, Activity activity) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
        this.activity = activity;
    }

    public int getItemCount() {
        return model.size();
    }

    @Override
    public Adaptador_Entregas.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_entregas, parent, false);
        return new Adaptador_Entregas.MyHolder(v);
    }

    @Override
    public void onBindViewHolder(final Adaptador_Entregas.MyHolder holder, final int position) {
        final Entrega entrega = model.get(position);
        if (entrega.getSeRealizoEntrega().equals("SI")) {
            holder.cardEntregas.setCardBackgroundColor(Color.GREEN);
        } else {
            holder.cardEntregas.setCardBackgroundColor(Color.RED);
        }
        String barrioPrincipal = String.valueOf(position + 1) + " - " + entrega.getBarrioPrincipal();
        holder.txt_barrioPrincipal.setText(barrioPrincipal);
        String nombreCliente = entrega.getNombreCliente();
        String telefonoCliente = entrega.getTelefonoCliente();
        if (nombreCliente.isEmpty()) {
            nombreCliente = "No hay nombre";
        }
        if (telefonoCliente.isEmpty()) {
            telefonoCliente = "No tel";
        }
        String nombreYTelefono = nombreCliente + " - " + telefonoCliente;
        holder.txt_nombreYTelefono.setText(nombreYTelefono);
        String domicilio = entrega.getPrecioDomicilio() + " cup";
        holder.txt_precioDomicilio.setText(domicilio);
        if (entrega.getSeRealizoEntrega().equals("NO")) {
            holder.imagen_estadoEntrega.setImageResource(R.drawable.img_not_ok);
        } else {
            holder.imagen_estadoEntrega.setImageResource(R.drawable.img_ok);
        }
        if (entrega.getTelefonoCliente().equals("")) {
            holder.llamarClienteBoton.setVisibility(View.GONE);
        }
        holder.llamarClienteBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogo_Tipo_Llamada dialogo_tipo_llamada = new Dialogo_Tipo_Llamada(context, activity, entrega.getTelefonoCliente());
                dialogo_tipo_llamada.mostrarDialogoTipoLlamada();
            }
        });

    }

    public void setClickListener(Adaptador_Entregas.RecyclerTouchListener value) {
        this.listener = value;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txt_barrioPrincipal, txt_nombreYTelefono, txt_precioDomicilio;
        ImageView imagen_estadoEntrega;
        ImageButton llamarClienteBoton;
        CardView cardEntregas;
        LinearLayout layoutParent;

        public MyHolder(final View view) {
            super(view);
            txt_barrioPrincipal = (TextView) view.findViewById(R.id.txt_barrioPrincipal);
            txt_nombreYTelefono = (TextView) view.findViewById(R.id.txt_nombreYTelefono);
            txt_precioDomicilio = (TextView) view.findViewById(R.id.txt_precioDomicilio);
            imagen_estadoEntrega = (ImageView) view.findViewById(R.id.imagen_estadoEntrega);
            llamarClienteBoton = (ImageButton) view.findViewById(R.id.llamarClienteBoton);
            cardEntregas = (CardView) view.findViewById(R.id.cardEntregas);
            layoutParent = (LinearLayout) view.findViewById(R.id.layoutParent);

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