package com.example.user.trucksales.Visual.TruckSales;


import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Encapsuladoras.FacturaItemZebra;
import com.example.user.trucksales.Encapsuladoras.FacturaZebra;
import com.example.user.trucksales.Encapsuladoras.ValoresPago;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.loopj.android.http.RequestParams;
import com.szsicod.print.io.BluetoothAPI;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.BluetoothConnectionInsecure;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class IngresarValoresTruckSales extends AppCompatActivity {

    private static Context context;
    RequestParams paramsIng = new RequestParams();
    private Utilidades Utilidad;
    TextView nombreUsu,fecha, tipoC, ClienteSeleccionado, inputTV,
            Efectivo,EfectivoUSD, Dif,Vuelto,Cheques,Transferencias,TarjetaCredito,TarjetaDebito,Retenciones,
            total,diferencia,acuenta, Cliente, CreditoSus;
    LinearLayout titleEfectivo, TitleCredSus, TitleVuelto, titleEfectivoUSD, titleCheques,TitleDif, TitleTransferencias,TitleTarjetaCredito,TitleTarjetaDebito,TitleRetenciones;
    private static double val_tot=0;
    ImageView atras;
    private static EditText input;
    private  boolean otraDeuda;
    private static int cont_filas = 0;

    public String nro_viaje = "0";

    private static int dato = 0;

    private Button btnValores;
    public static String valorIntent;

    private static Double total2;
    private static String nro_viaje1 = "0";
    private static String lineasPago = "";
    private static String lineasDeudaSelec = "";
    private String fac;

    RequestParams params2 = new RequestParams();
    RequestParams params1 = new RequestParams();
    RequestParams params = new RequestParams();
    private String Nro_tran="";


    private Spinner spinnerBTDeviceList;
    private ArrayList<String> mBTAddrList = new ArrayList<String>();

    private Connection printerConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  this.requestWindowFeature( Window.FEATURE_NO_TITLE );

        setContentView(R.layout.activity_ingresar_valores_trucksales);

        final String nro_viaje = "0";

        context = this;
        Utilidad = new Utilidades(context);

        GuardarDatosUsuario.Contexto = context;

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        if (WebService.usuarioActual != null) {
            try{
                if(Utilidad.isNetworkAvailable()){


                    titleEfectivo = findViewById(R.id.titleEfectivo);
                    Efectivo = findViewById(R.id.Efectivo);

                    titleEfectivoUSD = findViewById(R.id.titleEfectivoUSD);
                    EfectivoUSD = findViewById(R.id.EfectivoUSD);

                    tipoC = findViewById(R.id.tipoC);

                    titleCheques = findViewById(R.id.titleCheques);
                    Cheques = findViewById(R.id.Cheques);

                    TitleTransferencias = findViewById(R.id.TitleTransferencias);
                    Transferencias = findViewById(R.id.Transferencias);

                    TitleTarjetaCredito = findViewById(R.id.TitleTarjetaCredito);
                    TarjetaCredito = findViewById(R.id.TarjetaCredito);

                    TitleTarjetaDebito = findViewById(R.id.TitleTarjetaDebito);
                    TarjetaDebito = findViewById(R.id.TarjetaDebito);

                    TitleRetenciones = findViewById(R.id.TitleRetenciones);
                    Retenciones = findViewById(R.id.Retenciones);

                    TitleVuelto = findViewById(R.id.TitleVuelto);
                    Vuelto = findViewById(R.id.Vuelto);

                    total = findViewById(R.id.total);
                    diferencia = findViewById(R.id.diferencia);
                    acuenta = findViewById(R.id.acuenta);
                    ClienteSeleccionado = findViewById( R.id.ClienteSeleccionado );

                    TitleCredSus = findViewById(R.id.TitleCredSus);
                    CreditoSus = findViewById(R.id.CreditoSus);

                    TitleDif = findViewById(R.id.TitleDif);
                    Dif = findViewById(R.id.Dif);

                    btnValores = findViewById(R.id.btnValores);

                    atras = findViewById(R.id.btnAtras);
                    atras.setClickable(true);


                    /* IMPRESORA */
                    spinnerBTDeviceList = (Spinner) findViewById(R.id.spinner_bt_device_list);

                    BluetoothAPI io = null;
                    io = new BluetoothAPI(this);
                    mBTAddrList.clear();
                    if (io.isBTSupport() == true && io.openBluetooth(15000)) {
                        Set<BluetoothDevice> setL = io.getBondedDevices();
                        if (setL != null && false == setL.isEmpty()) {
                            for (BluetoothDevice bd : setL) {
                                mBTAddrList.add(bd.getAddress());
                            }
                        }
                    }
                    if (mBTAddrList.isEmpty() == true) {
                        mBTAddrList.add("NONE");
                        Toast.makeText(this, "La impresora no está configurada. No podrá imprimir el comprobante.", Toast.LENGTH_LONG).show();
                    }
                    ArrayAdapter<String> adapterTemp = null;
                    adapterTemp = new ArrayAdapter<String>(this,	android.R.layout.simple_spinner_item,mBTAddrList);
                    adapterTemp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerBTDeviceList.setAdapter(adapterTemp);


                    addListeners();


                    if(WebService.configuracion.getVuelto().equals("N")){
                        TitleVuelto.setVisibility(View.GONE);
                        Vuelto.setVisibility(View.GONE);
                    }
                    else {
                        TitleVuelto.setVisibility(View.VISIBLE);
                        Vuelto.setVisibility(View.VISIBLE);
                    }
                    if(WebService.configuracion.getCred_suspenso().equals("N")){
                        TitleCredSus.setVisibility(View.GONE);
                        CreditoSus.setVisibility(View.GONE);
                    }
                    else {
                        CreditoSus.setText(WebService.monedaSeleccionada.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.totalCredSus));
                    }

                    if(WebService.configuracion.getDif_reten_auto().equals("N")){
                        TitleDif.setVisibility(View.VISIBLE);
                        Dif.setVisibility(View.VISIBLE);
                        Dif.setText(WebService.simboloMonedaNacional.trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.totalDif + WebService.totalDifACuenta));
                    }else {
                        TitleDif.setVisibility(View.GONE);
                        Dif.setVisibility(View.GONE);
                    }

                    WebService.totalDeudas2 = WebService.totalDeudas;

                    //ClienteSeleccionado.setText(getResources().getString(R.string.Cobrar).toString() + " " +  NumberFormat.getInstance(Locale.ITALY).format(WebService.totalDeudas2).toString());
                    ClienteSeleccionado.setText(getResources().getString(R.string.Cobrar).toString() + " " +  NumberFormat.getInstance(Locale.ITALY).format(WebService.TotalFactura).toString());
                    //}



                    Efectivo.setText(WebService.simboloMonedaNacional.trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.totalEfectivo));
                    EfectivoUSD.setText(WebService.simboloMonedaTr.trim() + " " +  NumberFormat.getInstance(Locale.ITALY).format(WebService.totalEfectivoUSD));

                    if(WebService.monedaCheque.getCod_Moneda() != null){
                        Cheques.setText(WebService.monedaCheque.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.totalCheques));
                    }
                    else {
                        Cheques.setText(WebService.monedaSeleccionada.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.totalCheques));
                    }
                    if(WebService.Entrega_A_Realizar.getCodigoMoneda() != null){
                        Transferencias.setText(WebService.monedaTransferencia.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.totalTransferencias));
                    }
                    else {
                        Transferencias.setText(WebService.monedaSeleccionada.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.totalTransferencias));
                    }
                    if(WebService.monedaTarjCred.getCod_Moneda() != null){
                        TarjetaCredito.setText(WebService.monedaTarjCred.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.totalTarjetaCredito));
                    }
                    else {
                        TarjetaCredito.setText(WebService.monedaSeleccionada.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.totalTarjetaCredito));
                    }
                    if(WebService.monedaTarjDeb.getCod_Moneda() != null){
                        TarjetaDebito.setText(WebService.monedaTarjDeb.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.totalTarjetaDebito));
                    }
                    else {
                        TarjetaDebito.setText(WebService.monedaSeleccionada.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.totalTarjetaDebito));
                    }
                    if(WebService.monedaCredSusp.getCod_Moneda() != null){
                        if(WebService.monedaCredSusp.getCod_Moneda().trim().equals("1")){
                            CreditoSus.setText(WebService.monedaCredSusp.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.totalCredSus));
                        }else{
                            CreditoSus.setText(WebService.monedaCredSusp.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.totalCredSusUSD));
                        }
                    }else{
                        CreditoSus.setText(WebService.monedaSeleccionada.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.totalCredSus));
                    }
                    if(WebService.monedaVuelto.getCod_Moneda() != null){
                        Vuelto.setText(WebService.monedaVuelto.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.totalVuelto));
                    }else {
                        Vuelto.setText(WebService.monedaSeleccionada.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.totalVuelto));
                    }

                    Retenciones.setText(WebService.simboloMonedaNacional.trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.totalRetenciones /*+ WebService.totalLey*/));

                    Double totalEfectivoUSDConvertido;
                    Double totalEfectivoConvertido;
                    Double totalTransferencia = WebService.totalTransferencias;
                    Double totalVuelto = WebService.totalVuelto;
                    Double totalCredSuspenso = WebService.totalCredSus;
                    Double totalCheque = WebService.totalCheques;
                    Double totalTarjCred = WebService.totalTarjetaCredito;
                    Double totalTarjDeb = WebService.totalTarjetaDebito;

                    if(WebService.Entrega_A_Realizar.getCodigoMoneda().trim().equals("1")) {
                        totalEfectivoUSDConvertido =  Utilidad.redondearDecimales(WebService.totalEfectivoUSD * WebService.tipoCambio, 2);
                        totalEfectivoConvertido = Utilidad.redondearDecimales(WebService.totalEfectivo, 2);

                        if(WebService.Entrega_A_Realizar.getCodigoMoneda() != null & WebService.transferencias.size() != 0) {
                            if(WebService.Entrega_A_Realizar.getCodigoMoneda().trim().equals("2")){
                                totalTransferencia = Utilidad.redondearDecimales(WebService.totalTransferencias * WebService.tipoCambio,  2);
                            }
                        }
                        if(WebService.monedaVuelto.getCod_Moneda() != null & WebService.Vuelto.size() != 0) {
                            if(WebService.monedaVuelto.getCod_Moneda().trim().equals("2")){
                                totalVuelto = Utilidad.redondearDecimales(WebService.totalVuelto * WebService.tipoCambio,  2);
                            }
                        }
                        if((WebService.monedaCredSusp.getCod_Moneda() != null)) {
               /* if(WebService.monedaCredSusp.getCod_Moneda().trim().equals("2")){
                    totalCredSuspenso = Utilidad.redondearDecimales(WebService.totalCredSus * WebService.tipoCambio,  0);
                }*/
                        }
                        if(WebService.monedaCheque.getCod_Moneda() != null & WebService.cheques.size() != 0) {
                            if(WebService.monedaCheque.getCod_Moneda().trim().equals("2")){
                                totalCheque = Utilidad.redondearDecimales(WebService.totalCheques * WebService.tipoCambio,  2);
                            }
                        }
                        if(WebService.monedaTarjCred.getCod_Moneda() != null & WebService.tarjetaCreditos.size() != 0) {
                            if(WebService.monedaTarjCred.getCod_Moneda().trim().equals("2")){
                                totalTarjCred = Utilidad.redondearDecimales(WebService.totalTarjetaCredito * WebService.tipoCambio,  2);
                            }
                        }
                        if(WebService.monedaTarjDeb.getCod_Moneda() != null & WebService.tarjetaDebitos.size() != 0) {
                            if(WebService.monedaTarjDeb.getCod_Moneda().trim().equals("2")){
                                totalTarjDeb = Utilidad.redondearDecimales(WebService.totalTarjetaDebito * WebService.tipoCambio,  2);
                            }
                        }
                    }
                    else {
                        totalEfectivoUSDConvertido = Utilidad.redondearDecimales(WebService.totalEfectivoUSD,2);
                        totalEfectivoConvertido = Utilidad.redondearDecimales(WebService.totalEfectivo / WebService.tipoCambio, 2);
                        if(WebService.Entrega_A_Realizar.getCodigoMoneda() != null & WebService.transferencias.size() != 0) {
                            if (WebService.Entrega_A_Realizar.getCodigoMoneda().trim().equals("1")) {
                                totalTransferencia = Utilidad.redondearDecimales(WebService.totalTransferencias / WebService.tipoCambio, 2);
                            }
                        }
                        if(WebService.monedaVuelto.getCod_Moneda() != null & WebService.Vuelto.size() != 0) {
                            if (WebService.monedaVuelto.getCod_Moneda().trim().equals("1")) {
                                totalVuelto = Utilidad.redondearDecimales(WebService.totalVuelto / WebService.tipoCambio, 2);
                            }
                        }
                        if((WebService.monedaCredSusp.getCod_Moneda() != null)) {
                            if (WebService.monedaCredSusp.getCod_Moneda().trim().equals("1")) {
                                totalCredSuspenso = Utilidad.redondearDecimales(WebService.totalCredSus / WebService.tipoCambio, 2);
                            }
                        }
                        if(WebService.monedaCheque.getCod_Moneda() != null & WebService.cheques.size() != 0) {
                            if (WebService.monedaCheque.getCod_Moneda().trim().equals("1")) {
                                totalCheque = Utilidad.redondearDecimales(WebService.totalCheques / WebService.tipoCambio, 2);
                            }
                        }
                        if(WebService.monedaTarjCred.getCod_Moneda() != null & WebService.tarjetaCreditos.size() != 0){
                            if (WebService.monedaTarjCred.getCod_Moneda().trim().equals("1")) {
                                totalTarjCred = Utilidad.redondearDecimales(WebService.totalTarjetaCredito / WebService.tipoCambio, 2);
                            }
                        }
                        if(WebService.monedaTarjDeb.getCod_Moneda() != null & WebService.tarjetaDebitos.size() != 0) {
                            if (WebService.monedaTarjDeb.getCod_Moneda().trim().equals("1")) {
                                totalTarjDeb = Utilidad.redondearDecimales(WebService.totalTarjetaDebito / WebService.tipoCambio, 2);
                            }
                        }
                    }

                    Double diferencia2 = WebService.totalDif;
                    Double difAC = WebService.totalDifACuenta;
                    Double totalValores = 0D;
                    if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("2")){
                        diferencia2 = diferencia2 / WebService.tipoCambio;
                        difAC = difAC / WebService.tipoCambio;

                        totalValores = totalEfectivoConvertido + totalEfectivoUSDConvertido + totalCheque + totalTransferencia + totalTarjCred + totalTarjDeb + WebService.totalRetencionesUSD + WebService.totalCredSusUSD/*+ WebService.totalLeyUSD*/;

                        totalValores = (totalValores + difAC) + diferencia2;
                        totalValores = Utilidad.redondearDecimales(totalValores, 2);
                    }
                    else {
                        totalValores = totalEfectivoConvertido + totalEfectivoUSDConvertido + totalCheque + totalTransferencia + totalTarjCred + totalTarjDeb + totalCredSuspenso +WebService.totalRetenciones/* + WebService.totalLey*/;
                        totalValores = (totalValores + difAC) + diferencia2;
                        totalValores = Utilidad.redondearDecimales(totalValores, 2);

                    }

        /*if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("2")) {
            total.setText(getResources().getString(R.string.TotalCaja) + " " + WebService.monedaSeleccionada.getSim_Moneda().trim() + " " + (totalValores - totalVuelto));
        }else{*/
                    total.setText(getResources().getString(R.string.TotalCaja) + " " + WebService.monedaSeleccionada.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(totalValores - totalVuelto));
                    // }
                    WebService.totalACobrar = totalValores - totalVuelto;

                    //Double diferenciaValores = WebService.totalDeudas2 - totalValores;
                    Double diferenciaValores = WebService.TotalFactura - WebService.totalACobrar;
                    //Double diferenciaValores = WebService.totalDeudas2 - totalEfectivoConvertido - totalEfectivoUSDConvertido - WebService.totalCheques - WebService.totalTransferencias - WebService.totalTarjetaCredito - WebService.totalTarjetaDebito - WebService.totalRetenciones;
                    if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("2")){
                        diferenciaValores = Utilidad.redondearDecimales(diferenciaValores, 2);
                    }

                    if (diferenciaValores < 0) {
                        diferenciaValores = 0D;
                    }

                    WebService.diferenciaValores = diferenciaValores;

                    if(diferenciaValores > 0 && WebService.totalDif > 0){
           /* if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("2")) {
                diferencia.setText(getResources().getString(R.string.Diferencia) + " " + WebService.monedaSeleccionada.getSim_Moneda().trim() + " " + (diferenciaValores + diferencia2));
            }else{*/
                        diferencia.setText(getResources().getString(R.string.Diferencia) + " " + WebService.monedaSeleccionada.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(diferenciaValores + diferencia2));
                        //  }
                    }else {
           /* if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("2")) {
                diferencia.setText(getResources().getString(R.string.Diferencia) + " " + WebService.monedaSeleccionada.getSim_Moneda().trim() + " " + diferenciaValores);
            } else {*/
                        diferencia.setText(getResources().getString(R.string.Diferencia) + " " + WebService.monedaSeleccionada.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(diferenciaValores));
                        //  }
                    }

                    Double acuentaValores = (totalValores - WebService.totalDeudas2) - totalVuelto;
                    if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("2")){
                        acuentaValores = Utilidad.redondearDecimales(acuentaValores, 2);
                    }

                    if ( acuentaValores < 0) {
                        acuentaValores = 0D;
                    }

                    WebService.acuentaValores = acuentaValores;

                    if(WebService.acuentaValores > 0 && WebService.totalDif > 0) {
            /*if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("2")) {
                acuenta.setText(getResources().getString(R.string.ACuenta) + " " + WebService.monedaSeleccionada.getSim_Moneda().trim() + " " + (WebService.acuentaValores + difAC));
            }else{*/
                        acuenta.setText(getResources().getString(R.string.ACuenta) + " " + WebService.monedaSeleccionada.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.acuentaValores + difAC));
                        // }
                    }else {
            /*if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("2")) {
                acuenta.setText(getResources().getString(R.string.ACuenta) + " " + WebService.monedaSeleccionada.getSim_Moneda().trim() + " " + (WebService.acuentaValores));
            } else {*/
                        acuenta.setText(getResources().getString(R.string.ACuenta) + " " + WebService.monedaSeleccionada.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.acuentaValores));
                        // }
                    }

                    final Double finalDiferenciaValores;
                    if(difAC > 0 || diferencia2 > 0) {
                        finalDiferenciaValores = diferenciaValores;
                    }else{
                        finalDiferenciaValores = diferenciaValores + difAC + diferencia2;
                    }


                    /*
                    TitleVuelto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (WebService.bancos.isEmpty()) {
                                dato = 3;
                                new TraerDatos().execute();
                            } else {
                                Intent myIntent = new Intent(context, IngresarVuelto.class);
                                startActivity(myIntent);
                            }
                        }
                    });

                    */
                }else{
                    Utilidad.CargarToastConexion(context);
                }
            }catch(Exception ex){
                ex.printStackTrace();
                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
            }
        }else  {
            Intent myIntent = new Intent( context, Login.class );
            startActivity( myIntent );
        }
    }

    private void addListeners() {

        titleEfectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, IngresarEfectivoTruckSales.class);
                startActivity(myIntent);
            }
        });
