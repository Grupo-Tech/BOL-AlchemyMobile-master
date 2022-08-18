package com.example.user.trucksales.Visual.TruckSales;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.Viaje;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.FacturaDirecta.BuscarFactura;
import com.example.user.trucksales.Visual.FacturaDirecta.FacturaDirecta;
import com.example.user.trucksales.Visual.FacturaDirecta.ReporteStock;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.Visual.Pedidos.Pedidos;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Consultas extends Activity {
    TextView nombreUsu,TxtFecha;
    Button facturaGeneradas,botonTraerAnulados, btnFaltantes, btnReporte;
    private static RequestParams params;
    private static RequestParams params1;
    ImageView atras;
    Context contexto;
    Utilidades Utilidad;
    public static ArrayList<String> URLDistanciaInicial1 = new ArrayList<>(  );
    private Location location;
    private boolean localizacionBuscada;
    public  static LatLng ubicacionActual;
    LocationManager locationManager;
    public static String valorIntent;
    Criteria criteria;
    View actualView;

    private static ImageView pedido;

    public void CargarToastError() {
        Toast toast = Toast.makeText( contexto, getResources().getString( R.string.ErrorAnulados ), Toast.LENGTH_SHORT );
        toast.setGravity( Gravity.CENTER, 0, 0 ); // last two args are X and Y are used for setting position
        toast.setDuration( Toast.LENGTH_LONG );//you can even use milliseconds to display toast
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_consultas );
        contexto = this;
        Utilidad = new Utilidades( contexto );
        actualView = new View(contexto );
            if(WebService.USUARIOLOGEADO!=null) {

            try {
                valorIntent = getIntent().getStringExtra("intent");
            }catch (Exception ex){
                ex.printStackTrace();
            }

                btnFaltantes = findViewById(R.id.btnFaltantes);
                if(WebService.usuarioActual.getEs_VentaDirecta().equals("S")) {
                    btnFaltantes.setVisibility(View.GONE);
                }

                btnReporte = findViewById(R.id.btnReporte);
                btnReporte.setVisibility(View.GONE);

                if(WebService.usuarioActual.getEs_VentaDirecta().equals("S")){
                    if(valorIntent.equals("FacturaDirecta")){
                        btnReporte.setVisibility(View.VISIBLE);
                        btnReporte.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TraerProductos task01 = new TraerProductos(new AsyncResponse() {
                                    @Override
                                    public void processFinish(Object output) {
                                        if (!WebService.errToken.equals("")) {
                                            Intent myIntent = new Intent(contexto, Login.class);
                                            startActivity(myIntent);
                                        }else {
                                            Intent myIntent = new Intent(contexto, ReporteStock.class);
                                            myIntent.putExtra("intent", valorIntent);
                                            startActivity(myIntent);
                                        }
                                    }
                                });
                                task01.execute();
                            }
                        });
                    }
                }

                botonTraerAnulados = findViewById( R.id.btnAnulado );
                botonTraerAnulados.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        Utilidad.vibraticionBotones( contexto );
                        try {
                            if (Utilidad.isNetworkAvailable()) {
                                TrearAnulados task = new TrearAnulados(new AsyncResponse() {
                                    public void processFinish(Object output) {
                                        if (!WebService.errToken.equals("")) {
                                            Intent myIntent = new Intent(contexto, Login.class);
                                            startActivity(myIntent);
                                        } else {
                                            if (WebService.banderaAnuladas)//Verifico que haya traido datos sin drama
                                            {
                                                Intent myIntent = new Intent(contexto, Anulados.class);
                                                myIntent.putExtra("intent", valorIntent);
                                                startActivity(myIntent);
                                            } else {
                                                CargarToastError();
                                            }
                                        }
                                    }
                                });
                                task.execute();
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.ErrorConexion), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                                toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                                toast.show();//showing the toast is important**
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
                atras = findViewById( R.id.btnAtras );
                atras.setClickable( true );
                atras.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        Utilidad.vibraticionBotones( contexto );
                        BotonVolver( v );
                    }
                } );
                nombreUsu = findViewById( R.id.nombreUsuario );
                nombreUsu.setText( getResources().getString( R.string.usuarioTool ) + WebService.USUARIOLOGEADO );

                String timeStamp = new SimpleDateFormat( "dd-MM-yyyy" ).format( Calendar.getInstance().getTime() );
                TxtFecha = findViewById( R.id.Fecha );
                TxtFecha.setText( timeStamp );

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
                                myIntent.putExtra("intent", "Consultas");
                                startActivity(myIntent);
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    });
                }

                facturaGeneradas = findViewById( R.id.btnFactGenerada );
                facturaGeneradas.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        Utilidad.vibraticionBotones( contexto );
                        if (Utilidad.isNetworkAvailable()) {
                            params = new RequestParams();
                            params.add( "username", WebService.USUARIOLOGEADO );
                            TraerFacturas task = new TraerFacturas( new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {
                                    if (!WebService.errToken.equals("")) {
                                        Intent myIntent = new Intent(contexto, Login.class);
                                        startActivity(myIntent);
                                    } else {
                                        if (WebService.listaRemitos.size() != 0 || WebService.listafacturas.size() != 0) {
                                            try {
                                                Intent myIntent = new Intent(contexto, Generados.class);
                                                myIntent.putExtra("intent", valorIntent);
                                                startActivity(myIntent);
                                            } catch (Exception exc) {
                                                exc.printStackTrace();
                                                //  Toast.makeText( getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG ).show();
                                            }
                                        } else {
                                            Toast.makeText(contexto, "No existen facturas o remitos generados", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            } );
                            task.execute();
                        } else {
                            Toast toast = Toast.makeText( getApplicationContext(), getResources().getString( R.string.ErrorConexion ), Toast.LENGTH_SHORT );
                            toast.setGravity( Gravity.CENTER, 0, 0 ); // last two args are X and Y are used for setting position
                            toast.setDuration( Toast.LENGTH_LONG );//you can even use milliseconds to display toast
                            toast.show();//showing the toast is important**
                        }
                    }
                } );
            } else {
                //System.out.println( "Antes de entrar a generados" );
                Intent myIntent = new Intent( contexto, Generados.class );
                startActivity( myIntent );
            }
    }

    private class TraerFacturas extends AsyncTask<String,Void,Void> {
        public AsyncResponse delegate = null;//Call back interface
        public TraerFacturas(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... strings) {
            WebService.TraerFacturasXDia("Consultas/generados.php",params );
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
    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    private class  TrearAnulados extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface
        public TrearAnulados(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            params1 = new RequestParams(  );
            params1.put( "username",WebService.USUARIOLOGEADO );
            WebService.TraerListaAnuladas(params1,"Facturas/TraerAnuladas.php");
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
        protected void onProgressUpdate(Void... values) { }
    }

    private void BotonVolver(View v){
        try{
        if(WebService.ViajeActualSeleccionado) {
            if (WebService.viajeSeleccionado.getTipo().equals( "entrega" )) {
                Intent NextActivity = new Intent( v.getContext(), Recorrido_Viaje.class );
                NextActivity.putExtra("intent", "TruckSales");
                startActivity( NextActivity );
            } else if (WebService.viajeSeleccionado.getTipo().equals( "retiro" )) {
                SeleccionarViaje();
                Intent NextActivity = new Intent( v.getContext(), ClienteXDefecto.class );
                startActivity( NextActivity );
            }else if (WebService.viajeSeleccionado.getTipo().equals( "ventadirecta" )) {
                Intent NextActivity = new Intent( contexto, FacturaDirecta.class );
                startActivity( NextActivity );
            }
        }else{
            final Intent NextActivity = new Intent(contexto,Viajes.class);
            if(Generados.Anulado){
                CargarViajes tarea = new CargarViajes( new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        if(!WebService.errToken.equals("")){
                            Intent myIntent = new Intent(contexto, Login.class);
                            startActivity(myIntent);
                        }else {
                            startActivity(NextActivity);
                        }
                    }
                } );
                tarea.execute(  );
            }else{startActivity( NextActivity );}
        }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void SeleccionarViaje(){
        if (Utilidad.isNetworkAvailable()) { //SI HAY INTERNET SIGUE
            //SI LA APP NO TIENE LOS PERMISOS DE GEOLOCALIZACION LOS PIDE
            if (ActivityCompat.checkSelfPermission( contexto, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( contexto, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions( (Activity) contexto, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1 );
                return;
            } else {
                //SI LOS TIENE SIGUE CON EL CODIGO
                WebService.viajeSeleccionado = new Viaje();
                boolean PrimeroSeleccionado = false;
                for(int index = 0; index < WebService.viajesUsu.size();index++){
                    if((!WebService.viajesUsu.get(index).getTipo().equals( "retiro" ))&&(!PrimeroSeleccionado)){
                        WebService.viajeSeleccionado = WebService.viajesUsu.get( index );
                        WebService.LastIndexSelected = Integer.parseInt( WebService.viajesUsu.get( index ).getNumViaje() );
                        WebService.IndexViajes = index;
                        PrimeroSeleccionado = true;
                    }
                }
                params1 = new RequestParams();
                params1.put( "nro_viaje", WebService.viajeSeleccionado.getNumViaje() );
                params1.put( "username", WebService.USUARIOLOGEADO );
                final VerEntrega task = new VerEntrega( new Login.AsyncResponse() {
                    public void processFinish(Object output) {
                        if (!WebService.errToken.equals("")) {
                            Intent myIntent = new Intent(contexto, Login.class);
                            startActivity(myIntent);
                        } else {
                            TraerEstado task2 = new TraerEstado(new Login.AsyncResponse() {
                                public void processFinish(Object output) {
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
                                                //TRATA DE CAMBIAR DE ACTIVITY, SE MUEVE AL MAPA
                                                Intent myIntent = new Intent(contexto, TransMap.class);
                                                startActivity(myIntent);
                                                //String provaider = locManager.getBestProvider( criteria, true );
                                                //Utilidad.PedirLocacion(contexto);//PEDIR LA GEOLOCALIZACION
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

                                                InvocarApiDistancia2 task3 = new InvocarApiDistancia2(new CajasTraidas.AsyncResponse() {//CREA OTRA TAREA EN PARALELO
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
                                                for (int i = 0; i < WebService.entregasTraidas.size(); i++) {
                                                    //entregaAConsultar = WebService.entregasTraidas.get( i );
                                                    String UrlDistActual = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + ubicacionActual.latitude + "," + ubicacionActual.longitude + "&destinations=" + WebService.entregasTraidas.get(i).getLatiud_Ubic() + "," + WebService.entregasTraidas.get(i).getLongitud_Ubic() + "&key=AIzaSyBQKwACTJTM2k25jnI7itqYKbeap2vKULQ";
                                                    URLDistanciaInicial1.add(UrlDistActual);
                                                }
                                                //HACE LO MISMO QUE EL TASK3 ANTERIOR
                                                InvocarApiDistancia2 task3 = new InvocarApiDistancia2(new CajasTraidas.AsyncResponse() {
                                                    public void processFinish(Object output) {
                                                        //Tarea3();
                                                    }
                                                });
                                                task3.execute();
                                            }
                                        } catch (Exception exc) {
                                            exc.toString();
                                            // Toast.makeText( getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG ).show();
                                        }

                                    } else {//SI LAS ENTREGAS DEL VIAJE SON IGUALES O MENORES A 0 ENTONCES
                                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.NoHayEntregas), Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                                        toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                                        toast.show();//showing the toast is important**
                                    }
                                }
                            });
                            task2.execute();
                        }
                    }
                } );
                task.execute();
            }
        } else {//SI NO HAY CONEXION A INTERNET ENTONCES
            Toast toast = Toast.makeText( getApplicationContext(), getResources().getString( R.string.ErrorConexion ), Toast.LENGTH_SHORT );
            toast.setGravity( Gravity.CENTER, 0, 0 ); // last two args are X and Y are used for setting position
            toast.setDuration( Toast.LENGTH_LONG );//you can even use milliseconds to display toast
            toast.show();//showing the toast is important**
        }
    }

    //clasee verentrega
    private class  VerEntrega extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public Login.AsyncResponse delegate = null;//Call back interface
        public VerEntrega(Login.AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            if(Utilidad.isNetworkAvailable()) {
                WebService.ListaEntregasViaje(params1,"Viajes/Entregas.php");
                return null;

            }
            else {
                Utilidad.dispalyAlertConexion(contexto);
            }

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

    //clase traer estado
    private class TraerEstado  extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public Login.AsyncResponse delegate = null;//Call back interface
        public TraerEstado(Login.AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            if(Utilidad.isNetworkAvailable()) {
                params1 = new RequestParams(  );
                params1.add( "nro_viaje",WebService.viajeSeleccionado.getNumViaje() );
                WebService.ConsultarEstadoViaje("Viajes/consultarEstado.php",params1);
                return null;

            }
            else {
                Utilidad.dispalyAlertConexion(contexto);
            }

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

    //clase InvocarApiDistancia2
    private class InvocarApiDistancia2 extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public CajasTraidas.AsyncResponse delegate = null;//Call back interface


        public InvocarApiDistancia2(CajasTraidas.AsyncResponse asyncResponse) {
            delegate = asyncResponse;
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

    private class CargarViajes extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface
        public CargarViajes(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            params1 = new RequestParams(  );
            //System.out.println("Nombre: "+WebService.usuarioActual.getNombre());
            params1.put( "username",WebService.usuarioActual.getNombre() );
            WebService.TraerViajes(params1,"Viajes/Viajes.php");
            return null;
        }
        @Override
        public void onPreExecute(){
            Utilidad.showLoadingMessage();
        }
        @Override
        protected void onPostExecute(Void result) {
            Utilidad.deleteLoadingMessage();
            delegate.processFinish( WebService.logueado);
        }
        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    @Override
    public void onBackPressed() {
        try{
        Utilidad.vibraticionBotones( contexto );
        BotonVolver(actualView);
        finish();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private class TraerProductos extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface
        public TraerProductos(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            if(Utilidad.isNetworkAvailable()) {
                RequestParams params1 = new RequestParams();
                /*params1.add("cod_fpago", WebService.sucursalActual.getCod_Sucursal().trim());
                params1.add("cod_tit", WebService.clienteActual.getCod_Tit_Gestion().trim());*/
                params1.add("nro_viaje", WebService.viajeSeleccionado.getNumViaje().trim());
                WebService.TraerProductos(params1, "VentasDirectas/TraerProductos.php");
                return null;

            } else {
                Utilidad.CargarToastConexion(contexto);
            }
            return null;
        }
        @Override
        public void onPreExecute() {
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
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
