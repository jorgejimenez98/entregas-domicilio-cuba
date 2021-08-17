package com.example.jorge.entregasadomicilio.otros.dialogos;

import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jorge.entregasadomicilio.R;


public class SnackBar_Mensajes {
    private View parent_view;
    private LayoutInflater inflater;
    private Resources resources;
    private String sms;

    public SnackBar_Mensajes(View parent_view, LayoutInflater inflater, Resources resources, String sms) {
        this.parent_view = parent_view;
        this.inflater = inflater;
        this.resources = resources;
        this.sms = sms;
    }


    public void mostrarMensajedeOK() {
        Snackbar make = Snackbar.make(this.getParent_view(), (CharSequence) "", -1);
        View inflate = this.getInflater().inflate(R.layout.snackbar_icon_text, null);
        make.getView().setBackgroundColor(0);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) make.getView();
        snackbarLayout.setPadding(0, 0, 0, 0);
        ((TextView) inflate.findViewById(R.id.message)).setText(this.getSms());
        ((ImageView) inflate.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done);
        inflate.findViewById(R.id.parent_view).setBackgroundColor(this.getResources().getColor(R.color.green_500));
        snackbarLayout.addView(inflate, 0);
        make.show();
    }

    public void mostrarMensajedeError() {
        Snackbar make = Snackbar.make(this.getParent_view(), (CharSequence) "", -1);
        View inflate = this.getInflater().inflate(R.layout.snackbar_icon_text, null);
        make.getView().setBackgroundColor(0);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) make.getView();
        snackbarLayout.setPadding(0, 0, 0, 0);
        ((TextView) inflate.findViewById(R.id.message)).setText(this.getSms());
        ((ImageView) inflate.findViewById(R.id.icon)).setImageResource(R.drawable.ic_close);
        inflate.findViewById(R.id.parent_view).setBackgroundColor(this.getResources().getColor(R.color.red_600));
        snackbarLayout.addView(inflate, 0);
        make.show();
    }

    public View getParent_view() {
        return parent_view;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public Resources getResources() {
        return resources;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }
}
