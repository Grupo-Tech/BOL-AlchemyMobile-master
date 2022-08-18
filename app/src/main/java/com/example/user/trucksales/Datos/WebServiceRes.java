package com.example.user.trucksales.Datos;

import android.util.Log;
import com.example.user.trucksales.Encapsuladoras.Usuario;
import com.example.user.trucksales.Encapsuladoras.DeudaPagar;
import com.google.gson.stream.JsonReader;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import okhttp3.OkHttpClient;

import static com.example.user.trucksales.Datos.WebService.usuParam;
import static com.example.user.trucksales.Datos.WebService.usuarioActual;

/**
 * Created by USER on 10/24/2018.
 */

public class WebServiceRes
{
    private HttpURLConnection aramarConexion(RequestParams params,String metodo)
    {
        try
        {
            String sUrl = WebService.URL + metodo + "?" + params;
            URL urlServer = new URL(sUrl);
            HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();

            urlConn.setConnectTimeout(3000); //<- 3Seconds Timeout
            urlConn.connect();
            return urlConn;
        }
        catch (Exception exc)
        {
            return  null;
        }
    }

    private JsonReader armarReader(HttpURLConnection urlConn)
    {
        JsonReader jsonReader;
        try
        {
            InputStream responseBody = urlConn.getInputStream();
            InputStreamReader responseBodyReader =
                    new InputStreamReader(responseBody, "UTF-8");
            jsonReader = new JsonReader(responseBodyReader);
        }
        catch (Exception exc)
        {
            jsonReader = null;
        }
        return  jsonReader;
    }

   /* public void Logeo(String metodo, RequestParams params)
    {
        WebService.usuarioActual = new Usuario();
        try
        {
            HttpURLConnection urlConn = aramarConexion( params,metodo );
            if (urlConn.getResponseCode() == 200) {
                JsonReader jsonReader = armarReader( urlConn );
                jsonReader.beginObject(); // Start processing the JSON object
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName(); // Fetch the next key
                    if (key.equals("success")) { // Check if desired key
                        // Fetch the value as a String
                        // String value = jsonReader.nextString();
                        boolean value = jsonReader.nextBoolean();
                        if(value)
                        {
                            WebService.logueado = true;
                            WebService.USUARIOLOGEADO = usuParam;
                            usuarioActual = new Usuario();
                            usuarioActual.setNombre( usuParam );
                            usuarioActual.setActivo( true );
                            //TraerPermisos(params,"session/TraerPermisos.php");
                        }
                        else
                        {
                            WebService.USUARIOLOGEADO ="";
                        }
                        break; // Break out of the loop
                    }
                    else
                    {
                        jsonReader.skipValue(); // Skip values of other keys
                    }
                }
                jsonReader.close();
            } else {
                String test= "err";
            }
            urlConn.disconnect();
        }
        catch (Exception exc)
        {

        }
    }*/

    //Devuelve un jsonReader con la respuesta del web service Rest
    public static JsonReader jsRespuestaWSRest (final RequestParams rpParams, String sMetodo) {
        JsonReader jsonReader = null;
        HttpURLConnection urlConn = null;
        String sUrl = WebService.URL + sMetodo + "?" + rpParams;
        sUrl = sUrl.replace( "%20","" );
        try {
            URL urlServer = new URL(sUrl);
            urlConn = (HttpURLConnection) urlServer.openConnection();
            urlConn.setRequestProperty("Authorization", WebService.token);
            urlConn.setConnectTimeout(3000); //<- 3 Seconds Timeout
            urlConn.setRequestMethod( "GET" );
            urlConn.setUseCaches(false);
            urlConn.setAllowUserInteraction(false);
            urlConn.connect();
            if (urlConn.getResponseCode() == 200) {
                try{
                    InputStream responseBody = urlConn.getInputStream();
                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                    jsonReader = new JsonReader(responseBodyReader);
                }  catch (Exception ex)
                {
                  ex.getMessage();
                }

            }
            else {
            }
        }
        catch (Exception ex)
        {
            jsonReader = null;
            urlConn = null;
        }
        finally
        {
          // if (urlConn != null) urlConn.disconnect();
        }
        return  jsonReader;
    }


