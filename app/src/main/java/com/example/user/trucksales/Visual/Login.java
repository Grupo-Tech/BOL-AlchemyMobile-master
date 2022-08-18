package com.example.user.trucksales.Visual;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.Viaje;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Cobranza.MenuCobranzas;
import com.example.user.trucksales.Visual.Cobranza.SeleccionCliente;
import com.example.user.trucksales.Visual.FacturaDirecta.FacturaDirecta;
import com.example.user.trucksales.Visual.TruckSales.ClienteXDefecto;
import com.example.user.trucksales.Visual.TruckSales.Recorrido_Viaje;
import com.example.user.trucksales.Visual.TruckSales.TransMap;
import com.example.user.trucksales.Visual.TruckSales.Viajes;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.RequestParams;
import com.nullwire.trace.ExceptionHandler;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Login extends AppCompatActivity {
    LinearLayout mainLayout;
    EditText txtUsuario;
    EditText txtPassword;
    Button btnLogin,btnSettings;
    TextView LblRetorno, version;
    Context contexto;
    String nombre;
    String password;

    public static RequestParams params1= new RequestParams();
    private Utilidades Utilidad;
    public  static LatLng ubicacionActual;
    LocationManager locationManager;
    Criteria criteria;
    public static ArrayList<String> URLDistanciaInicial1 = new ArrayList<>(  );
    private Location location;
    private boolean localizacionBuscada;
    private GuardarDatosUsuario Guardado;
    private boolean LogIn;
    private boolean empresa = false;
    private static boolean ThreadCreado = false;
    public static int TiempoThread = 0;
    private static int Tiempo = 0;
    private static LatLng lastLocationSaved;
    private static String lastNickName;
    public static RequestParams errorParams= new RequestParams();
    private SwitchCompat SwitchCompat;
    private static boolean location_created = false;
    RequestParams params;
    private class Errores extends AsyncTask<String, Void, Void> {
        public AsyncResponse delegate = null;//Call back interface
        public Errores(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {
            if(Utilidad.isNetworkAvailable()){
                WebService.Logueo(errorParams,"Errores/ErrorCatcher.php");
            }
            return null;
        }

        @Override
        public void onPreExecute(){
            Utilidad.showLoadingMessage();
        }

        @Override
        protected void onPostExecute(Void result) {
            if(!WebService.logueado){
                Utilidad.deleteLoadingMessage();
            }
            delegate.processFinish( WebService.logueado);
        }

        @Override
        protected void onProgressUpdate(Void... values){}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //PARA FRESH
        //ExceptionHandler.register( this,"http://200.85.41.198:8081/TerosMobApp/mobileDesa/Errores/server.php" );
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        contexto = this;
        LogIn = true;
        Guardado = new GuardarDatosUsuario(contexto);

        try {
            WebService.LastLocationSaved = new LatLng(0.0, 0.0);
            //System.out.println("Entra en login");
            WebService.EstadoActual = -2;
            Utilidad = new Utilidades(contexto);

            //Utilidad.disableSSLCertificateChecking();

            mainLayout = (LinearLayout) findViewById(R.id.mainLay);
            txtUsuario = (EditText) findViewById(R.id.txtNombUsu);
            WebService.USUARIOLOGEADO = null;
            WebService.logueado = false;
            WebService.viajesUsu = new ArrayList<>();
            txtPassword = (EditText) findViewById(R.id.txtPassword);

            SwitchCompat = findViewById(R.id.switchCompat);

            if (lastNickName != null) {
                txtUsuario.setText(lastNickName);
            }
            LblRetorno = (TextView) findViewById(R.id.login_error);
            version = findViewById(R.id.version);
            version.setText(getResources().getString(R.string.version) + WebService.version);
        /*try{
            Intent intent = getIntent();
            String Mensaje = intent.getExtras().getString("Mensaje");
            LblRetorno.setText(Mensaje);
        }catch(Exception e){
            e.printStackTrace();
        }*/
            //Agregado para mostrar el gif de carga mientras dura la invocacion
            Spannable spannable = new SpannableString("prueba color");
            btnSettings = (Button) findViewById(R.id.btnSettings);


            //loadIcon.setVisibility( View.VISIBLE );
            btnSettings.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Utilidad.vibraticionBotones(contexto);
                    BuscarConf();
                /*ObtenerConfEmpresa task = new ObtenerConfEmpresa( new AsyncResponse(){
                    @Override
                    public void processFinish(Object output) {*/
                    Intent myIntent = new Intent(contexto, Configuracion.class);
                    startActivity(myIntent);
                   /* }
                } );
                task.execute(  );*/
                }
            });
            WebService.EstadoAnterior = 0;
            btnLogin = (Button) findViewById(R.id.btnLogin);

            //POR BDL 28/06/2019
            final SharedPreferences preferencias = getSharedPreferences("recordar", Context.MODE_PRIVATE);
            String nombreusu = preferencias.getString("txtUsuario", "");
            String pass = preferencias.getString("txtPassword", "");
            boolean stateSwitch = preferencias.getBoolean("stateSwitch", false);
            SwitchCompat.setChecked(stateSwitch);
            txtUsuario.setText(nombreusu);
            txtPassword.setText(pass);
            //HASTA ACA AGREGADO BDL

            BuscarConf();

            ExceptionHandler.register(this, WebService.URL + "Errores/server.php");

            btnLogin.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View v) {
                    if (Utilidad.isNetworkAvailable()) {
                        Utilidad.vibraticionBotones(contexto);

                        //ACTIVADO PARA EVITAR VARIOS CLICK EN EL BOTON... 21/11/2019 BDL
                        btnLogin.setEnabled(false);

                        BuscarConf();

                        if (empresa == false) {
                            Toast.makeText(contexto, getResources().getString(R.string.errorEmpresa), Toast.LENGTH_LONG).show();
                        } else {

                            //AGREGADO BDL 28/06/2019
                            if (SwitchCompat.isChecked()) {
                                SharedPreferences.Editor editor = preferencias.edit();
                                editor.putString("txtUsuario", txtUsuario.getText().toString());
                                editor.putString("txtPassword", txtPassword.getText().toString());
                                editor.putBoolean("stateSwitch", SwitchCompat.isChecked());
                                editor.commit();
                            } else {
                                SharedPreferences.Editor editor = preferencias.edit();
                                editor.putString("txtUsuario", "");
                                editor.putString("txtPassword", "");
                                editor.putBoolean("stateSwitch", SwitchCompat.isChecked());
                                editor.commit();
                            }
                            if (ActivityCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions((Activity) contexto, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                                //ACTIVADO PARA EVITAR VARIOS CLICK EN EL BOTON... 21/11/2019 BDL
                                btnLogin.setEnabled(true);
                                return;
                            } else {
                                if (!location_created) {
                                    actualizarPosicion();
                                }
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
                                WebService.viajesUsu = new ArrayList<>();
                                nombre = txtUsuario.getText().toString();
                                password = txtPassword.getText().toString();
                                WebService.usuParam = nombre;
                                params1.put("username", nombre);
                                params1.put("password", password);
                                params1.put("version", WebService.version);
                                logeo task = new logeo(new AsyncResponse() {
                                    public void processFinish(Object output) {
                                        if (WebService.logueado) {
                                            if (!WebService.token.equals("")) {
                                                lastNickName = nombre;
                                                LogIn = false;
                                                WebService.EstadoActual = 0;

                                                new traerParempTask().execute();

                                                //SI EL USUARIO NO TIENE PERMISOS
                                                if (WebService.usuarioActual.getEs_Cobranza() == null && WebService.usuarioActual.getEs_Entrega() == null && WebService.usuarioActual.getEs_VentaDirecta() == null) {
                                                    LblRetorno.setText(getResources().getString(R.string.Sin_permiosos));
                                                    btnLogin.setEnabled(true);
                                                } else {
                                                    if (!WebService.usuarioActual.getEs_Cobranza().equals("S") && !WebService.usuarioActual.getEs_Entrega().equals("S") && !WebService.usuarioActual.getEs_VentaDirecta().equals("S")) {
                                                        LblRetorno.setText(getResources().getString(R.string.Sin_permiosos));
                                                        btnLogin.setEnabled(true);
                                                    }
                                                    //SI EL USUARIO TIENE PERMISOS PARA COBRANZA Y PARA TRUCKSALES //TODO VER ESTOOOOOOOO
                                                    if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getEs_Entrega().equals("S")) {

                                                        ObtenerTiempo obtnTiemp = new ObtenerTiempo(new AsyncResponse() {
                                                            @Override
                                                            public void processFinish(Object output) {
                                                                CargarViajes task2 = new CargarViajes(new AsyncResponse() {
                                                                    public void processFinish(Object output) {
                                                                        if(!WebService.errToken.equals("")){
                                                                            Intent myIntent = new Intent(contexto, Login.class);
                                                                            startActivity(myIntent);
                                                                        }else {
                                                                            if (!ThreadCreado) {
                                                                                Hilo();
                                                                            }
                                                                            if (WebService.viajesUsu.size() == 1) {
                                                                                SeleccionarViaje();
                                                                            } else {
                                                                                Intent nextActivity = new Intent(contexto, Viajes.class);
                                                                                startActivity(nextActivity);
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                                task2.execute();
                                                            }
                                                        });
                                                        obtnTiemp.execute();

                                                    }
                                                    // COMENTO YA QUE NO VA A SER SOLO DE COBRANZA SINO POR COBRANZA LIBRE ETC
                                                    //SI EL USUARIO ACTUAL TIENE PERMISOS PARA SOLO COBRANZA
                                                    if (WebService.usuarioActual.getEs_Cobranza().equals("S") && !WebService.usuarioActual.getEs_Entrega().equals("S")) {

                                                        //SI EL USUARIO ES COBRANZA LIBRE O FIJO
                                                        if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getEs_Entrega().equals("N") && WebService.usuarioActual.getTipoCobrador().equals("L") || WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getEs_Entrega().equals("N") && WebService.usuarioActual.getTipoCobrador().equals("F")) {

                                                            Intent myIntent = new Intent(contexto, SeleccionCliente.class);
                                                            startActivity(myIntent);

                                                        }
                                                        //SI EL USUARIO ES COBRANZA DIARIO
                                                        if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getEs_Entrega().equals("N") && WebService.usuarioActual.getTipoCobrador().equals("D")) {

                                                            if (!ThreadCreado) {
                                                                Hilo();
                                                            }

                                                            Intent myIntent = new Intent(contexto, MenuCobranzas.class);
                                                            startActivity(myIntent);

                                                        }
                                                    }
                                                    //SI EL USUARIO SOLO TIENE PERMISOS PARA TRUCKSALES
                                                    if (!WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getEs_Entrega().equals("S")) {
                                                        ObtenerTiempo obttiem = new ObtenerTiempo(new AsyncResponse() {
                                                            @Override
                                                            public void processFinish(Object output) {
                                                                CargarViajes task2 = new CargarViajes(new AsyncResponse() {
                                                                    public void processFinish(Object output) {

                                                                        if(!WebService.errToken.equals("")){
                                                                            Intent myIntent = new Intent(contexto, Login.class);
                                                                            startActivity(myIntent);
                                                                        }else {
                                                                            if (WebService.viajesUsu.size() == 1) {
                                                                                SeleccionarViaje();
                                                                            } else {
                                                                                Intent nextActivity = new Intent(contexto, Viajes.class);
                                                                                startActivity(nextActivity);
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                                task2.execute();
                                                            }
                                                        });
                                                        obttiem.execute();
                                                    }
                                                    if (WebService.usuarioActual.getEs_VentaDirecta().equals("S")) {
                                                        ObtenerTiempo obt = new ObtenerTiempo(new AsyncResponse() {
                                                            @Override
                                                            public void processFinish(Object output) {
                                                                CargarViajesVenta task = new CargarViajesVenta(new AsyncResponse() {
                                                                    @Override
                                                                    public void processFinish(Object output) {
                                                                        if (!ThreadCreado) {
                                                                            Hilo();
                                                                        }
                                                                        Intent nextActivity = new Intent(contexto, Viajes.class);
                                                                        nextActivity.putExtra("intent", "VentaDirecta");
                                                                        startActivity(nextActivity);
                                                                    }
                                                                });
                                                                task.execute();
                                                            }
                                                        });
                                                        obt.execute();
                                                    }
                                                    btnLogin.setEnabled(true);
                                                }

                                            } else {
                                                if (WebService.mensajeLogueo != null) {
                                                    LblRetorno.setText(WebService.mensajeLogueo);
                                                    btnLogin.setEnabled(true);
                                                } else {
                                                    LblRetorno.setText(getResources().getString(R.string.retornoLogInvalido));
                                                    btnLogin.setEnabled(true);
                                                }
                                            }
                                        } else {
                                            if (WebService.mensajeLogueo != null) {
                                                LblRetorno.setText(WebService.mensajeLogueo);
                                                btnLogin.setEnabled(true);
                                            } else {
                                                LblRetorno.setText(getResources().getString(R.string.retornoLogInvalido));
                                                btnLogin.setEnabled(true);
                                            }
                                        }
                                    }
                                });
                                task.execute();
                            }
                        }
                    } else {
                        LblRetorno.setText(getResources().getString(R.string.ErrorConexion));
                        btnLogin.setEnabled(true);
                    }
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }

    protected void onResume() {
        WebService.USUARIOLOGEADO="";
        WebService.logueado = false;
        if(!location_created) {
            actualizarPosicion();
        }
        btnLogin.setEnabled(true);
        super.onResume();
    }

    class traerParempTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progress;

        protected void onPreExecute (){
            progress = ProgressDialog.show(contexto, "Obteniendo datos", "Por favor, espere..", true);
        }
        protected String doInBackground(String... params) {

            String txt ="";

            WebService.traerParemp();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    WebService.limpiarValores();
                }
            });

            return txt;
        }
        protected void onProgressUpdate(Integer...a){
        }
        protected void onPostExecute(String result) {
            progress.dismiss();
            if (!WebService.errToken.equals("")) {
                Intent myIntent = new Intent(contexto, Login.class);
                startActivity(myIntent);
            }
        }
    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    //------------------------------------------LOGUEO DEL SISTEMA------------------------------------------------------

    private class logeo extends AsyncTask<String, Void, Void> {
        public AsyncResponse delegate = null;//Call back interface
        public logeo(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {
            if(Utilidad.isNetworkAvailable()){
                WebService.Logueo(params1,"Session/login.php");
            }
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
        protected void onProgressUpdate(Void... values){}
    }

    private class ObtenerTiempo extends AsyncTask<String, Void, Void>{
        public AsyncResponse delegate = null;//Call back interface
        public ObtenerTiempo(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... strings) {
            params1 = new RequestParams(  );
            params1.put( "cod_emp",WebService.IdEmpresa );
            WebService.ObtenerTiempo(params1);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            delegate.processFinish( null );
        }
    }

    private class ObtenerConfEmpresa extends AsyncTask<String, Void, Void> {
        public AsyncResponse delegate;
        public ObtenerConfEmpresa(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            if(Utilidad.isNetworkAvailable()) {
                WebService.traerConf();
                return null;
            } else {
                Utilidad.CargarToastConexion(contexto);
            }
            return null;
        }
        @Override
        public void onPreExecute() {
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


    private class CargarViajesVenta extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface
        public CargarViajesVenta(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            params1 = new RequestParams(  );
            params1.put( "username",WebService.usuarioActual.getNombre());
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



    //------------------------------------------LOGUEO DEL SISTEMA------------------------------------------------------

    //------------------------------------------SELECCION DE VIAJES------------------------------------------------------

    //METODO PARA SELECCIONAR UN VIAJE O RETIRO
    private void SeleccionarViaje() {
        if (Utilidad.isNetworkAvailable()) { //SI HAY INTERNET SIGUE
            try {
                //SI LA APP NO TIENE LOS PERMISOS DE GEOLOCALIZACION LOS PIDE
                if (ActivityCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) contexto, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    return;
                } else {
                    //SI LOS TIENE SIGUE CON EL CODIGO
                    WebService.viajeSeleccionado = new Viaje();
                    boolean PrimeroSeleccionado = false;
                    for (int index = 0; index < WebService.viajesUsu.size(); index++) {
                        if (!PrimeroSeleccionado) {
                            WebService.viajeSeleccionado = WebService.viajesUsu.get(index);
                            WebService.LastIndexSelected = Integer.parseInt(WebService.viajesUsu.get(index).getNumViaje());
                            WebService.IndexViajes = index;
                            PrimeroSeleccionado = true;
                        }
                    }
                    params1 = new RequestParams();
                    params1.put("nro_viaje", WebService.viajeSeleccionado.getNumViaje());
                    params1.put("seleccion", WebService.viajeSeleccionado.getTipo().equals("retiro") ? "r" : "e");
                    params1.put("username", WebService.USUARIOLOGEADO);
                    //SI ES UNA ENTREGA HACER ESTO
                    final VerEntrega task = new VerEntrega(new AsyncResponse() {
                        public void processFinish(Object output) {
                            if (!WebService.errToken.equals("")) {
                                Intent myIntent = new Intent(contexto, Login.class);
                                startActivity(myIntent);
                            } else {
                                TraerEstado task2 = new TraerEstado(new AsyncResponse() {
                                    public void processFinish(Object output) {
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
                                                        //TRATA DE CAMBIAR DE ACTIVITY, SE MUEVE AL MAPA
                                                        LogIn = false;
                                                        if (!ThreadCreado) {
                                                            Hilo();
                                                        }
                                                        Intent myIntent = new Intent(contexto, TransMap.class);
                                                        startActivity(myIntent);
                                                        //String provaider = locManager.getBestProvider( criteria, true );
                                                        //Utilidad.PedirLocacion( contexto );//PEDIR LA GEOLOCALIZACION
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
                                                        Intent nextActivity = new Intent(contexto, ClienteXDefecto.class);
                                                        startActivity(nextActivity);
                                                    }
                                                } catch (Exception exc) {
                                                    exc.toString();
                                                }

                                            } else {//SI LAS ENTREGAS DEL VIAJE SON IGUALES O MENORES A 0 ENTONCES
                                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.NoHayEntregas), Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                                                toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                                                toast.show();//showing the toast is important**
                                                Intent NextActivity = new Intent(contexto, Login.class);
                                                startActivity(NextActivity);
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
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {//SI NO HAY CONEXION A INTERNET ENTONCES
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.ErrorConexion), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
            toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
            toast.show();//showing the toast is important**
        }
    }

    //CLASE VERENTREGA
    private class  VerEntrega extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface
        public VerEntrega(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            /*if(Utilidad.isNetworkAvailable()) {*/
                WebService.ListaEntregasViaje(params1,"Viajes/Entregas.php");
                return null;

           /* }
            else {
                Utilidad.dispalyAlertConexion(contexto);
            }*/

           // return null;
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

    //CLASE TRAERESTADO
    private class TraerEstado  extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface
        public TraerEstado(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            if(Utilidad.isNetworkAvailable()) {
                params1 = new RequestParams(  );
                params1.add( "nro_viaje",WebService.viajeSeleccionado.getNumViaje() );
                WebService.ConsultarEstadoViaje("Viajes/ConsultarEstado.php",params1);
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

    //CLASE INVOCARAPIDISTANCIA2
    private class InvocarApiDistancia2 extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
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
    @Override
    public void onBackPressed() {
        Utilidad.vibraticionBotones( contexto );
    }

    private class Logueo extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface

        public Logueo(AsyncResponse asyncResponse) {
            delegate = asyncResponse;
        }

        @Override
        protected Void doInBackground(String... params) {

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

    //THREAD PARA EL ENCAPSULAMIENTO DEL USUARIO
    private void Hilo(){
        ThreadCreado = true;
        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    if (!WebService.viajesUsu.isEmpty() || !WebService.viajesCobradorUsu.isEmpty()) {
                        while (!LogIn && WebService.viajesUsu.size() > 0 || !LogIn && WebService.viajesCobradorUsu.size() > 0) {
                        //while (!LogIn && WebService.viajesUsu.size() > 0 ) {
                            try {
                                Thread.sleep( 5000 );
                                Tiempo += 5;
                                //REALIZA LA BUSQUEDA DE LA LOCALIZACION Y LA GUARDA
                                if (Tiempo % 5 == 0) {
                                    Guardado.VerificacionDeEstado();
                                }
                                //if ((Tiempo % TiempoThread == 0 || Tiempo > TiempoThread ) && WebService.llegadaHabilitada) {
                                if (( Tiempo > TiempoThread ) && WebService.llegadaHabilitada) {

                                    if (lastLocationSaved == null) {
                                        lastLocationSaved = new LatLng(1.0, 1.0);
                                    }
                                    //Utilidad.PedirLocacion( contexto );//PEDIR LA GEOLOCALIZACION
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
                                    double earthRadius = 6371000; //meters
                                    double dLat = Math.toRadians(location.getLatitude() - lastLocationSaved.latitude);
                                    double dLng = Math.toRadians(location.getLongitude() - lastLocationSaved.longitude);
                                    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                                            Math.cos(Math.toRadians(lastLocationSaved.latitude)) * Math.cos(Math.toRadians(location.getLatitude())) *
                                                    Math.sin(dLng / 2) * Math.sin(dLng / 2);
                                    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                                    float dist = (float) (earthRadius * c);
                                    float distDestino = 11;
                                    if (WebService.usuarioActual.getEs_Entrega().equals("S")) {
                                        if (WebService.ViajeActualSeleccionado && WebService.Entrega_A_Realizar != null) {
                                            double dLatDestino = Math.toRadians(location.getLatitude() - WebService.Entrega_A_Realizar.getLatiud_Ubic());
                                            double dLngDestino = Math.toRadians(location.getLongitude() - WebService.Entrega_A_Realizar.getLongitud_Ubic());
                                            double aDestino = Math.sin(dLatDestino / 2) * Math.sin(dLatDestino / 2) +
                                                    Math.cos(Math.toRadians(WebService.Entrega_A_Realizar.getLatiud_Ubic())) * Math.cos(Math.toRadians(location.getLatitude())) *
                                                            Math.sin(dLngDestino / 2) * Math.sin(dLngDestino / 2);
                                            double cDestino = 2 * Math.atan2(Math.sqrt(aDestino), Math.sqrt(1 - aDestino));
                                            distDestino = (float) (earthRadius * cDestino);
                                        }
                                    } else if (WebService.usuarioActual.getEs_Cobranza().equals("S")) {
                                        if (WebService.clienteActual.getCod_Docum() != null){
                                            double dLatDestino = Math.toRadians(location.getLatitude() - WebService.clienteActual.getLatiud_Ubic());
                                        double dLngDestino = Math.toRadians(location.getLongitude() - WebService.clienteActual.getLongitud_Ubic());
                                        double aDestino = Math.sin(dLatDestino / 2) * Math.sin(dLatDestino / 2) +
                                                Math.cos(Math.toRadians(WebService.clienteActual.getLatiud_Ubic())) * Math.cos(Math.toRadians(location.getLatitude())) *
                                                        Math.sin(dLngDestino / 2) * Math.sin(dLngDestino / 2);
                                        double cDestino = 2 * Math.atan2(Math.sqrt(aDestino), Math.sqrt(1 - aDestino));
                                        distDestino = (float) (earthRadius * cDestino);
                                    }
                                }else if (WebService.usuarioActual.getEs_VentaDirecta().equals("S")) {
                                        if (WebService.clienteActual.getCod_Docum() != null){
                                            double dLatDestino = Math.toRadians(location.getLatitude() - WebService.clienteActual.getLatiud_Ubic());
                                            double dLngDestino = Math.toRadians(location.getLongitude() - WebService.clienteActual.getLongitud_Ubic());
                                            double aDestino = Math.sin(dLatDestino / 2) * Math.sin(dLatDestino / 2) +
                                                    Math.cos(Math.toRadians(WebService.clienteActual.getLatiud_Ubic())) * Math.cos(Math.toRadians(location.getLatitude())) *
                                                            Math.sin(dLngDestino / 2) * Math.sin(dLngDestino / 2);
                                            double cDestino = 2 * Math.atan2(Math.sqrt(aDestino), Math.sqrt(1 - aDestino));
                                            distDestino = (float) (earthRadius * cDestino);
                                        }
                                    }
                                    if (dist > 100 && distDestino > 10 || lastLocationSaved == null) {
                                        if (WebService.ViajeActualSeleccionado && WebService.Entrega_A_Realizar != null){
                                            Guardado.GuardarDatos();
                                        }
                                        else if(WebService.clienteActual.getCod_Docum() != null){
                                            Guardado.GuardarDatos();
                                        }
                                        double lat = location.getLatitude();
                                        double lon = location.getLongitude();
                                        lastLocationSaved = new LatLng( lat, lon );
                                        WebService.llegadaHabilitada = false;
                                    }
                                }
                            }catch (Exception e ){
                                //System.out.println(e.toString());
                                e.printStackTrace();
                            }
                        }
                    }
                    ThreadCreado = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }

    private void actualizarPosicion() {
        try {
            if (ActivityCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LocationManager locManager = (LocationManager) contexto.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //System.out.println("Coordenadas GPS actualizadas: " + "lat--> " + location.getLatitude() + " long--> " + location.getLongitude());
                    try{
                        if (location.getLatitude() != WebService.lat_actual || location.getLongitude() != WebService.long_actual) {
                            WebService.lat_actual = location.getLatitude();
                            WebService.long_actual = location.getLongitude();

                       /*if (Utilidad.calcularDistancia(WebService.lat_actual, WebService.long_actual, WebService.LastLocationSaved.latitude, WebService.LastLocationSaved.longitude) > 100) {
                            try {
                                //location = locationManager.getLastKnownLocation( locationManager.getBestProvider( criteria, false ) );
                                double latitude = 0;
                                double longitude = 0;
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                                double latitudDestino = 0.0;
                                double longitudDestino = 0.0;

                                Guardado.GuardarDatos();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }*/
                        if (Recorrido_Viaje.AppPrendida) {
                            Recorrido_Viaje obj = new Recorrido_Viaje();

                            /*final String timeStamp2 = new SimpleDateFormat( "dd-MM-yyyy hh:mm:ss" ).format( Calendar.getInstance().getTime() );
                            System.out.println("Login" + timeStamp2);*/

                            obj.verificaMapa();
                            obj.mover_google_maps_marker(new LatLng(WebService.lat_actual, WebService.long_actual));
                        }
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                //System.out.println("Cambios en proveedor " + s + " Estado-->" + i);
            }
            @Override
            public void onProviderEnabled(String s) {
                //System.out.println("Proveedor habilitado " + s);
            }
            @Override
            public void onProviderDisabled(String s) {
                //System.out.println("Proveedor deshabilitado " + s);
            }
        };
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locListener);
        System.out.println("Se creo el actualizador correctamente");
        location_created = true;
    }catch (Exception e){
        e.printStackTrace();
    }
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
        protected void onPostExecute(Void aVoid) {
            delegate.processFinish(WebService.logueado);
        }
    }
    //------------------------------------------SELECCION DE VIAJES------------------------------------------------------

    public void BuscarConf(){
        if(WebService.configuracion.getCod_Empresa() == null) {
            ObtenerConfEmpresa task = new ObtenerConfEmpresa(new AsyncResponse() {
                @Override
                public void processFinish(Object output) {
                    if (WebService.configuracion.getCod_Empresa() == null) {
                        Toast.makeText(contexto, getResources().getString(R.string.errorEmpresa), Toast.LENGTH_LONG).show();
                    }else{
                        empresa = true;
                        WebService.max_lin_rec = WebService.configuracion.getMax_lin_rec();
                    }
                }
            });
            task.execute();
        }else
        {
            empresa = true;
        }
    }

    public static class NukeSSLCerts {
        protected static final String TAG = "NukeSSLCerts";

        public static void nuke() {
            try {
                TrustManager[] trustAllCerts = new TrustManager[] {
                        new X509TrustManager() {
                            public X509Certificate[] getAcceptedIssuers() {
                                X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                                return myTrustedAnchors;
                            }

                            @Override
                            public void checkClientTrusted(X509Certificate[] certs, String authType) {}

                            @Override
                            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                        }
                };

                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

