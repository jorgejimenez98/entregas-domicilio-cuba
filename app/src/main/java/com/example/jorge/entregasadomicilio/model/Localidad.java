package com.example.jorge.entregasadomicilio.model;



public class Localidad {
    private String id;
    private String provincia;
    private String pais;


    public Localidad(String id, String provincia, String pais) {
        this.id = id;
        this.provincia = provincia;
        this.pais = pais;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    @Override
    public String toString() {
        return "Localidad{" +
                "id='" + id + '\'' +
                ", provincia='" + provincia + '\'' +
                ", pais='" + pais + '\'' +
                '}';
    }
}
