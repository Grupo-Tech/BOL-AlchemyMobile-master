package com.example.user.trucksales.Visual;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Cobranza.SeleccionCliente;
import com.example.user.trucksales.Visual.TruckSales.Viajes;
import com.example.user.trucksales.Visual.Cobranza.MenuCobranzas;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SeleccionFuncionablidad extends Activity {
    TextView nomUsuario,txtFecha,holaUsu;
    Button btnCobranza,btnViajes;
    private static ImageView flecha;
    Context context1;
    public static RequestParams params1= new RequestParams();
    private Utilidades Utilidad;

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            if (WebService.usuarioActual.getEs_Entrega().equals("S") && WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("L") || WebService.usuarioActual.getTipoCobrador().equals("F")) {
                WebService.EstadoActual = -1;
                super.onCreate(savedInstanceState);
                this.requestWindowFeature(Window.FEATURE_NO_TITLE);
                setContentView(R.layout.activity_seleccion_funcionablidad);
                context1 = this;
                Utilidad = new Utilidades(context1);
                nomUsuario = (TextView) findViewById(R.id.nombreUsuario);
                nomUsuario.setText(getResources().getString(R.string.usuarioTool) + WebService.USUARIOLOGEADO);
                String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                txtFecha = (TextView) findViewById(R.id.Fecha);
                txtFecha.setText(timeStamp);
                WebService.usuarioActual.setActivo(true);
                if (WebService.usuarioActual.getActivo()) {
                    flecha = findViewById(R.id.btnAtras);
                    flecha.setClickable(true);
                    flecha.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(context1);
                            Intent myIntent = new Intent(v.getContext(), Login.class);
                            startActivity(myIntent);
                        }
                    });
                    btnCobranza = findViewById(R.id.btnCobranza);
                    btnCobranza.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(context1);
                            //new traerParempTask().execute();
                        /*TraerMonedas task2 = new TraerMonedas(new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) {

                            }
                        });
                        task2.execute();*/

                            //SI EL USUARIO ES COBRANZA LIBRE O FIJO
                            if (WebService.usuarioActual.getTipoCobrador().equals("L") || WebService.usuarioActual.getTipoCobrador().equals("F")) {
                                params1.put("username", WebService.usuarioActual.getNombre());
                                params1.put("cod_empresa", WebService.usuarioActual.getEmpresa().trim());

                           /* TraerClientes task01 = new TraerClientes(new AsyncResponse() {
                                public void processFinish(Object output) {*/
                                Intent myIntent = new Intent(context1, SeleccionCliente.class);
                                startActivity(myIntent);
                               /* }
                            });
                            task01.execute();*/
                            }
                        }
                    });

                    btnViajes = findViewById(R.id.BtnTS);
                    btnViajes.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(context1);
                            CargarViajes task = new CargarViajes(new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {
                                    if(!WebService.errToken.equals("")){
                                        Intent myIntent = new Intent(context1, Login.class);
                                        startActivity(myIntent);
                                    }else {
                                        Intent myIntent = new Intent(context1, Viajes.class);
                                        startActivity(myIntent);
                                    }
                                }
                            });
                            task.execute();
                        }
                    });

                }
            } else {
                Toast.makeText(getApplicationContext(), "Usuario sin permisos", Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Usuario sin permisos", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(context1, Login.class);
            startActivity(myIntent);
        }
    }

    private class CargarViajes extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context1 );
        public AsyncResponse delegate = null;//Call back interface
        public CargarViajes(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            params1 = new RequestParams(  );
            params1.put( "username",WebService.usuarioActual.getNombre() );
            WebService.TraerViajes(params1,"Viajes/Viajes.php");
            return null;
        }
        @Override
        public void onPreExecute(){
            Utilidad.showLoadingMessage();
        }
        @Override
        protected void onPostExecute(Void result) {
            Utilidad.deleteLoadingMessage();
            delegate.processFinish( WebService.logueado);
        }
        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    @Override
    public void onBackPressed() {
        if(WebService.usuarioActual.getActivo()) {
            Intent nextActivity = new Intent( context1, Login.class );
            startActivity( nextActivity );
            finish();
        }
    }

    class traerParempTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progress;

        protected void onPreExecute (){
            progress = ProgressDialog.show(SeleccionFuncionablidad.this, "Obteniendo datos", "Por favor, espere..", true);
        }
        protected String doInBackground(String... params) {

            String txt ="";

            WebService.traerParemp();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    WebService.limpiarValores();
                }
            });

            return txt;
        }
        protected void onProgressUpdate(Integer...a){
        }
        protected void onPostExecute(String result) {
            progress.dismiss();
            if (!WebService.errToken.equals("")) {
                Intent myIntent = new Intent(context1, Login.class);
                startActivity(myIntent);
            }
        }
    }

}
