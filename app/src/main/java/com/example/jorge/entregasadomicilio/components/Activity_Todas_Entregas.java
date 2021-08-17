package com.example.jorge.entregasadomicilio.components;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jorge.entregasadomicilio.MainActivity;
import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.adapters.Adaptador_Todas_Entregas;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Entrega;
import com.example.jorge.entregasadomicilio.otros.dialogos.SnackBar_Mensajes;

import java.util.ArrayList;

public class Activity_Todas_Entregas extends AppCompatActivity {
    private View parent_view;
    BaseDeDatos baseDeDatos;
    LinearLayout layoutVacio;
    TextView txt_cantEntrega;
    RecyclerView recyclerViewEntregas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__todas__entregas);
        this.parent_view = findViewById(16908290);
        baseDeDatos = new BaseDeDatos(this);
        txt_cantEntrega = (TextView) findViewById(R.id.txt_cantEntrega);
        layoutVacio = (LinearLayout) findViewById(R.id.layoutVacio);
        recyclerViewEntregas = (RecyclerView) findViewById(R.id.recyclerViewEntregas);

        checkearIntent();
        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        getSupportActionBar().setTitle("Todas las Entregas de hoy");
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
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    public void initComponent() {
        ArrayList<Entrega> listaEntregas = baseDeDatos.obtenerTodasEntregas();
        if (listaEntregas.size() == 0) {
            layoutVacio.setVisibility(View.VISIBLE);
            recyclerViewEntregas.setVisibility(View.GONE);
        } else {
            layoutVacio.setVisibility(View.GONE);
            recyclerViewEntregas.setVisibility(View.VISIBLE);
            // Annadir titulo de la tabla
            String tituloTabla = listaEntregas.size() + " Entrega(s)";
            txt_cantEntrega.setText(tituloTabla);
            // Lista de jefes
            ArrayList<String> listaIdJefes = new ArrayList<>();
            for (Entrega entrega : listaEntregas) {
                if (listaIdJefes.indexOf(entrega.getId_jefe()) == -1) {
                    if (baseDeDatos.obtenerEntregasPorJefe(entrega.getId_jefe()).size() != 0) {
                        listaIdJefes.add(entrega.getId_jefe());
                    }
                }
            }
            // INICIAR EL RECYCLER VIEW
            final Adaptador_Todas_Entregas adaptador_entregas = new Adaptador_Todas_Entregas(this, listaIdJefes, Activity_Todas_Entregas.this, baseDeDatos);
            recyclerViewEntregas.setNestedScrollingEnabled(false);
            recyclerViewEntregas.setHasFixedSize(true);
            recyclerViewEntregas.setLayoutManager(new LinearLayoutManager(Activity_Todas_Entregas.this));
            recyclerViewEntregas.setAdapter(adaptador_entregas);

        }
    }

    public void checkearIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            final SnackBar_Mensajes snackBar_mensajes = new SnackBar_Mensajes(this.parent_view, getLayoutInflater(), getResources(), "");
            if (intent.getStringExtra("ESTADO_ENTREGA_EDITADO") != null) {
                snackBar_mensajes.setSms("Estado de entrega editado satisfactoriamente");
                snackBar_mensajes.mostrarMensajedeOK();
            }
            if (intent.getStringExtra("ENTREGA_REALIZADA") != null) {
                String mensaje = "Entrega de " + intent.getStringExtra("ENTREGA_REALIZADA") + " realizada";
                snackBar_mensajes.setSms(mensaje);
                snackBar_mensajes.mostrarMensajedeOK();
            }
            if (intent.getStringExtra("ENTREGA_ELIMINADA") != null) {
                snackBar_mensajes.setSms("Entrega eliminada satisfactoriamente");
                snackBar_mensajes.mostrarMensajedeOK();
            }
        }
    }
}
