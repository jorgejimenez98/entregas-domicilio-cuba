package com.example.jorge.entregasadomicilio.model;


public class Producto {
    private String id;
    private String id_entrega;
    private String nombre;
    private int precio;
    private int cantidad;
    private int cantidadAVender;
    private String seVendio;
    private int precioTotal;
    private String descripcion;


    public Producto(String id, String id_entrega, String nombre, int precio, int cantidad, String seVendio, int precioTotal, String descripcion, int cantidadAVender) {
        this.id = id;
        this.id_entrega = id_entrega;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.seVendio = seVendio;
        this.precioTotal = precioTotal;
        this.descripcion = descripcion;
        this.cantidadAVender = cantidadAVender;
    }

    public int getPrecioPorCantidad() {
        return getPrecio() * getCantidadAVender();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_entrega() {
        return id_entrega;
    }

    public void setId_entrega(String id_entrega) {
        this.id_entrega = id_entrega;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getSeVendio() {
        return seVendio;
    }

    public void setSeVendio(String seVendio) {
        this.seVendio = seVendio;
    }

    public int getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(int precioTotal) {
        this.precioTotal = precioTotal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidadAVender() {
        return cantidadAVender;
    }

    public void setCantidadAVender(int cantidadAVender) {
        this.cantidadAVender = cantidadAVender;
    }
}

