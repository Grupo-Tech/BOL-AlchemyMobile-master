package com.example.user.trucksales.Visual.Cobranza;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Encapsuladoras.Viaje;
import com.example.user.trucksales.Visual.TruckSales.ClienteXDefecto;
import com.example.user.trucksales.Encapsuladoras.ClienteCobranza;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.Datos.WebService;
import com.loopj.android.http.RequestParams;
import com.example.user.trucksales.Negocio.Utilidades;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MenuCobranzas extends Activity {
    public Context contexto;
    private Utilidades Utilidad;
    TextView nroDocum, fechaVencimineto, cod_moneda, nombreUsu, fecha, txtNombUsu;
    TableLayout tablaClientes;
    //ListView tablaClientes;
    ImageView atras, BtnConsulta, btnSeleccionarCaja;

    String imei;
    static final Integer PHONESTATS = 0x1;

    //public ScrollView Lista;
    protected static RequestParams params2 = new RequestParams();
    protected static RequestParams params1 = new RequestParams();
    protected static RequestParams params = new RequestParams();

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ai = cm.getActiveNetworkInfo();
        return ai != null;
    }

    @Override
    public void onBackPressed() {
        Utilidad.vibraticionBotones(contexto);
        Intent myIntent = new Intent(contexto, Login.class);
        startActivity(myIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebService.EstadoActual = 1;

        setContentView(R.layout.activity_menu_cobranzas);
        contexto = this;

        WebService.viajeSeleccionadoCobrador = new Viaje();
        Utilidad = new Utilidades(contexto);

        atras = findViewById(R.id.btnAtras);
        atras.setClickable(true);
        atras.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utilidad.vibraticionBotones(contexto);
                Intent myIntent = new Intent(v.getContext(), Login.class);
                startActivity(myIntent);
            }
        });

        BtnConsulta = findViewById(R.id.BtnConsulta);
        BtnConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidad.vibraticionBotones(contexto);
                Intent myIntent = new Intent(v.getContext(), ConsultasCobrador.class);
                startActivity(myIntent);
            }
        });

        if (WebService.USUARIOLOGEADO != null) {
            try {
                if (Utilidad.isNetworkAvailable()) {

                    tablaClientes = (TableLayout) findViewById(R.id.tabla);
                    //tablaClientes = (ListView) findViewById( R.id.tabla );
                    String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                    nombreUsu = (TextView) findViewById(R.id.LblUsu);
                    fecha = (TextView) findViewById(R.id.LblFecha);
                    fecha.setText(timeStamp);
                    nombreUsu.setText(getResources().getString(R.string.usuarioTool) + WebService.USUARIOLOGEADO);

                    final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

                    swipeRefreshLayout.setColorSchemeColors(Color.BLACK);
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            swipeRefreshLayout.setRefreshing(true);
                            CargarViajesCobrador task = new CargarViajesCobrador(new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {
                                    tablaClientes.removeAllViews();
                                    CargarDatos();
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            });
                            task.execute();
                        }
                    });

                    txtNombUsu = (TextView) findViewById(R.id.NomUus);

                    if (WebService.recibo_class == false) {
                        CargarViajesCobrador task = new CargarViajesCobrador(new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) {
                                txtNombUsu.setText(getResources().getString(R.string.Hola) + " " + WebService.USUARIOLOGEADO + ", " + getResources().getString(R.string.Tienes) + " " + WebService.viajesCobradorUsu.size() + " " + getResources().getString(R.string.Viajes) + "\n" + " " + getResources().getString(R.string.Selec));
                                txtNombUsu.setGravity(Gravity.CENTER);
                                CargarDatos();
                            }
                        });
                        task.execute();
                    } else {
                        WebService.recibo_class = false;
                        txtNombUsu.setText(getResources().getString(R.string.Hola) + " " + WebService.USUARIOLOGEADO + ", " + getResources().getString(R.string.Tienes) + " " + WebService.viajesCobradorUsu.size() + " " + getResources().getString(R.string.Viajes) + "\n" + " " + getResources().getString(R.string.Selec));
                        txtNombUsu.setGravity(Gravity.CENTER);
                        CargarDatos();
                    }
                } else {
                    Utilidad.CargarToastConexion(contexto);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                // Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
            }
        } else {
            Intent myIntent = new Intent(contexto, Login.class);
            startActivity(myIntent);
        }
    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    private class TraerClientesViajes extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog(contexto);
        public AsyncResponse delegate = null;//Call back interface

        public TraerClientesViajes(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {
            if (Utilidad.isNetworkAvailable()) {
                WebService.TraerClientesViajes(params2, "Viajes/ViajesCobrador/TraerClientesViajes.php");

                return null;
            }
            return null;
        }

        @Override
        public void onPreExecute() {
            dialog1.setMessage(getResources().getString(R.string.cargando_dialog));
            dialog1.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog1.isShowing()) {
                dialog1.dismiss();
            }
            delegate.processFinish(WebService.logueado);
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    private void CargarDatos() {
        for (int i = 0; i < WebService.viajesCobradorUsu.size(); i++) {
            try {
                final Viaje cliente = WebService.viajesCobradorUsu.get(i);
                TableRow tr1 = (TableRow) getLayoutInflater().inflate(R.layout.trviajes, null);
                TableRow tr2 = new TableRow(this);
                final TextView salto = new TextView(this);
                salto.setText("\n");
                tr2.addView(salto);

                ImageView cliIcon = new ImageView(contexto);
                cliIcon.setImageResource(R.drawable.cliente);
                cliIcon.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                cliIcon.getLayoutParams().height = 125;
                cliIcon.getLayoutParams().width = 125;
                cliIcon.requestLayout();

                final TextView numViajePosta = new TextView(contexto);
                numViajePosta.setText(getResources().getString(R.string.NumViajeProd) + " " + cliente.getNumViaje().trim() + "\n" + getResources().getString(R.string.Fecha) + " " + cliente.getFecha().trim());
                numViajePosta.setTextSize(20);
                numViajePosta.setPadding(15, 0, 0, 0);
                numViajePosta.setTextColor(Color.parseColor("#000000"));
                numViajePosta.setTypeface(null, Typeface.BOLD);

                tr1.setClickable(true);
                tr1.setBackgroundResource(R.color.viajes);

                tr1.addView(cliIcon);
                tr1.addView(numViajePosta);

                tr1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(final View v) {
                        Utilidad.vibraticionBotones(contexto);
                        if (Utilidad.isNetworkAvailable()) {
                            WebService.viajeSeleccionadoCobrador = new Viaje();
                            WebService.viajeSeleccionadoCobrador = cliente;
                            if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D")) {
                                // if (WebService.usuarioActual.getTipoCobrador().equals("D")) {
                                params2.put("num_viaje", WebService.viajeSeleccionadoCobrador.getNumViaje());
                                params2.put("username", WebService.USUARIOLOGEADO);
                                try {
                                    TraerClientesViajes task2 = new TraerClientesViajes(new AsyncResponse() {
                                        @Override
                                        public void processFinish(Object output) {
                                            if (!WebService.errToken.equals("")) {
                                                Intent myIntent = new Intent(contexto, Login.class);
                                                startActivity(myIntent);
                                            } else {
                                                if (WebService.clienteTraidos.size() > 0) {
                                                    //SI LAS ENTREGAS POSIBLES SON MAYORES A 0 (SI EXISTEN ENTREGAS) SIGUE EL CODIGO NORMAL
                                                    if (WebService.viajeSeleccionadoCobrador != null) {
                                                        for (int i = 0; i < WebService.clienteTraidos.size(); i++) {
                                                            ClienteCobranza cliente = WebService.clienteTraidos.get(i);
                                                            WebService.clienteActual = cliente;
                                                            break;
                                                        }
                                                        Intent myIntent = new Intent(contexto, ClienteXDefecto.class);
                                                        startActivity(myIntent);
                                                    }
                                                } else {
                                                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.ToastText1) + WebService.reto_AgregaCobranza, Toast.LENGTH_SHORT);
                                                    toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                                                    toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                                                    toast.show();//showing the toast is important**
                                                }
                                            }
                                        }
                                    });
                                    task2.execute();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                                }
                                //}
                            }
                        }
                    }
                });
                tablaClientes.addView(tr1);
                tablaClientes.addView(tr2);

            } catch (Exception exc) {
                exc.printStackTrace();
                // Toast.makeText( getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG ).show();
            }
        }
    }

    private class CargarViajesCobrador extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog(contexto);
        public AsyncResponse delegate = null;//Call back interface

        public CargarViajesCobrador(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {
            params1 = new RequestParams();
            params1.put("username", WebService.usuarioActual.getNombre());
            WebService.TraerViajesCobrador(params1, "Viajes/ViajesCobrador/ViajesCobrador.php");
            return null;
        }

        @Override
        public void onPreExecute() {
            Utilidad.showLoadingMessage();
            /*
            dialog1.setMessage(getResources().getString( R.string.cargando_dialog));
            dialog1.show();*/
        }

        @Override
        protected void onPostExecute(Void result) {
            delegate.processFinish(WebService.logueado);
            Utilidad.deleteLoadingMessage();
            if (!WebService.errToken.equals("")) {
                Intent myIntent = new Intent(contexto, Login.class);
                startActivity(myIntent);
            }
            /*if(dialog1.isShowing())
            {
                dialog1.dismiss();
            }
            delegate.processFinish( WebService.logueado);*/
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}
