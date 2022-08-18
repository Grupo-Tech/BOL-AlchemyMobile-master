package com.example.user.trucksales.Encapsuladoras;

/**
 * Created by USER on 9/20/2018.
 */

public class Usuario {
    private String nombre;
    private String es_Cobranza;
    private String es_Entrega;
    private Boolean Activo;
    private String Empresa;
    private String TipoCobrador;
    private String es_VentaDirecta;
    private String es_Pedidos;

    public String getEs_Pedidos() {
        return es_Pedidos;
    }

    public void setEs_Pedidos(String es_Pedidos) {
        this.es_Pedidos = es_Pedidos;
    }

    public String getEs_VentaDirecta() {
        return es_VentaDirecta;
    }

    public void setEs_VentaDirecta(String es_VentaDirecta) {
        this.es_VentaDirecta = es_VentaDirecta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEs_Cobranza() {
        return es_Cobranza;
    }

    public void setEs_Cobranza(String es_Cobranza) {
        this.es_Cobranza = es_Cobranza;
    }

    public String getEs_Entrega() {
        return es_Entrega;
    }

    public void setEs_Entrega(String es_Entrega) {
        this.es_Entrega = es_Entrega;
    }

    public Boolean getActivo() {
        return Activo;
    }

    public void setActivo(Boolean activo) {
        Activo = activo;
    }

    public String getEmpresa() {
        return Empresa;
    }

    public void setEmpresa(String empresa) {
        Empresa = empresa;
    }

    public String getTipoCobrador(){
        return  TipoCobrador;
    }

    public void setTipoCobrador(String TipoCobrador){ this.TipoCobrador = this.es_Cobranza != null ? TipoCobrador : null; }
}
