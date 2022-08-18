package com.example.user.trucksales.Negocio;

import android.content.Context;
import android.os.AsyncTask;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.FacturaZebra;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by USER on 7/10/2018.
 */

public class Printer
{
    WebView mWebView;
    RequestParams params;
    RequestParams params1;
    //String WEB_URL = "http://200.85.41.198:8081/TerosMobAppDesa/ServerCode/mobileProd/Impresion/Impresiones/";
    //String WEB_URL;
    String WEB_URL_PRINT_CAJAS;
    String Nro_tran = "";
    String cod_tit = "";
    private PrintManager prMang;
    private Context contx;
    private String valor;
    private String tipo;

    public Printer(){
        WebService.WEB_URL = WebService.URL + WebService.URL_PRINT;
        WEB_URL_PRINT_CAJAS = WebService.URL_PRINT_CAJAS;
    }

    public PrintManager getPrMang() {
        return prMang;
    }

    public void setPrMang(PrintManager prMang) {
        this.prMang = prMang;
    }

    public Context getContx() {
        return contx;
    }

    public void setContx(Context contx) {
        this.contx = contx;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    private class  GenerarHtml extends AsyncTask<String, Void, Void> {
        private boolean generacionCajas;
        @Override
        protected Void doInBackground(String... strings) {
            generacionCajas = false;
            params = new RequestParams();
            params.add("nrotrans", Nro_tran);

            params1 = new RequestParams();
            params1.add("cod_tit", Nro_tran);

            switch (tipo.toUpperCase()) {
                case "R":
                    generacionCajas = true;
                    WebService.GenerarHtml("Impresion/printremito.php", params);
                    break;
                case "F":
                    generacionCajas = true;
                    WebService.GenerarHtml("Impresion/printfactura.php", params);
                    break;
                case "E":
                    WebService.GenerarHtml("Impresion/impreCajasSS.php", params);
                    break;
                case "RE":
                    WebService.GenerarHtml( "Impresion/printrecibo.php",params );
                    break;
                case "CC":
                    WebService.GenerarHtml("Impresion/printCierre.php", params);
                    break;
                case "EC":
                    WebService.GenerarHtml("Impresion/estadoCuenta.php", params1);
                    break;
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            BorrarArchivo task2 = new BorrarArchivo();
            task2.execute(  ); //comento de prueba para que no borre
            System.out.println("La variable cajas: " + generacionCajas);
        }
    }

    private class  GenerarHtmlZebra extends AsyncTask<String, Void, Void> {
        private boolean generacionCajas;
        @Override
        protected Void doInBackground(String... strings) {
            generacionCajas = false;
            params = new RequestParams();
            params.add("nrotrans", Nro_tran);

            params1 = new RequestParams();
            params1.add("cod_tit", Nro_tran);

            WebService.GenerarHtmlZebra("Impresion/printfactura.php", params);

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            BorrarArchivo task2 = new BorrarArchivo();
            task2.execute(  ); //comento de prueba para que no borre
            System.out.println("La variable cajas: " + generacionCajas);
        }
    }

    public void genarPdf(Printer prt){

        Nro_tran =prt.getValor();
        //GenerarHtml task = new GenerarHtml();
        //task.execute( );
        try {
            GenerarHtml task = new GenerarHtml();
            task.execute( );
            TimeUnit Unidad = TimeUnit.MILLISECONDS;
            task.get(80000, Unidad);

            WebService.WEB_URL = WebService.WEB_URL + Nro_tran + ".html";
            //WEB_URL = WEB_URL + Nro_tran + ".html";
            mWebView = new WebView( prt.getContx() );
            mWebView.setWebViewClient(new WebViewClient());
            mWebView.getSettings().setJavaScriptEnabled(true);

            //mWebView.loadUrl( WebService.WEB_URL);

            //DocumentPrinter printer = new DocumentPrinter( prt.getContx() );
            PrintDocumentAdapter printAdapter;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                printAdapter = mWebView.createPrintDocumentAdapter("my.pdf");
            }
            else{
                //noinspection deprecation
                printAdapter = mWebView.createPrintDocumentAdapter();
            }

            String jobName = " Imprimir Info";

            PrintAttributes attributes = new PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .setResolution(new PrintAttributes.Resolution("id", Context.PRINT_SERVICE, 200, 200))
                    .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                    .setMinMargins( PrintAttributes.Margins.NO_MARGINS)
                    .build();

            mWebView.loadUrl( WebService.WEB_URL );

            prt.getPrMang().print(jobName, printAdapter, attributes);
            //BorrarArchivo task2 = new BorrarArchivo(); //por ahora no llama a borrar
            //task2.execute(); //comento de prueba para que no borre
        } catch (Exception ex) {
            BorrarArchivoMy();
            Toast.makeText( contx, "Error: " + ex.toString(), Toast.LENGTH_LONG ).show();
        }
    }


    public FacturaZebra genarPdfZebra(Printer prt){

        Nro_tran =prt.getValor();
        //GenerarHtml task = new GenerarHtml();
        //task.execute( );
        try {
            GenerarHtmlZebra task = new GenerarHtmlZebra();
            task.execute( );
            TimeUnit Unidad = TimeUnit.MILLISECONDS;
            task.get(80000, Unidad);

            WebService.WEB_URL = WebService.WEB_URL + Nro_tran + ".html";
            //WEB_URL = WEB_URL + Nro_tran + ".html";
            mWebView = new WebView( prt.getContx() );
            mWebView.setWebViewClient(new WebViewClient());
            mWebView.getSettings().setJavaScriptEnabled(true);

            //mWebView.loadUrl( WebService.WEB_URL);

            //DocumentPrinter printer = new DocumentPrinter( prt.getContx() );
            PrintDocumentAdapter printAdapter;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                printAdapter = mWebView.createPrintDocumentAdapter("my.pdf");
            }
            else{
                //noinspection deprecation
                printAdapter = mWebView.createPrintDocumentAdapter();
            }

            String jobName = " Imprimir Info";

            PrintAttributes attributes = new PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .setResolution(new PrintAttributes.Resolution("id", Context.PRINT_SERVICE, 200, 200))
                    .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                    .setMinMargins( PrintAttributes.Margins.NO_MARGINS)
                    .build();

            mWebView.loadUrl( WebService.WEB_URL );

            prt.getPrMang().print(jobName, printAdapter, attributes);
            //BorrarArchivo task2 = new BorrarArchivo(); //por ahora no llama a borrar
            //task2.execute(); //comento de prueba para que no borre
        } catch (Exception ex) {
            BorrarArchivoMy();
            Toast.makeText( contx, "Error: " + ex.toString(), Toast.LENGTH_LONG ).show();
        }
        return null;
    }

