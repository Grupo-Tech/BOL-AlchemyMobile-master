package com.example.user.trucksales.Visual.Cobranza;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.SeleccionFuncionablidad;
import com.example.user.trucksales.Datos.WebService;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class transmap_cobranza extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Context context;
    ProgressDialog dialog;
    public static ArrayList<String> URLDistanciaInicial1 = new ArrayList<String>(  );

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ai= cm.getActiveNetworkInfo();
        return ai!= null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_trans_map );
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById( R.id.map );
        mapFragment.getMapAsync( this );
        context = this;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        dialog = new ProgressDialog( this );
        dialog.setMessage( getResources().getString( R.string.buscarCliente ) );
        dialog.show();
        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled( true );

        LocationManager locationManager = (LocationManager)
                getSystemService( Context.LOCATION_SERVICE );
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation( locationManager
                .getBestProvider( criteria, false ) );
        double latitude =0;
        double longitude = 0;
        if(location==null)
        {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            WebService.lat_origen = latitude;
            WebService.long_origen = longitude;
            Intent myIntent = new Intent( context, SeleccionFuncionablidad.class );
            startActivity( myIntent );
        }
        else
        {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            WebService.lat_origen = latitude;
            WebService.long_origen = longitude;
            WebService.ubicacionActual = new LatLng( latitude,longitude );
            Intent myIntent = new Intent( context, SeleccionFuncionablidad.class );
            startActivity( myIntent );
          /*  for (int i = 0; i < WebService.entregasTraidas.size(); i++)
            {
                //entregaAConsultar = WebService.entregasTraidas.get( i );
                String UrlDistActual ="https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + ubicacionActual.latitude + "," + ubicacionActual.longitude + "&destinations=" + WebService.entregasTraidas.get( i ).getLatiud_Ubic() + "," + WebService.entregasTraidas.get( i ).getLongitud_Ubic() + "&key=AIzaSyBfGTwzYCGElsxU160HiLTPez4AjEPkVAI";
                URLDistanciaInicial1.add( UrlDistActual );
            }
            InvocarApiDistancia2 task3 = new InvocarApiDistancia2( new AsyncResponse() {
                public void processFinish(Object output) {
                    if (WebService.entregasTraidas.size() == 1) {
                        Calendar rightNow = Calendar.getInstance();
                        int hour = rightNow.get( Calendar.HOUR_OF_DAY );
                        int minute = rightNow.get( Calendar.MINUTE );
                        String horaAguardar = "";
                        if (hour < 10) {
                            horaAguardar = "0" + Integer.toString( hour ) + ":";
                        } else {
                            horaAguardar = Integer.toString( hour ) + ":";
                        }
                        if (minute < 10) {
                            horaAguardar = horaAguardar + "0" + Integer.toString( minute );
                        } else {
                            horaAguardar = horaAguardar + Integer.toString( minute );
                        }
                        WebService.horaComienzoViaje = horaAguardar;
                        WebService.Entrega_A_Realizar = WebService.entregasTraidas.get( 0 );
                        WebService.clienteDestino = WebService.entregasTraidas.get( 0 ).getNom_Tit();
                        WebService.nombreLocal = WebService.entregasTraidas.get( 0 ).getNom_Sucursal();
                        WebService.direccion = WebService.entregasTraidas.get( 0 ).getDireccion();
                        WebService.nro_orden = WebService.entregasTraidas.get( 0 ).getNro_Docum();
                        WebService.cod_sucu = WebService.entregasTraidas.get( 0 ).getCod_Sucursal();
                        WebService.cod_sucu = WebService.entregasTraidas.get( 0 ).getCod_Sucursal();
                        Intent myIntent = new Intent(context, Recorrido_Viaje.class );
                        startActivity( myIntent );
                    }
                    else
                    {
                        Intent myIntent = new Intent( context, ClienteXDefecto.class );
                        startActivity( myIntent );
                    }
                    dialog.dismiss();
                }
            });
            task3.execute();*/
        }

    }
    public interface AsyncResponse {
        void processFinish(Object output);
    }
    private class InvocarApiDistancia2 extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface
        public InvocarApiDistancia2(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            if(isNetworkAvailable())
            {
                WebService.TraerEntregaMasCercana(URLDistanciaInicial1);
                return null;

            }
            else
            {
                dispalyAlertConexion();
            }

            return null;
        }
        @Override
        public void onPreExecute()
        {
            dialog1.setMessage(getResources().getString( R.string.cargando_dialog));
            dialog1.show();
        }
        @Override
        protected void onPostExecute(Void result) {
            if(dialog1.isShowing())
            {
                dialog1.dismiss();
            }
            delegate.processFinish( WebService.logueado);
            //tv.setText(fahre11n + "� F");
            //dialog();
            //OcultarGif();
        }


        @Override
        protected void onProgressUpdate(Void... values)
        {

        }
        protected  void dispalyAlertConexion() {

            AlertDialog.Builder constructor = new AlertDialog.Builder( context );
            TextView texto1 = new TextView( context );
            constructor.setView( texto1 );
            constructor.setMessage( "No se pudo conectar" ).setCancelable(
                    false ).setPositiveButton( "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                dialog.cancel();
                            } catch (Exception exc) {
                            }
                        }
                    } );
            AlertDialog alerta1 = constructor.create();
            try {
                alerta1.getWindow().setType( WindowManager.LayoutParams.TYPE_SYSTEM_ALERT );
                alerta1.show();
            } catch (Exception errrssd) {
                errrssd.toString();
            }
        }
    }

}
