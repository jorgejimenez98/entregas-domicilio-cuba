package com.example.jorge.entregasadomicilio.otros.dialogos;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.components.Activity_Estadisticas;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Repositorio;
import com.github.javiersantos.bottomdialogs.BottomDialog;

public class Dialogo_Resetear_Repositorio {
    private Context context;
    private Repositorio repositorio;
    private BaseDeDatos baseDeDatos;

    public Dialogo_Resetear_Repositorio(Context context, Repositorio repositorio, BaseDeDatos baseDeDatos) {
        this.context = context;
        this.repositorio = repositorio;
        this.baseDeDatos = baseDeDatos;
    }


    public boolean mostrar() {
        new BottomDialog.Builder(getContext())
                .setTitle("Resetear Repositorio")
                .setContent(getContext().getString(R.string.preguntaReseteo))
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
                        getRepositorio().setTotalEntregas(0);
                        getRepositorio().setTotalIngresosJefes(0);
                        getRepositorio().setTotalIngresosMios(0);
                        getBaseDeDatos().actualizarRepositorio(getRepositorio().getId(), getRepositorio());
                        Intent i = new Intent(getContext(), Activity_Estadisticas.class);
                        i.putExtra("RESETEAR_REPOSITORIO", "OK");
                        ((Activity_Estadisticas) getContext()).finish();
                        getContext().startActivity(i);
                    }
                })
                .onNegative(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(@NonNull BottomDialog bottomDialog) {
                        bottomDialog.dismiss();
                    }
                })
                .show();
        return true;
    }


    public Context getContext() {
        return context;
    }

    public Repositorio getRepositorio() {
        return repositorio;
    }

    public BaseDeDatos getBaseDeDatos() {
        return baseDeDatos;
    }
}
