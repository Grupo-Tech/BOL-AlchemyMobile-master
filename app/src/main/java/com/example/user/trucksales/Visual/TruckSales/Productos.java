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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.Entrega;
import com.example.user.trucksales.Encapsuladoras.Item;
import com.example.user.trucksales.Negocio.DecimalDigitsInputFilter;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.InputFilterMinMax;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.dialog.Dialog_Reclamo;
import com.example.user.trucksales.dialog.Dialog_Reclamo.NoticeDialogListenerNormal;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.trucksales.Datos.WebService.WEB_URL;
import static com.example.user.trucksales.Datos.WebService.entregasTraidas;

public class Productos extends Activity implements NoticeDialogListenerNormal {

    protected static RequestParams params1= new RequestParams();
    public TextView orden, pedido, viaje, cliente,destino, iva10, iva5, descuento, TotSinIVA, totaPedi, tota, TotalIVA;
    TableLayout tablaFactura;
    TableLayout tablaFactura2;
    ImageView atras;
    List<EditText> LstCantEntregadas = new ArrayList<>();
    List<EditText> LstDtoLinea = new ArrayList<>();
    List<Double> LstPreciosUnitarios = new ArrayList<>();
    List<Double> LstIva = new ArrayList<>();
    private static double Total, TotalSinIva, IVA5, IVA10, DESCUENTO;
    private static int TotalEntrega;
    private static int posicion = 0;
    private static String ordenSiguiente;
    Context contexto;
    Button btnFactura, btnRemito, btnSiguienteOE;
    double neto;
    int UMBotones;
    private ArrayList<TextView> ListaTextViews;
    private Utilidades Utilidad;

    String cantidad = "";
    String cod = "";
    String observ = "";
    String repo = "";

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

    private class TraerReclamos extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog(contexto);
        public AsyncResponse delegate = null;//Call back interface

