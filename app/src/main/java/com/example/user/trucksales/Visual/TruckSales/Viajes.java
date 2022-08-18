package com.example.user.trucksales.Visual.TruckSales;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.ClienteCobranza;
import com.example.user.trucksales.Encapsuladoras.Entrega;
import com.example.user.trucksales.Encapsuladoras.Viaje;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.FacturaDirecta.FacturaDirecta;
import com.example.user.trucksales.Visual.FacturaDirecta.Menu;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.Visual.Pedidos.Pedidos;
import com.example.user.trucksales.Visual.SeleccionFuncionablidad;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EmptyStackException;

public class Viajes extends AppCompatActivity  {
    LocationManager locationManager;
    Criteria criteria;
    private Location location;
    ImageView atras,btnConsulta, factura_directa;
    TextView nombreUsu,txtNombUsu,txtFecha;
    TableLayout tablaViajes;
    static Context context1;
    public static ArrayList<String> URLDistanciaInicial1 = new ArrayList<>(  );
    public  static  LatLng ubicacionActual;
    //public static ArrayList<Entrega> entregaAConsultar = new ArrayList<>();
    protected static RequestParams params1= new RequestParams();
    private int LOCATION_PERMISSION=0;
    Context contexto;
    private boolean localizacionBuscada;
    private Utilidades Utilidad;
    public ScrollView Lista;
    private int lastIndex;
    private static ImageView pedido;

    //devuelve el activity actual en el que se encuentra
    public Context getActivity() {
        return contexto;
    }

    //interfase de clases async
    public interface AsyncResponse {
        void processFinish(Object output);
    }

