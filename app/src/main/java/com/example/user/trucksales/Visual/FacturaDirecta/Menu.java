package com.example.user.trucksales.Visual.FacturaDirecta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.Visual.TruckSales.Viajes;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Menu extends Activity {

    TextView nomUsuario,txtFecha,holaUsu;
    Button btnNota,btnFactura;
    private static ImageView flecha;
    Context contexto;
    public static RequestParams params1= new RequestParams();
    private Utilidades Utilidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_seleccion_funcionablidad );
        contexto = this;
        Utilidad = new Utilidades( contexto );
        WebService.EstadoActual = 1;
        try {
            if (WebService.USUARIOLOGEADO != null) {

                nomUsuario = (TextView) findViewById(R.id.nombreUsuario);
                nomUsuario.setText(getResources().getString(R.string.usuarioTool) + WebService.USUARIOLOGEADO);
                String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                txtFecha = (TextView) findViewById(R.id.Fecha);
                txtFecha.setText(timeStamp);
                flecha = findViewById(R.id.btnAtras);
                flecha.setClickable(true);
                flecha.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        try{
                        Utilidad.vibraticionBotones(contexto);
                        Intent myIntent = new Intent(v.getContext(), Viajes.class);
                        startActivity(myIntent);
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });

                btnNota = findViewById(R.id.btnCobranza);
                btnFactura = findViewById(R.id.BtnTS);

                btnNota.setText(getResources().getString(R.string.NotaCredito));
                btnFactura.setText(getResources().getString(R.string.Fact));

                btnNota.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent nextActivity = new Intent(contexto, BuscarFactura.class);
                        startActivity(nextActivity);
                    }
                });

                btnFactura.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent nextActivity = new Intent(contexto, FacturaDirecta.class);
                        startActivity(nextActivity);
                    }
                });


            } else {
                Intent myIntent = new Intent(contexto, Login.class);
                startActivity(myIntent);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try{
        Utilidad.vibraticionBotones(contexto);
        Intent myIntent = new Intent(contexto, Viajes.class);
        startActivity(myIntent);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
