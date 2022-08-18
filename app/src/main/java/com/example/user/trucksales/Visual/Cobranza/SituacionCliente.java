package com.example.user.trucksales.Visual.Cobranza;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.print.PrintManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.ClienteCobranza;
import com.example.user.trucksales.Negocio.Printer;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.loopj.android.http.RequestParams;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SituacionCliente extends AppCompatActivity {

    private Utilidades Utilidad;
    ImageView atras,seleccionarCaja,consultas;
    LinearLayout mainLayout;
    public static EditText fechaProxVis;
    TextView nombreUsu,fecha, SeleccionarCliente, titulo, TituloFecha;
    AutoCompleteTextView nombreCliente,codCliente;

    public static String cod_emp;

    public List<String> editTextArray =  new ArrayList<String>();
    public List<String> Codigos = new ArrayList<>(  );
    Button BtnBuscarPorNomCod;
    Context contexto;
    String cod_tit_mandar ="";
    boolean bandera = false;

    RequestParams params1 = new RequestParams(  );

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();
    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    private Date fechaSeleccionada;
    private static final String CERO = "0";
    private static final String GUION = "-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_situacion_cliente);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        contexto = this;
        Utilidad = new Utilidades(contexto);
        if (WebService.USUARIOLOGEADO != null) {
            try{
                if(Utilidad.isNetworkAvailable()) {

                    SeleccionarCliente = findViewById(R.id.toolbar_titlePausa);
                    titulo = findViewById(R.id.titulo);
                    TituloFecha = findViewById(R.id.TituloFecha);

                    if (WebService.configuracion.getNom_repcli().equals("Estado de Cuenta")){
                        titulo.setText(WebService.configuracion.getNom_repcli().toUpperCase());
                    }

                    fechaProxVis = findViewById(R.id.fechaProxVisi);

                    if (WebService.configuracion.getPide_fecrepcli().equals("N")) {
                        fechaProxVis.setVisibility(View.GONE);
                        TituloFecha.setVisibility(View.GONE);
                    }

                    fechaProxVis.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            obtenerFecha();
                        }
                    });

               /* if(WebService.deudores.size() == 0){
                    Toast toast = Toast.makeText(contexto, getResources().getString(R.string.noClientes), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                    toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                    toast.show();//showing the toast is important***/
                    //}

                    final String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                    nombreUsu = (TextView) findViewById(R.id.LblUsu);
                    fecha = (TextView) findViewById(R.id.LblFecha);
                    fecha.setText(timeStamp);
                    nombreCliente = findViewById(R.id.TxtNombreCliente);
                    codCliente = findViewById(R.id.TxtCodCliente);
                    nombreUsu.setText(getResources().getString(R.string.usuarioTool) + WebService.USUARIOLOGEADO);

                    mainLayout = findViewById(R.id.mainLay);

                    atras = findViewById(R.id.btnAtras);
                    atras.setClickable(true);
                    atras.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try{
                            Utilidad.vibraticionBotones(contexto);
                        /*if (!WebService.usuarioActual.getTipoCobrador().equals("L")) {
                            WebService.USUARIOLOGEADO = null;
                            WebService.logueado = false;
                            Intent myIntent = new Intent(v.getContext(), SeleccionCliente.class);
                            startActivity(myIntent);
                        } else {
                            Intent myIntent = new Intent(v.getContext(), MenuCobranzas.class);
                            startActivity(myIntent);
                        }*/

                            Intent myIntent = new Intent(v.getContext(), ConsultasCobrador.class);
                            startActivity(myIntent);
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
                                if (!WebService.errToken.equals("")) {
                                    Intent myIntent = new Intent(contexto, Login.class);
                                    startActivity(myIntent);
                                } else {
                                    CargarSpinner();
                                    nombreCliente.setAdapter(adapter);
                                    codCliente.setAdapter(adapter2);
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
                    BtnBuscarPorNomCod.setText(getResources().getString(R.string.buscar));
                    BtnBuscarPorNomCod.setOnClickListener(new View.OnClickListener() {
                        public void onClick(final View v) {
                            Utilidad.vibraticionBotones(contexto);

                            String fecha = timeStamp;

                            if (WebService.configuracion.getPide_fecrepcli().equals("S")) {
                                fecha = fechaProxVis.getText().toString();
                            }
                            String cod_tit = codCliente.getText().toString();

                            if (fecha.equals("")) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.FechaIngc), Toast.LENGTH_LONG).show();
                            }else if (cod_tit.equals("")) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Cli), Toast.LENGTH_LONG).show();
                            } else {
                                bandera = false;
                                String URL_SITUACION;

                                if (WebService.configuracion.getTipo_impresora().equals("T")) {
                                    Printer pr = new Printer();
                                    PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
                                    pr.setPrMang(printManager);
                                    pr.setContx(contexto);
                                    pr.setValor(cod_tit.trim());
                                    pr.setTipo("EC");
                                    pr.genarPdf(pr);
                                }else {

                                /*if (WebService.configuracion.getNom_repcli().equals("Estado de Cuenta")) {
                                    URL_SITUACION = WebService.URL + "Cobranzas/estadoCuenta.php?" + "&cod_tit=" + cod_tit;
                                }else {*/
                                    URL_SITUACION = WebService.URL + "Cobranzas/situacionCliente.php?" + "fecha_hasta=" + fecha + "&cod_tit=" + cod_tit;
                                    // }

                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_SITUACION));
                                    startActivity(browserIntent);
                                }
                            }
                        }
                    });
                }else{
                    Utilidad.CargarToastConexion(contexto);
                }
            }catch (Exception ex){
                ex.printStackTrace();
                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
            }

        }else {
            Intent myIntent = new Intent(contexto, Login.class);
            startActivity(myIntent);
        }
    }

    public interface AsyncResponse {
        void processFinish(Object output);
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
    protected void onResume() {
        super.onResume();

        fechaProxVis.setText("");
        codCliente.setText("");
        nombreCliente.setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try{
        Utilidad.vibraticionBotones(contexto);
        Intent myIntent = new Intent(contexto, ConsultasCobrador.class);
        startActivity(myIntent);
        }catch (Exception ex){
            ex.printStackTrace();
        }
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
}
