package com.example.user.trucksales.Visual.TruckSales;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.Anulada;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.EmptyStackException;

public class Anulados extends Activity {
    Context contexto;
    TextView fecha,usuario,encabezado;
    ImageView atras;
    private static TableLayout tablaAnulados;
    public static String valorIntent;
    Utilidades Utilidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        this.requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_anulados );

        try {
            valorIntent = getIntent().getStringExtra("intent");
        }catch (Exception ex){
            ex.printStackTrace();
            // Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
        }
        
        contexto = this;
        Utilidad = new Utilidades( contexto );
        if(WebService.USUARIOLOGEADO != null)
        {
            atras = findViewById( R.id.btnAtras );
            atras.setClickable( true );
            atras.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    try{
                    Utilidad.vibraticionBotones( contexto );
                    Intent myIntent = new Intent( v.getContext(), Consultas.class );
                    myIntent.putExtra("intent", valorIntent);
                    startActivity( myIntent );
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            } );

            String timeStamp = new SimpleDateFormat( "dd-MM-yyyy" ).format( Calendar.getInstance().getTime() );
            usuario = findViewById( R.id.nombreUsuario );
            usuario.setText( getResources().getString( R.string.usuarioTool ) + WebService.USUARIOLOGEADO );
            encabezado =(TextView) findViewById( R.id.cantiadaTraida );
            String encabe =   getResources().getString( R.string.anuladasHoy )+" "+WebService.ArrayAnuladas.size();
            encabezado.setText(encabe);
            fecha    = (TextView) findViewById( R.id.Fecha );
            fecha.setText( timeStamp );
            tablaAnulados = findViewById( R.id.tabla );
            if(WebService.ArrayAnuladas.size()==0)
            {
                encabezado.setText(getResources().getString( R.string.noHayAnuladas));
            }
            else
            {
                TableRow tr1 = new TableRow( this );
                TableRow tr2 = new TableRow( this );
                for(int i = 0;i<WebService.ArrayAnuladas.size();i++)
                {
                    try
                    {
                        final Anulada instaAnulada= WebService.ArrayAnuladas.get( i );

                      //  tr1.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT ) );
                        tr2.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, 20 ) );
                        tr1 = (TableRow) getLayoutInflater().inflate(R.layout.anulados, null);
                        tr2 = new TableRow( this     );

                        final TextView cliente = new TextView( this );
                        cliente.setText(    getResources().getString( R.string.clienteGenerados )+" "+ instaAnulada.getNom_tit().trim()+"\n"+
                                            getResources().getString( R.string.sucursal)+": "+instaAnulada.getSucursal().trim()+"\n"+
                                            getResources().getString( R.string.NroViaje )+": "+instaAnulada.getCod_tit().trim()+"\n"+
                                            getResources().getString( R.string.ordenEntrega )+" "+instaAnulada.getNro_doc_ref().trim()+"\n"+
                                            getResources().getString( R.string.NroFact )+" "+instaAnulada.getCod_suc_tribut()+" - "+instaAnulada.getCod_fact_tribut()+" - "+instaAnulada.getNro_docum().trim()+"\n"+
                                            getResources().getString( R.string.HroGen )+" "+instaAnulada.getHora());

                        cliente.setTextSize( 15 );
                        cliente.setTextColor( Color.parseColor( "#000000"));
                        final TextView espacio = new TextView( this  );
                        espacio.setText( "\n" );

                        tr1.setPadding( 0, 20, 0, 30 );

                        tr1.addView( cliente );
                        tr2.addView( espacio );

                        tr2.setBackgroundResource( R.color.blanco );
                        tablaAnulados.addView( tr1 );
                        tablaAnulados.addView( tr2 );
                    }
                    catch (Exception exxc)
                    {
                        exxc.printStackTrace();
                        //Toast.makeText( getApplicationContext(), exxc.getMessage(), Toast.LENGTH_LONG ).show();
                    }
                }
            }

        }
        else
        {
            Intent myIntent = new Intent( contexto, Login.class );
            //PONER ESTE STRING
            //myIntent.putExtra("Mensaje","Anulados");
            startActivity( myIntent );
        }
    }

    @Override
    public void onBackPressed() {
        try{
        Utilidad.vibraticionBotones( contexto );
        Intent myIntent = new Intent( contexto, Consultas.class );
        myIntent.putExtra("intent", valorIntent);
        startActivity( myIntent );
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
