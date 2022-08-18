package com.example.user.trucksales.Visual.Cobranza;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Encapsuladoras.Moneda;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.Encapsuladoras.ClienteCobranza;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Visual.SeleccionFuncionablidad;
import com.example.user.trucksales.Visual.TruckSales.ClienteXDefecto;
import com.example.user.trucksales.Visual.TruckSales.Recorrido_Viaje;
import com.example.user.trucksales.Visual.TruckSales.factura;
import com.loopj.android.http.RequestParams;

import java.nio.file.WatchEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;

public class SeleccionarDeudas extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try{
        Utilidad.vibraticionBotones(context);
        val_tot = 0D;

        totalDescuento = 0D;
        totalRetenSelec = 0D;

        WebService.totalDeudas = 0D;
        WebService.setTotalDescuento(0D);
        WebService.setTotalReten(0D);

        if (WebService.usuarioActual.getEs_Entrega().equals("S") && WebService.usuarioActual.getEs_Cobranza().equals("S") || WebService.usuarioActual.getEs_Entrega().equals("S") && !WebService.usuarioActual.getEs_Cobranza().equals("S")) {
            Intent myIntent = new Intent(context, SeleccionCliente.class);
            startActivity(myIntent);
        } else {
            if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("L") || WebService.usuarioActual.getTipoCobrador().equals("F")) {
                if (WebService.clienteActual.getLatiud_Ubic().equals(0.0)) {
                    Intent myIntent = new Intent(context, SeleccionCliente.class);
                    startActivity(myIntent);
                    finish();
                } else {
                    Intent myIntent = new Intent(context, Recorrido_Viaje.class);
                    myIntent.putExtra("intent", "Cobranza");
                    startActivity(myIntent);
                    finish();
                }
            } else if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D")) {
                if (WebService.clienteActual.getLatiud_Ubic().equals(0.0)) {
                    Intent myIntent = new Intent(context, ClienteXDefecto.class);
                    startActivity(myIntent);
                    finish();
                } else {
                    Intent myIntent = new Intent(context, Recorrido_Viaje.class);
                    myIntent.putExtra("intent", "Cobranza");
                    startActivity(myIntent);
                    finish();
                }
            }
        }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private Utilidades Utilidad;
    public static Context context;
    TextView nombreUsu,fecha,total,ClienteSeleccionado, tipoCambio, NomEmp;
    private static Double val_tot=0D, totalDescuento=0D, /*totalInteres=0D,*/ totalRetenSelec=0D;
    ImageView atras,casita, nocobrado;
    private static EditText input;
    private static TextView inputTV, TxtFecha;
    private static TableLayout tablaDeudas;
    private static int cont_filas = 0;

    private ArrayList<ClienteCobranza> listaLeidaCliente;

    private static String deudaSiguiente;

    private Button btnValores, BtnSiguiente;

    private static int posicion = 0;

    static int num = 0;

    Spinner spMonedas;
    List<String> spinnerMonedaArray = new ArrayList<>(  );

    RequestParams params = new RequestParams();

    private Date fechaSeleccionada;
    public final Calendar c = Calendar.getInstance();
    private static final String CERO = "0";
    private static final String GUION = "-";
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    @Override
    protected void onResume() {
        super.onResume();

        totalRetenSelec = 0D;
    }
    private String CargarTexto(ClienteCobranza instaCliente) {
            String fecha =  instaCliente.getFecha_Vence().substring( 0,10 );
            String[] formatear = fecha.split( "-" );
            int valorParcial = 0;
            fecha = formatear[2]+"-"+formatear[1]+"-"+formatear[0];
            String tx1 = "<font color='black'><strong>"+instaCliente.getCod_dpto() + instaCliente.getSerie_docum() + instaCliente.getNro_Docum()+"</strong></font>";
            String texto;
            if(instaCliente.getDescuento() > 0.0) {
                double desc = instaCliente.getDescuentoAgregado();
                double imp = instaCliente.getTotalEntregado();
                double impdesc = imp - desc;
                if(instaCliente.getCod_Moneda().trim().equals("1")) {
                    texto = instaCliente.getCod_Docum() + " - " + tx1 + "<br>" + getResources().getString(R.string.Vencimiento) + " " + Html.fromHtml(fecha) + "<br>" + "Saldo: " + "<font color='blue'> " + instaCliente.getSimMoneda() + " " + instaCliente.getImp_mov_mo() + "</font><br>" + "Descuento: " + "<font color='blue'> " + instaCliente.getSimMoneda() + " " + NumberFormat.getInstance(Locale.ITALY).format(instaCliente.getDescuento()) + "</font><br>" + getResources().getString(R.string.Importe) + "<font color='blue'> " + instaCliente.getSimMoneda() + " " + NumberFormat.getInstance(Locale.ITALY).format(impdesc) + "</font>";
                }else{
                    texto = instaCliente.getCod_Docum() + " - " + tx1 + "<br>" + getResources().getString(R.string.Vencimiento) + " " + Html.fromHtml(fecha) + "<br>" + "Saldo: " + "<font color='blue'> " + instaCliente.getSimMoneda() + " " + instaCliente.getImp_mov_mo() + "</font><br>" + "Descuento: " + "<font color='blue'> " + instaCliente.getSimMoneda() + " " + instaCliente.getDescuento() + "</font><br>" + getResources().getString(R.string.Importe) + "<font color='blue'> " + instaCliente.getSimMoneda() + " " + impdesc + "</font>";
                }
            }else{
                if(instaCliente.getCod_Moneda().trim().equals("1")) {
                    texto = instaCliente.getCod_Docum() + " - " + tx1 + "<br>" + getResources().getString(R.string.Vencimiento) + " " + Html.fromHtml(fecha) + "<br>" + "Saldo: " + "<font color='blue'> " + instaCliente.getSimMoneda() + " " + instaCliente.getImp_mov_mo() + "</font><br>" + getResources().getString(R.string.Importe) + "<font color='blue'> " + instaCliente.getSimMoneda() + " " + NumberFormat.getInstance(Locale.ITALY).format(instaCliente.getTotalEntregado()) + "</font>";
                }else{
                    texto = instaCliente.getCod_Docum() + " - " + tx1 + "<br>" + getResources().getString(R.string.Vencimiento) + " " + Html.fromHtml(fecha) + "<br>" + "Saldo: " + "<font color='blue'> " + instaCliente.getSimMoneda() + " " + instaCliente.getImp_mov_mo() + "</font><br>" + getResources().getString(R.string.Importe) + "<font color='blue'> " + instaCliente.getSimMoneda() + " " + instaCliente.getTotalEntregado() + "</font>";
                }

            }
            texto = texto.replaceAll(fecha,"<font color='black'><strong>"+fecha+"</strong></font>");
            if(instaCliente.getCod_Moneda().trim().equals("1")){
                texto = texto.replaceAll(String.valueOf(  instaCliente.getImp_mov_mo()),"<font color='blue'>"+NumberFormat.getInstance( Locale.ITALY ).format(instaCliente.getImp_mov_mo())+"</font>" );
            }else{
                texto = texto.replaceAll(String.valueOf(  instaCliente.getImp_mov_mo()),"<font color='blue'>"+instaCliente.getImp_mov_mo()+"</font>" );
            }

             return texto;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  this.requestWindowFeature( Window.FEATURE_NO_TITLE );

        setContentView(R.layout.activity_seleccionar_deudas);

        WebService.deudasSeleccionadas.clear();
        WebService.deudasPagar.clear();
        WebService.deudasSeleccionadasReten.clear();
        WebService.deudasPagarRetenc.clear();

        totalDescuento = 0D;
        totalRetenSelec = 0D;

        WebService.totalDescuento = 0D;
        WebService.totalRetencion = 0D;
        WebService.setTotalReten(0D);
        WebService.setTotalDescuento(0D);

        context = this;
        Utilidad = new Utilidades(context);

        GuardarDatosUsuario.Contexto = context;
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        try {
            if (WebService.usuarioActual != null) {
                try {

                    if (Utilidad.isNetworkAvailable()) {

                        btnValores = findViewById(R.id.btnValores);
                        BtnSiguiente = findViewById(R.id.BtnSiguiente);
                        if (WebService.usuarioActual.getTipoCobrador().equals("D")) {
                            ClienteCobranza entregaActualCobrador = new ClienteCobranza();
                            try {
                                entregaActualCobrador = WebService.clienteActual;
                                deudaSiguiente = WebService.clienteActual.getCodEmp().trim();
                                listaLeidaCliente = new ArrayList<>();
                                ArrayList<ClienteCobranza> Lista = new ArrayList<>();
                                for (int i = 0; i < WebService.clienteTraidos.size(); i++) {
                                    Lista.add(WebService.clienteTraidos.get(i));
                                }
                                for (ClienteCobranza objeto : Lista) {
                                    if (objeto.getCod_Tit_Gestion().equals(entregaActualCobrador.getCod_Tit_Gestion()) && !listaLeidaCliente.contains(objeto)) {
                                        listaLeidaCliente.add(objeto);
                                    }
                                }
                                if (listaLeidaCliente.size() == 1) {
                                    BtnSiguiente.setVisibility(View.GONE);
                                } else {
                                    BtnSiguiente.setVisibility(View.VISIBLE);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                //Toast.makeText( getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG ).show();
                            }
                        } else {
                            BtnSiguiente.setVisibility(View.GONE);
                        }

                        NomEmp = findViewById(R.id.NomEmp);
                        if (WebService.usuarioActual.getTipoCobrador().equals("D")) {
                            NomEmp.setText(WebService.clienteActual.getNomEmp());
                            NomEmp.setTypeface(null, Typeface.BOLD);
                        } else {
                            NomEmp.setVisibility(View.GONE);
                        }

                        WebService.totalDeudas = 0D;
                        val_tot = 0D;

                        tipoCambio = findViewById(R.id.tipoC);

                        String tipoCamb = String.valueOf(WebService.tipoCambio);
                        if(tipoCamb.contains(".0")){
                            double tipo = Utilidad.redondearDecimales(WebService.tipoCambio, 0);
                            tipoCambio.setText(getResources().getString(R.string.tipocambio) + NumberFormat.getInstance(Locale.ITALY).format(tipo).toString());
                        }else{
                            tipoCambio.setText(getResources().getString(R.string.tipocambio) + Utilidad.redondearDecimales(WebService.tipoCambio, 2));
                        }

                       /* double tipo = Utilidad.redondearDecimales(WebService.tipoCambio, 0);
                        tipoCambio.setText(getResources().getString(R.string.tipocambio) + NumberFormat.getInstance(Locale.ITALY).format(tipo).toString());*/

                        total = findViewById(R.id.total);

                       double valdesc = val_tot - totalDescuento;
                        total.setText(getResources().getString(R.string.TotalDeuda) + String.valueOf(NumberFormat.getInstance(Locale.ITALY).format(valdesc)));

                        WebService.setTotalDeudas(0D);
                        WebService.setTotalReten(0D);
                        //WebService.setTotalInteres(0D);

                        atras = findViewById(R.id.btnAtras);
                        atras.setClickable(true);

                        String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                        nombreUsu = (TextView) findViewById(R.id.LblUsu);
                        fecha = (TextView) findViewById(R.id.LblFecha);
                        fecha.setText(timeStamp);

                        WebService.fecha = fecha.getText().toString();

                        TxtFecha = findViewById(R.id.TxtFecha);
                        TxtFecha.setText(timeStamp);

                        TxtFecha.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                obtenerFecha();
                            }
                        });

                        atras.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                try {
                                    Utilidad.vibraticionBotones(context);
                                    val_tot = 0D;

                                    totalDescuento = 0D;
                                    totalRetenSelec = 0D;

                                    WebService.totalDeudas = 0D;
                                    WebService.setTotalDescuento(0D);
                                    WebService.setTotalReten(0D);

                                    if (WebService.usuarioActual.getEs_Entrega().equals("S") && WebService.usuarioActual.getEs_Cobranza().equals("S") || WebService.usuarioActual.getEs_Entrega().equals("S") && !WebService.usuarioActual.getEs_Cobranza().equals("S")) {
                                        Intent myIntent = new Intent(v.getContext(), SeleccionCliente.class);
                                        startActivity(myIntent);
                                    } else {
                                        if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("L") || WebService.usuarioActual.getTipoCobrador().equals("F")) {
                                            if (WebService.clienteActual.getLatiud_Ubic().equals(0.0)) {
                                                Intent myIntent = new Intent(v.getContext(), SeleccionCliente.class);
                                                startActivity(myIntent);
                                                finish();
                                            } else {
                                                Intent myIntent = new Intent(v.getContext(), Recorrido_Viaje.class);
                                                myIntent.putExtra("intent", "Cobranza");
                                                startActivity(myIntent);
                                                finish();
                                            }
                                        } else if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D")) {
                                            if (WebService.clienteActual.getLatiud_Ubic().equals(0.0)) {
                                                Intent myIntent = new Intent(v.getContext(), ClienteXDefecto.class);
                                                startActivity(myIntent);
                                                finish();
                                            } else {
                                                Intent myIntent = new Intent(v.getContext(), Recorrido_Viaje.class);
                                                myIntent.putExtra("intent", "Cobranza");
                                                startActivity(myIntent);
                                                finish();
                                            }
                                        }
                                    }
                                }catch (Exception ex){
                                    ex.printStackTrace();
                                }
                            }
                        });

                        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

                        swipeRefreshLayout.setColorSchemeColors(Color.BLACK);
                        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                swipeRefreshLayout.setRefreshing(true);
                                if(WebService.usuarioActual.getTipoCobrador().equals("D")){
                                    TraerDeudasViaje task1 = new TraerDeudasViaje(new AsyncResponse() {
                                        @Override
                                        public void processFinish(Object output) {
                                            if (!WebService.errToken.equals("")) {
                                                Intent myIntent = new Intent(context, Login.class);
                                                startActivity(myIntent);
                                            } else {
                                                val_tot = 0D;
                                                totalDescuento = 0D;
                                                totalRetenSelec = 0D;
                                                WebService.totalDeudas = 0D;
                                                WebService.setTotalDescuento(0D);
                                                WebService.setTotalDeudas(0D);
                                                WebService.setTotalReten(0D);
                                                WebService.deudasSeleccionadas.clear();
                                                WebService.deudasPagar.clear();
                                                tablaDeudas.removeAllViews();
                                                CargarDatos();
                                                swipeRefreshLayout.setRefreshing(false);
                                            }
                                        }
                                    });
                                    task1.execute();
                                }else{
                                    TraerDeudores task = new TraerDeudores(new AsyncResponse() {
                                        @Override
                                        public void processFinish(Object output) {
                                            if (!WebService.errToken.equals("")) {
                                                Intent myIntent = new Intent(context, Login.class);
                                                startActivity(myIntent);
                                            } else {
                                                val_tot = 0D;
                                                totalDescuento = 0D;
                                                totalRetenSelec = 0D;
                                                WebService.totalDeudas = 0D;
                                                WebService.setTotalDescuento(0D);
                                                WebService.setTotalDeudas(0D);
                                                WebService.setTotalReten(0D);
                                                WebService.deudasSeleccionadas.clear();
                                                WebService.deudasPagar.clear();
                                                tablaDeudas.removeAllViews();
                                                CargarDatos();
                                                swipeRefreshLayout.setRefreshing(false);
                                            }
                                        }
                                    });
                                    task.execute();
                                }

                            }
                        });

                        casita = findViewById(R.id.casita);
                        casita.setClickable(true);
                        casita.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                try {
                                    Utilidad.vibraticionBotones(context);
                                    val_tot = 0D;
                                    WebService.totalDeudas = 0D;
                                    if (WebService.usuarioActual.getEs_Entrega().equals("S") && WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("L") || WebService.usuarioActual.getTipoCobrador().equals("F")) {
                                        Intent myIntent = new Intent(v.getContext(), SeleccionFuncionablidad.class);
                                        startActivity(myIntent);
                                        finish();
                                    } else if (!WebService.usuarioActual.getEs_Entrega().equals("S") && WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("L") || WebService.usuarioActual.getTipoCobrador().equals("F")) {
                                        Intent myIntent = new Intent(v.getContext(), SeleccionCliente.class);
                                        startActivity(myIntent);
                                        finish();
                                    } else if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D")) {
                                        Intent myIntent = new Intent(v.getContext(), MenuCobranzas.class);
                                        startActivity(myIntent);
                                        finish();
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                        nocobrado = findViewById(R.id.nocobrado);
                        nocobrado.setClickable(true);
                        nocobrado.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                try {
                                    Utilidad.vibraticionBotones(context);
                                    Intent myIntent = new Intent(v.getContext(), IngresarObservaciones.class);
                                    myIntent.putExtra("intent", "SeleccionarDeudas");
                                    startActivity(myIntent);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                   // Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                                }
                            }
                        });
