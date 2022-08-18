package com.example.user.trucksales.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.Item;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.Visual.TruckSales.Productos;
import com.example.user.trucksales.Visual.TruckSales.Productos2;
import com.example.user.trucksales.dialog.Dialog_Reclamo;
import com.example.user.trucksales.dialog.Dialog_Reclamo.NoticeDialogListenerNormal;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;


public class AdaptadorProductos extends BaseAdapter /*implements NoticeDialogListenerNormal*/ {
    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 123 ;
    private static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 123;

    private static Context context;
    private Utilidades Utilidad;

    RequestParams params1 = new RequestParams();
    RequestParams params4 = new RequestParams();

    double neto;

    List<EditText> LstCantEntregadas = new ArrayList<>();
    List<Double> LstPreciosUnitarios = new ArrayList<>();
    List<Double> LstIva = new ArrayList<>();
    private static double Total, TotalSinIva, IVA5, IVA10;


    Fila view = new Fila();

    String cantidad = "";
    String cod = "";
    String observ = "";

    private final AdaptadorProductos adaptador;
    private Productos2 activity; //Activity desde el cual se hace referencia al llenado de la lista
    private List<Item> arrayItems; // Lista de items

    // Constructor con parámetros que recibe la Acvity y los datos de los items.
    public AdaptadorProductos(Productos2 activity, List<Item> items){
        super();
        this.activity = activity;
        this.arrayItems = items;
        this.adaptador=this;
    }

    // Retorna el número de items de la lista
    @Override
    public int getCount() {
        return arrayItems.size();
    }
    // Retorna el objeto TitularItems de la lista
    @Override
    public Object getItem(int position) {
        return arrayItems.get(position);
    }
    // Retorna la posición del item en la lista
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
   Clase estática que contiene los elementos de la lista
    */
    public static class Fila   {

        public RelativeLayout ly_cliente;
        public RelativeLayout ly_cliente3;
        public RelativeLayout ly_cliente2;
        public ImageView reclamo;
        public TextView datoCant,datoUnit, datoIva, datoSubTotal, producto, datocantEnt;
        //public EditText datocantEnt;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        context = parent.getContext();
        Utilidad = new Utilidades(context);

        //final Fila view2 = new Fila();
        LayoutInflater inflator = activity.getLayoutInflater();
        final Item itm = arrayItems.get(position);
        final View convertViewAux;
        /*
        Condicional para recrear la vista y no distorcionar el número de elementos
         */
        try {
            if (convertView == null) {
                convertView = inflator.inflate(R.layout.item_productos, parent, false);

                view.ly_cliente = (RelativeLayout) convertView.findViewById(R.id.ly_cliente);
                view.ly_cliente2 = (RelativeLayout) convertView.findViewById(R.id.ly_cliente2);
                view.ly_cliente3 = (RelativeLayout) convertView.findViewById(R.id.ly_cliente3);
                //iew.txtTotal = (TextView) convertView.findViewById(R.id.txtTotal);
                view.datoCant = (TextView) convertView.findViewById(R.id.datoCant);
                //view.datocantEnt = (EditText) convertView.findViewById(R.id.datocantEnt);
                view.datocantEnt = (TextView) convertView.findViewById(R.id.datocantEnt);

                view.datoUnit = (TextView) convertView.findViewById(R.id.datoUnit);
                view.datoIva = (TextView) convertView.findViewById(R.id.datoIva);
                view.datoSubTotal = (TextView) convertView.findViewById(R.id.datoSubTotal);
                view.producto = (TextView) convertView.findViewById(R.id.producto);
                view.reclamo = (ImageView) convertView.findViewById(R.id.reclamo);

                convertView.setTag(view);
            } else {
                view = (Fila) convertView.getTag();
            }
            convertViewAux = convertView;
            // Se asigna el dato proveniente del objeto
            final int order = position + 1;

            String nombItem = itm.getNom_Articulo().trim() + "(" + itm.getCod_uni_vta().trim() + ")";
            String codArt = itm.getCod_Articulo().trim();

            view.producto.setText(nombItem + " - " + codArt);
            view.producto.setTextSize(15);

            view.datoCant.setText(Double.toString(itm.getCantidad()));

            view.datocantEnt.setText(Double.toString(itm.getCantidad()));
            view.datocantEnt.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            view.datocantEnt.setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));
            //LstCantEntregadas.add(view.datocantEnt);

           /* view.datocantEnt.addTextChangedListener(new TextWatcher() {

                double cantidadAnterior;
                double lastNonCeroNumber;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                   /* if (!(s.equals(null))) {
                        try {

                            if (Double.valueOf(s.toString()) > Double.valueOf(view.datoCant.getText().toString()) * 1.10) {
                                cantidadAnterior = Double.valueOf(view.datoCant.getText().toString());
                            } else {
                                cantidadAnterior = s.toString().equals("") ? 0 : Double.valueOf(s.toString());
                            }
                            if (cantidadAnterior != 0.0) {
                                lastNonCeroNumber = cantidadAnterior;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }*/
            /*    }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        if (!s.equals("")) {
                            String str = s.toString();
                            if (str.isEmpty()) return;
                            String str2 = Utilidad.PerfectDecimal(str, 1000, 2);
                            if (!str2.equals(str)) {
                                view.datocantEnt.setText(str2);
                                int pos = s.length();
                                view.datocantEnt.setSelection(pos);
                            }

                            float stcant = Float.parseFloat(s.toString());

                            float punit = Float.parseFloat(Utilidad.NumeroSinPunto(view.datoUnit.getText().toString()));

                            int subtot = (int) (Float.valueOf(stcant) * Float.valueOf(punit));
                            //int subtot = (int) (Double.valueOf(cantidadEntregada.getText().toString()) * Double.valueOf(Utilidad.NumeroSinPunto(PrecioUnitario.getText().toString())));
                            view.datoSubTotal.setText(Utilidad.GenerarFormato(subtot));
                            double cantidadActual = Double.valueOf(s.toString());
                           /* double multiplicador = cantidadActual == 0 && cantidadAnterior == 0 ? cantidadActual - lastNonCeroNumber : cantidadActual - cantidadAnterior;
                            lastNonCeroNumber = 0;*/

