package com.example.user.trucksales.Visual.FacturaDirecta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.Item;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.Visual.TruckSales.Consultas;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReporteStock extends AppCompatActivity {

    private Utilidades Utilidad;
    LinearLayout mainLayout;
    Context contexto;
    TextView usuario, viaje, fecha;
    ImageView atras;
    AutoCompleteTextView nombreProd,codProd;
    TableLayout tablaProductos;
    private ArrayList<TextView> ListaTextViews;

    public static String valorIntent;
    String fechadeldia = "";

    public List<String> NombreProducto =  new ArrayList<String>();
    public List<String> CodigosProducto = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_reporte_stock);
        contexto = this;
        Utilidad = new Utilidades(contexto);

        GuardarDatosUsuario.Contexto = contexto;

        try{
            if(WebService.USUARIOLOGEADO != null){
                if(Utilidad.isNetworkAvailable()){

                    try {
                        valorIntent = getIntent().getStringExtra("intent");
                    }catch (Exception ex){
                        ex.printStackTrace();
                        // Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                    }

                    ListaTextViews = new ArrayList<>();
                    atras = findViewById(R.id.btnAtras);
                    usuario = findViewById(R.id.LblUsu);
                    fecha = findViewById(R.id.LblFecha);
                    viaje = findViewById(R.id.viaje);
                    tablaProductos = findViewById(R.id.tablaProductos);
                    nombreProd = findViewById(R.id.NomProducto);
                    codProd = findViewById(R.id.CodProducto);

                    viaje.setText(WebService.viajeSeleccionado.getNumViaje().trim());
                    usuario.setText(WebService.usuarioActual.getNombre().trim());
                    String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                    fecha.setText(timeStamp);
                    fechadeldia = fecha.getText().toString();

                    atras.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try{
                            Intent myIntent = new Intent(contexto, Consultas.class);
                            myIntent.putExtra("intent", valorIntent);
                            startActivity(myIntent);
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    });

                    CargarSpinner();

                    final ArrayAdapter<String> adapterProd = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, NombreProducto);
                    final ArrayAdapter<String> adapterCodProd = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, CodigosProducto);

                    nombreProd.setAdapter(adapterProd);
                    codProd.setAdapter(adapterCodProd);

                    TraerProductosViaje task = new TraerProductosViaje(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            if (!WebService.errToken.equals("")) {
                                Intent myIntent = new Intent(contexto, Login.class);
                                startActivity(myIntent);
                            }else {
                                String codigoProd = "TODOS";
                                Lista(codigoProd);
                            }
                        }
                    });
                    task.execute();

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
                            tablaProductos.removeAllViews();
                            String codigoProd = codProd.getText().toString();
                            if (codigoProd.equals("")) {
                                codigoProd = "TODOS";
                            }
                            Lista(codigoProd);
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

                    codProd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tablaProductos.removeAllViews();
                            Lista("TODOS");
                            codProd.setText("");
                            nombreProd.setText("");
                        }
                    });

                    nombreProd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tablaProductos.removeAllViews();
                            Lista("TODOS");
                            codProd.setText("");
                            nombreProd.setText("");
                        }
                    });

                    codProd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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

                            tablaProductos.removeAllViews();
                            String codigoProd = codProd.getText().toString();
                            if (codigoProd.equals("")) {
                                codigoProd = "TODOS";
                            }
                            Lista(codigoProd);
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

                }else{
                    Utilidad.CargarToastConexion(contexto);
                }
            }else {
                Intent myIntent = new Intent(contexto, Login.class);
                startActivity(myIntent);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    private class TraerProductosViaje extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface
        public TraerProductosViaje(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            if(Utilidad.isNetworkAvailable()) {
                RequestParams params1 = new RequestParams();
                params1.add("nro_viaje", WebService.viajeSeleccionado.getNumViaje().trim());
                /*params1.add("usuario", WebService.usuarioActual.getNombre().trim());
                params1.add("fecha", fechadeldia.trim());*/
                WebService.TraerProductosViaje(params1, "VentasDirectas/TraerProductosViaje.php");
                return null;

            } else {
                Utilidad.CargarToastConexion(contexto);
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

    private void CargarSpinner(){
        try {
            for (int i = 0; i < WebService.ArrayItemsProductos.size(); i++) {
                String nombreAgregar = WebService.ArrayItemsProductos.get(i).getNom_Articulo();
                NombreProducto.add(nombreAgregar);
                String codigoAgregar = WebService.ArrayItemsProductos.get(i).getCod_Articulo().trim();
                CodigosProducto.add(codigoAgregar);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }

    public void Lista(String codigoProd) {
        try {

            if(WebService.ArrayItemsProductosViaje.size()>0){

                TableLayout tr0 = new TableLayout(this);
                TableRow tr1 = new TableRow(this);
                TableRow tr6 = new TableRow(this);
                TableRow tr7 = new TableRow(this);
                TableRow tr8 = new TableRow(this);

                tr1 = new TableRow(this);

                //Aca cargamos los titulos
                TextView titCodProd = new TextView(this);
                titCodProd.setText(getResources().getString(R.string.CodProd));
                titCodProd.setTextColor(Color.parseColor("#ffffff"));
                titCodProd.setPadding(5, 10, 10, 10);

                TextView titProd = new TextView(this);
                titProd.setText(getResources().getString(R.string.NombProd));
                titProd.setTextColor(Color.parseColor("#ffffff"));
                titProd.setPadding(20, 10, 10, 10);

                TextView titUnidades = new TextView(this);
                titUnidades.setText(getResources().getString(R.string.Unidades));
                titUnidades.setTextColor(Color.parseColor("#ffffff"));
                titUnidades.setPadding(20, 10, 10, 10);
                titUnidades.setGravity(Gravity.RIGHT);

                TextView titCantidadVen = new TextView(this);
                titCantidadVen.setText(getResources().getString(R.string.Vendida));
                titCantidadVen.setTextColor(Color.parseColor("#ffffff"));
                titCantidadVen.setPadding(10, 10, 10, 10);
                titCantidadVen.setGravity(Gravity.RIGHT);

                tr1.addView(titCodProd);
                tr1.addView(titProd);
                tr1.addView(titUnidades);
                tr1.addView(titCantidadVen);
                tr1.setBackgroundResource(R.color.colorPrimary);
                //Aca Terminamos de cargar los titulos

               // tr0.addView(tr1);
                tablaProductos.addView(tr1);

            for (int i = 0; WebService.ArrayItemsProductosViaje.size() > i; i++) {

                final Item instaProd = WebService.ArrayItemsProductosViaje.get(i);

                TextView nombreItem = new TextView(this);
                TextView codigoItem = new TextView(this);
                TextView unidades = new TextView(this);
                TextView vendidas = new TextView(this);
                String nombItem = instaProd.getNom_Articulo().trim();
                String codArt = instaProd.getCod_Articulo().trim();
                String unid = String.valueOf(instaProd.getCantidad());
                String vend = String.valueOf(instaProd.getCantidad_vendida());

                if(instaProd.getCod_Articulo().trim().equals(codigoProd)){
                    instaProd.setCantidad_entregada(instaProd.getCantidad());

                    tr0 = new TableLayout(this);
                    tr6 = new TableRow(this);

                    codigoItem.setText(codArt);
                    codigoItem.setTextSize(12);
                    codigoItem.setTextColor(Color.parseColor("#000000"));
                    codigoItem.setGravity(Gravity.LEFT);
                    codigoItem.setPadding(5, 10, 10, 10);
                    ListaTextViews.add(codigoItem);

                    nombreItem.setText(nombItem);
                    nombreItem.setTextSize(12);
                    nombreItem.setTextColor(Color.parseColor("#000000"));
                    nombreItem.setPadding(20, 10, 10, 10);
                    nombreItem.setGravity(Gravity.LEFT);
                    ListaTextViews.add(nombreItem);

                    unidades.setText(unid);
                    unidades.setTextSize(12);
                    unidades.setTextColor(Color.parseColor("#000000"));
                    unidades.setPadding(20, 10, 10, 10);
                    unidades.setGravity(Gravity.RIGHT);
                    ListaTextViews.add(unidades);

                    vendidas.setText(vend);
                    vendidas.setTextSize(12);
                    vendidas.setTextColor(Color.parseColor("#000000"));
                    vendidas.setPadding(10, 10, 10, 10);
                    vendidas.setGravity(Gravity.RIGHT);
                    ListaTextViews.add(vendidas);

                    tr6.addView(codigoItem);
                    tr6.addView(nombreItem);
                    tr6.addView(unidades);
                    tr6.addView(vendidas);
                    tr6.setBackgroundResource(R.color.viajes);

                    tablaProductos.addView(tr6);

                }else if(codigoProd.equals("TODOS")){
                    instaProd.setCantidad_entregada(instaProd.getCantidad());

                    tr6 = new TableRow(this);

                    codigoItem.setText(codArt);
                    codigoItem.setTextSize(12);
                    codigoItem.setTextColor(Color.parseColor("#000000"));
                    codigoItem.setGravity(Gravity.LEFT);
                    codigoItem.setPadding(5, 10, 10, 10);
                    ListaTextViews.add(codigoItem);

                    nombreItem.setText(nombItem);
                    nombreItem.setTextSize(12);
                    nombreItem.setTextColor(Color.parseColor("#000000"));
                    nombreItem.setPadding(20, 10, 10, 10);
                    nombreItem.setGravity(Gravity.LEFT);
                    ListaTextViews.add(nombreItem);

                    unidades.setText(unid);
                    unidades.setTextSize(12);
                    unidades.setTextColor(Color.parseColor("#000000"));
                    unidades.setPadding(20, 10, 10, 10);
                    unidades.setGravity(Gravity.RIGHT);
                    ListaTextViews.add(unidades);

                    vendidas.setText(vend);
                    vendidas.setTextSize(12);
                    vendidas.setTextColor(Color.parseColor("#000000"));
                    vendidas.setPadding(10, 10, 10, 10);
                    vendidas.setGravity(Gravity.RIGHT);
                    ListaTextViews.add(vendidas);

                    tr6.addView(codigoItem);
                    tr6.addView(nombreItem);
                    tr6.addView(unidades);
                    tr6.addView(vendidas);
                    tr6.setBackgroundResource(R.color.viajes);

                    tablaProductos.addView(tr6);
                }
            }
            }else{
                TableRow tr1 = new TableRow(this);
                tr1 = new TableRow(this);

                TextView retor = new TextView(this);
                retor.setText(getResources().getString(R.string.stockError));
                tr1.addView(retor);
                tablaProductos.addView(tr1);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
        }

        Utilidad.SetFontSizeTextView(ListaTextViews, this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try{
        Intent myIntent = new Intent(contexto, Consultas.class);
        myIntent.putExtra("intent", valorIntent);
        startActivity(myIntent);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
