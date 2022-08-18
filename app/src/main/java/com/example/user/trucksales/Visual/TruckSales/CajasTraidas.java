package com.example.user.trucksales.Visual.TruckSales;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.print.PrintManager;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.Caja;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Printer;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EmptyStackException;
import java.util.List;

import static android.view.Window.FEATURE_NO_TITLE;

public class CajasTraidas extends Activity {
    TextView nombreUsuario,txtFecha,txtCliente,txtSucursal,titTipo,titSAnt,titEntrega,titRetira,titSaldo,tx1,tx2,tx3,tx4,tx5;
    TableLayout tablaCajas;
    List<EditText> TotCajasEntregadas =  new ArrayList<>();
    List<EditText> TotCajasRetiradas = new ArrayList<>();
    List<TextView> totSaldoAntetior = new ArrayList<>();
    List<TextView> totSaldo = new ArrayList<>();
    ImageView atras;
    RequestParams params1;
    Context context;
    Button btnAceptar;
    private Utilidades Utilidad;
    public  static LatLng ubicacionActual;
    LocationManager locationManager;
    Criteria criteria;
    public static ArrayList<String> URLDistanciaInicial1 = new ArrayList<>(  );
    private Location location;
    private boolean localizacionBuscada;
    private boolean noNull = false;

