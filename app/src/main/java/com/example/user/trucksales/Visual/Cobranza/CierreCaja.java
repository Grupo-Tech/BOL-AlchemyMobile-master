package com.example.user.trucksales.Visual.Cobranza;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.print.PrintManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.CajaCobranza;
import com.example.user.trucksales.Encapsuladoras.ValoresPago;
import com.example.user.trucksales.Encapsuladoras.Viaje;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Printer;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.loopj.android.http.RequestParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CierreCaja extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static Context contexto;

    private Utilidades Utilidad;
    TextView nombreUsu,fecha,
            Cheques,Transferencias,TarjetaCredito,TarjetaDebito,Retenciones,
            faltaDls, faltaEfect, retorno, toolbar_titlePausa,
            ChequesUSD, TransferenciasUSD, TarjetaCreditoUSD, TarjetaDebitoUSD, RetencionesUSD;
    LinearLayout titleEfectivo, titleEfectivoUSD, titleCheques,TitleTransferencias,TitleTarjetaCredito,TitleTarjetaDebito,TitleRetenciones;
    ImageView atras;

    Spinner spinner;
    List<String> spinnerArray = new ArrayList<>();

    LinearLayout mainLayout;
    private static EditText Efectivo, EfectivoUSD;

    private Button btnAceptar;

    private double val_totCheque = 0D;
    private double val_totChequeUSD = 0D;
    private double val_totReten = 0D;;
    private double val_totRetenUSD = 0D;;
    private double val_totCred = 0D;
    private double val_totCredUSD = 0D;
    private double val_totDeb = 0D;
    private double val_totDebUSD = 0D;
    private double val_totTran = 0D;
    private double val_totTranUSD = 0D;

    private double numeroEfect;
    private double numeroEfectUSD;

    private String numViaje = "0";

    private String cod_caja;

    private double totalEfecEntrega;
    private double totalEfectUSDEntrega;

    View actualView;

    protected static RequestParams paramsIng = new RequestParams(  );

    @Override
    protected void onResume() {
        super.onResume();
        if(WebService.reto_IngresarCierre.equals("ok")){
            if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("L") || WebService.usuarioActual.getTipoCobrador().equals("F")) {
                Intent myIntent = new Intent(contexto, SeleccionCliente.class);
                startActivity(myIntent);
                finish();
            } else if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D")) {
                Intent myIntent = new Intent(contexto, MenuCobranzas.class);
                startActivity(myIntent);
                finish();
            }
        }

        WebService.nro_transCierreCaja = 0;
        WebService.nro_trans_impresion = "0";
        WebService.reto_IngresarCierre = "";
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo ai= cm.getActiveNetworkInfo();
        return ai!= null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cierre_caja);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        contexto = this;
        GuardarDatosUsuario.Contexto = contexto;
        Utilidad = new Utilidades(contexto);
        actualView = new View(contexto);

        try {
            if (WebService.USUARIOLOGEADO != null) {
                if (Utilidad.isNetworkAvailable()) {

                    retorno = findViewById(R.id.Retorno);
                    retorno.setVisibility(View.GONE);

                    final String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                    nombreUsu = (TextView) findViewById(R.id.LblUsu);
                    fecha = (TextView) findViewById(R.id.LblFecha);
                    fecha.setText(timeStamp);
                    nombreUsu.setText(getResources().getString(R.string.usuarioTool) + WebService.USUARIOLOGEADO);
                    mainLayout = findViewById(R.id.mainLay);
                    toolbar_titlePausa = findViewById(R.id.toolbar_titlePausa);

                    atras = findViewById(R.id.btnAtras);
                    atras.setClickable(true);
                    atras.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(contexto);
                        /*if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("L") || WebService.usuarioActual.getTipoCobrador().equals("F")) {
                            Intent myIntent = new Intent(v.getContext(), SeleccionCliente.class);
                            startActivity(myIntent);
                            finish();
                        } else if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D")) {
                            Intent myIntent = new Intent(v.getContext(), MenuCobranzas.class);
                            startActivity(myIntent);
                            finish();
                        }*/
                            Intent myIntent = new Intent(v.getContext(), ConsultasCobrador.class);
                            startActivity(myIntent);
                            finish();
                        }
                    });

                    titleEfectivo = findViewById(R.id.titleEfectivo);
                    Efectivo = findViewById(R.id.Efectivo);
            /*if(Efectivo.getText().equals(0)){
                Efectivo.setText("");
            }*/

                    titleEfectivoUSD = findViewById(R.id.titleEfectivoUSD);
                    EfectivoUSD = findViewById(R.id.EfectivoUSD);

                    titleCheques = findViewById(R.id.titleCheques);
                    Cheques = findViewById(R.id.Cheques);
                    ChequesUSD = findViewById(R.id.ChequesUSD);

               /* TitleTransferencias = findViewById(R.id.TitleTransferencias);
                Transferencias = findViewById(R.id.Transferencias);
                TransferenciasUSD = findViewById(R.id.TransferenciasUSD);*/

                    TitleTarjetaCredito = findViewById(R.id.TitleTarjetaCredito);
                    TarjetaCredito = findViewById(R.id.TarjetaCredito);
                    TarjetaCreditoUSD = findViewById(R.id.TarjetaCreditoUSD);

                    TitleTarjetaDebito = findViewById(R.id.TitleTarjetaDebito);
                    TarjetaDebito = findViewById(R.id.TarjetaDebito);
                    TarjetaDebitoUSD = findViewById(R.id.TarjetaDebitoUSD);

                    TitleRetenciones = findViewById(R.id.TitleRetenciones);
                    Retenciones = findViewById(R.id.Retenciones);
                    RetencionesUSD = findViewById(R.id.RetencionesUSD);

                    //SE HABILITAN SOLO SI SE MODIFICA EL TOTAL
                    faltaEfect = findViewById(R.id.faltaEfect);
                    faltaEfect.setVisibility(View.GONE);
                    faltaDls = findViewById(R.id.faltaDls);
                    faltaDls.setVisibility(View.GONE);

                    btnAceptar = findViewById(R.id.btnAceptar);


                    spinner = (Spinner) findViewById(R.id.Caja);

                    if (WebService.configuracion.getCierre_app().equals("N")) {
                        spinner.setVisibility(View.GONE);
                        btnAceptar.setVisibility(View.GONE);
                        toolbar_titlePausa.setText(getResources().getString(R.string.NoCierre));
                    } else {
                        btnAceptar.setClickable(true);

                        toolbar_titlePausa.setText(getResources().getString(R.string.CierreCaja));

                        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, spinnerArray);
                        spinnerArray.add(getResources().getString(R.string.SeleccCaja));
                        for (int i = 0; i < WebService.ArrayCajasCobranza.size(); i++) {
                            String nombreAgregar = WebService.ArrayCajasCobranza.get(i).getNom_caja();
                            spinnerArray.add(nombreAgregar);
                        }
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(dataAdapter);


                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String cajaSelec = spinner.getSelectedItem().toString();
                                CajaCobranza instanciaCajaCobranza = new CajaCobranza();
                                for (int x = 0; x < WebService.ArrayCajasCobranza.size(); x++) {
                                    instanciaCajaCobranza = WebService.ArrayCajasCobranza.get(x);
                                    if (instanciaCajaCobranza.getNom_caja().trim().equals(cajaSelec.trim())) {
                                        cod_caja = instanciaCajaCobranza.getCod_caja().trim();
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                    for (int i = 0; i < WebService.ArrayValores.size(); i++) {
                        ValoresPago valor = WebService.ArrayValores.get(i);

                        if (valor.getEstadoEntregado() == 0) {
                            double valor2 = valor.getImporte();

                            if (valor.getCod_docum().equals("efectivo")) {
                                WebService.totalEfec = valor2;
                                numeroEfect = valor2;
                                Efectivo.setText(String.valueOf(Utilidad.GenerarFormato2(WebService.totalEfec)));
                            }
                            if (valor.getCod_docum().equals("efectUSD")) {
                                WebService.totalEfecUSD = valor2;
                                numeroEfectUSD = valor2;
                                EfectivoUSD.setText(String.valueOf(Utilidad.GenerarFormato2(WebService.totalEfecUSD)));
                            }
                            if (valor.getCod_docum().equals("cheque")) {
                                if (valor.getCod_moneda().equals("1")) {
                                    val_totCheque = valor2 + val_totCheque;
                                    WebService.totalCheque$ = val_totCheque;
                                    Cheques.setText(WebService.simboloMonedaNacional + "\n" + Utilidad.GenerarFormato2(WebService.totalCheque$));
                                } else {
                                    val_totChequeUSD = valor2 + val_totChequeUSD;
                                    WebService.totalChequeUSD = val_totChequeUSD;
                                    ChequesUSD.setText("U$S " + "\n" + Utilidad.GenerarFormato2(WebService.totalChequeUSD));
                                }
                            }
                            if (valor.getCod_docum().equals("reten")) {

                                if (valor.getCod_moneda().equals("1")) {
                                    val_totReten = valor2 + val_totReten;
                                    WebService.totalReten = val_totReten;
                                    Retenciones.setText(WebService.simboloMonedaNacional + "\n" + Utilidad.GenerarFormato2(WebService.totalReten));
                                } else {
                                    val_totRetenUSD = valor2 + val_totRetenUSD;
                                    WebService.totalRetenUSD = val_totRetenUSD;
                                    RetencionesUSD.setText("U$S " + "\n" + Utilidad.GenerarFormato2(WebService.totalRetenUSD));
                                }
                            }
                            if (valor.getCod_docum().equals("tarjCred")) {
                                if (valor.getCod_moneda().equals("1")) {
                                    val_totCred = valor2 + val_totCred;
                                    WebService.totalCred = val_totCred;
                                    TarjetaCredito.setText(WebService.simboloMonedaNacional + "\n" + Utilidad.GenerarFormato2(WebService.totalCred));
                                } else {
                                    val_totCredUSD = valor2 + val_totCredUSD;
                                    WebService.totalCredUSD = val_totCredUSD;
                                    TarjetaCreditoUSD.setText("U$S " + "\n" + Utilidad.GenerarFormato2(WebService.totalCredUSD));
                                }
                            }

                            if (valor.getCod_docum().equals("tarjDeb")) {
                                if (valor.getCod_moneda().equals("1")) {
                                    val_totDeb = valor2 + val_totDeb;
                                    WebService.totalDebi = val_totDeb;
                                    TarjetaDebito.setText(WebService.simboloMonedaNacional + "\n" + Utilidad.GenerarFormato2(WebService.totalDebi));
                                } else {
                                    val_totDebUSD = valor2 + val_totDebUSD;
                                    WebService.totalDebiUSD = val_totDebUSD;
                                    TarjetaDebitoUSD.setText("U$S " + "\n" + Utilidad.GenerarFormato2(WebService.totalDebiUSD));
                                }
                            }

                      /*  if (valor.getCod_docum().equals("tranfb")) {
                            if (valor.getCod_moneda().equals("1")) {
                                val_totTran = valor2 + val_totTran;
                                WebService.totalTran = val_totTran;
                                Transferencias.setText(WebService.simboloMonedaNacional + " " + Utilidad.GenerarFormato2(WebService.totalTran));
                            } else {
                                val_totTranUSD = valor2 + val_totTranUSD;
                                WebService.totalTranUSD = val_totTranUSD;
                                TransferenciasUSD.setText("U$S " + Utilidad.GenerarFormato2(WebService.totalTranUSD));
                            }
                        }*/
                        }
                    }

                    Efectivo.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            try {
                                numeroEfect = Double.parseDouble(Efectivo.getText().toString());
                                faltaEfect.setVisibility(View.VISIBLE);
                                if (numeroEfect == 0) {
                                    faltaEfect.setText(getResources().getText(R.string.IngreseTot) + Utilidad.GenerarFormato2(WebService.totalEfec));
                                    //Efectivo.setText(String.valueOf(Utilidad.GenerarFormato2(importeEfectivo)));
                                } else if (Double.valueOf(numeroEfect) > WebService.totalEfec) {
                                    faltaEfect.setText(getResources().getText(R.string.ImporteRec) + "\n" + getResources().getText(R.string.IngreseTot) + Utilidad.GenerarFormato2(WebService.totalEfec));
                                    // Efectivo.setText(String.valueOf(Utilidad.GenerarFormato2(WebService.totalEfec)));
                                } else {
                                    totalEfecEntrega = Double.valueOf(Efectivo.getText().toString());
                                    faltaEfect.setText((getResources().getString(R.string.Falta) + Utilidad.GenerarFormato2(WebService.totalEfec - totalEfecEntrega)));
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    EfectivoUSD.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            try {
                                numeroEfectUSD = Double.parseDouble(EfectivoUSD.getText().toString());
                                faltaDls.setVisibility(View.VISIBLE);
                                if (numeroEfectUSD == 0) {
                                    faltaDls.setText(getResources().getText(R.string.IngreseTot) + Utilidad.GenerarFormato2(WebService.totalEfecUSD));
                                } else if (Double.valueOf(numeroEfectUSD) > WebService.totalEfecUSD) {
                                    faltaDls.setText(getResources().getText(R.string.ImporteRec) + "\n" + getResources().getText(R.string.IngreseTot) + Utilidad.GenerarFormato2(WebService.totalEfecUSD));
                                    //EfectivoUSD.setText(String.valueOf(Utilidad.GenerarFormato2(WebService.totalEfecUSD)));
                                } else {
                                    totalEfectUSDEntrega = Double.valueOf(EfectivoUSD.getText().toString());
                                    faltaDls.setText((getResources().getString(R.string.Falta) + Utilidad.GenerarFormato2(WebService.totalEfecUSD - totalEfectUSDEntrega)));
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });


                    titleCheques.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (WebService.totalCheque$ > 0 || WebService.totalChequeUSD > 0) {
                                WebService.formapago = "cheque";
                                Intent myIntent = new Intent(contexto, ValoresRecibidos.class);
                                startActivity(myIntent);
                            } else {
                                Toast();
                            }
                        }
                    });

                    if (WebService.totalEfecUSD == 0.0) {
                        EfectivoUSD.setEnabled(false);
                    }

                    if (WebService.totalEfec == 0.0) {
                        Efectivo.setEnabled(false);
                    }

               /* TitleTransferencias.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (WebService.totalTran > 0 || WebService.totalTranUSD > 0) {
                            WebService.formapago = "tranfb";
                            Intent myIntent = new Intent(contexto, ValoresRecibidos.class);
                            startActivity(myIntent);
                        } else {
                            Toast();
                        }
                    }
                });*/
                    TitleTarjetaCredito.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (WebService.totalCred > 0 || WebService.totalCredUSD > 0) {
                                WebService.formapago = "tarjCred";
                                Intent myIntent = new Intent(contexto, ValoresRecibidos.class);
                                startActivity(myIntent);
                            } else {
                                Toast();
                            }
                        }
                    });
                    TitleTarjetaDebito.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (WebService.totalDebiUSD > 0 || WebService.totalDebi > 0) {
                                WebService.formapago = "tarjDeb";
                                Intent myIntent = new Intent(contexto, ValoresRecibidos.class);
                                startActivity(myIntent);
                            } else {
                                Toast();
                            }
                        }
                    });
                    TitleRetenciones.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (WebService.totalReten > 0 || WebService.totalRetenUSD > 0) {
                                WebService.formapago = "reten";
                                Intent myIntent = new Intent(contexto, ValoresRecibidos.class);
                                startActivity(myIntent);
                            } else {
                                Toast();
                            }
                        }
                    });

                    btnAceptar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Utilidad.vibraticionBotones(contexto);
                                if (Utilidad.isNetworkAvailable()) {

                            /*if (Efectivo.getText().equals("") || EfectivoUSD.getText().equals("")) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.IngreseDeud), Toast.LENGTH_LONG).show();
                            }else*/
                                    if (numeroEfect > WebService.totalEfec || numeroEfectUSD > WebService.totalEfecUSD) {
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Valmen), Toast.LENGTH_LONG).show();
                                    } else if (spinner.getSelectedItem().equals("Seleccionar Caja")) {
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Caja), Toast.LENGTH_LONG).show();
                                    } else {
                                        for (int i = 0; i < WebService.viajesCobradorUsu.size(); i++) {
                                            final Viaje cliente = WebService.viajesCobradorUsu.get(i);
                                            numViaje = cliente.getNumViaje();
                                            break;
                                        }
                                        paramsIng = new RequestParams();

                                        String lineasPago = "";
                                        String fechvalor;
                                        String nuevoFormatoFecha = "";
                                        Date date = null;

                                        double linea = 0;

                                        for (int i = 0; WebService.ArrayValores.size() > i; i++) {
                                            ValoresPago instaValores = WebService.ArrayValores.get(i);

                                            if (instaValores.getEstadoEntregado() == 0) {

                                                linea++;

                                                try {
                                                    fechvalor = instaValores.getFec_valor();
                                                   // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                                                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                                    date = formatter.parse(fechvalor);

                                                } catch (ParseException ex) {
                                                    ex.printStackTrace();
                                                    //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                                                }

                                                //nuevoFormatoFecha = new SimpleDateFormat("dd/MM/yyyy").format(date);

                                                double importe;

                                                if (instaValores.getCod_docum().equals("efectivo")) {
                                                    importe = numeroEfect;
                                                } else if (instaValores.getCod_docum().equals("efectUSD")) {
                                                    importe = numeroEfectUSD;
                                                } else {
                                                    importe = instaValores.getImporte();
                                                }

                                                if (i != WebService.ArrayValores.size() - 1) {

                                                    lineasPago = lineasPago
                                                            + instaValores.getCod_docum().trim()
                                                            + "," + instaValores.getNro_docum()
                                                            + "," + instaValores.getCod_moneda().trim()
                                                            + "," + instaValores.getCod_banco().trim()
                                                            + "," + instaValores.getCod_sctatit().trim()
                                                            + "," + instaValores.getCod_tenedor().trim()
                                                            + "," + nuevoFormatoFecha
                                                            + "," + importe + ";";
                                                } else {
                                                    lineasPago = lineasPago
                                                            + instaValores.getCod_docum().trim()
                                                            + "," + instaValores.getNro_docum()
                                                            + "," + instaValores.getCod_moneda().trim()
                                                            + "," + instaValores.getCod_banco().trim()
                                                            + "," + instaValores.getCod_sctatit().trim()
                                                            + "," + instaValores.getCod_tenedor().trim()
                                                            + "," + nuevoFormatoFecha
                                                            + "," + importe;
                                                }
                                            }
                                        }

                                        paramsIng.add("fec_doc", timeStamp);
                                        paramsIng.add("cod_emp", WebService.usuarioActual.getEmpresa());
                                        paramsIng.add("usuario", WebService.USUARIOLOGEADO);

                                        // PARA PASAR VALORES DE PAGO
                                        paramsIng.add("cantidad_lineas_pago", String.valueOf(linea));
                                        paramsIng.add("lineasPago", lineasPago);
                                        paramsIng.add("cod_caja", String.valueOf(cod_caja));
                                        paramsIng.add("nro_viaje", String.valueOf(numViaje));

                                        AgregarValores task = new AgregarValores(new AsyncResponse() {
                                            @Override
                                            public void processFinish(Object output) {
                                                if (WebService.reto_IngresarCierre.equals("ok")) {
                                                    WebService.ArrayValores.clear();
                                                    if (WebService.nro_trans_impresion != null && !WebService.nro_trans_impresion.equals("0")) {
                                                        retorno.setVisibility(View.VISIBLE);
                                                        retorno.setText(getResources().getString(R.string.reto_Cierre));
                                                        //onResume();
                                                    Printer pr = new Printer();
                                                    PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
                                                    pr.setPrMang(printManager);
                                                    pr.setContx(contexto);
                                                    System.out.println(WebService.nro_trans_impresion);
                                                    pr.setValor(String.valueOf(WebService.nro_trans_impresion));
                                                    pr.setTipo("CC");
                                                    pr.genarPdf(pr);
                                                    } else {
                                                        retorno.setVisibility(View.VISIBLE);
                                                        retorno.setText(getResources().getString(R.string.errorCierre) + "\n" + WebService.reto_IngresarCierre);
                                                    }
                                                } else {
                                                    if (WebService.reto_IngresarCierre.toUpperCase().contains("JSON")) {
                                                        Intent myIntent = new Intent(contexto, Login.class);
                                                        startActivity(myIntent);
                                                    } else {
                                                        retorno.setVisibility(View.VISIBLE);
                                                        retorno.setText(getResources().getString(R.string.errorCierre) + "\n" + WebService.reto_IngresarCierre);
                                                    }
                                                }
                                            }
                                        });
                                        task.execute();
                                    }

                                } else {
                                    WebService.reto_IngresarCierre = "";
                                    Utilidad.CargarToastConexion(contexto);
                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                               /* Toast toast = Toast.makeText(contexto, getResources().getString(R.string.errorCierre) + ex.getMessage(), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                                toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                                toast.show();//showing the toast is important***/
                            }
                        }
                    });
                } else {
                    Intent myIntent = new Intent(contexto, Login.class);
                    startActivity(myIntent);
                }
            }else{
                Utilidad.CargarToastConexion(contexto);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }

    private class AgregarValores extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(contexto);
        public AsyncResponse delegate = null;

        public AgregarValores(AsyncResponse asyncResponse){
            delegate = asyncResponse;
        }

        @Override
        protected Void doInBackground(String... strings) {
            if(Utilidad.isNetworkAvailable())
            {
                WebService.IngresarCierre("Cobranzas/IngresarCierre.php", paramsIng);
                return null;
            }
            else{
                Utilidad.dispalyAlertConexion(contexto);
            }
            return null;
        }
        @Override
        public void onPreExecute() {
            Utilidad.showLoadingMessage();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            delegate.processFinish( WebService.logueado);
            Utilidad.deleteLoadingMessage();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try{
        Utilidad.vibraticionBotones(contexto);
        if(WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("L") || WebService.usuarioActual.getTipoCobrador().equals("F")) {
            Intent myIntent = new Intent(contexto, SeleccionCliente.class);
            startActivity(myIntent);
            finish();
        }
        else if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D")){
            Intent myIntent = new Intent(contexto, MenuCobranzas.class);
            startActivity(myIntent);
            finish();
        }
        WebService.ArrayValores.clear();
        WebService.totalCheque$ = 0D;
        WebService.totalChequeUSD = 0D;
        //WebService.totalTran = 0D;
        WebService.totalTranUSD = 0D;
        WebService.totalReten = 0D;
        WebService.totalRetenUSD = 0D;
        WebService.totalDebi = 0D;
        WebService.totalDebiUSD = 0D;
        WebService.totalCred = 0D;
        WebService.totalCredUSD = 0D;
        faltaDls.setVisibility(View.GONE);
        faltaEfect.setVisibility(View.GONE);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void Toast(){
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Noexiste), Toast.LENGTH_LONG).show();
    }



}
