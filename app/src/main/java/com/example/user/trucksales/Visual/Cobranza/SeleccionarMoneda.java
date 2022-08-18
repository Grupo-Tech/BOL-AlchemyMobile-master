package com.example.user.trucksales.Visual.Cobranza;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.Encapsuladoras.ClienteCobranza;
import com.example.user.trucksales.Encapsuladoras.Moneda;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.Visual.SeleccionFuncionablidad;
import com.example.user.trucksales.Datos.WebService;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SeleccionarMoneda extends Activity
{
    private Utilidades Utilidad;
    ImageView casita,atras;
    TextView nombreUsu,fecha;
    Context context;
    TableLayout tablaMonedas;

    private class  TraerDeudores extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface
        public TraerDeudores(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            if(Utilidad.isNetworkAvailable())
            {
                RequestParams params1 = new RequestParams(  );
                params1.put( "cod_tit", WebService.clienteActual.getCod_Tit() );
                params1.put( "cod_empresa",WebService.usuarioActual.getEmpresa().trim() );
                params1.put("username", WebService.USUARIOLOGEADO.trim());
                WebService.TraerDeudas(params1);
                return null;

            }
            else
            {
            }

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
        protected void onProgressUpdate(Void... values)
        {

        }
    }
    public interface AsyncResponse {
        void processFinish(Object output);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        this.requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_seleccionar_moneda );
        context = this;

        Utilidad = new Utilidades(context);

        if(WebService.usuarioActual!=null) {
            try {
                if (Utilidad.isNetworkAvailable()) {
                    String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                    nombreUsu = (TextView) findViewById(R.id.LblUsu);
                    fecha = (TextView) findViewById(R.id.LblFecha);
                    fecha.setText(timeStamp);
                    atras = findViewById(R.id.btnAtras);
                    atras.setClickable(true);
                    atras.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(context);
                            Intent myIntent = new Intent(v.getContext(), SeleccionFuncionablidad.class);
                            startActivity(myIntent);
                        }
                    });
                    casita = findViewById(R.id.casita);
                    casita.setClickable(true);
                    casita.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(context);
                            Intent myIntent = new Intent(v.getContext(), SeleccionFuncionablidad.class);
                            startActivity(myIntent);
                        }
                    });
                    nombreUsu.setText(getResources().getString(R.string.usuarioTool) + WebService.USUARIOLOGEADO);
                    tablaMonedas = (TableLayout) findViewById(R.id.tabla);
                    for (int i = 0; i < WebService.monedas.size(); i++) {
                        final Moneda instaMoneda = WebService.monedas.get(i);
                        final TextView NombreTit = new TextView(context);
                        TableRow tr1 = (TableRow) getLayoutInflater().inflate(R.layout.trviajes, null);
                        TableRow tr2 = new TableRow(this);
                        final TextView salto = new TextView(this);
                        salto.setText("\n");
                        tr2.addView(salto);

                        ImageView cliIcon = new ImageView(context);
                        final TextView numViajePosta = new TextView(context);
                        numViajePosta.setText(instaMoneda.getNom_Moneda());
                        numViajePosta.setTextSize(20);

                        cliIcon.setImageResource(R.drawable.moneda);
                        cliIcon.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));


                        cliIcon.getLayoutParams().height = 125;
                        cliIcon.getLayoutParams().width = 125;
                        cliIcon.requestLayout();

                        tr1.addView(NombreTit);
                        tr1.addView(numViajePosta);
                        tr1.addView(cliIcon);
                        tr1.setClickable(true);
                        NombreTit.setBackgroundResource(R.color.viajes);

                        tr1.setOnClickListener(new View.OnClickListener() {
                            public void onClick(final View v) {
                                if (Utilidad.isNetworkAvailable()) {
                                    WebService.monedaSeleccionada = instaMoneda;
                                    ClienteCobranza instaCliente = new ClienteCobranza();
                                    WebService.deudasFiltradasXMoneda = new ArrayList<ClienteCobranza>();
                                    for (int i = 0; i < WebService.deudasViaje.size(); i++) {
                                        instaCliente = new ClienteCobranza();
                                        instaCliente = WebService.deudasViaje.get(i);
                                        if (instaCliente.getCod_Moneda().equals(instaMoneda.getCod_Moneda())) {
                                            WebService.deudasFiltradasXMoneda.add(instaCliente);
                                        }
                                    }
                                    TraerDeudores task = new TraerDeudores(new AsyncResponse() {
                                        public void processFinish(Object output) {
                                            Intent myIntent = new Intent(context, SeleccionarDeudas.class);
                                            startActivity(myIntent);
                                        }
                                    });
                                    task.execute();
                                }
                            }
                        });
                        tablaMonedas.addView(tr1);
                        tablaMonedas.addView(tr2);

                    }
                }else{
                    Utilidad.CargarToastConexion(context);
                }
            }catch (Exception ex){
                ex.printStackTrace();
                Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
            }
        }
        else
        {
            Intent myIntent = new Intent( context, Login.class );
            startActivity( myIntent );
        }
    }

  /*  if (WebService.distintasMonedas) {
    for (int i = 0; i < WebService.deudasFiltradasXMoneda.size(); i++) {

        final ClienteCobranza instaDeuda = WebService.deudasFiltradasXMoneda.get(i);
        instaDeuda.setTotalEntregado(0);
        //WebService.clienteActual.setCod_Moneda(instaDeuda.getCod_Moneda()) ;
        //instaDeuda.setTotalInteresEnt( 0 );
        final TextView NombreTit = new TextView(context);
        //final TextView Interes = new TextView( context );
        TableRow tr1 = (TableRow) getLayoutInflater().inflate(R.layout.trviajes, null);
        TableRow tr2 = new TableRow(this);
        final TextView salto = new TextView(this);
        salto.setText("\n");
        tr2.addView(salto);
        tr1.setGravity(3);
        final TextView numViajePosta = new TextView(context);
        String fecha = instaDeuda.getFecha_Vence().substring(0, 10);
        numViajePosta.setText(CargarTexto(instaDeuda));
        numViajePosta.setTextSize(15);

        final ImageView checkBox = new ImageView(context);
        checkBox.setImageResource(R.drawable.negative);
        checkBox.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));


        checkBox.getLayoutParams().height = 125;
        checkBox.getLayoutParams().width = 125;
        checkBox.requestLayout();

        final ImageView partial = new ImageView(context);
        partial.setImageResource(R.drawable.parcialpay);
        partial.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));


        partial.getLayoutParams().height = 125;
        partial.getLayoutParams().width = 125;
        partial.requestLayout();
        partial.setClickable(true);

        partial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partial.setImageResource(R.drawable.parcialpaypositive);
            }
        });

        if (instaDeuda.getCod_Docum().trim().equals("NCREDITO")) {
            tr1.setBackgroundResource(R.color.Ncredito);
        }

        tr1.addView(NombreTit);
        tr1.addView(numViajePosta);
        tr1.addView(checkBox);
        tr1.addView(partial);
        tr1.setClickable(true);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (instaDeuda.getEstado() == 0) {
                    instaDeuda.setEstado(1);
                    checkBox.setImageResource(R.drawable.positive);
                    double val = instaDeuda.getImp_mov_mo();
                    val_tot = val_tot + val;
                    instaDeuda.setTotalEntregado(instaDeuda.getImp_mov_mo());
                    WebService.addDeuda(instaDeuda, instaDeuda.getImp_mov_mo());
                    total.setText(getResources().getString(R.string.TotalDeuda) + String.valueOf(val_tot + val));
                    WebService.setTotalDeudas(val_tot + val);

                    double reten = instaDeuda.getImp_a_retenc();
                    totalRetenSelec = totalRetenSelec + reten;
                    instaDeuda.setImp_a_retenc(instaDeuda.getImp_a_retenc());
                    WebService.addReten(instaDeuda, instaDeuda.getImp_a_retenc());
                    WebService.setTotalReten(totalRetenSelec + reten);

                    numViajePosta.setText(Html.fromHtml(CargarTexto(instaDeuda)));
                } else {
                    instaDeuda.setEstado(0);
                    checkBox.setImageResource(R.drawable.negative);
                    double val = Double.valueOf(instaDeuda.getImp_mov_mo());
                    instaDeuda.setTotalEntregado(0);
                    WebService.removeDeuda(instaDeuda);
                    val_tot = val_tot - val;
                    numViajePosta.setText(Html.fromHtml(CargarTexto(instaDeuda)));
                    total.setText(getResources().getString(R.string.TotalDeuda) + String.valueOf(val_tot));
                    WebService.setTotalDeudas(val_tot);

                    WebService.removeReten(instaDeuda);
                    double reten = instaDeuda.getImp_a_retenc();
                    totalRetenSelec = totalRetenSelec + reten;
                    instaDeuda.setImp_a_retenc(instaDeuda.getImp_a_retenc());
                    WebService.addReten(instaDeuda, instaDeuda.getImp_a_retenc());
                    WebService.setTotalReten(totalRetenSelec + reten);

                }
            }
        });
        tablaDeudas.addView(tr1);
        tablaDeudas.addView(tr2);

    }
}*/
}