    public static JSONArray jsObjectRespuestaWSRest (final RequestParams rpParams, String sMetodo) {
        JSONArray jsonArray =null;

        HttpURLConnection urlConn = null;
        String sUrl = WebService.URL + sMetodo + "?" + rpParams;
        sUrl = sUrl.replace( "%20","" );
        try {
            URL urlServer = new URL(sUrl);
            urlConn = (HttpURLConnection) urlServer.openConnection();
            urlConn.setRequestProperty("Authorization", WebService.token);
            urlConn.setConnectTimeout(3000); //<- 3 Seconds Timeout
            urlConn.setRequestMethod( "GET" );
            urlConn.setUseCaches(false);
            urlConn.setAllowUserInteraction(false);
            urlConn.connect();
            if (urlConn.getResponseCode() == 200) {
                InputStream responseBody = urlConn.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                //jsonReader = new JsonReader(responseBodyReader);

                BufferedReader streamReader = new BufferedReader(responseBodyReader);
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);

                jsonArray = new JSONArray(responseStrBuilder.toString());
            }
            else {
            }
        }
        catch (Exception ex)
        {
            jsonArray = null;
            urlConn = null;
        }
        finally
        {
            // if (urlConn != null) urlConn.disconnect();
        }
        return  jsonArray;
    }


    public static String imprimirTicket() {
        InputStream is = null;
        JSONObject jObj = null;
        String json = "";
        JSONArray articles = null;
        JsonElement jelement = null;

        String username=WebService.usuarioActual.getNombre();
        String cod_empresa=WebService.usuarioActual.getEmpresa().trim() ;


        String cobrador=WebService.usuarioActual.getNombre();
        String empresaNombre=WebService.usuarioActual.getEmpresa().trim();
        String empresaRuc="123";
        String empresaDireccion="Abc 123";
        String empresaTelefono="123-456";
        String clienteNombre=WebService.clienteActual.getNom_Tit();
        String clienteRuc="123456";
        ArrayList<DeudaPagar> facturas = WebService.deudasPagar;

        String total=WebService.totalDeudas.toString();
        String moneda=WebService.simboloMonedaNacional;

        String efectivo=WebService.totalEfectivo.toString();
        String usd=WebService.totalEfectivoUSD.toString();
        String tipoCambio=WebService.tipoCambio.toString();
        String cheque=WebService.totalCheques.toString();
        String tarjetaCredito=WebService.totalTarjetaCredito.toString();
        String tarjetaDebito=WebService.totalTarjetaDebito.toString();
        String transferencia=WebService.totalTransferencias.toString();
        String retenciones=WebService.totalRetenciones.toString();

        //String urlFinal =  "http://104.236.121.203:8080/trucksalews/SavePagos?empresanombre="+empresaNombre+"&total="+total;
        String urlFinal =  "http://104.236.121.203:8080/trucksalews/SavePagos";
        //String urlFinal = "http://104.236.121.203:8080/relevadatosws/imeiactivo?imei=123456";
        try {
            /*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
            HttpClient httpclient = new DefaultHttpClient();

            /*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
            HttpPost httppost = new HttpPost(urlFinal);

            //AÑADIR HEADER
            httppost.addHeader("Authorization", WebService.token);

            //AÑADIR PARAMETROS
            List<NameValuePair> params = new ArrayList<NameValuePair>();
           // params.add(new BasicNameValuePair("username",username));

            params.add(new BasicNameValuePair("cobrador",cobrador));
            params.add(new BasicNameValuePair("empresanombre",empresaNombre));
            params.add(new BasicNameValuePair("empresaruc",empresaRuc));
            params.add(new BasicNameValuePair("empresadireccion",empresaDireccion));
            params.add(new BasicNameValuePair("empresatelefono",empresaTelefono));
            params.add(new BasicNameValuePair("clientenombre",clienteNombre));
            params.add(new BasicNameValuePair("clienteruc",clienteRuc));

            params.add(new BasicNameValuePair("nrofacturas",String.valueOf(facturas.size())));
            int i=1;
            for ( DeudaPagar  factura: facturas) {

                params.add(new BasicNameValuePair("documento"+i,factura.getDocumento()));
                params.add(new BasicNameValuePair("importe"+i,moneda+" "+factura.getImporte()));

                i++;
            }
            params.add(new BasicNameValuePair("total",moneda+" "+total));
            params.add(new BasicNameValuePair("moneda",moneda));

            params.add(new BasicNameValuePair("efectivo",efectivo));
            params.add(new BasicNameValuePair("usd",usd));
            params.add(new BasicNameValuePair("tipocambio",tipoCambio));
            params.add(new BasicNameValuePair("cheque",cheque));
            params.add(new BasicNameValuePair("tarjetacredito",tarjetaCredito));
            params.add(new BasicNameValuePair("tarjetadebito",tarjetaDebito));
            params.add(new BasicNameValuePair("transferencia",transferencia));
            params.add(new BasicNameValuePair("retenciones",retenciones));

            /*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor envien los datos que hemos añadido*/
            httppost.setEntity(new UrlEncodedFormEntity(params));

            /*Finalmente ejecutamos enviando la info al server*/
            HttpResponse resp = httpclient.execute(httppost);

            /*y obtenemos una respuesta*/
            HttpEntity ent = resp.getEntity();
            is = ent.getContent();

            StringBuilder sb = new StringBuilder();

            // Se parsea la respuesta
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                //json = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            String id = sb.toString().replace("\n", "");
            return id;


        }   catch(Exception e) {

            return "";
        }
    }

    public static JsonReader jsRespuestaWSRestSinParam ( String sMetodo) {
        JsonReader jsonReader = null;
        HttpURLConnection urlConn = null;
        String sUrl = WebService.URL + sMetodo;
        sUrl = sUrl.replace( "%20","" );
        try {
            URL urlServer = new URL(sUrl);
            urlConn = (HttpURLConnection) urlServer.openConnection();
            urlConn.setRequestProperty("Authorization", WebService.token);
            urlConn.setConnectTimeout(3000); //<- 40 Seconds Timeout
            urlConn.connect();
            if (urlConn.getResponseCode() == 200) {
                InputStream responseBody = urlConn.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                jsonReader = new JsonReader(responseBodyReader);
            }
        }
        catch (Exception ex)
        {
            jsonReader = null;
            urlConn = null;
        }
        finally {
            // if (urlConn != null) urlConn.disconnect();
        }
        return  jsonReader;
    }

  /*  public void TraerPermisos(RequestParams params, String metodo)
    {
        JSONObject obj = new JSONObject();
        try
        {
            HttpURLConnection urlConn = aramarConexion( params,metodo );
            if (urlConn.getResponseCode() == 200) {
                JsonReader jsonReader = armarReader( urlConn );
                jsonReader.beginArray(); // Start processing the JSON object

                while (jsonReader.hasNext())
                { // Loop through all keys
                    jsonReader.beginObject();
                    while( jsonReader.hasNext() )
                    {
                        String key = jsonReader.nextName();

                        if(key.equals( "es_cobranza" ))
                        {
                            WebService.usuarioActual.setEs_Cobranza(jsonReader.nextString());
                        }
                        if(key.equals( "es_entrega" ))
                        {
                            WebService.usuarioActual.setEs_Entrega( jsonReader.nextString() );
                        }
                    }
                    jsonReader.endObject();
                }
                jsonReader.endArray();
            }
            else
            {
                String test= "err";
            }
            urlConn.disconnect();
        }
        catch (Exception exc)
        {
            exc.toString();
        }
        // Make RESTful webservice call using AsyncHttpClient object
    /*    SyncHttpClient client = new SyncHttpClient();
        String pUrl = URL +Metodo;
        client.get(URL+Metodo,param ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                try
                {
                    String response = new String(responseBody,"UTF-8");
                    objResponseA = new JSONArray(response);
                    JSONObject objetojSon= new JSONObject();
                    if(objResponseA.length()>0)
                    {
                        for(int i = 0;i<objResponseA.length();i++)
                        {
                            objetojSon = objResponseA.getJSONObject(i);
                            if (objetojSon.getString("es_cobranza")!=null)
                            {
                                usuarioActual.setEs_Cobranza( objetojSon.getString("es_cobranza") );
                            }
                            if (objetojSon.getString("es_entrega")!=null)
                            {
                                usuarioActual.setEs_Entrega( objetojSon.getString("es_entrega") );
                            }
                        }
                    }


                }
                catch (Exception exc)
                {
                    exc.toString();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {
                String err = error.toString();
            }
        });*/
    //}


    // --------------------------------------COBRANZA---------------------------------------//
    public static JsonElement TraerClientes(){
        InputStream is = null;
        JSONObject jObj = null;
        String json = "";
        JSONArray articles = null;
        JsonElement jelement = null;

        String username=WebService.usuarioActual.getNombre().trim();
        String cod_empresa=WebService.usuarioActual.getEmpresa().trim();

        String urlFinal =  WebService.URL + "/Cobranzas/TraerClientes.php?username="+username+"&"+"cod_empresa="+cod_empresa;
        try {
            /*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
            HttpClient httpclient = new DefaultHttpClient();

            /*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
            HttpPost httppost = new HttpPost(urlFinal);

            //AÑADIR HEADER
            httppost.addHeader("Authorization", WebService.token);

            //AÑADIR PARAMETROS
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username",username));
            params.add(new BasicNameValuePair("cod_empresa",cod_empresa));

            /*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor envien los datos que hemos añadido*/
            httppost.setEntity(new UrlEncodedFormEntity(params));

            /*Finalmente ejecutamos enviando la info al server*/
            HttpResponse resp = httpclient.execute(httppost);

            /*y obtenemos una respuesta*/
            HttpEntity ent = resp.getEntity();
            is = ent.getContent();


            // Se parsea la respuesta
            try {
                /*
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                */
                jelement = new JsonParser().parse( new InputStreamReader(is, "UTF-8"));
                is.close();
               // json = sb.toString();
                 //jelement = new JsonParser().parse(sb.toString());
                //JsonObject jobject = jelement.getAsJsonObject();
                //jobject = jobject.getAsJsonObject("data");
                //JsonArray jarray = jobject.getAsJsonArray("translations");
                //jobject = jarray.get(0).getAsJsonObject();
                //String result = jobject.get("translatedText").getAsString();

            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            /*try {
                jObj = new JSONObject(json);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }*/

            // obtenemos los valores
            //JSONObject usuario = jObj.getJSONObject("estadosleidos");
            //Long usuarioId= Long.valueOf(usuario.getString("id"));

            return jelement;
        }   catch(Exception e) {
            Log.e("err", "Error resp " + e.toString());
            return null;
        }
    }

    public static JsonElement TraerDeudas() {
        InputStream is = null;
        JSONObject jObj = null;
        String json = "";
        JSONArray articles = null;
        JsonElement jelement = null;

        String cod_tit=WebService.clienteActual.getCod_Tit_Gestion();
        String cod_empresa=WebService.usuarioActual.getEmpresa().trim();
        String usuario = WebService.USUARIOLOGEADO.trim();
        //String cod_moneda = WebService.monedaSeleccionada.getCod_Moneda().trim();

        String urlFinal =  WebService.URL + "Cobranzas/TraerDeudas.php?cod_tit="+cod_tit.trim()+"&"+"cod_empresa="+cod_empresa +"&"+"username="+usuario;
        try {
            /*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
            HttpClient httpclient = new DefaultHttpClient();

            /*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
            HttpPost httppost = new HttpPost(urlFinal);

            //AÑADIR HEADER
            httppost.addHeader("Authorization", WebService.token);

            //AÑADIR PARAMETROS
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("cod_tit",cod_tit));
            params.add(new BasicNameValuePair("cod_empresa",cod_empresa));
            params.add(new BasicNameValuePair("username", usuario));

            /*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor envien los datos que hemos añadido*/
            httppost.setEntity(new UrlEncodedFormEntity(params));

            /*Finalmente ejecutamos enviando la info al server*/
            HttpResponse resp = httpclient.execute(httppost);

            /*y obtenemos una respuesta*/
            HttpEntity ent = resp.getEntity();
            is = ent.getContent();
            
            // Se parsea la respuesta
            try {

                jelement = new JsonParser().parse( new InputStreamReader(is, "UTF-8"));
                is.close();


            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            /*try {
                jObj = new JSONObject(json);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }*/

            return jelement;
        }   catch(Exception e) {

            return null;
        }
    }

    public static JsonElement TraerDeudasMot(RequestParams params1, String metodo) {
        InputStream is = null;
        JSONObject jObj = null;
        String json = "";
        JSONArray articles = null;
        JsonElement jelement = null;

        String urlFinal =  WebService.URL + metodo + "?" + params1;
        try {
            /*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
            HttpClient httpclient = new DefaultHttpClient();

            /*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
            HttpPost httppost = new HttpPost(urlFinal);

            //AÑADIR HEADER
            httppost.addHeader("Authorization", WebService.token);

            //AÑADIR PARAMETROS
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("cod_tit",WebService.clienteActual.getCod_Tit_Gestion()));
            params.add(new BasicNameValuePair("cod_empresa", usuarioActual.getEmpresa()));

            /*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor envien los datos que hemos añadido*/
            httppost.setEntity(new UrlEncodedFormEntity(params));

            /*Finalmente ejecutamos enviando la info al server*/
            HttpResponse resp = httpclient.execute(httppost);

            /*y obtenemos una respuesta*/
            HttpEntity ent = resp.getEntity();
            is = ent.getContent();

            // Se parsea la respuesta
            try {

                jelement = new JsonParser().parse( new InputStreamReader(is, "UTF-8"));
                is.close();


            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            /*try {
                jObj = new JSONObject(json);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }*/

            return jelement;
        }   catch(Exception e) {

            return null;
        }
    }

    public static JsonElement TraerDeudasViaje() {
        InputStream is = null;
        JSONObject jObj = null;
        String json = "";
        JSONArray articles = null;
        JsonElement jelement = null;

        String cod_tit=WebService.clienteActual.getCod_Tit_Gestion();
        String num_viaje=WebService.viajeSeleccionadoCobrador.getNumViaje().trim();
        String cod_emp=WebService.clienteActual.getCodEmp().trim();
        //String cod_moneda = WebService.monedaSeleccionada.getCod_Moneda().trim();

        String urlFinal =  WebService.URL + "/Viajes/ViajesCobrador/TraerDeudasViaje.php?cod_tit="+cod_tit.trim()+"&"+"num_viaje="+num_viaje+"&"+"cod_emp="+cod_emp;/*+"&"+"cod_moneda="+cod_moneda;*/
        try {
            /*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
            HttpClient httpclient = new DefaultHttpClient();

            /*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
            HttpPost httppost = new HttpPost(urlFinal);

            //AÑADIR HEADER
            httppost.addHeader("Authorization", WebService.token);

            //AÑADIR PARAMETROS
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("cod_tit",cod_tit));
            params.add(new BasicNameValuePair("num_viaje",num_viaje));
            params.add(new BasicNameValuePair("cod_emp",cod_emp));

            /*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor envien los datos que hemos añadido*/
            httppost.setEntity(new UrlEncodedFormEntity(params));

            /*Finalmente ejecutamos enviando la info al server*/
            HttpResponse resp = httpclient.execute(httppost);

            /*y obtenemos una respuesta*/
            HttpEntity ent = resp.getEntity();
            is = ent.getContent();

            // Se parsea la respuesta
            try {

                jelement = new JsonParser().parse( new InputStreamReader(is, "UTF-8"));
                is.close();


            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            /*try {
                jObj = new JSONObject(json);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }*/


            return jelement;
        }   catch(Exception e) {

            return null;
        }
    }

    public static JsonElement traerParemp(){
        InputStream is = null;
        JSONObject jObj = null;
        String json = "";
        JSONArray articles = null;
        JsonElement jelement = null;

        String username=WebService.usuarioActual.getNombre();
        String cod_empresa=WebService.usuarioActual.getEmpresa().trim() ;
        //String cod_moneda =

        String urlFinal =  WebService.URL + "/Cobranzas/TraerParemp.php?cod_emp="+cod_empresa;

        try {
            /*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
            HttpClient httpclient = new DefaultHttpClient();

            /*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
            HttpPost httppost = new HttpPost(urlFinal);

            //AÑADIR HEADER
            httppost.addHeader("Authorization", WebService.token);

            //AÑADIR PARAMETROS
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username",username));
            params.add(new BasicNameValuePair("cod_emp",cod_empresa));

            /*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor envien los datos que hemos añadido*/
            httppost.setEntity(new UrlEncodedFormEntity(params));


            /*Finalmente ejecutamos enviando la info al server*/
            HttpResponse resp = httpclient.execute(httppost);

            /*y obtenemos una respuesta*/
            HttpEntity ent = resp.getEntity();
            is = ent.getContent();

            // Se parsea la respuesta
            try {
                /*
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                */
                jelement = new JsonParser().parse( new InputStreamReader(is, "UTF-8"));
                is.close();
                //json = sb.toString();
                // jelement = new JsonParser().parse(sb.toString());
                //JsonObject jobject = jelement.getAsJsonObject();
                //jobject = jobject.getAsJsonObject("data");
                //JsonArray jarray = jobject.getAsJsonArray("translations");
                //jobject = jarray.get(0).getAsJsonObject();
                //String result = jobject.get("translatedText").getAsString();

            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
           /* try {
                jObj = new JSONObject(json);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
*/
            // obtenemos los valores
            //JSONObject usuario = jObj.getJSONObject("estadosleidos");
            //Long usuarioId= Long.valueOf(usuario.getString("id"));

            return jelement;
        }   catch(Exception e) {

            return null;
        }
    }


    public static JsonElement traerTC(){
        InputStream is = null;
        JSONObject jObj = null;
        String json = "";
        JSONArray articles = null;
        JsonElement jelement = null;

        String username=WebService.usuarioActual.getNombre();
        String cod_empresa=WebService.usuarioActual.getEmpresa().trim() ;
        String fecha = WebService.fecha;
        //String cod_moneda =

        String urlFinal =  WebService.URL + "/Cobranzas/TraerTipoCambio.php?cod_emp="+cod_empresa+"&fecha="+fecha;

        try {
            /*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
            HttpClient httpclient = new DefaultHttpClient();

            /*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
            HttpPost httppost = new HttpPost(urlFinal);

            //AÑADIR HEADER
            httppost.addHeader("Authorization", WebService.token);

            //AÑADIR PARAMETROS
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username",username));
            params.add(new BasicNameValuePair("cod_emp",cod_empresa));

            /*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor envien los datos que hemos añadido*/
            httppost.setEntity(new UrlEncodedFormEntity(params));


            /*Finalmente ejecutamos enviando la info al server*/
            HttpResponse resp = httpclient.execute(httppost);

            /*y obtenemos una respuesta*/
            HttpEntity ent = resp.getEntity();
            is = ent.getContent();

            // Se parsea la respuesta
            try {
                /*
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                */
                jelement = new JsonParser().parse( new InputStreamReader(is, "UTF-8"));
                is.close();
                //json = sb.toString();
                // jelement = new JsonParser().parse(sb.toString());
                //JsonObject jobject = jelement.getAsJsonObject();
                //jobject = jobject.getAsJsonObject("data");
                //JsonArray jarray = jobject.getAsJsonArray("translations");
                //jobject = jarray.get(0).getAsJsonObject();
                //String result = jobject.get("translatedText").getAsString();

            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
           /* try {
                jObj = new JSONObject(json);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
*/
            // obtenemos los valores
            //JSONObject usuario = jObj.getJSONObject("estadosleidos");
            //Long usuarioId= Long.valueOf(usuario.getString("id"));

            return jelement;
        }   catch(Exception e) {

            return null;
        }
    }

    public static JsonElement traerConfig(){
        InputStream is = null;
        JSONObject jObj = null;
        String json = "";
        JSONArray articles = null;
        JsonElement jelement = null;

        String urlFinal =  WebService.URL + "Session/SeleccionarConfiguracion.php";

        try {
            /*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
            HttpClient httpclient = new DefaultHttpClient();

            /*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
            HttpPost httppost = new HttpPost(urlFinal);

            //AÑADIR HEADER
            httppost.addHeader("Authorization", WebService.token);

            /*Finalmente ejecutamos enviando la info al server*/
            HttpResponse resp = httpclient.execute(httppost);

            /*y obtenemos una respuesta*/
            HttpEntity ent = resp.getEntity();
            is = ent.getContent();

            // Se parsea la respuesta
            try {
                /*
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                */
                jelement = new JsonParser().parse( new InputStreamReader(is, "UTF-8"));
                is.close();
                //json = sb.toString();
                // jelement = new JsonParser().parse(sb.toString());
                //JsonObject jobject = jelement.getAsJsonObject();
                //jobject = jobject.getAsJsonObject("data");
                //JsonArray jarray = jobject.getAsJsonArray("translations");
                //jobject = jarray.get(0).getAsJsonObject();
                //String result = jobject.get("translatedText").getAsString();

            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            /*try {
                jObj = new JSONObject(json);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }*/

            // obtenemos los valores
            //JSONObject usuario = jObj.getJSONObject("estadosleidos");
            //Long usuarioId= Long.valueOf(usuario.getString("id"));

            return jelement;
        }   catch(Exception e) {

            return null;
        }
    }
}

 
