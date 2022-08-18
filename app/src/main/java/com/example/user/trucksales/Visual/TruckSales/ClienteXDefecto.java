package com.example.user.trucksales.Visual.TruckSales;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.ClienteCobranza;
import com.example.user.trucksales.Encapsuladoras.Efectivo;
import com.example.user.trucksales.Encapsuladoras.Entrega;
import com.example.user.trucksales.Encapsuladoras.FacturaXDia;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Cobranza.MenuCobranzas;
import com.example.user.trucksales.Visual.Cobranza.SeleccionarDeudas;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.Visual.Pedidos.Pedidos;
import com.example.user.trucksales.Visual.SeleccionFuncionablidad;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EmptyStackException;

import static android.view.Window.FEATURE_NO_TITLE;

public class ClienteXDefecto extends Activity {
    Button btnAceptar,btnOtroCliente,btnPausar;
    TextView txtEntrega, LblUsu, LblFecha;;
    ImageView flecha, casita,factura_directa;
    Context contexto;
    Utilidades Utilidad;
    RequestParams params1;
    RequestParams params2;
    RequestParams params3;
    RequestParams params4 = new RequestParams();
    protected static RequestParams params;
    //LocationManager locationManager;

    private static ImageView pedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cliente_xdefecto);

        //locationManager = (LocationManager)getSystemService( Context.LOCATION_SERVICE );

        contexto = this;
        WebService.Entrega_A_Realizar = new Entrega();
        WebService.EstadoActual = 0;
        //System.out.println("Entra en ClienteXDefecto");
        Utilidad = new Utilidades(contexto);

        GuardarDatosUsuario.Contexto = contexto;

        try{
        if (WebService.USUARIOLOGEADO != null) {
            WebService.ViajeActualSeleccionado = false;
            flecha = findViewById(R.id.btnAtras);

            //WebService.addEfectivo(new Efectivo(Double.valueOf(100.00)));

            WebService.limpiarValores();
            WebService.listaProductos.clear();
            WebService.Entrega_A_Realizar = WebService.entregaDefault;

            flecha.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try{
                    Utilidad.vibraticionBotones(contexto);
                    if (WebService.usuarioActual.getEs_Entrega().equals("S") && !WebService.usuarioActual.getEs_Cobranza().equals("S")) {
                        Intent myIntent = new Intent(v.getContext(), SalirViaje.class);
                        startActivity(myIntent);
                    } else if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D")) {
                        /*CargarViajesCobrador task = new CargarViajesCobrador(new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) {*/
                        Intent myIntent = new Intent(contexto, MenuCobranzas.class);
                        startActivity(myIntent);
                           /* }
                        });
                        task.execute();*/
                    }else if(WebService.usuarioActual.getEs_Entrega().equals("S") && WebService.usuarioActual.getEs_Cobranza().equals("S")){
                        Intent myIntent = new Intent(contexto, SeleccionFuncionablidad.class);
                        startActivity(myIntent);
                    }
                    }catch (Exception ex){
                        ex.printStackTrace();
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
                            Utilidad.vibraticionBotones(contexto);
                            Intent myIntent = new Intent(v.getContext(), Pedidos.class);
                            myIntent.putExtra("intent", "ClienteXDefecto");
                            startActivity(myIntent);
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
            }

            LblUsu = findViewById(R.id.LblUsu);
            LblUsu.setText(getResources().getString(R.string.usuarioTool) + WebService.USUARIOLOGEADO);

            final String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
            LblFecha = (TextView) findViewById(R.id.LblFecha);
            LblFecha.setText(timeStamp);


            factura_directa = (ImageView) findViewById(R.id.factura_directa);
            factura_directa.setClickable(true);

            //METODO ON CLICK DEL factura_directa
            factura_directa.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        Utilidad.vibraticionBotones(contexto);
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

            //AGREGADO 24/06/2019 BDL
            casita = findViewById(R.id.casita);
            casita.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                    Utilidad.vibraticionBotones(contexto);
                    if (!WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getEs_Entrega().equals("S")) {
                        Intent myIntent = new Intent(v.getContext(), Viajes.class);
                        startActivity(myIntent);
                    } else if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D")) {
                        /*CargarViajesCobrador task = new CargarViajesCobrador(new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) {*/
                        Intent myIntent = new Intent(contexto, MenuCobranzas.class);
                        startActivity(myIntent);
                           /* }
                        });
                        task.execute();*/
                    }
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            });
            casita.setVisibility(View.INVISIBLE);
            casita.setEnabled(false);

            txtEntrega = findViewById(R.id.TxtDatosCliente);//asdsadasdasd
            btnAceptar = findViewById(R.id.btnAceptarViaje);
            btnOtroCliente = findViewById(R.id.btnOtroCliente);
            btnPausar = findViewById(R.id.btnPausar);

            CargarDatos();

            if (WebService.EstadoAnterior == 0 || WebService.usuarioActual.getEs_Entrega().equals("S") && WebService.usuarioActual.getEs_Cobranza().equals("S")) {
                if (WebService.usuarioActual.getTipoCobrador().equals("D")) {
                    flecha.setVisibility(View.VISIBLE);
                    flecha.setEnabled(true);
                } else if (WebService.usuarioActual.getTipoCobrador().equals("L")) {
                    flecha.setVisibility(View.VISIBLE);
                    flecha.setEnabled(true);
                }else {
                    flecha.setVisibility(View.INVISIBLE);
                    flecha.setEnabled(false);
                }
            }
            btnPausar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Utilidad.vibraticionBotones(contexto);
                    if (Utilidad.isNetworkAvailable()) {
                        Intent myIntent = new Intent(v.getContext(), PausarViaje.class);
                        startActivity(myIntent);
                    } else {
                        Utilidad.CargarToastConexion(contexto);
                    }

                }
            });
            btnOtroCliente.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Utilidad.vibraticionBotones(contexto);
                    if (Utilidad.isNetworkAvailable()) {
                        Intent nextActivity = new Intent(contexto, Entregas.class);
                        startActivity(nextActivity);
                    }
                }

            });

            final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

            swipeRefreshLayout.setColorSchemeColors(Color.BLACK);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.setRefreshing(true);
                    if (!WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getEs_Entrega().equals("S")) {
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
                                    if (!(WebService.entregasTraidas.size() - 1 > 0)) {
                                        btnOtroCliente.setBackgroundColor(contexto.getColor(R.color.entrega));
                                        btnOtroCliente.setEnabled(false);
                                    } else {
                                        btnOtroCliente.setBackgroundColor(contexto.getColor(R.color.colorBoton));
                                        btnOtroCliente.setEnabled(true);
                                    }
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        });
                        task.execute();
                    } else if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D")) {
                        params2 = new RequestParams();
                        params2.put("nro_viaje", WebService.viajeSeleccionadoCobrador.getNumViaje());
                        params2.put("username", WebService.USUARIOLOGEADO);
                        TraerClientesViajes task2 = new TraerClientesViajes(new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) {
                                if (!WebService.errToken.equals("")) {
                                    Intent myIntent = new Intent(contexto, Login.class);
                                    startActivity(myIntent);
                                }else {
                                    btnOtroCliente.setBackgroundColor(contexto.getColor(R.color.colorBoton));
                                    btnOtroCliente.setEnabled(true);
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        });
                        task2.execute();
                    }
                }
            });
            btnAceptar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Utilidad.vibraticionBotones(contexto);
                    if (Utilidad.isNetworkAvailable()) {
                        if (!WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getEs_Entrega().equals("S") || WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getEs_Entrega().equals("S")) {
                            WebService.EstadoActual = 1;
                            WebService.ViajeActualSeleccionado = true;

                            obtenerHora();
                            //Hasta aca el metodo guarda la hora de comienzo del viaje
                            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            Criteria criteria = new Criteria();
                            if (ActivityCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            Location location = new Location(locationManager.getBestProvider(criteria, false));
                            double latitude = 0;
                            double longitude = 0;
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }

                            WebService.lat_origen = latitude;
                            WebService.long_origen = longitude;

                            //CODIGO PRUEBA 11/11/19
                            if (WebService.viajeSeleccionado.getTipo().equals("retiro")) {
                                casita.setVisibility(View.VISIBLE);
                                casita.setEnabled(true);
                                params1 = new RequestParams();
                                params1.add("cod_tit", WebService.RetiroSeleccionado.getCod_Tit().trim());
                                params1.add("cod_suc", WebService.RetiroSeleccionado.getCod_Sucursal().trim());
                                params1.add("nro_trans", WebService.RetiroSeleccionado.getNro_Trans().trim());
                                params1.add("user", WebService.USUARIOLOGEADO);
                                WebService.nro_trans = Integer.valueOf(WebService.RetiroSeleccionado.getNro_Trans().trim());
                                WebService.nFac.setCod_Sucursal(WebService.RetiroSeleccionado.getCod_Sucursal().trim());
                                WebService.nFac.setNro_Trans(WebService.RetiroSeleccionado.getNro_Trans().trim());
                                WebService.nFac.setCod_Tit(WebService.RetiroSeleccionado.getCod_Tit().trim());
                                TreaerInfoCajas task = new TreaerInfoCajas(new Generados.AsyncResponse() {
                                    public void processFinish(Object output) {
                                        if (!WebService.errToken.equals("")) {
                                            Intent myIntent = new Intent(contexto, Login.class);
                                            startActivity(myIntent);
                                        } else {
                                            TraerTipoCajas task2 = new TraerTipoCajas(new Generados.AsyncResponse() {
                                                @Override
                                                public void processFinish(Object output) {
                                                    Intent myIntent = new Intent(contexto, CajasTraidas.class);
                                                    startActivity(myIntent);
                                                }
                                            });
                                            task2.execute();
                                        }
                                    }
                                });
                                task.execute();
                            } else {
                                WebService.viajeSeleccionado.setEstado("1");
                                Intent myIntent = new Intent(contexto, Recorrido_Viaje.class);
                                myIntent.putExtra("intent", "TruckSales");
                                startActivity(myIntent);
                            }

                            /*WebService.traerEntregaXNroOrden(WebService.nro_orden);
                            WebService.Entrega_A_Realizar = WebService.entregaDefault;
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
                            params.put("en_pausa", "1");
                            params.put("nro_orden", Integer.valueOf(WebService.nro_orden));
                            params.put("distancia_a_recorrer", WebService.distancia_a_recorrer);
                            params.put("motivo_pausa", WebService.ultimaPausa);//2
                            ActualizarUbicacion actuUbic = new ActualizarUbicacion(new AsyncResponse() {
                                public void processFinish(Object output) {

                                }
                            });
                            actuUbic.execute();*/

                        } else if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D") && WebService.usuarioActual.getEs_Entrega().equals("N")) {
                            WebService.EstadoActual = 1;

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

                            WebService.lat_actual = latitude;
                            WebService.long_actual = longitude;

                            params = new RequestParams();
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

                            ActualizarUbicacion actuUbic = new ActualizarUbicacion();
                            actuUbic.execute();

                            /*WebService.EstadoActual = 1;
                            GuardarDatosUsuario objeto = new GuardarDatosUsuario( contexto );
                            objeto.GuardarDatos();*/

                            WebService.EstadoActual = 1; //PARA QUITAR EL SONIDO
                            if (WebService.clienteActual.getLatiud_Ubic() == 0.0 || WebService.clienteActual.getLongitud_Ubic() == 0.0) {
                                if (WebService.viajeSeleccionadoCobrador.getNumViaje() != null) {
                                    TraerDeudasViaje task2 = new TraerDeudasViaje(new AsyncResponse() {
                                        @Override
                                        public void processFinish(Object output) {
                                            if (!WebService.errToken.equals("")) {
                                                Intent myIntent = new Intent(contexto, Login.class);
                                                startActivity(myIntent);
                                            }else {
                                                Intent myIntent = new Intent(contexto, SeleccionarDeudas.class);
                                                startActivity(myIntent);
                                            }
                                        }
                                    });
                                    task2.execute();
                                }
                            } else {
                                obtenerHora();
                                WebService.EstadoActual = 1;
                                WebService.ViajeActualSeleccionadoCobrador = true;
                                Intent myIntent = new Intent(contexto, Recorrido_Viaje.class);
                                myIntent.putExtra("intent", "Cobranza");
                                startActivity(myIntent);
                            }
                        }

                    } else {
                        Utilidad.CargarToastConexion(contexto);
                    }
                }
            });

        } else {
            Intent myIntent = new Intent(contexto, Login.class);
            startActivity(myIntent);

        }
    }catch(Exception ex){
        ex.printStackTrace();
    }

        //HILO QUE GUARDA LA LOCALIZACION ACTUAL CADA 5 SEGUNDOS
    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    private class  VerEntrega extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface
        public VerEntrega(AsyncResponse asyncResponse) {
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

    private class  TraerTipoCajas extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public Generados.AsyncResponse delegate = null;//Call back interface
        public TraerTipoCajas(Generados.AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            // vj = new Viajes();
            if(Utilidad.isNetworkAvailable()) {
                params = new String[0];
                WebService.TraerTipoCajas("Consultas/Cajas.php");
                return null;

            } else {
                Toast toast=Toast.makeText(getApplicationContext(),getResources().getString( R.string.ErrorConexion ), Toast.LENGTH_SHORT);
                toast.setGravity( Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                toast.show();//showing the toast is important**

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

    private class TreaerInfoCajas extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public Generados.AsyncResponse delegate = null;//Call back interface
        public TreaerInfoCajas(Generados.AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            WebService.ObtengoSucursal( "Envases/ValidacionEnvase.php",params1 );
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

    //CARGA LOS DATOS DEL ACTIVITY CORRECTAMENTE
    private void CargarDatos(){
        try {
            if (WebService.usuarioActual.getEs_Entrega().equals("S")) {
                if (WebService.viajeSeleccionado != null & WebService.viajeSeleccionado.getTipo() != null) {//agrego para evitar crash por null
                    if (WebService.viajeSeleccionado.getTipo().equals("entrega")) {
                        String NV, HD, HH, NT, NS, D, ND, CS, NDR, OB;
                        NV = WebService.viajeSeleccionado.getNumViaje() == null ? "" : WebService.viajeSeleccionado.getNumViaje().trim();
                        HD = WebService.entregaDefault.getHora_Desde() == null ? "" : WebService.entregaDefault.getHora_Desde().trim();
                        HH = WebService.entregaDefault.getHora_Hasta() == null ? "" : WebService.entregaDefault.getHora_Hasta().trim();
                        NT = WebService.entregaDefault.getNom_Tit() == null ? "" : WebService.entregaDefault.getNom_Tit().trim();
                        NS = WebService.entregaDefault.getNom_Sucursal() == null ? "" : WebService.entregaDefault.getNom_Sucursal().trim();
                        D = WebService.entregaDefault.getDireccion() == null ? "" : WebService.entregaDefault.getDireccion().trim();
                        ND = WebService.entregaDefault.getNro_Docum() == null ? "" : WebService.entregaDefault.getNro_Docum().trim();
                        CS = WebService.entregaDefault.getCod_Sucursal() == null ? "" : WebService.entregaDefault.getCod_Sucursal().trim();
                        NDR = WebService.entregaDefault.getNro_doc_ref() == null ? "" : WebService.entregaDefault.getNro_doc_ref().toString();
                        //Agrego a pedido Observaciones 17/07/2019
                        OB = WebService.entregaDefault.getObservaciones() == null ? "" : WebService.entregaDefault.getObservaciones().trim();
                        if (OB.equals("")) {
                            String textoCarga = getResources().getString(R.string.NumViajeProd) + " " + NV + "\n";
                            textoCarga += getResources().getString(R.string.HorDeCliDef) + " " + HD + " " + getResources().getString(R.string.aCli) + " " + HH;
                            textoCarga += NT + "\n" + NS + "\n" + D + "\n" + getResources().getString(R.string.NroOrdenCli) + " " + ND;

                            txtEntrega.setText(textoCarga);
                        } else {
                            String textoCarga = getResources().getString(R.string.NumViajeProd) + " " + NV + "\n";
                            textoCarga += getResources().getString(R.string.HorDeCliDef) + " " + HD + " " + getResources().getString(R.string.aCli) + " " + HH;
                            textoCarga += NT + "\n" + NS + "\n" + D + "\n" + getResources().getString(R.string.NroOrdenCli) + " " + ND;
                            textoCarga += "\n" + getResources().getString(R.string.Observaciones) + " " + OB;

                            txtEntrega.setText(textoCarga);
                        }

                        String[] partes = txtEntrega.getText().toString().split("\n");
                        //System.out.println( "La distancia del cliente default es: " + D );
                        WebService.Entrega_A_Realizar = WebService.entregaDefault;
                        WebService.clienteDestino = NT;
                        WebService.nombreLocal = NS;
                        WebService.direccion = D;
                        WebService.observaciones = OB;
                        WebService.nro_orden = ND;
                        WebService.cod_sucu = CS;
                        WebService.nro_doc_ref = NDR;
                        if (!(WebService.entregasTraidas.size() - 1 > 0)) {
                            btnOtroCliente.setBackgroundColor(contexto.getColor(R.color.entrega));
                            btnOtroCliente.setEnabled(false);
                        }

                    } else if (WebService.viajeSeleccionado.getTipo().equals("retiro")) {
                        casita.setVisibility(View.VISIBLE);
                        casita.setEnabled(true);

                        txtEntrega.setText(getResources().getString(R.string.CXDTitleMessage_Retiro) + "\n"
                                + getResources().getString(R.string.CXDTextRetiro_NumViaje) + ": " + WebService.viajeSeleccionado.getNumViaje() + "\n"
                                + getResources().getString(R.string.CXDTextRetiro_NombreSucursal) + ": " + WebService.RetiroSeleccionado.getSucursal());
                        if (!(WebService.ListaRetiros.size() - 1 > 0)) {
                            btnOtroCliente.setBackgroundColor(contexto.getColor(R.color.entrega));
                            btnOtroCliente.setEnabled(false);
                        }
                    }
                    txtEntrega.setTextSize(20);
                }
            } else {
                if (WebService.viajeSeleccionadoCobrador.getTipo() != null) {
                    String NV, NT, NS, D, NE;
                    NV = WebService.viajeSeleccionadoCobrador.getNumViaje() == null ? "" : WebService.viajeSeleccionadoCobrador.getNumViaje().trim();
                    NE = WebService.clienteActual.getNomEmp() == null ? "" : WebService.clienteActual.getNomEmp().trim();
                    NT = WebService.clienteActual.getNom_Tit() == null ? "" : WebService.clienteActual.getNom_Tit().trim();
                    NS = WebService.clienteActual.getCod_Tit_Gestion() == null ? "" : WebService.clienteActual.getCod_Tit_Gestion().trim();
                    D = WebService.clienteActual.getDireccion() == null ? "" : WebService.clienteActual.getDireccion().trim();

                    String textoCarga = getResources().getString(R.string.NumViajeProd) + " " + NV + "\n";
                    textoCarga += getResources().getString(R.string.NomEmp) + " " + NE + "\n";
                    textoCarga += getResources().getString(R.string.NomTit) + " " + NT + "\n";
                    textoCarga += getResources().getString(R.string.CodTit) + " " + NS + "\n";
                    textoCarga += getResources().getString(R.string.Direccion) + " " + D + "\n";

                    txtEntrega.setText(textoCarga);
                    txtEntrega.setTextSize(20);

                    WebService.Cliente_A_IR = WebService.clienteActual;
                    WebService.cod_emp = NE;
                    WebService.cod_tit = NS;
                    WebService.direccion = D;
                    if (!(WebService.clienteTraidos.size() - 1 > 0)) {
                        btnOtroCliente.setBackgroundColor(contexto.getColor(R.color.entrega));
                        btnOtroCliente.setEnabled(false);
                    }
                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    //RECORRO EL ARRAY VIENDO LA PRIORIDAD DE CADA UNO, EN BASE A ESTO SE REALIZA LA BUSQUEDA Y SE GUARDAN LOS DATOS
    public void SeleccionarEntrega(ArrayList<Entrega> Lista){
        try {
            //ESTAS VARIABLES SE UTILIZAN PARA SABER SI EL SISTEMA YA TIENE UN OBJETO GUARDADO DEL TIPO
            boolean EntregaEncontradaP1 = false;
            boolean EntregaEncontradaP2 = false;
            boolean EntregaEncontradaP9 = false;
            //RECORRER EL ARRAY
            ArrayList<Entrega> listaEntregas = new ArrayList<>();
            for (Entrega Objeto : Lista) {
                //SI LA PRIORIDAD ES 1 Y NO EXISTEN GUARDADOS CON ESA PRIORIDAD LO GUARDA
                if (Objeto.getPrioridad().equals("1")) {
                    // System.out.println( "Guarda el tipo 1" );
                    if (!EntregaEncontradaP1) {
                        listaEntregas = new ArrayList<>();
                    }
                    listaEntregas.add(Objeto);
                    EntregaEncontradaP1 = true;
                }

                //SI LA PRIORIDAD ES 2 Y NO EXISTEN GUARDADOS CON ESA PRIORIDAD PERO TAMPOCO EXISTEN GUARDADOS CON PRIORIDAD 1 GUARDA
                if (Objeto.getPrioridad().equals("2") && (!EntregaEncontradaP1)) {
                    // System.out.println( "Guarda el tipo 2" );
                    if (!EntregaEncontradaP2) {
                        listaEntregas = new ArrayList<>();
                    }
                    listaEntregas.add(Objeto);
                    EntregaEncontradaP2 = true;
                }

                //SI LA PRIORIDAD ES 9 Y NO EXISTEN GUARDADOS CON ESA PRIORIDAD PERO TAMPOCO DE PRIORIDAD 1 Y 2 ENTOCES GUARDA
                if (Objeto.getPrioridad().equals("9") && (!EntregaEncontradaP1) && (!EntregaEncontradaP2)) {
                    // System.out.println( "Guarda el tipo 9" );
                    if (!EntregaEncontradaP9) {
                        listaEntregas = new ArrayList<>();
                    }
                    listaEntregas.add(Objeto);
                    EntregaEncontradaP9 = true;
                }
            }
            if (listaEntregas != null) {
                WebService.entregaDefault = devolverMasCercano(listaEntregas);
                WebService.EntregasAnteriores.add(WebService.entregaDefault);
            } else {
                Toast.makeText(contexto, "Error: no existen otras entregas", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private Entrega devolverMasCercano(ArrayList<Entrega> listaEntregas){
        Entrega Object = new Entrega();
        try {

            //acá se cae porque no permite systemservice antes de onCreate
            //java.lang.IllegalStateException: System services not available to Activities before onCreate()
            LocationManager locationManager = (LocationManager)getSystemService( Context.LOCATION_SERVICE );

            Criteria criteria = new Criteria();

            //acá da error de nullpointer exception
            if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }

            Location location = locationManager.getLastKnownLocation( locationManager
                    .getBestProvider( criteria, false ) );
            double latitude = 0;
            double longitude = 0;
            if (location == null) {
                latitude = 0;
                longitude = 0;
            } else {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            double distanciaAnterior = 0.0;
            double distanciaActual;
            for (Entrega obj : listaEntregas) {
                distanciaActual = Math.sqrt( Math.pow( obj.getLatiud_Ubic() - latitude, 2 ) + Math.pow( obj.getLongitud_Ubic() - longitude, 2 ) );
                if (distanciaActual < distanciaAnterior) {   //si la latitud y la longitud son inferiores
                    Object = obj;
                    distanciaAnterior = distanciaActual;
                }
            }
            return Object;
        }catch(Exception e){
            e.printStackTrace();
           // Toast.makeText( getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG ).show();
            return listaEntregas.get( 0 );
        }

    }

    //RECORRO EL ARRAY DE RETIRO VIENDO SI EL IDEX ESTA REPETIDO O NO
    public void seleccionarRetiro(ArrayList<FacturaXDia>Lista){
        try {
            if (WebService.ListaRetiros.size() - 1 == WebService.lastRetiroSelected) {
                WebService.lastRetiroSelected = 0;
            }
            WebService.RetiroSeleccionado = WebService.ListaRetiros.get(WebService.LastIndexSelected);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (WebService.EstadoAnterior != 0) {
                if (WebService.usuarioActual.getEs_Entrega().equals("S") && !WebService.usuarioActual.getEs_Cobranza().equals("S")) {
                    Intent myIntent = new Intent(contexto, SalirViaje.class);
                    startActivity(myIntent);
                    finish();
                } else if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D")) {
                /*CargarViajesCobrador task = new CargarViajesCobrador(new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {*/
                    Intent myIntent = new Intent(contexto, MenuCobranzas.class);
                    startActivity(myIntent);
                    finish();
                   /* }
                });
                task.execute();*/
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private class  TraerClientesViajes extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface
        public TraerClientesViajes(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            if(Utilidad.isNetworkAvailable()) {
                WebService.TraerClientesViajes( params2, "Viajes/ViajesCobrador/TraerClientesViajes.php");
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
            delegate.processFinish( WebService.logueado);
            Utilidad.deleteLoadingMessage();
        }

        @Override
        protected void onProgressUpdate(Void... values)
        {

        }
    }

    private class TraerDeudasViaje extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface
        public TraerDeudasViaje(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            /*RequestParams params4 = new RequestParams(  );
            params4.put("cod_tit", WebService.clienteActual.getCod_Tit_Gestion());
            params4.put("num_viaje", WebService.viajeSeleccionadoCobrador.getNumViaje());
            params4.put("cod_emp",WebService.clienteActual.getCodEmp());*/
            WebService.TraerDeudasViaje(/*params4*/);
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

    private void obtenerHora() {
        try {
            Calendar rightNow = Calendar.getInstance();
            int hour = rightNow.get(Calendar.HOUR_OF_DAY);
            int minute = rightNow.get(Calendar.MINUTE);
            String horaAguardar = "";
            if (hour < 10) {
                horaAguardar = "0" + Integer.toString(hour) + ":";
            } else {
                horaAguardar = Integer.toString(hour) + ":";
            }
            if (minute < 10) {
                horaAguardar = horaAguardar + "0" + Integer.toString(minute);
            } else {
                horaAguardar = horaAguardar + Integer.toString(minute);
            }
            WebService.horaComienzoViaje = horaAguardar;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class ActualizarUbicacion extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... strings) {
            WebService.ActualizarUbicacion( params,"Viajes/ActualizarUbicacion.php" );
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {}
    }
}
