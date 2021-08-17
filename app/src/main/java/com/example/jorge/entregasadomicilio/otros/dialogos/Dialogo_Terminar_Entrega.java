package com.example.jorge.entregasadomicilio.otros.dialogos;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.components.Activity_Entrega_Jefe;
import com.example.jorge.entregasadomicilio.components.Activity_Entregas;
import com.example.jorge.entregasadomicilio.components.Activity_Todas_Entregas;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Entrega;
import com.example.jorge.entregasadomicilio.model.Entrega_Realizada;
import com.example.jorge.entregasadomicilio.model.Producto;
import com.example.jorge.entregasadomicilio.model.Repositorio;
import com.example.jorge.entregasadomicilio.otros.HelpFunctions;

import java.util.ArrayList;


public class Dialogo_Terminar_Entrega {
    public Context context;
    private Entrega entrega;
    private ArrayList<Producto> listaProducto;
    private BaseDeDatos baseDeDatos;
    private String idJefe;
    private Intent intent;
    HelpFunctions hp = new HelpFunctions();

    public Dialogo_Terminar_Entrega(Context context, Entrega entrega, ArrayList<Producto> listaProducto, BaseDeDatos baseDeDatos, String idJefe, Intent intent) {
        this.context = context;
        this.entrega = entrega;
        this.listaProducto = listaProducto;
        this.baseDeDatos = baseDeDatos;
        this.idJefe = idJefe;
        this.intent = intent;
    }

