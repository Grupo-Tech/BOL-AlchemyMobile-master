package com.example.user.trucksales.Visual.TruckSales;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Cobranza.SeleccionCliente;
import com.example.user.trucksales.Visual.Login;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EmptyStackException;
import java.util.List;

public class PausarViaje extends Activity implements AdapterView.OnItemSelectedListener {

    Context context2;
    Spinner sp;
    Button btnPausa;
    ImageView atras;
    TextView nombreUsu, fecha, Retorno;
    List<String> spinnerArray = new ArrayList<>();
    protected static RequestParams params = new RequestParams();
    private Utilidades Utilidad;
    public static String viaje = "0";

    private class PausarViajeActual extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            WebService.PausarViaje( params, "Viajes/PausarViaje.php" );
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(!WebService.errToken.equals("")){
                Intent myIntent = new Intent(context2, Login.class);
                startActivity(myIntent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        this.requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_sppiner__prueba );
        context2 = this;

        GuardarDatosUsuario.Contexto = context2;
        Utilidad = new Utilidades( context2 );

        try{
        if (WebService.USUARIOLOGEADO != null) {

            String timeStamp = new SimpleDateFormat( "dd-MM-yyyy" ).format( Calendar.getInstance().getTime() );
            nombreUsu = (TextView) findViewById( R.id.LblUsu );
            fecha = (TextView) findViewById( R.id.LblFecha );
            fecha.setText( timeStamp );
            atras = findViewById( R.id.btnAtras );
            TraerMotivosPausa task = new TraerMotivosPausa();//BUSCA LOS MOTIVOS DE PAUSA GUARDADOS EN LA BASE DE DATOS
            task.execute();
            try {
                task.get();

            } catch (Exception exc) {
                exc.printStackTrace();
            }
            atras.setClickable( true );
            atras.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    try{
                    Utilidad.vibraticionBotones( context2 );
                    if(WebService.usuarioActual.getEs_Entrega().equals("S") && !WebService.usuarioActual.getEs_Cobranza().equals("S") || WebService.usuarioActual.getEs_Entrega().equals("S") && WebService.usuarioActual.getEs_Cobranza().equals("S")) {
                        Intent myIntent = new Intent( v.getContext(), Recorrido_Viaje.class );
                        startActivity( myIntent );
                    }
                    else {
                        Intent myIntent = new Intent(v.getContext(), Recorrido_Viaje.class);
                        startActivity(myIntent);
                    }
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            } );
            Retorno = findViewById( R.id.TxtRetorno );
            nombreUsu.setText( getResources().getString( R.string.usuarioTool ) + WebService.USUARIOLOGEADO );
            btnPausa = findViewById( R.id.btnPausa );

            if(WebService.usuarioActual.getTipoCobrador().equals("D")){
                viaje = WebService.viajeSeleccionadoCobrador.getNumViaje();
            }

            btnPausa.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    Utilidad.vibraticionBotones( context2 );
                    if (Utilidad.isNetworkAvailable()) {
                        if (!sp.getSelectedItem().toString().equals( "Seleccionar..." )) {
                            LocationManager locationManager = (LocationManager)
                                    getSystemService( Context.LOCATION_SERVICE );
                            Criteria criteria = new Criteria();
                            if (ActivityCompat.checkSelfPermission( context2, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( context2, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            Location location = locationManager.getLastKnownLocation( locationManager.getBestProvider( criteria, false ) );
                            double latitude = 0.0;
                            double longitude = 0.0;
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }

                            WebService.lat_actual = latitude;
                            WebService.long_actual = longitude;

                            PausarViajeActual task = new PausarViajeActual();

                            WebService.ultimaPausa = sp.getSelectedItem().toString().trim();
                            params.put("usuario", WebService.USUARIOLOGEADO);//1
                            params.put("motivo_pausa", WebService.ultimaPausa);//2
                            params.put("latitud_origen", String.valueOf(WebService.lat_origen));//5
                            params.put("longitud_origen", WebService.long_origen);//6
                            params.put("latitud_pausa", String.valueOf(WebService.lat_actual));//7
                            params.put("longitud_pausa", String.valueOf(WebService.long_actual));//8
                            params.put("en_pausa", "2");//12
                            params.put("longitud_actual", String.valueOf(WebService.long_actual));//14
                            params.put("latitud_actual", String.valueOf(WebService.lat_actual));//15

                            if(WebService.usuarioActual.getEs_Entrega().equals("S")) {
                                params.put("latitud_destino", WebService.entregaDefault.getLatiud_Ubic());//3
                                params.put("longitud_destino", WebService.entregaDefault.getLongitud_Ubic());//4
                                params.put("nro_viaje", WebService.viajeSeleccionado.getNumViaje());//9
                                params.put("cod_sucursal", WebService.cod_sucu);//10
                                params.put("nom_cliente", WebService.clienteDestino.trim());//11
                                params.put("nro_orden", WebService.nro_orden);//13
                            }
                            else if(WebService.usuarioActual.getEs_VentaDirecta().equals("S")){
                                params.put("latitud_destino", WebService.sucursalActual.getLatiud_Ubic() );//3
                                params.put("longitud_destino", WebService.sucursalActual.getLongitud_Ubic());//4
                                params.put("nro_viaje", WebService.viajeSeleccionado.getNumViaje());//9
                                params.put("cod_sucursal", WebService.clienteActual.getCod_Tit_Gestion());//10
                                params.put("nro_orden", "0");//13
                                params.put("nom_cliente", WebService.clienteActual.getNom_Tit());//11
                            }else{
                                params.put("latitud_destino", WebService.clienteActual.getLatiud_Ubic() );//3
                                params.put("longitud_destino", WebService.clienteActual.getLongitud_Ubic());//4
                                params.put("nro_viaje", viaje);//9
                                params.put("cod_sucursal", WebService.clienteActual.getCod_Tit_Gestion());//10
                                params.put("nro_orden", "0");//13
                                params.put("nom_cliente", WebService.clienteActual.getNom_Tit());//11
                            }
                            task.execute();

                            try {
                                task.get();
                                if (WebService.retornoPausa.equals( "1" )) {
                                    Retorno.setText( "La pausa se cargo correctamente" );
                                    if(WebService.usuarioActual.getEs_Entrega().equals("S")) {
                                        WebService.viajeSeleccionado.setEstado("2");
                                        WebService.EstadoAnterior = WebService.EstadoActual;
                                        WebService.EstadoActual = 2;
                                        Intent Volver;
                                        Volver = new Intent(context2, Recorrido_Viaje.class);
                                        Volver.putExtra("intent", "TruckSales");
                                        startActivity(Volver);
                                    }
                                    else if (WebService.usuarioActual.getEs_Cobranza().equals( "S" ) && WebService.usuarioActual.getTipoCobrador().equals("D") && WebService.lat_actual != 0.0 || WebService.usuarioActual.getEs_Cobranza().equals( "S" ) && WebService.usuarioActual.getTipoCobrador().equals("L") && WebService.lat_actual != 0.0 ){
                                        WebService.EstadoActual = 2;
                                        Intent Volver;
                                        Volver = new Intent(context2, Recorrido_Viaje.class);
                                        Volver.putExtra("intent", "Cobranza");
                                        startActivity(Volver);
                                    }
                                    else if(WebService.usuarioActual.getEs_Cobranza().equals( "S" ) && WebService.usuarioActual.getTipoCobrador().equals("D") && WebService.lat_actual == 0.0){
                                        Intent Volver = new Intent(context2, ClienteXDefecto.class);
                                        startActivity(Volver);
                                    }else if(WebService.usuarioActual.getEs_Cobranza().equals( "S" ) && WebService.usuarioActual.getTipoCobrador().equals("L") && WebService.lat_actual == 0.0){
                                        Intent Volver = new Intent(context2, SeleccionCliente.class);
                                        startActivity(Volver);
                                    }else if(WebService.usuarioActual.getEs_VentaDirecta().equals( "S" )){
                                        WebService.viajeSeleccionado.setEstado("2");
                                        WebService.EstadoAnterior = 3;
                                        WebService.EstadoActual = 2;
                                        Intent Volver = new Intent(context2, Recorrido_Viaje.class);
                                        Volver.putExtra("intent", "VentaDirecta");
                                        startActivity(Volver);
                                    }
                                } else {
                                    Retorno.setText( "Lo que devolvio la base de datos es distinto de 1" );
                                    Thread.sleep( 5000 );
                                    Retorno.setText( WebService.retornoPausa );
                                }

                            } catch (Exception exc) {
                                Retorno.setText( exc.toString() );
                            }
                        } else {
                            Retorno.setText( "Seleccione una pausa de la lista" );
                        }

                    } else {
                        Toast toast = Toast.makeText( context2, getResources().getString( R.string.ErrorConexion ), Toast.LENGTH_SHORT );
                        toast.setGravity( Gravity.CENTER, 0, 0 ); // last two args are X and Y are used for setting position
                        toast.setDuration( Toast.LENGTH_LONG );//you can even use milliseconds to display toast
                        toast.show();
                    }
                }
            });
            sp = (Spinner) findViewById( R.id.DDLMotivoPausa );
            sp.setOnItemSelectedListener( (AdapterView.OnItemSelectedListener) this );
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>( this,
                    android.R.layout.simple_spinner_item, spinnerArray );
            spinnerArray.add( "Seleccionar..." );
            for (int i = 0; i < WebService.ArrayMotivosPausa.size(); i++) {
                String nombreAgregar = WebService.ArrayMotivosPausa.get( i ).getNom_Pausa();
                spinnerArray.add( nombreAgregar );
            }
            dataAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
            sp.setAdapter( dataAdapter );
        } else {
            Intent myIntent = new Intent( context2, Login.class );
            //PONER ESTE STRING
            //myIntent.putExtra("Mensaje","PausarViaje");
            startActivity( myIntent );
        }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private class TraerMotivosPausa extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {

            WebService.TrearMotivosPausa( "Viajes/TraerMotivoPausa.php" );

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(!WebService.errToken.equals("")){
                Intent myIntent = new Intent(context2, Login.class);
                startActivity(myIntent);
            }
        }

    }

    @Override
    public void onBackPressed() {
        Utilidad.vibraticionBotones( context2 );
        try{
        if(WebService.usuarioActual.getEs_Entrega().equals("S") && !WebService.usuarioActual.getEs_Cobranza().equals("S") || WebService.usuarioActual.getEs_Entrega().equals("S") && WebService.usuarioActual.getEs_Cobranza().equals("S")) {
            Intent myIntent = new Intent( context2, Recorrido_Viaje.class );
            startActivity( myIntent );
        }
        else {
            Intent myIntent = new Intent(context2, ClienteXDefecto.class);
            startActivity(myIntent);
        }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}

