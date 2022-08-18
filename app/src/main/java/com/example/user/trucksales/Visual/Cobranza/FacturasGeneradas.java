package com.example.user.trucksales.Visual.Cobranza;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.FacturaXDia;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FacturasGeneradas extends AppCompatActivity {

    Context contexto;
    TextView fecha, usuario, encabezado;
    ImageView atras;
    private static TableLayout tablaAnulados;
    Utilidades Utilidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_facturas_generadas);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        contexto = this;
        Utilidad = new Utilidades(contexto);
        if (WebService.USUARIOLOGEADO != null) {
            try {
                if (Utilidad.isNetworkAvailable()) {
                    atras = findViewById(R.id.btnAtras);
                    atras.setClickable(true);
                    atras.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try{
                            Utilidad.vibraticionBotones(contexto);
                            Intent myIntent = new Intent(v.getContext(), ConsultasCobrador.class);
                            startActivity(myIntent);
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    });

                    String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                    usuario = findViewById(R.id.nombreUsuario);
                    usuario.setText(getResources().getString(R.string.usuarioTool) + WebService.USUARIOLOGEADO);
                    encabezado = (TextView) findViewById(R.id.cantiadaTraida);
                    fecha = (TextView) findViewById(R.id.Fecha);
                    fecha.setText(timeStamp);
                    tablaAnulados = findViewById(R.id.tabla);

                    CargarDatos();
                    String encabe = getResources().getString(R.string.facGen) + " " + WebService.listafact.size();
                    encabezado.setText(encabe);

                } else {
                    Utilidad.CargarToastConexion(contexto);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                //Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Intent myIntent = new Intent(contexto, Login.class);
            startActivity(myIntent);
        }
    }

    private void CargarDatos() {
        TableRow tr1 = new TableRow(this);
        TableRow tr2 = new TableRow(this);
        for (int i = 0; i < WebService.listafact.size(); i++) {
            try {
                final FacturaXDia n_rec = WebService.listafact.get(i);

                //  tr1.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT ) );
                tr2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 20));
                tr1 = (TableRow) getLayoutInflater().inflate(R.layout.anulados, null);
                tr2 = new TableRow(this);

                final TextView cliente = new TextView(this);
                cliente.setText(getResources().getString(R.string.clienteGenerados) + " " + n_rec.getNom_Tit().trim() + "\n" +
                        getResources().getString(R.string.Nro_Docum) + " " + n_rec.getNro_Docum().trim() + "\n" +
                        // getResources().getString(R.string.ViajesGen) + " " + n_fac.getCod_Tit().trim() + "\n" +
                        getResources().getString(R.string.FecGen) + " " + n_rec.getHora());
                cliente.setTextSize(15);
                cliente.setBackgroundResource(R.color.viajes);
                cliente.setTextColor(Color.parseColor("#000000"));
                final TextView espacio = new TextView(this);
                espacio.setText("\n");

                tr1.setPadding(0, 20, 0, 30);

                tr1.addView(cliente);
                tr2.addView(espacio);

                tr2.setBackgroundResource(R.color.blanco);
                tablaAnulados.addView(tr1);
                tablaAnulados.addView(tr2);
            } catch (Exception exxc) {
               // Toast.makeText(getApplicationContext(), exxc.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try{
            Utilidad.vibraticionBotones(contexto);
            Intent myIntent = new Intent(contexto, ConsultasCobrador.class);
            startActivity(myIntent);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
