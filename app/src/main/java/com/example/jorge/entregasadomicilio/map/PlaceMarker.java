package com.example.jorge.entregasadomicilio.map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.components.Activity_Detalles;
import com.example.jorge.entregasadomicilio.model.Place;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.layer.overlay.Marker;

public class PlaceMarker extends Marker {
    private Context ctx;
    private Place place;


    public PlaceMarker(Context ctx, LatLong latLong, Bitmap bitmap, int horizontalOffset,
                       int verticalOffset, Place place) {
        super(latLong, bitmap, horizontalOffset, verticalOffset);
        this.ctx = ctx;
        this.place = place;

    }

    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
        if (this.contains(layerXY, tapXY)) {
            alertDialogCreate();
            return true;
        }
        return super.onTap(tapLatLong, layerXY, tapXY);
    }


    public void alertDialogCreate() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setIcon(R.drawable.img_lugar);
        builder.setTitle(place.getNombre());
        builder.setMessage("Descripción: " + place.getDescripcion());
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.mas, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(ctx.getApplicationContext(), Activity_Detalles.class);
                intent.putExtra("NAME", place.getNombre());
                intent.putExtra("DESC", place.getDescripcion());
                intent.putExtra("LATLONG", "Lat: " + place.getLatitud() + " Long: " + place.getLongitud());
                ctx.startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }


}
