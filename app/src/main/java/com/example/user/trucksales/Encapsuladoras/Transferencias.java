package com.example.user.trucksales.Encapsuladoras;

public class Transferencias {

    private String banco;
    private String detalle;
    private String bancodestino;
    private String cuentadestino;
    private Double importe;
    private String codMon;


    public Transferencias(String banco,String detalle,String bancodestino,String cuentadestino,Double importe, String codMon) {

        this.banco=banco;
        this.bancodestino=bancodestino;
        this.cuentadestino=cuentadestino;
        this.detalle = detalle;
        this.importe = importe;
        this.codMon = codMon;
    }

    public String getCodMon() {
        return codMon;
    }

    public void setCodMon(String codMon) {
        this.codMon = codMon;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }


    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getBancodestino() {
        return bancodestino;
    }

    public void setBancodestino(String bancodestino) {
        this.bancodestino = bancodestino;
    }

    public String getCuentadestino() {
        return cuentadestino;
    }

    public void setCuentadestino(String cuentadestino) {
        this.cuentadestino = cuentadestino;
    }
}
