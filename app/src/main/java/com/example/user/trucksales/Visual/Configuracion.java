package com.example.user.trucksales.Visual;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;

import java.util.ArrayList;
import java.util.List;

public class Configuracion extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Spinner sp;
    List<String> spinnerArray =  new ArrayList<String>();
    static Context contConf;
    TextView txtURL;
    EditText TextoURL;
    String UltimURL;
    Button Guardar;
    Utilidades Utilidad;

    public static Context getAppContext() { return Configuracion.contConf; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        try {
            contConf = this;
            Utilidad = new Utilidades(contConf);
            UltimURL = WebService.URL;
            //txtCodEmp = findViewById( R.id.textoEmpresa );
            //txtEntorno = findViewById( R.id.textoEntorno );
            txtURL = findViewById(R.id.textoUrl);
            sp = findViewById(R.id.SpConfiguraciones);
            sp.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, spinnerArray);
            spinnerArray.add("Seleccionar...");
            spinnerArray.add("Servidor de pruebas");
            spinnerArray.add("Servidor de produccion");
            TextoURL = findViewById(R.id.textoUrlEditable);
       /* for (int i = 0; i < WebService.empresasRegistradas.size(); i++) {
            String nombreAgregar = WebService.empresasRegistradas.get( i ).getCod_Empresa();
            spinnerArray.add( nombreAgregar );
        }*/
            Guardar = findViewById(R.id.CargarConfig);
            Guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utilidad.vibraticionBotones(contConf);
                    WebService.GuardarPreferecia(TextoURL.getText().toString().trim());
                    Intent myIntent = new Intent(v.getContext(), Login.class);
                    startActivity(myIntent);
                    finish();

                }
            });
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp.setAdapter(dataAdapter);
            TextoURL.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    /*if (!WebService.URL.trim().equals(TextoURL.getText().toString().trim())) {
                        WebService.URL = WebService.URL.replace(UltimURL, TextoURL.getText().toString().trim());
                        WebService.URL_PRINT = WebService.URL_PRINT.replace(UltimURL, TextoURL.getText().toString().trim());
                        WebService.URL_PRINT_CAJAS = WebService.URL_PRINT_CAJAS.replace(UltimURL, TextoURL.getText().toString().trim());
                        UltimURL = WebService.URL;
                    }*/
                }
            });

            //agregado probar
            if (!WebService.CargarPreferencias().equals("")){
                TextoURL.setText(WebService.CargarPreferencias());
            }

        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(sp.getSelectedItem().toString().equals("Servidor de pruebas")){
            /* if(WebService.URL.contains("mobileProd")) {
                    WebService.URL =  WebService.URL.replace( "mobileProd", "mobileDesa");
                    WebService.URL_PRINT = WebService.URL_PRINT.replace("mobileProd", "mobileDesa"  );
                    WebService.URL_PRINT_CAJAS = WebService.URL_PRINT_CAJAS.replace( "mobileProd", "mobileDesa" );
            }*/
                    //TOYOTOSHI
           /*if(WebService.URL.contains("CobranzasMovilProd")) {
                    WebService.URL =  WebService.URL.replace( "CobranzasMovilProd", "CobranzasMovil");
                    WebService.URL_PRINT = WebService.URL_PRINT.replace("CobranzasMovilProd", "CobranzasMovil"  );
            }*/
           WebService.URL = WebService.URL_BASE2 + WebService.configuracion.getWs_pruebas();

        }else if(sp.getSelectedItem().toString().equals("Servidor de produccion")){
           /* if(WebService.URL.contains( "mobileDesa" )){
                    WebService.URL = WebService.URL.replace("mobileDesa", "mobileProd");
                    WebService.URL_PRINT = WebService.URL_PRINT.replace("mobileDesa", "mobileProd");
                    WebService.URL_PRINT_CAJAS = WebService.URL_PRINT_CAJAS.replace("mobileDesa", "mobileProd");
            }
                //TOYOTOSHI
                if(WebService.URL.contains( "CobranzasMovil" )) {
                    WebService.URL = WebService.URL.replace("CobranzasMovil", "CobranzasMovilProd");
                    WebService.URL_PRINT = WebService.URL_PRINT.replace("CobranzasMovil", "CobranzasMovilProd");
                }*/
           WebService.URL = WebService.URL_BASE + WebService.configuracion.getWs_produccion();

        }
        txtURL.setText( getResources().getString( R.string.ruta ) + " " );
        //TextoURL.setText( WebService.URL );
       // TextoURL.setText( "http://techgroup.dyndns.biz:85/TSmobile/Servicios" );
        TextoURL.setText( "https://trucksales.toyosa-test.com/TSmobile/Servicios" );

       /* txtEntorno.setText( getResources().getString( R.string.Entorno ) + " " );
        txtCodEmp.setText( getResources().getString( R.string.cod_empre ) + " " );*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void onBackPressed() {
        Utilidad.vibraticionBotones( contConf );
    }
}
