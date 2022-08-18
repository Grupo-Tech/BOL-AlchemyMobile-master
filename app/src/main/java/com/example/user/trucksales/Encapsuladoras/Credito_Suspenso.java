package com.example.user.trucksales.Encapsuladoras;

public class Credito_Suspenso {

    private int numero;
    private String cod_tit;
    private String nom_tit;
    private String fecha;
    private Double importe;
    private Double tipo_cambio;
    private String cod_Moneda;
    private Double importeUSD;

    public Double getImporteUSD() {
        return importeUSD;
    }

    public void setImporteUSD(Double importeUSD) {
        this.importeUSD = importeUSD;
    }

    public String getCod_Moneda() {
        return cod_Moneda;
    }

    public void setCod_Moneda(String cod_Moneda) {
        this.cod_Moneda = cod_Moneda;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getCod_tit() {
        return cod_tit;
    }

    public void setCod_tit(String cod_tit) {
        this.cod_tit = cod_tit;
    }

    public String getNom_tit() {
        return nom_tit;
    }

    public void setNom_tit(String nom_tit) {
        this.nom_tit = nom_tit;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Double getTipo_cambio() {
        return tipo_cambio;
    }

    public void setTipo_cambio(Double tipo_cambio) {
        this.tipo_cambio = tipo_cambio;
    }

    public Credito_Suspenso(int numero, String fecha, Double importe, String cod_Moneda, Double importeUSD) {
        this.numero = numero;
        this.fecha = fecha;
        this.importe = importe;
        this.cod_Moneda = cod_Moneda;
        this.importeUSD = importeUSD;
    }

    public Credito_Suspenso() {
    }
}