/*
        TitleCredSus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, IngresarCredSuspenso.class);
                startActivity(myIntent);
            }
        });

 */
        titleEfectivoUSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, IngresarEfectivoUSDTruckSales.class);
                startActivity(myIntent);
            }
        });
        titleCheques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(WebService.bancos.isEmpty()) {
                    dato = 1;
                    new TraerDatos().execute();
                }
                else{
                    Intent myIntent = new Intent(context, IngresarChequesTruckSales.class);
                    startActivity(myIntent);
                }
            }
        });
        TitleTransferencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(WebService.bancos.isEmpty()) {
                    new TraerDatos().execute();
                }
                if(WebService.bancosPropios.isEmpty()){
                    new TraerDatosTransf().execute();
                }
                else {
                    WebService.ObtenerDatosTarjetaDebito();
                    Intent myIntent = new Intent(context, IngresarTransferenciasTruckSales.class);
                    startActivity(myIntent);
                }
            }
        });
        TitleTarjetaCredito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WebService.tarjetaCreditos.isEmpty()) {
                    new TraerDatosTarjC().execute();
                }
                else{
                    Intent myIntent = new Intent(context, IngresarTarjetaCreditoTruckSales.class);
                    startActivity(myIntent);
                }
            }
        });
        TitleTarjetaDebito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(WebService.tarjetasDebito.isEmpty()){
                    new TraerDatosDeb().execute();
                }
                else{
                    Intent myIntent = new Intent(context, IngresarTarjetaDebitoTruckSales.class);
                    startActivity(myIntent);
                }
            }
        });
        TitleRetenciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WebService.documentos.isEmpty()) {
                    new TraerDocumentos().execute();
                } else {
                    Intent myIntent = new Intent(context, IngresarRetencionesTruckSales.class);
                    startActivity(myIntent);
                }
            }
        });
        TitleDif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(WebService.retenciones.size()>0){
                    Intent myIntent = new Intent(context, IngresarDifRetenTruckSales.class);
                    startActivity(myIntent);
                }
            }
        });
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent myIntent = new Intent(v.getContext(), factura.class);
                    startActivity(myIntent);
                    finish();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        btnValores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidad.vibraticionBotones(v.getContext());
                if (Utilidad.isNetworkAvailable()) {
                    try {

                        AgregarFactura task2 = new AgregarFactura();
                        task2.execute();
                        try {
                            task2.get();
                            if(!WebService.reto_AgregaFactura.equals("ok")){
                                //retorno.setText(WebService.reto_AgregaFactura.trim());
                                Toast.makeText( getApplicationContext(), WebService.reto_AgregaFactura, Toast.LENGTH_LONG ).show();
                            }else{
                                try {
                                    Nro_tran=WebService.nro_trans_impresion;
                                    GenerarHtmlZebra task = new GenerarHtmlZebra();
                                    task.execute();
                                    TimeUnit Unidad = TimeUnit.MILLISECONDS;
                                    task.get(80000, Unidad);
                                } catch (Exception ex) {
                                    Toast.makeText( getApplicationContext(), "Error: " + ex.toString(), Toast.LENGTH_LONG ).show();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }catch (Exception ex){
                        Toast.makeText( getApplicationContext(), "Valores: " + ex.getMessage(), Toast.LENGTH_LONG ).show();
                        ex.printStackTrace();
                    }
                } else {
                    Utilidad.CargarToastConexion(context);
                }
            }
        });
    }

    /*

    private class  TraerClientesViajes extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface
        public TraerClientesViajes(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            if(Utilidad.isNetworkAvailable()) {
                WebService.TraerClientesViajes(params2, "Viajes/ViajesCobrador/TraerClientesViajes.php");

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
            delegate.processFinish( WebService.logueado);
            Utilidad.deleteLoadingMessage();
        }

        @Override
        protected void onProgressUpdate(Void... values)
        {

        }
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
            params1.put( "username",WebService.usuarioActual.getNombre() );
            WebService.TraerViajesCobrador(params1,"Viajes/ViajesCobrador/ViajesCobrador.php");
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
    */
    class TraerDatosTarjC extends AsyncTask<String, Integer, String> {
        ProgressDialog progress;

        protected void onPreExecute (){
            progress = ProgressDialog.show(context, getResources().getString(R.string.datos), getResources().getString(R.string.espere), true);
        }
        protected String doInBackground(String... params) {
            WebService.ObtenerDatosTarjetaCredito();
            Intent myIntent = new Intent(context, IngresarTarjetaCreditoTruckSales.class);
            startActivity(myIntent);
            return null;
        }
        protected void onProgressUpdate(Integer...a){
        }
        protected void onPostExecute(String result) {
            progress.dismiss();
        }
    }

    class TraerDocumentos extends AsyncTask<String, Integer, String> {
        ProgressDialog progress;

        protected void onPreExecute (){
            progress = ProgressDialog.show(context, getResources().getString(R.string.datos), getResources().getString(R.string.espere), true);
        }
        protected String doInBackground(String... params) {
            WebService.TraerDocumentos();
            Intent myIntent = new Intent(context, IngresarRetencionesTruckSales.class);
            startActivity(myIntent);
            return null;
        }
        protected void onProgressUpdate(Integer...a){
        }
        protected void onPostExecute(String result) {
            progress.dismiss();
            if (!WebService.errToken.equals("")) {
                Intent myIntent = new Intent(context, Login.class);
                startActivity(myIntent);
            }
        }
    }

    class TraerDatos extends AsyncTask<String, Integer, String> {
        ProgressDialog progress;

        protected void onPreExecute (){
            progress = ProgressDialog.show(context, getResources().getString(R.string.datos), getResources().getString(R.string.espere), true);
        }
        protected String doInBackground(String... params) {
            WebService.ObtenerDatosIngresarCheques();
            if(dato == 1){
                Intent myIntent = new Intent(context, IngresarChequesTruckSales.class);
                startActivity(myIntent);
            }if(dato == 3){
                Intent myIntent = new Intent(context, IngresarVueltoTruckSales.class);
                startActivity(myIntent);
            }
            return null;
        }
        protected void onProgressUpdate(Integer...a){
        }
        protected void onPostExecute(String result) {
            progress.dismiss();
            if (!WebService.errToken.equals("")) {
                Intent myIntent = new Intent(context, Login.class);
                startActivity(myIntent);
            }
        }
    }


    class TraerDatosDeb extends AsyncTask<String, Integer, String> {
        ProgressDialog progress;

        protected void onPreExecute (){
            progress = ProgressDialog.show(context, getResources().getString(R.string.datos), getResources().getString(R.string.espere), true);
        }
        protected String doInBackground(String... params) {
            WebService.ObtenerDatosTarjetaDebito();
            Intent myIntent = new Intent(context, IngresarTarjetaDebitoTruckSales.class);
            startActivity(myIntent);
            return null;
        }
        protected void onProgressUpdate(Integer...a){
        }
        protected void onPostExecute(String result) {
            progress.dismiss();
        }
    }

    class TraerDatosTransf extends AsyncTask<String, Integer, String> {
        ProgressDialog progress;

        protected void onPreExecute (){
            progress = ProgressDialog.show(context, getResources().getString(R.string.datos), getResources().getString(R.string.espere), true);
        }
        protected String doInBackground(String... params) {
            RequestParams params2 = new RequestParams();
            if(WebService.usuarioActual.getTipoCobrador().equals("D")) {
                params2.add("cod_empresa", WebService.clienteActual.getCodEmp().trim());
            }
            else {
                params2.add("cod_empresa", WebService.usuarioActual.getEmpresa());
            }
            WebService.ObtenerDatosIngresarTransferencias(params2);
            Intent myIntent = new Intent(context, IngresarTransferenciasTruckSales.class);
            startActivity(myIntent);
            return null;
        }
        protected void onProgressUpdate(Integer...a){
        }
        protected void onPostExecute(String result) {
            progress.dismiss();
            if (!WebService.errToken.equals("")) {
                Intent myIntent = new Intent(context, Login.class);
                startActivity(myIntent);
            }
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

        WebService.lat_actual = latitude;
        WebService.long_actual = longitude;
    }

    protected void displayAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder( context );

        inputTV = new TextView( context );
        inputTV.setGravity(Gravity.CENTER);
        builder.setView( inputTV );

        builder.setMessage(getResources().getString(R.string.ToastText2) +  "\n" + WebService.reto_AgregaCobranza).setCancelable(
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




    private class AgregarFactura extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... strings){
            //setValoresEfectivo();
            addPagos();
            WebService.IngresarFactura( "Facturas/IngresarFactura.php",WebService.paramsIng);
            return null;
        }
        @Override
        public void onPreExecute() {
            Utilidad.showLoadingMessage();
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            Utilidad.deleteLoadingMessage();
        }
    }

    private void addPagos() {
        lineasPago = "";

        for (int i = 0; WebService.valoresPago.size() > i; i++) {
            ValoresPago instaValores = WebService.valoresPago.get(i);

            String fechvalor = instaValores.getFec_valor();
            fechvalor = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

            if (i != WebService.valoresPago.size() - 1) {

                lineasPago = lineasPago
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
        WebService.paramsIng.add("cantidad_lineas_pago", String.valueOf(WebService.valoresPago.size()));
        WebService.paramsIng.add("lineasPago",lineasPago);
    }

    private void setValoresEfectivo() {
        if(WebService.efectivo.size()>0){
            String itemsEfectivo="";
            for (com.example.user.trucksales.Encapsuladoras.Efectivo item:WebService.efectivo) {
                if(!itemsEfectivo.equals("")){
                    itemsEfectivo=itemsEfectivo+",";
                }
                itemsEfectivo=itemsEfectivo+item.getImporte();
            }
            WebService.paramsIng.add("efectivo",itemsEfectivo);
        }
    }


    private class  GenerarHtmlZebra extends AsyncTask<String, Void, FacturaZebra> {
        private boolean generacionCajas;
        @Override
        protected FacturaZebra doInBackground(String... strings) {
            generacionCajas = false;
            // Nro_tran=WebService.nro_trans;
            RequestParams params = new RequestParams();
            params.add("nrotrans", Nro_tran);

            FacturaZebra item = WebService.GenerarHtmlZebra("Impresion/printfactura.php", params);
            return item;

        }

        @Override
        protected void onPostExecute(FacturaZebra facturaZebra) {
            imprimirZebraFactura(facturaZebra);

        }
    }



    public void imprimirZebraFactura(final FacturaZebra factura) {
        // public void imprimirZebraFactura(final String s) {

        if(mBTAddrList.get(spinnerBTDeviceList.getSelectedItemPosition()) != "") {

            new Thread(new Runnable() {
                public void run() {
                    //                enableTestButton(false);
                    Looper.prepare();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          //  edit.setText(edit.getText()+"\n"+"Comienza proceso " );
                        }
                    });


                    String items="";
                    Double suma=0D;
                    for (FacturaItemZebra item:factura.getItems()) {

                        items = items + "    "+ getPalabra(item.getNom_articulo(), 60) + " \r\n"
                                + getPalabraNumero(Utilidad.redondearDecimalesNew(Double.valueOf(item.getCant_aprobada()), Integer.valueOf(WebService.configuracion.getDec_cant())), 8) + " X "
                                + getPalabraNumero(Utilidad.redondearDecimalesNew(Double.valueOf(item.getPrecio_iva_inc()), Integer.valueOf(WebService.configuracion.getDec_montomn())), 9) + " - 0.00                         "
                                + getPalabraNumero(Utilidad.redondearDecimalesNew(Double.valueOf(item.getTotal_iva_inc()), Integer.valueOf(WebService.configuracion.getDec_montomn())), 9) + "    "
                                + " \r\n";
                        suma = suma + Double.valueOf(item.getTotal_iva_inc());
                    }

                    String mac = mBTAddrList.get(spinnerBTDeviceList.getSelectedItemPosition());
                    final boolean imprime = Utilidad.printFacturaQR(factura, items, suma, mac); //llama a Utilidades
                    //printQRGonzalo(factura,items,suma)

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //ir a entregas
                            TraerEntregasCliente();
                        }
                    });

                    //Toast.makeText(MainActivity.this, "Comprobante impreso.", Toast.LENGTH_SHORT).show();

                    Looper.loop();
                    Looper.myLooper().quit();
                }
            }).start();


        } else {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   // edit.setText(edit.getText()+"\n"+"No hay impresora seleccionada.");
                }
            });

            Toast.makeText(this, "No hay impresora seleccionada.", Toast.LENGTH_LONG).show();
        }
    }





    private void printQRGonzalo(FacturaZebra factura, String items, Double suma) {
        //Log.i("body", body);
         int itemsSize=factura.getItems().size();
        String mac = mBTAddrList.get(spinnerBTDeviceList.getSelectedItemPosition());
        try {
            try {
                Connection thePrinterConn = new BluetoothConnectionInsecure(mac);
                thePrinterConn.open();
                String example1 = "" +
                        "! 0 200 200 "+getLinea(25,itemsSize)+" 1\r\n" +
                        "CENTER\r\n" +
                        "T 4 0 10 0 " +factura.getNom_empresa()+"\r\n" +
                        "T 4 0 10 50 "+factura.getTitulo()+"\r\n" +
                        "T 7 0 10 100 "+factura.getSubtitulo_movil()+"\r\n" +
                        "T 7 0 10 125 "+factura.getNom_localidad()+"\r\n" +
                        "T 7 0 10 150 No. Punto de Venta "+factura.getCod_control_fact()+"\r\n" +
                        "T 7 0 10 175 "+factura.getDir_empresa()+"\r\n" +
                        "T 7 0 10 200 Tel.: "+factura.getTel_empresa()+"\r\n" +
                        "T 7 0 10 225 "+factura.getMunicipio()+ "\r\n" +
                        "T 7 0 10 250 ---------------------------------------------------------------------\r\n"+
                        "T 7 0 10 275 NIT: "+factura.getNro_cuit()+"\r\n" +
                        "T 7 0 10 300 Nro FACTURA: "+factura.getNro_docum()+"\r\n" +
                        "T 7 0 10 325 Nro AUTORIZACIÓN: \r\n"+
                        "T 7 0 10 350 "+factura.getN_autorizacion()+"\r\n" +
                        "T 7 0 10 375 ---------------------------------------------------------------------\r\n"+
                        "T 7 0 10 400 Venta de partes, piezas y accesorios de vehículos automotores\r\n" +
                        "LEFT\r\n" +
                        "T 7 0 10 425  NOMBRE/RAZÓN SOCIAL: "+factura.getNom_tit()+"\r\n" +
                        "T 7 0 10 450  NIT/CI/CEX: "+factura.getNro_doc_uni()+"\r\n" +
                        "T 7 0 10 475  COD. CLIENTE: "+factura.getCod_cliente()+"\r\n" +
                        "T 7 0 10 500  FECHA EMISIÓN: "+factura.getFec_doc()+"\r\n" +
                        "CENTER\r\n" +
                        "T 7 0 10 525 ---------------------------------------------------------------------\r\n"+
                        "T 7 0 10 550                                   DETALLE                            \r\n" +
                        "LEFT\r\n" +
                        "ML 25\r\n" +
                        "T 7 0 10 575\r\n" +
                        items+
                        "ENDML\r\n" +
                        "CENTER\r\n" +
                        "T 7 0 10 "+getLinea(1,itemsSize)+"                                   TOTAL "+getPalabraNumero(Utilidad.redondearDecimalesNew(suma, Integer.valueOf(WebService.configuracion.getDec_montomn())),12) +" \r\n"+
                        "T 7 0 10 "+getLinea(3,itemsSize)+"  SON: "+factura.getMontoenletras()+" \r\n"+
                        "T 7 0 10 "+getLinea(4,itemsSize)+"---------------------------------------------------------------------\r\n"+
                        "T 7 0 10 "+getLinea(6,itemsSize)+"  ''ESTA FACTURA CONTRIBUYE AL DESARROLLO DEL PAÍS, EL USO ILÍCITO\r\n"+
                        "T 7 0 10 "+getLinea(7,itemsSize)+"  DE ESTA SERÁ SANCIONADO DE ACUERDO A LA LEY''\r\n"+
                        "T 7 0 10 "+getLinea(8,itemsSize)+" "+getPart(0,53,factura.getLeyenda_fac())+"\r\n"+
                        "T 7 0 10 "+getLinea(9,itemsSize)+" "+getPart(53,120,factura.getLeyenda_fac())+"\r\n"+
                        "T 7 0 10 "+getLinea(10,itemsSize)+" "+getPart(0,50,factura.getLeyenda2())+"\r\n"+
                        "T 7 0 10 "+getLinea(11,itemsSize)+" "+getPart(50,116,factura.getLeyenda2())+"\r\n"+
                        "T 7 0 10 "+getLinea(12,itemsSize)+" "+getPart(116,135,factura.getLeyenda2())+"\r\n"+
                        "B QR 325 "+getLinea(15,itemsSize)+"  M 2 U 5\r\n" +
                        "MA,"+factura.getImagen_qr()+"\r\n" +
                        "ENDQR\r\n" +
                        "PRINT\r\n";

                thePrinterConn.write(example1.getBytes("ISO-8859-1"));
                Thread.sleep(500);
                thePrinterConn.close();
                Looper.myLooper().quit();
            } catch (Exception e) {
                e.printStackTrace();
            }

            TraerEntregasCliente();


        } catch (Exception e) {
            e.printStackTrace();
            TraerEntregasCliente();
        }

    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    public void TraerEntregasCliente(){
        params2 = new RequestParams();
        params2.put("nro_viaje", WebService.viajeSeleccionado.getNumViaje());
        params2.put("seleccion", WebService.viajeSeleccionado.getTipo().equals("retiro") ? "r" : "e");
        params2.put("username", WebService.USUARIOLOGEADO);
        final VerEntrega task2 = new VerEntrega(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                WebService.entregaDefault = WebService.entregasTraidas.get(0);
                Intent myIntent = new Intent(context, ClienteXDefecto.class);
                startActivity(myIntent);
            }
        });
        task2.execute();
    }

    public class  VerEntrega extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface
        public VerEntrega(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            WebService.ListaEntregasViaje(params2,"Viajes/Entregas.php");
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

    private String getPart(int desde,int hasta, String leyenda_fac) {
        if(leyenda_fac.length()<desde){
            return "";
        }else if(leyenda_fac.length()<hasta){
            return leyenda_fac.substring(desde,leyenda_fac.length());
        }else{
            return leyenda_fac.substring(desde,hasta);
        }
    }

    private String getLinea(int i, int itemsSize) {
        Long x=625L;

        int aux=(i*25)+(itemsSize*50);

        x=x+aux;
        return x.toString();
    }

    private String getPalabra(String valor, int i) {
        if(valor.length()>i){
            return valor.substring(0,i);
        }else if(valor.length()==i){
            return valor;
        }else {
            while(valor.length()<i){
                valor=valor+" ";
            }
            return valor;
        }

    }


    private String getPalabraNumero(String valor, int i) {
        if(valor.length()>i){
            return valor.substring(0,i);
        }else if(valor.length()==i){
            return valor;
        }else {
            while(valor.length()<i){
                valor=" "+valor;
            }
            return valor;
        }

    }

    private String getEspacio(String palabra) {
        int ancho = palabra.length();
        if(ancho>69){
            return "";
        }else{
            int espacios = 69- ancho;
            Double libre=espacios/2D;
            espacios=libre.intValue();
            String aux="";
            while(espacios>0){
                aux=aux+" ";
                espacios=espacios-1;
            }
            return aux;
        }



    }



    public void printTest(final String s, String mac) {
//    	ZebraPrinter printer = connect();
        connect(mac);
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                 //   edit.setText(edit.getText()+"\n"+"Imprimiendo texto prueba: " + s);
                }
            });

            byte[] printerInstructions = s.getBytes("ASCII");
            printerConnection.write(printerInstructions);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                //    edit.setText(edit.getText()+"\n"+"Instruccion de impresion enviada correctamente: ");
                }
            });
        } catch (final ConnectionException e) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   // edit.setText(edit.getText()+"\n"+"Error imprimiendo. " + e.getMessage());
                }
            });

            Log.e("cobranza_movil", "Error imprimiendo. " + e.getMessage());
