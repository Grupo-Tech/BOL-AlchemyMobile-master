package com.example.user.trucksales.Visual.TruckSales;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.user.trucksales.Encapsuladoras.Item;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Cobranza.SeleccionCliente;
import com.example.user.trucksales.Visual.FacturaDirecta.FacturaDirecta;
import com.example.user.trucksales.Visual.FacturaDirecta.FacturaProductos;
import com.example.user.trucksales.Visual.FacturaDirecta.NotaCredito;
import com.example.user.trucksales.Visual.Login;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FacturaDirectaCono  extends Activity implements AdapterView.OnItemSelectedListener{

    private Utilidades Utilidad;
    Context contexto;

    AutoCompleteTextView nombreCliente,codCliente,buscarCliente;

    public List<String> editTextArray =  new ArrayList<String>();
    public List<String> Codigos = new ArrayList<>(  );
    ImageView atras;

    Spinner spSucursales;
    List<String> spinnerSucursalesArray = new ArrayList<>(  );

    Button btnAgregarProducto;

    TableLayout tablaProductos;

    double neto;
    private static double Total = 0, TotalSinIva = 0, IVA5 = 0, IVA10 = 0;

    private ArrayList<TextView> ListaTextViews;
    public TextView tota,totaPedi,iva5,iva10,TotSinIVA,TotalIVA,retorno;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_factura_directa_cono);

        contexto = this;
        Utilidad = new Utilidades(contexto);

        try {

            atras = findViewById(R.id.btnAtras);
            atras.setClickable(true);
            atras.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try{
                        Utilidad.vibraticionBotones(contexto);
                            Intent myIntent = new Intent(v.getContext(), ClienteXDefecto.class);
                            startActivity(myIntent);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            });

            nombreCliente = findViewById(R.id.TxtNombreCliente);
            codCliente = findViewById(R.id.TxtCodCliente);
            buscarCliente= findViewById(R.id.TxtBuscarCliente);

            btnAgregarProducto = findViewById(R.id.btnAgregarProducto);

            spSucursales = findViewById(R.id.spSucursales);
           // spSucursales.setOnItemSelectedListener( (AdapterView.OnItemSelectedListener) this );
            spSucursales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Object item = parent.getItemAtPosition(position);
                    Entrega instaSucu = new Entrega();

                    boolean encontrado = false;
                    for(int i = 0; i < WebService.ArraySucursales.size() && !encontrado; i++){
                        instaSucu = WebService.ArraySucursales.get(i);
                        if (instaSucu.getNom_Sucursal().trim().equals(item)){
                            encontrado = true;
                            //codSucursal = instaSucu.getCod_Sucursal().trim();
                            WebService.sucursalActual = instaSucu;
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            final ArrayAdapter<String> dataAdapterSucursales = new ArrayAdapter<String>( this,
                    android.R.layout.simple_spinner_item, spinnerSucursalesArray );

            spSucursales.setVisibility(View.GONE);

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
                            buscarCliente.setAdapter(adapter2);
                        }else{
                            Intent myIntent = new Intent(contexto, Login.class);
                            startActivity(myIntent);
                        }
                    }
                });
                task01.execute();
            } else {
                CargarSpinner();
                nombreCliente.setAdapter(adapter);
                codCliente.setAdapter(adapter2);
                buscarCliente.setAdapter(adapter2);
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
                            View view2 = getCurrentFocus();
                            view2.clearFocus();
                            if (view2 != null) {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                            }
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


            buscarCliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object item = parent.getItemAtPosition(position);
                    ClienteCobranza instaCliente = new ClienteCobranza();
                    boolean encontrado = false;
                    for (int i = 0; i < WebService.deudores.size() && !encontrado; i++) {
                        instaCliente = WebService.deudores.get(i);
                        String codigoAgregar = instaCliente.getCod_Tit_Gestion().trim()+" - "+instaCliente.getNom_Tit().trim();
                        if (codigoAgregar.trim().equals(item)) {
                        //if (instaCliente.getCod_Tit_Gestion().trim().equals(item)) {
                            encontrado = true;
                            nombreCliente.setText(instaCliente.getNom_Tit().trim());
                            codCliente.setText(instaCliente.getCod_Tit_Gestion().trim());

                            WebService.clienteActual = instaCliente;
                            spSucursales.setVisibility(View.VISIBLE);
                            TraerSucursales task = new TraerSucursales(new FacturaDirecta.AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {
                                    try {
                                        if (!WebService.errToken.equals("")) {
                                            Intent myIntent = new Intent(contexto, Login.class);
                                            startActivity(myIntent);
                                        }else {
                                            if (WebService.ArraySucursales.size() == 0) {
                                                Toast toast = Toast.makeText(contexto, getResources().getString(R.string.sucursales), Toast.LENGTH_LONG);
                                                toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                                                toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                                                toast.show();//showing the toast is important***/
                                            } else {
                                                for (int i = 0; i < WebService.ArraySucursales.size(); i++) {
                                                    Entrega instaSucu = new Entrega();
                                                    instaSucu = WebService.ArraySucursales.get(i);

                                                    String nombreAgregar = instaSucu.getNom_Sucursal().trim();
                                                    spinnerSucursalesArray.add(nombreAgregar);
                                                }
                                                dataAdapterSucursales.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spSucursales.setAdapter(dataAdapterSucursales);
                                            }
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            task.execute();



                            View view2 = getCurrentFocus();
                            view2.clearFocus();
                            if (view2 != null) {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                            }
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

            buscarCliente.setOnEditorActionListener(new EditText.OnEditorActionListener() {
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

            buscarCliente.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    nombreCliente.setText("");
                    codCliente.setText("");

                    return false;
                }

            });
            btnAgregarProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(WebService.clienteActual.getCod_Tit_Gestion()==null){
                        Toast.makeText( getApplicationContext(), "Debe seleccionar un cliente", Toast.LENGTH_LONG ).show();
                    }else if(WebService.sucursalActual.getCod_Sucursal()==null){
                        Toast.makeText( getApplicationContext(), "Debe seleccionar una sucursal", Toast.LENGTH_LONG ).show();
                    }else{
                        Utilidad.vibraticionBotones(contexto);
                        if(Utilidad.isNetworkAvailable()) {
                            Intent myIntent2 = new Intent(v.getContext(), FacturaDirectaProductoCono.class);
                            myIntent2.putExtra("intent2", "FacturaDirectaProductoCono");
                            startActivity(myIntent2);
                        }else {
                            Utilidad.CargarToastConexion(contexto);
                        }
                    }
                }
            });

            tablaProductos.removeAllViews();
            if (WebService.listaProductos.size() > 0) {
                Total = 0;
                TotalSinIva = 0;
                IVA5 = 0;
                IVA10 = 0;
                Lista();
            }

        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    private class TraerSucursales extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public FacturaDirecta.AsyncResponse delegate = null;//Call back interface
        public TraerSucursales(FacturaDirecta.AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            if(Utilidad.isNetworkAvailable()) {
                RequestParams params1 = new RequestParams();
                params1.add("cod_tit", WebService.clienteActual.getCod_Tit_Gestion().trim());
                WebService.TraerSucursales("VentasDirectas/TraerSucursales.php", params1);
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

    private class TraerClientes extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
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
            //String codigoAgregar = WebService.deudores.get(i).getCod_Tit_Gestion().trim();
            String codigoAgregar = WebService.deudores.get(i).getCod_Tit_Gestion().trim()+" - "+WebService.deudores.get(i).getNom_Tit().trim();
            Codigos.add(codigoAgregar);
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

    @Override
    public void onBackPressed() {
        Utilidad.vibraticionBotones(contexto);
        try {
            Intent myIntent = new Intent(contexto, ClienteXDefecto.class);
            startActivity(myIntent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
