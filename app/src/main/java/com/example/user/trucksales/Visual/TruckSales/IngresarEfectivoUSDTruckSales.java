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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.EfectivoUSD;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.adapter.AdaptadorEfectivoUSDTruckSales;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class IngresarEfectivoUSDTruckSales extends AppCompatActivity {

    private Utilidades Utilidad;
    private static Context context;
    private static int val_tot = 0;
    ImageView atras;
    private static EditText input;

    public int decimales = 2;

    private static int cont_filas = 0;

    private Button btnValores;
    private EditText importe;
    ListView tablaCheques;
    private AdaptadorEfectivoUSDTruckSales dataAdapterRuta;

    @Override
    protected void onResume() {
        super.onResume();

        val_tot = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  this.requestWindowFeature( Window.FEATURE_NO_TITLE );

        setContentView(R.layout.activity_ingresar_efectivousd);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        context = this;
        Utilidad = new Utilidades(context);

        if (WebService.usuarioActual != null) {
            try {
                if (Utilidad.isNetworkAvailable()) {
                    importe = findViewById(R.id.importe);

                    Double total = WebService.diferenciaValores;

                    //if (WebService.intereses == false) {
                    if (WebService.monedaSeleccionada.getCod_Moneda().trim().equals("1")) {
                        total = Utilidad.redondearDecimales(total / WebService.tipoCambio, 2);
                        //total = Double.valueOf(Utilidad.PerfectDecimal(total.toString(), 10000, 2));
                    }
                    importe.setText(total.toString());
               /* } else {
                    importe.setText("");
                }*/

                    importe.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

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
                                formatter.applyPattern("#,###,###,###.##");
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
                            try {
                                String str = importe.getText().toString();
                                if (str.isEmpty()) return;
                                String str2 = Utilidad.PerfectDecimal(str, 1000, decimales);
                                if (!str2.equals(str)) {
                                    importe.setText(str2);
                                    int pos = importe.getText().length();
                                    importe.setSelection(pos);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                            }
                        }
                    });

                    tablaCheques = (ListView) findViewById(R.id.tabla);


                    dataAdapterRuta = new AdaptadorEfectivoUSDTruckSales(IngresarEfectivoUSDTruckSales.this, WebService.efectivoUSD);

                    // Desplegamos los elementos en el ListView
                    tablaCheques.setAdapter(dataAdapterRuta);

                    btnValores = findViewById(R.id.btnValores);

                    atras = findViewById(R.id.btnAtras);
                    atras.setClickable(true);
                    atras.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(context);
                            Intent myIntent = new Intent(v.getContext(), IngresarValoresTruckSales.class);
                            startActivity(myIntent);
                            finish();
                        }
                    });

                    btnValores.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(context);
                            agregar(dataAdapterRuta);
                            if (!dataAdapterRuta.isEmpty()) {
                                Intent myIntent = new Intent(context, IngresarValoresTruckSales.class);
                                startActivity(myIntent);
                            }
                        }
                    });
                }else{
                    Utilidad.CargarToastConexion(context);
                }
            }catch (Exception ex){
                ex.printStackTrace();
                //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
            }
        }else {
            Intent myIntent = new Intent(context, Login.class);
            startActivity(myIntent);
        }
    }

    private void agregar(AdaptadorEfectivoUSDTruckSales dataAdapterRuta) {
        if (Utilidad.isNetworkAvailable()) {
            try {
                if (importe.getText().toString().equals("") || importe.getText().toString().equals("0.0")) {

                    Toast.makeText(getApplicationContext(), "Debe completar todos los datos", Toast.LENGTH_LONG).show();
                } else {

                    String importes = importe.getText().toString();

                    if (importes.contains(",")) {
                        importes = Utilidad.NumeroSinComa(importes);
                    }

                    Double importa = Double.parseDouble(importes);
                    importa = Utilidad.redondearDecimales(importa, 2);

                    WebService.addEfectivoUSD(new EfectivoUSD(Double.valueOf(importa)));

                    importe.setText("");

                    dataAdapterRuta.notifyDataSetChanged();
                }
            }catch (Exception ex){
                ex.printStackTrace();
               // Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
            }
        }
    }


    public void refreshList() {

        dataAdapterRuta.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Utilidad.vibraticionBotones(context);
        Intent myIntent = new Intent(context, IngresarValoresTruckSales.class);
        startActivity(myIntent);
        finish();
    }
}
