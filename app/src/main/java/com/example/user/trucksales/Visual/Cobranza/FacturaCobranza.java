package com.example.user.trucksales.Visual.Cobranza;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.ClienteCobranza;
import com.example.user.trucksales.Encapsuladoras.Moneda;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FacturaCobranza extends Activity implements AdapterView.OnItemSelectedListener{


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

            }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

            }

    private Utilidades Utilidad;
    public EditText EdtCodSucTribut, EdtCodFacTribut, EdtNumFactu, Importe;
    Button validarFac;
    protected Context context;
    public TextView LblUsu, LblFecha, CodTit, NomTit, retorno, NomEmp;
    public ImageView atras;
    protected static RequestParams params = new RequestParams(  );
    private boolean facturaVal;

    public String nro_viaje = "0";
    public String cod_Suc;
    public String cod_Fac;
    public Integer num_Fa = 0;

    public static String valorIntent;

    Spinner spMonedas;
    List<String> spinnerMonedaArray = new ArrayList<>(  );
    RequestParams params2 = new RequestParams();
    RequestParams params4 = new RequestParams();

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    public Double importeInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_factura_cobranza);

        context = this;
        Utilidad = new Utilidades(context);

        facturaVal = false;
        GuardarDatosUsuario.Contexto = context;

        if (WebService.USUARIOLOGEADO != null) {
            if (Utilidad.isNetworkAvailable()) {
                try {

                    try {
                        valorIntent = getIntent().getStringExtra("intent");
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }

                    Importe = findViewById(R.id.Importe);
                    importeInt = Double.parseDouble(WebService.reto_Interes);

                    NomEmp = findViewById(R.id.NomEmp);
                    NomEmp.setText(WebService.clienteActual.getNomEmp().trim());
                    WebService.usuarioActual.setEmpresa(WebService.clienteActual.getCodEmp());
                    NomEmp.setTypeface(null, Typeface.BOLD);

                    LblUsu = findViewById(R.id.LblUsu);
                    LblUsu.setText(getResources().getString(R.string.usuarioTool) + WebService.USUARIOLOGEADO);

                    final String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                    LblFecha = (TextView) findViewById(R.id.LblFecha);
                    LblFecha.setText(timeStamp);

                    validarFac = findViewById(R.id.validarFac);
                    retorno = findViewById(R.id.retorno);
                    retorno.setVisibility(View.GONE);

                    spMonedas = findViewById(R.id.SPMonedas);
                    spMonedas.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, spinnerMonedaArray);

                    for (int i = 0; i < WebService.monedas.size(); i++) {
                        String nombreAgregar1 = WebService.monedas.get(i).getNom_Moneda();
                        spinnerMonedaArray.add(nombreAgregar1);
                    }
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMonedas.setAdapter(dataAdapter2);

                    spMonedas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (spMonedas != null) {
                                if (spMonedas.getSelectedItem() != null) {
                                    if (spMonedas.getSelectedItem().toString() != null) {
                                        String moneda = spMonedas.getSelectedItem().toString().trim();
                                        Moneda instanciaMoneda = new Moneda();
                                        for (int i = 0; i < WebService.monedas.size(); i++) {
                                            instanciaMoneda = WebService.monedas.get(i);
                                            if (instanciaMoneda.getNom_Moneda().trim().equals(moneda)) {
                                                WebService.monedaSeleccionada = instanciaMoneda;
                                                break;
                                            }
                                        }
                                        if(WebService.cod_monedaInteres.trim().equals("1")){
                                       // if(WebService.clienteActual.getCod_Moneda().trim().equals("1")){
                                            if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")){
                                                //GUARANIES AMBOS
                                                importeInt = Double.parseDouble(WebService.reto_Interes);
                                                Importe.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                importeInt = Utilidad.redondearDecimales(importeInt, 0);
                                                Importe.setText(importeInt.toString());
                                            }else {
                                                importeInt = Double.parseDouble(WebService.reto_Interes);
                                                Importe.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                                importeInt = importeInt * WebService.tipoCambio;
                                                importeInt = Utilidad.redondearDecimales(importeInt, 2);
                                                Importe.setText(importeInt.toString());
                                            }
                                        }else {
                                            if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("2")){
                                                //DOLARES AMBOS
                                                importeInt = Double.parseDouble(WebService.reto_Interes);
                                                Importe.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                                importeInt = importeInt / WebService.tipoCambio;
                                                importeInt = Utilidad.redondearDecimales(importeInt, 2);
                                                Importe.setText(importeInt.toString());
                                            }else {
                                                importeInt = Double.parseDouble(WebService.reto_Interes);
                                                Importe.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                importeInt = Utilidad.redondearDecimales(importeInt, 0);
                                                Importe.setText(importeInt.toString());
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    atras = findViewById(R.id.btnAtras);
                    atras.setClickable(true);
                    atras.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(context);

                           /* if(valorIntent.equals("Recibo")) {
                                if (WebService.usuarioActual.getTipoCobrador().equals("D")) {
                                    nro_viaje = WebService.viajeSeleccionadoCobrador.getNumViaje().trim();
                                }
                            }


                            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            Criteria criteria = new Criteria();
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

                            WebService.EstadoActual = 5;

                            params4.put("usuario", WebService.USUARIOLOGEADO);//1
                            params4.put("latitud_actual", String.valueOf(WebService.lat_actual));
                            params4.put("longitud_actual", String.valueOf(WebService.long_actual));
                            params4.put("latitud_destino", String.valueOf(WebService.clienteActual.getLatiud_Ubic()));
                            params4.put("longitud_destino", String.valueOf(WebService.clienteActual.getLongitud_Ubic()));
                            params4.put("latitud_origen", String.valueOf(WebService.lat_origen));
                            params4.put("longitud_origen", String.valueOf(WebService.long_origen));
                            params4.put("en_pausa", WebService.EstadoActual);//12
                            params4.put("nro_viaje", nro_viaje);//9
                            params4.put("cod_sucursal", "0");//10
                            params4.put("nro_orden", "0");//13
                            params4.put("nom_cliente",WebService.clienteActual.getNom_Tit().trim());//11
                            params4.put("distancia_a_recorrer", WebService.distancia_a_recorrer);
                            ActualizarUbicacion taskact = new ActualizarUbicacion(new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {*/
                           try{
                            if (valorIntent.equals("Recibo")) {
                                if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D")) {
                                    params2.put("num_viaje", WebService.viajeSeleccionadoCobrador.getNumViaje());
                                    params2.put("username", WebService.USUARIOLOGEADO);
                                    TraerClientesViajes task = new TraerClientesViajes(new AsyncResponse() {
                                        @Override
                                        public void processFinish(Object output) {
                                            for (int i = 0; i < WebService.clienteTraidos.size(); i++) {
                                                ClienteCobranza cliente = WebService.clienteTraidos.get(i);
                                                WebService.clienteActual = cliente;
                                                break;
                                            }
                                            WebService.intereses = false;
                                            Intent myIntent = new Intent(context, MenuCobranzas.class);
                                            startActivity(myIntent);
                                        }

                                    });
                                    task.execute();
                                } else {
                                    Intent myIntent = new Intent(context, SeleccionCliente.class);
                                    startActivity(myIntent);
                                }
                            }else{
                                Intent myIntent = new Intent(context, RecibosGenerados.class);
                                startActivity(myIntent);
                            }
                           }catch (Exception ex){
                               ex.printStackTrace();
                           }
                        }
                           /* });
                            taskact.execute();
                        }*/
                    });

                    CodTit = findViewById(R.id.CodTit);
                    NomTit = findViewById(R.id.NomTit);
                    CodTit.setText(WebService.clienteActual.getCod_Tit_Gestion().trim());
                    NomTit.setText(WebService.clienteActual.getNom_Tit().trim());

                    EdtCodSucTribut = findViewById(R.id.EdtCodSucTribut);
                    EdtCodSucTribut.setInputType(InputType.TYPE_CLASS_NUMBER);
                    EdtCodSucTribut.setText(WebService.cod_suc_tribut);

                    EdtCodFacTribut = findViewById(R.id.EdtCodFacTribut);
                    EdtCodFacTribut.setInputType(InputType.TYPE_CLASS_NUMBER);
                    EdtCodFacTribut.setText(WebService.cod_fac_trubut);

                    EdtNumFactu = findViewById(R.id.EdtNumFactu);
                    EdtNumFactu.setInputType(InputType.TYPE_CLASS_NUMBER);
                    EdtNumFactu.setText(WebService.nro_sugerido);

                    cod_Suc = EdtCodSucTribut.getText().toString().trim();
                    cod_Fac = EdtCodFacTribut.getText().toString().trim();
                    String num_Fac = EdtNumFactu.getText().toString().trim();
                    if(!num_Fac.equals("")){
                        num_Fa = Integer.valueOf(num_Fac);
                    }

                    EdtCodSucTribut.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String str = EdtCodSucTribut.getText().toString();
                            if (str.isEmpty()) return;
                            String str2 = Utilidad.PerfectDecimal(str, 3, 0);
                            if (!str2.equals(str)) {
                                EdtCodSucTribut.setText(str2);
                            }
                            noValido();
                        }
                    });

                    EdtCodFacTribut.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String str = EdtCodFacTribut.getText().toString();
                            if (str.isEmpty()) return;
                            String str2 = Utilidad.PerfectDecimal(str, 3, 0);
                            if (!str2.equals(str)) {
                                EdtCodFacTribut.setText(str2);
                            }
                            noValido();
                        }
                    });

                    EdtNumFactu.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            /*String str = EdtNumFactu.getText().toString();
                            if (str.isEmpty()) return;
                            String str2 = Utilidad.PerfectDecimal(str, 4, 0);
                            if (!str2.equals(str)) {
                                EdtNumFactu.setText(str2);
                            }*/
                            noValido();
                        }
                    });

                    Importe.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String str = Importe.getText().toString();
                            if (str.isEmpty()) return;
                            String str2 = Utilidad.PerfectDecimal(str, 10000, 2);
                            if (!str2.equals(str)) {
                                Importe.setText(str2);
                            }
                        }
                    });

                    validarFac.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(context);
                            if (Utilidad.isNetworkAvailable()) {
                                cod_Suc = EdtCodSucTribut.getText().toString().trim();
                                cod_Fac = EdtCodFacTribut.getText().toString().trim();
                                num_Fa = Integer.valueOf(EdtNumFactu.getText().toString().trim());
                                String importe = Importe.getText().toString();

                                if(WebService.monedaSeleccionada.getCod_Moneda().equals("1")){
                                   if(importe.contains(".")){
                                      importe = Utilidad.NumeroSinPunto(importe);
                                   }
                                }
                                importe = Utilidad.NumeroSinComa(importe);

                                if (!facturaVal) {
                                    ValidarFacturaCobranza task = new ValidarFacturaCobranza(new AsyncResponse() {
                                        @Override
                                        public void processFinish(Object output) {
                                            try {
                                                if (!WebService.errToken.equals("")) {
                                                    Intent myIntent = new Intent(context, Login.class);
                                                    startActivity(myIntent);
                                                }else {
                                                    if (WebService.n_factu.getFact().equals("Factura Invalida")) {
                                                        //esto no lo está haciendo
                                                        String novalida = getResources().getString(R.string.Ndispo) + " " + cod_Suc + "-" + cod_Fac + "-" + num_Fa;
                                                        retorno.setVisibility(View.VISIBLE);
                                                        retorno.setText(novalida);
                                                        validarFac.setText("Validar factura");
                                                        facturaVal = false;
                                                    } else {
                                                        retorno.setVisibility(View.VISIBLE);
                                                        retorno.setText(getResources().getString(R.string.dispo) + " " + cod_Suc + "-" + cod_Fac + "-" + num_Fa);
                                                        validarFac.setText(getResources().getString(R.string.IngresoVal));
                                                        facturaVal = true;
                                                    }
                                                }
                                            } catch (Exception exc) {
                                                retorno.setVisibility(View.VISIBLE);
                                                retorno.setText(exc.toString());
                                                exc.printStackTrace();
                                                //Toast.makeText( getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG ).show();
                                            }
                                        }
                                    });
                                    task.execute();
                                }
                                if (facturaVal) {

                                    WebService.totalDeudas = 0D;
                                    WebService.limpiarValores();
                                    WebService.valoresPago.clear();
                                    WebService.clienteInteres.clear();

                                    //INGRESO EL DOCUMENTO
                                    ClienteCobranza deudor = new ClienteCobranza();
                                    String cod_doca = "intcont";
                                    String mon = WebService.monedaSeleccionada.getCod_Moneda().trim();

                                    deudor.setCod_Moneda(mon);
                                    deudor.setCod_Docum(cod_doca);
                                    deudor.setCod_dpto(EdtCodSucTribut.getText().toString());
                                    deudor.setNro_Docum(EdtNumFactu.getText().toString());
                                    deudor.setFecha_Vence(LblFecha.getText().toString());
                                    deudor.setSerie_docum(EdtCodFacTribut.getText().toString());
                                    deudor.setImp_mov_mo(Double.valueOf(importe));

                                    WebService.clienteInteres.add(deudor);
                                    WebService.totalDeudas = Double.valueOf(importe);

                                    if (Importe.getText().equals("") || EdtCodSucTribut.getText().equals("") || EdtNumFactu.getText().equals("") || LblFecha.getText().equals("") || EdtCodFacTribut.getText().equals("")) {
                                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.ToastText3) + WebService.reto_AgregaCobranza, Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                                        toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                                        toast.show();//showing the toast is important**
                                    } else {
                                        WebService.intereses = true;
                                        WebService.nro_ingresado = EdtNumFactu.getText().toString();
                                        Intent myIntent = new Intent(context, IngresarValores.class);
                                        myIntent.putExtra("intent", valorIntent);
                                        startActivity(myIntent);
                                    }
                                }
                            } else {
                                Utilidad.dispalyAlertConexion(context);
                            }
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                    //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                }
            }else {
                Utilidad.CargarToastConexion(context);
            }
        }
    }

    private class  TraerClientesViajes extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context);
        public AsyncResponse delegate = null;//Call back interface
        public TraerClientesViajes(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            if(Utilidad.isNetworkAvailable()) {
                WebService.TraerClientesViajes(params2, "Viajes/ViajesCobrador/TraerClientesViajes.php");
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
            if (!WebService.errToken.equals("")) {
                Intent myIntent = new Intent(context, Login.class);
                startActivity(myIntent);
            }
        }
        @Override
        protected void onProgressUpdate(Void... values){        }
    }

    private class ValidarFacturaCobranza extends AsyncTask<String,Void,Void> {
        public AsyncResponse delegate = null;//Call back interface
        public ValidarFacturaCobranza(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... strings) {
            params = new RequestParams();
            params.add( "cod_suc_trib", cod_Suc);
            params.add( "cod_fac_tribut", cod_Fac );
            params.add( "numfac", num_Fa.toString());
            params.add("cod_emp", WebService.clienteActual.getCodEmp().trim());
            WebService.validarFacturaCobranza( "Facturas/ValidarFactura.php",params );
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
        try{
        Utilidad.vibraticionBotones(context);
        nro_viaje = "0";

        if(valorIntent.equals("Recibo")) {
            if (WebService.usuarioActual.getTipoCobrador().equals("D")) {
                nro_viaje = WebService.viajeSeleccionadoCobrador.getNumViaje();
            }
            params2.put("num_viaje", nro_viaje);
            params2.put("username", WebService.USUARIOLOGEADO);
            if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D")) {
                TraerClientesViajes task = new TraerClientesViajes(new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        WebService.intereses = false;
                        Intent myIntent = new Intent(context, MenuCobranzas.class);
                        startActivity(myIntent);
                    }

                });
                task.execute();
            } else {
                Intent myIntent = new Intent(context, SeleccionCliente.class);
                startActivity(myIntent);
            }
        }else{
            Intent myIntent = new Intent(context, RecibosGenerados.class);
            startActivity(myIntent);
        }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private class ActualizarUbicacion extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface
        public ActualizarUbicacion(AsyncResponse asyncResponse) {
            delegate = asyncResponse;
        }
        @Override
        protected Void doInBackground(String... strings) {
            WebService.ActualizarUbicacion( params4, "Viajes/ActualizarUbicacion.php" );
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
    }

    private void noValido(){
        retorno.setText( "" );
        validarFac.setText( "Validar factura" );
        facturaVal = false;
    }
}
