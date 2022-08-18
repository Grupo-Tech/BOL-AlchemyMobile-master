package com.example.user.trucksales.Encapsuladoras;

public class Cheque {

    private int numero;
    private String banco;
    private String cod_banco;
    private Double importe;

    private String fecha;
    private String tipoDoc;
    private int nroDoc;
    private String codMon;

    public String getCodMon() {
        return codMon;
    }

    public void setCodMon(String codMon) {
        this.codMon = codMon;
    }

    public Cheque(int numero, String banco, Double importe, String fecha, String tipoDoc, int nroDoc, String codMon, String cod_banco) {
        this.numero = numero;
        this.banco = banco;
        this.importe = importe;
        this.fecha = fecha;
        this.tipoDoc = tipoDoc;
        this.nroDoc = nroDoc;
        this.codMon = codMon;
        this.cod_banco = cod_banco;
    }

    public Cheque(int numero, String banco, Double importe, String fecha, String tipoDoc, int nroDoc) {
        this.numero = numero;
        this.banco = banco;
        this.importe = importe;

        this.fecha = fecha;
        this.tipoDoc = tipoDoc;
        this.nroDoc = nroDoc;
    }

    public Cheque() {
    }

    public String getCod_banco() {
        return cod_banco;
    }

    public void setCod_banco(String cod_banco) {
        this.cod_banco = cod_banco;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public int getNroDoc() {
        return nroDoc;
    }

    public void setNroDoc(int nroDoc) {
        this.nroDoc = nroDoc;
    }
}
