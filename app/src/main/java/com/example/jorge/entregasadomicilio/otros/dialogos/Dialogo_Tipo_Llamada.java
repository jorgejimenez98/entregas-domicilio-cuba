package com.example.jorge.entregasadomicilio.otros.dialogos;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.example.jorge.entregasadomicilio.R;

public class Dialogo_Tipo_Llamada {
    Context context;
    Activity activity;
    String telefono;

    public Dialogo_Tipo_Llamada(Context context, Activity activity, String telefono) {
        this.context = context;
        this.activity = activity;
        this.telefono = telefono;
    }

    public void mostrarDialogoTipoLlamada() {
        final Dialog dialog = new Dialog(this.context);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialogo_seleccionar_tipo_llamada);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(true);
        dialog.show();
        AppCompatButton llamar = (AppCompatButton) dialog.findViewById(R.id.rb_llamarNormal);
        AppCompatButton llamar99 = (AppCompatButton) dialog.findViewById(R.id.llamar_con99);
        llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                realizarLlamada(getTelefono());
            }
        });
        llamar99.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                realizarLlamada("*99" + getTelefono());
            }
        });
    }

    public void realizarLlamada(String telefono) {
        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:" + telefono));
        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            context.startActivity(i);
        } else {
            ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
    }

    public String getTelefono() {
        return this.telefono;
    }
}
