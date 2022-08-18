package com.example.user.trucksales.Visual.Cobranza;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.Vuelto;
import com.example.user.trucksales.Encapsuladoras.Moneda;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.adapter.AdaptadorVuelto;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class IngresarVuelto extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    private Utilidades Utilidad;
    private static Context context;

    private AdaptadorVuelto dataAdapterRuta;

    private static int val_tot=0;
    ImageView btnAtras;
    private static EditText nroDoc, importe, fecha;
    ListView tablaVuelto;

    String cod_monedaVuelto = "";

    Spinner spMonedas;
    List<String> spinnerMonedaArray = new ArrayList<>(  );

    private static Double total;

    Button btnValores;

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();
    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    private Date fechaSeleccionada;
    private static final String CERO = "0";
    private static final String BARRA = "/";

    public int decimales = 1;

    @Override
    protected void onResume() {
        super.onResume();
        val_tot = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  this.requestWindowFeature( Window.FEATURE_NO_TITLE );

        setContentView(R.layout.activity_ingresar_vuelto);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        context = this;

        Utilidad = new Utilidades(context);

        if (WebService.usuarioActual != null) {
            try {
                if (Utilidad.isNetworkAvailable()) {
                    importe = findViewById(R.id.importe);

                    total = WebService.acuentaValores;

                    spMonedas = findViewById(R.id.spMonedas);
                    spMonedas.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                    final ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, spinnerMonedaArray);

                    try {
                        for (int i = 0; i < WebService.monedasCheques.size(); i++) {
                            String nombreAgregar1 = WebService.monedasCheques.get(i).getSim_Moneda();
                            spinnerMonedaArray.add(nombreAgregar1);
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                        //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                    }

                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMonedas.setAdapter(dataAdapter2);

                    spMonedas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                String monedaSelec = spMonedas.getSelectedItem().toString();
                                Moneda instanciaMoneda = new Moneda();
                                for (int x = 0; x < WebService.monedas.size(); x++) {
                                    instanciaMoneda = WebService.monedas.get(x);
                                    if (instanciaMoneda.getSim_Moneda().trim().equals(monedaSelec.trim())) {
                                        cod_monedaVuelto = instanciaMoneda.getCod_Moneda();
                                        break;
                                    }
                                }
                                total = WebService.acuentaValores;
                                if (cod_monedaVuelto.trim().equals("1")) {
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

                                /*if(cod_monedaVuelto.trim().equals("2")){
                                    decimales = 2;
                                    importe.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                }
                                else{
                                    importe.setInputType(InputType.TYPE_CLASS_NUMBER);
                                }*/

                            } catch (Exception ex) {
                                ex.printStackTrace();
                                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    /*if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("2")) {
                        decimales = 2;
                        importe.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        total = Utilidad.redondearDecimales(total, 2);
                        importe.setText(total.toString());
                    } else {
                        decimales = 1;
                        total = Utilidad.redondearDecimales(total, 0);
                        importe.setText(NumberFormat.getInstance(Locale.ITALY).format(total).toString());
                        importe.setInputType(InputType.TYPE_CLASS_NUMBER);
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

                    tablaVuelto = (ListView) findViewById(R.id.tabla);

                    dataAdapterRuta = new AdaptadorVuelto(IngresarVuelto.this, WebService.Vuelto);

                    // Desplegamos los elementos en el ListView
                    tablaVuelto.setAdapter(dataAdapterRuta);

                    btnValores = findViewById(R.id.btnValores);

                    btnAtras = findViewById(R.id.btnAtras);
                    btnAtras.setClickable(true);
                    btnAtras.setOnClickListener(new View.OnClickListener() {
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
                }else{
                    Utilidad.CargarToastConexion(context);
                }
            }catch(Exception ex)
            {
                ex.printStackTrace();
                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
            }
        }else  {
            Intent myIntent = new Intent( context, Login.class );
            startActivity( myIntent );
        }
    }

    private void agregar(AdaptadorVuelto dataAdapterRuta) {
        if (Utilidad.isNetworkAvailable()) {
            try {
                if (importe.getText().toString().equals("") || importe.getText().toString().equals("0.0")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Datos), Toast.LENGTH_LONG).show();
                } else {
                    String importes = importe.getText().toString();

                    if (importes.contains(",")) {
                        importes = Utilidad.NumeroSinComa(importes);
                    }

                    if(cod_monedaVuelto.trim().equals("1")) {
                        importes = Utilidad.NumeroSinPunto(importes);
                    }
                    String moneda = cod_monedaVuelto.trim();

                    for(int i = 0; i < WebService.Vuelto.size(); i++){
                        Vuelto cod_moneda2 = WebService.Vuelto.get(i);
                        WebService.monedaVueltoUlti = cod_moneda2.getCod_moneda();
                    }

                    WebService.addVuelto(new Vuelto(Double.valueOf(importes), moneda));

                    if(WebService.Vuelto.size()>1){
                        WebService.monedaVuelto = WebService.monedaSeleccionada;
                    }else {
                        String monedaSelec = spMonedas.getSelectedItem().toString();
                        Moneda instanciaMoneda = new Moneda();
                        for (int x = 0; x < WebService.monedas.size(); x++) {
                            instanciaMoneda = WebService.monedas.get(x);
                            if (instanciaMoneda.getSim_Moneda().trim().equals(monedaSelec.trim())) {
                                WebService.monedaVuelto = instanciaMoneda;
                                break;
                            }
                        }
                    }

                    importe.setText("");

                    dataAdapterRuta.notifyDataSetChanged();
                }
            }catch (Exception ex){
                ex.printStackTrace();
               // Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
            }
        }
    }

    public void refreshList() {
        dataAdapterRuta.notifyDataSetChanged();
    }
}
