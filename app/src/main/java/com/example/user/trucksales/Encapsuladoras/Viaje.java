package com.example.user.trucksales.Encapsuladoras;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by USER on 12/22/2017.
 */

public class Viaje {
    private String numViaje;
    private String turno;
    private String tipo;
    private String estado;
    private LatLng localizacionCentral;
    private String fecha;

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNumViaje() {
        return numViaje;
    }
    public void setNumViaje(String numViaje) {
        this.numViaje = numViaje;
    }

    public String getTurno() {
        return turno;
    }
    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LatLng getLocalizacionCentral() {return localizacionCentral;}
    public void setLocalizacionCentral(LatLng localizacionCentral) {this.localizacionCentral = localizacionCentral;}
}
