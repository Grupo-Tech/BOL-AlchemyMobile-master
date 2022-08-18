package com.example.user.trucksales.Negocio;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.FacturaZebra;
import com.example.user.trucksales.R;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.RequestParams;
import com.zebra.sdk.comm.BluetoothConnectionInsecure;
import com.zebra.sdk.comm.Connection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.conn.ClientConnectionManager;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.conn.ssl.TrustStrategy;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.impl.conn.tsccm.ThreadSafeClientConnManager;
import okhttp3.OkHttpClient;

/**
 * Created by USER on 27/02/2019.
 */

public class Utilidades {
    private Context InternalContext;
    ProgressDialog Dialog;
    private static boolean DialogExist;
    private LocationManager Localizacion;
    private Vibrator v;

    public Utilidades(Context contexto) {
        InternalContext = contexto;
    }

    public void showLoadingMessage() {
        try {
            if (!DialogExist) {
                Dialog = new ProgressDialog(InternalContext);
                if (!Dialog.isShowing()) {
                    Dialog.setMessage(InternalContext.getResources().getString(R.string.cargando_dialog));
                    Dialog.show();
                    DialogExist = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteLoadingMessage() {
        try {
            if (DialogExist && Dialog != null) {
                if (Dialog.isShowing()) {
                    Dialog.dismiss();
                    DialogExist = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) InternalContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ai = cm.getActiveNetworkInfo();
        return ai != null;
    }

    public void vibraticionBotones(Context Contexto) {
        v = (Vibrator) Contexto.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(100);
        }
    }

    public void CargarToastConexion(final Context Contexto) {
        //modificado RP 06/05/2019 porque daba error
        //Can't create handler inside thread that has not called Looper.prepare()
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                Toast toast = Toast.makeText(Contexto, Contexto.getString(R.string.ErrorConexion), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                toast.show();
            }
        });
        /*
        Toast toast = Toast.makeText( Contexto, Contexto.getString( R.string.ErrorConexion ), Toast.LENGTH_SHORT );
        toast.setGravity( Gravity.CENTER, 0, 0 ); // last two args are X and Y are used for setting position
        toast.setDuration( Toast.LENGTH_LONG );//you can even use milliseconds to display toast
        toast.show();
        */

    }

    public static void dispalyAlertConexion(Context Contexto) {

        AlertDialog.Builder constructor = new AlertDialog.Builder(Contexto);
        TextView texto1 = new TextView(Contexto);
        constructor.setView(texto1);
        constructor.setMessage("No se pudo conectar").setCancelable(
                false).setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            dialog.cancel();
                        } catch (Exception exc) {
                        }
                    }
                });
        AlertDialog alerta1 = constructor.create();
        try {
            alerta1.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alerta1.show();
        } catch (Exception errrssd) {
            errrssd.toString();
        }
    }

    public String GenerarFormato(int Numero) {
        DecimalFormat Formato = (DecimalFormat) NumberFormat.getInstance(Locale.forLanguageTag("SPANISH"));
        DecimalFormatSymbols Simbolos = Formato.getDecimalFormatSymbols();
        Simbolos.setGroupingSeparator('.');
        Formato.setDecimalFormatSymbols(Simbolos);
        return Formato.format(Numero);
    }

    public String GenerarFormato2(double Numero) {
        DecimalFormat Formato = (DecimalFormat) NumberFormat.getInstance(Locale.forLanguageTag("SPANISH"));
        DecimalFormatSymbols Simbolos = Formato.getDecimalFormatSymbols();
        Simbolos.setGroupingSeparator('.');
        Formato.setDecimalFormatSymbols(Simbolos);
        return Formato.format(Numero);
    }

    public static void graba_logws(String paramsIng, String nro, String metodo) {
        String prueba = String.valueOf(paramsIng);
        prueba = prueba.replace("&", ",");

        RequestParams param = new RequestParams();
        param.add("string", prueba);
        param.add("nro_docum", nro);
        param.add("metodo", metodo);

        WebService.guardar(param, "Errores2/grabaLog.php");
    }

    public static double redondearDecimales(double valorInicial, int numeroDecimales) {
        double parteEntera, resultado;
        resultado = valorInicial;
        parteEntera = Math.floor(resultado);
        resultado = (resultado - parteEntera) * Math.pow(10, numeroDecimales);
        resultado = Math.round(resultado);
        resultado = (resultado / Math.pow(10, numeroDecimales)) + parteEntera;
        /*float parteEntera, resultado;
        resultado = (float)valorInicial;
        double resu =(double)resultado;
        parteEntera = (float) Math.floor(resu);
        resultado= (float) ((resultado-parteEntera)*Math.pow(10, numeroDecimales));
        resultado=Math.round(resultado);
        resultado= (float) ((resultado/Math.pow(10, numeroDecimales))+parteEntera);*/

        BigDecimal bd = BigDecimal.valueOf(valorInicial);
        bd = bd.setScale(numeroDecimales, RoundingMode.HALF_UP);
        return bd.doubleValue();

        //return resultado;
    }
    public static String redondearDecimalesNew(Double valorInicial, int numeroDecimales) {
        double parteEntera, resultado;
        resultado = valorInicial;
        parteEntera = Math.floor(resultado);
        resultado = (resultado - parteEntera) * Math.pow(10, numeroDecimales);
        resultado = Math.round(resultado);
        resultado = (resultado / Math.pow(10, numeroDecimales)) + parteEntera;
        /*float parteEntera, resultado;
        resultado = (float)valorInicial;
        double resu =(double)resultado;
        parteEntera = (float) Math.floor(resu);
        resultado= (float) ((resultado-parteEntera)*Math.pow(10, numeroDecimales));
        resultado=Math.round(resultado);
        resultado= (float) ((resultado/Math.pow(10, numeroDecimales))+parteEntera);*/

        BigDecimal bd = BigDecimal.valueOf(valorInicial);
        bd = bd.setScale(numeroDecimales, RoundingMode.HALF_UP);
        return bd.toString();

        //return resultado;
    }
    public void SetFontSizeTextView(ArrayList<TextView> Lista, Context context) {
        float TextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13F, context.getResources().getDisplayMetrics());
        for (TextView Texto : Lista) {
            Texto.setTextSize(TypedValue.COMPLEX_UNIT_PX, TextSize);
        }
    }

    public String NumeroSinPunto(String Numero) {
        String NumeroReal = "";
        char letra;
        for (int x = 0; x < Numero.length(); x++) {
            letra = Numero.charAt(x);
            if (letra != '.') {
                NumeroReal = NumeroReal + String.valueOf(letra);
            }
        }
        return NumeroReal;
    }

    public String NumeroSinComa(String Numero) {
        String NumeroReal = "";
        char letra;
        for (int x = 0; x < Numero.length(); x++) {
            letra = Numero.charAt(x);
            if (letra != ',') {
                NumeroReal = NumeroReal + String.valueOf(letra);
            }
        }
        return NumeroReal;
    }

    public String NumeroSinGuion(String Numero) {
        String NumeroReal = "";
        char letra;
        for (int x = 0; x < Numero.length(); x++) {
            letra = Numero.charAt(x);
            if (letra != '-') {
                NumeroReal = NumeroReal + String.valueOf(letra);
            }
        }
        return NumeroReal;
    }

    public void setContext(Context contexto) {
        InternalContext = contexto;
    }

    public double calcularDistancia(double latitud_actual, double longitud_actual, double latitud_comparar, double longitud_comparar) {
       /* double earthRadius = 6371000; //meters
        double dLat = Math.toRadians( latitud_actual - latitud_comparar );
        double dLng = Math.toRadians( longitud_actual - longitud_comparar );
        double a = Math.sin( dLat / 2 ) * Math.sin( dLat / 2 ) +
                Math.cos( Math.toRadians( latitud_comparar ) ) * Math.cos( Math.toRadians( latitud_actual ) ) *
                        Math.sin( dLng / 2 ) * Math.sin( dLng / 2 );
        double c = 2 * Math.atan2( Math.sqrt( a ), Math.sqrt( 1 - a ) );*/


        double radioTierra = 637100;//en kilómetros
        double dLat = Math.toRadians(latitud_comparar - latitud_actual);
        double dLng = Math.toRadians(longitud_comparar - longitud_actual);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(latitud_actual)) * Math.cos(Math.toRadians(latitud_comparar));
        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));
        double distancia = radioTierra * va2;

        return distancia;
        //return earthRadius * c;
    }

    public String PerfectDecimal(String str, int MAX_BEFORE_POINT, int MAX_DECIMAL) {
        if (str.charAt(0) == '.') str = "0" + str;
        int max = str.length();
        String rFinal = "";
        boolean after = false;
        int i = 0, up = 0, decimal = 0;
        char t;
        while (i < max) {
            t = str.charAt(i);
            if (t != '.' && after == false) {
                up++;
                if (up > MAX_BEFORE_POINT)
                    return rFinal;
            } else if (t == '.') {
                after = true;
            } else {
                decimal++;
                if (decimal > MAX_DECIMAL) return rFinal;
            }
            rFinal = rFinal + t;
            i++;
        }
        return rFinal;
    }

    protected static final String TAG = "NukeSSLCerts";

    public static void nuke() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                            return myTrustedAnchors;
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());


            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sc.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1)  {
                // Not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
                // Not implemented
            }
        } };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    //impresion zebra
    public String getPalabraNumero(String valor, int i) {
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

    public String getPalabra(String valor, int i) {
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

    public boolean printFacturaQR(FacturaZebra factura, String items, Double suma, String mac) {
        System.out.println("Entra en printFacturaQR");
        int itemsSize=factura.getItems().size();
        try {

            String contenido = "" +
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
                    "T 7 0 10 "+getLinea(1,itemsSize)+"                                       TOTAL "+getPalabraNumero(redondearDecimalesNew(suma, Integer.valueOf(WebService.configuracion.getDec_montomn())),12) +" \r\n"+
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

            Connection thePrinterConn = new BluetoothConnectionInsecure(mac);
            thePrinterConn.open();
            if (thePrinterConn.isConnected()){
                //thePrinterConn.open();
                thePrinterConn.write(contenido.getBytes("ISO-8859-1"));
                Thread.sleep(500);
                thePrinterConn.close();
                return true;
            } else {
                System.out.println("No encuentra impresora");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
