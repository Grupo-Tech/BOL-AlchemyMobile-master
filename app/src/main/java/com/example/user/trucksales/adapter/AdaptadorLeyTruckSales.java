package com.example.user.trucksales.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.Diferencia_Reten;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Cobranza.IngresarDifReten;
import com.example.user.trucksales.Visual.TruckSales.IngresarDifRetenTruckSales;

import java.util.List;

public class AdaptadorLeyTruckSales extends BaseAdapter {

    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 123 ;
    private static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 123;


    private final AdaptadorLeyTruckSales adaptador;
    private IngresarDifRetenTruckSales activity; //Activity desde el cual se hace referencia al llenado de la lista
    private List<Diferencia_Reten> arrayItems; // Lista de items

    public AdaptadorLeyTruckSales(IngresarDifRetenTruckSales activity, List<Diferencia_Reten> items){
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


    public static class Fila   {

        public RelativeLayout ly_cliente;
        public ImageView ico_eliminar;
        public TextView txtImporte,txtNumero;
    }

    // Método que retorna la vista formateada
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try{
        Fila view = new Fila();
        LayoutInflater inflator = activity.getLayoutInflater();
        final Diferencia_Reten itm = arrayItems.get(position);
        final View convertViewAux;
        /*
        Condicional para recrear la vista y no distorcionar el número de elementos
         */

        if (convertView == null) {
            convertView = inflator.inflate(R.layout.item_ley, parent, false);

            view.ly_cliente = (RelativeLayout) convertView.findViewById(R.id.ly_cliente);
            view.ico_eliminar = (ImageView) convertView.findViewById(R.id.ico_eliminar);
            view.txtImporte = (TextView) convertView.findViewById(R.id.txtImporte);
            convertView.setTag(view);
        } else {
            view = (Fila) convertView.getTag();
        }
        convertViewAux = convertView;
        // Se asigna el dato proveniente del objeto
        final int order = position + 1;

        String importe = itm.getImporte().toString();

        view.txtImporte.setText(WebService.simboloMonedaNacional.trim() + " " + importe);
        //view.txtNumero.setText(itm.getNumero().trim().toString());

        view.ly_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        view.ico_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebService.removeDif(itm);
                activity.refreshList();
            }
        });
        return convertView;
         }catch(Exception ex) {
            ex.printStackTrace();
        }
        return convertView;
    }
}
