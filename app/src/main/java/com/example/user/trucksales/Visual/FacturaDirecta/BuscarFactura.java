package com.example.user.trucksales.Visual.FacturaDirecta;

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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BuscarFactura extends AppCompatActivity {

    private Utilidades Utilidad;
    ImageView atras;
    Context contexto;

    TextView LblUsu, LblFecha, retorno;
    EditText EdtCodSucTribut, EdtCodFacTribut, EdtNumFactu;
    Button validarFac;

    public String cod_Suc;
    public String cod_Fac;
    public Integer num_Fa = 0;

    RequestParams params = new RequestParams();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_buscar_factura);
        contexto = this;
        Utilidad = new Utilidades(contexto);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        GuardarDatosUsuario.Contexto = contexto;

        try {
            if (WebService.USUARIOLOGEADO != null) {
                if (Utilidad.isNetworkAvailable()) {

                    LblUsu = findViewById(R.id.LblUsu);
                    LblFecha = findViewById(R.id.LblFecha);
                    retorno = findViewById(R.id.retorno);
                    atras = findViewById(R.id.btnAtras);
                    EdtCodSucTribut = findViewById(R.id.EdtCodSucTribut);
                    EdtCodSucTribut.setInputType(InputType.TYPE_CLASS_NUMBER);
                    EdtCodSucTribut.setText(WebService.cod_suc_tribut);


                    EdtCodFacTribut = findViewById(R.id.EdtCodFacTribut);
                    EdtCodFacTribut.setInputType(InputType.TYPE_CLASS_NUMBER);
                    EdtCodFacTribut.setText(WebService.cod_suc_tribut);

                    EdtNumFactu = findViewById(R.id.EdtNumFactu);
                    EdtNumFactu.setInputType(InputType.TYPE_CLASS_NUMBER);
                    EdtNumFactu.setText(WebService.cod_suc_tribut);

                    //PARA PROBAR
                   /* EdtCodSucTribut.setText("001");
                    EdtCodFacTribut.setText("005");
                    EdtNumFactu.setText("12916");*/

                    validarFac = findViewById(R.id.validarFac);

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

                        }
                    });

                    atras.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try{
                            Intent myIntent = new Intent(contexto, Menu.class);
                            startActivity(myIntent);
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    });

                    LblUsu.setText(getResources().getString(R.string.usuarioTool) + WebService.usuarioActual.getNombre().trim());
                    String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                    LblFecha.setText(timeStamp);

                    validarFac.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try{
                            cod_Suc = EdtCodSucTribut.getText().toString().trim();
                            cod_Fac = EdtCodFacTribut.getText().toString().trim();
                            num_Fa = Integer.valueOf(EdtNumFactu.getText().toString().trim());

                            ExisteFactura task = new ExisteFactura(new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {
                                    if (!WebService.errToken.equals("")) {
                                        Intent myIntent = new Intent(contexto, Login.class);
                                        startActivity(myIntent);
                                    } else {
                                        if (WebService.fact.getTipo().equals("Factura Invalida")) {
                                            retorno.setText(getResources().getString(R.string.Error).toUpperCase());
                                        } else {
                                            TraerProductosFac task = new TraerProductosFac(new AsyncResponse() {
                                                @Override
                                                public void processFinish(Object output) {
                                                    if (!WebService.errToken.equals("")) {
                                                        Intent myIntent = new Intent(contexto, Login.class);
                                                        startActivity(myIntent);
                                                    }else {
                                                        WebService.fact.setCod_Fac_Tribut(cod_Fac);
                                                        WebService.fact.setCod_Suc_Tribut(cod_Suc);
                                                        WebService.fact.setNro_Docum(num_Fa.toString());
                                                        Intent myIntent = new Intent(contexto, NotaCredito.class);
                                                        startActivity(myIntent);
                                                    }
                                                }
                                            });
                                            task.execute();
                                        }
                                    }
                                }
                            });
                            task.execute();
                        }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    });

                } else {
                    Utilidad.CargarToastConexion(contexto);
                }
            } else {
                Intent myIntent = new Intent(contexto, Login.class);
                startActivity(myIntent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    private class ExisteFactura extends AsyncTask<String,Void,Void> {
        public AsyncResponse delegate = null;//Call back interface
        public ExisteFactura(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... strings) {
            params = new RequestParams();
            params.add( "cod_suc_trib", cod_Suc);
            params.add( "cod_fac_tribut", cod_Fac );
            params.add( "numfac", num_Fa.toString());
            WebService.ExisteFactura( "Facturas/ExisteFactura.php",params );
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

    private class TraerProductosFac extends AsyncTask<String,Void,Void> {
        public AsyncResponse delegate = null;//Call back interface
        public TraerProductosFac(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... strings) {
            params = new RequestParams();
            params.add( "nro_trans", WebService.fact.getNro_Trans());
            WebService.TraerProductosFac( params, "Facturas/TraerProductosFac.php" );
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
        super.onBackPressed();
        try{
        Intent myIntent = new Intent(contexto, Menu.class);
        startActivity(myIntent);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
