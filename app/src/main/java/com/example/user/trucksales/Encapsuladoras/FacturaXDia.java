package com.example.user.trucksales.Encapsuladoras;

/**
 * Created by USER on 4/5/2018.
 */

 public class FacturaXDia {
    private String tipo;
    private String tipo_docum;
    private String cod_Deposito;
    private String nro_Trans;
    private String nro_trans_ref;
    private String nro_Doc_Ref;
    private String cod_Suc_Tribut;
    private String cod_Fac_Tribut;
    private String nro_Docum;
    private String nom_Tit;
    private String hora;
    private String cod_Tit_Gestion;
    private String cod_Sucursal;
    private String cod_Tit;
    private String sucursal;
    private String fecha;
    private String nro_viaje;
    private String cod_emp;
    private String calcula_interes;
    private String cod_moneda;
    private String nom_emp;
    private Double latitud;
    private Double longitud;
    private double importe;

    public String getTipo_docum() {
        return tipo_docum;
    }

    public void setTipo_docum(String tipo_docum) {
        this.tipo_docum = tipo_docum;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getNom_emp() {
        return nom_emp;
    }

    public void setNom_emp(String nom_emp) {
        this.nom_emp = nom_emp;
    }

    public String getCod_moneda() {
        return cod_moneda;
    }

    public void setCod_moneda(String cod_moneda) {
        this.cod_moneda = cod_moneda;
    }

    public String getCalcula_interes() {
        return calcula_interes;
    }

    public void setCalcula_interes(String calcula_interes) {
        this.calcula_interes = calcula_interes;
    }

    public String getCod_emp() {
        return cod_emp;
    }

    public void setCod_emp(String cod_emp) {
        this.cod_emp = cod_emp;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public String getNro_viaje() {
        return nro_viaje;
    }

    public void setNro_viaje(String nro_viaje) {
        this.nro_viaje = nro_viaje;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNro_trans_ref() {return nro_trans_ref;}
    public void setNro_trans_ref(String nro_trans_ref) {this.nro_trans_ref = nro_trans_ref;}

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCod_Deposito() {
        return cod_Deposito;
    }
    public void setCod_Deposito(String cod_Deposito) {
        this.cod_Deposito = cod_Deposito;
    }

    public String getNro_Trans() {
        return nro_Trans;
    }
    public void setNro_Trans(String nro_Trans) {
        this.nro_Trans = nro_Trans;
    }

    public String getNro_Doc_Ref() {
        return nro_Doc_Ref;
    }
    public void setNro_Doc_Ref(String nro_Doc_Ref) {
        this.nro_Doc_Ref = nro_Doc_Ref;
    }

    public String getCod_Suc_Tribut() {
        return cod_Suc_Tribut;
    }
    public void setCod_Suc_Tribut(String cod_Suc_Tribut) {
        this.cod_Suc_Tribut = cod_Suc_Tribut;
    }

    public String getCod_Fac_Tribut() {
        return cod_Fac_Tribut;
    }
    public void setCod_Fac_Tribut(String cod_Fac_Tribut) {
        this.cod_Fac_Tribut = cod_Fac_Tribut;
    }

    public String getNro_Docum() {
        return nro_Docum;
    }
    public void setNro_Docum(String nro_Docum) {
        this.nro_Docum = nro_Docum;
    }

    public String getNom_Tit() {return nom_Tit;}
    public void setNom_Tit(String nom_Tit) {this.nom_Tit = nom_Tit;}

    public String getHora() {return hora;}
    public void setHora(String hora) {this.hora = hora;}

    public String getCod_Tit_Gestion() {return cod_Tit_Gestion;}
    public void setCod_Tit_Gestion(String cod_Tit_Gestion) {this.cod_Tit_Gestion = cod_Tit_Gestion;}

    public String getCod_Sucursal() {
        return cod_Sucursal;
    }
    public void setCod_Sucursal(String cod_Sucursal) {
        this.cod_Sucursal = cod_Sucursal;
    }

    public String getCod_Tit() {
        return cod_Tit;
    }
    public void setCod_Tit(String cod_Tit) {
        this.cod_Tit = cod_Tit;
    }

    public String getSucursal() {
        return sucursal;
    }
    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }
}
