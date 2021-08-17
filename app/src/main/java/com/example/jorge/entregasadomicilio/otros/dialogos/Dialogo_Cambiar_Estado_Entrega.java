package com.example.jorge.entregasadomicilio.otros.dialogos;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.CheckBox;

import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.components.Activity_Entrega_Jefe;
import com.example.jorge.entregasadomicilio.components.Activity_Entregas;
import com.example.jorge.entregasadomicilio.components.Activity_Todas_Entregas;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Entrega;


public class Dialogo_Cambiar_Estado_Entrega {
    public Context context;
    private Entrega entrega;
    private BaseDeDatos baseDeDatos;
    private String idJefe;
    private Intent intent;

    public Dialogo_Cambiar_Estado_Entrega(Context context, Entrega entrega, BaseDeDatos baseDeDatos, String idJefe, Intent intent) {
        this.context = context;
        this.entrega = entrega;
        this.baseDeDatos = baseDeDatos;
        this.idJefe = idJefe;
        this.intent = intent;
    }

    public void mostrar() {
        // INICIAR EL DIALOGO
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialogo_editar_estado_entrega);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(true);
        // INICIAR LOS ELEMENTOS DEL DIALOGO
        final CheckBox checkboxEstado = (CheckBox) dialog.findViewById(R.id.checkboxEstado);
        final FloatingActionButton fabCambiar = (FloatingActionButton) dialog.findViewById(R.id.fabCambiarEstado);
        fabCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkboxEstado.isChecked()) {
                    if (entrega.getSeRealizoEntrega().equals("SI")) {
                        entrega.setSeRealizoEntrega("NO");
                    } else {
                        entrega.setSeRealizoEntrega("SI");
                    }
                }
                getBaseDeDatos().actualizarEntrega(getEntrega().getId(), entrega);
                // REGRESAR AL ACTIVITY DE LAS ENTREGAS DEL JEFE Y CERRAR EL DIALOGO
                // REGRESAR AL ACTIVITY DE LAS ENTREGAS DEL JEFE Y CERRAR EL DIALOGO
                if (getIntent() != null) {
                    if (getIntent().getStringExtra("TODAS_ENTREGAS") != null) {
                        Intent i = new Intent(getContext(), Activity_Todas_Entregas.class);
                        i.putExtra("ESTADO_ENTREGA_EDITADO", getEntrega().getBarrioPrincipal());
                        getContext().startActivity(i);
                        ((Activity_Entrega_Jefe) getContext()).finish();
                        return;
                    }
                }
                Intent i = new Intent(getContext(), Activity_Entregas.class);
                i.putExtra("ID", getIdJefe());
                i.putExtra("ESTADO_ENTREGA_EDITADO", getEntrega().getBarrioPrincipal());
                getContext().startActivity(i);
                ((Activity_Entrega_Jefe) getContext()).finish();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Entrega getEntrega() {
        return entrega;
    }

    public void setEntrega(Entrega entrega) {
        this.entrega = entrega;
    }

    public BaseDeDatos getBaseDeDatos() {
        return baseDeDatos;
    }

    public void setBaseDeDatos(BaseDeDatos baseDeDatos) {
        this.baseDeDatos = baseDeDatos;
    }

    public String getIdJefe() {
        return idJefe;
    }

    public Intent getIntent() {
        return intent;
    }
}
