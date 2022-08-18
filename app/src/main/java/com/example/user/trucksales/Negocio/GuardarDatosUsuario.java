package com.example.user.trucksales.Negocio;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.example.user.trucksales.Datos.WebService;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.RequestParams;

/**
 * Created by USER on 27/02/2019.
 */
public class GuardarDatosUsuario {
    private Utilidades Utilidad;
    private LatLng LocalizacionActual;
    public static Context Contexto;
    private MediaPlayer Sonido;
    private Uri notification;
    private Ringtone r;
    private Vibrator v;
    private RequestParams params;
    private LatLng UbicacionCero = new LatLng( 0.0,0.0 );

    public GuardarDatosUsuario(Context Contexto){
        this.Contexto = Contexto;
        Utilidad = new Utilidades(Contexto);
        LocalizacionActual = new LatLng( 0,0 );
        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        r = RingtoneManager.getRingtone(Contexto.getApplicationContext(), notification);
        v = (Vibrator) Contexto.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void GuardarDatosRec() {
        if (Utilidad.isNetworkAvailable()) {
            String nro_viaje = "0";
            params = new RequestParams();
            if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D") && WebService.usuarioActual.getEs_Entrega().equals("N")) {
                nro_viaje = WebService.viajeSeleccionadoCobrador.getNumViaje().trim();
                params.put("usuario", WebService.USUARIOLOGEADO);//1
                params.put("latitud_actual", String.valueOf(WebService.lat_actual));
                params.put("longitud_actual", String.valueOf(WebService.long_actual));
                params.put("latitud_destino", String.valueOf(WebService.clienteActual.getLatiud_Ubic()));
                params.put("longitud_destino", String.valueOf(WebService.clienteActual.getLongitud_Ubic()));
                params.put("latitud_origen", String.valueOf(WebService.lat_origen));
                params.put("longitud_origen", String.valueOf(WebService.long_origen));
                params.put("en_pausa", String.valueOf(WebService.EstadoActual));//12
                params.put("nro_viaje", nro_viaje);//9
                params.put("cod_sucursal", "0");//10
                params.put("nro_orden", "0");//13
                params.put("nom_cliente",WebService.clienteActual.getNom_Tit().trim());//11
                params.put("distancia_a_recorrer", WebService.distancia_a_recorrer);
            }else if(WebService.usuarioActual.getTipoCobrador().equals("L") || WebService.usuarioActual.getTipoCobrador().equals("F")){
                params.put("usuario", WebService.USUARIOLOGEADO);//1
                params.put("latitud_actual", String.valueOf(WebService.lat_actual));
                params.put("longitud_actual", String.valueOf(WebService.long_actual));
                params.put("latitud_destino", String.valueOf(WebService.clienteActual.getLatiud_Ubic()));
                params.put("longitud_destino", String.valueOf(WebService.clienteActual.getLongitud_Ubic()));
                params.put("latitud_origen", String.valueOf(WebService.lat_origen));
                params.put("longitud_origen", String.valueOf(WebService.long_origen));
                params.put("en_pausa", String.valueOf(WebService.EstadoActual));//12
                params.put("nro_viaje", nro_viaje);//9
                params.put("cod_sucursal", "0");//10
                params.put("nro_orden", "0");//13
                params.put("nom_cliente",WebService.clienteActual.getNom_Tit().trim());//11
                params.put("distancia_a_recorrer", WebService.distancia_a_recorrer);
            }
            ActualizarUbicacion actuUbic = new ActualizarUbicacion();
            actuUbic.execute();
            try {
                actuUbic.get();
            } catch (Exception exc) {
                exc.printStackTrace();
            }

        } else {
            Utilidad.CargarToastConexion(Contexto);
        }
    }

