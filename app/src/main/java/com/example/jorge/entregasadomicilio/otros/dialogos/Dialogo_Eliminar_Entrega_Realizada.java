package com.example.jorge.entregasadomicilio.otros.dialogos;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.components.Activity_Dias_Trabajo;
import com.example.jorge.entregasadomicilio.components.Activity_Estadisticas;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Entrega_Realizada;
import com.example.jorge.entregasadomicilio.model.Repositorio;
import com.github.javiersantos.bottomdialogs.BottomDialog;

public class Dialogo_Eliminar_Entrega_Realizada {
    private Context context;
    private Entrega_Realizada entrega_realizada;
    private BaseDeDatos baseDeDatos;
    private String tipoClase;

    public Dialogo_Eliminar_Entrega_Realizada(Context context, Entrega_Realizada entrega_realizada, BaseDeDatos baseDeDatos, String tipoClase) {
        this.context = context;
        this.entrega_realizada = entrega_realizada;
        this.baseDeDatos = baseDeDatos;
        this.tipoClase = tipoClase;
    }

    public void mostrar() {
        new BottomDialog.Builder(getContext())
                .setTitle("Eliminar Entrega realizada")
                .setContent("¿ Seguro que desea eliminar la entrega de " + getEntrega_realizada().getBarrioEntrega() + " ?:")
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
                        getBaseDeDatos().eliminarEntrega_Realizada(getEntrega_realizada().getIdEntrega());
                        Repositorio repositorio = getBaseDeDatos().obtenerRepositorio("1");
                        repositorio.setTotalEntregas(repositorio.getTotalEntregas() - 1);
                        repositorio.setTotalIngresosMios(repositorio.getTotalIngresosMios() - Integer.parseInt(getEntrega_realizada().getPrecioDomicilio()));
                        getBaseDeDatos().actualizarRepositorio("1", repositorio);
                        if (getTipoClase().equals("ESTADISTICAS")) {
                            Intent intent = new Intent(getContext(), Activity_Estadisticas.class);
                            intent.putExtra("ENTREGA_ELIMINADA", getEntrega_realizada().getBarrioEntrega());
                            ((Activity_Estadisticas) getContext()).finish();
                            getContext().startActivity(intent);
                        } else if (getTipoClase().equals("DIAS_TRABAJO")) {
                            Intent intent = new Intent(getContext(), Activity_Dias_Trabajo.class);
                            intent.putExtra("ENTREGA_ELIMINADA", getEntrega_realizada().getBarrioEntrega());
                            ((Activity_Dias_Trabajo) getContext()).finish();
                            getContext().startActivity(intent);
                        }
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

    public Entrega_Realizada getEntrega_realizada() {
        return entrega_realizada;
    }

    public BaseDeDatos getBaseDeDatos() {
        return baseDeDatos;
    }

    public String getTipoClase() {
        return tipoClase;
    }
}
