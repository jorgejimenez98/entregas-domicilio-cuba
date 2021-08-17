package com.example.jorge.entregasadomicilio.components;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.jorge.entregasadomicilio.MainActivity;
import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.adapters.Adaptador_Entrega_Realizada;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Entrega_Realizada;
import com.example.jorge.entregasadomicilio.model.Repositorio;
import com.example.jorge.entregasadomicilio.otros.HelpFunctions;
import com.example.jorge.entregasadomicilio.otros.dialogos.Dialogo_Resetear_Repositorio;
import com.example.jorge.entregasadomicilio.otros.dialogos.SnackBar_Mensajes;

import java.util.ArrayList;

public class Activity_Estadisticas extends AppCompatActivity {

    BaseDeDatos baseDeDatos;
    HelpFunctions hp = new HelpFunctions();
    // ELEMENTOS DEL COMPONENTE
    TextView txt_dineroJefes, txt_totalEntregas, txt_gananciasMias, txt_tituloEntregasHoy, txt_gananciasHoy, txt_cantEntregas;
    RecyclerView recyclerViewEntregasAuxiliares;
    AppCompatButton btn_consultarDiasTrabajo;
    Repositorio repositorio;
    private View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__estadisticas);
        baseDeDatos = new BaseDeDatos(this);
        this.parent_view = findViewById(16908290);
        // INICIAR LOS ELEMENTOS DEL COMPONENTE
        txt_dineroJefes = (TextView) findViewById(R.id.txt_dineroJefes);
        txt_totalEntregas = (TextView) findViewById(R.id.txt_totalEntregas);
        txt_gananciasMias = (TextView) findViewById(R.id.txt_gananciasMias);
        txt_tituloEntregasHoy = (TextView) findViewById(R.id.txt_tituloEntregasHoy);
        recyclerViewEntregasAuxiliares = (RecyclerView) findViewById(R.id.recyclerViewEntregasAuxiliares);
        txt_gananciasHoy = (TextView) findViewById(R.id.txt_gananciasHoy);
        txt_cantEntregas = (TextView) findViewById(R.id.txt_cantEntregas);
        btn_consultarDiasTrabajo = (AppCompatButton) findViewById(R.id.btn_consultarDiasTrabajo);

        checkearIntent();
        initToolbar();
        initComponent();

        btn_consultarDiasTrabajo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Activity_Dias_Trabajo.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initToolbar() {
        getSupportActionBar().setTitle("Estadísticas Generales");
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

    // CREACION DEL MENU
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_estadisticas, menu);
        return true;
    }

    // FUNCIONES AL DARLE CLICK A UN ELEMENTO DEL MENU
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == 16908332) {
            finish();
        }
        if (id == R.id.action_resetaerRepositorio) {
            Dialogo_Resetear_Repositorio dialogo_resetear_repositorio = new Dialogo_Resetear_Repositorio(this, repositorio, baseDeDatos);
            dialogo_resetear_repositorio.mostrar();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void checkearIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("RESETEAR_REPOSITORIO") != null) {
                SnackBar_Mensajes snackBar_mensajes = new SnackBar_Mensajes(this.parent_view, getLayoutInflater(), getResources(), "Repositorio reseteado satisfactoriamente");
                snackBar_mensajes.mostrarMensajedeOK();
                initComponent();
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

    public void initComponent() {
        // AGREGAR EL REPOSITORIO AL DASHBOARD
        repositorio = baseDeDatos.obtenerRepositorio("1");
        String ingresosJefes = repositorio.getTotalIngresosJefes() + " cup";
        txt_dineroJefes.setText(ingresosJefes);
        txt_totalEntregas.setText(String.valueOf(repositorio.getTotalEntregas()));
        String misIngresos = repositorio.getTotalIngresosMios() + " cup";
        txt_gananciasMias.setText(misIngresos);
        String titulo = "Entregas de hoy (" + hp.obtenerDiaDeLaSemana() + " " + hp.obtenerFechaActual() + ")";
        txt_tituloEntregasHoy.setText(titulo);

        // INICIAR RECICLER VIEW
        int gananciasHoy = 0;
        ArrayList<Entrega_Realizada> listaEntregasAux = baseDeDatos.obtenerEntregasRealizadasHoy(hp.obtenerFechaActual().toString());
        for (Entrega_Realizada entrega_realizada : listaEntregasAux) {
            gananciasHoy += Integer.parseInt(entrega_realizada.getPrecioDomicilio());
        }
        String gananciasString = String.valueOf(gananciasHoy) + " cup";
        txt_gananciasHoy.setText(gananciasString);
        txt_cantEntregas.setText(String.valueOf(listaEntregasAux.size()));
        final Adaptador_Entrega_Realizada bossAdapter = new Adaptador_Entrega_Realizada(this, listaEntregasAux, "ESTADISTICAS", baseDeDatos);
        recyclerViewEntregasAuxiliares.setNestedScrollingEnabled(false);
        recyclerViewEntregasAuxiliares.setHasFixedSize(true);
        recyclerViewEntregasAuxiliares.setLayoutManager(new LinearLayoutManager(Activity_Estadisticas.this));
        recyclerViewEntregasAuxiliares.setAdapter(bossAdapter);
    }

}
