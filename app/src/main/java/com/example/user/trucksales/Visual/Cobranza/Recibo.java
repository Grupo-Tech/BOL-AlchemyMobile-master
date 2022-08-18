package com.example.user.trucksales.Visual.Cobranza;

import android.Manifest;
import android.app.Activity;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.print.PageRange;
import android.print.PrintManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.DragAndDropPermissions;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.ClienteCobranza;
import com.example.user.trucksales.Encapsuladoras.ValoresPago;
import com.example.user.trucksales.Encapsuladoras.Viaje;
import com.example.user.trucksales.Negocio.ApplicationContext;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Printer;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.loopj.android.http.RequestParams;
import com.nullwire.trace.ExceptionHandler;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;

public class Recibo extends Activity {
    public EditText numRecibo;
    ImageView casita, atras;
    TextView usuario, cliente, totalFactura, fecha, retorno, tipoC, TxtFecha, TitulRec, NomEmp, TxtACuenta;
    Button btnAceptarRecibo;
    TableLayout TablaFacAfec, TablaValores, TablaTitulo;
    private boolean reciboValido;
    private Utilidades Utilidad;
    private  boolean otraDeuda;
    private  boolean aceptaRec = false;

    ArrayList<String> getbtName = new ArrayList<>();
    ArrayList<String> getbtNM = new ArrayList<>();
    ArrayList<String> getbtMax = new ArrayList<>();
    public boolean mBconnect = false;
    public int state;

    public String nom_copia;


    ApplicationContext contextoImpre;
    //private static String fac;

    protected static RequestParams paramsIng = new RequestParams();
    protected static RequestParams params = new RequestParams();
    protected static RequestParams params1 = new RequestParams();
    protected static RequestParams params2 = new RequestParams();
    protected static RequestParams params4 = new RequestParams(  );
    protected static RequestParams params5 = new RequestParams(  );

    String numeroDoc = "1";

