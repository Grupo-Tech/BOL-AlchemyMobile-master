package com.example.user.trucksales.Visual.Pedidos;

import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import com.example.user.trucksales.Encapsuladoras.Pedido;
import com.example.user.trucksales.Encapsuladoras.Presentacion;
import com.example.user.trucksales.Encapsuladoras.ProductosItem;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;

import com.example.user.trucksales.Visual.TruckSales.ClienteXDefecto;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ProductosPedidos extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    private static EditText input, inputDto;
    RequestParams params = new RequestParams();
    private Utilidades Utilidad;
    ImageView atras;
    LinearLayout mainLayout;
    Context context;
    public static String valorIntent;
    public double descuentoLin;

    String cantidadIng = "";
    String descIng = "";

    private ArrayList<TextView> ListaTextViews;
    public TextView tota, totaPedi,iva5,iva10,retorno;

    TextView nombreUsu,fecha, dtoText, totalText, precioText, TituloPresent, cantidadTitle, adicional, total, cliSelect;
    AutoCompleteTextView nombreProd,codProd;

    Button btnagregar, btnFacturar;
    EditText cantidad, dto, precio;

    public int decimales = 1;
    public int decimalesCant = 0;

    Spinner spPresentacion;
    List<String> spinnerPresentArray = new ArrayList<>(  );

    TableLayout tablaProductos;

    public static String prodSelect;
    public static String depoSelect;
    public static String prodSelectNom;
    public static String cantidadIngre;

    public List<String> NombreProducto =  new ArrayList<String>();
    public List<String> CodigosProducto = new ArrayList<>();

    JSONObject postData;
    RequestParams param;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pedidos_productos);

        context = this;
        Utilidad = new Utilidades(context);

        GuardarDatosUsuario.Contexto = context;
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        try {
            decimalesCant = Integer.parseInt(WebService.configuracion.getDec_cant());
            tablaProductos = findViewById(R.id.tablaProductos);
            cliSelect = findViewById(R.id.cliSelect);
            cliSelect.setText(WebService.pedido.getNom_tit());
            atras = findViewById(R.id.btnAtras);
            nombreUsu = findViewById(R.id.LblUsu);
            nombreUsu.setText(WebService.USUARIOLOGEADO);
            fecha = (TextView) findViewById(R.id.LblFecha);
            String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
            fecha.setText(timeStamp);
            nombreProd = findViewById(R.id.nombreProd);
            codProd = findViewById(R.id.codProd);
            adicional = findViewById(R.id.adicional);
            adicional.setText("");
            precio = findViewById(R.id.precio);
            precioText = findViewById(R.id.precioText);
            ListaTextViews = new ArrayList<>();
            spPresentacion = (Spinner) findViewById(R.id.spPresentacion);
            TituloPresent = findViewById(R.id.TituloPresent);
            cantidadTitle = findViewById(R.id.cantidadTitle);
            cantidad = findViewById(R.id.cantidad);
            btnagregar = findViewById(R.id.btnagregar);
            btnFacturar = findViewById(R.id.btnFacturar);
            if(WebService.listaProductosPedidos.size() == 0){
                btnFacturar.setEnabled(false);
            }
            dtoText = findViewById(R.id.dtoText);
            totalText = findViewById(R.id.totalText);
            tota = findViewById(R.id.total);
            dto = findViewById(R.id.dto);
            total = findViewById(R.id.total);

            //Se ocultan hasta que se seleccione un producto
            precio.setVisibility(View.GONE);
            precioText.setVisibility(View.GONE);
            spPresentacion.setVisibility(View.GONE);
            TituloPresent.setVisibility(View.GONE);
            cantidad.setVisibility(View.GONE);
            cantidadTitle.setVisibility(View.GONE);
            dtoText.setVisibility(View.GONE);
            totalText.setVisibility(View.GONE);
            dto.setVisibility(View.GONE);
            total.setVisibility(View.GONE);

            cantidad.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    cantidad.removeTextChangedListener(this);

                    try {
                        String originalString = s.toString();

                        Long longval;
                        if (originalString.contains(",")) {
                            originalString = originalString.replaceAll(",", "");
                        }
                        if (originalString.contains(".") && decimalesCant == 0) {
                            originalString = originalString.replace(".", "");
                        }
                        longval = Long.parseLong(originalString);

                        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);

                        //DecimalFormat formatter = new DecimalFormat("#,###,###,###", new DecimalFormatSymbols(new Locale("pt", "BR")));
                        formatter.applyPattern("#,###,###,###.##");
                        String formattedString = formatter.format(longval);

                        //setting text after format to EditText
                        cantidad.setText(formattedString);
                        cantidad.setSelection(cantidad.getText().length());

                        double desc = 0;
                        double Precio_con_descuento = 0;
                        double totalP = 0;
                        double cant = Double.parseDouble(cantidad.getText().toString());
                        if(dto.getText().toString().equals("0")){
                            totalP = cant*WebService.presentacionActualPedido.getPrecio();
                        }else{
                            desc  = Double.parseDouble(dto.getText().toString());
                            Precio_con_descuento = WebService.presentacionActualPedido.getPrecio()*(1-(desc/100));
                            totalP = cant*Precio_con_descuento;
                        }
                        total.setText(Utilidad.PerfectDecimal(String.valueOf(totalP), 10000000, 2));


                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    }

                    cantidad.addTextChangedListener(this);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                       String str = cantidad.getText().toString();
                        if (str.isEmpty()) {
                            double totalP = 0;
                            total.setText(Utilidad.PerfectDecimal(String.valueOf(totalP), 10000000, 2));
                            return;
                        }
                        if (decimalesCant == 0) {
                            str = str.replace(".", "");
                            //String str2 = Utilidad.PerfectDecimal(str, 1000, decimalesCant);
                            //cantidad.setText(NumberFormat.getInstance(Locale.ITALY).format(str2).toString());
                        }
                        String str2 = Utilidad.PerfectDecimal(str, 1000, decimalesCant);
                        if (!str2.equals(str)) {
                            if (decimalesCant == 1) {
                                cantidad.setText(str2);
                            } else {
                                cantidad.setText(NumberFormat.getInstance(Locale.ITALY).format(str2).toString());
                            }
                            int pos = cantidad.getText().length();
                            cantidad.setSelection(pos);
                        }
                        } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            dto.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    dto.removeTextChangedListener(this);

                    try {
                        String originalString = s.toString();

                        Long longval;
                        if (originalString.contains(",")) {
                            originalString = originalString.replaceAll(",", "");
                        }
                        longval = Long.parseLong(originalString);

                        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);

                        //DecimalFormat formatter = new DecimalFormat("#,###,###,###", new DecimalFormatSymbols(new Locale("pt", "BR")));
                        formatter.applyPattern("#,###,###,###.##");
                        String formattedString = formatter.format(longval);

                        //setting text after format to EditText
                        dto.setText(formattedString);
                        dto.setSelection(dto.getText().length());

                        double desc = Double.parseDouble(dto.getText().toString());
                        double Precio_con_descuento = 0;
                        double totalP = 0;
                        double cant = Double.parseDouble(cantidad.getText().toString());
                        if(cant == 0 || desc == 0){
                            totalP = WebService.presentacionActualPedido.getPrecio();
                        }else{
                            Precio_con_descuento = WebService.presentacionActualPedido.getPrecio()*(1-(desc/100));
                            totalP = cant*Precio_con_descuento;
                        }
                        total.setText(Utilidad.PerfectDecimal(String.valueOf(totalP), 10000000, 2));
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    }

                    dto.addTextChangedListener(this);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        String str = dto.getText().toString();
                        if (str.isEmpty()) {
                            return;
                        }
                        if (decimales == 1) {
                            str.replace(".", "");
                        }
                        String str2 = Utilidad.PerfectDecimal(str, 1000, decimales);
                        if (!str2.equals(str)) {
                            if (decimales == 1) {
                                dto.setText(str2);
                            } else {
                                dto.setText(NumberFormat.getInstance(Locale.ITALY).format(str2).toString());
                            }
                            int pos = dto.getText().length();
                            dto.setSelection(pos);
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            atras.setClickable(true);
            atras.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebService.listaProductosPedidos.clear();
                    tablaProductos.removeAllViews();
                    WebService.totalProductosPedidos = 0D;
                    Utilidad.vibraticionBotones(context);
                    Intent myIntent = new Intent(context, Pedidos.class);
                    startActivity(myIntent);
                }
            });

            if (WebService.usuarioActual != null) {
                if (Utilidad.isNetworkAvailable()) {
                    final ArrayAdapter<String> adapterProd = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, NombreProducto);
                    final ArrayAdapter<String> adapterCodProd = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, CodigosProducto);

                    spPresentacion.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                    final ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, spinnerPresentArray);

                    TraerProductos task01 = new TraerProductos(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            if (!WebService.errToken.equals("")) {
                                Intent myIntent = new Intent(context, Login.class);
                                startActivity(myIntent);
                            } else {
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
                            ProductosItem instaProd = new ProductosItem();

                            boolean encontrado = false;
                            for (int i = 0; i < WebService.ArrayItemsProductosPedido.size() && !encontrado; i++) {
                                instaProd = WebService.ArrayItemsProductosPedido.get(i);
                                if (instaProd.getNom_articulo().equals(item)) {
                                    encontrado = true;
                                    WebService.prodActualPedido = instaProd;
                                    nombreProd.setText(instaProd.getNom_articulo().trim());
                                    codProd.setText(instaProd.getCod_articulo().trim());
                                    adicional.setText(instaProd.getAdicional());
                                    View view2 = getCurrentFocus();
                                    view2.clearFocus();
                                    if (view2 != null) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                                    }
                                }
                            }//FIN FOR

                            prodSelectNom = instaProd.getNom_articulo().toString().trim();
                            prodSelect = instaProd.getCod_articulo().toString().trim();

                            TraerPresentacion task = new TraerPresentacion(new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {
                                    try {
                                        if (!WebService.errToken.equals("")) {
                                            Intent myIntent = new Intent(context, Login.class);
                                            startActivity(myIntent);
                                        } else {
                                            if (WebService.listPresentacion.size() == 0) {
                                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.presentacionno), Toast.LENGTH_LONG).show();
                                                //Se ocultan hasta que se seleccione un producto
                                                precio.setVisibility(View.GONE);
                                                precioText.setVisibility(View.GONE);
                                                spPresentacion.setVisibility(View.GONE);
                                                TituloPresent.setVisibility(View.GONE);
                                                cantidad.setVisibility(View.GONE);
                                                cantidadTitle.setVisibility(View.GONE);
                                                dtoText.setVisibility(View.GONE);
                                                totalText.setVisibility(View.GONE);
                                                dto.setVisibility(View.GONE);
                                                total.setVisibility(View.GONE);
                                            } else {
                                                spPresentacion.setVisibility(View.VISIBLE);
                                                TituloPresent.setVisibility(View.VISIBLE);
                                                cantidad.setVisibility(View.VISIBLE);
                                                cantidadTitle.setVisibility(View.VISIBLE);
                                                dtoText.setVisibility(View.VISIBLE);
                                                totalText.setVisibility(View.VISIBLE);
                                                dto.setVisibility(View.VISIBLE);
                                                total.setVisibility(View.VISIBLE);

                                                spinnerPresentArray.clear();
                                                for (int i = 0; i < WebService.listPresentacion.size(); i++) {
                                                    String nombreAgregar1 = WebService.listPresentacion.get(i).getNom_unidad().trim();
                                                    spinnerPresentArray.add(nombreAgregar1);
                                                }
                                                dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spPresentacion.setAdapter(dataAdapter2);

                                                for (int i = 0; i < WebService.listPresentacion.size(); i++) {
                                                    Presentacion present = WebService.listPresentacion.get(i);
                                                    String[] parts = present.getDescripcion().split("-");
                                                    String parts1_1 = parts[0];
                                                    if(parts1_1.trim().equals(WebService.sucuEmpSelect.getCod_Tit().trim())){
                                                        spPresentacion.setSelection(i);
                                                        break;
                                                    }
                                                }

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

                            Object item = parent.getItemAtPosition(position);
                            ProductosItem instaProd = new ProductosItem();
                            boolean encontrado = false;
                            for (int i = 0; i < WebService.ArrayItemsProductosPedido.size() && !encontrado; i++) {
                                instaProd = WebService.ArrayItemsProductosPedido.get(i);
                                if (instaProd.getCod_articulo().trim().equals(item)) {
                                    encontrado = true;
                                    WebService.prodActualPedido = instaProd;
                                    nombreProd.setText(instaProd.getNom_articulo().trim());
                                    codProd.setText(instaProd.getCod_articulo().trim());
                                    adicional.setText(instaProd.getAdicional());
                                    View view2 = getCurrentFocus();
                                    view2.clearFocus();
                                    if (view2 != null) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                                    }
                                }
                            }//FIN FOR

                            prodSelectNom = instaProd.getNom_articulo().toString().trim();
                            prodSelect = instaProd.getCod_articulo().toString().trim();

                            TraerPresentacion task = new TraerPresentacion(new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {
                                    try {
                                        if (!WebService.errToken.equals("")) {
                                            Intent myIntent = new Intent(context, Login.class);
                                            startActivity(myIntent);
                                        } else {
                                            if (WebService.listPresentacion.size() == 0) {
                                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.presentacionno), Toast.LENGTH_LONG).show();
                                                //Se ocultan hasta que se seleccione un producto
                                                precio.setVisibility(View.GONE);
                                                precioText.setVisibility(View.GONE);
                                                spPresentacion.setVisibility(View.GONE);
                                                TituloPresent.setVisibility(View.GONE);
                                                cantidad.setVisibility(View.GONE);
                                                cantidadTitle.setVisibility(View.GONE);
                                                dtoText.setVisibility(View.GONE);
                                                totalText.setVisibility(View.GONE);
                                                dto.setVisibility(View.GONE);
                                                total.setVisibility(View.GONE);
                                            } else {
                                                spPresentacion.setVisibility(View.VISIBLE);
                                                TituloPresent.setVisibility(View.VISIBLE);
                                                cantidad.setVisibility(View.VISIBLE);
                                                cantidadTitle.setVisibility(View.VISIBLE);
                                                dtoText.setVisibility(View.VISIBLE);
                                                totalText.setVisibility(View.VISIBLE);
                                                dto.setVisibility(View.VISIBLE);
                                                total.setVisibility(View.VISIBLE);
                                                spinnerPresentArray.clear();
                                                for (int i = 0; i < WebService.listPresentacion.size(); i++) {
                                                    String nombreAgregar1 = WebService.listPresentacion.get(i).getDescripcion().trim();
                                                    spinnerPresentArray.add(nombreAgregar1);
                                                }
                                                dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spPresentacion.setAdapter(dataAdapter2);

                                                for (int i = 0; i < WebService.listPresentacion.size(); i++) {
                                                    Presentacion present = WebService.listPresentacion.get(i);
                                                    String[] parts = present.getDescripcion().split("-");
                                                    String parts1_1 = parts[0];
                                                    if(parts1_1.trim().equals(WebService.sucuEmpSelect.getCod_Tit().trim())){
                                                        spPresentacion.setSelection(i);
                                                        break;
                                                    }
                                                }
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
                                    if (instanciaPr.getDescripcion().trim().equals(presentSelect.trim())) {
                                        String[] parts = presentSelect.split("-");
                                        String parts1_1 = parts[0];
                                        String parts1_2 = parts[1];
                                        depoSelect = parts1_1;
                                        WebService.presentacionActualPedido = instanciaPr;
                                        break;
                                    }
                                }//FIN FOR
                                precio.setVisibility(View.VISIBLE);
                                precioText.setVisibility(View.VISIBLE);
                                precio.setText(String.valueOf(WebService.presentacionActualPedido.getPrecio()));
                                dto.setText("0");
                                total.setText(String.valueOf(WebService.presentacionActualPedido.getPrecio()));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
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

                                if (Utilidad.isNetworkAvailable()) {
                                    try {
                                        if (codProd.getText().toString().equals("")) {
                                            Toast.makeText(getApplicationContext(), "Debe ingresar el codigo del producto", Toast.LENGTH_LONG).show();
                                        } else if (depoSelect.toString().equals("")) {
                                            Toast.makeText(getApplicationContext(), "Debe seleccionar la presentacion", Toast.LENGTH_LONG).show();
                                        } else if (cantidadIngre.toString().equals("")) {
                                            Toast.makeText(getApplicationContext(), "Debe ingresar la cantidad", Toast.LENGTH_LONG).show();
                                        } else {
                                            Double cantidad2 = Double.parseDouble(cantidad.getText().toString());
                                            Double precio2 = Double.parseDouble(precio.getText().toString());



                                            double descuento = Double.parseDouble(dto.getText().toString());
                                            double Precio_con_descuento = 0D;
                                            if(descuento == 0){
                                                Precio_con_descuento = precio2;
                                            }else{
                                                Precio_con_descuento = precio2*(1-(descuento/100));
                                            }
                                            Double imp_descuento = (precio2 - Precio_con_descuento) * cantidad2;
                                            double totalP = cantidad2*Precio_con_descuento;
                                            String tot = String.valueOf(totalP);
                                            tot = Utilidad.PerfectDecimal(String.valueOf(tot), 1000, 2);
                                            double factor_iva = 0.87;
                                            int cod_tasa_iva = 1;
                                            double porc_iva = WebService.prodActualPedido.getIva();
                                            double imp_iva = totalP*(porc_iva/100);
                                            WebService.addProductoPedido(new ProductosItem(prodSelectNom, prodSelect, cantidad2, cantidad2, precio2, Precio_con_descuento, Double.parseDouble(tot), Double.parseDouble(tot), descuento, depoSelect.trim(), imp_descuento, factor_iva, imp_iva, cod_tasa_iva));

                                            tablaProductos.removeAllViews();
                                            descuentoLin = 0;
                                            Lista();

                                            codProd.setText("");
                                            nombreProd.setText("");
                                            cantidad.setText("");
                                            precio.setText("");
                                            dto.setText("");
                                            total.setText("");
                                            precio.setVisibility(View.GONE);
                                            precioText.setVisibility(View.GONE);
                                            spPresentacion.setVisibility(View.GONE);
                                            TituloPresent.setVisibility(View.GONE);
                                            total.setVisibility(View.GONE);
                                            totalText.setVisibility(View.GONE);
                                            cantidad.setVisibility(View.GONE);
                                            cantidadTitle.setVisibility(View.GONE);
                                            dto.setVisibility(View.GONE);
                                            dtoText.setVisibility(View.GONE);
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
                    });

                    btnFacturar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ingresarPedido();
                        }
                    });

                } else {
                    Utilidad.CargarToastConexion(context);
                }
            } else {
                Intent myIntent = new Intent(context, Login.class);
                startActivity(myIntent);
            }


        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void CargarSpinner(){
        try {
            for (int i = 0; i < WebService.ArrayItemsProductosPedido.size(); i++) {
                String nombreAgregar = WebService.ArrayItemsProductosPedido.get(i).getNom_articulo();
                NombreProducto.add(nombreAgregar);
                String codigoAgregar = WebService.ArrayItemsProductosPedido.get(i).getCod_articulo().trim();
                CodigosProducto.add(codigoAgregar);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }

    public interface AsyncResponse {
        void processFinish(Object output);
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
            titCant.setPadding(5, 10, 0, 10);

            TextView titPrecioUnitario = new TextView(this);
            titPrecioUnitario.setText(getResources().getString(R.string.UnitProd));
            titPrecioUnitario.setTextColor(Color.parseColor("#ffffff"));
            titPrecioUnitario.setPadding(5, 10, 15, 10);
            titPrecioUnitario.setGravity(Gravity.RIGHT);

            TextView titDtoLinea = new TextView(this);
            titDtoLinea.setText(getResources().getString(R.string.Dto));
            titDtoLinea.setTextColor(Color.parseColor("#ffffff"));
            titDtoLinea.setPadding(30, 10, 22, 10);
            titDtoLinea.setGravity(Gravity.RIGHT);

            TextView precDesc = new TextView(this);
            precDesc.setText(getResources().getString(R.string.PrecioDesc));
            precDesc.setTextColor(Color.parseColor("#ffffff"));
            precDesc.setPadding(10, 10, 22, 10);
            precDesc.setGravity(Gravity.RIGHT);

            TextView titSubTotal = new TextView(this);
            titSubTotal.setText(getResources().getString(R.string.SubTotProd));
            titSubTotal.setTextColor(Color.parseColor("#ffffff"));
            titSubTotal.setGravity(Gravity.RIGHT);
            titSubTotal.setPadding(10, 10, 22, 10);

            tr1.addView(titCant);
            tr1.addView(titPrecioUnitario);
            tr1.addView(titDtoLinea);
            tr1.addView(precDesc);
            tr1.addView(titSubTotal);
            tr1.setBackgroundResource(R.color.colorPrimary);
            //Aca Terminamos de cargar los titulos

            tablaProductos.addView(tr1);

            for (int i = 0; WebService.listaProductosPedidos.size() > i; i++) {
                btnFacturar.setEnabled(true);
                final ProductosItem instaProd = WebService.listaProductosPedidos.get(i);

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

                quitar.getLayoutParams().height = 40;
                quitar.getLayoutParams().width = 40;
                quitar.requestLayout();

                TextView nombreItem = new TextView(this);
                String nombItem = instaProd.getNom_articulo().trim();
                String codArt = instaProd.getCod_articulo().trim();
                String sucursal = String.valueOf(instaProd.getCod_deposito()).trim();
                nombreItem.setText(nombItem + " - " + codArt + " - " + sucursal);
                nombreItem.setTextSize(10);

                nombreItem.setTextColor(Color.parseColor("#000000"));
                nombreItem.setPadding(10, 5, 5, 10);

                final ImageView editar = new ImageView(context);
                editar.setImageResource(R.drawable.editar);
                editar.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                editar.setPadding(5, 5, 0, 5);

                editar.getLayoutParams().height = 40;
                editar.getLayoutParams().width = 40;
                editar.requestLayout();

                tr6.addView(quitar);
                tr6.addView(editar);
                tr6.addView(nombreItem);
                tr0.addView(tr6);

                quitar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebService.removeProductoPedido(instaProd);
                        tablaProductos.removeAllViews();
                        descuentoLin = 0;
                        if(WebService.listaProductosPedidos.size()>0){
                            Lista();
                        }
                    }
                });

                editar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebService.instaItemPed = instaProd;
                        displayAlertEditar(WebService.instaItemPed);
                    }
                });

                //Aca cargamos los productos
                final TextView cantidadPedida = new TextView(this);
                cantidadPedida.setTextSize(12);
                ListaTextViews.add(cantidadPedida);
                cantidadPedida.setTextColor(Color.parseColor("#000000"));
                cantidadPedida.setText(String.valueOf(instaProd.getCantidad()));
                cantidadPedida.setGravity(Gravity.LEFT);

                final TextView PrecioUnitario = new TextView(this);
                PrecioUnitario.setText(Utilidad.GenerarFormato((int) Math.floor(instaProd.getPrecio_iva_inc())));
                PrecioUnitario.setGravity(Gravity.RIGHT);
                PrecioUnitario.setTextSize(12);
                PrecioUnitario.setTextColor(Color.parseColor("#000000"));
                ListaTextViews.add(PrecioUnitario);

                Double calculo = (instaProd.getPrecio_iva_inc() - instaProd.getPrecio_presup()) * instaProd.getCantidad();
                descuentoLin = calculo + descuentoLin;
                final TextView dtoLinea = new TextView(this);
                ListaTextViews.add(dtoLinea);
                dtoLinea.setText(String.valueOf(instaProd.getPorc_desc()));
                dtoLinea.setInputType(InputType.TYPE_CLASS_NUMBER);
                dtoLinea.setGravity(Gravity.CENTER);

                TextView precioDesc = new TextView(this);
                ListaTextViews.add(precioDesc);
                double descPrecio = instaProd.getPrecio_presup();
                precioDesc.setText(Utilidad.GenerarFormato2(descPrecio));
                precioDesc.setGravity(Gravity.RIGHT);
                precioDesc.setTextSize(12);
                precioDesc.setTextColor(Color.parseColor("#000000"));
                precioDesc.setPadding(5, 0, 5, 0);

                final TextView subtotal = new TextView(this);
                ListaTextViews.add(subtotal);
                double subTotal = instaProd.getSubtotal();
                subtotal.setText(Utilidad.GenerarFormato2(subTotal));
                subtotal.setGravity(Gravity.RIGHT);
                subtotal.setTextSize(12);
                subtotal.setTextColor(Color.parseColor("#000000"));

                tr2.addView(cantidadPedida);
                tr2.addView(PrecioUnitario);
                tr2.addView(dtoLinea);
                tr2.addView(precioDesc);
                tr2.addView(subtotal);
                tr2.setTag(i);
                tablaProductos.addView(tr0);
                tablaProductos.addView(tr2);

                if (WebService.listaProductosPedidos.size() == i + 1) {
                    TableRow tr01 = new TableRow(this);
                    tr1 = new TableRow(this);
                    tr2 = new TableRow(this);
                    tr3 = new TableRow(this);
                    TableRow tr4 = new TableRow(this);
                    TableRow tr5 = new TableRow(this);

                    TextView desc = new TextView(this);
                    ListaTextViews.add(desc);
                    desc.setTextColor(Color.parseColor("#ffffff"));
                    //desc.setText(getResources().getString(R.string.Descuento) + " " + Utilidad.GenerarFormato((int) descuentoLin));
                    desc.setText(getResources().getString(R.string.Descuento) + " " + Utilidad.GenerarFormato2((double) descuentoLin));
                    tr2.setGravity(Gravity.RIGHT);
                    tr2.setPadding(10, 10, 10, 10);
                    tr2.addView(desc);
                    tr2.setBackgroundResource(R.color.Iva);
                    tablaProductos.addView(tr2);

                    tota = new TextView(this);
                    ListaTextViews.add(tota);
                    tota.setTextColor(Color.parseColor("#ffffff"));
                    tota.setText(getResources().getString(R.string.TotalCaja) + " " + Utilidad.GenerarFormato2(WebService.totalProductosPedidos));
                    tr01.setGravity(Gravity.RIGHT);
                    tr01.setPadding(10, 10, 10, 10);
                    tr01.addView(tota);
                    tr01.setBackgroundResource(R.color.colorPrimary);
                    tablaProductos.addView(tr01);
                }
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
        }

        Utilidad.SetFontSizeTextView(ListaTextViews, this);
    }

    protected void displayAlertEditar(final ProductosItem miItm) {
        LayoutInflater factory = LayoutInflater.from(context);
        View textEntryView = factory.inflate(R.layout.dialog_editar_prod, null);
        final EditText inputcant = (EditText) textEntryView.findViewById(R.id.editTextCant);
        final EditText inputdto = (EditText) textEntryView.findViewById(R.id.editTextDto);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(textEntryView);
        inputcant.setText(String.valueOf(miItm.getCantidad()));
        inputdto.setText(String.valueOf(miItm.getPorc_desc()));
        WebService.totalProductosPedidos = WebService.totalProductosPedidos - miItm.getTotal();
        inputdto.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputdto.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    if (originalString.contains(".") && decimales == 0) {
                        originalString = originalString.replace(".", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);

                    //DecimalFormat formatter = new DecimalFormat("#,###,###,###", new DecimalFormatSymbols(new Locale("pt", "BR")));
                    formatter.applyPattern("#,###,###,###.##");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    inputdto.setText(formattedString);
                    inputdto.setSelection(inputdto.getText().length());

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                inputdto.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String str = dto.getText().toString();
                    if (str.isEmpty()) {
                        return;
                    }
                    if (decimales == 1) {
                        str.replace(".", "");
                    }
                    String str2 = Utilidad.PerfectDecimal(str, 1000, decimales);
                    if (!str2.equals(str)) {
                        if (decimales == 1) {
                            dto.setText(str2);
                        } else {
                            dto.setText(NumberFormat.getInstance(Locale.ITALY).format(str2).toString());
                        }
                        int pos = dto.getText().length();
                        dto.setSelection(pos);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        inputcant.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputcant.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    if (originalString.contains(".") && decimalesCant == 0) {
                        originalString = originalString.replace(".", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);

                    //DecimalFormat formatter = new DecimalFormat("#,###,###,###", new DecimalFormatSymbols(new Locale("pt", "BR")));
                    formatter.applyPattern("#,###,###,###.##");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    inputcant.setText(formattedString);
                    inputcant.setSelection(inputcant.getText().length());

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                inputcant.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String str = inputcant.getText().toString();
                    if (str.isEmpty()) {
                        double totalP = 0;
                        total.setText(Utilidad.PerfectDecimal(String.valueOf(totalP), 10000000, 2));
                        return;
                    }
                    if (decimalesCant == 0) {
                        str = str.replace(".", "");
                        //String str2 = Utilidad.PerfectDecimal(str, 1000, decimalesCant);
                        //cantidad.setText(NumberFormat.getInstance(Locale.ITALY).format(str2).toString());
                    }
                    String str2 = Utilidad.PerfectDecimal(str, 1000, decimalesCant);
                    if (!str2.equals(str)) {
                        if (decimalesCant == 1) {
                            inputcant.setText(str2);
                        } else {
                            inputcant.setText(NumberFormat.getInstance(Locale.ITALY).format(str2).toString());
                        }
                        int pos = inputcant.getText().length();
                        inputcant.setSelection(pos);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        builder.setMessage( getResources().getString( R.string.ModificaVal )).setCancelable(
                false ).setPositiveButton( getResources().getString( R.string.aceptar ),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            double cant = Double.parseDouble(inputcant.getText().toString());
                            double desc = Double.parseDouble(inputdto.getText().toString());
                            miItm.setCantidad(cant);
                            miItm.setCant_aprob(cant);
                            miItm.setPorc_desc(desc);
                            double Precio_con_descuento = 0;
                            double totalP = 0;
                            if(desc > 0){
                                Precio_con_descuento = WebService.presentacionActualPedido.getPrecio()*(1-(desc/100));
                                totalP = cant*Precio_con_descuento;
                            }else{
                                totalP = cant*WebService.presentacionActualPedido.getPrecio();
                            }

                            if(Precio_con_descuento == 0){
                                Precio_con_descuento =  WebService.presentacionActualPedido.getPrecio();
                            }
                            miItm.setSubtotal(totalP);
                            miItm.setTotal(totalP);
                            miItm.setPrecio_presup(Precio_con_descuento);
                            WebService.totalProductosPedidos = WebService.totalProductosPedidos + totalP;

                            tablaProductos.removeAllViews();
                            descuentoLin = 0;
                            Lista();
                        } catch (Exception e) {
                            e.toString();
                        }
                    }
                }).setNegativeButton( getResources().getString(R.string.Cancelar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alerta = builder.create();
        try {
            alerta.getWindow().setType(TYPE_APPLICATION_PANEL );
            alerta.show();
        } catch (Exception errrssd) {
            errrssd.toString();
            // Toast.makeText( getApplicationContext(), errrssd.getMessage(), Toast.LENGTH_LONG ).show();
        }
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
                params1.add("nro_viaje", WebService.pedido.getCod_suc().trim());
                WebService.TraerProductosPedidos(params1, "VentasDirectas/TraerProductos.php");
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
                params1.add("cod_tit", WebService.pedido.getCod_tit());
                params1.add("cod_sucursal", WebService.pedido.getCod_suc_cli().trim());
                params1.add("cod_art", WebService.prodActualPedido.getCod_articulo().trim());
                params1.add("cod_deposito", WebService.pedido.getCod_suc().toString().trim());
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

    private void ingresarPedido() {

        if(WebService.listaProductosPedidos.size() > 0) {
            param = new RequestParams();
            List stockList = new ArrayList();

            try {
                //postData = new JSONObject();
                Double imp_iva = WebService.totalProductosPedidos*0.13;

                param.add("cod_tit", WebService.pedido.getCod_tit());
                param.add("usuario", WebService.USUARIOLOGEADO);
                param.add("valorIntent", "mob");
                param.add("fec_doc", fecha.getText().toString());
                param.add("cod_suc", WebService.pedido.getCod_suc_cli());
                param.add("cod_deposito", WebService.pedido.getCod_suc());
                param.add("cod_emp", WebService.pedido.getCod_emp());
                param.add("cod_doc_uni", WebService.pedido.getDoc_fac());
                param.add("nro_doc_uni", WebService.pedido.getNro_doc_uni());
                param.add("cod_fpago", WebService.pedido.getForma_pago());
                param.add("fec_entrega", WebService.pedido.getFec_entrega());
                param.add("cod_zona_entrega", WebService.pedido.getZona_entrega());
                param.add("nom_turno", WebService.pedido.getTurno());
                param.add("cod_tipo", WebService.pedido.getTipo());
                param.add("cod_moneda", "1");
                param.add("cod_dpto", "0");
                param.add("es_express", "N");
                param.add("imp_iva", String.valueOf(imp_iva));
                param.add("total", String.valueOf(WebService.totalProductosPedidos));
                param.add("subtotal", String.valueOf(WebService.totalProductosPedidos));
                param.add("imp_descto_fin", String.valueOf(descuentoLin));
                param.add("cantidad_lineas", String.valueOf(WebService.listaProductosPedidos.size()));

                JSONArray pedidosProductos = new JSONArray();

                //stockList = WebService.listaProductosPedidos;

                for (int i = 0; i < WebService.listaProductosPedidos.size(); i++) {
                    //final JSONObject item = (JSONObject) stockList.get(i);
                    final JSONObject item = new JSONObject();
                    item.put("cod_articulo", WebService.listaProductosPedidos.get(i).getCod_articulo());
                    item.put("cantidad", WebService.listaProductosPedidos.get(i).getCantidad());
                    item.put("cant_aprob", WebService.listaProductosPedidos.get(i).getCant_aprob());
                    item.put("precio_iva_inc", WebService.listaProductosPedidos.get(i).getPrecio_iva_inc());
                    item.put("porc_desc", WebService.listaProductosPedidos.get(i).getPorc_desc());
                    item.put("imp_descto_linea", WebService.listaProductosPedidos.get(i).getImp_descto_linea());
                    item.put("precio_presup", WebService.listaProductosPedidos.get(i).getPrecio_presup());
                    item.put("total", WebService.listaProductosPedidos.get(i).getTotal());
                    item.put("subtotal", WebService.listaProductosPedidos.get(i).getSubtotal());
                    item.put("imp_iva", WebService.listaProductosPedidos.get(i).getImp_iva());
                    item.put("cod_tasa_iva", WebService.listaProductosPedidos.get(i).getCod_tasa_iva());
                    item.put("factor_iva", WebService.listaProductosPedidos.get(i).getFactor_iva());
                    item.put("cod_deposito", WebService.listaProductosPedidos.get(i).getCod_deposito());
                    pedidosProductos.put(item);
                }
                param.add("lineas", String.valueOf(pedidosProductos));
                try
                {
                    AgregarPedido task = new AgregarPedido();
                    task.execute();
                    try{
                        task.get();
                        if(WebService.reto_AgregaPedido.equals("ok")){
                            Toast.makeText( getApplicationContext(), "Pedido agregado correctamente", Toast.LENGTH_LONG ).show();
                            WebService.totalProductosPedidos = 0D;
                            WebService.listPresentacion = new ArrayList<>();
                            WebService.ArrayItemsProductosPedido = new ArrayList<>();
                            WebService.listaProductosPedidos = new ArrayList<>();

                            tablaProductos.removeAllViews();
                            WebService.totalProductosPedidos = 0D;
                            descuentoLin = 0;
                            try {
                                valorIntent = getIntent().getStringExtra("intent");
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                            Intent myIntent = new Intent(context, Pedidos.class);
                            myIntent.putExtra("intent", valorIntent);
                            startActivity(myIntent);
                        }else{
                            Toast.makeText( getApplicationContext(), "No se pudo ingresar el pedido" + WebService.reto_AgregaPedido, Toast.LENGTH_LONG ).show();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }catch (Exception ex){
                    Toast.makeText( getApplicationContext(), "Pedido: " + ex.getMessage(), Toast.LENGTH_LONG ).show();
                    ex.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.pedidoslistavacia), Toast.LENGTH_LONG).show();
        }
    }

    private class AgregarPedido extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... strings){
            WebService.IngresarPedido( "Pedidos/IngresarPedido.php",param);
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
}