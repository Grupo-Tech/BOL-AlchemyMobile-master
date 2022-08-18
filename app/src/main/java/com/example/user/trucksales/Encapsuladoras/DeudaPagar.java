package com.example.user.trucksales.Encapsuladoras;

public class DeudaPagar {

    private String documento;
    private String importe;


    public DeudaPagar(String documento, String importe) {
        this.documento = documento;
        this.importe = importe;
    }


    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }
}
