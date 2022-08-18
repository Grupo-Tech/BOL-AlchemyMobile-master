package com.example.user.trucksales.Encapsuladoras;

public class Retenciones {

    private String detalle;
    private String fecha;
    private Double importe;
    private String suc_ref;
    private String fac_ref;
    private Double tipo_cambio;
    private Double importeUSD;
    private Double diferencia;
    private String cod_docum;

    public String getCod_docum() {
        return cod_docum;
    }

    public void setCod_docum(String cod_docum) {
        this.cod_docum = cod_docum;
    }

    public Double getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(Double diferencia) {
        this.diferencia = diferencia;
    }

    public Retenciones(String detalle, String fecha, Double importe) {
        this.detalle = detalle;
        this.fecha=fecha;
        this.importe = importe;
    }

    public Retenciones(String detalle, String fecha, Double importe, String suc_ref, String fac_ref, Double tipo_cambio, Double importeUSD, Double diferencia, String cod_docum) {
        this.detalle = detalle;
        this.fecha = fecha;
        this.importe = importe;
        this.suc_ref = suc_ref;
        this.fac_ref = fac_ref;
        this.tipo_cambio = tipo_cambio;
        this.importeUSD = importeUSD;
        this.diferencia = diferencia;
        this.cod_docum = cod_docum;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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
}
