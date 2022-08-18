package com.example.user.trucksales.Visual.TruckSales;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.ClienteCobranza;
import com.example.user.trucksales.Encapsuladoras.FacturaXDia;
import com.example.user.trucksales.Encapsuladoras.Viaje;
import com.example.user.trucksales.Negocio.DataParser;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Cobranza.MenuCobranzas;
import com.example.user.trucksales.Visual.Cobranza.SeleccionCliente;
import com.example.user.trucksales.Visual.Cobranza.SeleccionarDeudas;
import com.example.user.trucksales.Visual.FacturaDirecta.FacturaDirecta;
import com.example.user.trucksales.Visual.FacturaDirecta.FacturaProductos;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.Visual.Pedidos.Pedidos;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.RequestParams;
import com.nullwire.trace.ExceptionHandler;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.WatchEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

public class Recorrido_Viaje extends FragmentActivity implements OnMapReadyCallback {

    // private Circle circle;
    protected static Polyline line;
    PolylineOptions lineOptions;
    protected static HashMap hashMapMarker;
    public static Marker marcador_actual;
    public static Marker marcador_final;
    public static GoogleMap mMap;
    public TextView horaComienzo;
    public static Context context2;
    private static ArrayList<LatLng> google_directions_points;
    private static int google_directions_points_index;
    public TextView numViaje;
    public static Integer invoca = 0;
    public TextView numOrden, distanciaTotal, nombreUsu, fecha, client, ActivityTitle;
    public ImageView ticky, atras, BtnPausarViaje1, factura_directa;
    public static String URLDistanciaInicial;
    public TextView datos;
    LocationManager locManager;
    public Button finalizarViaje;
    MarkerOptions mo;
    MarkerOptions options1 = new MarkerOptions();
    public static ArrayList<LatLng> MarkerPoints = new ArrayList<>();
    final static int PERMISSIONS_ALL = 1;
    final static String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    public static LatLng cordenadasDestino;
    protected static RequestParams params = new RequestParams();
    public static Utilidades Utilidad;
    private LatLng ultimaLocalizacionDibujada;
    public static LatLng ubicacionActual;
    private boolean localizacionBuscada;
    private boolean guardadollegaDestino;
    public static LocationManager locationManager;
    private Location location;
    Criteria criteria;
    public static ArrayList<String> URLDistanciaInicial1 = new ArrayList<>();
    private Location localizacion;
    private String Destino;
    private LatLng Inicio;
    private LatLng Fin;
    private boolean rehacerDibujo;
    public static boolean AppPrendida;
    public static boolean cambiar;
    public static Polyline mPolyline;
    private static ImageView pedido;
    public static String valorIntent;

    public static String viaje = "0";

    /*INTERFACE UTILIZADA PARA LAS CLASES ASINCRONAS DENTRO DE LA APLICACION
     ************************************************************************ */
    public interface AsyncResponse {
        void processFinish(Object output);
    }

