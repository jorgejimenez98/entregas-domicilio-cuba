package com.example.jorge.entregasadomicilio.model;


import android.graphics.Bitmap;

public class Entrega {
    // ATRIBUTOS PARA TODOS
    private String id;
    private String id_jefe;
    private String barrioPrincipal;
    private String precioDomicilio;
    private String notas;
    // ATRIBUTOS QUE SE INSERTAN VACIOS
    private String sePagoDomicilio;
    private int precioTotal;
    // ATRIBUTOS PARA LAS ENTREGAS CON FOTOS
    private String esPorFoto;
    private Bitmap fotoDireccion;
    private String seRealizoEntrega;
    // ATRIBUTOS PARA LAS ENTREGAS SIN FOTOS
    private String telefonoCliente;
    private String nombreCliente;
    private String direccion;


    public Entrega(
            String id,
            String id_jefe,
            String barrioPrincipal,
            String precioDomicilio,
            String notas,
            String sePagoDomicilio,
            int precioTotal,
            String esPorFoto,
            Bitmap fotoDireccion,
            String seRealizoEntrega,
            String telefonoCliente,
            String nombreCliente,
            String direccion
    ) {
        this.id = id;
        this.id_jefe = id_jefe;
        this.barrioPrincipal = barrioPrincipal;
        this.precioDomicilio = precioDomicilio;
        this.notas = notas;
        this.sePagoDomicilio = sePagoDomicilio;
        this.precioTotal = precioTotal;
        this.esPorFoto = esPorFoto;
        this.fotoDireccion = fotoDireccion;
        this.seRealizoEntrega = seRealizoEntrega;
        this.telefonoCliente = telefonoCliente;
        this.nombreCliente = nombreCliente;
        this.direccion = direccion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_jefe() {
        return id_jefe;
    }

    public void setId_jefe(String id_jefe) {
        this.id_jefe = id_jefe;
    }

    public String getBarrioPrincipal() {
        return barrioPrincipal;
    }

    public void setBarrioPrincipal(String barrioPrincipal) {
        this.barrioPrincipal = barrioPrincipal;
    }

    public String getPrecioDomicilio() {
        return precioDomicilio;
    }

    public void setPrecioDomicilio(String precioDomicilio) {
        this.precioDomicilio = precioDomicilio;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getSePagoDomicilio() {
        return sePagoDomicilio;
    }

    public void setSePagoDomicilio(String sePagoDomicilio) {
        this.sePagoDomicilio = sePagoDomicilio;
    }

    public int getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(int precioTotal) {
        this.precioTotal = precioTotal;
    }

    public String getEsPorFoto() {
        return esPorFoto;
    }

    public void setEsPorFoto(String esPorFoto) {
        this.esPorFoto = esPorFoto;
    }

    public Bitmap getFotoDireccion() {
        return fotoDireccion;
    }

    public void setFotoDireccion(Bitmap fotoDireccion) {
        this.fotoDireccion = fotoDireccion;
    }

    public String getSeRealizoEntrega() {
        return seRealizoEntrega;
    }

    public void setSeRealizoEntrega(String seRealizoEntrega) {
        this.seRealizoEntrega = seRealizoEntrega;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "Entrega{" +
                "id='" + id + '\'' +
                ", id_jefe='" + id_jefe + '\'' +
                ", barrioPrincipal='" + barrioPrincipal + '\'' +
                ", precioDomicilio='" + precioDomicilio + '\'' +
                ", notas='" + notas + '\'' +
                ", sePagoDomicilio='" + sePagoDomicilio + '\'' +
                ", precioTotal=" + precioTotal +
                ", esPorFoto='" + esPorFoto + '\'' +
                ", fotoDireccion=" + fotoDireccion +
                ", seRealizoEntrega='" + seRealizoEntrega + '\'' +
                ", telefonoCliente='" + telefonoCliente + '\'' +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }
}
