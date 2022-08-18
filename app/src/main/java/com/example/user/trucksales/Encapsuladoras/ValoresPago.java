package com.example.user.trucksales.Encapsuladoras;

import java.util.Date;

public class ValoresPago {

    private String cod_docum;
    private int nro_docum;
    private String cod_moneda;
    private String cod_banco;
    private String cod_sctatit;
    private String fec_valor;
    private double importe;
    private String cod_tenedor;

    private int estadoEntregado;
    private int estado2;
    private double totalEntregado;

    private String suc_ref = "";
    private String fac_ref = "";
    private Double tipo_cambio = 0D;
    private Double importeUSD;
    private Double diferencia;

    public ValoresPago() {

    }

    public ValoresPago(String cod_docum, int nro_docum, String cod_moneda, String cod_banco, String cod_sctatit, String fec_valor, double importe, String suc_ref, String fac_ref, Double tipo_cambio, Double importeUSD, Double diferencia) {
        this.cod_docum = cod_docum;
        this.nro_docum = nro_docum;
        this.cod_moneda = cod_moneda;
        this.cod_banco = cod_banco;
        this.cod_sctatit = cod_sctatit;
        this.fec_valor = fec_valor;
        this.importe = importe;
        this.suc_ref = suc_ref;
        this.fac_ref = fac_ref;
        this.tipo_cambio = tipo_cambio;
        this.importeUSD = importeUSD;
        this.diferencia = diferencia;
    }

    public ValoresPago(String cod_docum, int nro_docum, String cod_moneda, String cod_banco, String cod_sctatit, String fec_valor, double importe) {
        this.cod_docum = cod_docum;
        this.nro_docum = nro_docum;
        this.cod_moneda = cod_moneda;
        this.cod_banco = cod_banco;
        this.cod_sctatit = cod_sctatit;
        this.fec_valor = fec_valor;
        this.importe = importe;
    }


    public ValoresPago(String cod_docum, int nro_docum, String cod_moneda, String cod_banco, String cod_sctatit, String fec_valor, double importe, double importeUSD) {
        this.cod_docum = cod_docum;
        this.nro_docum = nro_docum;
        this.cod_moneda = cod_moneda;
        this.cod_banco = cod_banco;
        this.cod_sctatit = cod_sctatit;
        this.fec_valor = fec_valor;
        this.importe = importe;
        this.importeUSD = importeUSD;
    }

    public Double getTipo_cambio() {
        return tipo_cambio;
    }

    public void setTipo_cambio(Double tipo_cambio) {
        this.tipo_cambio = tipo_cambio;
    }

    public Double getImporteUSD() {
        return importeUSD;
    }

    public void setImporteUSD(Double importeUSD) {
        this.importeUSD = importeUSD;
    }

    public String getSuc_ref() {
        return suc_ref;
    }

    public void setSuc_ref(String suc_ref) {
        this.suc_ref = suc_ref;
    }

    public String getFac_ref() {
        return fac_ref;
    }

    public void setFac_ref(String fac_ref) {
        this.fac_ref = fac_ref;
    }

    public String getCod_docum() {
        return cod_docum;
    }

    public void setCod_docum(String cod_docum) {
        this.cod_docum = cod_docum;
    }

    public int getNro_docum() {
        return nro_docum;
    }

    public void setNro_docum(int nro_docum) {
        this.nro_docum = nro_docum;
    }

    public String getCod_moneda() {
        return cod_moneda;
    }

    public void setCod_moneda(String cod_moneda) {
        this.cod_moneda = cod_moneda;
    }

    public String getCod_banco() {
        return cod_banco;
    }

    public void setCod_banco(String cod_banco) {
        this.cod_banco = cod_banco;
    }

    public String getCod_sctatit() {
        return cod_sctatit;
    }

    public void setCod_sctatit(String cod_sctatit) {
        this.cod_sctatit = cod_sctatit;
    }

    public String getFec_valor() {
        return fec_valor;
    }

    public void setFec_valor(String fec_valor) {
        this.fec_valor = fec_valor;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public String getCod_tenedor() {
        return cod_tenedor;
    }

    public void setCod_tenedor(String cod_tenedor) {
        this.cod_tenedor = cod_tenedor;
    }

    public double getTotalEntregado() {
        return totalEntregado;
    }

    public void setTotalEntregado(double totalEntregado) {
        this.totalEntregado = totalEntregado;
    }

    public int getEstado2() {
        return estado2;
    }

    public void setEstado2(int estado2) {
        this.estado2 = estado2;
    }

    public int getEstadoEntregado() {
        return estadoEntregado;
    }

    public void setEstadoEntregado(int estadoEntregado) {
        this.estadoEntregado = estadoEntregado;
    }

    public Double getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(Double diferencia) {
        this.diferencia = diferencia;
    }
}
