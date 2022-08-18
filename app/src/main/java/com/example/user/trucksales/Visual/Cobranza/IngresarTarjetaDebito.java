package com.example.user.trucksales.Visual.Cobranza;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.ClienteCobranza;
import com.example.user.trucksales.Encapsuladoras.Moneda;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.Encapsuladoras.TarjetaCredito;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.adapter.AdaptadorTarjetaDebito;
import com.example.user.trucksales.Encapsuladoras.TarjetaDebito;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;

public class IngresarTarjetaDebito extends AppCompatActivity   implements AdapterView.OnItemSelectedListener{

    private Utilidades Utilidad;
    private static Context context;
    private static int val_tot=0;
    ImageView atras;
    private static EditText input;

    String cod_monedaTarjDeb = "";
    public int decimales = 1;

    private static Double total;
    private static int cont_filas = 0;

    private Button btnValores;
    private EditText tarjeta,voucher, importe,importeconvertido,cuotas;
    ListView tablaCheques;
    private AdaptadorTarjetaDebito dataAdapterRuta;

    Spinner spTarjeta;
    final List<String> spinnerTarjetaArray = new ArrayList<>(  );

    Spinner spMonedas;
    final List<String> spinnerMonedaArray = new ArrayList<>(  );

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

        setContentView( R.layout.activity_ingresar_tarjetadebito );


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        context = this;
        Utilidad = new Utilidades(context);
        if(WebService.usuarioActual!=null) {
            try {

                if (Utilidad.isNetworkAvailable()) {
                    tarjeta = findViewById(R.id.tarjeta);
                    voucher = findViewById(R.id.voucher);

                    importe = findViewById(R.id.importe);

           /* Double total = WebService.diferenciaValores;

            if (WebService.intereses == false) {
                if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")){
                    total = Utilidad.redondearDecimales(total, 0);
                    importe.setText(NumberFormat.getInstance(Locale.ITALY).format(total).toString());
                    importe.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                else {
                    total = Utilidad.redondearDecimales(total, 2);
                    importe.setText(total.toString());
                    importe.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }

            } else {
                importe.setText("");
            }*/

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

                                //DecimalFormat formatter = new DecimalFormat("#,###,###,###", new DecimalFormatSymbols(new Locale("pt", "BR")));
                                formatter.applyPattern("#,###,###,###.##");
                                String formattedString = formatter.format(longval);

                        /*DecimalFormat formatter = new DecimalFormat("#.###.###.###");
                        String formattedString = formatter.format(longval);*/

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

                    importeconvertido = findViewById(R.id.importeconvertido);
                    cuotas = findViewById(R.id.cuotas);
                    cuotas.setText("1");

                    tablaCheques = (ListView) findViewById(R.id.tabla);


                    dataAdapterRuta = new AdaptadorTarjetaDebito(IngresarTarjetaDebito.this, WebService.tarjetaDebitos);

                    // Desplegamos los elementos en el ListView
                    tablaCheques.setAdapter(dataAdapterRuta);

                    btnValores = findViewById(R.id.btnValores);

                    atras = findViewById(R.id.btnAtras);
                    atras.setClickable(true);
                    atras.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(context);
                            Intent myIntent = new Intent(v.getContext(), IngresarValores.class);
                            startActivity(myIntent);
                            finish();
                        }
                    });

