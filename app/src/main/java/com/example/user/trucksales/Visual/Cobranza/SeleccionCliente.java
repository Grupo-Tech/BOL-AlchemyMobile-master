package com.example.user.trucksales.Visual.Cobranza;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.Encapsuladoras.ClienteCobranza;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Visual.SeleccionFuncionablidad;
import com.example.user.trucksales.Visual.TruckSales.Recorrido_Viaje;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SeleccionCliente extends Activity implements AdapterView.OnItemSelectedListener
{
    private Utilidades Utilidad;
    ImageView atras,/*casita,*/seleccionarCaja,consultas;
    LinearLayout mainLayout;


    AutoCompleteTextView nombreCliente,codCliente;
    public List<String> editTextArray =  new ArrayList<String>();
    public List<String> Codigos = new ArrayList<>(  );
    


    protected static RequestParams params = new RequestParams(  );
    protected static RequestParams params1 = new RequestParams(  );
    protected static RequestParams params4 = new RequestParams(  );

    public static String empresa = "";
    public static String codemp = "";

    TextView nombreUsu,fecha;
    //Spinner spMonedas;
    //List<String> spinnerMonedaArray = new ArrayList<>(  );
    Button BtnBuscarPorNomCod;
    Context contexto;
    String cod_tit_mandar ="";
    boolean bandera = false;
    Spinner spEmpresa;
    final List<String> spinnerEmpresa = new ArrayList<>(  );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_cliente);
        contexto = this;
        Utilidad = new Utilidades(contexto);

        GuardarDatosUsuario.Contexto = contexto;
        try {
            if (WebService.USUARIOLOGEADO != null) {
                if (Utilidad.isNetworkAvailable()) {

                    WebService.ArrayValores.clear();

                    String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                    nombreUsu = (TextView) findViewById(R.id.LblUsu);
                    fecha = (TextView) findViewById(R.id.LblFecha);
                    fecha.setText(timeStamp);
                    nombreCliente = findViewById(R.id.TxtNombreCliente);
                    codCliente = findViewById(R.id.TxtCodCliente);

                    nombreUsu.setText(getResources().getString(R.string.usuarioTool) + WebService.USUARIOLOGEADO);

                    mainLayout = findViewById(R.id.mainLay);

                    spEmpresa = findViewById(R.id.spEmpresa);

                /*if (WebService.deudores.size() <= 0) {
                    Toast toast = Toast.makeText(contexto, getResources().getString(R.string.noClientes), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                    toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                    toast.show();//showing the toast is important***/
                    //}

                    spEmpresa.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                    final ArrayAdapter<String> dataAdapterEmp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerEmpresa);

                    TraerDatosEmpresa task = new TraerDatosEmpresa(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            if (!WebService.errToken.equals("")) {
                                Intent myIntent = new Intent(contexto, Login.class);
                                startActivity(myIntent);
                            }else {
                                for (int i = 0; i < WebService.listEmpresas.size(); i++) {
                                    String nombreAgregar1 = WebService.listEmpresas.get(i).getNomEmp();
                                    spinnerEmpresa.add(nombreAgregar1);
                                }
                                dataAdapterEmp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spEmpresa.setAdapter(dataAdapterEmp);
                                spEmpresa.setSelection(0);
                            }
                        }
                    });
                    task.execute();

                    spEmpresa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                String empSelec = spEmpresa.getSelectedItem().toString();
                                ClienteCobranza instanciaCl = new ClienteCobranza();
                                for (int x = 0; x < WebService.listEmpresas.size(); x++) {
                                    instanciaCl = WebService.listEmpresas.get(x);
                                    if (instanciaCl.getNomEmp().trim().equals(empSelec.trim())) {
                                        if(WebService.configuracion.getPide_empresa().equals("S")) {
                                            WebService.usuarioActual.setEmpresa(instanciaCl.getCodEmp().trim());
                                        }
                                        //WebService.clienteActual.setCodEmp(instanciaCl.getCodEmp().trim());
                                        empresa = instanciaCl.getNomEmp().trim();
                                        codemp = instanciaCl.getCodEmp().trim();
                                        break;
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                /*seleccionarCaja = findViewById(R.id.btnseleccionarCaja);
                seleccionarCaja.setVisibility(View.GONE);*/

               /* if (WebService.configuracion.getCierre_app().equals("S")) {
                    seleccionarCaja.setClickable(true);
                    seleccionarCaja.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(contexto);
                            if (Utilidad.isNetworkAvailable()) {
                                TraerValoresPago task = new TraerValoresPago(new AsyncResponse() {
                                    @Override
                                    public void processFinish(Object output) {
                                        if (WebService.ArrayValores.size() > 0) {
                                            TraerCajasCobrador task2 = new TraerCajasCobrador(new AsyncResponse() {
                                                @Override
                                                public void processFinish(Object output) {
                                                    Intent myIntent = new Intent(contexto, CierreCaja.class);
                                                    startActivity(myIntent);
                                                }
                                            });
                                            task2.execute();
                                        } else {
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Sindatos), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                task.execute();
                            } else {
                                Utilidad.CargarToastConexion(contexto);
                            }
                        }
                    });
                } else {
                    seleccionarCaja.setVisibility(View.GONE);
                }*/
                    //TODO DEJO DESACTIVADO PARA PRUEBAS EN FRESH FOOD
            /*seleccionarCaja.setVisibility(View.INVISIBLE);
            seleccionarCaja.setEnabled(false);*/

                    consultas = findViewById(R.id.consultas);
                    consultas.setClickable(true);
                    consultas.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(contexto);
                            Intent myIntent = new Intent(v.getContext(), ConsultasCobrador.class);
                            startActivity(myIntent);
                        }
                    });

                    atras = findViewById(R.id.btnAtras);
                    atras.setClickable(true);
                    atras.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try{
                            Utilidad.vibraticionBotones(contexto);
                            if (!WebService.usuarioActual.getEs_Entrega().equals("S")) {
                                WebService.USUARIOLOGEADO = null;
                                WebService.logueado = false;
                                Intent myIntent = new Intent(v.getContext(), Login.class);
                                startActivity(myIntent);
                            } else {
                                Intent myIntent = new Intent(v.getContext(), SeleccionFuncionablidad.class);
                                startActivity(myIntent);
                            }
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    });

                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, editTextArray);
                    final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Codigos);

                    if (WebService.deudores.size() == 0) {
                        TraerClientes task01 = new TraerClientes(new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) {
                                if(WebService.errToken.equals("")){
                                    CargarSpinner();
                                    nombreCliente.setAdapter(adapter);
                                    codCliente.setAdapter(adapter2);
                                }else{
                                    Intent myIntent = new Intent(contexto, Login.class);
                                    startActivity(myIntent);
                                }
                            }
                        });
                        task01.execute();
                    } else {
                        CargarSpinner();
                        nombreCliente.setAdapter(adapter);
                        codCliente.setAdapter(adapter2);
                    }

                    nombreCliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Object item = parent.getItemAtPosition(position);
                            ClienteCobranza instaCliente = new ClienteCobranza();

                            boolean encontrado = false;
                            for (int i = 0; i < WebService.deudores.size() && !encontrado; i++) {
                                instaCliente = WebService.deudores.get(i);
                                if (instaCliente.getNom_Tit().trim().equals(item)) {
                                    encontrado = true;
                                    codCliente.setText(instaCliente.getCod_Tit_Gestion().trim());
                                    View view2 = getCurrentFocus();
                                    view2.clearFocus();
                                    if (view2 != null) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                                    }
                                }
                            }
                        }
                    });
                    nombreCliente.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            boolean handled = false;
                            if (actionId == EditorInfo.IME_ACTION_SEND) {
                                //  sendMessage();
                                handled = true;
                            }
                            return handled;
                        }
                    });

                    codCliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Object item = parent.getItemAtPosition(position);
                            ClienteCobranza instaCliente = new ClienteCobranza();
                            boolean encontrado = false;
                            for (int i = 0; i < WebService.deudores.size() && !encontrado; i++) {
                                instaCliente = WebService.deudores.get(i);
                                if (instaCliente.getCod_Tit_Gestion().trim().equals(item)) {
                                    encontrado = true;
                                    nombreCliente.setText(instaCliente.getNom_Tit().trim());
                                    View view2 = getCurrentFocus();
                                    view2.clearFocus();
                                    if (view2 != null) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                                    }
                                }
                            }
                        }
                    });
                    codCliente.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            boolean handled = false;
                            if (actionId == EditorInfo.IME_ACTION_SEND) {
                                //  sendMessage();
                                handled = true;
                            }
                            return handled;
                        }
                    });

                    BtnBuscarPorNomCod = findViewById(R.id.btnTraerDeudas);
                    BtnBuscarPorNomCod.setOnClickListener(new View.OnClickListener() {
                        public void onClick(final View v) {
                            Utilidad.vibraticionBotones(contexto);
                            bandera = false;
                            ClienteCobranza insta = new ClienteCobranza();
                            String[] valores1 = nombreCliente.getText().toString().split("-");
                            String[] valores2 = codCliente.getText().toString().split("-");

                            String cod_tit = codCliente.getText().toString();

                            for (int i = 0; i < WebService.deudores.size() && !bandera; i++) {
                                insta = WebService.deudores.get(i);
                                if (insta.getCod_Tit_Gestion().trim().equals(cod_tit)) {
                                    bandera = true;
                                    cod_tit_mandar = codCliente.getText().toString().trim();
                                    WebService.clienteActual = insta;
                                    WebService.clienteActual.setNomEmp(empresa);
                                    WebService.clienteActual.setCodEmp(codemp);
                                }
                                /*if (valores2.length > 0) {
                                    if (insta.getCod_Tit_Gestion().trim().equals(valores2[0])) {
                                        bandera = true;
                                        cod_tit_mandar = codCliente.getText().toString().trim();
                                        WebService.clienteActual = insta;
                                        WebService.clienteActual.setNomEmp(empresa);
                                        WebService.clienteActual.setCodEmp(codemp);
                                    }
                                }
                                if (valores1.length > 0) {
                                    if (insta.getNom_Tit().trim().equals(nombreCliente.getText().toString().trim())) {
                                        bandera = true;
                                        WebService.clienteActual = insta;
                                        WebService.clienteActual.setNomEmp(empresa);
                                        WebService.clienteActual.setCodEmp(codemp);
                                        cod_tit_mandar = insta.getCod_Tit_Gestion().trim();
                                    }
                                }*/

                            }
                            if (!bandera) {
                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.ToastCobranza), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                                toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                                toast.show();//showing the toast is important**
                            } else {

                                TraerDeudores task = new TraerDeudores(new AsyncResponse() {
                                    @Override
                                    public void processFinish(Object output) {
                                        if (!WebService.errToken.equals("")) {
                                            Intent myIntent = new Intent(contexto, Login.class);
                                            startActivity(myIntent);
                                        } else {
                                            //WebService.deudas = new ArrayList<>();
                                            // WebService.deudasSeleccionadas.clear();
                                            if (WebService.deudasViaje.size() <= 0) {
                                                codCliente.setText("");
                                                nombreCliente.setText("");
                                                Toast toast = Toast.makeText(contexto, getResources().getString(R.string.noDeuda), Toast.LENGTH_LONG);
                                                toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                                                toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                                                toast.show();//showing the toast is important***/
                                            } else {
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
                                                WebService.lat_origen = latitude;
                                                WebService.long_origen = longitude;

                                                params = new RequestParams();
                                                params.put("usuario", WebService.USUARIOLOGEADO);//1
                                                params.put("latitud_actual", String.valueOf(WebService.lat_actual));
                                                params.put("longitud_actual", String.valueOf(WebService.long_actual));
                                                params.put("latitud_destino", String.valueOf(WebService.clienteActual.getLatiud_Ubic()));
                                                params.put("longitud_destino", String.valueOf(WebService.clienteActual.getLongitud_Ubic()));
                                                params.put("latitud_origen", String.valueOf(WebService.lat_origen));
                                                params.put("longitud_origen", String.valueOf(WebService.long_origen));
                                                params.put("en_pausa", String.valueOf(WebService.EstadoActual));//12
                                                params.put("nro_viaje", 0);//9
                                                params.put("cod_sucursal", "0");//10
                                                params.put("nro_orden", "0");//13
                                                params.put("nom_cliente", WebService.clienteActual.getNom_Tit().trim());//11
                                                params.put("distancia_a_recorrer", WebService.distancia_a_recorrer);

                                                ActualizarUbicacion actuUbic = new ActualizarUbicacion();
                                                actuUbic.execute();

                                                if (WebService.usuarioActual.getEs_Entrega().equals("S") && WebService.usuarioActual.getEs_Cobranza().equals("S") || WebService.usuarioActual.getEs_Entrega().equals("S") && !WebService.usuarioActual.getEs_Cobranza().equals("S")) {
                                                    Intent myIntent = new Intent(contexto, SeleccionarDeudas.class);
                                                    startActivity(myIntent);
                                                } else {
                                                    if (WebService.clienteActual.getLatiud_Ubic() == 0.0 || WebService.clienteActual.getLongitud_Ubic() == 0.0) {
                                                        Intent myIntent = new Intent(contexto, SeleccionarDeudas.class);
                                                        startActivity(myIntent);
                                                    } else {
                                                        obtenerHora();

                                                        WebService.ViajeActualSeleccionadoCobrador = true;
                                                        Intent myIntent = new Intent(contexto, Recorrido_Viaje.class);
                                                        myIntent.putExtra("intent", "Cobranza");
                                                        startActivity(myIntent);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                });
                                task.execute();
                            }
                        }
                    });
                }else{
                    Utilidad.CargarToastConexion(contexto);
                }
            }else {
                Intent myIntent = new Intent(contexto, Login.class);
                startActivity(myIntent);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
           // Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    private class  TraerDeudores extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface
        public TraerDeudores(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            if(Utilidad.isNetworkAvailable()) {
                RequestParams params1 = new RequestParams(  );
                params1.put( "cod_tit",cod_tit_mandar );
                params1.put( "cod_empresa",WebService.usuarioActual.getEmpresa().trim());
                params1.put("username", WebService.USUARIOLOGEADO.trim());
                //params1.put( "cod_moneda",WebService.monedaSeleccionada.getCod_Moneda() );
                WebService.TraerDeudas(params1);
                return null;
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
        protected void onProgressUpdate(Void... values) { }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    public class  TraerDatosEmpresa extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface
        public TraerDatosEmpresa(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            if(Utilidad.isNetworkAvailable())   {
                WebService.ObtenerDatosEmpresa();
                return null;
            }
            else  {
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
        protected void onProgressUpdate(Void... values)
        {

        }
    }

    private void obtenerHora(){
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
    }

    private class TraerClientes extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface
        public TraerClientes(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            if(Utilidad.isNetworkAvailable()) {
                RequestParams params1 = new RequestParams(  );
                /*params1.put("username", WebService.usuarioActual.getNombre().trim());
                params1.put("cod_empresa",WebService.usuarioActual.getEmpresa().trim());*/
                WebService.TraerClientes("Cobranzas/TraerClientes.php",params1);
                return null;

            } else {}
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

    private void CargarSpinner(){
        for (int i = 0; i < WebService.deudores.size(); i++) {
            String nombreAgregar = WebService.deudores.get(i).getNom_Tit().trim();
            editTextArray.add(nombreAgregar);
            String codigoAgregar = WebService.deudores.get(i).getCod_Tit_Gestion().trim();
            Codigos.add(codigoAgregar);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try{
        Utilidad.vibraticionBotones(contexto);
        if (!WebService.usuarioActual.getEs_Entrega().equals("S")) {
            WebService.USUARIOLOGEADO = null;
            WebService.logueado = false;
            Intent myIntent = new Intent(contexto, Login.class);
            startActivity(myIntent);
        } else {
            Intent myIntent = new Intent(contexto, SeleccionFuncionablidad.class);
            startActivity(myIntent);
        }
        }catch (Exception ex){
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

