package com.example.user.trucksales.Visual.Cobranza;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.Credito_Suspenso;
import com.example.user.trucksales.Encapsuladoras.Moneda;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.adapter.AdaptadorCredSuspenso;
import com.loopj.android.http.RequestParams;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IngresarCredSuspenso extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Utilidades Utilidad;
    private static Context context;

    private static int val_tot=0;
    ImageView atras;
    private Button btnValores;

    RequestParams params1 = new RequestParams(  );
    ListView tablaCreditos;

    private AdaptadorCredSuspenso dataAdapterRuta;

    Spinner spMonedas;
    List<String> spinnerMonedaArray = new ArrayList<>(  );

    Spinner spCreditos;
    List<String> spinnerCreditoSus = new ArrayList<>();

    private TextView fecha, TituloMon;
    private EditText importe;

    public int decimales = 1;

    public void refreshList() {

        dataAdapterRuta.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView( R.layout.activity_credito_suspenso );

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        context = this;

        Utilidad = new Utilidades(context);
        if(WebService.usuarioActual!=null) {
            try {
                if (Utilidad.isNetworkAvailable()) {
                    TituloMon = findViewById(R.id.TituloMon);
                    fecha = findViewById(R.id.fecha);
                    fecha.setVisibility(View.GONE);

                    importe = findViewById(R.id.importe);
                    importe.setVisibility(View.GONE);

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
                                //.makeText( getApplicationContext(), nfe.getMessage(), Toast.LENGTH_LONG ).show();
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

                    tablaCreditos = (ListView) findViewById(R.id.tabla);

                    dataAdapterRuta = new AdaptadorCredSuspenso(IngresarCredSuspenso.this, WebService.creditoSus);

                    // Desplegamos los elementos en el ListView
                    tablaCreditos.setAdapter(dataAdapterRuta);

                    btnValores = findViewById(R.id.btnValores);

                    spMonedas = findViewById(R.id.spMonedas);
                    spMonedas.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                    final ArrayAdapter<String> dataAdapterMoneda = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerMonedaArray);

                    spCreditos = findViewById(R.id.spCreditos);
                    spCreditos.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                    final ArrayAdapter<String> dataAdapterCreditos = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerCreditoSus);

                    for (int i = 0; i < WebService.monedas.size(); i++) {
                        String nombreAgregar1 = WebService.monedas.get(i).getNom_Moneda();
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
                                    if (instanciaMoneda.getNom_Moneda().trim().equals(monedaSelec.trim())) {
                                        WebService.monedaCredSusp = instanciaMoneda;
                                        break;
                                    }
                                }

                                TraerCreditoSuspenso task = new TraerCreditoSuspenso(new AsyncResponse() {
                                    @Override
                                    public void processFinish(Object output) {
                                        if (!WebService.errToken.equals("")) {
                                            Intent myIntent = new Intent(context, Login.class);
                                            startActivity(myIntent);
                                        }else {
                                            if (WebService.ArrayCredSus.size() > 0) {
                                                spinnerCreditoSus.clear();
                                                for (int i = 0; i < WebService.ArrayCredSus.size(); i++) {
                                                    String numero = String.valueOf(WebService.ArrayCredSus.get(i).getNumero());
                                                    String importe = String.valueOf(WebService.ArrayCredSus.get(i).getImporte());
                                                    String fecha = String.valueOf(WebService.ArrayCredSus.get(i).getFecha());
                                                    spinnerCreditoSus.add(numero + " - " + importe + " - " + fecha);
                                                }
                                                dataAdapterCreditos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spCreditos.setAdapter(dataAdapterCreditos);
                                            } else {
                                                spinnerCreditoSus.clear();

                                                String prueba = getResources().getString(R.string.Sindatos);
                                                spinnerCreditoSus.add(prueba);

                                                fecha.setVisibility(View.GONE);
                                                importe.setVisibility(View.GONE);

                                                dataAdapterCreditos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spCreditos.setAdapter(dataAdapterCreditos);
                                            }
                                        }
                                    }
                                });
                                task.execute();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    spCreditos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                String creditoSelect = spCreditos.getSelectedItem().toString();

                                Credito_Suspenso instanciaCredito = new Credito_Suspenso();
                                for (int x = 0; x < WebService.ArrayCredSus.size(); x++) {
                                    instanciaCredito = WebService.ArrayCredSus.get(x);
                                    String creditoSusp = instanciaCredito.getNumero() + " - " + instanciaCredito.getImporte() + " - " + instanciaCredito.getFecha();
                                    if (creditoSusp.equals(creditoSelect.trim())) {
                                        WebService.CredSeleccionado = instanciaCredito;
                                        importe.setVisibility(View.VISIBLE);
                                        fecha.setVisibility(View.VISIBLE);

                                        Double total = instanciaCredito.getImporte();
                                        if (WebService.CredSeleccionado.getCod_Moneda().trim().equals("1")) {
                                            decimales = 1;
                                            total = Utilidad.redondearDecimales(total, 0);
                                            importe.setText(NumberFormat.getInstance(Locale.ITALY).format(total).toString());
                                            importe.setInputType(InputType.TYPE_CLASS_NUMBER);
                                        } else {
                                            decimales = 2;
                                            total = Utilidad.redondearDecimales(total, 2);
                                            importe.setText(total.toString());
                                            importe.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                        }

                                        // importe.setText(instanciaCredito.getImporte().toString());
                                        fecha.setText(instanciaCredito.getFecha().toString());
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

                    btnValores.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(context);
                            agregarCreditoS(dataAdapterRuta);
                            if (!dataAdapterRuta.isEmpty()) {
                                Intent myIntent = new Intent(context, IngresarValores.class);
                                startActivity(myIntent);
                            }
                        }
                    });

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
    }

    private void agregarCreditoS(AdaptadorCredSuspenso dataAdapterRuta) {
        if (Utilidad.isNetworkAvailable()) {
            try {
                if (importe.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Debe ingresar el monto", Toast.LENGTH_LONG).show();
                } else {

                    //gs
                    String importe2 = importe.getText().toString();
                    if(WebService.CredSeleccionado.getCod_Moneda().trim().equals("1")) {
                        if (importe2.contains(".")) {
                            importe2 = Utilidad.NumeroSinPunto(importe2);
                        }
                    }
                    Double importe3 = Double.parseDouble(importe2);

                    //usd
                    Double importa = Double.parseDouble(importe2);

                    if(WebService.CredSeleccionado.getCod_Moneda().trim().equals("1")){

                        importa = Double.valueOf(Utilidad.NumeroSinPunto(importe2));

                        importa = importa / WebService.tipoCambio;

                        importa = Utilidad.redondearDecimales(importa, 2);

                       /* if (importe2.contains(".")) {
                            importe2 = Utilidad.NumeroSinPunto(importe2);
                        }*/

                    }else{
                        importe3 = importe3 * WebService.tipoCambio;
                        importe3 = Utilidad.redondearDecimales(importe3, 0);
                        importe2 = String.valueOf(importe3);
                        importa = Utilidad.redondearDecimales(importa, 2);
                    }

                    if(importe2.contains(",")){
                        importe2 = importe2.replace(",", "");
                    }

                  /*  if(WebService.monedaCredSusp.getCod_Moneda().trim().equals("1")){
                       if (importe2.contains(".")) {
                            importe2 = Utilidad.NumeroSinPunto(importe2);
                        }
                    }
                    else {
                        importe2 = String.valueOf(Utilidad.redondearDecimales(Double.parseDouble(importe2), 2));
                    }*/

                   /* if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("2") || WebService.CredSeleccionado.getCod_Moneda().trim().equals("1")) {
                        importa = importa / WebService.tipoCambio;
                        importa = Utilidad.redondearDecimales(importa, 2);
                    }*/



                   WebService.addCreditoSus(new Credito_Suspenso(WebService.CredSeleccionado.getNumero(),fecha.getText().toString(), Double.valueOf(importe2),WebService.CredSeleccionado.getCod_Moneda(), importa));

                    importe.setText("");
                    fecha.setText("");

                    dataAdapterRuta.notifyDataSetChanged();
                }
            }catch (Exception ex){
                ex.printStackTrace();
                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Utilidad.vibraticionBotones(context);
        Intent myIntent = new Intent(context, IngresarValores.class);
        startActivity(myIntent);
        finish();
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

    private class TraerCreditoSuspenso extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface
        public TraerCreditoSuspenso(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            params1 = new RequestParams(  );
            params1.put( "cod_emp",WebService.usuarioActual.getEmpresa().trim());
            params1.put( "cod_tit",WebService.clienteActual.getCod_Tit_Gestion().trim() );
            params1.put( "cod_moneda",WebService.monedaCredSusp.getCod_Moneda().trim() );
            WebService.TraerCredSus("Cobranzas/TraerCredSusp.php", params1);
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
}
