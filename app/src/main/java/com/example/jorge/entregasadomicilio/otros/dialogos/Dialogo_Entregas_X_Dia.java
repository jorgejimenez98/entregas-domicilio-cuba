package com.example.jorge.entregasadomicilio.otros.dialogos;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.adapters.Adaptador_Entrega_Realizada;
import com.example.jorge.entregasadomicilio.components.Activity_Dias_Trabajo;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Entrega_Realizada;

import java.util.ArrayList;


public class Dialogo_Entregas_X_Dia {

    private Context context;
    private String fecha;
    private BaseDeDatos baseDeDatos;
    long backPressedTime = 0;

    public Dialogo_Entregas_X_Dia(Context context, String fecha, BaseDeDatos baseDeDatos) {
        this.context = context;
        this.fecha = fecha;
        this.baseDeDatos = baseDeDatos;
    }


    public void mostrar() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialogo_entregas_x_dia);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(true);

        // INICIAR RECYCLER VIEW
        ArrayList<Entrega_Realizada> entrega_realizadas = baseDeDatos.obtenerEntregasRealizadasHoy(fecha);
        final Adaptador_Entrega_Realizada bossAdapter = new Adaptador_Entrega_Realizada(getContext(), entrega_realizadas, "DIAS_TRABAJO", getBaseDeDatos());
        final RecyclerView recyclerViewDiasTrabajoAux = (RecyclerView) dialog.findViewById(R.id.recyclerViewDiasTrabajoAux);
        recyclerViewDiasTrabajoAux.setNestedScrollingEnabled(false);
        recyclerViewDiasTrabajoAux.setHasFixedSize(true);
        recyclerViewDiasTrabajoAux.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDiasTrabajoAux.setAdapter(bossAdapter);

        final ImageView bt_close = (ImageView) dialog.findViewById(R.id.bt_close);
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // PONER LOS DATOS EN EL DIALOGO
        int ganancias = 0;
        for (Entrega_Realizada entrega : entrega_realizadas) {
            ganancias += Integer.parseInt(entrega.getPrecioDomicilio());
        }
        final TextView txt_totalEntregas = (TextView) dialog.findViewById(R.id.txt_totalEntregas);
        txt_totalEntregas.setText(String.valueOf(entrega_realizadas.size()));
        final TextView txt_ganancias = (TextView) dialog.findViewById(R.id.txt_ganancias);
        String gananciasString = String.valueOf(ganancias) + " cup";
        txt_ganancias.setText(gananciasString);
        final TextView txt_tituloDialogo = (TextView) dialog.findViewById(R.id.txt_tituloDialogo);
        String titulo = "Entregas del Día " + getFecha();
        txt_tituloDialogo.setText(titulo);
        txt_ganancias.setText(gananciasString);

        final ImageView bt_close2 = (ImageView) dialog.findViewById(R.id.bt_close2);
        bt_close2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final AppCompatButton btn_eliminarDiaTrabajo = (AppCompatButton) dialog.findViewById(R.id.btn_eliminarDiaTrabajo);
        btn_eliminarDiaTrabajo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast backToast = Toast.makeText(getContext(), "Presione otra vez para eliminar el día de trabajo", Toast.LENGTH_LONG);
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    backToast.cancel();
                    for (Entrega_Realizada entrega : baseDeDatos.obtenerEntregasRealizadasHoy(fecha)) {
                        baseDeDatos.eliminarEntrega_Realizada(entrega.getIdEntrega());
                    }
                    Intent intent = new Intent(getContext(), Activity_Dias_Trabajo.class);
                    intent.putExtra("DIA_ELIMINADO", getFecha());
                    ((Activity_Dias_Trabajo) getContext()).finish();
                    getContext().startActivity(intent);
                    dialog.dismiss();
                    return;
                } else {
                    backToast.show();
                }
                backPressedTime = System.currentTimeMillis();
            }
        });

        dialog.show();
    }

    public Context getContext() {
        return context;
    }

    public String getFecha() {
        return fecha;
    }

    public BaseDeDatos getBaseDeDatos() {
        return baseDeDatos;
    }
}
