package com.example.user.trucksales.Visual.FacturaDirecta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.FacturaXDia;
import com.example.user.trucksales.Encapsuladoras.Item;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.Visual.TruckSales.factura;
import com.loopj.android.http.RequestParams;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NotaCredito extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    private Utilidades Utilidad;
    ImageView atras, btnAdd;
    LinearLayout mainLayout;
    Context contexto;
    TextView LblUsu, LblFecha, texto, precio, cantidadTitle, precioTitle, nombreCliente;
    RequestParams params = new RequestParams();
    Button btnIR;
    EditText cantidad;
    //public static double cantidadIngre;
    public static String cantidadIngre;
    double neto;
    private static double Total = 0, TotalSinIva = 0, IVA5 = 0, IVA10 = 0;
    Item inst = new Item();

    TableLayout tablaProductos;

    private ArrayList<TextView> ListaTextViews;
    public TextView tota,totaPedi,iva5,iva10,TotSinIVA,TotalIVA,retorno, rucCliente;

    public static String prodSelect;
    public static String prodSelectNom;
    public static String ivaSlect;

    Spinner spProductos;
    List<String> spinnerProductos = new ArrayList<>(  );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_nota_credito);
        contexto = this;
        Utilidad = new Utilidades(contexto);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        GuardarDatosUsuario.Contexto = contexto;

        try {
            if (WebService.USUARIOLOGEADO != null) {
                if (Utilidad.isNetworkAvailable()) {

                    ListaTextViews = new ArrayList<>();
                    LblUsu = findViewById(R.id.LblUsu);
                    LblFecha = findViewById(R.id.LblFecha);
                    atras = findViewById(R.id.btnAtras);
                    texto = findViewById(R.id.texto);
                    spProductos = findViewById(R.id.spProductos);
                    cantidad = findViewById(R.id.cantidad);
                    cantidad.setVisibility(View.GONE);
                    btnIR = findViewById(R.id.btnIR);
                    precio = findViewById(R.id.precio);
                    btnAdd = findViewById(R.id.btnAdd);
                    cantidadTitle = findViewById(R.id.cantidadTitle);
                    precioTitle = findViewById(R.id.precioTitle);
                    tablaProductos = findViewById(R.id.tablaProductos);
                    nombreCliente = findViewById(R.id.nombreCliente);
                    rucCliente = findViewById(R.id.rucCliente);

                    btnAdd.setVisibility(View.GONE);
                    cantidadTitle.setVisibility(View.GONE);
                    precioTitle.setVisibility(View.GONE);

                    LblUsu.setText(getResources().getString(R.string.usuarioTool) + WebService.usuarioActual.getNombre().trim());
                    String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                    LblFecha.setText(timeStamp);

                    nombreCliente.setText(WebService.fact.getNom_Tit().trim().toUpperCase());
                    rucCliente.setText(WebService.fact.getCod_Tit_Gestion());
                    texto.setText(getResources().getString(R.string.FecGen) + " " + WebService.fact.getFecha()+ "\n" + getResources().getString(R.string.NroViaje) + " " + WebService.fact.getNro_viaje()+ "\n" + getResources().getString(R.string.Importe2) +  NumberFormat.getInstance(Locale.ITALY).format(WebService.fact.getImporte()).toString());

                    spProductos.setOnItemSelectedListener( (AdapterView.OnItemSelectedListener) this );
                    final ArrayAdapter<String> dataAdapterSucursales = new ArrayAdapter<String>( this,
                            android.R.layout.simple_spinner_item, spinnerProductos );

                    for (int i = 0; i < WebService.ArrayItemsProductosFactura.size(); i++) {
                        Item insta = new Item();
                        insta = WebService.ArrayItemsProductosFactura.get(i);

                        String nombreAgregar = insta.getNom_Articulo().trim();
                        String codAgregar = insta.getCod_Articulo().trim();
                        String cant = String.valueOf(insta.getCantidad_vendida());
                        spinnerProductos.add(codAgregar + " - " + nombreAgregar + " - " + getResources().getString(R.string.cant) + cant);
                    }
                    dataAdapterSucursales.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spProductos.setAdapter(dataAdapterSucursales);

                    spProductos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            cantidad.setVisibility(View.VISIBLE);
                            btnAdd.setVisibility(View.VISIBLE);
                            precio.setVisibility(View.VISIBLE);
                            cantidadTitle.setVisibility(View.VISIBLE);
                            precioTitle.setVisibility(View.VISIBLE);
                            inst = new Item();
                            for (int x = 0; x < WebService.ArrayItemsProductosFactura.size(); x++) {
                                inst = WebService.ArrayItemsProductosFactura.get(x);
                                String docSelec = spProductos.getSelectedItem().toString();
                                String[] parts = docSelec.split("-");
                                String part1 = parts[0];
                                docSelec = part1;

                                if (inst.getCod_Articulo().trim().equals(docSelec.trim())) {
                                    cantidad.setText(String.valueOf(inst.getCantidad_vendida()).trim());
                                    precio.setText(String.valueOf(inst.getPrecio_Unitario()).trim());
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

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

                    atras.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try{
                            Utilidad.vibraticionBotones(contexto);
                            WebService.listaProductos.clear();
                            WebService.fact = new FacturaXDia();
                            Intent myIntent = new Intent(contexto, BuscarFactura.class);
                            startActivity(myIntent);
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    });

                    btnAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Utilidad.vibraticionBotones(contexto);
                                cantidadIngre = cantidad.getText().toString();
                                if (Double.valueOf(cantidadIngre) > inst.getCantidad_vendida()) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.cantError), Toast.LENGTH_LONG).show();
                                } else {
                                    if (Utilidad.isNetworkAvailable()) {
                                        try {
                                            double subTot = Double.valueOf(cantidadIngre) * inst.getPrecio_Unitario();

                                            WebService.addProducto(new Item(inst.getNom_Articulo(), inst.getCod_Articulo(), Double.valueOf(cantidadIngre), inst.getPrecio_Unitario(), inst.getPorc_Iva(), subTot, inst.getCod_uni_vta()));

                                            tablaProductos.removeAllViews();
                                            Total = 0;
                                            TotalSinIva = 0;
                                            IVA5 = 0;
                                            IVA10 = 0;

                                            Lista();

                                            inst = new Item();
                                            /*cantidadIngre = "";
                                            cantidad.setText("");
                                            cantidad.setVisibility(View.GONE);
                                            precio.setText("");
                                            precio.setVisibility(View.GONE);
                                            btnAdd.setVisibility(View.GONE);
                                            cantidadTitle.setVisibility(View.GONE);
                                            precioTitle.setVisibility(View.GONE);*/

                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }

                                    }

                                }
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    });

                    btnIR.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(contexto);
                            if (Utilidad.isNetworkAvailable()) {
                                try {
                                    if (WebService.listaProductos.size() > 0) {
                                        ObtenerNumRecomendado task = new ObtenerNumRecomendado(new AsyncResponse() {
                                            @Override
                                            public void processFinish(Object output) {
                                                if (!WebService.errToken.equals("")) {
                                                    Intent myIntent = new Intent(contexto, Login.class);
                                                    startActivity(myIntent);
                                                } else {
                                                    WebService.ArrayItemsViaje = WebService.listaProductos;
                                                    WebService.Entrega_A_Realizar.setCod_Tit(WebService.fact.getCod_Tit());
                                                    WebService.Entrega_A_Realizar.setCod_Tit_Gestion(WebService.fact.getCod_Tit_Gestion());
                                                    WebService.Entrega_A_Realizar.setCod_Tit(WebService.fact.getCod_Tit());
                                                    WebService.Entrega_A_Realizar.setNro_Docum(WebService.fact.getNro_Docum());
                                                    WebService.Entrega_A_Realizar.setNom_Sucursal(WebService.fact.getSucursal());
                                                    WebService.Entrega_A_Realizar.setNro_doc_ref(0);
                                                    WebService.TotalFactura = WebService.totalProductos;
                                                    WebService.cod_sucu = WebService.fact.getCod_Sucursal().trim();
                                                    WebService.viajeSeleccionado.setNumViaje(WebService.fact.getNro_viaje());
                                                    WebService.clienteActual.setCod_Tit(WebService.fact.getCod_Tit());
                                                    WebService.clienteActual.setCod_Tit_Gestion(WebService.fact.getCod_Tit_Gestion());
                                                    WebService.clienteActual.setLatiud_Ubic(WebService.fact.getLatitud());
                                                    WebService.clienteActual.setLongitud_Ubic(WebService.fact.getLongitud());
                                                    WebService.clienteActual.setNom_Tit(WebService.fact.getNom_Tit());

                                                    Intent myIntent = new Intent(contexto, factura.class);
                                                    myIntent.putExtra("intent", "NotaCredito");
                                                    startActivity(myIntent);
                                                }
                                            }
                                        });
                                        task.execute();
                                    }else{
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.ingArt), Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Utilidad.CargarToastConexion(contexto);
                            }
                        }
                    });

                } else {
                    Utilidad.CargarToastConexion(contexto);
                }
            } else {
                Intent myIntent = new Intent(contexto, Login.class);
                startActivity(myIntent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
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

            TextView titSubTotal = new TextView(this);
            titSubTotal.setText(getResources().getString(R.string.SubTotProd));
            titSubTotal.setTextColor(Color.parseColor("#ffffff"));
            titSubTotal.setGravity(Gravity.RIGHT);
            titSubTotal.setPadding(10, 10, 22, 10);

            tr1.addView(titCant);
            tr1.addView(titPrecioUnitario);
            tr1.addView(titIva);
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

                final ImageView quitar = new ImageView(contexto);
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
                        WebService.removeProducto(instaProd);
                        tablaProductos.removeAllViews();
                        Total = 0;
                        TotalSinIva = 0;
                        IVA5 = 0;
                        IVA10 = 0;
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

                double sub_tot = Math.floor(instaProd.getCantidad() * instaProd.getPrecio_Unitario());

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

                    tota = new TextView(this);
                    ListaTextViews.add(tota);
                    tota.setTextColor(Color.parseColor("#ffffff"));
                    tota.setText(getResources().getString(R.string.TotalCaja) + " " + Utilidad.GenerarFormato2(WebService.totalProductos));
                    tr01.setGravity(Gravity.RIGHT);
                    tr01.setPadding(10, 10, 10, 10);
                    tr01.addView(tota);
                    tr01.setBackgroundResource(R.color.colorPrimary);
                    tablaProductos.addView(tr01);

                    iva5 = new TextView(this);
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
                    tablaProductos.addView(tr5);
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

    private class ObtenerNumRecomendado extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog(contexto);
        public AsyncResponse delegate = null;//Call back interface

        public ObtenerNumRecomendado(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {
            if (Utilidad.isNetworkAvailable()) {
                RequestParams params2 = new RequestParams();
                params2.put("username", WebService.usuarioActual.getNombre());
                WebService.RecomendarNumero("Facturas/RecomendarNumeroNC.php", params2);
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
    public void onBackPressed() {
        super.onBackPressed();
        try{
        Utilidad.vibraticionBotones(contexto);
        WebService.listaProductos.clear();
        WebService.fact = new FacturaXDia();
        Intent myIntent = new Intent(contexto, BuscarFactura.class);
        startActivity(myIntent);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