    //clasee verentrega
    private class  VerEntrega extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context1 );
        public AsyncResponse delegate = null;//Call back interface
        public VerEntrega(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            if(Utilidad.isNetworkAvailable()) {
                WebService.ListaEntregasViaje(params1,"Viajes/Entregas.php");
                return null;

            } else {
                Utilidad.dispalyAlertConexion(contexto);
            }

            return null;
        }
        @Override
        public void onPreExecute() {Utilidad.showLoadingMessage(); }
        @Override
        protected void onPostExecute(Void result) {
            delegate.processFinish( WebService.logueado);
        }

    }

    //clase traer estado
    private class TraerEstado  extends AsyncTask<String, Void, Void> {
     ProgressDialog dialog1 = new ProgressDialog( context1 );
     public AsyncResponse delegate = null;//Call back interface
     public TraerEstado(AsyncResponse asyncResponse) {
         delegate = asyncResponse;//Assigning call back interfacethrough constructor
     }
     @Override
     protected Void doInBackground(String... params) {

         if(Utilidad.isNetworkAvailable()) {
             params1 = new RequestParams(  );
             params1.add( "nro_viaje",WebService.viajeSeleccionado.getNumViaje() );
             WebService.ConsultarEstadoViaje("Viajes/consultarEstado.php",params1);
             return null;

         } else {
             Utilidad.dispalyAlertConexion(contexto);
         }

         return null;
     }
     @Override
     protected void onPostExecute(Void result) {
         delegate.processFinish( WebService.logueado);
     }


     @Override
     protected void onProgressUpdate(Void... values) {}
 }

    //clase para pedir los permisos de usuario
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] result){
        super.onRequestPermissionsResult(requestCode, permissions, result);
        if(requestCode == LOCATION_PERMISSION && result[0] == PackageManager.PERMISSION_GRANTED){
            //do things as usual init map or something else when location permission is granted
        }
    }

    //clase creadora del activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_viajes);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        try {
            localizacionBuscada = false;
            context1 = this;
            contexto = this;
            GuardarDatosUsuario.Contexto = context1;
            Utilidad = new Utilidades(context1);
            WebService.Entrega_A_Realizar = new Entrega();
            WebService.ListaRetiros = new ArrayList<>();
            WebService.entregasTraidas = new ArrayList<>();
            WebService.ArrayItemsViaje = new ArrayList<>();
            WebService.entregaDefault = new Entrega();
            WebService.viajeSeleccionado = new Viaje();
            WebService.ViajeActualSeleccionado = false;
            WebService.EstadoActual = 0;
            lastIndex = 0;
            if (WebService.USUARIOLOGEADO != null) {

                pedido = findViewById( R.id.btnPedidos );
                pedido.setVisibility(View.GONE);
                if(WebService.usuarioActual.getEs_Pedidos().equals("S")){
                    pedido.setVisibility(View.VISIBLE);
                    pedido.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Utilidad.vibraticionBotones(contexto);
                                Intent myIntent = new Intent(v.getContext(), Pedidos.class);
                                myIntent.putExtra("intent", "Viajes");
                                startActivity(myIntent);
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    });
                }

                atras = findViewById(R.id.btnAtras);
                atras.setClickable(true);
                atras.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        try {
                            Utilidad.vibraticionBotones(context1);
                            if (!WebService.usuarioActual.getEs_Cobranza().equals("S")) {
                                WebService.USUARIOLOGEADO = null;
                                WebService.logueado = false;
                                Intent myIntent = new Intent(v.getContext(), Login.class);
                                startActivity(myIntent);
                            } else {
                                Intent myIntent = new Intent(v.getContext(), SeleccionFuncionablidad.class);
                                startActivity(myIntent);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                factura_directa = (ImageView) findViewById(R.id.factura_directa);
                factura_directa.setClickable(true);

                //METODO ON CLICK DEL factura_directa
                factura_directa.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        try {
                            Utilidad.vibraticionBotones(context1);
                            if (Utilidad.isNetworkAvailable()) {
                                Intent myIntent = new Intent(v.getContext(), FacturaDirectaCono.class);
                                startActivity(myIntent);
                            } else {
                                Utilidad.dispalyAlertConexion(contexto);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                        }
                    }

                });

                btnConsulta = findViewById(R.id.BtnConsulta);

                if (WebService.usuarioActual.getEs_VentaDirecta().equals("S")) {
                    btnConsulta.setVisibility(View.GONE);
                }

                btnConsulta.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Utilidad.vibraticionBotones(context1);
                        Intent myIntent = new Intent(v.getContext(), Consultas.class);
                        myIntent.putExtra("intent", "Viajes");
                        startActivity(myIntent);
                    }
                });
                nombreUsu = (TextView) findViewById(R.id.nombreUsuario);
                nombreUsu.setText(getResources().getString(R.string.usuarioTool) + WebService.USUARIOLOGEADO);
                String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                txtFecha = (TextView) findViewById(R.id.Fecha);
                txtFecha.setText(timeStamp);
                Lista = (ScrollView) findViewById(R.id.ListaViajes);
                tablaViajes = (TableLayout) findViewById(R.id.tabla);

                final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

                swipeRefreshLayout.setColorSchemeColors(Color.BLACK);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeRefreshLayout.setRefreshing(true);
                        if (WebService.usuarioActual.getEs_Entrega().equals("S")) {
                            CargarViajes task = new CargarViajes(new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {
                                    if(!WebService.errToken.equals("")){
                                        Intent myIntent = new Intent(contexto, Login.class);
                                        startActivity(myIntent);
                                    }else {
                                        tablaViajes.removeAllViews();
                                        cargarDatos();
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }
                            });
                            task.execute();
                        } else {
                            CargarViajesVenta task = new CargarViajesVenta(new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {
                                    if(!WebService.errToken.equals("")){
                                        Intent myIntent = new Intent(contexto, Login.class);
                                        startActivity(myIntent);
                                    }else {
                                        tablaViajes.removeAllViews();
                                        cargarDatos();
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }
                            });
                            task.execute();
                        }
                    }
                });

                cargarDatos();
                txtNombUsu = (TextView) findViewById(R.id.NomUus);
                txtNombUsu.setText(getResources().getString(R.string.Hola) + " " + WebService.USUARIOLOGEADO + " " + getResources().getString(R.string.Tienes) + " " + WebService.viajesUsu.size() + " " + getResources().getString(R.string.Viajes) + "\n" + " " + getResources().getString(R.string.Selec));
                if (WebService.viajesUsu.size() == 0) {
                    txtNombUsu.setText(getResources().getString(R.string.Hola) + " " + WebService.USUARIOLOGEADO + " " + getResources().getString(R.string.Tienes) + " " + WebService.viajesUsu.size() + " " + getResources().getString(R.string.Viajes) + "\n" + " " + getResources().getString(R.string.Selec));
                }
            } else {
                Intent myIntent = new Intent(context1, Login.class);
                //PONER ESTE STRING
                //myIntent.putExtra("Mensaje","Viajes");
                startActivity(myIntent);
                // throw new EmptyStackException();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //tarea numero 3
    private void Tarea3() {
        try{
        if (WebService.entregasTraidas.size() == 1) {//SI LAS ENTREGAS TRAIDAS SON 1 ENTONCES
            Calendar rightNow = Calendar.getInstance();//CREA UN CALENDARIO
            int hour = rightNow.get( Calendar.HOUR_OF_DAY );//PIDE LA HORA Y EL MINUTO
            int minute = rightNow.get( Calendar.MINUTE );
            String horaAguardar = "";
            if (hour < 10) {//SI LA HORA ES INFERIOR A 10 GUARDA UN 0 ANTES DE LA HORA ACTUAL
                horaAguardar = "0" + Integer.toString( hour ) + ":";
            } else {
                horaAguardar = Integer.toString( hour ) + ":";
            }
            if (minute < 10) {//SI LOS MINUTOS ACTUALES SON MENORES A 10 GUARDA UN 0 ANTES DEL MINUTO ACTUAL
                horaAguardar = horaAguardar + "0" + Integer.toString( minute );
            } else {
                horaAguardar = horaAguardar + Integer.toString( minute );
            }
            WebService.horaComienzoViaje = horaAguardar;//GUARDA LA HORA DEL COMIENZO DEL VIAJE
            WebService.Entrega_A_Realizar = WebService.entregasTraidas.get( 0 );//GUARDA LA ENTREGA A REALIZAR COMO LA ENTREGA TRAIDA
            WebService.clienteDestino = WebService.entregasTraidas.get( 0 ).getNom_Sucursal();//GUARDA EL CLIENTE
            WebService.nombreLocal = WebService.entregasTraidas.get( 0 ).getNom_Sucursal();//GUARDA EL NOMBRE DEL LOCAL
            WebService.direccion = WebService.entregasTraidas.get( 0 ).getDireccion();//GUARDA LA DIRECCION
            WebService.nro_orden = WebService.entregasTraidas.get( 0 ).getNro_Docum();//GUARDA EL NUMERO DE ORDEN
            WebService.cod_sucu = WebService.entregasTraidas.get( 0 ).getCod_Sucursal();//GUARDA EL CODIGO DE SUCURSAL
            WebService.cod_sucu = WebService.entregasTraidas.get( 0 ).getCod_Sucursal();//GUARDA EL CODIGO DE LA SUCURSAL
            Intent myIntent = new Intent( contexto, Recorrido_Viaje.class );//TRATA DE IRSE AL ACTIVITY RECORRIDO VIAJE
            myIntent.putExtra("intent", "TruckSales");
            startActivity( myIntent );
        } else {//SI LAS ENTREGAS TRAIDAS NO SON IGUALES A 1 ENTONCES
            //MANDA DIRECTAMENTE A CLIENTE POR DEFECTO
            Intent myIntent = new Intent( contexto, ClienteXDefecto.class );
            startActivity( myIntent );
        }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //clase InvocarApiDistancia2
    private class InvocarApiDistancia2 extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context1 );
        public AsyncResponse delegate = null;//Call back interface
        public InvocarApiDistancia2(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            if(Utilidad.isNetworkAvailable()) {
                WebService.TraerEntregaMasCercana(URLDistanciaInicial1);
                return null;

            } else {
                Utilidad.dispalyAlertConexion(contexto);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Utilidad.deleteLoadingMessage();
            delegate.processFinish( WebService.logueado);
        }


        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private void cargarDatos(){
        try{
        final TableRow espacio = new TableRow( this );
        lastIndex = 0;
        WebService.ViajeActualSeleccionado = false;
        for (int i = 0; i < WebService.viajesUsu.size(); i++) {
            try {
                final Viaje viaje1 = WebService.viajesUsu.get( i );
                final TextView NumViaje = new TextView( context1 );
                TableRow tr1;
                if (viaje1.getTipo().equals( "retiro" )) {
                    tr1 = (TableRow) getLayoutInflater().inflate( R.layout.trretiros, null );
                } else {
                    tr1 = (TableRow) getLayoutInflater().inflate( R.layout.trviajes, null );
                }
                TableRow tr2 = new TableRow( this );
                final TextView salto = new TextView( this );
                salto.setText( "\n" );
                tr2.addView( salto );

                ImageView camionsito = new ImageView( context1 );
                final TextView numViajePosta = new TextView( context1 );
                numViajePosta.setText( viaje1.getNumViaje() );

                camionsito.setImageResource( R.drawable.truckicon0 );
                camionsito.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT ) );

                camionsito.getLayoutParams().height = 125;
                camionsito.getLayoutParams().width = 125;
                camionsito.requestLayout();

                NumViaje.setText( getResources().getString( R.string.NumViajeProd ) + viaje1.getNumViaje().trim() + "\n" + getResources().getString( R.string.Turno ) + " " + viaje1.getTurno().trim() + "                            " );
                NumViaje.setTypeface( null, Typeface.BOLD );//Este hace negrita todo el codigo
                NumViaje.setTextSize( 20 );
                NumViaje.setTextColor( Color.parseColor( "#000000" ) );

                numViajePosta.setVisibility( View.GONE );
                if (viaje1.getTipo().equals( "retiro" )) {
                    NumViaje.setText( NumViaje.getText() + "\n" + "Retiro de envases" );

                }
                if(viaje1.getTipo().equals("ventadirecta")){
                    NumViaje.setText( NumViaje.getText() + "\n" + getResources().getString(R.string.venta) );
                }
                tr1.addView( NumViaje );
                tr1.addView( numViajePosta );

                tr1.addView( camionsito );
                tr1.setClickable( true );
                tr1.setOnClickListener( new View.OnClickListener() {
                    public void onClick(final View v) {
                        //AL CLICKEAR DENTRO DE LA CAJA DE CADA VIAJE
                        Utilidad.vibraticionBotones( context1 );
                        if (Utilidad.isNetworkAvailable()) { //SI HAY INTERNET SIGUE
                            //SI LA APP NO TIENE LOS PERMISOS DE GEOLOCALIZACION LOS PIDE
                            if (ActivityCompat.checkSelfPermission( context1, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( context1, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions( (Activity) context1, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1 );
                                return;
                            } else {
                                //SI LOS TIENE SIGUE CON EL CODIGO
                                WebService.viajeSeleccionado = new Viaje();
                                WebService.viajeSeleccionado = viaje1;

                                if(WebService.usuarioActual.getEs_Entrega().equals("S")){
                                ////////////////////
                                params1 = new RequestParams();
                                params1.put("nro_viaje", WebService.viajeSeleccionado.getNumViaje());
                                params1.put("seleccion", WebService.viajeSeleccionado.getTipo().equals("retiro") ? "r" : "e");
                                params1.put("username", WebService.USUARIOLOGEADO);
                                final VerEntrega task = new VerEntrega(new AsyncResponse() {
                                    public void processFinish(Object output) {
                                        if (!WebService.errToken.equals("")) {
                                            Intent myIntent = new Intent(contexto, Login.class);
                                            startActivity(myIntent);
                                        } else {
                                            TraerEstado task2 = new TraerEstado(new AsyncResponse() {
                                                public void processFinish(Object output) {
                                                    WebService.ViajeActualSeleccionado = true;
                                                    if (WebService.viajeSeleccionado.getTipo()!=null) {
                                                    if (WebService.viajeSeleccionado.getTipo().equals("entrega")) {
                                                        if (WebService.entregasTraidas.size() > 0) {
                                                            //SI LAS ENTREGAS POSIBLES SON MAYORES A 0 (SI EXISTEN ENTREGAS) SIGUE EL CODIGO NORMAL
                                                            if (WebService.viajeSeleccionado.getEstado() == null) {
                                                                WebService.viajeSeleccionado.setEstado("1");
                                                            }
                                                            WebService.entregaDefault = WebService.entregasTraidas.get(0);
                                                            try {
                                                                if (location == null) {//SI LA GEOLOCALIZACION ES NULL ENTONCES
                                                                    if (localizacionBuscada) {//SI LA LOCALIZACION BUSCADA ES TRUE
                                                                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.ToastText), Toast.LENGTH_SHORT);
                                                                        toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                                                                        toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                                                                        toast.show();//showing the toast is important**
                                                                    }
                                                                    Intent myIntent = new Intent(contexto, TransMap.class);
                                                                    startActivity(myIntent);
                                                                    //String provaider = locManager.getBestProvider( criteria, true );
                                                                    try {
                                                                        // Utilidad.PedirLocacion( contexto );//PEDIR LA GEOLOCALIZACION
                                                                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);//PIDE EL ADMINISTRADOR DE LA GEOLOCALIZACION
                                                                        criteria = new Criteria();//BUSCADOR MULTIPARAMETROS
                                                                        //GEO:LOC | BUSQUEDA Y POSICIONAMIENTO DE LA UBICACION ACTUAL
                                                                        if (ActivityCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                                                            // TODO: Consider calling
                                                                            //    ActivityCompat#requestPermissions
                                                                            // here to request the missing permissions, and then overriding
                                                                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                                            //                                          int[] grantResults)
                                                                            // to handle the case where the user grants the permission. See the documentation
                                                                            // for ActivityCompat#requestPermissions for more details.
                                                                            return;
                                                                        }
                                                                        location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));//BUSCA LA LOCALIZACION MAS CERCANA
                                                                        double latitude = 0;
                                                                        double longitude = 0;
                                                                        if (location == null) {//SI LA LOCALIZACION ES NULA ENTONCES
                                                                            latitude = 0;
                                                                            longitude = 0;
                                                                        } else {//SI EXISTE UN USUARIO MAS CERCANO ENTONCES LO GUARDA EN VARIABLES
                                                                            latitude = location.getLatitude();
                                                                            longitude = location.getLongitude();
                                                                        }
                                                                        WebService.lat_origen = latitude;
                                                                        WebService.long_origen = longitude;
                                                                        ubicacionActual = new LatLng(latitude, longitude);//GUARDA LA UBICACION ACTUAL
                                                                        //GEO:LOC | FIN DE BUSQUEDA Y POSICIONAMIENTO DE LA UBICACION ACTUAL
                                                                        for (int i = 0; i < WebService.entregasTraidas.size(); i++) {
                                                                            //entregaAConsultar = WebService.entregasTraidas.get( i );
                                                                            String UrlDistActual = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + ubicacionActual.latitude + "," + ubicacionActual.longitude + "&destinations=" + WebService.entregasTraidas.get(i).getLatiud_Ubic() + "," + WebService.entregasTraidas.get(i).getLongitud_Ubic() + "&key=AIzaSyBQKwACTJTM2k25jnI7itqYKbeap2vKULQ";
                                                                            //System.out.println( "La url de la entrega es: " + UrlDistActual );
                                                                            URLDistanciaInicial1.add(UrlDistActual);
                                                                        }
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                        // Toast.makeText( getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG ).show();
                                                                    }

                                                                    InvocarApiDistancia2 task3 = new InvocarApiDistancia2(new AsyncResponse() {//CREA OTRA TAREA EN PARALELO
                                                                        public void processFinish(Object output) {
                                                                            //Tarea3();
                                                                        }
                                                                    });
                                                                    task3.execute();
                                                                    localizacionBuscada = true;
                                                                } else {//SI YA TIENE UNA LOCALIZACION ENTONCES
                                                                    //GEO:LOC | BUSQUEDA Y POSICIONAMIENTO DE LA UBICACION ACTUAL
                                                                    double latitude = location.getLatitude();
                                                                    double longitude = location.getLongitude();
                                                                    WebService.lat_origen = latitude;
                                                                    WebService.long_origen = longitude;
                                                                    ubicacionActual = new LatLng(latitude, longitude);
                                                                    //GEO:LOC | FIN DE BUSQUEDA Y POSICIONAMIENTO DE LA UBICACION ACTUAL
                                                                    Intent myIntent = new Intent(contexto, TransMap.class);
                                                                    startActivity(myIntent);
                                                                    for (int i = 0; i < WebService.entregasTraidas.size(); i++) {
                                                                        //entregaAConsultar = WebService.entregasTraidas.get( i );
                                                                        String UrlDistActual = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + ubicacionActual.latitude + "," + ubicacionActual.longitude + "&destinations=" + WebService.entregasTraidas.get(i).getLatiud_Ubic() + "," + WebService.entregasTraidas.get(i).getLongitud_Ubic() + "&key=AIzaSyBQKwACTJTM2k25jnI7itqYKbeap2vKULQ";
                                                                        URLDistanciaInicial1.add(UrlDistActual);
                                                                    }
                                                                    //HACE LO MISMO QUE EL TASK3 ANTERIOR
                                                                    InvocarApiDistancia2 task3 = new InvocarApiDistancia2(new AsyncResponse() {
                                                                        public void processFinish(Object output) {
                                                                            //Tarea3();
                                                                        }
                                                                    });
                                                                    task3.execute();
                                                                }
                                                            } catch (Exception exc) {
                                                                exc.printStackTrace();
                                                                //Toast.makeText( getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG ).show();
                                                            }

                                                        } else {//SI LAS ENTREGAS DEL VIAJE SON IGUALES O MENORES A 0 ENTONCES
                                                            Toast.makeText(contexto, getResources().getString(R.string.NoHayEntregas), Toast.LENGTH_LONG).show();
                                                        }
                                                    } else {
                                                        if (WebService.ListaRetiros.size() != 0) {
                                                            WebService.RetiroSeleccionado = WebService.ListaRetiros.get(0);
                                                            Intent nextActivity = new Intent(contexto, ClienteXDefecto.class);
                                                            startActivity(nextActivity);
                                                        }
                                                    }
                                                    } else {
                                                        if (WebService.ListaRetiros.size() != 0) {
                                                            WebService.RetiroSeleccionado = WebService.ListaRetiros.get(0);
                                                            Intent nextActivity = new Intent(contexto, ClienteXDefecto.class);
                                                            startActivity(nextActivity);
                                                        }
                                                    }
                                                }
                                            });
                                            task2.execute();
                                        }
                                    }
                                });
                                task.execute();
                            }else if(WebService.usuarioActual.getEs_VentaDirecta().equals("S") && WebService.viajeSeleccionado.getTipo().equals("ventadirecta")){
                                    WebService.ViajeActualSeleccionado = true;
                                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                    Criteria criteria = new Criteria();
                                    if (ActivityCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        return;
                                    }
                                    Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                                    double latitude = 0;
                                    double longitude = 0;
                                    if (location != null) {
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                    }

                                    WebService.lat_origen = latitude;
                                    WebService.long_origen = longitude;
                                    ubicacionActual = new LatLng(latitude, longitude);

                                    WebService.viajeSeleccionado = new Viaje();
                                    WebService.viajeSeleccionado = viaje1;

                                    Intent nextActivity = new Intent(contexto, Menu.class);
                                    startActivity(nextActivity);
                                }
                            }
                        }
                        else {//SI NO HAY CONEXION A INTERNET ENTONCES
                            Toast toast = Toast.makeText( getApplicationContext(), getResources().getString( R.string.ErrorConexion ), Toast.LENGTH_SHORT );
                            toast.setGravity( Gravity.CENTER, 0, 0 ); // last two args are X and Y are used for setting position
                            toast.setDuration( Toast.LENGTH_LONG );//you can even use milliseconds to display toast
                            toast.show();//showing the toast is important**
                        }
                    }
                } );
                tablaViajes.addView( tr1 );
                tablaViajes.addView( tr2 );
                lastIndex =+ 2;
            } catch (Exception exc) {
                exc.toString();
               // Toast.makeText( getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG ).show();
            }
        }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private class CargarViajes extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context1 );
        public AsyncResponse delegate = null;//Call back interface
        public CargarViajes(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            params1 = new RequestParams(  );
            params1.put( "username",WebService.usuarioActual.getNombre() );
            WebService.TraerViajes(params1,"Viajes/Viajes.php");
            return null;
        }
        @Override
        public void onPreExecute() {
            Utilidad.showLoadingMessage();
        }
        @Override
        protected void onPostExecute(Void result) {
            delegate.processFinish( WebService.logueado);
            Utilidad.deleteLoadingMessage();
        }
        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private void ActualizarLista(int indexDesde,int IndexCantidad) {
        tablaViajes.removeViews( indexDesde,IndexCantidad );
    }

    @Override
    public void onBackPressed() {
        try{
        Utilidad.vibraticionBotones( context1 );
        if (WebService.USUARIOLOGEADO != null) {
            if (!WebService.usuarioActual.getEs_Cobranza().equals( "S" )) {
                WebService.USUARIOLOGEADO = null;
                WebService.logueado = false;
                Intent myIntent = new Intent( contexto, Login.class );
                startActivity( myIntent );
            } else {
                Intent myIntent = new Intent( contexto, SeleccionFuncionablidad.class );
                startActivity( myIntent );
            }
        }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private class CargarViajesVenta extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface
        public CargarViajesVenta(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            params1 = new RequestParams(  );
            params1.put( "username",WebService.usuarioActual.getNombre() );
            WebService.TraerViajes(params1,"Venta/Viajes.php");
            return null;
        }
        @Override
        public void onPreExecute() {
            Utilidad.showLoadingMessage();
        }
        @Override
        protected void onPostExecute(Void result) {
            delegate.processFinish( WebService.logueado);
            Utilidad.deleteLoadingMessage();
        }
        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}