package com.example.user.trucksales.Visual.Cobranza;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.ClienteCobranza;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class IngresarMotNoCob extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Utilidades Utilidad;
    ImageView atras;
    LinearLayout mainLayout;

    protected static RequestParams params = new RequestParams(  );
    protected static RequestParams params1 = new RequestParams(  );

    public static String codemp = "";

    TextView nombreUsu,fecha;
    AutoCompleteTextView nombreCliente,codCliente;
    public List<String> editTextArray =  new ArrayList<String>();
    public List<String> Codigos = new ArrayList<>(  );
    Button BtnBuscarPorNomCod;
    Context contexto;
    String cod_tit_mandar ="";

    Spinner spEmpresa;
    final List<String> spinnerEmpresa = new ArrayList<>(  );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motnocob);
        contexto = this;
        Utilidad = new Utilidades(contexto);

        GuardarDatosUsuario.Contexto = contexto;

        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();

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
                                        codemp = instanciaCl.getCodEmp().trim();
                                        break;
                                    }
                                }
                                codCliente.setText("");
                                nombreCliente.setText("");

                            } catch (Exception ex) {
                                ex.printStackTrace();
                                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    atras = findViewById(R.id.btnAtras);
                    atras.setClickable(true);
                    atras.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(contexto);
                                Intent myIntent = new Intent(v.getContext(), ConsultasCobrador.class);
                                startActivity(myIntent);
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
                                    WebService.clienteActual = instaCliente;
                                    encontrado = true;
                                    codCliente.setText(instaCliente.getCod_Tit_Gestion().trim());
                                    cod_tit_mandar = instaCliente.getCod_Tit_Gestion().trim();
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
                                    WebService.clienteActual = instaCliente;
                                    encontrado = true;
                                    nombreCliente.setText(instaCliente.getNom_Tit().trim());
                                    cod_tit_mandar = instaCliente.getCod_Tit_Gestion().trim();
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
                            WebService.clienteActual.setCodEmp(WebService.usuarioActual.getEmpresa());
                            Intent myIntent = new Intent(contexto, IngresarObservaciones.class);
                            myIntent.putExtra("intent", "IngresarMotNoCob");
                            startActivity(myIntent);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }

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
        Intent myIntent = new Intent(contexto, ConsultasCobrador.class);
        startActivity(myIntent);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
