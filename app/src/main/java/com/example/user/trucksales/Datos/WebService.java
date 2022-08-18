package com.example.user.trucksales.Datos;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.user.trucksales.Encapsuladoras.Anulada;
import com.example.user.trucksales.Encapsuladoras.Banco;
import com.example.user.trucksales.Encapsuladoras.BancoPropio;
import com.example.user.trucksales.Encapsuladoras.Caja;
import com.example.user.trucksales.Encapsuladoras.CajaCobranza;
import com.example.user.trucksales.Encapsuladoras.Cheque;
import com.example.user.trucksales.Encapsuladoras.ClienteCobranza;
import com.example.user.trucksales.Encapsuladoras.Configuracion_Empresa;
import com.example.user.trucksales.Encapsuladoras.Credito_Suspenso;
import com.example.user.trucksales.Encapsuladoras.Diferencia_Reten;
import com.example.user.trucksales.Encapsuladoras.FacturaItemZebra;
import com.example.user.trucksales.Encapsuladoras.FacturaZebra;
import com.example.user.trucksales.Encapsuladoras.Observaciones;
import com.example.user.trucksales.Encapsuladoras.Pedido;
import com.example.user.trucksales.Encapsuladoras.Presentacion;
import com.example.user.trucksales.Encapsuladoras.ProductosItem;
import com.example.user.trucksales.Encapsuladoras.Vuelto;
import com.example.user.trucksales.Encapsuladoras.DeudaPagar;
import com.example.user.trucksales.Encapsuladoras.EParametros;
import com.example.user.trucksales.Encapsuladoras.Efectivo;
import com.example.user.trucksales.Encapsuladoras.EfectivoUSD;
import com.example.user.trucksales.Encapsuladoras.Entrega;
import com.example.user.trucksales.Encapsuladoras.FactValidada;
import com.example.user.trucksales.Encapsuladoras.FacturaXDia;
import com.example.user.trucksales.Encapsuladoras.Item;
import com.example.user.trucksales.Encapsuladoras.Moneda;
import com.example.user.trucksales.Encapsuladoras.MotivoPausa;
import com.example.user.trucksales.Encapsuladoras.Retenciones;
import com.example.user.trucksales.Encapsuladoras.TarjetaCredito;
import com.example.user.trucksales.Encapsuladoras.TarjetaDebito;
import com.example.user.trucksales.Encapsuladoras.TipoDoc;
import com.example.user.trucksales.Encapsuladoras.Transferencias;
import com.example.user.trucksales.Encapsuladoras.Usuario;
import com.example.user.trucksales.Encapsuladoras.ValoresPago;
import com.example.user.trucksales.Encapsuladoras.Viaje;
import com.example.user.trucksales.Visual.Configuracion;
import com.example.user.trucksales.Visual.Login;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.conn.ClientConnectionManager;
import cz.msebera.android.httpclient.entity.SerializableEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import cz.msebera.android.httpclient.protocol.BasicHttpContext;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by USER on 12/7/2017.
 *
 */
//import com.example.user.trucksales.http.*;

public class WebService {
    //public static String URL = "http://192.168.0.100:8079/TerosMobVerGT/ServerCode/mobileProd/";//probar local
    // public static  String URL = "http://techgroup.dyndns.biz/TerosMobVerGT/ServerCode/mobileProd/";
    public static EParametros ParametrosSistema;

    // public static String URL_PRINT = "http://10.10.10.129/CobranzasMovil/Impresion/Impresiones/";

    /* TODO URLS DE PRUEBA GTCONT*/
  /*  public static String URL = "https://192.168.0.104/serverphp/Cobranzas/";
    public static String URL_BASE = "https://192.168.0.104/serverphp/";
    public static double version = 3.5;*/

    /*// TODO URLS DE TOYOTOSHI
    //public static String URL = "http://10.10.10.129/CobranzasMovil/";
    public static String URL = "https://app.toyotoshi.com.py:7443/CobranzasMovilProd/";
    //public static String URL = "https://app.toyotoshi.com.py:7443/CobranzasMovilDesa/";
    //public static String URL = "http://10.10.10.129/CobranzasMovilToken/";
    public static String URL_BASE = "https://app.toyotoshi.com.py:7443/";
    public static double version = 4.4;
    //public static String URL_BASE2 = "http://10.10.10.129/"; //antes
    public static String URL_BASE2 = "https://app.toyotoshi.com.py:7443/CobranzasMovilDesa/";*/

    public static String WEB_URL;
    public static RequestParams paramsIng;

    //public static String URL_PRINT_CAJAS = "http://200.85.41.198:8081/TerosMobApp/mobileProd/Impresion/";*/

    //TODO URLS DE FRESHFOOD
    //    /*public static String URL = "http://200.85.41.198:8081/TerosMobApp/mobileProd/";
    //    //public static String URL = "https://200.85.41.198/TerosMobApp/mobileProd/";
    //    //public static String URL = "http://200.85.41.198:8081/TerosMobApp/mobileDesa/";
    //    public static String URL_BASE = "http://200.85.41.198:8081/TerosMobApp/";
    //    //public static String URL_BASE = "https://200.85.41.198/TerosMobApp/";
    //    public static double version = 2.33;
    //    public static String URL_BASE2 = "http://200.85.41.198:8081/TerosMobApp/";
    //    //public static String URL_BASE2 = "https://200.85.41.198/TerosMobApp/";*/


    //TODO URLS DE test
    //static String  base="http://techgroup.dyndns.biz:85/";  //PRODUCCION DESDE WINDOWS(LOCAL GTECH)
    static String base = "https://trucksales.toyosa-test.com/";   //DESARROLLO
    //static String  base="https://trucksales.toyosa.com/";  //PRODUCCION

    public static String URL = base + "TSmobile/Servicios/";
    public static String URL_BASE = base + "TSmobile/";
    public static double version = 2.33;
    public static String URL_BASE2 = base + "TSmobile/";
    //fin

    public static String URL_PRINT_CAJAS = URL + "Impresion/";
    //public static String URL_PRINT = URL + "Impresion/Impresiones/";
    public static String URL_PRINT = "Impresion/Impresiones/";
    public static String WEB_URL_SITUACION = URL + "Cobranzas/situacionCliente.php?";

    /*public static String URL = "http://200.85.41.198:8081/TerosMobApp/mobileProd/";
    public static String URL_PRINT = "http://200.85.41.198:8081/TerosMobApp/mobileProd/Impresion/Impresiones/";*/

    //public static String URL_PRINT_CAJAS = "http://200.85.41.198:8081/TerosMobApp/mobileProd/Impresion/";

    //public static  String URL = "http://techgroup.dyndns.biz:8081/TerosMobVerGT/ServerCode/mobileProd/";
    //   public static String URL = "http://192.168.0.118:8188/";
    public static List<FacturaXDia> listaRemitos = new ArrayList<>();
    public static List<FacturaXDia> listafacturas = new ArrayList<>();
    public static List<FacturaXDia> listarecibos = new ArrayList<>();
    public static List<FacturaXDia> listafact = new ArrayList<>();
    public static List<Observaciones> listaobservaciones = new ArrayList<>();
    public static List<ClienteCobranza> listEmpresas = new ArrayList<>();
    /*---------------------MANEJO DE CONFIGURACION----------------------------------------------------------------------------*/

    public static List<Configuracion_Empresa> empresasRegistradas = new ArrayList<>();
    /*---------------------FIN DE MANEJO---------------------------------------------------------------------------------------------*/
    //public static String URL ="http://200.85.41.198:8081/mobileWS_20180424/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static JSONArray objResponseA;
    public static JSONObject objResponse;
    public static String USUARIOLOGEADO = "";
    public static int contadorRemito;
    public static int contadorFactura;
    public static int contadorRecibo;
    public static String nro_rem;
    public static String usuParam;
    public static int max_lin_rec;
    //public static  String depoParam="";
    public static Boolean logueado = false;
    public static List<Viaje> viajesUsu = new ArrayList<>();
    public static List<Viaje> viajesCobradorUsu = new ArrayList<>();
    public static List<Caja> cajas = new ArrayList<>();
    public static List<Caja> tipoCajas = new ArrayList<>();
    public static List<Entrega> entregasTraidas = new ArrayList<>();
    public static List<ClienteCobranza> ListaclienteViaje = new ArrayList<>();
    public static List<FacturaXDia> facturasDelDia = new ArrayList<>();
    public static List<FacturaXDia> recibosDelDia = new ArrayList<>();
    //public static String retornoEnvases;
    //public static int codigoEnvases;
    public static FacturaXDia nFac = new FacturaXDia();
    public static Viaje viajeSeleccionado;
    public static Viaje viajeSeleccionadoCobrador;
    public static ClienteCobranza clienteActualCobrador;
    public static List<ClienteCobranza> clienteTraidos = new ArrayList<>();
    public static Entrega Entrega_A_Realizar = new Entrega();
    public static ClienteCobranza Cliente_A_IR = new ClienteCobranza();

    public static String token = "";
    public static String errToken = "";
    public static String intent;

    public static String ordenSiguiente;
    public static String formapago;

    public static String calula_interes = "";
    public static String respuesta = "";
    public static String fecha = "";

    public static String nombreLocal;
    public static Double totalACobrar;
    //public static List<Caja> listaConSaldoEnvase = new ArrayList<>();
    public static String direccion;
    public static String observaciones;
    public static String nro_orden;
    public static String clienteDestino;
    public static String horaComienzoViaje;
    public static String cod_sucu;
    public static String ErrorLogin;
    public static String cod_tit;
    public static String cod_emp;

    public static Double totalRetencion;
    public static Double totalDiferenciaReten = 0D;
    public static Double totalInteres;
    public static Double totalDescuento;

    //AGREGADO BDL 25/6
    public static String nro_doc_ref;
    //public int retorno_envases;
    public static List<MotivoPausa> ArrayMotivosPausa = new ArrayList<>();
    public static List<MotivoPausa> ArrayMotivosNoCobro = new ArrayList<>();
    public static List<MotivoPausa> ArrayReclamos = new ArrayList<>();
    public static List<ValoresPago> ArrayValores = new ArrayList<>();

    public static List<CajaCobranza> ArrayCajasCobranza = new ArrayList<>();

    public static List<Credito_Suspenso> ArrayCredSus = new ArrayList<>();

    public static List<Item> ArrayItemsViaje = new ArrayList<>();

    public static Double Total = 0D;
    public static int ivaTotal = 0;
    public static int total = 0;
    public static int iv10 = 0;
    public static int iv5 = 0;
    public static int TotSinIv = 0;
    // public static List<ClienteCobranza> ListaTextViews = new ArrayList<>();

    public static String retornoPausa;
    //Agregado para las pausas
    public static double lat_actual = 0.0;//Estos valores captura la ubicacion al momento de pausar o continuar el viaje
    public static double long_actual = 0.0, TotalFactura, IVA5, IVA10, Gravada5, Gravada10;
    public static LatLng ubicacionActual;
    public static double lat_origen = 0.0;
    public static double long_origen = 0.0;
    //Estos dos valores los traemos de la api de google disntanceMatrix
    public static double distancia_a_recorrer = 0D;
    public static String tiempo_estimado;
    //fin de agregado api google
    //Agregado para traer entrega x defecto
    public static Entrega entregaDefault = new Entrega();
    //Agregado para obtener la ubicacion del deposito trayendo lo del webService
    //public static LatLng COORDENADASDELDEPOSITO;
    public static Integer Dis1 = 0;
    public static String ultimaPausa = "";
    public static int ultimoMotivo = 0;
    public static double totalEntregado;
    public static boolean trajoCajas = false;
    public static boolean recibo_class = false;
    public static FactValidada n_factu = new FactValidada();
    public static FacturaXDia fact = new FacturaXDia();
    public static String respuestaWSImpresora;

    public static Item instaItem = new Item();

    //CAMBIAR A RECIBOVALIDADO
    public static FactValidada n_recibo = new FactValidada();

    public static boolean remito_guardado;
    public static String reto_AgregaFactura = "";
    public static String reto_AgregarReclamo = "";
    public static String err_ValidaFact = "";
    public static String reto_AgregaCobranza = "";
    public static String reto_prueba = "";
    public static String reto_Interes = "0";
    public static String cod_monedaInteres = "0";
    public static String reto_Observaciones = "";
    public static String reto_IngresarCierre = "";
    public static String retoRemito = "";
    public static Integer nro_trans;
    public static Integer nro_transRec;
    public static Integer nro_transObs;
    public static Integer nro_transCierreCaja;
    public static String nro_trans_impresion;
    public static Integer retoEnvase = 0;
    public static List<Anulada> ArrayAnuladas = new ArrayList<>();
    public static List<Anulada> ArrayRecibosAnuladas = new ArrayList<>();
    public static FacturaXDia facturaSeleccionada;
    public static boolean banderaAnuladas = false;
    public static boolean banderaRecibosAnuladas = false;
    public static boolean banderaValores = false;
    public static Usuario usuarioActual = new Usuario();
    //public static boolean errorConexion= false;

    public static String editable = "";

    //Datos para recomendar factura
    public static String cod_fac_trubut = "";
    public static String cod_suc_tribut = "";
    public static String nro_recibo = "";
    public static String nro_inicial = "";
    public static String nro_fin = "";
    public static String nro_sugerido = "";
    public static String nro_ingresado = "";
    public static ClienteCobranza clienteActual = new ClienteCobranza();
    //fin dataos recomendar
    //Cobranza
    public static List<ClienteCobranza> deudores = new ArrayList<>();
    public static List<ValoresPago> valores = new ArrayList<>();
    public static List<ValoresPago> valoresDocum = new ArrayList<>();
    public static List<Moneda> monedas = new ArrayList<>();
    public static Moneda monedaSeleccionada = new Moneda();
    public static Moneda monedaTransferencia = new Moneda();
    public static Moneda monedaCheque = new Moneda();
    public static Moneda monedaCredSusp = new Moneda();
    public static Moneda monedaTarjCred = new Moneda();
    public static Moneda monedaVuelto = new Moneda();
    public static Moneda monedaTarjDeb = new Moneda();

    public static String monedaTransferenciaUlti = "";
    public static String monedaChequeUlti = "";
    public static String monedaCredSuspUlti = "";
    public static String monedaTarjCredUlti = "";
    public static String monedaVueltoUlti = "";
    public static String monedaTarjDebUlti = "";

    public static TipoDoc doc = new TipoDoc();

    public static Credito_Suspenso CredSeleccionado = new Credito_Suspenso();

    //public static List<ClienteCobranza> deudas = new ArrayList<>();
    public static List<ClienteCobranza> deudasViaje = new ArrayList<>();
    public static List<ClienteCobranza> deudasMotNoCob = new ArrayList<>();
    public static List<ClienteCobranza> deudasFiltradasXMoneda = new ArrayList<>();
    public static List<ClienteCobranza> deudasSeleccionadas = new ArrayList<>();

    public static Configuracion_Empresa configuracion = new Configuracion_Empresa();
    public static List<ClienteCobranza> deudasSeleccionadasReten = new ArrayList<>();
    public static List<ClienteCobranza> deudasSeleccionadasInteres = new ArrayList<>();
    public static ArrayList<DeudaPagar> deudasPagarRetenc = new ArrayList<DeudaPagar>();
    //public static List<ValoresPago> deudaValorSeleccionada = new ArrayList<>();

    //Agregado para Cobranza
    public static List<ValoresPago> valoresPago = new ArrayList<>();
    public static ValoresPago pago = new ValoresPago();
    //Fin agregado

    //public static boolean EstadoValores = true;

    public static boolean distintasMonedas = false;
    public static NSucursal retornoSucursal;
    //public static int ing_factu_exito = 0;
    // A SyncHttpClient is an AsyncHttpClient
    //public static AsyncHttpClient syncHttpClient= new SyncHttpClient();
    //public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    RequestParams params1;
    public static String retoEnvases = "";
    public static double TotalPedido = 0;
    public static int LastIndexSelected = 0;
    public static int IndexViajes = 0;
    public static ArrayList<Entrega> EntregasAnteriores = new ArrayList<>();
    public static boolean ViajeActualSeleccionado = false;
    public static boolean ViajeActualSeleccionadoCobrador = false;
    public static int EstadoActual = 0;
    public static int EstadoAnterior = 0;
    //public static boolean ThreadCreado = false;
    public static String IdEmpresa = "";
    public static LatLng Retorno;
    public static ArrayList<FacturaXDia> ListaRetiros = new ArrayList<>();
    public static FacturaXDia RetiroSeleccionado;
    public static int lastRetiroSelected = 0;
    public static String respuestaGuardarCaja;
    public static String respuestaGuardarRecibo;
    public static String mensajeLogueo;
    public static boolean llegadaHabilitada = true;
    public static LatLng LastLocationSaved;
    public static boolean intereses = false;

    public static List<ClienteCobranza> clienteInteres = new ArrayList<>();
    /* ingresar cheques */

    public static List<Banco> bancos = new ArrayList<>();

    public static List<Moneda> monedasCheques = new ArrayList<>();
    public static List<TipoDoc> documentos = new ArrayList<>();

    public static List<TipoDoc> tiposDoc = new ArrayList<>();

    public static List<TarjetaCredito> tarjetasCredito = new ArrayList<>();
    public static List<TarjetaDebito> tarjetasDebito = new ArrayList<>();
    public static List<BancoPropio> bancosPropios = new ArrayList<>();

    public class NSucursal {
        public String NSucursal;

        public String getNSucursal() {
            return NSucursal;
        }

        public void setNSucursal(String NSucursal) {
            this.NSucursal = NSucursal;
        }
    }

    public static ClienteCobranza deudaMot = new ClienteCobranza();
    public static List<Efectivo> efectivo = new ArrayList<>();
    public static Double totalEfectivo = 0D;

    public static List<EfectivoUSD> efectivoUSD = new ArrayList<>();
    public static Double totalEfectivoUSD = 0D;

    public static List<Cheque> cheques = new ArrayList<>();
    public static Double totalCheques = 0D;

    public static Double totalCredSus = 0D;
    public static Double totalCredSusUSD = 0D;
    public static List<Credito_Suspenso> creditoSus = new ArrayList<>();


    public static List<Diferencia_Reten> dif_reten = new ArrayList<>();
    public static Double totalDif = 0D;
    public static Double totalDifACuenta = 0D;

    public static List<Vuelto> Vuelto = new ArrayList<>();
    public static Double totalVuelto = 0D;
    public static Double acuentaValores = 0D;
    public static Double acuentaValores2 = 0D;

    public static List<Retenciones> retenciones = new ArrayList<>();
    public static Double totalRetenciones = 0D;
    public static Double totalRetencionesUSD = 0D;

    public static List<TarjetaCredito> tarjetaCreditos = new ArrayList<>();
    public static Double totalTarjetaCredito = 0D;

    public static List<TarjetaDebito> tarjetaDebitos = new ArrayList<>();
    public static Double totalTarjetaDebito = 0D;

    public static List<Transferencias> transferencias = new ArrayList<>();
    public static Double totalTransferencias = 0D;


    public static Double totalDeudas = 0D;
    public static Double totalDeudas2 = 0D;
    public static Double diferenciaValores = 0D;

    public static Double totalValores = 0D;

    public static Double totalEfec = 0D;
    public static Double totalEfecUSD = 0D;
    public static Double totalCheque$ = 0D;
    public static Double totalChequeUSD = 0D;
    public static Double totalTran = 0D;
    public static Double totalTranUSD = 0D;
    public static Double totalReten = 0D;
    public static Double totalRetenUSD = 0D;
    public static Double totalDebi = 0D;
    public static Double totalDebiUSD = 0D;
    public static Double totalCred = 0D;
    public static Double totalCredUSD = 0D;

    public static Double tipoCambio = 0D;
    public static String simboloMonedaNacional = "$";
    public static String simboloMonedaTr = "";
    public static String codMonedaNacional = "";
    public static String codMonedaTr = "";

    public static int cantidadCopias;

    public static ArrayList<DeudaPagar> deudasPagar = new ArrayList<DeudaPagar>();
    public static ArrayList<DeudaPagar> deudasValorPagar = new ArrayList<DeudaPagar>();
    public static ArrayList<DeudaPagar> deudasInteres = new ArrayList<DeudaPagar>();

    ////////////////////////PEDIDOS/////////////////////////////

    public static String reto_AgregaPedido = "";
    public static List<ClienteCobranza> listEmpPedidos = new ArrayList<>();
    public static Entrega sucursalActualPedidos = new Entrega();
    public static List<Entrega> ArrayEntrega = new ArrayList<>();
    public static List<Entrega> ArraySucursalesEmp = new ArrayList<>();
    public static List<TipoDoc> listTipoDoc = new ArrayList<>();
    public static Pedido pedido = new Pedido();
    public static List<ProductosItem> ArrayItemsProductosPedido = new ArrayList<>();
    public static ProductosItem prodActualPedido = new ProductosItem();
    public static Presentacion presentacionActualPedido = new Presentacion();
    public static List<ProductosItem> listaProductosPedidos = new ArrayList<>();
    public static Entrega sucuEmpSelect = new Entrega();
    public static Double totalProductosPedidos = 0D;
    public static ProductosItem instaItemPed = new ProductosItem();
    ////////////////////////FIN PEDIDOS/////////////////////////////

    ////////////////////////FACTURA DIRECTA/////////////////////////////
    public static List<ClienteCobranza> ArrayClientes = new ArrayList<>();
    public static List<Entrega> ArraySucursales = new ArrayList<>();
    public static Entrega sucursalActual = new Entrega();
    public static Presentacion presentacionActual = new Presentacion();
    public static List<Item> ArrayItemsProductos = new ArrayList<>();
    public static List<Item> ArrayItemsProductosFactura = new ArrayList<>();
    public static List<Item> ArrayItemsProductosViaje = new ArrayList<>();
    public static Item prodActual = new Item();
    public static List<Item> listaProductos = new ArrayList<>();
    public static Double totalProductos = 0D;
    public static List<Presentacion> listPresentacion = new ArrayList<>();
    public static String retorno_stock = "";
    public static int cantidad_stock = 0;
    public static String cantidadString = "";