                    btnValores.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(context);
                            agregar(dataAdapterRuta);
                            if (!dataAdapterRuta.isEmpty()) {
                                Intent myIntent = new Intent(context, IngresarValores.class);
                                startActivity(myIntent);
                            }
                        }
                    });

                    spTarjeta = findViewById(R.id.spTarjeta);
                    spTarjeta.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                    final ArrayAdapter<String> dataAdapterTarjeta = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerTarjetaArray);
                    spTarjeta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String bancoAux = dataAdapterTarjeta.getItem(position);
                            if (bancoAux.equals("Otra tarjeta")) {
                                tarjeta.setText("");
                                tarjeta.setVisibility(View.VISIBLE);
                            } else {
                                tarjeta.setText(bancoAux);
                                tarjeta.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    spMonedas = findViewById(R.id.spMonedas);
                    spMonedas.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                    final ArrayAdapter<String> dataAdapterMoneda = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerMonedaArray);

                   /* TraerDatos task = new TraerDatos(new AsyncResponse() {
                        public void processFinish(Object output) {*/
                            for (int i = 0; i < WebService.tarjetasDebito.size(); i++) {
                                String nombreAgregar1 = WebService.tarjetasDebito.get(i).getNom_tarjeta();
                                spinnerTarjetaArray.add(nombreAgregar1);
                            }
                            dataAdapterTarjeta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spTarjeta.setAdapter(dataAdapterTarjeta);
                            spTarjeta.setSelection(0);

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
                                        String monedaSelec = spMonedas.getSelectedItem().toString();
                                        Moneda instanciaMoneda = new Moneda();
                                        for (int x = 0; x < WebService.monedas.size(); x++) {
                                            instanciaMoneda = WebService.monedas.get(x);
                                            if (instanciaMoneda.getSim_Moneda().trim().equals(monedaSelec.trim())) {
                                                cod_monedaTarjDeb = instanciaMoneda.getCod_Moneda();
                                                break;
                                            }
                                        }
                                        total = WebService.diferenciaValores;
                                        if (cod_monedaTarjDeb.trim().equals("1")) {
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

                    /*    }
                    });
                    task.execute();*/
                }else{
                    Utilidad.CargarToastConexion(context);
                }
            }catch (Exception ex){
                ex.printStackTrace();
               // Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
            }
        }else  {
        Intent myIntent = new Intent( context, Login.class );
        startActivity( myIntent );
    }
    }

    private void agregar(AdaptadorTarjetaDebito dataAdapterRuta) {
        if (Utilidad.isNetworkAvailable()) {
            if (tarjeta.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Debe ingresar/ seleccionar la tarjeta", Toast.LENGTH_LONG).show();
            } else if (voucher.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Debe ingresar el voucher", Toast.LENGTH_LONG).show();
            } else if (importe.getText().toString().equals("") || importe.getText().toString().equals("0.0")) {
                Toast.makeText(getApplicationContext(), "Debe ingresar el monto", Toast.LENGTH_LONG).show();
            } else if (cuotas.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Debe ingresar las cuotas", Toast.LENGTH_LONG).show();
            } else if (Long.valueOf(cuotas.getText().toString()) < 1 || Long.valueOf(cuotas.getText().toString()) > 99) {
                Toast.makeText(getApplicationContext(), "Debe ingresar correctamente las cuotas", Toast.LENGTH_LONG).show();
            } else {

                importeconvertido.setText(importe.getText().toString());
                String cod_moneda;
                if (spMonedas.getSelectedItem().toString().equals(WebService.simboloMonedaTr.trim())) {
                    /*Double totalConvertido = Double.valueOf(importe.getText().toString()) * WebService.tipoCambio;
                    importeconvertido.setText(totalConvertido.toString());*/
                    cod_moneda = WebService.codMonedaTr;
                } else {
                    //importeconvertido.setText(importe.getText().toString());
                    cod_moneda = WebService.codMonedaNacional;
                }

                //Agregado BDL 12/7/19
                String cod_banco;
                cod_banco = WebService.tarjetasDebito.get( spTarjeta.getSelectedItemPosition() ).getCod_tarjeta();

                String importes = importe.getText().toString();
                String numVoucher = voucher.getText().toString();

                if(importes.contains(",")){
                    importes =  importes.replace(",", "");
                }
                if(cod_monedaTarjDeb.trim().equals("1")){
                    if (importes.contains(".")) {
                        importes = Utilidad.NumeroSinPunto(importes);
                    }
                }
                else {
                    importes = String.valueOf(Utilidad.redondearDecimales(Double.parseDouble(importes), 2));
                }

                for(int i = 0; i < WebService.tarjetaDebitos.size(); i++){
                    TarjetaDebito cod_moneda2 = WebService.tarjetaDebitos.get(i);
                    WebService.monedaTarjDebUlti = cod_moneda2.getCod_Mon();
                }

                WebService.addTarjetaDebito(new TarjetaDebito(cod_banco, Integer.parseInt(numVoucher), Double.valueOf(importes), cuotas.getText().toString(), cod_moneda));

                if(WebService.tarjetaDebitos.size()>1){
                    WebService.monedaTarjDeb = WebService.monedaSeleccionada;
                }else {
                    String monedaSelec = spMonedas.getSelectedItem().toString();
                    Moneda instanciaMoneda = new Moneda();
                    for (int x = 0; x < WebService.monedas.size(); x++) {
                        instanciaMoneda = WebService.monedas.get(x);
                        if (instanciaMoneda.getSim_Moneda().trim().equals(monedaSelec.trim())) {
                            WebService.monedaTarjDeb = instanciaMoneda;
                            break;
                        }
                    }
                }

                voucher.setText("");
                importe.setText("");
                cuotas.setText("");

                dataAdapterRuta.notifyDataSetChanged();
            }

        }
    }

    public void refreshList() {

        dataAdapterRuta.notifyDataSetChanged();
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
                WebService.ObtenerDatosTarjetaDebito();
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
        }

        @Override
        protected void onProgressUpdate(Void... values)
        {

        }
    }

    @Override
    public void onBackPressed() {
        Utilidad.vibraticionBotones(context);
        Intent myIntent = new Intent(context, IngresarValores.class);
        startActivity(myIntent);
        finish();
    }
}
