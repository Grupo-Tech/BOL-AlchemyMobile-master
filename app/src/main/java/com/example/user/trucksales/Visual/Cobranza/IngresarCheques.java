package com.example.user.trucksales.Visual.Cobranza;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.Banco;
import com.example.user.trucksales.Encapsuladoras.Moneda;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.adapter.AdaptadorCheques;
import com.example.user.trucksales.Encapsuladoras.Cheque;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import java.util.Calendar;

public class IngresarCheques extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{

    private Utilidades Utilidad;
    private static Context context;
    private static int val_tot=0;
    ImageView atras;
    private static EditText input;

    private static int dias;

    private static int cont_filas = 0;

    private Button btnValores;
    private EditText numero,/*banco,*/importe,nroDoc,tipodoc,importeconvertido;
    ListView tablaCheques;
    private AdaptadorCheques dataAdapterRuta;

   /* Spinner spBancos;
    List<String> spinnerBancoArray = new ArrayList<>(  );*/

    Spinner spMonedas;
    List<String> spinnerMonedaArray = new ArrayList<>(  );

    private EditText fecha;

    Spinner spTipodoc;
    List<String> spinnerTipoDocArray = new ArrayList<>(  );

    private static final String CERO = "0";
    private static final String BARRA = "/";

    private static Double total;
    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    String cod_monedaCheque = "";

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    private Date fechaSeleccionada;

    AutoCompleteTextView TxtNomBanco,TxtCodBanco;
    public List<String> editTextArray =  new ArrayList<String>();
    public List<String> Codigos = new ArrayList<>(  );

    public int decimales = 1;

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

        setContentView( R.layout.activity_ingresar_cheques );

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        context = this;

