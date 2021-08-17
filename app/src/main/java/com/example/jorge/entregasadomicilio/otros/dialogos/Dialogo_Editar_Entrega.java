package com.example.jorge.entregasadomicilio.otros.dialogos;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.components.Activity_Entrega_Jefe;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Entrega;

/**
 * Created by Jorge on 25/03/2021.
 */

public class Dialogo_Editar_Entrega {

    private Context context;
    private Entrega entrega;
    private BaseDeDatos baseDeDatos;
    private String idJefe;

    public Dialogo_Editar_Entrega(Context context, Entrega entrega, BaseDeDatos baseDeDatos, String idJefe) {
        this.context = context;
        this.entrega = entrega;
        this.baseDeDatos = baseDeDatos;
        this.idJefe = idJefe;
    }

    public void mostrar() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialogo_editar_otras_car_entrega);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(false);
        TextView txt_nombreEntrega = (TextView) dialog.findViewById(R.id.txt_nombreEntrega);
        String titulo = "Entrega de " + getEntrega().getBarrioPrincipal();
        txt_nombreEntrega.setText(titulo);

        final TextInputEditText text_BarrioPrincipal = (TextInputEditText) dialog.findViewById(R.id.text_BarrioPrincipal);
        final TextInputEditText txt_notasEntregas = (TextInputEditText) dialog.findViewById(R.id.txt_notasEntregas);
        final TextInputEditText text_TelefonoCliente = (TextInputEditText) dialog.findViewById(R.id.text_TelefonoCliente);
        final TextInputEditText txt_nombreCliente = (TextInputEditText) dialog.findViewById(R.id.txt_nombreCliente);
        final TextInputEditText text_Direccion = (TextInputEditText) dialog.findViewById(R.id.text_Direccion);
        final TextInputEditText txt_precioDomicilio = (TextInputEditText) dialog.findViewById(R.id.txt_precioDomicilio);
        txt_precioDomicilio.setText(getEntrega().getPrecioDomicilio());
        text_BarrioPrincipal.setText(getEntrega().getBarrioPrincipal());
        txt_notasEntregas.setText(getEntrega().getNotas());
        text_TelefonoCliente.setText(getEntrega().getTelefonoCliente());
        txt_nombreCliente.setText(getEntrega().getNombreCliente());
        text_Direccion.setText(getEntrega().getDireccion());

        AppCompatButton bt_cancel = (AppCompatButton) dialog.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        AppCompatButton bt_submit = (AppCompatButton) dialog.findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEntrega().setBarrioPrincipal(text_BarrioPrincipal.getText().toString());
                getEntrega().setNotas(txt_notasEntregas.getText().toString());
                getEntrega().setTelefonoCliente(text_TelefonoCliente.getText().toString());
                getEntrega().setNombreCliente(txt_nombreCliente.getText().toString());
                getEntrega().setDireccion(text_Direccion.getText().toString());
                getEntrega().setPrecioDomicilio(txt_precioDomicilio.getText().toString());
                getBaseDeDatos().actualizarEntrega(getEntrega().getId(), getEntrega());
                Intent intent = new Intent(getContext(), Activity_Entrega_Jefe.class);
                intent.putExtra("ENTREGA_EDITADA", "OK");
                intent.putExtra("ID_ENTREGA", getEntrega().getId());
                intent.putExtra("ID_JEFE", getIdJefe());
                getContext().startActivity(intent);
                ((Activity_Entrega_Jefe) getContext()).finish();
            }
        });

        dialog.show();
    }

    public Context getContext() {
        return context;
    }

    public Entrega getEntrega() {
        return entrega;
    }

    public BaseDeDatos getBaseDeDatos() {
        return baseDeDatos;
    }

    public String getIdJefe() {
        return idJefe;
    }

    public void setEntrega(Entrega entrega) {
        this.entrega = entrega;
    }
}
