package com.example.user.trucksales.Encapsuladoras;

/**
 * Created by USER on 10/2/2018.
 */

public class Moneda
{
    private String cod_Moneda;
    private String nom_Moneda;
    private String sim_Moneda;
    private Double tipoCambio;

    public String getCod_Moneda() {
        if(cod_Moneda == null){
            return "";
        }else{
            return cod_Moneda;
        }
    }

    public void setCod_Moneda(String cod_Moneda) {
        this.cod_Moneda = cod_Moneda;
    }

    public String getNom_Moneda() {
        return nom_Moneda;
    }

    public void setNom_Moneda(String nom_Moneda) {
        this.nom_Moneda = nom_Moneda;
    }

    public String getSim_Moneda() {
        if(sim_Moneda == null){
            return "";
        }else{
            return sim_Moneda;
        }
    }

    public void setSim_Moneda(String sim_Moneda) {
        this.sim_Moneda = sim_Moneda;
    }

    public Double getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(Double tipoCambio) {
        this.tipoCambio = tipoCambio;
    }
}