/*
            nocobrado.setVisibility(View.VISIBLE);
            nocobrado.setEnabled(false);*/

                        spMonedas = findViewById(R.id.SPMonedas);
                        spMonedas.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                        final ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, spinnerMonedaArray);

                        ClienteSeleccionado = findViewById(R.id.ClienteSeleccionado);
                        if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("L") || WebService.usuarioActual.getTipoCobrador().equals("F")) {
                            ClienteSeleccionado.setText(WebService.clienteActual.getNom_Tit().trim() + "  " + getResources().getString(R.string.DeudasTraidas) + " " + WebService.deudasViaje.size() + " " + getResources().getString(R.string.DeudasP));
                        } else if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D")) {
                            ClienteSeleccionado.setText(WebService.clienteActual.getNom_Tit().trim() + "  " + getResources().getString(R.string.DeudasTraidas) + " " + WebService.deudasViaje.size() + " " + getResources().getString(R.string.DeudasP));
                        }

                        nombreUsu.setText(WebService.USUARIOLOGEADO);
                        tablaDeudas = (TableLayout) findViewById(R.id.tabla);

                /*dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spMonedas.setAdapter(dataAdapter2);*/

                if(WebService.tipoCambio.equals(0.0)){
                    displayAlertTC();
                }else {
                    try {
                        if (WebService.monedas.size() == 0) {
                            TraerMonedas task = new TraerMonedas(new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {
                                    if (!WebService.errToken.equals("")) {
                                        Intent myIntent = new Intent(context, Login.class);
                                        startActivity(myIntent);
                                    } else {
                                        CargaMoneda();
                                        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spMonedas.setAdapter(dataAdapter2);
                                        SeleccionMoneda();
                                        CargarDatos();
                                    }
                                }
                            });
                            task.execute();
                        } else {
                            CargaMoneda();
                            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spMonedas.setAdapter(dataAdapter2);
                            SeleccionMoneda();
                            CargarDatos();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                       // Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                    }
                }

                    spMonedas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            SeleccionMoneda();
                            if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("2")){
                                val_tot = Utilidad.redondearDecimales(val_tot / WebService.tipoCambio, 2);
                                totalDescuento = totalDescuento / WebService.tipoCambio;
                                double valdesc = val_tot - totalDescuento;
                                valdesc = Utilidad.redondearDecimales(valdesc, 2);
                                total.setText(getResources().getString(R.string.TotalDeuda) + " " + (valdesc));
                            }else {
                                val_tot = Utilidad.redondearDecimales(val_tot * WebService.tipoCambio, 0);
                                totalDescuento = totalDescuento * WebService.tipoCambio;
                                double valdesc = val_tot - totalDescuento;
                                valdesc = Utilidad.redondearDecimales(valdesc, 0);
                                total.setText(getResources().getString(R.string.TotalDeuda) + " " + NumberFormat.getInstance(Locale.ITALY).format(valdesc));
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    btnValores.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(context);
                            SeleccionMoneda();
                            if (WebService.totalDeudas <= 0) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.deudaselec), Toast.LENGTH_LONG).show();
                            } else {
                                WebService.limpiarValores();

                                WebService.fecha = String.valueOf(TxtFecha.getText().toString());
                                traerParempTask task2 = new traerParempTask(new AsyncResponse() {
                                    @Override
                                    public void processFinish(Object output) {
                                        if (!WebService.errToken.equals("")) {
                                            Intent myIntent = new Intent(context, Login.class);
                                            startActivity(myIntent);
                                        } else {
                                            WebService.EstadoActual = 4;
                                            params = new RequestParams();

                                            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                            Criteria criteria = new Criteria();
                                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                                return;
                                            }
                                            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                                            double latitude = 0;
                                            double longitude = 0;
                                            if (location != null) {
                                                latitude = location.getLatitude();
                                                longitude = location.getLongitude();
                                            }

                                            WebService.lat_actual = latitude;
                                            WebService.long_actual = longitude;

                                            if (WebService.usuarioActual.getTipoCobrador().trim().equals("D")) {
                                                params.put("usuario", WebService.USUARIOLOGEADO);//1
                                                params.put("latitud_actual", String.valueOf(WebService.lat_actual));
                                                params.put("longitud_actual", String.valueOf(WebService.long_actual));
                                                params.put("latitud_destino", String.valueOf(WebService.clienteActual.getLatiud_Ubic()));
                                                params.put("longitud_destino", String.valueOf(WebService.clienteActual.getLongitud_Ubic()));
                                                params.put("latitud_origen", String.valueOf(WebService.lat_origen));
                                                params.put("longitud_origen", String.valueOf(WebService.long_origen));
                                                params.put("en_pausa", String.valueOf(WebService.EstadoActual));//12
                                                params.put("nro_viaje", WebService.viajeSeleccionadoCobrador.getNumViaje().trim());//9
                                                params.put("cod_sucursal", "0");//10
                                                params.put("nro_orden", "0");//13
                                                params.put("nom_cliente", WebService.clienteActual.getNom_Tit().trim());//11
                                                params.put("distancia_a_recorrer", WebService.distancia_a_recorrer);
                                            } else {
                                                params.put("usuario", WebService.USUARIOLOGEADO);//1
                                                params.put("latitud_actual", String.valueOf(WebService.lat_actual));
                                                params.put("longitud_actual", String.valueOf(WebService.long_actual));
                                                params.put("latitud_destino", String.valueOf(WebService.clienteActual.getLatiud_Ubic()));
                                                params.put("longitud_destino", String.valueOf(WebService.clienteActual.getLongitud_Ubic()));
                                                params.put("latitud_origen", String.valueOf(WebService.lat_origen));
                                                params.put("longitud_origen", String.valueOf(WebService.long_origen));
                                                params.put("en_pausa", String.valueOf(WebService.EstadoActual));//12
                                                params.put("nro_viaje", "0");//9
                                                params.put("cod_sucursal", "0");//10
                                                params.put("nro_orden", "0");//13
                                                params.put("nom_cliente", WebService.clienteActual.getNom_Tit().trim());//11
                                                params.put("distancia_a_recorrer", WebService.distancia_a_recorrer);
                                            }
                                            ActualizarUbicacion actuUbic = new ActualizarUbicacion();
                                            actuUbic.execute();

                                            WebService.setTotalDeudas(0.0);
                                            WebService.setTotalDescuento(0.0);

                                            val_tot = 0D;
                                            totalDescuento = 0D;

                                            for (int i = 0; i < WebService.deudasSeleccionadas.size(); i++) {
                                                ClienteCobranza cli = WebService.deudasSeleccionadas.get(i);
                                                Double val = cli.getTotalEntregado();
                                                if (!(WebService.monedaSeleccionada.getCod_Moneda().trim().equals(cli.getCod_Moneda().trim()))) {
                                                    if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                                                        val_tot = val_tot + (val * WebService.tipoCambio);
                                                        totalDescuento = totalDescuento + (cli.getDescuentoAgregado() * WebService.tipoCambio);
                                                    } else {
                                                        val_tot = val_tot + (val / WebService.tipoCambio);
                                                        totalDescuento = totalDescuento + (cli.getDescuentoAgregado() / WebService.tipoCambio);
                                                    }
                                                } else {
                                                    val_tot = val_tot + val;
                                                    totalDescuento = totalDescuento + cli.getDescuentoAgregado();
                                                }
                                            }

                                            double valdesc = val_tot - totalDescuento;
                                            WebService.totalDeudas = 0D;
                                            WebService.totalDescuento = 0D;
                                            WebService.setTotalDeudas(0D);
                                            WebService.setTotalDescuento(0D);

                                            WebService.setTotalDeudas(valdesc);
                                            WebService.setTotalDescuento(totalDescuento);

                                            Intent myIntent = new Intent(context, IngresarValores.class);
                                            myIntent.putExtra("intent", "Deuda");
                                            startActivity(myIntent);
                                        }
                                    }
                                });
                                task2.execute();
                            }
                        }
                    });

                    BtnSiguiente.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(context);
                            try {
                                // if (Utilidad.isNetworkAvailable()) {
                                WebService.deudasSeleccionadas.clear();
                                WebService.deudasPagar.clear();

                                posicion = posicion + 1;
                                if (listaLeidaCliente.size() > 1) {
                                    if (listaLeidaCliente.size() == posicion) {
                                        posicion = 0;
                                    }
                                    deudaSiguiente = listaLeidaCliente.get(posicion).getCodEmp();
                                    WebService.clienteActual = listaLeidaCliente.get(posicion);
                                }
                                TraerDeudasViaje task2 = new TraerDeudasViaje(new AsyncResponse() {
                                    @Override
                                    public void processFinish(Object output) {
                                        if (!WebService.errToken.equals("")) {
                                            Intent myIntent = new Intent(context, Login.class);
                                            startActivity(myIntent);
                                        }else {
                                            tablaDeudas.removeAllViews();
                                            val_tot = 0D;
                                            NomEmp.setText(WebService.clienteActual.getNomEmp());
                                            CargarDatos();
                                        }
                                    }
                                });
                                task2.execute();
                        /*} else {
                            Utilidad.CargarToastConexion(context);
                        }*/
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                ///Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                            }
                        }
                    });
                    }else{
                        Utilidad.CargarToastConexion(context);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                    //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                }
            }else {
                Intent myIntent = new Intent(context, Login.class);
                startActivity(myIntent);
            }
        }  catch (Exception ex) {
            ex.printStackTrace();
            //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    protected void displayAlertCantidad(final ClienteCobranza miItm) {
        AlertDialog.Builder builder = new AlertDialog.Builder( context );
        input = new EditText( context );
       // input.setInputType( InputType.TYPE_CLASS_NUMBER );
        input.setGravity(Gravity.RIGHT);
        builder.setView( input );
        input.setText("0");
        input.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    input.setText(formattedString);
                    input.setSelection(input.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    //Toast.makeText( getApplicationContext(), nfe.getMessage(), Toast.LENGTH_LONG ).show();
                }

                input.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /*18/6/19 Se quita valor del text y se agrega al texto BDL*/
        builder.setMessage( getResources().getString( R.string.alertIngresarImporte )+ miItm.getCod_dpto().trim() + " " + miItm.getSerie_docum().trim() + " " + miItm.getNro_Docum().trim().trim() +"\n"+ getResources().getString(R.string.deudatotal) + miItm.getSimMoneda().trim() +" "+ NumberFormat.getInstance( Locale.ITALY ).format(miItm.getImp_mov_mo()) ).setCancelable(
                false ).setPositiveButton( getResources().getString( R.string.aceptar ),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(miItm.getCod_Moneda().trim().equals("1")){
                            input.setInputType( InputType.TYPE_CLASS_NUMBER );
                        }else{
                            input.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        }
                        String num =  input.getText().toString();
                        if (!num.trim().equals("0")) {
                            try {
                                if (Integer.valueOf(input.getText().toString().replace(",", "")) > miItm.getImp_mov_mo()) {
                                    miItm.setEstado(0);
                                    Toast toast = Toast.makeText(context, getResources().getString(R.string.ValorMen), Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                                    toast.setDuration(Toast.LENGTH_SHORT);//you can even use milliseconds to display toast
                                    toast.show();//showing the toast is important***/
                                } else {
                                    try {
                                        //  WebService.,.( miItm.getNumerador() );
                                        if (Integer.valueOf(input.getText().toString().replace(",", "")) < miItm.getImp_mov_mo()) {
                                            miItm.setTotalEntregado(Integer.valueOf(input.getText().toString().trim().replace(",", "")));
                                            Double valor = miItm.getTotalEntregado();
                                            WebService.addDeuda(miItm, Integer.valueOf(input.getText().toString().trim().replace(",", "")));
                                            int val = 0;
                                    /*if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("F") || WebService.usuarioActual.getTipoCobrador().equals("L")) {
                                        for (int i = 0; WebService.deudasViaje.size() - 1 >= i; i++) {
                                            val++;
                                            ClienteCobranza it = WebService.deudasViaje.get(i);
                                            if (it.getCod_Tit() == miItm.getCod_Tit()) {

                                            } else {
                                                //listaItms.get( i ).setNumerador( val );
                                            }
                                        }
                                    }
                                    if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D")) {*/
                                            for (int i = 0; i < WebService.deudasViaje.size(); i++) {

                                                ClienteCobranza instaDeuda = WebService.deudasViaje.get(i);
                                                if (instaDeuda.getEstado() == 3) {
                                                    instaDeuda.setTotalEntregado(valor);
                                                    break;
                                                }
                                            }
                                            if (!(WebService.monedaSeleccionada.getCod_Moneda().trim().equals(miItm.getCod_Moneda().trim()))) {
                                                if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                                                    val_tot = val_tot + (valor * WebService.tipoCambio);
                                                } else {
                                                    val_tot = val_tot + (valor / WebService.tipoCambio);
                                                }
                                            } else {
                                                val_tot = val_tot + valor;
                                            }
                                            miItm.setEstado(3);
                                            //val_tot = val_tot + valor;
                                            double valdesc = val_tot - totalDescuento;
                                            if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")){
                                                valdesc = Utilidad.redondearDecimales(valdesc, 0);
                                            }else {
                                                valdesc = Utilidad.redondearDecimales(valdesc, 2);
                                            }
                                            total.setText(getResources().getString(R.string.TotalDeuda) + " " + NumberFormat.getInstance(Locale.ITALY).format(valdesc));
                                            WebService.setTotalDeudas(val_tot);
                                            //}
                                        } else {
                                            if (Integer.valueOf(input.getText().toString().replace(",", "")) == miItm.getImp_mov_mo()) {
                                                miItm.setEstado(1);
                                                miItm.setTotalEntregado(miItm.getImp_mov_mo());
                                                WebService.addDeuda(miItm, miItm.getImp_mov_mo());
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.toString();
                                       // Toast.makeText( getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG ).show();
                                    }
                                }
                                RefrescarTabla();
                            } catch (Exception e) {
                                e.toString();
                                //Toast.makeText( getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG ).show();
                            }
                        }else{
                            miItm.setEstado(0);
                            RefrescarTabla();
                        }
                    }
                } ).setNegativeButton( "Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        miItm.setEstado( 0 );
                        RefrescarTabla();
                        dialog.cancel();
                    }
                } );
        AlertDialog alerta = builder.create();

        try {
            alerta.getWindow().setType(TYPE_APPLICATION_PANEL );
            alerta.show();
        } catch (Exception errrssd) {
            errrssd.toString();
           // Toast.makeText( getApplicationContext(), errrssd.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }

    protected void displayAlertTC() {
        AlertDialog.Builder builder = new AlertDialog.Builder( context );

        inputTV = new TextView( context );
        inputTV.setGravity(Gravity.CENTER);
        builder.setView( inputTV );

        builder.setMessage(getResources().getString(R.string.reto_Fac_5)).setCancelable(
                false).setPositiveButton(getResources().getString(R.string.aceptar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            dialog.cancel();
                                Intent myIntent = new Intent(context, Login.class);
                                startActivity(myIntent);
                        } catch (Exception exc) {
                        }
                    }
                });
        AlertDialog alerta1 = builder.create();
        alerta1.show();
    }

    protected void displayAlertCantDeuda() {
        AlertDialog.Builder builder = new AlertDialog.Builder( context );

        inputTV = new TextView( context );
        inputTV.setGravity(Gravity.CENTER);
        builder.setView( inputTV );

        builder.setMessage(getResources().getString(R.string.deuda)).setCancelable(
                false).setPositiveButton(getResources().getString(R.string.aceptar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            dialog.cancel();
                        } catch (Exception exc) {
                        }
                    }
                });
        AlertDialog alerta1 = builder.create();
        alerta1.show();
    }

    protected void displayAlertDesc(final ClienteCobranza miItm) {
        AlertDialog.Builder builder = new AlertDialog.Builder( context );
        input = new EditText( context );
        input.setInputType( InputType.TYPE_CLASS_NUMBER );
        input.setGravity(Gravity.RIGHT);
        builder.setView( input );
        input.setText(String.valueOf(miItm.getDescuento()));
        input.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    input.setText(formattedString);
                    input.setSelection(input.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                input.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /*18/6/19 Se quita valor del text y se agrega al texto BDL*/
        builder.setMessage( getResources().getString( R.string.Descu )).setCancelable(
                false ).setPositiveButton( getResources().getString( R.string.aceptar ),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            String desc = input.getText().toString();

                            if (!(WebService.monedaSeleccionada.getCod_Moneda().trim().equals(miItm.getCod_Moneda().trim()))) {
                                if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                                    totalDescuento = totalDescuento + (Double.valueOf(desc) * WebService.tipoCambio);
                                } else {
                                    totalDescuento = totalDescuento + (Double.valueOf(desc) / WebService.tipoCambio);
                                }
                            } else {
                                totalDescuento = totalDescuento + Double.valueOf(desc);
                            }

                            WebService.setTotalDescuento(totalDescuento);
                            miItm.setDescuentoAgregado(Double.valueOf(desc));
                            RefrescarTabla();
                        } catch (Exception e) {
                            e.toString();
                        }
                    }
                }).setNegativeButton( getResources().getString(R.string.Cancelar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        //modificado 25/05/2021 para que NO muestre ingreso de descuento
        /*AlertDialog alerta = builder.create();
        try {
            alerta.getWindow().setType(TYPE_APPLICATION_PANEL );
            alerta.show();
        } catch (Exception errrssd) {
            errrssd.toString();
        }*/
    }

    public void RefrescarTabla() {//Agregado para actualizar tabla una vez borrado
        tablaDeudas.removeAllViews();
        cont_filas = 0;

        totalRetenSelec = 0D;

        for (int i = 0; i < WebService.deudasViaje.size(); i++) {

            final ClienteCobranza instaDeuda = WebService.deudasViaje.get(i);
            //WebService.calula_interes = instaDeuda.getCalcula_interes();
            final TextView NombreTit = new TextView(context);
            TableRow tr1 = (TableRow) getLayoutInflater().inflate(R.layout.trviajes, null);
            TableRow tr2 = new TableRow(this);
            final TextView salto = new TextView(this);
            salto.setText("\n");
            tr2.addView(salto);
            tr1.setGravity(3);
            final TextView numViajePosta = new TextView(context);
            numViajePosta.setText(Html.fromHtml(CargarTexto(instaDeuda)));

            final ImageView checkBox = new ImageView(context);
            checkBox.setImageResource(R.drawable.positive);
            checkBox.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            checkBox.getLayoutParams().height = 125;
            checkBox.getLayoutParams().width = 125;
            checkBox.requestLayout();

            final ImageView partial = new ImageView(context);
            partial.setImageResource(R.drawable.parcialpay);
            partial.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            partial.getLayoutParams().height = 120;
            partial.getLayoutParams().width = 120;
            partial.requestLayout();
            partial.setClickable(true);

            if(instaDeuda.getHabilita().equals("N")){
                partial.setVisibility(View.INVISIBLE);
                checkBox.setVisibility(View.INVISIBLE);
            }

            partial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(WebService.monedaSeleccionada.getCod_Moneda().trim().equals(instaDeuda.getCod_Moneda().trim()))) {
                        if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                            totalDescuento = totalDescuento - (instaDeuda.getDescuentoAgregado() * WebService.tipoCambio);
                        } else {
                            totalDescuento = totalDescuento - (instaDeuda.getDescuentoAgregado() / WebService.tipoCambio);
                        }
                    } else {
                        totalDescuento = totalDescuento - instaDeuda.getDescuentoAgregado();
                    }
                    instaDeuda.setDescuentoAgregado(0);

                    if (WebService.configuracion.getMax_lin_rec() > 0 && WebService.deudasSeleccionadas.size() == WebService.max_lin_rec) {
                        displayAlertCantDeuda();
                    } else {
                        if (instaDeuda.getEstado() == 0 || instaDeuda.getEstado() == 1) {
                            partial.setImageResource(R.drawable.parcialpaypositive);
                            if (instaDeuda.getEstado() == 1) {
                                instaDeuda.setEstado(0);
                                checkBox.setImageResource(R.drawable.negative);

                                double val = Double.valueOf(instaDeuda.getTotalEntregado());
                                instaDeuda.setTotalEntregado(0);
                                WebService.removeDeuda(instaDeuda);

                                if (!(WebService.monedaSeleccionada.getCod_Moneda().trim().equals(instaDeuda.getCod_Moneda().trim()))) {
                                    if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                                        val_tot = val_tot - (val * WebService.tipoCambio);
                                    } else {
                                        val_tot = val_tot - (val / WebService.tipoCambio);
                                    }
                                } else {
                                    val_tot = val_tot - val;
                                }

                                numViajePosta.setText(Html.fromHtml(CargarTexto(instaDeuda)));

                                double valdesc = val_tot - totalDescuento;
                                if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")){
                                    valdesc = Utilidad.redondearDecimales(valdesc, 0);
                                }else {
                                    valdesc = Utilidad.redondearDecimales(valdesc, 2);
                                }
                                total.setText(getResources().getString(R.string.TotalDeuda) + " " + NumberFormat.getInstance(Locale.ITALY).format(valdesc));
                                WebService.setTotalDeudas(val_tot);

                                double reten = instaDeuda.getImp_a_retenc();
                                totalRetenSelec = totalRetenSelec - reten;
                                instaDeuda.setImp_a_retenc(instaDeuda.getImp_a_retenc());
                                WebService.removeReten(instaDeuda);
                                WebService.setTotalReten(totalRetenSelec);

                            }
                            displayAlertCantidad(instaDeuda);
                            instaDeuda.setEstado(3);//Este estado es para cuando se parcializo el precio

                        } else {
                            partial.setImageResource(R.drawable.parcialpay);
                            double val = Double.valueOf(instaDeuda.getTotalEntregado());
                            WebService.removeDeuda(instaDeuda);
                            instaDeuda.setEstado(0);

                            if (!(WebService.monedaSeleccionada.getCod_Moneda().trim().equals(instaDeuda.getCod_Moneda().trim()))) {
                                if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                                    val_tot = val_tot - (val * WebService.tipoCambio);
                                } else {
                                    val_tot = val_tot - (val / WebService.tipoCambio);
                                }
                            } else {
                                val_tot = val_tot - val;
                            }

                            numViajePosta.setText(Html.fromHtml(CargarTexto(instaDeuda)));

                            double valdesc = val_tot - totalDescuento;
                            if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")){
                                valdesc = Utilidad.redondearDecimales(valdesc, 0);
                            }else {
                                valdesc = Utilidad.redondearDecimales(valdesc, 2);
                            }
                            total.setText(getResources().getString(R.string.TotalDeuda) + " " + NumberFormat.getInstance(Locale.ITALY).format(valdesc));
                            WebService.setTotalDeudas(val_tot);
                            instaDeuda.setTotalEntregado(0);

                            numViajePosta.setText(Html.fromHtml(CargarTexto(instaDeuda)));

                            double reten = instaDeuda.getImp_a_retenc();
                            totalRetenSelec = totalRetenSelec + reten;
                            instaDeuda.setImp_a_retenc(instaDeuda.getImp_a_retenc());
                            WebService.removeReten(instaDeuda);
                            WebService.setTotalReten(totalRetenSelec);

                            numViajePosta.setText(Html.fromHtml(CargarTexto(instaDeuda)));
                        }
                    }
                }
            });

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (WebService.configuracion.getMax_lin_rec() > 0 && WebService.deudasSeleccionadas.size() == WebService.max_lin_rec) {
                        displayAlertCantDeuda();
                    } else {
                        if (instaDeuda.getEstado() == 0 || instaDeuda.getEstado() == 3) {
                            if (instaDeuda.getEstado() == 3) {
                                val_tot = val_tot - (int) instaDeuda.getTotalEntregado();
                            }
                            instaDeuda.setEstado(1);
                            checkBox.setImageResource(R.drawable.positive);
                            double val = Double.valueOf(instaDeuda.getImp_mov_mo());

                            instaDeuda.setTotalEntregado(val);
                            WebService.addDeuda(instaDeuda, val);

                            if (!(WebService.monedaSeleccionada.getCod_Moneda().trim().equals(instaDeuda.getCod_Moneda().trim()))) {
                                if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                                    val_tot = val_tot + (val * WebService.tipoCambio);
                                } else {
                                    val_tot = val_tot + (val / WebService.tipoCambio);
                                }
                            } else {
                                val_tot = val_tot + val;
                            }

                            if(instaDeuda.getDescuento() > 0.0) {
                                displayAlertDesc(instaDeuda);
                            }

                            numViajePosta.setText(Html.fromHtml(CargarTexto(instaDeuda)));

                            double valdesc = val_tot - totalDescuento;
                            if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")){
                                valdesc = Utilidad.redondearDecimales(valdesc, 0);
                            }else {
                                valdesc = Utilidad.redondearDecimales(valdesc, 2);
                            }
                            total.setText(getResources().getString(R.string.TotalDeuda) + " " + NumberFormat.getInstance(Locale.ITALY).format(valdesc));
                            WebService.setTotalDeudas(val_tot);

                            double reten = instaDeuda.getImp_a_retenc();
                            totalRetenSelec = totalRetenSelec + reten;
                            instaDeuda.setImp_a_retenc(instaDeuda.getImp_a_retenc());
                            WebService.addReten(instaDeuda, instaDeuda.getImp_a_retenc());
                            WebService.setTotalReten(totalRetenSelec);

                            partial.setImageResource(R.drawable.parcialpay);

                        } else {
                            instaDeuda.setEstado(0);
                            checkBox.setImageResource(R.drawable.negative);

                            double val = Double.valueOf(instaDeuda.getTotalEntregado());
                            //double val = Double.valueOf(instaDeuda.getImp_mov_mo());
                            instaDeuda.setTotalEntregado(0);
                            WebService.removeReten(instaDeuda);

                            double valDesc = Double.valueOf(instaDeuda.getDescuentoAgregado());

                            if (!(WebService.monedaSeleccionada.getCod_Moneda().trim().equals(instaDeuda.getCod_Moneda().trim()))) {
                                if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                                    val_tot = val_tot - (val * WebService.tipoCambio);
                                    totalDescuento = totalDescuento - (valDesc * WebService.tipoCambio);
                                } else {
                                    val_tot = val_tot - (val / WebService.tipoCambio);
                                    totalDescuento = totalDescuento - (valDesc / WebService.tipoCambio);
                                }
                            } else {
                                val_tot = val_tot - val;
                                totalDescuento = totalDescuento - valDesc;
                            }

                            instaDeuda.setDescuentoAgregado(0);
                            numViajePosta.setText(Html.fromHtml(CargarTexto(instaDeuda)));

                            double valdesc = val_tot - totalDescuento;
                            if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")){
                                valdesc = Utilidad.redondearDecimales(valdesc, 0);
                            }else {
                                valdesc = Utilidad.redondearDecimales(valdesc, 2);
                            }
                            total.setText(getResources().getString(R.string.TotalDeuda) + " " + NumberFormat.getInstance(Locale.ITALY).format(valdesc));
                            WebService.setTotalDeudas(val_tot);

                            WebService.removeReten(instaDeuda);
                            double reten = instaDeuda.getImp_a_retenc();
                            totalRetenSelec = totalRetenSelec - reten;
                            WebService.setTotalReten(totalRetenSelec);
                        }
                    }
                }
            });

            switch (instaDeuda.getEstado()) {
                case 0:
                    checkBox.setImageResource(R.drawable.negative);
                    partial.setImageResource(R.drawable.parcialpay);
                    break;
                case 1:
                    checkBox.setImageResource(R.drawable.positive);
                    partial.setImageResource(R.drawable.parcialpay);
                    break;
                case 3:
                    partial.setImageResource(R.drawable.parcialpaypositive);
                    checkBox.setImageResource(R.drawable.negative);
                    break;
            }

            numViajePosta.setText(Html.fromHtml(CargarTexto(instaDeuda)));

            if (instaDeuda.getCod_Docum().trim().equals("NCREDITO")) {
                tr1.setBackgroundResource(R.color.Ncredito);
            }

            tr1.addView(NombreTit);
            tr1.addView(numViajePosta);
            tr1.addView(checkBox);
            tr1.addView(partial);
            tablaDeudas.addView(tr1);
            tablaDeudas.addView(tr2);

        }

        double valdesc = val_tot - totalDescuento;
        if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")){
            valdesc = Utilidad.redondearDecimales(valdesc, 0);
        }else {
            valdesc = Utilidad.redondearDecimales(valdesc, 2);
        }
        total.setText(getResources().getString(R.string.TotalDeuda) + " " + String.valueOf(NumberFormat.getInstance(Locale.ITALY).format(valdesc)));
        WebService.setTotalDeudas(val_tot);
        WebService.setTotalReten(totalRetenSelec);
    }

    private void SeleccionMoneda(){
        try {
            if (spMonedas != null) {
                if (spMonedas.getSelectedItem() != null) {
                    if (spMonedas.getSelectedItem().toString() != null) {
                        String moneda = spMonedas.getSelectedItem().toString().trim();
                        Moneda instanciaMoneda = new Moneda();
                        for (int i = 0; i < WebService.monedas.size(); i++) {
                            instanciaMoneda = WebService.monedas.get(i);
                            if (instanciaMoneda.getNom_Moneda().trim().equals(moneda)) {
                                WebService.monedaSeleccionada = instanciaMoneda;
                                WebService.clienteActual.setCod_Moneda(WebService.monedaSeleccionada.getCod_Moneda().trim());
                            }
                        }
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void CargarDatos() {
        try {
            for (int i = 0; i < WebService.deudasViaje.size(); i++) {

                final ClienteCobranza instaDeuda = WebService.deudasViaje.get(i);
                //WebService.calula_interes = instaDeuda.getCalcula_interes();
                instaDeuda.setTotalEntregado(0);
                instaDeuda.setDescuentoAgregado(0);
                instaDeuda.setEstado(0);
                //Fin agregado
                final TextView NombreTit = new TextView(context);
                TableRow tr1 = (TableRow) getLayoutInflater().inflate(R.layout.trviajes, null);
                TableRow tr2 = new TableRow(this);
                final TextView salto = new TextView(this);
                salto.setText("\n");
                tr2.addView(salto);
                tr1.setGravity(3);
                final TextView numViajePosta = new TextView(context);
                numViajePosta.setText(Html.fromHtml(CargarTexto(instaDeuda)));

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
                partial.getLayoutParams().height = 120;
                partial.getLayoutParams().width = 120;
                partial.requestLayout();
                partial.setClickable(true);

                if(instaDeuda.getHabilita().equals("N")){
                    partial.setVisibility(View.INVISIBLE);
                    checkBox.setVisibility(View.INVISIBLE);
                }

                       /* double val = Double.valueOf(instaDeuda.getImp_mov_mo());

                        WebService.addDeuda(instaDeuda, val);
                        instaDeuda.setTotalEntregado(val);*/

                // double valDesc = Double.valueOf(instaDeuda.getDescuento());

                    /*if (WebService.configuracion.getMax_lin_rec() > 0 && WebService.deudasSeleccionadas.size() == WebService.max_lin_rec) {
                        displayAlertCantDeuda();
                        instaDeuda.setEstado(0);
                    } else {
                        if (!(WebService.monedaSeleccionada.getCod_Moneda().trim().equals(instaDeuda.getCod_Moneda().trim()))) {
                            if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                                val_tot = val_tot + (val * WebService.tipoCambio);
                                //totalDescuento = totalDescuento + (valDesc * WebService.tipoCambio);
                            } else {
                                val_tot = val_tot + (val / WebService.tipoCambio);
                                //totalDescuento = totalDescuento + (valDesc / WebService.tipoCambio);
                            }
                        } else {
                            val_tot = val_tot + val;
                            //totalDescuento = totalDescuento + valDesc;
                        }
                    }*/

                double valdesc = val_tot - totalDescuento;
                if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")){
                    valdesc = Utilidad.redondearDecimales(valdesc, 0);
                }else {
                    valdesc = Utilidad.redondearDecimales(valdesc, 2);
                }
                total.setText(getResources().getString(R.string.TotalDeuda) + " " + NumberFormat.getInstance(Locale.ITALY).format(valdesc));
                WebService.setTotalDeudas(val_tot);

                numViajePosta.setText(Html.fromHtml(CargarTexto(instaDeuda)));
                numViajePosta.setTextSize(15);

                partial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!(WebService.monedaSeleccionada.getCod_Moneda().trim().equals(instaDeuda.getCod_Moneda().trim()))) {
                            if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                                totalDescuento = totalDescuento - (instaDeuda.getDescuentoAgregado() * WebService.tipoCambio);
                            } else {
                                totalDescuento = totalDescuento - (instaDeuda.getDescuentoAgregado() / WebService.tipoCambio);
                            }
                        } else {
                            totalDescuento = totalDescuento - instaDeuda.getDescuentoAgregado();
                        }
                        instaDeuda.setDescuentoAgregado(0);

                        if (WebService.configuracion.getMax_lin_rec() > 0 && WebService.deudasSeleccionadas.size() == WebService.max_lin_rec) {
                            displayAlertCantDeuda();
                        } else {
                            if (instaDeuda.getEstado() == 0 || instaDeuda.getEstado() == 1) {
                                partial.setImageResource(R.drawable.parcialpaypositive);
                                if (instaDeuda.getEstado() == 1) {
                                    instaDeuda.setEstado(0);
                                    checkBox.setImageResource(R.drawable.negative);
                                    double val = Double.valueOf(instaDeuda.getImp_mov_mo());
                                    instaDeuda.setTotalEntregado(0);
                                    WebService.removeDeuda(instaDeuda);

                                    if (!(WebService.monedaSeleccionada.getCod_Moneda().trim().equals(instaDeuda.getCod_Moneda().trim()))) {
                                        if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                                            val_tot = val_tot - (val * WebService.tipoCambio);
                                            totalDescuento = totalDescuento - (instaDeuda.getDescuentoAgregado() * WebService.tipoCambio);
                                        } else {
                                            val_tot = val_tot - (val / WebService.tipoCambio);
                                            totalDescuento = totalDescuento - (instaDeuda.getDescuentoAgregado() / WebService.tipoCambio);
                                        }
                                    } else {
                                        val_tot = val_tot - val;
                                        totalDescuento = totalDescuento - instaDeuda.getDescuentoAgregado();
                                    }
                                    instaDeuda.setDescuentoAgregado(0);

                                    numViajePosta.setText(Html.fromHtml(CargarTexto(instaDeuda)));

                                    double valdesc = val_tot - totalDescuento;
                                    if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")){
                                        valdesc = Utilidad.redondearDecimales(valdesc, 0);
                                    }else {
                                        valdesc = Utilidad.redondearDecimales(valdesc, 2);
                                    }
                                    total.setText(getResources().getString(R.string.TotalDeuda) + " " + NumberFormat.getInstance(Locale.ITALY).format(valdesc));
                                    WebService.setTotalDeudas(val_tot);

                                    double reten = instaDeuda.getImp_a_retenc();
                                    totalRetenSelec = totalRetenSelec - reten;
                                    instaDeuda.setImp_a_retenc(instaDeuda.getImp_a_retenc());
                                    WebService.removeReten(instaDeuda);
                                    WebService.setTotalReten(totalRetenSelec);

                                }
                                displayAlertCantidad(instaDeuda);

                            } else {
                                partial.setImageResource(R.drawable.parcialpay);
                                instaDeuda.setTotalEntregado(0);
                                Double val = instaDeuda.getImp_mov_mo();

                                if (!(WebService.monedaSeleccionada.getCod_Moneda().trim().equals(instaDeuda.getCod_Moneda().trim()))) {
                                    if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                                        val_tot = val_tot + (val * WebService.tipoCambio);
                                    } else {
                                        val_tot = val_tot + (val / WebService.tipoCambio);
                                    }
                                } else {
                                    val_tot = val_tot + val;
                                }

                                WebService.addDeuda(instaDeuda, instaDeuda.getImp_mov_mo());
                                instaDeuda.setEstado(0);
                                WebService.setTotalDeudas(val_tot);
                                numViajePosta.setText(Html.fromHtml(CargarTexto(instaDeuda)));

                                double reten = instaDeuda.getImp_a_retenc();
                                totalRetenSelec = totalRetenSelec + reten;
                                instaDeuda.setImp_a_retenc(instaDeuda.getImp_a_retenc());
                                WebService.addReten(instaDeuda, instaDeuda.getImp_a_retenc());
                                WebService.setTotalReten(totalRetenSelec);

                                numViajePosta.setText(Html.fromHtml(CargarTexto(instaDeuda)));
                            }
                        }
                    }
                });

                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (WebService.configuracion.getMax_lin_rec() > 0 && WebService.deudasSeleccionadas.size() == WebService.max_lin_rec) {
                            displayAlertCantDeuda();
                        } else {
                            if (instaDeuda.getEstado() == 0 || instaDeuda.getEstado() == 3) {
                                if (instaDeuda.getEstado() == 3) {
                                    val_tot = val_tot - (int) instaDeuda.getTotalEntregado();
                                }
                                instaDeuda.setEstado(1);
                                checkBox.setImageResource(R.drawable.positive);
                                double val = Double.valueOf(instaDeuda.getImp_mov_mo());
                                instaDeuda.setTotalEntregado(val);
                                WebService.addDeuda(instaDeuda, instaDeuda.getImp_mov_mo());

                                if (!(WebService.monedaSeleccionada.getCod_Moneda().trim().equals(instaDeuda.getCod_Moneda().trim()))) {
                                    if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                                        val_tot = val_tot + (val * WebService.tipoCambio);
                                    } else {
                                        val_tot = val_tot + (val / WebService.tipoCambio);
                                    }
                                } else {
                                    val_tot = val_tot + val;
                                }

                                if(instaDeuda.getDescuento() > 0.0) {
                                    displayAlertDesc(instaDeuda);
                                }

                                numViajePosta.setText(Html.fromHtml(CargarTexto(instaDeuda)));

                                double valdesc = val_tot - totalDescuento;
                                if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")){
                                    valdesc = Utilidad.redondearDecimales(valdesc, 0);
                                }else {
                                    valdesc = Utilidad.redondearDecimales(valdesc, 2);
                                }
                                total.setText(getResources().getString(R.string.TotalDeuda) + " " + NumberFormat.getInstance(Locale.ITALY).format(valdesc));
                                WebService.setTotalDeudas(val_tot);

                                double reten = instaDeuda.getImp_a_retenc();
                                totalRetenSelec = totalRetenSelec + reten;
                                instaDeuda.setImp_a_retenc(instaDeuda.getImp_a_retenc());
                                WebService.addReten(instaDeuda, instaDeuda.getImp_a_retenc());
                                WebService.setTotalReten(totalRetenSelec);

                                partial.setImageResource(R.drawable.parcialpay);

                            } else {
                                instaDeuda.setEstado(0);
                                checkBox.setImageResource(R.drawable.negative);
                                partial.setImageResource(R.drawable.parcialpay);
                                double val = Double.valueOf(instaDeuda.getImp_mov_mo());
                                instaDeuda.setTotalEntregado(0);
                                WebService.removeDeuda(instaDeuda);
                                instaDeuda.setDescuentoAgregado(0);
                                numViajePosta.setText(Html.fromHtml(CargarTexto(instaDeuda)));

                                double valDesc = Double.valueOf(instaDeuda.getDescuentoAgregado());

                                if (!(WebService.monedaSeleccionada.getCod_Moneda().trim().equals(instaDeuda.getCod_Moneda().trim()))) {
                                    if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                                        val_tot = val_tot - (val * WebService.tipoCambio);
                                        totalDescuento = totalDescuento - (valDesc * WebService.tipoCambio);
                                    } else {
                                        val_tot = val_tot - (val / WebService.tipoCambio);
                                        totalDescuento = totalDescuento - (valDesc / WebService.tipoCambio);
                                    }
                                } else {
                                    val_tot = val_tot - val;
                                    totalDescuento = totalDescuento - valDesc;
                                }

                                double valdesc = val_tot - totalDescuento;
                                if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")){
                                    valdesc = Utilidad.redondearDecimales(valdesc, 0);
                                }else {
                                    valdesc = Utilidad.redondearDecimales(valdesc, 2);
                                }
                                total.setText(getResources().getString(R.string.TotalDeuda) + " " + NumberFormat.getInstance(Locale.ITALY).format(valdesc));
                                WebService.setTotalDeudas(val_tot);
                                WebService.setTotalDescuento(totalDescuento);

                                WebService.removeReten(instaDeuda);
                                double reten = instaDeuda.getImp_a_retenc();
                                totalRetenSelec = totalRetenSelec - reten;
                                WebService.setTotalReten(totalRetenSelec);
                            }
                        }
                    }
                });

                switch (instaDeuda.getEstado()) {
                    case 0:
                        checkBox.setImageResource(R.drawable.negative);
                        partial.setImageResource(R.drawable.parcialpay);
                        break;
                    case 1:
                        checkBox.setImageResource(R.drawable.positive);
                        partial.setImageResource(R.drawable.parcialpay);
                        break;
                    case 3:
                        partial.setImageResource(R.drawable.parcialpaypositive);
                        checkBox.setImageResource(R.drawable.negative);
                        break;
                }

                if (instaDeuda.getCod_Docum().trim().equals("NCREDITO")) {
                    tr1.setBackgroundResource(R.color.Ncredito);
                }

                tr1.addView(NombreTit);
                tr1.addView(numViajePosta);
                tr1.addView(checkBox);
                tr1.addView(partial);
                tablaDeudas.addView(tr1);
                tablaDeudas.addView(tr2);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    private class TraerDeudasViaje extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface
        public TraerDeudasViaje(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            WebService.TraerDeudasViaje();
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

    class TraerMonedas extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface
        public TraerMonedas(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
                WebService.ObtenerMonedas();
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

    private void CargaMoneda() {
        try {
            for (int i = 0; i < WebService.monedas.size(); i++) {
                String nombreAgregar1 = WebService.monedas.get(i).getNom_Moneda();
                spinnerMonedaArray.add(nombreAgregar1);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private class ActualizarUbicacion extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... strings) {
            WebService.ActualizarUbicacion( params,"Viajes/ActualizarUbicacion.php" );
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {}
    }


    class traerParempTask extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface
        public traerParempTask(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            WebService.traerTipoC();
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

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                TxtFecha.setText(diaFormateado + GUION + mesFormateado + GUION + year);

                fechaSeleccionada = new Date();
                fechaSeleccionada.setHours(0);
                fechaSeleccionada.setMinutes(0);
                fechaSeleccionada.setSeconds(0);
                fechaSeleccionada.setDate(dayOfMonth);
                fechaSeleccionada.setMonth(month);
                fechaSeleccionada.setYear(year-1900);

            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }

    private class  TraerDeudores extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface
        public TraerDeudores(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            if(Utilidad.isNetworkAvailable()) {
                RequestParams params1 = new RequestParams(  );
                params1.put( "cod_tit",WebService.clienteActual.getCod_Tit_Gestion().trim() );
                params1.put( "cod_empresa",WebService.usuarioActual.getEmpresa().trim());
                params1.put("username", WebService.USUARIOLOGEADO.trim());
                //params1.put( "cod_moneda",WebService.monedaSeleccionada.getCod_Moneda() );
                WebService.TraerDeudas(params1);
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
        protected void onProgressUpdate(Void... values) { }
    }
}
