package com.example.user.trucksales.Encapsuladoras;

/**
 * Created by USER on 9/21/2018.
 */

public class ClienteCobranza {
    private String nom_Tit;
    private String cod_Docum;
    private String nro_Docum;
    private String fecha_Vence;
    private String cod_Moneda;
    private double imp_mov_mo;
    private String cod_Aux;
    private String cod_Banco;
    private String cod_Tit;
    private int estado;
    private double totalEntregado;
    private double totalInteresEnt;
    private String cod_dpto;
    private String serie_docum;
    private double  imp_a_retenc;
    private double imp_interes;
    private String calcula_interes;
    private String cod_Tit_Gestion;
    private String direccion;
    private Double latiud_Ubic;
    private Double longitud_Ubic;
    private String prioridad;
    private String telParticular;
    private String telLaboral;
    private String codEmp;
    private String nomEmp;
    private String simMoneda;
    private String habilita;
    private double descuento;
    private double descuentoAgregado;

    public String getHabilita() {
        return habilita;
    }

    public void setHabilita(String habilita) {
        this.habilita = habilita;
    }

    public double getDescuentoAgregado() {
        return descuentoAgregado;
    }

    public void setDescuentoAgregado(double descuentoAgregado) {
        this.descuentoAgregado = descuentoAgregado;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public String getSimMoneda() {
        return simMoneda;
    }

    public void setSimMoneda(String simMoneda) {
        this.simMoneda = simMoneda;
    }

    public String getNomEmp() {
        return nomEmp;
    }

    public void setNomEmp(String nomEmp) {
        this.nomEmp = nomEmp;
    }

    public String getCodEmp() {
        return codEmp;
    }

    public void setCodEmp(String codEmp) {
        this.codEmp = codEmp;
    }

    public String getTelParticular() {
        return telParticular;
    }

    public void setTelParticular(String telParticular) {
        this.telParticular = telParticular;
    }

    public String getTelLaboral() {
        return telLaboral;
    }

    public void setTelLaboral(String telLaboral) {
        this.telLaboral = telLaboral;
    }

    public String getCod_Tit_Gestion() {
        return cod_Tit_Gestion;
    }

    public void setCod_Tit_Gestion(String cod_Tit_Gestion) {
        this.cod_Tit_Gestion = cod_Tit_Gestion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getLatiud_Ubic() {
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

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getCalcula_interes() {
        return calcula_interes;
    }

    public void setCalcula_interes(String calcula_interes) {
        this.calcula_interes = calcula_interes;
    }

    public double getImp_interes() {
        return imp_interes;
    }

    public void setImp_interes(double imp_interes) {
        this.imp_interes = imp_interes;
    }

    public double getImp_a_retenc() {
        return imp_a_retenc;
    }

    public void setImp_a_retenc(double imp_a_retenc) {
        this.imp_a_retenc = imp_a_retenc;
    }

    /*private String cod_tit_gestion;

    public String getCod_tit_gestion() {
        return cod_tit_gestion;
    }

    public void setCod_tit_gestion(String cod_tit_gestion) {
        this.cod_tit_gestion = cod_tit_gestion;
    }*/

    public String getCod_dpto() {
        return cod_dpto;
    }

    public void setCod_dpto(String cod_dpto) {
        this.cod_dpto = cod_dpto;
    }

    public String getSerie_docum() {
        return serie_docum;
    }

    public void setSerie_docum(String serie_docum) {
        this.serie_docum = serie_docum;
    }

    public String getCod_Docum() {
        return cod_Docum;
    }

    public void setCod_Docum(String cod_Docum) {
        this.cod_Docum = cod_Docum;
    }

    public String getNro_Docum() {
        return nro_Docum;
    }

    public void setNro_Docum(String nro_Docum) {
        this.nro_Docum = nro_Docum;
    }

    public String getFecha_Vence() {
        return fecha_Vence;
    }

    public void setFecha_Vence(String fecha_Vence) {
        this.fecha_Vence = fecha_Vence;
    }

    public String getCod_Moneda() {
        return cod_Moneda;
    }

    public void setCod_Moneda(String cod_Moneda) {
        this.cod_Moneda = cod_Moneda;
    }

    public double getImp_mov_mo() {
        return imp_mov_mo;
    }

    public void setImp_mov_mo(double imp_mov_mo) {
        this.imp_mov_mo = imp_mov_mo;
    }

    public String getCod_Aux() {
        return cod_Aux;
    }

    public void setCod_Aux(String cod_Aux) {
        this.cod_Aux = cod_Aux;
    }

    public String getCod_Banco() {
        return cod_Banco;
    }

    public void setCod_Banco(String cod_Banco) {
        this.cod_Banco = cod_Banco;
    }

    public String getNom_Tit() {
        return nom_Tit;
    }

    public void setNom_Tit(String nom_Tit) {
        this.nom_Tit = nom_Tit;
    }

    public String getCod_Tit() {
        return cod_Tit;
    }

    public void setCod_Tit(String cod_Tit) {
        this.cod_Tit = cod_Tit;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public double getTotalEntregado() {
        return totalEntregado;
    }

    public void setTotalEntregado(double totalEntregado) {
        this.totalEntregado = totalEntregado;
    }

    public double getTotalInteresEnt() {
        return totalInteresEnt;
    }

    public void setTotalInteresEnt(double totalInteresEnt) {
        this.totalInteresEnt = totalInteresEnt;
    }
}
