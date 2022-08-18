package com.example.user.trucksales.Visual.TruckSales;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.Entrega;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Cobranza.IngresarObservaciones;
import com.example.user.trucksales.Visual.Login;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

public class SalirViaje extends AppCompatActivity {
    public TextView Texto;
    public ImageView ImgVolver,ImgConsultas;
    public Button btnVolver,OtroEnvio,Retornar,Generados;
    public Context Contexto;
    private Utilidades Utilidad;
    public static ArrayList<String> URLDistanciaInicial1 = new ArrayList<>();
    public static RequestParams params1= new RequestParams();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        this.requestWindowFeature( Window.FEATURE_NO_TITLE);
        setContentView( R.layout.activity_salir_viaje );

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //System.out.println("Entra en salir viaje");
        Contexto = this;

        GuardarDatosUsuario.Contexto = Contexto;
        Utilidad = new Utilidades(Contexto);

        try{
        btnVolver = findViewById( R.id.CXD_Button_LogOut );
        btnVolver.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Utilidad.vibraticionBotones( Contexto );
                WebService.logueado = false;
                WebService.ViajeActualSeleccionado = false;
                WebService.EstadoActual = WebService.EstadoAnterior;
                Intent NextActivity = new Intent(Contexto,Login.class);
                //PONER ESTE STRING
                //NextActivity.putExtra("Mensaje","SalirViaje");
                startActivity( NextActivity );
            }
        } );
        if(WebService.USUARIOLOGEADO!= null) {
            Texto = (TextView) findViewById( R.id.SVTVDatosCliente );
            OtroEnvio = (Button) findViewById( R.id.SVBTNOtroEnvio );
            Retornar = (Button) findViewById( R.id.SVBTNRetornar );
            ImgConsultas = findViewById( R.id.btnConsultas);
            ImgConsultas.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utilidad.vibraticionBotones( Contexto );
                    Intent NextActivity = new Intent(Contexto, Consultas.class);
                    startActivity( NextActivity );
                }
            } );
            final SwipeRefreshLayout swipeRefreshLayout= (SwipeRefreshLayout) findViewById( R.id.swipeRefresh );

            swipeRefreshLayout.setColorSchemeColors( Color.BLACK);
            swipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.setRefreshing( true );
                    params1 = new RequestParams();
                    params1.put( "nro_viaje", WebService.viajeSeleccionado.getNumViaje() );
                    params1.put( "seleccion", WebService.viajeSeleccionado.getTipo().equals( "retiro" ) ? "r":"e");
                    params1.put( "username", WebService.USUARIOLOGEADO );
                    final VerEntrega task = new VerEntrega( new AsyncResponse() {
                        public void processFinish(Object output) {
                            if (!WebService.errToken.equals("")) {
                                Intent myIntent = new Intent(Contexto, Login.class);
                                startActivity(myIntent);
                            } else {
                                if ((WebService.viajeSeleccionado.getTipo().equals("entrega"))) {
                                    if (!(WebService.entregasTraidas.size() - 1 > 0)) {
                                        OtroEnvio.setEnabled(false);
                                        OtroEnvio.setBackgroundColor(Contexto.getColor(R.color.entrega));
                                    } else {
                                        OtroEnvio.setEnabled(true);
                                        OtroEnvio.setBackgroundColor(Contexto.getColor(R.color.colorBoton));
                                    }
                                } else if (WebService.viajeSeleccionado.getTipo().equals("retiro")) {
                                    if (!(WebService.ListaRetiros.size() - 1 > 0)) {
                                        OtroEnvio.setEnabled(false);
                                        OtroEnvio.setBackgroundColor(Contexto.getColor(R.color.entrega));
                                    } else {
                                        OtroEnvio.setEnabled(true);
                                        OtroEnvio.setBackgroundColor(Contexto.getColor(R.color.colorBoton));
                                    }
                                }
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });
                    task.execute(  );
                }
            } );
            ImgVolver = findViewById( R.id.btnAtras );
            ImgVolver.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utilidad.vibraticionBotones( Contexto );
                    retornoSalirViaje();
                }
            } );
            OtroEnvio.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utilidad.vibraticionBotones( Contexto );
                    ActionOtroEnvio();
                }
            } );
            Retornar.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utilidad.vibraticionBotones( Contexto );
                    ActionRetornar();
                }
            } );
            if(Utilidad.isNetworkAvailable()){
                String Sucursal = WebService.Entrega_A_Realizar.getNom_Sucursal() == null ? "" : WebService.Entrega_A_Realizar.getNom_Sucursal();
                String TextoFinal = Contexto.getString(R.string.SVMessage) + "\n" + Sucursal;
                Texto.setText( TextoFinal);
                try {
                    if ((WebService.viajeSeleccionado.getTipo().equals( "entrega" ))) {
                        if (!(WebService.entregasTraidas.size() - 1 > 0)) {
                            OtroEnvio.setEnabled( false );
                            OtroEnvio.setBackgroundColor( Contexto.getColor( R.color.entrega ) );
                        }
                    } else if (WebService.viajeSeleccionado.getTipo().equals( "retiro" )) {
                        if (!(WebService.ListaRetiros.size() - 1 > 0)) {
                            OtroEnvio.setEnabled( false );
                            OtroEnvio.setBackgroundColor( Contexto.getColor( R.color.entrega ) );
                        }
                    }
                }catch(Exception e ){
                    Toast.makeText( Contexto, "Error: " + e.toString(), Toast.LENGTH_SHORT ).show();
                    Log.e("Error SalirViaje", e.toString());
                }
            }
        }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    private class  VerEntrega extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( Contexto );
        public AsyncResponse delegate = null;//Call back interface
        public VerEntrega(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            if(Utilidad.isNetworkAvailable()) {
                WebService.ListaEntregasViaje(params1,"Viajes/Entregas.php");
                return null;

            }
            else {
                Utilidad.dispalyAlertConexion(Contexto);
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
        protected void onProgressUpdate(Void... values) {}
    }

    private void ActionOtroEnvio(){
        if(Utilidad.isNetworkAvailable()) {
            try{
            if (WebService.usuarioActual.getEs_Cobranza().equals( "S" ) && WebService.usuarioActual.getEs_Entrega().equals( "N" ) && (WebService.usuarioActual.getTipoCobrador().equals("libre") || WebService.usuarioActual.getTipoCobrador().equals("fijo"))) {
                Intent NextActivity = new Intent(Contexto, IngresarObservaciones.class);
                NextActivity.putExtra("Volver",SalirViaje.class);
                startActivity(NextActivity);
            }else {
                WebService.ViajeActualSeleccionado = false;
                ArrayList<Entrega> Lista = new ArrayList<>(WebService.entregasTraidas);
                if (Lista.size() != WebService.EntregasAnteriores.size()) {
                    for (Entrega YaLeidos : WebService.EntregasAnteriores) {
                        Lista.remove(YaLeidos);
                    }
                } else {
                    WebService.EntregasAnteriores.clear();
                }
                ClienteXDefecto Instancia = new ClienteXDefecto();
                Instancia.SeleccionarEntrega(Lista);
                Intent NextActivity = new Intent(Contexto, ClienteXDefecto.class);
                startActivity(NextActivity);
            }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private void ActionRetornar(){
        try{
        WebService.EstadoActual = 3;
        if( WebService.viajeSeleccionado != null) {
            WebService.viajeSeleccionado.setEstado( "3" );
        }
        WebService.ViajeActualSeleccionado = false;
        WebService.LastIndexSelected = 0;
        WebService.IndexViajes = 0;
        final ObtenerCentral Tarea = new ObtenerCentral(new AsyncResponse(){
            public void processFinish(Object output) {
                Intent NextActivity = new Intent(Contexto,Recorrido_Viaje.class);
                startActivity( NextActivity );
            }
        });
        Tarea.execute();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private class ObtenerCentral extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( Contexto );
        public AsyncResponse delegate = null;//Call back interface

        public ObtenerCentral(AsyncResponse asyncResponse) {
            delegate = asyncResponse;
        }

        @Override
        protected Void doInBackground(String... params) {
            if(Utilidad.isNetworkAvailable()) {
                WebService.TraerEntregaMasCercana(URLDistanciaInicial1);
                return null;
            } else {
                Utilidad.dispalyAlertConexion(Contexto);
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
        protected void onProgressUpdate(Void... values) {}
    }

    private void retornoSalirViaje(){
        try{
        Intent NextActivity;
        if(WebService.ViajeActualSeleccionado == true){
            NextActivity = new Intent(this,Recorrido_Viaje.class);
        }else{
            NextActivity = new Intent(this,ClienteXDefecto.class);
        }
        startActivity( NextActivity );
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try{
        Utilidad.vibraticionBotones( Contexto );
        retornoSalirViaje();
        finish();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
