package com.example.user.trucksales.Visual.FacturaDirecta;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.ClienteCobranza;
import com.example.user.trucksales.Encapsuladoras.Entrega;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.Visual.TruckSales.Consultas;
import com.example.user.trucksales.Visual.TruckSales.Recorrido_Viaje;
import com.example.user.trucksales.Visual.TruckSales.Viajes;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FacturaDirecta extends Activity implements AdapterView.OnItemSelectedListener{

    private Utilidades Utilidad;
    ImageView atras;
    LinearLayout mainLayout;
    Context contexto;

    TextView nombreUsu,fecha, viaje;

    Spinner spSucursales;
    List<String> spinnerSucursalesArray = new ArrayList<>(  );

    Button btnIR;

    AutoCompleteTextView nombreCli,codCli/*, nombreSucu, codSucu*/;
    public List<String> NombreCliente =  new ArrayList<String>();
    public List<String> CodigosCliente = new ArrayList<>();
    public List<String> NombreSucursal =  new ArrayList<String>();
    public List<String> CodigosSucursal = new ArrayList<>();
    public  static  LatLng ubicacionActual;

    public ImageView informe;

    public String cod_tit;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            this.requestWindowFeature(Window.FEATURE_NO_TITLE);

            setContentView(R.layout.activity_factura_directa);
        contexto = this;
        Utilidad = new Utilidades(contexto);

        GuardarDatosUsuario.Contexto = contexto;

        try {
            if (WebService.USUARIOLOGEADO != null) {
                if (Utilidad.isNetworkAvailable()) {

                atras = findViewById(R.id.btnAtras);

                mainLayout = findViewById(R.id.mainLay);
                nombreUsu = findViewById(R.id.LblUsu);
                fecha = findViewById(R.id.LblFecha);
                viaje = findViewById(R.id.viaje);

                spSucursales = findViewById(R.id.spSucursales);
                spSucursales.setOnItemSelectedListener( (AdapterView.OnItemSelectedListener) this );
                final ArrayAdapter<String> dataAdapterSucursales = new ArrayAdapter<String>( this,
                        android.R.layout.simple_spinner_item, spinnerSucursalesArray );

                spSucursales.setVisibility(View.GONE);

                nombreCli = findViewById(R.id.TxtNombreCliente);
                codCli = findViewById(R.id.TxtCodCliente);

                btnIR = findViewById(R.id.btnIR);
                informe = findViewById(R.id.informe);

                informe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(v.getContext(), Consultas.class);
                        myIntent.putExtra("intent", "FacturaDirecta");
                        startActivity(myIntent);
                    }
                });

                nombreUsu.setText(WebService.USUARIOLOGEADO.trim());
                viaje.setText(getResources().getString(R.string.viaje) + WebService.viajeSeleccionado.getNumViaje());
                String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                fecha.setText(timeStamp);

                atras.setClickable(true);
                atras.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Utilidad.vibraticionBotones(contexto);
                            Intent myIntent = new Intent(v.getContext(), Menu.class);
                            startActivity(myIntent);
                        }catch (Exception ex){
                            ex.printStackTrace();
                            Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
                        }
                    }
                });

                final ArrayAdapter<String> adapterNomCli = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, NombreCliente);
                final ArrayAdapter<String> adapterCodCli = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, CodigosCliente);

                if (WebService.ArrayClientes.size() == 0) {
                    TraerClientesVenta task01 = new TraerClientesVenta(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            if (!WebService.errToken.equals("")) {
                                Intent myIntent = new Intent(contexto, Login.class);
                                startActivity(myIntent);
                            }else {
                                CargarSpinner();
                                nombreCli.setAdapter(adapterNomCli);
                                codCli.setAdapter(adapterCodCli);
                            }
                        }
                    });
                    task01.execute();
                } else {
                    CargarSpinner();
                    nombreCli.setAdapter(adapterNomCli);
                    codCli.setAdapter(adapterCodCli);
                }

                nombreCli.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Object item = parent.getItemAtPosition(position);
                        ClienteCobranza instaCliente = new ClienteCobranza();

                        boolean encontrado = false;
                        for (int i = 0; i < WebService.ArrayClientes.size() && !encontrado; i++) {
                            instaCliente = WebService.ArrayClientes.get(i);
                            if (instaCliente.getNom_Tit().trim().equals(item)) {
                                encontrado = true;
                                codCli.setText(instaCliente.getCod_Tit_Gestion().trim());
                                View view2 = getCurrentFocus();
                                view2.clearFocus();
                                if (view2 != null) {
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                                }
                            }
                        }
                    }
                });

                nombreCli.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        boolean handled = false;
                        if (actionId == EditorInfo.IME_ACTION_SEND) {
                            //  sendMessage();
                            handled = true;
                        }
                        return handled;
                    }
                });

                codCli.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Object item = parent.getItemAtPosition(position);
                        ClienteCobranza instaCliente = new ClienteCobranza();
                        boolean encontrado = false;
                        for (int i = 0; i < WebService.ArrayClientes.size() && !encontrado; i++) {
                            instaCliente = WebService.ArrayClientes.get(i);
                            if (instaCliente.getCod_Tit_Gestion().trim().equals(item)) {
                                encontrado = true;
                                nombreCli.setText(instaCliente.getNom_Tit().trim());
                                WebService.clienteActual = instaCliente;
                                spSucursales.setVisibility(View.VISIBLE);
                                TraerSucursales task = new TraerSucursales(new AsyncResponse() {
                                    @Override
                                    public void processFinish(Object output) {
                                        try {
                                            if (!WebService.errToken.equals("")) {
                                                Intent myIntent = new Intent(contexto, Login.class);
                                                startActivity(myIntent);
                                            }else {
                                                if (WebService.ArraySucursales.size() == 0) {
                                                    Toast toast = Toast.makeText(contexto, getResources().getString(R.string.sucursales), Toast.LENGTH_LONG);
                                                    toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                                                    toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                                                    toast.show();//showing the toast is important***/
                                                } else {
                                                    for (int i = 0; i < WebService.ArraySucursales.size(); i++) {
                                                        Entrega instaSucu = new Entrega();
                                                        instaSucu = WebService.ArraySucursales.get(i);

                                                        String nombreAgregar = instaSucu.getNom_Sucursal().trim();
                                                        spinnerSucursalesArray.add(nombreAgregar);
                                                    }
                                                    dataAdapterSucursales.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                    spSucursales.setAdapter(dataAdapterSucursales);
                                                }
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                task.execute();

                                View view2 = getCurrentFocus();
                                view2.clearFocus();
                                if (view2 != null) {
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                                }
                            }
                        }
                    }
                });
                codCli.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        boolean handled = false;
                        if (actionId == EditorInfo.IME_ACTION_SEND) {
                            //  sendMessage();
                            handled = true;
                        }
                        return handled;
                    }
                });

                spSucursales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object item = parent.getItemAtPosition(position);
                        Entrega instaSucu = new Entrega();

                        boolean encontrado = false;
                        for(int i = 0; i < WebService.ArraySucursales.size() && !encontrado; i++){
                            instaSucu = WebService.ArraySucursales.get(i);
                            if (instaSucu.getNom_Sucursal().trim().equals(item)){
                                encontrado = true;
                                //codSucursal = instaSucu.getCod_Sucursal().trim();
                                WebService.sucursalActual = instaSucu;
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                btnIR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utilidad.vibraticionBotones(contexto);
                        if(Utilidad.isNetworkAvailable()) {
                            Calendar rightNow = Calendar.getInstance();//CREA UN CALENDARIO
                            int hour = rightNow.get( Calendar.HOUR_OF_DAY );//PIDE LA HORA Y EL MINUTO
                            int minute = rightNow.get( Calendar.MINUTE );
                            String horaAguardar = "";
                            if (hour < 10) {//SI LA HORA ES INFERIOR A 10 GUARDA UN 0 ANTES DE LA HORA ACTUAL
                                horaAguardar = "0" + Integer.toString( hour ) + ":";
                            } else {
                                horaAguardar = Integer.toString( hour ) + ":";
                            }
                            if (minute < 10) {//SI LOS MINUTOS ACTUALES SON MENORES A 10 GUARDA UN 0 ANTES DEL MINUTO ACTUAL
                                horaAguardar = horaAguardar + "0" + Integer.toString( minute );
                            } else {
                                horaAguardar = horaAguardar + Integer.toString( minute );
                            }
                            WebService.horaComienzoViaje = horaAguardar;
                            Intent myIntent2;
                            if (WebService.sucursalActual.getLatiud_Ubic() == 0.0) {
                                myIntent2 = new Intent(v.getContext(), FacturaProductos.class);
                            }else {
                                myIntent2 = new Intent(v.getContext(), Recorrido_Viaje.class);
                            }
                            WebService.EstadoActual = 1;
                            WebService.ViajeActualSeleccionado = true;
                            myIntent2.putExtra("intent", "VentaDirecta");
                            startActivity(myIntent2);
                        }else {
                            Utilidad.CargarToastConexion(contexto);
                        }
                    }
                });

            } else {
                Utilidad.CargarToastConexion(contexto);
            }
        } else {
            Intent myIntent = new Intent(contexto, Login.class);
            startActivity(myIntent);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
    }
}

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    private class TraerClientesVenta extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface
        public TraerClientesVenta(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            if(Utilidad.isNetworkAvailable()) {
                WebService.TraerClientesVenta("VentasDirectas/TraerClientes.php");
                return null;

            } else {}
            return null;
        }
        @Override
        public void onPreExecute() {
            dialog1.setMessage(getResources().getString( R.string.cargando_dialog));
            dialog1.show();
        }
        @Override
        protected void onPostExecute(Void result) {
            if(dialog1.isShowing())
            {
                dialog1.dismiss();
            }
            delegate.processFinish( WebService.logueado);
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private class TraerSucursales extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface
        public TraerSucursales(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            if(Utilidad.isNetworkAvailable()) {
                RequestParams params1 = new RequestParams();
                params1.add("cod_tit", WebService.clienteActual.getCod_Tit_Gestion().trim());
                WebService.TraerSucursales("VentasDirectas/TraerSucursales.php", params1);
                return null;

            } else {}
            return null;
        }
        @Override
        public void onPreExecute() {
            dialog1.setMessage(getResources().getString( R.string.cargando_dialog));
            dialog1.show();
        }
        @Override
        protected void onPostExecute(Void result) {
            if(dialog1.isShowing())
            {
                dialog1.dismiss();
            }
            delegate.processFinish( WebService.logueado);
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private void CargarSpinner(){
        for (int i = 0; i < WebService.ArrayClientes.size(); i++) {
            String nombreAgregar = WebService.ArrayClientes.get(i).getNom_Tit().trim();
            NombreCliente.add(nombreAgregar);
            String codigoAgregar = WebService.ArrayClientes.get(i).getCod_Tit_Gestion().trim();
            CodigosCliente.add(codigoAgregar);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            Utilidad.vibraticionBotones(contexto);
            Intent myIntent = new Intent(contexto, Login.class);
            startActivity(myIntent);
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }
}
