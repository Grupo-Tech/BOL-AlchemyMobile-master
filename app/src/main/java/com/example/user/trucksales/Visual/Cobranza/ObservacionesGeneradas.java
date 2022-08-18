package com.example.user.trucksales.Visual.Cobranza;

import android.content.Context;
import android.content.Intent;
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
import com.example.user.trucksales.Encapsuladoras.Observaciones;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ObservacionesGeneradas extends AppCompatActivity {

    public static TableLayout tablaGenerados;
    private static Context contextG;
    TextView nombreUsuario, txtFecha, holaUsu;
    private static ImageView flecha;
    private Utilidades Utilidad;

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        try {
            setContentView( R.layout.activity_observaciones_generadas);
            contextG = this;
            Utilidad = new Utilidades( contextG );
            try {
                if (WebService.USUARIOLOGEADO != null) {
                    if (Utilidad.isNetworkAvailable()) {

                        flecha = findViewById(R.id.btnAtras);
                        flecha.setClickable(true);
                        flecha.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Utilidad.vibraticionBotones(contextG);
                                Intent myIntent = new Intent(v.getContext(), ConsultasCobrador.class);
                                startActivity(myIntent);
                            }
                        });
                        holaUsu = (TextView) findViewById(R.id.Rotulo);
                        holaUsu.setText(getResources().getString(R.string.Hola) + " " + WebService.USUARIOLOGEADO + " " + getResources().getString(R.string.Tienes) + " " + WebService.listaobservaciones.size() + " " + getResources().getString(R.string.motGen));
                        tablaGenerados = findViewById(R.id.tabla);
                        nombreUsuario = findViewById(R.id.nombreUsuario);
                        nombreUsuario.setText(getResources().getString(R.string.usuarioTool) + WebService.USUARIOLOGEADO);

                        String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                        txtFecha = (TextView) findViewById(R.id.Fecha);
                        txtFecha.setText(timeStamp);

                        TableRow tr1 = new TableRow(this);
                        TableRow tr2 = new TableRow(this);

                        if (WebService.listaobservaciones.size() == 0) {
                            final TextView mensaje = new TextView(contextG);
                            mensaje.setText(getResources().getString(R.string.Observ));
                            tablaGenerados.addView(tr1);
                        }

                        if (WebService.listaobservaciones.size() != 0) {
                            for (int i = 0; WebService.listaobservaciones.size() > i; i++) {
                                try {
                                    final Observaciones n_rec = WebService.listaobservaciones.get(i);
                                    tr1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tr2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 20));
                                    tr1 = new TableRow(contextG);
                                    tr2 = new TableRow(contextG);

                                    final TextView cliente = new TextView(contextG);
                                    final TextView espacio = new TextView(contextG);
                                    espacio.setText("\n");

                                    cliente.setText(getResources().getString(R.string.clienteGenerados) + " " + n_rec.getCod_tit().trim() + "\n" +
                                            getResources().getString(R.string.Observaciones) + " " + n_rec.getDescripcion() + "\n" +
                                            getResources().getString(R.string.MotNoCob) + " " + n_rec.getCod_motivo());

                                    cliente.setTextSize(15);
                                    cliente.setBackgroundResource(R.color.viajes);

                                    TableRow.LayoutParams Parametros = new TableRow.LayoutParams();
                                    Parametros.weight = (float) 0.5;
                                    Parametros.width = 400;
                                    Parametros.rightMargin = 100;
                                    cliente.setLayoutParams(Parametros);

                                    tr1.addView(cliente);
                                    tr2.addView(espacio);

                                    tr1.setPadding(0, 20, 0, 30);
                                    tr1.setBackgroundResource(R.color.viajes);
                                    tr2.setBackgroundResource(R.color.blanco);

                                    tablaGenerados.addView(tr1);
                                    tablaGenerados.addView(tr2);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    //Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                                }
                            }
                        }
                    }else{
                        Utilidad.CargarToastConexion(contextG);
                    }
                }else {
                    Intent myIntent = new Intent( contextG, Login.class );
                    startActivity( myIntent );
                }
            } catch (Exception error) {
                error.printStackTrace();
                //Toast.makeText( getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG ).show();
            }
            GuardarDatosUsuario.Contexto = contextG;
        } catch (Exception error) {
            error.printStackTrace();
            //Toast.makeText( getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }//Cierra el oncreate

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            Utilidad.vibraticionBotones(contextG);
            Intent myIntent = new Intent(contextG, ConsultasCobrador.class);
            startActivity(myIntent);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
