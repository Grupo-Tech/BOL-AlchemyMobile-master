package com.example.user.trucksales.Encapsuladoras;

/**
 * Created by USER on 4/16/2018.
 */

public class FactValidada
{
    private String fact;
    private String vence;
    private String timbr;
    private String Id_Factura;


    public String getFact() {
        return fact;
    }

    public void setFact(String fact) {
        this.fact = fact;
    }

    public String getVence() {
        return vence;
    }

    public void setVence(String vence) {
        this.vence = vence;
    }

    public String getTimbr() {
        if(timbr==null){
            return "";
        }else{
            return timbr;
        }
    }

    public void setTimbr(String timbr) {
        this.timbr = timbr;
    }

    public String getId_Factura() {
        return Id_Factura;
    }

    public void setId_Factura(String id_Factura) {
        Id_Factura = id_Factura;
    }
}
