package com.example.jorge.entregasadomicilio.adapters;


import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Entrega_Realizada;
import com.example.jorge.entregasadomicilio.otros.dialogos.Dialogo_Editar_Entrega_Realizada;
import com.example.jorge.entregasadomicilio.otros.dialogos.Dialogo_Eliminar_Entrega_Realizada;

import java.util.ArrayList;

public class Adaptador_Entrega_Realizada extends RecyclerView.Adapter<Adaptador_Entrega_Realizada.MyHolder> {

    Adaptador_Entrega_Realizada.RecyclerTouchListener listener;

    public interface RecyclerTouchListener {
        void onClickItem(View v, int position);
    }

    Context context;
    LayoutInflater inflater;
    ArrayList<Entrega_Realizada> model;
    String tipoClase;
    BaseDeDatos baseDeDatos;

    public Adaptador_Entrega_Realizada(Context context, ArrayList<Entrega_Realizada> model, String tipoClase, BaseDeDatos baseDeDatos) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
        this.tipoClase = tipoClase;
        this.baseDeDatos = baseDeDatos;
    }

    public int getItemCount() {
        return model.size();
    }

    @Override
    public Adaptador_Entrega_Realizada.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_entregas_auxiliares, parent, false);
        return new Adaptador_Entrega_Realizada.MyHolder(v);
    }

    @Override
    public void onBindViewHolder(final Adaptador_Entrega_Realizada.MyHolder holder, final int position) {
        final Entrega_Realizada entrega = model.get(position);
        holder.txt_Hora.setText(entrega.getHora());
        holder.txt_Barrio.setText(entrega.getBarrioEntrega());
        String precio = entrega.getPrecioDomicilio() + " cup";
        holder.txt_precioDomic.setText(precio);

        holder.layoutPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.layoutBotones.getVisibility() == View.VISIBLE) {
                    Animation slideUpAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_up);
                    holder.layoutBotones.startAnimation(slideUpAnimation);
                    holder.layoutBotones.setVisibility(View.GONE);
                } else {
                    holder.layoutBotones.setVisibility(View.VISIBLE);
                    Animation slideDownAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_down);
                    holder.layoutBotones.startAnimation(slideDownAnimation);
                }
            }
        });

        holder.btn_eliminarEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogo_Eliminar_Entrega_Realizada dialogo = new Dialogo_Eliminar_Entrega_Realizada(context, entrega, baseDeDatos, tipoClase);
                dialogo.mostrar();
            }
        });

        holder.btn_editarEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogo_Editar_Entrega_Realizada dialogo = new Dialogo_Editar_Entrega_Realizada(context, baseDeDatos, entrega, tipoClase);
                dialogo.mostrar();
            }
        });

    }

    public void setClickListener(Adaptador_Entrega_Realizada.RecyclerTouchListener value) {
        this.listener = value;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txt_Hora, txt_Barrio, txt_precioDomic;
        AppCompatButton btn_eliminarEntrega, btn_editarEntrega;
        LinearLayout layoutBotones, layoutPrincipal;


        public MyHolder(final View view) {
            super(view);
            txt_Hora = (TextView) view.findViewById(R.id.txt_Hora);
            txt_Barrio = (TextView) view.findViewById(R.id.txt_Barrio);
            txt_precioDomic = (TextView) view.findViewById(R.id.txt_precioDomic);
            layoutBotones = (LinearLayout) view.findViewById(R.id.layoutBotones);
            layoutPrincipal = (LinearLayout) view.findViewById(R.id.layoutPrincipal);
            btn_eliminarEntrega = (AppCompatButton) view.findViewById(R.id.btn_eliminarEntrega);
            btn_editarEntrega = (AppCompatButton) view.findViewById(R.id.btn_editarEntrega);

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