    public static void IngresarViaje(RequestParams params, String metodo) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo); //obtengo datos
        try {
            jsonReader.beginObject(); //leo objeto del array
            while (jsonReader.hasNext()) {
                String key = jsonReader.nextName();
                if (key.equals("resultado")) {
                    errToken = jsonReader.nextString();
                    if (errToken.toUpperCase().equals("OK")) {
                        errToken = "";
                    }
                    String paso = "";
                }

            }

        } catch (Exception exxc) {
            exxc.toString();
        }
    }

    public static void ActualizarUbicacion(RequestParams params, String metodo) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo); //obtengo datos
        try {
            jsonReader.beginObject(); //leo objeto del array
            while (jsonReader.hasNext()) {
                String key = jsonReader.nextName();
                if (key.equals("resultado")) {
                    errToken = jsonReader.nextString();
                    if (errToken.toUpperCase().equals("OK")) {
                        errToken = "";
                    }
                    String paso = "";
                }

            }

        } catch (Exception exxc) {
            exxc.toString();
        }
    }

    public static void BorrarArchivo(String metodo, RequestParams params) {
        SyncHttpClient cliente = new SyncHttpClient();
        //cliente.addHeader("Authorization", WebService.token);
        cliente.get(URL + metodo, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String p = "ok";

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    public static Integer PausarViaje(RequestParams params, String metodo) {
        try {
            JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo);
            jsonReader.beginObject(); // Start processing the JSON object
            while (jsonReader.hasNext()) { // Loop through all keys
                String key = jsonReader.nextName(); // Fetch the next key
                if (key.equals("resultado")) {
                    String valor = jsonReader.nextString();
                    if (valor.toUpperCase().equals("OK")) {
                        errToken = "";
                        retornoPausa = "1";
                    } else {
                        retornoPausa = "0";
                        errToken = valor;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return 0;
    }

    public static void TraerListaItems(RequestParams params, String metodo) {
        ArrayItemsViaje = new ArrayList<Item>();
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo); //obtengo datos
        try {
            jsonReader.beginArray();
            boolean nroEncontrado = false;
            errToken = "";
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                Item instaItem = new Item();
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName();
                    if (key.equals("nro_docum")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setNro_docum(0);
                        } else {
                            instaItem.setNro_docum(jsonReader.nextInt());
                        }
                    } else if (key.equals("cod_articulo")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setCod_Articulo("");
                        } else {
                            instaItem.setCod_Articulo(jsonReader.nextString());
                        }
                    } else if (key.equals("cod_art_gestion")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setCod_Art_Gestion("");
                        } else {
                            instaItem.setCod_Art_Gestion(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_articulo")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setNom_Articulo("");
                        } else {
                            instaItem.setNom_Articulo(jsonReader.nextString());
                        }
                    } else if (key.equals("cantidad")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setCantidad(0.0);
                        } else {
                            instaItem.setCantidad(jsonReader.nextDouble());
                        }
                    } else if (key.equals("precio_unitario")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setPrecio_Unitario(0.0);
                        } else {
                            instaItem.setPrecio_Unitario(jsonReader.nextDouble());
                        }
                    } else if (key.equals("cod_tasa_iva")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setCod_Tasa_Iva("");
                        } else {
                            instaItem.setCod_Tasa_Iva(jsonReader.nextString());
                        }
                    } else if (key.equals("cod_uni_vta")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setCod_uni_vta("");
                        } else {
                            instaItem.setCod_uni_vta(jsonReader.nextString());
                        }
                    } else if (key.equals("porc_iva")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setPorc_Iva("");
                        } else {
                            instaItem.setPorc_Iva(jsonReader.nextString());
                        }
                    } else if (key.equals("nro_doc_ref") && !nroEncontrado) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            Entrega_A_Realizar.setNro_doc_ref(0);
                        } else {
                            Entrega_A_Realizar.setNro_doc_ref(Integer.parseInt(jsonReader.nextString()));
                            nroEncontrado = true;
                        }
                    } else if (key.equals("porc_desc")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setPorc_desc(0.0);
                        } else {
                            instaItem.setPorc_desc(jsonReader.nextDouble());
                        }
                    } else if (key.equals("imp_descto")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setImp_descto(0.0);
                        } else {
                            instaItem.setImp_descto(jsonReader.nextDouble());
                        }
                    } else if (key.equals("precio_presup")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setPrecio_presup(0.0);
                        } else {
                            instaItem.setPrecio_presup(jsonReader.nextDouble());
                        }
                    } else {
                        jsonReader.skipValue();
                    }

                }
                jsonReader.endObject();
                ArrayItemsViaje.add(instaItem);

            }
            jsonReader.endArray();

        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
        //Funciona en servidores apache y iis en privado y en api mayor a 26 comentado el 26-10-2018
        /*
        SyncHttpClient cliente = new SyncHttpClient(  );
        cliente.get(URL + metodo, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                try
                {
                    ArrayItemsViaje = new ArrayList<Item>();
                    String respuesta =  new String(responseBody,"UTF-8");
                    objResponseA = new JSONArray(respuesta);
                    JSONObject objetojSon= new JSONObject();
                    if(objResponseA.length()!=0)
                    {
                        for (int i=0;i<objResponseA.length();i++)
                        {
                            objetojSon = objResponseA.getJSONObject(i);
                            Item n_Item = new Item();
                            if(!objetojSon.getString( "nro_docum" ).equals(""))
                            {
                                n_Item.setNro_docum( Integer.valueOf(objetojSon.getString( "nro_docum" ) ));
                            }
                            else
                            {
                                n_Item.setNro_docum( 0 );
                            }
                            if(!objetojSon.getString( "cod_articulo" ).equals( ""))
                            {
                                n_Item.setCod_Articulo( objetojSon.getString( "cod_articulo" ) );
                            }
                            else
                            {
                                n_Item.setCod_Articulo( "" );
                            }
                            if(!objetojSon.getString( "cod_art_gestion" ).equals(""))
                            {
                                n_Item.setCod_Art_Gestion( objetojSon.getString( "cod_art_gestion" ) );
                            }
                            else
                            {
                                n_Item.setCod_Art_Gestion( "" );
                            }
                            if(!objetojSon.getString("nom_articulo").equals(""))
                            {
                                n_Item.setNom_Articulo( objetojSon.getString("nom_articulo") );
                            }
                            else
                            {
                                n_Item.setNom_Articulo( "" );
                            }
                            if(!objetojSon.getString( "cantidad" ).equals(""))
                            {
                                n_Item.setCantidad( Double.valueOf( objetojSon.getString( "cantidad" ) ) );
                            }
                            else
                            {
                                n_Item.setCantidad( 0.0 );
                            }
                            if(!objetojSon.getString( "precio_unitario" ).equals(""))
                            {
                                n_Item.setPrecio_Unitario( Double.valueOf(objetojSon.getString( "precio_unitario" )) );
                            }
                            else
                            {
                                n_Item.setPrecio_Unitario( 0.0 );
                            }
                            if(!objetojSon.getString( "cod_tasa_iva" ).equals(""))
                            {
                                n_Item.setCod_Tasa_Iva( objetojSon.getString( "cod_tasa_iva" ) );
                            }
                            else
                            {
                                n_Item.setCod_Tasa_Iva( "" );
                            }
                            if(!objetojSon.getString( "porc_iva" ).equals(""))
                            {
                                n_Item.setPorc_Iva( objetojSon.getString( "porc_iva" ) );
                            }
                            else
                            {
                                n_Item.setPorc_Iva( "" );
                            }
                            if(!objetojSon.getString( "cod_uni_vta" ).equals("") && !objetojSon.getString( "cod_uni_vta" ).equals( "null" ))
                            {
                                n_Item.setCod_uni_vta( objetojSon.getString( "cod_uni_vta" ) );
                            }
                            else
                            {
                                n_Item.setCod_uni_vta( "" );
                            }
                            ArrayItemsViaje.add( n_Item );
                        }
                    }
                }
                catch (Exception exc)
                {
                    exc.toString();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String dioError = "que bajon";
            }
        });*/

    }

    public static void TraerViajes(RequestParams params, String metodo) {
        SyncHttpClient cliente = new SyncHttpClient();
        String resp = "";
        cliente.addHeader("Authorization", WebService.token);
        cliente.get(URL + metodo, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    viajesUsu = new ArrayList<>();
                    String respuesta = new String(responseBody, "UTF-8");
                    objResponseA = new JSONArray(respuesta);
                    JSONObject objetojSon;
                    Viaje InstanciaViaje = new Viaje();
                    errToken = "";
                    if (objResponseA.length() != 0) {
                        for (int i = 0; i < objResponseA.length(); i++) {
                            objetojSon = objResponseA.getJSONObject(i);
                            double latitud = 0.0;
                            double longitud = 0.0;
                            if (objetojSon.getString("turno").toString() != "") {
                                InstanciaViaje.setTurno(objetojSon.getString("turno").toString().trim());
                            }
                            if (objetojSon.getString("nro_viaje").toString() != "") {
                                InstanciaViaje.setNumViaje(objetojSon.getString("nro_viaje").toString().trim());
                            }
                            if (objetojSon.getString("tipo").toString() != "") {
                                InstanciaViaje.setTipo(objetojSon.getString("tipo").toString().trim());
                            }
                            latitud = objetojSon.getDouble("latitud");
                            longitud = objetojSon.getDouble("longitud");
                            if (WebService.usuarioActual.getEs_VentaDirecta().equals("S")) {
                                lat_actual = objetojSon.getDouble("longitud");
                                long_actual = objetojSon.getDouble("longitud");
                            }
                            LatLng localizacion = new LatLng(latitud, longitud);
                            InstanciaViaje.setLocalizacionCentral(localizacion);
                            viajesUsu.add(InstanciaViaje);
                            InstanciaViaje = new Viaje();
                        }
                    }
                } catch (Exception exc) {
                    try {
                        String respuesta = new String(responseBody, "UTF-8");
                        JSONObject objetojSon = new JSONObject(respuesta);
                        errToken = objetojSon.getString("error");
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    } catch (Exception ex) {

                    }
                    exc.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
            }
        });
    }

    public static void TrearMotivosPausa(String metodo) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRestSinParam(metodo); //obtengo datos
        try {
            ArrayMotivosPausa = new ArrayList<>();
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                MotivoPausa pausa = new MotivoPausa();
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName();
                    if (key.equals("cod_motivopausa")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            pausa.setCod_Pausa("");

                        } else {
                            pausa.setCod_Pausa(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_motivopausa")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            pausa.setNom_Pausa("");
                        } else {
                            pausa.setNom_Pausa(jsonReader.nextString());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                ArrayMotivosPausa.add(pausa);
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();

        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
        //El codigo comentado funciona para servidores apache se uso hasta el 25-10-2018
       /* SyncHttpClient cliente = new SyncHttpClient(  );
        cliente.get( URL + metodo, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                ArrayMotivosPausa= new ArrayList<>(  );
                try
                {
                    String respuesta =  new String(responseBody,"UTF-8");
                    objResponseA = new JSONArray(respuesta);
                    JSONObject objetojSon= new JSONObject();
                    if(objResponseA.length()>0)
                    {
                        for(int i = 0;i<objResponseA.length();i++)
                        {
                            objetojSon = objResponseA.getJSONObject(i);
                            MotivoPausa n_MotPausa = new MotivoPausa();
                            if(objetojSon.getString( "cod_motivopausa" ).toString()!="")
                            {
                                n_MotPausa.setCod_Pausa(objetojSon.getString( "cod_motivopausa" ).toString());
                            }
                            if(objetojSon.getString( "nom_motivopausa" ).toString()!="")
                            {
                                n_MotPausa.setNom_Pausa( objetojSon.getString( "nom_motivopausa" ).toString() );
                            }
                            ArrayMotivosPausa.add( n_MotPausa );
                        }
                    }
                    else
                    {

                    }
                }
                catch (Exception exc )
                {
                    exc.toString();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String dioError = "dio error";
            }
        } );*/
    }

    public static void ObtenerConfig(String metodo, RequestParams parms) {
        //usuarioActual.setEmpresa(""); //inicializo vacío para evitar error de null
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(parms, metodo); //obtengo datos
        try {
            if (jsonReader != null) {
                jsonReader.beginArray(); //leo array
                errToken = "";
                while (jsonReader.hasNext()) {
                    jsonReader.beginObject(); //leo objeto del array
                    while (jsonReader.hasNext()) {
                        String key = jsonReader.nextName();
                        if (key.equals("cod_empresa")) {
                            usuarioActual.setEmpresa(jsonReader.nextString());
                        } else {
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.endObject(); //fin objeto
                }

                jsonReader.endArray(); //fin array
                jsonReader.close(); //cierro reader
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                ex.toString();
            } catch (Exception exct) {

            }
        }
    }

    public static void ListaEntregasViaje(RequestParams params, String metodo) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo); //obtengo datos
        entregasTraidas = new ArrayList<>();
        ListaRetiros = new ArrayList<>();
        try {
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                Entrega InstanciaEntrega = new Entrega();
                FacturaXDia FacturaActual = new FacturaXDia();
                jsonReader.beginObject();
                while (jsonReader.hasNext()) { // Loop through all keys
                    if (viajeSeleccionado.getTipo().equals("entrega")) {
                        String key = jsonReader.nextName();
                        if (key.equals("fec_doc")) {
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                InstanciaEntrega.setFec_Doc(jsonReader.nextString());
                            }
                        } else if (key.equals("cod_tit_gestion")) {
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                InstanciaEntrega.setCod_Tit_Gestion(jsonReader.nextString().trim());
                            }
                        } else if (key.equals("cod_tit")) {
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                InstanciaEntrega.setCod_Tit(jsonReader.nextString().trim());
                            }
                        } else if (key.equals("nom_tit")) {
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                InstanciaEntrega.setNom_Tit(jsonReader.nextString().trim());
                            }
                        } else if (key.equals("cod_sucursal")) {
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                InstanciaEntrega.setCod_Sucursal(jsonReader.nextString().trim());
                            }
                        } else if (key.equals("nom_sucursal")) {
                            InstanciaEntrega.setNom_Sucursal(jsonReader.nextString().trim());
                        } else if (key.equals("direccion")) {
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                InstanciaEntrega.setDireccion(jsonReader.nextString().trim());
                            }
                        } else if (key.equals("nro_docum")) {
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                InstanciaEntrega.setNro_Docum(jsonReader.nextString().trim());
                            }
                        } else if (key.equals("latitud_ubic")) {
                            Double lat = 0.0; //inicializo para evitar error
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                                InstanciaEntrega.setLatiud_Ubic(lat);
                            } else {
                                lat = jsonReader.nextDouble();
                                InstanciaEntrega.setLatiud_Ubic(lat);
                            }
                        } else if (key.equals("longitud_ubic")) {
                            Double lon = 0.0; //inicializo para evitar error
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                                InstanciaEntrega.setLongitud_Ubic(lon);
                            } else {
                                lon = jsonReader.nextDouble();
                                InstanciaEntrega.setLongitud_Ubic(lon);
                            }
                        } else if (key.equals("hora_desde_ent")) {
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                InstanciaEntrega.setHora_Desde(jsonReader.nextString().trim());
                            }
                        } else if (key.equals("hora_hasta_ent")) {
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                InstanciaEntrega.setHora_Hasta(jsonReader.nextString().trim());
                            }
                        } else if (key.equals("prioridad")) {
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                InstanciaEntrega.setPrioridad(jsonReader.nextString().trim());
                            }
                        } else if (key.equals("cant_facts")) {
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                InstanciaEntrega.setCant_Facturas(jsonReader.nextInt());
                            }
                        } else if (key.equals("nro_doc_ref")) {
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                InstanciaEntrega.setNro_doc_ref(jsonReader.nextInt());
                            }
                        }//Agregado 17/07/2019 a pedido de cliente BDL
                        else if (key.equals("observaciones")) {
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                InstanciaEntrega.setObservaciones(jsonReader.nextString().trim());
                            }
                        } else if (key.equals("cod_emp")) {
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                InstanciaEntrega.setCodEmp(jsonReader.nextString().trim());
                            }
                        } else if (key.equals("tipo")) {
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                InstanciaEntrega.setTipo(jsonReader.nextString().trim());
                            }
                        } else if (key.equals("descuento")) {
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                InstanciaEntrega.setDescuento(0.0);
                                jsonReader.skipValue();
                            } else {
                                InstanciaEntrega.setDescuento(jsonReader.nextDouble());
                            }
                        } else if (key.equals("cod_doc_uni")) {
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                InstanciaEntrega.setCodigoDocumentoCliente(jsonReader.nextString().trim());
                            }
                        } else if (key.equals("cod_moneda")) {
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                InstanciaEntrega.setCodigoMoneda(jsonReader.nextString().trim());
                            }
                        } else if (key.equals("cod_fpago")) {
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                InstanciaEntrega.setCodigoTipoPago(jsonReader.nextString().trim());
                            }
                        } else {
                            jsonReader.skipValue();
                        }
                    } else if (viajeSeleccionado.getTipo().equals("retiro")) {
                        JsonToken check = jsonReader.peek();
                        String key;
                        key = check == JsonToken.NAME ? jsonReader.nextName() : jsonReader.nextString();
                        if (key.equals("cod_tit")) {
                            //Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                FacturaActual.setCod_Tit(jsonReader.nextString().trim());
                            }

                        } else if (key.equals("cod_sucursal")) {
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                FacturaActual.setCod_Sucursal(jsonReader.nextString().trim());
                            }

                        } else if (key.equals("nro_trans")) {
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                FacturaActual.setNro_trans_ref(jsonReader.nextString().trim());
                            }
                        } else if (key.equals("nro_docum")) {
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                FacturaActual.setNro_Docum(jsonReader.nextString().trim());
                            }
                        } else if (key.equals("cod_tit_gestion")) {
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                FacturaActual.setCod_Tit_Gestion(jsonReader.nextString().trim());
                            }
                        } else if (key.equals("nom_sucursal")) {
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                FacturaActual.setSucursal(jsonReader.nextString().trim());
                            }
                        } else if (key.equals("nro_doc_ref")) {
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                            } else {
                                FacturaActual.setNro_Doc_Ref(jsonReader.nextString().trim());
                            }
                        }

                        FacturaActual.setNro_Trans("0");
                    }
                }
                if (viajeSeleccionado.getTipo().equals("entrega")) {
                    entregasTraidas.add(InstanciaEntrega);
                } else if (viajeSeleccionado.getTipo().equals("retiro")) {
                    ListaRetiros.add(FacturaActual);
                }
                jsonReader.endObject(); //fin objeto
            }
            jsonReader.endArray(); //fin array
            jsonReader.close(); //cierro reader
        } catch (Exception e) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                e.toString();
            } catch (Exception exct) {

            }
        }
    }

    public static void TraerListaAnuladas(RequestParams params, String metodo) {
        ArrayAnuladas = new ArrayList<Anulada>();
        SyncHttpClient cliente = new SyncHttpClient();
        cliente.addHeader("Authorization", WebService.token);
        cliente.get(URL + metodo, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                banderaAnuladas = true;
                try {
                    String respuesta = new String(responseBody, "UTF-8");
                    objResponseA = new JSONArray(respuesta);
                    errToken = "";
                    Anulada instanciaAnulada = new Anulada();
                    JSONObject objetojSon;
                    if (objResponseA.length() > 0) {
                        for (int i = 0; i < objResponseA.length(); i++) {
                            instanciaAnulada = new Anulada();
                            objetojSon = objResponseA.getJSONObject(i);
                            if (objetojSon.getString("cod_deposito") != null) {
                                instanciaAnulada.setCod_deposito(objetojSon.getString("cod_deposito"));
                            }
                            if (objetojSon.getString("nro_trans") != null) {
                                instanciaAnulada.setNro_trans(objetojSon.getString("nro_trans"));
                            }
                            if (objetojSon.getString("nro_doc_ref") != null) {
                                instanciaAnulada.setNro_doc_ref(objetojSon.getString("nro_doc_ref"));
                            }
                            if (objetojSon.getString("cod_suc_tribut") != null) {
                                instanciaAnulada.setCod_suc_tribut(objetojSon.getString("cod_suc_tribut"));
                            }
                            if (objetojSon.getString("cod_fac_tribut") != null) {
                                instanciaAnulada.setCod_fact_tribut(objetojSon.getString("cod_fac_tribut"));
                            }
                            if (objetojSon.getString("nro_docum") != null) {
                                instanciaAnulada.setNro_docum(objetojSon.getString("nro_docum"));
                            }
                            if (objetojSon.getString("nom_tit") != null) {
                                instanciaAnulada.setNom_tit(objetojSon.getString("nom_tit"));
                            }
                            if (objetojSon.getString("hora") != null) {
                                instanciaAnulada.setHora(objetojSon.getString("hora"));
                            }
                            if (objetojSon.getString("cod_sucursal") != null) {
                                instanciaAnulada.setCod_sucursal(objetojSon.getString("cod_sucursal"));
                            }
                            if (objetojSon.getString("sucursal") != null) {
                                instanciaAnulada.setSucursal(objetojSon.getString("sucursal"));
                            }
                            if (objetojSon.getString("cod_tit_gestion") != null) {
                                instanciaAnulada.setCod_tit_gestion(objetojSon.getString("cod_tit_gestion"));
                            }
                            if (objetojSon.getString("cod_tit") != null) {
                                instanciaAnulada.setCod_tit(objetojSon.getString("cod_tit"));
                            }
                            ArrayAnuladas.add(instanciaAnulada);
                        }
                    }
                } catch (Exception exc) {
                    try {
                        String respuesta = new String(responseBody, "UTF-8");
                        JSONObject objetojSon = new JSONObject(respuesta);
                        errToken = objetojSon.getString("error");
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    } catch (Exception ex) {

                    }
                    banderaAnuladas = false;
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                banderaAnuladas = false;
            }
        });

    }

    public static void AnularFactura(RequestParams params, String metodo) {
        banderaAnuladas = false;
        try {
            JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo);
            jsonReader.beginObject(); // Start processing the JSON object
            while (jsonReader.hasNext()) { // Loop through all keys
                String key = jsonReader.nextName(); // Fetch the next key
                if (key.equals("resultado")) {
                    String valor = jsonReader.nextString();
                    if (valor.equals("ok")) {
                        errToken = "";
                        banderaAnuladas = true;
                    } else {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                        banderaAnuladas = false;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            banderaAnuladas = false;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            banderaAnuladas = false;
        }
    }

    public static void ErrorCatcher(final RequestParams params, String metodo) {
        try {
            JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void Logueo(final RequestParams params, String metodo) {
        try {
            JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo);
            jsonReader.beginObject(); // Start processing the JSON object

            while (jsonReader.hasNext()) { // Loop through all keys
                String key = jsonReader.nextName(); // Fetch the next key
                if (key.equals("success")) {
                    boolean value = jsonReader.nextBoolean();
                    if (value) {
                        logueado = true;
                        USUARIOLOGEADO = usuParam.trim();
                        usuarioActual = new Usuario();
                        usuarioActual.setNombre(usuParam);
                        usuarioActual.setActivo(true);
                        usuarioActual.setEmpresa("1"); //valor por defecto por si da error en identificador
                    } else {
                        WebService.USUARIOLOGEADO = "";
                    }
                } else if (key.equals("identificador")) {
                    /*jsonReader.beginArray();
                    jsonReader.beginObject();
                    jsonReader.nextName();*/
                    IdEmpresa = jsonReader.nextString();
                    if (!IdEmpresa.equals("0")) {
                        usuarioActual.setEmpresa(IdEmpresa);
                    }
                } else if (key.equals("message")) {
                    mensajeLogueo = jsonReader.nextString();
                } else if (key.equals("token")) {
                    token = jsonReader.nextString();
                } else {
                    jsonReader.skipValue(); // Skip values of other keys
                }
            }
            jsonReader.close();
            /*if (logueado){
                RequestParams params1 = new RequestParams();
                params1.put( "username",WebService.usuarioActual.getNombre() );
                TraerPermisos(params,"session/TraerPermisos.php");
                if (usuarioActual.getEs_Cobranza().equals("S")){
                    WebService.ObtenerConfig("Cobranzas/TraerConfiguracion.php",params1);
                } else if (usuarioActual.getEs_Entrega().equals("S")){
                    WebService.TraerListaViajes(params1,"Viajes/Viajes.php");
                }
            }*/
        } catch (Exception exxx) {
            exxx.printStackTrace();
        }
        //RP 16/04/2019 paso acá porque puede dar error pero debe seguir ya que no obtuvo array en idenficador
        try {
            if (logueado) {
                RequestParams params1 = new RequestParams();
                params1.put("username", WebService.usuarioActual.getNombre());
                //TraerPermisos(params, "session/TraerPermisos2.php");
                TraerPermisos(params, "Session/TraerPermisos.php");
                if (usuarioActual.getEs_Cobranza().equals("S")) {
                    WebService.ObtenerConfig("Cobranzas/TraerConfiguracion.php", params1);
                    // WebService.TraerViajesCobrador(params1, "Viajes/ViajesCobrador/ViajesCobrador.php");
                } else if (usuarioActual.getEs_Entrega().equals("S")) {
                    WebService.TraerListaViajes(params1, "Viajes/Viajes.php");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public static void TraerListaViajes(RequestParams params, String metodo) {
        viajesUsu = new ArrayList<>();
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo);
        try {
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                Viaje InstanciaViaje = new Viaje();
                jsonReader.beginObject();
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName();
                    double latitud = 0.0;
                    double longitud = 0.0;
                    if (key.equals("turno")) {
                        InstanciaViaje.setTurno(jsonReader.nextString());
                    } else if (key.equals("nro_viaje")) {
                        InstanciaViaje.setNumViaje(jsonReader.nextString());
                    } else if (key.equals("tipo")) {
                        InstanciaViaje.setTipo(jsonReader.nextString());
                    } else if (key.equals("latitud")) {
                        latitud = jsonReader.nextDouble();
                    } else if (key.equals("longitud")) {
                        longitud = jsonReader.nextDouble();
                    } else {
                        jsonReader.skipValue();
                    }
                    LatLng localizacion = new LatLng(latitud, longitud);
                    InstanciaViaje.setLocalizacionCentral(localizacion);
                }
                viajesUsu.add(InstanciaViaje);
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();
        } catch (IOException e) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                e.toString();
            } catch (Exception exct) {
                System.out.println(exct.toString());
            }
        }
    }

    public static void TraerPermisos(RequestParams param, String Metodo) {
        usuarioActual.setEs_Cobranza("N");
        usuarioActual.setEs_Entrega("N");
        usuarioActual.setEs_VentaDirecta("N");
        usuarioActual.setEs_Pedidos("N");
        //usuarioActual.setTipoCobrador("N");
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(param, Metodo);
        try {
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName();
                    if (key.equals("es_cobranza")) {
                        usuarioActual.setEs_Cobranza(jsonReader.nextString());
                    } else if (key.equals("es_entrega")) {
                        usuarioActual.setEs_Entrega(jsonReader.nextString());
                    } else if (key.equals("tipo_usucob")) {//AGREGADO PARA TIPO DE USUARIO DE COBRANZA
                        usuarioActual.setTipoCobrador(jsonReader.nextString());
                    } else if (key.equals("es_ventadir")) {//AGREGADO PARA TIPO DE USUARIO DE COBRANZA
                        usuarioActual.setEs_VentaDirecta(jsonReader.nextString());
                    } else if (key.equals("toma_ped")) {//AGREGADO PARA TIPO DE USUARIO ES PEDIDOS
                        usuarioActual.setEs_Pedidos(jsonReader.nextString());
                    } else {
                        jsonReader.skipValue();
                    }
                }
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();
        } catch (IOException e) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                e.toString();
            } catch (Exception exct) {
                System.out.println(exct.toString());
            }
        }

    }

    public static Entrega traerEntregaXNroOrden(String p_NroOrden) {
        Entrega n_Entrega = new Entrega();
        boolean esEste = false;
        for (int i = 0; i < entregasTraidas.size(); i++) {
            if (!esEste) {
                if (p_NroOrden.equals(entregasTraidas.get(i).getNro_Docum())) {
                    esEste = true;
                    Entrega_A_Realizar = entregasTraidas.get(i);
                }
            }
        }
        return n_Entrega;
    }

    public static void TraerEntregaMasCercana(ArrayList<String> ruta) {
        int i = 0;
        SyncHttpClient client = new SyncHttpClient();
        try {

            for (String s : ruta) {/*

                cliente.addHeader("Authorization", WebService.token);
                client.get(URL + metodo, parametrosIng, new AsyncHttpResponseHandler() {
                client.get(s , new AsyncHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try
                        {
                            String response = new String(responseBody,"UTF-8");
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString( "status" )!=null)
                            {
                                if(obj.getString( "status" ).equals( "OK" ))
                                {
                                    if(obj.getString( "distance" )!=null)
                                    {
                                        distancia_a_recorrer = obj.getString( "text" );

                                    }
                                }
                            }

                        }
                        catch (Exception ex)
                        {
                            ex.toString();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });*/

                /*Entrega entre = entregasTraidas.get( i );
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();

                HttpGet httpGet = new HttpGet(s);
                HttpResponse response = httpClient.execute(httpGet, localContext);
                String responseBody = EntityUtils.toString(response.getEntity());
                String[] parts = responseBody.split("\n");
                distancia_a_recorrer = parts[8].replace( " ","" ).replace( "text","" ).replace( ",","" ).replace( ":","" ).replace( "\"","" );
                Double distancia = Double.valueOf( distancia_a_recorrer.replace( "km","" ) );
                entre.setDistancia( distancia );
                if(Dis1 == 0) {
                    entregaDefault = entre;
                    Dis1 =1;
                }
                if(entre.getDistancia()<entregaDefault.getDistancia()) {
                    entregaDefault = entre;
                }
                i++;*/
            }

            //tiempo_estimado = parts[12].replace( " ","" ).replace( "text","" ).replace( ",","" ).replace( ":","" ).replace( "\"","" ).replace( "hours","horas " );

        } catch (Exception ex) {
            ex.toString();
        }

    }

    public static void TraerDistancia(String ruta) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();

            HttpGet httpGet = new HttpGet(ruta);
            HttpResponse response = httpClient.execute(httpGet, localContext);

            //response.addHeader("Authorization", WebService.token);

            String distancia = getTextValue(response, "distance");
            System.out.println(distancia);
            //String responseBody = EntityUtils.toString(response.getEntity());
            //String[] parts = responseBody.split("\n");
            //distancia_a_recorrer = Double.parseDouble(parts[29].replace( " ","" ).replace( "text","" ).replace( ",","" ).replace( ":","" ).replace( "\"","" ).replace( "km","" ));
            //tiempo_estimado = parts[33].replace( " ","" ).replace( "text","" ).replace( ",","" ).replace( ":","" ).replace( "\"","" ).replace( "hours","horas " );
            distancia_a_recorrer = Math.round(Double.parseDouble(distancia) / 1000);

            tiempo_estimado = "0"; //por ahora por defecto, luego obtener
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getTextValue(HttpResponse response, String tag) {

        String value = "0";
        Document dom;
        String xml = "";
        // Make an  instance of the DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            for (String line = null; (line = reader.readLine()) != null; ) {
                builder.append(line).append("\n");
            }
            JSONTokener tokener = new JSONTokener(builder.toString());
            JSONObject obj = new JSONObject(tokener);
            Iterator<String> keys = obj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                if (key.equals("routes")) {
                    JSONArray jArray = obj.getJSONArray("routes");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        JSONArray jArray2 = json_data.getJSONArray("legs");
                        JSONObject obj2 = jArray2.getJSONObject(0);
                        JSONObject obj3 = obj2.getJSONObject(tag);
                        value = obj3.getString("value");
                        System.out.print(value);
                    }
                    //Object keyvalue = obj.get(tag);
                }
            }
            return value;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return value;
    }

    public static void ConsultarEstadoViaje(String metodo, RequestParams params) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo);
        try {
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("en_pausa")) {
                        viajeSeleccionado.setEstado(jsonReader.nextString());
                    } else {
                        jsonReader.skipValue();
                        viajeSeleccionado.setEstado("1");
                    }
                }
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();
        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }

    public static void TraerFacturasXDia(String metodo, RequestParams parms) {
        SyncHttpClient client = new SyncHttpClient();
        client.addHeader("Authorization", WebService.token);
        client.get(URL + metodo, parms, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    FacturaXDia instanciaFact = new FacturaXDia();
                    listafacturas = new ArrayList<>();
                    listaRemitos = new ArrayList<>();
                    facturasDelDia = new ArrayList<FacturaXDia>();
                    String respuesta = new String(responseBody, "UTF-8");
                    objResponseA = new JSONArray(respuesta);
                    errToken = "";
                    JSONObject objetojSon = new JSONObject();
                    if (objResponseA.length() > 0) {
                        contadorFactura = 0;
                        contadorRemito = 0;
                        for (int i = 0; i < objResponseA.length(); i++) {
                            instanciaFact = new FacturaXDia();
                            objetojSon = objResponseA.getJSONObject(i);
                            if (objetojSon.getString("tipo").toString() != "" && objetojSon.getString("tipo") != null) {
                                instanciaFact.setTipo(objetojSon.getString("tipo").toString());

                            }
                            if (objetojSon.getString("cod_deposito").toString() != "" && objetojSon.getString("cod_deposito") != null) {
                                instanciaFact.setCod_Deposito(objetojSon.getString("cod_deposito").toString());
                            }
                            if (objetojSon.getString("nro_trans").toString() != "" && objetojSon.getString("nro_trans") != null) {
                                instanciaFact.setNro_Trans(objetojSon.getString("nro_trans").toString());
                            }
                            if (objetojSon.getString("nro_doc_ref").toString() != "" && objetojSon.getString("nro_doc_ref") != null) {
                                instanciaFact.setNro_Doc_Ref(objetojSon.getString("nro_doc_ref").toString());
                            }
                            if (objetojSon.getString("cod_suc_tribut").toString() != "" && objetojSon.getString("cod_suc_tribut") != null) {
                                instanciaFact.setCod_Suc_Tribut(objetojSon.getString("cod_suc_tribut").toString());
                            }
                            if (objetojSon.getString("cod_fac_tribut").toString() != "" && objetojSon.getString("cod_fac_tribut") != null) {
                                instanciaFact.setCod_Fac_Tribut(objetojSon.getString("cod_fac_tribut").toString());
                            }
                            if (objetojSon.getString("nro_docum").toString() != "" && objetojSon.getString("nro_docum") != null) {
                                instanciaFact.setNro_Docum(objetojSon.getString("nro_docum").toString());
                            }
                            if (objetojSon.getString("nom_tit").toString() != "" && objetojSon.getString("nom_tit") != null) {
                                instanciaFact.setNom_Tit(objetojSon.getString("nom_tit").toString());
                            }
                            if (objetojSon.getString("hora").toString() != "" && objetojSon.getString("hora") != null) {
                                instanciaFact.setHora(objetojSon.getString("hora").toString());
                            }
                            if (objetojSon.getString("cod_tit_gestion").toString() != "" && objetojSon.getString("cod_tit_gestion") != null) {
                                instanciaFact.setCod_Tit_Gestion(objetojSon.getString("cod_tit_gestion").toString());
                            }
                            if (objetojSon.getString("cod_sucursal").toString() != "" && objetojSon.getString("cod_sucursal") != null) {
                                instanciaFact.setCod_Sucursal(objetojSon.getString("cod_sucursal").toString());
                            }
                            if (objetojSon.getString("cod_tit").toString() != "" && objetojSon.getString("cod_tit") != null) {
                                instanciaFact.setCod_Tit(objetojSon.getString("cod_tit").toString());
                            }
                            if (objetojSon.getString("sucursal").toString() != "" && objetojSon.getString("sucursal") != null) {
                                instanciaFact.setSucursal(objetojSon.getString("sucursal").toString());
                            }
                            if (objetojSon.getString("tipo_docum").toString() != "" && objetojSon.getString("tipo_docum") != null) {
                                instanciaFact.setTipo_docum(objetojSon.getString("tipo_docum").toString());
                            }
                            if (instanciaFact.getTipo().trim().toUpperCase().equals("R")) {
                                contadorRemito++;
                                listaRemitos.add(instanciaFact);
                            } else {
                                contadorFactura++;
                                listafacturas.add(instanciaFact);
                            }
                            facturasDelDia.add(instanciaFact);
                        }
                    }
                } catch (Exception ex) {
                    try {
                        String respuesta = new String(responseBody, "UTF-8");
                        JSONObject objetojSon = new JSONObject(respuesta);
                        errToken = objetojSon.getString("error");
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    } catch (Exception ecx) {

                    }
                    ex.toString();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String er1 = error.toString();
            }
        });
    }

    public static void TraerTipoCajas(String metodo) {
        SyncHttpClient client = new SyncHttpClient();
        client.addHeader("Authorization", WebService.token);
        client.get(URL + metodo, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    tipoCajas = new ArrayList<>();
                    Caja instanciaCaja = new Caja();
                    String respuesta = new String(responseBody, "UTF-8");
                    trajoCajas = true;
                    objResponseA = new JSONArray(respuesta);
                    errToken = "";
                    JSONObject objetojSon = new JSONObject();
                    int size = objResponseA.length();
                    for (int i = 0; i < objResponseA.length(); i++) {
                        instanciaCaja = new Caja();
                        objetojSon = objResponseA.getJSONObject(i);
                        if (objetojSon.getString("nom_tipo") != null) {
                            instanciaCaja.setNom_Caja(objetojSon.getString("nom_tipo"));
                        }
                        if (objetojSon.getString("cod_tipo") != null) {
                            instanciaCaja.setId_TipoCaja(objetojSon.getString("cod_tipo"));
                        }
                        tipoCajas.add(instanciaCaja);
                    }
                } catch (Exception exc) {
                    try {
                        String respuesta = new String(responseBody, "UTF-8");
                        JSONObject objetojSon = new JSONObject(respuesta);
                        errToken = objetojSon.getString("error");
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    } catch (Exception ex) {

                    }
                    exc.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public static void GenerarHtml(String metodo, RequestParams params) {
        System.out.println(metodo);
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo);

        try {
            jsonReader.setLenient(true);
            jsonReader.beginObject(); // Start processing the JSON object
            jsonReader.endObject();
            jsonReader.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
       /* SyncHttpClient client = new SyncHttpClient(  );
        client.get( URL+metodo,params,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String status ="";
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String string;
            }
        });*/
    }


    public static FacturaZebra GenerarHtmlZebra(String metodo, RequestParams params) {
        System.out.println(metodo);
        //JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo);
        JSONArray jsonArray = WebServiceRes.jsObjectRespuestaWSRest(params, metodo);
        FacturaZebra factura = null;
        ArrayList<FacturaItemZebra> items = new ArrayList<FacturaItemZebra>();
        try {

            org.json.JSONObject object = (org.json.JSONObject) jsonArray.get(0);
            //object.get("dir_empresa");
            org.json.JSONObject objectDetalle = (org.json.JSONObject) jsonArray.get(1);

            org.json.JSONArray objectsItem = (org.json.JSONArray) objectDetalle.get("detalle");
            int i = 0;
            while (objectsItem.length() > i) {
                org.json.JSONObject objectItem = (org.json.JSONObject) objectsItem.get(i);

                items.add(new FacturaItemZebra(objectItem.get("cant_aprobada").toString(), objectItem.get("nom_articulo").toString(), objectItem.get("precio_iva_inc").toString(), objectItem.get("total_iva_inc").toString()));
                i++;
            }

            factura = new FacturaZebra(object.get("titulo").toString(),object.get("subtitulo_movil").toString(),object.get("nom_empresa").toString(),object.get("dir_empresa").toString(), object.get("dir_empresa_aux").toString(), object.get("tel_empresa").toString(), object.get("nom_localidad").toString(),
                    object.get("nom_pais").toString(), object.get("nro_cuit").toString(), object.get("nro_docum").toString(), object.get("n_autorizacion").toString(), object.get("fec_doc").toString(),
                    object.get("nro_doc_uni").toString(), object.get("nom_tit").toString(), object.get("cod_control_fact").toString(), object.get("fec_vto_fac").toString(), object.get("imagen_qr").toString(), object.get("montoenletras").toString(), object.get("leyenda_fac").toString(),
                    items,object.get("municipio").toString(),object.get("cod_cliente").toString(),object.get("leyenda2").toString());

            //object.get("dir_empresa");
            //factura=new FacturaZebra(jsonArray.get(0).get(""));
            //jsonReader.setLenient(true);
            //jsonReader.beginObject(); // Start processing the JSON object
            //jsonReader.endObject();
            //jsonReader.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
       /* SyncHttpClient client = new SyncHttpClient(  );
        client.get( URL+metodo,params,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String status ="";
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String string;
            }
        });*/
        return factura;
    }

    public static void ObtengoSucursal(String metodo, RequestParams params2) {
        SyncHttpClient client = new SyncHttpClient();
        client.addHeader("Authorization", WebService.token);
        client.get(URL + metodo, params2, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String respuesta = new String(responseBody, "UTF-8");
                    JSONObject objetojSon = new JSONObject(respuesta);
                    if (objetojSon.getString("error").equals("Ok") || objetojSon.getString("error").toUpperCase().contains("NRO_TRANS_REF")) {
                        nFac.setNro_Trans(objetojSon.getString("nro_trans"));
                        System.out.println(nFac.getNro_Trans());

                        nFac.setNro_trans_ref(objetojSon.getString("nro_trans_ref"));
                        System.out.println(nFac.getNro_trans_ref());

                        nFac.setNom_Tit(objetojSon.getString("nom_tit"));
                        System.out.println(nFac.getNom_Tit());

                        nFac.setSucursal(objetojSon.getString("nom_sucursal"));
                        System.out.println(nFac.getSucursal());
                    } else {
                        errToken = objetojSon.getString("error");
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException JSError) {
                    JSError.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    public static void TraerCajas(String metodo, RequestParams params1) {
        SyncHttpClient client = new SyncHttpClient();
        client.addHeader("Authorization", WebService.token);
        client.get(URL + metodo, params1, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    respuestaGuardarCaja = new String(responseBody, "UTF-8");
                    JSONObject Objeto = new JSONObject(respuestaGuardarCaja);
                    if (Objeto.getString("retorno").toUpperCase().equals("OK")) {
                        errToken = "";
                        nFac.setNro_trans_ref(Objeto.getString("nro_trans_ref"));
                        retoEnvases = Objeto.getString("retorno").toString();
                        System.out.println(retoEnvases);
                        trajoCajas = true;
                        if (retoEnvases.toUpperCase().equals("OK")) {
                            retoEnvase = 1;
                        }
                    } else {
                        errToken = Objeto.getString("retorno");
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                        trajoCajas = false;
                    }
                } catch (Exception exc) {
                    System.out.println(exc.toString());
                    exc.printStackTrace();
                }
               /* if (retoEnvases.equals("ok")) {
                    retoEnvase = 1;
                }
                trajoCajas = true;*/
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.toString();
                trajoCajas = false;
            }
        });
    }

    public static void validarFactura(String metodo, RequestParams params1) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params1, metodo);
        try {
            n_factu = new FactValidada();
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String key = jsonReader.nextName();
                if (key.equals("error")) {
                    errToken = jsonReader.nextString();
                    err_ValidaFact = errToken;
                    if (!errToken.toUpperCase().contains("TOKEN")) {
                        errToken = "";
                    }
                    if (err_ValidaFact.toUpperCase().contains("OK")) {
                        err_ValidaFact = "";
                    }
                } else if (key.equals("fact")) {
                    JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                    if (check == JsonToken.NULL) {
                        jsonReader.skipValue();
                        n_factu.setFact("Factura Invalida");
                    } else {
                        n_factu.setFact(jsonReader.nextString());
                    }
                } else if (key.equals("venc")) {
                    JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                    if (check == JsonToken.NULL) {
                        jsonReader.skipValue();
                    } else {
                        n_factu.setVence(jsonReader.nextString().replace("00:00:00.0000000", "").trim());
                        String[] valoresfecha = n_factu.getVence().split("-");
                            /*String nuevaFecha = valoresfecha[0].trim()+"-"+valoresfecha[2].trim()+"-"+valoresfecha[1].trim();
                            n_factu.setVence(nuevaFecha);*/
                    }
                } else if (key.equals("timbr")) {
                    JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                    if (check == JsonToken.NULL) {
                        jsonReader.skipValue();
                    } else {
                        n_factu.setTimbr(jsonReader.nextString());
                    }
                } else if (key.equals("idfactura")) {
                    JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                    if (check == JsonToken.NULL) {
                        jsonReader.skipValue();
                    } else {
                        n_factu.setId_Factura(jsonReader.nextString());
                    }
                } else {
                    jsonReader.skipValue();
                }
            }
            //Se cambio porque el php no devuelve nro_inic 09/1/2019
            if (n_factu.getFact() == null) {
                n_factu.setFact("Factura Invalida");
            }
            jsonReader.endObject();
            jsonReader.close();
//            }
        } catch (Exception exc) {
            n_factu.setFact("Factura Invalida");
        }
    }

    public static void ExisteFactura(String metodo, RequestParams params) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo);
        try {
            fact = new FacturaXDia();
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("nro_trans")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            fact.setNro_Trans("");
                        } else {
                            fact.setNro_Trans(jsonReader.nextString());
                        }
                    } else if (key.equals("cod_tit_gestion")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                        } else {
                            fact.setCod_Tit_Gestion(jsonReader.nextString());
                        }
                    } else if (key.equals("fec_doc")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                        } else {
                            fact.setFecha(jsonReader.nextString());
                        }
                    } else if (key.equals("imp_mov_mn")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                        } else {
                            fact.setImporte(jsonReader.nextDouble());
                        }
                    } else if (key.equals("cod_sucursal")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                        } else {
                            fact.setCod_Sucursal(jsonReader.nextString());
                        }
                    } else if (key.equals("nro_viaje")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                        } else {
                            fact.setNro_viaje(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_tit")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                        } else {
                            fact.setNom_Tit(jsonReader.nextString());
                        }
                    } else if (key.equals("sucursal")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                        } else {
                            fact.setSucursal(jsonReader.nextString());
                        }
                    } else if (key.equals("cod_moneda")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                        } else {
                            fact.setCod_moneda(jsonReader.nextString());
                        }
                    } else if (key.equals("cod_tit")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                        } else {
                            fact.setCod_Tit(jsonReader.nextString());
                        }
                    } else if (key.equals("latitud_ubic")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                        } else {
                            fact.setLatitud(jsonReader.nextDouble());
                        }
                    } else if (key.equals("longitud_ubic")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                        } else {
                            fact.setLongitud(jsonReader.nextDouble());
                        }
                    } else {
                        jsonReader.skipValue();
                    }

                }
                jsonReader.endObject();
            }
            if (fact.getNro_Trans().equals("0")) {
                fact.setTipo("Factura Invalida");
            } else {
                fact.setTipo("Factura Valida");
            }
            jsonReader.endArray();
            jsonReader.close();
        } catch (Exception exc) {
            exc.toString();
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
            fact.setTipo("Factura Invalida");
        }
    }

    public static void IngresarRemito(String metodo, RequestParams params) {
        /*remito_guardado = false;
        SyncHttpClient client = new SyncHttpClient();
        client.get(URL+metodo,params,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody,"UTF-8");
                    JSONObject obj = new JSONObject( response );
                    if (obj.getString( "retorno" ) != null) {
                        retoRemito = obj.getString( "retorno" );
                    }
                    if (obj.getString( "nro_trans" ) != null) {
                        nro_trans = Integer.valueOf( obj.getString( "nro_trans" ) );
                        nro_rem = obj.getString( "nro_trans" );
                        nro_trans_impresion = nro_rem;
                        remito_guardado = true;
                    }
                }catch (Exception error) {
                    retoRemito= String.valueOf( R.string.reto_Fac_0);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.toString();
            }
        });*/
        remito_guardado = false;

        try {
            String sUrl = URL + metodo + "?" + params;
            System.out.println(sUrl);
            java.net.URL urlServer = new URL(sUrl);
            HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
            urlConn.setRequestProperty("Authorization", WebService.token);
            urlConn.setConnectTimeout(3000); //<- 3Seconds Timeout
            urlConn.connect();
            if (urlConn.getResponseCode() == 200) {
                System.out.println(urlConn.getResponseMessage());
                JsonReader jsonReader;
                InputStream responseBody = urlConn.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                jsonReader = new JsonReader(responseBodyReader);
                jsonReader.beginObject(); // Start processing the JSON object
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName(); // Fetch the next key
                    if (key.equals("retorno")) {
                        retoRemito = jsonReader.nextString();
                        if (retoRemito.toUpperCase().equals("OK")) {
                            errToken = "";
                        } else {
                            errToken = retoRemito;
                            if (!errToken.toUpperCase().contains("TOKEN")) {
                                errToken = "";
                            }
                        }
                    }
                    if (key.equals("nro_trans")) {
                        remito_guardado = true;
                        String nro = jsonReader.nextString();
                        nro_trans = Integer.valueOf(nro);
                        nro_rem = nro;
                        nro_trans_impresion = nro_rem;
                    }

                }
            } else {
                retoRemito = "Error al recibir los datos";
                nro_trans = 0;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private static void postText(RequestParams parametrosIng) {
        try {
            // url where the data will be posted
            String postReceiverUrl = URL + "Facturas/IngresarFactura.php";
            // HttpClient
            HttpClient httpClient = new DefaultHttpClient();
            // post header
            HttpPost httpPost = new HttpPost(postReceiverUrl);
            // add your data
            // List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            // nameValuePairs.add(new BasicNameValuePair("firstname", "Mike"));
            //nameValuePairs.add(new BasicNameValuePair("lastname", "Dalisay"));
            //nameValuePairs.add(new BasicNameValuePair("email", "mike@testmail.com"));

            httpPost.addHeader("Authorization", WebService.token);

            // httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpPost.setEntity(new SerializableEntity(parametrosIng, false));
            // execute HTTP post request

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {

                String responseStr = EntityUtils.toString(resEntity).trim();
                //Log.v(TAG, "Response: " +  responseStr);
                System.out.println("Error ingresar factura: "+responseStr);

                // you can add an if statement here and do other actions based on the response
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
    //fin prueba

    public static void IngresarReclamo(RequestParams parametrosIng, String metodo) {

        System.out.println(URL + metodo);
        System.out.println(parametrosIng.toString());
        //solo para pruebas
        try {
            String sUrl = URL + metodo + "?" + parametrosIng;
            java.net.URL urlServer = new URL(sUrl);
            HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
            urlConn.setRequestProperty("Authorization", WebService.token);
            urlConn.setConnectTimeout(3000); //<- 3Seconds Timeout
            urlConn.connect();
            if (urlConn.getResponseCode() == 200) {
                System.out.println(urlConn.getResponseMessage());
                JsonReader jsonReader;
                InputStream responseBody = urlConn.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                jsonReader = new JsonReader(responseBodyReader);
                jsonReader.beginObject(); // Start processing the JSON object
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName(); // Fetch the next key
                    if (key.equals("resultado")) {
                        reto_AgregarReclamo = jsonReader.nextString();
                        if (reto_AgregarReclamo.toUpperCase().equals("OK")) {
                            errToken = "";
                        } else {
                            errToken = reto_AgregarReclamo;
                            if (!errToken.toUpperCase().contains("TOKEN")) {
                                errToken = "";
                            }
                        }
                        System.out.println(reto_AgregarReclamo);
                    }
                }
            } else {
                reto_AgregarReclamo = "Error al recibir los datos";
            }
        } catch (Exception exc) {
            System.out.println(exc.toString());
        }

    }

    public static void IngresarFactura(String metodo, RequestParams parametrosIng) {

        System.out.println(URL + metodo);
        System.out.println(parametrosIng.toString());
        //solo para pruebas
        try {
            String sUrl = URL + metodo + "?" + parametrosIng;
            java.net.URL urlServer = new URL(sUrl);
            HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
            urlConn.setRequestProperty("Authorization", WebService.token);
            urlConn.setConnectTimeout(3000); //<- 3Seconds Timeout
            urlConn.connect();
            if (urlConn.getResponseCode() == 200) {
                System.out.println(urlConn.getResponseMessage());
                JsonReader jsonReader;
                InputStream responseBody = urlConn.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                jsonReader = new JsonReader(responseBodyReader);
                jsonReader.beginObject(); // Start processing the JSON object
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName(); // Fetch the next key
                    if (key.equals("retorno")) {
                        reto_AgregaFactura = jsonReader.nextString();
                        if (reto_AgregaFactura.toUpperCase().equals("OK")) {
                            errToken = "";
                        } else {
                            errToken = reto_AgregaFactura;
                            if (!errToken.toUpperCase().contains("TOKEN")) {
                                errToken = "";
                            }
                        }
                        System.out.println("reto_AgregarFactura: " + reto_AgregaFactura);
                    }
                    if (key.equals("nro_trans")) {
                        nro_trans = jsonReader.nextInt();
                        WebService.nro_trans_impresion = nro_trans.toString();

                        System.out.println("IngresarFactura NroTrans: " + nro_trans_impresion);
                    }
                }
                if (!nro_trans_impresion.equals("0")){
                    reto_AgregaFactura = "ok";
                }
            } else {
                reto_AgregaFactura = "Error al recibir los datos";
                nro_trans = 0;
            }
        } catch (Exception exc) {
            reto_AgregaFactura = "Error al grabar factura. " + exc.toString();
            System.out.println(exc.toString());
        }
    }

    public static void RecomendarNumero(String metodo, RequestParams params) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo);
        try {
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("0")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            cod_suc_tribut = "0";
                            cod_suc_tribut = "0";
                        } else {
                            cod_suc_tribut = jsonReader.nextString();
                        }
                    } else if (key.equals("1")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            cod_fac_trubut = "10000";
                        } else {
                            // nro_fin = jsonReader.nextString();
                            cod_fac_trubut = jsonReader.nextString();
                        }
                    } else if (key.equals("2")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            nro_inicial = "0";
                        } else {
                            nro_inicial = jsonReader.nextString();
                        }
                    } else if (key.equals("3")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            nro_fin = "10000";
                        } else {
                            nro_fin = jsonReader.nextString().trim();
                        }
                    } else if (key.equals("4")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            nro_sugerido = "3000";
                        } else {
                            nro_sugerido = jsonReader.nextString().trim();
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                    //Deberimos obtener 3 valores pero despues lo veo 26 10 2018

                }
                jsonReader.endObject();
            }
            jsonReader.endArray();
        } catch (Exception exc) {
            System.out.println("Error: WebService.RecomendarNumero");
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
      /*  SyncHttpClient client = new SyncHttpClient();
        client.get(URL+metodo,params,new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try
                {
                    String respuesta =  new String(responseBody,"UTF-8");
                    objResponseA = new JSONArray(respuesta);
                    String [] retorno = respuesta.split( "," );
                    String [] val1 = retorno[1].split( ":" );
                    cod_fac_trubut = val1[1].replace( "\"","" );
                    String [] val2 = retorno[2].split( ":" );
                    cod_suc_tribut = val2[1].replace( "\"","" );
                    String [] val3 = retorno[3].split( ":" );
                    nro_inicial = val3[1].replace( "\"","" );
                    String [] val4 = retorno[5].split( ":" );
                    nro_fin = val4[1].replace( "\"","" ).trim();
                    String [] val5 = retorno[7].split( ":" );
                    nro_sugerido = val5[1].replace( "\"","" ).replace( "}","" ).trim();
                }
                catch (Exception exc)
                {
                    cod_fac_trubut = "000";
                    cod_suc_tribut = "000";
                    nro_inicial = "0";
                    nro_fin = "100000";
                    nro_sugerido = "0";
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });*/
    }


    public static String imprimirTicket() {
        deudores = new ArrayList<ClienteCobranza>();
        String jelement = WebServiceRes.imprimirTicket();
        //JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(parms,metodo);

        ClienteCobranza deudor = new ClienteCobranza();
        /*
        try  {
            JsonArray jobject = jelement.getAsJsonArray();

            for (JsonElement item:jobject  ) {
                deudor = new ClienteCobranza();

                deudor.setNom_Tit(  item.getAsJsonObject().get("nom_tit").getAsString() );
                deudor.setCod_Tit(  item.getAsJsonObject().get("cod_tit").getAsString() );
                deudores.add( deudor );
            }
            //jobject = jobject.getAsJsonObject("data");
            //JsonArray jarray = jobject.getAsJsonArray("translations");
            //jobject = jarray.get(0).getAsJsonObject();
            //String result = jobject.get("translatedText").getAsString();


        }
        catch (Exception exc)
        {
            exc.toString();
        }
        */
        return jelement;
    }

    public static void traerParemp() {
        JsonElement jelement = WebServiceRes.traerParemp();
        try {
            JsonArray jobject = jelement.getAsJsonArray();
            errToken = "";
            for (JsonElement item : jobject) {
                tipoCambio = item.getAsJsonObject().get("tipo_cambio").getAsDouble();
                simboloMonedaNacional = item.getAsJsonObject().get("simbolo_mn").getAsString();
                codMonedaNacional = item.getAsJsonObject().get("mone_mn").getAsString();
                simboloMonedaTr = item.getAsJsonObject().get("simbolo_tr").getAsString();
                codMonedaTr = item.getAsJsonObject().get("mone_trad").getAsString();

            }
        } catch (Exception exc) {
            try {
                errToken = jelement.getAsJsonObject().get("error").getAsString();
                if (!errToken.toUpperCase().contains("TOKEN")) {
                    errToken = "";
                }
            } catch (Exception ex) {

            }
            exc.toString();
        }

    }

    public static void traerTipoC() {
        JsonElement jelement = WebServiceRes.traerTC();
        try {
            JsonArray jobject = jelement.getAsJsonArray();
            errToken = "";
            for (JsonElement item : jobject) {
                tipoCambio = item.getAsJsonObject().get("tipo_cambio").getAsDouble();
            }
        } catch (Exception exc) {
            try {
                errToken = jelement.getAsJsonObject().get("error").getAsString();
                if (!errToken.toUpperCase().contains("TOKEN")) {
                    errToken = "";
                }
            } catch (Exception ex) {

            }
            exc.toString();
        }

    }


    /*
    public static void TraerDeudas(RequestParams params)    {
        //JsonReader jsonReader =WebServiceRes.jsRespuestaWSRest(params,"Cobranzas/TraerDeudoresSinMoneda.php"); //obtengo datos
        JsonReader jsonReader =WebServiceRes.jsRespuestaWSRest(params,"Cobranzas/TraerDeudores.php"); //obtengo datos

        try
        {
            ClienteCobranza deudor = new ClienteCobranza();
            jsonReader.beginArray();
            deudas = new ArrayList<>(  );
            while (jsonReader.hasNext())
            {
                deudor = new ClienteCobranza();
                jsonReader.beginObject(); //leo objeto del array
                while (jsonReader.hasNext())
                {
                    String key = jsonReader.nextName();
                    if(key.equals( "cod_docum" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            deudor.setCod_Docum( "NA" );
                        }
                        else
                        {
                            deudor.setCod_Docum( jsonReader.nextString() );
                        }
                    }
                    else if(key.equals("nro_docum"))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            deudor.setNro_Docum( "NA" );
                        }
                        else
                        {
                            deudor.setNro_Docum( jsonReader.nextString() );
                        }
                    }
                    else if(key.equals( "fec_venc" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            deudor.setFecha_Vence( "NA" );
                        }
                        else
                        {
                            deudor.setFecha_Vence( jsonReader.nextString() );
                        }
                    }
                    else if(key.equals("cod_moneda"))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            deudor.setCod_Moneda( "NA" );
                        }
                        else
                        {
                            deudor.setCod_Moneda( jsonReader.nextString() );
                        }
                    }
                    else if(key.equals( "imp_mov_mo" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            deudor.setImp_mov_mo( 0 );
                        }
                        else
                        {
                            deudor.setImp_mov_mo( Integer.valueOf( jsonReader.nextString() ) );
                        }
                    }
                    else
                    {
                        jsonReader.skipValue();
                    }
                }
                deudor.setEstado( 0 );
                deudas.add( deudor );
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();
        }
        catch (Exception exc)
        {
            exc.toString();
        }

    /*    SyncHttpClient cliente = new SyncHttpClient(  );
        cliente.get(URL + "Cobranzas/TraerDeudoresSinMoneda.php", params, new AsyncHttpResponseHandler()  {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try
                {
                    String respuesta =  new String(responseBody,"UTF-8");
                    objResponseA = new JSONArray(respuesta);
                    JSONObject objetojSon= new JSONObject();
                    ClienteCobranza deudor = new ClienteCobranza();
                    deudas = new ArrayList<>(  );
                    if(objResponseA.length()>0)
                    {
                        for(int i = 0;i<objResponseA.length();i++)
                        {
                            objetojSon = objResponseA.getJSONObject(i);
                            deudor = new ClienteCobranza();

                           if(objetojSon.getString( "cod_docum" )!=null)
                            {
                                deudor.setCod_Docum( objetojSon.getString( "cod_docum" ) );
                            }
                            if(objetojSon.getString( "nro_docum" )!=null)
                            {
                                deudor.setNro_Docum( objetojSon.getString( "nro_docum" ) );
                            }
                            if(objetojSon.getString( "fec_venc" )!=null)
                            {
                                deudor.setFecha_Vence( objetojSon.getString( "fec_venc" ) );
                            }
                            if(objetojSon.getString( "cod_moneda" )!=null)
                            {
                                deudor.setCod_Moneda( objetojSon.getString( "cod_moneda" ) );
                            }
                            if(objetojSon.getString( "imp_mov_mo" )!=null)
                            {
                                try
                                {
                                    Double val = Double.valueOf(  objetojSon.getString( "imp_mov_mo" ) );
                                    deudor.setImp_mov_mo(val.intValue() );

                                }
                                catch (Exception ex)
                                {}
                            }
                            if(objetojSon.getString( "cod_aux" )!=null)
                            {
                                deudor.setCod_Aux( objetojSon.getString( "cod_aux" ) );
                            }
                            if(objetojSon.getString( "cod_banco" )!=null)
                            {
                                deudor
                                .setCod_Banco( objetojSon.getString( "cod_banco" ) );
                            }
                            deudor.setEstado( 0 );
                            deudas.add( deudor );
                        }
                    }
                }
                catch (Exception exc)
                {
                    exc.toString();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });*/


    public static void ObtenerTiempo(RequestParams params) {
        SyncHttpClient cliente = new SyncHttpClient();
        cliente.addHeader("Authorization", WebService.token);
        cliente.get(URL + "Session/ObtenerTiempoConfigurado.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String respuesta = "";
                try {
                    respuesta = new String(responseBody, "UTF-8");
                    if (!respuesta.equals(null)) {
                        Login.TiempoThread = Integer.parseInt(respuesta);
                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                    System.out.println(respuesta);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public static void traerConf() {
        JsonElement jelement = WebServiceRes.traerConfig();
        try {
            JsonArray jobject = jelement.getAsJsonArray();
            configuracion = new Configuracion_Empresa();
            errToken = "";
            for (JsonElement objetojSon : jobject) {

                configuracion.setCod_Empresa(objetojSon.getAsJsonObject().get("cod_emp").getAsString());
                configuracion.setTiempo_Guardado(objetojSon.getAsJsonObject().get("tiempo_guardado").getAsInt());
                configuracion.setPorc_ent_mas(objetojSon.getAsJsonObject().get("porc_ent_mas").getAsInt());
                configuracion.setPorc_ent_menos(objetojSon.getAsJsonObject().get("porc_ent_menos").getAsInt());
                configuracion.setControl_tolerancia(objetojSon.getAsJsonObject().get("control_tolerancia").getAsString());
                configuracion.setTipo_cobranza(objetojSon.getAsJsonObject().get("tipo_cobranza").getAsString());
                configuracion.setIdioma(objetojSon.getAsJsonObject().get("idioma").getAsString());
                configuracion.setWs_pruebas(objetojSon.getAsJsonObject().get("ws_pruebas").getAsString() + "/");
                configuracion.setWs_produccion(objetojSon.getAsJsonObject().get("ws_produccion").getAsString() + "/");
                if (!objetojSon.getAsJsonObject().get("reten_completa").isJsonNull()) {
                    configuracion.setReten_completa(objetojSon.getAsJsonObject().get("reten_completa").getAsString());
                } else {
                    configuracion.setReten_completa("");
                }
                configuracion.setTipo_impresora(objetojSon.getAsJsonObject().get("tipo_impresora").getAsString());
                configuracion.setCierre_app(objetojSon.getAsJsonObject().get("cierre_app").getAsString());
                configuracion.setVuelto(objetojSon.getAsJsonObject().get("vuelto").getAsString());
                configuracion.setCred_suspenso(objetojSon.getAsJsonObject().get("cred_suspenso").getAsString());
                configuracion.setNum_recibo_aut(objetojSon.getAsJsonObject().get("num_recibo_aut").getAsString());
                configuracion.setEs_ley2051(objetojSon.getAsJsonObject().get("ley_2051").getAsString());
                configuracion.setMax_lin_rec(objetojSon.getAsJsonObject().get("max_lin_rec").getAsInt());
                configuracion.setDif_reten_auto(objetojSon.getAsJsonObject().get("dif_reten_auto").getAsString());
                configuracion.setCant_copias(objetojSon.getAsJsonObject().get("cant_copias").getAsInt());
                configuracion.setSol_guardar_ubi(objetojSon.getAsJsonObject().get("sol_guardar_ubi").getAsString());
                configuracion.setDiasfec_cheque(objetojSon.getAsJsonObject().get("diasfec_cheque").getAsInt());
                configuracion.setNom_repcli(objetojSon.getAsJsonObject().get("nom_repcli").getAsString());
                configuracion.setPide_fecrepcli(objetojSon.getAsJsonObject().get("pide_fecrepcli").getAsString());
                configuracion.setTransf_completa(objetojSon.getAsJsonObject().get("transf_completa").getAsString());
                configuracion.setFac_interes(objetojSon.getAsJsonObject().get("fac_interes").getAsString());
                configuracion.setGraba_logws(objetojSon.getAsJsonObject().get("graba_logws").getAsString());
                configuracion.setHabilita_reclamo(objetojSon.getAsJsonObject().get("habilita_reclamo").getAsString());
                configuracion.setPide_empresa(objetojSon.getAsJsonObject().get("pide_empresa").getAsString());
                configuracion.setPide_docref(objetojSon.getAsJsonObject().get("pide_docref").getAsString());
                //configuracion.setNivel_reclamo(objetojSon.getAsJsonObject().get("nivel_reclamo").getAsString());

                configuracion.setDec_cant(objetojSon.getAsJsonObject().get("dec_cant").getAsString());
                configuracion.setIva(objetojSon.getAsJsonObject().get("iva").getAsString());
                configuracion.setDec_montomn(objetojSon.getAsJsonObject().get("dec_montomn").getAsString());
                configuracion.setDto_linea(objetojSon.getAsJsonObject().get("dto_linea").getAsString());
                configuracion.setPropone_nrofac(objetojSon.getAsJsonObject().get("propone_nrofac").getAsString());
                configuracion.setFac_otro(objetojSon.getAsJsonObject().get("fac_otro").getAsString());


                cantidadCopias = configuracion.getCant_copias();
            }
        } catch (Exception exc) {
            try {
                errToken = jelement.getAsJsonObject().get("error").getAsString();
                if (!errToken.toUpperCase().contains("TOKEN")) {
                    errToken = "";
                }
            } catch (Exception ex) {

            }
            exc.toString();
        }

    }
    
    /*public static void ConfigEmpresa() {
        SyncHttpClient cliente = new SyncHttpClient();
        cliente.get(URL + "Session/SeleccionarConfiguracion.php", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String respuesta = new String(responseBody, "UTF-8");
                    objResponseA = new JSONArray(respuesta);
                    JSONObject objetojSon = new JSONObject();
                    Configuracion_Empresa confi = new Configuracion_Empresa();
                    empresasRegistradas = new ArrayList<>();
                    for (int i = 0; i < objResponseA.length(); i++) {
                        objetojSon = objResponseA.getJSONObject(i);
                        confi = new Configuracion_Empresa();
                        if (objetojSon.getString("cod_empresa") != null) {
                            confi.setCod_Empresa(objetojSon.getString("cod_empresa"));
                        }
                        if (objetojSon.getString("ruta_del_Servicio") != null) {
                            confi.setUrl(objetojSon.getString("ruta_del_Servicio"));
                        }
                        if (objetojSon.getString("entorno") != null) {
                            confi.setEntorno(objetojSon.getString("entorno"));
                        }
                        empresasRegistradas.add(confi);
                    }
                } catch (Exception exc) {

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }*/

    //METODO PARA TRAER LOS DATOS DE LA LOCALIZACION DE LA CENTRAL A DONDE SE TIENE QUE RETORNAR
    public static void localizacionCentral(RequestParams params) {
        SyncHttpClient cliente = new SyncHttpClient();
        cliente.addHeader("Authorization", WebService.token);
        cliente.get(URL + "Viajes/obtenerCentral.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String resultado = new String(responseBody, "UTF-8");
                    JSONObject objeto = new JSONObject(resultado);
                    if (objeto != null || objeto.getString("error").equals("Ok")) {
                        errToken = "";
                        Retorno = new LatLng(objeto.getDouble("lat"), objeto.getDouble("long"));
                    } else {
                        errToken = objeto.getString("error");
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    /*----------------------------Configuracion por empresa-----------------------------*/

    public static String CargarPreferencias() {
        String valor;
        Context contx = Configuracion.getAppContext();
        SharedPreferences PreferenciaURL = contx.getSharedPreferences("N_URL", Context.MODE_PRIVATE);
        valor = PreferenciaURL.getString("N_URL", WebService.URL);
        WebService.URL = PreferenciaURL.getString(valor, WebService.URL);
        return valor;

    }

    public static SharedPreferences GuardarPreferecia(String valorURL) {
        Context contx = Configuracion.getAppContext();
        SharedPreferences PreferenciaURL = contx.getSharedPreferences("N_URL", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = PreferenciaURL.edit();
        editor.clear();
        editor.putString("N_URL", valorURL);
        editor.commit();
        return PreferenciaURL;
    }

    public static void setTotalDeudas(Double val_tot) {
        totalDeudas = val_tot;
    }

    public static void setTotalInteres(Double val_tot) {
        totalInteres = val_tot;
    }

    public static void setTotalDescuento(Double val_tot) {
        totalDescuento = val_tot;
    }

    public static void setTotalValor(Double val_tot) {
        totalValores = val_tot;
    }

    public static void setTotalReten(Double imp_reten) {
        totalRetencion = imp_reten;
    }

    public static void addEfectivo(Efectivo efectivoAux) {
        totalEfectivo = totalEfectivo + efectivoAux.getImporte();
        efectivo.add(efectivoAux);

        pago = new ValoresPago("efectivo", 0, "1", "0", "", "", efectivoAux.getImporte());
        valoresPago.add(pago);
    }

    public static void removeEfectivo(Efectivo efectivoAux) {
        totalEfectivo = totalEfectivo - efectivoAux.getImporte();
        efectivo.remove(efectivoAux);

        for (int i = 0; valoresPago.size() > i; i++) {
            pago = valoresPago.get(i);
            if (pago.getCod_docum() == "efectivo" & efectivoAux.getImporte() == pago.getImporte()) {
                int a = valoresPago.indexOf(pago);
                if (a != -1) {
                    valoresPago.remove(a);
                }
            }
        }
    }

    public static void addEfectivoUSD(EfectivoUSD efectivoUSDAux) {
        totalEfectivoUSD = totalEfectivoUSD + efectivoUSDAux.getImporte();
        efectivoUSD.add(efectivoUSDAux);

        //double precioconvertido = efectivoUSDAux.getImporte() * tipoCambio;
        pago = new ValoresPago("efectUSD", 0, "2", "0", "", "", efectivoUSDAux.getImporte());
        valoresPago.add(pago);
    }

    public static void removeEfectivoUSD(EfectivoUSD efectivoUSDAux) {
        totalEfectivoUSD = totalEfectivoUSD - efectivoUSDAux.getImporte();
        efectivoUSD.remove(efectivoUSDAux);

        for (int i = 0; valoresPago.size() > i; i++) {
            pago = valoresPago.get(i);
            if (pago.getCod_docum() == "efectUSD" & (efectivoUSDAux.getImporte()/* * WebService.tipoCambio*/) == pago.getImporte()) {
                int a = valoresPago.indexOf(pago);
                if (a != -1) {
                    valoresPago.remove(a);
                }
            }
        }
    }

    public static void addCheque(Cheque cheque) {
        if (WebService.cheques.size() == 0) {
            totalCheques = totalCheques + cheque.getImporte();
        } else {
            if (monedaChequeUlti != cheque.getCodMon().trim()) {
                totalCheques = 0D;
                for (int i = 0; i < cheques.size(); i++) {
                    Cheque tarj = cheques.get(i);
                    if (tarj.getCodMon().trim().equals("2") & monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                        totalCheques = totalCheques + (tarj.getImporte() * tipoCambio);
                    } else if (tarj.getCodMon().trim().equals("1") & monedaSeleccionada.getCod_Moneda().trim().equals("2")) {
                        totalCheques = totalCheques + Math.round(tarj.getImporte() / tipoCambio);
                    } else {
                        totalCheques = totalCheques + tarj.getImporte();
                    }
                }
            }
            if (cheque.getCodMon().trim().equals("2") & monedaSeleccionada.getCod_Moneda().trim().equals("1") & monedaChequeUlti != cheque.getCodMon().trim()) {
                totalCheques = totalCheques + (cheque.getImporte() * tipoCambio);
            } else if (cheque.getCodMon().trim().equals("1") & monedaSeleccionada.getCod_Moneda().trim().equals("2") & monedaChequeUlti != cheque.getCodMon().trim()) {
                totalCheques = totalCheques + Math.round(cheque.getImporte() / tipoCambio);
            } else {
                totalCheques = totalCheques + cheque.getImporte();
            }
        }

        cheques.add(cheque);

        pago = new ValoresPago("cheque", Integer.valueOf(cheque.getNumero()), cheque.getCodMon(), cheque.getBanco(), "", cheque.getFecha(), cheque.getImporte());
        valoresPago.add(pago);
    }

    public static void removeCheque(Cheque cheque) {
        if (WebService.cheques.size() == 1) {
            totalCheques = totalCheques - cheque.getImporte();
        } else {
            if (cheque.getCodMon().trim().equals("2") & monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                totalCheques = totalCheques - (cheque.getImporte() * tipoCambio);
            } else if (cheque.getCodMon().trim().equals("1") & monedaSeleccionada.getCod_Moneda().trim().equals("2")) {
                totalCheques = totalCheques - Math.round(cheque.getImporte() / tipoCambio);
            } else {
                totalCheques = totalCheques - cheque.getImporte();
            }
        }

        cheques.remove(cheque);

        for (int i = 0; valoresPago.size() > i; i++) {
            pago = valoresPago.get(i);
            if (pago.getCod_docum() == "cheque" & cheque.getNumero() == pago.getNro_docum() & cheque.getCodMon() == pago.getCod_moneda() & cheque.getBanco() == pago.getCod_banco() & cheque.getFecha() == pago.getFec_valor() & cheque.getImporte() == pago.getImporte()) {
                int a = valoresPago.indexOf(pago);
                if (a != -1) {
                    valoresPago.remove(a);
                }
            }
        }
    }

    public static void addCreditoSus(Credito_Suspenso credito) {

        totalCredSus = totalCredSus + credito.getImporte();
        totalCredSusUSD = totalCredSusUSD + credito.getImporteUSD();
        Double importe = credito.getImporte();

        creditoSus.add(credito);
        if (credito.getCod_Moneda().trim().equals("2")) {
            importe = credito.getImporteUSD();
        }
        pago = new ValoresPago("cresusp", Integer.valueOf(credito.getNumero()), credito.getCod_Moneda(), "0", "", credito.getFecha(), importe, credito.getImporteUSD());
        valoresPago.add(pago);
    }

    public static void removeCreditoSus(Credito_Suspenso credito) {

        totalCredSus = totalCredSus - credito.getImporte();
        totalCredSusUSD = totalCredSusUSD - credito.getImporteUSD();

        creditoSus.remove(credito);

        for (int i = 0; valoresPago.size() > i; i++) {
            pago = valoresPago.get(i);
            if (pago.getCod_docum() == "cresusp" & credito.getNumero() == pago.getNro_docum() & credito.getCod_Moneda() == pago.getCod_moneda() & credito.getFecha() == pago.getFec_valor() & credito.getImporte() == pago.getImporte()) {
                int a = valoresPago.indexOf(pago);
                if (a != -1) {
                    valoresPago.remove(a);
                }
            }
        }
    }

    public static void addVuelto(Vuelto vuelto) {
        if (WebService.Vuelto.size() == 0) {
            totalVuelto = totalVuelto + vuelto.getImporte();
        } else {
            if (monedaVueltoUlti != vuelto.getCod_moneda().trim()) {
                totalVuelto = 0D;
                for (int i = 0; i < Vuelto.size(); i++) {
                    Vuelto tarj = Vuelto.get(i);
                    if (tarj.getCod_moneda().trim().equals("2") & monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                        totalVuelto = totalVuelto + (tarj.getImporte() * tipoCambio);
                    } else if (tarj.getCod_moneda().trim().equals("1") & monedaSeleccionada.getCod_Moneda().trim().equals("2")) {
                        totalVuelto = totalVuelto + Math.round(tarj.getImporte() / tipoCambio);
                    } else {
                        totalVuelto = totalVuelto + tarj.getImporte();
                    }
                }
            }
            if (vuelto.getCod_moneda().trim().equals("2") & monedaSeleccionada.getCod_Moneda().trim().equals("1") & monedaVueltoUlti != vuelto.getCod_moneda().trim()) {
                totalVuelto = totalVuelto + (vuelto.getImporte() * tipoCambio);
            } else if (vuelto.getCod_moneda().trim().equals("1") & monedaSeleccionada.getCod_Moneda().trim().equals("2") & monedaVueltoUlti != vuelto.getCod_moneda().trim()) {
                totalVuelto = totalVuelto + Math.round(vuelto.getImporte() / tipoCambio);
            } else {
                totalVuelto = totalVuelto + vuelto.getImporte();
            }
        }

        Vuelto.add(vuelto);

        String importe = "-" + vuelto.getImporte().toString();
        String moneda = vuelto.getCod_moneda().trim();

        pago = new ValoresPago("efectivo", 0, moneda, "0", "", "", Double.parseDouble(importe));
        valoresPago.add(pago);
    }

    public static void removeVuelto(Vuelto vuelto) {
        if (WebService.Vuelto.size() == 1) {
            totalVuelto = totalVuelto - vuelto.getImporte();
        } else {
            if (vuelto.getCod_moneda().trim().equals("2") & monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                totalVuelto = totalVuelto - (vuelto.getImporte() * tipoCambio);
            } else if (vuelto.getCod_moneda().trim().equals("1") & monedaSeleccionada.getCod_Moneda().trim().equals("2")) {
                totalVuelto = totalVuelto - Math.round(vuelto.getImporte() / tipoCambio);
            } else {
                totalVuelto = totalVuelto - vuelto.getImporte();
            }
        }

        Vuelto.remove(vuelto);

        for (int i = 0; valoresPago.size() > i; i++) {
            pago = valoresPago.get(i);
            String vuelto2 = "-" + vuelto.getImporte();
            if (pago.getCod_docum() == "efectivo" & Double.valueOf(vuelto2) == pago.getImporte()) {
                int a = valoresPago.indexOf(pago);
                if (a != -1) {
                    valoresPago.remove(a);
                }
            }
        }
    }

    public static void addRetenciones(Retenciones retencion) {
        totalRetenciones = totalRetenciones + retencion.getImporte();
        totalRetencionesUSD = totalRetencionesUSD + retencion.getImporteUSD();
        retenciones.add(retencion);

        totalDiferenciaReten = totalDiferenciaReten + retencion.getDiferencia();

        pago = new ValoresPago(retencion.getCod_docum(), Integer.valueOf(retencion.getDetalle()), "1", "0", "", retencion.getFecha(), retencion.getImporte(), retencion.getSuc_ref(), retencion.getFac_ref(), retencion.getTipo_cambio(), retencion.getImporteUSD(), retencion.getDiferencia());
        valoresPago.add(pago);
    }

    public static void removeRetenciones(Retenciones retencion) {
        totalRetenciones = totalRetenciones - retencion.getImporte();
        totalRetencionesUSD = totalRetencionesUSD - retencion.getImporteUSD();
        retenciones.remove(retencion);

        for (int i = 0; valoresPago.size() > i; i++) {
            pago = valoresPago.get(i);
            if (retencion.getCod_docum() == pago.getCod_docum() & retencion.getFecha() == pago.getFec_valor() & retencion.getImporte() == pago.getImporte()) {
                int a = valoresPago.indexOf(pago);
                if (a != -1) {
                    valoresPago.remove(a);
                }
            }
        }
    }

    public static void addProducto(Item item) {
        totalProductos = totalProductos + (item.getPrecio_Unitario() * item.getCantidad());
        listaProductos.add(item);
    }

    public static void addProductoPedido(ProductosItem item) {
        totalProductosPedidos = totalProductosPedidos + item.getTotal();
        listaProductosPedidos.add(item);
    }

    public static void removeProductoPedido(ProductosItem item) {
        totalProductosPedidos = totalProductosPedidos - item.getTotal();
        listaProductosPedidos.remove(item);
    }


    public static void addProductoDescuento(Item item) {
        totalProductos = totalProductos + (item.getCantidad() * (item.getPrecio_Unitario() - (item.getDtoLinea() * item.getPrecio_Unitario() / 100)));
        listaProductos.add(item);
    }

    public static void removeProductoDescuento(Item item) {
        totalProductos = totalProductos - (item.getCantidad() * (item.getPrecio_Unitario() - (item.getDtoLinea() * item.getPrecio_Unitario() / 100)));
        listaProductos.remove(item);
    }


    public static void removeProducto(Item item) {
        totalProductos = totalProductos - (item.getPrecio_Unitario() * item.getCantidad());
        listaProductos.remove(item);
    }

    public static void addDif(Diferencia_Reten dif) {
        if (diferenciaValores > 0) {
            totalDif = totalDif + dif.getImporte();
        } else {
            totalDifACuenta = totalDifACuenta + dif.getImporte();
        }
        dif_reten.add(dif);

        pago = new ValoresPago("difreten", 0, "1", "0", "", "", dif.getImporte());
        valoresPago.add(pago);
    }

    public static void removeDif(Diferencia_Reten dif) {
        if (dif.getImporte() < 0) {
            totalDifACuenta = totalDifACuenta - dif.getImporte();
        } else {
            totalDif = totalDif - dif.getImporte();
        }
        //totalDifACuenta = totalDifACuenta - dif.getImporte();

        if (totalDif < 0) {
            totalDif = 0D;
        }
        if (totalDifACuenta < 0) {
            totalDifACuenta = 0D;
        }

        dif_reten.remove(dif);

        for (int i = 0; valoresPago.size() > i; i++) {
            pago = valoresPago.get(i);
            if (pago.getCod_docum() == "difreten") {
                int a = valoresPago.indexOf(pago);
                if (a != -1) {
                    valoresPago.remove(a);
                }
            }
        }
    }

    public static void addTarjetaCredito(TarjetaCredito tarjetaCredito) {
        if (WebService.tarjetaCreditos.size() == 0) {
            totalTarjetaCredito = totalTarjetaCredito + tarjetaCredito.getImporte();
        } else {
            if (monedaTarjCredUlti != tarjetaCredito.getCodMon().trim()) {
                totalTarjetaCredito = 0D;
                for (int i = 0; i < tarjetaCreditos.size(); i++) {
                    TarjetaCredito tarj = tarjetaCreditos.get(i);
                    if (tarj.getCodMon().trim().equals("2") & monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                        totalTarjetaCredito = totalTarjetaCredito + (tarj.getImporte() * tipoCambio);
                    } else if (tarj.getCodMon().trim().equals("1") & monedaSeleccionada.getCod_Moneda().trim().equals("2")) {
                        totalTarjetaCredito = totalTarjetaCredito + Math.round(tarj.getImporte() / tipoCambio);
                    } else {
                        totalTarjetaCredito = totalTarjetaCredito + tarj.getImporte();
                    }
                }
            }
            if (tarjetaCredito.getCodMon().trim().equals("2") & monedaSeleccionada.getCod_Moneda().trim().equals("1") & monedaTarjCredUlti != tarjetaCredito.getCodMon().trim()) {
                totalTarjetaCredito = totalTarjetaCredito + (tarjetaCredito.getImporte() * tipoCambio);
            } else if (tarjetaCredito.getCodMon().trim().equals("1") & monedaSeleccionada.getCod_Moneda().trim().equals("2") & monedaTarjCredUlti != tarjetaCredito.getCodMon().trim()) {
                totalTarjetaCredito = totalTarjetaCredito + Math.round(tarjetaCredito.getImporte() / tipoCambio);
            } else {
                totalTarjetaCredito = totalTarjetaCredito + tarjetaCredito.getImporte();
            }
        }

        tarjetaCreditos.add(tarjetaCredito);

        pago = new ValoresPago("tarjCred", tarjetaCredito.getVoucher(), tarjetaCredito.getCodMon(), tarjetaCredito.getTarjeta(), "", "", tarjetaCredito.getImporte());
        valoresPago.add(pago);
    }

    public static void removeTarjetaCredito(TarjetaCredito tarjetaCredito) {
        if (WebService.tarjetaCreditos.size() == 1) {
            totalTarjetaCredito = totalTarjetaCredito - tarjetaCredito.getImporte();
        } else {
            if (tarjetaCredito.getCodMon().trim().equals("2") & monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                totalTarjetaCredito = totalTarjetaCredito - (tarjetaCredito.getImporte() * tipoCambio);
            } else if (tarjetaCredito.getCodMon().trim().equals("1") & monedaSeleccionada.getCod_Moneda().trim().equals("2")) {
                totalTarjetaCredito = totalTarjetaCredito - Math.round(tarjetaCredito.getImporte() / tipoCambio);
            } else {
                totalTarjetaCredito = totalTarjetaCredito - tarjetaCredito.getImporte();
            }
        }

        tarjetaCreditos.remove(tarjetaCredito);

        for (int i = 0; valoresPago.size() > i; i++) {
            pago = valoresPago.get(i);
            if (pago.getCod_docum() == "tarjCred" & pago.getNro_docum() == tarjetaCredito.getVoucher() & tarjetaCredito.getCodMon() == pago.getCod_moneda() & tarjetaCredito.getTarjeta() == pago.getCod_banco() & tarjetaCredito.getImporte() == pago.getImporte()) {
                int a = valoresPago.indexOf(pago);
                if (a != -1) {
                    valoresPago.remove(a);
                }
            }
        }
    }

    public static void addTarjetaDebito(TarjetaDebito tarjetaDebito) {
        if (WebService.tarjetaDebitos.size() == 0) {
            totalTarjetaDebito = totalTarjetaDebito + tarjetaDebito.getImporte();
        } else {
            if (monedaTarjDebUlti != tarjetaDebito.getCod_Mon().trim()) {
                totalTarjetaDebito = 0D;
                for (int i = 0; i < tarjetaDebitos.size(); i++) {
                    TarjetaDebito tarj = tarjetaDebitos.get(i);
                    if (tarj.getCod_Mon().trim().equals("2") & monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                        totalTarjetaDebito = totalTarjetaDebito + (tarj.getImporte() * tipoCambio);
                    } else if (tarj.getCod_Mon().trim().equals("1") & monedaSeleccionada.getCod_Moneda().trim().equals("2")) {
                        totalTarjetaDebito = totalTarjetaDebito + Math.round(tarj.getImporte() / tipoCambio);
                    } else {
                        totalTarjetaDebito = totalTarjetaDebito + tarj.getImporte();
                    }
                }
            }
            if (tarjetaDebito.getCod_Mon().trim().equals("2") & monedaSeleccionada.getCod_Moneda().trim().equals("1") & monedaTarjDebUlti != tarjetaDebito.getCod_Mon().trim()) {
                totalTarjetaDebito = totalTarjetaDebito + (tarjetaDebito.getImporte() * tipoCambio);
            } else if (tarjetaDebito.getCod_Mon().trim().equals("1") & monedaSeleccionada.getCod_Moneda().trim().equals("2") & monedaTarjDebUlti != tarjetaDebito.getCod_Mon().trim()) {
                totalTarjetaDebito = totalTarjetaDebito + Math.round(tarjetaDebito.getImporte() / tipoCambio);
            } else {
                totalTarjetaDebito = totalTarjetaDebito + tarjetaDebito.getImporte();
            }
        }

        tarjetaDebitos.add(tarjetaDebito);

        pago = new ValoresPago("tarjDeb", tarjetaDebito.getVoucher(), tarjetaDebito.getCod_Mon(), tarjetaDebito.getTarjeta(), "", "", tarjetaDebito.getImporte());
        valoresPago.add(pago);
    }

    public static void removeTarjetaDebito(TarjetaDebito tarjetaDebito) {
        if (WebService.tarjetaDebitos.size() == 1) {
            totalTarjetaDebito = totalTarjetaDebito - tarjetaDebito.getImporte();
        } else {
            if (tarjetaDebito.getCod_Mon().trim().equals("2") & monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                totalTarjetaDebito = totalTarjetaDebito - (tarjetaDebito.getImporte() * tipoCambio);
            } else if (tarjetaDebito.getCod_Mon().trim().equals("1") & monedaSeleccionada.getCod_Moneda().trim().equals("2")) {
                totalTarjetaDebito = totalTarjetaDebito - Math.round(tarjetaDebito.getImporte() / tipoCambio);
            } else {
                totalTarjetaDebito = totalTarjetaDebito - tarjetaDebito.getImporte();
            }
        }

        tarjetaDebitos.remove(tarjetaDebito);

        for (int i = 0; valoresPago.size() > i; i++) {
            pago = valoresPago.get(i);
            if (pago.getCod_docum() == "tarjDeb" & pago.getNro_docum() == tarjetaDebito.getVoucher() & tarjetaDebito.getCod_Mon() == pago.getCod_moneda() & tarjetaDebito.getTarjeta() == pago.getCod_banco() & tarjetaDebito.getImporte() == pago.getImporte()) {
                int a = valoresPago.indexOf(pago);
                if (a != -1) {
                    valoresPago.remove(a);
                }
            }
        }
    }

    public static void addTransferencia(Transferencias transferencia) {
        if (WebService.transferencias.size() == 0) {
            totalTransferencias = totalTransferencias + transferencia.getImporte();
        } else {
            if (monedaTransferenciaUlti != transferencia.getCodMon().trim()) {
                totalTransferencias = 0D;
                for (int i = 0; i < transferencias.size(); i++) {
                    Transferencias tarj = transferencias.get(i);
                    if (tarj.getCodMon().trim().equals("2") & monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                        totalTransferencias = totalTransferencias + (tarj.getImporte() * tipoCambio);
                    } else if (tarj.getCodMon().trim().equals("1") & monedaSeleccionada.getCod_Moneda().trim().equals("2")) {
                        totalTransferencias = totalTransferencias + Math.round(tarj.getImporte() / tipoCambio);
                    } else {
                        totalTransferencias = totalTransferencias + tarj.getImporte();
                    }
                }
            }
            if (transferencia.getCodMon().trim().equals("2") & monedaSeleccionada.getCod_Moneda().trim().equals("1") & monedaTransferenciaUlti != transferencia.getCodMon().trim()) {
                totalTransferencias = totalTransferencias + (transferencia.getImporte() * tipoCambio);
            } else if (transferencia.getCodMon().trim().equals("1") & monedaSeleccionada.getCod_Moneda().trim().equals("2") & monedaTransferenciaUlti != transferencia.getCodMon().trim()) {
                totalTransferencias = totalTransferencias + Math.round(transferencia.getImporte() / tipoCambio);
            } else {
                totalTransferencias = totalTransferencias + transferencia.getImporte();
            }
        }

        transferencias.add(transferencia);

        pago = new ValoresPago("tranfb", Integer.valueOf(transferencia.getDetalle()), transferencia.getCodMon(), transferencia.getBanco(), transferencia.getCuentadestino(), WebService.fecha, transferencia.getImporte());
        valoresPago.add(pago);
    }

    public static void removeTransferencia(Transferencias transferencia) {
        if (WebService.transferencias.size() == 1) {
            totalTransferencias = totalTransferencias - transferencia.getImporte();
        } else {
            if (transferencia.getCodMon().trim().equals("2") & monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                totalTransferencias = totalTransferencias - (transferencia.getImporte() * tipoCambio);
            } else if (transferencia.getCodMon().trim().equals("1") & monedaSeleccionada.getCod_Moneda().trim().equals("2")) {
                totalTransferencias = totalTransferencias - Math.round(transferencia.getImporte() / tipoCambio);
            } else {
                totalTransferencias = totalTransferencias - transferencia.getImporte();
            }
        }
        transferencias.remove(transferencia);

        for (int i = 0; valoresPago.size() > i; i++) {
            pago = valoresPago.get(i);
            if (pago.getCod_docum() == "tranfb" & /*pago.getNro_docum() == 0 &*/ transferencia.getBanco() == pago.getCod_banco() & transferencia.getCuentadestino() == pago.getCod_sctatit() & transferencia.getImporte() == pago.getImporte()) {
                int a = valoresPago.indexOf(pago);
                if (a != -1) {
                    valoresPago.remove(a);
                }
            }
        }
    }

    public static void limpiarValores() {
        valoresPago.clear();

        totalEfectivo = 0D;
        efectivo = new ArrayList<>();

        totalEfectivoUSD = 0D;
        efectivoUSD = new ArrayList<>();

        totalCheques = 0D;
        cheques = new ArrayList<>();

        totalTarjetaCredito = 0D;
        tarjetaCreditos = new ArrayList<>();

        totalTarjetaDebito = 0D;
        tarjetaDebitos = new ArrayList<>();

        totalTransferencias = 0D;
        transferencias = new ArrayList<>();

        totalRetenciones = 0D;
        totalRetencionesUSD = 0D;
        retenciones = new ArrayList<>();

        totalVuelto = 0D;
        Vuelto = new ArrayList<>();

        totalCredSusUSD = 0D;
        totalCredSus = 0D;
        creditoSus = new ArrayList<>();

        totalDif = 0D;
        totalDifACuenta = 0D;
        dif_reten = new ArrayList<>();

    }


    /*public static void addValor(ValoresPago instaValor, double importe) {
        removeValor(instaValor);
        deudasValorPagar.add(new DeudaPagar(instaValor.getCod_docum() + " " + instaValor.getNro_docum(), String.valueOf(importe)));
        deudaValorSeleccionada.add(instaValor);
    }

    public static void removeValor(ValoresPago instaValor) {
        try{
            for (DeudaPagar valor : deudasValorPagar) {
                if (valor.getDocumento().equals(instaValor.getCod_docum() + " " + instaValor.getNro_docum())) {
                    deudasValorPagar.remove(valor);
                    break;
                }
            }
            for (ValoresPago valor : deudaValorSeleccionada) {
                if (valor.getCod_docum().equals(instaValor.getCod_docum()) & valor.getNro_docum()==instaValor.getNro_docum()) {
                    deudaValorSeleccionada.remove(valor);
                    break;
                }
            }
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }*/

    public static void addDeuda(ClienteCobranza instaDeuda, double imp_mov_mo) {
        removeDeuda(instaDeuda);
        deudasPagar.add(new DeudaPagar(instaDeuda.getCod_Docum() + " " + instaDeuda.getNro_Docum(), String.valueOf(imp_mov_mo)));
        deudasSeleccionadas.add(instaDeuda);
    }

    public static void removeDeuda(ClienteCobranza instaDeuda) {
        try {
            for (DeudaPagar item : deudasPagar) {
                if (item.getDocumento().equals(instaDeuda.getCod_Docum() + " " + instaDeuda.getNro_Docum())) {
                    deudasPagar.remove(item);
                    break;
                }
            }
            for (ClienteCobranza item : deudasSeleccionadas) {
                if (item.getCod_Docum().equals(instaDeuda.getCod_Docum()/* + " " + instaDeuda.getNro_Docum()*/) & item.getNro_Docum().equals(instaDeuda.getNro_Docum())) {
                    deudasSeleccionadas.remove(item);
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void addReten(ClienteCobranza instaDeuda, double imp_a_retenc) {
        removeReten(instaDeuda);
        deudasPagarRetenc.add(new DeudaPagar(instaDeuda.getCod_Docum() + " " + instaDeuda.getNro_Docum(), String.valueOf(imp_a_retenc)));
        deudasSeleccionadasReten.add(instaDeuda);
    }

    public static void removeReten(ClienteCobranza instaDeuda) {
        try {
            for (DeudaPagar item : deudasPagarRetenc) {
                if (item.getDocumento().equals(instaDeuda.getCod_Docum() + " " + instaDeuda.getNro_Docum())) {
                    deudasPagarRetenc.remove(item);
                    break;
                }
            }
            for (ClienteCobranza item : deudasSeleccionadasReten) {
                if (item.getCod_Docum().equals(instaDeuda.getCod_Docum()) & item.getNro_Docum().equals(instaDeuda.getNro_Docum())) {
                    deudasSeleccionadasReten.remove(item);
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void addInteres(ClienteCobranza instaDeuda, double imp_interes) {
        removeInteres(instaDeuda);
        deudasInteres.add(new DeudaPagar(instaDeuda.getCod_Docum() + " " + instaDeuda.getNro_Docum(), String.valueOf(imp_interes)));
        deudasSeleccionadasInteres.add(instaDeuda);
    }

    public static void removeInteres(ClienteCobranza instaDeuda) {
        try {
            for (DeudaPagar item : deudasInteres) {
                if (item.getDocumento().equals(instaDeuda.getCod_Docum() + " " + instaDeuda.getNro_Docum())) {
                    deudasInteres.remove(item);
                    break;
                }
            }
            for (ClienteCobranza item : deudasSeleccionadasInteres) {
                if (item.getCod_Docum().equals(instaDeuda.getCod_Docum()/* + " " + instaDeuda.getNro_Docum()*/) & item.getNro_Docum().equals(instaDeuda.getNro_Docum())) {
                    deudasSeleccionadasInteres.remove(item);
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void removeFacturas() {
        for (DeudaPagar item : deudasPagar) {
            deudasPagar.remove(item);
        }
    }

    /*----------------------------fin de config por empresa----------------------------*/

    /*----------------------------COBRANZA----------------------------*/

    private static void getBancos() {
        bancos = new ArrayList<>();
        String metodo = "Cobranzas/TraerBancos.php";
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRestSinParam(metodo);
        Banco banco = new Banco();
        try {
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                banco = new Banco();
                jsonReader.beginObject(); //leo objeto del array
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("cod_banco")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            banco.setCod_banco("NA");
                        } else {
                            banco.setCod_banco(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_banco")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            banco.setNom_banco("NA");
                        } else {
                            banco.setNom_banco(jsonReader.nextString());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                bancos.add(banco);
                jsonReader.endObject();
            }

            banco = new Banco();
            banco.setCod_banco("-1");
            banco.setNom_banco("Otro banco");
            bancos.add(banco);

            jsonReader.endArray();
            jsonReader.close();
        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }

    public static void ObtenerDatosTarjetaCredito() {
        tarjetasCredito = new ArrayList<>();
        String metodo = "Cobranzas/TraerTarjetasCredito.php";
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRestSinParam(metodo);
        TarjetaCredito tarjetaCredito = new TarjetaCredito();
        try {
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                tarjetaCredito = new TarjetaCredito();
                jsonReader.beginObject(); //leo objeto del array
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("cod_tarjeta")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            tarjetaCredito.setCod_tarjeta("NA");
                        } else {
                            tarjetaCredito.setCod_tarjeta(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_tarjeta")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            tarjetaCredito.setNom_tarjeta("NA");
                        } else {
                            tarjetaCredito.setNom_tarjeta(jsonReader.nextString());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                tarjetasCredito.add(tarjetaCredito);
                jsonReader.endObject();
            }

            tarjetaCredito = new TarjetaCredito();
            tarjetaCredito.setCod_tarjeta("-1");
            tarjetaCredito.setNom_tarjeta("Otra tarjeta                               ");
            tarjetasCredito.add(tarjetaCredito);

            jsonReader.endArray();
            jsonReader.close();
        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }

        monedasCheques = new ArrayList<>();
        /* get Monedas*/
        Moneda moneda = new Moneda();
        moneda.setTipoCambio(1D);
        moneda.setSim_Moneda(simboloMonedaNacional);
        moneda.setCod_Moneda(codMonedaNacional);
        monedasCheques.add(moneda);

        moneda = new Moneda();
        moneda.setTipoCambio(tipoCambio);
        moneda.setSim_Moneda(simboloMonedaTr);
        moneda.setCod_Moneda(codMonedaTr);
        monedasCheques.add(moneda);


        /* get tipos de doc*/
        tiposDoc = new ArrayList<>();
        metodo = "Cobranzas/TipoDocsUni.php";
        jsonReader = WebServiceRes.jsRespuestaWSRestSinParam(metodo);
        TipoDoc item = new TipoDoc();
        try {
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                item = new TipoDoc();
                jsonReader.beginObject(); //leo objeto del array
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("cod_doc_uni")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            item.setNom_doc_uni("NA");
                        } else {
                            item.setNom_doc_uni(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_doc_uni")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            item.setNom_doc_uni("NA");
                        } else {
                            item.setNom_doc_uni(jsonReader.nextString());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                tiposDoc.add(item);
                jsonReader.endObject();
            }

            item = new TipoDoc();
            item.setCod_doc_uni("-1");
            item.setNom_doc_uni("Otro tipo");
            tiposDoc.add(item);

            jsonReader.endArray();
            jsonReader.close();
        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }

    }

    public static void ObtenerDatosTarjetaDebito() {

        tarjetasDebito = new ArrayList<>();
        //String metodo="Cobranzas/TraerTarjetasDebito.php";
        String metodo = "Cobranzas/TraerTarjetasCredito.php";
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRestSinParam(metodo);
        TarjetaDebito tarjetadebito = new TarjetaDebito();
        try {
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                tarjetadebito = new TarjetaDebito();
                jsonReader.beginObject(); //leo objeto del array
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("cod_tarjeta")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            tarjetadebito.setCod_tarjeta("NA");
                        } else {
                            tarjetadebito.setCod_tarjeta(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_tarjeta")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            tarjetadebito.setNom_tarjeta("NA");
                        } else {
                            tarjetadebito.setNom_tarjeta(jsonReader.nextString());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                tarjetasDebito.add(tarjetadebito);
                jsonReader.endObject();
            }

            tarjetadebito = new TarjetaDebito();
            tarjetadebito.setCod_tarjeta("-1");
            tarjetadebito.setNom_tarjeta("Otra tarjeta                               ");
            tarjetasDebito.add(tarjetadebito);

            jsonReader.endArray();
            jsonReader.close();
        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }

        monedasCheques = new ArrayList<>();
        /* get Monedas*/
        Moneda moneda = new Moneda();
        moneda.setTipoCambio(1D);
        moneda.setSim_Moneda(simboloMonedaNacional);
        monedasCheques.add(moneda);

        moneda = new Moneda();
        moneda.setTipoCambio(tipoCambio);
        moneda.setSim_Moneda(simboloMonedaTr);
        monedasCheques.add(moneda);


        /* get tipos de doc*/
        tiposDoc = new ArrayList<>();
        metodo = "Cobranzas/TipoDocsUni.php";
        jsonReader = WebServiceRes.jsRespuestaWSRestSinParam(metodo);
        TipoDoc item = new TipoDoc();
        try {
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                item = new TipoDoc();
                jsonReader.beginObject(); //leo objeto del array
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("cod_doc_uni")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            item.setNom_doc_uni("NA");
                        } else {
                            item.setNom_doc_uni(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_doc_uni")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            item.setNom_doc_uni("NA");
                        } else {
                            item.setNom_doc_uni(jsonReader.nextString());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                tiposDoc.add(item);
                jsonReader.endObject();
            }

            item = new TipoDoc();
            item.setCod_doc_uni("-1");
            item.setNom_doc_uni("Otro tipo");
            tiposDoc.add(item);

            jsonReader.endArray();
            jsonReader.close();
        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }


    }

    public static void ObtenerDatosIngresarTransferencias(RequestParams params) {
        getBancos();
        tiposDoc = new ArrayList<>();
        String metodo = "Cobranzas/BancosPropios.php";
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo);
        BancoPropio item = new BancoPropio();
        try {
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                item = new BancoPropio();
                jsonReader.beginObject(); //leo objeto del array
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("nrocta_banco")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            item.setNrocta_banco("NA");
                        } else {
                            item.setNrocta_banco(jsonReader.nextString());
                        }
                    } else if (key.equals("cod_banco")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            item.setCod_banco("NA");
                        } else {
                            item.setCod_banco(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_banco")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            item.setNom_banco("NA");
                        } else {
                            item.setNom_banco(jsonReader.nextString());
                        }
                    } else if (key.equals("tipo_cta")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            item.setTipo_cta("NA");
                        } else {
                            item.setTipo_cta(jsonReader.nextString());
                        }
                    } else if (key.equals("cod_moneda")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            item.setCod_moneda("NA");
                        } else {
                            item.setCod_moneda(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_moneda")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            item.setNom_moneda("NA");
                        } else {
                            item.setNom_moneda(jsonReader.nextString());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }

                bancosPropios.add(item);
                jsonReader.endObject();
            }

            item = new BancoPropio();
            item.setCod_banco("-1");
            item.setNom_banco("Otro banco");
            bancosPropios.add(item);

            jsonReader.endArray();
            jsonReader.close();
        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }


        monedasCheques = new ArrayList<>();
        /* get Monedas*/
        Moneda moneda = new Moneda();
        moneda.setTipoCambio(1D);
        moneda.setSim_Moneda(simboloMonedaNacional);
        moneda.setCod_Moneda(codMonedaNacional);
        monedasCheques.add(moneda);

        moneda = new Moneda();
        moneda.setTipoCambio(tipoCambio);
        moneda.setSim_Moneda(simboloMonedaTr);
        moneda.setCod_Moneda(codMonedaTr);
        monedasCheques.add(moneda);


    }

    public static void TraerDeudas(RequestParams params) {
        deudasViaje = new ArrayList<>();
        //deudores = new ArrayList<ClienteCobranza>(  );
        JsonElement jelement = WebServiceRes.TraerDeudas();
        //JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(parms,metodo);
        ClienteCobranza deudor = new ClienteCobranza();
        try {
            JsonArray jobject = jelement.getAsJsonArray();
            errToken = "";
            for (JsonElement item : jobject) {
                deudor = new ClienteCobranza();

                deudor.setCod_Docum(item.getAsJsonObject().get("cod_docum").getAsString());
                deudor.setNro_Docum(item.getAsJsonObject().get("nro_docum").getAsString());
                deudor.setFecha_Vence(item.getAsJsonObject().get("fec_venc").getAsString());
                deudor.setCod_Moneda(item.getAsJsonObject().get("cod_moneda").getAsString());
                deudor.setSimMoneda(item.getAsJsonObject().get("simbolo_moneda").getAsString());
                deudor.setCod_dpto(item.getAsJsonObject().get("cod_dpto").getAsString());
                deudor.setSerie_docum(item.getAsJsonObject().get("serie_docum").getAsString());
                deudor.setCalcula_interes(item.getAsJsonObject().get("calcula_interes").getAsString());
                deudor.setHabilita(item.getAsJsonObject().get("habilita").getAsString());
                // Double imp_mov_mo = item.getAsJsonObject().get("imp_mov_mo").getAsDouble();
                // deudor.setImp_mov_mo(imp_mov_mo.intValue() );
               /* if (item.getAsJsonObject().get("imp_mov_mo").getAsString().contains(".")) {
                    deudor.setImp_mov_mo(Double.valueOf(item.getAsJsonObject().get("imp_mov_mo").getAsString().substring(0, item.getAsJsonObject().get("imp_mov_mo").getAsString().indexOf("."))));
                } else {*/
                deudor.setImp_mov_mo(Double.valueOf(item.getAsJsonObject().get("imp_mov_mo").getAsString()));
                //}

                deudor.setImp_a_retenc(Double.valueOf(item.getAsJsonObject().get("imp_a_retenc").getAsString()));

                deudor.setDescuento(Double.valueOf(item.getAsJsonObject().get("descuento").getAsString()));

                deudor.setEstado(0);
                deudasViaje.add(deudor);
            }
        } catch (Exception exc) {
            try {
                errToken = jelement.getAsJsonObject().get("error").getAsString();
                if (!errToken.toUpperCase().contains("TOKEN")) {
                    errToken = "";
                }
            } catch (Exception ex) {

            }
            exc.toString();
        }
    }


    public static void TraerDeudasMot(RequestParams params, String metodo) {
        deudasMotNoCob = new ArrayList<>();
        //deudores = new ArrayList<ClienteCobranza>(  );
        JsonElement jelement = WebServiceRes.TraerDeudasMot(params, metodo);
        //JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(parms,metodo);
        ClienteCobranza deudor = new ClienteCobranza();
        try {
            JsonArray jobject = jelement.getAsJsonArray();
            errToken = "";
            for (JsonElement item : jobject) {
                deudor = new ClienteCobranza();

                deudor.setCod_Docum(item.getAsJsonObject().get("cod_docum").getAsString());
                deudor.setNro_Docum(item.getAsJsonObject().get("nro_docum").getAsString());
                deudor.setCod_dpto(item.getAsJsonObject().get("cod_dpto").getAsString());
                deudor.setSerie_docum(item.getAsJsonObject().get("serie_docum").getAsString());
                deudasMotNoCob.add(deudor);
            }
        } catch (Exception exc) {
            try {
                errToken = jelement.getAsJsonObject().get("error").getAsString();
                if (!errToken.toUpperCase().contains("TOKEN")) {
                    errToken = "";
                }
            } catch (Exception ex) {

            }
            exc.toString();
        }
    }


    /*
    public static void TraerDeudas(RequestParams params)    {
        //JsonReader jsonReader =WebServiceRes.jsRespuestaWSRest(params,"Cobranzas/TraerDeudoresSinMoneda.php"); //obtengo datos
        JsonReader jsonReader =WebServiceRes.jsRespuestaWSRest(params,"Cobranzas/TraerDeudores.php"); //obtengo datos

        try
        {
            ClienteCobranza deudor = new ClienteCobranza();
            jsonReader.beginArray();
            deudas = new ArrayList<>(  );
            while (jsonReader.hasNext())
            {
                deudor = new ClienteCobranza();
                jsonReader.beginObject(); //leo objeto del array
                while (jsonReader.hasNext())
                {
                    String key = jsonReader.nextName();
                    if(key.equals( "cod_docum" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            deudor.setCod_Docum( "NA" );
                        }
                        else
                        {
                            deudor.setCod_Docum( jsonReader.nextString() );
                        }
                    }
                    else if(key.equals("nro_docum"))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            deudor.setNro_Docum( "NA" );
                        }
                        else
                        {
                            deudor.setNro_Docum( jsonReader.nextString() );
                        }
                    }
                    else if(key.equals( "fec_venc" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            deudor.setFecha_Vence( "NA" );
                        }
                        else
                        {
                            deudor.setFecha_Vence( jsonReader.nextString() );
                        }
                    }
                    else if(key.equals("cod_moneda"))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            deudor.setCod_Moneda( "NA" );
                        }
                        else
                        {
                            deudor.setCod_Moneda( jsonReader.nextString() );
                        }
                    }
                    else if(key.equals( "imp_mov_mo" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            deudor.setImp_mov_mo( 0 );
                        }
                        else
                        {
                            deudor.setImp_mov_mo( Integer.valueOf( jsonReader.nextString() ) );
                        }
                    }
                    else
                    {
                        jsonReader.skipValue();
                    }
                }
                deudor.setEstado( 0 );
                deudas.add( deudor );
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();
        }
        catch (Exception exc)
        {
            exc.toString();
        }

    /*    SyncHttpClient cliente = new SyncHttpClient(  );
        cliente.get(URL + "Cobranzas/TraerDeudoresSinMoneda.php", params, new AsyncHttpResponseHandler()  {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try
                {
                    String respuesta =  new String(responseBody,"UTF-8");
                    objResponseA = new JSONArray(respuesta);
                    JSONObject objetojSon= new JSONObject();
                    ClienteCobranza deudor = new ClienteCobranza();
                    deudas = new ArrayList<>(  );
                    if(objResponseA.length()>0)
                    {
                        for(int i = 0;i<objResponseA.length();i++)
                        {
                            objetojSon = objResponseA.getJSONObject(i);
                            deudor = new ClienteCobranza();

                           if(objetojSon.getString( "cod_docum" )!=null)
                            {
                                deudor.setCod_Docum( objetojSon.getString( "cod_docum" ) );
                            }
                            if(objetojSon.getString( "nro_docum" )!=null)
                            {
                                deudor.setNro_Docum( objetojSon.getString( "nro_docum" ) );
                            }
                            if(objetojSon.getString( "fec_venc" )!=null)
                            {
                                deudor.setFecha_Vence( objetojSon.getString( "fec_venc" ) );
                            }
                            if(objetojSon.getString( "cod_moneda" )!=null)
                            {
                                deudor.setCod_Moneda( objetojSon.getString( "cod_moneda" ) );
                            }
                            if(objetojSon.getString( "imp_mov_mo" )!=null)
                            {
                                try
                                {
                                    Double val = Double.valueOf(  objetojSon.getString( "imp_mov_mo" ) );
                                    deudor.setImp_mov_mo(val.intValue() );

                                }
                                catch (Exception ex)
                                {}
                            }
                            if(objetojSon.getString( "cod_aux" )!=null)
                            {
                                deudor.setCod_Aux( objetojSon.getString( "cod_aux" ) );
                            }
                            if(objetojSon.getString( "cod_banco" )!=null)
                            {
                                deudor
                                .setCod_Banco( objetojSon.getString( "cod_banco" ) );
                            }
                            deudor.setEstado( 0 );
                            deudas.add( deudor );
                        }
                    }
                }
                catch (Exception exc)
                {
                    exc.toString();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });*/

    //METODOS COBRANZA
    public static void TraerClientes(String metodo, RequestParams parms) {
        deudores = new ArrayList<ClienteCobranza>();
        JsonElement jelement = WebServiceRes.TraerClientes();
        //JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(parms,metodo);
        ClienteCobranza deudor = new ClienteCobranza();
        try {
            JsonArray jobject = jelement.getAsJsonArray();
            errToken = "";
            for (JsonElement item : jobject) {
                deudor = new ClienteCobranza();

                deudor.setNom_Tit(item.getAsJsonObject().get("nom_tit").getAsString());
                deudor.setCod_Tit_Gestion(item.getAsJsonObject().get("cod_tit").getAsString());
                deudor.setLongitud_Ubic(item.getAsJsonObject().get("longitud_ubic").getAsDouble());
                deudor.setLatiud_Ubic(item.getAsJsonObject().get("latitud_ubic").getAsDouble());
                deudor.setDireccion(item.getAsJsonObject().get("direccion").toString());

                deudores.add(deudor);
            }
            //jobject = jobject.getAsJsonObject("data");
            //JsonArray jarray = jobject.getAsJsonArray("translations");
            //jobject = jarray.get(0).getAsJsonObject();
            //String result = jobject.get("translatedText").getAsString();

        } catch (Exception exc) {
            try {
                errToken = jelement.getAsJsonObject().get("error").getAsString();
                if (!errToken.toUpperCase().contains("TOKEN")) {
                    errToken = "";
                }
            } catch (Exception ex) {

            }
            exc.toString();
        }

    }

    public static void ObtenerMonedas() {
        monedas.clear();
        String metodo = "Cobranzas/TraerTipoMoneda.php";
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRestSinParam(metodo);
        Moneda moneda = new Moneda();
        try {
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                moneda = new Moneda();
                jsonReader.beginObject(); //leo objeto del array
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("cod_moneda")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            moneda.setCod_Moneda("NA");
                        } else {
                            moneda.setCod_Moneda(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_moneda")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            moneda.setNom_Moneda("NA");
                        } else {
                            moneda.setNom_Moneda(jsonReader.nextString());
                        }
                    } else if (key.equals("simbolo_moneda")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            moneda.setSim_Moneda("NA");
                        } else {
                            moneda.setSim_Moneda(jsonReader.nextString());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                monedas.add(moneda);
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();
        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }

    public static void ObtenerDatosIngresarCheques() {
        getBancos();

        monedasCheques = new ArrayList<>();
        /* get Monedas*/
        Moneda moneda = new Moneda();
        moneda.setTipoCambio(1D);
        moneda.setSim_Moneda(simboloMonedaNacional);
        monedasCheques.add(moneda);

        moneda = new Moneda();
        moneda.setTipoCambio(tipoCambio);
        moneda.setSim_Moneda(simboloMonedaTr);
        monedasCheques.add(moneda);


        /* get tipos de doc*/
        tiposDoc = new ArrayList<>();
        String metodo = "Cobranzas/TipoDocsUni.php";
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRestSinParam(metodo);
        TipoDoc item = new TipoDoc();
        try {
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                item = new TipoDoc();
                jsonReader.beginObject(); //leo objeto del array
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("cod_doc_uni")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            item.setNom_doc_uni("NA");
                        } else {
                            item.setNom_doc_uni(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_doc_uni")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            item.setNom_doc_uni("NA");
                        } else {
                            item.setNom_doc_uni(jsonReader.nextString());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                tiposDoc.add(item);
                jsonReader.endObject();
            }

            item = new TipoDoc();
            item.setCod_doc_uni("-1");
            item.setNom_doc_uni("Otro tipo");
            tiposDoc.add(item);

            jsonReader.endArray();
            jsonReader.close();
        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }

    public static void TraerNumRecibo(String metodo, RequestParams params) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo);
        nro_recibo = "";
        editable = "";
        try {
            if (jsonReader == null) {
                System.out.println("Error: JSONREADER null");
            } else {
                jsonReader.beginArray();
                errToken = "";
                while (jsonReader.hasNext()) {
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        String key = jsonReader.nextName();
                        if (key.equals("nro_recibo")) {
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                                nro_recibo = "0";
                            } else {
                                nro_recibo = jsonReader.nextString();
                            }
                        } else if (key.equals("editable")) {
                            JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                            if (check == JsonToken.NULL) {
                                jsonReader.skipValue();
                                editable = "S";
                            } else {
                                editable = jsonReader.nextString();
                            }
                        } else {
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.endObject();
                }
                jsonReader.endArray();
            }
        } catch (Exception exc) {
            System.out.println("Error: WebService.TraerNumRecibo");
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }

   /* public static void TraerListaValores(RequestParams params,String metodo) {
        ListaTextViews = new ArrayList<Item>();
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params,metodo); //obtengo datos
        try
        {
            jsonReader.beginArray();
            boolean nroEncontrado = false;
            while (jsonReader.hasNext())
            {
                jsonReader.beginObject();
                ClienteCobranza instaItem = new ClienteCobranza();
                while (jsonReader.hasNext())
                { // Loop through all keys
                    String key = jsonReader.nextName();
                    if(key.equals( "cod_Docum" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            instaItem.setCod_Docum( "" );
                        }
                        else
                        {
                            instaItem.setCod_Docum( jsonReader.nextString() );
                        }
                    }
                    else if(key.equals( "cod_Moneda" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            instaItem.setCod_Moneda( "" );
                        }
                        else
                        {
                            instaItem.setCod_Moneda( jsonReader.nextString() );
                        }
                    }
                    else if(key.equals( "cod_art_gestion" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            instaItem.setCod_Art_Gestion( "" );
                        }
                        else
                        {
                            instaItem.setCod_Art_Gestion( jsonReader.nextString() );
                        }
                    }
                    else if(key.equals( "nom_articulo" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            instaItem.setNom_Articulo( "" );
                        }
                        else
                        {
                            instaItem.setNom_Articulo( jsonReader.nextString() );
                        }
                    }
                    else if(key.equals( "cantidad" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            instaItem.setCantidad( 0.0 );
                        }
                        else
                        {
                            instaItem.setCantidad( jsonReader.nextDouble() );
                        }
                    }
                    else if(key.equals( "precio_unitario" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            instaItem.setPrecio_Unitario( 0.0 );
                        }
                        else
                        {
                            instaItem.setPrecio_Unitario( jsonReader.nextDouble() );
                        }
                    }
                    else if(key.equals( "cod_tasa_iva" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            instaItem.setCod_Tasa_Iva( "" );
                        }
                        else
                        {
                            instaItem.setCod_Tasa_Iva( jsonReader.nextString() );
                        }
                    }
                    else if(key.equals( "cod_uni_vta" )) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setCod_uni_vta( "" );
                        } else {
                            instaItem.setCod_uni_vta( jsonReader.nextString() );
                        }
                    }
                    else if(key.equals( "porc_iva" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            instaItem.setPorc_Iva( "" );
                        }
                        else
                        {
                            instaItem.setPorc_Iva( jsonReader.nextString() );
                        }
                    }
                    else if(key.equals( "nro_doc_ref" ) && !nroEncontrado) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            Entrega_A_Realizar.setNro_doc_ref(0);
                        } else {
                            Entrega_A_Realizar.setNro_doc_ref( Integer.parseInt(jsonReader.nextString()));
                            nroEncontrado = true;
                        }
                    } else {
                        jsonReader.skipValue();
                    }

                }
                jsonReader.endObject();
                ListaTextViews.add( instaItem );

            }
            jsonReader.endArray();

        }
        catch (Exception exc)
        {
            exc.toString();
        }

    }*/

    public static void IngresarCobranza(String metodo, RequestParams parametrosIng) {

        System.out.println(URL + metodo);
        System.out.println(parametrosIng.toString());
        boolean retorno = false;
        reto_AgregaCobranza = "";
        nro_transRec = 0;
        try {
            String sUrl = URL + metodo + "?" + parametrosIng;
            java.net.URL urlServer = new URL(sUrl);
            HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
            urlConn.setRequestProperty("Authorization", WebService.token);
            urlConn.setConnectTimeout(5000); //<- 3Seconds Timeout
            urlConn.connect();
            if (urlConn.getResponseCode() == 200) {
                System.out.println(urlConn.getResponseMessage());
                JsonReader jsonReader;
                InputStream responseBody = urlConn.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                jsonReader = new JsonReader(responseBodyReader);
                jsonReader.setLenient(true);
                jsonReader.beginObject(); // Start processing the JSON object
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName(); // Fetch the next key
                    if (key.equals("retorno")) {
                        reto_AgregaCobranza = jsonReader.nextString();
                        if (reto_AgregaCobranza.toUpperCase().equals("OK")) {
                            errToken = "";
                        } else {
                            errToken = reto_AgregaCobranza;
                            if (!errToken.toUpperCase().contains("TOKEN")) {
                                errToken = "";
                            }
                        }
                        System.out.println(reto_AgregaCobranza);
                    }
                    if (key.equals("nro_trans")) {
                        nro_transRec = jsonReader.nextInt();
                       /* if(nro_transRec > 0){
                            reto_AgregaCobranza = "ok";
                        }*/
                        WebService.nro_trans_impresion = nro_transRec.toString();
                        System.out.println(nro_transRec);
                    }
                }
            } else {
                reto_AgregaCobranza = "Error con conexion al servidor";
                nro_transRec = 0;
            }
        } catch (Exception exc) {
            System.out.println(exc.toString());
            //reto_AgregaCobranza = reto_AgregaCobranza + "Error al recibir datos desde el servidor";
        }
    }

    /*public static void TraerListaCobranza(RequestParams params,String metodo) {
        ListaTextViews = new ArrayList<ClienteCobranza>();
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params,metodo);
        try
        {
            jsonReader.beginArray();
            boolean nroEncontrado = false;
            while (jsonReader.hasNext())
            {
                jsonReader.beginObject();
                ClienteCobranza instaCobr = new ClienteCobranza();
                while (jsonReader.hasNext())
                { // Loop through all keys
                    String key = jsonReader.nextName();
                    if(key.equals( "nom_Tit" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            instaCobr.setNom_Tit("");
                        }
                        else
                        {
                            instaCobr.setNom_Tit(jsonReader.nextString());
                        }
                    }
                    else if(key.equals( "cod_Docum" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            instaCobr.setCod_Docum("");
                        }
                        else
                        {
                            instaCobr.setCod_Docum(jsonReader.nextString());
                        }
                    }
                    else if(key.equals( "nro_Docum" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            instaCobr.setNro_Docum("");
                        }
                        else
                        {
                            instaCobr.setNro_Docum(jsonReader.nextString());
                        }
                    }
                    else if(key.equals( "fecha_Vence" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            instaCobr.setFecha_Vence("");
                        }
                        else
                        {
                            instaCobr.setFecha_Vence(jsonReader.nextString());
                        }
                    }
                    else if(key.equals( "cod_Moneda" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            instaCobr.setCod_Moneda("");
                        }
                        else
                        {
                            instaCobr.setCod_Moneda(jsonReader.nextString());
                        }
                    }
                    else if(key.equals( "imp_mov_mo" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            instaCobr.setImp_mov_mo(0);
                        }
                        else
                        {
                            instaCobr.setImp_mov_mo(jsonReader.nextInt());
                        }
                    }
                    else if(key.equals( "cod_Aux" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            instaCobr.setCod_Aux("");
                        }
                        else
                        {
                            instaCobr.setCod_Aux(jsonReader.nextString());
                        }
                    }
                    else if(key.equals( "cod_Banco" )) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaCobr.setCod_Banco("");
                        } else {
                            instaCobr.setCod_Banco(jsonReader.nextString());
                        }
                    }
                    else if(key.equals( "cod_Tit" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            instaCobr.setCod_Tit("");
                        }
                        else
                        {
                            instaCobr.setCod_Tit(jsonReader.nextString());
                        }
                    }
                    else if(key.equals( "estado" )) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaCobr.setEstado(0);
                        } else {
                            instaCobr.setEstado(jsonReader.nextInt());
                        }
                    } else if(key.equals( "totalEntregado" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            instaCobr.setTotalEntregado(0);
                        }
                        else
                        {
                            instaCobr.setTotalEntregado(jsonReader.nextInt());
                        }
                    }else if(key.equals( "cod_dpto" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            instaCobr.setCod_dpto("");
                        }
                        else
                        {
                            instaCobr.setCod_dpto(jsonReader.nextString());
                        }
                    }else if(key.equals( "serie_docum" ))
                    {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL)
                        {
                            jsonReader.skipValue();
                            instaCobr.setSerie_docum("");
                        }
                        else
                        {
                            instaCobr.setSerie_docum(jsonReader.nextString());
                        }
                    }

                    else {
                        jsonReader.skipValue();
                    }

                }
                jsonReader.endObject();
                ListaTextViews.add(instaCobr);

            }
            jsonReader.endArray();

        }
        catch (Exception exc)
        {
            exc.toString();
        }
    }*/

    public static void ValidarRecibo(String metodo, RequestParams params1) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params1, metodo);
        try {
            //CAMBIAR A RECIBOVALIDADO
            n_recibo = new FactValidada();

            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String key = jsonReader.nextName();
                if (key.equals("error")) {
                    errToken = jsonReader.nextString();
                    if (errToken.toUpperCase().equals("OK")) {
                        errToken = "";
                    }

                    if (!errToken.toUpperCase().contains("TOKEN")) {
                        errToken = "";
                    }
                }
                if (key.equals("recibo")) {
                    JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                    if (check == JsonToken.NULL) {
                        jsonReader.skipValue();
                        n_recibo.setFact("Recibo Invalido");
                    } else {
                        n_recibo.setFact(jsonReader.nextString());
                    }
                }
              /*  else if(key.equals( "venc" )) {
                    JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                    if (check == JsonToken.NULL) {
                        jsonReader.skipValue();
                    } else {
                        n_factu.setVence( jsonReader.nextString().replace( "00:00:00.0000000","" ).trim());
                        String [] valoresfecha = n_factu.getVence().split( "-" );
                           }
                }
                else if(key.equals( "timbr" )) {
                    JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                    if (check == JsonToken.NULL) {
                        jsonReader.skipValue();
                    } else {
                        n_factu.setTimbr( jsonReader.nextString() );
                    }
                }*/
                else if (key.equals("idRecibo")) {
                    JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                    if (check == JsonToken.NULL) {
                        jsonReader.skipValue();
                    } else {
                        n_recibo.setId_Factura(jsonReader.nextString());
                    }
                } else {
                    jsonReader.skipValue();
                }
            }
            if (n_recibo.getFact() == null) {
                n_recibo.setFact("Recibo Invalido");
            }
            jsonReader.endObject();
            jsonReader.close();
//            }
        } catch (Exception exc) {
            n_recibo.setFact("Recibo Invalido");
        }
    }

    public static void AnularRecibo(RequestParams params, String metodo) {
        banderaAnuladas = false;
        try {
            JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo);
            jsonReader.beginObject(); // Start processing the JSON object
            while (jsonReader.hasNext()) { // Loop through all keys
                String key = jsonReader.nextName(); // Fetch the next key
                if (key.equals("resultado")) {
                    String valor = jsonReader.nextString();
                    if (valor.equals("ok")) {
                        banderaAnuladas = true;
                        errToken = "";
                    } else {
                        errToken = valor;
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                        banderaAnuladas = false;
                    }
                }
            }
        } catch (Exception e) {
            banderaAnuladas = false;
        }
    }

    public static void TraerListaRecibosAnuladas(RequestParams params, String metodo) {
        ArrayRecibosAnuladas = new ArrayList<Anulada>();
        SyncHttpClient cliente = new SyncHttpClient();
        cliente.addHeader("Authorization", WebService.token);
        cliente.get(URL + metodo, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                banderaRecibosAnuladas = true;
                try {
                    String respuesta = new String(responseBody, "UTF-8");
                    objResponseA = new JSONArray(respuesta);
                    errToken = "";
                    Anulada instanciaAnulada = new Anulada();
                    JSONObject objetojSon;
                    if (objResponseA.length() > 0) {
                        for (int i = 0; i < objResponseA.length(); i++) {
                            instanciaAnulada = new Anulada();
                            objetojSon = objResponseA.getJSONObject(i);
                            if (objetojSon.getString("nro_trans") != null) {
                                instanciaAnulada.setNro_trans(objetojSon.getString("nro_trans"));
                            }
                            if (objetojSon.getString("nro_doc") != null) {
                                instanciaAnulada.setNro_docum(objetojSon.getString("nro_doc"));
                            }
                            if (objetojSon.getString("nom_tit") != null) {
                                instanciaAnulada.setNom_tit(objetojSon.getString("nom_tit"));
                            }
                            if (objetojSon.getString("hora") != null) {
                                instanciaAnulada.setHora(objetojSon.getString("hora"));
                            }
                            if (objetojSon.getString("cod_tit") != null) {
                                instanciaAnulada.setCod_tit(objetojSon.getString("cod_tit"));
                            }
                            ArrayRecibosAnuladas.add(instanciaAnulada);
                        }
                    }
                } catch (Exception exc) {
                    try {
                        String respuesta = new String(responseBody, "UTF-8");
                        JSONObject objetojSon = new JSONObject(respuesta);
                        errToken = objetojSon.getString("error");
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    } catch (Exception ex) {

                    }
                    banderaRecibosAnuladas = false;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                banderaRecibosAnuladas = false;
            }
        });

    }

    public static void TraerRecibosXDia(String metodo, RequestParams parms) {
        SyncHttpClient client = new SyncHttpClient();
        client.addHeader("Authorization", WebService.token);
        client.get(URL + metodo, parms, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    FacturaXDia instanciaRec = new FacturaXDia();
                    listarecibos = new ArrayList<>();
                    recibosDelDia = new ArrayList<FacturaXDia>();
                    String respuesta = new String(responseBody, "UTF-8");
                    objResponseA = new JSONArray(respuesta);
                    errToken = "";
                    JSONObject objetojSon = new JSONObject();
                    if (objResponseA.length() > 0) {
                        contadorRecibo = 0;
                        for (int i = 0; i < objResponseA.length(); i++) {
                            instanciaRec = new FacturaXDia();
                            objetojSon = objResponseA.getJSONObject(i);

                            if (objetojSon.getString("nro_trans").toString() != "" && objetojSon.getString("nro_trans") != null) {
                                instanciaRec.setNro_Trans(objetojSon.getString("nro_trans").toString());
                            }

                            if (objetojSon.getString("nro_docum").toString() != "" && objetojSon.getString("nro_docum") != null) {
                                instanciaRec.setNro_Docum(objetojSon.getString("nro_docum").toString());
                            }
                            if (objetojSon.getString("nom_tit").toString() != "" && objetojSon.getString("nom_tit") != null) {
                                instanciaRec.setNom_Tit(objetojSon.getString("nom_tit").toString());
                            }
                            if (objetojSon.getString("hora").toString() != "" && objetojSon.getString("hora") != null) {
                                instanciaRec.setHora(objetojSon.getString("hora").toString());
                            }

                            if (objetojSon.getString("cod_tit").toString() != "" && objetojSon.getString("cod_tit") != null) {
                                instanciaRec.setCod_Tit(objetojSon.getString("cod_tit").toString());
                            }
                            if (objetojSon.getString("cod_emp").toString() != "" && objetojSon.getString("cod_emp") != null) {
                                instanciaRec.setCod_emp(objetojSon.getString("cod_emp").toString());
                            }
                            if (objetojSon.getString("calcula_interes").toString() != "" && objetojSon.getString("calcula_interes") != null) {
                                instanciaRec.setCalcula_interes(objetojSon.getString("calcula_interes").toString());
                            }
                            if (objetojSon.getString("nom_emp").toString() != "" && objetojSon.getString("nom_emp") != null) {
                                instanciaRec.setNom_emp(objetojSon.getString("nom_emp").toString());
                            }
                            if (objetojSon.getString("cod_moneda").toString() != "" && objetojSon.getString("cod_moneda") != null) {
                                instanciaRec.setCod_moneda(objetojSon.getString("cod_moneda").toString());
                            }
                            contadorRecibo++;
                            listarecibos.add(instanciaRec);
                        }
                        recibosDelDia.add(instanciaRec);
                    }

                } catch (Exception ex) {
                    try {
                        String respuesta = new String(responseBody, "UTF-8");
                        JSONObject objetojSon = new JSONObject(respuesta);
                        errToken = objetojSon.getString("error");
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    } catch (Exception exc) {

                    }
                    ex.toString();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String er1 = error.toString();
            }
        });
    }

    public static void TraerFacturasIntXDia(String metodo, RequestParams parms) {
        SyncHttpClient client = new SyncHttpClient();
        client.addHeader("Authorization", WebService.token);
        client.get(URL + metodo, parms, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    FacturaXDia instanciaRec = new FacturaXDia();
                    listafact = new ArrayList<>();
                    facturasDelDia = new ArrayList<FacturaXDia>();
                    String respuesta = new String(responseBody, "UTF-8");
                    objResponseA = new JSONArray(respuesta);
                    errToken = "";
                    JSONObject objetojSon = new JSONObject();
                    if (objResponseA.length() > 0) {
                        contadorRecibo = 0;
                        for (int i = 0; i < objResponseA.length(); i++) {
                            instanciaRec = new FacturaXDia();
                            objetojSon = objResponseA.getJSONObject(i);

                            if (objetojSon.getString("nro_trans").toString() != "" && objetojSon.getString("nro_trans") != null) {
                                instanciaRec.setNro_Trans(objetojSon.getString("nro_trans").toString());
                            }

                            if (objetojSon.getString("nro_docum").toString() != "" && objetojSon.getString("nro_docum") != null) {
                                instanciaRec.setNro_Docum(objetojSon.getString("nro_docum").toString());
                            }
                            if (objetojSon.getString("nom_tit").toString() != "" && objetojSon.getString("nom_tit") != null) {
                                instanciaRec.setNom_Tit(objetojSon.getString("nom_tit").toString());
                            }
                            if (objetojSon.getString("hora").toString() != "" && objetojSon.getString("hora") != null) {
                                instanciaRec.setHora(objetojSon.getString("hora").toString());
                            }

                            if (objetojSon.getString("cod_tit").toString() != "" && objetojSon.getString("cod_tit") != null) {
                                instanciaRec.setCod_Tit(objetojSon.getString("cod_tit").toString());
                            }
                            contadorRecibo++;
                            listafact.add(instanciaRec);
                        }
                        facturasDelDia.add(instanciaRec);
                    }

                } catch (Exception ex) {
                    try {
                        String respuesta = new String(responseBody, "UTF-8");
                        JSONObject objetojSon = new JSONObject(respuesta);
                        errToken = objetojSon.getString("error");
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    } catch (Exception exc) {

                    }
                    ex.toString();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String er1 = error.toString();
            }
        });
    }

    public static void TraerObservacionesDia(String metodo, RequestParams parms) {
        SyncHttpClient client = new SyncHttpClient();
        client.addHeader("Authorization", WebService.token);
        client.get(URL + metodo, parms, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Observaciones instanciaObs = new Observaciones();
                    listaobservaciones = new ArrayList<>();
                    String respuesta = new String(responseBody, "UTF-8");
                    objResponseA = new JSONArray(respuesta);
                    errToken = "";
                    JSONObject objetojSon = new JSONObject();
                    if (objResponseA.length() > 0) {
                        contadorRecibo = 0;
                        for (int i = 0; i < objResponseA.length(); i++) {
                            instanciaObs = new Observaciones();
                            objetojSon = objResponseA.getJSONObject(i);

                            if (objetojSon.getString("nom_motivo").toString() != "" && objetojSon.getString("nom_motivo") != null) {
                                instanciaObs.setCod_motivo(objetojSon.getString("nom_motivo").toString());
                            }
                            if (objetojSon.getString("fec_proxv").toString() != "" && objetojSon.getString("fec_proxv") != null) {
                                instanciaObs.setFec_proxv(objetojSon.getString("fec_proxv").toString());
                            }
                            if (objetojSon.getString("cod_tit").toString() != "" && objetojSon.getString("cod_tit") != null) {
                                instanciaObs.setCod_tit(objetojSon.getString("cod_tit").toString());
                            }
                            if (objetojSon.getString("fec_doc").toString() != "" && objetojSon.getString("fec_doc") != null) {
                                instanciaObs.setFec_doc(objetojSon.getString("fec_doc").toString());
                            }
                            if (objetojSon.getString("descripcion").toString() != "" && objetojSon.getString("descripcion") != null) {
                                instanciaObs.setDescripcion(objetojSon.getString("descripcion").toString());
                            }
                            if (objetojSon.getString("cod_emp").toString() != "" && objetojSon.getString("cod_emp") != null) {
                                instanciaObs.setCod_emp(objetojSon.getInt("cod_emp"));
                            }
                            if (objetojSon.getString("nro_viaje").toString() != "" && objetojSon.getString("nro_viaje") != null) {
                                instanciaObs.setNum_viaje(objetojSon.getInt("nro_viaje"));
                            }
                            contadorRecibo++;
                            listaobservaciones.add(instanciaObs);
                        }
                    }

                } catch (Exception ex) {
                    try {
                        String respuesta = new String(responseBody, "UTF-8");
                        JSONObject objetojSon = new JSONObject(respuesta);
                        errToken = objetojSon.getString("error");
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    } catch (Exception exc) {

                    }
                    ex.toString();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String er1 = error.toString();
            }
        });
    }

    public static void TraerValoresPago(RequestParams params, String metodo) {
        ArrayValores = new ArrayList<ValoresPago>();
        SyncHttpClient cliente = new SyncHttpClient();
        cliente.addHeader("Authorization", WebService.token);
        cliente.get(URL + metodo, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                banderaValores = true;
                try {
                    String respuesta = new String(responseBody, "UTF-8");
                    objResponseA = new JSONArray(respuesta);
                    errToken = "";
                    ValoresPago instanciaValores = new ValoresPago();
                    JSONObject objetojSon;
                    if (objResponseA.length() > 0) {
                        for (int i = 0; i < objResponseA.length(); i++) {
                            instanciaValores = new ValoresPago();
                            objetojSon = objResponseA.getJSONObject(i);

                            if (objetojSon.getString("cod_docum") != null) {
                                instanciaValores.setCod_docum(objetojSon.getString("cod_docum"));
                            }
                            if (objetojSon.getString("nro_docum") != null) {
                                instanciaValores.setNro_docum(objetojSon.getInt("nro_docum"));
                            }
                            if (objetojSon.getString("cod_moneda") != null) {
                                instanciaValores.setCod_moneda(objetojSon.getString("cod_moneda"));
                            }
                            if (objetojSon.getString("cod_banco") != null) {
                                instanciaValores.setCod_banco(objetojSon.getString("cod_banco"));
                            }
                            if (objetojSon.getString("cod_sctatit") != null) {
                                instanciaValores.setCod_sctatit(objetojSon.getString("cod_sctatit"));
                            }
                            if (objetojSon.getString("fec_valor") != null) {
                                instanciaValores.setFec_valor(objetojSon.getString("fec_valor"));
                            }
                            if (objetojSon.getString("importe") != null) {
                                instanciaValores.setImporte(objetojSon.getDouble("importe"));
                            }
                            if (objetojSon.getString("cod_tenedor") != null) {
                                instanciaValores.setCod_tenedor(objetojSon.getString("cod_tenedor"));
                            }
                            ArrayValores.add(instanciaValores);
                        }
                    }
                } catch (Exception exc) {
                    try {
                        String respuesta = new String(responseBody, "UTF-8");
                        JSONObject objetojSon = new JSONObject(respuesta);
                        errToken = objetojSon.getString("error");
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    } catch (Exception ex) {

                    }
                    banderaValores = false;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                banderaValores = false;
            }
        });

    }

    public static void IngresarCierre(String metodo, RequestParams parametrosIng) {
        System.out.println(URL + metodo);
        System.out.println(parametrosIng.toString());
        //solo para pruebas
        try {
            String sUrl = URL + metodo + "?" + parametrosIng;
            java.net.URL urlServer = new URL(sUrl);
            HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
            urlConn.setRequestProperty("Authorization", WebService.token);
            urlConn.setConnectTimeout(3000); //<- 3Seconds Timeout
            urlConn.connect();
            if (urlConn.getResponseCode() == 200) {
                System.out.println(urlConn.getResponseMessage());
                JsonReader jsonReader;
                InputStream responseBody = urlConn.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                jsonReader = new JsonReader(responseBodyReader);
                jsonReader.beginObject(); // Start processing the JSON object
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName(); // Fetch the next key
                    if (key.equals("retorno")) {
                        reto_IngresarCierre = jsonReader.nextString();
                        if (reto_IngresarCierre.toUpperCase().equals("OK")) {
                            errToken = "";
                        } else {
                            errToken = reto_IngresarCierre;
                            if (!errToken.toUpperCase().contains("TOKEN")) {
                                errToken = "";
                            }
                        }
                        System.out.println(reto_IngresarCierre);
                    }
                    if (key.equals("nro_trans")) {
                        nro_transCierreCaja = jsonReader.nextInt();
                        WebService.nro_trans_impresion = nro_transCierreCaja.toString();
                        reto_IngresarCierre = "ok";
                        System.out.println(nro_trans_impresion);
                    }
                }
            } else {
                reto_IngresarCierre = "Error al recibir los datos";
                nro_transCierreCaja = 0;
            }
        } catch (Exception exc) {
            System.out.println(exc.toString());
        }
    }

    public static void AgregarObservaciones(String metodo, RequestParams parametrosIng) {

        System.out.println(URL + metodo);
        System.out.println(parametrosIng.toString());
        boolean retorno = false;
        try {
            String sUrl = URL + metodo + "?" + parametrosIng;
            java.net.URL urlServer = new URL(sUrl);
            HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
            urlConn.setRequestProperty("Authorization", WebService.token);
            urlConn.setConnectTimeout(3000); //<- 3Seconds Timeout
            urlConn.connect();
            if (urlConn.getResponseCode() == 200) {
                System.out.println(urlConn.getResponseMessage());
                JsonReader jsonReader;
                InputStream responseBody = urlConn.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                jsonReader = new JsonReader(responseBodyReader);
                jsonReader.beginObject(); // Start processing the JSON object
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName(); // Fetch the next key
                    if (key.equals("retorno")) {
                        reto_Observaciones = jsonReader.nextString();
                        if (reto_Observaciones.toUpperCase().equals("OK")) {
                            errToken = "";
                        } else {
                            errToken = reto_Observaciones;
                            if (!errToken.toUpperCase().contains("TOKEN")) {
                                errToken = "";
                            }
                        }
                        System.out.println(reto_Observaciones);
                    }
                    if (key.equals("nro_trans")) {
                        nro_transObs = jsonReader.nextInt();
                        System.out.println(nro_transObs);
                    }
                }
            } else {
                reto_Observaciones = "Error al recibir los datos";
                nro_transObs = 0;
            }
        } catch (Exception exc) {
            System.out.println(exc.toString());
        }
    }


    //////////////////////VIAJES COBRADOR /////////////////////////////////
    public static void TraerViajesCobrador(RequestParams params, String metodo) {
        viajesCobradorUsu = new ArrayList<>();
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo);
        try {
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                Viaje InstanciaViajeCobrador = new Viaje();
                jsonReader.beginObject();
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName();
                    double latitud = 0.0;
                    double longitud = 0.0;
                    if (key.equals("turno")) {
                        InstanciaViajeCobrador.setTurno(jsonReader.nextString());
                    } else if (key.equals("nro_viaje")) {
                        InstanciaViajeCobrador.setNumViaje(jsonReader.nextString());
                    } else if (key.equals("tipo")) {
                        InstanciaViajeCobrador.setTipo(jsonReader.nextString());
                    } else if (key.equals("fec_doc")) {
                        InstanciaViajeCobrador.setFecha(jsonReader.nextString());
                    } else if (key.equals("latitud")) {
                        latitud = jsonReader.nextDouble();
                    } else if (key.equals("longitud")) {
                        longitud = jsonReader.nextDouble();
                    } else {
                        jsonReader.skipValue();
                    }
                    LatLng localizacion = new LatLng(latitud, longitud);
                    InstanciaViajeCobrador.setLocalizacionCentral(localizacion);
                }
                viajesCobradorUsu.add(InstanciaViajeCobrador);
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();
        } catch (IOException e) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                e.toString();
            } catch (Exception exct) {
                System.out.println(exct.toString());
            }
        }
    }

  /*  public static void TraerViajesCobrador1(RequestParams params, String metodo) {
        SyncHttpClient cliente = new SyncHttpClient();
        cliente.setTimeout(3);
        cliente.get(URL + metodo, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    viajesCobradorUsu = new ArrayList<>();
                    String respuesta = new String(responseBody, "UTF-8");
                    objResponseA = new JSONArray(respuesta);
                    JSONObject objetojSon;
                    Viaje InstanciaViajeCobrador = new Viaje();
                    if (objResponseA.length() != 0) {
                        for (int i = 0; i < objResponseA.length(); i++) {
                            objetojSon = objResponseA.getJSONObject(i);
                            double latitud = 0.0;
                            double longitud = 0.0;
                            if (objetojSon.getString("turno").toString() != "") {
                                InstanciaViajeCobrador.setTurno(objetojSon.getString("turno").toString().trim());
                            }
                            if (objetojSon.getString("nro_viaje").toString() != "") {
                                InstanciaViajeCobrador.setNumViaje(objetojSon.getString("nro_viaje").toString().trim());
                            }
                            if (objetojSon.getString("tipo").toString() != "") {
                                InstanciaViajeCobrador.setTipo(objetojSon.getString("tipo").toString().trim());
                            }
                            if (objetojSon.getString("fec_doc").toString() != "") {
                                InstanciaViajeCobrador.setFecha(objetojSon.getString("fec_doc").toString().trim());
                            }
                            latitud = objetojSon.getDouble("latitud");
                            longitud = objetojSon.getDouble("longitud");
                            LatLng localizacion = new LatLng(latitud, longitud);
                            InstanciaViajeCobrador.setLocalizacionCentral(localizacion);
                            viajesCobradorUsu.add(InstanciaViajeCobrador);
                            InstanciaViajeCobrador = new Viaje();
                        }
                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
            }
        });
    }*/

    public static void TrearMotivosNoCobro(String metodo) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRestSinParam(metodo); //obtengo datos
        try {
            ArrayMotivosNoCobro = new ArrayList<>();
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                MotivoPausa pausa = new MotivoPausa();
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName();
                    if (key.equals("cod_motivo")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            pausa.setCod_Pausa("");

                        } else {
                            pausa.setCod_Pausa(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_motivo")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            pausa.setNom_Pausa("");
                        } else {
                            pausa.setNom_Pausa(jsonReader.nextString());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                ArrayMotivosNoCobro.add(pausa);
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();

        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }

    public static void TraerReclamos(String metodo) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRestSinParam(metodo); //obtengo datos
        try {
            ArrayReclamos = new ArrayList<>();
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                MotivoPausa pausa = new MotivoPausa();
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName();
                    if (key.equals("cod_concepto")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            pausa.setCod_Pausa("");

                        } else {
                            pausa.setCod_Pausa(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_concepto")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            pausa.setNom_Pausa("");
                        } else {
                            pausa.setNom_Pausa(jsonReader.nextString());
                        }
                    } else if (key.equals("pide_cantidad")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            pausa.setPide_cant("");
                        } else {
                            pausa.setPide_cant(jsonReader.nextString());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                ArrayReclamos.add(pausa);
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();

        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }

    public static void TraerCajasCobrador(String metodo) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRestSinParam(metodo); //obtengo datos
        try {
            ArrayCajasCobranza = new ArrayList<>();
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                CajaCobranza caja = new CajaCobranza();
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName();
                    if (key.equals("cod_caja")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            caja.setCod_caja("");

                        } else {
                            caja.setCod_caja(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_caja")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            caja.setNom_caja("");
                        } else {
                            caja.setNom_caja(jsonReader.nextString());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                ArrayCajasCobranza.add(caja);
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();

        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }

    public static void TraerDeudasViaje(/*RequestParams params*/) {
        deudasViaje = new ArrayList<>();
        //deudores = new ArrayList<ClienteCobranza>(  );
        JsonElement jelement = WebServiceRes.TraerDeudasViaje();
        //JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(parms,metodo);
        ClienteCobranza deudor = new ClienteCobranza();
        try {
            JsonArray jobject = jelement.getAsJsonArray();
            errToken = "";
            for (JsonElement item : jobject) {
                deudor = new ClienteCobranza();

                deudor.setCodEmp(item.getAsJsonObject().get("cod_emp").getAsString());
                deudor.setCod_Docum(item.getAsJsonObject().get("cod_docum").getAsString());
                deudor.setNro_Docum(item.getAsJsonObject().get("nro_docum").getAsString());
                deudor.setFecha_Vence(item.getAsJsonObject().get("fec_venc").getAsString());
                deudor.setCod_Moneda(item.getAsJsonObject().get("cod_moneda").getAsString());
                deudor.setSimMoneda(item.getAsJsonObject().get("simbolo_moneda").getAsString());
                deudor.setCod_dpto(item.getAsJsonObject().get("cod_dpto").getAsString());
                deudor.setSerie_docum(item.getAsJsonObject().get("serie_docum").getAsString());
                deudor.setHabilita(item.getAsJsonObject().get("habilita").getAsString());
                // Double imp_mov_mo = item.getAsJsonObject().get("imp_mov_mo").getAsDouble();
                // deudor.setImp_mov_mo(imp_mov_mo.intValue() );

                String cod_mon = deudor.getCod_Moneda().trim();

                if (cod_mon.equals("1")) {
                    if (item.getAsJsonObject().get("imp_mov_mo").getAsString().contains(".")) {
                        deudor.setImp_mov_mo(Double.valueOf(item.getAsJsonObject().get("imp_mov_mo").getAsString().substring(0, item.getAsJsonObject().get("imp_mov_mo").getAsString().indexOf("."))));
                    } else {
                        deudor.setImp_mov_mo(Double.valueOf(item.getAsJsonObject().get("imp_mov_mo").getAsString()));
                    }
                } else {
                    deudor.setImp_mov_mo(Double.valueOf(item.getAsJsonObject().get("imp_mov_mo").getAsString()));
                }

                deudor.setImp_a_retenc(Double.valueOf(item.getAsJsonObject().get("imp_a_retenc").getAsString()));

                deudor.setCalcula_interes(item.getAsJsonObject().get("calcula_interes").getAsString());
                deudor.setDescuento(Double.valueOf(item.getAsJsonObject().get("descuento").getAsString()));

                deudor.setEstado(0);
                deudasViaje.add(deudor);
            }

        } catch (Exception exc) {
            try {
                errToken = jelement.getAsJsonObject().get("error").getAsString();
                if (!errToken.toUpperCase().contains("TOKEN")) {
                    errToken = "";
                }
            } catch (Exception ex) {

            }
            exc.toString();
        }
    }

    public static void TraerClientesViajes(RequestParams params, String metodo) {
        clienteTraidos = new ArrayList<>();
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo);
        try {
            if (jsonReader == null) {
                System.out.println("jsonReader null");
            } else {
                jsonReader.beginArray();
                errToken = "";
                while (jsonReader.hasNext()) {
                    ClienteCobranza InstanciaEntrega = new ClienteCobranza();
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        String key = jsonReader.nextName();
                        if (key.equals("cod_tit_gestion")) {
                            InstanciaEntrega.setCod_Tit_Gestion(jsonReader.nextString());
                        } else if (key.equals("nom_tit")) {
                            InstanciaEntrega.setNom_Tit(jsonReader.nextString());
                        } else if (key.equals("direccion")) {
                            InstanciaEntrega.setDireccion(jsonReader.nextString());
                        } else if (key.equals("tel_particular")) {
                            InstanciaEntrega.setTelParticular(jsonReader.nextString());
                        } else if (key.equals("tel_laboral")) {
                            InstanciaEntrega.setTelLaboral(jsonReader.nextString());
                        } else if (key.equals("cod_emp")) {
                            InstanciaEntrega.setCodEmp(jsonReader.nextString());
                        } else if (key.equals("nom_emp")) {
                            InstanciaEntrega.setNomEmp(jsonReader.nextString());
                        } else if (key.equals("prioridad")) {
                            InstanciaEntrega.setPrioridad(jsonReader.nextString());
                        } else if (key.equals("vencimiento")) {
                            InstanciaEntrega.setFecha_Vence(jsonReader.nextString());
                        } else if (key.equals("latitud_ubic")) {
                            InstanciaEntrega.setLatiud_Ubic(jsonReader.nextDouble());
                        } else if (key.equals("longitud_ubic")) {
                            InstanciaEntrega.setLongitud_Ubic(jsonReader.nextDouble());
                        } else {
                            jsonReader.skipValue();
                        }
                    }
                    clienteTraidos.add(InstanciaEntrega);
                    jsonReader.endObject();
                }
                jsonReader.endArray();
                jsonReader.close();
            }
        } catch (IOException e) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                e.toString();
            } catch (Exception exct) {

            }
        }
    }

    public static void GuardarDestino(RequestParams params, String metodo) {
        SyncHttpClient cliente = new SyncHttpClient();
        cliente.addHeader("Authorization", WebService.token);
        //System.out.println(params.toString()); //comentado 0/05/2019
        cliente.get(URL + metodo, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    respuesta = new String(responseBody, "UTF-8");
                    if (respuesta.toUpperCase().contains("TOKEN")) {
                        errToken = respuesta;
                    } else {
                        errToken = "";
                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String dioError = error.toString();
                Log.d("Failed: ", "" + statusCode);
                Log.d("Error : ", "" + error);
            }
        });
    }

    public static void RecomendarNumeroFactura(String metodo, RequestParams params) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo);
        cod_suc_tribut = "";
        cod_fac_trubut = "";
        cod_fac_trubut = "";
        nro_inicial = "";
        nro_fin = "";
        nro_sugerido = "";
        try {
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("0")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            cod_suc_tribut = "0";
                            cod_suc_tribut = "0";
                        } else {
                            cod_suc_tribut = jsonReader.nextString();
                        }
                    } else if (key.equals("1")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            cod_fac_trubut = "10000";
                        } else {
                            cod_fac_trubut = jsonReader.nextString();
                        }
                    } else if (key.equals("2")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            nro_inicial = "0";
                        } else {
                            nro_inicial = jsonReader.nextString();
                        }
                    } else if (key.equals("3")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            nro_fin = "10000";
                        } else {
                            nro_fin = jsonReader.nextString().trim();
                        }
                    } else if (key.equals("4")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            nro_sugerido = "3000";
                        } else {
                            nro_sugerido = jsonReader.nextString().trim();
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                jsonReader.endObject();
            }
            jsonReader.endArray();
        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }

    public static void validarFacturaCobranza(String metodo, RequestParams params1) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params1, metodo);
        try {
            n_factu = new FactValidada();
            /*jsonReader.beginArray();
            while(jsonReader.hasNext())
            {*/
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String key = jsonReader.nextName();
                if (key.equals("error")) {
                    errToken = jsonReader.nextString();
                    if (errToken.toUpperCase().equals("OK")) {
                        errToken = "";
                    }
                    if (!errToken.toUpperCase().contains("TOKEN")) {
                        errToken = "";
                    }
                } else if (key.equals("fact")) {
                    JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                    if (check == JsonToken.NULL) {
                        jsonReader.skipValue();
                        n_factu.setFact("Factura Invalida");
                    } else {
                        n_factu.setFact(jsonReader.nextString());
                    }
                } else if (key.equals("venc")) {
                    JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                    if (check == JsonToken.NULL) {
                        jsonReader.skipValue();
                    } else {
                        n_factu.setVence(jsonReader.nextString().replace("00:00:00.0000000", "").trim());
                        String[] valoresfecha = n_factu.getVence().split("-");
                            /*String nuevaFecha = valoresfecha[0].trim()+"-"+valoresfecha[2].trim()+"-"+valoresfecha[1].trim();
                            n_factu.setVence(nuevaFecha);*/
                    }
                } else if (key.equals("timbr")) {
                    JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                    if (check == JsonToken.NULL) {
                        jsonReader.skipValue();
                    } else {
                        n_factu.setTimbr(jsonReader.nextString());
                    }
                } else if (key.equals("idfactura")) {
                    JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                    if (check == JsonToken.NULL) {
                        jsonReader.skipValue();
                    } else {
                        n_factu.setId_Factura(jsonReader.nextString());
                    }
                } else {
                    jsonReader.skipValue();
                }
            }
            if (n_factu.getFact() == null) {
                n_factu.setFact("Factura Invalida");
            }
            jsonReader.endObject();
            jsonReader.close();
