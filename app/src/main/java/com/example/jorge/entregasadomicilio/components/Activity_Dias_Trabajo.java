package com.example.jorge.entregasadomicilio.components;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.adapters.Adaptador_Dias_Trabajo;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Entrega_Realizada;
import com.example.jorge.entregasadomicilio.model.Repositorio;
import com.example.jorge.entregasadomicilio.otros.dialogos.Dialogo_Eliminar_Historial;
import com.example.jorge.entregasadomicilio.otros.dialogos.Dialogo_Entregas_X_Dia;
import com.example.jorge.entregasadomicilio.otros.dialogos.SnackBar_Mensajes;

import org.mapsforge.map.rendertheme.renderinstruction.Line;

import java.util.ArrayList;
import java.util.Collections;

public class Activity_Dias_Trabajo extends AppCompatActivity {
    private View parent_view;
    RecyclerView recyclerViewDiasTrabajo;
    BaseDeDatos baseDeDatos;
    LinearLayout layoutVacio, layoutTitulo;
    TextView txt_cantidadDias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activityi__dias__trabajo);
        this.parent_view = findViewById(16908290);
        baseDeDatos = new BaseDeDatos(this);
        recyclerViewDiasTrabajo = (RecyclerView) findViewById(R.id.recyclerViewDiasTrabajo);
        layoutVacio = (LinearLayout) findViewById(R.id.layoutVacio);
        layoutTitulo = (LinearLayout) findViewById(R.id.layoutTitulo);
        txt_cantidadDias = (TextView) findViewById(R.id.txt_cantidadDias);

        initToolbar();
        chequearIntent();
        initComponent();

    }

    private void initToolbar() {
        getSupportActionBar().setTitle("Días de Trabajo");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Activity_Estadisticas.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    // CREACION DEL MENU DE LA ACTIVIDAD
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dias_trabajo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_EliminarHistorial) {
            Dialogo_Eliminar_Historial dialogo_eliminar_historial = new Dialogo_Eliminar_Historial(this, baseDeDatos);
            dialogo_eliminar_historial.mostrar();
        }
        return super.onOptionsItemSelected(item);
    }

    public Context getContext() {
        return this;
    }

    public void chequearIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("DIA_ELIMINADO") != null) {
                String sms = "Se han eliminado satisfactoriamente todas las entregas realizadas del dia " + intent.getStringExtra("DIA_ELIMINADO");
                SnackBar_Mensajes snackBar_mensajes = new SnackBar_Mensajes(this.parent_view, getLayoutInflater(), getResources(), sms);
                snackBar_mensajes.mostrarMensajedeOK();
            }
            if (intent.getStringExtra("ENTREGAS_ELIMINADAS") != null) {
                String sms = "Se han eliminado satisfactoriamente todo el historial de las entregas realizadas";
                SnackBar_Mensajes snackBar_mensajes = new SnackBar_Mensajes(this.parent_view, getLayoutInflater(), getResources(), sms);
                snackBar_mensajes.mostrarMensajedeOK();
            }
            if (intent.getStringExtra("ENTREGA_ELIMINADA") != null) {
                String sms = "Entrega " + intent.getStringExtra("ENTREGA_ELIMINADA") + " eliminada satisfactoriamente";
                SnackBar_Mensajes snackBar_mensajes = new SnackBar_Mensajes(this.parent_view, getLayoutInflater(), getResources(), sms);
                snackBar_mensajes.mostrarMensajedeOK();
                initComponent();
            }
            if (intent.getStringExtra("ENTREGA_EDITADA") != null) {
                String sms = "Entrega " + intent.getStringExtra("ENTREGA_EDITADA") + " editada satisfactoriamente";
                SnackBar_Mensajes snackBar_mensajes = new SnackBar_Mensajes(this.parent_view, getLayoutInflater(), getResources(), sms);
                snackBar_mensajes.mostrarMensajedeOK();
                initComponent();
            }
        }
    }

    private void initComponent() {
        // INICIAR RECICLER VIEW
        final ArrayList<String> listaFechas = new ArrayList<>();
        for (Entrega_Realizada entrega : baseDeDatos.obtenerEntregasRealizadas()) {
            if (listaFechas.indexOf(entrega.getFecha()) == -1) {
                listaFechas.add(entrega.getFecha());
            }
        }
        if (listaFechas.size() == 0) {
            recyclerViewDiasTrabajo.setVisibility(View.GONE);
            layoutTitulo.setVisibility(View.GONE);
            layoutVacio.setVisibility(View.VISIBLE);
        } else {
            recyclerViewDiasTrabajo.setVisibility(View.VISIBLE);
            layoutTitulo.setVisibility(View.VISIBLE);
            layoutVacio.setVisibility(View.GONE);

            String cantidad = listaFechas.size() + " Día(s) trabajado(s)";
            txt_cantidadDias.setText(cantidad);
            Collections.reverse(listaFechas);
            final Adaptador_Dias_Trabajo bossAdapter = new Adaptador_Dias_Trabajo(this, listaFechas, baseDeDatos, getResources());
            recyclerViewDiasTrabajo.setNestedScrollingEnabled(false);
            recyclerViewDiasTrabajo.setHasFixedSize(true);
            recyclerViewDiasTrabajo.setLayoutManager(new LinearLayoutManager(Activity_Dias_Trabajo.this));
            recyclerViewDiasTrabajo.setAdapter(bossAdapter);
            bossAdapter.setClickListener(new Adaptador_Dias_Trabajo.RecyclerTouchListener() {
                @Override
                public void onClickItem(View v, int position) {
                    Dialogo_Entregas_X_Dia dialogo_entregas_x_dia = new Dialogo_Entregas_X_Dia(getContext(), listaFechas.get(position), baseDeDatos);
                    dialogo_entregas_x_dia.mostrar();
                }
            });
        }
    }
}
