package com.example.jorge.entregasadomicilio.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.components.Activity_Entrega_Jefe;
import com.example.jorge.entregasadomicilio.components.Activity_Todas_Entregas;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Entrega;
import com.example.jorge.entregasadomicilio.model.Jefe;

import java.util.ArrayList;

public class Adaptador_Todas_Entregas extends RecyclerView.Adapter<Adaptador_Todas_Entregas.MyHolder> {
    Adaptador_Todas_Entregas.RecyclerTouchListener listener;

    public interface RecyclerTouchListener {
        void onClickItem(View v, int position);
    }

    Context context;
    LayoutInflater inflater;
    ArrayList<String> model;
    Activity activity;
    BaseDeDatos baseDeDatos;

    public Adaptador_Todas_Entregas(Context context, ArrayList<String> model, Activity activity, BaseDeDatos baseDeDatos) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
        this.activity = activity;
        this.baseDeDatos = baseDeDatos;
        this.activity = activity;
    }

    public int getItemCount() {
        return model.size();
    }

    @Override
    public Adaptador_Todas_Entregas.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_todas_entregas, parent, false);
        return new Adaptador_Todas_Entregas.MyHolder(v);
    }

    @Override
    public void onBindViewHolder(final Adaptador_Todas_Entregas.MyHolder holder, final int position) {
        final String idJefe = model.get(position);
        final Jefe jefe = baseDeDatos.obtenerJefePorID(idJefe);
        holder.txt_nombreJefe.setText(jefe.getNombre());

        // INICIAR EL RECICLER VIEW CON SU RESPECTIVO ADAPTADOR
        final ArrayList<Entrega> listaEntregas = baseDeDatos.obtenerEntregasPorJefe(jefe.getId());
        String titulo = listaEntregas.size() + " Entrega(s)";
        holder.txt_entregas.setText(titulo);
        final Adaptador_Entregas adaptador_entregas = new Adaptador_Entregas(context, listaEntregas, activity);
        holder.recyclerViewEntregas.setNestedScrollingEnabled(false);
        holder.recyclerViewEntregas.setHasFixedSize(true);
        holder.recyclerViewEntregas.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerViewEntregas.setAdapter(adaptador_entregas);
        adaptador_entregas.setClickListener(new Adaptador_Entregas.RecyclerTouchListener() {
            @Override
            public void onClickItem(View v, int position) {
                Intent intent = new Intent(context, Activity_Entrega_Jefe.class);
                intent.putExtra("ID_ENTREGA", listaEntregas.get(position).getId());
                intent.putExtra("TODAS_ENTREGAS", "OK");
                intent.putExtra("ID_JEFE", jefe.getId());
                context.startActivity(intent);
                ((Activity_Todas_Entregas) context).finish();
            }
        });

    }


    public class MyHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerViewEntregas;
        TextView txt_barrioPrincipal, txt_nombreYTelefono, txt_precioDomicilio, txt_nombreJefe, txt_entregas;
        ImageView imagen_estadoEntrega;
        ImageButton llamarClienteBoton;
        CardView cardEntregas;
        LinearLayout layoutParent, layout_NombreJefe;

        public MyHolder(final View view) {
            super(view);
            txt_barrioPrincipal = (TextView) view.findViewById(R.id.txt_barrioPrincipal);
            txt_nombreYTelefono = (TextView) view.findViewById(R.id.txt_nombreYTelefono);
            txt_precioDomicilio = (TextView) view.findViewById(R.id.txt_precioDomicilio);
            imagen_estadoEntrega = (ImageView) view.findViewById(R.id.imagen_estadoEntrega);
            llamarClienteBoton = (ImageButton) view.findViewById(R.id.llamarClienteBoton);
            cardEntregas = (CardView) view.findViewById(R.id.cardEntregas);
            layoutParent = (LinearLayout) view.findViewById(R.id.layoutParent);
            txt_nombreJefe = (TextView) view.findViewById(R.id.txt_nombreJefe);
            layout_NombreJefe = (LinearLayout) view.findViewById(R.id.layout_NombreJefe);
            recyclerViewEntregas = (RecyclerView) view.findViewById(R.id.recyclerViewEntregas);
            txt_entregas = (TextView) view.findViewById(R.id.txt_entregas);

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