    /*
    public void genarPdf(Printer prt){

        Nro_tran =prt.getValor();
        //GenerarHtml task = new GenerarHtml();
        //task.execute( );
        try {
            GenerarHtml task = new GenerarHtml();
            task.execute( );
            TimeUnit Unidad = TimeUnit.MILLISECONDS;
            task.get(8000,Unidad);
            WebService.WEB_URL = WebService.WEB_URL + Nro_tran + ".html";

            mWebView = new WebView( prt.getContx() );
            mWebView.setWebViewClient(new WebViewClient());
            mWebView.getSettings().setJavaScriptEnabled(true);

            //mWebView.loadUrl(WEB_URL);

            //DocumentPrinter printer = new DocumentPrinter( prt.getContx() );
            PrintDocumentAdapter printAdapter;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                printAdapter = mWebView.createPrintDocumentAdapter("my.pdf");
            }
            else{
                //noinspection deprecation
                printAdapter = mWebView.createPrintDocumentAdapter();
            }

            String jobName = " Imprimir Info";

            PrintAttributes attributes = new PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .setResolution(new PrintAttributes.Resolution("id", Context.PRINT_SERVICE, 200, 200))
                    .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                    .setMinMargins( PrintAttributes.Margins.NO_MARGINS)
                    .build();

            //mWebView.loadUrl( WEB_URL );

            prt.getPrMang().print(jobName, printAdapter, attributes);
            //BorrarArchivo task2 = new BorrarArchivo(); //por ahora no llama a borrar
            //task2.execute(); //comento de prueba para que no borre
        } catch (Exception ex) {
            BorrarArchivoMy();
            Toast.makeText( contx, "Error: " + ex.toString(), Toast.LENGTH_LONG ).show();
        }
    }*/

    private void BorrarArchivoMy(){
        String Path =  "Local/Memoria interna/Download/my.pdf";
        File Archivo = new File( Path );
        try {
            if (Archivo.exists()) {
                Archivo.delete();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class BorrarArchivo extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... strings)
        {
            params = new RequestParams(  );
            params.add( "ruta",Nro_tran );
            WebService.BorrarArchivo( "Impresion/Borrar.php",params );
            return null;
        }
    }
}