//            }
        } catch (Exception exc) {
            n_factu.setFact("Factura Invalida");
        }
    }

    public static void CalculaInteres(String metodo, RequestParams params) {
        //ArrayAnuladas = new ArrayList<Anulada>();
        reto_Interes = "0";
        cod_monedaInteres = "0";
        SyncHttpClient cliente = new SyncHttpClient();
        cliente.addHeader("Authorization", WebService.token);
        cliente.get(URL + metodo, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //banderaAnuladas = true;
                try {
                    String respuesta = new String(responseBody, "UTF-8");
                    objResponseA = new JSONArray(respuesta);
                    errToken = "";
                    JSONObject objetojSon;
                    if (objResponseA.length() > 0) {
                        for (int i = 0; i < objResponseA.length(); i++) {
                            objetojSon = objResponseA.getJSONObject(i);
                            if (objetojSon.getString("interes") != null) {
                                reto_Interes = objetojSon.getString("interes");
                            }
                            if (objetojSon.getString("cod_moneda") != null) {
                                cod_monedaInteres = objetojSon.getString("cod_moneda");
                            }
                        }
                    }
                } catch (Exception exc) {
                    try {
                        String respuesta = new String(responseBody, "UTF-8");
                        JSONObject objetojSon = new JSONObject(respuesta);
                        errToken = objetojSon.getString("error");
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    } catch (Exception ex) {

                    }
                    //banderaAnuladas = false;
                    exc.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                banderaAnuladas = false;
            }
        });

    }

    public static void ObtenerDatosEmpresa() {
        listEmpresas = new ArrayList<>();
        String metodo = "Cobranzas/TraerDatoEmpresas.php";
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRestSinParam(metodo);
        ClienteCobranza cliente = new ClienteCobranza();
        try {
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                cliente = new ClienteCobranza();
                jsonReader.beginObject(); //leo objeto del array
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("cod_emp")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            cliente.setCodEmp("NA");
                        } else {
                            cliente.setCodEmp(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_emp")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            cliente.setNomEmp("NA");
                        } else {
                            cliente.setNomEmp(jsonReader.nextString().trim());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                listEmpresas.add(cliente);
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();
        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }

    /*public static void BuscarDatosImpresora(RequestParams params){
        SyncHttpClient cliente = new SyncHttpClient();
        String URLDato = URL + "Impresion/printRecibo.php" + "?" + params;
        respuestaWSImpresora = "";
        cliente.get(URLDato, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String respuesta = new String(responseBody, "UTF-8");
                    objResponseA = new JSONArray(respuesta);
                    JSONObject objetojSon;
                    if (objResponseA.length() != 0) {
                        for (int i = 0; i < objResponseA.length(); i++) {
                            objetojSon = objResponseA.getJSONObject(i);
                            if (objetojSon.getString("valor").toString() != "") {
                                respuestaWSImpresora = objetojSon.getString("valor");
                            }
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("Error");
            }
        });
    }*/

    public static void BuscarDatosImpresora(String metodo, RequestParams params1) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params1, metodo);
        try {
            respuestaWSImpresora = "";
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                String key = jsonReader.nextName();
                if (key.equals("valor")) {
                    JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                    if (check == JsonToken.NULL) {
                        jsonReader.skipValue();
                        respuestaWSImpresora = "";
                    } else {
                        respuestaWSImpresora = jsonReader.nextString();
                    }
                } else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
            jsonReader.close();
//            }
        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }

    public static void TraerCredSus(String metodo, RequestParams params) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo); //obtengo datos
        try {
            ArrayCredSus = new ArrayList<>();
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                Credito_Suspenso credS = new Credito_Suspenso();
                jsonReader.beginObject();
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName();
                    if (key.equals("numero")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            credS.setNumero(0);

                        } else {
                            credS.setNumero(jsonReader.nextInt());
                        }
                    } else if (key.equals("cod_tit")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            credS.setCod_tit("");
                        } else {
                            credS.setCod_tit(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_tit")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            credS.setNom_tit("");
                        } else {
                            credS.setNom_tit(jsonReader.nextString());
                        }
                    } else if (key.equals("fecha")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            credS.setFecha("");
                        } else {
                            credS.setFecha(jsonReader.nextString());
                        }
                    } else if (key.equals("importe")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            credS.setImporte(0D);
                        } else {
                            credS.setImporte(jsonReader.nextDouble());
                        }
                    } else if (key.equals("tipo_cambio")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            credS.setTipo_cambio(0D);
                        } else {
                            credS.setTipo_cambio(jsonReader.nextDouble());
                        }
                    } else if (key.equals("cod_moneda")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            credS.setCod_Moneda("");
                        } else {
                            credS.setCod_Moneda(jsonReader.nextString());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                ArrayCredSus.add(credS);
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();

        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }

    public static void TraerDocumentos() {
        documentos = new ArrayList<TipoDoc>();
        String metodo = "Cobranzas/TraerDocumentos.php";
        SyncHttpClient cliente = new SyncHttpClient();
        cliente.addHeader("Authorization", WebService.token);
        cliente.get(URL + metodo, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                banderaValores = true;
                try {
                    String respuesta = new String(responseBody, "UTF-8");
                    objResponseA = new JSONArray(respuesta);
                    errToken = "";
                    TipoDoc instanciaDoc = new TipoDoc();
                    JSONObject objetojSon;
                    if (objResponseA.length() > 0) {
                        for (int i = 0; i < objResponseA.length(); i++) {
                            instanciaDoc = new TipoDoc();
                            objetojSon = objResponseA.getJSONObject(i);

                            if (objetojSon.getString("cod_docum") != null) {
                                instanciaDoc.setCod_doc_uni(objetojSon.getString("cod_docum"));
                            }
                            if (objetojSon.getString("nom_docum") != null) {
                                instanciaDoc.setNom_doc_uni(objetojSon.getString("nom_docum"));
                            }
                            documentos.add(instanciaDoc);
                        }
                    }
                } catch (Exception exc) {
                    try {
                        String respuesta = new String(responseBody, "UTF-8");
                        JSONObject objetojSon = new JSONObject(respuesta);
                        errToken = objetojSon.getString("error");
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    } catch (Exception ex) {

                    }
                    banderaValores = false;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                banderaValores = false;
            }
        });
    }


    ////////////////////////////FACTURA DIRECTA////////////////////////////////////////

    public static void TraerClientesVenta(String metodo) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRestSinParam(metodo); //obtengo datos
        try {
            ArrayClientes = new ArrayList<>();
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                ClienteCobranza cliente = new ClienteCobranza();
                jsonReader.beginObject();
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName();
                    if (key.equals("cod_tit_gestion")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            cliente.setCod_Tit_Gestion("");

                        } else {
                            cliente.setCod_Tit_Gestion(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_tit")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            cliente.setNom_Tit("");
                        } else {
                            cliente.setNom_Tit(jsonReader.nextString());
                        }
                    } else if (key.equals("cod_tit")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            cliente.setCod_Tit("");
                        } else {
                            cliente.setCod_Tit(jsonReader.nextString());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                ArrayClientes.add(cliente);
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();

        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }

    public static void TraerSucursales(String metodo, RequestParams params) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo); //obtengo datos
        try {
            ArraySucursales = new ArrayList<>();
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                Entrega sucursal = new Entrega();
                jsonReader.beginObject();
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName();
                    if (key.equals("cod_sucursal")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            sucursal.setCod_Sucursal("");

                        } else {
                            sucursal.setCod_Sucursal(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_tit")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            sucursal.setNom_Sucursal("");
                        } else {
                            sucursal.setNom_Sucursal(jsonReader.nextString());
                        }
                    } else if (key.equals("dir_tit")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            sucursal.setDireccion("");
                        } else {
                            sucursal.setDireccion(jsonReader.nextString());
                        }
                    } else if (key.equals("longitud_ubic")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            sucursal.setLongitud_Ubic(0D);
                        } else {
                            sucursal.setLongitud_Ubic(jsonReader.nextDouble());
                        }
                    } else if (key.equals("latitud_ubic")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            sucursal.setLatiud_Ubic(0D);
                        } else {
                            sucursal.setLatiud_Ubic(jsonReader.nextDouble());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                ArraySucursales.add(sucursal);
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();

        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }

    public static void TraerProductos(RequestParams params, String metodo) {
        ArrayItemsProductos = new ArrayList<Item>();
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo); //obtengo datos
        try {
            jsonReader.beginArray();
            errToken = "";
            boolean nroEncontrado = false;
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                Item instaItem = new Item();
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName();
                    if (key.equals("cod_art")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setCod_Articulo("");
                        } else {
                            instaItem.setCod_Articulo(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_articulo")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setNom_Articulo("");
                        } else {
                            instaItem.setNom_Articulo(jsonReader.nextString());
                        }
                    } else if (key.equals("porc_impuesto")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setPorc_Iva("");
                        } else {
                            instaItem.setPorc_Iva(jsonReader.nextString());
                        }
                    } else if (key.equals("cantidad")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setCantidadString("");
                        } else {
                            instaItem.setCantidadString(jsonReader.nextString());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                jsonReader.endObject();
                ArrayItemsProductos.add(instaItem);
            }
            jsonReader.endArray();

        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }

    public static void TraerProductosViaje(RequestParams params, String metodo) {
        ArrayItemsProductosViaje = new ArrayList<Item>();
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo); //obtengo datos
        try {
            jsonReader.beginArray();
            errToken = "";
            boolean nroEncontrado = false;
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                Item instaItem = new Item();
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName();
                    if (key.equals("cod_art")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setCod_Articulo("");
                        } else {
                            instaItem.setCod_Articulo(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_articulo")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setNom_Articulo("");
                        } else {
                            instaItem.setNom_Articulo(jsonReader.nextString());
                        }
                    } else if (key.equals("cantidad2")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setCantidad(0D);
                        } else {
                            instaItem.setCantidad(jsonReader.nextInt());
                        }
                    } else if (key.equals("cantVendidas")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setCantidad_vendida(0D);
                        } else {
                            instaItem.setCantidad_vendida(jsonReader.nextInt());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                jsonReader.endObject();
                ArrayItemsProductosViaje.add(instaItem);
            }
            jsonReader.endArray();

        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }

    public static void TraerPresentacion(String metodo, RequestParams params) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo); //obtengo datos
        try {
            listPresentacion = new ArrayList<>();
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                Presentacion presentacion = new Presentacion();
                jsonReader.beginObject();
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName();
                    if (key.equals("cod_unidad")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            presentacion.setCod_unidad("");

                        } else {
                            presentacion.setCod_unidad(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_unidad")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            presentacion.setNom_unidad("");
                        } else {
                            presentacion.setNom_unidad(jsonReader.nextString());
                        }
                    } else if (key.equals("precio")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            presentacion.setPrecio(0);
                        } else {
                            presentacion.setPrecio(jsonReader.nextDouble());
                        }
                    } else if (key.equals("cod_tit")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            presentacion.setCod_tit("");
                        } else {
                            presentacion.setCod_tit(jsonReader.nextString());
                        }
                    } else if (key.equals("descripcion")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            presentacion.setDescripcion("");
                        } else {
                            presentacion.setDescripcion(jsonReader.nextString());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                listPresentacion.add(presentacion);
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();

        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }

    public static void ValidarCantidad(String metodo, RequestParams params1) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params1, metodo);
        try {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String key = jsonReader.nextName();
                if (key.equals("Retorno")) {
                    errToken = jsonReader.nextString();
                    if (errToken.equals("Nok")) {
                        retorno_stock = "Nok";
                    } else if (errToken.equals("Ok")) {
                        retorno_stock = "Ok";
                        /*
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            retorno_stock = "Stock insuficiente";
                        } else {
                            retorno_stock = jsonReader.nextString();
                        }
                         */
                    }
                } else if (key.equals("stock")) {
                    JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                    if (check == JsonToken.NULL) {
                        jsonReader.skipValue();
                    } else {
                        //cantidad_stock = jsonReader.nextInt();
                        cantidadString = jsonReader.nextString();
                    }
                } else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
            jsonReader.close();
//            }
        } catch (Exception exc) {
            retorno_stock = "Nok";
        }
    }

    public static void TraerProductosFac(RequestParams params, String metodo) {
        ArrayItemsProductosFactura = new ArrayList<Item>();
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo); //obtengo datos
        try {
            jsonReader.beginArray();
            errToken = "";
            boolean nroEncontrado = false;
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                Item instaItem = new Item();
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName();
                    if (key.equals("cod_articulo")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setCod_Articulo("");
                        } else {
                            instaItem.setCod_Articulo(jsonReader.nextString());
                        }
                    } else if (key.equals("cantidad")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setCantidad_vendida(0);
                        } else {
                            instaItem.setCantidad_vendida(jsonReader.nextDouble());
                        }
                    } else if (key.equals("nom_articulo")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setNom_Articulo("");
                        } else {
                            instaItem.setNom_Articulo(jsonReader.nextString());
                        }
                    } else if (key.equals("precio_iva_inc")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setPrecio_Unitario(0D);
                        } else {
                            instaItem.setPrecio_Unitario(jsonReader.nextDouble());
                        }
                    } else if (key.equals("porc_impuesto")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setPorc_Iva("");
                        } else {
                            instaItem.setPorc_Iva(jsonReader.nextString());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                jsonReader.endObject();
                ArrayItemsProductosFactura.add(instaItem);
            }
            jsonReader.endArray();

        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }


    public static void guardar(RequestParams params, String metodo) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo); //obtengo datos
        try {
            if (jsonReader == null) {
                System.out.println("JSON READER NULL GUARDAR LOG");
            } else {
                jsonReader.nextString(); //leo objeto del array
                // while (jsonReader.hasNext()) {
                //String key = jsonReader;
                //if (key.equals("ok")) {
                String paso = "";
                //}
                //  }
            }

        } catch (Exception exxc) {
            exxc.toString();
        }
    }

    public static void TraerProductosPedidos(RequestParams params, String metodo) {
        ArrayItemsProductosPedido = new ArrayList<ProductosItem>();
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo); //obtengo datos
        try {
            jsonReader.beginArray();
            errToken = "";
            boolean nroEncontrado = false;
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                ProductosItem instaItem = new ProductosItem();
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName();
                    if (key.equals("cod_art")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setCod_articulo("");
                        } else {
                            instaItem.setCod_articulo(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_articulo")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setNom_articulo("");
                        } else {
                            instaItem.setNom_articulo(jsonReader.nextString());
                        }
                    } else if (key.equals("porc_impuesto")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setIva(0);
                        } else {
                            instaItem.setIva(jsonReader.nextDouble());
                        }
                    } else if (key.equals("cantidad")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setCantidad(0);
                        } else {
                            instaItem.setCantidad(jsonReader.nextDouble());
                        }
                    } else if (key.equals("adicional")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            instaItem.setAdicional("");
                        } else {
                            instaItem.setAdicional(jsonReader.nextString());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                jsonReader.endObject();
                ArrayItemsProductosPedido.add(instaItem);
            }
            jsonReader.endArray();

        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }

    public static void TraeDocumCliente(String metodo, RequestParams params) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo); //obtengo datos
        try {
            listTipoDoc = new ArrayList<>();
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                TipoDoc documento = new TipoDoc();
                jsonReader.beginObject();
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName();
                    if (key.equals("cod_doc_uni")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            documento.setCod_doc_uni("");

                        } else {
                            documento.setCod_doc_uni(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_doc_uni")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            documento.setNom_doc_uni("");

                        } else {
                            documento.setNom_doc_uni(jsonReader.nextString());
                        }
                    } else if (key.equals("nro_doc_uni")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            documento.setNro_doc_uni("");

                        } else {
                            documento.setNro_doc_uni(jsonReader.nextString());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                listTipoDoc.add(documento);
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();

        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }

    public static void TraeEmpresaUsu(RequestParams params) {
        listEmpPedidos = new ArrayList<>();
        String metodo = "Pedidos/TraeEmpresaUsu.php";
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo);
        ClienteCobranza cliente = new ClienteCobranza();
        try {
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                cliente = new ClienteCobranza();
                jsonReader.beginObject(); //leo objeto del array
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("cod_emp")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            cliente.setCodEmp("NA");
                        } else {
                            cliente.setCodEmp(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_emp")) {

                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null

                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            cliente.setNomEmp("NA");
                        } else {
                            cliente.setNomEmp(jsonReader.nextString().trim());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                listEmpPedidos.add(cliente);
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();
        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }

    public static void TraerSucDepEmp(String metodo, RequestParams params) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo); //obtengo datos
        try {
            ArraySucursalesEmp = new ArrayList<>();
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                Entrega sucursal = new Entrega();
                jsonReader.beginObject();
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName();
                    if (key.equals("cod_tit")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            sucursal.setCod_Tit("");

                        } else {
                            sucursal.setCod_Tit(jsonReader.nextString());
                        }
                    } else if (key.equals("nom_tit")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            sucursal.setNom_Tit("");
                        } else {
                            sucursal.setNom_Tit(jsonReader.nextString());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                ArraySucursalesEmp.add(sucursal);
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();

        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }

    public static void TraerZonasEntrega(String metodo, RequestParams params) {
        JsonReader jsonReader = WebServiceRes.jsRespuestaWSRest(params, metodo); //obtengo datos
        try {
            ArrayEntrega = new ArrayList<>();
            jsonReader.beginArray();
            errToken = "";
            while (jsonReader.hasNext()) {
                Entrega entrega = new Entrega();
                jsonReader.beginObject();
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName();
                    if (key.equals("cod_zona_entrega")) {
                        JsonToken check = jsonReader.peek();//Con esto valido que el token valor no sea null
                        if (check == JsonToken.NULL) {
                            jsonReader.skipValue();
                            entrega.setCod_Zona_Entrega("");

                        } else {
                            entrega.setCod_Zona_Entrega(jsonReader.nextString());
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                }
                ArrayEntrega.add(entrega);
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();

        } catch (Exception exc) {
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("error")) {
                        errToken = jsonReader.nextString();
                        if (!errToken.toUpperCase().contains("TOKEN")) {
                            errToken = "";
                        }
                    }
                }
                exc.toString();
            } catch (Exception exct) {

            }
        }
    }

    /*public void postData(String url,JSONObject obj) {
        // Create a new HttpClient and Post Header

        HttpParams myParams = (HttpParams) new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(myParams, 10000);
        HttpConnectionParams.setSoTimeout(myParams, 10000);
        HttpClient httpclient = new DefaultHttpClient((ClientConnectionManager) myParams);
        String json=obj.toString();

        try {

            HttpPost httppost = new HttpPost(url.toString());
            httppost.setHeader("Content-type", "application/json");

            StringEntity se = new StringEntity(obj.toString());
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setEntity(se);

            HttpResponse response = httpclient.execute(httppost);
            String temp = EntityUtils.toString(response.getEntity());


        } catch (ClientProtocolException e) {

        } catch (IOException e) {
        }
    }*/

    public static void sendPedido(String metodo, JSONObject param) {
        String result = "";
        BufferedReader in = null;
        HttpURLConnection connection = null;
        try {
           /* byte[] postData       = param.get( StandardCharsets.UTF_8 );
            int    postDataLength = postData.length;*/

            String sUrl = URL + metodo + "?" + param;
            java.net.URL getUrl = new URL(sUrl);
            //String sUrl = URL + metodo + "?" + param;
            //URL getUrl = new URL(URL + metodo );
            // Abre la conexión con la URL
            connection = (HttpURLConnection) getUrl.openConnection();

            // Antes de conectar, establezca atributos de solicitud generales
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Charsert", "UTF-8");

            // Configure el tipo de contenido de esta conexión, el formulario del formulario es "application / x-www-form-urlencoded", json es "application / json", etc.
            // Ajuste el tipo de contenido según sus necesidades
            connection.setRequestProperty("Accept", "application/json");

            //connection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));

            // Establecer el tiempo de espera para conectarse al servidor host: 15000 milisegundos
            connection.setConnectTimeout(15000);
            // Establecer el tiempo para leer los datos devueltos por el control remoto: 60000 milisegundos
            connection.setReadTimeout(60000);
            // Establecer el método de conexión: get
            connection.setRequestMethod("GET");

            // Establezca la conexión real, tenga en cuenta que connection.getOutputStream se conectará implícitamente.
            //connection.connect();

            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(param.toString());
            wr.flush();

            StringBuilder sb = new StringBuilder();
            int HttpResult = connection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                System.out.println("" + sb.toString());
            } else {
                System.out.println(connection.getResponseMessage());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void IngresarPedido(String metodo, RequestParams parametrosIng) {

        System.out.println(URL + metodo);
        try {
            String sUrl = URL + metodo + "?" + parametrosIng;
            java.net.URL urlServer = new URL(sUrl);
            HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
            urlConn.setRequestProperty("Authorization", WebService.token);
            urlConn.setRequestProperty("Content-type", "application/json");
            urlConn.setConnectTimeout(3000); //<- 3Seconds Timeout
            urlConn.connect();
            if (urlConn.getResponseCode() == 200) {
                System.out.println(urlConn.getResponseMessage());
                JsonReader jsonReader;
                InputStream responseBody = urlConn.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                jsonReader = new JsonReader(responseBodyReader);
                jsonReader.beginObject(); // Start processing the JSON object
                while (jsonReader.hasNext()) { // Loop through all keys
                    String key = jsonReader.nextName(); // Fetch the next key
                    if (key.equals("retorno")) {
                        reto_AgregaPedido = jsonReader.nextString();
                        if (reto_AgregaPedido.toUpperCase().equals("OK")) {
                            errToken = "";
                        } else {
                            errToken = reto_AgregaPedido;
                            if (!errToken.toUpperCase().contains("TOKEN")) {
                                errToken = "";
                            }
                        }
                        System.out.println(reto_AgregaPedido);
                    }
                    if (key.equals("nro_trans")) {
                        nro_trans = jsonReader.nextInt();
                        WebService.nro_trans_impresion = nro_trans.toString();
                        reto_AgregaPedido = "ok";
                        System.out.println(nro_trans_impresion);
                    }
                }
            } else {
                reto_AgregaPedido = "Error al recibir los datos";
                nro_trans = 0;
            }
        } catch (Exception exc) {
            System.out.println(exc.toString());
        }
    }
}
