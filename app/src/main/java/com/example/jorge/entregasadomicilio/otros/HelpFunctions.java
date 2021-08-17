package com.example.jorge.entregasadomicilio.otros;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;

import java.io.ByteArrayOutputStream;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class HelpFunctions {

    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public CharSequence obtenerDiaDeLaSemana() {
        return android.text.format.DateFormat.format("EEEE", new Date());
    }

    public CharSequence obtenerFechaActual() {
        return android.text.format.DateFormat.format("dd/MM/yyyy", new Date());
    }

    public CharSequence obtenerHoraActuall() {
        return android.text.format.DateFormat.format("HH:mm", new Date());
    }

}