    public void GuardarDatos(){
        if(Utilidad.isNetworkAvailable()) {
            params = new RequestParams();
            //System.out.println("Entra en guardar datos");
            if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D") && WebService.usuarioActual.getEs_Entrega().equals("N")) {
                params.put("usuario", WebService.USUARIOLOGEADO);//1
                params.put("latitud_actual", String.valueOf(WebService.lat_actual));
                params.put("longitud_actual", String.valueOf(WebService.long_actual));
                params.put("latitud_destino", String.valueOf(WebService.clienteActual.getLatiud_Ubic()));
                params.put("longitud_destino", String.valueOf(WebService.clienteActual.getLongitud_Ubic()));
                params.put("latitud_origen", String.valueOf(WebService.lat_origen));
                params.put("longitud_origen", String.valueOf(WebService.long_origen));
                params.put("en_pausa", String.valueOf(WebService.EstadoActual));//12
                params.put("nro_viaje", WebService.viajeSeleccionadoCobrador.getNumViaje().trim());//9
                params.put("cod_sucursal", "0");//10
                params.put("nro_orden", "0");//13
                params.put("nom_cliente", WebService.clienteActual.getNom_Tit().trim());//11
                params.put("distancia_a_recorrer", WebService.distancia_a_recorrer);
            }else if(WebService.usuarioActual.getTipoCobrador().equals("L") && WebService.usuarioActual.getEs_Entrega().equals("N")){
                params.put("usuario", WebService.USUARIOLOGEADO);//1
                params.put("latitud_actual", String.valueOf(WebService.lat_actual));
                params.put("longitud_actual", String.valueOf(WebService.long_actual));
                params.put("latitud_destino", String.valueOf(WebService.clienteActual.getLatiud_Ubic()));
                params.put("longitud_destino", String.valueOf(WebService.clienteActual.getLongitud_Ubic()));
                params.put("latitud_origen", String.valueOf(WebService.lat_origen));
                params.put("longitud_origen", String.valueOf(WebService.long_origen));
                params.put("en_pausa", String.valueOf(WebService.EstadoActual));//12
                params.put("nro_viaje", "0");//9
                params.put("cod_sucursal", "0");//10
                params.put("nro_orden", "0");//13
                params.put("nom_cliente",WebService.clienteActual.getNom_Tit().trim());//11
                params.put("distancia_a_recorrer", WebService.distancia_a_recorrer);
            }else if(WebService.usuarioActual.getEs_VentaDirecta().equals("S")){
                params.put("usuario", WebService.USUARIOLOGEADO);//1
                params.put("latitud_actual", String.valueOf(WebService.lat_actual));
                params.put("longitud_actual", String.valueOf(WebService.long_actual));
                params.put("latitud_destino", String.valueOf(WebService.clienteActual.getLatiud_Ubic()));
                params.put("longitud_destino", String.valueOf(WebService.clienteActual.getLongitud_Ubic()));
                params.put("latitud_origen", String.valueOf(WebService.lat_origen));
                params.put("longitud_origen", String.valueOf(WebService.long_origen));
                params.put("en_pausa", String.valueOf(WebService.EstadoActual));//12
                params.put("nro_viaje", Integer.valueOf(WebService.viajeSeleccionado.getNumViaje()));//9
                params.put("cod_sucursal", WebService.sucursalActual.getCod_Sucursal());//10
                params.put("nro_orden", "0");//13
                params.put("nom_cliente",WebService.clienteActual.getNom_Tit().trim());//11
                params.put("distancia_a_recorrer", WebService.distancia_a_recorrer);
            }else {
                params.put("latitud_actual", String.valueOf(WebService.lat_actual));
                params.put("longitud_actual", String.valueOf(WebService.long_actual));
                params.put("nro_orden", Integer.valueOf(WebService.nro_orden));
                params.put("usuario", WebService.USUARIOLOGEADO);
                params.put("latitud_destino", String.valueOf(WebService.Entrega_A_Realizar.getLatiud_Ubic()));
                params.put("longitud_destino", String.valueOf(WebService.Entrega_A_Realizar.getLongitud_Ubic()));
                params.put("latitud_origen", String.valueOf(WebService.lat_origen));
                params.put("longitud_origen", String.valueOf(WebService.long_origen));
                params.put("nro_viaje", Integer.valueOf(WebService.viajeSeleccionado.getNumViaje()));
                params.put("cod_sucursal", WebService.cod_sucu);
                params.put("nom_cliente", WebService.nombreLocal.trim());
                params.put("en_pausa", String.valueOf(WebService.EstadoActual));
                //Valores agregados 14-03-2018
                params.put("distancia_a_recorrer", WebService.distancia_a_recorrer);
            }
            //System.out.println("Fin de guardar datos ");
            ActualizarUbicacion actuUbic = new ActualizarUbicacion();
            actuUbic.execute();
            try {
                actuUbic.get();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        } else {
            Utilidad.CargarToastConexion(Contexto);
        }
    }

    private class ActualizarUbicacion extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... strings) {
            WebService.ActualizarUbicacion( params,"Viajes/ActualizarUbicacion.php" );
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

    public void VerificacionDeEstado(){
        if(WebService.EstadoActual == 0) {
            try {
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate( VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(500);
            }
        }
    }



}
