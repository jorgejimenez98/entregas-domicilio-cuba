package com.example.jorge.entregasadomicilio.components;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.adapters.Adaptador_Producto;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Entrega;
import com.example.jorge.entregasadomicilio.model.Producto;
import com.example.jorge.entregasadomicilio.otros.dialogos.Dialogo_Agrandar_Foto;
import com.example.jorge.entregasadomicilio.otros.dialogos.Dialogo_Cambiar_Estado_Entrega;
import com.example.jorge.entregasadomicilio.otros.dialogos.Dialogo_Editar_Entrega;
import com.example.jorge.entregasadomicilio.otros.dialogos.Dialogo_Editar_Producto;
import com.example.jorge.entregasadomicilio.otros.dialogos.Dialogo_Eliminar_Entrega;
import com.example.jorge.entregasadomicilio.otros.dialogos.Dialogo_Insertar_Producto;
import com.example.jorge.entregasadomicilio.otros.dialogos.Dialogo_Terminar_Entrega;
import com.example.jorge.entregasadomicilio.otros.dialogos.Dialogo_Tipo_Llamada;
import com.example.jorge.entregasadomicilio.otros.dialogos.SnackBar_Mensajes;
import com.github.javiersantos.bottomdialogs.BottomDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Activity_Entrega_Jefe extends AppCompatActivity {
    static final int REQUEST_TAKE_PHOTO = 2;
    private static final int SELECT_FILE = 1;
    String currentPhotoPath;
    BaseDeDatos bd;
    Entrega entrega;
    private View parent_view;
    ArrayList<Producto> listaProducto = new ArrayList<>();

    // ELEMENTOS DEL COMPONENTE
    TextView txt_toolbarEntrega, txt_barrioYNombre, txt_telefonoJefeYDomicilio, txt_listado, txt_notasEntregas, txt_direccion;
    ImageView fotoEntrega, fotoEntregaDialogo;
    Button llamarClienteBoton;
    AppCompatButton btn_terminarVenta, bt_submit;
    LinearLayout layoutProductos, layoutProcutosVacio, layoutNotasEntrega, layoutDireccion;
    TextInputEditText txt_cantidad;
    FloatingActionButton fab_InsertarProducto;
    CardView cardDescription;
    RecyclerView recyclerViewProductos;
    ImageButton imgButtonLocalizacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__entrega);
        bd = new BaseDeDatos(this);
        entrega = bd.obtenerEntregaPorID(getIdEntrega());

        this.parent_view = findViewById(16908290);
        // INICIAR ELEMENTOS DEL COMPONENTE
        txt_toolbarEntrega = (TextView) findViewById(R.id.txt_toolbarEntrega);
        txt_barrioYNombre = (TextView) findViewById(R.id.txt_nombreJefeItem);
        txt_telefonoJefeYDomicilio = (TextView) findViewById(R.id.txt_telefonoJefeYDomicilio);
        fotoEntrega = (ImageView) findViewById(R.id.fotoEntrega);
        llamarClienteBoton = (Button) findViewById(R.id.llamarClienteBoton);
        btn_terminarVenta = (AppCompatButton) findViewById(R.id.btn_terminarVenta);
        layoutProductos = (LinearLayout) findViewById(R.id.layoutProductos);
        layoutProcutosVacio = (LinearLayout) findViewById(R.id.layoutProcutosVacio);
        txt_listado = (TextView) findViewById(R.id.txt_listado);
        layoutNotasEntrega = (LinearLayout) findViewById(R.id.layoutNotasEntrega);
        txt_notasEntregas = (TextView) findViewById(R.id.txt_notasEntregas);
        fab_InsertarProducto = (FloatingActionButton) findViewById(R.id.fab_InsertarProducto);
        layoutDireccion = (LinearLayout) findViewById(R.id.layoutDireccion);
        txt_direccion = (TextView) findViewById(R.id.txt_direccion);
        cardDescription = (CardView) findViewById(R.id.cardDescription);
        recyclerViewProductos = (RecyclerView) findViewById(R.id.recyclerViewProductos);
        bt_submit = (AppCompatButton) findViewById(R.id.bt_submit);
        txt_cantidad = (TextInputEditText) findViewById(R.id.txt_cantidad);
        imgButtonLocalizacion = (ImageButton) findViewById(R.id.imgButtonLocalizacion);


        // CHECKEAR INTENT
        chackearIntent();
        iniciarValoresDelComponente();
        initToolbar();

        // EVENTO A LA ESCUCHA PARA AGRANDAR LA FOTO
        fotoEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogo_Agrandar_Foto dialogo_agrandar_foto = new Dialogo_Agrandar_Foto(Activity_Entrega_Jefe.this, entrega, fotoEntrega);
                dialogo_agrandar_foto.mostrar();
            }
        });

        // EVENTO A LA ESCUCHA PARA LLAMAR AL CLIENTE
        llamarClienteBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarDialogoLlamada();
            }
        });

        // EVENTO A LA ESCUCHA PARA MOSTRAR EL FORMULARIO DEL PRODUCTO
        fab_InsertarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogo_Insertar_Producto dialogo_insertar_producto = new Dialogo_Insertar_Producto(getContext(), entrega, getParent_view(), getLayoutInflater(), getResources(), bd);
                dialogo_insertar_producto.mostrar();
            }
        });

        // EVENTO A LA ESCUCHA PARA TERMINAR LA VENTA
        btn_terminarVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogo_Terminar_Entrega dialogo_terminar_entrega = new Dialogo_Terminar_Entrega(getContext(), entrega, listaProducto, bd, getIDJefe(), getIntent());
                dialogo_terminar_entrega.mostrar();
            }
        });

    }

    // METODO PARA INICIAR EL TOOLBAR
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEntregas);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    // METODO A LA ECUCHA DE CUANDO SE PRESIONAR EL BOTON DE ATRAS DE LA BARRA
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("TODAS_ENTREGAS") != null) {
                Intent i = new Intent(getApplicationContext(), Activity_Todas_Entregas.class);
                startActivity(i);
                finish();
                return;
            }
        }
        Intent i = new Intent(getApplicationContext(), Activity_Entregas.class);
        i.putExtra("ID", getIDJefe());
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    public void iniciarValoresDelComponente() {
        this.invalidateOptionsMenu();
        // LLENAR VALORES DEL COMPONENTE DE LA ENTREGA Y COMPROBAR VISIBILIDAD DEL COMPONENTE
        String tituloBarra = "Entrega de " + entrega.getBarrioPrincipal();
        txt_toolbarEntrega.setText(tituloBarra);
        if (entrega.getEsPorFoto().equals("SI")) {
            fotoEntrega.setImageBitmap(entrega.getFotoDireccion());
            fotoEntrega.setTag("OtraFoto");
        } else {
            fotoEntrega.setImageResource(R.drawable.img_no_friend);
            fotoEntrega.setTag("fotoVacia");
        }
        String barrioPrincipal = entrega.getBarrioPrincipal();
        String nombreCliente = entrega.getNombreCliente();
        if (nombreCliente.equals("")) {
            txt_barrioYNombre.setText(barrioPrincipal);
        } else {
            String nombreYBarrio = barrioPrincipal + " (" + nombreCliente + ")";
            txt_barrioYNombre.setText(nombreYBarrio);
        }
        String telefono = entrega.getTelefonoCliente();
        if (telefono.equals("")) {
            llamarClienteBoton.setVisibility(View.GONE);
            String domicilio = "Dom: " + entrega.getPrecioDomicilio();
            txt_telefonoJefeYDomicilio.setText(domicilio);
        } else {
            llamarClienteBoton.setVisibility(View.VISIBLE);
            String telefonoYDomicilio = "Cel: " + entrega.getTelefonoCliente() + " - " + " Dom: " + entrega.getPrecioDomicilio() + " cup";
            txt_telefonoJefeYDomicilio.setText(telefonoYDomicilio);
        }
        if (entrega.getNotas().equals("") && entrega.getDireccion().equals("")) {
            cardDescription.setVisibility(View.GONE);
        } else {
            cardDescription.setVisibility(View.VISIBLE);
            if (entrega.getNotas().equals("")) {
                layoutNotasEntrega.setVisibility(View.GONE);
            } else {
                layoutNotasEntrega.setVisibility(View.VISIBLE);
                txt_notasEntregas.setText(entrega.getNotas());
            }
            if (entrega.getDireccion().equals("")) {
                layoutDireccion.setVisibility(View.GONE);
            } else {
                layoutDireccion.setVisibility(View.VISIBLE);
                txt_direccion.setText(entrega.getDireccion());
            }
        }


        // INICIAR EL LISTADO DE PRODUCTOS
        listaProducto = bd.obtenerProductosPorEntrega(entrega.getId());
        String tituloLista = "Listado: " + listaProducto.size() + " producto(s)";
        txt_listado.setText(tituloLista);
        if (listaProducto.size() == 0) {
            layoutProcutosVacio.setVisibility(View.VISIBLE);
            layoutProductos.setVisibility(View.GONE);
            btn_terminarVenta.setVisibility(View.GONE);
        } else {
            layoutProcutosVacio.setVisibility(View.GONE);
            layoutProductos.setVisibility(View.VISIBLE);
            btn_terminarVenta.setVisibility(View.VISIBLE);

            final Adaptador_Producto adaptador_producto = new Adaptador_Producto(this, listaProducto, getIDJefe());
            recyclerViewProductos = (RecyclerView) findViewById(R.id.recyclerViewProductos);
            recyclerViewProductos.setNestedScrollingEnabled(false);
            recyclerViewProductos.setHasFixedSize(true);
            recyclerViewProductos.setLayoutManager(new LinearLayoutManager(Activity_Entrega_Jefe.this));
            recyclerViewProductos.setAdapter(adaptador_producto);

        }
    }

    // OBTENER ID DE LA ENTREGA A TRAVEZ DEL INTENT
    public String getIdEntrega() {
        return getIntent().getStringExtra("ID_ENTREGA");
    }

    // OBTENER EL ID DEL JEFE A TRAVEZ DEL INTENT
    public String getIDJefe() {
        return getIntent().getStringExtra("ID_JEFE");
    }

    // CREACION DEL MENU DE LA ACTIVIDAD
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_entrega, menu);
        return true;
    }

    // EVENTOS A LA ESCUCHA PARA CUANDO SE PRESIONA UN ITEM DEL MENU
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //CONFIRMAR Y ELIMINAR JEFE
        if (id == R.id.action_eliminarEntrega) {
            Dialogo_Eliminar_Entrega dialogo_eliminar_entrega = new Dialogo_Eliminar_Entrega(this, entrega, bd, getIDJefe(), getIntent());
            dialogo_eliminar_entrega.mostrar();
            return true;
        }

        // EDITAR FOTO DIRECCION ENTREGA
        if (id == R.id.actionEditarFotoEntrega) {
            final Dialog dialog = new Dialog(Activity_Entrega_Jefe.this);
            dialog.requestWindowFeature(1);
            dialog.setContentView(R.layout.dialogo_editar_foto_entrega);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setCancelable(false);
            final ImageView image_FotoEntrega = (ImageView) dialog.findViewById(R.id.image_FotoEntrega);
            fotoEntregaDialogo = image_FotoEntrega;
            image_FotoEntrega.setImageBitmap(entrega.getFotoDireccion());
            image_FotoEntrega.setTag("OtraFoto");
            FloatingActionButton fab_BotonFotoEntrega = (FloatingActionButton) dialog.findViewById(R.id.fab_BotonFotoEntrega);
            fab_BotonFotoEntrega.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mostrarDialogoDeSeleccionDeFoto();
                }
            });
            FloatingActionButton fabCambiarRotacion = (FloatingActionButton) dialog.findViewById(R.id.fabCambiarRotacion);
            fabCambiarRotacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // CAMBIAR ROTACION DE LA IMAGEN
                    if (fotoEntregaDialogo.getRotation() == 0) {
                        fotoEntregaDialogo.setRotation(90);
                    } else if (fotoEntregaDialogo.getRotation() == 90) {
                        fotoEntregaDialogo.setRotation(180);
                    } else if (fotoEntregaDialogo.getRotation() == 180) {
                        fotoEntregaDialogo.setRotation(270);
                    } else if (fotoEntregaDialogo.getRotation() == 270) {
                        fotoEntregaDialogo.setRotation(360);
                    } else if (fotoEntregaDialogo.getRotation() == 360) {
                        fotoEntregaDialogo.setRotation(0);
                    }
                }
            });
            AppCompatButton bt_cancel = (AppCompatButton) dialog.findViewById(R.id.bt_cancel);
            bt_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            AppCompatButton bt_submit = (AppCompatButton) dialog.findViewById(R.id.bt_submit);
            bt_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fotoEntregaDialogo.buildDrawingCache();
                    Bitmap bmap = fotoEntregaDialogo.getDrawingCache();
                    entrega.setFotoDireccion(bmap);
                    bd.actualizarEntrega(entrega.getId(), entrega);

                    Intent intent = new Intent(getApplicationContext(), Activity_Entrega_Jefe.class);
                    intent.putExtra("FOTO_EDITADA", "OK");
                    intent.putExtra("ID_ENTREGA", entrega.getId());
                    intent.putExtra("ID_JEFE", getIDJefe());
                    startActivity(intent);
                    finish();
                }
            });
            dialog.show();
        }

        if (id == R.id.action_EditarOtrasCaracteristicas) {
            Dialogo_Editar_Entrega dialogo_editar_entrega = new Dialogo_Editar_Entrega(this, entrega, bd, getIDJefe());
            dialogo_editar_entrega.mostrar();
        }

        if (id == R.id.action_CambiarEstadoEntrega) {
            Dialogo_Cambiar_Estado_Entrega dialogo_cambiar_estado_entrega = new Dialogo_Cambiar_Estado_Entrega(this, entrega, bd, getIDJefe(), getIntent());
            dialogo_cambiar_estado_entrega.mostrar();
            this.invalidateOptionsMenu();
        }

        if (id == R.id.action_ubicacion) {
            Intent intent = new Intent(getApplicationContext(), Activity_Mapa_Direccion.class);
            intent.putExtra("DIRECCION", entrega.getDireccion());
            intent.putExtra("ID_ENTREGA", entrega.getId());
            intent.putExtra("ID_JEFE", getIDJefe());
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    // FUNCION A LA ESPERA DEL RESULTADO DE USAR LA CAMARA
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            galleryAddPic();
            setPic();
        } else {
            Uri selectedImage;
            switch (requestCode) {
                case SELECT_FILE:
                    if (resultCode == Activity.RESULT_OK) {
                        selectedImage = imageReturnedIntent.getData();
                        String selectedPath = selectedImage.getPath();
                        if (requestCode == SELECT_FILE) {
                            if (selectedPath != null) {
                                InputStream imageStream = null;
                                try {
                                    imageStream = getContentResolver().openInputStream(selectedImage);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                Bitmap bmp = BitmapFactory.decodeStream(imageStream);
                                fotoEntregaDialogo.setImageBitmap(bmp);
                                fotoEntregaDialogo.setTag("Otra Foto");
                            }
                        }
                    }
                    break;
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic() {
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        fotoEntregaDialogo.setImageBitmap(bitmap);
        fotoEntregaDialogo.setRotation(90);
        fotoEntregaDialogo.setTag("Otra foto");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemFoto = menu.findItem(R.id.actionEditarFotoEntrega);
        itemFoto.setVisible(fotoEntrega.getTag() != "fotoVacia");
        MenuItem itemEstado = menu.findItem(R.id.action_CambiarEstadoEntrega);
        itemEstado.setVisible(entrega.getSeRealizoEntrega().equals("SI"));
        return super.onPrepareOptionsMenu(menu);
    }


    // FUNCION PARA CHECKEAR EL INTENT EN CASO DE QUE SE MANDEN DATOS A TRAVEZ DEL MISMO
    public void chackearIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            final SnackBar_Mensajes snackBar_mensajes = new SnackBar_Mensajes(getParent_view(), getLayoutInflater(), getResources(), "");
            if (intent.getStringExtra("PRODUCTO_ELIMINADO") != null) {
                iniciarValoresDelComponente();
                snackBar_mensajes.setSms("Producto eliminado satisfactoriamente");
                snackBar_mensajes.mostrarMensajedeOK();
            }
            if (intent.getStringExtra("FOTO_EDITADA") != null) {
                snackBar_mensajes.setSms("Foto editada satisfactoriamente");
                snackBar_mensajes.mostrarMensajedeOK();
            }
            if (intent.getStringExtra("ENTREGA_EDITADA") != null) {
                snackBar_mensajes.setSms("Entrega editada satisfactoriamente");
                snackBar_mensajes.mostrarMensajedeOK();
            }
            if (intent.getStringExtra("EDITAR_PRODUCTO") != null) {
                Dialogo_Editar_Producto dialogo_editar_producto = new Dialogo_Editar_Producto(bd, intent.getStringExtra("ID_PRODUCTO"), this, getParent_view(), getLayoutInflater(), getResources(), entrega);
                dialogo_editar_producto.mostrar();
            }
        }
    }

    public void mostrarDialogoDeSeleccionDeFoto() {
        new BottomDialog.Builder(this)
                .setTitle("Opciones de Foto")
                .setCancelable(true)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveText("Seleccionar de Galeria")
                .setNegativeText("Hacer Foto")
                .setPositiveTextColorResource(android.R.color.white)
                .setNegativeTextColorResource(android.R.color.black)
                .setPositiveBackgroundColorResource(android.R.color.holo_blue_light)
                .onPositive(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(@NonNull BottomDialog bottomDialog) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Seleccione una imagen"), SELECT_FILE);
                        bottomDialog.dismiss();
                        bottomDialog.dismiss();
                    }
                })
                .onNegative(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(@NonNull BottomDialog bottomDialog) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                            }
                            if (photoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), "com.example.android.fileprovider", photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                            }
                        }
                        bottomDialog.dismiss();
                    }
                })
                .show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // METODO PARA ABRIR EL DIALOGO DE SELECCION DE LLAMADA
    public void iniciarDialogoLlamada() {
        Dialogo_Tipo_Llamada dialogo_tipo_llamada = new Dialogo_Tipo_Llamada(this, Activity_Entrega_Jefe.this, entrega.getTelefonoCliente());
        dialogo_tipo_llamada.mostrarDialogoTipoLlamada();
    }

    public View getParent_view() {
        return this.parent_view;
    }

    public Context getContext() {
        return this;
    }

}
