package com.example.user.trucksales.Visual.Cobranza;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.Anulada;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RecibosAnulados extends AppCompatActivity {

    Context contexto;
    TextView fecha,usuario,encabezado;
    ImageView atras;
    private static TableLayout tablaAnulados;
    Utilidades Utilidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        this.requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_recibos_anulados );

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        contexto = this;
        Utilidad = new Utilidades( contexto );
        if(WebService.USUARIOLOGEADO != null) {
            try {
                if (Utilidad.isNetworkAvailable()) {
                    atras = findViewById(R.id.btnAtras);
                    atras.setClickable(true);
                    atras.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(contexto);
                            Intent myIntent = new Intent(v.getContext(), ConsultasCobrador.class);
                            startActivity(myIntent);
                        }
                    });

                    String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                    usuario = findViewById(R.id.nombreUsuario);
                    usuario.setText(getResources().getString(R.string.usuarioTool) + WebService.USUARIOLOGEADO);
                    encabezado = (TextView) findViewById(R.id.cantiadaTraida);
                    fecha = (TextView) findViewById(R.id.Fecha);
                    fecha.setText(timeStamp);
                    tablaAnulados = findViewById(R.id.tabla);

                    TraerRecibosAnulados task = new TraerRecibosAnulados(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            if (!WebService.errToken.equals("")) {
                                Intent myIntent = new Intent(contexto, Login.class);
                                startActivity(myIntent);
                            } else {
                                CargarDatos();
                                String encabe = getResources().getString(R.string.anuladasHoyRec) + " " + WebService.ArrayRecibosAnuladas.size();
                                encabezado.setText(encabe);
                            }

                        }
                    });
                    task.execute();

                   /* String encabe = getResources().getString(R.string.anuladasHoyRec) + " " + WebService.ArrayRecibosAnuladas.size();
                    encabezado.setText(encabe);*/


            } else{
                    Utilidad.CargarToastConexion(contexto);
                }
            }catch (Exception ex){
                ex.printStackTrace();
                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
            }
        }
        else
        {
            Intent myIntent = new Intent( contexto, Login.class );
            startActivity( myIntent );
        }
    }

    @Override
    public void onBackPressed() {
        try{
        Utilidad.vibraticionBotones( contexto );
        Intent myIntent = new Intent( contexto, ConsultasCobrador.class );
        startActivity( myIntent );
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    private class TraerRecibosAnulados extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog(contexto);
        public AsyncResponse delegate = null;//Call back interface

        public TraerRecibosAnulados(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {
            RequestParams params1 = new RequestParams();
            params1.put("username", WebService.USUARIOLOGEADO);
            WebService.TraerListaRecibosAnuladas(params1, "Cobranzas/recibosAnulados.php");
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
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void CargarDatos(){
        if (WebService.ArrayRecibosAnuladas.size() == 0) {
            encabezado.setText(getResources().getString(R.string.noHayRecAnuladas));
            Intent myIntent = new Intent( contexto, ConsultasCobrador.class );
            startActivity( myIntent );
        } else {
            TableRow tr1 = new TableRow(this);
            TableRow tr2 = new TableRow(this);
            for (int i = 0; i < WebService.ArrayRecibosAnuladas.size(); i++) {
                try {
                    final Anulada instaAnulada = WebService.ArrayRecibosAnuladas.get(i);

                    //  tr1.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT ) );
                    tr2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 20));
                    tr1 = (TableRow) getLayoutInflater().inflate(R.layout.anulados, null);
                    tr2 = new TableRow(this);

                    final TextView cliente = new TextView(this);
                    cliente.setText(getResources().getString(R.string.clienteGenerados) + " " + instaAnulada.getNom_tit().trim() + "\n" +
                            getResources().getString(R.string.Nro_Docum) + " " + instaAnulada.getNro_docum().trim() + "\n" +
                            //getResources().getString( R.string.ViajesGen )+": "+instaAnulada.getCod_tit().trim()+"\n"+
                            getResources().getString(R.string.FecGen) + " " + instaAnulada.getHora());

                    cliente.setTextSize(15);
                    cliente.setTextColor(Color.parseColor("#000000"));
                    final TextView espacio = new TextView(this);
                    espacio.setText("\n");

                    tr1.setPadding(0, 20, 0, 30);

                    tr1.addView(cliente);
                    tr2.addView(espacio);

                    tr2.setBackgroundResource(R.color.blanco);
                    tablaAnulados.addView(tr1);
                    tablaAnulados.addView(tr2);
                } catch (Exception exxc) {
                    exxc.printStackTrace();
                    //Toast.makeText( getApplicationContext(), exxc.getMessage(), Toast.LENGTH_LONG ).show();
                }
            }
        }
    }
}
