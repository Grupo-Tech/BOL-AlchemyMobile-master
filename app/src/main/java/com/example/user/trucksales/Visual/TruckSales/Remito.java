package com.example.user.trucksales.Visual.TruckSales;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.print.PrintManager;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.Entrega;
import com.example.user.trucksales.Encapsuladoras.Item;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Printer;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import static android.view.Window.FEATURE_NO_TITLE;
import static com.example.user.trucksales.Datos.WebService.entregasTraidas;
import static com.example.user.trucksales.Datos.WebService.nro_trans;

public class Remito extends Activity {
    public TextView orden,destino,pedido,viaje,tota,totaPedi,iva5,iva10,TotSinIVA,retorno,TotalIVA;
    TableLayout tablaFactura;
    public static String nro_rem;
    EditText cod_Suc_Tribut,cod_Fac_Tribut,num_Remito;
    List <TextView> subtotales = new ArrayList<>(  );
    Context contexto;
    ImageView atras;
    Button btnRemito;
    WebView mWebView;
    protected static RequestParams ParametrosCajas = new RequestParams(  );
    //private static final String WEB_URL = "http://192.168.0.100/TerosMobVerGT/ServerCode/mobileProd/Impresion/Impresiones/3686.html";
    private double neto;
    private ArrayList<TextView> ListaTextViews;
    private Productos IProductos = new Productos();
    private String cod_Suc;
    private Utilidades Utilidad;
    private static ArrayList<Entrega> listaEntregas;
    private static ArrayList<Entrega> listaEntregasHechas  = new ArrayList<>( );
    protected static RequestParams params1 = new RequestParams(  );

    protected static RequestParams params = new RequestParams(  );

    public Context getActivity() {
        return this;
    }

