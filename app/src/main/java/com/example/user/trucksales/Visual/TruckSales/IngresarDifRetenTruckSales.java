package com.example.user.trucksales.Visual.TruckSales;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.Diferencia_Reten;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.adapter.AdaptadorLeyTruckSales;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class IngresarDifRetenTruckSales extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public Utilidades Utilidad;
    public static Context contexto;
    ImageView atras;
    private static EditText importe;
    private EditText fecha;


    private Button btnValores;
    ListView tablaLey;
    private AdaptadorLeyTruckSales dataAdapterRuta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView( R.layout.activity_ingresar_difreten);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        contexto = this;
        Utilidad = new Utilidades(contexto);

        if(WebService.usuarioActual!=null) {
            if (Utilidad.isNetworkAvailable()) {
                fecha = findViewById(R.id.fecha);
                importe = findViewById(R.id.importe);

                importe.setInputType(InputType.TYPE_CLASS_NUMBER);

                Double valorUSD = 0D;
                Double valor$ = 0D;
                for(int i = 0; i<WebService.valoresPago.size(); i++){
                    if(WebService.valoresPago.get(i).getCod_moneda().trim().equals("2")){
                        valorUSD = valorUSD + (WebService.valoresPago.get(i).getImporte() * WebService.tipoCambio);
                    }else{
                        valor$ = valor$ + WebService.valoresPago.get(i).getImporte();
                    }
                }
                Double dif =  WebService.totalDeudas2 - valorUSD - valor$;
                //Double total = WebService.diferenciaValores + WebService.acuentaValores2;

                if(WebService.monedaSeleccionada.getCod_Moneda().trim().equals("2")){
                    dif = (WebService.totalDeudas2 * WebService.tipoCambio) - valorUSD - valor$;
                }

                Double total = dif;

                total = Utilidad.redondearDecimales(total, 0);
                importe.setText(NumberFormat.getInstance(Locale.ITALY).format(total).toString());

                importe.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        importe.removeTextChangedListener(this);

                        try {
                            String originalString = s.toString();

                            Long longval;
                            if (originalString.contains(",")) {
                                originalString = originalString.replaceAll(",", "");
                            }
                            longval = Long.parseLong(originalString);

                            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                            formatter.applyPattern("#,###,###,###");
                            String formattedString = formatter.format(longval);

                            //setting text after format to EditText
                            importe.setText(formattedString);
                            importe.setSelection(importe.getText().length());
                        } catch (NumberFormatException nfe) {
                            nfe.printStackTrace();
                            //Toast.makeText( getApplicationContext(), nfe.getMessage(), Toast.LENGTH_LONG ).show();
                        }

                        importe.addTextChangedListener(this);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                tablaLey = (ListView) findViewById(R.id.tabla);
                dataAdapterRuta = new AdaptadorLeyTruckSales(IngresarDifRetenTruckSales.this, WebService.dif_reten);

                tablaLey.setAdapter(dataAdapterRuta);

                btnValores = findViewById(R.id.btnValores);

                atras = findViewById(R.id.btnAtras);
                atras.setClickable(true);
                atras.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Utilidad.vibraticionBotones(contexto);
                        Intent myIntent = new Intent(v.getContext(), IngresarValoresTruckSales.class);
                        startActivity(myIntent);
                        finish();
                    }
                });

                btnValores.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utilidad.vibraticionBotones(contexto);
                        agregar(dataAdapterRuta);
                        if (!dataAdapterRuta.isEmpty()) {
                            Intent myIntent = new Intent(contexto, IngresarValoresTruckSales.class);
                            startActivity(myIntent);
                        }
                    }
                });

            }else{
                Utilidad.CargarToastConexion(contexto);
            }
        }else {
            Intent myIntent = new Intent( contexto, Login.class );
            startActivity( myIntent );
        }

    }

    public void refreshList() {
        dataAdapterRuta.notifyDataSetChanged();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        Utilidad.vibraticionBotones(contexto);
        Intent myIntent = new Intent(contexto, IngresarValoresTruckSales.class);
        startActivity(myIntent);
        finish();
    }

    private void agregar(AdaptadorLeyTruckSales dataAdapterRuta) {
        if (Utilidad.isNetworkAvailable()) {
            try {
                if (importe.getText().toString().equals("") || importe.getText().toString().equals("0.0")) {
                    Toast.makeText(getApplicationContext(), "Debe ingresar el monto", Toast.LENGTH_LONG).show();
                } else {

                    String importes = importe.getText().toString();

                    importes = importes.replace(",", "");

                    importes = importes.replace(".", "");

                   /* if(WebService.diferenciaValores > 0){
                        WebService.addDif(new Diferencia_Reten(Double.valueOf(importes)));
                    }else {
                        WebService.addDif(new Diferencia_Reten(Double.valueOf("-" + importes)));
                    }*/

                    WebService.addDif(new Diferencia_Reten(Double.valueOf(importes)));

                    importe.setText("");

                    dataAdapterRuta.notifyDataSetChanged();
                }
            }catch (Exception ex){
                ex.printStackTrace();
               // Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
            }
        }else{
            Utilidad.CargarToastConexion(contexto);
        }
    }

}