        public TraerReclamos(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {
            if (Utilidad.isNetworkAvailable()) {
                RequestParams params2 = new RequestParams();
                WebService.TraerReclamos("Consultas/TraerReclamos.php");
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
        setContentView(R.layout.activity_productos);
        contexto = this;
        GuardarDatosUsuario.Contexto = contexto;
        Utilidad = new Utilidades(contexto);
        ListaTextViews = new ArrayList<>();
        UMBotones = 0;
        try{
        if (WebService.USUARIOLOGEADO != null) {
            Total = 0;
            IVA5 = 0;
            DESCUENTO = 0;
            IVA10 = 0;
            WebService.Gravada5 = 0;
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

            atras = findViewById(R.id.btnAtras);
            atras.setClickable(true);
            atras.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        Utilidad.vibraticionBotones(contexto);
                        WebService.EstadoActual = 1;
                        Intent myIntent = new Intent(v.getContext(), Recorrido_Viaje.class);
                        myIntent.putExtra("intent", "TruckSales");
                        startActivity(myIntent);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            tablaFactura = findViewById(R.id.TablaFactura);
            tablaFactura2 = findViewById(R.id.TablaFactura2);

            cliente = findViewById(R.id.TextCliente);
            cliente.setText(WebService.Entrega_A_Realizar.getNom_Tit());

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
                            tablaFactura2.removeAllViews();
                            Total = 0;
                            IVA5 = 0;
                            DESCUENTO = 0;
                            IVA10 = 0;
                            TotalSinIva=0;
                            LstCantEntregadas.clear();
                            LstDtoLinea.clear();
                            orden.setText(getResources().getString(R.string.OredenProd) + WebService.Entrega_A_Realizar.getNro_Docum());
                            CargarDatosProductos();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                    task.execute();

                }
            });
            //aca cargar los datos
            CargarBotones();
            CargarDatosProductos();
        }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void CargarDatosProductos() {
        try {

            DESCUENTO = WebService.Entrega_A_Realizar.getDescuento();
            TableLayout tr0;// = new TableLayout( this );
            TableRow tr1 = new TableRow(this);
            TableRow tr2; // = new TableRow( this );
            //List<Integer> valoresSubTot = new ArrayList<Integer>();
            for (int i = 0; WebService.ArrayItemsViaje.size() > i; i++) {
                final Item instaItem = WebService.ArrayItemsViaje.get(i);
                //final Integer indiceInterno = i;
                tr0 = new TableLayout(this);
                TableRow tr001 = new TableRow(this);
                //TableRow tr002 = new TableRow( this );
                tr1 = new TableRow(this);
                tr2 = new TableRow(this);

                TextView nombreItem = new TextView(this);
                //TextView codSucu = new TextView( this );

                String nombItem = WebService.ArrayItemsViaje.get(i).getNom_Articulo().trim() + "(" + WebService.ArrayItemsViaje.get(i).getCod_uni_vta() + ")";
                String codArt = WebService.ArrayItemsViaje.get(i).getCod_Articulo().trim();

                nombreItem.setText(nombItem + " - " + codArt);
                nombreItem.setTextSize(15);
                nombreItem.setTextColor(Color.parseColor("#000000"));

                tr001.addView(nombreItem);
                tr0.addView(tr001);
                //Aca cargamos los titulos
                TextView titCant = new TextView(this);
                titCant.setText(getResources().getString(R.string.CantidadPrdo));
                titCant.setTextColor(Color.parseColor("#ffffff"));
                //titCant.setPadding(5, 10, 5, 10);
                titCant.setPadding(5, 10, 10, 10);

                TextView titCantEntreg = new TextView(this);
                titCantEntreg.setText(getResources().getString(R.string.CantEntProd));
                titCantEntreg.setTextColor(Color.parseColor("#ffffff"));
                titCantEntreg.setGravity(Gravity.LEFT);
                //titCantEntreg.setPadding(30, 10, 5, 10);
                titCantEntreg.setPadding(10, 10, 22, 10);

                TextView titDtoLinea = new TextView(this);
                titDtoLinea.setText(getResources().getString(R.string.Dto));
                titDtoLinea.setTextColor(Color.parseColor("#ffffff"));
                //titPrecioUnitario.setPadding(30, 10, 5, 10);
                titDtoLinea.setPadding(30, 10, 22, 10);
                titDtoLinea.setGravity(Gravity.RIGHT);

                TextView titPrecioUnitario = new TextView(this);
                titPrecioUnitario.setText(getResources().getString(R.string.UnitProd));
                titPrecioUnitario.setTextColor(Color.parseColor("#ffffff"));
                //titPrecioUnitario.setPadding(30, 10, 5, 10);
                titPrecioUnitario.setPadding(30, 10, 22, 10);
                titPrecioUnitario.setGravity(Gravity.RIGHT);

                TextView titIva = new TextView(this);
                titIva.setText(getResources().getString(R.string.IVAProd));
                titIva.setTextColor(Color.parseColor("#ffffff"));
                //titIva.setPadding(30, 10, 5, 10);
                titIva.setPadding(30, 10, 22, 10);
                titIva.setGravity(Gravity.RIGHT);

                TextView titSubTotal = new TextView(this);
                titSubTotal.setText(getResources().getString(R.string.SubTotProd));
                titSubTotal.setTextColor(Color.parseColor("#ffffff"));
                titSubTotal.setGravity(Gravity.RIGHT);
                // titSubTotal.setPadding(30, 10, 0, 10);
                titSubTotal.setPadding(30, 10, 22, 10);

                TextView titReclamo = new TextView(this);
                titReclamo.setText(getResources().getString(R.string.SubTotRec));
                titReclamo.setTextColor(Color.parseColor("#ffffff"));
                titReclamo.setGravity(Gravity.LEFT);
                titReclamo.setPadding(30, 10, 5, 10);

                tr1.addView(titCant);
                tr1.addView(titCantEntreg);
                if(WebService.configuracion.getDto_linea().equals("S")){
                    tr1.addView(titDtoLinea);
                }
                tr1.addView(titPrecioUnitario);
                if(WebService.configuracion.getIva().equals("S")){
                    tr1.addView(titIva);
                }

                tr1.addView(titSubTotal);
                tr1.addView(titReclamo);
                tr1.setBackgroundResource(R.color.colorPrimary);
                // tr1.setPadding(5,0,5,0);
                //Aca Terminamos de cargar los titulos

                //Aca cargamos los productos
                final TextView cantidadPedida = new TextView(this);


                cantidadPedida.setText(Utilidad.redondearDecimalesNew(WebService.ArrayItemsViaje.get(i).getCantidad(), Integer.valueOf(WebService.configuracion.getDec_cant())));
                cantidadPedida.setGravity(Gravity.LEFT);
                cantidadPedida.setPadding(5, 10, 5, 10);
                ListaTextViews.add(cantidadPedida);

                final EditText cantidadEntregada = new EditText(this);
                cantidadEntregada.setText(Utilidad.redondearDecimalesNew(WebService.ArrayItemsViaje.get(i).getCantidad(), Integer.valueOf(WebService.configuracion.getDec_cant())));
                cantidadEntregada.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                cantidadEntregada.setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));
                cantidadEntregada.setGravity(Gravity.CENTER);
                ListaTextViews.add(cantidadEntregada);

                final EditText dtoLinea = new EditText(this);
                dtoLinea.setText(String.valueOf(WebService.ArrayItemsViaje.get(i).getPorc_desc()));
                dtoLinea.setEnabled(false);
                //dtoLinea.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                dtoLinea.setInputType(InputType.TYPE_CLASS_NUMBER);
               // dtoLinea.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "100")});
                dtoLinea.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(3,2)});
                dtoLinea.setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));
                dtoLinea.setGravity(Gravity.CENTER);
                ListaTextViews.add(dtoLinea);

                final TextView PrecioUnitario = new TextView(this);
                //int preUnit = (int) WebService.ArrayItemsViaje.get(i).getPrecio_Unitario();
                //PrecioUnitario.setText(Utilidad.GenerarFormato((preUnit)));
                //LstPreciosUnitarios.add(Double.valueOf(preUnit));
                PrecioUnitario.setText(Utilidad.redondearDecimalesNew(WebService.ArrayItemsViaje.get(i).getPrecio_Unitario(), Integer.valueOf(WebService.configuracion.getDec_montomn())));
                LstPreciosUnitarios.add(Double.valueOf(WebService.ArrayItemsViaje.get(i).getPrecio_Unitario()));
                PrecioUnitario.setGravity(Gravity.RIGHT);
                ListaTextViews.add(PrecioUnitario);

                final TextView subtotal = new TextView(this);
                subtotal.setGravity(Gravity.LEFT);
                ListaTextViews.add(subtotal);

                LstCantEntregadas.add(cantidadEntregada);
                LstDtoLinea.add(dtoLinea);

                TextView iva = new TextView(this);
                ListaTextViews.add(iva);
                double valorIva = Double.valueOf(WebService.ArrayItemsViaje.get(i).getPorc_Iva());
                int iv = (int) valorIva;
                if(WebService.configuracion.getIva().equals("S")){
                    iva.setText("     " + Utilidad.GenerarFormato(iv));
                }else{
                    iva.setText("");
                }

                iva.setGravity(Gravity.RIGHT);
                iva.setPadding(5, 0, 5, 0);
                if(iva.getText().toString().equals("")){
                    LstIva.add(0D);
                }else{
                    LstIva.add(Double.valueOf(iva.getText().toString()));
                }


                float stcant = (float) WebService.ArrayItemsViaje.get(i).getCantidad();
                float desc =Float.parseFloat(dtoLinea.getText().toString());
                float punit = (float) WebService.ArrayItemsViaje.get(i).getPrecio_Unitario();

                final double sub_tot = (double) (Float.valueOf(stcant) * (Float.valueOf(punit)- (Float.valueOf(desc)*Float.valueOf(punit)/100)));

                //final double sub_tot = Math.floor(WebService.ArrayItemsViaje.get(i).getCantidad() * WebService.ArrayItemsViaje.get(i).getPrecio_Unitario());
                //int s_tot = (int) sub_tot;
                //subtotal.setText(Utilidad.GenerarFormato(s_tot));
                subtotal.setText(Utilidad.redondearDecimalesNew(sub_tot, Integer.valueOf(WebService.configuracion.getDec_montomn())));
                subtotal.setGravity(Gravity.RIGHT);
                //Aca cargamos el IVA para mostrar
                if (Double.parseDouble(WebService.ArrayItemsViaje.get(i).getPorc_Iva()) == 5.000) {
                    neto = sub_tot / 1.05;
                    IVA5 = IVA5 + (neto * 0.05);
                    WebService.Gravada5 = WebService.Gravada5 + sub_tot;
                } else {
                    neto = sub_tot / 1.10;
                    IVA10 = IVA10 + (neto * 0.10);
                    WebService.Gravada10 = WebService.Gravada10 + sub_tot;
                }

                final ImageView recl = new ImageView(contexto);
                recl.setImageResource(R.drawable.addclaim);
                recl.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                recl.setPadding(15, 15, 0, 0);

                recl.getLayoutParams().height = 55;
                recl.getLayoutParams().width = 55;
                recl.requestLayout();

                recl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TraerReclamos task = new TraerReclamos(new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) {
                                if (!WebService.errToken.equals("")) {
                                    Intent myIntent = new Intent(contexto, Login.class);
                                    startActivity(myIntent);
                                } else {
                                    WebService.instaItem = instaItem;
                                    openDialogReclamo();
                                }
                            }
                        });
                        task.execute();
                    }
                });

                final double porIva = Double.valueOf(WebService.ArrayItemsViaje.get(i).getPorc_Iva());
                final double precioUni = WebService.ArrayItemsViaje.get(i).getPrecio_Unitario();

                TextWatcher textWatcherSubtotal = new TextWatcher() {
                    double cantidadAnterior;
                    double lastNonCeroNumber;

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        if (!(s.equals(null))) {
                            try {

                                if (Double.valueOf(s.toString()) > Double.valueOf(cantidadPedida.getText().toString()) * 1.10) {
                                    cantidadAnterior = Double.valueOf(cantidadPedida.getText().toString());
                                } else {
                                    cantidadAnterior = s.toString().equals("") ? 0 : Double.valueOf(s.toString());
                                }
                                if (cantidadAnterior != 0.0) {
                                    lastNonCeroNumber = cantidadAnterior;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                //Toast.makeText( getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG ).show();
                            }
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        try {
                            if (!cantidadEntregada.getText().equals("")) {

                                String str = cantidadEntregada.getText().toString();
                                if (str.isEmpty()) return;
                                String str2 = Utilidad.PerfectDecimal(str, 1000, 2);
                                if (!str2.equals(str)) {
                                    cantidadEntregada.setText(str2);
                                    int pos = cantidadEntregada.getText().length();
                                    cantidadEntregada.setSelection(pos);
                                }

                                if(!dtoLinea.getText().equals("")){
                                    if( Float.parseFloat(dtoLinea.getText().toString())>100)dtoLinea.setText("100");
                                    if( Float.parseFloat(dtoLinea.getText().toString())<0)dtoLinea.setText("0");
                                }else{
                                    dtoLinea.setText("0");
                                }

                                //MODIFICADO POR PEDIDO 13/06/2019
                               /* if (Double.valueOf( cantidadEntregada.getText().toString() ) > Double.valueOf( cantidadPedida.getText().toString() ) * 1.10) {
                                    cantidadEntregada.setText( cantidadPedida.getText().toString() );
                                } else {*/
                                float stcant = Float.parseFloat(cantidadEntregada.getText().toString());
                                float desc =Float.parseFloat(dtoLinea.getText().toString());
                                float punit = Float.parseFloat(PrecioUnitario.getText().toString());
                            /*System.out.println(stcant);
                            System.out.println(punit);
                            System.out.println(stcant * punit);*/

                                double subtot = (double) (Float.valueOf(stcant) * (Float.valueOf(punit)- (Float.valueOf(desc)*Float.valueOf(punit)/100)));
                                //int subtot = (int) (Double.valueOf(cantidadEntregada.getText().toString()) * Double.valueOf(Utilidad.NumeroSinPunto(PrecioUnitario.getText().toString())));
                                subtotal.setText(Utilidad.redondearDecimalesNew(subtot, Integer.valueOf(WebService.configuracion.getDec_montomn())));

                                //subtotal.setText(Utilidad.GenerarFormato(subtot));
                                double cantidadActual = Double.valueOf(cantidadEntregada.getText().toString());
                                double multiplicador = cantidadActual == 0 && cantidadAnterior == 0 ? cantidadActual - lastNonCeroNumber : cantidadActual - cantidadAnterior;
                                lastNonCeroNumber = 0;

                            /*TotalSinIva = Total;
                            System.out.println(TotalSinIva);
                            TotalSinIva += multiplicador * precioUni;
                            System.out.println(multiplicador * precioUni);

                            if (porIva == 5.000) {
                                neto = multiplicador * precioUni / 1.05;
                                IVA5 += Double.valueOf(neto * 0.05);
                                WebService.IVA5 = IVA5;
                                WebService.Gravada5 += multiplicador * precioUni;
                            } else {
                                neto = multiplicador * precioUni / 1.10;
                                IVA10 += (Double.valueOf(neto * 0.10));
                                WebService.IVA10 = IVA10;
                                WebService.Gravada10 = IVA10;
                            }*/
                                Total = 0;
                                TotalSinIva = 0;
                                IVA5 = 0;
                                IVA10 = 0;
                                WebService.Gravada5 = 0;
                                for (int i = 0; i < LstCantEntregadas.size(); i++) {
                                    TotalSinIva += (Double.valueOf(LstCantEntregadas.get(i).getText().toString()) * (Double.valueOf(LstPreciosUnitarios.get(i).toString())- (Double.valueOf(LstDtoLinea.get(i).getText().toString())*Double.valueOf(LstPreciosUnitarios.get(i).toString())/100)));
                                    if (Double.parseDouble(WebService.ArrayItemsViaje.get(i).getPorc_Iva()) == 5.000) {
                                        neto = Double.valueOf(LstCantEntregadas.get(i).getText().toString()) * (Double.valueOf(LstPreciosUnitarios.get(i).toString())- (Double.valueOf(LstDtoLinea.get(i).getText().toString())*Double.valueOf(LstPreciosUnitarios.get(i).toString())/100)) / 1.05;
                                        IVA5 = IVA5 + Double.valueOf(neto * 0.05);
                                        WebService.IVA5 = IVA5;
                                        WebService.Gravada5 = WebService.Gravada5 + Double.valueOf(LstCantEntregadas.get(i).getText().toString()) * (Double.valueOf(LstPreciosUnitarios.get(i).toString())- (Double.valueOf(LstDtoLinea.get(i).getText().toString())*Double.valueOf(LstPreciosUnitarios.get(i).toString())/100));
                                    } else {
                                        neto = Double.valueOf(LstCantEntregadas.get(i).getText().toString()) * (Double.valueOf(LstPreciosUnitarios.get(i).toString())- (Double.valueOf(LstDtoLinea.get(i).getText().toString())*Double.valueOf(LstPreciosUnitarios.get(i).toString())/100)) / 1.10;
                                        IVA10 = IVA10 + (Double.valueOf(neto * 0.10));
                                        WebService.IVA10 = IVA10;
                                        WebService.Gravada10 = IVA10;
                                    }
                                }
                                //Aca hay que cargar los valores a la clase webservice
                                Total = TotalSinIva;
                                Total = Double.valueOf(Total - DESCUENTO);
                                WebService.Gravada5 = Double.valueOf(WebService.Gravada5 - DESCUENTO);
                                IVA5 = Double.valueOf(WebService.Gravada5 * 0.05) / 1.05;
                                WebService.TotalFactura = (int) Total;
                                WebService.IVA5 = IVA5;
                                TotalSinIva = (int) Total - ((int) IVA10 + (int) IVA5);
                                int ivaTotal = ((int) IVA5 + (int) IVA10);
                                int total = (int) Total;
                                int iv10 = (int) IVA10;
                                int TotSinIv = (int) TotalSinIva;
                                TotalIVA.setText(getString(R.string.TotIVA) + Utilidad.GenerarFormato(ivaTotal));
                                iva10.setText(getResources().getString(R.string.ImpusetoIVA10) + Utilidad.GenerarFormato(iv10));
                                int iv5 = (int) IVA5;
                                iva5.setText(getResources().getString(R.string.ImpuestoIVA5) + Utilidad.GenerarFormato(iv5));
                                TotSinIVA.setText(getResources().getString(R.string.TotSinIVAProd) + Utilidad.GenerarFormato(TotSinIv));
                                tota.setText(getResources().getString(R.string.TotalCaja) + Utilidad.GenerarFormato(total));
                            }
                            //}
                        } catch (Exception exc) {
                            exc.printStackTrace();
                            // Toast.makeText( getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG ).show();
                        }

                    }
                };

                cantidadEntregada.addTextChangedListener(textWatcherSubtotal);
                dtoLinea.addTextChangedListener(textWatcherSubtotal);

            /*neto = Double.valueOf(neto - DESCUENTO);
            IVA5 = Double.valueOf(neto * 0.05 );*/

                Total = Total + sub_tot;
                tr2.addView(cantidadPedida);
                tr2.addView(cantidadEntregada);
                if(WebService.configuracion.getDto_linea().equals("S")){
                    tr2.addView(dtoLinea);
                }
                tr2.addView(PrecioUnitario);
                if(WebService.configuracion.getIva().equals("S")){
                    tr2.addView(iva);
                }
                tr2.addView(subtotal);
                tr2.addView(recl);
                tr2.setTag(i);
                tablaFactura.addView(tr0);
                tablaFactura.addView(tr1);
                tablaFactura.addView(tr2);
                tablaFactura.setPadding(5, 0, 5, 0);
                if (WebService.ArrayItemsViaje.size() == i + 1) {
                    TableRow tr01 = new TableRow(this);
                    TableRow tr02 = new TableRow(this);
                    //tr1 = new TableRow(this);
                    //tr2 = new TableRow(this);
                    TableRow tr3 = new TableRow(this);
                    TableRow tr4 = new TableRow(this);
                    TableRow tr5 = new TableRow(this);
                    TableRow tr6 = new TableRow(this);
                    TableRow tr7 = new TableRow(this);
               /* int ind = 1;
                while (ind <= 4) {
                    TextView relleno = new TextView(this);
                    relleno.setText("");
                    relleno.setPadding(5, 10, 10, 10);
                    tr01.addView(relleno);
                    ind++;
                }
                if (ind == 5) {*/
                    Total = Total - DESCUENTO;
                    tota = new TextView(this);
                    ListaTextViews.add(tota);
                    tota.setTextColor(Color.parseColor("#ffffff"));
                    tota.setText(getResources().getString(R.string.TotalCaja) + Utilidad.GenerarFormato((int) Total));
                    WebService.totalEntregado = (int) Total;
                    tr01.addView(tota);
                    tr01.setGravity(Gravity.RIGHT);
                    tr01.setPadding(0, 0, 5, 0);
                    tr01.setBackgroundResource(R.color.colorPrimary);
                    tablaFactura2.addView(tr01);
                   /* ind = 1;
                    while (ind <= 4) {
                        TextView relleno = new TextView(this);
                        relleno.setText("");
                        relleno.setPadding(5, 10, 10, 10);
                        tr02.addView(relleno);
                        ind++;
                    }
                    if (ind == 5) {*/
                    totaPedi = new TextView(this);
                    ListaTextViews.add(totaPedi);
                    totaPedi.setTextColor(Color.parseColor("#ffffff"));
                    totaPedi.setText(getResources().getString(R.string.TotPedInicial) + Utilidad.GenerarFormato((int) Total));
                    TotalEntrega = (int) Total;
                    WebService.TotalPedido = TotalEntrega;
                    WebService.TotalFactura = TotalEntrega;
                    tr02.addView(totaPedi);
                    tr02.setGravity(Gravity.RIGHT);
                    tr02.setPadding(0, 0, 5, 0);
                    tr02.setBackgroundResource(R.color.colorPrimary);
                    tablaFactura2.addView(tr02);
                   /* }
                    ind = 1;
                    while (ind <= 4) {
                        TextView relleno = new TextView(this);
                        relleno.setText("");
                        relleno.setTextColor(Color.parseColor("#ffffff"));
                        relleno.setPadding(5, 10, 10, 10);
                        tr6.addView(relleno);
                        ind++;
                    }
                    if (ind == 5) {*/

                    descuento = new TextView(this);
                    ListaTextViews.add(descuento);
                    descuento.setTextColor(Color.parseColor("#ffffff"));
                    descuento.setText(getResources().getString(R.string.Descuento) + Utilidad.GenerarFormato((int) DESCUENTO));
                    tr7.addView(descuento);
                    tr7.setGravity(Gravity.RIGHT);
                    tr7.setPadding(0, 0, 5, 0);
                    tr7.setBackgroundResource(R.color.Iva);
                    tablaFactura2.addView(tr7);

                    WebService.Gravada5 = Double.valueOf(WebService.Gravada5 - DESCUENTO);
                    IVA5 = Double.valueOf(WebService.Gravada5 * 0.05)/1.05;
                    iva5 = new TextView(this);
                    ListaTextViews.add(iva5);
                    iva5.setTextColor(Color.parseColor("#ffffff"));
                    iva5.setText(getResources().getString(R.string.ImpuestoIVA5) + Utilidad.GenerarFormato((int) IVA5));
                    tr6.addView(iva5);
                    tr6.setGravity(Gravity.RIGHT);
                    tr6.setPadding(0, 0, 5, 0);
                    tr6.setBackgroundResource(R.color.Iva);
                    if(WebService.configuracion.getIva().equals("S")){
                        tablaFactura2.addView(tr6);
                    }

                  /*  }
                    ind = 1;
                    while (ind <= 4) {
                        TextView relleno = new TextView(this);
                        relleno.setText("");
                        relleno.setTextColor(Color.parseColor("#ffffff"));
                        relleno.setPadding(5, 10, 10, 10);
                        tr3.addView(relleno);
                        ind++;
                    }
                    if (ind == 5) {*/
                    iva10 = new TextView(this);
                    ListaTextViews.add(iva10);
                    iva10.setTextColor(Color.parseColor("#ffffff"));
                    iva10.setText(getResources().getString(R.string.ImpusetoIVA10) + Utilidad.GenerarFormato((int) IVA10));
                    tr3.addView(iva10);
                    tr3.setGravity(Gravity.RIGHT);
                    tr3.setPadding(0, 0, 5, 0);
                    tr3.setBackgroundResource(R.color.Iva);

                    if(WebService.configuracion.getIva().equals("S")){
                        tablaFactura2.addView(tr3);
                    }

                   /* }
                    ind = 1;
                    while (ind <= 4) {
                        TextView relleno = new TextView(this);
                        relleno.setText("");
                        relleno.setTextColor(Color.parseColor("#ffffff"));
                        relleno.setPadding(5, 10, 10, 10);
                        tr4.addView(relleno);
                        ind++;
                    }
                    if (ind == 5) {*/
                    TotalIVA = new TextView(this);
                    ListaTextViews.add(TotalIVA);
                    TotalIVA.setTextColor(Color.parseColor("#ffffff"));
                    int iv10 = (int) IVA10;
                    int iv5 = (int) IVA5;
                    int Tot = iv5 + iv10;
                    TotalIVA.setText(getResources().getString(R.string.TotIVA) + Utilidad.GenerarFormato(Tot));
                    tr4.addView(TotalIVA);
                    tr4.setGravity(Gravity.RIGHT);
                    tr4.setPadding(0, 0, 5, 0);
                    tr4.setBackgroundResource(R.color.Iva);

                    if(WebService.configuracion.getIva().equals("S")){
                        tablaFactura2.addView(tr4);
                    }

                   /* }
                    ind = 1;
                    while (ind <= 4) {
                        TextView relleno = new TextView(this);
                        relleno.setText("");
                        relleno.setTextColor(Color.parseColor("#ffffff"));
                        relleno.setPadding(5, 10, 10, 10);
                        tr5.addView(relleno);
                        ind++;
                    }
                    if (ind == 5) {*/
                    TotSinIVA = new TextView(this);
                    ListaTextViews.add(TotSinIVA);
                    TotSinIVA.setTextColor(Color.parseColor("#ffffff"));
                    int totSIva = ((int) Total - ((int) IVA5 + (int) IVA10));
                    TotSinIVA.setText(getResources().getString(R.string.TotSinIVAProd) + Utilidad.GenerarFormato(totSIva));
                    tr5.addView(TotSinIVA);
                    tr5.setGravity(Gravity.RIGHT);
                    tr5.setPadding(0, 0, 5, 0);
                    tr5.setBackgroundResource(R.color.colorPrimary);

                    if(WebService.configuracion.getIva().equals("S")){
                        tablaFactura2.addView(tr5);
                    }


                    tablaFactura2.setPadding(5, 0, 5, 0);
                   /* }

                }*/

                }

            }

            if (WebService.ArrayItemsViaje.size() == 0) {
                TextView retor = new TextView(this);
                retor.setText("No Tiene productos para este viaje");
                tr1.addView(retor);
                tablaFactura.addView(tr1);
            } /*else {
            Intent myIntent = new Intent(contexto, Login.class);
            startActivity(myIntent);
        }*/
            Utilidad.SetFontSizeTextView(ListaTextViews, this);
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
                        WebService.ArrayItemsViaje.get(i).setDtoLinea(Double.valueOf(LstDtoLinea.get(i).getText().toString().equals("") || LstDtoLinea.get(i).getText() == null ? "0.0" : LstDtoLinea.get(i).getText().toString()));
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
                                if (!WebService.errToken.equals("")) {
                                    Intent myIntent = new Intent(contexto, Login.class);
                                    startActivity(myIntent);
                                } else {
                                    WebService.IVA5 = IVA5;
                                    //WebService.TotalFactura = WebService.totalEntregado;
                                    WebService.IVA10 = IVA10;
                                    for (int i = 0; i < LstCantEntregadas.size(); i++) {
                                        WebService.ArrayItemsViaje.get(i).setCantidad_entregada(Double.valueOf(LstCantEntregadas.get(i).getText().toString().equals("") || LstCantEntregadas.get(i).getText() == null ? "0.0" : LstCantEntregadas.get(i).getText().toString()));
                                        WebService.ArrayItemsViaje.get(i).setDtoLinea(Double.valueOf(LstDtoLinea.get(i).getText().toString().equals("") || LstDtoLinea.get(i).getText() == null ? "0.0" : LstDtoLinea.get(i).getText().toString()));

                                    }
                                    //AGREGADO 24/06/2019 BDL
                                    WebService.EstadoActual = 4;
                                    GuardarDatosUsuario objeto = new GuardarDatosUsuario(contexto);
                                    objeto.GuardarDatos();

                                    Intent myIntent = new Intent(contexto, factura.class);
                                    myIntent.putExtra("intent", "TruckSales");
                                    startActivity(myIntent);
                                }
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
                            tablaFactura2.removeAllViews();
                            Total = 0;
                            IVA5 = 0;
                            IVA10 = 0;
                            TotalSinIva = 0;
                            LstCantEntregadas.clear();
                            LstDtoLinea.clear();
                            orden.setText(getResources().getString(R.string.OredenProd) + WebService.Entrega_A_Realizar.getNro_Docum());
                            CargarDatosProductos();
                        }
                    });
                    task2.execute();
                }else {
                    Utilidad.CargarToastConexion(contexto);
                }
            }
        });

        if(WebService.Entrega_A_Realizar.getTipo().equals("R")){
            btnFactura.setVisibility(View.GONE);
        }else{
            btnRemito.setVisibility(View.GONE);
        }

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
           if(!WebService.errToken.equals("")){
               Intent myIntent = new Intent(contexto, Login.class);
               startActivity(myIntent);
           }
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
            if(!WebService.errToken.equals("")){
                Intent myIntent = new Intent(contexto, Login.class);
                startActivity(myIntent);
            }
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
    private void openDialogReclamo(/*Item item*/) {

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
    }

    public void onDialogNormalGuardarClick(DialogFragment dialog) {
        FragmentManager fragmentManager = getFragmentManager();
        Dialog_Reclamo newFragment = (Dialog_Reclamo) fragmentManager.findFragmentByTag("dialog");

        try {
            cantidad = String.valueOf(newFragment.cantidadIngresada);
            cod = String.valueOf(newFragment.cod);
            observ = String.valueOf(newFragment.observ);
            repo = String.valueOf(newFragment.repo);

            if (Utilidad.isNetworkAvailable()) {

                for (int i = 0; i < WebService.ArrayItemsViaje.size(); i++) {
                    Item insta = WebService.ArrayItemsViaje.get(i);
                    if (WebService.instaItem.getCod_Articulo().trim().equals(insta.getCod_Articulo().trim())) {
                        insta.setCant(Integer.valueOf(cantidad));
                        insta.setCod_rec(Integer.valueOf(cod));
                        insta.setObservacion(observ);
                        insta.setReposicion(repo);
                        break;
                    }
                }

                newFragment.dismiss();
            } else {
                Utilidad.CargarToastConexion(contexto);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


}
