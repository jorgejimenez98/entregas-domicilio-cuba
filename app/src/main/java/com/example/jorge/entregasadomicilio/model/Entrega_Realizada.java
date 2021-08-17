package com.example.jorge.entregasadomicilio.model;

public class Entrega_Realizada {
    private String idEntrega;
    private String fecha;
    private String hora;
    private String barrioEntrega;
    private String precioDomicilio;

    public Entrega_Realizada(String idEntrega, String fecha, String hora, String barrioEntrega, String precioDomicilio) {
        this.idEntrega = idEntrega;
        this.fecha = fecha;
        this.hora = hora;
        this.barrioEntrega = barrioEntrega;
        this.precioDomicilio = precioDomicilio;
    }


    public String getIdEntrega() {
        return idEntrega;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getBarrioEntrega() {
        return barrioEntrega;
    }

    public String getPrecioDomicilio() {
        return precioDomicilio;
    }

    public void setIdEntrega(String idEntrega) {
        this.idEntrega = idEntrega;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setBarrioEntrega(String barrioEntrega) {
        this.barrioEntrega = barrioEntrega;
    }

    public void setPrecioDomicilio(String precioDomicilio) {
        this.precioDomicilio = precioDomicilio;
    }
}
