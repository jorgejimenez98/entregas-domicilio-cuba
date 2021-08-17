package com.example.jorge.entregasadomicilio.components;

import android.app.Activity;
import android.app.Dialog;
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
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jorge.entregasadomicilio.otros.HelpFunctions;
import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Jefe;
import com.example.jorge.entregasadomicilio.otros.dialogos.SnackBar_Mensajes;
import com.github.javiersantos.bottomdialogs.BottomDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Activity_Insertar_Entrega extends AppCompatActivity {

    private static final int SELECT_FILE = 1;
    static final int REQUEST_TAKE_PHOTO = 2;
    private View parent_view;

    String currentPhotoPath;
    BaseDeDatos bd;
    Jefe jefe;
    HelpFunctions hp = new HelpFunctions();
    boolean esEntregaDeFoto;

    // ELEMENTOS DEL COMPONENTE
    TextView txt_toolbarInsertarEntrega, txt_PreciosBasicos, txt_preciosNormales;
    LinearLayout layout_formularioEntregasNotas, layout_formularioEntregasTodos, layout_formularioEntregasFoto, layout_seleccionTipoEntrega, layout_OtroPrecio;
    TextInputEditText text_BarrioPrincipal, txt_notasEntregas, txt_nombreCliente, txt_TelefonoCliente, text_Direccion, txt_otroPrecio;
    RadioGroup rg_seleccionTipoEntrega, rg_preciosDomicilio;
    FloatingActionButton fab_BotonFotoEntrega, fab_submitEntrega;
    ImageView image_FotoEntrega;
    RadioButton rb_OtroPrecio, rb_precio25;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_entrega);
        bd = new BaseDeDatos(this);
        this.parent_view = findViewById(16908290);
        // LLAMAR A LA BASE DE DATOS
        jefe = bd.obtenerJefePorID(getIDJefe());
        esEntregaDeFoto = false;

        // INCIAR ELEMENTOS DEL COMPONENTE
        txt_toolbarInsertarEntrega = (TextView) findViewById(R.id.txt_toolbarInsertarEntrega);
        txt_PreciosBasicos = (TextView) findViewById(R.id.txt_PreciosBasicos);
        txt_preciosNormales = (TextView) findViewById(R.id.txt_preciosNormales);
        layout_formularioEntregasNotas = (LinearLayout) findViewById(R.id.layout_formularioEntregasNotas);
        layout_formularioEntregasTodos = (LinearLayout) findViewById(R.id.layout_formularioEntregasTodos);
        layout_formularioEntregasFoto = (LinearLayout) findViewById(R.id.layout_formularioEntregasFoto);
        layout_seleccionTipoEntrega = (LinearLayout) findViewById(R.id.layout_seleccionTipoEntrega);
        layout_OtroPrecio = (LinearLayout) findViewById(R.id.layout_OtroPrecio);
        rg_seleccionTipoEntrega = (RadioGroup) findViewById(R.id.rg_seleccionTipoEntrega);
        rg_preciosDomicilio = (RadioGroup) findViewById(R.id.rg_preciosDomicilio);
        image_FotoEntrega = (ImageView) findViewById(R.id.image_FotoEntrega);
        text_BarrioPrincipal = (TextInputEditText) findViewById(R.id.text_BarrioPrincipal);
        txt_notasEntregas = (TextInputEditText) findViewById(R.id.txt_notasEntregas);
        txt_nombreCliente = (TextInputEditText) findViewById(R.id.txt_nombreCliente);
        txt_TelefonoCliente = (TextInputEditText) findViewById(R.id.text_TelefonoCliente);
        text_Direccion = (TextInputEditText) findViewById(R.id.text_Direccion);
        txt_otroPrecio = (TextInputEditText) findViewById(R.id.txt_otroPrecio);
        fab_BotonFotoEntrega = (FloatingActionButton) findViewById(R.id.fab_BotonFotoEntrega);
        fab_submitEntrega = (FloatingActionButton) findViewById(R.id.fab_submitEntrega);
        rb_OtroPrecio = (RadioButton) findViewById(R.id.rb_OtroPrecio);
        rb_precio25 = (RadioButton) findViewById(R.id.rb_25Pesos);


        initToolbar();
        initComponent();

        // EVENTO A LA ESCUCHA DEL SUBMIT
        fab_submitEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarVisibilidadDelComponente(view);
            }
        });
        // EVENTO A LA ESCUCHA DE OTRO PRECIO DE DOMICILIO
        rb_OtroPrecio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_OtroPrecio.setChecked(false);
                rb_precio25.setChecked(true);
                txt_preciosNormales.setVisibility(View.GONE);
                txt_PreciosBasicos.setVisibility(View.VISIBLE);
                rg_preciosDomicilio.setVisibility(View.GONE);
                layout_OtroPrecio.setVisibility(View.VISIBLE);
            }
        });
        txt_PreciosBasicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_preciosNormales.setVisibility(View.VISIBLE);
                txt_PreciosBasicos.setVisibility(View.GONE);
                rg_preciosDomicilio.setVisibility(View.VISIBLE);
                layout_OtroPrecio.setVisibility(View.GONE);
            }
        });
        // EVENTO  A LA ESCUCHA DE LA CAMARA
        fab_BotonFotoEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoDeSeleccionDeFoto();
            }
        });

    }

    // INICIAR EL TOOLBAR
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarInsertarEntregas);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    // INICIAR EL COMPONENTE
    private void initComponent() {
        // ANNADIR TEXTOS A LA INSERTFAZ DEL JEFE SELECCIONADO
        String stringTXTJefes = "Insertar Entrega";
        txt_toolbarInsertarEntrega.setText(stringTXTJefes);
    }

    // CREACION DEL MENU
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_insertar_entrega, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem resetearFormulario = menu.findItem(R.id.action_resetarFormularioEntrega);
        MenuItem itemFoto = menu.findItem(R.id.acttion_cambiarPorFoto);
        MenuItem itemNotas = menu.findItem(R.id.acttion_cambiarNotas);
        if (esEntregaDeFoto) {
            itemFoto.setVisible(false);
            itemNotas.setVisible(true);
        } else {
            itemFoto.setVisible(true);
            itemNotas.setVisible(false);
        }
        if (layout_seleccionTipoEntrega.getVisibility() == View.VISIBLE) {
            itemFoto.setVisible(false);
            itemNotas.setVisible(false);
            resetearFormulario.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    // FUNCIONES AL DARLE CLICK A UN ELEMENTO DEL MENU
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == 16908332) {
            finish();
        }
        if (id == R.id.action_resetarFormularioEntrega) {
            resetearFormulario();
        }
        if (id == R.id.acttion_cambiarPorFoto) {
            layout_seleccionTipoEntrega.setVisibility(View.GONE);
            layout_formularioEntregasFoto.setVisibility(View.VISIBLE);
            layout_formularioEntregasNotas.setVisibility(View.GONE);
            layout_formularioEntregasTodos.setVisibility(View.VISIBLE);
            esEntregaDeFoto = true;
        }
        if (id == R.id.acttion_cambiarNotas) {
            layout_seleccionTipoEntrega.setVisibility(View.GONE);
            layout_formularioEntregasFoto.setVisibility(View.GONE);
            layout_formularioEntregasNotas.setVisibility(View.VISIBLE);
            layout_formularioEntregasTodos.setVisibility(View.VISIBLE);
            esEntregaDeFoto = false;
        }
        return super.onOptionsItemSelected(menuItem);
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
                                image_FotoEntrega.setImageBitmap(bmp);
                                image_FotoEntrega.setTag("Otra Foto");
                            }
                        }
                    }
                    break;
            }
        }
    }

    // FUNCION PARA CAMBIAR EL ESTADO VISUAL DEL COMPONENTE
    public void cambiarVisibilidadDelComponente(View view) {
        if (rg_seleccionTipoEntrega.getCheckedRadioButtonId() == -1) {
            Snackbar.make(view, "Debes seleccionar obligatoriamente una opción", Snackbar.LENGTH_LONG).setAction("Danger", null).show();
        } else {
            if (layout_seleccionTipoEntrega.getVisibility() == View.VISIBLE) {
                layout_seleccionTipoEntrega.setVisibility(View.GONE);
                if (rg_seleccionTipoEntrega.getCheckedRadioButtonId() == R.id.rb_entregaFoto) {
                    layout_formularioEntregasFoto.setVisibility(View.VISIBLE);
                    esEntregaDeFoto = true;
                }
                if (rg_seleccionTipoEntrega.getCheckedRadioButtonId() == R.id.rb_entregaNotas) {
                    layout_formularioEntregasNotas.setVisibility(View.VISIBLE);
                    esEntregaDeFoto = false;
                }
                this.invalidateOptionsMenu();
                layout_formularioEntregasTodos.setVisibility(View.VISIBLE);
            } else {
                submitFormulariodeEntrega(view);
            }
        }
    }

    // FUNCION PARA RESETEAR FORMULARIO
    public void resetearFormulario() {
        if (esEntregaDeFoto) {
            image_FotoEntrega.setImageResource(R.drawable.ic_location);
            image_FotoEntrega.setTag("entregaVacia");
        } else {
            txt_nombreCliente.setText("");
            txt_TelefonoCliente.setText("");
            text_Direccion.setText("");
        }
        text_BarrioPrincipal.setText("");
        txt_notasEntregas.setText("");
        Toast.makeText(getApplicationContext(), "Formulario reseteado", Toast.LENGTH_SHORT).show();
    }

    // FUNCION PARA EL SUBMIT DEL FORMULARIO DE LA ENTREGA
    public void submitFormulariodeEntrega(View view) {
        final SnackBar_Mensajes snackBar_mensajes = new SnackBar_Mensajes(this.parent_view, getLayoutInflater(), getResources(), "");
        try {
            validarFormulario();
            // CONVERTIR FOTO A BYTE ARRAY
            image_FotoEntrega.buildDrawingCache();
            byte[] fotoEntrega = {};
            String entregaFoto = "NO";
            if (esEntregaDeFoto) {
                Bitmap bmap = image_FotoEntrega.getDrawingCache();
                fotoEntrega = hp.getBytes(bmap);
                entregaFoto = "SI";
            }
            RadioButton precioDomicilio = (RadioButton) findViewById(rg_preciosDomicilio.getCheckedRadioButtonId());
            String precioDom = "0";
            if (!precioDomicilio.getText().toString().equals("Gratis")) {
                precioDom = precioDomicilio.getText().toString();
            }
            if (txt_PreciosBasicos.getVisibility() == View.VISIBLE) {
                precioDom = txt_otroPrecio.getText().toString();
            }
            // INSERTAR LA ENTREGA EN LA BASE DE DATOS
            bd.insertarEntrega(
                    jefe.getId(),
                    text_BarrioPrincipal.getText().toString(),
                    txt_TelefonoCliente.getText().toString().trim(),
                    txt_nombreCliente.getText().toString(),
                    text_Direccion.getText().toString(),
                    precioDom,
                    "NO", 0,
                    txt_notasEntregas.getText().toString(),
                    entregaFoto, fotoEntrega, "NO"
            );
            Intent intent = new Intent(getApplicationContext(), Activity_Entregas.class);
            intent.putExtra("ID", jefe.getId());
            intent.putExtra("ENTREGA_INSERTADA", "OK");
            startActivity(intent);
            finish();
        } catch (Exception e) {
            snackBar_mensajes.setSms(e.getMessage());
            snackBar_mensajes.mostrarMensajedeError();
        }
    }

    // FUNCION PARA VALIDAR EL FORMULARIO
    public void validarFormulario() throws Exception {
        if (esEntregaDeFoto) {
            if (image_FotoEntrega.getTag().equals("entregaVacia")) {
                throw new Exception("La foto de la entrega es requerida, sino inserte la entrega manualmente");
            }
        } else {
            if (text_Direccion.getText().toString().trim().length() == 0) {
                throw new Exception("La Dirección del Cliente no sebe estar vacío");
            }
        }
        if (layout_OtroPrecio.getVisibility() == View.VISIBLE) {
            if (txt_otroPrecio.getText().toString().trim().length() == 0) {
                throw new Exception("El precio de domicilio es requerido");
            }
        }
        if (text_BarrioPrincipal.getText().toString().trim().length() == 0) {
            throw new Exception("El barrio principal es obligatorio");
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Activity_Entregas.class);
        intent.putExtra("ID", jefe.getId());
        startActivity(intent);
        super.onBackPressed();
    }

    private String getIDJefe() {
        return getIntent().getStringExtra("ID_JEFE");
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

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic() {
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        image_FotoEntrega.setImageBitmap(bitmap);
        image_FotoEntrega.setRotation(90);
        image_FotoEntrega.setTag("Otra foto");
    }

}
