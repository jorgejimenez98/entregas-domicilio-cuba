package com.example.jorge.entregasadomicilio.model;


import android.graphics.Bitmap;

public class Jefe {
    private String id;
    private Bitmap fotoPerfil;
    private String nombre;
    private String telefono;
    private int totalIngresos;
    private int totalEntregas;

    public Jefe(String id, Bitmap fotoPerfil, String nombre, String telefono, int totalIngresos, int totalEntregas) {
        this.id = id;
        this.fotoPerfil = fotoPerfil;
        this.nombre = nombre;
        this.telefono = telefono;
        this.totalIngresos = totalIngresos;
        this.totalEntregas = totalEntregas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bitmap getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(Bitmap fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(int totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public int getTotalEntregas() {
        return totalEntregas;
    }

    public void setTotalEntregas(int totalEntregas) {
        this.totalEntregas = totalEntregas;
    }


    @Override
    public String toString() {
        return "Jefe{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", totalIngresos=" + totalIngresos +
                ", totalEntregas=" + totalEntregas +
                ", fotoPerfil=" + fotoPerfil +
                '}';
    }
}
