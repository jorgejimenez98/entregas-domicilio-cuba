package com.example.jorge.entregasadomicilio.components;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jorge.entregasadomicilio.MainActivity;
import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.adapters.Adaptador_Entregas;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Entrega;
import com.example.jorge.entregasadomicilio.model.Jefe;
import com.example.jorge.entregasadomicilio.otros.dialogos.Dialogo_Eliminar_Jefe;
import com.example.jorge.entregasadomicilio.otros.dialogos.Dialogo_Tipo_Llamada;
import com.example.jorge.entregasadomicilio.otros.dialogos.SnackBar_Mensajes;

import java.util.ArrayList;

public class Activity_Entregas extends AppCompatActivity {

    BaseDeDatos bd;
    Jefe jefe;
    ArrayList<Entrega> listaEntregas = new ArrayList<>();
    private View parent_view;

    // ELEMENTOS DEL COMPONENTE
    TextView txt_toolbarJefe, txt_nombreJefe, txt_telefonoJefe, txt_cantidadEntregas, txt_listado;
    LinearLayout layoutEntregasVacio, layoutEntregas;
    FloatingActionButton fabInsertarEntrega;
    ImageView imageFoToPerfil;
    RecyclerView recyclerViewEntregas;
    AppCompatButton btn_entregarDinero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entregas);
        bd = new BaseDeDatos(this);
        this.parent_view = findViewById(16908290);
        // LLAMAR A LA BASE DE DATOS
        jefe = bd.obtenerJefePorID(getIDJefe());

        // INICIAR ELEMENTOS DEL COMPONENTE
        txt_toolbarJefe = (TextView) findViewById(R.id.txt_toolbarJefe);
        txt_nombreJefe = (TextView) findViewById(R.id.txt_nombreJefeItem);
        txt_telefonoJefe = (TextView) findViewById(R.id.txt_telefonoJefe);
        txt_cantidadEntregas = (TextView) findViewById(R.id.txt_cantidadEntregas);
        txt_listado = (TextView) findViewById(R.id.txt_listado);
        imageFoToPerfil = (ImageView) findViewById(R.id.fotoPerfilJefe);
        fabInsertarEntrega = (FloatingActionButton) findViewById(R.id.fab_InsertarEntrega);
        layoutEntregas = (LinearLayout) findViewById(R.id.layoutEntregas);
        layoutEntregasVacio = (LinearLayout) findViewById(R.id.layoutEntregasVacio);
        recyclerViewEntregas = (RecyclerView) findViewById(R.id.recyclerViewEntregas);
        btn_entregarDinero = (AppCompatButton) findViewById(R.id.btn_entregarDinero);


        // LLAMAR FUNCIONES DEL COMPONENTE
        chequearIntent();
        initToolbar();
        initComponent();

        // LLAMADAS A LA ESCUCHA
        fabInsertarEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Activity_Insertar_Entrega.class);
                i.putExtra("ID_JEFE", jefe.getId());
                startActivity(i);
                finish();
            }
        });

        // EVENTO A LA ESCUCHA DE ENTREGAR EL DINERO AL JEFE
        btn_entregarDinero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Activity_Reporte_Entrega_Dinero.class);
                intent.putExtra("ID_JEFE", jefe.getId());
                startActivity(intent);
                finish();
            }
        });

    }

    // METODO PARA CHEQUEAR EL INTENT EN CASO QUE SE INICIE DE OTRA ACTIVITY
    public void chequearIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            final SnackBar_Mensajes snackBar_mensajes = new SnackBar_Mensajes(this.parent_view, getLayoutInflater(), getResources(), "");
            if (intent.getStringExtra("ENTREGA_INSERTADA") != null) {
                snackBar_mensajes.setSms("Entrega insertada satisfactoriamente");
                snackBar_mensajes.mostrarMensajedeOK();
                initComponent();
            }
            if (intent.getStringExtra("ENTREGA_ELIMINADA") != null) {
                snackBar_mensajes.setSms("Entrega eliminada satisfactoriamente");
                snackBar_mensajes.mostrarMensajedeOK();
                initComponent();
            }
            if (intent.getStringExtra("ENTREGA_REALIZADA") != null) {
                String mensaje = "Entrega de " + intent.getStringExtra("ENTREGA_REALIZADA") + " realizada";
                snackBar_mensajes.setSms(mensaje);
                snackBar_mensajes.mostrarMensajedeOK();
                initComponent();
            }
            if (intent.getStringExtra("DINERO_ENTREGADO") != null) {
                snackBar_mensajes.setSms("Dinero entregado a " + jefe.getNombre());
                snackBar_mensajes.mostrarMensajedeOK();
                initComponent();
            }
            if (intent.getStringExtra("ESTADO_ENTREGA_EDITADO") != null) {
                snackBar_mensajes.setSms("Estado de entrega editado satisfactoriamente");
                snackBar_mensajes.mostrarMensajedeOK();
                initComponent();
            }
        }
    }

    // METODO PARA INICIAR EL TOOLBAR
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEntregas);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    // METODO PARA INICIAR EL COMPONENTE Y MOSTRAR LOS DATOS NECESARIOS
    private void initComponent() {
        listaEntregas = bd.obtenerEntregasPorJefe(jefe.getId());
        // ANNADIR TEXTOS A LA INSERTFAZ DEL JEFE SELECCIONADO
        String stringTXTJefes = "Entregas";
        txt_toolbarJefe.setText(stringTXTJefes);
        imageFoToPerfil.setImageBitmap(jefe.getFotoPerfil());
        txt_nombreJefe.setText(jefe.getNombre());
        String telefono = "Teléfono: " + jefe.getTelefono();
        txt_telefonoJefe.setText(telefono);
        String cantEntregas = String.valueOf(listaEntregas.size()) + " Entrega(s)";
        txt_cantidadEntregas.setText(cantEntregas);

        //COMPROBAR LA VISIBILIDAD DE LOS ELEMENTOS DEL COMPONENTE
        if (listaEntregas.size() == 0) {
            txt_listado.setVisibility(View.INVISIBLE);
            txt_cantidadEntregas.setVisibility(View.INVISIBLE);
            layoutEntregasVacio.setVisibility(View.VISIBLE);
            layoutEntregas.setVisibility(View.GONE);
            btn_entregarDinero.setVisibility(View.GONE);
        } else {
            txt_listado.setVisibility(View.VISIBLE);
            txt_cantidadEntregas.setVisibility(View.VISIBLE);
            layoutEntregasVacio.setVisibility(View.GONE);
            layoutEntregas.setVisibility(View.VISIBLE);
            btn_entregarDinero.setVisibility(View.VISIBLE);

            // INICIAR EL RECICLER VIEW CON SU RESPECTIVO ADAPTADOR
            final Adaptador_Entregas adaptador_entregas = new Adaptador_Entregas(this, listaEntregas, Activity_Entregas.this);
            recyclerViewEntregas = (RecyclerView) findViewById(R.id.recyclerViewEntregas);
            recyclerViewEntregas.setNestedScrollingEnabled(false);
            recyclerViewEntregas.setHasFixedSize(true);
            recyclerViewEntregas.setLayoutManager(new LinearLayoutManager(Activity_Entregas.this));
            recyclerViewEntregas.setAdapter(adaptador_entregas);
            adaptador_entregas.setClickListener(new Adaptador_Entregas.RecyclerTouchListener() {
                @Override
                public void onClickItem(View v, int position) {
                    Intent intent = new Intent(getApplicationContext(), Activity_Entrega_Jefe.class);
                    intent.putExtra("ID_ENTREGA", listaEntregas.get(position).getId());
                    intent.putExtra("ID_JEFE", jefe.getId());
                    startActivity(intent);
                    finish();
                }
            });
        }

    }

    // INICIAR EL MENU DEL ACTIVITY
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_entregas, menu);
        return true;
    }

    // METODO A LA ECUCHA DE LA FUNCIONALIDAD DE APRETAR EL BOTON DE LA FLECHA DEL TOOLBAR
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("CARGAR_COMPONTENTE", "OK");
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    // MEOTODOS A LA ESCUCHA CUANDO SE PRESIONA UN ITEM DEL MENU
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //CONFIRMAR Y ELIMINAR JEFE
        if (id == R.id.action_eliminarJefe) {
            Dialogo_Eliminar_Jefe dialogo_eliminar_jefe = new Dialogo_Eliminar_Jefe(this, jefe);
            dialogo_eliminar_jefe.eliminar();
        }

        // LLAMAR AL JEFE
        if (id == R.id.action_llamarJefe) {
            Dialogo_Tipo_Llamada dialogo_tipo_llamada = new Dialogo_Tipo_Llamada(this, Activity_Entregas.this, jefe.getTelefono());
            dialogo_tipo_llamada.mostrarDialogoTipoLlamada();
        }

        if (id == R.id.action_EditarJefe) {
            Intent intent = new Intent(getApplicationContext(), Activity_Editar_Jefe.class);
            intent.putExtra("ID_JEFE", jefe.getId());
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);

    }

    private String getIDJefe() {
        return getIntent().getStringExtra("ID");
    }

}
