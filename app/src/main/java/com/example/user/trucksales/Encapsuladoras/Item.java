package com.example.user.trucksales.Encapsuladoras;

/**
 * Created by USER on 2/7/2018.
 */

public class Item {
    private String nom_Articulo;
    private Integer nro_docum;
    private String cod_Art_Gestion;
    private String cod_Articulo;
    private double cantidad;
    private double cantidad_entregada;
    private double dtoLinea;
    private double precio_Unitario;
    private String cod_Tasa_Iva;
    private String porc_Iva;
    private String cantidadString;
    private String cod_uni_vta;
    private String observacion = "";
    private String reposicion = "";
    private int cant;
    private int cod_rec;
    private double sub_Total;
    private double cantidad_vendida;

    private double porc_desc;
    private double imp_descto;
    private double precio_presup;
    private String adicional = "";
    private double deposito;
    private String sucursal;


    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public double getDeposito() {
        return deposito;
    }

    public void setDeposito(double deposito) {
        this.deposito = deposito;
    }



    public String getAdicional() {
        return adicional;
    }

    public void setAdicional(String adicional) {
        this.adicional = adicional;
    }


    public String getReposicion() {
        return reposicion;
    }

    public void setReposicion(String reposicion) {
        this.reposicion = reposicion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public int getCant() {
        return cant;
    }

    public void setCant(int cant) {
        this.cant = cant;
    }

    public int getCod_rec() {
        return cod_rec;
    }

    public void setCod_rec(int cod_rec) {
        this.cod_rec = cod_rec;
    }

    public double getCantidad_vendida() {
        return cantidad_vendida;
    }

    public void setCantidad_vendida(double cantidad_vendida) {
        this.cantidad_vendida = cantidad_vendida;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getSub_Total() {
        return sub_Total;
    }

    public void setSub_Total(double sub_Total) {
        this.sub_Total = sub_Total;
    }

    public String getNom_Articulo() {
        return nom_Articulo;
    }
    public void setNom_Articulo(String nom_Articulo) {
        this.nom_Articulo = nom_Articulo;
    }

    public Integer getNro_docum() {
        return nro_docum;
    }
    public void setNro_docum(Integer nro_docum) {
        this.nro_docum = nro_docum;
    }

    public String getCod_Art_Gestion() {
        return cod_Art_Gestion;
    }
    public void setCod_Art_Gestion(String cod_Art_Gestion) {this.cod_Art_Gestion = cod_Art_Gestion;}

    public String getCod_Articulo() {
        return cod_Articulo;
    }
    public void setCod_Articulo(String cod_Articulo) {
        this.cod_Articulo = cod_Articulo;
    }

    public double getCantidad() {
        return cantidad;
    }
    public void setCantidad_entregada(double cantidad_entregada) {this.cantidad_entregada = cantidad_entregada;}

    public double getPrecio_Unitario() {
        return precio_Unitario;
    }
    public void setPrecio_Unitario(double precio_Unitario) {this.precio_Unitario = precio_Unitario;}

    public String getCod_Tasa_Iva() {
        return cod_Tasa_Iva;
    }
    public void setCod_Tasa_Iva(String cod_Tasa_Iva) {
        this.cod_Tasa_Iva = cod_Tasa_Iva;
    }

    public String getPorc_Iva() {
        return porc_Iva;
    }
    public void setPorc_Iva(String porc_Iva) {
        this.porc_Iva = porc_Iva;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }
    public double getCantidad_entregada() {
        return cantidad_entregada;
    }

    public String getCod_uni_vta() {
        return cod_uni_vta;
    }
    public void setCod_uni_vta(String cod_uni_vta) {
        this.cod_uni_vta = cod_uni_vta;
    }

    public double getDtoLinea() {
        return dtoLinea;
    }

    public void setDtoLinea(double dtoLinea) {
        this.dtoLinea = dtoLinea;
    }

    public double getPorc_desc() {
        return porc_desc;
    }

    public void setPorc_desc(double porc_desc) {
        this.porc_desc = porc_desc;
    }

    public double getImp_descto() {
        return imp_descto;
    }

    public void setImp_descto(double imp_descto) {
        this.imp_descto = imp_descto;
    }

    public double getPrecio_presup() {
        return precio_presup;
    }

    public void setPrecio_presup(double precio_presup) {
        this.precio_presup = precio_presup;
    }

    public Item(String nom_Articulo, String cod_Articulo, double cantidad, double precio_Unitario, String porc_Iva, double sub_Total, String cod_uni_vta) {
        this.nom_Articulo = nom_Articulo;
        this.cod_Articulo = cod_Articulo;
        this.cantidad = cantidad;
        this.precio_Unitario = precio_Unitario;
        this.porc_Iva = porc_Iva;
        this.sub_Total = sub_Total;
        this.cod_uni_vta = cod_uni_vta;
        setDtoLinea(0);
    }

    public Item(String nom_Articulo, String cod_Articulo, double cantidad, double precio_Unitario, String porc_Iva, double sub_Total, String cod_uni_vta, double dtoLinea) {
        this.nom_Articulo = nom_Articulo;
        this.cod_Articulo = cod_Articulo;
        this.cantidad = cantidad;
        this.precio_Unitario = precio_Unitario;
        this.porc_Iva = porc_Iva;
        this.sub_Total = sub_Total;
        this.cod_uni_vta = cod_uni_vta;
        this.dtoLinea = dtoLinea;
    }

    public Item() {
    }

    public String getCantidadString() {
        return cantidadString;
    }

    public void setCantidadString(String cantidadString) {
        this.cantidadString = cantidadString;
    }
}
