package com.example.jorge.entregasadomicilio.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.Build;

import com.example.jorge.entregasadomicilio.model.Entrega_Realizada;
import com.example.jorge.entregasadomicilio.model.Repositorio;
import com.example.jorge.entregasadomicilio.otros.HelpFunctions;
import com.example.jorge.entregasadomicilio.model.Entrega;
import com.example.jorge.entregasadomicilio.model.Jefe;
import com.example.jorge.entregasadomicilio.model.Localidad;
import com.example.jorge.entregasadomicilio.model.Producto;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class BaseDeDatos extends SQLiteOpenHelper {
    HelpFunctions hp = new HelpFunctions();
    private static final String NOMBRE_BASE_DATOS = "entregasDomicilio.db";
    private static final int VERSION_ACTUAL = 1;
    private final Context contexto;

    interface Tablas {
        String JEFES = "jefes";
        String ENTREGAS = "entregas";
        String PRODUCTOS = "productos";
    }


    public BaseDeDatos(Context contexto) {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        this.contexto = contexto;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                String sql = "PRAGMA foreign_keys=ON";
                db.execSQL(sql);
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tablas.JEFES + " ( " +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "FOTO_PERFIL BLOB, " +
                "NOMBRE TEXT NOT NULL, " +
                "TELEFONO TEXT NOT NULL, " +
                "TOTAL_INGRESOS INTEGER, " +
                "TOTAL_ENTREGAS INTEGER ) ");

        db.execSQL("CREATE TABLE " + Tablas.ENTREGAS + " ( " +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ID_JEFE INTEGER NOT NULL, " +
                "BARRIO_PRINCIPAL TEXT NOT NULL, " +
                "TELEFONO_CLIENTE TEXT NOT NULL, " +
                "NOMBRE_CLIENTE TEXT NOT NULL, " +
                "DIRECCION TEXT NOT NULL, " +
                "PRECIO_DOMICILIO TEXT NOT NULL, " +
                "SE_PAGO_DOMICILIO TEXT NOT NULL, " +
                "PRECIO_TOTAL INTEGER NOT NULL, " +
                "NOTAS TEXT NOT NULL, " +
                "ES_POR_FOTO TEXT NOT NULL, " +
                "FOTO_ENTREGA BLOB NOT NULL, " +
                "SE_REALIZO_ENTREGA TEXT NOT NULL, " +
                "CONSTRAINT FK_ENTREGAS_JEFES FOREIGN KEY (ID_JEFE) " +
                "REFERENCES jefes (ID) ON DELETE CASCADE ) ");

        db.execSQL("CREATE TABLE " + Tablas.PRODUCTOS + " ( " +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ID_ENTREGA INTEGER NOT NULL, " +
                "NOMBRE TEXT NOT NULL, " +
                "PRECIO TEXT NOT NULL, " +
                "CANTIDAD TEXT NOT NULL, " +
                "DESCRIPCION TEXT NOT NULL, " +
                "PRECIO_TOTAL TEXT NOT NULL, " +
                "SE_VENDIO TEXT NOT NULL, " +
                "CANTIDAD_A_VENDER TEXT NOT NULL, " +
                "CONSTRAINT FK_ENTREGA_PRODUCTO FOREIGN KEY (ID_ENTREGA) " +
                "REFERENCES entregas (ID) ON DELETE CASCADE) ");

        db.execSQL("CREATE TABLE repositorio ( " +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TOTAL_ENTREGAS INTEGER NOT NULL, " +
                "TOTAL_INGRESOS_MIOS INTEGER NOT NULL, " +
                "TOTAL_INGRESOS_JEFES INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE entregas_realizadas ( " +
                "ID_ENTREGA TEXT NOT NULL, " +
                "FECHA TEXT DEFAULT CURRENT_DATE, " +
                "HORA TEXT DEFAULT CURRENT_TIME, " +
                "BARRIO_ENTREGA TEXT NOT NULL, " +
                "PRECIO_DOMICILIO TEXT NOT NULL)");


        // INSERTAR REPOSITORIO PERMANENTE
        ContentValues contentValues = new ContentValues();
        contentValues.put("TOTAL_ENTREGAS", 0);
        contentValues.put("TOTAL_INGRESOS_MIOS", 0);
        contentValues.put("TOTAL_INGRESOS_JEFES", 0);
        db.insert("repositorio", null, contentValues);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.JEFES);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.ENTREGAS);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.PRODUCTOS);
        db.execSQL("DROP TABLE IF EXISTS repositorio");
        db.execSQL("DROP TABLE IF EXISTS entregas_realizadas");
        onCreate(db);
    }

    // =============================================================================================

    public boolean actualizarRepositorio(String id, Repositorio repositorio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TOTAL_ENTREGAS", repositorio.getTotalEntregas());
        contentValues.put("TOTAL_INGRESOS_MIOS", repositorio.getTotalIngresosMios());
        contentValues.put("TOTAL_INGRESOS_JEFES", repositorio.getTotalIngresosJefes());
        db.update("repositorio", contentValues, "ID=?", new String[]{id});
        return true;
    }


    public Repositorio obtenerRepositorio(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM repositorio WHERE ID=?";
        String[] selectionArgs = {id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        Repositorio repositorio = new Repositorio("", 0, 0, 0);
        while (cursor.moveToNext()) {
            repositorio.setId(cursor.getString(0));
            repositorio.setTotalEntregas(cursor.getInt(1));
            repositorio.setTotalIngresosMios(cursor.getInt(2));
            repositorio.setTotalIngresosJefes(cursor.getInt(3));
        }
        return repositorio;
    }

    public String obtenerNumero() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursorJefes = db.rawQuery("SELECT * FROM jefes", null);
        Cursor cursorEntregas = db.rawQuery("SELECT * FROM entregas", null);
        Cursor cursorProductos = db.rawQuery("SELECT * FROM productos", null);
        String res = "Jefes: " + cursorJefes.getCount() + ", Entregas: " + cursorEntregas.getCount() + ", Productos: " + cursorProductos.getCount();
        return res;
    }

    // ============================================================================================
    // GESTIONES Y CONSULTAS DE LA TABLA ENTREGAS REALIZADAS

    public ArrayList<Entrega_Realizada> obtenerEntregasRealizadas() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursorRes = db.rawQuery("SELECT * FROM entregas_realizadas", null);
        ArrayList<Entrega_Realizada> listaJefes = new ArrayList<>();
        while (cursorRes.moveToNext()) {
            String idEntrega = cursorRes.getString(0);
            String fecha = cursorRes.getString(1);
            String hora = cursorRes.getString(2);
            String barrio = cursorRes.getString(3);
            String precioDomicilio = cursorRes.getString(4);
            listaJefes.add(new Entrega_Realizada(idEntrega, fecha, hora, barrio, precioDomicilio));
        }
        return listaJefes;
    }

    public ArrayList<Entrega_Realizada> obtenerEntregasRealizadasHoy(String fecha) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM entregas_realizadas WHERE FECHA=?";
        String[] selectionArgs = {fecha};
        Cursor cursorRes = db.rawQuery(sql, selectionArgs);
        ArrayList<Entrega_Realizada> listaEntregas = new ArrayList<>();
        while (cursorRes.moveToNext()) {
            String idEntrega = cursorRes.getString(0);
            String fecha1 = cursorRes.getString(1);
            String hora = cursorRes.getString(2);
            String barrio = cursorRes.getString(3);
            String precioDomicilio = cursorRes.getString(4);
            listaEntregas.add(new Entrega_Realizada(idEntrega, fecha1, hora, barrio, precioDomicilio));
        }
        return listaEntregas;
    }

    public Entrega_Realizada obtenerEntregasRealizadasPorId(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM entregas_realizadas WHERE ID_ENTREGA=?";
        String[] selectionArgs = {id};
        Cursor cursorRes = db.rawQuery(sql, selectionArgs);
        Entrega_Realizada entrega_realizada = new Entrega_Realizada("", "", "", "", "");
        while (cursorRes.moveToNext()) {
            entrega_realizada.setIdEntrega(cursorRes.getString(0));
            entrega_realizada.setFecha(cursorRes.getString(1));
            entrega_realizada.setHora(cursorRes.getString(2));
            entrega_realizada.setBarrioEntrega(cursorRes.getString(3));
            entrega_realizada.setPrecioDomicilio(cursorRes.getString(4));
        }
        return entrega_realizada;
    }

    public boolean insertarEntregaRealizada(Entrega_Realizada entrega_realizada) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID_ENTREGA", entrega_realizada.getIdEntrega());
        contentValues.put("FECHA", entrega_realizada.getFecha());
        contentValues.put("HORA", entrega_realizada.getHora());
        contentValues.put("BARRIO_ENTREGA", entrega_realizada.getBarrioEntrega());
        contentValues.put("PRECIO_DOMICILIO", entrega_realizada.getPrecioDomicilio());
        long result = db.insert("entregas_realizadas", null, contentValues);
        return result == 1;
    }

    public boolean actualizarEntregaRealizada(String id, Entrega_Realizada entrega) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("BARRIO_ENTREGA", entrega.getBarrioEntrega());
        contentValues.put("PRECIO_DOMICILIO", entrega.getPrecioDomicilio());
        db.update("entregas_realizadas", contentValues, "ID_ENTREGA=?", new String[]{id});
        return true;
    }

    public Integer eliminarEntrega_Realizada(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("entregas_realizadas", "ID_ENTREGA=?", new String[]{id});
    }


    // ============================================================================================
    // GESTIONES Y CONSULTAS DE LA TABLA LOCALIDAD
    public ArrayList<Jefe> obtenerJefes() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursorRes = db.rawQuery("SELECT * FROM jefes", null);
        ArrayList<Jefe> listaJefes = new ArrayList<>();
        while (cursorRes.moveToNext()) {
            String id = cursorRes.getString(0);
            Bitmap fotoPerfil = hp.getImage(cursorRes.getBlob(1));
            String nombre = cursorRes.getString(2);
            String telefono = cursorRes.getString(3);
            int totalIngresos = cursorRes.getInt(4);
            int totalEntregas = cursorRes.getInt(5);
            Jefe newJefe = new Jefe(id, fotoPerfil, nombre, telefono, totalIngresos, totalEntregas);
            listaJefes.add(newJefe);
        }
        return listaJefes;
    }

    public boolean insertarJefe(byte[] foto, String nombre, String telefono, int totalIngresos, int totalEntregas) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("FOTO_PERFIL", foto);
        contentValues.put("NOMBRE", nombre);
        contentValues.put("TELEFONO", telefono);
        contentValues.put("TOTAL_INGRESOS", totalIngresos);
        contentValues.put("TOTAL_ENTREGAS", totalEntregas);
        long result = db.insert("jefes", null, contentValues);
        return result == 1;
    }

    public boolean actualizarJefe(String id, byte[] foto, String nombre, String telefono, int totalIngresos, int totalEntregas) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("FOTO_PERFIL", foto);
        contentValues.put("NOMBRE", nombre);
        contentValues.put("TOTAL_INGRESOS", totalIngresos);
        contentValues.put("TOTAL_ENTREGAS", totalEntregas);
        contentValues.put("TELEFONO", telefono);
        db.update("jefes", contentValues, "ID=?", new String[]{id});
        return true;
    }

    public Integer eliminarJefe(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("jefes", "ID=?", new String[]{id});
    }

    public Jefe obtenerJefePorID(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM jefes WHERE ID=?";
        String[] selectionArgs = {id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        Jefe jefe = new Jefe("", null, "", "", -1, -1);
        while (cursor.moveToNext()) {
            jefe.setId(cursor.getString(0));
            jefe.setFotoPerfil(hp.getImage(cursor.getBlob(1)));
            jefe.setNombre(cursor.getString(2));
            jefe.setTelefono(cursor.getString(3));
            jefe.setTotalIngresos(cursor.getInt(4));
            jefe.setTotalEntregas(cursor.getInt(5));
        }
        return jefe;
    }

    // =============================================================================================
    // GESTIONES Y CONSULTAS DE ENTREGAS

    public ArrayList<Entrega> obtenerTodasEntregas() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM entregas";
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<Entrega> listaEntregas = new ArrayList<>();
        while (cursor.moveToNext()) {
            Entrega entrega = new Entrega(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(6),
                    cursor.getString(9),
                    cursor.getString(7),
                    cursor.getInt(8),
                    cursor.getString(10),
                    hp.getImage(cursor.getBlob(11)),
                    cursor.getString(12),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
            listaEntregas.add(entrega);
        }
        return listaEntregas;
    }

    public ArrayList<Entrega> obtenerEntregasPorJefe(String idJefe) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM entregas WHERE ID_JEFE=?";
        String[] selectionArgs = {idJefe};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        ArrayList<Entrega> listaEntregas = new ArrayList<>();
        while (cursor.moveToNext()) {
            Entrega entrega = new Entrega(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(6),
                    cursor.getString(9),
                    cursor.getString(7),
                    cursor.getInt(8),
                    cursor.getString(10),
                    hp.getImage(cursor.getBlob(11)),
                    cursor.getString(12),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
            listaEntregas.add(entrega);
        }
        return listaEntregas;
    }

    public ArrayList<Entrega> obtenerEntregasPorJefeRealizadas(String idJefe) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM entregas WHERE ID_JEFE=?";
        String[] selectionArgs = {idJefe};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        ArrayList<Entrega> listaEntregas = new ArrayList<>();
        while (cursor.moveToNext()) {
            Entrega entrega = new Entrega(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(6),
                    cursor.getString(9),
                    cursor.getString(7),
                    cursor.getInt(8),
                    cursor.getString(10),
                    hp.getImage(cursor.getBlob(11)),
                    cursor.getString(12),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
            if (entrega.getSeRealizoEntrega().equals("SI")) {
                listaEntregas.add(entrega);
            }
        }
        return listaEntregas;
    }

    public boolean insertarEntrega(String idJefe, String barrioPrincipal, String telefonoCliente, String nombreCliente, String direccion, String precioDomicilio, String sePagoDomicilio, int precioTotal, String notasExtras, String esPorFoto, byte[] fotoEntrega, String seRealizoEntrega) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID_JEFE", idJefe);
        contentValues.put("BARRIO_PRINCIPAL", barrioPrincipal);
        contentValues.put("TELEFONO_CLIENTE", telefonoCliente);
        contentValues.put("NOMBRE_CLIENTE", nombreCliente);
        contentValues.put("DIRECCION", direccion);
        contentValues.put("PRECIO_DOMICILIO", precioDomicilio);
        contentValues.put("SE_PAGO_DOMICILIO", sePagoDomicilio);
        contentValues.put("PRECIO_TOTAL", precioTotal);
        contentValues.put("NOTAS", notasExtras);
        contentValues.put("ES_POR_FOTO", esPorFoto);
        contentValues.put("FOTO_ENTREGA", fotoEntrega);
        contentValues.put("SE_REALIZO_ENTREGA", seRealizoEntrega);
        long result = db.insert("entregas", null, contentValues);
        return result == 1;
    }

    public boolean actualizarEntrega(String id, Entrega entrega) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID_JEFE", entrega.getId_jefe());
        contentValues.put("BARRIO_PRINCIPAL", entrega.getBarrioPrincipal());
        contentValues.put("TELEFONO_CLIENTE", entrega.getTelefonoCliente());
        contentValues.put("NOMBRE_CLIENTE", entrega.getNombreCliente());
        contentValues.put("DIRECCION", entrega.getDireccion());
        contentValues.put("PRECIO_DOMICILIO", entrega.getPrecioDomicilio());
        contentValues.put("SE_PAGO_DOMICILIO", entrega.getSePagoDomicilio());
        contentValues.put("PRECIO_TOTAL", entrega.getPrecioTotal());
        contentValues.put("NOTAS", entrega.getNotas());
        contentValues.put("ES_POR_FOTO", entrega.getEsPorFoto());
        byte[] fotoEntrega = {};
        if (entrega.getEsPorFoto().equals("SI")) {
            fotoEntrega = hp.getBytes(entrega.getFotoDireccion());
        }
        contentValues.put("FOTO_ENTREGA", fotoEntrega);
        contentValues.put("SE_REALIZO_ENTREGA", entrega.getSeRealizoEntrega());
        db.update("entregas", contentValues, "ID=?", new String[]{id});
        return true;
    }

    public Entrega obtenerEntregaPorID(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM entregas WHERE ID=?";
        String[] selectionArgs = {id};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        Entrega entrega = new Entrega("", "", "", "", "", "", 0, "", null, "", "", "", "");
        while (cursor.moveToNext()) {
            entrega.setId(cursor.getString(0));
            entrega.setId_jefe(cursor.getString(1));
            entrega.setBarrioPrincipal(cursor.getString(2));
            entrega.setPrecioDomicilio(cursor.getString(6));
            entrega.setNotas(cursor.getString(9));
            entrega.setSePagoDomicilio(cursor.getString(7));
            entrega.setPrecioTotal(cursor.getInt(8));
            entrega.setEsPorFoto(cursor.getString(10));
            entrega.setFotoDireccion(hp.getImage(cursor.getBlob(11)));
            entrega.setSeRealizoEntrega(cursor.getString(12));
            entrega.setTelefonoCliente(cursor.getString(3));
            entrega.setNombreCliente(cursor.getString(4));
            entrega.setDireccion(cursor.getString(5));
        }
        return entrega;
    }

    public Integer eliminarEntrega(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("entregas", "ID=?", new String[]{id});
    }

    // =============================================================================================
    // GESTIONES Y CONSULTAS DE PRODUCTOS
    public ArrayList<Producto> obtenerProductosPorEntrega(String idEntrega) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM productos WHERE ID_ENTREGA=?";
        String[] selectionArgs = {idEntrega};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        ArrayList<Producto> listaProductos = new ArrayList<>();
        while (cursor.moveToNext()) {
            Producto producto = new Producto(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    Integer.parseInt(cursor.getString(3)),
                    Integer.parseInt(cursor.getString(4)),
                    cursor.getString(7),
                    Integer.parseInt(cursor.getString(6)),
                    cursor.getString(5),
                    cursor.getInt(8)
            );
            listaProductos.add(producto);
        }
        return listaProductos;
    }

    public boolean insertarProducto(String idEntrega, String nombre, int precio, int cantidad, String seVendio, int precioTotal, String descripcion, int cantidadAVender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID_ENTREGA", idEntrega);
        contentValues.put("NOMBRE", nombre);
        contentValues.put("PRECIO", precio);
        contentValues.put("CANTIDAD", cantidad);
        contentValues.put("SE_VENDIO", seVendio);
        contentValues.put("PRECIO_TOTAL", precioTotal);
        contentValues.put("DESCRIPCION", descripcion);
        contentValues.put("CANTIDAD_A_VENDER", cantidadAVender);
        long result = db.insert("productos", null, contentValues);
        return result == 1;
    }

    public Producto obtenerProductoPorId(String idProducto) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM productos WHERE ID=?";
        String[] selectionArgs = {idProducto};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        Producto producto = new Producto("", "", "", 0, 0, "", 0, "", 0);
        while (cursor.moveToNext()) {
            producto.setId(cursor.getString(0));
            producto.setId_entrega(cursor.getString(1));
            producto.setNombre(cursor.getString(2));
            producto.setPrecio(Integer.parseInt(cursor.getString(3)));
            producto.setCantidad(Integer.parseInt(cursor.getString(4)));
            producto.setSeVendio(cursor.getString(7));
            producto.setPrecioTotal(Integer.parseInt(cursor.getString(6)));
            producto.setDescripcion(cursor.getString(5));
            producto.setCantidadAVender(cursor.getInt(8));
        }
        return producto;
    }

    public Integer eliminarProducto(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("productos", "ID=?", new String[]{id});
    }

    public boolean actualizarProducto(String id, Producto producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID_ENTREGA", producto.getId_entrega());
        contentValues.put("NOMBRE", producto.getNombre());
        contentValues.put("PRECIO", producto.getPrecio());
        contentValues.put("CANTIDAD", producto.getCantidad());
        contentValues.put("SE_VENDIO", producto.getSeVendio());
        contentValues.put("PRECIO_TOTAL", producto.getPrecioTotal());
        contentValues.put("DESCRIPCION", producto.getDescripcion());
        contentValues.put("CANTIDAD_A_VENDER", producto.getCantidadAVender());
        db.update("productos", contentValues, "ID=?", new String[]{id});
        return true;
    }
}