package com.example.user.trucksales.Encapsuladoras;

public class Presentacion {

    private String cod_unidad;
    private String nom_unidad;
    private double precio;
    private String cod_tit;
    private String descripcion;

    public String getCod_tit() {
        return cod_tit;
    }

    public void setCod_tit(String cod_tit) {
        this.cod_tit = cod_tit;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getCod_unidad() {
        return cod_unidad;
    }

    public void setCod_unidad(String cod_unidad) {
        this.cod_unidad = cod_unidad;
    }

    public String getNom_unidad() {
        return nom_unidad;
    }

    public void setNom_unidad(String nom_unidad) {
        this.nom_unidad = nom_unidad;
    }
}
