package com.example.user.trucksales.Visual.TruckSales;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.ClienteCobranza;
import com.example.user.trucksales.Encapsuladoras.Entrega;
import com.example.user.trucksales.Encapsuladoras.FacturaItemZebra;
import com.example.user.trucksales.Encapsuladoras.FacturaZebra;
import com.example.user.trucksales.Encapsuladoras.Item;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Cobranza.SeleccionCliente;
import com.example.user.trucksales.Visual.Cobranza.SeleccionarDeudas;
import com.example.user.trucksales.Visual.FacturaDirecta.BuscarFactura;
import com.example.user.trucksales.Visual.FacturaDirecta.FacturaDirecta;
import com.example.user.trucksales.Visual.FacturaDirecta.NotaCredito;
import com.example.user.trucksales.Visual.Login;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.loopj.android.http.RequestParams;
import com.szsicod.print.io.BluetoothAPI;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.BluetoothConnectionInsecure;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.comm.TcpConnection;
import com.zebra.sdk.common.card.containers.GraphicsInfo;
import com.zebra.sdk.common.card.containers.JobStatusInfo;
import com.zebra.sdk.common.card.enumerations.CardSide;
import com.zebra.sdk.common.card.enumerations.GraphicType;
import com.zebra.sdk.common.card.enumerations.OrientationType;
import com.zebra.sdk.common.card.enumerations.PrintType;
import com.zebra.sdk.common.card.exceptions.ZebraCardException;
import com.zebra.sdk.common.card.graphics.ZebraCardGraphics;
import com.zebra.sdk.common.card.graphics.ZebraCardImage;
import com.zebra.sdk.common.card.graphics.ZebraCardImageI;
import com.zebra.sdk.common.card.graphics.ZebraGraphics;
import com.zebra.sdk.common.card.graphics.barcode.BarcodeUtil;
import com.zebra.sdk.common.card.graphics.barcode.Code39Util;
import com.zebra.sdk.common.card.graphics.barcode.CodePDF417Util;
import com.zebra.sdk.common.card.graphics.barcode.CodeQRUtil;
import com.zebra.sdk.common.card.graphics.barcode.ZebraBarcodeFactory;
import com.zebra.sdk.common.card.graphics.barcode.enumerations.HumanReadablePlacement;
import com.zebra.sdk.common.card.graphics.barcode.enumerations.Rotation;
import com.zebra.sdk.common.card.jobSettings.ZebraCardJobSettingNames;
import com.zebra.sdk.common.card.printer.ZebraCardPrinter;
import com.zebra.sdk.common.card.printer.ZebraCardPrinterFactory;
import com.zebra.sdk.device.ZebraIllegalArgumentException;
import com.zebra.sdk.graphics.internal.ZebraImageAndroid;
import com.zebra.sdk.printer.PrinterStatus;
import com.zebra.sdk.printer.SGD;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.sdk.printer.ZebraPrinterLinkOs;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.example.user.trucksales.Datos.WebService.entregasTraidas;
import static com.example.user.trucksales.Datos.WebService.nro_trans;

public class factura extends Activity implements AdapterView.OnItemSelectedListener{

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public TextView orden,destino,pedido,viaje,tota,totaPedi,descuento,iva5,iva10,TotSinIVA,TotalIVA,retorno;
    EditText cod_Suc_Tribut,cod_Fac_Tribut,num_Factu;
    ImageView atras;
    TableLayout tablaFactura;
    TableLayout tablaFactura2;
    Button btnValidarFac;
    protected Context context;
    protected static RequestParams ParametrosCajas = new RequestParams(  );
    protected static RequestParams params = new RequestParams(  );
    protected static RequestParams paramsIng = new RequestParams(  );
    private boolean facturaValida;
    double neto;
    private Productos IProductos = new Productos();
    private ArrayList<TextView> ListaTextViews;
    private String cod_Suc;
    private Utilidades Utilidad;
    private static ArrayList<Entrega> listaEntregas;
    private static ArrayList<Entrega> listaEntregasHechas  = new ArrayList<>( );
    protected static RequestParams params1 = new RequestParams(  );

    public static String valorIntent;

    Spinner spFPago;
    final List<String> spinnerFPago = new ArrayList<>(  );
    public static String f_pago = "";

    private Spinner spinnerBTDeviceList;
    private ArrayList<String> mBTAddrList = new ArrayList<String>();

    private Connection printerConnection;
    private EditText edit;
    private String Nro_tran;

    private ImageView imageView;


    public List<String> editTextArray =  new ArrayList<String>();
    public List<String> Codigos = new ArrayList<>(  );

