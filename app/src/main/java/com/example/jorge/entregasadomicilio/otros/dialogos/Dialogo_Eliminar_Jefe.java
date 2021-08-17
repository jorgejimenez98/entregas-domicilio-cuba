package com.example.jorge.entregasadomicilio.otros.dialogos;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.jorge.entregasadomicilio.MainActivity;
import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.components.Activity_Entregas;
import com.example.jorge.entregasadomicilio.model.Jefe;
import com.github.javiersantos.bottomdialogs.BottomDialog;


public class Dialogo_Eliminar_Jefe {
    private Context context;
    private Jefe jefe;

    public Dialogo_Eliminar_Jefe(Context context, Jefe jefe) {
        this.context = context;
        this.jefe = jefe;
    }

    public boolean eliminar() {
        new BottomDialog.Builder(getContext())
                .setTitle("Eliminar Jefe")
                .setContent("¿ Seguro que desea eliminar al jefe " + getJefe().getNombre().toUpperCase() + " y sus entregas permanentemente ?:")
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
                        Intent i = new Intent(getContext(), MainActivity.class);
                        i.putExtra("ELIMINAR_JEFE", "OK");
                        i.putExtra("ID", getJefe().getId());
                        getContext().startActivity(i);
                        ((Activity_Entregas) getContext()).finish();
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
        return this.context;
    }

    public Jefe getJefe() {
        return this.jefe;
    }
}
