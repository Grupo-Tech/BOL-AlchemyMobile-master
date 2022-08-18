package com.example.user.trucksales.Encapsuladoras;

public class TarjetaDebito {

    private String tarjeta;
    private int voucher;
    private Double importe;

    private String cod_tarjeta;
    private String nom_tarjeta;

    private String cuotas;
    private String cod_Mon;

    public TarjetaDebito() {
        super();
    }

    public TarjetaDebito(String tarjeta, int voucher, Double importe, String cuotas, String cod_Mon) {
        this.tarjeta = tarjeta;
        this.voucher = voucher;
        this.importe = importe;
        this.cuotas =cuotas;
        this.cod_Mon = cod_Mon;
    }


    public String getCod_Mon() {
        return cod_Mon;
    }

    public void setCod_Mon(String cod_Mon) {
        this.cod_Mon = cod_Mon;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public int getVoucher() {
        return voucher;
    }

    public void setVoucher(int voucher) {
        this.voucher = voucher;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public String getCod_tarjeta() {
        return cod_tarjeta;
    }

    public void setCod_tarjeta(String cod_tarjeta) {
        this.cod_tarjeta = cod_tarjeta;
    }

    public String getNom_tarjeta() {
        return nom_tarjeta;
    }

    public void setNom_tarjeta(String nom_tarjeta) {
        this.nom_tarjeta = nom_tarjeta;
    }

    public String getCuotas() {
        return cuotas;
    }

    public void setCuotas(String cuotas) {
        this.cuotas = cuotas;
    }

}