    private String ConvertirCaja() {
        String lineas = "";
        // (tipo, cant.ini, entrega, retira,saldo)
        for(int i = 0; WebService.tipoCajas.size()>i; i++) {
            Caja instaCaja = new Caja();
            instaCaja = WebService.tipoCajas.get( i );
            if(i==WebService.tipoCajas.size()-1) {
                lineas = lineas+instaCaja.getId_TipoCaja().trim()+","+instaCaja.getCantidad()+","+instaCaja.getEntrega()+","+instaCaja.getRetira()+","+0;
                if((instaCaja.getCantidad() != 0) || (instaCaja.getEntrega() != 0) || instaCaja.getRetira() != 0){
                    noNull = true;
                }
            } else {
                lineas = lineas+instaCaja.getId_TipoCaja().trim()+","+instaCaja.getCantidad()+","+instaCaja.getEntrega()+","+instaCaja.getRetira()+","+0+";";
                if((instaCaja.getCantidad() != 0) || (instaCaja.getEntrega() != 0) || instaCaja.getRetira() != 0){
                    noNull = true;
                }
            }
        }
        return lineas;
    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    protected class CargarEnvases extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface
        public CargarEnvases(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            if(Utilidad.isNetworkAvailable()) {
                WebService.TraerCajas("Envases/Envases.php",params1);
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

    @Override
    protected void onResume() {
        super.onResume();
        try{
        if (WebService.retoEnvases.equals( "ok" )) {
            if (WebService.entregasTraidas.size() != 0 || WebService.ListaRetiros.size() != 0) {
                WebService.reto_AgregaFactura = "";
                WebService.retoRemito = "";
                WebService.retoEnvases = "";
                WebService.EstadoAnterior = 0;
                SeleccionarViaje();
            } else {
                ActionRetornar();
            }
        }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void ActionRetornar(){
        try{
        WebService.EstadoActual = 3;
        WebService.viajeSeleccionado.setEstado( "3" );
        WebService.ViajeActualSeleccionado = false;
        WebService.LastIndexSelected = 0;
        WebService.IndexViajes = 0;
        final ObtenerCentral Tarea = new ObtenerCentral( new SalirViaje.AsyncResponse(){
            public void processFinish(Object output) {
                Intent NextActivity = new Intent(context,Recorrido_Viaje.class);
                NextActivity.putExtra("intent", "TruckSales");
                startActivity( NextActivity );
            }
        });
        Tarea.execute();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private class ObtenerCentral extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public SalirViaje.AsyncResponse delegate = null;//Call back interface

        public ObtenerCentral(SalirViaje.AsyncResponse asyncResponse) {
            delegate = asyncResponse;
        }

        @Override
        protected Void doInBackground(String... params) {
            if(Utilidad.isNetworkAvailable()) {
                WebService.TraerEntregaMasCercana(URLDistanciaInicial1);
                return null;
            } else {
                Utilidad.dispalyAlertConexion(context);
            }
            return null;
        }

        //MUESTRA MENSAJE DE CARGANDO
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
        this.requestWindowFeature( FEATURE_NO_TITLE );
        setContentView( R.layout.activity_cajas_traidas );

        WebService.retoEnvases = "";
        try{
        if(WebService.USUARIOLOGEADO != null) {
            context = this;
            if(WebService.viajeSeleccionado.getNumViaje() == null){
                for(int i = 0; i<WebService.viajesUsu.size();i++){
                    WebService.viajeSeleccionado = WebService.viajesUsu.get(i);
                    break;
                }
            }
            Utilidad = new Utilidades( context );
            atras = findViewById( R.id.btnAtras );
            atras.setClickable( true );
            atras.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    try{
                    Utilidad.vibraticionBotones( context );
                    if(WebService.ViajeActualSeleccionado) {
                        if (WebService.viajeSeleccionado.getTipo().equals( "entrega" )) {
                            if (WebService.retoEnvases.equals( "" ) && (WebService.reto_AgregaFactura.equals( "ok" ) || WebService.retoRemito.equals( "ok" ))) {
                                WebService.reto_AgregaFactura = "";
                                WebService.retoRemito = "";
                                WebService.retoEnvases = "";
                                SeleccionarViaje();
                                Intent myIntent = new Intent( context, ClienteXDefecto.class );
                                startActivity( myIntent );
                            } else {
                                WebService.retoEnvases = "";
                                Intent myIntent = new Intent( v.getContext(), Generados.class );
                                startActivity( myIntent );
                            }
                        } else if (WebService.viajeSeleccionado.getTipo().equals( "retiro" )) {
                            Intent myIntent = new Intent( context, ClienteXDefecto.class );
                            startActivity( myIntent );
                        }
                    }else {
                        WebService.retoEnvases = "";
                        Intent myIntent = new Intent( v.getContext(), Generados.class );
                        startActivity( myIntent );
                    }
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            } );
            nombreUsuario = findViewById( R.id.nombreUsuario );
            nombreUsuario.setText( getResources().getString( R.string.usuarioTool ) + WebService.USUARIOLOGEADO );

            String timeStamp = new SimpleDateFormat( "dd-MM-yyyy" ).format( Calendar.getInstance().getTime() );
            txtFecha = findViewById( R.id.Fecha );
            txtFecha.setText( timeStamp );

            txtCliente = findViewById( R.id.nombCliente );
            if (WebService.nFac.getNom_Tit() == null) {
                WebService.nFac.setNom_Tit( "" );
            }

            try {
                txtCliente.setText( getResources().getString( R.string.clienteGenerados ) + WebService.nFac.getNom_Tit());
            }catch (Exception e){
                txtCliente.setText( getResources().getString( R.string.clienteGenerados ) + "Error al cargar el nombre del cliente");
                Toast.makeText( context, e.toString(), Toast.LENGTH_SHORT ).show();
            }
            txtCliente.setPaintFlags( txtCliente.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG );

            txtSucursal = findViewById( R.id.sucursal );
            try {
                if (WebService.nFac.getSucursal()!=null) {
                    txtSucursal.setText(getResources().getString(R.string.SucursalGenerados) + WebService.nFac.getSucursal());
                }
            }catch(Exception e){
                txtSucursal.setText( getResources().getString( R.string.SucursalGenerados ) + "Error al cargar el nombre de la sucursal" );
                Toast.makeText( context, e.toString() , Toast.LENGTH_LONG ).show();
            }
            txtSucursal.setPaintFlags( txtSucursal.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG );
            //System.out.println(txtSucursal.getText()+"|");
            btnAceptar = findViewById( R.id.BtnAceptar );
            btnAceptar.setOnClickListener( new View.OnClickListener() {
                public void onClick(final View v) {
                    Utilidad.vibraticionBotones( context );
                    if (Utilidad.isNetworkAvailable()) {
                        ConvertirCaja();
                        if(noNull) {
                            params1 = new RequestParams();
                            params1.add( "cod_tit", WebService.nFac.getCod_Tit().trim() );
                            params1.add( "cod_suc_afec", WebService.nFac.getCod_Sucursal().trim() );
                            params1.add( "nro_trans_asoc", WebService.nFac.getNro_Trans().trim() );
                            params1.add( "user", WebService.USUARIOLOGEADO );
                            params1.add( "nro_viaje", WebService.viajeSeleccionado.getNumViaje());
                            params1.add( "lineas", ConvertirCaja() );
                            //ACA SE GRABA EL MOVIMIENTO DE CAJAS
                            CargarEnvases task = new CargarEnvases( new AsyncResponse() {
                                public void processFinish(Object output) {
                                    if (!WebService.errToken.equals("")) {
                                        Intent myIntent = new Intent(context, Login.class);
                                        startActivity(myIntent);
                                    } else {
                                        //ACA SE MANDA A IMPRIMIR EL NUMERO DE TRANASACCION DEL TIPO ENVASES
                                        if (WebService.retoEnvases.equals("ok")) { //Esto quiere decir que guardo ok la caja
                                            if (WebService.nFac.getNro_Trans() != null && !WebService.nFac.getNro_trans_ref().equals("0")) {

                                                Printer pr = new Printer();
                                                PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
                                                pr.setPrMang(printManager);
                                                pr.setContx(context);
                                                // System.out.println(String.valueOf(WebService.nFac.getNro_trans_ref()));
                                                pr.setValor(String.valueOf(WebService.nFac.getNro_trans_ref()));
                                                pr.setTipo("E");
                                                pr.genarPdf(pr);
                                            } else {
                                                Toast.makeText(context, "Error al grabar datos \n" + WebService.respuestaGuardarCaja, Toast.LENGTH_LONG).show();
                                            }
                                        } else { //si no guardo la caja muestra error y retorna
                                            //Toast.makeText(context, "Error al grabar datos \n" + WebService.respuestaGuardarCaja, Toast.LENGTH_LONG).show();
                                            Toast toast = Toast.makeText(context, "Error al grabar datos \n" + WebService.respuestaGuardarCaja, Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                                            toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                                            toast.show();//showing the toast is important**

                                            onBackPressed();
                                        }
                                    }
                                }
                            } );
                            task.execute();
                        }else{
                            WebService.reto_AgregaFactura ="";
                            WebService.retoRemito = "";
                            WebService.retoEnvases="";
                            SeleccionarViaje();
                        }
                    } else {
                        Toast toast = Toast.makeText( getApplicationContext(), getResources().getString( R.string.ErrorConexion ), Toast.LENGTH_SHORT );
                        toast.setGravity( Gravity.CENTER, 0, 0 ); // last two args are X and Y are used for setting position
                        toast.setDuration( Toast.LENGTH_LONG );//you can even use milliseconds to display toast
                        toast.show();//showing the toast is important**

                    }

                }
            } );

            tablaCajas = findViewById( R.id.tblCajas );

            TableRow tr0 = new TableRow( this );

            titTipo = new TextView( this );
            titSAnt = new TextView( this );
            titEntrega = new TextView( this );
            titRetira = new TextView( this );
            titSaldo = new TextView( this );

            titTipo.setText( getResources().getString( R.string.tipo ) );
            titTipo.setPadding( 10, 15, 30, 0 );
            titTipo.setTextColor( this.getResources().getColor( R.color.blanco ) );
            titSAnt.setText( getResources().getString( R.string.saldoAnterior ) );
            titSAnt.setPadding( 10, 15, 30, 0 );
            titSAnt.setTextColor( this.getResources().getColor( R.color.blanco ) );
            titEntrega.setText( getResources().getString( R.string.EntregaCaja ) );
            titEntrega.setPadding( 10, 15, 30, 0 );
            titEntrega.setTextColor( this.getResources().getColor( R.color.blanco ) );
            titRetira.setText( getResources().getString( R.string.RetriaCaja ) );
            titRetira.setPadding( 10, 15, 30, 0 );
            titRetira.setTextColor( this.getResources().getColor( R.color.blanco ) );
            titSaldo.setText( getResources().getString( R.string.SaldoCaja ) );
            titSaldo.setPadding( 10, 15, 30, 0 );
            titSaldo.setTextColor( this.getResources().getColor( R.color.blanco ) );

            tr0.addView( titTipo );
            tr0.addView( titSAnt );
            tr0.addView( titEntrega );
            tr0.addView( titRetira );
            tr0.addView( titSaldo );
            tr0.setBackgroundResource( R.color.colorPrimary );
            tablaCajas.addView( tr0 );

            for (int i = 0; i < WebService.tipoCajas.size(); i++) {
                final Caja insCaja = WebService.tipoCajas.get( i );
                TableRow tr1 = new TableRow( this );

                TextView tipo = new TextView( this );
                tipo.setText( insCaja.getNom_Caja().trim().replace( " ", "\n" ) );
                tipo.setPadding( 50, 15, 30, 20 );

                TextView SAnt = new TextView( this );
                SAnt.setText( String.valueOf( insCaja.getCantidad() ) );//Vamos a cargar asi hasta saber que valor debo obtener
                SAnt.setGravity( Gravity.CENTER );
                totSaldoAntetior.add( SAnt );

                final EditText entrega = new EditText( this );
                entrega.setInputType( InputType.TYPE_CLASS_NUMBER );
                entrega.setText( "0" );
                entrega.setGravity( Gravity.CENTER );

                TotCajasEntregadas.add( entrega );

                final EditText retira = new EditText( this );
                retira.setInputType( InputType.TYPE_CLASS_NUMBER );
                retira.setText( "0" );
                retira.setGravity( Gravity.CENTER );
                TotCajasRetiradas.add( retira );

                final TextView saldo = new TextView( this );
                saldo.setText( "0" );
                saldo.setGravity( Gravity.CENTER );
                totSaldo.add( saldo );

                tr1.addView( tipo );
                tr1.addView( SAnt );
                tr1.addView( entrega );
                tr1.addView( retira );
                tr1.addView( saldo );
                tablaCajas.addView( tr1 );
                if (i == WebService.tipoCajas.size() - 1) {
                    TableRow tr3 = new TableRow( this );
                    tr3.setBackgroundResource( R.color.colorPrimary );

                    tx1 = new TextView( this );
                    tx1.setText( getResources().getString( R.string.TotalCaja ) );
                    tx1.setTextColor( this.getResources().getColor( R.color.blanco ) );
                    tx1.setTextSize( 15 );
                    tx1.setGravity( Gravity.CENTER );

                    tx2 = new TextView( this );
                    tx2.setTextColor( this.getResources().getColor( R.color.blanco ) );
                    tx2.setGravity( Gravity.CENTER );
                    int totSAnt = 0;
                    for (int i1 = 0; i1 < totSaldoAntetior.size(); i1++) {
                        totSAnt = totSAnt + Integer.valueOf( totSaldoAntetior.get( i1 ).getText().toString() );
                    }
                    tx2.setText( String.valueOf( totSAnt ) );

                    tx3 = new TextView( this );
                    tx3.setTextColor( this.getResources().getColor( R.color.blanco ) );
                    tx3.setGravity( Gravity.CENTER );
                    int totEnt = 0;
                    for (int i2 = 0; i2 < TotCajasEntregadas.size(); i2++) {
                        totEnt = totEnt + Integer.valueOf( TotCajasEntregadas.get( i2 ).getText().toString() );
                    }
                    tx3.setText( String.valueOf( totEnt ) );

                    tx4 = new TextView( this );
                    tx4.setTextColor( this.getResources().getColor( R.color.blanco ) );
                    tx4.setGravity( Gravity.CENTER );
                    int totRet = 0;
                    for (int i3 = 0; i3 < TotCajasRetiradas.size(); i3++) {
                        totRet = totRet + Integer.valueOf( TotCajasRetiradas.get( i ).getText().toString() );
                    }
                    tx4.setText( String.valueOf( totRet ) );

                    tx5 = new TextView( this );
                    tx5.setTextColor( this.getResources().getColor( R.color.blanco ) );
                    tx5.setGravity( Gravity.CENTER );
                    int saldoTot = totEnt + totSAnt - totRet;
                    tx5.setText( String.valueOf( saldoTot ) );

                    tr3.addView( tx1 );
                    tr3.addView( tx2 );
                    tr3.addView( tx3 );
                    tr3.addView( tx4 );
                    tr3.addView( tx5 );

                    tablaCajas.addView( tr3 );
                }
                entrega.addTextChangedListener( new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        int valorEntrega = 0;
                        try {
                            if (!entrega.getText().toString().equals( "" )) {
                                insCaja.setEntrega( Integer.valueOf( entrega.getText().toString() ) );
                                for (int i = 0; i < TotCajasEntregadas.size(); i++) {
                                    valorEntrega = valorEntrega + Integer.valueOf( TotCajasEntregadas.get( i ).getText().toString() );
                                }
                                tx3.setText( String.valueOf( valorEntrega ) );
                                int totSaldo = Integer.valueOf( tx3.getText().toString() ) + Integer.valueOf( tx2.getText().toString() ) - Integer.valueOf( tx4.getText().toString() );
                                tx5.setText( String.valueOf( totSaldo ) );
                                saldo.setText( String.valueOf( Integer.valueOf( entrega.getText().toString() ) - Integer.valueOf( retira.getText().toString() ) ) );
                                insCaja.setSaldo( Integer.valueOf( saldo.getText().toString() ) );
                            }
                        } catch (Exception exc) {
                            exc.printStackTrace();
                           // Toast.makeText( getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG ).show();
                        }
                    }
                } );
                retira.addTextChangedListener( new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        int valorRetira = 0;
                        try {
                            if (!retira.getText().toString().equals( "" )) {
                                insCaja.setRetira( Integer.valueOf( retira.getText().toString() ) );
                                for (int i = 0; i < TotCajasRetiradas.size(); i++) {
                                    valorRetira = valorRetira + Integer.valueOf( TotCajasRetiradas.get( i ).getText().toString() );
                                }
                                tx4.setText( String.valueOf( valorRetira ) );
                                int totSaldo = Integer.valueOf( tx3.getText().toString() ) + Integer.valueOf( tx2.getText().toString() ) - Integer.valueOf( tx4.getText().toString() );
                                tx5.setText( String.valueOf( totSaldo ) );
                                saldo.setText( String.valueOf( Integer.valueOf( entrega.getText().toString() ) - Integer.valueOf( retira.getText().toString() ) ) );
                                insCaja.setSaldo( Integer.valueOf( saldo.getText().toString() ) );

                            }
                        } catch (Exception exc) {
                            exc.printStackTrace();
                          //  Toast.makeText( getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG ).show();
                        }
                    }
                } );
            }
        } else {
            Intent myIntent = new Intent( context, Login.class );
            //PONER ESTE STRING
            //myIntent.putExtra("Mensaje","CajasTaidas");
            startActivity( myIntent );

        }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void SeleccionarViaje(){
        try{
        if (Utilidad.isNetworkAvailable()) { //SI HAY INTERNET SIGUE
            WebService.ViajeActualSeleccionado = false;
            WebService.EntregasAnteriores.clear();
            //SI LA APP NO TIENE LOS PERMISOS DE GEOLOCALIZACION LOS PIDE
            if (ActivityCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( context, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions( (Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1 );
                return;
            } else {
                /*//SI LOS TIENE SIGUE CON EL CODIGO
                WebService.viajeSeleccionado = new Viaje();
                boolean PrimeroSeleccionado = false;
                for(int index = 0; index < WebService.viajesUsu.size();index++){
                    if((WebService.LastIndexSelected != Integer.parseInt(WebService.viajesUsu.get( index ).getNumViaje()))&&(!PrimeroSeleccionado) &&(index>WebService.IndexViajes)) {
                        WebService.viajeSeleccionado = WebService.viajesUsu.get( index );
                        WebService.LastIndexSelected = Integer.parseInt( WebService.viajesUsu.get( index ).getNumViaje() );
                        WebService.IndexViajes = index;
                        PrimeroSeleccionado = true;
                    }
                }
                if(!PrimeroSeleccionado) {
                    WebService.viajeSeleccionado.setNumViaje( WebService.viajesUsu.get( 0 ).getNumViaje());
                    WebService.LastIndexSelected = Integer.parseInt( WebService.viajesUsu.get( 0 ).getNumViaje() );
                    WebService.IndexViajes = 0;
                }
                //WebService.viajeSeleccionado.setNumViaje(WebService.viajesUsu.get( 7 ).getNumViaje());
                params1 = new RequestParams();
                params1.put( "nro_viaje", WebService.viajeSeleccionado.getNumViaje() );
                params1.put( "seleccion", WebService.viajeSeleccionado.getTipo().equals( "retiro" ) ? "r":"e");
                params1.put( "username", WebService.USUARIOLOGEADO );
                //SI ES UNA ENTREGA HACER ESTO
                final VerEntrega task = new VerEntrega( new AsyncResponse() {
                    public void processFinish(Object output) {
                        TraerEstado task2 = new TraerEstado( new AsyncResponse() {
                            public void processFinish(Object output) {
                                */
                WebService.ViajeActualSeleccionado = true;
                if (WebService.viajeSeleccionado.getTipo().equals( "entrega" )) {
                    if (WebService.entregasTraidas.size() > 0) {
                        //SI LAS ENTREGAS POSIBLES SON MAYORES A 0 (SI EXISTEN ENTREGAS) SIGUE EL CODIGO NORMAL
                        if (WebService.viajeSeleccionado.getEstado() == null) {
                            WebService.viajeSeleccionado.setEstado( "1" );
                        }
                        WebService.entregaDefault = WebService.entregasTraidas.get( 0 );
                        try {
                            if (location == null) {//SI LA GEOLOCALIZACION ES NULL ENTONCES
                                if (localizacionBuscada) {//SI LA LOCALIZACION BUSCADA ES TRUE
                                    Toast toast = Toast.makeText( getApplicationContext(), getResources().getString( R.string.ToastText ), Toast.LENGTH_SHORT );
                                    toast.setGravity( Gravity.CENTER, 0, 0 ); // last two args are X and Y are used for setting position
                                    toast.setDuration( Toast.LENGTH_LONG );//you can even use milliseconds to display toast
                                    toast.show();//showing the toast is important**
                                }
                                Intent myIntent = new Intent( context, TransMap.class );
                                startActivity( myIntent );
                                //String provaider = locManager.getBestProvider( criteria, true );
                                //Utilidad.PedirLocacion( context );//PEDIR LA GEOLOCALIZACION
                                locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );//PIDE EL ADMINISTRADOR DE LA GEOLOCALIZACION
                                criteria = new Criteria();//BUSCADOR MULTIPARAMETROS
                                //GEO:LOC | BUSQUEDA Y POSICIONAMIENTO DE LA UBICACION ACTUAL
                                if (ActivityCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( context, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                location = locationManager.getLastKnownLocation( locationManager.getBestProvider( criteria, false ) );//BUSCA LA LOCALIZACION MAS CERCANA
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
                                ubicacionActual = new LatLng( latitude, longitude );//GUARDA LA UBICACION ACTUAL
                                //GEO:LOC | FIN DE BUSQUEDA Y POSICIONAMIENTO DE LA UBICACION ACTUAL
                                for (int i = 0; i < WebService.entregasTraidas.size(); i++) {
                                    //entregaAConsultar = WebService.entregasTraidas.get( i );
                                    String UrlDistActual = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + ubicacionActual.latitude + "," + ubicacionActual.longitude + "&destinations=" + WebService.entregasTraidas.get( i ).getLatiud_Ubic() + "," + WebService.entregasTraidas.get( i ).getLongitud_Ubic() + "&key=AIzaSyBQKwACTJTM2k25jnI7itqYKbeap2vKULQ";
                                   // System.out.println( "La url de la entrega es: " + UrlDistActual );
                                    URLDistanciaInicial1.add( UrlDistActual );
                                }

                                InvocarApiDistancia2 task3 = new InvocarApiDistancia2( new AsyncResponse() {//CREA OTRA TAREA EN PARALELO
                                    public void processFinish(Object output) {
                                        //Tarea3();
                                    }
                                } );
                                task3.execute();
                                localizacionBuscada = true;
                            } else {//SI YA TIENE UNA LOCALIZACION ENTONCES
                                //GEO:LOC | BUSQUEDA Y POSICIONAMIENTO DE LA UBICACION ACTUAL
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                WebService.lat_origen = latitude;
                                WebService.long_origen = longitude;
                                ubicacionActual = new LatLng( latitude, longitude );
                                //GEO:LOC | FIN DE BUSQUEDA Y POSICIONAMIENTO DE LA UBICACION ACTUAL
                                for (int i = 0; i < WebService.entregasTraidas.size(); i++) {
                                    //entregaAConsultar = WebService.entregasTraidas.get( i );
                                    String UrlDistActual = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + ubicacionActual.latitude + "," + ubicacionActual.longitude + "&destinations=" + WebService.entregasTraidas.get( i ).getLatiud_Ubic() + "," + WebService.entregasTraidas.get( i ).getLongitud_Ubic() + "&key=AIzaSyBQKwACTJTM2k25jnI7itqYKbeap2vKULQ";
                                    URLDistanciaInicial1.add( UrlDistActual );
                                }
                                //HACE LO MISMO QUE EL TASK3 ANTERIOR
                                InvocarApiDistancia2 task3 = new InvocarApiDistancia2( new AsyncResponse() {
                                    public void processFinish(Object output) {
                                        //Tarea3();
                                    }
                                } );
                                task3.execute();
                            }
                        } catch (Exception exc) {
                            exc.toString();
                           // Toast.makeText( getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG ).show();
                        }
                        if(WebService.entregasTraidas.size()!=0) {
                            Intent nextActivity = new Intent( context, ClienteXDefecto.class );
                            startActivity( nextActivity );
                        }
                    } else {//SI LAS ENTREGAS DEL VIAJE SON IGUALES O MENORES A 0 ENTONCES
                        ActionRetornar( );
                    }
                } else {
                    if (WebService.ListaRetiros.size() != 0) {
                        WebService.RetiroSeleccionado = WebService.ListaRetiros.get( 0 );
                        Intent nextActivity = new Intent( context, ClienteXDefecto.class );
                        startActivity( nextActivity );
                    }
                }
                //}
                //} );
                //task2.execute();
                //}
                //} );
                //task.execute();
            }
        } else {//SI NO HAY CONEXION A INTERNET ENTONCES
            Toast toast = Toast.makeText( getApplicationContext(), getResources().getString( R.string.ErrorConexion ), Toast.LENGTH_SHORT );
            toast.setGravity( Gravity.CENTER, 0, 0 ); // last two args are X and Y are used for setting position
            toast.setDuration( Toast.LENGTH_LONG );//you can even use milliseconds to display toast
            toast.show();//showing the toast is important**
        }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //clase InvocarApiDistancia2
    private class InvocarApiDistancia2 extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface


        public InvocarApiDistancia2(AsyncResponse asyncResponse) {
            delegate = asyncResponse;
        }

        @Override
        protected Void doInBackground(String... params) {

            if(Utilidad.isNetworkAvailable()) {
                WebService.TraerEntregaMasCercana(URLDistanciaInicial1);
                return null;

            } else {
                Utilidad.dispalyAlertConexion(context);
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

    @Override
    public void onBackPressed() {
        Utilidad.vibraticionBotones( context );
        try{
        if(WebService.ViajeActualSeleccionado) {
            if (WebService.viajeSeleccionado.getTipo().equals( "entrega" )) {
                if (WebService.retoEnvases.equals( "" ) && (WebService.reto_AgregaFactura.equals( "ok" ) || WebService.retoRemito.equals( "ok" ))) {
                    WebService.reto_AgregaFactura = "";
                    WebService.retoRemito = "";
                    WebService.retoEnvases = "";
                    SeleccionarViaje();
                    Intent myIntent = new Intent( context, ClienteXDefecto.class );
                    startActivity( myIntent );
                } else {
                    WebService.retoEnvases = "";
                    Intent myIntent = new Intent( context, Generados.class );
                    startActivity( myIntent );
                }
            } else if (WebService.viajeSeleccionado.getTipo().equals( "retiro" )) {
                Intent myIntent = new Intent( context, ClienteXDefecto.class );
                startActivity( myIntent );
            }
        }else {
            WebService.retoEnvases = "";
            Intent myIntent = new Intent( context, Generados.class );
            startActivity( myIntent );
        }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
