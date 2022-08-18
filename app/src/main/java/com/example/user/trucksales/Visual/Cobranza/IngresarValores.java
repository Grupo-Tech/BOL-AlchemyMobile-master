package com.example.user.trucksales.Visual.Cobranza;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Encapsuladoras.ValoresPago;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.ClienteCobranza;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.Visual.TruckSales.ClienteXDefecto;
import com.loopj.android.http.RequestParams;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class IngresarValores extends AppCompatActivity {

private static Context context;
RequestParams paramsIng = new RequestParams();
private Utilidades Utilidad;
TextView nombreUsu,fecha, tipoC, ClienteSeleccionado, inputTV,
Efectivo,EfectivoUSD, Dif,Vuelto,Cheques,Transferencias,TarjetaCredito,TarjetaDebito,Retenciones,
total,diferencia,acuenta, Cliente, CreditoSus;
LinearLayout titleEfectivo, TitleCredSus, TitleVuelto, titleEfectivoUSD, titleCheques,TitleDif, TitleTransferencias,TitleTarjetaCredito,TitleTarjetaDebito,TitleRetenciones;
private static double val_tot=0;
ImageView atras,casita;
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

@Override
protected void onResume()
{
    super.onResume();
    val_tot = 0;
}

public interface AsyncResponse {
    void processFinish(Object output);
}

private class ObtenerNumeroRecibo extends AsyncTask<String, Void, Void> {
    ProgressDialog dialog1 = new ProgressDialog(context);
    public AsyncResponse delegate = null;

    public ObtenerNumeroRecibo(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected Void doInBackground(String... strings) {
        if (Utilidad.isNetworkAvailable()) {
            RequestParams params2 = new RequestParams();
            params2.put("username", WebService.usuarioActual.getNombre());
            params2.put("cod_emp", WebService.clienteActual.getCodEmp().trim());
            WebService.TraerNumRecibo("Cobranzas/NumeroRecibo.php", params2);
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
    protected void onProgressUpdate(Void... values) {
    }
}

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //  this.requestWindowFeature( Window.FEATURE_NO_TITLE );

    setContentView(R.layout.activity_ingresar_valores);

    final String nro_viaje = "0";

    context = this;
    Utilidad = new Utilidades(context);

    GuardarDatosUsuario.Contexto = context;

    ActionBar actionBar = getSupportActionBar();
    actionBar.hide();
    if (WebService.usuarioActual != null) {
        try{
        if(Utilidad.isNetworkAvailable()){

        try {
            valorIntent = getIntent().getStringExtra("intent");
            if(!valorIntent.equals("")){
                WebService.intent = valorIntent;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        Cliente = findViewById(R.id.Cliente);
        Cliente.setText(WebService.clienteActual.getNom_Tit().trim());

        titleEfectivo = findViewById(R.id.titleEfectivo);
        Efectivo = findViewById(R.id.Efectivo);

        titleEfectivoUSD = findViewById(R.id.titleEfectivoUSD);
        EfectivoUSD = findViewById(R.id.EfectivoUSD);

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
        final String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        nombreUsu = (TextView) findViewById(R.id.LblUsu);
        fecha = (TextView) findViewById(R.id.LblFecha);
        fecha.setText(timeStamp);
        atras = findViewById(R.id.btnAtras);
        atras.setClickable(true);
        casita = findViewById(R.id.casita);

        if (WebService.intereses == true) {
            atras.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Utilidad.vibraticionBotones(context);
                        WebService.intereses = false;
                        WebService.totalDeudas = 0D;
                        WebService.limpiarValores();
                        WebService.setTotalDescuento(0D);
                        WebService.setTotalReten(0D);
                        WebService.valoresPago.clear();
                        WebService.clienteInteres.clear();
                        if (WebService.intent.equals("Generados")) {
                            Intent myIntent = new Intent(v.getContext(), RecibosGenerados.class);
                            startActivity(myIntent);
                            finish();
                        } else {
                            Intent myIntent = new Intent(v.getContext(), MenuCobranzas.class);
                            startActivity(myIntent);
                            finish();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            casita.setVisibility(View.GONE);
        }
        else {
            atras.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        Utilidad.vibraticionBotones(context);
                        WebService.totalDeudas = 0D;
                        WebService.totalDescuento = 0D;
                        WebService.totalRetencion = 0D;

                        WebService.setTotalReten(0D);
                        WebService.setTotalDescuento(0D);

                 /* SE COMENTA A PEDIDO 23092019
               WebService.limpiarValores();*/
                        WebService.intereses = false;
                        Intent myIntent = new Intent(v.getContext(), SeleccionarDeudas.class);
                        startActivity(myIntent);
                        finish();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            casita.setClickable(true);
            casita.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        Utilidad.vibraticionBotones(context);
                        WebService.limpiarValores();
                        WebService.totalDescuento = 0D;
                        WebService.totalRetencion = 0D;

                        WebService.setTotalReten(0D);
                        WebService.setTotalDescuento(0D);
                        if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("L") || WebService.usuarioActual.getTipoCobrador().equals("F")) {
                            Intent myIntent = new Intent(context, SeleccionCliente.class);
                            startActivity(myIntent);
                        } else if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D")) {
                            Intent myIntent = new Intent(context, ClienteXDefecto.class);
                            startActivity(myIntent);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }

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

            /*if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("2")) {
                //WebService.totalDeudas2 = Utilidad.redondearDecimales((WebService.totalDeudas - WebService.totalDescuento) / WebService.tipoCambio, 2);
                ClienteSeleccionado.setText(getResources().getString(R.string.Cobrar).toString() + " " + WebService.monedaSeleccionada.getSim_Moneda().trim().toString()+ " " + WebService.totalDeudas2.toString());
            }else {*/
               //WebService.totalDeudas2 = Utilidad.redondearDecimales(WebService.totalDeudas - WebService.totalDescuento, 0);
               if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("2")){
                    //String valor = String.valueOf(WebService.totalDeudas2);
                    WebService.totalDeudas2 = Double.valueOf(Utilidad.redondearDecimales(WebService.totalDeudas2, 2));
                }else{
                   WebService.totalDeudas2 = Double.valueOf(Utilidad.redondearDecimales(WebService.totalDeudas2, 0));
                }
                ClienteSeleccionado.setText(getResources().getString(R.string.Cobrar).toString() + " " + WebService.monedaSeleccionada.getSim_Moneda().trim().toString()+ " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.totalDeudas2).toString());
            //}

        nombreUsu.setText(WebService.USUARIOLOGEADO);

        Efectivo.setText(WebService.simboloMonedaNacional.trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.totalEfectivo));
        EfectivoUSD.setText(WebService.simboloMonedaTr.trim() + " " +  NumberFormat.getInstance(Locale.ITALY).format(WebService.totalEfectivoUSD));

        if(WebService.monedaCheque.getCod_Moneda() != null){
            Cheques.setText(WebService.monedaCheque.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.totalCheques));
        }
        else {
            Cheques.setText(WebService.monedaSeleccionada.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(WebService.totalCheques));
        }
        if(WebService.monedaTransferencia.getCod_Moneda() != null){
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

        if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
            totalEfectivoUSDConvertido =  Utilidad.redondearDecimales(WebService.totalEfectivoUSD * WebService.tipoCambio, 0);
            totalEfectivoConvertido = Utilidad.redondearDecimales(WebService.totalEfectivo, 0);

            if(WebService.monedaTransferencia.getCod_Moneda() != null & WebService.transferencias.size() != 0) {
                if(WebService.monedaTransferencia.getCod_Moneda().trim().equals("2")){
                    totalTransferencia = Utilidad.redondearDecimales(WebService.totalTransferencias * WebService.tipoCambio,  0);
                }
            }
            if(WebService.monedaVuelto.getCod_Moneda() != null & WebService.Vuelto.size() != 0) {
                if(WebService.monedaVuelto.getCod_Moneda().trim().equals("2")){
                    totalVuelto = Utilidad.redondearDecimales(WebService.totalVuelto * WebService.tipoCambio,  0);
                }
            }
            if((WebService.monedaCredSusp.getCod_Moneda() != null)) {
               /* if(WebService.monedaCredSusp.getCod_Moneda().trim().equals("2")){
                    totalCredSuspenso = Utilidad.redondearDecimales(WebService.totalCredSus * WebService.tipoCambio,  0);
                }*/
            }
            if(WebService.monedaCheque.getCod_Moneda() != null & WebService.cheques.size() != 0) {
                if(WebService.monedaCheque.getCod_Moneda().trim().equals("2")){
                    totalCheque = Utilidad.redondearDecimales(WebService.totalCheques * WebService.tipoCambio,  0);
                }
            }
            if(WebService.monedaTarjCred.getCod_Moneda() != null & WebService.tarjetaCreditos.size() != 0) {
                if(WebService.monedaTarjCred.getCod_Moneda().trim().equals("2")){
                    totalTarjCred = Utilidad.redondearDecimales(WebService.totalTarjetaCredito * WebService.tipoCambio,  0);
                }
            }
            if(WebService.monedaTarjDeb.getCod_Moneda() != null & WebService.tarjetaDebitos.size() != 0) {
                if(WebService.monedaTarjDeb.getCod_Moneda().trim().equals("2")){
                    totalTarjDeb = Utilidad.redondearDecimales(WebService.totalTarjetaDebito * WebService.tipoCambio,  0);
                }
            }
        }
        else {
            totalEfectivoUSDConvertido = Utilidad.redondearDecimales(WebService.totalEfectivoUSD,2);
            totalEfectivoConvertido = Utilidad.redondearDecimales(WebService.totalEfectivo / WebService.tipoCambio, 2);
            if(WebService.monedaTransferencia.getCod_Moneda() != null & WebService.transferencias.size() != 0) {
                if (WebService.monedaTransferencia.getCod_Moneda().trim().equals("1")) {
                    totalTransferencia = Utilidad.redondearDecimales(WebService.totalTransferencias / WebService.tipoCambio, 0);
                }
            }
            if(WebService.monedaVuelto.getCod_Moneda() != null & WebService.Vuelto.size() != 0) {
                if (WebService.monedaVuelto.getCod_Moneda().trim().equals("1")) {
                    totalVuelto = Utilidad.redondearDecimales(WebService.totalVuelto / WebService.tipoCambio, 0);
                }
            }
            if((WebService.monedaCredSusp.getCod_Moneda() != null)) {
                if (WebService.monedaCredSusp.getCod_Moneda().trim().equals("1")) {
                    totalCredSuspenso = Utilidad.redondearDecimales(WebService.totalCredSus / WebService.tipoCambio, 0);
                }
            }
            if(WebService.monedaCheque.getCod_Moneda() != null & WebService.cheques.size() != 0) {
                if (WebService.monedaCheque.getCod_Moneda().trim().equals("1")) {
                    totalCheque = Utilidad.redondearDecimales(WebService.totalCheques / WebService.tipoCambio, 0);
                }
            }
            if(WebService.monedaTarjCred.getCod_Moneda() != null & WebService.tarjetaCreditos.size() != 0){
                if (WebService.monedaTarjCred.getCod_Moneda().trim().equals("1")) {
                    totalTarjCred = Utilidad.redondearDecimales(WebService.totalTarjetaCredito / WebService.tipoCambio, 0);
                }
            }
            if(WebService.monedaTarjDeb.getCod_Moneda() != null & WebService.tarjetaDebitos.size() != 0) {
                if (WebService.monedaTarjDeb.getCod_Moneda().trim().equals("1")) {
                    totalTarjDeb = Utilidad.redondearDecimales(WebService.totalTarjetaDebito / WebService.tipoCambio, 0);
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
            totalValores = Utilidad.redondearDecimales(totalValores, 0);

        }

        /*if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("2")) {
            total.setText(getResources().getString(R.string.TotalCaja) + " " + WebService.monedaSeleccionada.getSim_Moneda().trim() + " " + (totalValores - totalVuelto));
        }else{*/
            total.setText(getResources().getString(R.string.TotalCaja) + " " + WebService.monedaSeleccionada.getSim_Moneda().trim() + " " + NumberFormat.getInstance(Locale.ITALY).format(totalValores - totalVuelto));
       // }
        WebService.totalACobrar = totalValores - totalVuelto;

        //Double diferenciaValores = WebService.totalDeudas2 - totalValores;
        Double diferenciaValores = WebService.totalDeudas2 - WebService.totalACobrar;
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

        btnValores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidad.vibraticionBotones(v.getContext());
                if (Utilidad.isNetworkAvailable()) {
                    try {
                    ObtenerNumeroRecibo task = new ObtenerNumeroRecibo(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            if (!WebService.errToken.equals("")) {
                                Intent myIntent = new Intent(context, Login.class);
                                startActivity(myIntent);
                            } else {
                                if (finalDiferenciaValores > 0) {
                                    Toast toast = Toast.makeText(context, getResources().getString(R.string.Completar), Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                                    toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                                    toast.show();//showing the toast is important***/
                                } else if (WebService.intereses == true) {
                                    try {

                                        paramsIng = new RequestParams();
                                        lineasDeudaSelec = "";

                                        for (int i = 0; WebService.clienteInteres.size() > i; i++) {
                                            ClienteCobranza instaCliente = WebService.clienteInteres.get(i);

                                            if (i != WebService.clienteInteres.size() - 1) {

                                                lineasDeudaSelec = lineasDeudaSelec
                                                        + instaCliente.getCod_dpto()
                                                        + "," + instaCliente.getSerie_docum().trim()
                                                        + "," + instaCliente.getNro_Docum().trim()
                                                        + "," + instaCliente.getCod_Docum()
                                                        + "," + instaCliente.getFecha_Vence().trim()
                                                        + "," + WebService.reto_Interes
                                                        + "," + instaCliente.getImp_mov_mo()
                                                        + "," + instaCliente.getDescuento()
                                                        + "," + instaCliente.getCod_Moneda() + ";";
                                            } else {
                                                lineasDeudaSelec = lineasDeudaSelec
                                                        + instaCliente.getCod_dpto()
                                                        + "," + instaCliente.getSerie_docum().trim()
                                                        + "," + instaCliente.getNro_Docum().trim()
                                                        + "," + instaCliente.getCod_Docum()
                                                        + "," + instaCliente.getFecha_Vence().trim()
                                                        + "," + WebService.reto_Interes
                                                        + "," + instaCliente.getImp_mov_mo()
                                                        + "," + instaCliente.getDescuento()
                                                        + "," + instaCliente.getCod_Moneda().trim();
                                            }
                                        }

                                        String fechvalor;
                                        lineasPago = "";

                                        for (int i = 0; WebService.valoresPago.size() > i; i++) {
                                            ValoresPago instaValores = WebService.valoresPago.get(i);

                                            fechvalor = instaValores.getFec_valor();
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

                                        Double totalEfectivoUSDConvertido = WebService.totalEfectivoUSD * WebService.tipoCambio;
                                        Double totalValores = WebService.totalEfectivo + totalEfectivoUSDConvertido + WebService.totalCheques + WebService.totalTransferencias + WebService.totalTarjetaCredito + WebService.totalTarjetaDebito + WebService.totalRetenciones;
                                        fac = NumberFormat.getInstance().format(totalValores);
                                        if (fac.contains(",")) {
                                            fac = fac.replace(",", ".");
                                        }

                                        if (WebService.intent.equals("Recibo")) {
                                            if (WebService.usuarioActual.getTipoCobrador().equals("D")) {
                                                nro_viaje1 = WebService.viajeSeleccionadoCobrador.getNumViaje().trim();
                                            }
                                        }

                                        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                                        total2 = Double.valueOf(Utilidad.NumeroSinPunto(fac));
                                        paramsIng.add("nro_docum", WebService.nro_ingresado.trim());
                                        paramsIng.add("fec_doc", timeStamp);
                                        paramsIng.add("cod_emp", WebService.usuarioActual.getEmpresa().trim());
                                        paramsIng.add("cod_moneda", WebService.monedaSeleccionada.getCod_Moneda().trim());
                                        paramsIng.add("cod_tit", WebService.clienteActual.getCod_Tit_Gestion().trim());
                                        paramsIng.add("usuario", WebService.USUARIOLOGEADO);
                                        paramsIng.add("total", total2.toString());
                                        paramsIng.add("tipo_cambio", WebService.tipoCambio.toString());
                                        paramsIng.add("nro_viaje", nro_viaje1);
                                        paramsIng.add("id_mobile", deviceId);

                                        // PARA PASAR VALORES DE PAGO
                                        paramsIng.add("cantidad_lineas_pago", String.valueOf(WebService.valoresPago.size()));
                                        paramsIng.add("lineasPago", lineasPago);
                                        // PARA PASAR DEUDA
                                        paramsIng.add("cantidad_lineas", String.valueOf(WebService.clienteInteres.size()));
                                        paramsIng.add("lineas", lineasDeudaSelec);

                                        AgregarCobranza task1 = new AgregarCobranza(new AsyncResponse() {
                                            @Override
                                            public void processFinish(Object output) {
                                                if (WebService.reto_AgregaCobranza.equals("ok")) {
                                                    WebService.intereses = false;
                                                    WebService.reto_AgregaCobranza = "";
                                                    WebService.nro_trans_impresion = "";

                                                    WebService.EstadoActual = 0;
                                                    LatiLong();
                                                    params = new RequestParams();

                                                    WebService.totalDeudas = 0D;
                                                    WebService.limpiarValores();
                                                    WebService.valoresPago.clear();
                                                    WebService.clienteInteres.clear();

                                                    if (WebService.intent.equals("Recibo")) {
                                                        if (WebService.usuarioActual.getTipoCobrador().equals("D")) {
                                                            params.put("num_viaje", WebService.viajeSeleccionadoCobrador.getNumViaje());
                                                            params.put("username", WebService.USUARIOLOGEADO);
                                                            otraDeuda = false;
                                                            WebService.clienteTraidos.clear();

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
                                                                            CargarViajesCobrador viaje = new CargarViajesCobrador(new AsyncResponse() {
                                                                                @Override
                                                                                public void processFinish(Object output) {
                                                                                    if (!WebService.errToken.equals("")) {
                                                                                        Intent myIntent = new Intent(context, Login.class);
                                                                                        startActivity(myIntent);
                                                                                    } else {
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

                                                                                        ActualizarUbicacion actuUbic = new ActualizarUbicacion();
                                                                                        actuUbic.execute();

                                                                                        if (WebService.clienteTraidos.size() <= 0) {
                                                                                            Intent myIntent = new Intent(context, MenuCobranzas.class);
                                                                                            startActivity(myIntent);
                                                                                        } else {
                                                                                            for (int i = 0; i < WebService.clienteTraidos.size(); i++) {
                                                                                                ClienteCobranza cliente = WebService.clienteTraidos.get(i);
                                                                                                WebService.clienteActual = cliente;
                                                                                                break;
                                                                                            }
                                                                                            Intent myIntent = new Intent(context, ClienteXDefecto.class);
                                                                                            startActivity(myIntent);
                                                                                        }
                                                                                    }
                                                                                }
                                                                            });
                                                                            viaje.execute();
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                            task3.execute();
                                                        } else if (WebService.usuarioActual.getTipoCobrador().equals("L") || WebService.usuarioActual.getTipoCobrador().equals("D")) {
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

                                                            ActualizarUbicacion actuUbic = new ActualizarUbicacion();
                                                            actuUbic.execute();

                                                            Intent myIntent = new Intent(context, SeleccionCliente.class);
                                                            startActivity(myIntent);
                                                        }
                                                    } else {
                                                        params = new RequestParams();
                                                        params.add("username", WebService.USUARIOLOGEADO);
                                                        TraerRecibosGenerados task = new TraerRecibosGenerados(new AsyncResponse() {
                                                            @Override
                                                            public void processFinish(Object output) {
                                                                if (!WebService.errToken.equals("")) {
                                                                    Intent myIntent = new Intent(context, Login.class);
                                                                    startActivity(myIntent);
                                                                } else {
                                                                    Intent myIntent = new Intent(context, RecibosGenerados.class);
                                                                    startActivity(myIntent);
                                                                }
                                                            }
                                                        });
                                                        task.execute();

                                                    }
                                                } else {
                                                    if(WebService.reto_AgregaCobranza.toUpperCase().contains("JSON")) {
                                                        Intent myIntent = new Intent(context, Login.class);
                                                        startActivity(myIntent);
                                                    }else {
                                                        displayAlert();
                                                    }
                                            /*Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.ToastText2) + "\n" + WebService.reto_AgregaCobranza, Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                                            toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                                            toast.show();//showing the toast is important***/
                                                }
                                            }
                                        });
                                        task1.execute();
                                    } catch (Exception ex) {
                                        WebService.reto_AgregaCobranza = "Error: " + "Deuda:" + lineasDeudaSelec + "\n" + "Valores:" + lineasPago + "\n" + "Total:" + fac + "\n" + "Nro Doc:" + WebService.nro_sugerido;
                                        displayAlert();
                                        ex.printStackTrace();
                                        //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                                    }
                                } else {

                                    WebService.EstadoActual = 5;
                                    LatiLong();

                                    params = new RequestParams();
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

                                    Intent myIntent = new Intent(context, Recibo.class);
                                    startActivity(myIntent);
                                }
                            }
                        }
                        });
                        task.execute(); //DE LA OBTENCION DEL NUM DE RECIBO*/

                    }catch (Exception ex){
                        Toast.makeText( getApplicationContext(), "Valores: " + ex.getMessage(), Toast.LENGTH_LONG ).show();
                        ex.printStackTrace();
                    }
                } else {
                    Utilidad.CargarToastConexion(context);
                }
            }
        });


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
        TitleCredSus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent myIntent = new Intent(context, IngresarCredSuspenso.class);
                    startActivity(myIntent);
            }
        });
        titleEfectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, IngresarEfectivo.class);
                startActivity(myIntent);
            }
        });
        titleEfectivoUSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, IngresarEfectivoUSD.class);
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
                    Intent myIntent = new Intent(context, IngresarCheques.class);
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
                    Intent myIntent = new Intent(context, IngresarTransferencias.class);
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
                    Intent myIntent = new Intent(context, IngresarTarjetaCredito.class);
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
                    Intent myIntent = new Intent(context, IngresarTarjetaDebito.class);
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
                    Intent myIntent = new Intent(context, IngresarRetenciones.class);
                    startActivity(myIntent);
                }
            }
        });
        TitleDif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(WebService.retenciones.size()>0){
                    Intent myIntent = new Intent(context, IngresarDifReten.class);
                    startActivity(myIntent);
                }
            }
        });

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

    private class AgregarCobranza extends AsyncTask<String,Void,Void> {
        ProgressDialog dialog1 = new ProgressDialog(context);
        public AsyncResponse delegate = null;//Call back interface

        public AgregarCobranza(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... strings) {
            if (Utilidad.isNetworkAvailable()) {

                if(WebService.configuracion.getGraba_logws().equals("S")){
                    Utilidad.graba_logws(String.valueOf(paramsIng), WebService.nro_sugerido.trim(), "Ing.Cob - Fact. Interes");
                }

                WebService.IngresarCobranza("Cobranzas/IngresarCobranza.php", paramsIng);
                return null;
            } else {
                Utilidad.dispalyAlertConexion(context);
            }
            return null;
        }

        @Override
        public void onPreExecute() {
            Utilidad.showLoadingMessage();
        }

        @Override
        protected void onPostExecute(Void result) {
            delegate.processFinish(WebService.logueado);
            Utilidad.deleteLoadingMessage();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            WebService.totalDeudas = 0D;
            WebService.totalDescuento = 0D;
            WebService.totalRetencion = 0D;
            WebService.clienteInteres.clear();

            WebService.setTotalReten(0D);
            WebService.setTotalDescuento(0D);

            if (WebService.intereses == true) {
                WebService.intereses = false;
                if (WebService.intent.equals("Generados")) {
                    Intent myIntent = new Intent(context, RecibosGenerados.class);
                    startActivity(myIntent);
                    finish();
                } else {
                    Intent myIntent = new Intent(context, MenuCobranzas.class);
                    startActivity(myIntent);
                    finish();
                }
            } else {
                if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("L") || WebService.usuarioActual.getTipoCobrador().equals("F")) {
                    Intent myIntent = new Intent(context, SeleccionarDeudas.class);
                    startActivity(myIntent);
                } else if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getTipoCobrador().equals("D")) {
                    Intent myIntent = new Intent(context, ClienteXDefecto.class);
                    startActivity(myIntent);
                }
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

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
            /*RequestParams params4 = new RequestParams(  );
            params4.put("cod_tit", WebService.clienteActual.getCod_Tit_Gestion());
            params4.put("num_viaje", WebService.viajeSeleccionadoCobrador.getNumViaje());
            params4.put("cod_emp",WebService.clienteActual.getCodEmp());*/
            WebService.TraerDeudasViaje(/*params4*/);
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

    class TraerDatosTarjC extends AsyncTask<String, Integer, String> {
        ProgressDialog progress;

        protected void onPreExecute (){
            progress = ProgressDialog.show(context, getResources().getString(R.string.datos), getResources().getString(R.string.espere), true);
        }
        protected String doInBackground(String... params) {
            WebService.ObtenerDatosTarjetaCredito();
            Intent myIntent = new Intent(context, IngresarTarjetaCredito.class);
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
            Intent myIntent = new Intent(context, IngresarRetenciones.class);
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
                Intent myIntent = new Intent(context, IngresarCheques.class);
                startActivity(myIntent);
            }if(dato == 3){
                Intent myIntent = new Intent(context, IngresarVuelto.class);
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
            Intent myIntent = new Intent(context, IngresarTarjetaDebito.class);
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
            Intent myIntent = new Intent(context, IngresarTransferencias.class);
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

    private class TraerRecibosGenerados extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface

        public TraerRecibosGenerados(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... strings) {
            WebService.TraerRecibosXDia("Cobranzas/recibosGenerados.php", params);
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
    }
}
