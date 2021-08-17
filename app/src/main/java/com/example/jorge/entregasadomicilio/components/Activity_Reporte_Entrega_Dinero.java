package com.example.jorge.entregasadomicilio.components;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jorge.entregasadomicilio.MainActivity;
import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.adapters.Adaptador_Entregas;
import com.example.jorge.entregasadomicilio.adapters.Adaptador_Resumen_Entrega;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Entrega;
import com.example.jorge.entregasadomicilio.model.Jefe;

import java.util.ArrayList;

public class Activity_Reporte_Entrega_Dinero extends AppCompatActivity {

    BaseDeDatos bd;
    ArrayList<Entrega> listaEntregas = new ArrayList<>();
    Jefe jefe;
    private View parent_view;
    // ELEMENTOS DEL COMPONENTE
    TextView txt_nombresEntregas, txt_preciosTotalEntrega, precioTotalEntregas, txt_toolbarJefe;
    RecyclerView recyclerViewResumenEntregas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__reporte__entrega__dinero);
        bd = new BaseDeDatos(this);
        listaEntregas = bd.obtenerEntregasPorJefeRealizadas(getIDJefe());
        jefe = bd.obtenerJefePorID(getIDJefe());
        this.parent_view = findViewById(16908290);

        // INICIAR ELEMENTOS DEL COMPONENTE
        precioTotalEntregas = (TextView) findViewById(R.id.precioTotalEntregas);
        txt_preciosTotalEntrega = (TextView) findViewById(R.id.txt_preciosTotalEntrega);
        txt_nombresEntregas = (TextView) findViewById(R.id.txt_nombresEntregas);
        recyclerViewResumenEntregas = (RecyclerView) findViewById(R.id.recyclerViewResumenEntregas);
        txt_toolbarJefe = (TextView) findViewById(R.id.txt_toolbarJefe);

        initToolbar();

        // PONER EL TITULO DEL TOOLBAR
        String titulo = "Dinero de " + jefe.getNombre();
        txt_toolbarJefe.setText(titulo);

        // INICIAR EL RECICLER VIEW CON SU RESPECTIVO ADAPTADOR PARA MOSTRAR EL REPORTE DE LAS ENTREGAS
        final Adaptador_Resumen_Entrega adaptador_resumen_entrega = new Adaptador_Resumen_Entrega(getApplicationContext(), listaEntregas);
        recyclerViewResumenEntregas.setNestedScrollingEnabled(false);
        recyclerViewResumenEntregas.setHasFixedSize(true);
        recyclerViewResumenEntregas.setLayoutManager(new LinearLayoutManager(Activity_Reporte_Entrega_Dinero.this));
        recyclerViewResumenEntregas.setAdapter(adaptador_resumen_entrega);

        // LLENAR LOS CAMPOS DE LA SUMA DE TODAS LAS ENTREGAS Y EL PRECIO TOTAL A ENTREGAR
        int sumaToalEntregar = 0;
        String nombresBarriosEntregas = "";
        String preciosTotales = "";
        for (Entrega entrega : listaEntregas) {
            if (entrega.getSeRealizoEntrega().equals("SI")) {
                nombresBarriosEntregas += entrega.getBarrioPrincipal();
                preciosTotales += String.valueOf(entrega.getPrecioTotal());
                if (listaEntregas.indexOf(entrega) != listaEntregas.size() - 1) {
                    nombresBarriosEntregas += "\n";
                    preciosTotales += "\n";
                }
                sumaToalEntregar += entrega.getPrecioTotal();
            }
        }
        txt_nombresEntregas.setText(nombresBarriosEntregas);
        txt_preciosTotalEntrega.setText(preciosTotales);
        String precioTotal = String.valueOf(sumaToalEntregar) + " cup";
        precioTotalEntregas.setText(precioTotal);

        // EVENTO A LA ESCUCHA PARA MOSTRAR MENSAJE DE OK
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_OK);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Activity_Entregas.class);
                intent.putExtra("ID", getIDJefe());
                intent.putExtra("DINERO_ENTREGADO", "OK");
                startActivity(intent);
                finish();
            }
        });
    }

    private void initToolbar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbarEntregas));
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    // METODO A LA ECUCHA DE LA FUNCIONALIDAD DE APRETAR EL BOTON DE LA FLECHA DEL TOOLBAR
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Activity_Entregas.class);
        intent.putExtra("ID", getIDJefe());
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    private String getIDJefe() {
        return getIntent().getStringExtra("ID_JEFE");
    }


}
