package com.example.user.trucksales.Visual.TruckSales;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.ClienteCobranza;
import com.example.user.trucksales.Encapsuladoras.Entrega;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Entregas extends Activity {

    protected static RequestParams params1= new RequestParams();
    ImageView atras;
    TextView nombreUsuario;
    TextView fecha;
    TextView numeroViaje, titulo;
    public static TableLayout tablaEntregas;
    private static Context context2;
    Utilidades Utilidad;
    ArrayList<Entrega> listaLeida;
    ArrayList<ClienteCobranza> listaLeidaCliente;
    private static ArrayList<Entrega> listaAgrupados = new ArrayList<>(  );

    RequestParams params2 = new RequestParams();
    public interface AsyncResponse {
        void processFinish(Object output);
    }

    private void CargarEntregas() {
        try {
            if (!WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getEs_Entrega().equals("S") || WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getEs_Entrega().equals("S")) {
                listaLeida = new ArrayList<>();
                for (int i = 0; WebService.entregasTraidas.size() > i; i++) {
                    if (!listaLeida.contains(WebService.entregasTraidas.get(i))) {

                        final Entrega entregaActual = WebService.entregasTraidas.get(i);
                        TableRow tr1 = new TableRow(context2);
                        final TextView TextoEntrega = new TextView(context2);
                        TextView p1 = new TextView(this);
                        final Integer indice = i;
                        String valorHora = getResources().getString(R.string.HorFecVenc) + " " + entregaActual.getHora_Desde().trim() + " a " + entregaActual.getHora_Hasta().trim();
                        p1.setText(valorHora);
                        Spannable spannable = new SpannableString(valorHora);
                        String Ordenes = entregaActual.getNro_Docum();
                        String Prioridad = entregaActual.getPrioridad();
                        boolean EntregaEncontradaP1 = false;
                        boolean EntregaEncontradaP2 = false;
                        boolean EntregaEncontradaP9 = false;
                        listaLeida.add(entregaActual);
                        for (Entrega objeto : WebService.entregasTraidas) {
                            if (objeto.getCod_Sucursal().equals(entregaActual.getCod_Sucursal()) && objeto.getCod_Tit().equals(entregaActual.getCod_Tit()) && objeto != entregaActual && !listaLeida.contains(objeto)) {
                                listaLeida.add(objeto);
                                Ordenes = Ordenes + ", " + objeto.getNro_Docum();
                                if (objeto.getPrioridad().equals("1")) {
                                    if (!EntregaEncontradaP1) {
                                        Prioridad = objeto.getPrioridad();
                                    }
                                    EntregaEncontradaP1 = true;
                                }
                                //SI LA PRIORIDAD ES 2 Y NO EXISTEN GUARDADOS CON ESA PRIORIDAD PERO TAMPOCO EXISTEN GUARDADOS CON PRIORIDAD 1 GUARDA
                                if (objeto.getPrioridad().equals("2") && (!EntregaEncontradaP1)) {
                                    if (!EntregaEncontradaP2) {
                                        Prioridad = objeto.getPrioridad();
                                    }
                                    EntregaEncontradaP2 = true;
                                }
                                //SI LA PRIORIDAD ES 9 Y NO EXISTEN GUARDADOS CON ESA PRIORIDAD PERO TAMPOCO DE PRIORIDAD 1 Y 2 ENTOCES GUARDA
                                if (objeto.getPrioridad().equals("9") && (!EntregaEncontradaP1) && (!EntregaEncontradaP2)) {
                                    if (!EntregaEncontradaP9) {
                                        Prioridad = objeto.getPrioridad();
                                    }
                                    EntregaEncontradaP9 = true;
                                }
                            }
                        }
                        if (Prioridad.trim().equals("1")) {
                            spannable.setSpan(new ForegroundColorSpan(Color.RED), 0, valorHora.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        if (Prioridad.trim().equals("2")) {
                            spannable.setSpan(new ForegroundColorSpan(Color.RED), 0, valorHora.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        if (Prioridad.trim().equals("9")) {
                            spannable.setSpan(new ForegroundColorSpan(Color.GREEN), valorHora.length(), (valorHora).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }

                        if (entregaActual.getObservaciones().equals("")) {
                            TextoEntrega.setText(spannable + "\n" + entregaActual.getNom_Tit() + "\n" + entregaActual.getNom_Sucursal() + "\n" + entregaActual.getDireccion() + "\n" + getResources().getString(R.string.NroOrdenCli) + " " + Ordenes);
                        } else {
                            TextoEntrega.setText(spannable + "\n" + entregaActual.getNom_Tit() + "\n" + entregaActual.getNom_Sucursal() + "\n" + entregaActual.getDireccion() + "\n" + getResources().getString(R.string.Observaciones) + entregaActual.getObservaciones() + "\n" + getResources().getString(R.string.NroOrdenCli) + " " + Ordenes);
                        }
                        TextoEntrega.setTextSize(18);
                        if (entregaActual.getCant_Facturas() != null) {
                            if (entregaActual.getCant_Facturas() > 0) {
                                TextoEntrega.setBackgroundResource(R.color.prioridad_uno);
                            } else {
                                TextoEntrega.setBackgroundResource(R.color.entrega);
                            }
                        } else {
                            TextoEntrega.setBackgroundResource(R.color.entrega);
                        }

                        //#f2a7b2   ROJO CLARO
                        //#94c9c0   VERDE
                        tr1.addView(TextoEntrega);
                        tr1.setPadding(0, 10, 0, 20);
                        tr1.setClickable(true);
                        tr1.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Utilidad.vibraticionBotones(context2);
                                Integer indiceInt = indice;
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
                                //Hasta aca el metodo guarda la hora de comienzo del viaje
                                //Aca armo con el string guardado la nueva entrega me boludie y me toco hacer esto
                                String[] partes = TextoEntrega.getText().toString().split("\n");
                                WebService.entregaDefault = entregaActual;
                                Intent myIntent = new Intent(v.getContext(), ClienteXDefecto.class);
                                startActivity(myIntent);
                            }
                        });
                        tablaEntregas.addView(tr1);
                    }
                }
            } else if (WebService.usuarioActual.getTipoCobrador().equals("D") && WebService.usuarioActual.getEs_Cobranza().equals("S")) {
                listaLeidaCliente = new ArrayList<>();
                for (int i = 0; WebService.clienteTraidos.size() > i; i++) {
                    if (!listaLeidaCliente.contains(WebService.clienteTraidos.get(i))) {

                        final ClienteCobranza entregaActualCobrador = WebService.clienteTraidos.get(i);
                        TableRow tr1 = new TableRow(context2);
                        final TextView TextoEntrega = new TextView(context2);
                        //TextView p1 = new TextView(this);
                        final Integer indice = i;
                        String valorHora = getResources().getString(R.string.HorFecVenc) + " " + entregaActualCobrador.getFecha_Vence().trim();
                        //p1.setText(valorHora);
                        Spannable spannable = new SpannableString(valorHora);
                        String Ordenes = entregaActualCobrador.getCod_Tit_Gestion();
                        String Prioridad = entregaActualCobrador.getPrioridad();
                        boolean EntregaEncontradaP1 = false;
                        boolean EntregaEncontradaP2 = false;
                        boolean EntregaEncontradaP9 = false;
                        listaLeidaCliente.add(entregaActualCobrador);
                        String empresas_extra = "";

                        for (ClienteCobranza objeto : WebService.clienteTraidos) {
                            if (objeto.getCod_Tit_Gestion().equals(entregaActualCobrador.getCod_Tit_Gestion()) && objeto != entregaActualCobrador && !listaLeidaCliente.contains(objeto)) {
                                listaLeidaCliente.add(objeto);
                                Ordenes = Ordenes + ", " + objeto.getNom_Tit();
                                if(!objeto.getCodEmp().trim().equals(entregaActualCobrador.getCodEmp().trim())){
                                    empresas_extra = empresas_extra + ", " + objeto.getNomEmp();
                                    entregaActualCobrador.setEstado(11);
                                    }
                                if (objeto.getPrioridad().equals(" ")) {
                                    if (!EntregaEncontradaP1) {
                                        Prioridad = objeto.getPrioridad();
                                    }
                                    EntregaEncontradaP1 = true;
                                }
                                //SI LA PRIORIDAD ES 2 Y NO EXISTEN GUARDADOS CON ESA PRIORIDAD PERO TAMPOCO EXISTEN GUARDADOS CON PRIORIDAD 1 GUARDA
                                if (objeto.getPrioridad().equals("2") && (!EntregaEncontradaP1)) {
                                    if (!EntregaEncontradaP2) {
                                        Prioridad = objeto.getPrioridad();
                                    }
                                    EntregaEncontradaP2 = true;
                                }
                                //SI LA PRIORIDAD ES 9 Y NO EXISTEN GUARDADOS CON ESA PRIORIDAD PERO TAMPOCO DE PRIORIDAD 1 Y 2 ENTOCES GUARDA
                                if (objeto.getPrioridad().equals("9") && (!EntregaEncontradaP1) && (!EntregaEncontradaP2)) {
                                    if (!EntregaEncontradaP9) {
                                        Prioridad = objeto.getPrioridad();
                                    }
                                    EntregaEncontradaP9 = true;
                                }
                            }
                        }

                        if (Prioridad.trim().equals("")) {
                            spannable.setSpan(new ForegroundColorSpan(Color.RED), 0, valorHora.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        if (Prioridad.trim().equals("2")) {
                            spannable.setSpan(new ForegroundColorSpan(Color.RED), 0, valorHora.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        if (Prioridad.trim().equals("9")) {
                            spannable.setSpan(new ForegroundColorSpan(Color.GREEN), valorHora.length(), (valorHora).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }

                        if(entregaActualCobrador.getEstado() == 11) {
                            TextoEntrega.setTextSize(15);
                            TextoEntrega.setText(getResources().getString(R.string.NomEmp) + " " +entregaActualCobrador.getNomEmp() + " " + empresas_extra + "\n" + getResources().getString(R.string.NomTit) + " " + entregaActualCobrador.getNom_Tit() + "\n" + getResources().getString(R.string.CodTit) + " " + entregaActualCobrador.getCod_Tit_Gestion() + "\n" + getResources().getString(R.string.Direccion) + " " + entregaActualCobrador.getDireccion()+ "\n" + getResources().getString(R.string.Tel) + " " + entregaActualCobrador.getTelLaboral() + "-" + entregaActualCobrador.getTelParticular());
                            TextoEntrega.setBackgroundResource(R.color.entrega);
                        }
                        else {
                            TextoEntrega.setTextSize(15);
                            TextoEntrega.setText(getResources().getString(R.string.NomEmp) + " " +entregaActualCobrador.getNomEmp() + "\n" + getResources().getString(R.string.NomTit) + " " + entregaActualCobrador.getNom_Tit() + "\n" + getResources().getString(R.string.CodTit) + " " + entregaActualCobrador.getCod_Tit_Gestion() + "\n" + getResources().getString(R.string.Direccion) + " " + entregaActualCobrador.getDireccion()+ "\n" + getResources().getString(R.string.Tel) + " " + entregaActualCobrador.getTelLaboral() + "-" + entregaActualCobrador.getTelParticular());
                            TextoEntrega.setBackgroundResource(R.color.entrega);
                        }

                        tr1.addView(TextoEntrega);
                        tr1.setPadding(0, 10, 0, 20);
                        tr1.setClickable(true);
                        tr1.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Utilidad.vibraticionBotones(context2);
                                Integer indiceInt = indice;
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
                                //Hasta aca el metodo guarda la hora de comienzo del viaje
                                //Aca armo con el string guardado la nueva entrega me boludie y me toco hacer esto
                                String[] partes = TextoEntrega.getText().toString().split("\n");
                                WebService.clienteActual = entregaActualCobrador;
                                Intent myIntent = new Intent(v.getContext(), ClienteXDefecto.class);
                                startActivity(myIntent);
                            }
                        });
                        tablaEntregas.addView(tr1);
                    }
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
           // Toast.makeText( getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_entregas);

        context2 = this;
        try{
        if (WebService.USUARIOLOGEADO != null) {
                atras = findViewById(R.id.btnAtras);
                WebService.EstadoActual = 0;
                Utilidad = new Utilidades(context2);
                atras.setClickable(true);
                atras.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Utilidad.vibraticionBotones(context2);
                        Intent myIntent = new Intent(v.getContext(), ClienteXDefecto.class);
                        startActivity(myIntent);
                    }
                });

                nombreUsuario = (TextView) findViewById(R.id.nombreUsuario);
                nombreUsuario.setText(getResources().getString(R.string.usuarioTool) + WebService.USUARIOLOGEADO);

                String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                fecha = (TextView) findViewById(R.id.Fecha);
                fecha.setText(timeStamp);
                numeroViaje = (TextView) findViewById(R.id.numeroViaje);
                titulo = findViewById(R.id.toolbar_titlePausa);

                if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D")) {
                    ClienteCobranza cliente = new ClienteCobranza();
                    for (int i = 0; i<WebService.clienteTraidos.size(); i++){
                        cliente = WebService.clienteTraidos.get(i);
                        break;
                    }
                   numeroViaje.setText(getResources().getString(R.string.NroViajeEntrega) + " " + WebService.viajeSeleccionadoCobrador.getNumViaje() + " " + getResources().getString(R.string.HorFec) + cliente.getFecha_Vence());
                    titulo.setText(getResources().getString(R.string.Cobranza).toUpperCase());
                } else {
                    numeroViaje.setText(getResources().getString(R.string.NroViajeEntrega) + " " + WebService.viajeSeleccionado.getNumViaje());
                }

                tablaEntregas = (TableLayout) findViewById(R.id.tabla);
                CargarEntregas();

                final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
                swipeRefreshLayout.setColorSchemeColors(Color.BLACK);

                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D")) {
                            params2.put("nro_viaje", WebService.viajeSeleccionadoCobrador.getNumViaje());
                            params2.put("username", WebService.USUARIOLOGEADO);
                            swipeRefreshLayout.setRefreshing(true);
                            TraerClientesViajes task2 = new TraerClientesViajes(new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {
                                    if (!WebService.errToken.equals("")) {
                                        Intent myIntent = new Intent(context2, Login.class);
                                        startActivity(myIntent);
                                    }else {
                                        tablaEntregas.removeAllViews();
                                        CargarEntregas();
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }
                            });
                            task2.execute();
                        } else if (WebService.usuarioActual.getEs_Entrega().equals("S") && !WebService.usuarioActual.getEs_Cobranza().equals("S") || WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getEs_Entrega().equals("S")) {
                            swipeRefreshLayout.setRefreshing(true);
                            TraerEntregas task = new TraerEntregas(new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {
                                    if (!WebService.errToken.equals("")) {
                                        Intent myIntent = new Intent(context2, Login.class);
                                        startActivity(myIntent);
                                    } else {
                                        tablaEntregas.removeAllViews();
                                        CargarEntregas();
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }
                            });
                            task.execute();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                });
        }else {
            Intent myIntent = new Intent(context2, Login.class);
            startActivity(myIntent);
        }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private class TraerEntregas extends AsyncTask<String,Void,Void>{
        ProgressDialog dialog = new ProgressDialog(context2);
        public AsyncResponse delegate = null;

        public TraerEntregas(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {
            params1 = new RequestParams(  );
           params1.put("nro_viaje", Integer.valueOf(WebService.viajeSeleccionado.getNumViaje()));
            //params1.put( "seleccion", WebService.viajeSeleccionado.getTipo().equals( "entrega" ));
            params1.put("username", WebService.usuarioActual.getNombre());

            WebService.ListaEntregasViaje(params1,"Viajes/Entregas.php");
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
        try{
        Utilidad.vibraticionBotones( context2 );
        Intent myIntent = new Intent(context2, ClienteXDefecto.class);
        startActivity(myIntent);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private class  TraerClientesViajes extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context2 );
        public AsyncResponse delegate = null;//Call back interface
        public TraerClientesViajes(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            if(Utilidad.isNetworkAvailable()) {
                WebService.TraerClientesViajes( params2, "Viajes/Viajes Cobrador/TraerClientesViajes.php");
                return null;
            }

            return null;
        }
        @Override
        public void onPreExecute() {
            Utilidad.showLoadingMessage();
        }
        @Override
        protected void onPostExecute(Void result) {
            Utilidad.deleteLoadingMessage();
        }


        @Override
        protected void onProgressUpdate(Void... values)
        {

        }
    }
}