    TextView txtCliente,txtSucursal,txtFormaPago;
    Spinner spinner_fac_directo;
    private ArrayList<String> fac_directo_List = new ArrayList<String>();
    AutoCompleteTextView nombreCliente,codCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_factura);
        WebService.reto_AgregaFactura = "";
        context = this;

        Utilidad = new Utilidades(context);
        GuardarDatosUsuario.Contexto = context;
        facturaValida = false;
        WebService.EstadoActual = 1;

        try{
        ListaTextViews = new ArrayList<>();
        if (WebService.USUARIOLOGEADO != null) {
        try{

            try {
                valorIntent = getIntent().getStringExtra("intent");
            }catch (Exception ex){
                ex.printStackTrace();
               // Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
            }

            if(WebService.Entrega_A_Realizar.getDescuento() > WebService.Gravada5){
                try{
                    Toast toast = Toast.makeText(context, getResources().getString(R.string.ErrorDescuento), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                    toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                    toast.show();//showing the toast is important***/

                    // Toast.makeText( getApplicationContext(), getResources().getString(R.string.ErrorDescuento), Toast.LENGTH_LONG ).show();
                    if(valorIntent.equals("VentaDirecta")){
                        WebService.listaProductos.clear();
                        Intent myIntent = new Intent(context, FacturaDirecta.class);
                        startActivity(myIntent);
                    }else if(valorIntent.equals("NotaCredito")){
                        WebService.listaProductos.clear();
                        Intent myIntent = new Intent(context, NotaCredito.class);
                        startActivity(myIntent);
                    }else {
                        Intent myIntent = new Intent(context, Productos.class);
                        startActivity(myIntent);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            imageView= findViewById(R.id.qr);

            cod_Suc_Tribut = findViewById(R.id.EdtCodSucTribut);
            cod_Suc_Tribut.setInputType(InputType.TYPE_CLASS_NUMBER);
            cod_Suc_Tribut.setText(WebService.cod_suc_tribut);

            cod_Fac_Tribut = findViewById(R.id.EdtCodFacTribut);
            cod_Fac_Tribut.setInputType(InputType.TYPE_CLASS_NUMBER);
            cod_Fac_Tribut.setText(WebService.cod_fac_trubut);

            num_Factu = findViewById(R.id.EdtNumFactu);
            num_Factu.setInputType(InputType.TYPE_CLASS_NUMBER);
            num_Factu.setText(WebService.nro_sugerido);

            btnValidarFac = findViewById(R.id.validarFac);

            txtCliente = findViewById(R.id.txtCliente);
            txtCliente.setText(WebService.Entrega_A_Realizar.getNom_Tit());
            txtSucursal = findViewById(R.id.txtSucursal);
            txtSucursal.setText(WebService.Entrega_A_Realizar.getNom_Sucursal());
            spinner_fac_directo = findViewById(R.id.spinner_fac_directo);
            fac_directo_List.clear();
            fac_directo_List.add("No");
            fac_directo_List.add("Si");

            spFPago = findViewById(R.id.spiPago);

            spFPago.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
            final ArrayAdapter<String> dataAdapterFPago = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerFPago);

                spFPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            f_pago = spFPago.getSelectedItem().toString();
                            WebService.Entrega_A_Realizar.setCodigoTipoPago(f_pago);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            /*txtFormaPago = findViewById(R.id.txtFormaPago);
            txtFormaPago.setText(WebService.Entrega_A_Realizar.getCodigoTipoPago());*/

            ArrayAdapter<String> adapterTempDirecto = null;
            adapterTempDirecto = new ArrayAdapter<String>(this,	android.R.layout.simple_spinner_item,fac_directo_List);
            adapterTempDirecto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_fac_directo.setAdapter(adapterTempDirecto);

            codCliente = findViewById(R.id.TxtCodCliente);
            //codCliente.setEnabled(false); //agregado para que si quiere cambiar debe seleccionar el combo Si RP 15/12/2021//no funciona porque despues muestra de nuevo
            //codCliente.setText(WebService.Entrega_A_Realizar.getCod_Tit());
            nombreCliente = findViewById(R.id.TxtNombreCliente);

            spinner_fac_directo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String seleccion = spinner_fac_directo.getSelectedItem().toString();
                    if(seleccion.equals("No")){
                        codCliente.setEnabled(false);
                        codCliente.setText(WebService.Entrega_A_Realizar.getCod_Tit_Gestion()); //cambiado de codtit a codtitgestion RP 21/12/2021
                        nombreCliente.setEnabled(false);
                        nombreCliente.setText(WebService.Entrega_A_Realizar.getNom_Tit());
                    }else if(seleccion.equals("Si")){
                        codCliente.setEnabled(true);
                        codCliente.setText("");
                        //nombreCliente.setEnabled(true); //modificado RP para no permitir ingresar nombre
                        nombreCliente.setText("");
                        //CargarSpinner();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, editTextArray);
            final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Codigos);

            if (WebService.deudores.size() == 0) {
                TraerClientes task01 = new TraerClientes(new SeleccionCliente.AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        if(WebService.errToken.equals("")){
                            CargarSpinner();
                            nombreCliente.setAdapter(adapter);
                            codCliente.setAdapter(adapter2);
                        }else{
                            Intent myIntent = new Intent(context, Login.class);
                            startActivity(myIntent);
                        }
                    }
                });
                task01.execute();
            } else {
                CargarSpinner();
                nombreCliente.setAdapter(adapter);
                codCliente.setAdapter(adapter2);
            }


            nombreCliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object item = parent.getItemAtPosition(position);
                    ClienteCobranza instaCliente = new ClienteCobranza();

                    boolean encontrado = false;
                    for (int i = 0; i < WebService.deudores.size() && !encontrado; i++) {
                        instaCliente = WebService.deudores.get(i);
                        if (instaCliente.getNom_Tit().trim().equals(item)) {
                            encontrado = true;
                            codCliente.setText(instaCliente.getCod_Tit_Gestion().trim());
                            cerrarTeclado();
                            /*View view2 = getCurrentFocus();
                            view2.clearFocus();
                            if (view2 != null) {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                            }*/
                        }
                    }
                }
            });
            nombreCliente.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        //  sendMessage();
                        handled = true;
                    }
                    return handled;
                }
            });

            codCliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object item = parent.getItemAtPosition(position);
                    ClienteCobranza instaCliente = new ClienteCobranza();
                    boolean encontrado = false;
                    for (int i = 0; i < WebService.deudores.size() && !encontrado; i++) {
                        instaCliente = WebService.deudores.get(i);
                        if (instaCliente.getCod_Tit_Gestion().trim().equals(item)) {
                            encontrado = true;
                            nombreCliente.setText(instaCliente.getNom_Tit().trim());

                            WebService.clienteActual = instaCliente;

                            cerrarTeclado();
                            /*View view2 = getCurrentFocus();
                            view2.clearFocus();
                            if (view2 != null) {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                            }*/

                        }
                    }
                }
            });
            codCliente.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        //  sendMessage();
                        handled = true;
                    }
                    return handled;
                }
            });





            if(WebService.configuracion.getPropone_nrofac().equals("S")){
                cod_Suc_Tribut.setVisibility(View.VISIBLE);
                cod_Fac_Tribut.setVisibility(View.VISIBLE);
                num_Factu.setVisibility(View.VISIBLE);
                btnValidarFac.setText("Validar factura");
            }else{
                cod_Suc_Tribut.setVisibility(View.GONE);
                cod_Fac_Tribut.setVisibility(View.GONE);
                num_Factu.setVisibility(View.GONE);
                btnValidarFac.setText("Generar factura");
            }
            num_Factu.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    noValido();
                }
            });
            cod_Suc_Tribut.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    noValido();
                }
            });
            cod_Fac_Tribut.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    noValido();
                }
            });

            retorno = findViewById(R.id.solFac);

            btnValidarFac.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //imprimirQR();
                   // printQRGonzalo(null);
                    String seleccion = spinner_fac_directo.getSelectedItem().toString();
                    if(seleccion.equals("Si") && codCliente.getText().toString().equals("")){
                        Toast.makeText( getApplicationContext(), "Debe seleccionar el cliente a facturar", Toast.LENGTH_LONG ).show();
                    }else{
                        //agregado para que cuando no exista el cliente y el nombre queda en blanco no lo deje continuar RP 15/12/2021
                        if (nombreCliente.getText().toString().equals("")){
                            Toast.makeText( getApplicationContext(), "Nombre cliente no puede quedar sin valor", Toast.LENGTH_LONG ).show();
                        }else {     //fin de agregado RP 15/12/2021
                            if (WebService.Entrega_A_Realizar.getCodigoTipoPago().equals("CO")) {
                                cobrarContado(v);
                            } else {
                                cobrarCredito();
                            }
                        }
                    }


                }
            });


            edit = (EditText) findViewById(R.id.edit);

            /*edit.setVisibility(View.GONE);*/
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

            tablaFactura = findViewById(R.id.TablaFactura);
            tablaFactura2 = findViewById(R.id.TablaFactura2);

            destino = findViewById(R.id.TextDestino);
            //destino.setText(WebService.Entrega_A_Realizar.getNom_Tit() + " - " + WebService.Entrega_A_Realizar.getNom_Sucursal() );
            destino.setText(WebService.Entrega_A_Realizar.getNom_Sucursal());

            orden = findViewById(R.id.TxtOrden);
            orden.setText(getResources().getString(R.string.OredenProd) + WebService.Entrega_A_Realizar.getNro_Docum());

            pedido = findViewById(R.id.TxtPedido);
            pedido.setText(getResources().getString(R.string.PedidoProd) + WebService.Entrega_A_Realizar.getNro_doc_ref());

            viaje = findViewById(R.id.TxtViaje);
            viaje.setText(getResources().getString(R.string.NumViajeProd) + WebService.viajeSeleccionado.getNumViaje());

            atras = findViewById(R.id.btnAtras);
            atras.setClickable(true);
            atras.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try{
                    Utilidad.vibraticionBotones(context);
                    if(valorIntent.equals("VentaDirecta")){
                        WebService.listaProductos.clear();
                        Intent myIntent = new Intent(v.getContext(), FacturaDirecta.class);
                        startActivity(myIntent);
                    }else if(valorIntent.equals("VentaDirectaProductoCono") || valorIntent.equals("RecorridoViaje")){
                        //WebService.listaProductos.clear();
                        Intent myIntent = new Intent(v.getContext(), FacturaDirectaProductoCono.class);
                        myIntent.putExtra("intent2", valorIntent);
                        startActivity(myIntent);
                    }else if(valorIntent.equals("NotaCredito")){
                        WebService.listaProductos.clear();
                        Intent myIntent = new Intent(v.getContext(), NotaCredito.class);
                        startActivity(myIntent);
                    }else {
                        Intent myIntent = new Intent(v.getContext(), Productos.class);
                        startActivity(myIntent);
                    }
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            });
            TableLayout tr0 = new TableLayout(this);
            TableRow tr1 = new TableRow(this);
            TableRow tr2 = new TableRow(this);

            for (int i = 0; WebService.ArrayItemsViaje.size() > i; i++) {

                tr0 = new TableLayout(this);
                tr1 = new TableRow(this);
                tr2 = new TableRow(this);
                TextView nombreItem = new TextView(this);
                String nombItem = WebService.ArrayItemsViaje.get(i).getNom_Articulo().trim();
                String codArt = WebService.ArrayItemsViaje.get(i).getCod_Articulo().trim();

                nombreItem.setText(nombItem + " - " + codArt);
                nombreItem.setTextSize(15);
                nombreItem.setTextColor(Color.parseColor("#000000"));
                nombreItem.setPadding(10, 5, 20, 5);
                tr0.addView(nombreItem);

                //Aca cargamos los titulos
                TextView titCant = new TextView(this);
                titCant.setText(getResources().getString(R.string.CantidadPrdo));
                titCant.setTextColor(Color.parseColor("#ffffff"));
                titCant.setPadding(5, 10, 30, 10);

                TextView titCantEntreg = new TextView(this);
                titCantEntreg.setText(getResources().getString(R.string.CantEntProd));
                titCantEntreg.setTextColor(Color.parseColor("#ffffff"));
                titCantEntreg.setGravity(Gravity.RIGHT);
                titCantEntreg.setPadding(10, 10, 40, 10);

                TextView titDtoLinea = new TextView(this);
                titDtoLinea.setText(getResources().getString(R.string.Dto));
                titDtoLinea.setTextColor(Color.parseColor("#ffffff"));
                //titPrecioUnitario.setPadding(30, 10, 5, 10);
                titDtoLinea.setPadding(30, 10, 22, 10);
                titDtoLinea.setGravity(Gravity.RIGHT);

                TextView titPrecioUnitario = new TextView(this);
                titPrecioUnitario.setText(getResources().getString(R.string.UnitProd));
                titPrecioUnitario.setTextColor(Color.parseColor("#ffffff"));
                titPrecioUnitario.setPadding(30, 10, 40, 10);
                titPrecioUnitario.setGravity(Gravity.RIGHT);

                TextView titIva = new TextView(this);
                titIva.setText(getResources().getString(R.string.IVAProd));
                titIva.setTextColor(Color.parseColor("#ffffff"));
                titIva.setPadding(30, 10, 30, 10);
                titIva.setGravity(Gravity.RIGHT);

                TextView titSubTotal = new TextView(this);
                titSubTotal.setText(getResources().getString(R.string.SubTotProd));
                titSubTotal.setTextColor(Color.parseColor("#ffffff"));
                titSubTotal.setGravity(Gravity.RIGHT);
                titSubTotal.setPadding(30, 10, 5, 10);

                tr1.addView(titCant);
                tr1.addView(titCantEntreg);
                if(WebService.configuracion.getDto_linea().equals("S")){
                    tr1.addView(titDtoLinea);
                }
                tr1.addView(titPrecioUnitario);
                if(WebService.configuracion.getIva().equals("S")){
                    tr1.addView(titIva);
                }
                tr1.addView(titSubTotal);
                tr1.setBackgroundResource(R.color.colorPrimary);
                //Aca Terminamos de cargar los titulos

                //Aca cargamos los productos
                final TextView cantidadPedida = new TextView(this);
                ListaTextViews.add(cantidadPedida);
                cantidadPedida.setText(Utilidad.redondearDecimalesNew(WebService.ArrayItemsViaje.get(i).getCantidad(), Integer.valueOf(WebService.configuracion.getDec_cant())));
                cantidadPedida.setGravity(Gravity.RIGHT);

                final TextView cantidadEntregada = new TextView(this);
                ListaTextViews.add(cantidadEntregada);
                cantidadEntregada.setText(Utilidad.redondearDecimalesNew(WebService.ArrayItemsViaje.get(i).getCantidad_entregada(), Integer.valueOf(WebService.configuracion.getDec_cant())));
                cantidadEntregada.setInputType(InputType.TYPE_CLASS_NUMBER);
                cantidadEntregada.setGravity(Gravity.CENTER);

                final TextView dtoLinea = new TextView(this);
                ListaTextViews.add(dtoLinea);
                dtoLinea.setText(Double.toString(WebService.ArrayItemsViaje.get(i).getDtoLinea()));
                dtoLinea.setInputType(InputType.TYPE_CLASS_NUMBER);
                dtoLinea.setGravity(Gravity.CENTER);

                final TextView PrecioUnitario = new TextView(this);
                PrecioUnitario.setText(Utilidad.redondearDecimalesNew(WebService.ArrayItemsViaje.get(i).getPrecio_Unitario(), Integer.valueOf(WebService.configuracion.getDec_montomn())));
                cantidadPedida.setGravity(Gravity.RIGHT);
                ListaTextViews.add(PrecioUnitario);

                final TextView subtotal = new TextView(this);
                subtotal.setGravity(Gravity.RIGHT);
                ListaTextViews.add(subtotal);

                TextView iva = new TextView(this);
                ListaTextViews.add(iva);
                double valorIva = Double.valueOf(WebService.ArrayItemsViaje.get(i).getPorc_Iva());
                int iv = (int) valorIva;

                if(WebService.configuracion.getIva().equals("S")){
                    iva.setText(String.valueOf(iv));
                }else{
                    iva.setText("");
                }

                iva.setGravity(Gravity.CENTER);
                //iva.setPadding(5, 0, 5, 0);

                double sub_tot = Math.floor(WebService.ArrayItemsViaje.get(i).getCantidad_entregada() * (WebService.ArrayItemsViaje.get(i).getPrecio_Unitario()-(WebService.ArrayItemsViaje.get(i).getDtoLinea()*WebService.ArrayItemsViaje.get(i).getPrecio_Unitario()/100)));

                subtotal.setText(Utilidad.GenerarFormato((int) sub_tot));
                subtotal.setGravity(Gravity.RIGHT);
                //Aca cargamos el IVA para mostrar

                tr2.addView(cantidadPedida);
                tr2.addView(cantidadEntregada);
                if(WebService.configuracion.getDto_linea().equals("S")){
                    tr2.addView(dtoLinea);
                }
                tr2.addView(PrecioUnitario);
                if(WebService.configuracion.getIva().equals("S")){
                    tr2.addView(iva);
                }
                tr2.addView(subtotal);
                tr2.setTag(i);
                tablaFactura.addView(tr0);
                tablaFactura.addView(tr1);
                tablaFactura.addView(tr2);
                tablaFactura.setPadding(5,0,5,0);

                if (WebService.ArrayItemsViaje.size() == i + 1) {
                    TableRow tr01 = new TableRow(this);
                    TableRow tr02 = new TableRow(this);
                   /* tr1 = new TableRow(this);
                    tr2 = new TableRow(this);*/

                    TableRow tr3 = new TableRow(this);
                    TableRow tr4 = new TableRow(this);
                    TableRow tr5 = new TableRow(this);
                    TableRow tr6 = new TableRow(this);
                    TableRow tr7 = new TableRow(this);

                    /*int ind = 1;
                    while (ind <= 4) {
                        TextView relleno = new TextView(this);
                        relleno.setText("");
                        relleno.setPadding(5, 10, 10, 10);
                        tr01.addView(relleno);
                        ind++;
                    }
                    if (ind == 5) {*/
                    tota = new TextView(this);
                    ListaTextViews.add(tota);
                    tota.setTextColor(Color.parseColor("#ffffff"));
                    tota.setText(getResources().getString(R.string.TotalCaja) + Utilidad.GenerarFormato((int) WebService.TotalFactura));
                    tr01.addView(tota);
                    tr01.setGravity(Gravity.RIGHT);
                    tr01.setPadding(0, 0, 5, 0);
                    tr01.setBackgroundResource(R.color.colorPrimary);
                    tablaFactura2.addView(tr01);
                       /* ind = 1;
                        while (ind <= 4) {
                            TextView relleno = new TextView(this);
                            relleno.setText("");
                            relleno.setPadding(5, 10, 10, 10);
                            tr1.addView(relleno);
                            ind++;
                        }
                        if (ind == 5) {*/
                    totaPedi = new TextView(this);
                    ListaTextViews.add(totaPedi);
                    totaPedi.setTextColor(Color.parseColor("#ffffff"));
                    totaPedi.setText(getResources().getString(R.string.TotPedInicial) + Utilidad.GenerarFormato2(WebService.TotalPedido));
                    tr02.addView(totaPedi);
                    tr02.setGravity(Gravity.RIGHT);
                    tr02.setPadding(0, 0, 5, 0);
                    tr02.setBackgroundResource(R.color.colorPrimary);
                    tablaFactura2.addView(tr02);

                    descuento = new TextView(this);
                    ListaTextViews.add(descuento);
                    descuento.setTextColor(Color.parseColor("#ffffff"));
                    descuento.setText(getResources().getString(R.string.Descuento) + Utilidad.GenerarFormato2(WebService.Entrega_A_Realizar.getDescuento()));
                    tr7.addView(descuento);
                    tr7.setGravity(Gravity.RIGHT);
                    tr7.setPadding(0, 0, 5, 0);
                    tr7.setBackgroundResource(R.color.Iva);
                    tablaFactura2.addView(tr7);

                        /*}
                        ind = 1;
                        while (ind <= 4) {
                            TextView relleno = new TextView(this);
                            relleno.setText("");
                            relleno.setTextColor(Color.parseColor("#ffffff"));
                            relleno.setPadding(5, 10, 10, 10);
                            tr2.addView(relleno);
                            ind++;
                        }
                        if (ind == 5) {*/

                    iva5 = new TextView(this);
                    ListaTextViews.add(iva5);
                    iva5.setTextColor(Color.parseColor("#ffffff"));
                    iva5.setText(getResources().getString(R.string.ImpuestoIVA5) + Utilidad.GenerarFormato((int) WebService.IVA5));
                    tr6.addView(iva5);
                    tr6.setGravity(Gravity.RIGHT);
                    tr6.setPadding(0, 0, 5, 0);
                    tr6.setBackgroundResource(R.color.Iva);
                    if(WebService.configuracion.getIva().equals("S")){
                        tablaFactura2.addView(tr6);
                    }
                      /*  }
                        ind = 1;
                        while (ind <= 4) {
                            TextView relleno = new TextView(this);
                            relleno.setText("");
                            relleno.setTextColor(Color.parseColor("#ffffff"));
                            relleno.setPadding(5, 10, 10, 10);
                            tr3.addView(relleno);
                            ind++;
                        }
                        if (ind == 5) {*/
                    iva10 = new TextView(this);
                    ListaTextViews.add(iva10);
                    iva10.setTextColor(Color.parseColor("#ffffff"));
                    iva10.setText(getResources().getString(R.string.ImpusetoIVA10) + Utilidad.GenerarFormato((int) WebService.IVA10));
                    tr3.addView(iva10);
                    tr3.setGravity(Gravity.RIGHT);
                    tr3.setPadding(0, 0, 5, 0);
                    tr3.setBackgroundResource(R.color.Iva);
                    if(WebService.configuracion.getIva().equals("S")){
                        tablaFactura2.addView(tr3);
                    }
                        /*}
                        ind = 1;
                        while (ind <= 4) {
                            TextView relleno = new TextView(this);
                            relleno.setText("");
                            relleno.setTextColor(Color.parseColor("#ffffff"));
                            relleno.setPadding(5, 10, 10, 10);
                            tr4.addView(relleno);
                            ind++;
                        }
                        if (ind == 5) {*/
                    TotalIVA = new TextView(this);
                    ListaTextViews.add(TotalIVA);
                    TotalIVA.setTextColor(Color.parseColor("#ffffff"));
                    int valorIVA5 = (int) WebService.IVA5;
                    int valorIVA10 = (int) WebService.IVA10;
                    TotalIVA.setText(getResources().getString(R.string.TotIVA) + Utilidad.GenerarFormato((valorIVA5 + valorIVA10)));
                    tr4.addView(TotalIVA);
                    tr4.setGravity(Gravity.RIGHT);
                    tr4.setPadding(0, 0, 5, 0);
                    tr4.setBackgroundResource(R.color.Iva);
                    if(WebService.configuracion.getIva().equals("S")){
                        tablaFactura2.addView(tr4);
                    }

                       /* }
                        ind = 1;
                        while (ind <= 4) {
                            TextView relleno = new TextView(this);
                            relleno.setText("");
                            relleno.setTextColor(Color.parseColor("#ffffff"));
                            relleno.setPadding(5, 10, 10, 10);
                            tr5.addView(relleno);
                            ind++;
                        }
                        if (ind == 5) {*/
                    TotSinIVA = new TextView(this);
                    ListaTextViews.add(TotSinIVA);
                    TotSinIVA.setTextColor(Color.parseColor("#ffffff"));
                    TotSinIVA.setText(getResources().getString(R.string.TotSinIVAProd) + Utilidad.GenerarFormato(((int) WebService.TotalFactura - ((int) WebService.IVA5 + (int) WebService.IVA10))));
                    tr5.addView(TotSinIVA);
                    tr5.setGravity(Gravity.RIGHT);
                    tr5.setPadding(0, 0, 5, 0);
                    tr5.setBackgroundResource(R.color.colorPrimary);
                    if(WebService.configuracion.getIva().equals("S")){
                        tablaFactura2.addView(tr5);
                    }

                    tablaFactura2.setPadding(5, 0, 5, 0);
                    //}
                    // }
                }
            }
        }catch(Exception ex){
           ex.printStackTrace();
            //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }
        else{
            Intent myIntent = new Intent( context, Login.class );
            //PONER ESTE STRING
            //myIntent.putExtra("Mensaje","Factura");
            startActivity( myIntent );
        }
        Utilidad.SetFontSizeTextView( ListaTextViews , this);

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void cerrarTeclado() {  //agregado para ocultar el teclado RP 16/12/2021
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private class TraerClientes extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public SeleccionCliente.AsyncResponse delegate = null;//Call back interface
        public TraerClientes(SeleccionCliente.AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            if(Utilidad.isNetworkAvailable()) {
                RequestParams params1 = new RequestParams(  );
                /*params1.put("username", WebService.usuarioActual.getNombre().trim());
                params1.put("cod_empresa",WebService.usuarioActual.getEmpresa().trim());*/
                WebService.TraerClientes("Cobranzas/TraerClientes.php",params1);
                return null;

            } else {}
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
    private void CargarSpinner(){
        for (int i = 0; i < WebService.deudores.size(); i++) {
            String nombreAgregar = WebService.deudores.get(i).getNom_Tit().trim();
            editTextArray.add(nombreAgregar);
            String codigoAgregar = WebService.deudores.get(i).getCod_Tit_Gestion().trim();
            Codigos.add(codigoAgregar);
        }
    }


    private void cobrarContado(final View v) {
        if(cod_Suc_Tribut.getText()!=null){
            cod_Suc = cod_Suc_Tribut.getText().toString().trim();
        }else{
            cod_Suc ="";
        }

        String cod_Fac ="";
        if(cod_Fac_Tribut.getText()!=null){
            cod_Fac =cod_Fac_Tribut.getText().toString().trim();
        }


        Utilidad.vibraticionBotones(context);
        if (Utilidad.isNetworkAvailable()) {
            final String num_Fa=num_Factu.getText().toString().trim();

            //final Integer num_Fa = Integer.valueOf(num_Factu.getText().toString().trim());
            if (!facturaValida) {
                params = new RequestParams();
                params.add("cod_suc_trib", cod_Suc.toString());
                params.add("cod_fac_tribut", cod_Fac);
                params.add("numfac", num_Fa.toString());
                params.add("username", WebService.USUARIOLOGEADO);
                final String finalCod_Fac = cod_Fac;
                ValidarFactura task = new ValidarFactura(new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        try {
                            if (!WebService.errToken.equals("")) {
                                Intent myIntent = new Intent(context, Login.class);
                                startActivity(myIntent);
                            } else {
                                if (WebService.n_factu.getFact().equals("Factura Invalida")) {
                                    //esto no lo está haciendo
                                    String novalida = getResources().getString(R.string.Ndispo) + " " + cod_Suc + "-" + finalCod_Fac + "-" + num_Fa + "\n"+WebService.err_ValidaFact;
                                    retorno.setText(novalida);
                                    btnValidarFac.setText("Validar factura");
                                    facturaValida = false;
                                } else {
                                    if(btnValidarFac.getText().toString().equals("Generar factura")){
                                        retorno.setText(getResources().getString(R.string.dispo) + " " + cod_Suc + "-" + finalCod_Fac + "-" + num_Fa);
                                        facturaValida = true;
                                        facturarContado(finalCod_Fac,v);
                                    }else{
                                        retorno.setText(getResources().getString(R.string.dispo) + " " + cod_Suc + "-" + finalCod_Fac + "-" + num_Fa);
                                        btnValidarFac.setText("Generar factura");
                                        facturaValida = true;
                                    }

                                }
                            }
                        } catch (Exception exc) {

                            retorno.setText(exc.toString());
                            exc.printStackTrace();
                        }

                    }
                });
                task.execute();
            }
            if (facturaValida) {
                facturarContado(cod_Fac,v);
            }

        } else {
            Utilidad.CargarToastConexion(context);
        }


    }

    private void facturarContado(String cod_Fac, View v) {



        if(!valorIntent.equals("NotaCredito")) {
            GuardarDatosUsuario objeto = new GuardarDatosUsuario(context);
            //AGREGADO 24/06/2019 ESTADO ACTUAL ERA -1 A 9
            WebService.EstadoActual = 5;
            objeto.GuardarDatos();
        }
        paramsIng = new RequestParams();

        String itmsMandar = "";
        String items="";
        for (int i = 0; WebService.ArrayItemsViaje.size() > i; i++) {
            Item instenciaItem = WebService.ArrayItemsViaje.get(i);
            double valor = Math.floor(instenciaItem.getCantidad_entregada() * instenciaItem.getPrecio_Unitario());
            double impDesLin = 0;
            if(instenciaItem.getDtoLinea()>0){
                impDesLin =   instenciaItem.getCantidad_entregada() * instenciaItem.getPrecio_Unitario()*(instenciaItem.getDtoLinea()/100);
            }
            double valorCdesc =valor-impDesLin;

            if (i != WebService.ArrayItemsViaje.size() - 1) {

                itmsMandar = itmsMandar
                        //+ instenciaItem.getNom_Articulo().trim().replace( ',','.' )
                        + "," + instenciaItem.getCod_Articulo().trim()
                        + "," + instenciaItem.getCantidad_entregada()
                        + "," + instenciaItem.getPrecio_Unitario()
                        + "," + instenciaItem.getPorc_Iva()
                        + "," + valor
                        + "," + instenciaItem.getCod_Tasa_Iva()
                        + "," + instenciaItem.getCantidad()
                        + "," + instenciaItem.getCant()
                        + "," + instenciaItem.getCod_rec()
                        + "," + instenciaItem.getObservacion()
                        + "," + instenciaItem.getReposicion()
                        + "," + instenciaItem.getCod_uni_vta()
                        + "," + instenciaItem.getDtoLinea()
                        + "," + impDesLin
                        + "," + valorCdesc
                        + ";";

                items=items+ instenciaItem.getNom_Articulo() +"    "+instenciaItem.getCantidad()+"    "+valorCdesc+" \r\n";
            } else {
                itmsMandar = itmsMandar
                        //+ instenciaItem.getNom_Articulo().trim().replace( ',','.' )
                        + "," + instenciaItem.getCod_Articulo().trim()
                        + "," + instenciaItem.getCantidad_entregada()
                        + "," + instenciaItem.getPrecio_Unitario()
                        + "," + instenciaItem.getPorc_Iva()
                        + "," + valor
                        + "," + instenciaItem.getCod_Tasa_Iva()
                        + "," + instenciaItem.getCantidad()
                        + "," + instenciaItem.getCant()
                        + "," + instenciaItem.getCod_rec()
                        + "," + instenciaItem.getObservacion()
                        + "," + instenciaItem.getReposicion()
                        + "," + instenciaItem.getCod_uni_vta()
                        + "," + instenciaItem.getDtoLinea()
                        + "," + impDesLin
                        + "," + valorCdesc
                ;
                items=items+ instenciaItem.getNom_Articulo() +"    "+instenciaItem.getCantidad()+"    "+valorCdesc+" \r\n";
            }

        }
        String tot = String.valueOf(WebService.TotalFactura);
        try {
            String cod_suc = "";
            String cod_fac = "";
            String fec_fac = "";
            String nro_doc = "0";
            double imp_origen = 0;

            if(valorIntent.equals("NotaCredito")){
                cod_suc = WebService.fact.getCod_Suc_Tribut();
                cod_fac = WebService.fact.getCod_Fac_Tribut();
                fec_fac = WebService.fact.getFecha();
                nro_doc = WebService.fact.getNro_Docum();
                imp_origen = WebService.fact.getImporte();
            }

            //AGREGADO PARA NOTA CREDITO
            paramsIng.add("cod_suct_ref", cod_suc);
            paramsIng.add("cod_fact_ref",  cod_fac);
            paramsIng.add("nro_doca", nro_doc);
            paramsIng.add("fec_factura", fec_fac);
            paramsIng.add("imp_origen", String.valueOf(imp_origen));
            //FIN AGREGADO

            // a
            paramsIng.add("cod_emp", WebService.Entrega_A_Realizar.getCodEmp());
            paramsIng.add("cod_doc_uni", WebService.Entrega_A_Realizar.getCodigoDocumentoCliente());
            paramsIng.add("cod_moneda",WebService.Entrega_A_Realizar.getCodigoMoneda());
            paramsIng.add("cod_fpago",WebService.Entrega_A_Realizar.getCodigoTipoPago() );

            //aaa
            String seleccion = spinner_fac_directo.getSelectedItem().toString();
            if(seleccion.equals("No")){
                paramsIng.add("cod_tit", WebService.Entrega_A_Realizar.getCod_Tit().trim());
            }else{
                paramsIng.add("cod_tit", codCliente.getText().toString());
            }

            paramsIng.add("valorIntent", valorIntent);
            paramsIng.add("total", String.valueOf(WebService.TotalFactura));
            paramsIng.add("importe_tot_sin_coma", tot);
            paramsIng.add("iva5", String.valueOf(WebService.IVA5));
            paramsIng.add("iva10", String.valueOf(WebService.IVA10));
            paramsIng.add("total_iva", Double.toString(WebService.TotalFactura - (WebService.IVA5 + WebService.IVA10)));
            paramsIng.add("gravada_10", String.valueOf(WebService.Gravada10));
            paramsIng.add("gravada_5", String.valueOf(WebService.Gravada5));
            paramsIng.add("nro_viaje", WebService.viajeSeleccionado.getNumViaje());
            paramsIng.add("nro_orden_entrega", WebService.Entrega_A_Realizar.getNro_Docum());
            //MODIFICADO RP 14/12/2021 para que en el nro_doc_uni le pase el cod_tit
            if(seleccion.equals("No")){
                //paramsIng.add("cod_tit_gestion", WebService.Entrega_A_Realizar.getCod_Tit_Gestion().trim());
                //RP - modificado para que cuando es NO pero selecciona otro documento del mismo cliente, le pase el seleccionado 16/12/2021
                if (codCliente.getText().toString().equals(WebService.Entrega_A_Realizar.getCod_Tit_Gestion().trim())) {
                    paramsIng.add("cod_tit_gestion", WebService.Entrega_A_Realizar.getCod_Tit_Gestion().trim());
                }else {
                    paramsIng.add("cod_tit_gestion",  codCliente.getText().toString());
                }
                //RP - fin de agregado 16/12/2021
            }else{
                paramsIng.add("cod_tit_gestion", codCliente.getText().toString());
            }
            //fin agregado
            paramsIng.add("cod_suc", WebService.cod_sucu);
            paramsIng.add("cod_suc_tribut", cod_Suc);
            paramsIng.add("cod_fac_tribut", cod_Fac);
            paramsIng.add("nro_factura", WebService.n_factu.getFact());
            paramsIng.add("nro_timbrado", WebService.n_factu.getTimbr().replace("\"", "").replace("\\", "").trim());
            paramsIng.add("vance_timbrado", WebService.n_factu.getVence());
            paramsIng.add("id_factura", String.valueOf(WebService.n_factu.getId_Factura()));
            paramsIng.add("cantidad_lineas", String.valueOf(WebService.ArrayItemsViaje.size()));
            paramsIng.add("usuario", WebService.USUARIOLOGEADO);
            paramsIng.add("porc_iva", "5");
            paramsIng.add("lineas", itmsMandar);
            paramsIng.add("fec_venc", "2018-12-31");
            paramsIng.add("imp_dcto_com_iva", String.valueOf(WebService.Entrega_A_Realizar.getDescuento()));
            paramsIng.add("mac",mBTAddrList.get(spinnerBTDeviceList.getSelectedItemPosition()));
           // paramsIng.add("mac","A4:83:E7:54:A8:56");

            WebService.paramsIng=paramsIng;
            try{
                Intent myIntent = new Intent(v.getContext(), IngresarValoresTruckSales.class);
                startActivity(myIntent);
            }catch (Exception e){
                e.getMessage();
            }

        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    private void cobrarCredito() {

        Utilidad.vibraticionBotones(context);
        if (Utilidad.isNetworkAvailable()) {
            cod_Suc = cod_Suc_Tribut.getText().toString().trim();
            final String cod_Fac = cod_Fac_Tribut.getText().toString().trim();
            final String num_Fa=num_Factu.getText().toString().trim();

            //final Integer num_Fa = Integer.valueOf(num_Factu.getText().toString().trim());
            if (!facturaValida) {
                params = new RequestParams();
                params.add("cod_suc_trib", cod_Suc.toString());
                params.add("cod_fac_tribut", cod_Fac);
                params.add("numfac", num_Fa.toString());
                params.add("username", WebService.USUARIOLOGEADO);
                ValidarFactura task = new ValidarFactura(new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        try {
                            if (!WebService.errToken.equals("")) {
                                Intent myIntent = new Intent(context, Login.class);
                                startActivity(myIntent);
                            } else {
                                if (WebService.n_factu.getFact().equals("Factura Invalida")) {
                                    //esto no lo está haciendo
                                    String novalida = getResources().getString(R.string.Ndispo) + " " + cod_Suc + "-" + cod_Fac + "-" + num_Fa + "\n"+WebService.err_ValidaFact;
                                    retorno.setText(novalida);
                                    btnValidarFac.setText("Validar factura");
                                    facturaValida = false;
                                } else {
                                    if(btnValidarFac.getText().toString().equals("Generar factura")){
                                        retorno.setText(getResources().getString(R.string.dispo) + " " + cod_Suc + "-" + cod_Fac + "-" + num_Fa);
                                        facturaValida = true;
                                        facturar(cod_Fac);
                                    }else{
                                        retorno.setText(getResources().getString(R.string.dispo) + " " + cod_Suc + "-" + cod_Fac + "-" + num_Fa);
                                        btnValidarFac.setText("Generar factura");
                                        facturaValida = true;
                                    }

                                }
                            }
                        } catch (Exception exc) {

                            retorno.setText(exc.toString());
                            exc.printStackTrace();
                        }

                    }
                });
                task.execute();
            }
            if (facturaValida) {
                facturar(cod_Fac);
            }

        } else {
            Utilidad.CargarToastConexion(context);
        }
        
    }

    private void facturar(String cod_Fac) {
        retorno.setText("");
        if(!valorIntent.equals("NotaCredito")) {
            GuardarDatosUsuario objeto = new GuardarDatosUsuario(context);
            //AGREGADO 24/06/2019 ESTADO ACTUAL ERA -1 A 9
            WebService.EstadoActual = 5;
            objeto.GuardarDatos();
        }
        paramsIng = new RequestParams();

        String itmsMandar = "";
        String items="";
        for (int i = 0; WebService.ArrayItemsViaje.size() > i; i++) {
            Item instenciaItem = WebService.ArrayItemsViaje.get(i);
            double valor = Math.floor(instenciaItem.getCantidad_entregada() * instenciaItem.getPrecio_Unitario());
            double impDesLin = 0;
            if(instenciaItem.getDtoLinea()>0){
                impDesLin =   instenciaItem.getCantidad_entregada() * instenciaItem.getPrecio_Unitario()*(instenciaItem.getDtoLinea()/100);
            }
            double valorCdesc =valor-impDesLin;

            if (i != WebService.ArrayItemsViaje.size() - 1) {

                itmsMandar = itmsMandar
                        //+ instenciaItem.getNom_Articulo().trim().replace( ',','.' )
                        + "," + instenciaItem.getCod_Articulo().trim()
                        + "," + instenciaItem.getCantidad_entregada()
                        + "," + instenciaItem.getPrecio_Unitario()
                        + "," + instenciaItem.getPorc_Iva()
                        + "," + valor
                        + "," + instenciaItem.getCod_Tasa_Iva()
                        + "," + instenciaItem.getCantidad()
                        + "," + instenciaItem.getCant()
                        + "," + instenciaItem.getCod_rec()
                        + "," + instenciaItem.getObservacion()
                        + "," + instenciaItem.getReposicion()
                        + "," + instenciaItem.getCod_uni_vta()
                        + "," + instenciaItem.getDtoLinea()
                        + "," + impDesLin
                        + "," + valorCdesc
                        + ";";

                items=items+ instenciaItem.getNom_Articulo() +"    "+instenciaItem.getCantidad()+"    "+valorCdesc+" \r\n";
            } else {
                itmsMandar = itmsMandar
                        //+ instenciaItem.getNom_Articulo().trim().replace( ',','.' )
                        + "," + instenciaItem.getCod_Articulo().trim()
                        + "," + instenciaItem.getCantidad_entregada()
                        + "," + instenciaItem.getPrecio_Unitario()
                        + "," + instenciaItem.getPorc_Iva()
                        + "," + valor
                        + "," + instenciaItem.getCod_Tasa_Iva()
                        + "," + instenciaItem.getCantidad()
                        + "," + instenciaItem.getCant()
                        + "," + instenciaItem.getCod_rec()
                        + "," + instenciaItem.getObservacion()
                        + "," + instenciaItem.getReposicion()
                        + "," + instenciaItem.getCod_uni_vta()
                        + "," + instenciaItem.getDtoLinea()
                        + "," + impDesLin
                        + "," + valorCdesc
                ;
                items=items+ instenciaItem.getNom_Articulo() +"    "+instenciaItem.getCantidad()+"    "+valorCdesc+" \r\n";
            }

        }
        String tot = String.valueOf(WebService.TotalFactura);
        try {
            String cod_suc = "";
            String cod_fac = "";
            String fec_fac = "";
            String nro_doc = "0";
            double imp_origen = 0;

            if(valorIntent.equals("NotaCredito")){
                cod_suc = WebService.fact.getCod_Suc_Tribut();
                cod_fac = WebService.fact.getCod_Fac_Tribut();
                fec_fac = WebService.fact.getFecha();
                nro_doc = WebService.fact.getNro_Docum();
                imp_origen = WebService.fact.getImporte();
            }

            //AGREGADO PARA NOTA CREDITO
            paramsIng.add("cod_suct_ref", cod_suc);
            paramsIng.add("cod_fact_ref",  cod_fac);
            paramsIng.add("nro_doca", nro_doc);
            paramsIng.add("fec_factura", fec_fac);
            paramsIng.add("imp_origen", String.valueOf(imp_origen));
            //FIN AGREGADO

            // a
            paramsIng.add("cod_emp", WebService.Entrega_A_Realizar.getCodEmp());
            paramsIng.add("cod_doc_uni", WebService.Entrega_A_Realizar.getCodigoDocumentoCliente());
            paramsIng.add("cod_moneda",WebService.Entrega_A_Realizar.getCodigoMoneda());
            paramsIng.add("cod_fpago",WebService.Entrega_A_Realizar.getCodigoTipoPago() );

            //aaa
            String seleccion = spinner_fac_directo.getSelectedItem().toString();
            if(seleccion.equals("No")){
                paramsIng.add("cod_tit", WebService.Entrega_A_Realizar.getCod_Tit().trim());
            }else{
                paramsIng.add("cod_tit", codCliente.getText().toString());
            }

            paramsIng.add("valorIntent", valorIntent);
            paramsIng.add("total", String.valueOf(WebService.TotalFactura));
            paramsIng.add("importe_tot_sin_coma", tot);
            paramsIng.add("iva5", String.valueOf(WebService.IVA5));
            paramsIng.add("iva10", String.valueOf(WebService.IVA10));
            paramsIng.add("total_iva", Double.toString(WebService.TotalFactura - (WebService.IVA5 + WebService.IVA10)));
            paramsIng.add("gravada_10", String.valueOf(WebService.Gravada10));
            paramsIng.add("gravada_5", String.valueOf(WebService.Gravada5));
            paramsIng.add("nro_viaje", WebService.viajeSeleccionado.getNumViaje());
            paramsIng.add("nro_orden_entrega", WebService.Entrega_A_Realizar.getNro_Docum());
            //MODIFICADO RP - 14/12/2021 para que en el nro_doc_uni le pase el cod_tit
            if(seleccion.equals("No")){
                //RP - agregado para que cuando es NO pero selecciona otro documento del mismo cliente, le pase el seleccionado 16/12/2021
                if (codCliente.getText().toString().equals(WebService.Entrega_A_Realizar.getCod_Tit_Gestion().trim())) {
                    paramsIng.add("cod_tit_gestion", WebService.Entrega_A_Realizar.getCod_Tit_Gestion().trim());
                }else {
                    paramsIng.add("cod_tit_gestion",  codCliente.getText().toString());
                }
                //RP - fin de agregado 16/12/2021
            }else{
                paramsIng.add("cod_tit_gestion", codCliente.getText().toString());
            }
            //fin agregado 14/12/2021
            paramsIng.add("cod_suc", WebService.cod_sucu);
            paramsIng.add("cod_suc_tribut", cod_Suc);
            paramsIng.add("cod_fac_tribut", cod_Fac);
            paramsIng.add("nro_factura", WebService.n_factu.getFact());
            paramsIng.add("nro_timbrado", WebService.n_factu.getTimbr().replace("\"", "").replace("\\", "").trim());
            paramsIng.add("vance_timbrado", WebService.n_factu.getVence());
            paramsIng.add("id_factura", String.valueOf(WebService.n_factu.getId_Factura()));
            //paramsIng.add( "imp_mov_mo_insert", "0" );
            //paramsIng.add( "cod_cta", "5" );
            paramsIng.add("cantidad_lineas", String.valueOf(WebService.ArrayItemsViaje.size()));
            paramsIng.add("usuario", WebService.USUARIOLOGEADO);
            paramsIng.add("porc_iva", "5");
            paramsIng.add("lineas", itmsMandar);
            paramsIng.add("fec_venc", "2018-12-31");
            paramsIng.add("imp_dcto_com_iva", String.valueOf(WebService.Entrega_A_Realizar.getDescuento()));

            paramsIng.add("mac",mBTAddrList.get(spinnerBTDeviceList.getSelectedItemPosition()));
          //  paramsIng.add("mac","A4:83:E7:54:A8:56");

            AgregarFactura task2 = new AgregarFactura();

            task2.execute();
            try {
                task2.get();
                if(!WebService.reto_AgregaFactura.equals("ok")){
                    retorno.setText(WebService.reto_AgregaFactura.trim());
                }else{
                    if(valorIntent.equals("NotaCredito")){
                        retorno.setText(getResources().getString(R.string.reto_NC));
                    }else {
                        retorno.setText(getResources().getString(R.string.reto_Fac_1));
                    }
                    /*
                    Printer pr = new Printer();
                    PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
                    pr.setPrMang(printManager);
                    pr.setContx(context);
                    pr.setValor(String.valueOf(WebService.nro_trans));
                    pr.setTipo("F");
                    pr.genarPdf(pr);
                    */
                    //FacturaZebra item = new FacturaZebra();

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
                //System.out.println( "ERROR| Error en la excepcion dentro de las lineas 181-194 factura.java: \n" + e.toString() );
                e.printStackTrace();
                // Toast.makeText( getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG ).show();
            }
        } catch (Exception error) {
            error.printStackTrace();
            // Toast.makeText( getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG ).show();
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
            if (facturaZebra!=null){
                imprimirZebraFactura(facturaZebra);
            }

        }
    }

    public void imprimirZebraFactura(final FacturaZebra factura) {
    try {


        if (mBTAddrList.get(spinnerBTDeviceList.getSelectedItemPosition()) != "") {

            new Thread(new Runnable() {
                public void run() {
                    //                enableTestButton(false);
                    Looper.prepare();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            edit.setText(edit.getText()+"\n"+"Comienza proceso " );
                        }
                    });

                    String items = "";
                    Double suma = 0D;
                    for (FacturaItemZebra item : factura.getItems()) {

                        //items=items +getPalabraNumero(Utilidad.redondearDecimalesNew(Double.valueOf(item.getCant_aprobada()), Integer.valueOf(WebService.configuracion.getDec_cant())),8)+"     "
                        items = items + "    "+ getPalabra(item.getNom_articulo(), 60) + " \r\n"
                                + getPalabraNumero(Utilidad.redondearDecimalesNew(Double.valueOf(item.getCant_aprobada()), Integer.valueOf(WebService.configuracion.getDec_cant())), 8) + " X "
                                + getPalabraNumero(Utilidad.redondearDecimalesNew(Double.valueOf(item.getPrecio_iva_inc()), Integer.valueOf(WebService.configuracion.getDec_montomn())), 9) + " - 0.00                         "
                                + getPalabraNumero(Utilidad.redondearDecimalesNew(Double.valueOf(item.getTotal_iva_inc()), Integer.valueOf(WebService.configuracion.getDec_montomn())), 9) + "    "
                                + " \r\n";
                        suma = suma + Double.valueOf(item.getTotal_iva_inc());
                    }

                    System.out.println("Prepara para imprimir");

                    String mac = mBTAddrList.get(spinnerBTDeviceList.getSelectedItemPosition());
                    //edit.setText(mac);

                    final boolean imprime = Utilidad.printFacturaQR(factura, items, suma, mac); //llama a Utilidades
                    //printFacturaNuevo(factura, items, suma); //RP 21/06/2022 lo cambio por llamar a utilidades
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                             if (imprime) {
                                 edit.setText(edit.getText() + "\n" + "Proceso finalizado. ");
                             }
                             else {
                                 edit.setText("No se pudo imprimir ");
                             }
                             //ir a entregas
                            TraerEntregasCliente();
                        }
                    });
                    //Toast.makeText(MainActivity.this, "Comprobante impreso.", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    Looper.myLooper().quit();

                    //TraerEntregasCliente();

                }
            }).start();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //edit.setText(edit.getText()+"\n"+"No hay impresora seleccionada.");
                }
            });
            Toast.makeText(this, "No hay impresora seleccionada.", Toast.LENGTH_LONG).show();
        }
     }
    catch(Exception e) {
        e.printStackTrace();
        }
    }

    private void printFacturaNuevo(FacturaZebra factura, String items, Double suma) {
        int itemsSize=factura.getItems().size();
        String mac = mBTAddrList.get(spinnerBTDeviceList.getSelectedItemPosition());
        try {
            String example1 = "" +
                    "! 0 200 200 "+getLinea(25,itemsSize)+" 1\r\n" +
                    "CENTER\r\n" +
                    "T 4 0 10 0" +factura.getNom_empresa()+"\r\n" +
                    "T 4 0 10 50"+factura.getTitulo()+"\r\n" +
                    "T 7 0 10 100"+factura.getSubtitulo_movil()+"\r\n" +
                    "T 7 0 10 125"+factura.getNom_localidad()+"\r\n" +
                    "T 7 0 10 150 No. Punto de Venta "+factura.getCod_control_fact()+"\r\n" +
                    "T 7 0 10 175 "+factura.getDir_empresa()+"\r\n" +
                    "T 7 0 10 200 Tel.: "+factura.getTel_empresa()+"\r\n" +
                    "T 7 0 10 225 "+factura.getMunicipio()+
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
                    "T 7 0 10 "+getLinea(1,itemsSize)+"                                       TOTAL "+getPalabraNumero(Utilidad.redondearDecimalesNew(suma, Integer.valueOf(WebService.configuracion.getDec_montomn())),12) +" \r\n"+
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

            edit.setText(example1);

            Connection thePrinterConn = new BluetoothConnectionInsecure(mac);
            thePrinterConn.open();
            if (!thePrinterConn.isConnected()) {
                edit.setText("No conectado");
            } else {
                thePrinterConn.write(example1.getBytes("ISO-8859-1"));
                Thread.sleep(500);
                thePrinterConn.close();
            }
            Looper.myLooper().quit();
            TraerEntregasCliente();
        } catch (Exception e) {
            edit.setText(e.getMessage());
            e.printStackTrace();
        }
    }

    public void TraerEntregasCliente(){
        params1 = new RequestParams();
        params1.put("nro_viaje", WebService.viajeSeleccionado.getNumViaje());
        params1.put("seleccion", WebService.viajeSeleccionado.getTipo().equals("retiro") ? "r" : "e");
        params1.put("username", WebService.USUARIOLOGEADO);
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
    private void printQR(final String mac) {
        new Thread(new Runnable() {
            public void run() {
                Connection connection = null;
                ZebraCardPrinter zebraCardPrinter = null;

                try {
                    connection = new TcpConnection(mac, 9100);
                    connection.open();

                    zebraCardPrinter = ZebraCardPrinterFactory.getInstance(connection);

                    List<GraphicsInfo> graphicsData = new ArrayList<GraphicsInfo>();
                    graphicsData.add(drawGraphics(zebraCardPrinter));

                    zebraCardPrinter.setJobSetting(ZebraCardJobSettingNames.K_OPTIMIZATION_FRONT, "Barcode");

                    // Send job
                    int jobId = zebraCardPrinter.print(1, graphicsData);

                    // Poll job status
                    JobStatusInfo jobStatusInfo = pollJobStatus(jobId, zebraCardPrinter);
                    showToastMessage(String.format(Locale.US, "Job %d completed with status '%s'", jobId, jobStatusInfo.printStatus));
                } catch (Exception e) {
                    showToastMessage("Error printing barcode image: " + e.getLocalizedMessage());
                } finally {
                    cleanUpQuietly(connection, zebraCardPrinter);
                }
            }
        }).start();
    }




    private GraphicsInfo drawGraphics(ZebraCardPrinter zebraCardPrinter) throws ConnectionException, IOException, ZebraCardException {
        ZebraGraphics graphics = null;
        try {
            graphics = new ZebraCardGraphics(zebraCardPrinter);
            graphics.initialize(getApplicationContext(), 0, 0, OrientationType.Landscape, PrintType.MonoK, Color.WHITE);

            drawQRCode(graphics);
            drawCode39(graphics);
            drawCode128(graphics);
            drawCodePDF417(graphics);

            ZebraCardImageI imageData = graphics.createImage();
            return addBasicImage(CardSide.Front, PrintType.MonoK, 0, 0, -1, imageData.getImageData());
        } finally {
            if (graphics != null) {
                graphics.close();
            }
        }
    }

    private void drawQRCode(ZebraGraphics graphics) throws IllegalArgumentException, ZebraCardException {
        CodeQRUtil codeQRUtil = ZebraBarcodeFactory.getQRCode(graphics);
        codeQRUtil.drawBarcode("https://www.zebra.com", 50, 50, 100, 100, Rotation.ROTATE_0);
    }

    private void drawCode39(ZebraGraphics graphics) throws IllegalArgumentException, ZebraCardException {
        Code39Util code39Util = ZebraBarcodeFactory.getCode39(graphics);
        code39Util.setMessagePosition(HumanReadablePlacement.BOTTOM);
        code39Util.setDisplayStartStopChars(true);
        code39Util.drawBarcode("1234567890", 50, 175, 400, 75, Rotation.ROTATE_0);
    }

    private void drawCode128(ZebraGraphics graphics) throws IllegalArgumentException, ZebraCardException {
        BarcodeUtil barcodeUtil = ZebraBarcodeFactory.getCode128(graphics);
        barcodeUtil.setMessagePosition(HumanReadablePlacement.BOTTOM);
        barcodeUtil.drawBarcode("Code128 Test", 50, 325, 400, 75, Rotation.ROTATE_0);
    }

    private void drawCodePDF417(ZebraGraphics graphics) throws IllegalArgumentException, ZebraCardException {
        CodePDF417Util codePDF417Util = ZebraBarcodeFactory.getCodePDF417(graphics);
        codePDF417Util.drawBarcode("Zebra Technologies", 50, 450, 400, 300, Rotation.ROTATE_0);
    }

    private GraphicsInfo addBasicImage(CardSide side, PrintType printType, int xOffset, int yOffset, int fillColor, byte[] imageData) {
        GraphicsInfo graphicsInfo = new GraphicsInfo();
        graphicsInfo.fillColor = fillColor;
        graphicsInfo.graphicData = imageData != null ? new ZebraCardImage(imageData) : null;
        graphicsInfo.graphicType = imageData != null ? GraphicType.BMP : GraphicType.NA;
        graphicsInfo.opacity = 0;
        graphicsInfo.overprint = false;
        graphicsInfo.printType = printType;
        graphicsInfo.side = side;
        graphicsInfo.xOffset = xOffset;
        graphicsInfo.yOffset = yOffset;
        return graphicsInfo;
    }

    private JobStatusInfo pollJobStatus(int jobId, ZebraCardPrinter zebraCardPrinter) throws ConnectionException, ZebraCardException, ZebraIllegalArgumentException {
        long dropDeadTime = System.currentTimeMillis() + 40000;
        long pollInterval = 500;

        // Poll job status
        JobStatusInfo jobStatusInfo = new JobStatusInfo();

        do {
            jobStatusInfo = zebraCardPrinter.getJobStatus(jobId);

            String alarmDesc = jobStatusInfo.alarmInfo.value > 0 ? String.format(Locale.US, " (%s)", jobStatusInfo.alarmInfo.description) : "";
            String errorDesc = jobStatusInfo.errorInfo.value > 0 ? String.format(Locale.US, " (%s)", jobStatusInfo.errorInfo.description) : "";

            System.out.println(String.format("Job %d, Status:%s, Card Position:%s, Alarm Code:%d%s, Error Code:%d%s", jobId, jobStatusInfo.printStatus, jobStatusInfo.cardPosition, jobStatusInfo.alarmInfo.value, alarmDesc, jobStatusInfo.errorInfo.value, errorDesc));

            if (jobStatusInfo.printStatus.contains("done_ok")) {
                break;
            } else if (jobStatusInfo.printStatus.contains("alarm_handling")) {
                System.out.println("Alarm Detected: " + jobStatusInfo.alarmInfo.description);
            } else if (jobStatusInfo.printStatus.contains("error") || jobStatusInfo.printStatus.contains("cancelled")) {
                break;
            } else if (jobStatusInfo.errorInfo.value > 0) {
                System.out.println(String.format(Locale.US, "The job encountered an error [%s] and was cancelled.", jobStatusInfo.errorInfo.description));
                zebraCardPrinter.cancel(jobId);
            }

            if (System.currentTimeMillis() > dropDeadTime) {
                break;
            }

            try {
                Thread.sleep(pollInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (true);

        return jobStatusInfo;
    }

    private void showToastMessage(final String message) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cleanUpQuietly(Connection connection, ZebraCardPrinter genericPrinter) {
        try {
            if (genericPrinter != null) {
                genericPrinter.destroy();
                genericPrinter = null;
            }
        } catch (ZebraCardException e) {
            e.printStackTrace();
        }

        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (ConnectionException e) {
                e.printStackTrace();
            }
        }
    }

    public void printTest(final String s, String mac) {
//    	ZebraPrinter printer = connect();
        connect(mac);
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //edit.setText(edit.getText()+"\n"+"Imprimiendo texto prueba: " + s);
                }
            });

            byte[] printerInstructions = s.getBytes("ASCII");
            printerConnection.write(printerInstructions);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //edit.setText(edit.getText()+"\n"+"Instruccion de impresion enviada correctamente: ");
                }
            });
        } catch (final ConnectionException e) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //edit.setText(edit.getText()+"\n"+"Error imprimiendo. " + e.getMessage());
                }
            });

            Log.e("cobranza_movil", "Error imprimiendo. " + e.getMessage());