    private class IngresarRemito extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... strings)
        {
            //params = new RequestParams(  );
            WebService.IngresarRemito( "Facturas/Remito.php",params );

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!WebService.errToken.equals("")) {
                Intent myIntent = new Intent(contexto, Login.class);
                startActivity(myIntent);
            }
        }
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
       /* @Override
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
        }*/
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

    private class TreaerInfoCajas extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public Generados.AsyncResponse delegate = null;//Call back interface
        public TreaerInfoCajas(Generados.AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            WebService.ObtengoSucursal( "Envases/ValidacionEnvase.php",ParametrosCajas );
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

    @Override
    protected void onResume() {

        super.onResume();
        final ArrayList<Entrega> Lista = new ArrayList<>(  );
        try {
            if (WebService.retoRemito.equals("ok")) {
                //System.out.println("1");
                if (Utilidad.isNetworkAvailable()) {
                    listaEntregasHechas.add(WebService.Entrega_A_Realizar);
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
                    params.put("en_pausa", "5");
                    params.put("nro_orden", Integer.valueOf(WebService.nro_orden));
                    params.put("distancia_a_recorrer", WebService.distancia_a_recorrer);
                    params.put("motivo_pausa", WebService.ultimaPausa);//2
                    ActualizarUbicacion actuUbic = new ActualizarUbicacion(new AsyncResponse() {
                        public void processFinish(Object output) {
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
                                        listaEntregas = new ArrayList<>();

                                        //crea el array auxiliar sacando las entregas anteriores
                                        for (int i = 0; i < entregasTraidas.size(); i++) {
                                            for (int j = 0; j < listaEntregasHechas.size(); j++) {
                                                if (entregasTraidas.get(i).getNro_Docum() != listaEntregasHechas.get(j).getNro_Docum()) {
                                                    Lista.add(entregasTraidas.get(i));
                                                }
                                            }
                                        }

                                        //crea el array de entregas del mismo cliente y sucursal
                                        for (Entrega objeto : Lista) {
                                            if (objeto.getCod_Sucursal().equals(WebService.Entrega_A_Realizar.getCod_Sucursal()) && objeto.getCod_Tit().equals(WebService.Entrega_A_Realizar.getCod_Tit())) {
                                                listaEntregas.add(objeto);
                                            }
                                        }

                                        if (listaEntregas.size() > 0) {
                                            WebService.EstadoAnterior = WebService.EstadoActual;
                                            String prueba = listaEntregas.get(listaEntregas.size() - 1).getNro_Docum();
                                            WebService.Entrega_A_Realizar = listaEntregas.get(listaEntregas.size() - 1);
                                            params = new RequestParams();
                                            params.put("num_Viaje", Integer.valueOf(WebService.viajeSeleccionado.getNumViaje()));
                                            params.put("num_Orden", Integer.valueOf(prueba));
                                            params.put("cod_Sucursal", listaEntregas.get(listaEntregas.size() - 1).getCod_Sucursal());
                                            TraerItems task = new TraerItems(new AsyncResponse() {
                                                @Override
                                                public void processFinish(Object output) {
                                                    Intent myIntent = new Intent(contexto, Productos.class);
                                                    startActivity(myIntent);
                                                }
                                            });
                                            task.execute();
                                        } else {
                                            ParametrosCajas = new RequestParams();
                                            ParametrosCajas.add("cod_tit", WebService.Entrega_A_Realizar.getCod_Tit().trim());
                                            ParametrosCajas.add("cod_suc", WebService.cod_sucu.trim());
                                            ParametrosCajas.add("nro_trans", WebService.nro_trans_impresion);
                                            ParametrosCajas.add("user", WebService.USUARIOLOGEADO);
                                            WebService.nro_trans = Integer.valueOf(WebService.nro_trans);
                                            WebService.nFac.setCod_Sucursal(WebService.cod_sucu.trim());
                                            WebService.nFac.setNro_Trans(String.valueOf(nro_trans));
                                            WebService.nFac.setCod_Tit(WebService.Entrega_A_Realizar.getCod_Tit().trim());
                                            //GuardarFactura task3 = new GuardarFactura( new AsyncResponse() {
                                            // @Override
                                            //public void processFinish(Object output) {
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
                                        }

                                    }
                                }
                            });
                            task.execute();
                        }
                    });
                    actuUbic.execute();

                } else {
                    Utilidad.CargarToastConexion(contexto);
                }
            } else {
                retorno.setText(WebService.reto_AgregaFactura);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    private class ActualizarUbicacion extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface

        public ActualizarUbicacion(AsyncResponse asyncResponse) {
            delegate = asyncResponse;
        }

        @Override
        protected Void doInBackground(String... strings) {
            WebService.ActualizarUbicacion( params, "Viajes/ActualizarUbicacion.php" );
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

    private class TraerItems extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface

        public TraerItems(AsyncResponse asyncResponse) {
            delegate = asyncResponse;
        }
        @Override
        protected Void doInBackground(String... strings) {
            WebService.TraerListaItems( params, "Viajes/TraerItems.php" );
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
                Intent myIntent = new Intent(contexto, Login.class);
                startActivity(myIntent);
            }
        }
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
        /*@Override
        public void onPreExecute() {
            dialog1.setMessage(getResources().getString( R.string.cargando_dialog));
            dialog1.show();
        }
        @Override
        protected void onPostExecute(Void result) {
            if(dialog1.isShowing()) {
                dialog1.dismiss();
            }
            delegate.processFinish( WebService.logueado);
        }*/
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        this.requestWindowFeature( FEATURE_NO_TITLE);
        try {
            setContentView(R.layout.activity_remito);
            WebService.retoRemito = "";
            ListaTextViews = new ArrayList<>();
            contexto = this;
            GuardarDatosUsuario.Contexto = contexto;
            Utilidad = new Utilidades(contexto);
            if (WebService.USUARIOLOGEADO != null) {
            /*mWebView = new WebView( this );
            mWebView.setWebViewClient( new WebViewClient() );
            mWebView.getSettings().setJavaScriptEnabled( true );

            mWebView.loadUrl( WEB_URL );
            */
                retorno = findViewById(R.id.solRem);
                atras = findViewById(R.id.btnAtras);
                atras.setClickable(true);
                atras.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Utilidad.vibraticionBotones(contexto);
                        Intent myIntent = new Intent(v.getContext(), Productos.class);
                        startActivity(myIntent);
                    }
                });

                cod_Suc_Tribut = findViewById(R.id.EdtCodSucTribut);
                cod_Suc_Tribut.setInputType(InputType.TYPE_CLASS_NUMBER);
                cod_Fac_Tribut = findViewById(R.id.EdtCodFacTribut);
                cod_Fac_Tribut.setInputType(InputType.TYPE_CLASS_NUMBER);
                cod_Suc_Tribut.setVisibility(View.GONE); //agregado RP 08/05/2019 para no mostrar
                cod_Fac_Tribut.setVisibility(View.GONE); //agregado RP 08/05/2019 para no mostrar
                num_Remito = findViewById(R.id.EdtNumFactu);
                num_Remito.setInputType(InputType.TYPE_CLASS_NUMBER);

                btnRemito = findViewById(R.id.BtnIngRemito);
                btnRemito.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Utilidad.vibraticionBotones(contexto);
                        if (Utilidad.isNetworkAvailable()) {
                            //cod_Suc = cod_Suc_Tribut.getText().toString().trim();
                            params = new RequestParams();
                            String itmsMandar = "";
                            for (int i = 0; WebService.ArrayItemsViaje.size() > i; i++) {
                                Item instenciaItem = WebService.ArrayItemsViaje.get(i);
                                double valor = Math.floor(instenciaItem.getCantidad_entregada() * instenciaItem.getPrecio_Unitario());
                                if (i != WebService.ArrayItemsViaje.size() - 1) {

                                    itmsMandar = itmsMandar +
                                            instenciaItem.getNom_Articulo().trim().replace(',', '.') + "," +
                                            instenciaItem.getCod_Articulo().trim() + "," +
                                            instenciaItem.getCantidad_entregada() + "," +
                                            instenciaItem.getPrecio_Unitario() + "," +
                                            valor + "," +
                                            instenciaItem.getCantidad() + "," +
                                            instenciaItem.getCant() + "," +
                                            instenciaItem.getCod_rec() + "," +
                                            instenciaItem.getObservacion() + "," +
                                            instenciaItem.getReposicion() + "," +
                                            instenciaItem.getCod_uni_vta() + ";";
                                } else {
                                    itmsMandar = itmsMandar +
                                            instenciaItem.getNom_Articulo().trim().replace(',', '.') + "," +
                                            instenciaItem.getCod_Articulo().trim() + "," +
                                            instenciaItem.getCantidad_entregada() + "," +
                                            instenciaItem.getPrecio_Unitario() + "," +
                                            valor + "," +
                                            instenciaItem.getCantidad() + "," +
                                            instenciaItem.getCant() + "," +
                                            instenciaItem.getCod_rec() + "," +
                                            instenciaItem.getObservacion() + "," +
                                            instenciaItem.getReposicion() + "," +
                                            instenciaItem.getCod_uni_vta();
                                }
                            }
                            nro_rem = num_Remito.getText().toString();
                            params.add("nro_remision", num_Remito.getText().toString());
                            params.add("nro_viaje", WebService.viajeSeleccionado.getNumViaje());
                            params.add("nro_orden_entrega", WebService.Entrega_A_Realizar.getNro_Docum());
                            params.add("cod_tit", WebService.Entrega_A_Realizar.getCod_Tit());
                            params.add("cod_suc", WebService.cod_sucu);
                            params.add("cantidad_lineas", String.valueOf(WebService.ArrayItemsViaje.size()));
                            params.add("total", String.valueOf(WebService.TotalFactura));
                            params.add("cod_tit_gestion", WebService.Entrega_A_Realizar.getCod_Tit_Gestion());
                            params.add("usuario", WebService.USUARIOLOGEADO);
                            params.add("lineas", itmsMandar);

                            IngresarRemito task = new IngresarRemito();

                            task.execute();
                            try {
                                task.get();

                                if (!WebService.remito_guardado) {
                                    //retorno.setText( getResources().getString( R.string.RemitoExito ) + "  Retorno remito: " + WebService.retoRemito );
                                    //aca es por no guardooooooooooo
                                    retorno.setText(WebService.retoRemito);
                                } else {
                                    //retorno.setText( WebService.retoRemito );
                                    //WebService.retoRemito=WebService.retoRemito.replace( "\"","" );
                                    if ("ok".equals(WebService.retoRemito)) {
                                        try {
                                            //entregasTraidas.remove( WebService.Entrega_A_Realizar );
                                            Printer pr = new Printer();
                                            PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
                                            pr.setPrMang(printManager);
                                            pr.setContx(contexto);
                                            pr.setValor(String.valueOf(WebService.nro_rem));
                                            pr.setTipo("R");
                                            pr.genarPdf(pr);
                                        } catch (Exception e) {
                                            //System.out.println(e.toString());
                                            e.printStackTrace();
                                           // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        retorno.setText(WebService.retoRemito);
                                    }
                                }
                            } catch (Exception error) {
                                //System.out.println(error.toString());
                                error.printStackTrace();
                                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Utilidad.CargarToastConexion(contexto);
                        }
                    }
                });
                tablaFactura = findViewById(R.id.TablaFactura);

                destino = findViewById(R.id.TextDestino);
                //destino.setText( WebService.Entrega_A_Realizar.getNom_Tit() + " - " + WebService.Entrega_A_Realizar.getNom_Sucursal() );
                destino.setText(WebService.Entrega_A_Realizar.getNom_Sucursal());

                orden = findViewById(R.id.TxtOrden);
                orden.setText(getResources().getString(R.string.OredenProd) + WebService.Entrega_A_Realizar.getNro_Docum());

                pedido = findViewById(R.id.TxtPedido);
                pedido.setText(getResources().getString(R.string.PedidoProd) + WebService.Entrega_A_Realizar.getNro_doc_ref());

                viaje = findViewById(R.id.TxtViaje);
                viaje.setText(getResources().getString(R.string.NumViajeProd) + WebService.viajeSeleccionado.getNumViaje());


                TableLayout tr0 = new TableLayout(this);
                TableRow tr1 = new TableRow(this);
                TableRow tr2 = new TableRow(this);
                for (int i = 0; WebService.ArrayItemsViaje.size() > i; i++) {

                    tr0 = new TableLayout(this);
                    tr1 = new TableRow(this);
                    tr2 = new TableRow(this);
                    TextView nombreItem = new TextView(this);

                    String nombItem = WebService.ArrayItemsViaje.get(i).getNom_Articulo().trim();
                    String codArt = WebService.ArrayItemsViaje.get(i).getCod_Articulo().trim();

                    nombreItem.setText(nombItem + " - " + codArt);
                    nombreItem.setTextSize(15);
                    nombreItem.setTextColor(Color.parseColor("#000000"));
                    nombreItem.setPadding(10, 5, 0, 5);
                    tr0.addView(nombreItem);

                    //Aca cargamos los titulos
                    TextView titCant = new TextView(this);
                    titCant.setText(getResources().getString(R.string.CantidadPrdo));
                    titCant.setTextColor(Color.parseColor("#ffffff"));
                    titCant.setPadding(5, 10, 10, 10);

                    TextView titCantEntreg = new TextView(this);
                    titCantEntreg.setText(getResources().getString(R.string.CantEntProd));
                    titCantEntreg.setTextColor(Color.parseColor("#ffffff"));
                    titCantEntreg.setGravity(Gravity.LEFT);
                    titCantEntreg.setPadding(10, 10, 22, 10);

                    TextView titPrecioUnitario = new TextView(this);
                    titPrecioUnitario.setText(getResources().getString(R.string.UnitProd));
                    titPrecioUnitario.setTextColor(Color.parseColor("#ffffff"));
                    titPrecioUnitario.setPadding(30, 10, 22, 10);
                    titPrecioUnitario.setGravity(Gravity.RIGHT);

                    TextView titIva = new TextView(this);
                    titIva.setText(getResources().getString(R.string.IVAProd));
                    titIva.setTextColor(Color.parseColor("#ffffff"));
                    titIva.setPadding(30, 10, 22, 10);
                    titIva.setGravity(Gravity.RIGHT);

                    TextView titSubTotal = new TextView(this);
                    titSubTotal.setText(getResources().getString(R.string.SubTotProd));
                    titSubTotal.setTextColor(Color.parseColor("#ffffff"));
                    titSubTotal.setGravity(Gravity.CENTER);
                    titSubTotal.setPadding(30, 10, 22, 10);

                    tr1.addView(titCant);
                    tr1.addView(titCantEntreg);
                    tr1.addView(titPrecioUnitario);
                    tr1.addView(titIva);
                    tr1.addView(titSubTotal);
                    tr1.setBackgroundResource(R.color.colorPrimary);
                    //Aca Terminamos de cargar los titulos

                    //Aca cargamos los productos
                    final TextView cantidadPedida = new TextView(this);
                    ListaTextViews.add(cantidadPedida);
                    cantidadPedida.setText(Double.toString(WebService.ArrayItemsViaje.get(i).getCantidad()));
                    cantidadPedida.setGravity(Gravity.LEFT);

                    final TextView cantidadEntregada = new TextView(this);
                    ListaTextViews.add(cantidadEntregada);
                    cantidadEntregada.setText(Double.toString((WebService.ArrayItemsViaje.get(i).getCantidad_entregada())));
                    cantidadEntregada.setInputType(InputType.TYPE_CLASS_NUMBER);
                    cantidadEntregada.setGravity(Gravity.CENTER);

                    final TextView PrecioUnitario = new TextView(this);
                    PrecioUnitario.setText(Utilidad.GenerarFormato((int) Math.floor(WebService.ArrayItemsViaje.get(i).getPrecio_Unitario())));
                    ListaTextViews.add(PrecioUnitario);

                    final TextView subtotal = new TextView(this);
                    ListaTextViews.add(subtotal);


                    TextView iva = new TextView(contexto);
                    ListaTextViews.add(iva);
                    iva.setText("     " + Utilidad.GenerarFormato((int) (Double.parseDouble(WebService.ArrayItemsViaje.get(i).getPorc_Iva()))));
                    iva.setGravity(Gravity.RIGHT);
                    iva.setPadding(5, 0, 5, 0);

                    double sub_tot = Math.floor(WebService.ArrayItemsViaje.get(i).getCantidad_entregada() * WebService.ArrayItemsViaje.get(i).getPrecio_Unitario());

                    subtotal.setText(Utilidad.GenerarFormato((int) sub_tot));
                    subtotal.setGravity(Gravity.CENTER);
                    //Aca cargamos el IVA para mostrar

                    tr2.addView(cantidadPedida);
                    tr2.addView(cantidadEntregada);
                    tr2.addView(PrecioUnitario);
                    tr2.addView(iva);
                    tr2.addView(subtotal);
                    tr2.setTag(i);
                    tablaFactura.addView(tr0);
                    tablaFactura.addView(tr1);
                    tablaFactura.addView(tr2);
                    tablaFactura.setPadding(5,0,5,0);
                    if (WebService.ArrayItemsViaje.size() == i + 1) {
                        TableRow tr01 = new TableRow(this);
                        tr1 = new TableRow(this);
                        tr2 = new TableRow(this);
                        TableRow tr3 = new TableRow(this);
                        TableRow tr4 = new TableRow(this);
                        TableRow tr5 = new TableRow(this);
                        int ind = 1;
                        while (ind <= 4) {
                            TextView relleno = new TextView(this);
                            relleno.setText("");
                            relleno.setPadding(5, 10, 10, 10);
                            tr01.addView(relleno);
                            ind++;
                        }
                        if (ind == 5) {
                            tota = new TextView(this);
                            ListaTextViews.add(tota);
                            tota.setTextColor(Color.parseColor("#ffffff"));
                            tota.setText(getResources().getString(R.string.TotalCaja) + Utilidad.GenerarFormato((int) WebService.TotalFactura));

                            tr01.addView(tota);
                            tr01.setBackgroundResource(R.color.colorPrimary);
                            tablaFactura.addView(tr01);
                            ind = 1;
                            while (ind <= 4) {
                                TextView relleno = new TextView(this);
                                relleno.setText("");
                                relleno.setPadding(5, 10, 10, 10);
                                tr1.addView(relleno);
                                ind++;
                            }
                            if (ind == 5) {
                                totaPedi = new TextView(this);
                                ListaTextViews.add(totaPedi);
                                totaPedi.setTextColor(Color.parseColor("#ffffff"));
                                totaPedi.setText(getResources().getString(R.string.TotPedInicial) + Utilidad.GenerarFormato2(WebService.TotalPedido));
                                tr1.addView(totaPedi);
                                tr1.setBackgroundResource(R.color.colorPrimary);
                                tablaFactura.addView(tr1);
                            }
                            ind = 1;
                            while (ind <= 4) {
                                TextView relleno = new TextView(this);
                                relleno.setText("");
                                relleno.setTextColor(Color.parseColor("#ffffff"));
                                relleno.setPadding(5, 10, 10, 10);
                                tr2.addView(relleno);
                                ind++;
                            }
                            if (ind == 5) {
                                iva5 = new TextView(this);
                                ListaTextViews.add(iva5);
                                iva5.setTextColor(Color.parseColor("#ffffff"));
                                iva5.setText(getResources().getString(R.string.ImpuestoIVA5) + Utilidad.GenerarFormato((int) WebService.IVA5));
                                tr2.addView(iva5);
                                tr2.setBackgroundResource(R.color.Iva);
                                tablaFactura.addView(tr2);
                            }
                            ind = 1;
                            while (ind <= 4) {
                                TextView relleno = new TextView(this);
                                relleno.setText("");
                                relleno.setTextColor(Color.parseColor("#ffffff"));
                                relleno.setPadding(5, 10, 10, 10);
                                tr3.addView(relleno);
                                ind++;
                            }
                            if (ind == 5) {
                                iva10 = new TextView(this);
                                ListaTextViews.add(iva10);
                                iva10.setTextColor(Color.parseColor("#ffffff"));
                                iva10.setText(getResources().getString(R.string.ImpusetoIVA10) + Utilidad.GenerarFormato((int) WebService.IVA10));
                                tr3.addView(iva10);
                                tr3.setBackgroundResource(R.color.Iva);
                                tablaFactura.addView(tr3);
                            }
                            ind = 1;
                            while (ind <= 4) {
                                TextView relleno = new TextView(this);
                                relleno.setText("");
                                relleno.setTextColor(Color.parseColor("#ffffff"));
                                relleno.setPadding(5, 10, 10, 10);
                                tr4.addView(relleno);
                                ind++;
                            }
                            if (ind == 5) {
                                TotalIVA = new TextView(this);
                                ListaTextViews.add(TotalIVA);
                                TotalIVA.setTextColor(Color.parseColor("#ffffff"));
                                int valorIVA5 = (int) WebService.IVA5;
                                int valorIVA10 = (int) WebService.IVA10;
                                TotalIVA.setText(getResources().getString(R.string.TotIVA) + Utilidad.GenerarFormato((valorIVA5 + valorIVA10)));
                                tr4.addView(TotalIVA);
                                tr4.setBackgroundResource(R.color.Iva);
                                tablaFactura.addView(tr4);
                            }
                            ind = 1;
                            while (ind <= 4) {
                                TextView relleno = new TextView(this);
                                relleno.setText("");
                                relleno.setTextColor(Color.parseColor("#ffffff"));
                                relleno.setPadding(5, 10, 10, 10);
                                tr5.addView(relleno);
                                ind++;
                            }
                            if (ind == 5) {
                                TotSinIVA = new TextView(this);
                                ListaTextViews.add(TotSinIVA);
                                TotSinIVA.setTextColor(Color.parseColor("#ffffff"));
                                TotSinIVA.setText(getResources().getString(R.string.TotSinIVAProd) + Utilidad.GenerarFormato(((int) WebService.TotalFactura - ((int) WebService.IVA5 + (int) WebService.IVA10))));
                                tr5.addView(TotSinIVA);
                                tr5.setBackgroundResource(R.color.colorPrimary);
                                tablaFactura.addView(tr5);
                            }


                        }

                    }

                }
            } else {
                Intent myIntent = new Intent(contexto, Productos.class);
                startActivity(myIntent);
            }
            Utilidad.SetFontSizeTextView(ListaTextViews, this);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }//cierra oncreate
    @Override
    public void onBackPressed() {
        try {
            Utilidad.vibraticionBotones(contexto);
            Intent myIntent = new Intent(contexto, Productos.class);
            startActivity(myIntent);
            finish();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}