        Utilidad = new Utilidades(context);
        if(WebService.usuarioActual!=null) {
            try {
                if (Utilidad.isNetworkAvailable()) {

                    TxtNomBanco = findViewById(R.id.TxtNomBanco);
                    TxtCodBanco = findViewById(R.id.TxtCodBanco);

                    numero = findViewById(R.id.numero);
                    numero.setInputType(InputType.TYPE_CLASS_NUMBER);

                    nroDoc = findViewById(R.id.nroDoc);

                    nroDoc.setText(WebService.clienteActual.getCod_Tit_Gestion());
                    nroDoc.setKeyListener(DigitsKeyListener.getInstance("0123456789-"));

                    tipodoc = findViewById(R.id.tipodoc);

                    importe = findViewById(R.id.importe);

               /* total = WebService.diferenciaValores;

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
                            }
                        }
                    });

                    importeconvertido = findViewById(R.id.importeconvertido);

                    tablaCheques = (ListView) findViewById(R.id.tabla);

                    dataAdapterRuta = new AdaptadorCheques(IngresarCheques.this, WebService.cheques);

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
                            agregarCheque(dataAdapterRuta);
                            if (!dataAdapterRuta.isEmpty()) {
                                CerrarTeclado();
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

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, editTextArray);
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Codigos);

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

        spMonedas = findViewById( R.id.spMonedas );
        spMonedas.setOnItemSelectedListener( (AdapterView.OnItemSelectedListener) this );
        final ArrayAdapter<String> dataAdapterMoneda = new ArrayAdapter<String>( this,
                android.R.layout.simple_spinner_item, spinnerMonedaArray );

        fecha= (EditText) findViewById(R.id.fecha);
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               obtenerFecha();
            }
        });

        spTipodoc = findViewById( R.id.spTipodoc );
        spTipodoc.setOnItemSelectedListener( (AdapterView.OnItemSelectedListener) this );
        final ArrayAdapter<String> dataAdapterTipoDoc = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item, spinnerTipoDocArray );
        spTipodoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tipoAux = dataAdapterTipoDoc.getItem(position);
                if(tipoAux.equals("Otro tipo")){
                    tipodoc.setText("");
                    tipodoc.setVisibility(View.VISIBLE);
                }else{
                    tipodoc.setText(tipoAux);
                    tipodoc.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Se cargar en IngresarValores 25/11/2019 BDL
       /* TraerDatos task = new TraerDatos(new AsyncResponse() {
            public void processFinish(Object output)            {*/
                for (int i = 0; i < WebService.bancos.size(); i++) {
                    String nombreAgregar = WebService.bancos.get(i).getNom_banco().trim();
                    editTextArray.add(nombreAgregar);
                }
                TxtNomBanco.setAdapter(adapter);

                for (int i = 0; i < WebService.bancos.size(); i++) {
                    String nombreAgregar = WebService.bancos.get(i).getCod_banco().trim();
                    Codigos.add(nombreAgregar);
                }
                TxtCodBanco.setAdapter(adapter2);

                for (int i = 0; i < WebService.monedasCheques.size(); i++) {
                    String nombreAgregar1 = WebService.monedasCheques.get( i ).getSim_Moneda();
                    spinnerMonedaArray.add( nombreAgregar1 );
                }
                dataAdapterMoneda.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
                spMonedas.setAdapter( dataAdapterMoneda );

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

                spMonedas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            String monedaSelec = spMonedas.getSelectedItem().toString();
                            Moneda instanciaMoneda = new Moneda();
                            for (int x = 0; x < WebService.monedas.size(); x++) {
                                instanciaMoneda = WebService.monedas.get(x);
                                if (instanciaMoneda.getSim_Moneda().trim().equals(monedaSelec.trim())) {
                                    cod_monedaCheque = instanciaMoneda.getCod_Moneda();
                                    break;
                                }
                            }
                            total = WebService.diferenciaValores;
                            if (cod_monedaCheque.trim().equals("1")) {
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
                        }catch (Exception ex){
                            ex.printStackTrace();
                            //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                for (int i = 0; i < WebService.tiposDoc.size(); i++) {
                    String nombreAgregar1 = WebService.tiposDoc.get( i ).getNom_doc_uni();
                    spinnerTipoDocArray.add( nombreAgregar1 );
                }
                dataAdapterTipoDoc.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
                spTipodoc.setAdapter( dataAdapterTipoDoc );
                spTipodoc.setSelection(1);
           // }
       /* } );
        task.execute(  );*/

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


    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo ai= cm.getActiveNetworkInfo();
        return ai!= null;
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




    private void agregarCheque(AdaptadorCheques dataAdapterRuta) {
        if (Utilidad.isNetworkAvailable()) {
            try {
                if (TxtCodBanco.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Debe ingresar/ seleccionar el banco", Toast.LENGTH_LONG).show();
                } else if (numero.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Debe ingresar el nro de cheque", Toast.LENGTH_LONG).show();
                } else if (importe.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Debe ingresar el monto", Toast.LENGTH_LONG).show();
                } else if (fecha.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Debe seleccionar la fecha", Toast.LENGTH_LONG).show();
                } else if (tipodoc.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Debe ingresar/ seleccionar el tipo de documento", Toast.LENGTH_LONG).show();
                } else if (nroDoc.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Debe ingresar el nro de documento", Toast.LENGTH_LONG).show();
                }else if (!fechaValida()) {
                        Toast.makeText(getApplicationContext(), "Debe seleccionar una fecha valida", Toast.LENGTH_LONG).show();
                } else {
                    importeconvertido.setText((importe.getText().toString()));
                    String cod_moneda;
                    String importe2 = importe.getText().toString();
                    if (spMonedas.getSelectedItem().toString().trim().equals(WebService.simboloMonedaTr.trim())) {
                        if(importe2.contains(",")){
                            importe2 = importe2.replace(",", "");
                        }
                        Double totalConvertido = Double.valueOf(importe2) * WebService.tipoCambio;
                        importeconvertido.setText(totalConvertido.toString());
                        cod_moneda = WebService.codMonedaTr.trim();
                    } else {
                        importeconvertido.setText(importe2);
                        cod_moneda = WebService.codMonedaNacional.trim();
                    }
                    String cod_banco;
                    String cod_banco2;
                    cod_banco = TxtCodBanco.getText().toString().trim();
                    cod_banco2 = TxtNomBanco.getText().toString().trim();//WebService.bancos.get(spBancos.getSelectedItemPosition()).getNom_banco();

                    String numeroDoc = numero.getText().toString();

                    String nro_doc = nroDoc.getText().toString();

                    if(importe2.contains(",")){
                        importe2 =  importe2.replace(",", "");
                    }
                    if(cod_monedaCheque.trim().equals("1")){
                        if (importe2.contains(".")) {
                            importe2 = Utilidad.NumeroSinPunto(importe2);
                        }
                    }
                    else {
                        importe2 = String.valueOf(Utilidad.redondearDecimales(Double.parseDouble(importe2), 2));
                    }

                    for(int i = 0; i < WebService.cheques.size(); i++){
                        Cheque cod_moneda2 = WebService.cheques.get(i);
                        WebService.monedaChequeUlti = cod_moneda2.getCodMon();
                    }

                    String fecha2 = fecha.getText().toString();

                    WebService.addCheque(new Cheque(Integer.parseInt(numeroDoc), cod_banco, Double.valueOf(importe2),
                            fecha2, tipodoc.getText().toString(), Integer.valueOf(Utilidad.NumeroSinGuion(nro_doc)), cod_moneda, cod_banco2));

                    if(WebService.cheques.size()>1){
                        WebService.monedaCheque = WebService.monedaSeleccionada;
                    }else {
                        String monedaSelec = spMonedas.getSelectedItem().toString();
                        Moneda instanciaMoneda = new Moneda();
                        for (int x = 0; x < WebService.monedas.size(); x++) {
                            instanciaMoneda = WebService.monedas.get(x);
                            if (instanciaMoneda.getSim_Moneda().trim().equals(monedaSelec.trim())) {
                                WebService.monedaCheque = instanciaMoneda;
                                break;
                            }
                        }
                    }

                    numero.setText("");
                    // banco.setText("");
                    importe.setText("");
                    TxtCodBanco.setText("");
                    TxtNomBanco.setText("");
                    nroDoc.setText("");

                    dataAdapterRuta.notifyDataSetChanged();
                }
            }catch (Exception ex){
                ex.printStackTrace();
                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
            }
        }
    }

    private boolean fechaValida() {

        Date hoy = new Date();
        hoy.setHours(0);
        hoy.setMinutes(0);
        hoy.setSeconds(0);

        if(fechaSeleccionada==null){
            return true;
        }
        else if (fechaSeleccionada.before(hoy)){

            dias = diasRestantes(fechaSeleccionada);

            if(dias > WebService.configuracion.getDiasfec_cheque()){
                return false;
            }else {
                return true;
            }
        }
       /* else if(fechaSeleccionada==null){
            return true;
        }/*else  if(fechaSeleccionada.before(hoy) ){
            return false;
        }*/else{
            return true;
        }
    }

    private int diasRestantes(Date fecha){
        int dias = 0;
        try {
            DateFormat dd = new SimpleDateFormat("dd/MM/yyyy");
            boolean activo = false;
            Calendar calendar;
            Date aux;
            do {
                calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -dias);
                aux = calendar.getTime();
                if (dd.format(aux).equals(dd.format(fecha)))
                    activo = true;
                else
                    dias++;
            } while (activo != true);
            return dias;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return dias;
    }

    public Date sumarRestarHorasFecha(Date fecha, int dias){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_MONTH, -dias);  // numero de horas a añadir, o restar en caso de horas<0
        return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas
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

    private void CerrarTeclado(){
        View view = this.getCurrentFocus();
        view.clearFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
