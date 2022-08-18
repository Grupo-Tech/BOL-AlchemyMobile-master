package com.example.user.trucksales.Visual.TruckSales;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.Retenciones;
import com.example.user.trucksales.Encapsuladoras.TipoDoc;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.adapter.AdaptadorLey;
import com.example.user.trucksales.adapter.AdaptadorRetencionesTruckSales;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class IngresarRetencionesTruckSales extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{

    private Utilidades Utilidad;
    private static Context context;
    private static int val_tot=0;
    ImageView atras;
    private static EditText input;

    private static int cont_filas = 0;
    private TextView TituloObs;
    private Button btnValores;
    private EditText detalle,importe, codsuca, codfaca, tipoCambio;
    private EditText fecha;
    ListView tablaCheques;
    private AdaptadorRetencionesTruckSales dataAdapterRuta;
    private AdaptadorLey dataAdapterLey;

    Spinner spDocum;
    List<String> spinnerDcoumArray = new ArrayList<>(  );

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    private static final String CERO = "0";
    private static final String BARRA = "/";

    private Date fechaSeleccionada;


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

        setContentView( R.layout.activity_ingresar_retenciones );


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        context = this;
        Utilidad = new Utilidades(context);

        if(WebService.usuarioActual!=null) {
            if(Utilidad.isNetworkAvailable()) {
                TituloObs = findViewById(R.id.TituloObs);
                TituloObs.setVisibility(View.GONE);

                fecha = findViewById(R.id.fecha);
                detalle = findViewById(R.id.detalle);
                detalle.setInputType(InputType.TYPE_CLASS_NUMBER);

                importe = findViewById(R.id.importe);

                codsuca = findViewById(R.id.CodSuc);
                codsuca.setInputType(InputType.TYPE_CLASS_NUMBER);

                codfaca = findViewById(R.id.CodFac);
                codfaca.setInputType(InputType.TYPE_CLASS_NUMBER);

                tipoCambio = findViewById(R.id.tipoCa);
                tipoCambio.setVisibility(View.GONE);
                //tipoCambio.setInputType(InputType.TYPE_CLASS_NUMBER);

                spDocum = findViewById( R.id.spDocum );
                spDocum.setOnItemSelectedListener( (AdapterView.OnItemSelectedListener) this );
                final ArrayAdapter<String> dataAdapterDocum = new ArrayAdapter<String>( this,
                        android.R.layout.simple_spinner_item, spinnerDcoumArray );

                if (WebService.configuracion.getReten_completa().equals("N")) {
                    codsuca.setVisibility(View.GONE);
                    codfaca.setVisibility(View.GONE);
                }

                Double importeRetencion = WebService.totalRetencion;

                if(WebService.configuracion.getReten_completa().equals("N")) {
                    importe.setText(String.valueOf(importeRetencion.toString()));
                }

                importe.setInputType(InputType.TYPE_CLASS_NUMBER);
                //tipoCambio.setInputType(InputType.TYPE_CLASS_NUMBER);

                for (int i = 0; i < WebService.documentos.size(); i++) {
                    String nombreAgregar1 = WebService.documentos.get( i ).getCod_doc_uni().trim();
                    String codigoAgregar = WebService.documentos.get(i).getNom_doc_uni().trim();
                    spinnerDcoumArray.add( nombreAgregar1 + " - " + codigoAgregar );
                }
                dataAdapterDocum.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
                spDocum.setAdapter( dataAdapterDocum );

                spDocum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String docSelec = spDocum.getSelectedItem().toString();
                        String[] parts = docSelec.split("-");
                        String part1 = parts[0];
                        docSelec = part1;

                        TipoDoc instanciaDocum = new TipoDoc();
                        for (int x = 0; x < WebService.documentos.size(); x++) {
                            instanciaDocum = WebService.documentos.get(x);
                            if (instanciaDocum.getCod_doc_uni().trim().equals(docSelec.trim())) {
                                WebService.doc = instanciaDocum;
                                break;
                            }
                        }

                        if (WebService.configuracion.getDif_reten_auto().trim().equals("N")) {
                            if (WebService.doc.getCod_doc_uni().trim().equals("ley2051")) {
                                codsuca.setVisibility(View.GONE);
                                codfaca.setVisibility(View.GONE);
                            } else {
                                codsuca.setVisibility(View.VISIBLE);
                                codfaca.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                detalle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                tipoCambio.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        tipoCambio.removeTextChangedListener(this);

                        try {
                            String originalString = s.toString();

                            Long longval;
                            if (originalString.contains(",")) {
                                originalString = originalString.replaceAll(",", "");
                            }
                            longval = Long.parseLong(originalString);

                            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                            formatter.applyPattern("#,###,###,###");
                            String formattedString = formatter.format(longval);

                            //setting text after format to EditText
                            tipoCambio.setText(formattedString);
                            tipoCambio.setSelection(tipoCambio.getText().length());
                        } catch (NumberFormatException nfe) {
                            nfe.printStackTrace();
                            //Toast.makeText( getApplicationContext(), nfe.getMessage(), Toast.LENGTH_LONG ).show();
                        }

                        tipoCambio.addTextChangedListener(this);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                codfaca.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String str = codfaca.getText().toString();
                        if (str.isEmpty()) return;
                        String str2 = Utilidad.PerfectDecimal(str, 3, 0);
                        if (!str2.equals(str)) {
                            codfaca.setText(str2);
                        }
                    }
                });

                codsuca.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String str = codsuca.getText().toString();
                        if (str.isEmpty()) return;
                        String str2 = Utilidad.PerfectDecimal(str, 3, 0);
                        if (!str2.equals(str)) {
                            codsuca.setText(str2);
                        }
                    }
                });

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
                            formatter.applyPattern("#,###,###,###");
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

                    }
                });

                tablaCheques = (ListView) findViewById(R.id.tabla);

                fecha = (EditText) findViewById(R.id.fecha);
                fecha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        obtenerFecha();
                    }
                });
                dataAdapterRuta = new AdaptadorRetencionesTruckSales(IngresarRetencionesTruckSales.this, WebService.retenciones);

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
                        agregar(dataAdapterRuta);
                        if (!dataAdapterRuta.isEmpty()) {
                            Intent myIntent = new Intent(context, IngresarValoresTruckSales.class);
                            startActivity(myIntent);
                        }
                    }
                });
            }else{
                Utilidad.CargarToastConexion(context);
            }
        } else  {
            Intent myIntent = new Intent( context, Login.class );
            startActivity( myIntent );
        }


        Date hoy = new Date();

        final int anioActual = hoy.getYear() + 1900;
        final int mesActual = hoy.getMonth() + 1;
        //Formateo el día obtenido: antepone el 0 si son menores de 10
        String diaFormateado = (hoy.getDate() < 10)? CERO + String.valueOf(hoy.getDate()):String.valueOf(hoy.getDate());
        //Formateo el mes obtenido: antepone el 0 si son menores de 10
        String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
        //Muestro la fecha con el formato deseado
        fecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + anioActual);
    }

    private void agregar(AdaptadorRetencionesTruckSales dataAdapterRuta) {
        if (Utilidad.isNetworkAvailable()) {
            try {
                if (WebService.configuracion.getReten_completa().equals("S") && !WebService.doc.getCod_doc_uni().trim().equals("ley2051")) {
                    if (codsuca.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Debe completar el nro de suc", Toast.LENGTH_LONG).show();
                    } else if (codfaca.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Debe completar el nro de fac", Toast.LENGTH_LONG).show();
                    } /*else if (tipoCambio.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Debe completar el tipo de cambio", Toast.LENGTH_LONG).show();
                    }*/
                }
                if (detalle.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Debe completar el nro de retencion", Toast.LENGTH_LONG).show();
                }else if (fecha.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Debe seleccionar la fecha", Toast.LENGTH_LONG).show();
                    }
                /*else if (!fechaValida()) {
                    Toast.makeText(getApplicationContext(), "Debe seleccionar una fecha valida", Toast.LENGTH_LONG).show();
                }*/ else if (importe.getText().toString().equals("") || importe.getText().toString().equals("0.0")) {
                    Toast.makeText(getApplicationContext(), "Debe ingresar el monto", Toast.LENGTH_LONG).show();
                }
            else {
                    Double tc = WebService.tipoCambio;
                    //tc = Double.parseDouble(tc.toString().replace(",", ""));

                    String importes = importe.getText().toString();
                    double diferencia = 0D;

                    importes = importes.replace(",", "");
                    Double importa = Double.parseDouble(importes);

                     /*if(WebService.configuracion.getReten_completa().equals("S") && !WebService.doc.getNom_doc_uni().trim().equals("ley2051")){
                            String tc2 = tipoCambio.getText().toString();

                            tc2 = tc2.replace(",", "");

                            if(tc2.contains(".")){
                                tc2 = tc2.replace(".", "");
                            }

                            tc = Double.parseDouble(tc2);

                            for(int i = 0; i< WebService.deudasSeleccionadas.size(); i++){
                                ClienteCobranza cli = WebService.deudasSeleccionadas.get(i);
                                if(cli.getCod_Moneda().trim().equals("2") && cli.getCod_Docum().trim().equals("pagare")){
                                    diferencia = (importa / tc) * WebService.tipoCambio;
                                    diferencia = diferencia - importa;
                                    diferencia = Utilidad.redondearDecimales(diferencia, 0);
                                    break;
                                }
                            }
                        }*/

                   // if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("2")) {
                        importa = importa / tc;
                        importa = Utilidad.redondearDecimales(importa, 2);

                        //importes = String.valueOf(importa * tc);
                  //  }

                    if (importes.contains(",")) {
                        importes = Utilidad.NumeroSinComa(importes);
                    }

                    String suc = "";
                    String fac = "";

                    if(WebService.configuracion.getReten_completa().equals("S") && !WebService.doc.getNom_doc_uni().trim().equals("ley2051")){
                        suc = codsuca.getText().toString();
                        fac = codfaca.getText().toString();
                    }

                    WebService.addRetenciones(new Retenciones(detalle.getText().toString(), fecha.getText().toString(), Double.valueOf(importes), suc, fac, tc, importa, diferencia, WebService.doc.getCod_doc_uni().trim()));

                    detalle.setText("");

                    importe.setText("");

                    dataAdapterRuta.notifyDataSetChanged();
                }
            }catch (Exception ex){
                ex.printStackTrace();
                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
            }
        }
    }


    public void refreshList() {

        dataAdapterRuta.notifyDataSetChanged();
    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                fecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


                fechaSeleccionada = new Date();
                fechaSeleccionada.setHours(0);
                fechaSeleccionada.setMinutes(0);
                fechaSeleccionada.setSeconds(0);
                fechaSeleccionada.setDate(dayOfMonth);
                fechaSeleccionada.setMonth(month);
                fechaSeleccionada.setYear(year-1900);

            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }


    private boolean fechaValida() {

        Date hoy = new Date();
        hoy.setHours(0);
        hoy.setMinutes(0);
        hoy.setSeconds(0);

        if(fechaSeleccionada==null){
            return true;
        }else  if(fechaSeleccionada.before(hoy) ){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        try{
        Utilidad.vibraticionBotones(context);
        Intent myIntent = new Intent(context, IngresarValoresTruckSales.class);
        startActivity(myIntent);
        finish();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