//            setStatus(e.getMessage(), Color.RED);
        } catch (final UnsupportedEncodingException e) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //edit.setText(edit.getText()+"\n"+"Error codificación. " + e.getMessage());
                }
            });

            Log.e("cobranza_movil", "Error de codificación. " + e.getMessage());
        } finally {
            disconnect();
        }
    }


    private void printTest2(String mac) {
        try {
            // Instantiate insecure connection for given Bluetooth&reg; MAC Address.
            Connection thePrinterConn = new BluetoothConnectionInsecure(mac);

            // Initialize
            Looper.prepare();

            // Open the connection - physical connection is established here.
            thePrinterConn.open();
            /*
            // This example prints "This is a ZPL test." near the top of the label.
            String zplData = "^XA^FO20,20^A0N,25,25^FDThis is a ZPL test.^FS^XZ";

            // Send the data to printer as a byte array.
            thePrinterConn.write(zplData.getBytes());



            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            qr.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            qr.recycle();
            */
            ZebraPrinter printer = ZebraPrinterFactory.getInstance(thePrinterConn);
            int x = 0;
            int y = 0;
            //String imageUri = "drawable://" + R.drawable.box;
            //printer.printImage(imageUri, x, y);
            Bitmap qr = getQR();
            printer.printImage(new ZebraImageAndroid(qr), 0, 0, 550, 412, false);
            // Make sure the data got to the printer before closing the connection
            Thread.sleep(500);

            // Close the insecure connection to release resources.
            thePrinterConn.close();

            Looper.myLooper().quit();
        } catch (Exception e) {
            // Handle communications error here.
            e.printStackTrace();
        }
    }


    /**
     * This method makes the call to the printer and send the images to the printer to print and implements best practices to check the status of the printer.
     *
     * @param mac
     */
    private void printPhotoFromExternal(final String mac) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //edit.setText(edit.getText()+"\n"+"Imprimo QR 1 ");
            }
        });
        final Bitmap bitmap=((BitmapDrawable)imageView.getDrawable()).getBitmap();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               // edit.setText(edit.getText()+"\n"+"Imprimo QR 2 ");
            }
        });
        new Thread(new Runnable() {
            public void run() {

                try {
                    //getAndSaveSettings();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // edit.setText(edit.getText()+"\n"+"Imprimo QR 3 ");
                        }
                    });
                    Looper.prepare();

                    BluetoothConnection connection = new BluetoothConnection(mac);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // edit.setText(edit.getText()+"\n"+"Imprimo QR 4 ");
                        }
                    });
                    connection.open();
                    ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // edit.setText(edit.getText()+"\n"+"Imprimo QR 5 ");
                        }
                    });
                    getPrinterStatus(connection);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // edit.setText(edit.getText()+"\n"+"Imprimo QR 7 ");
                        }
                    });
                    ZebraPrinterLinkOs linkOsPrinter = ZebraPrinterFactory.createLinkOsPrinter(printer);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //edit.setText(edit.getText()+"\n"+"Imprimo QR 8 ");
                        }
                    });
                    PrinterStatus printerStatus = (linkOsPrinter != null) ? linkOsPrinter.getCurrentStatus() : printer.getCurrentStatus();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // edit.setText(edit.getText()+"\n"+"Imprimo QR 9 ");
                        }
                    });
                    /**
                     * check if the printer is ready or not and then send the image to print
                     */

                    if (printerStatus.isReadyToPrint) {
                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //edit.setText(edit.getText()+"\n"+"Imprimo QR 9 ");
                                }
                            });


                            printer.printImage(new ZebraImageAndroid(bitmap), 0, 0, 550, 412, false);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //edit.setText(edit.getText()+"\n"+"Imprimo QR 10 ");
                                }
                            });
                        } catch (final ConnectionException e) {
                          //  helper.showErrorDialogOnGuiThread(e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                   // edit.setText(edit.getText()+"\n"+"Imprimo QR ERROR: "+e.getMessage());
                                }
                            });
                        }
                    } else if (printerStatus.isHeadOpen) {
                      //  helper.showErrorMessage("Error: Head Open \nPlease Close Printer Head to Print.");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               // edit.setText(edit.getText()+"\n"+"Imprimo QR ERROR: Head Open Please Close Printer Head to Print.");
                            }
                        });
                    } else if (printerStatus.isPaused) {
                       // helper.showErrorMessage("Error: Printer Paused.");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               // edit.setText(edit.getText()+"\n"+"Imprimo QR ERROR:  Printer Paused.");
                            }
                        });
                    } else if (printerStatus.isPaperOut) {
                      //  helper.showErrorMessage("Error: Media Out \nPlease Load Media to Print.");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // edit.setText(edit.getText()+"\n"+"Imprimo QR ERROR:  Media Out \\nPlease Load Media to Print.");
                            }
                        });
                    } else {
                      //  helper.showErrorMessage("Error: Please check the Connection of the Printer.");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //edit.setText(edit.getText()+"\n"+"Imprimo QR ERROR:  Please check the Connection of the Printer.");
                            }
                        });
                    }

                    connection.close();

                } catch (final ConnectionException e) {
                   // helper.showErrorDialogOnGuiThread(e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //edit.setText(edit.getText()+"\n"+"Imprimo QR ERROR:  : "+e.getMessage());
                        }
                    });
                } catch (final ZebraPrinterLanguageUnknownException e) {
                   // helper.showErrorDialogOnGuiThread(e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //edit.setText(edit.getText()+"\n"+"Imprimo QR ERROR: "+e.getMessage());
                        }
                    });
                } finally {
                    bitmap.recycle();
                  //  helper.dismissLoadingDialog();
                    Looper.myLooper().quit();

                }
            }

        }).start();

    }

    /**
     * This method implements the best practices i.e., Checks the language of the printer and set the language of the printer to ZPL.
     *
     * @throws ConnectionException
     * @param connection
     */
    private void getPrinterStatus(BluetoothConnection connection) throws ConnectionException {


        final String printerLanguage = SGD.GET("device.languages", connection);

        final String displayPrinterLanguage = "Printer Language is " + printerLanguage;

        SGD.SET("device.languages", "zpl", connection);

        factura.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // edit.setText(edit.getText()+"\n"+"Imprimo QR 6 "+displayPrinterLanguage + "\n" + "Language set to ZPL");
                    }
                });
                Toast.makeText(factura.this, displayPrinterLanguage + "\n" + "Language set to ZPL", Toast.LENGTH_LONG).show();

            }
        });

    }

    private Bitmap getQR() {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try{
            BitMatrix bitMatrix = multiFormatWriter.encode("Test PrintQR", BarcodeFormat.QR_CODE,500,500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
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




    private ZebraPrinter connect(String mac) {
//        printerConnection = null;
        printerConnection = new BluetoothConnection(mac);

        try {
            printerConnection.open();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // edit.setText(edit.getText()+"\n"+"Abriendo Conección" );
                }
            });

            Log.i("cobranza_movil", "Abriendo Conección");
        } catch (final ConnectionException e) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // edit.setText(edit.getText()+"\n"+"Error de conexión, desconectando.  "+ e.getMessage() );
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
                        //edit.setText(edit.getText()+"\n"+"No se pudo establecer conexión"+ e.getMessage());
                    }
                });

                Log.e("cobranza_movil", "No se pudo establecer conexión");
                printer = null;
                disconnect();
            } catch (final ZebraPrinterLanguageUnknownException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //edit.setText(edit.getText()+"\n"+"No se pudo determinar el lenguaje de la impresora. " + e.getMessage());
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
                    // edit.setText(edit.getText()+"\n"+"Desconectando");
                }
            });

            if (printerConnection != null) {
                printerConnection.close();
            }
        } catch (final ConnectionException e) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // edit.setText(edit.getText()+"\n"+"Error de conexión - desconectado. "+ e.getMessage());
                }
            });

            Log.e("cobranza_movil", "Error de conexión - desconectado. " + e.getMessage());
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        final ArrayList<Entrega> Lista = new ArrayList<>(  );
        try{
        if (WebService.reto_AgregaFactura.equals( "ok" )) {
            //System.out.println("1");
            if(Utilidad.isNetworkAvailable()) {
                if (valorIntent.equals("VentaDirecta")) {
                    try {
                        WebService.listaProductos.clear();
                        params = new RequestParams();
                        params.put("latitud_actual", String.valueOf(WebService.lat_actual));
                        params.put("longitud_actual", String.valueOf(WebService.long_actual));
                        params.put("nro_orden", "0");
                        params.put("usuario", WebService.USUARIOLOGEADO);
                        params.put("latitud_destino", WebService.sucursalActual.getLatiud_Ubic());//3
                        params.put("longitud_destino", WebService.sucursalActual.getLongitud_Ubic());//4
                        params.put("nro_viaje", WebService.viajeSeleccionado.getNumViaje());//9
                        params.put("cod_sucursal", WebService.clienteActual.getCod_Tit_Gestion());//10
                        params.put("nro_orden", "0");//13
                        params.put("nom_cliente", WebService.clienteActual.getNom_Tit());//11
                        params.put("latitud_origen", String.valueOf(WebService.lat_origen));
                        params.put("longitud_origen", String.valueOf(WebService.long_origen));
                        params.put("en_pausa", "5");
                        params.put("distancia_a_recorrer", WebService.distancia_a_recorrer);
                        params.put("motivo_pausa", WebService.ultimaPausa);//2
                        ActualizarUbicacion task2 = new ActualizarUbicacion(new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) {
                                    Intent myIntent = new Intent(context, FacturaDirecta.class);
                                    startActivity(myIntent);
                            }
                        });
                        task2.execute();
                    }catch (Exception ex){
                        ex.printStackTrace();
                        //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                        Intent myIntent = new Intent(context, FacturaDirecta.class);
                        startActivity(myIntent);
                    }
                }else if(valorIntent.equals("NotaCredito")){
                        Intent myIntent = new Intent(context, BuscarFactura.class);
                        startActivity(myIntent);

                }else {
                    //listaEntregasHechas = new ArrayList<>();
                    listaEntregasHechas.add(WebService.Entrega_A_Realizar);
                    params = new RequestParams();
                    params.put("latitud_actual", String.valueOf(WebService.lat_actual));
                    params.put("longitud_actual", String.valueOf(WebService.long_actual));
                    params.put("nro_orden", "0");
                    params.put("usuario", WebService.USUARIOLOGEADO);
                    params.put("latitud_destino", String.valueOf(WebService.Entrega_A_Realizar.getLatiud_Ubic()));
                    params.put("longitud_destino", String.valueOf(WebService.Entrega_A_Realizar.getLongitud_Ubic()));
                    params.put("latitud_origen", String.valueOf(WebService.lat_origen));
                    params.put("longitud_origen", String.valueOf(WebService.long_origen));
                    params.put("nro_viaje", Integer.valueOf(WebService.viajeSeleccionado.getNumViaje()));
                    params.put("cod_sucursal", WebService.cod_sucu);
                    params.put("nom_cliente", WebService.nombreLocal.trim());
                    params.put("en_pausa", "5");
                    params.put("nro_orden", Integer.valueOf(WebService.nro_orden));
                    params.put("distancia_a_recorrer", WebService.distancia_a_recorrer);
                    params.put("motivo_pausa", WebService.ultimaPausa);//2
                    ActualizarUbicacion actuUbic = new ActualizarUbicacion(new AsyncResponse() {
                        public void processFinish(Object output) {
                            params1 = new RequestParams();
                            params1.put("nro_viaje", WebService.viajeSeleccionado.getNumViaje());
                            params1.put("seleccion", WebService.viajeSeleccionado.getTipo().equals("retiro") ? "r" : "e");
                            params1.put("username", WebService.USUARIOLOGEADO);
                            final VerEntrega task = new VerEntrega(new AsyncResponse() {
                                public void processFinish(Object output) {
                                    if (!WebService.errToken.equals("")) {
                                        Intent myIntent = new Intent(context, Login.class);
                                        startActivity(myIntent);
                                    } else {
                                        listaEntregas = new ArrayList<>();

                                        //crea el array auxiliar sacando las entregas anteriores
                                        for (int i = 0; i < entregasTraidas.size(); i++) {
                                            for (int j = 0; j < listaEntregasHechas.size(); j++) {
                                                if (entregasTraidas.get(i).getNro_Docum() != listaEntregasHechas.get(j).getNro_Docum()) {
                                                    Lista.add(entregasTraidas.get(i));
                                                }
                                            }
                                        }
                                        //crea el array de entregas del mismo cliente y sucursal
                                        for (Entrega objeto : Lista) {
                                            if (objeto.getCod_Sucursal().equals(WebService.Entrega_A_Realizar.getCod_Sucursal()) && objeto.getCod_Tit().equals(WebService.Entrega_A_Realizar.getCod_Tit())) {
                                                listaEntregas.add(objeto);
                                            }
                                        }
                                        if (listaEntregas.size() > 0) {
                                            WebService.EstadoAnterior = WebService.EstadoActual;
                                            //String prueba = listaEntregas.get( listaEntregas.size() - 1 ).getNro_Docum();
                                            String prueba = listaEntregas.get(0).getNro_Docum();
                                            //WebService.Entrega_A_Realizar = listaEntregas.get( listaEntregas.size() - 1 );
                                            WebService.Entrega_A_Realizar = listaEntregas.get(0);
                                            params = new RequestParams();
                                            params.put("num_Viaje", Integer.valueOf(WebService.viajeSeleccionado.getNumViaje()));
                                            params.put("num_Orden", Integer.valueOf(prueba));
                                            // params.put( "cod_Sucursal", listaEntregas.get( listaEntregas.size() - 1 ).getCod_Sucursal() );
                                            params.put("cod_Sucursal", listaEntregas.get(0).getCod_Sucursal());
                                            TraerItems task = new TraerItems(new AsyncResponse() {
                                                @Override
                                                public void processFinish(Object output) {
                                                    Intent myIntent = new Intent(context, Productos.class);
                                                    startActivity(myIntent);
                                                }
                                            });
                                            task.execute();
                                        } else {
                                            ParametrosCajas = new RequestParams();
                                            ParametrosCajas.add("cod_tit", WebService.Entrega_A_Realizar.getCod_Tit().trim());
                                            ParametrosCajas.add("cod_suc", WebService.cod_sucu.trim());
                                            ParametrosCajas.add("nro_trans", WebService.nro_trans_impresion);
                                            ParametrosCajas.add("user", WebService.USUARIOLOGEADO);
                                            WebService.nro_trans = Integer.valueOf(WebService.nro_trans);
                                            WebService.nFac.setCod_Sucursal(WebService.cod_sucu.trim());
                                            WebService.nFac.setNro_Trans(String.valueOf(nro_trans));
                                            WebService.nFac.setCod_Tit(WebService.Entrega_A_Realizar.getCod_Tit().trim());
                                            //GuardarFactura task3 = new GuardarFactura( new AsyncResponse() {
                                            // @Override
                                            //public void processFinish(Object output) {

                                            TreaerInfoCajas task = new TreaerInfoCajas(new Generados.AsyncResponse() {
                                                public void processFinish(Object output) {
                                                    if (!WebService.errToken.equals("")) {
                                                        Intent myIntent = new Intent(context, Login.class);
                                                        startActivity(myIntent);
                                                    } else {
                                                        TraerTipoCajas task2 = new TraerTipoCajas(new Generados.AsyncResponse() {
                                                            @Override
                                                            public void processFinish(Object output) {
                                                                if (WebService.usuarioActual.getEs_Cobranza().equals("S") && WebService.usuarioActual.getEs_Entrega().equals("S")) {
                                                                    TraerDeudores task2 = new TraerDeudores(new AsyncResponse() {
                                                                        @Override
                                                                        public void processFinish(Object output) {
                                                                            if(WebService.deudasViaje.size() > 0){
                                                                                Intent myIntent = new Intent(context, SeleccionarDeudas.class);
                                                                                myIntent.putExtra("Cajas", true);
                                                                                startActivity(myIntent);
                                                                            }else{
                                                                                Intent myIntent = new Intent(context, CajasTraidas.class);
                                                                                startActivity(myIntent);
                                                                            }

                                                                        }
                                                                    });
                                                                    task2.execute();
                                                                } else {
                                                                    Intent myIntent = new Intent(context, CajasTraidas.class);
                                                                    startActivity(myIntent);
                                                                }
                                                            }
                                                        });
                                                        task2.execute();
                                                    }
                                                }
                                            });
                                            task.execute();
                                        }
                                    }
                                }
                            });
                            task.execute();
                        }
                    });
                    actuUbic.execute();
                }
            } else {
                Utilidad.CargarToastConexion(context);
            }
        } else {
            retorno.setText( WebService.reto_AgregaFactura );
        }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Utilidad.vibraticionBotones( context );
        try{
            if(valorIntent.equals("VentaDirecta")){
                WebService.listaProductos.clear();
                Intent myIntent = new Intent(context, FacturaDirecta.class);
                startActivity(myIntent);
            }else if(valorIntent.equals("VentaDirectaProductoCono") || valorIntent.equals("RecorridoViaje")){
                //WebService.listaProductos.clear();
                Intent myIntent = new Intent(context, FacturaDirectaProductoCono.class);
                myIntent.putExtra("intent2", valorIntent);
                startActivity(myIntent);
            }else if(valorIntent.equals("NotaCredito")){
                WebService.listaProductos.clear();
                Intent myIntent = new Intent(context, NotaCredito.class);
                startActivity(myIntent);
            }else {
                Intent myIntent = new Intent(context, Productos.class);
                startActivity(myIntent);
                finish();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private class  TraerDeudores extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context);
        public AsyncResponse delegate = null;//Call back interface
        public TraerDeudores(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            if(Utilidad.isNetworkAvailable()) {
                RequestParams params1 = new RequestParams(  );
                WebService.clienteActual.setCod_Tit_Gestion(WebService.Entrega_A_Realizar.getCod_Tit_Gestion());
                WebService.clienteActual.setNom_Tit(WebService.Entrega_A_Realizar.getNom_Tit());
                params1.put( "cod_tit",WebService.clienteActual.getCod_Tit() );
                params1.put( "cod_empresa",WebService.usuarioActual.getEmpresa().trim() );
                params1.put("username", WebService.USUARIOLOGEADO.trim());
                WebService.TraerDeudas(params1);
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
        protected void onProgressUpdate(Void... values) { }
    }

    //interfase de clases async
    public interface AsyncResponse {
        void processFinish(Object output);
    }

    private class TraerTipoCajas extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public Generados.AsyncResponse delegate = null;//Call back interface
        public TraerTipoCajas(Generados.AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            // vj = new Viajes();
            if(Utilidad.isNetworkAvailable()) {
                params = new String[0];
                WebService.TraerTipoCajas("Consultas/Cajas.php");
                return null;

            } else {
                Toast toast=Toast.makeText(getApplicationContext(),getResources().getString( R.string.ErrorConexion ), Toast.LENGTH_SHORT);
                toast.setGravity( Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                toast.show();//showing the toast is important**

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
        protected void onProgressUpdate(Void... values) {}
    }

    private class TreaerInfoCajas extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public Generados.AsyncResponse delegate = null;//Call back interface
        public TreaerInfoCajas(Generados.AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            WebService.ObtengoSucursal( "Envases/ValidacionEnvase.php",ParametrosCajas );
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

    private class AgregarFactura extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... strings){
            WebService.IngresarFactura( "Facturas/IngresarFactura.php", paramsIng);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!WebService.errToken.equals("")) {
                Intent myIntent = new Intent(context, Login.class);
                startActivity(myIntent);
            }
        }
    }

    private class ValidarFactura extends AsyncTask<String,Void,Void> {
        public AsyncResponse delegate = null;//Call back interface
        public ValidarFactura(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... strings) {
            //params = new RequestParams(  );
            String metodo;
            if(valorIntent.equals("NotaCredito")){
                metodo = "Facturas/ValidarFacturaNC.php";
            }else {
                metodo = "Facturas/ValidarFactura.php";
            }
            WebService.validarFactura(metodo, params);
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

    private class  VerEntrega extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface
        public VerEntrega(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            if(Utilidad.isNetworkAvailable()) {
                WebService.ListaEntregasViaje(params1,"Viajes/Entregas.php");
                return null;
            }
            else {
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
            delegate.processFinish( WebService.logueado);
            Utilidad.deleteLoadingMessage();
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private class ActualizarUbicacion extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface

        public ActualizarUbicacion(AsyncResponse asyncResponse) {
            delegate = asyncResponse;
        }

        @Override
        protected Void doInBackground(String... strings) {
            WebService.ActualizarUbicacion( params, "Viajes/ActualizarUbicacion.php" );
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

    private class TraerItems extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface

        public TraerItems(AsyncResponse asyncResponse) {
            delegate = asyncResponse;
        }
        @Override
        protected Void doInBackground(String... strings) {
            WebService.TraerListaItems( params, "Viajes/TraerItems.php" );
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
            if(!WebService.errToken.equals("")){
                Intent myIntent = new Intent(context, Login.class);
                startActivity(myIntent);
            }
        }
    }

    private void noValido(){
        retorno.setText( "" );
        btnValidarFac.setText( "Validar factura" );
        facturaValida = false;
    }

}