//            setStatus(e.getMessage(), Color.RED);
        } catch (final UnsupportedEncodingException e) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   // edit.setText(edit.getText()+"\n"+"Error codificación. " + e.getMessage());
                }
            });

            Log.e("cobranza_movil", "Error de codificación. " + e.getMessage());
        } finally {
            disconnect();
            WebService.Entrega_A_Realizar = WebService.entregaDefault;
            Intent myIntent = new Intent(context, ClienteXDefecto.class);
            startActivity(myIntent);
        }
    }




    private ZebraPrinter connect(String mac) {
//        printerConnection = null;
        printerConnection = new BluetoothConnection(mac);

        try {
            printerConnection.open();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  //  edit.setText(edit.getText()+"\n"+"Abriendo Conección" );
                }
            });

            Log.i("cobranza_movil", "Abriendo Conección");
        } catch (final ConnectionException e) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //edit.setText(edit.getText()+"\n"+"Error de conexión, desconectando.  "+ e.getMessage() );
                }
            });

            Log.e("cobranza_movil", "Error de conexión, desconectando. " + e.getMessage());
            disconnect();
        }

        ZebraPrinter printer = null;

        if (printerConnection.isConnected()) {
            try {
                printer = ZebraPrinterFactory.getInstance(printerConnection);
            } catch (final ConnectionException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      //  edit.setText(edit.getText()+"\n"+"No se pudo establecer conexión"+ e.getMessage());
                    }
                });

                Log.e("cobranza_movil", "No se pudo establecer conexión");
                printer = null;
                disconnect();
            } catch (final ZebraPrinterLanguageUnknownException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    //   edit.setText(edit.getText()+"\n"+"No se pudo determinar el lenguaje de la impresora. " + e.getMessage());
                    }
                });

                Log.e("cobranza_movil", "No se pudo determinar el lenguaje de la impresora. " + e.getMessage());
                printer = null;
                disconnect();
            }
        }

        return printer;
    }

    private void disconnect() {
        try {
            Log.i("cobranza_movil", "Desconectando");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                //    edit.setText(edit.getText()+"\n"+"Desconectando");
                }
            });

            if (printerConnection != null) {
                printerConnection.close();
            }
        } catch (final ConnectionException e) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                 //   edit.setText(edit.getText()+"\n"+"
                    //ssssssss
                    //    public void imprimirZebraFactura(final FacturaZebra factura) {
                    //        // public void imprimirZebraFactura(final String s) {
                    //
                    //        if(mBTAddrList.get(spinnerBTDeviceList.getSelectedItemPosition()) != "") {
                    //
                    //            new Thread(new Runnable() {
                    //                public void run() {
                    //                    //                enableTestButton(false);
                    //                    Looper.prepare();
                    //                    runOnUiThread(new Runnable() {
                    //                        @Override
                    //                        public void run() {
                    //                      >>>>>>>>>>>>>>>>>>>>>     // edit.setText(edit.getText()+"\n"+"Comienza proceso " );
                    //                        }
                    //                    });
                    //
                    //
                    //                    String items="";
                    //                    Double suma=0D;
                    //                    for (FacturaItemZebra item:factura.getItems()) {
                    //
                    //                        items=items +getPalabra(Utilidad.redondearDecimalesNew(Double.valueOf(item.getCant_aprobada()), Integer.valueOf(WebService.configuracion.getDec_cant())),15)
                    //                                +getPalabra(item.getNom_articulo(),28)
                    //                                +getPalabraNumero(Utilidad.redondearDecimalesNew(Double.valueOf(item.getPrecio_iva_inc()), Integer.valueOf(WebService.configuracion.getDec_montomn())),12)
                    //                                +getPalabraNumero(Utilidad.redondearDecimalesNew(Double.valueOf(item.getTotal_iva_inc()), Integer.valueOf(WebService.configuracion.getDec_montomn())),12)
                    //                                +" \r\n";
                    //                        suma=suma+Double.valueOf(item.getTotal_iva_inc());
                    //                    }
                    //
                    //                    String s =
                    //                            //"0123456789 0123456789 0123456789 0123456789 0123456789 0123456789 012"+"\r\n"+
                    //                            getEspacio("TOYOSA SA")+"TOYOSA SA"+" \r\n"+
                    //                                    getEspacio("Casa Matriz")+"Casa Matriz"+" \r\n"+
                    //                                    getEspacio(factura.getDir_empresa())+factura.getDir_empresa()+" \r\n"+
                    //                                    getEspacio(factura.getDir_empresa_aux())+factura.getDir_empresa_aux()+" \r\n"+
                    //                                    getEspacio(factura.getTel_empresa())+factura.getTel_empresa()+" \r\n"+
                    //                                    getEspacio(factura.getNom_localidad()+"-"+factura.getNom_pais())+factura.getNom_localidad()+"-"+factura.getNom_pais()+" \r\n"+
                    //                                    getEspacio("FACTURA")+"FACTURA"+" \r\n"+
                    //                                    "-------------------------------------------------------------------"+" \r\n"+
                    //                                    "NIT:                "+ factura.getNro_cuit()+" \r\n"+
                    //                                    "Nro FACTURA:         "+factura.getNro_docum()+" \r\n"+
                    //                                    "Nro AUTORIZACION:    "+ factura.getN_autorizacion()+" \r\n"+
                    //                                    "-------------------------------------------------------------------"+" \r\n"+
                    //                                    "Venta de partes, piezas y accesorios de vehiculos automotores"+" \r\n"+
                    //                                    "Fecha: "+factura.getFec_doc()+" \r\n"+
                    //                                    "NIT/CI: "+factura.getNro_doc_uni()+" \r\n"+
                    //                                    "Sr(es): "+factura.getNom_tit()+" \r\n"+
                    //                                    "CANTIDAD       DETALLE                     P.UNITARIO         TOTAL"+" \r\n"+
                    //                                    items+
                    //                                    "                                                TOTAL  "+getPalabraNumero(String.valueOf(suma),12) +" \r\n"+
                    //                                    "SON: "+factura.getMontoenletras()+" \r\n"+
                    //                                    "Importe base para Credito Fiscal"+" \r\n"+
                    //                                    " \r\n"+
                    //                                    " \r\n"+
                    //                                    "CODIGO DE CONTROL: "+factura.getCod_control_fact()+" \r\n"+
                    //                                    "FECHA LIMITE DE EMISION: "+factura.getFec_vto_fac()+" \r\n"+
                    //                                    //factura.getImagen_qr()+" \r\n"+
                    //                                    " \r\n"+
                    //                                    " \r\n"+
                    //                                    "ESTA FACTURA CONTRIBUYE AL DESARROLLO DEL PAIS EL USO ILICITO DE ESTA SERA SANCIONADO DE ACUERDO A LA LEY"+" \r\n"+
                    //                                    "Leyenda Ley Nro 453 segun dosificacioncion"
                    //
                    //                            ;
                    //                    ;
                    //
                    //                    String mac = mBTAddrList.get(spinnerBTDeviceList.getSelectedItemPosition());
                    //                    //Log.i("cobranza_movil", s);
                    //
                    //                    //printQR(mac);
                    //                    //printTest2(mac);
                    //                    printTest(s,mac);
                    //                    //printPhotoFromExternal(mac);
                    //
                    //                    runOnUiThread(new Runnable() {
                    //                        @Override
                    //                        public void run() {
                    //                            //edit.setText(edit.getText()+"\n"+"Proceso finalizado. " );
                    //                        }
                    //                    });
                    //
                    //                    //Toast.makeText(MainActivity.this, "Comprobante impreso.", Toast.LENGTH_SHORT).show();
                    //
                    //                    Looper.loop();
                    //                    Looper.myLooper().quit();
                    //                }
                    //            }).start();
                    //
                    //
                    //        } else {
                    //
                    //            runOnUiThread(new Runnable() {
                    //                @Override
                    //                public void run() {
                    //                    //edit.setText(edit.getText()+"\n"+"No hay impresora seleccionada.");
                    //                }
                    //            });
                    //
                    //            Toast.makeText(this, "No hay impresora seleccionada.", Toast.LENGTH_LONG).show();
                    //        }
                    //    }Error de conexión - desconectado. "+ e.getMessage());
                }
            });

            Log.e("cobranza_movil", "Error de conexión - desconectado. " + e.getMessage());
        }
    }

}
