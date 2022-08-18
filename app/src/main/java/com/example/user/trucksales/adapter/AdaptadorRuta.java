package com.example.user.trucksales.adapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.ClienteCobranza;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Cobranza.MenuCobranzas;
import com.example.user.trucksales.Visual.Cobranza.SeleccionarDeudas;
import com.example.user.trucksales.Visual.Cobranza.SeleccionarMoneda;
import com.loopj.android.http.RequestParams;

import java.util.List;

public class AdaptadorRuta extends BaseAdapter {


    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 123 ;
    private static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 123;


    private final AdaptadorRuta adaptador;
    private MenuCobranzas activity; //Activity desde el cual se hace referencia al llenado de la lista
    private List<ClienteCobranza> arrayItems; // Lista de items


    // Constructor con parámetros que recibe la Acvity y los datos de los items.
    public AdaptadorRuta(MenuCobranzas activity, List<ClienteCobranza> items){
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

        public LinearLayout ly_cliente;
        public ImageView ico_eliminar;
        public TextView txtNombre;
        //public TextView txtDireccion;
    }

    // Método que retorna la vista formateada
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Fila view = new Fila();
        LayoutInflater inflator = activity.getLayoutInflater();
        final ClienteCobranza itm = arrayItems.get(position);
        final View convertViewAux;
        /*
        Condicional para recrear la vista y no distorcionar el número de elementos
         */

        if(convertView==null)      {
            convertView = inflator.inflate( R.layout.items_rutas, parent, false);

            view.ly_cliente= (LinearLayout) convertView.findViewById(R.id.ly_cliente);
            //iew.txtTotal = (TextView) convertView.findViewById(R.id.txtTotal);
            view.ico_eliminar = (ImageView) convertView.findViewById(R.id.ico_eliminar);
            view.txtNombre = (TextView) convertView.findViewById(R.id.txtNombre);
            //view.txtDireccion = (TextView) convertView.findViewById(R.id.txtDireccion);

            convertView.setTag(view);
        }   else        {
            view = (Fila)convertView.getTag();
        }
        convertViewAux=convertView;
        // Se asigna el dato proveniente del objeto
        final int order=position+1;


        view.txtNombre.setText(itm.getNom_Tit().trim());
        //view.txtDireccion.setText(itm.getServiciotipo().getNombre());
        //view.txtTotal.setText("$ "+itm.getPrecio());
        /*
        Double precioFinal = Double.valueOf(itm.getPrecioganancia()) - Double.valueOf(itm.getPrecio());
        view.txtTotal.setText("("+itm.getCantidad()+" x "+precioFinal+")");
        Double total=(Double.valueOf(itm.getPrecioganancia()) - Double.valueOf(itm.getPrecio()) ) * Long.valueOf(itm.getCantidad());
        view.txtNombre.setText("$ "+total);
        */
        // Retornamos la vista

        view.ly_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.isNetworkAvailable())                {
                    WebService.clienteActual = itm;
                    TraerMonedas task = new TraerMonedas(new MenuCobranzas.AsyncResponse() {
                        public void processFinish(Object output)
                        {
                            TraerDeudores task2 = new TraerDeudores(new MenuCobranzas.AsyncResponse() {
                                public void processFinish(Object output) {
                                    if(WebService.deudasViaje.size()>0)
                                    {
                                        String moenda_compa = WebService.deudasViaje.get( 0 ).getCod_Moneda();
                                        boolean banderaDisMoneda = false;
                                        ClienteCobranza instaCli= new ClienteCobranza();
                                        for(int i = 1; i<WebService.deudasViaje.size(); i++)
                                        {
                                            instaCli = WebService.deudasViaje.get( i );
                                            if(!instaCli.getCod_Moneda().equals( moenda_compa ))
                                            {
                                                WebService.distintasMonedas = true;
                                            }
                                        }
                                        if(WebService.distintasMonedas)
                                        {
                                            Intent myIntent = new Intent( activity.contexto, SeleccionarMoneda.class );
                                            activity.startActivity( myIntent );
                                        }
                                        else
                                        {
                                            Intent myIntent = new Intent( activity.contexto, SeleccionarDeudas.class );
                                            activity.startActivity( myIntent );
                                        }
                                    }


                                }
                            });
                            task2.execute(  );

                        }
                    } );
                    task.execute(  );
                }

            }
        });
        return convertView;
    }



    public class  TraerDeudores extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( activity.contexto);
        public MenuCobranzas.AsyncResponse delegate = null;//Call back interface
        public TraerDeudores(MenuCobranzas.AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            if(activity.isNetworkAvailable())    {
                RequestParams params1 = new RequestParams(  );
                params1.put( "cod_tit",WebService.clienteActual.getCod_Tit() );
                params1.put( "cod_empresa",WebService.usuarioActual.getEmpresa().trim() );
                params1.put("username", WebService.USUARIOLOGEADO.trim());
                WebService.TraerDeudas(params1);
                return null;

            } else  {
            }

            return null;
        }
        @Override
        public void onPreExecute()
        {
            dialog1.setMessage(activity.getResources().getString( R.string.cargando_dialog));
            dialog1.show();
        }
        @Override
        protected void onPostExecute(Void result) {
            if(dialog1.isShowing())
            {
                dialog1.dismiss();
            }
            delegate.processFinish( WebService.logueado);
            //tv.setText(fahre11n + "� F");
            //dialog();
            //OcultarGif();
        }


        @Override
        protected void onProgressUpdate(Void... values)
        {

        }
    }



    public class  TraerMonedas extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( activity.contexto );
        public MenuCobranzas.AsyncResponse delegate = null;//Call back interface
        public TraerMonedas(MenuCobranzas.AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            if(activity.isNetworkAvailable())
            {

                WebService.ObtenerMonedas();
                return null;

            }
            else
            {
            }

            return null;
        }
        @Override
        public void onPreExecute()
        {
            dialog1.setMessage(activity.getResources().getString( R.string.cargando_dialog));
            dialog1.show();
        }
        @Override
        protected void onPostExecute(Void result) {
            if(dialog1.isShowing())
            {
                dialog1.dismiss();
            }
            delegate.processFinish( WebService.logueado);
            //tv.setText(fahre11n + "� F");
            //dialog();
            //OcultarGif();
        }


        @Override
        protected void onProgressUpdate(Void... values)
        {

        }
    }

}