          /*                  Total = 0;
                            TotalSinIva = 0;
                            IVA5 = 0;
                            IVA10 = 0;

                            for (int i = 0; i < LstCantEntregadas.size(); i++) {
                                TotalSinIva += (Double.valueOf(LstCantEntregadas.get(i).getText().toString()) * Double.valueOf(LstPreciosUnitarios.get(i).toString()));
                                if (Double.parseDouble(WebService.ArrayItemsViaje.get(i).getPorc_Iva()) == 5.000) {
                                    neto = Double.valueOf(LstCantEntregadas.get(i).getText().toString()) * Double.valueOf(LstPreciosUnitarios.get(i).toString()) / 1.05;
                                    IVA5 = IVA5 + Double.valueOf(neto * 0.05);
                                    WebService.IVA5 = IVA5;
                                    WebService.Gravada5 = WebService.Gravada5 + Double.valueOf(LstCantEntregadas.get(i).getText().toString()) * Double.valueOf(LstPreciosUnitarios.get(i).toString());
                                } else {
                                    neto = Double.valueOf(LstCantEntregadas.get(i).getText().toString()) * Double.valueOf(LstPreciosUnitarios.get(i).toString()) / 1.10;
                                    IVA10 = IVA10 + (Double.valueOf(neto * 0.10));
                                    WebService.IVA10 = IVA10;
                                    WebService.Gravada10 = IVA10;
                                }
                            }

                            //Aca hay que cargar los valores a la clase webservice
                            Total = TotalSinIva;

                            WebService.Total = WebService.Total + TotalSinIva;
                            WebService.IVA5 = IVA5;
                            WebService.IVA10 = IVA10;

                            WebService.TotalFactura = (int) Total;
                            TotalSinIva = (int) Total - ((int) IVA10 + (int) IVA5);
                            int ivaTotal = ((int) IVA5 + (int) IVA10);
                            int total = (int) Total;
                            int iv10 = (int) IVA10;
                            int iv5 = (int) IVA5;
                            int TotSinIv = (int) TotalSinIva;

                            WebService.ivaTotal = ivaTotal;
                            WebService.total = total;
                            WebService.iv10 = iv10;
                            WebService.iv5 = iv5;
                            WebService.TotSinIv = TotSinIv;
                        }
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
            });*/

            /* SETEO LOS VALORES DE LOS PRODUCTOS DE ACUERDO A LA CONFIGURACION RECIBIDA */
            int preUnit = (int) itm.getPrecio_Unitario();
            view.datoUnit.setText(Utilidad.GenerarFormato((preUnit)));
            LstPreciosUnitarios.add(Double.valueOf(preUnit));

            double valorIva = Double.valueOf(itm.getPorc_Iva());
            int iv = (int) valorIva;
            view.datoIva.setText("     " + Utilidad.GenerarFormato(iv));

            final double sub_tot = Math.floor(itm.getCantidad() * itm.getPrecio_Unitario());
            int s_tot = (int) sub_tot;
            view.datoSubTotal.setText(Utilidad.GenerarFormato(s_tot));

            //Aca cargamos el IVA para mostrar
            if (Double.parseDouble(itm.getPorc_Iva()) == 5.000) {
                neto = sub_tot / 1.05;
                IVA5 = IVA5 + (neto * 0.05);
                WebService.Gravada5 = WebService.Gravada5 + sub_tot;
            } else {
                neto = sub_tot / 1.10;
                IVA10 = IVA10 + (neto * 0.10);
                WebService.Gravada10 = WebService.Gravada10 + sub_tot;
            }

            if(WebService.configuracion.getHabilita_reclamo().equals("N")){
                view.reclamo.setVisibility(View.GONE);
            }
            if(WebService.configuracion.getHabilita_reclamo().equals("S") && WebService.configuracion.getNivel_reclamo().equals("L")){
                view.reclamo.setVisibility(View.VISIBLE);
            }else{
                //ACA SE DEJA PREPARADO PARA NIVEL DE RECLAMO POR CABECERA Y NO POR LINEA....
            }

            view.reclamo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        TraerReclamos task = new TraerReclamos(new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) {
                                if (!WebService.errToken.equals("")) {
                                    Intent myIntent = new Intent(context, Login.class);
                                    context.startActivity(myIntent);
                                }else {
                                    WebService.instaItem = itm;
                                    activity.openDialogReclamo();
                                }
                            }
                        });
                        task.execute();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
       /* if(position == arrayItems.size() - 1){
            activity.CargarDatosProductos();
        }*/

        return convertView;
    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    private class TraerReclamos extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog(context);
        public AsyncResponse delegate = null;//Call back interface

        public TraerReclamos(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {
                RequestParams params2 = new RequestParams();
                WebService.TraerReclamos("Consultas/TraerReclamos.php");
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
}
