package com.example.jorge.entregasadomicilio.otros.dialogos;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;

import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.model.Entrega;

public class Dialogo_Agrandar_Foto {
    private Context context;
    private Entrega entrega;
    private ImageView fotoPrincipal;

    public Dialogo_Agrandar_Foto(Context context, Entrega entrega, ImageView fotoPrincipal) {
        this.context = context;
        this.entrega = entrega;
        this.fotoPrincipal = fotoPrincipal;
    }

    public void mostrar() {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_image_quotes);
        final ImageView imgFotoEntregaGrande = (ImageView) dialog.findViewById(R.id.imgFotoEntregaGrande);
        final FloatingActionButton fabCambiarRotacion = (FloatingActionButton) dialog.findViewById(R.id.fabCambiarRotacion);
        if (getEntrega().getEsPorFoto().equals("SI")) {
            imgFotoEntregaGrande.setImageBitmap(getEntrega().getFotoDireccion());
        } else {
            imgFotoEntregaGrande.setImageResource(R.drawable.img_no_friend);
        }
        fabCambiarRotacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CAMBIAR ROTACION DE LA IMAGEN
                if (imgFotoEntregaGrande.getRotation() == 0) {
                    imgFotoEntregaGrande.setRotation(90);
                } else if (imgFotoEntregaGrande.getRotation() == 90) {
                    imgFotoEntregaGrande.setRotation(180);
                } else if (imgFotoEntregaGrande.getRotation() == 180) {
                    imgFotoEntregaGrande.setRotation(270);
                } else if (imgFotoEntregaGrande.getRotation() == 270) {
                    imgFotoEntregaGrande.setRotation(360);
                } else if (imgFotoEntregaGrande.getRotation() == 360) {
                    imgFotoEntregaGrande.setRotation(0);
                }
                // GUARDAR LA IMAGEN CON LA NUEVA ROTACION
                if (entrega.getEsPorFoto().equals("SI")) {
                    imgFotoEntregaGrande.setImageBitmap(entrega.getFotoDireccion());
                    getFotoPrincipal().setImageBitmap(entrega.getFotoDireccion());
                } else {
                    imgFotoEntregaGrande.setImageResource(R.drawable.img_no_friend);
                    getFotoPrincipal().setImageResource(R.drawable.img_no_friend);
                }
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(true);
        dialog.show();
    }

    public Context getContext() {
        return this.context;
    }

    public Entrega getEntrega() {
        return this.entrega;
    }

    public ImageView getFotoPrincipal() {
        return this.fotoPrincipal;
    }
}
