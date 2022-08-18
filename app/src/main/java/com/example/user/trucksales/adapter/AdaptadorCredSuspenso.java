package com.example.user.trucksales.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.Credito_Suspenso;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Cobranza.IngresarCredSuspenso;

import java.util.List;

public class AdaptadorCredSuspenso extends BaseAdapter {

    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 123 ;
    private static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 123;


    private final AdaptadorCredSuspenso adaptador;
    private IngresarCredSuspenso activity; //Activity desde el cual se hace referencia al llenado de la lista
    private List<Credito_Suspenso> arrayItems;

    public AdaptadorCredSuspenso(IngresarCredSuspenso activity, List<Credito_Suspenso> items){
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
        public TextView txtNombre,txtImporte,txtMoneda,txtFecha;
    }

    // Método que retorna la vista formateada
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Fila view = new Fila();
        LayoutInflater inflator = activity.getLayoutInflater();
        final Credito_Suspenso itm = arrayItems.get(position);
        final View convertViewAux;
        /*
        Condicional para recrear la vista y no distorcionar el número de elementos
         */

        if (convertView == null) {
            convertView = inflator.inflate(R.layout.item_creditosus, parent, false);

            view.ly_cliente = (RelativeLayout) convertView.findViewById(R.id.ly_cliente);
            view.ico_eliminar = (ImageView) convertView.findViewById(R.id.ico_eliminar);

            view.txtNombre = (TextView) convertView.findViewById(R.id.txtNumero);
            view.txtImporte = (TextView) convertView.findViewById(R.id.txtImporte);
            view.txtFecha = (TextView) convertView.findViewById(R.id.txtFecha);


            convertView.setTag(view);
        } else {
            view = (Fila) convertView.getTag();
        }
        convertViewAux = convertView;
        // Se asigna el dato proveniente del objeto
        final int order = position + 1;

        view.txtNombre.setText("Nº " + itm.getNumero());

        String importe = itm.getImporte().toString();

        if (itm.getCod_Moneda().trim().equals("1")){
            view.txtImporte.setText(WebService.simboloMonedaNacional + " " + importe);
        }
        else{
            view.txtImporte.setText(WebService.simboloMonedaTr + " " + importe);
        }
        view.txtFecha.setText(itm.getFecha().trim());

        view.ly_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        view.ico_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebService.removeCreditoSus(itm);
                activity.refreshList();
            }
        });
        return convertView;
    }

    public String PerfectDecimal(String str, int MAX_BEFORE_POINT, int MAX_DECIMAL)
    {
        if(str.charAt(0) == '.') str = "0"+str; int max = str.length(); String rFinal = "";
        boolean after = false; int i = 0, up = 0, decimal = 0; char t;
        while(i < max)
        {
            t = str.charAt(i);
            if(t != '.' && after == false){
                up++; if(up > MAX_BEFORE_POINT)
                    return rFinal;
            }
            else if(t == '.')
            { after = true; }
            else{
                decimal++; if(decimal > MAX_DECIMAL) return rFinal; }
            rFinal = rFinal + t; i++; }
        return rFinal;
    }
}
