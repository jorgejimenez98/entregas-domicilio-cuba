package com.example.jorge.entregasadomicilio.model;


public class Repositorio {
    private String id;
    private int totalEntregas;
    private int totalIngresosMios;
    private int totalIngresosJefes;


    public Repositorio(String id, int totalEntregas, int totalIngresosMios, int totalIngresosJefes) {
        this.id = id;
        this.totalEntregas = totalEntregas;
        this.totalIngresosMios = totalIngresosMios;
        this.totalIngresosJefes = totalIngresosJefes;
    }

    public int getTotalEntregas() {
        return totalEntregas;
    }

    public void setTotalEntregas(int totalEntregas) {
        this.totalEntregas = totalEntregas;
    }

    public int getTotalIngresosMios() {
        return totalIngresosMios;
    }

    public void setTotalIngresosMios(int totalIngresosMios) {
        this.totalIngresosMios = totalIngresosMios;
    }

    public int getTotalIngresosJefes() {
        return totalIngresosJefes;
    }

    public void setTotalIngresosJefes(int totalIngresosJefes) {
        this.totalIngresosJefes = totalIngresosJefes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
