package com.example.user.trucksales.Encapsuladoras;

/**
 * Created by USER on 4/9/2018.
 */

public class Caja {
    private String id_TipoCaja;
    private String nom_Caja;
    private int cantidad;
    private int entrega;
    private  int retira;
    private  int saldo;

    public String getId_TipoCaja() {
        return id_TipoCaja;
    }

    public void setId_TipoCaja(String id_TipoCaja) {
        this.id_TipoCaja = id_TipoCaja;
    }

    public String getNom_Caja() {
        return nom_Caja;
    }

    public void setNom_Caja(String nom_Caja) {
        this.nom_Caja = nom_Caja;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getEntrega() {
        return entrega;
    }

    public void setEntrega(int entrega) {
        this.entrega = entrega;
    }

    public int getRetira() {
        return retira;
    }

    public void setRetira(int retira) {
        this.retira = retira;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }
}
