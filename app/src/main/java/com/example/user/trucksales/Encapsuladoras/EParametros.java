package com.example.user.trucksales.Encapsuladoras;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by USER on 27/02/2019.
 */
//CLASE QUE ALMACENA LOS DATOS DE LOS PARAMETROS NECESARIOS.
public class EParametros {
    private LatLng LocalizacionCentral;
    private int SegundosActualizaLocalizacionCamion;
    private int SegundosEsperarSinEstado;
    private int MetrosEsperarSinEstado;
    private String HorarioEntradaUser;
    private String HorarioSalidaUser;

    public LatLng getLocalizacionCentral() {return LocalizacionCentral;}
    public void setLocalizacionCentral(LatLng localizacionCentral) {LocalizacionCentral = localizacionCentral;}

    public int getSegundosActualizaLocalizacionCamion() {return SegundosActualizaLocalizacionCamion;}
    public void setSegundosActualizaLocalizacionCamion(int segundosActualizaLocalizacionCamion) {SegundosActualizaLocalizacionCamion = segundosActualizaLocalizacionCamion;}

    public int getSegundosEsperarSinEstado() {return SegundosEsperarSinEstado;}
    public void setSegundosEsperarSinEstado(int segundosEsperarSinEstado) {SegundosEsperarSinEstado = segundosEsperarSinEstado;}

    public int getMetrosEsperarSinEstado() {return MetrosEsperarSinEstado;}
    public void setMetrosEsperarSinEstado(int metrosEsperarSinEstado) {MetrosEsperarSinEstado = metrosEsperarSinEstado;}

    public String getHorarioEntradaUser() {return HorarioEntradaUser;}
    public void setHorarioEntradaUser(String horarioEntradaUser) {HorarioEntradaUser = horarioEntradaUser;}

    public String getHorarioSalidaUser() {return HorarioSalidaUser;}
    public void setHorarioSalidaUser(String horarioSalidaUser) {HorarioSalidaUser = horarioSalidaUser;}
}
