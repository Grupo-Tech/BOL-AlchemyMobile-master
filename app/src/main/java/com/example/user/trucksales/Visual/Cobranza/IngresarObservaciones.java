package com.example.user.trucksales.Visual.Cobranza;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.ClienteCobranza;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.Visual.SeleccionFuncionablidad;
import com.loopj.android.http.RequestParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class IngresarObservaciones extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Utilidades Utilidad;
    Context context;

    TextView nombreUsu,fecha,ClienteSeleccionado, solRec, doc;
    ImageView atras, casita;
    private static EditText observaciones, fechaProxVis;
    Spinner sp;
    List<String> spinnerArray = new ArrayList<>();

    protected static RequestParams paramsIng = new RequestParams(  );

    View actualView;

    String valorIntent;

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();
    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    private Date fechaSeleccionada;
    private static final String CERO = "0";
    private static final String GUION = "-";

    private Button btnAceptar;

    Spinner spDocumentos;
    final List<String> spinnerDocumentos = new ArrayList<>(  );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView( R.layout.activity_no_cobrado );

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        context = this;
        Utilidad = new Utilidades( context );
        actualView = new View(context );

        if(WebService.usuarioActual!=null) {
            try {
                if (Utilidad.isNetworkAvailable()) {

                    try {
                        valorIntent = getIntent().getStringExtra("intent");
                    }catch (Exception ex){
                        ex.printStackTrace();
                        //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                    }

                    spDocumentos = findViewById(R.id.spDocumento);
                    doc = findViewById(R.id.doc);

                    spDocumentos.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                    final ArrayAdapter<String> dataAdapterDoc = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerDocumentos);

                    WebService.deudaMot.setCod_dpto("TODOS");
                    if(WebService.configuracion.getPide_docref().equals("S")){
                        spinnerDocumentos.add("TODOS");
                        TraerDeudasMot task = new TraerDeudasMot(new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) {
                                if (!WebService.errToken.equals("")) {
                                    Intent myIntent = new Intent(context, Login.class);
                                    startActivity(myIntent);
                                } else {
                                    for (int i = 0; i < WebService.deudasMotNoCob.size(); i++) {
                                        String cod_docum = WebService.deudasMotNoCob.get(i).getCod_Docum().trim();
                                        String cod_dpto = WebService.deudasMotNoCob.get(i).getCod_dpto().trim();
                                        String serie_docum = WebService.deudasMotNoCob.get(i).getSerie_docum().trim();
                                        String nro_docum = WebService.deudasMotNoCob.get(i).getNro_Docum().trim();

                                        spinnerDocumentos.add(cod_docum + ":" + cod_dpto + "-" + serie_docum + "-" + nro_docum);
                                    }
                                    dataAdapterDoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spDocumentos.setAdapter(dataAdapterDoc);
                                    spDocumentos.setSelection(0);
                                }
                            }
                        });
                        task.execute();
                    }else{
                        spDocumentos.setVisibility(View.GONE);
                        doc.setVisibility(View.GONE);
                        //WebService.deudaMot.setCod_dpto("TODOS");
                    }

                    spDocumentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                String docSelect = spDocumentos.getSelectedItem().toString();
                                ClienteCobranza instanciaCl = new ClienteCobranza();
                                for (int x = 0; x < WebService.deudasMotNoCob.size(); x++) {
                                    instanciaCl = WebService.deudasMotNoCob.get(x);
                                    String documento = spDocumentos.getSelectedItem().toString();
                                    if(documento.equals("TODOS")){
                                        WebService.deudaMot.setCod_dpto(documento);
                                        break;
                                    }else {
                                        String[] parts = documento.split(":");
                                        String part1 = parts[0];
                                        String part2 = parts[1];
                                        String[] parts1 = part2.split("-");
                                        String parts1_1 = parts1[0];
                                        String parts1_2 = parts1[1];
                                        String parts1_3 = parts1[2];
                                        docSelect = part1;

                                        if (instanciaCl.getCod_Docum().trim().equals(docSelect.trim()) && instanciaCl.getCod_dpto().trim().equals(parts1_1) && instanciaCl.getSerie_docum().trim().equals(parts1_2) && instanciaCl.getNro_Docum().trim().equals(parts1_3)) {
                                            WebService.deudaMot = instanciaCl;
                                            break;
                                        }
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    final String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());

                    nombreUsu = (TextView) findViewById(R.id.LblUsu);
                    nombreUsu.setText(getResources().getString(R.string.usuarioTool) + WebService.USUARIOLOGEADO);

                    fecha = (TextView) findViewById(R.id.LblFecha);
                    fecha.setText(timeStamp);

                    TraerMotivosNoCobro task1 = new TraerMotivosNoCobro();
                    task1.execute();
                    try {
                        task1.get();

                    } catch (Exception exc) {
                        exc.printStackTrace();
                        //Toast.makeText( getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG ).show();
                    }

                    atras = findViewById(R.id.btnAtras);
                    atras.setClickable(true);
                    atras.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                Utilidad.vibraticionBotones(context);
                                if (valorIntent.equals("IngresarMotNoCob")) {
                                    Intent myIntent = new Intent(v.getContext(), IngresarMotNoCob.class);
                                    startActivity(myIntent);
                                    finish();
                                } else {
                                   // if (WebService.usuarioActual.getTipoCobrador().equals("D")) {
                                        Intent myIntent = new Intent(v.getContext(), SeleccionarDeudas.class);
                                        startActivity(myIntent);
                                        finish();
                                  /*  } else {
                                        Intent myIntent = new Intent(v.getContext(), SeleccionCliente.class);
                                        startActivity(myIntent);
                                        finish();
                                    }*/
                                }
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    });

                    casita = findViewById(R.id.casita);
                    casita.setClickable(true);
                    casita.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                Utilidad.vibraticionBotones(context);
                                if (valorIntent.equals("IngresarMotNoCob")) {
                                    Intent myIntent = new Intent(v.getContext(), IngresarMotNoCob.class);
                                    startActivity(myIntent);
                                    finish();
                                } else {
                                   // if (WebService.usuarioActual.getTipoCobrador().equals("D")) {
                                        Intent myIntent = new Intent(v.getContext(), SeleccionarDeudas.class);
                                        startActivity(myIntent);
                                        finish();
                                  /*  } else {
                                        Intent myIntent = new Intent(v.getContext(), SeleccionFuncionablidad.class);
                                        startActivity(myIntent);
                                        finish();
                                    }*/
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                    solRec = findViewById(R.id.solRec);

                    ClienteSeleccionado = findViewById(R.id.ClienteSeleccionado);
                    ClienteSeleccionado.setText(WebService.clienteActual.getNom_Tit().trim());

                    observaciones = findViewById(R.id.observaciones);

                    sp = (Spinner) findViewById(R.id.DDLMotivoNoCobro);
                    sp.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, spinnerArray);
                    spinnerArray.add("Seleccionar...");
                    for (int i = 0; i < WebService.ArrayMotivosNoCobro.size(); i++) {
                        String nombreAgregar = WebService.ArrayMotivosNoCobro.get(i).getNom_Pausa();
                        spinnerArray.add(nombreAgregar);
                    }
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp.setAdapter(dataAdapter);

                    fechaProxVis = findViewById(R.id.fechaProxVisi);
                    fechaProxVis.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            obtenerFecha();
                        }
                    });


                    btnAceptar = findViewById(R.id.btnAceptar);

                    btnAceptar.setClickable(true);
                    btnAceptar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Utilidad.vibraticionBotones(context);
                                if (Utilidad.isNetworkAvailable()) {
                                    if (fechaProxVis.getText().equals("") || observaciones.getText().equals("") || sp.getSelectedItem().toString().equals("Seleccionar...")) {
                                        Toast toast = Toast.makeText(context, getResources().getString(R.string.ToastText3), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                                        toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                                        toast.show();
                                    } else {

                                        String nro_viaje = "0";
                                        String cod_suc_ref = WebService.deudaMot.getCod_dpto();
                                        String cod_fac_ref = "0";
                                        String nro_doc_ref = "0";

                                        if(valorIntent.equals("SeleccionarDeudas")){
                                            if (WebService.usuarioActual.getTipoCobrador().equals("D")) {
                                                nro_viaje = String.valueOf(WebService.viajeSeleccionadoCobrador.getNumViaje().trim());
                                            }
                                        }

                                        if(!WebService.deudaMot.getCod_dpto().equals("TODOS") & WebService.configuracion.getPide_docref().equals("S")){
                                            cod_fac_ref = WebService.deudaMot.getSerie_docum();
                                            nro_doc_ref = WebService.deudaMot.getNro_Docum();
                                        }

                                        paramsIng = new RequestParams();
                                        WebService.ultimoMotivo = sp.getSelectedItemPosition();
                                        paramsIng.add("cod_motivo", String.valueOf(WebService.ultimoMotivo));
                                        paramsIng.add("fec_proxv", fechaProxVis.getText().toString());
                                        paramsIng.add("cod_tit", WebService.clienteActual.getCod_Tit_Gestion());
                                        paramsIng.add("fec_doc", timeStamp);
                                        paramsIng.add("usuario", WebService.USUARIOLOGEADO);
                                        paramsIng.add("descripcion", observaciones.getText().toString());
                                        paramsIng.add("cod_emp", WebService.clienteActual.getCodEmp());
                                        paramsIng.add("num_viaje", nro_viaje);

                                        paramsIng.add("cod_suc_ref", cod_suc_ref);
                                        paramsIng.add("cod_fac_ref", cod_fac_ref);
                                        paramsIng.add("nro_doc_ref", nro_doc_ref);

                                        AgregarObservaciones task = new AgregarObservaciones(new AsyncResponse() {
                                            @Override
                                            public void processFinish(Object output) {

                                                if (WebService.reto_Observaciones.equals("ok")) {
                                                    Toast.makeText( getApplicationContext(), R.string.ObservacionAgregada, Toast.LENGTH_LONG ).show();
                                                    //solRec.setText(getResources().getString(R.string.ObservacionAgregada));
                                                } else {
                                                    if(WebService.reto_Observaciones.toUpperCase().contains("JSON")) {
                                                        Intent myIntent = new Intent(context, Login.class);
                                                        startActivity(myIntent);
                                                    }else {
                                                        solRec.setVisibility(View.VISIBLE);
                                                        Toast.makeText( getApplicationContext(), "Error al grabar datos \n" + WebService.reto_Observaciones, Toast.LENGTH_LONG ).show();
                                                        //solRec.setText("Error al grabar datos \n" + WebService.reto_Observaciones);
                                                    }
                                                }
                                                try {
                                                    Intent intent = getIntent();
                                                    if (intent.getExtras() != null) {
                                                        Intent myIntent = new Intent(context, intent.getExtras().getClass());
                                                        startActivity(myIntent);
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                if(valorIntent.equals("SeleccionarDeudas")) {
                                                    if (WebService.usuarioActual.getTipoCobrador().equals("D")) {
                                                        Intent myIntent = new Intent(context, SeleccionarDeudas.class);
                                                        startActivity(myIntent);
                                                        finish();
                                                    } else {
                                                        Intent myIntent = new Intent(context, SeleccionCliente.class);
                                                        startActivity(myIntent);
                                                        finish();
                                                    }
                                                }else{
                                                    Intent myIntent = new Intent(context, IngresarMotNoCob.class);
                                                    startActivity(myIntent);
                                                    finish();
                                                }
                                            }
                                        });
                                        task.execute();

                                    }
                                } else {
                                    Utilidad.CargarToastConexion(context);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                }else{
                    Utilidad.CargarToastConexion(context);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        else{
            Intent myIntent = new Intent( context, Login.class );
            startActivity( myIntent );
        }
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo ai= cm.getActiveNetworkInfo();
        return ai!= null;
    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    private class AgregarObservaciones extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog(context);
        public AsyncResponse delegate = null;

        public AgregarObservaciones(AsyncResponse asyncResponse){
            delegate = asyncResponse;
        }

        @Override
        protected Void doInBackground(String... strings) {
            if(Utilidad.isNetworkAvailable()){
                WebService.AgregarObservaciones("Cobranzas/IngresarObservaciones.php", paramsIng);
                return null;
            }else {
                Utilidad.dispalyAlertConexion(context);
            }
            return  null;
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

        WebService.reto_Observaciones = "";
        WebService.nro_transObs = 0;
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
                fechaProxVis.setText(diaFormateado + GUION + mesFormateado + GUION + year);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private class TraerMotivosNoCobro extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {

            WebService.TrearMotivosNoCobro( "Cobranzas/TraerMotivoNoCobrado.php" );

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (!WebService.errToken.equals("")) {
                Intent myIntent = new Intent(context, Login.class);
                startActivity(myIntent);
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try{
        Utilidad.vibraticionBotones(context);
        if (WebService.usuarioActual.getTipoCobrador().equals("D")) {
            Intent myIntent = new Intent(context, SeleccionarDeudas.class);
            startActivity(myIntent);
            finish();
        } else {
            Intent myIntent = new Intent(context, SeleccionCliente.class);
            startActivity(myIntent);
            finish();
        }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private class  TraerDeudasMot extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface
        public TraerDeudasMot(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            if(Utilidad.isNetworkAvailable()) {
                RequestParams params1 = new RequestParams(  );
                params1.put( "cod_tit",WebService.clienteActual.getCod_Tit_Gestion().trim() );
                params1.put( "cod_empresa",WebService.usuarioActual.getEmpresa().trim());
                WebService.TraerDeudasMot(params1, "Cobranzas/TraerDeudasNoCob.php");
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
}
