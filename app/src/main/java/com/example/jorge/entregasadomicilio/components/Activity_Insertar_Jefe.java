package com.example.jorge.entregasadomicilio.components;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jorge.entregasadomicilio.otros.HelpFunctions;
import com.example.jorge.entregasadomicilio.MainActivity;
import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.otros.dialogos.SnackBar_Mensajes;
import com.github.javiersantos.bottomdialogs.BottomDialog;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Activity_Insertar_Jefe extends AppCompatActivity {
    BaseDeDatos bd;
    HelpFunctions hpFuncionts = new HelpFunctions();
    // VARIABLES PARA LA ESPERA DEL USO DE LA CAMARA
    private static final int CAMERA_REQUEST = 500;
    private static final int SELECT_FILE = 1;
    // ELEMENTOS DEL COMPONENTE
    private TextInputEditText nombre, telefono;
    private ImageView fotoPerfilJefe;
    private FloatingActionButton fabFotoPerfil, fabSubmitForm;
    private View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar__jefe2);
        bd = new BaseDeDatos(this);
        // Creacion de Variables de los Elementos del Componente
        this.parent_view = findViewById(16908290);
        nombre = (TextInputEditText) findViewById(R.id.txt_nombreJefe);
        telefono = (TextInputEditText) findViewById(R.id.text_TelefonoJefe);
        fotoPerfilJefe = (ImageView) findViewById(R.id.image_FotoPerfilJefes);
        fabFotoPerfil = (FloatingActionButton) findViewById(R.id.fab_BotonFotoJefe);
        fabSubmitForm = (FloatingActionButton) findViewById(R.id.fab_SubmitJefe);
        fotoPerfilJefe.setImageResource(R.drawable.usuarioblanco);

        // CREACION DE LOS BOTONES DE EVENTOS A LA ESCUCHA
        fabFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDialogoSeleccionFoto(view);
            }
        });
        fabSubmitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFormulario(v);
            }
        });

        // INICIAR EL TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarInsertarJefe);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    // FUNCION A LA ESCUCHA AL DAR CLICK EN LA FECHA DE RETROCEDER LA ACTIVIDAD
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

    private void submitFormulario(View v) {
        final SnackBar_Mensajes snackBar_mensajes = new SnackBar_Mensajes(this.parent_view, getLayoutInflater(), getResources(), "");
        try {
            validarFormulacio();
            // CONVERTIR FOTO A BYTE ARRAY
            fotoPerfilJefe.buildDrawingCache();
            Bitmap bmap = fotoPerfilJefe.getDrawingCache();
            byte[] foto = hpFuncionts.getBytes(bmap);
            // INSERTAR JEFE Y VOLVER A LA MAIN ACTIVITY
            bd.insertarJefe(foto, nombre.getText().toString(), telefono.getText().toString(), 0, 0);
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("INSERTAR_JEFE", "OK");
            startActivity(i);
            finish();
        } catch (Exception e) {
            snackBar_mensajes.setSms(e.getMessage());
            snackBar_mensajes.mostrarMensajedeError();
        }
    }

    private void abrirDialogoSeleccionFoto(View view) {
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
                    }
                })
                .onNegative(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(@NonNull BottomDialog bottomDialog) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        bottomDialog.dismiss();
                    }
                })
                .show();
    }


    // CREACION DEL MENU
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_insertar_jefe, menu);
        return true;
    }

    // FUNCION A LA ESPERA DEL RESULTADO DE USAR LA CAMARA
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
            fotoPerfilJefe.setImageBitmap(photo);
            fotoPerfilJefe.setTag("Otra Foto");
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
                                fotoPerfilJefe.setImageBitmap(bmp);
                                fotoPerfilJefe.setTag("Otra Foto");
                            }
                        }
                    }
                    break;
            }
        }
    }

    // FUNCIONES AL DARLE CLICK A UN ELEMENTO DEL MENU
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == 16908332) {
            finish();
        }
        if (id == R.id.action_resetarFormulario) {
            resetearFormulario();
            Toast.makeText(getApplicationContext(), "Formulario reseteado", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    // VALIDACION DEL FORMULARIO
    private void validarFormulacio() throws Exception {
        if (fotoPerfilJefe.getTag().toString().equals("usuarioBlanco")) {
            throw new Exception("Debes seleccionar una foto de perfíl del Jefe");
        }
        if (nombre.getText().toString().trim().length() == 0) {
            throw new Exception("El nombre del Jefe no sebe estar vacío");
        }
        if (telefono.getText().toString().trim().length() == 0) {
            throw new Exception("El Teléfono del Jefe no sebe estar vacío");
        }
    }

    // FUNCION PARA RESETEAR EL FORMULARIO
    public void resetearFormulario() {
        fotoPerfilJefe.setImageResource(R.drawable.usuarioblanco);
        fotoPerfilJefe.setTag("usuarioBlanco");
        nombre.setText("");
        telefono.setText("");
    }

}

