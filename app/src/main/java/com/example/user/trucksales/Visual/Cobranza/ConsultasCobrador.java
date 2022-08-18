package com.example.user.trucksales.Visual.Cobranza;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ConsultasCobrador extends AppCompatActivity {
    TextView nombreUsu, TxtFecha;
    Button recibosGenerados, botonTraerAnulados, btnFicha, btnArqueo, btnMotivoNoCob, facturasGeneradas, btnIngresarNoCob;
    private static RequestParams params;
    private static RequestParams params1;
    private static RequestParams params2;
    ImageView atras;
    Context contexto;
    Utilidades Utilidad;
    public static LatLng ubicacionActual;
    View actualView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_consultas_cobrador);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        contexto = this;
        Utilidad = new Utilidades(contexto);
        actualView = new View(contexto);
        if (WebService.USUARIOLOGEADO != null) {

            btnIngresarNoCob = findViewById(R.id.btnIngMotivoNoCob);
            //todo ver!
            if(WebService.configuracion.getCierre_app().equals("S")){
                btnIngresarNoCob.setVisibility(View.GONE);
            }
            btnIngresarNoCob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(contexto, IngresarMotNoCob.class);
                    startActivity(myIntent);
                }
            });

            btnMotivoNoCob = findViewById(R.id.btnMotivoNoCob);
            btnMotivoNoCob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utilidad.vibraticionBotones(contexto);
                    params = new RequestParams();
                    params.add("username", WebService.USUARIOLOGEADO);
                    TraerObservacionesDia obs = new TraerObservacionesDia(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            if (!WebService.errToken.equals("")) {
                                Intent myIntent = new Intent(contexto, Login.class);
                                startActivity(myIntent);
                            } else {
                                if (WebService.listaobservaciones.size() != 0) {
                                    try {
                                        Intent myIntent = new Intent(contexto, ObservacionesGeneradas.class);
                                        startActivity(myIntent);
                                    } catch (Exception exc) {
                                        exc.printStackTrace();
                                        //Toast.makeText( getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG ).show();
                                    }
                                } else {
                                    Toast.makeText(contexto, getResources().getString(R.string.noMotivos), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                    obs.execute();
                }
            });

            botonTraerAnulados = findViewById(R.id.btnAnulado);
            botonTraerAnulados.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Utilidad.vibraticionBotones(contexto);
                    if (Utilidad.isNetworkAvailable()) {
                        Intent myIntent = new Intent(contexto, RecibosAnulados.class);
                        startActivity(myIntent);
                    }else{
                        Utilidad.CargarToastConexion(contexto);
                    }
                }
            });
            atras = findViewById(R.id.btnAtras);
            atras.setClickable(true);
            atras.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Utilidad.vibraticionBotones(contexto);
                    BotonVolver(v);
                }
            });
            nombreUsu = findViewById(R.id.nombreUsuario);
            nombreUsu.setText(getResources().getString(R.string.usuarioTool) + WebService.USUARIOLOGEADO);

            String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
            TxtFecha = findViewById(R.id.Fecha);
            TxtFecha.setText(timeStamp);

            btnFicha = findViewById(R.id.btnFicha);
            btnFicha.setText(WebService.configuracion.getNom_repcli().toUpperCase());

            btnFicha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utilidad.vibraticionBotones(contexto);
                    try {
                        //else {
                        Intent myIntent = new Intent(contexto, SituacionCliente.class);
                        startActivity(myIntent);
                        //  }
                    } catch (Exception exc) {
                        exc.printStackTrace();
                        //Toast.makeText( getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG ).show();
                    }
                }
            });

            btnArqueo = findViewById(R.id.btnArqueo);

            if (WebService.configuracion.getCierre_app().equals("S")) {
                btnArqueo.setText(getResources().getString(R.string.CierreCaja));
            }

            btnArqueo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utilidad.vibraticionBotones(contexto);
                    try {
                        TraerValoresPago task = new TraerValoresPago(new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) {
                                if (!WebService.errToken.equals("")) {
                                    Intent myIntent = new Intent(contexto, Login.class);
                                    startActivity(myIntent);
                                } else {
                                    if (WebService.configuracion.getCierre_app().equals("S")) {
                                        TraerCajasCobrador task = new TraerCajasCobrador(new AsyncResponse() {
                                            @Override
                                            public void processFinish(Object output) {
                                                if (!WebService.errToken.equals("")) {
                                                    Intent myIntent = new Intent(contexto, Login.class);
                                                    startActivity(myIntent);
                                                }else {
                                                    if (WebService.ArrayValores.size() > 0) {
                                                        Intent myIntent = new Intent(contexto, CierreCaja.class);
                                                        startActivity(myIntent);
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Sindatos), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }
                                        });
                                        task.execute();

                                    } else {
                                        if (WebService.ArrayValores.size() > 0) {
                                            Intent myIntent = new Intent(contexto, CierreCaja.class);
                                            startActivity(myIntent);
                                        } else {
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Sindatos), Toast.LENGTH_LONG).show();
                                        }
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
            });

            recibosGenerados = findViewById(R.id.btnFactGenerada);
            recibosGenerados.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Utilidad.vibraticionBotones(contexto);
                    if (Utilidad.isNetworkAvailable()) {
                        params = new RequestParams();
                        params.add("username", WebService.USUARIOLOGEADO);
                        TraerRecibosGenerados task = new TraerRecibosGenerados(new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) {
                                if (!WebService.errToken.equals("")) {
                                    Intent myIntent = new Intent(contexto, Login.class);
                                    startActivity(myIntent);
                                } else {
                                    if (WebService.listarecibos.size() != 0) {
                                        try {
                                            Intent myIntent = new Intent(contexto, RecibosGenerados.class);
                                            startActivity(myIntent);
                                        } catch (Exception exc) {
                                            exc.printStackTrace();
                                            //Toast.makeText( getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG ).show();
                                        }
                                    } else {
                                        Toast.makeText(contexto, getResources().getString(R.string.noGenerados), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
                        task.execute();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.ErrorConexion), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                        toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                        toast.show();//showing the toast is important**
                    }
                }
            });

            facturasGeneradas = findViewById(R.id.btnRecGenerada);

            if(WebService.configuracion.getFac_interes().equals("N")){
                facturasGeneradas.setVisibility(View.GONE);
            }
            facturasGeneradas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utilidad.vibraticionBotones(contexto);
                    if (Utilidad.isNetworkAvailable()) {
                        params = new RequestParams();
                        params.add("username", WebService.USUARIOLOGEADO);
                        TraerFacturasGeneradas task = new TraerFacturasGeneradas(new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) {
                                if (!WebService.errToken.equals("")) {
                                    Intent myIntent = new Intent(contexto, Login.class);
                                    startActivity(myIntent);
                                } else {
                                    if (WebService.listafact.size() != 0) {
                                        try {
                                            Intent myIntent = new Intent(contexto, FacturasGeneradas.class);
                                            startActivity(myIntent);
                                        } catch (Exception exc) {
                                            exc.printStackTrace();
                                            //Toast.makeText( getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG ).show();
                                        }
                                    } else {
                                        Toast.makeText(contexto, getResources().getString(R.string.noGeneradosFac), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
                        task.execute();
                    } else {
                        Utilidad.CargarToastConexion(contexto);
                    }
                }
            });


        } else {
            //System.out.println( "Antes de entrar a generados" );
            Intent myIntent = new Intent(contexto, RecibosGenerados.class);
            startActivity(myIntent);
        }
    }

    private class TraerRecibosGenerados extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface

        public TraerRecibosGenerados(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... strings) {
            WebService.TraerRecibosXDia("Cobranzas/recibosGenerados.php", params);
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
    }

    private class TraerFacturasGeneradas extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface

        public TraerFacturasGeneradas(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... strings) {
            WebService.TraerFacturasIntXDia("Cobranzas/facturasGeneradas.php", params);
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
    }

    private class TraerObservacionesDia extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface

        public TraerObservacionesDia(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... strings) {
            WebService.TraerObservacionesDia("Cobranzas/observacionesGenerados.php", params);
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
    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }


    private void BotonVolver(View v) {
        try{
        Utilidad.vibraticionBotones(contexto);
        if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("L") || WebService.usuarioActual.getTipoCobrador().equals("F")) {
            Intent myIntent = new Intent(contexto, SeleccionCliente.class);
            startActivity(myIntent);
        } else if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D")) {
                    Intent myIntent = new Intent(contexto, MenuCobranzas.class);
                    startActivity(myIntent);
        }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try{
        Utilidad.vibraticionBotones(contexto);
        BotonVolver(actualView);
        finish();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private class TraerValoresPago extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog(contexto);
        public AsyncResponse delegate = null;//Call back interface

        public TraerValoresPago(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... strings) {
                params2 = new RequestParams();
                params2.put("username", WebService.usuarioActual.getNombre());
                WebService.TraerValoresPago(params2, "Cobranzas/TraerValoresPago.php");
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
    }

    class TraerMonedas extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface
        public TraerMonedas(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            if(Utilidad.isNetworkAvailable())
            {
                WebService.ObtenerMonedas();
                return null;
            } else {}
            return null;
        }
        @Override
        public void onPreExecute() {
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
    protected void onResume() {
        super.onResume();
        if(WebService.monedas.size() == 0) {
            TraerMonedas task = new TraerMonedas(new AsyncResponse() {
                @Override
                public void processFinish(Object output) {
                    if (!WebService.errToken.equals("")) {
                        Intent myIntent = new Intent(contexto, Login.class);
                        startActivity(myIntent);
                    }
                }
            });
            task.execute();
        }
    }

    private class TraerCajasCobrador extends AsyncTask<String,Void,Void>{
        ProgressDialog dialog1 = new ProgressDialog(contexto);
        public AsyncResponse delegate = null;

        public TraerCajasCobrador(AsyncResponse asyncResponse)
        {
            delegate = asyncResponse;
        }

        @Override
        protected Void doInBackground(String... strings) {
            if(Utilidad.isNetworkAvailable()) {
                WebService.TraerCajasCobrador("Cobranzas/TraerCajasCobrador.php");
                return null;
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
    }
}