    private String nro_viaje = "0";
    protected Context context;
    private ArrayList<TextView> ListaTextViews;

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recibos);

        WebService.reto_AgregaCobranza = "";

        context = this;
        Utilidad = new Utilidades(context);

        GuardarDatosUsuario.Contexto = context;

        reciboValido = true;

        ListaTextViews = new ArrayList<>();
        if (WebService.USUARIOLOGEADO != null) {
            try {
               // if (Utilidad.isNetworkAvailable()) {
                    retorno = findViewById(R.id.solRec);
                    retorno.setVisibility(View.GONE);

                    TxtACuenta = findViewById(R.id.TxtACuenta);

                    if(WebService.acuentaValores <= 0){
                        TxtACuenta.setVisibility(View.GONE);
                    }else{
                        TxtACuenta.setText(getResources().getString(R.string.ACuenta) +" "+ NumberFormat.getInstance(Locale.ITALY).format(WebService.acuentaValores).toString());
                    }

                    String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                    fecha = (TextView) findViewById(R.id.LblFecha);
                    fecha.setText(timeStamp);

                    btnAceptarRecibo = (Button) findViewById(R.id.aceptarRecibo);
                    btnAceptarRecibo.setText("Aceptar");
                    numRecibo = findViewById(R.id.EdtNumRecibo);
                    numRecibo.setInputType(InputType.TYPE_CLASS_NUMBER);

                    TitulRec = findViewById(R.id.TitulRec);

                    if (WebService.configuracion.getNum_recibo_aut().trim().equals("S")) {
                        numRecibo.setEnabled(false);
                        numRecibo.setVisibility(View.GONE);
                        TitulRec.setVisibility(View.GONE);
                    } else {
                        numRecibo.setVisibility(View.VISIBLE);
                        numRecibo.setEnabled(true);
                        numRecibo.setText(WebService.nro_recibo);
                        TitulRec.setVisibility(View.VISIBLE);
                    }

                    numRecibo.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            //noValido();
                        }
                    });



                    casita = findViewById(R.id.casita);
                    casita.setClickable(true);
                    casita.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                if (aceptaRec == false) {
                                    Utilidad.vibraticionBotones(context);
                                    WebService.limpiarValores();
                                    TablaValores.removeAllViews();
                                    TablaFacAfec.removeAllViews();
                                    Intent myIntent = new Intent(v.getContext(), SeleccionarDeudas.class);
                                    startActivity(myIntent);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                    atras = findViewById(R.id.btnAtras);
                    atras.setClickable(true);
                    atras.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try{
                            if (aceptaRec == false) {
                                Utilidad.vibraticionBotones(context);
                                TablaFacAfec.removeAllViews();
                                Intent myIntent = new Intent(v.getContext(), IngresarValores.class);
                                startActivity(myIntent);
                            }
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    });

                    TablaFacAfec = findViewById(R.id.TablaFacAfec);
                    TablaValores = findViewById(R.id.TablaValores);
                    TablaTitulo = findViewById(R.id.TablaTitulo);

                    usuario = findViewById(R.id.LblUsu);
                    usuario.setText(WebService.USUARIOLOGEADO);

                    NomEmp = findViewById(R.id.NomEmp);
                    NomEmp.setText(WebService.clienteActual.getNomEmp());
                    NomEmp.setTypeface(null, Typeface.BOLD);

                    cliente = findViewById(R.id.TxtCliente);
                    cliente.setText(WebService.clienteActual.getNom_Tit().trim());

                    totalFactura = findViewById(R.id.TxtTotal);
                    tipoC = findViewById(R.id.tipoC);

                    String tipoCamb = String.valueOf(WebService.tipoCambio);
                    if(tipoCamb.contains(".0")){
                        double tipo = Utilidad.redondearDecimales(WebService.tipoCambio, 0);
                        tipoC.setText(getResources().getString(R.string.tipocambio) + NumberFormat.getInstance(Locale.ITALY).format(tipo).toString());
                    }else{
                        tipoC.setText(getResources().getString(R.string.tipocambio) + Utilidad.redondearDecimales(WebService.tipoCambio, 2));
                    }

                    /*double tipo = Utilidad.redondearDecimales(WebService.tipoCambio, 0);
                    tipoC.setText(getResources().getString(R.string.tipocambio) + NumberFormat.getInstance(Locale.ITALY).format(tipo).toString());*/

                    Double totalEfectivoUSDConvertido = WebService.totalEfectivoUSD;
                    Double totalEfectivoConvertido = WebService.totalEfectivo;

                    //final Double fac = WebService.totalACobrar - WebService.totalVuelto;
                    final Double fac = WebService.totalACobrar;

                   /* if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("2")){
                        totalFactura.setText(getResources().getString(R.string.IngreseTot) + WebService.monedaSeleccionada.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(fac).toString());
                    }else {*/
                        totalFactura.setText(getResources().getString(R.string.IngreseTot) + WebService.monedaSeleccionada.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(fac).toString());
                    //}
                    //if (WebService.usuarioActual.getTipoCobrador().equals("D") /*|| (WebService.usuarioActual.getTipoCobrador().equals("L"))*/) {
                        if (WebService.clienteActual.getLatiud_Ubic().equals(0.0)) {

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

                            WebService.lat_origen = latitude;
                            WebService.long_origen = longitude;

                           // if(WebService.configuracion.getSol_guardar_ubi().equals("S")) {
                                displayAlertUbicacion();
                            //}
                        }
                    //}

                    btnAceptarRecibo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                aceptaRec = true;
                                Utilidad.vibraticionBotones(context);
                                if (Utilidad.isNetworkAvailable()) {
                                    if (reciboValido) {
                                        // if(WebService.configuracion.getNum_recibo_aut().equals("N")) {
                                        if (WebService.configuracion.getNum_recibo_aut().equals("N") && numRecibo.getText().toString().equals("") || numRecibo.getText().toString().equals("0")) {
                                            Toast toast = Toast.makeText(context, getResources().getString(R.string.NumRec), Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                                            toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                                            toast.show();//showing the toast is important***/
                                        }
                                        //}
                                        else {
                                            paramsIng = new RequestParams();

                                            String nuevoFormatoFecha = "";
                                            String fechvenc;
                                            Date date = null;

                                            String lineasDeudaSelec = "";
                                            for (int i = 0; WebService.deudasSeleccionadas.size() > i; i++) {
                                                ClienteCobranza instaCliente = WebService.deudasSeleccionadas.get(i);

                                                try {
                                                    fechvenc = instaCliente.getFecha_Vence();
                                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                                                    date = formatter.parse(fechvenc);

                                                } catch (ParseException ex) {
                                                    ex.printStackTrace();
                                                    //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                                                }

                                                nuevoFormatoFecha = new SimpleDateFormat("dd/MM/yyyy").format(date);

                                                if (i != WebService.deudasSeleccionadas.size() - 1) {

                                                    lineasDeudaSelec = lineasDeudaSelec
                                                            //+ instenciaItem.getNom_Articulo().trim().replace( ',','.' )
                                                            + instaCliente.getCod_dpto().trim()
                                                            + "," + instaCliente.getSerie_docum().trim()
                                                            + "," + instaCliente.getNro_Docum().trim()
                                                            + "," + instaCliente.getCod_Docum().trim()
                                                            + "," + nuevoFormatoFecha
                                                            + "," + instaCliente.getImp_mov_mo()
                                                            + "," + instaCliente.getTotalEntregado()
                                                            + "," + instaCliente.getDescuentoAgregado()
                                                            + "," + instaCliente.getCod_Moneda() + ";";
                                                } else {
                                                    lineasDeudaSelec = lineasDeudaSelec
                                                            //+ instenciaItem.getNom_Articulo().trim().replace( ',','.' )
                                                            + instaCliente.getCod_dpto().trim()
                                                            + "," + instaCliente.getSerie_docum().trim()
                                                            + "," + instaCliente.getNro_Docum().trim()
                                                            + "," + instaCliente.getCod_Docum().trim()
                                                            + "," + nuevoFormatoFecha
                                                            + "," + instaCliente.getImp_mov_mo()
                                                            + "," + instaCliente.getTotalEntregado()
                                                            + "," + instaCliente.getDescuentoAgregado()
                                                            + "," + instaCliente.getCod_Moneda().trim();
                                                }

                                            }
                                            String lineasPago = "";
                                            String fechvalor;
                                            //String nuevoFormatoFechaValor = "";
                                            //Date date2 = null;

                                            for (int i = 0; WebService.valoresPago.size() > i; i++) {
                                                ValoresPago instaValores = WebService.valoresPago.get(i);

                                                fechvalor = instaValores.getFec_valor();
                                               // fechvalor = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

                                                if (i != WebService.valoresPago.size() - 1) {

                                                    lineasPago = lineasPago
                                                            //+ instenciaItem.getNom_Articulo().trim().replace( ',','.' )
                                                            + instaValores.getCod_docum().trim()
                                                            + "," + instaValores.getNro_docum()
                                                            + "," + instaValores.getCod_moneda().trim()
                                                            + "," + instaValores.getCod_banco().trim()
                                                            + "," + instaValores.getCod_sctatit().trim()
                                                            + "," + fechvalor
                                                            + "," + instaValores.getImporte()
                                                            + "," + instaValores.getSuc_ref().trim()
                                                            + "," + instaValores.getFac_ref().trim()
                                                            + "," + instaValores.getDiferencia()
                                                            + "," + instaValores.getTipo_cambio().toString().trim() + ";";
                                                } else {
                                                    lineasPago = lineasPago
                                                            //+ instenciaItem.getNom_Articulo().trim().replace( ',','.' )
                                                            + instaValores.getCod_docum().trim()
                                                            + "," + instaValores.getNro_docum()
                                                            + "," + instaValores.getCod_moneda().trim()
                                                            + "," + instaValores.getCod_banco().trim()
                                                            + "," + instaValores.getCod_sctatit().trim()
                                                            + "," + fechvalor
                                                            + "," + instaValores.getImporte()
                                                            + "," + instaValores.getSuc_ref().trim()
                                                            + "," + instaValores.getFac_ref().trim()
                                                            + "," + instaValores.getDiferencia()
                                                            + "," + instaValores.getTipo_cambio().toString().trim();
                                                }
                                            }

                                            String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                                            //System.out.println(deviceId);
                                            Double total = Double.valueOf(fac);
                                            //paramsIng.add("fec_doc", timeStamp);
                                            if (WebService.usuarioActual.getTipoCobrador().equals("L")) {
                                                WebService.clienteActual.setCodEmp(WebService.usuarioActual.getEmpresa().trim());
                                            }
                                            String cod_emp = WebService.clienteActual.getCodEmp().trim();

                                            int nro_viaje = 0;
                                            if (WebService.usuarioActual.getTipoCobrador().equals("D")) {
                                                nro_viaje = Integer.valueOf(WebService.viajeSeleccionadoCobrador.getNumViaje().trim());
                                            }

                                            if (WebService.configuracion.getNum_recibo_aut().trim().equals("N")) {
                                                numeroDoc = numRecibo.getText().toString();
                                            }
                                            paramsIng.add("fec_doc", WebService.fecha);
                                            paramsIng.add("nro_docum", numeroDoc.trim());
                                            paramsIng.add("cod_moneda", WebService.monedaSeleccionada.getCod_Moneda().trim());
                                            paramsIng.add("cod_tit", WebService.clienteActual.getCod_Tit_Gestion().trim());
                                            paramsIng.add("usuario", WebService.USUARIOLOGEADO);
                                            paramsIng.add("total", total.toString());
                                            paramsIng.add("tipo_cambio", WebService.tipoCambio.toString());
                                            paramsIng.add("cod_emp", cod_emp);
                                            paramsIng.add("nro_viaje", String.valueOf(nro_viaje));
                                            paramsIng.add("id_mobile", deviceId);

                                            // PARA PASAR VALORES DE PAGO
                                            paramsIng.add("cantidad_lineas_pago", String.valueOf(WebService.valoresPago.size()));
                                            paramsIng.add("lineasPago", lineasPago);
                                            // PARA PASAR DEUDA
                                            paramsIng.add("cantidad_lineas", String.valueOf(WebService.deudasSeleccionadas.size()));
                                            paramsIng.add("lineas", lineasDeudaSelec);

                                            AgregarCobranza task2 = new AgregarCobranza(new AsyncResponse() {
                                                @Override
                                                public void processFinish(Object output) {
                                                    if (WebService.reto_AgregaCobranza.equals("ok") & WebService.nro_transRec > 0) { //Esto quiere decir que guardo ok el recibo
                                                        if (WebService.nro_transRec != null && !WebService.nro_transRec.equals("0")) {
                                                            retorno.setVisibility(View.VISIBLE);
                                                            retorno.setText(getResources().getString(R.string.reto_Rec));
                                                            if (WebService.configuracion.getTipo_impresora().equals("M")) {
                                                               // for(int i = 0; i<WebService.cantidadCopias; i++) {
                                                                    contextoImpre = new ApplicationContext();
                                                                    contextoImpre = (ApplicationContext) getApplicationContext();
                                                                    contextoImpre.setObject();
                                                                    try {
                                                                       nom_copia = "Original";

                                                                        Conectar();
                                                                        TraerDatoImpresion task = new TraerDatoImpresion(new AsyncResponse() {
                                                                            @Override
                                                                            public void processFinish(Object output) {
                                                                                if (!WebService.errToken.equals("")) {
                                                                                    Intent myIntent = new Intent(context, Login.class);
                                                                                    startActivity(myIntent);
                                                                                } else {
                                                                                    Impr();
                                                                                    if (WebService.configuracion.getCant_copias() == 2) {
                                                                                        displayAlertImpr();
                                                                                    } else {
                                                                                        onResume();
                                                                                    }
                                                                                /* try {
                                                                                    String str = WebService.respuestaWSImpresora;
                                                                                    System.out.println(str);
                                                                                    Imprimir(str);
                                                                                } catch (Exception e) {
                                                                                    e.printStackTrace();
                                                                                }*/
                                                                                }
                                                                            }
                                                                        });
                                                                        task.execute();
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                        //Toast.makeText( getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG ).show();
                                                                        onResume();
                                                                    }
                                                               // }
                                                            }
                                                            if (WebService.configuracion.getTipo_impresora().equals("T")) {
                                                                Printer pr = new Printer();
                                                                PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
                                                                pr.setPrMang(printManager);
                                                                pr.setContx(context);
                                                                System.out.println(String.valueOf(WebService.nro_transRec));
                                                                pr.setValor(String.valueOf(WebService.nro_transRec));
                                                                pr.setTipo("RE");
                                                                pr.genarPdf(pr);
                                                                //System.out.println("s");
                                                                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(WebService.WEB_URL));
                                                                //startActivity(browserIntent);
                                                            }
                                                        } else {
                                                            retorno.setVisibility(View.VISIBLE);
                                                            retorno.setText("Error al grabar datos \n" + WebService.reto_AgregaCobranza);
                                                        }
                                                    } else { //si no guardo el recibo muestra error y retorna
                                                        if(WebService.reto_AgregaCobranza.toUpperCase().contains("JSON")) {
                                                            Intent myIntent = new Intent(context, Login.class);
                                                            startActivity(myIntent);
                                                        }else {
                                                            retorno.setVisibility(View.VISIBLE);
                                                            aceptaRec = false;
                                                            retorno.setText("Error al grabar datos \n" + WebService.reto_AgregaCobranza);
                                                        }
                                                    }
                                                }
                                            });
                                            task2.execute();
                                        }
                                    } else {
                                        WebService.reto_AgregaCobranza = "";
                                        aceptaRec = false;
                                    }
                                } else {
                                    Utilidad.CargarToastConexion(context);
                                    aceptaRec = false;
                                }
                            } catch (Exception ex) {
                                Toast.makeText( getApplicationContext(), "Recibo: " + paramsIng + ex.getMessage(), Toast.LENGTH_LONG ).show();
                                ex.printStackTrace();
                                aceptaRec = false;
                            }
                        }
                    });

                    TableLayout tl0 = new TableLayout(this);
                    TableLayout tl1 = new TableLayout(this);
                    TableLayout tl2 = new TableLayout(this);
                    TableRow tr1 = new TableRow(this);
                    TableRow tr2 = new TableRow(this);
                    TableRow tr3 = new TableRow(this);
                    TableRow tr4 = new TableRow(this);
                    TableRow tr5 = new TableRow(this);

                    TextView facAfect = new TextView(this);
                    facAfect.setText(getResources().getString(R.string.FacAfectadas));
                    facAfect.setTextSize(15);
                    facAfect.setTextColor(Color.parseColor("#ffffff"));
                    facAfect.setPadding(10, 5, 0, 5);
                    tr1.addView(facAfect);
                    tr1.setBackgroundResource(R.color.colorPrimary);

                    TablaFacAfec.addView(tl0);
                    TablaFacAfec.addView(tr1);

                    for (int i = 0; WebService.deudasSeleccionadas.size() > i; i++) {

                        final ClienteCobranza instaDeuda = WebService.deudasSeleccionadas.get(i);

                        tr2 = new TableRow(this);

                        String nomCod = instaDeuda.getCod_dpto();
                        String nomSer = instaDeuda.getSerie_docum();
                        String nomNro = instaDeuda.getNro_Docum();
                        double total = instaDeuda.getTotalEntregado();
                        double descuento = instaDeuda.getDescuentoAgregado();
                        String simb = instaDeuda.getSimMoneda();

                        TextView nombreItem = new TextView(this);
                        TextView totalItem = new TextView(this);
                        TextView desc = new TextView(this);

                        if (instaDeuda.getCod_Moneda().trim().equals("1")) {
                            total = Utilidad.redondearDecimales(total, 0);
                            descuento = Utilidad.redondearDecimales(descuento, 0);

                            nombreItem.setText(nomCod.trim() + " - " + nomSer + " - " + nomNro);
                            totalItem.setText(simb + " " + NumberFormat.getInstance(Locale.ITALY).format(total).toString());

                            if(descuento >0) {
                                desc.setText(getResources().getString(R.string.DTO) + simb + " " + NumberFormat.getInstance(Locale.ITALY).format(descuento).toString());
                            }
                            else {
                                desc.setVisibility(View.GONE);
                            }
                        } else {
                            total = Utilidad.redondearDecimales(total, 2);
                            descuento = Utilidad.redondearDecimales(descuento, 2);

                            nombreItem.setText(nomCod.trim() + " - " + nomSer + " - " + nomNro);
                            totalItem.setText(simb + " " + String.valueOf(total));

                            if(descuento > 0) {
                                desc.setText(getResources().getString(R.string.DTO) + simb + " " + String.valueOf(descuento));
                            }
                            else {
                                desc.setVisibility(View.GONE);
                            }
                        }

                        nombreItem.setPadding(10, 5, 20, 5);

                        totalItem.setPadding(20, 5, 0, 5);
                        totalItem.setGravity(Gravity.END);

                        desc.setPadding(30, 5, 0, 5);
                        desc.setGravity(Gravity.END);

                        tr2.addView(nombreItem);
                        tr2.addView(totalItem);
                        tr2.addView(desc);

                        TablaFacAfec.addView(tr2);
                    }

                    TextView valPago = new TextView(this);
                    valPago.setText(getResources().getString(R.string.ValoresPago));
                    valPago.setTextSize(15);
                    valPago.setTextColor(Color.parseColor("#ffffff"));
                    valPago.setPadding(10, 5, 0, 5);
                    tr3.addView(valPago);
                    tr3.setBackgroundResource(R.color.colorPrimary);

                    TablaTitulo.addView(tl1);
                    TablaTitulo.addView(tr3);

                    TextView CodDocum = new TextView(this);
                    CodDocum.setText(getResources().getString(R.string.CodigoDocum));
                    CodDocum.setTextColor(Color.parseColor("#ffffff"));
                    CodDocum.setPadding(5, 10, 15, 10);

                    TextView NroDoc = new TextView(this);
                    NroDoc.setText(getResources().getString(R.string.NumeroDocum));
                    NroDoc.setTextColor(Color.parseColor("#ffffff"));
                    NroDoc.setPadding(5, 10, 15, 10);

                    TextView CodMon = new TextView(this);
                    CodMon.setText(getResources().getString(R.string.CodigoMon));
                    CodMon.setTextColor(Color.parseColor("#ffffff"));
                    CodMon.setPadding(5, 10, 15, 10);

                    TextView Importe = new TextView(this);
                    Importe.setText(getResources().getString(R.string.Importe));
                    Importe.setTextColor(Color.parseColor("#ffffff"));
                    Importe.setPadding(5, 10, 15, 10);

                    tr4.addView(CodDocum);
                    tr4.addView(NroDoc);
                    tr4.addView(CodMon);
                    tr4.addView(Importe);
                    tr4.setBackgroundResource(R.color.colorPrimary);

                    TablaValores.addView(tl2);
                    TablaValores.addView(tr4);

                    for (int x = 0; WebService.valoresPago.size() > x; x++) {

                        tr5 = new TableRow(this);

                        final ValoresPago instaValores = WebService.valoresPago.get(x);

                        final TextView codDocum = new TextView(this);
                        instaValores.getCod_docum().trim();
                        codDocum.setText(WebService.valoresPago.get(x).getCod_docum().trim());
                        codDocum.setGravity(Gravity.LEFT);

                        final TextView nroDoc = new TextView(this);
                        instaValores.getNro_docum();
                        String numDocum = Utilidad.GenerarFormato(WebService.valoresPago.get(x).getNro_docum());
                        numDocum = Utilidad.NumeroSinPunto(numDocum);
                        nroDoc.setText(numDocum);
                        nroDoc.setGravity(Gravity.LEFT);

                        final TextView codMon = new TextView(this);
                        instaValores.getCod_moneda();

                        final TextView importe = new TextView(this);
                        instaValores.getImporte();
                        String importes = NumberFormat.getInstance(Locale.ITALY).format(WebService.valoresPago.get(x).getImporte());

                        if (WebService.valoresPago.get(x).getCod_moneda().trim().equals(WebService.codMonedaNacional) || WebService.valoresPago.get(x).getCod_moneda().trim().equals("") /*|| WebService.valoresPago.get(x).getCod_docum().trim().equals("efectivo") */ || WebService.valoresPago.get(x).getCod_docum().trim().equals("reten")) {
                            codMon.setText(WebService.simboloMonedaNacional);
                        } else {
                            codMon.setText(WebService.simboloMonedaTr);
                            if(WebService.valoresPago.get(x).getCod_docum().equals("cresusp")){
                                importes = NumberFormat.getInstance(Locale.ITALY).format(WebService.valoresPago.get(x).getImporteUSD());
                            }
                        }

                        codMon.setGravity(Gravity.LEFT);

                        importe.setText(importes);

                        importe.setGravity(Gravity.RIGHT);

                        tr5.addView(codDocum);
                        tr5.addView(nroDoc);
                        tr5.addView(codMon);
                        tr5.addView(importe);
                        tr5.setTag(x);
                        TablaValores.addView(tr5);

                    }
            }catch (Exception ex) {
                ex.printStackTrace();
                Intent myIntent = new Intent(context, Login.class);
                startActivity(myIntent);
            }
        }else {
            Intent myIntent = new Intent(context, Login.class);
            startActivity(myIntent);
        }
    }

    private class AgregarCobranza extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog(context);
        public AsyncResponse delegate = null;//Call back interface

        public AgregarCobranza(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                if (Utilidad.isNetworkAvailable()) {

                    if(WebService.configuracion.getGraba_logws().equals("S")){
                        Utilidad.graba_logws(String.valueOf(paramsIng), numeroDoc, "Ing.Cob - Recibo");
                    }

                    WebService.IngresarCobranza("Cobranzas/IngresarCobranza.php", paramsIng);
                    return null;
                } else {
                    Utilidad.dispalyAlertConexion(context);
                }
            }catch (Exception ex){
                ex.printStackTrace();
               // Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
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
        protected void onProgressUpdate(Void... values) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (WebService.reto_AgregaCobranza.equals("ok")) {

                if(WebService.usuarioActual.getEs_Entrega().equals("N")) {
                    for (int i = 0; i < WebService.deudasSeleccionadas.size(); i++) {
                        ClienteCobranza cli = new ClienteCobranza();
                        cli = WebService.deudasSeleccionadas.get(i);
                        if (cli.getCalcula_interes().equals("S")) {
                            WebService.calula_interes = "S";
                            break;
                        }
                    }
                }

                WebService.deudasSeleccionadas.clear();
                //WebService.deudas.clear();
                WebService.deudasViaje.clear();
                WebService.totalDeudas = 0D;
                WebService.limpiarValores();

                //Se envia a factura si la deuda es con interes
                if (WebService.calula_interes.equals("S")) {
                    CalculaInteres task2 = new CalculaInteres(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            if (!WebService.errToken.equals("")) {
                                Intent myIntent = new Intent(context, Login.class);
                                startActivity(myIntent);
                            } else {
                                ObtenerNumRecomendado task1 = new ObtenerNumRecomendado(new AsyncResponse() {
                                    @Override
                                    public void processFinish(Object output) {
                                        if (!WebService.errToken.equals("")) {
                                            Intent myIntent = new Intent(context, Login.class);
                                            startActivity(myIntent);
                                        } else {
                                            WebService.calula_interes = "N";
                                            Intent myIntent = new Intent(context, FacturaCobranza.class);
                                            myIntent.putExtra("intent", "Recibo");
                                            startActivity(myIntent);
                                        }
                                    }
                                });
                                task1.execute();
                            }
                        }
                    });
                    task2.execute();
                } else {

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
                    WebService.lat_origen = latitude;
                    WebService.long_origen = longitude;

                    if (WebService.usuarioActual.getTipoCobrador().equals("L") || WebService.usuarioActual.getTipoCobrador().equals("F")) {
                        retorno.setVisibility(View.VISIBLE);
                        retorno.setText(getResources().getString(R.string.reto_Rec));
                        System.out.println("1");
                        if (Utilidad.isNetworkAvailable()) {

                            WebService.reto_AgregaCobranza = "";
                            retorno.setText("");
                            retorno.setVisibility(View.GONE);
                            TablaFacAfec.removeAllViews();
                            TablaValores.removeAllViews();

                            WebService.EstadoActual = 0;
                            LatiLong();

                            String nro_viaje = "0";
                            params = new RequestParams();
                            if(WebService.usuarioActual.getTipoCobrador().trim().equals("D")) {
                                nro_viaje = WebService.viajeSeleccionadoCobrador.getNumViaje().trim();
                                params.put("usuario", WebService.USUARIOLOGEADO);//1
                                params.put("latitud_actual", String.valueOf(WebService.lat_actual));
                                params.put("longitud_actual", String.valueOf(WebService.long_actual));
                                params.put("latitud_destino", String.valueOf(WebService.clienteActual.getLatiud_Ubic()));
                                params.put("longitud_destino", String.valueOf(WebService.clienteActual.getLongitud_Ubic()));
                                params.put("latitud_origen", String.valueOf(WebService.lat_origen));
                                params.put("longitud_origen", String.valueOf(WebService.long_origen));
                                params.put("en_pausa", String.valueOf(WebService.EstadoActual));//12
                                params.put("nro_viaje", nro_viaje);//9
                                params.put("cod_sucursal", "0");//10
                                params.put("nro_orden", "0");//13
                                params.put("nom_cliente", WebService.clienteActual.getNom_Tit().trim());//11
                                params.put("distancia_a_recorrer", WebService.distancia_a_recorrer);
                            }else{
                                params.put("usuario", WebService.USUARIOLOGEADO);//1
                                params.put("latitud_actual", String.valueOf(WebService.lat_actual));
                                params.put("longitud_actual", String.valueOf(WebService.long_actual));
                                params.put("latitud_destino", String.valueOf(WebService.clienteActual.getLatiud_Ubic()));
                                params.put("longitud_destino", String.valueOf(WebService.clienteActual.getLongitud_Ubic()));
                                params.put("latitud_origen", String.valueOf(WebService.lat_origen));
                                params.put("longitud_origen", String.valueOf(WebService.long_origen));
                                params.put("en_pausa", String.valueOf(WebService.EstadoActual));//12
                                params.put("nro_viaje", nro_viaje);//9
                                params.put("cod_sucursal", "0");//10
                                params.put("nro_orden", "0");//13
                                params.put("nom_cliente",WebService.clienteActual.getNom_Tit().trim());//11
                                params.put("distancia_a_recorrer", WebService.distancia_a_recorrer);
                            }
                            ActualizarUbicacion actuUbic = new ActualizarUbicacion();
                            actuUbic.execute();

                            Intent myIntent = new Intent(context, SeleccionCliente.class);
                            startActivity(myIntent);

                        }else{
                            Utilidad.CargarToastConexion(context);
                        }
                    }
                    else if (WebService.usuarioActual.getTipoCobrador().equals("D")) {
                        retorno.setVisibility(View.VISIBLE);
                        retorno.setText(getResources().getString(R.string.reto_Rec));
                        if (Utilidad.isNetworkAvailable()) {
                            otraDeuda = false;
                            try {
                                TraerClientesViajes task3 = new TraerClientesViajes(new AsyncResponse() {
                                    @Override
                                    public void processFinish(Object output) {
                                        if (!WebService.errToken.equals("")) {
                                            Intent myIntent = new Intent(context, Login.class);
                                            startActivity(myIntent);
                                        } else {
                                            for (ClienteCobranza obj : WebService.clienteTraidos) {
                                                if (obj.getCod_Tit_Gestion().equals(WebService.clienteActual.getCod_Tit_Gestion()) && !(obj.getCodEmp().trim().equals(WebService.clienteActual.getCodEmp().trim()))) {
                                                    otraDeuda = true;
                                                    WebService.clienteActual = obj;
                                                    TraerDeudasViaje task2 = new TraerDeudasViaje(new AsyncResponse() {
                                                        @Override
                                                        public void processFinish(Object output) {
                                                            if (!WebService.errToken.equals("")) {
                                                                Intent myIntent = new Intent(context, Login.class);
                                                                startActivity(myIntent);
                                                            } else {
                                                                Intent myIntent = new Intent(context, SeleccionarDeudas.class);
                                                                startActivity(myIntent);
                                                            }
                                                        }
                                                    });
                                                    task2.execute();
                                                }
                                            }
                                            if (otraDeuda == false) {
                                                CargarViajesCobrador task1 = new CargarViajesCobrador(new AsyncResponse() {
                                                    @Override
                                                    public void processFinish(Object output) {
                                                        if (!WebService.errToken.equals("")) {
                                                            Intent myIntent = new Intent(context, Login.class);
                                                            startActivity(myIntent);
                                                        } else {
                                                            WebService.reto_AgregaCobranza = "";
                                                            retorno.setText("");
                                                            retorno.setVisibility(View.GONE);
                                                            TablaFacAfec.removeAllViews();
                                                            TablaValores.removeAllViews();
                                                            WebService.limpiarValores();

                                                            WebService.EstadoActual = 0;
                                                            LatiLong();

                                                            String nro_viaje = "0";
                                                            params = new RequestParams();
                                                            if (WebService.usuarioActual.getTipoCobrador().trim().equals("D")) {
                                                                nro_viaje = WebService.viajeSeleccionadoCobrador.getNumViaje().trim();
                                                                params.put("usuario", WebService.USUARIOLOGEADO);//1
                                                                params.put("latitud_actual", String.valueOf(WebService.lat_actual));
                                                                params.put("longitud_actual", String.valueOf(WebService.long_actual));
                                                                params.put("latitud_destino", String.valueOf(WebService.clienteActual.getLatiud_Ubic()));
                                                                params.put("longitud_destino", String.valueOf(WebService.clienteActual.getLongitud_Ubic()));
                                                                params.put("latitud_origen", String.valueOf(WebService.lat_origen));
                                                                params.put("longitud_origen", String.valueOf(WebService.long_origen));
                                                                params.put("en_pausa", String.valueOf(WebService.EstadoActual));//12
                                                                params.put("nro_viaje", nro_viaje);//9
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
                                                                params.put("nro_viaje", nro_viaje);//9
                                                                params.put("cod_sucursal", "0");//10
                                                                params.put("nro_orden", "0");//13
                                                                params.put("nom_cliente", WebService.clienteActual.getNom_Tit().trim());//11
                                                                params.put("distancia_a_recorrer", WebService.distancia_a_recorrer);
                                                            }
                                                            ActualizarUbicacion actuUbic = new ActualizarUbicacion();
                                                            actuUbic.execute();

                                                            if (WebService.viajesCobradorUsu.size() < 0) {
                                                                Intent myIntent = new Intent(context, Login.class);
                                                                startActivity(myIntent);
                                                            } else {
                                                                WebService.recibo_class = true;
                                                                Intent myIntent = new Intent(context, MenuCobranzas.class);
                                                                startActivity(myIntent);
                                                            }
                                                        }
                                                    }
                                                });
                                                task1.execute();
                                            }
                                        }
                                    }
                                });
                                task3.execute();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                            }
                        }
                    } else {
                        retorno.setVisibility(View.GONE);
                        Utilidad.CargarToastConexion(context);
                    }
                }
            } else {
                retorno.setVisibility(View.VISIBLE);
                retorno.setText(WebService.reto_AgregaCobranza);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (aceptaRec == false) {
            Utilidad.vibraticionBotones(context);
            try {
                Utilidad.vibraticionBotones(context);
                TablaFacAfec.removeAllViews();
                Intent myIntent = new Intent(context, IngresarValores.class);
                startActivity(myIntent);
            } catch (Exception ex) {
                ex.printStackTrace();
                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
            }
        }
    }

    private class ValidarRecibo extends AsyncTask<String,Void,Void> {
        public AsyncResponse delegate = null;//Call back interface
        public ValidarRecibo(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... strings) {
            //params = new RequestParams(  );
            WebService.ValidarRecibo( "Cobranzas/ValidarRecibo.php",params );
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

    private class CargarViajesCobrador extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface
        public CargarViajesCobrador(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            params1 = new RequestParams(  );
            params1.put( "username",WebService.usuarioActual.getNombre().trim());
            WebService.TraerViajesCobrador(params1,"Viajes/ViajesCobrador/ViajesCobrador.php");
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

    private class GuardarDestino extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface

        public GuardarDestino(AsyncResponse asyncResponse) {
            delegate = asyncResponse;
        }

        @Override
        protected Void doInBackground(String... strings) {
            params = new RequestParams();
            params.put("lat_actual", String.valueOf(WebService.lat_actual));
            params.put("long_actual", String.valueOf(WebService.long_actual));
            params.put("cod_tit", WebService.clienteActual.getCod_Tit_Gestion());
            WebService.GuardarDestino( params, "Viajes/ViajesCobrador/GuardarDestino.php" );
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

    private class  TraerClientesViajes extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context);
        public AsyncResponse delegate = null;//Call back interface
        public TraerClientesViajes(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            if(Utilidad.isNetworkAvailable()) {
                params2.put("num_viaje", WebService.viajeSeleccionadoCobrador.getNumViaje());
                params2.put("username", WebService.USUARIOLOGEADO);
                WebService.TraerClientesViajes(params2, "Viajes/ViajesCobrador/TraerClientesViajes.php");
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
        protected void onProgressUpdate(Void... values){        }
    }

    private class TraerDeudasViaje extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface
        public TraerDeudasViaje(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            /*params4 = new RequestParams(  );
            params4.put("cod_tit", WebService.clienteActual.getCod_Tit_Gestion().trim());
            params4.put("num_viaje", WebService.viajeSeleccionadoCobrador.getNumViaje());
            params4.put("cod_emp",WebService.clienteActual.getCodEmp().trim());*/
            WebService.TraerDeudasViaje(/*params4*/);
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

    private class ObtenerNumRecomendado extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog(context);
        public AsyncResponse delegate = null;//Call back interface

        public ObtenerNumRecomendado(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {
            if (Utilidad.isNetworkAvailable()) {
                RequestParams params2 = new RequestParams();
                params2.put("username", WebService.USUARIOLOGEADO);
                params2.put("cod_emp", WebService.clienteActual.getCodEmp().trim());
                WebService.RecomendarNumeroFactura("Facturas/RecomendarNumero.php", params2);
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
        protected void onProgressUpdate(Void... values) {
        }
    }

   private class CalculaInteres extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog(context);
        public AsyncResponse delegate = null;//Call back interface

        public CalculaInteres(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {
            if (Utilidad.isNetworkAvailable()) {
                RequestParams params2 = new RequestParams();
                params2.put("cod_tit", WebService.clienteActual.getCod_Tit_Gestion().trim());
                params2.put("cod_emp", WebService.clienteActual.getCodEmp().trim());
                WebService.CalculaInteres("Facturas/CalculoInteres.php", params2);
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
        protected void onProgressUpdate(Void... values) {
        }
    }

    protected void displayAlertUbicacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Recibo.this, R.style.myDialog));
        builder.setMessage(getResources().getString(R.string.GuardaUbi)).setCancelable(
                false).setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        GuardarDestino task = new GuardarDestino(new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) {
                                if (!WebService.errToken.equals("")) {
                                    Intent myIntent = new Intent(context, Login.class);
                                    startActivity(myIntent);
                                }else {
                                    if (WebService.respuesta.equals("")) {
                                        Toast toast = Toast.makeText(context, getResources().getString(R.string.errorCierre) + "\n" + WebService.respuesta, Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                                        toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                                        toast.show();//showing the toast is important***/
                                    } else {
                                        WebService.clienteActual.setLatiud_Ubic(WebService.lat_actual);
                                        WebService.clienteActual.setLongitud_Ubic(WebService.long_actual);
                                        Toast toast = Toast.makeText(context, getResources().getString(R.string.datoGuardo), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                                        toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                                        toast.show();//showing the toast is important***/
                                    }
                                }
                            }
                        });
                        task.execute();
                    }
                }).setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alerta = builder.create();
        alerta.show();
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

        //CIERRO CONEXION EN EL DISPLAYALERTIMPR POR LA COPIA PARA NO TENER QUE HACER LA CONEXION NUEVAMENTE... BDL 30/12/2019
        /*contextoImpre.getObject().CON_CloseDevices(contextoImpre.getState());*/
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

    private class  TraerDatoImpresion extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog(context);
        public AsyncResponse delegate = null;//Call back interface
        public TraerDatoImpresion(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(Void... voids) {
            if(Utilidad.isNetworkAvailable()) {
                RequestParams params4 = new RequestParams();
                params4.add("nrotrans", WebService.nro_transRec.toString());
                params4.add("nom_copia", nom_copia);
                WebService.BuscarDatosImpresora("Impresion/printRecibo.php", params4);
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

   /* private class ActualizarUbicacion extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface
        public ActualizarUbicacion(AsyncResponse asyncResponse) {
            delegate = asyncResponse;
        }
        @Override
        protected Void doInBackground(String... strings) {
            WebService.ActualizarUbicacion( params4, "Viajes/ActualizarUbicacion.php" );
            return null;
        }
        @Override
        public void onPreExecute() {
        }
        @Override
        protected void onPostExecute(Void result) {
            delegate.processFinish( WebService.logueado);
            Utilidad.deleteLoadingMessage();
        }
    }*/


    private class ActualizarUbicacion extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... strings) {
            WebService.ActualizarUbicacion( params,"Viajes/ActualizarUbicacion.php" );
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {}
    }

    public void LatiLong(){
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

        WebService.lat_origen = latitude;
        WebService.long_origen = longitude;
    }

    public void Impr(){
        try {
            String str = WebService.respuestaWSImpresora;
            System.out.println(str);
            Imprimir(str);
        } catch (Exception e) {
            e.printStackTrace();
           // Toast.makeText( getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }

    protected void displayAlertImpr() {
        AlertDialog.Builder builder = new AlertDialog.Builder( context );

        builder.setMessage(getResources().getString(R.string.imprCopia)).setCancelable(
                false).setPositiveButton(getResources().getString(R.string.aceptar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Utilidad.vibraticionBotones(context);
                            nom_copia = "Copia";
                            TraerDatoImpresion task2 = new TraerDatoImpresion(new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {
                                    if (!WebService.errToken.equals("")) {
                                        Intent myIntent = new Intent(context, Login.class);
                                        startActivity(myIntent);
                                    }else {
                                        Impr();
                                        contextoImpre.getObject().CON_CloseDevices(contextoImpre.getState());
                                        onResume();
                                    }
                                }
                            });
                            task2.execute();
                        } catch (Exception exc) {
                        }
                    }
                }).setNegativeButton( getResources().getString(R.string.Cancelar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Utilidad.vibraticionBotones(context);
                        dialog.cancel();
                        contextoImpre.getObject().CON_CloseDevices(contextoImpre.getState());
                        onResume();
                    }
                });
        AlertDialog alerta1 = builder.create();
        alerta1.show();
    }
}
