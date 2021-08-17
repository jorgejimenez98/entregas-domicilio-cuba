package com.example.jorge.entregasadomicilio.otros.dialogos;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.components.Activity_Dias_Trabajo;
import com.example.jorge.entregasadomicilio.components.Activity_Estadisticas;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Entrega_Realizada;
import com.example.jorge.entregasadomicilio.model.Repositorio;

public class Dialogo_Editar_Entrega_Realizada {
    private Context context;
    private BaseDeDatos baseDeDatos;
    private Entrega_Realizada entrega_realizada;
    private String tipoClase;

    public Dialogo_Editar_Entrega_Realizada(Context context, BaseDeDatos baseDeDatos, Entrega_Realizada entrega_realizada, String tipoClase) {
        this.context = context;
        this.baseDeDatos = baseDeDatos;
        this.entrega_realizada = entrega_realizada;
        this.tipoClase = tipoClase;
    }

    public void mostrar() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialogo_editar_entrega_realizada);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(false);
        final AppCompatButton bt_cancel = (AppCompatButton) dialog.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final TextView txt_TitulonombreEntrega = (TextView) dialog.findViewById(R.id.txt_TitulonombreEntrega);
        String nombreEntrega = "Entrega de " + getEntrega_realizada().getBarrioEntrega();
        txt_TitulonombreEntrega.setText(nombreEntrega);
        final TextInputEditText txt_precioDomicilio = (TextInputEditText) dialog.findViewById(R.id.txt_precioProducto);
        txt_precioDomicilio.setText(String.valueOf(getEntrega_realizada().getPrecioDomicilio()));
        final TextInputEditText txt_nombreEntrega = (TextInputEditText) dialog.findViewById(R.id.txt_nombreProducto);
        txt_nombreEntrega.setText(getEntrega_realizada().getBarrioEntrega());
        final AppCompatButton bt_submit = (AppCompatButton) dialog.findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validarEntregaRealizada(txt_nombreEntrega.getText().toString(), txt_precioDomicilio.getText().toString());
                    int antiguoPrecioDomicilio = Integer.parseInt(getEntrega_realizada().getPrecioDomicilio());
                    int nuevoPrecioDomicilio = Integer.parseInt(txt_precioDomicilio.getText().toString());

                    getEntrega_realizada().setBarrioEntrega(txt_nombreEntrega.getText().toString());
                    getEntrega_realizada().setPrecioDomicilio(txt_precioDomicilio.getText().toString());

                    getBaseDeDatos().actualizarEntregaRealizada(getEntrega_realizada().getIdEntrega(), getEntrega_realizada());

                    Repositorio repositorio = getBaseDeDatos().obtenerRepositorio("1");
                    if (antiguoPrecioDomicilio < nuevoPrecioDomicilio) {
                        repositorio.setTotalIngresosMios(repositorio.getTotalIngresosMios() + (nuevoPrecioDomicilio - antiguoPrecioDomicilio));
                    } else {
                        repositorio.setTotalIngresosMios(repositorio.getTotalIngresosMios() - (antiguoPrecioDomicilio - nuevoPrecioDomicilio));
                    }
                    getBaseDeDatos().actualizarRepositorio("1", repositorio);

                    if (getTipoClase().equals("ESTADISTICAS")) {
                        Intent intent = new Intent(getContext(), Activity_Estadisticas.class);
                        intent.putExtra("ENTREGA_EDITADA", getEntrega_realizada().getBarrioEntrega());
                        ((Activity_Estadisticas) getContext()).finish();
                        getContext().startActivity(intent);
                    } else if (getTipoClase().equals("DIAS_TRABAJO")) {
                        Intent intent = new Intent(getContext(), Activity_Dias_Trabajo.class);
                        intent.putExtra("ENTREGA_EDITADA", getEntrega_realizada().getBarrioEntrega());
                        ((Activity_Dias_Trabajo) getContext()).finish();
                        getContext().startActivity(intent);
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    public Context getContext() {
        return context;
    }

    public BaseDeDatos getBaseDeDatos() {
        return baseDeDatos;
    }

    public Entrega_Realizada getEntrega_realizada() {
        return entrega_realizada;
    }

    public String getTipoClase() {
        return tipoClase;
    }

    public void validarEntregaRealizada(String nombreEntrega, String precioDomicilio) throws Exception {
        if (nombreEntrega.trim().length() == 0) {
            throw new Exception("El nombre de la entrega no puede estar vacío");
        }
        if (Integer.parseInt(precioDomicilio) < 0) {
            throw new Exception("El precio de domicilio no puede ser negativo");
        }
    }
}
