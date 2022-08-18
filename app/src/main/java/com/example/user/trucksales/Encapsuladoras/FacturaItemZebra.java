package com.example.user.trucksales.Encapsuladoras;

public class FacturaItemZebra {

    private String cant_aprobada;
    private String nom_articulo;
    private String precio_iva_inc;
    private String total_iva_inc;

    public FacturaItemZebra(String cant_aprobada, String nom_articulo, String precio_iva_inc, String total_iva_inc) {
        this.cant_aprobada = cant_aprobada;
        this.nom_articulo = nom_articulo;
        this.precio_iva_inc = precio_iva_inc;
        this.total_iva_inc = total_iva_inc;
    }


    public String getCant_aprobada() {
        return cant_aprobada;
    }

    public void setCant_aprobada(String cant_aprobada) {
        this.cant_aprobada = cant_aprobada;
    }

    public String getNom_articulo() {
        return nom_articulo;
    }

    public void setNom_articulo(String nom_articulo) {
        this.nom_articulo = nom_articulo;
    }

    public String getPrecio_iva_inc() {
        return precio_iva_inc;
    }

    public void setPrecio_iva_inc(String precio_iva_inc) {
        this.precio_iva_inc = precio_iva_inc;
    }

    public String getTotal_iva_inc() {
        return total_iva_inc;
    }

    public void setTotal_iva_inc(String total_iva_inc) {
        this.total_iva_inc = total_iva_inc;
    }
}
