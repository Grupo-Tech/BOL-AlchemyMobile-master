package com.example.user.trucksales.Visual.TruckSales;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.Entrega;
import com.example.user.trucksales.Encapsuladoras.Item;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.adapter.AdaptadorProductos;
import com.example.user.trucksales.dialog.Dialog_Reclamo;
import com.example.user.trucksales.dialog.Dialog_Reclamo.NoticeDialogListenerNormal;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.trucksales.Datos.WebService.entregasTraidas;

public class Productos2 extends Activity implements NoticeDialogListenerNormal{

    protected static RequestParams params1= new RequestParams();
    public static TextView orden, pedido, viaje, destino, iva10, iva5, TotSinIVA, totaPedi, tota, TotalIVA;
    static TableLayout tablaFactura;
    static TableLayout tablaFactura2;
    ImageView atras;
    List<EditText> LstCantEntregadas = new ArrayList<>();
    List<Double> LstPreciosUnitarios = new ArrayList<>();
    List<Double> LstIva = new ArrayList<>();
    private static double Total, TotalSinIva, IVA5, IVA10;
    private static double TotalEntrega;
    private static int posicion = 0;
    private static String ordenSiguiente;
    static Context contexto;
    Button btnFactura, btnRemito, btnSiguienteOE;
    double neto;
    int UMBotones;
    public static ArrayList<TextView> ListaTextViews;
    private AdaptadorProductos dataAdapterRuta;
    public static Utilidades Utilidad;
    ListView tablaGenerados;

    String cantidad = "";
    String cod = "";
    String observ = "";

    private static ArrayList<Entrega> listaEntregas;

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    private class ObtenerNumRecomendado extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog(contexto);
        public AsyncResponse delegate = null;//Call back interface

