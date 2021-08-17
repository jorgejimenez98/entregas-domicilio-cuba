package com.example.jorge.entregasadomicilio;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jorge.entregasadomicilio.adapters.Adaptador_Jefes;
import com.example.jorge.entregasadomicilio.components.Activity_Estadisticas;
import com.example.jorge.entregasadomicilio.components.Activity_Insertar_Jefe;
import com.example.jorge.entregasadomicilio.components.Activity_Entregas;
import com.example.jorge.entregasadomicilio.components.Activity_Todas_Entregas;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Jefe;
import com.example.jorge.entregasadomicilio.otros.HelpFunctions;
import com.example.jorge.entregasadomicilio.otros.dialogos.Dialogo_Informacion;
import com.example.jorge.entregasadomicilio.otros.dialogos.SnackBar_Mensajes;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private View parent_view;
    ArrayList<Jefe> listaJefes = new ArrayList<>();
    HelpFunctions hp = new HelpFunctions();
    BaseDeDatos bd;
    long backPressedTime = 0;
    // ELEMENTOS DEL COMPONENTE
    RecyclerView recyclerView;
    LinearLayout layoutJefes, layoutJefesVacios;
    TextView txt_cantidadJefes;
    AppCompatButton btn_consultarTodasEntregas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // INICIAR LOS ELEMENTOS DEL COMPONENTE
        bd = new BaseDeDatos(this);
        this.parent_view = findViewById(16908290);
        layoutJefesVacios = (LinearLayout) findViewById(R.id.layoutJefesVacio);
        layoutJefes = (LinearLayout) findViewById(R.id.layoutJefes);
        txt_cantidadJefes = (TextView) findViewById(R.id.txt_cantidadJefes);
        btn_consultarTodasEntregas = (AppCompatButton) findViewById(R.id.btn_consultarTodasEntregas);
        // LLAMADAS A FUNCIONES
        requestPermission();
        initToolbar();
        comprobarIntent();
        initNavigationDrawer();
        initComponent();



        btn_consultarTodasEntregas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Activity_Todas_Entregas.class);
                startActivity(intent);
                finish();
            }
        });

    }

    // COMPROBAR QUE SE HA ELIMINADO O INSERTADO UN JEFE
    private void comprobarIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            SnackBar_Mensajes snackBar_mensajes = new SnackBar_Mensajes(this.parent_view, getLayoutInflater(), getResources(), "");
            if (intent.getStringExtra("ELIMINAR_JEFE") != null) {
                bd.eliminarJefe(intent.getStringExtra("ID"));
                snackBar_mensajes.setSms("Jefe eliminado satisfactoriamente");
                snackBar_mensajes.mostrarMensajedeOK();
            }
            if (intent.getStringExtra("INSERTAR_JEFE") != null) {
                snackBar_mensajes.setSms("Jefe Insertado Satisfactoriamente");
                snackBar_mensajes.mostrarMensajedeOK();
                initComponent();
            }
            if (intent.getStringExtra("EDITAR_JEFE") != null) {
                snackBar_mensajes.setSms("Jefe editado satisfactoriamente");
                snackBar_mensajes.mostrarMensajedeOK();
                initComponent();
            }
            if (intent.getStringExtra("CARGAR_COMPONTENTE") != null) {
                initComponent();
            }
        }
    }

    // INIT TOOLBAR
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon((int) R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabInsetarJefe);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Activity_Insertar_Jefe.class);
                startActivity(i);
                finish();
            }
        });
    }

    // INIT DRAWER NAVIGATION
    private void initNavigationDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // INICIAR EL COMPONENTE
    private void initComponent() {
        listaJefes = obtenerJefes();
        if (listaJefes.size() == 0) {
            layoutJefesVacios.setVisibility(View.VISIBLE);
            layoutJefes.setVisibility(View.GONE);
        } else {
            // INICIAR VALORES DEL COMPONTENTE SI LA LISTA DE JEFES NO ESTA VACIA
            layoutJefesVacios.setVisibility(View.GONE);
            layoutJefes.setVisibility(View.VISIBLE);
            // ANNADIR TITULO EN LA TABLA
            String tituloTabla = listaJefes.size() + " Jefes(S)";
            txt_cantidadJefes.setText(tituloTabla);
            // INICIAR RECICLER VIEW
            final Adaptador_Jefes bossAdapter = new Adaptador_Jefes(this, listaJefes, bd, MainActivity.this, getResources());
            recyclerView = (RecyclerView) findViewById(R.id.recyclerViewJefes);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            recyclerView.setAdapter(bossAdapter);
            bossAdapter.setClickListener(new Adaptador_Jefes.RecyclerTouchListener() {
                @Override
                public void onClickItem(View v, int position) {
                    Intent i = new Intent(getApplicationContext(), Activity_Entregas.class);
                    i.putExtra("ID", listaJefes.get(position).getId());
                    startActivity(i);
                    finish();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Toast backToast = Toast.makeText(getApplicationContext(), "Presione otra vez para salir de la Aplicación", Toast.LENGTH_LONG);
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                backToast.cancel();
                super.onBackPressed();
                return;
            } else {
                backToast.show();
            }
            backPressedTime = System.currentTimeMillis();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_InsertarJefe) {
            Intent i = new Intent(getApplicationContext(), Activity_Insertar_Jefe.class);
            startActivity(i);
            finish();
        }

        // MOSTRAR LA INFORMACION DE LA APLICACION
        if (id == R.id.nav_Informacion) {
            Dialogo_Informacion dialogo_informacion = new Dialogo_Informacion(this);
            dialogo_informacion.mostrarDialogo();
        }

        // MOSTRAR ESTADISTICAS DE LA APK
        if (id == R.id.nav_estadisticas) {
            Intent intent = new Intent(getApplicationContext(), Activity_Estadisticas.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // PERMISO DE LLAMADAS
    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
    }

    public ArrayList<Jefe> obtenerJefes() {
        ArrayList<Jefe> listaOriginal = bd.obtenerJefes();
        ArrayList<Jefe> jefesConEntregas = new ArrayList<>();
        ArrayList<Jefe> jefesSinEntregas = new ArrayList<>();
        ArrayList<Jefe> listaADevolver = new ArrayList<>();
        for (Jefe jefe : listaOriginal) {
            if (bd.obtenerEntregasPorJefe(jefe.getId()).size() == 0) {
                jefesSinEntregas.add(jefe);
            } else {
                jefesConEntregas.add(jefe);
            }
        }
        listaADevolver.addAll(jefesConEntregas);
        listaADevolver.addAll(jefesSinEntregas);
        return listaADevolver;
    }


}