    public void mostrar() {
        // INICIAR EL DIALOGO
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialogo_terminar_venta);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(true);
        // INICIAR LOS ELEMENTOS DEL DIALOGO
        final TextView tituloDialogoTerminarVenta = (TextView) dialog.findViewById(R.id.tituloDialogoTerminarVenta);
        final TextView txt_nombresProductos = (TextView) dialog.findViewById(R.id.txt_nombresProductos);
        final TextView txt_precios = (TextView) dialog.findViewById(R.id.txt_precios);
        final TextView precioTotalEntrega = (TextView) dialog.findViewById(R.id.precioTotalEntrega);
        final CheckBox checkboxDomicilio = (CheckBox) dialog.findViewById(R.id.checkboxDomicilio);
        // COMPROBAR SI EL DOMICILIO DE LA ENTREGA ES GRATIS
        if (getEntrega().getPrecioDomicilio().equals("0")) {
            checkboxDomicilio.setChecked(false);
        } else {
            checkboxDomicilio.setChecked(true);
        }
        // PROGRAMAR LAS NOTAS DE LA VENTA DE CADA PRODUCTO
        String productos = "";
        String precios = "";
        int precioTotal = 0;
        for (Producto producto : getListaProducto()) {
            if (producto.getCantidadAVender() > 0) {
                productos += producto.getNombre() + " (" + String.valueOf(producto.getCantidadAVender()) + ")" + "\n";
                precios += producto.getPrecio() + " x " + producto.getCantidadAVender() + " = " + producto.getPrecioPorCantidad() + " cup\n";
                precioTotal += producto.getPrecioPorCantidad();
            }
        }
        String tituloDialogo = "Entrega de " + getEntrega().getBarrioPrincipal();
        // INSERTAR LOS VALORES DE LA ENTREGA
        tituloDialogoTerminarVenta.setText(tituloDialogo);
        txt_nombresProductos.setText(productos);
        txt_precios.setText(precios);
        String resumenPrecio = String.valueOf(precioTotal) + "cup";
        if (checkboxDomicilio.isChecked()) {
            resumenPrecio = String.valueOf(precioTotal) + " + " + getEntrega().getPrecioDomicilio() + " Dom = " + (precioTotal + Integer.parseInt(getEntrega().getPrecioDomicilio())) + " cup";
        }
        precioTotalEntrega.setText(resumenPrecio);
        // EVENTO A LA ESCUCHA DE SI SE CHEQUEA EL PRECIO DE DOMICILIO, SI SE CHEQUEA EL PRECIO DE DOMICILIO SE SUMA A LA SUMA TOTAL DE LOS PRECIOS
        checkboxDomicilio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int precioTotal = 0;
                for (Producto producto : getListaProducto()) {
                    if (producto.getCantidadAVender() > 0) {
                        precioTotal += producto.getPrecioPorCantidad();
                    }
                }
                String resumenPrecio = String.valueOf(precioTotal) + "cup";
                if (isChecked) {
                    resumenPrecio = String.valueOf(precioTotal) + " + " + getEntrega().getPrecioDomicilio() + " Dom = " + (precioTotal + Integer.parseInt(getEntrega().getPrecioDomicilio())) + " cup";
                }
                precioTotalEntrega.setText(resumenPrecio);
            }
        });

        // COMPLETAR VENTA
        final FloatingActionButton fabCompletarVenta = (FloatingActionButton) dialog.findViewById(R.id.fabCompletarVenta);

        // COMPROBAR VISTA DEL BOTON DE TERMINAR ENTREGA
        if (entrega.getSeRealizoEntrega().equals("SI")) {
            fabCompletarVenta.setVisibility(View.GONE);
        } else {
            fabCompletarVenta.setVisibility(View.VISIBLE);
        }

        // EVENTO A LA ESUCHA DE TERMINAR LA ENTREGA
        fabCompletarVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sumaPrecios = 0;
                for (Producto producto : getListaProducto()) {
                    if (producto.getCantidadAVender() > 0) {
                        producto.setSeVendio("SI");
                        sumaPrecios += producto.getPrecioPorCantidad();
                    } else {
                        producto.setSeVendio("NO");
                    }
                    getBaseDeDatos().actualizarProducto(producto.getId(), producto);
                }
                // ACTUALIZAR LA ENTREGA EN LA BASE DE DATOS
                entrega.setSeRealizoEntrega("SI");
                entrega.setSePagoDomicilio("NO");
                if (checkboxDomicilio.isChecked()) {
                    entrega.setSePagoDomicilio("SI");
                }
                entrega.setPrecioTotal(sumaPrecios);
                entrega.setSeRealizoEntrega("SI");
                getBaseDeDatos().actualizarEntrega(entrega.getId(), entrega);

                // ACTUALIZAR EL REPOSTORIO
                Repositorio repositorio = baseDeDatos.obtenerRepositorio("1");
                repositorio.setTotalEntregas(repositorio.getTotalEntregas() + 1);
                int totalDomicilio = repositorio.getTotalIngresosMios() + Integer.parseInt(entrega.getPrecioDomicilio());
                repositorio.setTotalIngresosMios(totalDomicilio);
                repositorio.setTotalIngresosJefes(repositorio.getTotalIngresosJefes() + entrega.getPrecioTotal());
                baseDeDatos.actualizarRepositorio(repositorio.getId(), repositorio);

                // RECICLAR LA ENTREGA REALIZADA
                baseDeDatos.insertarEntregaRealizada(new Entrega_Realizada(
                        entrega.getId(),
                        hp.obtenerFechaActual().toString(),
                        hp.obtenerHoraActuall().toString(),
                        entrega.getBarrioPrincipal(),
                        entrega.getPrecioDomicilio())
                );

                // REGRESAR AL ACTIVITY DE LAS ENTREGAS DEL JEFE Y CERRAR EL DIALOGO
                if (getIntent() != null) {
                    if (getIntent().getStringExtra("TODAS_ENTREGAS") != null) {
                        Intent i = new Intent(getContext(), Activity_Todas_Entregas.class);
                        i.putExtra("ENTREGA_REALIZADA", getEntrega().getBarrioPrincipal());
                        getContext().startActivity(i);
                        ((Activity_Entrega_Jefe) getContext()).finish();
                        dialog.dismiss();
                        return;
                    }
                }
                Intent i = new Intent(getContext(), Activity_Entregas.class);
                i.putExtra("ID", getIdJefe());
                i.putExtra("ENTREGA_REALIZADA", getEntrega().getBarrioPrincipal());
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

    public Entrega getEntrega() {
        return entrega;
    }

    public ArrayList<Producto> getListaProducto() {
        return listaProducto;
    }

    public BaseDeDatos getBaseDeDatos() {
        return baseDeDatos;
    }

    public String getIdJefe() {
        return idJefe;
    }

    public Intent getIntent() {
        return intent;
    }
}
