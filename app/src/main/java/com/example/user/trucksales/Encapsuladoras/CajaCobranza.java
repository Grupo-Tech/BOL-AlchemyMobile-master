package com.example.user.trucksales.Encapsuladoras;

public class CajaCobranza {

    private String cod_caja;
    private String  nom_caja;

    public String getCod_caja() {
        return cod_caja;
    }

    public void setCod_caja(String cod_caja) {
        this.cod_caja = cod_caja;
    }

    public String getNom_caja() {
        return nom_caja;
    }

    public void setNom_caja(String nom_caja) {
        this.nom_caja = nom_caja;
    }

    public CajaCobranza(String cod_caja, String nom_caja) {
        this.cod_caja = cod_caja;
        this.nom_caja = nom_caja;
    }

    public CajaCobranza() {
    }
}
