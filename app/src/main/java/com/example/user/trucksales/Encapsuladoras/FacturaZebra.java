package com.example.user.trucksales.Encapsuladoras;

import java.util.ArrayList;

public class FacturaZebra {

    private String titulo;
    private String subtitulo_movil;
    private String nom_empresa;
    private String dir_empresa;
    private String dir_empresa_aux;
    private String tel_empresa;
    private String nom_localidad;
    private String nom_pais;
    private String nro_cuit;
    private String nro_docum;
    private String n_autorizacion;
    private String fec_doc;
    private String nro_doc_uni;
    private String nom_tit;
    private String cod_control_fact;
    private String fec_vto_fac;
    private String imagen_qr;
    private String montoenletras;
    private ArrayList<FacturaItemZebra> items;
    private String leyenda_fac;
    private String municipio;
    private String cod_cliente;
    private String leyenda2;

    public FacturaZebra(String titulo, String subtitulo_movil, String  nom_empresa, String dir_empresa, String dir_empresa_aux, String tel_empresa, String nom_localidad, String nom_pais, String nro_cuit, String nro_docum,
                        String n_autorizacion, String fec_doc, String nro_doc_uni, String nom_tit, String cod_control_fact, String fec_vto_fac, String imagen_qr,String montoenletras,String leyenda_fac,
                        ArrayList<FacturaItemZebra> items, String municipio, String cod_cliente, String leyenda2) {
        this.titulo = titulo;
        this.subtitulo_movil = subtitulo_movil;
        this.nom_empresa = nom_empresa;
        this.dir_empresa = dir_empresa;
        this.dir_empresa_aux = dir_empresa_aux;
        this.tel_empresa = tel_empresa;
        this.nom_localidad = nom_localidad;
        this.nom_pais = nom_pais;
        this.nro_cuit = nro_cuit;
        this.nro_docum = nro_docum;
        this.n_autorizacion = n_autorizacion;
        this.fec_doc = fec_doc;
        this.nro_doc_uni = nro_doc_uni;
        this.nom_tit = nom_tit;
        this.cod_control_fact = cod_control_fact;
        this.fec_vto_fac = fec_vto_fac;
        this.imagen_qr = imagen_qr;
        this.montoenletras=montoenletras;
        this.items = items;
        this.leyenda_fac=leyenda_fac;
        this.municipio=municipio;
        this.cod_cliente=cod_cliente;
        this.leyenda2=leyenda2;
    }

    public FacturaZebra() {

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo_movil() {
        return subtitulo_movil;
    }

    public void setSubtitulo_movil(String subtitulo_movil) {this.subtitulo_movil = subtitulo_movil;}

    public String getNom_empresa() {
        return nom_empresa;
    }

    public void setNom_empresa(String nom_empresa) {
        this.nom_empresa = nom_empresa;
    }

    public String getDir_empresa() {
        return dir_empresa;
    }

    public void setDir_empresa(String dir_empresa) {
        this.dir_empresa = dir_empresa;
    }

    public String getDir_empresa_aux() {
        return dir_empresa_aux;
    }

    public void setDir_empresa_aux(String dir_empresa_aux) {
        this.dir_empresa_aux = dir_empresa_aux;
    }

    public String getTel_empresa() {
        return tel_empresa;
    }

    public void setTel_empresa(String tel_empresa) {
        this.tel_empresa = tel_empresa;
    }

    public String getNom_localidad() {
        return nom_localidad;
    }

    public void setNom_localidad(String nom_localidad) {
        this.nom_localidad = nom_localidad;
    }

    public String getNom_pais() {
        return nom_pais;
    }

    public void setNom_pais(String nom_pais) {
        this.nom_pais = nom_pais;
    }

    public String getNro_cuit() {
        return nro_cuit;
    }

    public void setNro_cuit(String nro_cuit) {
        this.nro_cuit = nro_cuit;
    }

    public String getNro_docum() {
        return nro_docum;
    }

    public void setNro_docum(String nro_docum) {
        this.nro_docum = nro_docum;
    }

    public String getN_autorizacion() {
        return n_autorizacion;
    }

    public void setN_autorizacion(String n_autorizacion) {
        this.n_autorizacion = n_autorizacion;
    }

    public String getFec_doc() {
        return fec_doc;
    }

    public void setFec_doc(String fec_doc) {
        this.fec_doc = fec_doc;
    }

    public String getNro_doc_uni() {
        return nro_doc_uni;
    }

    public void setNro_doc_uni(String nro_doc_uni) {
        this.nro_doc_uni = nro_doc_uni;
    }

    public String getNom_tit() {
        return nom_tit;
    }

    public void setNom_tit(String nom_tit) {
        this.nom_tit = nom_tit;
    }

    public String getCod_control_fact() {
        return cod_control_fact;
    }

    public void setCod_control_fact(String cod_control_fact) {
        this.cod_control_fact = cod_control_fact;
    }

    public String getFec_vto_fac() {
        return fec_vto_fac;
    }

    public void setFec_vto_fac(String fec_vto_fac) {
        this.fec_vto_fac = fec_vto_fac;
    }

    public String getImagen_qr() {
        return imagen_qr;
    }

    public void setImagen_qr(String imagen_qr) {
        this.imagen_qr = imagen_qr;
    }


    public String getMontoenletras() {
        return montoenletras;
    }

    public void setMontoenletras(String montoenletras) {
        this.montoenletras = montoenletras;
    }

    public ArrayList<FacturaItemZebra> getItems() {
        return items;
    }

    public void setItems(ArrayList<FacturaItemZebra> items) {
        this.items = items;
    }


    public String getLeyenda_fac() {
        return leyenda_fac;
    }

    public void setLeyenda_fac(String leyenda_fac) {
        this.leyenda_fac = leyenda_fac;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getCod_cliente() {
        return cod_cliente;
    }

    public void setCod_cliente(String cod_cliente) {
        this.cod_cliente = cod_cliente;
    }

    public String getLeyenda2() {
        return leyenda2;
    }

    public void setLeyenda2(String leyenda2) {
        this.leyenda2 = leyenda2;
    }
}
