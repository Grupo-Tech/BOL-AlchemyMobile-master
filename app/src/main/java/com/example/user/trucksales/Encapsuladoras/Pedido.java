package com.example.user.trucksales.Encapsuladoras;

public class Pedido {
    private String cod_emp;
    private String cod_suc;
    private String cod_tit;
    private String nom_tit;
    private String cod_suc_cli;
    private String zona_entrega;
    private String doc_fac;
    private String nro_doc_uni;
    private String forma_pago;
    private String fec_entrega;
    private String tipo;
    private String turno;

    public String getNom_tit() {
        return nom_tit;
    }

    public void setNom_tit(String nom_tit) {
        this.nom_tit = nom_tit;
    }

    public void setNro_doc_uni(String nro_doc_uni) {
        this.nro_doc_uni = nro_doc_uni;
    }

    public String getNro_doc_uni() {
        return nro_doc_uni;
    }

    public String getCod_emp() {
        return cod_emp;
    }

    public void setCod_emp(String cod_emp) {
        this.cod_emp = cod_emp;
    }

    public String getCod_suc() {
        return cod_suc;
    }

    public void setCod_suc(String cod_suc) {
        this.cod_suc = cod_suc;
    }

    public String getCod_tit() {
        return cod_tit;
    }

    public void setCod_tit(String cod_tit) {
        this.cod_tit = cod_tit;
    }

    public String getCod_suc_cli() {
        return cod_suc_cli;
    }

    public void setCod_suc_cli(String cod_suc_cli) {
        this.cod_suc_cli = cod_suc_cli;
    }

    public String getZona_entrega() {
        return zona_entrega;
    }

    public void setZona_entrega(String zona_entrega) {
        this.zona_entrega = zona_entrega;
    }

    public String getDoc_fac() {
        return doc_fac;
    }

    public void setDoc_fac(String doc_fac) {
        this.doc_fac = doc_fac;
    }

    public String getForma_pago() {
        return forma_pago;
    }

    public void setForma_pago(String forma_pago) {
        this.forma_pago = forma_pago;
    }

    public String getFec_entrega() {
        return fec_entrega;
    }

    public void setFec_entrega(String fec_entrega) {
        this.fec_entrega = fec_entrega;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
}
