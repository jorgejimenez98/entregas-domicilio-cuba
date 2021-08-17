package com.example.jorge.entregasadomicilio.components;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.jorge.entregasadomicilio.R;

public class Activity_Detalles extends AppCompatActivity {

    private static final String lorem = "lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod " +
            "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
            "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo" +
            "consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse" +
            "cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non" +
            "proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

    private static String name, description, latlong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_);

        /**
         * obtengo el nombre y la descripcion del Place
         */
        name = getIntent().getExtras().getString("NAME");
        description = getIntent().getExtras().getString("DESC");
        latlong = getIntent().getExtras().getString("LATLONG");

        /**
         * init Toolbar y le cambio el titulo
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.CollapsingToolbarLayout);
        collapsingToolbarLayout.setTitle(name);

        /**
         * init el textview y le paso la descripcion y lat long
         */
        TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtDescription.setText(latlong + "\n------------------------\n" + description + "\n" + lorem);

    }

}
