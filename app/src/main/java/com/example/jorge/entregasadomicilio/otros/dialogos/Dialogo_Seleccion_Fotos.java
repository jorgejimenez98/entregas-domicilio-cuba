package com.example.jorge.entregasadomicilio.otros.dialogos;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.components.Activity_Editar_Jefe;
import com.github.javiersantos.bottomdialogs.BottomDialog;


public class Dialogo_Seleccion_Fotos {
    private Context context;
    private int SELECT_FILE;
    private int CAMERA_REQUEST;

    public Dialogo_Seleccion_Fotos(Context context, int SELECT_FILE, int CAMERA_REQUEST) {
        this.context = context;
        this.SELECT_FILE = SELECT_FILE;
        this.CAMERA_REQUEST = CAMERA_REQUEST;
    }


    public void mostrar() {
        new BottomDialog.Builder(getContext())
                .setTitle("Opciones de Foto")
                .setCancelable(true)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveText("Seleccionar de Galeria")
                .setNegativeText("Hacer Foto")
                .setPositiveTextColorResource(android.R.color.white)
                .setNegativeTextColorResource(android.R.color.black)
                .setPositiveBackgroundColorResource(android.R.color.holo_blue_light)
                .onPositive(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(@NonNull BottomDialog bottomDialog) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        ((Activity_Editar_Jefe) context).startActivityForResult(Intent.createChooser(intent, "Seleccione una imagen"), getSELECT_FILE());
                        bottomDialog.dismiss();
                    }
                })
                .onNegative(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(@NonNull BottomDialog bottomDialog) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        ((Activity_Editar_Jefe) context).startActivityForResult(cameraIntent, getCAMERA_REQUEST());
                        bottomDialog.dismiss();
                    }
                })
                .show();
    }

    public Context getContext() {
        return context;
    }

    public int getSELECT_FILE() {
        return SELECT_FILE;
    }

    public int getCAMERA_REQUEST() {
        return CAMERA_REQUEST;
    }
}