    /*METODO QUE REALIZA LA CALLA A GOOGLE PARA OBTENER LOS DATOS DE LA RUTA
     ************************************************************************
     * @params String strUrl = la string creada en metodos anteriores para realizar la call
     * */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;

    }

    /*CLASE ASINCRONA QUE REALIZA LA CALL A GOOGLE PARA OBTENER LOS DATOS DE LA RUTA (UTILIZA EL METODO downloadUrl()
     ****************************************************************************************************************** */
    private class FetchUrl extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Destino = result;
            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ACCIONES NECESARIAS PARA LA CLASE
        //***************************************************
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recorrido__viaje);
        context2 = this;
        GuardarDatosUsuario.Contexto = context2;
        Utilidad = new Utilidades(context2);
        mMap = null;
        guardadollegaDestino = false;
        cambiar = false;
        try {
            try {
                valorIntent = getIntent().getStringExtra("intent");
            } catch (Exception ex) {
                ex.printStackTrace();
                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
            }

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            mapFragment.getMapAsync(this);
            //agregado para permisos a partir de mashmellow
            locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            mo = new MarkerOptions().position(new LatLng(0, 0)).title("Va saliendo");
            if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
                requestPermissions(PERMISSIONS, PERMISSIONS_ALL);
            }

            //System.out.println(WebService.EstadoActual);

        /*final String timeStamp2 = new SimpleDateFormat( "dd-MM-yyyy hh:mm:ss" ).format( Calendar.getInstance().getTime() );
        System.out.println("Recorrido Viaje" + timeStamp2);*/

            //***************************************************
            if (WebService.USUARIOLOGEADO != null) {//SI EL USUARIO ESTA LOGUEADO SIGUE SINO LO MANDA AL LOGIN
                hashMapMarker = new HashMap<>();
                atras = findViewById(R.id.btnAtras);
                atras.setClickable(true);
                //METODO ON CLICK DEL BOTON DE NAVEGACION PARA IR PARA ATRAS**
                atras.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        try {
                            Utilidad.vibraticionBotones(context2);
                            if (valorIntent.equals("Cobranza")) {
                                if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getEs_Entrega().equals("N") && (WebService.usuarioActual.getTipoCobrador().equals("D"))) {
                                    Intent myIntent = new Intent(context2, ClienteXDefecto.class);
                                    startActivity(myIntent);
                                } else if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getEs_Entrega().equals("N") && (WebService.usuarioActual.getTipoCobrador().equals("L"))) {
                                    Intent myIntent = new Intent(context2, SeleccionCliente.class);
                                    startActivity(myIntent);
                                }
                            } else {
                                Utilidad.vibraticionBotones(context2);
                                if (WebService.usuarioActual.getEs_VentaDirecta().equals("S")) {
                                    Intent myIntent = new Intent(context2, FacturaDirecta.class);
                                    startActivity(myIntent);
                                } else {
                                    WebService.EstadoAnterior = WebService.EstadoActual;
                                    if (WebService.ViajeActualSeleccionado) {
                                        Intent myIntent = new Intent(v.getContext(), SalirViaje.class);
                                        startActivity(myIntent);
                                    } else {
                                        ViajeSeleccionado();
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            // Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                        }
                    }
                });

                pedido = findViewById( R.id.btnPedidos );
                pedido.setVisibility(View.GONE);
                if(WebService.usuarioActual.getEs_Pedidos().equals("S")){
                    pedido.setVisibility(View.VISIBLE);
                    pedido.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Utilidad.vibraticionBotones(context2);
                                Intent myIntent = new Intent(v.getContext(), Pedidos.class);                                    myIntent.putExtra("intent", "Generados");
                                myIntent.putExtra("intent", "Recorrido_Viaje");
                                startActivity(myIntent);
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    });
                }

                //************************************************************
                ticky = (ImageView) findViewById(R.id.tick);
                ticky.setClickable(true);
                invoca = 0;
                //METODO ON CLICK DEL TICK************************************
                ticky.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        try {
                            Utilidad.vibraticionBotones(context2);
                            if (Utilidad.isNetworkAvailable()) {
                                WebService.EstadoAnterior = WebService.EstadoActual;
                                if (valorIntent.equals("Cobranza")) {
                                    if (WebService.usuarioActual.getEs_Cobranza().equals("S") /*&& WebService.usuarioActual.getEs_Entrega().equals("N")*/ && (WebService.usuarioActual.getTipoCobrador().equals("D"))) {
                                        if (Utilidad.isNetworkAvailable()) {
                                            TraerDeudasViaje task2 = new TraerDeudasViaje(new AsyncResponse() {
                                                @Override
                                                public void processFinish(Object output) {
                                                    if (!WebService.errToken.equals("")) {
                                                        Intent myIntent = new Intent(context2, Login.class);
                                                        startActivity(myIntent);
                                                    } else {
                                                        if (localizacion != null) {
                                                            WebService.distancia_a_recorrer = Utilidad.calcularDistancia(localizacion.getLatitude(), localizacion.getLongitude(), cordenadasDestino.latitude, cordenadasDestino.longitude);
                                                        } else {
                                                            WebService.distancia_a_recorrer = 0;
                                                        }
                                                        Intent myIntent = new Intent(context2, SeleccionarDeudas.class);
                                                        startActivity(myIntent);
                                                    }
                                                }
                                            });
                                            task2.execute();
                                        }
                                    } else if (WebService.usuarioActual.getEs_Cobranza().equals("S") /*&& WebService.usuarioActual.getEs_Entrega().equals("N")*/ && (WebService.usuarioActual.getTipoCobrador().equals("L"))) {
                                        if (Utilidad.isNetworkAvailable()) {
                                            TraerDeudores task5 = new TraerDeudores(new AsyncResponse() {
                                                @Override
                                                public void processFinish(Object output) {
                                                    if (!WebService.errToken.equals("")) {
                                                        Intent myIntent = new Intent(context2, Login.class);
                                                        startActivity(myIntent);
                                                    } else {
                                                        if (localizacion != null) {
                                                            WebService.distancia_a_recorrer = Utilidad.calcularDistancia(localizacion.getLatitude(), localizacion.getLongitude(), cordenadasDestino.latitude, cordenadasDestino.longitude);
                                                        } else {
                                                            WebService.distancia_a_recorrer = 0;
                                                        }
                                                        Intent myIntent = new Intent(context2, SeleccionarDeudas.class);
                                                        startActivity(myIntent);
                                                    }
                                                }
                                            });
                                            task5.execute();
                                        }
                                    }
                                } else if (WebService.usuarioActual.getEs_VentaDirecta().equals("S")) {
                                    Intent myIntent = new Intent(context2, FacturaProductos.class);
                                    startActivity(myIntent);
                                } else {
                                    String prueba = WebService.Entrega_A_Realizar.getNro_Docum().trim();
                                    params = new RequestParams();
                                    params.put("num_Viaje", Integer.valueOf(WebService.viajeSeleccionado.getNumViaje()));
                                    params.put("num_Orden", Integer.valueOf(prueba));
                                    params.put("cod_Sucursal", WebService.Entrega_A_Realizar.getCod_Sucursal());
                                    TraerItems task = new TraerItems(new AsyncResponse() {
                                        @Override
                                        public void processFinish(Object output) {

                                        }
                                    });
                                    task.execute();
                                    try {
                                        task.get();
                                    } catch (Exception exc) {
                                        exc.printStackTrace();
                                        //Toast.makeText( getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG ).show();
                                    }
                                    double earthRadius = 6371000;
                                    if (localizacion != null) {
                                        WebService.distancia_a_recorrer = Utilidad.calcularDistancia(localizacion.getLatitude(), localizacion.getLongitude(), cordenadasDestino.latitude, cordenadasDestino.longitude);
                                    } else {
                                        WebService.distancia_a_recorrer = 0;
                                    }
                                    Intent myIntent2;
                                    if(WebService.entregaDefault.getNro_Docum().equals("0")){
                                        myIntent2 = new Intent(v.getContext(), FacturaDirectaProductoCono.class);
                                        myIntent2.putExtra("intent2", "RecorridoViaje");
                                    }else{
                                        myIntent2 = new Intent(v.getContext(), Productos.class);
                                    }
                                    startActivity(myIntent2);
                                    //startActivity(new Intent(v.getContext(), Productos2.class));
                                }
                            } else {
                                Utilidad.CargarToastConexion(context2);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                        }
                    }

                });

                factura_directa = (ImageView) findViewById(R.id.factura_directa);
                factura_directa.setClickable(true);
                invoca = 0;
                //METODO ON CLICK DEL factura_directa
                factura_directa.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        try {
                            Utilidad.vibraticionBotones(context2);
                            if (Utilidad.isNetworkAvailable()) {
                                Intent myIntent = new Intent(context2, FacturaDirectaCono.class);
                                startActivity(myIntent);
                            } else {
                                Utilidad.CargarToastConexion(context2);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                        }
                    }

                });

                //*************************************************************
                finalizarViaje = (Button) findViewById(R.id.RVButton_FinalizarViaje);
                final String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                nombreUsu = (TextView) findViewById(R.id.LblUsu);
                fecha = (TextView) findViewById(R.id.LblFecha);
                datos = (TextView) findViewById(R.id.TxtValoresDestino);
                distanciaTotal = findViewById(R.id.txtDistanciaTotal);
                horaComienzo = (TextView) findViewById(R.id.txtHoraComienzo);
                numViaje = (TextView) findViewById(R.id.numOrden);
                numOrden = findViewById(R.id.numPedido);
                client = findViewById(R.id.cliente);
                ActivityTitle = findViewById(R.id.recorrido_viaje_title);
                BtnPausarViaje1 = findViewById(R.id.BtnPausarViaje);
                BtnPausarViaje1.getLayoutParams().height = 100;
                BtnPausarViaje1.getLayoutParams().width = 100;
                BtnPausarViaje1.setClickable(true);
                if (WebService.EstadoActual == 2) {
                    ActivityTitle.setText(getString(R.string.PausadoTitle));
                }
                ticky.setEnabled(true);
                ticky.setVisibility(View.VISIBLE);
                finalizarViaje.setEnabled(true);
                finalizarViaje.setVisibility(View.VISIBLE);
                atras.setVisibility(View.VISIBLE);
                atras.setEnabled(true);
                try { //agregado RP 05/05/2019
                    if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D") || WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("L") && /*WebService.usuarioActual.getEs_Entrega().equals("N")*/ valorIntent.equals("Cobranza")) {
                        if ((WebService.EstadoActual == 1 && WebService.ViajeActualSeleccionadoCobrador)) {
                            //SI CUANDO ENTRA EN RECORRIDO VIAJE NO ES RETORNO
                            finalizarViaje.setEnabled(false);
                            finalizarViaje.setVisibility(View.INVISIBLE);
                            distanciaTotal.setText(getResources().getString(R.string.DistanciaRec) + " " + WebService.distancia_a_recorrer);
                            //client.setText(WebService.Entrega_A_Realizar.getNom_Tit() + " - " + WebService.Entrega_A_Realizar.getNom_Sucursal());
                            fecha.setText(timeStamp);
                            nombreUsu.setText(getResources().getString(R.string.usuarioTool) + " " + WebService.USUARIOLOGEADO);
                            horaComienzo.setText(getResources().getString(R.string.HComienz) + " " + WebService.horaComienzoViaje);
                            client.setText(WebService.clienteActual.getNom_Tit());
                            if (WebService.usuarioActual.getTipoCobrador().equals("L")) {
                                numViaje.setVisibility(View.GONE);
                                datos.setText(WebService.clienteActual.getDireccion().replace(",", ",\n"));
                            } else {
                                numViaje.setText(getResources().getString(R.string.NumViajeProd) + " " + WebService.viajeSeleccionadoCobrador.getNumViaje());
                                //client.setText(WebService.Entrega_A_Realizar.getNom_Sucursal());
                                datos.setText(WebService.direccion.replace(",", ",\n"));
                            }
                            numOrden.setVisibility(View.INVISIBLE);
                            BtnPausarViaje1.setEnabled(true);

                            BtnPausarViaje1.setBackgroundResource(R.drawable.pause);
                            BtnPausarViaje1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Utilidad.vibraticionBotones(context2);
                                    PausarEstado(v);
                                }
                            });
                        }
                        if (WebService.EstadoActual == 2) {
                            BtnPausarViaje1.setBackgroundResource(R.drawable.play);
                            ticky.setVisibility(View.INVISIBLE);
                            ticky.setEnabled(false);
                            atras.setVisibility(View.INVISIBLE);
                            atras.setEnabled(false);
                            BtnPausarViaje1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Utilidad.vibraticionBotones(context2);
                                    if (Utilidad.isNetworkAvailable()) {
                                        try {
                                            params = new RequestParams();
                                            params.put("usuario", WebService.USUARIOLOGEADO);//1
                                            params.put("motivo_pausa", WebService.ultimaPausa);//2
                                            params.put("latitud_origen", String.valueOf(WebService.lat_origen));//3
                                            params.put("longitud_origen", WebService.long_origen);//6
                                            params.put("latitud_pausa", String.valueOf(WebService.lat_actual));//7
                                            params.put("longitud_pausa", String.valueOf(WebService.long_actual));//8
                                            params.put("en_pausa", "1");//12
                                            params.put("longitud_actual", String.valueOf(WebService.long_actual));//14
                                            params.put("latitud_actual", String.valueOf(WebService.lat_actual));//15
                                            params.put("latitud_destino", WebService.clienteActual.getLatiud_Ubic());//3
                                            params.put("longitud_destino", WebService.clienteActual.getLongitud_Ubic());//4
                                            params.put("nro_viaje", viaje);//9
                                            params.put("cod_sucursal", WebService.clienteActual.getCod_Tit_Gestion());//10
                                            params.put("nro_orden", "0");//11
                                            params.put("nom_cliente", WebService.clienteActual.getNom_Tit());//12
                                            LevantarPausa task = new LevantarPausa(new AsyncResponse() {
                                                public void processFinish(Object output) {
                                                    distanciaTotal.setText(getResources().getString(R.string.DistanciaRec) + " " + WebService.distancia_a_recorrer);
                                                    if (WebService.usuarioActual.getTipoCobrador().equals("L")) {
                                                        numViaje.setVisibility(View.GONE);
                                                        datos.setText(WebService.clienteActual.getDireccion().replace(",", ",\n"));
                                                        client.setText(WebService.clienteActual.getNom_Tit());
                                                    } else if (WebService.usuarioActual.getTipoCobrador().equals("D")) {
                                                        numViaje.setText(getResources().getString(R.string.NumViajeProd) + " " + WebService.viajeSeleccionadoCobrador.getNumViaje());
                                                        //client.setText(WebService.Entrega_A_Realizar.getNom_Sucursal());
                                                        datos.setText(WebService.direccion.replace(",", ",\n"));
                                                        client.setText(WebService.clienteActual.getNom_Tit());
                                                    } else {
                                                        client.setText(WebService.Entrega_A_Realizar.getNom_Sucursal());
                                                        numViaje.setText(getResources().getString(R.string.NumViajeProd) + " " + WebService.viajeSeleccionado.getNumViaje());
                                                        //client.setText(WebService.Entrega_A_Realizar.getNom_Sucursal());
                                                        datos.setText(WebService.direccion.replace(",", ",\n"));
                                                    }

                                                    fecha.setText(timeStamp);
                                                    //datos.setText(WebService.direccion.replace(",", ",\n"));
                                                    nombreUsu.setText(getResources().getString(R.string.usuarioTool) + " " + WebService.USUARIOLOGEADO);
                                                    horaComienzo.setText(getResources().getString(R.string.HComienz) + " " + WebService.horaComienzoViaje);
                                                    ticky.setVisibility(View.VISIBLE);
                                                    ticky.setEnabled(true);
                                                    atras.setVisibility(View.VISIBLE);
                                                    atras.setEnabled(true);
                                                    ActivityTitle.setText(getResources().getString(R.string.RecorridoTitle));
                                                /*if (WebService.usuarioActual.getTipoCobrador().equals("D")) {
                                                    Intent NextActivity = new Intent(context2, ClienteXDefecto.class);
                                                    startActivity(NextActivity);
                                                } else if (WebService.usuarioActual.getTipoCobrador().equals("L")){
                                                    Intent NextActivity = new Intent(context2, SeleccionCliente.class);
                                                    startActivity(NextActivity);
                                                }*/
                                                    BtnPausarViaje1.setBackgroundResource(R.drawable.pause);
                                                    BtnPausarViaje1.setOnClickListener(new View.OnClickListener() {
                                                        public void onClick(View v) {
                                                            Utilidad.vibraticionBotones(context2);
                                                            PausarEstado(v);
                                                        }
                                                    });
                                                }
                                            });
                                            task.execute();
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                            // Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                                        }
                                    } else {
                                        Utilidad.CargarToastConexion(context2);
                                    }
                                }
                            });
                        } else if (WebService.EstadoActual == 3) {
                            //SI CUANDO ENTRA EN RECORRIDO VIAJE ES RETORNO
                            atras.setVisibility(View.INVISIBLE);
                            atras.setEnabled(false);
                            fecha.setText(timeStamp);
                            nombreUsu.setText(getResources().getString(R.string.usuarioTool) + " " + WebService.USUARIOLOGEADO);
                            horaComienzo.setText(getResources().getString(R.string.HComienz) + " " + WebService.horaComienzoViaje);
                            numViaje.setText(getString(R.string.RetornoMsg));
                            ActivityTitle.setText(getString(R.string.RetornoTitle));
                            ticky.setEnabled(false);
                            ticky.setVisibility(View.INVISIBLE);
                        }
                    } else if (WebService.usuarioActual.getEs_VentaDirecta().equals("S")) {
                        try {
                            if ((WebService.EstadoActual == 1 || (WebService.EstadoActual == 2 && WebService.EstadoAnterior != 3)) && WebService.ViajeActualSeleccionado) {
                                distanciaTotal.setText(getResources().getString(R.string.DistanciaRec) + " " + WebService.distancia_a_recorrer);
                                //client.setText(WebService.Entrega_A_Realizar.getNom_Tit() + " - " + WebService.Entrega_A_Realizar.getNom_Sucursal());

                                BtnPausarViaje1.setEnabled(true);
                                BtnPausarViaje1.setBackgroundResource(R.drawable.pause);
                                BtnPausarViaje1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Utilidad.vibraticionBotones(context2);
                                        PausarEstado(v);
                                    }
                                });

                                client.setText(WebService.sucursalActual.getNom_Tit());
                                fecha.setText(timeStamp);
                                datos.setText(WebService.sucursalActual.getDireccion().trim());
                                nombreUsu.setText(getResources().getString(R.string.usuarioTool) + " " + WebService.USUARIOLOGEADO);
                                horaComienzo.setText(getResources().getString(R.string.HComienz) + " " + WebService.horaComienzoViaje);
                                numViaje.setText(getResources().getString(R.string.NumViajeProd) + " " + WebService.viajeSeleccionado.getNumViaje());

                                numOrden.setVisibility(View.INVISIBLE);

                                finalizarViaje.setEnabled(false);
                                finalizarViaje.setVisibility(View.INVISIBLE);
                            } else if (WebService.EstadoActual == 3) {
                                //SI CUANDO ENTRA EN RECORRIDO VIAJE ES RETORNO
                                if (WebService.entregasTraidas.size() == 0) {
                                    atras.setVisibility(View.INVISIBLE);
                                    atras.setEnabled(false);
                                }
                                fecha.setText(timeStamp);
                                nombreUsu.setText(getResources().getString(R.string.usuarioTool) + " " + WebService.USUARIOLOGEADO);
                                horaComienzo.setText(getResources().getString(R.string.HComienz) + " " + WebService.horaComienzoViaje);
                                numViaje.setText(getString(R.string.RetornoMsg));
                                ActivityTitle.setText(getString(R.string.RetornoTitle));
                                ticky.setEnabled(false);
                                ticky.setVisibility(View.INVISIBLE);
                            } else if (WebService.viajeSeleccionado.getEstado().equals("2")) {
                                BtnPausarViaje1.setBackgroundResource(R.drawable.play);
                                ticky.setVisibility(View.INVISIBLE);
                                ticky.setEnabled(false);
                                atras.setVisibility(View.INVISIBLE);
                                atras.setEnabled(false);
                                BtnPausarViaje1.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v)//Aca reanudamos el viaje en caso de pausa
                                    {
                                        try {
                                            Utilidad.vibraticionBotones(context2);
                                            if (Utilidad.isNetworkAvailable()) {
                                                params.put("usuario", WebService.USUARIOLOGEADO);//1
                                                params.put("motivo_pausa", "");//2
                                                params.put("latitud_destino", WebService.sucursalActual.getLatiud_Ubic());//3
                                                params.put("longitud_destino", WebService.sucursalActual.getLongitud_Ubic());//4
                                                params.put("nro_viaje", WebService.viajeSeleccionado.getNumViaje());//9
                                                params.put("cod_sucursal", WebService.clienteActual.getCod_Tit_Gestion());//10
                                                params.put("nro_orden", "0");//13
                                                params.put("nom_cliente", WebService.clienteActual.getNom_Tit());//11
                                                params.put("latiud_origen", String.valueOf(WebService.lat_origen));//5
                                                params.put("longitud_origen", WebService.long_origen);//6
                                                params.put("latitud_pausa", String.valueOf(WebService.lat_actual));//7
                                                params.put("longitud_pausa", String.valueOf(WebService.long_actual));//8
                                                WebService.viajeSeleccionado.setEstado(String.valueOf(WebService.EstadoAnterior));
                                                params.put("en_pausa", WebService.viajeSeleccionado.getEstado());//12
                                                params.put("longitud_actual", String.valueOf(WebService.long_actual));//14
                                                params.put("latitud_actual", String.valueOf(WebService.lat_actual));//15
                                                params.put("latitud_reinicio", String.valueOf(WebService.lat_actual));//16
                                                params.put("longitud_reinicio", String.valueOf(WebService.long_actual));//17
                                                LevantarPausa task = new LevantarPausa(new AsyncResponse() {
                                                    public void processFinish(Object output) {
                                                        ticky.setVisibility(View.VISIBLE);
                                                        ticky.setEnabled(true);
                                                        atras.setVisibility(View.VISIBLE);
                                                        atras.setEnabled(true);
                                                        ActivityTitle.setText(getResources().getString(R.string.RecorridoTitle));
                                                        if (!WebService.ViajeActualSeleccionado) {
                                                            Intent NextActivity = new Intent(context2, FacturaDirecta.class);
                                                            startActivity(NextActivity);
                                                        }
                                                        BtnPausarViaje1.setBackgroundResource(R.drawable.pause);
                                                        BtnPausarViaje1.setOnClickListener(new View.OnClickListener() {
                                                            public void onClick(View v) {
                                                                Utilidad.vibraticionBotones(context2);
                                                                PausarEstado(v);
                                                            }
                                                        });
                                                    }
                                                });
                                                task.execute();
                                            } else {
                                                Utilidad.CargarToastConexion(context2);
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }

                                });
                            } else {
                                BtnPausarViaje1.setBackgroundResource(R.drawable.pause);
                                BtnPausarViaje1.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        Utilidad.vibraticionBotones(context2);
                                        PausarEstado(v);
                                    }
                                });
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            //  Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                        }
                    } else {
                        if ((WebService.EstadoActual == 1 || (WebService.EstadoActual == 2 && WebService.EstadoAnterior != 3)) && WebService.ViajeActualSeleccionado) {
                            //SI CUANDO ENTRA EN RECORRIDO VIAJE NO ES RETORNO
                            distanciaTotal.setText(getResources().getString(R.string.DistanciaRec) + " " + WebService.distancia_a_recorrer);
                            //client.setText(WebService.Entrega_A_Realizar.getNom_Tit() + " - " + WebService.Entrega_A_Realizar.getNom_Sucursal());
                            client.setText(WebService.Entrega_A_Realizar.getNom_Sucursal());
                            fecha.setText(timeStamp);
                            datos.setText(WebService.direccion.replace(",", ",\n"));
                            nombreUsu.setText(getResources().getString(R.string.usuarioTool) + " " + WebService.USUARIOLOGEADO);
                            horaComienzo.setText(getResources().getString(R.string.HComienz) + " " + WebService.horaComienzoViaje);
                            numViaje.setText(getResources().getString(R.string.NumViajeProd) + " " + WebService.viajeSeleccionado.getNumViaje());
                            if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getEs_Entrega().equals("N") && (WebService.usuarioActual.getTipoCobrador().equals("D"))) {
                                numOrden.setVisibility(View.INVISIBLE);
                            } else {
                                numOrden.setText(getResources().getString(R.string.OredenProd) + " " + WebService.Entrega_A_Realizar.getNro_Docum());
                            }
                            finalizarViaje.setEnabled(false);
                            finalizarViaje.setVisibility(View.INVISIBLE);
                        } else if (WebService.EstadoActual == 3) {
                            //SI CUANDO ENTRA EN RECORRIDO VIAJE ES RETORNO
                            if (WebService.entregasTraidas.size() == 0) {
                                atras.setVisibility(View.INVISIBLE);
                                atras.setEnabled(false);
                            }
                            fecha.setText(timeStamp);
                            nombreUsu.setText(getResources().getString(R.string.usuarioTool) + " " + WebService.USUARIOLOGEADO);
                            horaComienzo.setText(getResources().getString(R.string.HComienz) + " " + WebService.horaComienzoViaje);
                            numViaje.setText(getString(R.string.RetornoMsg));
                            ActivityTitle.setText(getString(R.string.RetornoTitle));
                            ticky.setEnabled(false);
                            ticky.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (WebService.usuarioActual.getEs_Entrega().equals("S") & valorIntent.equals("TruckSales")) {
                        if (WebService.viajeSeleccionado.getEstado().equals("2")) {//si el estado es 2 entonces esta en un paro
                            BtnPausarViaje1.setBackgroundResource(R.drawable.play);
                            ticky.setVisibility(View.INVISIBLE);
                            ticky.setEnabled(false);
                            atras.setVisibility(View.INVISIBLE);
                            atras.setEnabled(false);
                            BtnPausarViaje1.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v)//Aca reanudamos el viaje en caso de pausa
                                {
                                    try {
                                        Utilidad.vibraticionBotones(context2);
                                        if (Utilidad.isNetworkAvailable()) {
                                            params.put("usuario", WebService.USUARIOLOGEADO);//1
                                            params.put("motivo_pausa", "");//2
                                            params.put("latitud_destino", WebService.entregaDefault.getLatiud_Ubic());//3
                                            params.put("longitud_destino", WebService.entregaDefault.getLongitud_Ubic());//4
                                            params.put("latiud_origen", String.valueOf(WebService.lat_origen));//5
                                            params.put("longitud_origen", WebService.long_origen);//6
                                            params.put("latitud_pausa", String.valueOf(WebService.lat_actual));//7
                                            params.put("longitud_pausa", String.valueOf(WebService.long_actual));//8
                                            params.put("nro_viaje", WebService.viajeSeleccionado.getNumViaje());//9
                                            params.put("cod_sucursal", WebService.cod_sucu);//10
                                            params.put("nom_cliente", WebService.clienteDestino.trim());//11
                                            WebService.viajeSeleccionado.setEstado(String.valueOf(WebService.EstadoAnterior));
                                            params.put("en_pausa", WebService.viajeSeleccionado.getEstado());//12
                                            params.put("nro_orden", WebService.nro_orden);//13
                                            params.put("longitud_actual", String.valueOf(WebService.long_actual));//14
                                            params.put("latitud_actual", String.valueOf(WebService.lat_actual));//15
                                            params.put("latitud_reinicio", String.valueOf(WebService.lat_actual));//16
                                            params.put("longitud_reinicio", String.valueOf(WebService.long_actual));//17
                                            LevantarPausa task = new LevantarPausa(new AsyncResponse() {
                                                public void processFinish(Object output) {
                                                    ticky.setVisibility(View.VISIBLE);
                                                    ticky.setEnabled(true);
                                                    atras.setVisibility(View.VISIBLE);
                                                    atras.setEnabled(true);
                                                    ActivityTitle.setText(getResources().getString(R.string.RecorridoTitle));
                                                    if (!WebService.ViajeActualSeleccionado || WebService.Entrega_A_Realizar == null) {
                                                        Intent NextActivity = new Intent(context2, ClienteXDefecto.class);
                                                        startActivity(NextActivity);
                                                    }
                                                    BtnPausarViaje1.setBackgroundResource(R.drawable.pause);
                                                    BtnPausarViaje1.setOnClickListener(new View.OnClickListener() {
                                                        public void onClick(View v) {
                                                            Utilidad.vibraticionBotones(context2);
                                                            PausarEstado(v);
                                                        }
                                                    });
                                                }
                                            });
                                            task.execute();
                                        } else {
                                            Utilidad.CargarToastConexion(context2);
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }

                            });
                        } else {
                            BtnPausarViaje1.setBackgroundResource(R.drawable.pause);
                            BtnPausarViaje1.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Utilidad.vibraticionBotones(context2);
                                    PausarEstado(v);
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //  Toast.makeText( getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG ).show();
                }
                finalizarViaje.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Utilidad.vibraticionBotones(context2);
                            //Agregado 11/7 BDL
                            if (WebService.Entrega_A_Realizar.getNro_Docum() != null) {
                                //Verificamos que la entrega este vacia. Esto pasa cuando viene de cajas y no han seleccionado una entrega.
                                params = new RequestParams();
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
                                params.put("en_pausa", "0");
                                params.put("nro_orden", Integer.valueOf(WebService.nro_orden));
                                params.put("distancia_a_recorrer", WebService.distancia_a_recorrer);
                                params.put("motivo_pausa", WebService.ultimaPausa);//2
                                ActualizarUbicacion actuUbic = new ActualizarUbicacion(new AsyncResponse() {
                                    public void processFinish(Object output) {
                                        WebService.logueado = false;
                                        WebService.ViajeActualSeleccionado = false;
                                        WebService.EstadoActual = 0;
                                        WebService.LastLocationSaved = new LatLng(WebService.lat_actual, WebService.long_actual);
                                        startActivity(new Intent(context2, Login.class));
                                    }
                                });
                                actuUbic.execute();
                            } else {
                                if (WebService.usuarioActual.getEs_Cobranza().equals("S") && valorIntent.equals("Cobranza")/*WebService.usuarioActual.getEs_Entrega().equals( "N" )*/ && (WebService.usuarioActual.getTipoCobrador().equals("D")) || (WebService.usuarioActual.getTipoCobrador().equals("L")) || (WebService.usuarioActual.getTipoCobrador().equals("F"))) {
                                    String nro_viaje = "0";

                                    if (WebService.usuarioActual.getTipoCobrador().equals("D")) {
                                        nro_viaje = WebService.viajeSeleccionadoCobrador.getNumViaje().trim();
                                    }
                                    params.put("usuario", WebService.USUARIOLOGEADO);//1
                                    params.put("latitud_actual", String.valueOf(WebService.lat_actual));
                                    params.put("longitud_actual", String.valueOf(WebService.long_actual));
                                    params.put("latitud_destino", String.valueOf(WebService.clienteActual.getLatiud_Ubic()));
                                    params.put("longitud_destino", String.valueOf(WebService.clienteActual.getLongitud_Ubic()));
                                    params.put("latitud_origen", String.valueOf(WebService.lat_origen));
                                    params.put("longitud_origen", String.valueOf(WebService.long_origen));
                                    params.put("en_pausa", "0");//12
                                    params.put("nro_viaje", nro_viaje);//9
                                    params.put("cod_sucursal", "0");//10
                                    params.put("nro_orden", "0");//13
                                    params.put("nom_cliente", WebService.clienteActual.getNom_Tit().trim());//11
                                    params.put("distancia_a_recorrer", WebService.distancia_a_recorrer);
                                    ActualizarUbicacion taskact = new ActualizarUbicacion(new AsyncResponse() {
                                        @Override
                                        public void processFinish(Object output) {
                                            WebService.logueado = false;
                                            WebService.ViajeActualSeleccionadoCobrador = false;
                                            WebService.EstadoActual = 0;
                                            startActivity(new Intent(context2, Login.class));
                                        }
                                    });
                                    taskact.execute();
                                } else if (WebService.usuarioActual.getEs_VentaDirecta().equals("S")) {
                                    String nro_viaje = WebService.viajeSeleccionado.getNumViaje().trim();

                                    params.put("usuario", WebService.USUARIOLOGEADO);//1
                                    params.put("latitud_actual", String.valueOf(WebService.lat_actual));
                                    params.put("longitud_actual", String.valueOf(WebService.long_actual));
                                    params.put("latitud_destino", String.valueOf(WebService.clienteActual.getLatiud_Ubic()));
                                    params.put("longitud_destino", String.valueOf(WebService.clienteActual.getLongitud_Ubic()));
                                    params.put("latitud_origen", String.valueOf(WebService.lat_origen));
                                    params.put("longitud_origen", String.valueOf(WebService.long_origen));
                                    params.put("en_pausa", "0");//12
                                    params.put("nro_viaje", nro_viaje);//9
                                    params.put("cod_sucursal", "0");//10
                                    params.put("nro_orden", "0");//13
                                    params.put("nom_cliente", WebService.clienteActual.getNom_Tit().trim());//11
                                    params.put("distancia_a_recorrer", WebService.distancia_a_recorrer);
                                    ActualizarUbicacion taskact = new ActualizarUbicacion(new AsyncResponse() {
                                        @Override
                                        public void processFinish(Object output) {
                                            WebService.logueado = false;
                                            WebService.ViajeActualSeleccionadoCobrador = false;
                                            WebService.EstadoActual = 0;
                                            startActivity(new Intent(context2, Login.class));
                                        }
                                    });
                                    taskact.execute();
                                } else {
                                    startActivity(new Intent(context2, Viajes.class));
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            // Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                        }
                    }
                });

            } else {
                startActivity(new Intent(context2, Login.class));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    protected void onStop() {

        /*final String timeStamp2 = new SimpleDateFormat( "dd-MM-yyyy hh:mm:ss" ).format( Calendar.getInstance().getTime() );
        System.out.println("Recorrido Viaje + OnStop" + timeStamp2);*/

        AppPrendida = false;
        super.onStop();
    }

    @Override
    protected void onResume() {

        /*final String timeStamp2 = new SimpleDateFormat( "dd-MM-yyyy hh:mm:ss" ).format( Calendar.getInstance().getTime() );
        System.out.println("Recorrido Viaje + OnResume" + timeStamp2);*/

        AppPrendida = true;
        super.onResume();
    }

    private class TraerDeudores extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog(context2);
        public AsyncResponse delegate = null;//Call back interface

        public TraerDeudores(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {
            if (Utilidad.isNetworkAvailable()) {
                RequestParams params1 = new RequestParams();
                params1.put("cod_tit", WebService.clienteActual.getCod_Tit_Gestion().trim());
                params1.put("cod_empresa", WebService.usuarioActual.getEmpresa().trim());
                params1.put("username", WebService.USUARIOLOGEADO.trim());
                WebService.TraerDeudas(params1);
                return null;
            }
            return null;
        }

        @Override
        public void onPreExecute() {
            Utilidad.showLoadingMessage();
        }

        @Override
        protected void onPostExecute(Void result) {
            delegate.processFinish(WebService.logueado);
            Utilidad.deleteLoadingMessage();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    /*METODO PARA EJECUTAR LAS COSAS EN CASO DE QUE TENGA SELECCIONADO UN VIAJE
     ***************************************************************************/
    private void ViajeSeleccionado() {
        try {
            if (Utilidad.isNetworkAvailable()) { //SI HAY INTERNET SIGUE
                WebService.ViajeActualSeleccionado = false;
                WebService.EntregasAnteriores.clear();
                //SI LA APP NO TIENE LOS PERMISOS DE GEOLOCALIZACION LOS PIDE
                if (ActivityCompat.checkSelfPermission(context2, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context2, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context2, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    return;
                } else {
                    WebService.ViajeActualSeleccionado = true;
                    if (WebService.viajeSeleccionado.getTipo().equals("entrega")) {
                        if (WebService.entregasTraidas.size() > 0) {
                            //SI LAS ENTREGAS POSIBLES SON MAYORES A 0 (SI EXISTEN ENTREGAS) SIGUE EL CODIGO NORMAL
                            if (WebService.viajeSeleccionado.getEstado() == null) {
                                WebService.viajeSeleccionado.setEstado("1");
                            }
                            WebService.entregaDefault = WebService.entregasTraidas.get(0);
                            try {
                                double latitude;
                                double longitude;
                                if (location == null) {//SI LA GEOLOCALIZACION ES NULL ENTONCES
                                    if (localizacionBuscada) {//SI LA LOCALIZACION BUSCADA ES TRUE
                                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.ToastText), Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                                        toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                                        toast.show();//showing the toast is important**
                                    }
                                    Intent myIntent = new Intent(context2, TransMap.class);
                                    startActivity(myIntent);
                                    //String provaider = locManager.getBestProvider( criteria, true );
                                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);//PIDE EL ADMINISTRADOR DE LA GEOLOCALIZACION
                                    criteria = new Criteria();//BUSCADOR MULTIPARAMETROS
                                    //GEO:LOC | BUSQUEDA Y POSICIONAMIENTO DE LA UBICACION ACTUAL
                                    if (ActivityCompat.checkSelfPermission(context2, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context2, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        return;
                                    }
                                    location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));//BUSCA LA LOCALIZACION MAS CERCANA
                                    latitude = 0;
                                    longitude = 0;
                                    if (location == null) {//SI LA LOCALIZACION ES NULA ENTONCES
                                        latitude = 0;
                                        longitude = 0;
                                    } else {//SI EXISTE UN USUARIO MAS CERCANO ENTONCES LO GUARDA EN VARIABLES
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                    }
                                    localizacionBuscada = true;
                                } else {//SI YA TIENE UNA LOCALIZACION ENTONCES
                                    //GEO:LOC | BUSQUEDA Y POSICIONAMIENTO DE LA UBICACION ACTUAL
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                                WebService.lat_origen = latitude;
                                WebService.long_origen = longitude;
                                ubicacionActual = new LatLng(latitude, longitude);//GUARDA LA UBICACION ACTUAL
                                //GEO:LOC | FIN DE BUSQUEDA Y POSICIONAMIENTO DE LA UBICACION ACTUAL
                                for (int i = 0; i < WebService.entregasTraidas.size(); i++) {
                                    LatLng dest = new LatLng(WebService.entregasTraidas.get(i).getLatiud_Ubic(), WebService.entregasTraidas.get(i).getLongitud_Ubic());
                                    String UrlDistActual = getUrl(ubicacionActual, dest);
                                    URLDistanciaInicial1.add(UrlDistActual);
                                }
                                EntregaMasCercana task3 = new EntregaMasCercana(new AsyncResponse() {//CREA OTRA TAREA EN PARALELO
                                    public void processFinish(Object output) {  //Tarea3();
                                    }
                                });
                                task3.execute();
                            } catch (Exception exc) {
                                exc.printStackTrace();
                                // Toast.makeText( getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG ).show();
                            }
                            if (WebService.entregasTraidas.size() != 0) {
                                startActivity(new Intent(context2, ClienteXDefecto.class));
                            }
                        } else {//SI LAS ENTREGAS DEL VIAJE SON IGUALES O MENORES A 0 ENTONCES
                        }
                    } else {
                        if (WebService.ListaRetiros.size() != 0) {
                            WebService.RetiroSeleccionado = WebService.ListaRetiros.get(0);
                            startActivity(new Intent(context2, ClienteXDefecto.class));
                        }
                    }
                }
            } else {//SI NO HAY CONEXION A INTERNET ENTONCES
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.ErrorConexion), Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*CLASE PARA OBTENER LA ENTREGA MAS CERCANA
     *******************************************
     * @params URLDistanciaInicial1 = url de la distancia a verificar
     * */
    private class EntregaMasCercana extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog(context2);
        public AsyncResponse delegate = null;//Call back interface

        public EntregaMasCercana(AsyncResponse asyncResponse) {
            delegate = asyncResponse;
        }

        @Override
        protected Void doInBackground(String... params) {
            if (Utilidad.isNetworkAvailable()) {
                WebService.TraerEntregaMasCercana(URLDistanciaInicial1);
                return null;
            } else {
                Utilidad.dispalyAlertConexion(context2);
            }
            return null;
        }

        @Override
        public void onPreExecute() {
            Utilidad.showLoadingMessage();
        }

        @Override
        protected void onPostExecute(Void result) {
            delegate.processFinish(WebService.logueado);
            Utilidad.deleteLoadingMessage();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    /*CLASE PARA ACTUALIZAR LA UBICACION DENTRO DEL SERVIDOR,
     * @params RequestParams params = parametros para actualizar la tabla (ver en el codigo onCreate)
     * */
    private class ActualizarUbicacion extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog(context2);
        public AsyncResponse delegate = null;//Call back interface

        public ActualizarUbicacion(AsyncResponse asyncResponse) {
            delegate = asyncResponse;
        }

        @Override
        protected Void doInBackground(String... strings) {
            WebService.ActualizarUbicacion(params, "Viajes/ActualizarUbicacion.php");
            return null;
        }

        @Override
        public void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void result) {
            delegate.processFinish(WebService.logueado);
            Utilidad.deleteLoadingMessage();
        }
    }

    /*METODO QUE TRASLADA EL ACTIVITY HASTA PAUSAR VIAJE
     ****************************************************
     * @params View v = el context del activity actual
     * */
    private void PausarEstado(View v) {
        if (Utilidad.isNetworkAvailable()) {
            startActivity(new Intent(v.getContext(), PausarViaje.class));
        } else {
            Utilidad.CargarToastConexion(context2);
        }
    }

    private class TraerDeudasViaje extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog(context2);
        public AsyncResponse delegate = null;//Call back interface

        public TraerDeudasViaje(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {
            /*RequestParams params4 = new RequestParams(  );
            params4.put("cod_tit", WebService.clienteActual.getCod_Tit_Gestion());
            params4.put("num_viaje", WebService.viajeSeleccionadoCobrador.getNumViaje());
            params4.put("cod_emp",WebService.clienteActual.getCodEmp().trim());*/
            WebService.TraerDeudasViaje(/*params4*/);
            return null;
        }

        @Override
        public void onPreExecute() {
            dialog1.setMessage(getResources().getString(R.string.cargando_dialog));
            dialog1.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog1.isShowing()) {
                dialog1.dismiss();
            }
            delegate.processFinish(WebService.logueado);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    /***************************************************************************************************************************
     * METODOS ESPECIFICOS DE MAPAS Y GEOLOCALIZACION
     */

    /*METODO PARA REDIMENSIONAR LOS ICONOS DENTRO DEL MAPA
     ******************************************************
     * @params String iconName = nombre del icono
     * @params int width = el ancho del icono
     * @params int height = la altura del icono
     * */
    protected Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(context2.getResources(), context2.getResources().getIdentifier(iconName, "drawable", context2.getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }


    /*METODO QUE SE EJECUTA CUNADO EL MAPA ESTA LISTO
     *************************************************
     * @params GoogleMap googleMap = mapa actual creado dentro de la pagina
     * */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {

           /* final String timeStamp2 = new SimpleDateFormat( "dd-MM-yyyy hh:mm:ss" ).format( Calendar.getInstance().getTime() );
            System.out.println("Recorrido Viaje + onMapReady" + timeStamp2);*/

            if (Utilidad.isNetworkAvailable()) {
                if (mMap == null) {
                    mMap = googleMap;
                    mMap.clear();
                    double latitudDestino = 0.0;
                    double longitudDestino = 0.0;
                    if (WebService.viajeSeleccionado != null) {
                        if (WebService.EstadoActual == 3 || (WebService.EstadoActual == 2 && WebService.EstadoAnterior == 3)) {
                            latitudDestino = WebService.viajeSeleccionado.getLocalizacionCentral().latitude;
                            longitudDestino = WebService.viajeSeleccionado.getLocalizacionCentral().longitude;
                        } else {
                            if (valorIntent.equals("VentaDirecta")) {
                                latitudDestino = WebService.sucursalActual.getLatiud_Ubic();
                                longitudDestino = WebService.sucursalActual.getLongitud_Ubic();
                            } else {
                                latitudDestino = WebService.Entrega_A_Realizar.getLatiud_Ubic();
                                longitudDestino = WebService.Entrega_A_Realizar.getLongitud_Ubic();
                            }
                        }
                    } else {
                        latitudDestino = WebService.clienteActual.getLatiud_Ubic();
                        longitudDestino = WebService.clienteActual.getLongitud_Ubic();
                    }

                    // Add a marker in Sydney and move the camera
                    cordenadasDestino = new LatLng(latitudDestino, longitudDestino);
                    //String provaider = locManager.getBestProvider( criteria, true );
                    MarkerOptions marker = new MarkerOptions().position(cordenadasDestino).title("Destino");
                    mMap.addMarker(marker);
                    hashMapMarker.put("destino", marker);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(cordenadasDestino));

                    //LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    Criteria criteria = new Criteria();
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

                    MarkerOptions a = new MarkerOptions()
                            .position(new LatLng(50, 6));
                    Marker m = mMap.addMarker(a);
                    m.setPosition(new LatLng(50, 5));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                    marcador_actual = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
                    hashMapMarker.put("actual", marcador_actual);
                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Origen").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    //  obtenerDistanciaTotal(ubicacionArranque,cordenadasDestino);
                    //Aca iniciamos el viaje
                    if (invoca == 0 && WebService.EstadoActual == 1) {
                        params = new RequestParams();
                        IniciarViaje instanciaViaje = new IniciarViaje();
                        if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D") || (WebService.usuarioActual.getTipoCobrador().equals("L")) && valorIntent.equals("Cobranza") /*WebService.usuarioActual.getEs_Entrega().equals("N")*/) {
                            params.put("usuario", WebService.USUARIOLOGEADO);//1
                            params.put("latitud_destino", String.valueOf(latitudDestino));
                            params.put("longitud_destino", String.valueOf(longitudDestino));
                            params.put("latitud_origen", String.valueOf(latitude));
                            params.put("longitud_origen", String.valueOf(longitude));
                            params.put("en_pausa", "1" /*WebService.viajeSeleccionadoCobrador.getEstado()*/);//12
                            params.put("nro_viaje", viaje);//9
                            params.put("cod_sucursal", WebService.clienteActual.getCod_Tit_Gestion());//10
                            params.put("nro_orden", "0");//13
                            params.put("nom_cliente", WebService.clienteActual.getNom_Tit());//11
                        } else if (WebService.usuarioActual.getEs_VentaDirecta().equals("S")){
                            params.put("usuario", WebService.USUARIOLOGEADO);
                            params.put("latitud_destino", String.valueOf(latitudDestino));
                            params.put("longitud_destino", String.valueOf(longitudDestino));
                            params.put("latitud_origen", String.valueOf(latitude));
                            params.put("longitud_origen", String.valueOf(longitude));
                            params.put("nro_viaje", Integer.valueOf(WebService.viajeSeleccionado.getNumViaje()));
                            params.put("cod_sucursal", WebService.sucursalActual.getCod_Sucursal());
                            //cambiar el nom cliente
                            params.put("nom_cliente", WebService.clienteActual.getNom_Tit());
                            params.put("en_pausa", "3");
                            //cambiar el nro ortden
                            params.put("nro_orden", "0");
                        }else{
                            params.put("usuario", WebService.USUARIOLOGEADO);
                            params.put("latitud_destino", String.valueOf(latitudDestino));
                            params.put("longitud_destino", String.valueOf(longitudDestino));
                            params.put("latitud_origen", String.valueOf(latitude));
                            params.put("longitud_origen", String.valueOf(longitude));
                            params.put("nro_viaje", Integer.valueOf(WebService.viajeSeleccionado.getNumViaje()));
                            params.put("cod_sucursal", WebService.cod_sucu);
                            //cambiar el nom cliente
                            params.put("nom_cliente", WebService.viajeSeleccionado.getEstado().equals("3") ? "Casa central" : WebService.nombreLocal.trim());
                            params.put("en_pausa", WebService.viajeSeleccionado.getEstado());
                            //cambiar el nro ortden
                            params.put("nro_orden", Integer.valueOf(WebService.viajeSeleccionado.getEstado().equals("3") ? "0" : WebService.nro_orden));
                        }
                        WebService.LastLocationSaved = new LatLng(latitude, longitude);
                        //fin de agregado
                        invoca = 1;
                        instanciaViaje.execute();
                        try {
                            instanciaViaje.get();
                        } catch (Exception exc) {
                            exc.toString();
                        }
                    }
                    Location LA = new Location(locationManager.getBestProvider(criteria, false));
                    //Location LA = new Location(gpsLocation.getProvider());
                    LA.setLongitude(longitude);
                    LA.setLatitude(latitude);
                    ultimaLocalizacionDibujada = new LatLng(latitude, longitude);
                    DrawRoute(LA);

                } else {
                    System.out.println("mMap");
                }
            }else {
                Utilidad.CargarToastConexion(context2);
            }
        } catch (Exception e) {
            e.printStackTrace();
          //  Toast.makeText( getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }

    public interface LatLngInterpolator {
        LatLng interpolate(float fraction, LatLng a, LatLng b);
        class Spherical implements LatLngInterpolator {
            /* From github.com/googlemaps/android-maps-utils */
            @Override
            public LatLng interpolate(float fraction, LatLng from, LatLng to) {
                // http://en.wikipedia.org/wiki/Slerp
                double fromLat = toRadians(from.latitude);
                double fromLng = toRadians(from.longitude);
                double toLat = toRadians(to.latitude);
                double toLng = toRadians(to.longitude);
                double cosFromLat = cos(fromLat);
                double cosToLat = cos(toLat);
                // Computes Spherical interpolation coefficients.
                double angle = computeAngleBetween(fromLat, fromLng, toLat, toLng);
                double sinAngle = sin(angle);
                if (sinAngle < 1E-6) {
                    return from;
                }
                double a = sin((1 - fraction) * angle) / sinAngle;
                double b = sin(fraction * angle) / sinAngle;
                // Converts from polar to vector and interpolate.
                double x = a * cosFromLat * cos(fromLng) + b * cosToLat * cos(toLng);
                double y = a * cosFromLat * sin(fromLng) + b * cosToLat * sin(toLng);
                double z = a * sin(fromLat) + b * sin(toLat);
                // Converts interpolated vector back to polar.
                double lat = atan2(z, sqrt(x * x + y * y));
                double lng = atan2(y, x);
                return new LatLng(toDegrees(lat), toDegrees(lng));
            }
            private double computeAngleBetween(double fromLat, double fromLng, double toLat, double toLng) {
                // Haversine's formula
                double dLat = fromLat - toLat;
                double dLng = fromLng - toLng;
                return 2 * asin(sqrt(pow(sin(dLat / 2), 2) +
                        cos(fromLat) * cos(toLat) * pow(sin(dLng / 2), 2)));
            }
        }
    }

    public static void animateMarkerToGB(final Marker marker, final LatLng finalPosition, final LatLngInterpolator latLngInterpolator) {
        final LatLng startPosition = marker.getPosition();
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 2000;
        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;
            @Override
            public void run() {
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);
                marker.setPosition(latLngInterpolator.interpolate(v, startPosition, finalPosition));
                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    public void verificaMapa(){
        try {
            System.out.println("verificaMapa");

            /*final String timeStamp2 = new SimpleDateFormat( "dd-MM-yyyy hh:mm:ss" ).format( Calendar.getInstance().getTime() );
            System.out.println("Recorrido Viaje + verificaMapa" + timeStamp2);*/

            double distancia_destino = Utilidad.calcularDistancia(WebService.lat_actual, WebService.long_actual, cordenadasDestino.latitude, cordenadasDestino.longitude);
            WebService.distancia_a_recorrer = distancia_destino;
            if (AppPrendida) {
                if(google_directions_points_index == 0 ){
                    verificar_distancia_punto(new LatLng(WebService.lat_actual,WebService.long_actual));
                }
                verificar_continua_por_camino(new LatLng(WebService.lat_actual, WebService.long_actual));
            }
            if (distancia_destino <= 100 || !guardadollegaDestino) {
                WebService.distancia_a_recorrer = distancia_destino;
                WebService.EstadoActual = 4;
                guardadollegaDestino = true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void mover_google_maps_marker(LatLng toPosition) {
        try{
            animateMarkerToGB(marcador_actual,toPosition, new LatLngInterpolator.Spherical());
            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(toPosition,16);
            mMap.animateCamera(location);
            verificar_distancia_punto(toPosition);
            remake_polyline(toPosition);
        }catch (Exception e){
                e.printStackTrace();
            }
        }

    /*METODO QUE VERIFICA POR TRIGONOMETRIA SI SIGUE LA RUTA INDICADA O NO
    * POR TRIGONOMETRIA SI IMAGINAMOS UN LOS DOS PUNTOS SI EL MOVIL SE ENCUENTRA DENTRO LA DISTANCIA QUE SUMAN DESDE
    * EL PUNTO ANTERIOR HASTA EL PROXIMO DEBERIA SER APROXIMADAMENTE LA MISMA QUE LA DIFERENCIA ENTRE LOS PUNTOS
    *
    *  EJEMPLO CON A,B SIENDO LOS PUNTOS DE INICIO Y FIN, MIENTRAS QUE C ES LA LOCALIZACION ACTUAL
    * a         a \
    * |         |   \
    * c         |    c
    * |         |   /
    * b         b /
    */
    public boolean verificar_continua_por_camino(LatLng posicion_actual){
        try {
            System.out.println("verificar_continua_por_camino");
            double distancia_hasta_proximo_punto = Utilidad.calcularDistancia(posicion_actual.latitude, posicion_actual.longitude, google_directions_points.get(google_directions_points_index + 1).latitude, google_directions_points.get(google_directions_points_index+ 1).longitude);
            double distancia_hasta_punto_anterior = Utilidad.calcularDistancia(google_directions_points.get(google_directions_points_index).latitude, google_directions_points.get(google_directions_points_index).longitude,posicion_actual.latitude, posicion_actual.longitude);
            double distancia_entre_puntos = Utilidad.calcularDistancia(google_directions_points.get(google_directions_points_index).latitude, google_directions_points.get(google_directions_points_index).longitude, google_directions_points.get(google_directions_points_index + 1).latitude, google_directions_points.get(google_directions_points_index + 1).longitude);
            double diferencia = (distancia_hasta_proximo_punto + distancia_hasta_punto_anterior) - distancia_entre_puntos;
            if(diferencia > 20){
                if(AppPrendida) {
                    final Location actual_location = new Location("");
                    actual_location.setLatitude(posicion_actual.latitude);
                    actual_location.setLongitude(posicion_actual.longitude);

                    GuardarDatosUsuario objeto = new GuardarDatosUsuario( context2 );
                    objeto.GuardarDatos();

                    /*params = new RequestParams();
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
                    params.put("nro_orden", Integer.valueOf(WebService.nro_orden));
                    params.put("distancia_a_recorrer", WebService.distancia_a_recorrer);
                    params.put("motivo_pausa", WebService.ultimaPausa);//2
                    ActualizarUbicacion actuUbic = new ActualizarUbicacion(new AsyncResponse() {
                        public void processFinish(Object output) {*/
                            WebService.LastLocationSaved = new LatLng(WebService.lat_actual,WebService.long_actual);
                            DrawRoute(actual_location);
                       /* }
                    });
                    actuUbic.execute();*/
                }
                return false;
            }else{
                verificar_distancia_punto(posicion_actual);
                if(Utilidad.calcularDistancia(WebService.lat_actual,WebService.long_actual,WebService.LastLocationSaved.latitude,WebService.LastLocationSaved.longitude) > 30){
                    GuardarDatosUsuario objeto = new GuardarDatosUsuario( context2 );
                    objeto.GuardarDatos();

                    /*params = new RequestParams();
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
                    params.put("nro_orden", Integer.valueOf(WebService.nro_orden));
                    params.put("distancia_a_recorrer", WebService.distancia_a_recorrer);
                    params.put("motivo_pausa", WebService.ultimaPausa);//2
                    ActualizarUbicacion actuUbic = new ActualizarUbicacion(new AsyncResponse() {
                        public void processFinish(Object output) {*/
                            WebService.LastLocationSaved = new LatLng(WebService.lat_actual, WebService.long_actual);
                        /*}
                    });
                    actuUbic.execute();*/
                }
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void remake_polyline(LatLng actual_position){
        if(google_directions_points.size() >0) {
            try{
            lineOptions = new PolylineOptions();
            ArrayList<LatLng> points = new ArrayList<>();
            for(int i = google_directions_points_index; i<google_directions_points.size();i++){
                  points.add(google_directions_points.get(i));
            }
            ArrayList<LatLng> puntos = new ArrayList<>();
            puntos.add(0,actual_position);
            for(int index = 1;index < points.size();index++){
                puntos.add((index),points.get(index-1));
            }
            lineOptions.addAll(puntos);
            lineOptions.width(8);
            lineOptions.color(Color.RED);
            if (lineOptions != null) {
                mPolyline.remove();
                mPolyline = mMap.addPolyline(lineOptions);
            }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    //METODO PARA VERIFICAR SI ESTA SUFICIENTEMENTE CERCA DEL PUNTO FINAL
    private void verificar_distancia_punto(LatLng posicion_actual){
        try {
            if(google_directions_points.size() > 0 ) {
                double ultima_diferencia_prox = 100.0;
                double ultima_diferencia_ant = 100.0;
                int menor_index = google_directions_points_index;
                for(int index = google_directions_points_index; index < google_directions_points.size()-1;index++){
                    double distancia_hasta_proximo_punto = Utilidad.calcularDistancia(posicion_actual.latitude, posicion_actual.longitude, google_directions_points.get(index + 1).latitude, google_directions_points.get(index+ 1).longitude);
                    double distancia_hasta_punto_anterior = Utilidad.calcularDistancia(google_directions_points.get(index).latitude, google_directions_points.get(index).longitude,posicion_actual.latitude, posicion_actual.longitude);
                    if(distancia_hasta_proximo_punto < 100) {
                        System.out.println("Ultima dif prox: " + ultima_diferencia_prox + " Distancia Pox pun: " + distancia_hasta_proximo_punto);
                        System.out.println("Ultima dif ant: " + ultima_diferencia_ant + " Distancia ant pun" + distancia_hasta_punto_anterior);
                    }
                    if(ultima_diferencia_prox > distancia_hasta_proximo_punto || ultima_diferencia_ant > distancia_hasta_punto_anterior){
                        ultima_diferencia_prox = distancia_hasta_proximo_punto;
                        ultima_diferencia_ant = distancia_hasta_punto_anterior;
                        menor_index = index;
                        System.out.println("index: " + menor_index);
                    }
                    index++;
                }
                google_directions_points_index = menor_index;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /*METODO QUE DIBUJA LA LOCALIZACION
    ***********************************
    * @params Location location = localizacion actual del usuario (esta es una ubicacion que siempre es valida)
    * */
    private void DrawRoute(Location location) {
        System.out.println("DrawRoute");
        try{
       // Toast.makeText(context2, "DrawRoute", Toast.LENGTH_LONG).show();
        if (Utilidad.isNetworkAvailable() && AppPrendida) {
            Marker marker = (Marker) hashMapMarker.get( "actual" );
            if (hashMapMarker.containsValue( marcador_actual )) {
                hashMapMarker.remove( marker );
                marker.remove();
            }
            LatLng miUbicacion = new LatLng( location.getLatitude(), location.getLongitude() );
            WebService.lat_actual = location.getLatitude();
            WebService.long_actual = location.getLongitude();
            mMap.clear();
            rehacerDibujo = WebService.lat_actual == 0.0 && WebService.long_actual == 0.0 ? true : false;
            marcador_actual = mMap.addMarker( new MarkerOptions()
                    .position( new LatLng( WebService.lat_actual, WebService.long_actual ) )
                    .title( "Ubicacion actual" )
                    .snippet( "Ubicacion actual" )
                    .icon( BitmapDescriptorFactory.fromBitmap( resizeMapIcons( "truckicon8", 85, 85 ) ) )
                    .flat( true )
            );
            hashMapMarker.put( "actual", marcador_actual );
            MarkerPoints.add( miUbicacion );
            MarkerPoints.add( cordenadasDestino );
            marcador_actual.setPosition( miUbicacion );
            //Codigo que realiza el zoom hasta la localizacion actual
            mMap.moveCamera( CameraUpdateFactory.newLatLng( miUbicacion ) );
            options1.position( MarkerPoints.get( 0 ) );
            options1.position( MarkerPoints.get( 1 ) );
            if (MarkerPoints.size() == 1) {
                options1.icon( BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_GREEN ) );
            } else if (MarkerPoints.size() == 2) {
                options1.icon( BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_RED ) );
            }
            // Add new marker to the Google Map Android API V2
            mMap.addMarker( options1 );

            // Checks, whether start and end locations are captured
            if (MarkerPoints.size() >= 2) {
                LatLng origin = new LatLng( WebService.lat_actual, WebService.long_actual );
                LatLng dest = MarkerPoints.get( 1 );
                // Getting URL to the Google Directions API
                String url = getUrl( origin, dest );
                FetchUrl FetchUrl = new FetchUrl();
                // Start downloading json data from Google Directions API
                FetchUrl.execute( url );
                //move map camera
                mMap.moveCamera( CameraUpdateFactory.newLatLng( origin ) );
                mMap.animateCamera( CameraUpdateFactory.zoomTo( 16 ) );
                // LatLng destino = new LatLng( WebService.Entrega_A_Realizar.getLatiud_Ubic(),WebService.Entrega_A_Realizar.getLongitud_Ubic() );
                obtenerDistanciaTotal( miUbicacion, cordenadasDestino );
            }
            distanciaTotal.setText(getResources().getString(R.string.DistanciaRec) + " " + WebService.distancia_a_recorrer);

        } else {
           Utilidad.CargarToastConexion(context2);
        }

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    /*METODO PARA GENERAR UN MENSAJE DE ALERTA
    * @params Context context = la activity en la que se encuentra el usuario
    * */
    public static void LocationAlertDialog(final Context context) {
        AlertDialog.Builder d = new AlertDialog.Builder( context );
        d.setTitle( "GPS apagado" );
        d.setMessage( "No se puede encontrar su localizacion actual, por favor, verifique que esta activada" );
        d.setNegativeButton( context.getResources().getString( R.string.dialogCancel ), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        } );
        d.setPositiveButton( context.getResources().getString( R.string.diadialogAccept ), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                context.startActivity( new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS ) );
            }
        } );
        d.show();
    }

    /*METODO PARA VERIFICAR SI TIENE LOS PERMISOS DE GEOLOCALIZACION PARA LA APLICACION
    *********************************************************************************** */
    @SuppressLint("NewApi")
    private boolean isPermissionGranted() {
        if (checkSelfPermission( Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED || checkSelfPermission( Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /*METODO QUE CREA LA CALL A GOOGLE PARA PEDIR LOS DATOS
    *******************************************************
    * @params LatLng origin = latitud y longitud de inicio
    * @params LatLng dest = latitud y logitud de destino final
    * */
    private String getUrl(LatLng origin, LatLng dest) {
        //Este metodo crea la URL para pedirle a google que nos de la mejor ruta hasta el destino deseado
        // Origin of route
        Inicio = origin;
        Fin = dest;
        String str_origin = "&origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "&destination=" + dest.latitude + "," + dest.longitude;
        //Key of google maps api
        String key = "&key=AIzaSyCt8_WQruBJmssul2gJI4ng9R5CYNPMmrI";
        // Sensor enabled
        String sensor = "&sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin + str_dest + key + sensor;
        // Output format
        String output = "json?";
        //Units returned
        String units = "units=metric";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + units + parameters;
        return url;
    }

    /*METODO PARA VERIFICAR LA DISTANCIA TOTAL HASTA EL DESTINO
    ***********************************************************
    * @params LatLng ubicacion_actual = la ubicacion actual del usuario
    * @params LatLng ubicacion_destino = la ubicacion del destino
    * */
    public void obtenerDistanciaTotal(LatLng ubicacion_actual, LatLng ubicacion_destino) {
        String UrlPrueba = getUrl( ubicacion_actual, ubicacion_destino );
        URLDistanciaInicial = UrlPrueba;
        InvocarApiDistancia task = new InvocarApiDistancia();
        task.execute();
        try {
            task.get();
        } catch (Exception exc) {
            exc.toString();
        }
    }

    /*METODO PARA INICIAR EL VIAJE DENTRO DE LA BASE DE DATOS
    *********************************************************
    * @params RequestParams params = parametros para actualizar la tabla (ver en el codigo onMapReady)
    * */
    private class IniciarViaje extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            WebService.IngresarViaje( params, "Viajes/IniciarViaje.php" );
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            if(!WebService.errToken.equals("")){
                Intent myIntent = new Intent(context2, Login.class);
                startActivity(myIntent);
            }
        }
    }

    /*METODO QUE TRAE LA LISTA DE ARTICULOS DENTRO DE LA ENTREGA
    ************************************************************
    * @params RequestParams params = parametros para actualizar la tabla (ver en el codigo onCreate)
    * */
    private class TraerItems extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context2 );
        public AsyncResponse delegate = null;//Call back interface
        public TraerItems(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... strings) {
            WebService.TraerListaItems( params, "Viajes/TraerItems.php" );
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
            if(!WebService.errToken.equals("")){
                Intent myIntent = new Intent(context2, Login.class);
                startActivity(myIntent);
            }
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject( jsonData[0] );
                Log.d( "ParserTask", jsonData[0].toString() );
                DataParser parser = new DataParser();
                Log.d( "ParserTask", parser.toString() );

                // Starts parsing data
                routes = parser.parse( jObject );
                Log.d( "ParserTask", "Executing routes" );
                Log.d( "ParserTask", routes.toString() );

            } catch (Exception e) {
                Log.d( "ParserTask", e.toString() );
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            lineOptions = null;
            // Traversing through all the routes
            try {
                google_directions_points = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<>();

                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                        if (google_directions_points.size() == j) {
                            google_directions_points.add(position);
                        } else {
                            google_directions_points.set(j, position);
                        }
                    }


                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(8);
                    lineOptions.color(Color.RED);
                }

                // Drawing polyline in the Google Map for the i-th route
                if (lineOptions != null) {
                    if (mPolyline != null) {
                        mPolyline.remove();
                    }
                    mPolyline = mMap.addPolyline(lineOptions);
                }
                InvocarApiDistancia task = new InvocarApiDistancia();
                task.execute();
                try {
                    task.get();
                } catch (Exception exc) {
                    exc.toString();
                }
                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }catch(Exception e){
                Log.e( "onPostExecute" , "error dentro del try principal " + e.toString() );
            }
            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                //mMap.addPolyline( lineOptions );
            } else {
                Toast.makeText( Recorrido_Viaje.this, "No se ha podido dibujar el recorrido", Toast.LENGTH_SHORT ).show();
            }
        }
    }

    private class InvocarApiDistancia extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            WebService.TraerDistancia( URLDistanciaInicial );
            return null;
        }
    }

    /*CLASE PARA LEVANTAR LA PAUSA DENTRO DEL VIAJE (SE UTILIZA SOLAMENTE SI HA REALIZADO UNA PAUSA ANTERIORMENTE)
    **************************************************************************************************************/
    private class LevantarPausa extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context2 );
        public AsyncResponse delegate = null;//Call back interface

        public LevantarPausa(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params1) {
            if (Utilidad.isNetworkAvailable()) {
                WebService.PausarViaje( params, "Viajes/ReanudarViaje.php" );
            } else {
                Utilidad.CargarToastConexion( context2 );
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
            if(!WebService.errToken.equals("")){
                Intent myIntent = new Intent(context2, Login.class);
                startActivity(myIntent);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    /*METODO PARA CUANDO EL USUARIO LE DA ATRAS CON LOS BOTONES AUXILARES EN ANDROID
    ********************************************************************************
    * El metodo realiza el mismo back que realizan los botones de navegacion de la aplicacion normales*/
    @Override
    public void onBackPressed() {
        Utilidad.vibraticionBotones(context2);
        try {
            if (WebService.usuarioActual.getEs_Entrega().equals("S") & !valorIntent.equals("Cobranza")) {
                if (WebService.viajeSeleccionado.getEstado().equals("2")) {

                } else if (WebService.EstadoActual == 3) {
                    //SI CUANDO ENTRA EN RECORRIDO VIAJE ES RETORNO
                    if (WebService.entregasTraidas.size() == 0) {

                    }
                } else {
                    WebService.EstadoAnterior = WebService.EstadoActual;
                    if (WebService.ViajeActualSeleccionado) {
                        Intent myIntent = new Intent(context2, SalirViaje.class);
                        startActivity(myIntent);
                    } else {
                        ViajeSeleccionado();
                    }
                }
            } else if (WebService.usuarioActual.getEs_Cobranza().equals("S")/* && WebService.usuarioActual.getEs_Entrega().equals( "N" )*/ && (WebService.usuarioActual.getTipoCobrador().equals("D"))) {
                Intent myIntent = new Intent(context2, MenuCobranzas.class);
                startActivity(myIntent);
            } else if (WebService.usuarioActual.getEs_Cobranza().equals("S") /*&& WebService.usuarioActual.getEs_Entrega().equals( "N" )*/ && (WebService.usuarioActual.getTipoCobrador().equals("L"))) {
                Intent myIntent = new Intent(context2, SeleccionCliente.class);
                startActivity(myIntent);
            } else if (WebService.usuarioActual.getEs_VentaDirecta().equals("S")) {
                Intent myIntent = new Intent(context2, FacturaDirecta.class);
                startActivity(myIntent);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}