        public ObtenerNumRecomendado(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {
            if (Utilidad.isNetworkAvailable()) {
                RequestParams params2 = new RequestParams();
                params2.put("username", WebService.usuarioActual.getNombre());
                WebService.RecomendarNumero("Facturas/RecomendarNumero.php", params2);
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
        protected void onProgressUpdate(Void... values) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_productos2);
        contexto = this;
        GuardarDatosUsuario.Contexto = contexto;
        Utilidad = new Utilidades(contexto);
        ListaTextViews = new ArrayList<>();
        UMBotones = 0;
        try{
            if (WebService.USUARIOLOGEADO != null) {
                Total = 0;
                IVA5 = 0;
                IVA10 = 0;

                //COMIENZA AGREGADO
                btnSiguienteOE = findViewById(R.id.BtnSiguienteOE);

                try {
                    ordenSiguiente = WebService.Entrega_A_Realizar.getNro_Docum();
                    listaEntregas = new ArrayList<>();
                    ArrayList<Entrega> Lista = new ArrayList<>();
                    for (int i = 0; i < entregasTraidas.size(); i++) {
                        Lista.add(entregasTraidas.get(i));
                    }
                    for (Entrega objeto : Lista) {
                        if (objeto.getCod_Sucursal().equals(WebService.Entrega_A_Realizar.getCod_Sucursal()) && objeto.getCod_Tit().equals(WebService.Entrega_A_Realizar.getCod_Tit())) {
                            listaEntregas.add(objeto);
                        }
                    }
                    if (listaEntregas.size() == 1) {
                        btnSiguienteOE.setVisibility(View.GONE);
                    } else {
                        btnSiguienteOE.setVisibility(View.VISIBLE);
                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText( getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG ).show();
                }
                //FIN AGREGADO

                tablaGenerados = (ListView) findViewById(R.id.tabla);
                if (WebService.ArrayItemsViaje.size() == 0) {
                    final TextView mensaje = new TextView(contexto);
                    TableRow tr1 = new TableRow(this);
                    mensaje.setText("No Tiene productos para este viaje");
                    tablaGenerados.addView(tr1);
                }else {
                    //for(int i=0; i<WebService.ArrayItemsViaje.size();i++) {
                        dataAdapterRuta = new AdaptadorProductos(Productos2.this, WebService.ArrayItemsViaje);
                        // Desplegamos los elementos en el ListView
                        //tablaGenerados.removeAllViews();
                        tablaGenerados.setAdapter(dataAdapterRuta);
                        dataAdapterRuta.notifyDataSetChanged();
                    //}
                }

                atras = findViewById(R.id.btnAtras);
                atras.setClickable(true);
                atras.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Utilidad.vibraticionBotones( contexto );
                        WebService.EstadoActual = 1;

                        WebService.ivaTotal = 0;
                        WebService.total = 0;
                        WebService.iv10 = 0;
                        WebService.iv5 = 0;
                        WebService.TotSinIv = 0;
                        WebService.Total = 0D;
                        WebService.TotalFactura = 0;

                        Intent myIntent = new Intent(v.getContext(), Recorrido_Viaje.class);
                        myIntent.putExtra("intent", "TruckSales");
                        startActivity(myIntent);
                    }
                });
                tablaFactura = findViewById(R.id.TablaFactura);
                //tablaFactura2 = findViewById(R.id.TablaFactura2);
                destino = findViewById(R.id.TextDestino);
                //destino.setText( WebService.direccion.replace( ",",",\n                    " ));
                //destino.setText( WebService.Entrega_A_Realizar.getNom_Tit() + " - " + WebService.Entrega_A_Realizar.getNom_Sucursal());
                destino.setText(WebService.Entrega_A_Realizar.getNom_Sucursal());

                orden = findViewById(R.id.TxtOrden);
                orden.setText(getResources().getString(R.string.OredenProd) + /*WebService.Entrega_A_Realizar.getCod_Tit()*/WebService.Entrega_A_Realizar.getNro_Docum());
                pedido = findViewById(R.id.TxtPedido);
                pedido.setText(getResources().getString(R.string.PedidoProd) + WebService.Entrega_A_Realizar.getNro_doc_ref());
                viaje = findViewById(R.id.TxtViaje);
                viaje.setText(getResources().getString(R.string.NumViajeProd) + WebService.viajeSeleccionado.getNumViaje());

                //AGREGADO 13/06/2019
                final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

                swipeRefreshLayout.setColorSchemeColors(Color.BLACK);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeRefreshLayout.setRefreshing(true);
                        CargarProductos task = new CargarProductos(new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) {

                                tablaFactura.removeAllViews();
                                //tablaFactura2.removeAllViews();
                                Total = 0;
                                IVA5 = 0;
                                IVA10 = 0;
                                TotalSinIva=0;
                                LstCantEntregadas.clear();
                                orden.setText(getResources().getString(R.string.OredenProd) + WebService.Entrega_A_Realizar.getNro_Docum());

                               // for(int i=0; i<WebService.ArrayItemsViaje.size();i++) {
                                    dataAdapterRuta = new AdaptadorProductos(Productos2.this, WebService.ArrayItemsViaje);
                                    // Desplegamos los elementos en el ListView
                                    tablaGenerados.setAdapter(dataAdapterRuta);
                                   // tablaGenerados.removeAllViews();
                                    dataAdapterRuta.notifyDataSetChanged();
                               // }

                                //CargarDatosProductos();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                        task.execute();

                    }
                });
                //aca cargar los datos
                CargarBotones();
                //CargarDatosProductos();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void CargarDatosProductos() {
        try {
            TableRow tr01 = new TableRow(contexto);
            TableRow tr02 = new TableRow(contexto);
            TableRow tr3 = new TableRow(contexto);
            TableRow tr4 = new TableRow(contexto);
            TableRow tr5 = new TableRow(contexto);
            TableRow tr6 = new TableRow(contexto);

            tota = new TextView(contexto);
            ListaTextViews.add(tota);
            tota.setTextColor(Color.parseColor("#ffffff"));
            tota.setText(contexto.getResources().getString(R.string.TotalCaja) + Utilidad.GenerarFormato2(WebService.Total));
            WebService.totalEntregado = WebService.Total;
            tr01.addView(tota);
            tr01.setGravity(Gravity.RIGHT);
            tr01.setPadding(0, 0, 5, 0);
            tr01.setBackgroundResource(R.color.colorPrimary);
            tablaFactura.addView(tr01);

            totaPedi = new TextView(contexto);
            ListaTextViews.add(totaPedi);
            totaPedi.setTextColor(Color.parseColor("#ffffff"));
            totaPedi.setText(contexto.getResources().getString(R.string.TotPedInicial) + Utilidad.GenerarFormato2(WebService.Total));
            TotalEntrega = WebService.Total;
            WebService.TotalPedido = TotalEntrega;
            WebService.TotalFactura = TotalEntrega;
            tr02.addView(totaPedi);
            tr02.setGravity(Gravity.RIGHT);
            tr02.setPadding(0, 0, 5, 0);
            tr02.setBackgroundResource(R.color.colorPrimary);
            tablaFactura.addView(tr02);

            IVA5 = WebService.IVA5;
            iva5 = new TextView(contexto);
            ListaTextViews.add(iva5);
            iva5.setTextColor(Color.parseColor("#ffffff"));
            iva5.setText(contexto.getResources().getString(R.string.ImpuestoIVA5) + Utilidad.GenerarFormato((int) IVA5));
            tr6.addView(iva5);
            tr6.setGravity(Gravity.RIGHT);
            tr6.setPadding(0, 0, 5, 0);
            tr6.setBackgroundResource(R.color.Iva);
            tablaFactura.addView(tr6);

            IVA10 = WebService.IVA10;
            iva10 = new TextView(contexto);
            ListaTextViews.add(iva10);
            iva10.setTextColor(Color.parseColor("#ffffff"));
            iva10.setText(contexto.getResources().getString(R.string.ImpusetoIVA10) + Utilidad.GenerarFormato((int) IVA10));
            tr3.addView(iva10);
            tr3.setGravity(Gravity.RIGHT);
            tr3.setPadding(0, 0, 5, 0);
            tr3.setBackgroundResource(R.color.Iva);
            tablaFactura.addView(tr3);

            TotalIVA = new TextView(contexto);
            ListaTextViews.add(TotalIVA);
            TotalIVA.setTextColor(Color.parseColor("#ffffff"));
            int iv10 = (int) WebService.IVA10;
            int iv5 = (int) WebService.IVA5;
            int Tot = iv5 + iv10;
            TotalIVA.setText(contexto.getResources().getString(R.string.TotIVA) + " " + Utilidad.GenerarFormato(Tot));
            tr4.addView(TotalIVA);
            tr4.setGravity(Gravity.RIGHT);
            tr4.setPadding(0, 0, 5, 0);
            tr4.setBackgroundResource(R.color.Iva);
            tablaFactura.addView(tr4);

            TotSinIVA = new TextView(contexto);
            ListaTextViews.add(TotSinIVA);
            TotSinIVA.setTextColor(Color.parseColor("#ffffff"));
            double totSIva = (WebService.Total - ((int) WebService.IVA5 + (int) WebService.IVA10));
            TotSinIVA.setText(contexto.getResources().getString(R.string.TotSinIVAProd) + Utilidad.GenerarFormato2(totSIva));
            tr5.addView(TotSinIVA);
            tr5.setGravity(Gravity.RIGHT);
            tr5.setPadding(0, 0, 5, 0);
            tr5.setBackgroundResource(R.color.colorPrimary);
            tablaFactura.addView(tr5);

            Utilidad.SetFontSizeTextView(ListaTextViews, contexto);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void CargarBotones() {
        try{
            btnRemito = findViewById(R.id.BtnRemito);
            btnRemito.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Utilidad.vibraticionBotones( contexto );
                    if (Utilidad.isNetworkAvailable()) {
                        // ObtenerNumRecomendado task =
                        WebService.IVA5 = IVA5;
                        String totalRem = tota.getText().toString().replace(getResources().getString(R.string.TotalCaja), "");
                        WebService.TotalFactura = Double.valueOf(Utilidad.NumeroSinPunto(totalRem));
                        WebService.IVA10 = IVA10;
                        for (int i = 0; i < LstCantEntregadas.size(); i++) {
                            WebService.ArrayItemsViaje.get(i).setCantidad_entregada(Double.valueOf(LstCantEntregadas.get(i).getText().toString().equals( "" ) || LstCantEntregadas.get(i).getText() == null ? "0.0" : LstCantEntregadas.get(i).getText().toString()));
                        }
                        Intent myIntent = new Intent(v.getContext(), Remito.class);
                        startActivity(myIntent);
                    } else {
                        Utilidad.CargarToastConexion(contexto);
                    }

                }
            });
            btnFactura = findViewById(R.id.BtnFactura);
            btnFactura.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Utilidad.vibraticionBotones( contexto );
                    if (Utilidad.isNetworkAvailable()) {
                        try {
                            ObtenerNumRecomendado task = new ObtenerNumRecomendado(new AsyncResponse() {
                                public void processFinish(Object output) {
                                    WebService.IVA5 = IVA5;
                                    //WebService.TotalFactura = WebService.totalEntregado;
                                    WebService.IVA10 = IVA10;
                                    for (int i = 0; i < LstCantEntregadas.size(); i++) {
                                        WebService.ArrayItemsViaje.get(i).setCantidad_entregada(Double.valueOf(LstCantEntregadas.get(i).getText().toString().equals("") || LstCantEntregadas.get(i).getText() == null ? "0.0" : LstCantEntregadas.get(i).getText().toString()));
                                    }
                                    //AGREGADO 24/06/2019 BDL
                                    WebService.EstadoActual = 4;
                                    GuardarDatosUsuario objeto = new GuardarDatosUsuario(contexto);
                                    objeto.GuardarDatos();

                                    Intent myIntent = new Intent(contexto, factura.class);
                                    myIntent.putExtra("intent", "TruckSales");
                                    startActivity(myIntent);
                                }
                            });
                            task.execute();
                        }catch (Exception ex){
                            ex.printStackTrace();
                            //  Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                        }
                    }else {
                        Utilidad.CargarToastConexion(contexto);
                    }
                }
            });

            btnSiguienteOE.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utilidad.vibraticionBotones( contexto );
                    if (Utilidad.isNetworkAvailable()) {
                        posicion = posicion+1;
                        if (listaEntregas.size() > 1) {
                            if(listaEntregas.size() == posicion)
                            {
                                posicion = 0;
                            }
                            ordenSiguiente = listaEntregas.get(posicion).getNro_Docum();
                            WebService.Entrega_A_Realizar = listaEntregas.get(posicion);
                        }
                        CargarProductos task2 = new CargarProductos(new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) {
                                tablaFactura.removeAllViews();
                                Total = 0;
                                IVA5 = 0;
                                IVA10 = 0;
                                TotalSinIva = 0;
                                LstCantEntregadas.clear();
                                orden.setText(getResources().getString(R.string.OredenProd) + WebService.Entrega_A_Realizar.getNro_Docum());

                                for(int i=0; i<WebService.ArrayItemsViaje.size();i++) {
                                    dataAdapterRuta = new AdaptadorProductos(Productos2.this, WebService.ArrayItemsViaje);
                                    // Desplegamos los elementos en el ListView
                                    tablaGenerados.setAdapter(dataAdapterRuta);
                                    dataAdapterRuta.notifyDataSetChanged();
                                }
                                //CargarDatosProductos();
                            }
                        });
                        task2.execute();
                    }else {
                        Utilidad.CargarToastConexion(contexto);
                    }
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private class CargarProductos extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog(contexto);
        public AsyncResponse delegate = null;//Call back interface

        public CargarProductos(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {
            params1 = new RequestParams(  );
            params1.put("num_Viaje", Integer.valueOf(WebService.viajeSeleccionado.getNumViaje()));
            //String prueba = WebService.Entrega_A_Realizar.getNro_Docum().trim();
            params1.put("num_Orden", Integer.valueOf(ordenSiguiente));
            params1.put("cod_Sucursal", WebService.Entrega_A_Realizar.getCod_Sucursal());
            String pru = WebService.cod_sucu;

            WebService.TraerListaItems(params1, "Viajes/TraerItems.php");
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

    private class TraerItems extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            WebService.TraerListaItems( params1, "Productos/TraerItems.php" );
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }
    @Override
    public void onBackPressed() {
        Utilidad.vibraticionBotones( contexto );
        try{
            if (WebService.USUARIOLOGEADO != null) {
                WebService.EstadoActual = 1;
                Intent myIntent = new Intent( contexto, Recorrido_Viaje.class );
                myIntent.putExtra("intent", "TruckSales");
                startActivity( myIntent );
                finish();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void openDialogReclamo() {
        try {
            FragmentManager fragmentManager = getFragmentManager();
            Dialog_Reclamo newFragment = new Dialog_Reclamo();

            Bundle bundle = new Bundle();

            newFragment.setArguments(bundle);

            //mIsLargeLayout = getResources().getBoolean(R.bool.large_layout)
            Boolean mIsLargeLayout = true;
            if (mIsLargeLayout) {
                // The device is using a large layout, so show the fragment as a dialog
                newFragment.show(fragmentManager, "dialog");
            } else {
                // The device is smaller, so show the fragment fullscreen
                android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                // For a little polish, specify a transition animation
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                // To make it fullscreen, use the 'content' root view as the container
                // for the fragment, which is always the root view for the activity
                transaction.add(android.R.id.content, newFragment)
                        .addToBackStack(null).commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onDialogNormalGuardarClick(DialogFragment dialog) {
        FragmentManager fragmentManager = getFragmentManager();
        Dialog_Reclamo newFragment = (Dialog_Reclamo) fragmentManager.findFragmentByTag("dialog");

        try {
            cantidad = String.valueOf(newFragment.cantidadIngresada);
            cod = String.valueOf(newFragment.cod);
            observ = String.valueOf(newFragment.observ);

            if (Utilidad.isNetworkAvailable()) {

                for (int i = 0; i < WebService.ArrayItemsViaje.size(); i++) {
                    Item insta = WebService.ArrayItemsViaje.get(i);
                    if (WebService.instaItem.getCod_Articulo().trim().equals(insta.getCod_Articulo().trim())) {
                        insta.setCant(Integer.valueOf(cantidad));
                        insta.setCod_rec(Integer.valueOf(cod));
                        insta.setObservacion(observ);
                        break;
                    }
                }
                newFragment.dismiss();
            } else {
                Toast toast = Toast.makeText(contexto, getResources().getString(R.string.ErrorConexion), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                toast.show();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
