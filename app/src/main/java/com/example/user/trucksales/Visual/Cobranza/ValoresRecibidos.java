package com.example.user.trucksales.Visual.Cobranza;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.ValoresPago;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ValoresRecibidos extends AppCompatActivity {
    private Utilidades Utilidad;
    ImageView atras;
    TextView nombreUsu, fecha, total, retorno, TxtCodDocum;
    Context contexto;
    LinearLayout mainLayout;
    private static TableLayout tablaValores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        contexto = this;
        GuardarDatosUsuario.Contexto = contexto;
        setContentView(R.layout.activity_valores_recibidos);
        try {
            if (WebService.USUARIOLOGEADO != null) {
                Utilidad = new Utilidades(contexto);

                if (Utilidad.isNetworkAvailable()) {

                    TxtCodDocum = findViewById(R.id.TxtCodDocum);
                    TxtCodDocum.setText(WebService.formapago.toUpperCase());

                    retorno = findViewById(R.id.solVal);
                    retorno.setVisibility(View.GONE);

                    final String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                    nombreUsu = (TextView) findViewById(R.id.LblUsu);
                    fecha = (TextView) findViewById(R.id.LblFecha);
                    fecha.setText(timeStamp);
                    nombreUsu.setText(getResources().getString(R.string.usuarioTool) + WebService.USUARIOLOGEADO);
                    mainLayout = findViewById(R.id.mainLay);

                    tablaValores = (TableLayout) findViewById(R.id.tabla);

                    atras = findViewById(R.id.btnAtras);
                    atras.setClickable(true);
                    atras.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                Utilidad.vibraticionBotones(contexto);
                                Intent myIntent = new Intent(v.getContext(), CierreCaja.class);
                                startActivity(myIntent);
                                finish();
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    });

                    CrearValores(WebService.formapago);

                } else {
                    Intent myIntent = new Intent(contexto, Login.class);
                    startActivity(myIntent);
                }
            }else{
                Utilidad.CargarToastConexion(contexto);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }

    public void CrearValores(String cod_docum) {
        tablaValores.removeAllViews();
        String moneda = "";

        for (int i = 0; i < WebService.ArrayValores.size(); i++) {
            try {
                final ValoresPago instavaloresPago = WebService.ArrayValores.get(i);
                if(instavaloresPago.getCod_docum().equals(cod_docum)) {

                    TableRow tr1 = (TableRow) getLayoutInflater().inflate(R.layout.trviajes, null);
                    TableRow tr2 = new TableRow(this);
                    final TextView salto = new TextView(this);
                    salto.setHeight(10);
                    tr2.addView(salto);

                    final ImageView cliIcon = new ImageView(contexto);
                    cliIcon.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    cliIcon.getLayoutParams().height = 110;
                    cliIcon.getLayoutParams().width = 110;
                    cliIcon.requestLayout();
                    tr1.addView(cliIcon);

                    if(instavaloresPago.getEstado2() == 0) {
                        cliIcon.setImageResource(R.drawable.positive);
                    }else {
                        cliIcon.setImageResource(R.drawable.negative);
                    }

                    if (instavaloresPago.getCod_moneda().equals("1")) {
                        moneda = WebService.simboloMonedaNacional.trim();
                    } else {
                        moneda = WebService.simboloMonedaTr.trim();
                    }

                    final TextView CodDocum = new TextView(contexto);
                    CodDocum.setTextSize(15);
                    CodDocum.setTextColor(Color.parseColor("#000000"));
                    tr1.addView(CodDocum);
                    tr1.setClickable(true);
                    tr1.setBackgroundResource(R.color.viajes);

                    int nro_docum = instavaloresPago.getNro_docum();

                    /*if (instavaloresPago.getNro_docum() == 0) {
                        CodDocum.setText(getResources().getString(R.string.Fecha) + " " + instavaloresPago.getFec_valor().substring(0, 10) + "\n" + moneda + " " + instavaloresPago.getImporte());
                    } else {*/
                        CodDocum.setText(" " + getResources().getString(R.string.Nro) + " " + nro_docum + " " + moneda + " " + NumberFormat.getInstance(Locale.ITALY).format(instavaloresPago.getImporte())  + "\n " +  getResources().getString(R.string.Fecha) + " " + instavaloresPago.getFec_valor().substring(0, 10));
                    //}
                    tablaValores.addView(tr1);
                    tablaValores.addView(tr2);

                    cliIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(contexto);
                            try {
                                if(instavaloresPago.getEstado2() == 0) {
                                    cliIcon.setImageResource(R.drawable.negative);
                                    instavaloresPago.setEstado2(1);
                                    instavaloresPago.setEstadoEntregado(1);
                                }
                                else{
                                    cliIcon.setImageResource(R.drawable.positive);
                                    instavaloresPago.setEstado2(0);
                                    instavaloresPago.setEstadoEntregado(0);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                               // Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                            }
                        }
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
               // Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            Utilidad.vibraticionBotones(contexto);
            Intent myIntent = new Intent(contexto, CierreCaja.class);
            startActivity(myIntent);
            finish();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
