package com.example.user.trucksales.Visual.Pedidos;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.ClienteCobranza;
import com.example.user.trucksales.Encapsuladoras.Entrega;
import com.example.user.trucksales.Encapsuladoras.Pedido;
import com.example.user.trucksales.Encapsuladoras.TipoDoc;
import com.example.user.trucksales.Negocio.GuardarDatosUsuario;
import com.example.user.trucksales.Negocio.Utilidades;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Login;
import com.example.user.trucksales.Visual.TruckSales.ClienteXDefecto;
import com.example.user.trucksales.Visual.TruckSales.Consultas;
import com.example.user.trucksales.Visual.TruckSales.Generados;
import com.example.user.trucksales.Visual.TruckSales.Recorrido_Viaje;
import com.example.user.trucksales.Visual.TruckSales.Viajes;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Pedidos extends Activity implements AdapterView.OnItemSelectedListener{
    private Utilidades Utilidad;
    ImageView atras;
    LinearLayout mainLayout;
    Context contexto;

    public static String valorIntent;
    public static String empresa = "";
    public static String entrega = "";
    public static String codemp = "";
    public static String nom_tit_Suc = "";
    public static String cod_doc_uni = "";
    public static String nro_doc_uni = "";
    public static String f_pago = "";
    public static String tipo = "";
    public static String turno = "";

    TextView nombreUsu,fecha, titSuCliente, titEntrega, titDocFac;
    private static final String CERO = "0";
    private static final String BARRA = "/";
    EditText fechaEntrega;
    Button btnAceptar;

    public List<String> NombreCliente =  new ArrayList<String>();
    public List<String> CodigosCliente = new ArrayList<>();
    AutoCompleteTextView nombreCli,codCli;

    Spinner spEmpresa;
    final List<String> spinnerEmpresa = new ArrayList<>(  );

    Spinner spFPago;
    final List<String> spinnerFPago = new ArrayList<>(  );

    Spinner spTipo;
    final List<String> spinnerTipo = new ArrayList<>(  );

    Spinner spTurno;
    final List<String> spinnerTurno = new ArrayList<>(  );

    Spinner spiEntrega;
    final List<String> spinnerEntrega = new ArrayList<>();

    Spinner spiDocFac;
    final List<String> spinnerDocFac = new ArrayList<>();

    Spinner spSucursal;
    final List<String> spinnerSucursal= new ArrayList<>(  );

    Spinner spSucCliente;
    final List<String> spinnerSucCliente= new ArrayList<>(  );

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();
    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    private Date fechaSeleccionada;
    String nombreCliente = "";
    String codigoCliente = "";
    String sucursalPedido = "";

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

        setContentView(R.layout.activity_pedidos);
        contexto = this;
        Utilidad = new Utilidades(contexto);

        GuardarDatosUsuario.Contexto = contexto;
        WebService.EstadoActual = 1; //PARA QUITAR EL SONIDO
        WebService.pedido = new Pedido();
        WebService.clienteActual = new ClienteCobranza();
        WebService.sucursalActualPedidos = new Entrega();
        WebService.ArraySucursales = new ArrayList<>();
        WebService.ArrayEntrega = new ArrayList<>();
        WebService.listTipoDoc = new ArrayList<>();
        try{
            if (WebService.USUARIOLOGEADO != null) {
                if (Utilidad.isNetworkAvailable()) {
                    mainLayout = findViewById(R.id.mainLay);

                    spEmpresa = findViewById(R.id.spiEmpresa);
                    spSucursal = findViewById(R.id.spiSucursal);
                    spSucCliente = findViewById(R.id.spiSuCliente);
                    spiEntrega = findViewById(R.id.spiEntrega);
                    nombreCli = findViewById(R.id.TxtNombreCliente);
                    codCli = findViewById(R.id.TxtCodCliente);
                    spiDocFac = findViewById(R.id.spiDocFac);
                    spFPago = findViewById(R.id.spiPago);
                    spTurno = findViewById(R.id.spiTurno);
                    spTipo = findViewById(R.id.spiTipo);
                    titSuCliente = findViewById(R.id.titSuCliente);
                    titEntrega = findViewById(R.id.titEntrega);
                    titDocFac = findViewById(R.id.titDocFac);
                    fechaEntrega = findViewById(R.id.fecEntrega);
                    fechaEntrega.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            obtenerFecha();
                        }
                    });

                    atras = findViewById(R.id.btnAtras);
                    nombreUsu = findViewById(R.id.LblUsu);
                    fecha = findViewById(R.id.LblFecha);
                    btnAceptar = findViewById(R.id.btnAceptar);

                    String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                    fecha.setText(timeStamp);
                    nombreUsu.setText(WebService.USUARIOLOGEADO.trim());

                    spFPago.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                    final ArrayAdapter<String> dataAdapterFPago = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerFPago);
                    /*spinnerFPago.add("CO");
                    spinnerFPago.add("CR");*/

                    spFPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                f_pago = spFPago.getSelectedItem().toString();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    spTurno.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                    final ArrayAdapter<String> dataAdapterTurno = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerTurno);
                    /*spinnerTurno.add("Mañana");
                    spinnerTurno.add("Tarde");*/
                    spTurno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                turno = spTurno.getSelectedItem().toString();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    spTipo.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                    final ArrayAdapter<String> dataAdapterTipo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerTipo);
                    /*spinnerTipo.add("NORMAL");
                    spinnerTipo.add("CAMPAÑA");*/
                    spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                tipo = spTipo.getSelectedItem().toString();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    spiEntrega.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                    final ArrayAdapter<String> dataAdapterEntrega = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerEntrega);


                    spiDocFac.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                    final ArrayAdapter<String> dataAdapterDocFac = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerDocFac);
                    spiDocFac.setVisibility(View.GONE);
                    titDocFac.setVisibility(View.GONE);
                    spiEntrega.setVisibility(View.GONE);
                    titEntrega.setVisibility(View.GONE);
                    spSucCliente.setVisibility(View.GONE);
                    titSuCliente.setVisibility(View.GONE);

                    spSucCliente.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                    final ArrayAdapter<String> dataAdapterSucCli = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerSucCliente);

                    spiDocFac.setVisibility(View.GONE);
                    titDocFac.setVisibility(View.GONE);
                    spiEntrega.setVisibility(View.GONE);
                    titEntrega.setVisibility(View.GONE);
                    spSucCliente.setVisibility(View.GONE);
                    titSuCliente.setVisibility(View.GONE);

                    final ArrayAdapter<String> adapterNomCli = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, NombreCliente);
                    final ArrayAdapter<String> adapterCodCli = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, CodigosCliente);

                    spEmpresa.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                    final ArrayAdapter<String> dataAdapterEmp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerEmpresa);

                    TraeEmpresaUsu task1 = new TraeEmpresaUsu(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            if (!WebService.errToken.equals("")) {
                                Intent myIntent = new Intent(contexto, Login.class);
                                startActivity(myIntent);
                            }else {
                                for (int i = 0; i < WebService.listEmpPedidos.size(); i++) {
                                    String nombreAgregar1 = WebService.listEmpPedidos.get(i).getNomEmp();
                                    spinnerEmpresa.add(nombreAgregar1);
                                }
                                dataAdapterEmp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spEmpresa.setAdapter(dataAdapterEmp);
                                spEmpresa.setSelection(0);
                            }
                        }
                    });
                    task1.execute();



                    spEmpresa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                String empSelec = spEmpresa.getSelectedItem().toString();
                                ClienteCobranza instanciaCl = new ClienteCobranza();
                                for (int x = 0; x < WebService.listEmpPedidos.size(); x++) {
                                    instanciaCl = WebService.listEmpPedidos.get(x);
                                    if (instanciaCl.getNomEmp().trim().equals(empSelec.trim())) {
                                        empresa = instanciaCl.getNomEmp().trim();
                                        codemp = instanciaCl.getCodEmp().trim();
                                        break;
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

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
                                    nombreCliente = instaCliente.getNom_Tit().trim();
                                    codigoCliente = instaCliente.getCod_Tit_Gestion().trim();
                                    WebService.clienteActual = instaCliente;

                                    titSuCliente.setVisibility(View.VISIBLE);
                                    spSucCliente.setVisibility(View.VISIBLE);
                                    titDocFac.setVisibility(View.VISIBLE);
                                    spiDocFac.setVisibility(View.VISIBLE);

                                    WebService.listTipoDoc.clear();
                                    WebService.ArraySucursales.clear();
                                    spinnerDocFac.clear();
                                    spinnerSucCliente.clear();

                                    TraeDocumCliente task4 = new TraeDocumCliente(new AsyncResponse() {
                                        @Override
                                        public void processFinish(Object output) {
                                            if (!WebService.errToken.equals("")) {
                                                Intent myIntent = new Intent(contexto, Login.class);
                                                startActivity(myIntent);
                                            }else {
                                                if (WebService.listTipoDoc.size() == 0) {
                                                    Toast toast = Toast.makeText(contexto, getResources().getString(R.string.ErrorTipoDoc), Toast.LENGTH_LONG);
                                                    toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                                                    toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                                                    toast.show();
                                                    spiDocFac.setVisibility(View.GONE);
                                                    titDocFac.setVisibility(View.GONE);
                                                } else {
                                                    for (int i = 0; i < WebService.listTipoDoc.size(); i++) {
                                                        String cod_doc_uni = WebService.listTipoDoc.get(i).getCod_doc_uni().trim();
                                                        String nom_doc_uni = WebService.listTipoDoc.get(i).getNom_doc_uni().trim();
                                                        String nro_doc_uni = WebService.listTipoDoc.get(i).getNro_doc_uni().trim();

                                                        spinnerDocFac.add(cod_doc_uni + "-" + nom_doc_uni + "-" + nro_doc_uni);
                                                    }
                                                    dataAdapterDocFac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                    spiDocFac.setAdapter(dataAdapterDocFac);
                                                    spiDocFac.setSelection(0);
                                                }
                                            }
                                        }
                                    });
                                    task4.execute();

                                    TraerSucursalesCli task = new TraerSucursalesCli(new AsyncResponse() {
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
                                                        toast.show();
                                                        titSuCliente.setVisibility(View.GONE);
                                                        spSucCliente.setVisibility(View.GONE);
                                                    } else {
                                                        for (int i = 0; i < WebService.ArraySucursales.size(); i++) {
                                                            Entrega instaSucu = new Entrega();
                                                            instaSucu = WebService.ArraySucursales.get(i);

                                                            String nombreAgregar = instaSucu.getNom_Sucursal().trim();
                                                            spinnerSucCliente.add(nombreAgregar);
                                                        }
                                                        dataAdapterSucCli.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        spSucCliente.setAdapter(dataAdapterSucCli);
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

                    spSucCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Object item = parent.getItemAtPosition(position);
                            Entrega instaSucu = new Entrega();

                            boolean encontrado = false;
                            for(int i = 0; i < WebService.ArraySucursales.size() && !encontrado; i++){
                                instaSucu = WebService.ArraySucursales.get(i);
                                if (instaSucu.getNom_Sucursal().trim().equals(item)){
                                    encontrado = true;
                                    WebService.sucursalActualPedidos = instaSucu;
                                    sucursalPedido = WebService.sucursalActualPedidos.getCod_Sucursal().trim();
                                    titEntrega.setVisibility(View.VISIBLE);
                                    spiEntrega.setVisibility(View.VISIBLE);
                                    TraerZonaEntrega task = new TraerZonaEntrega(new AsyncResponse() {
                                        @Override
                                        public void processFinish(Object output) {
                                            try {
                                                if (!WebService.errToken.equals("")) {
                                                    Intent myIntent = new Intent(contexto, Login.class);
                                                    startActivity(myIntent);
                                                }else {
                                                    if (WebService.ArrayEntrega.size() == 0) {
                                                        Toast toast = Toast.makeText(contexto, getResources().getString(R.string.sucursales), Toast.LENGTH_LONG);
                                                        toast.setGravity(Gravity.CENTER, 0, 0); // last two args are X and Y are used for setting position
                                                        toast.setDuration(Toast.LENGTH_LONG);//you can even use milliseconds to display toast
                                                        toast.show();//showing the toast is important***/
                                                    } else {
                                                        for (int i = 0; i < WebService.ArrayEntrega.size(); i++) {
                                                            Entrega instaEntr = new Entrega();
                                                            instaEntr = WebService.ArrayEntrega.get(i);

                                                            String nombreAgregar = instaEntr.getCod_Zona_Entrega().trim();
                                                            spinnerEntrega.add(nombreAgregar);
                                                        }
                                                        dataAdapterEntrega.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        spiEntrega.setAdapter(dataAdapterEntrega);
                                                    }
                                                }
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                    task.execute();
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    spiEntrega.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                String spEntrega = spiEntrega.getSelectedItem().toString();
                                Entrega instanciaEnt = new Entrega();
                                for (int x = 0; x < WebService.ArrayEntrega.size(); x++) {
                                    instanciaEnt = WebService.ArrayEntrega.get(x);
                                    if (instanciaEnt.getCod_Zona_Entrega().trim().equals(spEntrega.trim())) {
                                        entrega = instanciaEnt.getCod_Zona_Entrega().trim();
                                        break;
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    //sucursal
                    spSucursal.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
                    final ArrayAdapter<String> dataAdapterSucursales = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerSucursal);

                    TraerSucursales task4 = new TraerSucursales(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            if (!WebService.errToken.equals("")) {
                                Intent myIntent = new Intent(contexto, Login.class);
                                startActivity(myIntent);
                            }else {
                                for (int i = 0; i < WebService.ArraySucursalesEmp.size(); i++) {
                                    String nombreAgregar1 = WebService.ArraySucursalesEmp.get(i).getNom_Tit();
                                    spinnerSucursal.add(nombreAgregar1);
                                }
                                dataAdapterSucursales.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spSucursal.setAdapter(dataAdapterSucursales);
                                spSucursal.setSelection(0);
                            }
                        }
                    });
                    task4.execute();

                    spSucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                String sucSelect = spSucursal.getSelectedItem().toString();
                                Entrega instanciaSuc = new Entrega();
                                for (int x = 0; x < WebService.ArraySucursalesEmp.size(); x++) {
                                    instanciaSuc = WebService.ArraySucursalesEmp.get(x);
                                    if (instanciaSuc.getNom_Tit().trim().equals(sucSelect.trim())) {
                                        nom_tit_Suc = instanciaSuc.getCod_Tit().trim();
                                        WebService.sucuEmpSelect = instanciaSuc;
                                        break;
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    spiDocFac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                String docFacSelect = spiDocFac.getSelectedItem().toString();
                                TipoDoc instanciaEnt = new TipoDoc();
                                for (int x = 0; x < WebService.listTipoDoc.size(); x++) {
                                    instanciaEnt = WebService.listTipoDoc.get(x);
                                    String[] parts = docFacSelect.split("-");
                                    String parts1_1 = parts[0];
                                    docFacSelect = parts1_1;
                                    if (instanciaEnt.getCod_doc_uni().trim().equals(docFacSelect.trim())) {
                                        cod_doc_uni = instanciaEnt.getCod_doc_uni().trim();
                                        nro_doc_uni = instanciaEnt.getNro_doc_uni().trim();
                                        break;
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    atras.setClickable(true);
                    atras.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(contexto);
                            try {
                                valorIntent = getIntent().getStringExtra("intent");
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                            Intent myIntent;
                            if(valorIntent.equals("Consultas")){
                                myIntent = new Intent(contexto, Consultas.class);
                            }else if(valorIntent.equals("Viajes")){
                                myIntent = new Intent(contexto, Viajes.class);
                            }else if(valorIntent.equals("Recorrido_Viaje")){
                                myIntent = new Intent(contexto, Recorrido_Viaje.class);
                            }else if(valorIntent.equals("Generados")){
                                myIntent = new Intent(contexto, Generados.class);
                            }else {
                                myIntent = new Intent(contexto, ClienteXDefecto.class);
                            }
                            startActivity(myIntent);
                        }
                    });

                    btnAceptar.setClickable(true);
                    btnAceptar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utilidad.vibraticionBotones(contexto);
                            if (fechaEntrega.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Debe seleccionar la fecha", Toast.LENGTH_LONG).show();
                            } else if (codemp.equals("")) {
                                Toast.makeText(getApplicationContext(), "Debe seleccionar la empresa", Toast.LENGTH_LONG).show();
                            } else if (nom_tit_Suc.equals("")) {
                                Toast.makeText(getApplicationContext(), "Debe seleccionar la sucursal", Toast.LENGTH_LONG).show();
                            } else if (!fechaValida()) {
                                Toast.makeText(getApplicationContext(), "Debe seleccionar una fecha valida", Toast.LENGTH_LONG).show();
                            }else if (codigoCliente.equals("")) {
                                Toast.makeText(getApplicationContext(), "Debe seleccionar un cliente", Toast.LENGTH_LONG).show();
                            }else if (sucursalPedido.equals("")) {
                                Toast.makeText(getApplicationContext(), "Debe seleccionar la sucursal del cliente", Toast.LENGTH_LONG).show();
                            } else if (cod_doc_uni.trim().equals("")) {
                                Toast.makeText(getApplicationContext(), "Debe seleccionar el codigo de factura", Toast.LENGTH_LONG).show();
                            } else if (f_pago.trim().equals("")) {
                                Toast.makeText(getApplicationContext(), "Debe seleccionar la forma de pago", Toast.LENGTH_LONG).show();
                            } else if (tipo.trim().equals("")) {
                                Toast.makeText(getApplicationContext(), "Debe seleccionar el tipo", Toast.LENGTH_LONG).show();
                            } else if (turno.trim().equals("")) {
                                Toast.makeText(getApplicationContext(), "Debe seleccionar el turno", Toast.LENGTH_LONG).show();
                            } else if (entrega.trim().equals("")) {
                                Toast.makeText(getApplicationContext(), "Debe seleccionar la zona de entrega", Toast.LENGTH_LONG).show();
                            }  else {
                                if (Utilidad.isNetworkAvailable()) {
                                    String fecha2 = fechaEntrega.getText().toString();

                                    WebService.pedido.setCod_emp(codemp);
                                    WebService.pedido.setCod_suc(nom_tit_Suc);
                                    WebService.pedido.setCod_tit(WebService.clienteActual.getCod_Tit_Gestion().trim());
                                    WebService.pedido.setNom_tit(WebService.clienteActual.getNom_Tit().trim());
                                    WebService.pedido.setCod_suc_cli(WebService.sucursalActualPedidos.getCod_Sucursal().trim());
                                    WebService.pedido.setZona_entrega(entrega.trim());
                                    WebService.pedido.setDoc_fac(cod_doc_uni.trim());
                                    WebService.pedido.setNro_doc_uni(nro_doc_uni.trim());
                                    WebService.pedido.setForma_pago(f_pago);
                                    WebService.pedido.setTipo(tipo);
                                    WebService.pedido.setTurno(turno);
                                    WebService.pedido.setFec_entrega(fecha2);
                                    try {
                                        valorIntent = getIntent().getStringExtra("intent");
                                    }catch (Exception ex){
                                        ex.printStackTrace();
                                    }
                                    Intent myIntent = new Intent(v.getContext(), ProductosPedidos.class);
                                    myIntent.putExtra("intent", valorIntent);
                                    startActivity(myIntent);
                                } else {
                                    Utilidad.CargarToastConexion(contexto);
                                }
                            }
                        }
                    });

                }else{
                    Utilidad.CargarToastConexion(contexto);
                }
            }else {
                Intent myIntent = new Intent(contexto, Login.class);
                startActivity(myIntent);
            }

        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    public class  TraeEmpresaUsu extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface
        public TraeEmpresaUsu(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {

            if(Utilidad.isNetworkAvailable())   {
                RequestParams params1 = new RequestParams(  );
                params1.put("username", WebService.USUARIOLOGEADO.trim());
                WebService.TraeEmpresaUsu(params1);
                return null;
            }
            else  {
            }

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
        protected void onProgressUpdate(Void... values)
        {

        }
    }

    private boolean fechaValida() {

        Date hoy = new Date();
        hoy.setHours(0);
        hoy.setMinutes(0);
        hoy.setSeconds(0);

        if(fechaSeleccionada==null){
            return true;
        } else{
            return true;
        }
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
                params1.add("username", WebService.USUARIOLOGEADO.trim());
                WebService.TraerSucDepEmp("Pedidos/TraeSucDepEmp.php", params1);
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

    private class TraerSucursalesCli extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface
        public TraerSucursalesCli(AsyncResponse asyncResponse) {
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
    private class TraerZonaEntrega extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface
        public TraerZonaEntrega(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            if(Utilidad.isNetworkAvailable()) {
                RequestParams params1 = new RequestParams();
                params1.add("cod_tit",  WebService.clienteActual.getCod_Tit_Gestion().trim());
                params1.add("cod_sucursal",  WebService.sucursalActualPedidos.getCod_Sucursal().trim());
                WebService.TraerZonasEntrega("Pedidos/TraeZonasEntrega.php", params1);
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

    private class TraeDocumCliente extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( contexto );
        public AsyncResponse delegate = null;//Call back interface
        public TraeDocumCliente(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(String... params) {
            if(Utilidad.isNetworkAvailable()) {
                RequestParams params1 = new RequestParams();
                params1.add("cod_tit",  WebService.clienteActual.getCod_Tit_Gestion().trim());
                WebService.TraeDocumCliente("Pedidos/TraerTiposDocumCliente.php", params1);
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
            try {
                valorIntent = getIntent().getStringExtra("intent");
            }catch (Exception ex){
                ex.printStackTrace();
            }
            Intent myIntent;
            if(valorIntent.equals("Consultas")){
                myIntent = new Intent(contexto, Consultas.class);
            }else if(valorIntent.equals("Viajes")){
                myIntent = new Intent(contexto, Viajes.class);
            }else if(valorIntent.equals("Recorrido_Viaje")){
                myIntent = new Intent(contexto, Recorrido_Viaje.class);
            }else if(valorIntent.equals("Generados")){
                myIntent = new Intent(contexto, Generados.class);
            }else {
                myIntent = new Intent(contexto, ClienteXDefecto.class);
            }
            startActivity(myIntent);
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText( getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);

                //Muestro la fecha con el formato deseado
                fechaEntrega.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


                fechaSeleccionada = new Date();
                fechaSeleccionada.setHours(0);
                fechaSeleccionada.setMinutes(0);
                fechaSeleccionada.setSeconds(0);
                fechaSeleccionada.setDate(dayOfMonth);
                fechaSeleccionada.setMonth(month);
                fechaSeleccionada.setYear(year-1900);

            }
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }
}
