package com.example.jorge.entregasadomicilio.otros.dialogos;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.components.Activity_Dias_Trabajo;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Entrega_Realizada;
import com.github.javiersantos.bottomdialogs.BottomDialog;

public class Dialogo_Eliminar_Historial {
    private Context context;
    private BaseDeDatos baseDeDatos;

    public Dialogo_Eliminar_Historial(Context context, BaseDeDatos baseDeDatos) {
        this.context = context;
        this.baseDeDatos = baseDeDatos;
    }

    public void mostrar() {
        new BottomDialog.Builder(getContext())
                .setTitle("Eliminar Historial de Entregas")
                .setContent("¿ Seguro que desea eliminar todo el historial de entregas realizadas ?:")
                .setCancelable(true)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveText("Eliminar Historial")
                .setNegativeText("Cancelar")
                .setPositiveTextColorResource(android.R.color.white)
                .setNegativeTextColorResource(android.R.color.black)
                .setPositiveBackgroundColorResource(android.R.color.holo_red_light)
                .onPositive(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(@NonNull BottomDialog bottomDialog) {
                        for (Entrega_Realizada entrega : baseDeDatos.obtenerEntregasRealizadas()) {
                            getBaseDeDatos().eliminarEntrega_Realizada(entrega.getIdEntrega());
                        }
                        Intent intent = new Intent(getContext(), Activity_Dias_Trabajo.class);
                        intent.putExtra("ENTREGAS_ELIMINADAS", "OK");
                        ((Activity_Dias_Trabajo) getContext()).finish();
                        getContext().startActivity(intent);
                        bottomDialog.dismiss();
                    }
                })
                .onNegative(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(@NonNull BottomDialog bottomDialog) {
                        bottomDialog.dismiss();
                    }
                })
                .show();
    }

    public Context getContext() {
        return context;
    }

    public BaseDeDatos getBaseDeDatos() {
        return baseDeDatos;
    }
}
