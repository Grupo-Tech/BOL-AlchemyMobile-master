package com.example.user.trucksales.Encapsuladoras;

public class Vuelto {

    private Double importe;
    private String cod_moneda;

    public String getCod_moneda() {
        return cod_moneda;
    }

    public void setCod_moneda(String cod_moneda) {
        this.cod_moneda = cod_moneda;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Vuelto(Double importe, String cod_moneda) {
        this.importe = importe;
        this.cod_moneda = cod_moneda;
    }
}
