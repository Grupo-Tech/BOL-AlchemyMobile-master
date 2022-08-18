package com.example.user.trucksales.Visual.TruckSales;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.Item;
import com.example.user.trucksales.Encapsuladoras.Presentacion;
import com.example.user.trucksales.Negocio.DecimalDigitsInputFilter;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.FacturaDirecta.FacturaDirecta;
import com.example.user.trucksales.Visual.Login;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FacturaDirectaProductoCono extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ArrayAdapter<String> dataAdapterProducto;


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    private Utilidades Utilidad;
    ImageView atras;
    Context context;

    private ArrayList<TextView> ListaTextViews;
    Spinner spPresentacion;
    List<String> spinnerPresentArray = new ArrayList<>(  );

    public static String prodSelect;
    public static String prodSelectNom;
    public static String ivaSlect;
    public static String cantidadIngre;

    public double descuentoLin;

    Spinner spProductos;
    final List<String> spinnerProductosArray = new ArrayList<>(  );

    public List<String> NombreProducto =  new ArrayList<String>();
    public List<String> CodigosProducto = new ArrayList<>();
    List<EditText> LstDtoLinea = new ArrayList<>();
    public TextView tota,totaPedi,desc, iva5,iva10,TotSinIVA,TotalIVA,retorno;

    TextView nombreUsu,fecha, viaje, nombreClienteText, precioText,descuentoText, TituloPresent, cantidadTitle;
    AutoCompleteTextView nombreProd,codProd;

    TableLayout tablaProductos;

    Button btnagregar, btnFacturar;

    EditText cantidad,descuento, precio;

    public static String valorIntent;

    double neto;
    private static double Total = 0, TotalSinIva = 0, IVA5 = 0, IVA10 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_factura_directa_producto_cono);

        context = this;
        Utilidad = new Utilidades(context);

        GuardarDatosUsuario.Contexto = context;
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        try {
            try {
                valorIntent = getIntent().getStringExtra("intent2");
                if(valorIntent.equals("RecorridoViaje")){
                    WebService.clienteActual.setCod_Tit_Gestion(WebService.entregaDefault.getCod_Tit());
                    WebService.clienteActual.setNom_Tit(WebService.entregaDefault.getNom_Tit());
                    WebService.sucursalActual.setCod_Sucursal(WebService.entregaDefault.getCod_Sucursal());
                    WebService.sucursalActual.setNom_Sucursal(WebService.entregaDefault.getNom_Sucursal());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
            }

            ListaTextViews = new ArrayList<>();
            spPresentacion = (Spinner) findViewById(R.id.spPresentacion);
            spProductos= (Spinner) findViewById(R.id.spProductos);
            btnagregar = findViewById(R.id.btnagregar);

            btnFacturar = findViewById(R.id.btnFacturar);

            /*
            spProductos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    //String bancoAux = dataAdapterTarjeta.getItem(position);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            */

            nombreProd = findViewById(R.id.nombreProd);
            codProd = findViewById(R.id.codProd);

            cantidadTitle = findViewById(R.id.cantidadTitle);
            cantidad = findViewById(R.id.cantidad);

            precioText = findViewById(R.id.precioText);
            precio = findViewById(R.id.precio);

            descuentoText = findViewById(R.id.descuentoText);
            descuento = findViewById(R.id.descuento);


            atras = findViewById(R.id.btnAtras);
            nombreClienteText = findViewById(R.id.nombreCliente);

            tablaProductos = findViewById(R.id.tablaProductos);
            TituloPresent = findViewById(R.id.TituloPresent);

            cantidadTitle.setVisibility(View.GONE);
            cantidad.setVisibility(View.GONE);
            precioText.setVisibility(View.GONE);
            precio.setVisibility(View.GONE);
            descuentoText.setVisibility(View.GONE);
            descuento.setVisibility(View.GONE);

            spPresentacion.setVisibility(View.GONE);
            TituloPresent.setVisibility(View.GONE);

            if (WebService.usuarioActual != null) {
                if (Utilidad.isNetworkAvailable()) {
                    atras.setClickable(true);
                    atras.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try{
                                Utilidad.vibraticionBotones(context);
                                WebService.listaProductos.clear();
                                tablaProductos.removeAllViews();
                                Total = 0;
                                TotalSinIva = 0;
                                IVA5 = 0;
                                IVA10 = 0;
                                descuentoLin = 0;
                                Intent myIntent;
                                if(valorIntent.equals("RecorridoViaje")){
                                    WebService.Entrega_A_Realizar = WebService.entregaDefault;
                                    myIntent = new Intent(context, ClienteXDefecto.class);
                                }else {
                                    myIntent = new Intent(context, FacturaDirectaCono.class);
                                }
                                startActivity(myIntent);
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    });

                    if(WebService.listaProductos.size() > 0){
                        tablaProductos.removeAllViews();
                        Total = 0;
                        TotalSinIva = 0;
                        IVA5 = 0;
                        IVA10 = 0;
                        descuentoLin = 0;
                        Lista();
                    }

                    spPresentacion.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                    final ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, spinnerPresentArray);

                    spProductos.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                    dataAdapterProducto = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, spinnerProductosArray);


                    cantidad.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    cantidad.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                    cantidad.setGravity(Gravity.CENTER);

                    cantidad.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            try {
                                if (!cantidad.getText().equals("")) {

                                    String str = cantidad.getText().toString();
                                    if (str.isEmpty()) return;
                                    String str2 = Utilidad.PerfectDecimal(str, 10000, 2);
                                    if (!str2.equals(str)) {
                                        cantidad.setText(str2);
                                        int pos = cantidad.getText().length();
                                        cantidad.setSelection(pos);
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    final ArrayAdapter<String> adapterProd = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, NombreProducto);
                    final ArrayAdapter<String> adapterCodProd = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, CodigosProducto);

                    TraerProductos task01 = new TraerProductos(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            if (!WebService.errToken.equals("")) {
                                Intent myIntent = new Intent(context, Login.class);
                                startActivity(myIntent);
                            }else {
                                CargarSpinner();
                                nombreProd.setAdapter(adapterProd);
                                codProd.setAdapter(adapterCodProd);
                            }
                        }
                    });
                    task01.execute();

                    nombreProd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Object item = parent.getItemAtPosition(position);
                            Item instaProd = new Item();

                            boolean encontrado = false;
                            for (int i = 0; i < WebService.ArrayItemsProductos.size() && !encontrado; i++) {
                                instaProd = WebService.ArrayItemsProductos.get(i);
                                if (instaProd.getNom_Articulo().equals(item)) {
                                    encontrado = true;
                                    WebService.prodActual = instaProd;
                                    nombreProd.setText(instaProd.getNom_Articulo().trim());
                                    codProd.setText(instaProd.getCod_Articulo().trim());
                                    View view2 = getCurrentFocus();
                                    view2.clearFocus();
                                    if (view2 != null) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                                    }
                                }
                            }//FIN FOR

                            prodSelectNom = instaProd.getNom_Articulo().toString().trim();
                            prodSelect = instaProd.getCod_Articulo().toString().trim();
                            ivaSlect = instaProd.getPorc_Iva();

                            TraerPresentacion task = new TraerPresentacion(new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {
                                    try {
                                        if (!WebService.errToken.equals("")) {
                                            Intent myIntent = new Intent(context, Login.class);
                                            startActivity(myIntent);
                                        }else {
                                            if (WebService.listPresentacion.size() == 0) {
                                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.presentacionno), Toast.LENGTH_LONG).show();
                                                spPresentacion.setVisibility(View.GONE);
                                                TituloPresent.setVisibility(View.GONE);
                                                cantidad.setVisibility(View.GONE);
                                                cantidadTitle.setVisibility(View.GONE);
                                                precio.setVisibility(View.GONE);
                                                precioText.setVisibility(View.GONE);
                                                descuento.setVisibility(View.GONE);
                                                descuentoText.setVisibility(View.GONE);
                                            } else {
                                                spPresentacion.setVisibility(View.VISIBLE);
                                                TituloPresent.setVisibility(View.VISIBLE);
                                                cantidad.setVisibility(View.VISIBLE);
                                                cantidadTitle.setVisibility(View.VISIBLE);
                                                spinnerPresentArray.clear();
                                                for (int i = 0; i < WebService.listPresentacion.size(); i++) {
                                                    String nombreAgregar1 = WebService.listPresentacion.get(i).getNom_unidad().trim();
                                                    spinnerPresentArray.add(nombreAgregar1);
                                                }
                                                dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spPresentacion.setAdapter(dataAdapter2);

                                                Present();
                                            }
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            task.execute();
                        }
                    });

                    nombreProd.setOnEditorActionListener(new EditText.OnEditorActionListener() {
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

                    codProd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            spPresentacion.setVisibility(View.VISIBLE);
                            TituloPresent.setVisibility(View.VISIBLE);
                            cantidad.setVisibility(View.VISIBLE);
                            cantidadTitle.setVisibility(View.VISIBLE);
                            precioText.setVisibility(View.VISIBLE);
                            precio.setVisibility(View.VISIBLE);
                            descuento.setText("0");
                            if(WebService.configuracion.getDto_linea().equals("S")){
                                descuentoText.setVisibility(View.VISIBLE);
                                descuento.setVisibility(View.VISIBLE);
                            }else{
                                descuentoText.setVisibility(View.GONE);
                                descuento.setVisibility(View.GONE);
                            }


                            Object item = parent.getItemAtPosition(position);
                            Item instaProd = new Item();
                            boolean encontrado = false;
                            for (int i = 0; i < WebService.ArrayItemsProductos.size() && !encontrado; i++) {
                                instaProd = WebService.ArrayItemsProductos.get(i);
                                if (instaProd.getCod_Articulo().trim().equals(item)) {
                                    WebService.prodActual = instaProd;
                                    encontrado = true;
                                    nombreProd.setText(instaProd.getNom_Articulo().trim());
                                    View view2 = getCurrentFocus();
                                    view2.clearFocus();
                                    if (view2 != null) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                                    }
                                }
                            }//FIN FOR

                            prodSelectNom = instaProd.getNom_Articulo().toString().trim();
                            prodSelect = instaProd.getCod_Articulo().toString().trim();
                            ivaSlect = instaProd.getPorc_Iva();

                            TraerPresentacion task = new TraerPresentacion(new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {
                                    try {
                                        if (!WebService.errToken.equals("")) {
                                            Intent myIntent = new Intent(context, Login.class);
                                            startActivity(myIntent);
                                        }else {
                                            if (WebService.listPresentacion.size() == 0) {
                                                Toast.makeText(getApplicationContext(), "El artículo ingresado no cuenta con presentación, seleccione otro artículo", Toast.LENGTH_LONG).show();
                                                spPresentacion.setVisibility(View.GONE);
                                                TituloPresent.setVisibility(View.GONE);
                                                cantidad.setVisibility(View.GONE);
                                                cantidadTitle.setVisibility(View.GONE);
                                                precio.setVisibility(View.GONE);
                                                precioText.setVisibility(View.GONE);

                                                descuento.setVisibility(View.GONE);
                                                descuentoText.setVisibility(View.GONE);

                                            } else {
                                                spinnerPresentArray.clear();
                                                for (int i = 0; i < WebService.listPresentacion.size(); i++) {
                                                    String nombreAgregar1 = WebService.listPresentacion.get(i).getNom_unidad().trim();
                                                    spinnerPresentArray.add(nombreAgregar1);
                                                }
                                                dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spPresentacion.setAdapter(dataAdapter2);

                                                Present();
                                            }
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            task.execute();
                        }
                    });
                    codProd.setOnEditorActionListener(new EditText.OnEditorActionListener() {
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

                    spPresentacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                String presentSelect = spPresentacion.getSelectedItem().toString();
                                Presentacion instanciaPr = new Presentacion();
                                for (int x = 0; x < WebService.listPresentacion.size(); x++) {
                                    instanciaPr = WebService.listPresentacion.get(x);
                                    if (instanciaPr.getNom_unidad().trim().equals(presentSelect.trim())) {
                                        WebService.presentacionActual = instanciaPr;
                                        break;
                                    }
                                }//FIN FOR
                                precio.setVisibility(View.VISIBLE);
                                precioText.setVisibility(View.VISIBLE);
                                precio.setText(String.valueOf(WebService.presentacionActual.getPrecio()));


                                descuento.setVisibility(View.VISIBLE);
                                descuentoText.setVisibility(View.VISIBLE);
                                descuento.setText("0");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    spProductos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                String productoSelect = spProductos.getSelectedItem().toString();

                                for (int i = 0; i < WebService.ArrayItemsProductos.size(); i++) {
                                    String nombreAgregar = WebService.ArrayItemsProductos.get(i).getNom_Articulo();
                                   // NombreProducto.add(nombreAgregar);
                                    String codigoAgregar = WebService.ArrayItemsProductos.get(i).getCod_Articulo().trim();
                                   // CodigosProducto.add(codigoAgregar);

                                    if(productoSelect.equals(nombreAgregar)){
                                        codProd.setText(codigoAgregar);
                                        nombreProd.setText(nombreAgregar);
                                    }
                                }

                                /*
                                Presentacion instanciaPr = new Presentacion();
                                for (int x = 0; x < WebService.listPresentacion.size(); x++) {
                                    instanciaPr = WebService.listPresentacion.get(x);
                                    if (instanciaPr.getNom_unidad().trim().equals(presentSelect.trim())) {
                                        WebService.presentacionActual = instanciaPr;
                                        break;
                                    }
                                }//FIN FOR
                                precio.setVisibility(View.VISIBLE);
                                precioText.setVisibility(View.VISIBLE);
                                precio.setText(String.valueOf(WebService.presentacionActual.getPrecio()));
                                */

                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    btnagregar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(context);
                            cantidadIngre = cantidad.getText().toString();
                            ValidarCantidad task = new ValidarCantidad(new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {
                                        if (!WebService.retorno_stock.equals("Ok")) {
                                            if(WebService.cantidadString.equals("0")){
                                                Toast.makeText(getApplicationContext(), "Producto no disponible", Toast.LENGTH_LONG).show();
                                            }else{
                                                Toast.makeText(getApplicationContext(), "El stock disponible es "+WebService.cantidadString, Toast.LENGTH_LONG).show();
                                            }

                                        } else {
                                            if (Utilidad.isNetworkAvailable()) {
                                                try {
                                                    if (codProd.getText().toString().equals("")) {
                                                        Toast.makeText(getApplicationContext(), "Debe ingresar el codigo del producto", Toast.LENGTH_LONG).show();
                                                    } else if (spPresentacion.getSelectedItem().toString().equals("")) {
                                                        Toast.makeText(getApplicationContext(), "Debe ingresar la presentacion", Toast.LENGTH_LONG).show();
                                                    } else if (cantidad.getText().toString().equals("")) {
                                                        Toast.makeText(getApplicationContext(), "Debe ingresar la cantidad", Toast.LENGTH_LONG).show();
                                                    }else if(WebService.configuracion.getDto_linea().equals("S") && descuento.getText().toString().equals("")){
                                                        Toast.makeText(getApplicationContext(), "Debe ingresar el descuento", Toast.LENGTH_LONG).show();
                                                    } else {

                                                        String cantidad2 = cantidad.getText().toString();
                                                        String precio2 = precio.getText().toString();
                                                        String descuento2 = descuento.getText().toString();
                                                        double subTot = Double.parseDouble(cantidad2) * (Double.parseDouble(precio2)-(Double.parseDouble(descuento2)*Double.parseDouble(precio2)/100));
                                                        String codPresent = spPresentacion.getSelectedItem().toString();

                                                        for (int i = 0; i < WebService.listPresentacion.size(); i++) {
                                                            if (WebService.listPresentacion.get(i).getNom_unidad().trim().equals(codPresent.trim())) {
                                                                codPresent = WebService.listPresentacion.get(i).getCod_unidad().trim();
                                                                break;
                                                            }
                                                        }

                                                        WebService.addProductoDescuento(new Item(prodSelectNom, prodSelect, Double.parseDouble(cantidad2), Double.parseDouble(precio2), ivaSlect, subTot, codPresent, Double.parseDouble(descuento2)));


                                                        tablaProductos.removeAllViews();
                                                        Total = 0;
                                                        TotalSinIva = 0;
                                                        IVA5 = 0;
                                                        IVA10 = 0;
                                                        descuentoLin = 0;

                                                        Lista();

                                                        codProd.setText("");
                                                        nombreProd.setText("");
                                                        cantidad.setText("");
                                                        precio.setVisibility(View.GONE);
                                                        precioText.setVisibility(View.GONE);
                                                        spPresentacion.setVisibility(View.GONE);
                                                        TituloPresent.setVisibility(View.GONE);
                                                        cantidad.setVisibility(View.GONE);
                                                        cantidadTitle.setVisibility(View.GONE);
                                                        descuento.setVisibility(View.GONE);
                                                        descuentoText.setVisibility(View.GONE);
                                                        spinnerPresentArray.clear();
                                                        prodSelectNom = "";
                                                        prodSelect = "";
                                                    }
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }
                                }
                            });
                            task.execute();
                        }
                    });



                    btnFacturar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(context);
                            if (Utilidad.isNetworkAvailable()) {
                                try {
                                    ObtenerNumRecomendado task = new ObtenerNumRecomendado(new AsyncResponse() {
                                        @Override
                                        public void processFinish(Object output) {
                                            if (!WebService.errToken.equals("")) {
                                                Intent myIntent = new Intent(context, Login.class);
                                                startActivity(myIntent);
                                            } else {
                                                WebService.ArrayItemsViaje = WebService.listaProductos;
                                                WebService.Entrega_A_Realizar.setNro_Docum("0");
                                                WebService.Entrega_A_Realizar.setNro_doc_ref(0);
                                                WebService.TotalFactura = WebService.totalProductos;
                                                WebService.cod_sucu = WebService.sucursalActual.getCod_Sucursal().trim();
                                                /*WebService.viajeSeleccionado = new Viaje();
                                                WebService.viajeSeleccionado.setNumViaje("123");*/
                                                Intent myIntent = new Intent(context, factura.class);
                                                if(valorIntent.equals("RecorridoViaje")){
                                                    WebService.cod_sucu = WebService.Entrega_A_Realizar.getCod_Sucursal();
                                                    myIntent.putExtra("intent", "RecorridoViaje");
                                                }else{
                                                    WebService.Entrega_A_Realizar.setCod_Sucursal(WebService.sucursalActual.getCod_Sucursal().trim());
                                                    WebService.Entrega_A_Realizar.setNom_Sucursal(WebService.sucursalActual.getNom_Sucursal().trim());
                                                    WebService.Entrega_A_Realizar.setCod_Tit_Gestion(WebService.clienteActual.getCod_Tit_Gestion());
                                                    WebService.Entrega_A_Realizar.setCod_Tit(WebService.clienteActual.getCod_Tit_Gestion());
                                                    WebService.Entrega_A_Realizar.setNom_Tit(WebService.clienteActual.getNom_Tit());
                                                    myIntent.putExtra("intent", "VentaDirectaProductoCono");
                                                }
                                                startActivity(myIntent);
                                            }
                                        }
                                    });
                                    task.execute();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Utilidad.CargarToastConexion(context);
                            }
                        }
                    });
                } else {
                    Utilidad.CargarToastConexion(context);
                }
            } else {
                Intent myIntent = new Intent(context, Login.class);
                startActivity(myIntent);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void CargarSpinner(){
        try {
            for (int i = 0; i < WebService.ArrayItemsProductos.size(); i++) {
                String nombreAgregar = WebService.ArrayItemsProductos.get(i).getNom_Articulo();
                NombreProducto.add(nombreAgregar);
                String codigoAgregar = WebService.ArrayItemsProductos.get(i).getCod_Articulo().trim();
                CodigosProducto.add(codigoAgregar);

                spinnerProductosArray.add(nombreAgregar);
            }

            dataAdapterProducto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spProductos.setAdapter(dataAdapterProducto);

        }catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    private class TraerProductos extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface
        public TraerProductos(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            if(Utilidad.isNetworkAvailable()) {
                RequestParams params1 = new RequestParams();
                params1.add("nro_viaje",WebService.viajeSeleccionado.getNumViaje());
                WebService.TraerProductos(params1, "VentasDirectas/TraerProductos.php");
                return null;

            } else {
                Utilidad.CargarToastConexion(context);
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
        protected void onProgressUpdate(Void... values) {}
    }

    private class TraerPresentacion extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface
        public TraerPresentacion(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            if(Utilidad.isNetworkAvailable()) {
                RequestParams params1 = new RequestParams();
                params1.add("cod_tit", WebService.clienteActual.getCod_Tit_Gestion());
                params1.add("cod_sucursal", WebService.sucursalActual.getCod_Sucursal().trim());
                params1.add("cod_art", WebService.prodActual.getCod_Articulo().trim());
                params1.add("cod_deposito", WebService.viajeSeleccionado.getNumViaje());
                WebService.TraerPresentacion("VentasDirectas/TraerPresentacion_Nuevo_pedidos.php", params1);
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

    private class ValidarCantidad extends AsyncTask<String,Void,Void> {
        public AsyncResponse delegate = null;//Call back interface
        public ValidarCantidad(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... strings) {
            if (!WebService.prodActual.getCod_Articulo().equals(null)) {
                RequestParams params = new RequestParams();
                params.add("cantidad", cantidadIngre);
                params.add("num_viaje", WebService.viajeSeleccionado.getNumViaje());
                params.add("cod_art", WebService.prodActual.getCod_Articulo().trim());
                params.add("cod_present", WebService.presentacionActual.getCod_unidad().trim());
                WebService.ValidarCantidad("VentasDirectas/ValidarCantidad.php", params);
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
                params2.put("username", WebService.usuarioActual.getNombre());
                WebService.RecomendarNumero("Facturas/RecomendarNumero.php", params2);
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



    public void Lista() {
        try {
            TableLayout tr0 = new TableLayout(this);
            TableRow tr1 = new TableRow(this);
            TableRow tr2 = new TableRow(this);
            TableRow tr3 = new TableRow(this);
            TableRow tr6 = new TableRow(this);
            TableRow tr7 = new TableRow(this);

            tr1 = new TableRow(this);

            //Aca cargamos los titulos
            TextView titCant = new TextView(this);
            titCant.setText(getResources().getString(R.string.CantidadPrdo));
            titCant.setTextColor(Color.parseColor("#ffffff"));
            titCant.setPadding(5, 10, 10, 10);

            TextView titPrecioUnitario = new TextView(this);
            titPrecioUnitario.setText(getResources().getString(R.string.UnitProd));
            titPrecioUnitario.setTextColor(Color.parseColor("#ffffff"));
            titPrecioUnitario.setPadding(10, 10, 22, 10);
            titPrecioUnitario.setGravity(Gravity.RIGHT);

            TextView titIva = new TextView(this);
            titIva.setText(getResources().getString(R.string.IVAProd));
            titIva.setTextColor(Color.parseColor("#ffffff"));
            titIva.setPadding(10, 10, 22, 10);
            titIva.setGravity(Gravity.RIGHT);

            TextView titDtoLinea = new TextView(this);
            titDtoLinea.setText(getResources().getString(R.string.Dto));
            titDtoLinea.setTextColor(Color.parseColor("#ffffff"));
            //titPrecioUnitario.setPadding(30, 10, 5, 10);
            titDtoLinea.setPadding(30, 10, 22, 10);
            titDtoLinea.setGravity(Gravity.RIGHT);


            TextView titSubTotal = new TextView(this);
            titSubTotal.setText(getResources().getString(R.string.SubTotProd));
            titSubTotal.setTextColor(Color.parseColor("#ffffff"));
            titSubTotal.setGravity(Gravity.RIGHT);
            titSubTotal.setPadding(10, 10, 22, 10);

            tr1.addView(titCant);
            tr1.addView(titPrecioUnitario);
            tr1.addView(titIva);
            if(WebService.configuracion.getDto_linea().equals("S")){
                tr1.addView(titDtoLinea);
            }
            tr1.addView(titSubTotal);
            tr1.setBackgroundResource(R.color.colorPrimary);
            //Aca Terminamos de cargar los titulos

            tablaProductos.addView(tr1);

            for (int i = 0; WebService.listaProductos.size() > i; i++) {

                final Item instaProd = WebService.listaProductos.get(i);
                instaProd.setCantidad_entregada(instaProd.getCantidad());

                tr0 = new TableLayout(this);
                tr6 = new TableRow(this);
                tr2 = new TableRow(this);
                tr7 = new TableRow(this);

                if(i!=0){
                    int ind = 1;
                    while (ind <= 5) {
                        TextView relleno = new TextView(this);
                        relleno.setText("");
                        relleno.setTextSize(1);
                        relleno.setPadding(5, 2, 10, 2);
                        tr7.addView(relleno);
                        tr7.setBackgroundResource(R.color.colorPrimary);
                        ind++;
                    }
                    tablaProductos.addView(tr7);
                }

                final ImageView quitar = new ImageView(context);
                quitar.setImageResource(R.drawable.eliminar);
                quitar.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                quitar.setPadding(5, 5, 0, 5);

                quitar.getLayoutParams().height = 50;
                quitar.getLayoutParams().width = 50;
                quitar.requestLayout();

                TextView nombreItem = new TextView(this);
                String nombItem = instaProd.getNom_Articulo().trim();
                String codArt = instaProd.getCod_Articulo().trim();

                nombreItem.setText(nombItem + " - " + codArt);
                nombreItem.setTextSize(12);

                nombreItem.setTextColor(Color.parseColor("#000000"));
                nombreItem.setPadding(10, 5, 0, 10);

                tr6.addView(quitar);
                tr6.addView(nombreItem);

                tr0.addView(tr6);

                quitar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebService.removeProductoDescuento(instaProd);
                        tablaProductos.removeAllViews();
                        Total = 0;
                        TotalSinIva = 0;
                        IVA5 = 0;
                        IVA10 = 0;
                        descuentoLin = 0;
                        Lista();
                    }
                });

                //Aca cargamos los productos
                final TextView cantidadPedida = new TextView(this);
                cantidadPedida.setTextSize(12);
                ListaTextViews.add(cantidadPedida);
                cantidadPedida.setTextColor(Color.parseColor("#000000"));
                cantidadPedida.setText(Double.toString(instaProd.getCantidad()));
                cantidadPedida.setGravity(Gravity.LEFT);

                final TextView PrecioUnitario = new TextView(this);
                PrecioUnitario.setText(Utilidad.GenerarFormato((int) Math.floor(instaProd.getPrecio_Unitario())));
                PrecioUnitario.setGravity(Gravity.RIGHT);
                PrecioUnitario.setTextSize(12);
                PrecioUnitario.setTextColor(Color.parseColor("#000000"));
                ListaTextViews.add(PrecioUnitario);

                /*
                final EditText dtoLinea = new EditText(this);
                dtoLinea.setText(String.valueOf(instaProd.getPorc_desc()));
                dtoLinea.setEnabled(false);
                //dtoLinea.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                dtoLinea.setInputType(InputType.TYPE_CLASS_NUMBER);
                // dtoLinea.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "100")});
                dtoLinea.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(3,2)});
                dtoLinea.setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));
                dtoLinea.setGravity(Gravity.CENTER);
                ListaTextViews.add(dtoLinea);
                */
                final TextView dtoLinea = new TextView(this);
                ListaTextViews.add(dtoLinea);
                dtoLinea.setText(Double.toString(instaProd.getDtoLinea()));
                dtoLinea.setInputType(InputType.TYPE_CLASS_NUMBER);
                dtoLinea.setGravity(Gravity.CENTER);


                final TextView subtotal = new TextView(this);
                ListaTextViews.add(subtotal);

                TextView iva = new TextView(this);
                ListaTextViews.add(iva);
                double valorIva = Double.valueOf(WebService.listaProductos.get(i).getPorc_Iva());
                int iv = (int) valorIva;
                iva.setText("     " + iv);
                iva.setGravity(Gravity.RIGHT);
                iva.setTextSize(12);
                iva.setTextColor(Color.parseColor("#000000"));
                iva.setPadding(5, 0, 5, 0);

                double sub_tot = Math.floor(instaProd.getCantidad() * (instaProd.getPrecio_Unitario()-(instaProd.getDtoLinea()*instaProd.getPrecio_Unitario()/100)));

                descuentoLin = instaProd.getDtoLinea() + descuentoLin;
                if (Double.parseDouble(instaProd.getPorc_Iva()) == 5.000) {
                    neto = Double.valueOf(sub_tot) / 1.05;
                    IVA5 = IVA5 + Double.valueOf(neto * 0.05);
                    WebService.IVA5 = IVA5;
                    WebService.Gravada5 = WebService.Gravada5 + Double.valueOf(sub_tot);
                } else {
                    neto = Double.valueOf(sub_tot) / 1.10;
                    IVA10 = IVA10 + (Double.valueOf(neto * 0.10));
                    WebService.IVA10 = IVA10;
                    WebService.Gravada10 = IVA10;
                }

                subtotal.setText(Utilidad.GenerarFormato((int) sub_tot));
                subtotal.setGravity(Gravity.RIGHT);
                subtotal.setTextSize(12);
                subtotal.setTextColor(Color.parseColor("#000000"));
                //Aca cargamos el IVA para mostrar

                tr2.addView(cantidadPedida);
                tr2.addView(PrecioUnitario);
                tr2.addView(iva);
                if(WebService.configuracion.getDto_linea().equals("S")){
                    tr2.addView(dtoLinea);
                }
                tr2.addView(subtotal);
                tr2.setTag(i);
                tablaProductos.addView(tr0);
                tablaProductos.addView(tr2);

                if (WebService.listaProductos.size() == i + 1) {
                    TableRow tr01 = new TableRow(this);
                    tr1 = new TableRow(this);
                    tr2 = new TableRow(this);
                    tr3 = new TableRow(this);
                    TableRow tr4 = new TableRow(this);
                    TableRow tr5 = new TableRow(this);

                    desc = new TextView(this);
                    ListaTextViews.add(desc);
                    desc.setTextColor(Color.parseColor("#ffffff"));
                    desc.setText(getResources().getString(R.string.Descuento) + " " + Utilidad.GenerarFormato((int) descuentoLin));
                    tr2.setGravity(Gravity.RIGHT);
                    tr2.setPadding(10, 10, 10, 10);
                    tr2.addView(desc);
                    tr2.setBackgroundResource(R.color.Iva);
                    tablaProductos.addView(tr2);

                    tota = new TextView(this);
                    ListaTextViews.add(tota);
                    tota.setTextColor(Color.parseColor("#ffffff"));
                    tota.setText(getResources().getString(R.string.TotalCaja) + " " + Utilidad.GenerarFormato2(WebService.totalProductos));
                    tr01.setGravity(Gravity.RIGHT);
                    tr01.setPadding(10, 10, 10, 10);
                    tr01.addView(tota);
                    tr01.setBackgroundResource(R.color.colorPrimary);
                    tablaProductos.addView(tr01);

                   /* iva5 = new TextView(this);
                    ListaTextViews.add(iva5);
                    iva5.setTextColor(Color.parseColor("#ffffff"));
                    iva5.setText(getResources().getString(R.string.ImpuestoIVA5) + " " + Utilidad.GenerarFormato((int) WebService.IVA5));
                    tr2.setGravity(Gravity.RIGHT);
                    tr2.setPadding(10, 10, 10, 10);
                    tr2.addView(iva5);
                    tr2.setBackgroundResource(R.color.Iva);
                    tablaProductos.addView(tr2);

                    iva10 = new TextView(this);
                    ListaTextViews.add(iva10);
                    iva10.setTextColor(Color.parseColor("#ffffff"));
                    iva10.setText(getResources().getString(R.string.ImpusetoIVA10) + " " + Utilidad.GenerarFormato((int) WebService.IVA10));
                    tr3.setGravity(Gravity.RIGHT);
                    tr3.setPadding(10, 10, 10, 10);
                    tr3.addView(iva10);
                    tr3.setBackgroundResource(R.color.Iva);
                    tablaProductos.addView(tr3);

                    TotalIVA = new TextView(this);
                    ListaTextViews.add(TotalIVA);
                    TotalIVA.setTextColor(Color.parseColor("#ffffff"));
                    int valorIVA5 = (int) WebService.IVA5;
                    int valorIVA10 = (int) WebService.IVA10;
                    TotalIVA.setText(getResources().getString(R.string.TotIVA) + " " + Utilidad.GenerarFormato((valorIVA5 + valorIVA10)));
                    tr4.setGravity(Gravity.RIGHT);
                    tr4.setPadding(10, 10, 10, 10);
                    tr4.addView(TotalIVA);
                    tr4.setBackgroundResource(R.color.Iva);
                    tablaProductos.addView(tr4);

                    TotSinIVA = new TextView(this);
                    ListaTextViews.add(TotSinIVA);
                    TotSinIVA.setTextColor(Color.parseColor("#ffffff"));
                    TotSinIVA.setText(getResources().getString(R.string.TotSinIVAProd) + " " + Utilidad.GenerarFormato2((WebService.totalProductos - ((int) WebService.IVA5 + (int) WebService.IVA10))));
                    tr5.setGravity(Gravity.RIGHT);
                    tr5.setPadding(10, 10, 10, 10);
                    tr5.addView(TotSinIVA);
                    tr5.setBackgroundResource(R.color.colorPrimary);
                    tablaProductos.addView(tr5);*/
                    //}
                }
                //}
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
        }

        Utilidad.SetFontSizeTextView(ListaTextViews, this);
    }

    public void Present(){
        spPresentacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String presentSelect = spPresentacion.getSelectedItem().toString();
                    Presentacion instanciaPr = new Presentacion();
                    for (int x = 0; x < WebService.listPresentacion.size(); x++) {
                        instanciaPr = WebService.listPresentacion.get(x);
                        if (instanciaPr.getNom_unidad().trim().equals(presentSelect.trim())) {
                            WebService.presentacionActual = instanciaPr;
                            break;
                        }
                    }//FIN FOR
                    precio.setVisibility(View.VISIBLE);
                    precioText.setVisibility(View.VISIBLE);
                    precio.setText(String.valueOf(WebService.presentacionActual.getPrecio()));
                    descuento.setVisibility(View.VISIBLE);
                    descuentoText.setVisibility(View.VISIBLE);
                    descuento.setText("0");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}
