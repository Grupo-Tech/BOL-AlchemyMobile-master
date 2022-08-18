package com.example.user.trucksales.Encapsuladoras;

import com.example.user.trucksales.Datos.WebService;

public class ProductosItem {

    private String nom_articulo;
    private String cod_articulo;
    private double cantidad;
    private double cant_aprob;
    private double precio_iva_inc;
    private double porc_desc;
    private double imp_descto_linea;
    private double precio_presup;
    private double total;
    private double subtotal;
    private double imp_iva;
    private double cod_tasa_iva;
    private double factor_iva;
    private String cod_deposito;
    private String adicional;
    private double iva;

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public String getAdicional() {
        return adicional;
    }

    public void setAdicional(String adicional) {
        this.adicional = adicional;
    }

    public String getNom_articulo() {
        return nom_articulo;
    }

    public void setNom_articulo(String nom_articulo) {
        this.nom_articulo = nom_articulo;
    }

    public String getCod_articulo() {
        return cod_articulo;
    }

    public void setCod_articulo(String cod_articulo) {
        this.cod_articulo = cod_articulo;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getCant_aprob() {
        return cant_aprob;
    }

    public void setCant_aprob(double cant_aprob) {
        this.cant_aprob = cant_aprob;
    }

    public double getPrecio_iva_inc() {
        return precio_iva_inc;
    }

    public void setPrecio_iva_inc(double precio_iva_inc) {
        this.precio_iva_inc = precio_iva_inc;
    }

    public double getPorc_desc() {
        return porc_desc;
    }

    public void setPorc_desc(double porc_desc) {
        this.porc_desc = porc_desc;
    }

    public double getImp_descto_linea() {
        return imp_descto_linea;
    }

    public void setImp_descto_linea(double imp_descto_linea) {
        this.imp_descto_linea = imp_descto_linea;
    }

    public double getPrecio_presup() {
        return precio_presup;
    }

    public void setPrecio_presup(double precio_presup) {
        this.precio_presup = precio_presup;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getImp_iva() {
        return imp_iva;
    }

    public void setImp_iva(double imp_iva) {
        this.imp_iva = imp_iva;
    }

    public double getCod_tasa_iva() {
        return cod_tasa_iva;
    }

    public void setCod_tasa_iva(double cod_tasa_iva) {
        this.cod_tasa_iva = cod_tasa_iva;
    }

    public double getFactor_iva() {
        return factor_iva;
    }

    public void setFactor_iva(double factor_iva) {
        this.factor_iva = factor_iva;
    }

    public String getCod_deposito() {
        return cod_deposito;
    }

    public void setCod_deposito(String cod_deposito) {
        this.cod_deposito = cod_deposito;
    }

    public ProductosItem(){}

    public ProductosItem(String nom_articulo, String cod_articulo, double cantidad, double cant_aprob, double precio_iva_inc, double precio_presup, double subtotal, double total, double porc_desc, String cod_deposito, double imp_descto_linea, double factor_iva, double imp_iva, double cod_tasa_iva) {
        this.nom_articulo=nom_articulo;
        this.cod_articulo = cod_articulo;
        this.cantidad = cantidad;
        this.cant_aprob = cant_aprob;
        this.precio_iva_inc = precio_iva_inc;
        this.precio_presup = precio_presup;
        this.subtotal = subtotal;
        this.total = total;
        this.porc_desc = porc_desc;
        this.cod_deposito = cod_deposito;
        this.imp_descto_linea = imp_descto_linea;
        this.factor_iva = factor_iva;
        this.imp_iva = imp_iva;
        this.cod_tasa_iva = cod_tasa_iva;
    }
}
