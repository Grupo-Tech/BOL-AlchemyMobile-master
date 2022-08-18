package com.example.user.trucksales.Encapsuladoras;

/**
 * Created by USER on 12/28/2017.
 */

public class Entrega {

    private String fec_Doc;
    private String cod_Tit_Gestion;
    private String cod_Tit;
    private String nom_Tit;
    private String cod_Sucursal;
    private String nom_Sucursal;
    private String nro_Docum;
    private String direccion;
    private Double latiud_Ubic;
    private Double longitud_Ubic;
    private Integer nro_doc_ref;
    private String cod_Zona_Entrega;
    private String nom_Zona_Entrega;
    private Integer cant_Facturas;
    private String hora_Desde;
    private String hora_Hasta;
    private Double distancia;
    private String prioridad;
    private String observaciones;
    private String codEmp;
    private String tipo;
    private Double descuento;

    private String codigoDocumentoCliente;
    private String codigoMoneda;
    private String codigoTipoPago;



    public Double getDescuento() {
        if(descuento==null) return 0D;
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCodEmp() { return codEmp; }
    public void setCodEmp(String codEmp) { this.codEmp = codEmp; }

    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFec_Doc() {
        return fec_Doc;
    }
    public void setFec_Doc(String fec_Doc) {
        this.fec_Doc = fec_Doc;
    }

    public String getCod_Tit_Gestion() {
        return cod_Tit_Gestion;
    }
    public void setCod_Tit_Gestion(String cod_Tit_Gestion) {this.cod_Tit_Gestion = cod_Tit_Gestion;}

    public String getCod_Tit() {
        return cod_Tit;
    }
    public void setCod_Tit(String cod_Tit) {
        this.cod_Tit = cod_Tit;
    }

    public String getNom_Tit() {
        return nom_Tit;
    }
    public void setNom_Tit(String nom_Tit) {
        this.nom_Tit = nom_Tit;
    }

    public String getCod_Sucursal() {
        return cod_Sucursal;
    }
    public void setCod_Sucursal(String cod_Sucursal) {
        this.cod_Sucursal = cod_Sucursal;
    }

    public String getNom_Sucursal() {
        return nom_Sucursal;
    }
    public void setNom_Sucursal(String nom_Sucursal) {
        this.nom_Sucursal = nom_Sucursal;
    }

    public String getNro_Docum() {
        return nro_Docum;
    }
    public void setNro_Docum(String nro_Docum) {
        this.nro_Docum = nro_Docum;
    }

    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getLatiud_Ubic() {
        return latiud_Ubic;
    }
    public void setLatiud_Ubic(Double latiud_Ubic) {
        this.latiud_Ubic = latiud_Ubic;
    }

    public Double getLongitud_Ubic() {
        return longitud_Ubic;
    }
    public void setLongitud_Ubic(Double longitud_Ubic) {
        this.longitud_Ubic = longitud_Ubic;
    }

    public String getCod_Zona_Entrega() {
        return cod_Zona_Entrega;
    }
    public void setCod_Zona_Entrega(String cod_Zona_Entrega) {this.cod_Zona_Entrega = cod_Zona_Entrega;}

    public String getNom_Zona_Entrega() {
        return nom_Zona_Entrega;
    }
    public void setNom_Zona_Entrega(String nom_Zona_Entrega) {this.nom_Zona_Entrega = nom_Zona_Entrega;}

    public Integer getCant_Facturas() {
        return cant_Facturas;
    }
    public void setCant_Facturas(Integer cant_Facturas) {
        this.cant_Facturas = cant_Facturas;
    }

    public String getHora_Desde() {
        return hora_Desde;
    }
    public void setHora_Desde(String hora_Desde) {
        this.hora_Desde = hora_Desde;
    }

    public String getHora_Hasta() {
        return hora_Hasta;
    }
    public void setHora_Hasta(String hora_Hasta) {
        this.hora_Hasta = hora_Hasta;
    }

    public String getPrioridad() {
        return prioridad;
    }
    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public Double getDistancia() {
        return distancia;
    }
    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public Integer getNro_doc_ref() {return nro_doc_ref;}
    public void setNro_doc_ref(Integer nro_doc_ref) {this.nro_doc_ref = nro_doc_ref;}


    public String getCodigoDocumentoCliente() {
        return codigoDocumentoCliente;
    }

    public void setCodigoDocumentoCliente(String codigoDocumentoCliente) {
        this.codigoDocumentoCliente = codigoDocumentoCliente;
    }

    public String getCodigoMoneda() {
        return codigoMoneda;
    }

    public void setCodigoMoneda(String codigoMoneda) {
        this.codigoMoneda = codigoMoneda;
    }

    public String getCodigoTipoPago() {
        return codigoTipoPago;
    }

    public void setCodigoTipoPago(String codigoTipoPago) {
        this.codigoTipoPago = codigoTipoPago;
    }
}
