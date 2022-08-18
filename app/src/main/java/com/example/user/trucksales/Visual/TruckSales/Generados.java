package com.example.user.trucksales.Visual.TruckSales;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.print.PrintManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.FacturaItemZebra;
import com.example.user.trucksales.Encapsuladoras.FacturaXDia;
import com.example.user.trucksales.Encapsuladoras.FacturaZebra;
import com.example.user.trucksales.Encapsuladoras.Item;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Printer;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.Visual.Pedidos.Pedidos;
import com.example.user.trucksales.dialog.Dialog_Reclamo;
import com.loopj.android.http.RequestParams;
import com.example.user.trucksales.dialog.Dialog_Reclamo_Fact;
import com.example.user.trucksales.dialog.Dialog_Reclamo_Fact.NoticeDialogListenerNormal;
import com.szsicod.print.io.BluetoothAPI;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.BluetoothConnectionInsecure;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class Generados extends Activity implements NoticeDialogListenerNormal  {
    public static TableLayout tablaGenerados;
    private static Context contextG;
    private static RequestParams params1;
    TextView nombreUsuario, txtFecha, holaUsu;
    WebView mWebView;
    private static ImageView flecha;
    public static String valorIntent;
    private Utilidades Utilidad;
    public static boolean Anulado = false;
    private static ImageView pedido;
    public int anterior = 0;
    public int actual = 0;

    String cod = "";
    String observ = "";
    String tipo_documento = "";
    int nrotrans = 0;

    private Spinner spinnerBTDeviceList;
    private ArrayList<String> mBTAddrList = new ArrayList<String>();

    private Connection printerConnection;
    private EditText edit;
    private String Nro_tran;

    @SuppressLint("WrongConstant")
    private void openDialogReclamo(/*Item item*/) {

        FragmentManager fragmentManager = getFragmentManager();
        Dialog_Reclamo_Fact newFragment = new Dialog_Reclamo_Fact();

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

    @Override
    public void onDialogNormalGuardarClick(android.app.DialogFragment dialog) {
        FragmentManager fragmentManager = getFragmentManager();
        Dialog_Reclamo_Fact newFragment = (Dialog_Reclamo_Fact) fragmentManager.findFragmentByTag("dialog");

        try {
            cod = String.valueOf(newFragment.cod);
            observ = String.valueOf(newFragment.observ);

            if (Utilidad.isNetworkAvailable()) {

                //LLAMAR AL SERVICIO
                params1 = new RequestParams();
                //n_fac.getNro_Trans() n_fac.getTipo_docum()
                params1.put( "nrotrans", nrotrans);
                params1.put( "username", WebService.USUARIOLOGEADO );
                params1.put( "tipo_docum", tipo_documento);
                params1.put( "cod_tiporeclamo", cod);
                params1.put( "obs_reclamo", observ);

                GuardarReclamo task = new GuardarReclamo(new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        if (!WebService.errToken.equals("")) {
                            Intent myIntent = new Intent(contextG, Login.class);
                            startActivity(myIntent);
                        } else {
                            String mensaje = "";
                            if(WebService.reto_AgregarReclamo.equals("ok")){
                                mensaje = "Se ha guardado el reclamo";
                            }else{
                                mensaje = WebService.reto_AgregarReclamo;
                            }
                            Toast.makeText(Generados.this, mensaje, Toast.LENGTH_LONG).show();
                        }
                    }
                });
                task.execute();

                newFragment.dismiss();
            } else {
                Utilidad.CargarToastConexion(contextG);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    private class GuardarReclamo extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contextG );
        public AsyncResponse delegate = null;//Call back interface

        public GuardarReclamo(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {
            if (Utilidad.isNetworkAvailable()) {

                WebService.IngresarReclamo( params1, "Facturas/IngresarReclamo.php" );
                return null;

            } else {
                Toast toast = Toast.makeText( getApplicationContext(), getResources().getString( R.string.ErrorConexion ), Toast.LENGTH_SHORT );
                toast.setGravity( Gravity.CENTER, 0, 0 ); // last two args are X and Y are used for setting position
                toast.setDuration( Toast.LENGTH_LONG );//you can even use milliseconds to display toast
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
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class AnularFactura extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contextG );
        public AsyncResponse delegate = null;//Call back interface

        public AnularFactura(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {
            // vj = new Viajes();
            if (Utilidad.isNetworkAvailable()) {

                WebService.AnularFactura( params1, "Facturas/AnularFactura.php" );
                return null;

            } else {
                Toast toast = Toast.makeText( getApplicationContext(), getResources().getString( R.string.ErrorConexion ), Toast.LENGTH_SHORT );
                toast.setGravity( Gravity.CENTER, 0, 0 ); // last two args are X and Y are used for setting position
                toast.setDuration( Toast.LENGTH_LONG );//you can even use milliseconds to display toast
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
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class TraerTipoCajas extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contextG );
        public AsyncResponse delegate = null;//Call back interface

        public TraerTipoCajas(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {
            // vj = new Viajes();
            if (Utilidad.isNetworkAvailable()) {
                params = new String[0];
                WebService.TraerTipoCajas( "Consultas/Cajas.php" );
                return null;

            } else {
                Toast toast = Toast.makeText( getApplicationContext(), getResources().getString( R.string.ErrorConexion ), Toast.LENGTH_SHORT );
                toast.setGravity( Gravity.CENTER, 0, 0 ); // last two args are X and Y are used for setting position
                toast.setDuration( Toast.LENGTH_LONG );//you can even use milliseconds to display toast
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
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class TreaerInfoCajas extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contextG );
        public AsyncResponse delegate = null;//Call back interface

        public TreaerInfoCajas(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {
            WebService.ObtengoSucursal( "Envases/ValidacionEnvase.php", params1 );
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
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        //System.out.println( "Entro dentro de generados" );
        try {
            setContentView( R.layout.activity_generados );
            contextG = this;
            Utilidad = new Utilidades( contextG );
            try {
                if (WebService.USUARIOLOGEADO != null) {

                    try {
                        valorIntent = getIntent().getStringExtra("intent");
                    }catch (Exception ex){
                        ex.printStackTrace();
                        // Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                    }

                    pedido = findViewById( R.id.btnPedidos );
                    pedido.setVisibility(View.GONE);
                    if(WebService.usuarioActual.getEs_Pedidos().equals("S")){
                        pedido.setVisibility(View.VISIBLE);
                        pedido.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Utilidad.vibraticionBotones(contextG);
                                    Intent myIntent = new Intent(v.getContext(), Pedidos.class);
                                    myIntent.putExtra("intent", "Generados");
                                    startActivity(myIntent);
                                }catch (Exception ex){
                                    ex.printStackTrace();
                                }
                            }
                        });
                    }

                    flecha = findViewById( R.id.btnAtras );
                    flecha.setClickable( true );
                    flecha.setOnClickListener( new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                Utilidad.vibraticionBotones(contextG);
                                Intent myIntent = new Intent(v.getContext(), Consultas.class);
                                myIntent.putExtra("intent", valorIntent);
                                startActivity(myIntent);
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    } );
                    holaUsu = (TextView) findViewById( R.id.Rotulo );
                    holaUsu.setText( getResources().getString( R.string.Hola ) + " " + WebService.USUARIOLOGEADO + " " + getResources().getString( R.string.Tienes ) + " " + WebService.listafacturas.size() + " " + getResources().getString( R.string.FacturasGen ) + " " + getResources().getString( R.string.y ) + " " + WebService.listaRemitos.size() + " " + getResources().getString( R.string.RemitoGen ) );


                    edit = (EditText) findViewById(R.id.edit);

                    /* IMPRESORA */
                    spinnerBTDeviceList = (Spinner) findViewById(R.id.spinner_bt_device_list);

                    BluetoothAPI io = null;
                    io = new BluetoothAPI(this);
                    mBTAddrList.clear();
                    if (io.isBTSupport() == true && io.openBluetooth(15000)) {
                        Set<BluetoothDevice> setL = io.getBondedDevices();
                        if (setL != null && false == setL.isEmpty()) {
                            for (BluetoothDevice bd : setL) {
                                mBTAddrList.add(bd.getAddress());
                            }
                        }
                    }
                    if (mBTAddrList.isEmpty() == true) {
                        mBTAddrList.add("NONE");
                        Toast.makeText(this, "La impresora no está configurada. No podrá imprimir el comprobante.", Toast.LENGTH_LONG).show();
                    }
                    ArrayAdapter<String> adapterTemp = null;
                    adapterTemp = new ArrayAdapter<String>(this,	android.R.layout.simple_spinner_item,mBTAddrList);
                    adapterTemp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerBTDeviceList.setAdapter(adapterTemp);

                    tablaGenerados = findViewById( R.id.tabla );
                    // FacturaXDia n_fac = new FacturaXDia();
                    nombreUsuario = findViewById( R.id.nombreUsuario );
                    nombreUsuario.setText( getResources().getString( R.string.usuarioTool ) + WebService.USUARIOLOGEADO );

                    String timeStamp = new SimpleDateFormat( "dd-MM-yyyy" ).format( Calendar.getInstance().getTime() );
                    txtFecha = (TextView) findViewById( R.id.Fecha );
                    txtFecha.setText( timeStamp );

                    TableRow tr1 = new TableRow( this );
                    TableRow tr2 = new TableRow( this );
                    if (WebService.listaRemitos.size() == 0 && WebService.listafacturas.size() == 0) {
                        final TextView mensaje = new TextView( contextG );
                        mensaje.setText( "No existen registros de la fecha actual de facturas o remitos" );
                        tablaGenerados.addView( tr1 );
                    }
                    if (WebService.listafacturas.size() != 0) {
                        for (int i = 0; WebService.listafacturas.size() > i; i++) {
                            try {
                                final FacturaXDia n_fac = WebService.listafacturas.get( i );
                                final TableRow trBoton = new TableRow( contextG );
                                trBoton.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT ) );
                                tr1.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT ) );
                                tr2.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, 20 ) );
                                tr1 = new TableRow( contextG );
                                tr2 = new TableRow( contextG );

                                final TextView cliente = new TextView( contextG );

                                ImageView cajita = new ImageView( contextG );
                                final TextView espacio = new TextView( contextG );
                                espacio.setText( "\n" );
                                cajita.setImageResource( R.drawable.box );
                                cajita.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT ) );
                                cajita.getLayoutParams().height = 100;
                                cajita.getLayoutParams().width = 100;
                                cajita.setClickable( true );
                                cajita.requestLayout();
                                if(WebService.usuarioActual.getEs_VentaDirecta().equals("S")){
                                    cajita.setVisibility(View.GONE);
                                }

                                cliente.setText(    getResources().getString( R.string.clienteGenerados ) + " " + n_fac.getNom_Tit().trim() + "\n" +
                                                    getResources().getString( R.string.SucursalGenerados ) + " " + n_fac.getSucursal().trim() + "\n" +
                                                    getResources().getString( R.string.ViajesGen ) + " " + n_fac.getCod_Deposito().trim() + "\n" +
                                                    getResources().getString( R.string.OEnt ) + " " + n_fac.getNro_Doc_Ref().trim() + "\n" +
                                                    getResources().getString( R.string.NroFact ) + " " + n_fac.getCod_Suc_Tribut().trim() + "  -  " + n_fac.getCod_Fac_Tribut().trim() + "  -  " + n_fac.getNro_Docum().trim() + "\n" +
                                                    getResources().getString( R.string.HroGen ) + " " + n_fac.getHora().trim() + "\n" +
                                                    n_fac.getTipo_docum().toUpperCase());
                                cliente.setTextSize( 15 );
                                cliente.setBackgroundResource( R.color.viajes );

                                final ImageView anular = new ImageView( contextG );

                                //anular.setText( "Anular" );
                                anular.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT ) );
                                anular.setImageResource( R.drawable.cancelar );
                                anular.getLayoutParams().height = 100;
                                anular.getLayoutParams().width = 100;
                                anular.setClickable( true );
                                anular.setOnClickListener( new View.OnClickListener() {
                                    public void onClick(View v) {
                                        Utilidad.vibraticionBotones( contextG );
                                        if (Utilidad.isNetworkAvailable()) {
                                            final TableRow tRow = (TableRow) v.getParent();
                                            int i = 0;
                                            TableRow tablaActual = (TableRow) tablaGenerados.getChildAt( i );
                                            while (tRow != tablaActual) {
                                                i++;
                                                tablaActual = (TableRow) tablaGenerados.getChildAt( i );
                                            }
                                            anterior = i - 1;
                                            actual = i;
                                            params1 = new RequestParams();
                                            params1.put( "nrotrans", n_fac.getNro_Trans() );
                                            params1.put( "username", WebService.USUARIOLOGEADO );
                                            params1.put( "tipo_docum", n_fac.getTipo_docum());

                                            displayAlertEliminar();

                                            //Intent myIntent = new Intent(Generados.this, Consultas.class );
                                            //startActivity( myIntent );
                                            //Toast.makeText( Generados.this, "La factura ha sido eliminada", Toast.LENGTH_SHORT ).show();
                                            //salir

                                        } else {
                                            Utilidad.CargarToastConexion( contextG );
                                        }
                                    }
                                } );
                                anular.requestLayout();

                                final ImageView reclamo = new ImageView( contextG );
                                reclamo.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT ) );
                                reclamo.setImageResource( R.drawable.addclaim );
                                reclamo.getLayoutParams().height = 100;
                                reclamo.getLayoutParams().width = 100;
                                reclamo.setClickable( true );
                                reclamo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        nrotrans = Integer.parseInt(n_fac.getNro_Trans().trim());
                                        tipo_documento = n_fac.getTipo_docum().trim();
                                        TraerReclamos task = new TraerReclamos(new AsyncResponse() {
                                            @Override
                                            public void processFinish(Object output) {
                                                if (!WebService.errToken.equals("")) {
                                                    Intent myIntent = new Intent(contextG, Login.class);
                                                    startActivity(myIntent);
                                                } else {
                                                    //WebService.instaItem = instaItem;
                                                    openDialogReclamo();
                                                }
                                            }
                                        });
                                        task.execute();
                                        //n_fac.getNro_Trans()
                                    }
                                });

                                TableRow.LayoutParams Parametros = new TableRow.LayoutParams();
                                Parametros.weight = (float) 0.5;
                                Parametros.width = 0;
                                cliente.setLayoutParams( Parametros );
                                tr1.addView( cliente );
                                //tr1.addView( cajita );
                                trBoton.addView( cajita );
                                trBoton.addView( anular );;
                                trBoton.addView( reclamo );
                                tr1.setClickable( true );
                                tr1.setOnClickListener( new View.OnClickListener() {
                                    public void onClick(View v) {
                                        /*
                                        // IMPRIMIO SOLO DATOS VISTOS POR PANTALLA
                                        imprimirZebra(n_fac);
                                        */

                                        try {
                                            edit.setText("inicia");
                                            FacturaXDia f1 = new FacturaXDia();
                                            f1 = n_fac;
                                            Nro_tran=f1.getNro_Trans().trim();
                                            GenerarHtmlZebra task = new GenerarHtmlZebra();
                                            task.execute();
                                            TimeUnit Unidad = TimeUnit.MILLISECONDS;
                                            task.get(80000, Unidad);


                                        } catch (Exception ex) {
                                            System.out.println(ex.toString());
                                            Toast.makeText( contextG, "Error: " + ex.toString(), Toast.LENGTH_LONG ).show();
                                        }

                                        // IMPRIMIO DATOS DEL weBSERVICES
                                        /*
                                        Utilidad.vibraticionBotones( contextG );
                                        if (Utilidad.isNetworkAvailable()) {
                                            FacturaXDia f1 = new FacturaXDia();
                                            f1 = n_fac;

                                            PrintManager printManager = (PrintManager) getSystemService( Context.PRINT_SERVICE );
                                            Printer pr = new Printer();
                                            pr.setPrMang( printManager );
                                            pr.setContx( contextG );
                                            pr.setValor( f1.getNro_Trans().trim() );
                                            pr.setTipo( f1.getTipo().trim() );
                                            FacturaZebra item=pr.genarPdfZebra( pr );



                                        } else {
                                            Utilidad.CargarToastConexion( contextG );
                                        }
                                        */
                                    }
                                } );
                                tr2.addView( espacio );

                                tr1.setPadding( 0, 20, 0, 30 );
                                tr1.setBackgroundResource( R.color.viajes );
                                trBoton.setBackgroundResource( R.color.viajes );
                                tr2.setBackgroundResource( R.color.blanco );
                                //tr1.setClickable( true );
                                cajita.setOnClickListener( new View.OnClickListener() {
                                    public void onClick(View v) {
                                        Utilidad.vibraticionBotones( contextG );
                                        String mac = mBTAddrList.get(spinnerBTDeviceList.getSelectedItemPosition());
                                        String s = "Test MAC: "+mac;
                                        edit.setText(s);
                                        printTest(s,mac);
                                        /*if (Utilidad.isNetworkAvailable()) {
                                            params1 = new RequestParams();
                                            params1.add( "cod_tit", n_fac.getCod_Tit().trim() );
                                            params1.add( "cod_suc", n_fac.getCod_Sucursal().trim() );
                                            params1.add( "nro_trans", n_fac.getNro_Trans().trim() );
                                            params1.add( "user", WebService.USUARIOLOGEADO );
                                            WebService.nro_trans = Integer.valueOf( n_fac.getNro_Trans().trim() );
                                            WebService.nFac.setCod_Sucursal( n_fac.getCod_Sucursal().trim() );
                                            WebService.nFac.setNro_Trans( n_fac.getNro_Trans().trim() );
                                            WebService.nFac.setCod_Tit( n_fac.getCod_Tit().trim() );
                                            WebService.nFac.setCod_Deposito(n_fac.getCod_Deposito().trim()); //Se modifico cambio del 07/5/2019

                                            //WebService.viajeSeleccionado.setNumViaje( n_fac.getCod_Deposito().trim() ); //agregado07/05/2019

                                            TreaerInfoCajas task = new TreaerInfoCajas( new AsyncResponse() {
                                                public void processFinish(Object output) {
                                                    if (!WebService.errToken.equals("")) {
                                                        Intent myIntent = new Intent(contextG, Login.class);
                                                        startActivity(myIntent);
                                                    } else {
                                                        //si ya existe el movimiento lo tiene que traer para imprimir
                                                        TraerTipoCajas task2 = new TraerTipoCajas(new AsyncResponse() {
                                                            @Override
                                                            public void processFinish(Object output) {
                                                                Intent myIntent = new Intent(contextG, CajasTraidas.class);
                                                                startActivity(myIntent);
                                                            }
                                                        });
                                                        task2.execute();
                                                    }
                                                }
                                            } );
                                            task.execute();
                                        } else {
                                            Utilidad.CargarToastConexion( contextG );
                                        }
                                        */

                                    }
                                } );
                                tablaGenerados.addView( tr1 );
                                tablaGenerados.addView( trBoton );
                                tablaGenerados.addView( tr2 );
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                            }
                        }
                    }
                    if (WebService.listaRemitos.size() != 0) {
                        for (int i = 0; WebService.listaRemitos.size() > i; i++) {
                            try {
                                final TableRow trBoton = new TableRow( contextG );
                                trBoton.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT ) );
                                final FacturaXDia n_fac = WebService.listaRemitos.get( i );

                                tr1.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT ) );
                                tr2.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, 20 ) );
                                tr1 = new TableRow( contextG );
                                tr2 = new TableRow( contextG );

                                final TextView cliente = new TextView( contextG );
                                ImageView cajita = new ImageView( contextG );
                                final TextView espacio = new TextView( contextG );
                                espacio.setText( "\n" );
                                cajita.setImageResource( R.drawable.box );
                                cajita.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT ) );
                                cajita.getLayoutParams().height = 150;
                                cajita.getLayoutParams().width = 150;
                                cajita.setClickable( true );
                                cajita.requestLayout();

                                cliente.setText( getResources().getString( R.string.clienteGenerados ) + " " + n_fac.getNom_Tit().trim() + "\n" + getResources().getString( R.string.SucursalGenerados ) + " \n" + n_fac.getSucursal().trim() + "\n" + getResources().getString( R.string.ViajesGen ) + " " + n_fac.getCod_Deposito().trim() + "\n" +
                                        getResources().getString( R.string.OEnt ) + " " + n_fac.getNro_Doc_Ref().trim() + "\n" + getResources().getString( R.string.NroRem ) + n_fac.getNro_Docum().trim() + "\n" + getResources().getString( R.string.HroGen ) + " " + n_fac.getHora().trim() );
                                cliente.setTextSize( 15 );
                                cliente.setBackgroundResource( R.color.viajes );
                                TableRow.LayoutParams Parametros = new TableRow.LayoutParams();
                                Parametros.weight = (float) 0.5;
                                Parametros.width = 0;
                                cliente.setLayoutParams( Parametros );
                                tr1.addView( cliente );
                                trBoton.addView( cajita );
                                trBoton.setBackgroundResource( R.color.viajes );
                                tr1.setClickable( true );
                                tr1.setOnClickListener( new View.OnClickListener() {
                                    public void onClick(View v) {
                                        Utilidad.vibraticionBotones( contextG );
                                        if (Utilidad.isNetworkAvailable()) {

                                            FacturaXDia f1 = n_fac;
                                            PrintManager printManager = (PrintManager) getSystemService( Context.PRINT_SERVICE );
                                            Printer pr = new Printer();
                                            pr.setPrMang( printManager );
                                            pr.setContx( contextG );
                                            pr.setValor( f1.getNro_Trans().trim() );
                                            pr.setTipo( f1.getTipo().trim() );
                                            pr.genarPdf( pr );
                                        } else {
                                            Utilidad.CargarToastConexion( contextG );
                                        }

                                    }
                                } );
                                tr2.addView( espacio );

                                tr1.setPadding( 0, 20, 0, 30 );
                                tr1.setBackgroundResource( R.color.viajes );
                                tr2.setBackgroundResource( R.color.blanco );
                                //tr1.setClickable( true );
                                cajita.setOnClickListener( new View.OnClickListener() {
                                    public void onClick(View v) {
                                        Utilidad.vibraticionBotones( contextG );
                                        if (Utilidad.isNetworkAvailable()) {
                                            //System.out.println( "La sucursal guardada es: " + n_fac.getSucursal() );
                                            params1 = new RequestParams();
                                            params1.add( "cod_tit", n_fac.getCod_Tit().trim() );
                                            params1.add( "cod_suc", n_fac.getCod_Sucursal().trim() );
                                            params1.add( "nro_trans", n_fac.getNro_Trans().trim() );
                                            params1.add( "user", WebService.USUARIOLOGEADO );
                                            WebService.nFac.setCod_Sucursal( n_fac.getCod_Sucursal().trim() );
                                            WebService.nFac.setNro_Trans( n_fac.getNro_Trans().trim() );
                                            WebService.nFac.setCod_Tit( n_fac.getCod_Tit().trim() );
                                            WebService.nFac.setNom_Tit( n_fac.getNom_Tit() );
                                            WebService.nFac.setSucursal( n_fac.getSucursal() );

                                            TreaerInfoCajas task = new TreaerInfoCajas( new AsyncResponse() {
                                                public void processFinish(Object output) {
                                                    if (!WebService.errToken.equals("")) {
                                                        Intent myIntent = new Intent(contextG, Login.class);
                                                        startActivity(myIntent);
                                                    } else {
                                                        TraerTipoCajas task2 = new TraerTipoCajas(new AsyncResponse() {
                                                            @Override
                                                            public void processFinish(Object output) {
                                                                Intent myIntent = new Intent(contextG, CajasTraidas.class);
                                                                startActivity(myIntent);

                                                            }
                                                        });
                                                        task2.execute();
                                                    }
                                                }
                                            } );
                                            task.execute();
                                        } else {
                                            Utilidad.CargarToastConexion( contextG );
                                        }
                                    }

                                } );
                                tablaGenerados.addView( tr1 );
                                tablaGenerados.addView( trBoton );
                                tablaGenerados.addView( tr2 );
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                            }
                        }
                    }
                }
            } catch (Exception error) {
                error.printStackTrace();
                //Toast.makeText( getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG ).show();
            }
            GuardarDatosUsuario.Contexto = contextG;
        } catch (Exception error) {
            error.printStackTrace();
           // Toast.makeText( getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }//Cierra el oncreate


    public void imprimirZebra(final FacturaXDia n_fac) {
		/*
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		//final String printerAddress = preferences.getString("blumac", "");
		final String mobileId = preferences.getString("lastId", "");
//		log.i("cobranza_movil", "Reimprimiendo: " + mobileId);
		*/
        if(mBTAddrList.get(spinnerBTDeviceList.getSelectedItemPosition()) != "") {
            //Toast.makeText(this, "Imprimiendo comprobante.", Toast.LENGTH_SHORT).show();
			/*
			final Cliente cliente= Session.getInstance().getCliente();

			String operacion;
			final double monto;
			final double saldo;

			operacion = cliente.getOperacion();
			monto =  Double.valueOf(100);
			saldo = cliente.getSaldo();
			*/

            new Thread(new Runnable() {
                public void run() {
                    //                enableTestButton(false);
                    Looper.prepare();


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //edit.setText(edit.getText()+"\n"+"Comienza proceso " );
                        }
                    });


                    //String s = CobrosController.makePrintingTest(getApplicationContext());
                    //String s = "Es una prueba de impresion";
                    String s =n_fac.getNom_Tit().trim()+" \n"+
                            n_fac.getSucursal().trim() +" \n"+
                            n_fac.getCod_Deposito().trim() +" \n"+
                            n_fac.getNro_Doc_Ref().trim() +" \n"+
                            n_fac.getCod_Suc_Tribut().trim() + "  -  " + n_fac.getCod_Fac_Tribut().trim() + "  -  " + n_fac.getNro_Docum().trim() + " \n"+
                            n_fac.getHora().trim() + " \n"+
                            n_fac.getTipo_docum().toUpperCase()+" \n"
                            ;

                    /*
                    s += "\r\n";
                    s += "\r\n";
                    s += n_fac.getNom_Tit().trim()+"\r\n";
                    s += n_fac.getSucursal().trim() +"\r\n";
                    s += n_fac.getCod_Deposito().trim() +"\r\n";
                    s += n_fac.getNro_Doc_Ref().trim() +"\r\n";
                    s += n_fac.getCod_Suc_Tribut().trim() + "  -  " + n_fac.getCod_Fac_Tribut().trim() + "  -  " + n_fac.getNro_Docum().trim() + "\r\n";
                    s += n_fac.getHora().trim() + "\r\n";
                    s += n_fac.getTipo_docum().toUpperCase()+"\r\n";

                    s += "\r\n";
                    */


                    String mac = mBTAddrList.get(spinnerBTDeviceList.getSelectedItemPosition());
                    //Log.i("cobranza_movil", s);
                    printTest(s,mac);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // edit.setText(edit.getText()+"\n"+"Proceso finalizado. " );
                        }
                    });

                    //Toast.makeText(MainActivity.this, "Comprobante impreso.", Toast.LENGTH_SHORT).show();

                    Looper.loop();
                    Looper.myLooper().quit();
                }
            }).start();


        } else {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // edit.setText(edit.getText()+"\n"+"No hay impresora seleccionada.");
                }
            });

            Toast.makeText(this, "No hay impresora seleccionada.", Toast.LENGTH_LONG).show();
        }
    }

    public void imprimirZebraFactura(final FacturaZebra factura) {
        try {
            //String mac = mBTAddrList.get(spinnerBTDeviceList.getSelectedItemPosition());
            //Connection thePrinterConn = new BluetoothConnectionInsecure(mac);

            //if (!thePrinterConn.isConnected()){
            //   edit.setText(edit.getText()+"\n"+"Impresora sin conexion");
            //}
            if(mBTAddrList.get(spinnerBTDeviceList.getSelectedItemPosition()) != "") {
                new Thread(new Runnable() {
                    public void run() {
                        Looper.prepare();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("Fin run");
                                 edit.setText(edit.getText()+"\n"+"Comienza proceso " );
                            }
                        });
                        String items="";
                        Double suma=0D;
                        for (FacturaItemZebra item:factura.getItems()) {
                            /*items=items +Utilidad.getPalabraNumero(Utilidad.redondearDecimalesNew(Double.valueOf(item.getCant_aprobada()), Integer.valueOf(WebService.configuracion.getDec_cant())),8)+"     "
                                    +Utilidad.getPalabra(item.getNom_articulo(),28)
                                    +Utilidad.getPalabraNumero(Utilidad.redondearDecimalesNew(Double.valueOf(item.getPrecio_iva_inc()), Integer.valueOf(WebService.configuracion.getDec_montomn())),9)+"    "
                                    +Utilidad.getPalabraNumero(Utilidad.redondearDecimalesNew(Double.valueOf(item.getTotal_iva_inc()), Integer.valueOf(WebService.configuracion.getDec_montomn())),8)+"    "
                                    +" \r\n";*/
                            items = items +"    "+  Utilidad.getPalabra(item.getNom_articulo(), 60) + " \r\n"
                                    + Utilidad.getPalabraNumero(Utilidad.redondearDecimalesNew(Double.valueOf(item.getCant_aprobada()), Integer.valueOf(WebService.configuracion.getDec_cant())), 8) + " X "
                                    + Utilidad.getPalabraNumero(Utilidad.redondearDecimalesNew(Double.valueOf(item.getPrecio_iva_inc()), Integer.valueOf(WebService.configuracion.getDec_montomn())), 9) + " - 0.00                         "
                                    + Utilidad.getPalabraNumero(Utilidad.redondearDecimalesNew(Double.valueOf(item.getTotal_iva_inc()), Integer.valueOf(WebService.configuracion.getDec_montomn())), 9) + "    "
                                    + " \r\n";
                            suma=suma+Double.valueOf(item.getTotal_iva_inc());
                        }
                        String mac = mBTAddrList.get(spinnerBTDeviceList.getSelectedItemPosition());
                        final boolean imprime = Utilidad.printFacturaQR(factura, items, suma, mac); //llama a Utilidades
                        System.out.println(imprime);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (imprime){
                                    System.out.println("imprime true");
                                    edit.setText(edit.getText()+"\n"+"Proceso finalizado ok.");
                                }
                                else{
                                    System.out.println("Imprime false");
                                    edit.setText(edit.getText()+"\n"+"Proceso finalizado sin impresion. ");
                                }
                            }
                        });
                        Looper.loop();
                        Looper.myLooper().quit();
                    }
                }).start();
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        edit.setText(edit.getText()+"\n"+"No hay impresora seleccionada.");
                    }
                });
                Toast.makeText(this, "No hay impresora seleccionada.", Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void printTest(String s, String mac) {
        connect(mac);
        try {
            byte[] printerInstructions = s.getBytes("ASCII");
            printerConnection.write(printerInstructions);
        } catch (final ConnectionException e) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                     edit.setText(edit.getText()+"\n"+"Error imprimiendo. " + e.getMessage());
                }
            });

            Log.e("cobranza_movil", "Error imprimiendo. " + e.getMessage());
        } catch (final UnsupportedEncodingException e) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // edit.setText(edit.getText()+"\n"+"Error codificación. " + e.getMessage());
                }
            });

            Log.e("cobranza_movil", "Error de codificación. " + e.getMessage());
        } finally {
            disconnect();
        }
    }



    private ZebraPrinter connect(String mac) {
//        printerConnection = null;
        printerConnection = new BluetoothConnection(mac);

        try {
            printerConnection.open();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //edit.setText(edit.getText()+"\n"+"Abriendo Conección" );
                }
            });

            Log.i("cobranza_movil", "Abriendo Conección");
        } catch (final ConnectionException e) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // edit.setText(edit.getText()+"\n"+"Error de conexión, desconectando.  "+ e.getMessage() );
                }
            });

            Log.e("cobranza_movil", "Error de conexión, desconectando. " + e.getMessage());
            disconnect();
        }

        ZebraPrinter printer = null;

        if (printerConnection.isConnected()) {
            try {
                printer = ZebraPrinterFactory.getInstance(printerConnection);
            } catch (final ConnectionException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //edit.setText(edit.getText()+"\n"+"No se pudo establecer conexión"+ e.getMessage());
                    }
                });

                Log.e("cobranza_movil", "No se pudo establecer conexión");
                printer = null;
                disconnect();
            } catch (final ZebraPrinterLanguageUnknownException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // edit.setText(edit.getText()+"\n"+"No se pudo determinar el lenguaje de la impresora. " + e.getMessage());
                    }
                });

                Log.e("cobranza_movil", "No se pudo determinar el lenguaje de la impresora. " + e.getMessage());
                printer = null;
                disconnect();
            }
        }

        return printer;
    }

    private void disconnect() {
        try {
            Log.i("cobranza_movil", "Desconectando");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // edit.setText(edit.getText()+"\n"+"Desconectando");
                }
            });

            if (printerConnection != null) {
                printerConnection.close();
            }
        } catch (final ConnectionException e) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // edit.setText(edit.getText()+"\n"+"Error de conexión - desconectado. "+ e.getMessage());
                }
            });

            Log.e("cobranza_movil", "Error de conexión - desconectado. " + e.getMessage());
        }
    }



    private void ActualizarLista(int indexDesde,int IndexCantidad) {
        try{
            tablaGenerados.removeViews( indexDesde,IndexCantidad );
        } catch (Exception error) {
            error.printStackTrace();
           // Toast.makeText( getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }

    @Override
    public void onBackPressed() {
        Utilidad.vibraticionBotones( contextG );
        Intent myIntent = new Intent( contextG, Consultas.class );
        myIntent.putExtra("intent", valorIntent);
        startActivity( myIntent );
    }

    protected void displayAlertEliminar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        builder.setMessage(getResources().getString(R.string.ElimFac)).setCancelable(
                false).setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        AnularFactura task = new AnularFactura( new AsyncResponse() {
                            public void processFinish(Object output) {
                                if (!WebService.errToken.equals("")) {
                                    Intent myIntent = new Intent(contextG, Login.class);
                                    startActivity(myIntent);
                                } else {
                                    if (WebService.banderaAnuladas == false) {
                                        Toast.makeText(Generados.this, "La factura no se pudo eliminar", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(Generados.this, "La factura ha sido eliminada", Toast.LENGTH_LONG).show();
                                        Anulado = true;
                                        ActualizarLista(actual - 2, 3);
                                    }
                                }

                                //acá debería cargar de nuevo la vista
                            }
                        });
                        task.execute();
                    }
                }).setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alerta = builder.create();
        alerta.show();
    }

    private class TraerReclamos extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog(contextG);
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


    private class  GenerarHtmlZebra extends AsyncTask<String, Void, FacturaZebra> {
        private boolean generacionCajas;
        @Override
        protected FacturaZebra doInBackground(String... strings) {
            generacionCajas = false;
            RequestParams params = new RequestParams();
            params.add("nrotrans", Nro_tran);
            FacturaZebra item = WebService.GenerarHtmlZebra("Impresion/printfactura.php", params);
            return item;
        }

        @Override
        protected void onPostExecute(FacturaZebra facturaZebra) {
            if (facturaZebra!=null){
                imprimirZebraFactura(facturaZebra);
            } else {
                edit.setText(edit.getText()+"\n"+"Error datos impresion factura");
            }
        }
    }

    public void imprimirZebraFacturaOld(final FacturaZebra factura) {
		/*
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		//final String printerAddress = preferences.getString("blumac", "");
		final String mobileId = preferences.getString("lastId", "");
//		log.i("cobranza_movil", "Reimprimiendo: " + mobileId);
		*/
        if(mBTAddrList.get(spinnerBTDeviceList.getSelectedItemPosition()) != "") {
            //Toast.makeText(this, "Imprimiendo comprobante.", Toast.LENGTH_SHORT).show();
			/*
			final Cliente cliente= Session.getInstance().getCliente();

			String operacion;
			final double monto;
			final double saldo;

			operacion = cliente.getOperacion();
			monto =  Double.valueOf(100);
			saldo = cliente.getSaldo();
			*/

            new Thread(new Runnable() {
                public void run() {
                    //                enableTestButton(false);
                    Looper.prepare();


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            edit.setText(edit.getText()+"\n"+"Comienza proceso " );
                        }
                    });


                    //String s = CobrosController.makePrintingTest(getApplicationContext());
                    //String s = "Es una prueba de impresion";

                    ;

                    String items="";
                    for (FacturaItemZebra item:factura.getItems()) {
                        items=items+ item.getNom_articulo() +" "+item.getCant_aprobada()+" "+item.getPrecio_iva_inc()+" "+item.getTotal_iva_inc()+" \r\n";
                    }

                    String s =factura.getDir_empresa()+" \r\n"+
                            factura.getDir_empresa_aux()+" \r\n"+
                            factura.getTel_empresa()+" \r\n"+
                            factura.getNom_localidad()+" \r\n"+
                            factura.getNom_pais()+" \r\n"+
                            factura.getNro_cuit()+" \r\n"+
                            factura.getNro_docum()+" \r\n"+
                            factura.getN_autorizacion()+" \r\n"+
                            factura.getFec_doc()+" \r\n"+
                            factura.getNro_doc_uni()+" \r\n"+
                            factura.getNom_tit()+" \r\n"+
                            factura.getCod_control_fact()+" \r\n"+
                            factura.getFec_vto_fac()+" \r\n"+
                            factura.getImagen_qr()+" \r\n"+
                            items;
                    ;

                    /*
                    s += "\r\n";
                    s += "\r\n";
                    s += n_fac.getNom_Tit().trim()+"\r\n";
                    s += n_fac.getSucursal().trim() +"\r\n";
                    s += n_fac.getCod_Deposito().trim() +"\r\n";
                    s += n_fac.getNro_Doc_Ref().trim() +"\r\n";
                    s += n_fac.getCod_Suc_Tribut().trim() + "  -  " + n_fac.getCod_Fac_Tribut().trim() + "  -  " + n_fac.getNro_Docum().trim() + "\r\n";
                    s += n_fac.getHora().trim() + "\r\n";
                    s += n_fac.getTipo_docum().toUpperCase()+"\r\n";

                    s += "\r\n";
                    */


                    String mac = mBTAddrList.get(spinnerBTDeviceList.getSelectedItemPosition());
                    //Log.i("cobranza_movil", s);
                    printTest(s,mac);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            edit.setText(edit.getText()+"\n"+"Proceso finalizado. " );
                        }
                    });

                    //Toast.makeText(MainActivity.this, "Comprobante impreso.", Toast.LENGTH_SHORT).show();

                    Looper.loop();
                    Looper.myLooper().quit();
                }
            }).start();


        } else {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    edit.setText(edit.getText()+"\n"+"No hay impresora seleccionada.");
                }
            });

            Toast.makeText(this, "No hay impresora seleccionada.", Toast.LENGTH_LONG).show();
        }
    }


}

