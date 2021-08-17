package com.example.jorge.entregasadomicilio.otros.dialogos;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.components.Activity_Entrega_Jefe;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Entrega;
import com.example.jorge.entregasadomicilio.model.Producto;

public class Dialogo_Editar_Producto {

    private BaseDeDatos baseDeDatos;
    private String idProducto;
    private Context context;
    private View parentView;
    private LayoutInflater layoutInflater;
    private Resources resources;
    private Entrega entrega;

    public Dialogo_Editar_Producto(BaseDeDatos baseDeDatos, String intent, Context context, View parentView, LayoutInflater layoutInflater, Resources resources, Entrega entrega) {
        this.baseDeDatos = baseDeDatos;
        this.idProducto = intent;
        this.context = context;
        this.parentView = parentView;
        this.layoutInflater = layoutInflater;
        this.resources = resources;
        this.entrega = entrega;
    }


    public boolean mostrar() {
        final Producto producto = getBaseDeDatos().obtenerProductoPorId(getIdProducto());
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialogo_insertar_producto);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(false);
        final AppCompatButton bt_cancel = (AppCompatButton) dialog.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final TextView txt_nombreEntrega = (TextView) dialog.findViewById(R.id.txt_nombreEntrega);
        txt_nombreEntrega.setText("Entrega de " + getEntrega().getBarrioPrincipal());
        final TextView  txt_tituloDialogo = (TextView) dialog.findViewById(R.id.txt_tituloDialogo);
        txt_tituloDialogo.setText("Editar Producto " + producto.getNombre());
        final TextView  txt_cantidad = (TextInputEditText) dialog.findViewById(R.id.txt_cantidad);
        txt_cantidad.setText(String.valueOf(producto.getCantidad()));
        final TextInputEditText txt_precioProducto = (TextInputEditText) dialog.findViewById(R.id.txt_precioProducto);
        txt_precioProducto.setText(String.valueOf(producto.getPrecio()));
        final TextInputEditText txt_nombreProducto = (TextInputEditText) dialog.findViewById(R.id.txt_nombreProducto);
        txt_nombreProducto.setText(producto.getNombre());
        final AppCompatButton bt_submit = (AppCompatButton) dialog.findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SnackBar_Mensajes snackBar_mensajes = new SnackBar_Mensajes(getParentView(), getLayoutInflater(), getResources(), "");
                try {
                    validarFormularioProducto(txt_nombreProducto.getText().toString(), txt_precioProducto.getText().toString(), txt_cantidad.getText().toString());
                    producto.setNombre(txt_nombreProducto.getText().toString());
                    producto.setPrecio(Integer.parseInt(txt_precioProducto.getText().toString()));
                    producto.setCantidad(Integer.parseInt(txt_cantidad.getText().toString()));
                    getBaseDeDatos().actualizarProducto(producto.getId(), producto);
                    dialog.dismiss();
                    snackBar_mensajes.setSms("Producto editado satsfactoriamente");
                    snackBar_mensajes.mostrarMensajedeOK();
                    ((Activity_Entrega_Jefe) getContext()).iniciarValoresDelComponente();
                } catch (Exception e) {
                    snackBar_mensajes.setSms(e.getMessage());
                    snackBar_mensajes.mostrarMensajedeError();
                }
            }
        });
        dialog.show();
        return false;
    }

    // METODO PARA VALIDAR LOS CAMPOS DEL FORMULARIO DEL PRODUCTO
    public void validarFormularioProducto(String nombreProducto, String precio, String cantidad) throws Exception {
        if (nombreProducto.trim().length() == 0) {
            throw new Exception("El nombre del producto no puede estar vacío");
        }
        if (precio.trim().length() == 0 || precio.equals("0")) {
            throw new Exception("El precio del producto no debe estar vacío ni debe ser 0");
        }
        if (cantidad.trim().length() == 0 || cantidad.equals("0")) {
            throw new Exception("La cantidad de productos no debe estar vacía ni debe ser 0");
        }
    }

    public BaseDeDatos getBaseDeDatos() {
        return baseDeDatos;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public Context getContext() {
        return context;
    }

    public View getParentView() {
        return parentView;
    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    public Resources getResources() {
        return resources;
    }

    public Entrega getEntrega() {
        return entrega;
    }
}
