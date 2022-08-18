package com.example.user.trucksales.Encapsuladoras;

public class Ley {

    private Double importe;
    private Double importeUSD;
    private String numero;

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Double getImporteUSD() {
        return importeUSD;
    }

    public void setImporteUSD(Double importeUSD) {
        this.importeUSD = importeUSD;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Ley(Double importe, Double importeUSD, String numero) {
        this.importe = importe;
        this.importeUSD = importeUSD;
        this.numero = numero;
    }
}
