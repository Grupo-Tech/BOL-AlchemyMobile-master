package com.example.user.trucksales.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.trucksales.Visual.Cobranza.IngresarTransferencias;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.Transferencias;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdaptadorTransferencias extends BaseAdapter {


    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 123 ;
    private static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 123;


    private final AdaptadorTransferencias adaptador;
    private IngresarTransferencias activity; //Activity desde el cual se hace referencia al llenado de la lista
    private List<Transferencias> arrayItems; // Lista de items


    // Constructor con parámetros que recibe la Acvity y los datos de los items.
    public AdaptadorTransferencias(IngresarTransferencias activity, List<Transferencias> items){
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
        public ImageView ico_eliminar;
        public TextView txtBanco,txtDetalle,txtBancoDestino,txtCuentadestino,txtImporte;
        //public TextView txtDireccion;
    }

    // Método que retorna la vista formateada
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Fila view = new Fila();
        LayoutInflater inflator = activity.getLayoutInflater();
        final Transferencias itm = arrayItems.get(position);
        final View convertViewAux;
        /*
        Condicional para recrear la vista y no distorcionar el número de elementos
         */

        if(convertView==null)      {
            convertView = inflator.inflate(R.layout.item_transferencias, parent, false);

            view.ly_cliente= (RelativeLayout) convertView.findViewById(R.id.ly_cliente);
            //iew.txtTotal = (TextView) convertView.findViewById(R.id.txtTotal);
            view.ico_eliminar = (ImageView) convertView.findViewById(R.id.ico_eliminar);

            view.txtBanco = (TextView) convertView.findViewById(R.id.txtBanco);
            view.txtDetalle = (TextView) convertView.findViewById(R.id.txtDetalle);
            view.txtBancoDestino = (TextView) convertView.findViewById(R.id.txtBancoDestino);
            view.txtCuentadestino = (TextView) convertView.findViewById(R.id.txtCuentadestino);
            view.txtImporte = (TextView) convertView.findViewById(R.id.txtImporte);

            //view.txtDireccion = (TextView) convertView.findViewById(R.id.txtDireccion);

            convertView.setTag(view);
        }   else        {
            view = (Fila)convertView.getTag();
        }
        convertViewAux=convertView;
        // Se asigna el dato proveniente del objeto
        final int order=position+1;

        String importe = itm.getImporte().toString();

        if(WebService.configuracion.getTransf_completa().equals("N")){
           // view.txtBanco.setText("Nº "+ itm.getDetalle().trim());
            if (itm.getCodMon().trim().equals("1")){
                view.txtBanco.setText(WebService.simboloMonedaNacional + " " + importe);
            }
            else{
                view.txtBanco.setText(WebService.simboloMonedaTr + " " + importe);
            }

            view.txtBancoDestino.setVisibility(View.INVISIBLE);
            view.txtCuentadestino.setVisibility(View.INVISIBLE);
            view.txtImporte.setVisibility(View.INVISIBLE);
            view.txtDetalle.setVisibility(View.INVISIBLE);

        }else {

            view.txtBanco.setText(itm.getBanco().trim());
            view.txtDetalle.setText("Nº " + itm.getDetalle().trim());

            view.txtBancoDestino.setText(itm.getBancodestino().trim());
            view.txtCuentadestino.setText("Nº " + itm.getCuentadestino().trim());

            //importe = PerfectDecimal(importe, 10000, 2);

            if (itm.getCodMon().trim().equals("1")) {
                view.txtImporte.setText(WebService.simboloMonedaNacional + " " + importe);
            } else {
                view.txtImporte.setText(WebService.simboloMonedaTr + " " + importe);
            }
        }

       // view.txtImporte.setText(WebService.simboloMonedaNacional+" "+importe);
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
            }
        });

        view.ico_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebService.removeTransferencia(itm);
                activity.refreshList();
            }
        });
        return convertView;
    }
}