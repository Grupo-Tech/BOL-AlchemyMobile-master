package com.example.user.trucksales.Visual.Cobranza;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.print.PrintManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.FacturaXDia;
import com.example.user.trucksales.Negocio.ApplicationContext;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Printer;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.adapter.AdaptadorGenerados;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RecibosGenerados extends AppCompatActivity {

    private static Context contextG;
    private static RequestParams params1;
    TextView nombreUsuario, txtFecha, holaUsu;
    WebView mWebView;
    ListView tablaGenerados;
    private static ImageView flecha;
    private Utilidades Utilidad;
    public static boolean Anulado = false;
   // FacturaXDia ri = new FacturaXDia();
    RequestParams params4 = new RequestParams();
    private AdaptadorGenerados dataAdapterRuta;

    public String nom_copia;

    //IMPRESION
    ApplicationContext contextoImpre;
    ArrayList<String> getbtName = new ArrayList<>();
    ArrayList<String> getbtNM = new ArrayList<>();
    ArrayList<String> getbtMax = new ArrayList<>();
    public boolean mBconnect = false;
    public int state = 0;

    public int anterior = 0;
    public int actual = 0;

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    private class AnularRecibo extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contextG );
        public RecibosGenerados.AsyncResponse delegate = null;//Call back interface

        public AnularRecibo(RecibosGenerados.AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {
            if (Utilidad.isNetworkAvailable()) {

                WebService.AnularRecibo( params1, "Cobranzas/AnularRecibo.php" );
                return null;

            } else {
                Toast toast = Toast.makeText( getApplicationContext(), getResources().getString( R.string.ErrorConexion ), Toast.LENGTH_SHORT );
                toast.setGravity( Gravity.CENTER, 0, 0 ); // last two args are X and Y are used for setting position
                toast.setDuration( Toast.LENGTH_LONG );//you can even use milliseconds to display toast
                toast.show();//showing the toast is important**

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
        protected void onProgressUpdate(Void... values) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

    try {
        setContentView( R.layout.activity_recibos_generados );
        contextG = this;
        Utilidad = new Utilidades( contextG );
        try {
            if (WebService.USUARIOLOGEADO != null) {
                if (Utilidad.isNetworkAvailable()) {

                    flecha = findViewById(R.id.btnAtras);
                    flecha.setClickable(true);
                    flecha.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(contextG);
                            Intent myIntent = new Intent(v.getContext(), ConsultasCobrador.class);
                            startActivity(myIntent);
                        }
                    });
                    holaUsu = (TextView) findViewById(R.id.Rotulo);
                    holaUsu.setText(getResources().getString(R.string.Hola) + " " + WebService.USUARIOLOGEADO + " " + getResources().getString(R.string.Tienes) + " " + WebService.listarecibos.size() + " " + getResources().getString(R.string.recibGen));
                    //tablaGenerados = findViewById(R.id.tabla);
                    nombreUsuario = findViewById(R.id.nombreUsuario);
                    nombreUsuario.setText(getResources().getString(R.string.usuarioTool) + WebService.USUARIOLOGEADO);

                    String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                    txtFecha = (TextView) findViewById(R.id.Fecha);
                    txtFecha.setText(timeStamp);

                    tablaGenerados = (ListView) findViewById(R.id.tabla);

                    if (WebService.listarecibos.size() == 0) {
                        final TextView mensaje = new TextView(contextG);
                        TableRow tr1 = new TableRow(this);
                        mensaje.setText("No existen registros de la fecha actual de recibos");
                        tablaGenerados.addView(tr1);
                    }else {
                       // for(int i=0; i<WebService.listarecibos.size();i++) {
                            dataAdapterRuta = new AdaptadorGenerados(RecibosGenerados.this, WebService.listarecibos);
                            // Desplegamos los elementos en el ListView
                            tablaGenerados.setAdapter(dataAdapterRuta);
                            dataAdapterRuta.notifyDataSetChanged();
                        //}
                    }

                   /* TableRow tr1 = new TableRow(this);
                    TableRow tr2 = new TableRow(this);
                    if (WebService.listarecibos.size() == 0) {
                        final TextView mensaje = new TextView(contextG);
                        mensaje.setText("No existen registros de la fecha actual de recibos");
                        tablaGenerados.addView(tr1);
                    }
                    if (WebService.listarecibos.size() != 0) {
                        for (int i = 0; WebService.listarecibos.size() > i; i++) {
                            try {
                                final FacturaXDia n_rec = WebService.listarecibos.get(i);
                                final TableRow trBoton = new TableRow( contextG );
                                trBoton.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT ) );
                                tr1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                tr2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 20));
                                tr1 = new TableRow(contextG);
                                tr2 = new TableRow(contextG);

                                final TextView cliente = new TextView(contextG);
                                //ImageView cajita = new ImageView(contextG);
                                final TextView espacio = new TextView(contextG);
                                espacio.setText("\n");

                                cliente.setText(getResources().getString(R.string.clienteGenerados) + " " + n_rec.getNom_Tit().trim() + "\n" +
                                        getResources().getString(R.string.Nro_Docum) + " " + n_rec.getNro_Docum().trim() + "\n" +
                                        // getResources().getString(R.string.ViajesGen) + " " + n_fac.getCod_Tit().trim() + "\n" +
                                        getResources().getString(R.string.FecGen) + " " + n_rec.getHora());
                                cliente.setTextSize(15);
                                cliente.setBackgroundResource(R.color.viajes);

                                ImageView cajita = new ImageView( contextG );


                                if(n_rec.getCalcula_interes().equals("S")){
                                    cajita.setImageResource( R.drawable.percentage );
                                    cajita.setBackgroundResource(R.color.blanco);
                                    cajita.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT,
                                            TableRow.LayoutParams.WRAP_CONTENT ) );
                                    cajita.getLayoutParams().height = 90;
                                    cajita.getLayoutParams().width = 90;
                                    cajita.setClickable( true );
                                    cajita.requestLayout();
                               }

                                cajita.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try{
                                        Utilidad.vibraticionBotones(contextG);
                                        params1 = new RequestParams();
                                        params1.put("cod_tit", n_rec.getCod_Tit().trim());
                                        params1.put("cod_emp", n_rec.getCod_emp().trim());
                                        CalculaInteres task2 = new CalculaInteres(new AsyncResponse() {
                                            @Override
                                            public void processFinish(Object output) {

                                                params1 = new RequestParams();
                                                params1.put("username", WebService.USUARIOLOGEADO);
                                                params1.put("cod_emp", n_rec.getCod_emp().trim());
                                                ObtenerNumRecomendado task1 = new ObtenerNumRecomendado(new AsyncResponse() {
                                                    @Override
                                                    public void processFinish(Object output) {
                                                        WebService.usuarioActual.setEmpresa(n_rec.getCod_emp());
                                                        WebService.clienteActual.setCodEmp(n_rec.getCod_emp());
                                                        WebService.clienteActual.setCod_Tit_Gestion(n_rec.getCod_Tit());
                                                        WebService.clienteActual.setNomEmp(n_rec.getNom_emp());
                                                        WebService.clienteActual.setNom_Tit(n_rec.getNom_Tit());
                                                        WebService.clienteActual.setCod_Moneda(n_rec.getCod_moneda());
                                                        Intent myIntent = new Intent(contextG, FacturaCobranza.class);
                                                        myIntent.putExtra("intent", "Generados");
                                                        startActivity(myIntent);
                                                    }
                                                });
                                                task1.execute();
                                            }
                                        });
                                        task2.execute();
                                    }catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                });

                                final ImageView anular = new ImageView(contextG);

                                anular.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT));
                                anular.setImageResource(R.drawable.cancelar);
                                anular.setBackgroundResource(R.color.blanco);
                                anular.getLayoutParams().height = 90;
                                anular.getLayoutParams().width = 90;
                                anular.setClickable(true);
                                anular.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        Utilidad.vibraticionBotones(contextG);
                                        params1 = new RequestParams();
                                        params1.put("nrotrans", n_rec.getNro_Trans());
                                        params1.put("username", WebService.USUARIOLOGEADO);
                                        if (Utilidad.isNetworkAvailable()) {
                                            final TableRow tRow = (TableRow) v.getParent();
                                            int i = 0;
                                            TableRow tablaActual = (TableRow) tablaGenerados.getChildAt(i);
                                            while (tRow != tablaActual) {
                                                i++;
                                                tablaActual = (TableRow) tablaGenerados.getChildAt(i);
                                            }
                                            anterior = i - 1;
                                            actual = i;

                                        } else {
                                            Utilidad.CargarToastConexion(contextG);
                                        }

                                        displayAlertEliminar();

                                    }
                                });
                                anular.requestLayout();
                                TableRow.LayoutParams Parametros = new TableRow.LayoutParams();
                                Parametros.weight = (float) 0.5;
                                Parametros.width = 400;
                                Parametros.rightMargin = 100;
                                cliente.setLayoutParams(Parametros);
                                tr1.addView(cliente);
                                trBoton.addView(anular);
                                trBoton.addView(cajita);

                                tr1.setClickable(true);
                                tr1.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        Utilidad.vibraticionBotones(contextG);
                                        if (Utilidad.isNetworkAvailable()) {
                                            if (WebService.configuracion.getTipo_impresora().equals("T")) {
                                                FacturaXDia ri = new FacturaXDia();
                                                ri = n_rec;

                                                PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
                                                Printer pr = new Printer();
                                                pr.setPrMang(printManager);
                                                pr.setContx(contextG);
                                                pr.setValor(ri.getNro_Trans().trim());
                                                pr.setTipo("RE");
                                                pr.genarPdf(pr);

                                /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(WebService.WEB_URL));
                                startActivity(browserIntent);*/
                                          /*  } else {
                                                try {
                                                    /*FacturaXDia ri = new FacturaXDia();
                                                    ri = n_rec;*/
                    //displayAlertOC();
                                              /*     contextoImpre = new ApplicationContext();
                                                    contextoImpre = (ApplicationContext) getApplicationContext();
                                                    contextoImpre.setObject();
                                                    params4 = new RequestParams();
                                                    nom_copia = "Copia";
                                                    params4.add("nom_copia", nom_copia);
                                                    params4.put("nrotrans", n_rec.getNro_Trans());
                                                    Conectar();

                                                    TraerDatoImpresion task = new TraerDatoImpresion(new AsyncResponse() {
                                                        @Override
                                                        public void processFinish(Object output) {
                                                            Impr();
                                                        }
                                                    });
                                                    task.execute();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    //Toast.makeText( getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG ).show();
                                                }
                                            }
                                        } else {
                                            Utilidad.CargarToastConexion(contextG);
                                        }
                                    }
                                });
                                tr2.addView(espacio);

                                tr1.setPadding(0, 20, 0, 30);
                                tr1.setBackgroundResource(R.color.viajes);
                                //trBoton.setBackgroundResource(R.color.viajes);
                                tr2.setBackgroundResource(R.color.blanco);
                                tr1.setClickable(true);

                                tablaGenerados.addView(tr1);
                                tablaGenerados.addView(trBoton);
                                tablaGenerados.addView(tr2);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                               // Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                            }*/
                        //}
                }else{
                    Utilidad.CargarToastConexion(contextG);
                }
            }else {
                Intent myIntent = new Intent( contextG, Login.class );
                startActivity( myIntent );
            }
        } catch (Exception error) {
            error.printStackTrace();
            //Toast.makeText( getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG ).show();
        }
        GuardarDatosUsuario.Contexto = contextG;
    } catch (Exception error) {
        error.printStackTrace();
       // Toast.makeText( getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG ).show();
    }
}//Cierra el oncreate

   /* private void ActualizarLista(int indexDesde,int IndexCantidad) {
        try{
            tablaGenerados.removeViews( indexDesde,IndexCantidad );
            } catch (Exception error) {
            error.printStackTrace();
           // Toast.makeText( getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }*/

    @Override
    public void onBackPressed() {
        try{
        Utilidad.vibraticionBotones( contextG );
        Intent myIntent = new Intent( contextG, ConsultasCobrador.class );
        startActivity( myIntent );
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void Imprimir(String datos){
        contextoImpre.getObject().CON_PageStart(contextoImpre.getState(), false, 0, 0);
        contextoImpre.getObject().ASCII_CtrlOppositeColor(contextoImpre.getState(), false);
        contextoImpre.getObject().ASCII_CtrlAlignType(contextoImpre.getState(), 0);
        contextoImpre.getObject().ASCII_PrintString(contextoImpre.getState(), 0,  0,
                0,  0, 0, datos, "gb2312");
        contextoImpre.getObject().ASCII_CtrlFeedLines(contextoImpre.getState(), 1);
        contextoImpre.getObject().ASCII_CtrlPrintCRLF(contextoImpre.getState(), 1);
        contextoImpre.getObject().CON_PageEnd(contextoImpre.getState(), contextoImpre.getPrintway());

        contextoImpre.getObject().CON_CloseDevices(contextoImpre.getState());
    }

    public void Conectar(){
        getbtNM = new ArrayList<>();
        getbtName = new ArrayList<>();
        getbtMax = new ArrayList<>();

        getbtNM = (ArrayList<String>) contextoImpre.getObject().CON_GetWirelessDevices(0);
        for (int i = 0; i < getbtNM.size(); i++) {
            getbtName.add(getbtNM.get(i).split(",")[0]);
            getbtMax.add(getbtNM.get(i).split(",")[1].substring(0, 17));
        }

        connect(getbtMax.get(0),200);
        contextoImpre.getObject().CON_QueryStatus(contextoImpre.getState());
    }

    public void connect(String port,int tiempo_conexion) {
        if (!mBconnect) {
            state = contextoImpre.getObject().CON_ConnectDevices(getbtName.get(0), port, tiempo_conexion);
            if (state > 0) {
                Toast.makeText(this, R.string.mes_consuccess, Toast.LENGTH_SHORT).show();
                mBconnect = true;
                contextoImpre.setState(state);
                contextoImpre.setName(getbtName.get(0));
                contextoImpre.setPrintway(0);
                mBconnect = false;
            } else {
                Toast.makeText(this, R.string.mes_confail, Toast.LENGTH_SHORT).show();
                mBconnect = false;
            }
        }
    }

    public void Impr() {
        try {
            String str = WebService.respuestaWSImpresora;
            System.out.println(str);
            Imprimir(str);
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText( getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }

    public void refreshList() {
        dataAdapterRuta.notifyDataSetChanged();
    }

}
