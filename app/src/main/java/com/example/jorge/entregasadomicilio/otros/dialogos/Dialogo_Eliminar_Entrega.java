package com.example.jorge.entregasadomicilio.otros.dialogos;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.components.Activity_Entrega_Jefe;
import com.example.jorge.entregasadomicilio.components.Activity_Entregas;
import com.example.jorge.entregasadomicilio.components.Activity_Todas_Entregas;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Entrega;
import com.example.jorge.entregasadomicilio.model.Repositorio;
import com.github.javiersantos.bottomdialogs.BottomDialog;

public class Dialogo_Eliminar_Entrega {
    private Context context;
    private Entrega entrega;
    private BaseDeDatos bd;
    private String idJefe;
    private Intent intent;

    public Dialogo_Eliminar_Entrega(Context context, Entrega entrega, BaseDeDatos bd, String idJefe, Intent intent) {
        this.context = context;
        this.entrega = entrega;
        this.bd = bd;
        this.idJefe = idJefe;
        this.intent = intent;
    }

    public void mostrar() {
        new BottomDialog.Builder(getContext())
                .setTitle("Eliminar Entrega")
                .setContent("¿ Seguro que desea eliminar la entrega de " + getEntrega().getBarrioPrincipal().toUpperCase() + " ?:")
                .setCancelable(true)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveText("Eliminar")
                .setNegativeText("Cancelar")
                .setPositiveTextColorResource(android.R.color.white)
                .setNegativeTextColorResource(android.R.color.black)
                .setPositiveBackgroundColorResource(android.R.color.holo_red_light)
                .onPositive(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(@NonNull BottomDialog bottomDialog) {
                        getBd().eliminarEntrega(getEntrega().getId());
                        // REGRESAR AL PARENT ACTIVITY
                        if (getIntent() != null) {
                            if (getIntent().getStringExtra("TODAS_ENTREGAS") != null) {
                                Intent i = new Intent(getContext(), Activity_Todas_Entregas.class);
                                i.putExtra("ENTREGA_ELIMINADA", "OK");
                                getContext().startActivity(i);
                                ((Activity_Entrega_Jefe) getContext()).finish();
                                return;
                            }
                        }
                        Intent i = new Intent(getContext(), Activity_Entregas.class);
                        i.putExtra("ID", getIdJefe());
                        i.putExtra("ENTREGA_ELIMINADA", "OK");
                        getContext().startActivity(i);
                        ((Activity_Entrega_Jefe) getContext()).finish();
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
        return this.context;
    }

    public Entrega getEntrega() {
        return this.entrega;
    }

    public BaseDeDatos getBd() {
        return this.bd;
    }

    public String getIdJefe() {
        return this.idJefe;
    }

    public Intent getIntent() {
        return intent;
    }
}
