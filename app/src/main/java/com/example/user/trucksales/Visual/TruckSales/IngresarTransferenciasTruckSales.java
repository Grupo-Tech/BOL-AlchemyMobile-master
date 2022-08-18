package com.example.user.trucksales.Visual.TruckSales;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.Banco;
import com.example.user.trucksales.Encapsuladoras.Moneda;
import com.example.user.trucksales.Encapsuladoras.Transferencias;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.adapter.AdaptadorTransferenciasTruckSales;
import com.loopj.android.http.RequestParams;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IngresarTransferenciasTruckSales extends AppCompatActivity   implements AdapterView.OnItemSelectedListener{


    private Utilidades Utilidad;
    private static Context context;
    private static int val_tot=0;
    ImageView atras;
    private static EditText input;
    /*
    Spinner spMonedas;
    final List<String> spinnerMonedaArray = new ArrayList<>(  );*/

    private static int cont_filas = 0;

    public int decimales = 1;

    private Button btnValores;
    private EditText banco,bancodestino,cuentadestino,detalle,moneda,importe;
    ListView tablaCheques;
    private AdaptadorTransferenciasTruckSales dataAdapterRuta;

    private Double total;

    String cod_monedaTransferencia = "";
    String monedaSelec = "";

    AutoCompleteTextView TxtNomBanco,TxtCodBanco;
    public List<String> editTextArray =  new ArrayList<String>();
    public List<String> Codigos = new ArrayList<>(  );

    Spinner spMonedas;
    List<String> spinnerMonedaArray = new ArrayList<>(  );

    Spinner spBancosDestino;
    List<String> spinnerBancoDestinoArray = new ArrayList<>(  );

    Spinner spCuentaDestino;
    List<String> spinnerCuentaDestinoArray = new ArrayList<>(  );

    @Override
    protected void onResume()
    {
        super.onResume();

        val_tot = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
      //  this.requestWindowFeature( Window.FEATURE_NO_TITLE );

        setContentView( R.layout.activity_ingresar_transferencias );


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        context = this;
        Utilidad = new Utilidades(context);

        if(WebService.usuarioActual!=null) {

            try {
                if (Utilidad.isNetworkAvailable()) {
                    banco = findViewById(R.id.banco);
                    bancodestino = findViewById(R.id.bancodestino);
                    cuentadestino = findViewById(R.id.cuentadestino);
                    spMonedas = findViewById(R.id.spMonedas);
                    detalle = findViewById(R.id.detalle);
                    detalle.setInputType(InputType.TYPE_CLASS_NUMBER);

                    TxtNomBanco = findViewById(R.id.TxtNomBanco);
                    TxtCodBanco = findViewById(R.id.TxtCodBanco);

                    spBancosDestino = findViewById( R.id.sp_bancodestino);
                    spCuentaDestino = findViewById( R.id.sp_cuentadestino);

                    moneda = findViewById(R.id.moneda);
                    importe = findViewById(R.id.importe);

                    if(WebService.configuracion.getTransf_completa().equals("N")){
                        banco.setVisibility(View.GONE);
                        bancodestino.setVisibility(View.GONE);
                        cuentadestino.setVisibility(View.GONE);
                        moneda.setVisibility(View.GONE);
                        spBancosDestino.setVisibility(View.GONE);
                        spCuentaDestino.setVisibility(View.GONE);
                        TxtNomBanco.setVisibility(View.GONE);
                        TxtCodBanco.setVisibility(View.GONE);
                        detalle.setVisibility(View.GONE);

                        spMonedas.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                        final ArrayAdapter<String> dataAdapterMoneda = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerMonedaArray);

                        for (int i = 0; i < WebService.monedasCheques.size(); i++) {
                            String nombreAgregar1 = WebService.monedasCheques.get(i).getSim_Moneda();
                            spinnerMonedaArray.add(nombreAgregar1);
                        }

                        dataAdapterMoneda.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spMonedas.setAdapter(dataAdapterMoneda);

                        spMonedas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    String monedaSelect = spMonedas.getSelectedItem().toString();
                                    Moneda instanciaMoneda = new Moneda();
                                    for (int x = 0; x < WebService.monedas.size(); x++) {
                                        instanciaMoneda = WebService.monedas.get(x);
                                        if (instanciaMoneda.getSim_Moneda().trim().equals(monedaSelect.trim())) {
                                            cod_monedaTransferencia = instanciaMoneda.getCod_Moneda().trim();
                                            monedaSelec = instanciaMoneda.getCod_Moneda().trim();
                                            break;
                                        }
                                    }
                                    total = WebService.diferenciaValores;
                                    if (cod_monedaTransferencia.trim().equals("1")) {
                                        decimales = 1;
                                        if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                                            total = Utilidad.redondearDecimales(total, 0);
                                            importe.setText(NumberFormat.getInstance(Locale.ITALY).format(total).toString());
                                            importe.setInputType(InputType.TYPE_CLASS_NUMBER);
                                        } else {
                                            total = total * WebService.tipoCambio;
                                            total = Utilidad.redondearDecimales(total, 0);
                                            importe.setText(NumberFormat.getInstance(Locale.ITALY).format(total).toString());
                                            importe.setInputType(InputType.TYPE_CLASS_NUMBER);
                                        }
                                    } else {
                                        decimales = 2;
                                        if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                                            total = total / WebService.tipoCambio;
                                            total = Utilidad.redondearDecimales(total, 2);
                                            importe.setText(total.toString());
                                            importe.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                        } else {
                                            total = Utilidad.redondearDecimales(total, 2);
                                            importe.setText(total.toString());
                                            importe.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
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
                    }else{
                        spMonedas.setVisibility(View.GONE);
                    }

                    importe.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            importe.removeTextChangedListener(this);

                            try {
                                String originalString = s.toString();

                                Long longval;
                                if (originalString.contains(",")) {
                                    originalString = originalString.replaceAll(",", "");
                                }
                                longval = Long.parseLong(originalString);

                                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                                formatter.applyPattern("#,###,###,###.##");
                                String formattedString = formatter.format(longval);

                                //setting text after format to EditText
                                importe.setText(formattedString);
                                importe.setSelection(importe.getText().length());
                            } catch (NumberFormatException nfe) {
                                nfe.printStackTrace();
                                //Toast.makeText( getApplicationContext(), nfe.getMessage(), Toast.LENGTH_LONG ).show();
                            }

                            importe.addTextChangedListener(this);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            try {
                                String str = importe.getText().toString();
                                if (str.isEmpty()) return;
                                if (decimales == 1) {
                                    str.replace(".", "");
                                }
                                String str2 = Utilidad.PerfectDecimal(str, 1000, decimales);
                                if (!str2.equals(str)) {
                                    if (decimales == 2) {
                                        importe.setText(str2);
                                    } else {
                                        importe.setText(NumberFormat.getInstance(Locale.ITALY).format(str2).toString());
                                    }
                                    int pos = importe.getText().length();
                                    importe.setSelection(pos);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                            }
                        }
                    });

                    tablaCheques = (ListView) findViewById(R.id.tabla);

                    dataAdapterRuta = new AdaptadorTransferenciasTruckSales(IngresarTransferenciasTruckSales.this, WebService.transferencias);

                    // Desplegamos los elementos en el ListView
                    tablaCheques.setAdapter(dataAdapterRuta);

                    btnValores = findViewById(R.id.btnValores);

                    atras = findViewById(R.id.btnAtras);
                    atras.setClickable(true);
                    atras.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(context);
                            Intent myIntent = new Intent(v.getContext(), IngresarValoresTruckSales.class);
                            startActivity(myIntent);
                            finish();
                        }
                    });

                    btnValores.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(context);
                            try {
                                agregarTransferencia(dataAdapterRuta);
                                if (!dataAdapterRuta.isEmpty()) {
                                    Intent myIntent = new Intent(context, IngresarValoresTruckSales.class);
                                    startActivity(myIntent);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                            }
                        }
                    });
                }else{
                    Utilidad.CargarToastConexion(context);
                }
            }catch (Exception ex){
                ex.printStackTrace();
                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
            }
        }else  {
            Intent myIntent = new Intent( context, Login.class );
            startActivity( myIntent );
        }


       /* spBancos = findViewById( R.id.spBancos );
        spBancos.setOnItemSelectedListener( (AdapterView.OnItemSelectedListener) this );
        final ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item, spinnerBancoArray );
        spBancos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String bancoAux = dataAdapter2.getItem(position);
                if(bancoAux.equals("Otro banco")){
                    banco.setText("");
                    banco.setVisibility(View.VISIBLE);
                }else{
                    banco.setText(bancoAux);
                    banco.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        spBancosDestino.setOnItemSelectedListener( (AdapterView.OnItemSelectedListener) this );
        final ArrayAdapter<String> dataAdapterBancosDestino = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item, spinnerBancoDestinoArray );

        spCuentaDestino.setOnItemSelectedListener( (AdapterView.OnItemSelectedListener) this );
        final ArrayAdapter<String> dataAdapterCuentaDestino = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item, spinnerCuentaDestinoArray);

        spBancosDestino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String bancoAux = dataAdapterBancosDestino.getItem(position);
                while (spinnerCuentaDestinoArray.size()>0){
                    spinnerCuentaDestinoArray.remove(0)   ;
                }

                if(bancoAux.equals("Otro banco")){
                    bancodestino.setText("");
                    bancodestino.setVisibility(View.VISIBLE);

                }else{
                    bancodestino.setText(bancoAux);
                    bancodestino.setVisibility(View.GONE);

                    for (int i = 0; i < WebService.bancosPropios.size(); i++) {
                        if(bancoAux.equals(WebService.bancosPropios.get( i ).getNom_banco())){
                            String nombreAgregar1 = WebService.bancosPropios.get( i ).getNrocta_banco();
                            spinnerCuentaDestinoArray.add( nombreAgregar1 );
                        }
                    }
                    dataAdapterCuentaDestino.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
                    spCuentaDestino.setAdapter( dataAdapterCuentaDestino );
                    spCuentaDestino.setSelection(0);
                }


                String nombreAgregar1 = "Otra cuenta";
                spinnerCuentaDestinoArray.add( nombreAgregar1 );
                dataAdapterCuentaDestino.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
                spCuentaDestino.setAdapter( dataAdapterCuentaDestino );
                spCuentaDestino.setSelection(0);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, editTextArray);
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Codigos);

        spCuentaDestino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String bancoAux = dataAdapterCuentaDestino.getItem(position);
                    if (bancoAux.equals("Otra cuenta")) {
                        cuentadestino.setText("");
                        cuentadestino.setVisibility(View.VISIBLE);

                    } else {
                        cuentadestino.setText(bancoAux);
                        cuentadestino.setVisibility(View.GONE);

                        for (int i = 0; i < WebService.bancosPropios.size(); i++) {
                            if (spBancosDestino.getSelectedItem().toString().equals(WebService.bancosPropios.get(i).getNom_banco()) &&
                                    spCuentaDestino.getSelectedItem().toString().equals(WebService.bancosPropios.get(i).getNrocta_banco())
                            ) {
                                monedaSelec = WebService.bancosPropios.get(i).getCod_moneda();
                                moneda.setText(WebService.bancosPropios.get(i).getNom_moneda());
                                //String moneda = spMonedas.getSelectedItem().toString().trim();
                                Moneda instanciaMoneda = new Moneda();
                                for (int x = 0; x < WebService.monedas.size(); x++) {
                                    instanciaMoneda = WebService.monedas.get(x);
                                    if (instanciaMoneda.getCod_Moneda().trim().equals(monedaSelec.trim())) {
                                        cod_monedaTransferencia = instanciaMoneda.getCod_Moneda();
                                        break;
                                    }
                                }

                                total = WebService.diferenciaValores;
                                if (cod_monedaTransferencia.trim().equals("1")) {
                                    decimales = 1;
                                    if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                                        total = Utilidad.redondearDecimales(total, 0);
                                        importe.setText(NumberFormat.getInstance(Locale.ITALY).format(total).toString());
                                        importe.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    } else {
                                        total = total * WebService.tipoCambio;
                                        total = Utilidad.redondearDecimales(total, 0);
                                        importe.setText(NumberFormat.getInstance(Locale.ITALY).format(total).toString());
                                        importe.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    }
                                } else {
                                    decimales = 2;
                                    if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                                        total = total / WebService.tipoCambio;
                                        total = Utilidad.redondearDecimales(total, 2);
                                        importe.setText(total.toString());
                                        importe.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                    } else {
                                        total = Utilidad.redondearDecimales(total, 2);
                                        importe.setText(total.toString());
                                        importe.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                   // Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*TraerDatos task = new TraerDatos(new AsyncResponse() {
            public void processFinish(Object output)            {*/
                /*for (int i = 0; i < WebService.bancos.size(); i++) {
                    String nombreAgregar1 = WebService.bancos.get( i ).getNom_banco();
                    spinnerBancoArray.add( nombreAgregar1 );
                }
                dataAdapter2.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
                spBancos.setAdapter( dataAdapter2 );
                spBancos.setSelection(0);*/

                for (int i = 0; i < WebService.bancos.size(); i++) {
                    String nombreAgregar = WebService.bancos.get(i).getNom_banco().trim();
                    editTextArray.add(nombreAgregar);

                    String cod = WebService.bancos.get(i).getCod_banco().trim();
                    Codigos.add(cod);
                }
                TxtNomBanco.setAdapter(adapter);

                /*for (int i = 0; i < WebService.bancos.size(); i++) {
                    String nombreAgregar = WebService.bancos.get(i).getCod_banco().trim();
                    Codigos.add(nombreAgregar);
                }*/
                TxtCodBanco.setAdapter(adapter2);

                TxtNomBanco.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Object item = parent.getItemAtPosition(position);
                        Banco instaCheque = new Banco();

                        boolean encontrado = false;
                        for (int i = 0; i < WebService.bancos.size() && !encontrado; i++) {
                            instaCheque = WebService.bancos.get(i);
                            if (instaCheque.getNom_banco().trim().equals(item)) {
                                encontrado = true;
                                TxtCodBanco.setText(instaCheque.getCod_banco().trim());
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

                TxtNomBanco.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

                TxtCodBanco.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Object item = parent.getItemAtPosition(position);
                        Banco instaBanco = new Banco();
                        boolean encontrado = false;
                        for (int i = 0; i < WebService.bancos.size() && !encontrado; i++) {
                            instaBanco = WebService.bancos.get(i);
                            if (instaBanco.getCod_banco().trim().equals(item)) {
                                encontrado = true;
                                TxtNomBanco.setText(instaBanco.getNom_banco().trim());
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

                TxtCodBanco.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

                for (int i = 0; i < WebService.bancosPropios.size(); i++) {
                    String nombreAgregar1 = WebService.bancosPropios.get( i ).getNom_banco();
                    if(!existeBanco(nombreAgregar1)){
                        spinnerBancoDestinoArray.add( nombreAgregar1 );
                    }
                }
                dataAdapterBancosDestino.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
                spBancosDestino.setAdapter( dataAdapterBancosDestino );
                spBancosDestino.setSelection(0);

           /* }
        } );
        task.execute( );*/
    }

    private boolean existeBanco(String nombre) {
        boolean exist=false;

        for (int i = 0; i < spinnerBancoDestinoArray.size(); i++) {
            if(spinnerBancoDestinoArray.get(i).equals(nombre)){
                exist=true;
                return true;
            }
        }

        return exist;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }



    public class  TraerDatos extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface
        public TraerDatos(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            if(Utilidad.isNetworkAvailable())   {
                RequestParams params2 = new RequestParams();
                if(WebService.usuarioActual.getTipoCobrador().equals("D")) {
                    params2.add("cod_empresa", WebService.clienteActual.getCodEmp().trim());
                }
                else {
                    params2.add("cod_empresa", WebService.usuarioActual.getEmpresa());
                }
                WebService.ObtenerDatosIngresarTransferencias(params2);
                return null;
            }
            else  {
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
        protected void onProgressUpdate(Void... values)
        {

        }
    }
    private void agregarTransferencia(AdaptadorTransferenciasTruckSales dataAdapterRuta) {
        if (Utilidad.isNetworkAvailable()) {
            try {
                if(WebService.configuracion.getTransf_completa().equals("S") && TxtCodBanco.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Debe ingresar/ seleccionar el banco", Toast.LENGTH_LONG).show();
                }else if (WebService.configuracion.getTransf_completa().equals("S") && detalle.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Debe ingresar la transaccion", Toast.LENGTH_LONG).show();
                } else if (WebService.configuracion.getTransf_completa().equals("S") && bancodestino.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Debe ingresar/ seleccionar el banco destino", Toast.LENGTH_LONG).show();
                } else if (WebService.configuracion.getTransf_completa().equals("S") && cuentadestino.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Debe ingresar/ seleccionar la cuenta destino", Toast.LENGTH_LONG).show();
                } else if (importe.getText().toString().equals("") || importe.getText().toString().equals("0.0")) {
                        Toast.makeText(getApplicationContext(), "Debe ingresar el monto", Toast.LENGTH_LONG).show();
                } else {

                /*String cuentadest;
                cuentadest = WebService.transferencias.get( spCuentaDestino.getSelectedItemPosition() ).getBancodestino();*/

                    String importes = importe.getText().toString();

                    String monedas = "";
                    String cod_moneda = cod_monedaTransferencia;

                    /*int cod_moneda;
                    if (monedas.trim().equals("Guaranies") || monedas.trim().equals("Guaraníes")) {
                        cod_moneda = 1;
                    } else {
                        cod_moneda = 2;
                    }*/

                    if(importes.contains(",")){
                        importes =  importes.replace(",", "");
                    }
                    if(cod_monedaTransferencia.trim().equals("1")){
                        if (importes.contains(".")) {
                            importes = Utilidad.NumeroSinPunto(importes);
                        }
                    }
                    else {
                        importes = String.valueOf(Utilidad.redondearDecimales(Double.parseDouble(importes), 2));
                    }

                    for (int i = 0; i < WebService.transferencias.size(); i++) {
                        Transferencias cod_moneda2 = WebService.transferencias.get(i);
                        WebService.monedaTransferenciaUlti = cod_moneda2.getCodMon();
                    }

                    String cuentaDestino = "0";
                    String bancDestino = "0";
                    String cod_banco = "0";
                    String operacion = "0";

                    if(WebService.configuracion.getTransf_completa().equals("S")){
                        monedas = moneda.getText().toString();

                        if (monedas.trim().equals("Guaranies") || monedas.trim().equals("Guaraníes")) {
                            cod_moneda = "1";
                        } else {
                            cod_moneda = "2";
                        }

                        cod_banco = TxtCodBanco.getText().toString().trim();/*WebService.bancos.get(spBancos.getSelectedItemPosition()).getCod_banco();*/
                        cuentaDestino = cuentadestino.getText().toString();
                        bancDestino = bancodestino.getText().toString();
                        operacion = detalle.getText().toString();
                    }

                    WebService.addTransferencia(new Transferencias(cod_banco, operacion, bancDestino,
                            cuentaDestino, Double.valueOf(importes), String.valueOf(cod_moneda)));

                    if(WebService.transferencias.size()>1){
                        WebService.monedaTransferencia = WebService.monedaSeleccionada;
                    }else {
                        Moneda instanciaMoneda = new Moneda();
                        for (int x = 0; x < WebService.monedas.size(); x++) {
                            instanciaMoneda = WebService.monedas.get(x);
                            if (instanciaMoneda.getCod_Moneda().trim().equals(monedaSelec.trim())) {
                                WebService.monedaTransferencia = instanciaMoneda;
                                break;
                            }
                        }
                    }

                    detalle.setText("");
                    importe.setText("");

                    dataAdapterRuta.notifyDataSetChanged();
                }

            }catch (Exception ex)
            {
                ex.printStackTrace();
                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
            }
        }
    }


    public void refreshList() {

        dataAdapterRuta.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Utilidad.vibraticionBotones(context);
        Intent myIntent = new Intent(context, IngresarValoresTruckSales.class);
        startActivity(myIntent);
        finish();
    }
}
