package com.example.jorge.entregasadomicilio.components;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jorge.entregasadomicilio.MainActivity;
import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.map.MyMarker;
import com.example.jorge.entregasadomicilio.map.PlaceMarker;
import com.example.jorge.entregasadomicilio.model.Entrega;
import com.example.jorge.entregasadomicilio.model.Place;
import com.example.jorge.entregasadomicilio.otros.Utils;
import com.example.jorge.entregasadomicilio.otros.dialogos.Dialogo_Agrandar_Foto;

import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Circle;
import org.mapsforge.map.layer.overlay.FixedPixelCircle;
import org.mapsforge.map.layer.overlay.Polyline;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Activity_Mapa_Direccion extends AppCompatActivity {
    private TileCache tileCache;
    private TileRendererLayer tileRendererLayer;
    private String provider, RUTA;
    private Location loc;
    private MyMarker marker;
    private Double longitud, latitud;

    LocationManager locManager;
    MapView mapView;
    BaseDeDatos baseDeDatos;
    Entrega entrega;
    ImageView fotoEntrega;
    TextView txt_direccionEntrega;
    /**
     * LocationListener -> Para que el GPS funcione.
     */
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            localizacion();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };
    private ArrayList<PlaceMarker> markers = new ArrayList<>();

    /**
     * PERMISOS -> Métodos necesarios para que en versiones de android superiores a 5.0 se garanticen los permisos de:
     * - WRITE_EXTERNAL_STORAGE
     * - READ_EXTERNAL_STORAGE
     * - ACCESS_FINE_LOCATION
     * - ACCESS_LOCATION_EXTRA_COMMANDS
     * - ACCESS_COARSE_LOCATION
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                boolean yes = true;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        yes = false;
                        break;
                    }
                }
                if (yes) {
                    initialize();
                } else {
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * PERMISOS
     */
    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
    }

    /**
     * PERMISOS
     *
     * @return
     */
    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
        if (result != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (result != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        return true;
    }

    /**
     * onCreate -> Primer método que llama Android y cuando se lanza la pantalla principal. (Main)
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidGraphicFactory.createInstance(getApplication());
        setContentView(R.layout.activity__mapa__direccion);

        getSupportActionBar().setTitle("GPS Local de Cuba");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        baseDeDatos = new BaseDeDatos(this);
        entrega = baseDeDatos.obtenerEntregaPorID(getIdEntrega());

        fotoEntrega = (ImageView) findViewById(R.id.fotoEntrega);
        txt_direccionEntrega = (TextView) findViewById(R.id.txt_direccionEntrega);

        // INICIAR LA DIRECCION Y LA FOTO EN EL CARD VIEW
        if (!entrega.getDireccion().equals("")) {
            txt_direccionEntrega.setText(entrega.getDireccion());
        } else {
            String sms = "La Dirección está en la foto";
            txt_direccionEntrega.setText(sms);
        }
        fotoEntrega.setImageBitmap(entrega.getFotoDireccion());
        fotoEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogo_Agrandar_Foto dialogo_agrandar_foto = new Dialogo_Agrandar_Foto(getContext(), entrega, fotoEntrega);
                dialogo_agrandar_foto.mostrar();
            }
        });

        /**
         * Revisa la version de android del dispositivo y muestra los mensajes de
         * confirmación si es superior a 5.0 (LOLLIPOP_MR1)
         */
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            } else {
                initialize();
            }
        } else {
            initialize();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Activity_Entrega_Jefe.class);
        intent.putExtra("ID_ENTREGA", getIdEntrega());
        intent.putExtra("ID_JEFE", getIdJefe());
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    private void initialize() {
        /**
         * Init el FloatingActionButton de la ubicacion
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (latitud != null && longitud != null) {
                    /**
                     * si se ha encontrado la lat y long, se mueve el foco a la posicion obtenida del GPS
                     */
                    byte zoom = mapView.getModel().mapViewPosition.getZoomLevel();
                    mapView.getModel().mapViewPosition.animateTo(new LatLong(latitud, longitud));
                    mapView.getModel().mapViewPosition.setZoomLevel(zoom);
                } else {
                    /**
                     * si lat y long no se han encontrado, se muestra el 'message2' de la carpeta 'values.strings.xml'
                     */
                    Toast.makeText(getApplicationContext(), getString(R.string.message2), Toast.LENGTH_LONG).show();
                }
            }
        });
        /**
         * cargar el mapa de las assets
         */
        loadMap();
        /**
         * Init el mapa
         */
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
        mapView.getMapScaleBar().setVisible(true);
        mapView.getModel().mapViewPosition.setZoomLevel((byte) 12);
        mapView.getMapZoomControls().setZoomLevelMin((byte) 8);
        mapView.getMapZoomControls().setZoomLevelMax((byte) 16);
        // centrar el mapa en CMG
        mapView.getModel().mapViewPosition.setCenter(new LatLong(21.3919882, -77.919945));

        /**
         * asignar atributos a la vista para que el mapa funcione
         */
        tileCache = AndroidUtil.createTileCache(getApplicationContext(), "mapcache",
                mapView.getModel().displayModel.getTileSize(), 1f,
                mapView.getModel().frameBufferModel.getOverdrawFactor());
        tileRendererLayer = new TileRendererLayer(tileCache,
                mapView.getModel().mapViewPosition, false, AndroidGraphicFactory.INSTANCE);
        tileRendererLayer.setMapFile(new File(RUTA));
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);
        mapView.getLayerManager().getLayers().add(tileRendererLayer);
        /**
         * GPS
         */
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locManager.getBestProvider(criteria, true);

        LocationProvider locationProvider = locManager.getProvider(provider);
        if (locationProvider == null)
            provider = LocationManager.GPS_PROVIDER;

        localizacion();
        /**
         * Dialogo para encender el gps
         */
        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.title_message_dialogo)).setCancelable(false)
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //click en si
                            Toast.makeText(Activity_Mapa_Direccion.this, getString(R.string.message_dialogo), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        //click en no
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    /**
     * metodo para añadir el marcador de mi posicion
     */
    private void localizacion() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        loc = locManager.getLastKnownLocation(provider);

        if (marker != null) {
            mapView.getLayerManager().getLayers().remove(marker);
        }
        if (loc != null) {
            latitud = loc.getLatitude();
            longitud = loc.getLongitude();

            marker = new MyMarker(getApplicationContext(), new LatLong(latitud, longitud), AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(R.drawable.img_ubicacion)), 0, 0);

            if (mapView != null) {
                mapView.getLayerManager().getLayers().add(marker);
            }
        }
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 1, locationListener);
    }

    /**
     * metodo para cargar el mapa de la carpeta assets y copiarla al almazanamientos del dispositivo
     */
    private void loadMap() {
        AssetManager am = getAssets();
        try {
            File dir = new File(Environment.getExternalStorageDirectory() + "/mapa/");
            if (!dir.exists()) {
                dir.mkdir();
            }
            // se remueve el .mp3 por .map
            String fileName = "cuba";
            RUTA = Environment.getExternalStorageDirectory() + "/mapa/" + fileName + ".map";
            File destinationFile = new File(RUTA);
            if (!destinationFile.exists()) {
                InputStream in = am.open(fileName + ".mp3");
                FileOutputStream f = new FileOutputStream(destinationFile);
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = in.read(buffer)) > 0) {
                    f.write(buffer, 0, len1);
                }
                f.close();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.message_toast_error) + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public String getDireccion() {
        return getIntent().getStringExtra("DIRECCION");
    }

    public String getIdEntrega() {
        return getIntent().getStringExtra("ID_ENTREGA");
    }

    public String getIdJefe() {
        return getIntent().getStringExtra("ID_JEFE");
    }

    public Context getContext() {
        return this;
    }
}
