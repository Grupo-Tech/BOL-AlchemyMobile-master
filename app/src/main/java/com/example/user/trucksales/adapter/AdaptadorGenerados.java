package com.example.user.trucksales.adapter;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.print.PrintManager;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.FacturaXDia;
import com.example.user.trucksales.Negocio.ApplicationContext;
import com.example.user.trucksales.Negocio.Printer;
import com.example.user.trucksales.R;
import com.example.user.trucksales.Visual.Cobranza.FacturaCobranza;
import com.example.user.trucksales.Visual.Cobranza.RecibosGenerados;
import com.example.user.trucksales.Visual.Login;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.trucksales.R.style.myDialog;

public class AdaptadorGenerados extends BaseAdapter {

    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 123 ;
    private static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 123;

    private static Context context;
    public String nom_copia;

    RequestParams params1 = new RequestParams();
    RequestParams params4 = new RequestParams();

    //IMPRESION
    ApplicationContext contextoImpre;
    ArrayList<String> getbtName = new ArrayList<>();
    ArrayList<String> getbtNM = new ArrayList<>();
    ArrayList<String> getbtMax = new ArrayList<>();
    public boolean mBconnect = false;
    public int state = 0;

    private final AdaptadorGenerados adaptador;
    private RecibosGenerados activity; //Activity desde el cual se hace referencia al llenado de la lista
    private List<FacturaXDia> arrayItems; // Lista de items

    // Constructor con parámetros que recibe la Acvity y los datos de los items.
    public AdaptadorGenerados(RecibosGenerados activity, List<FacturaXDia> items){
        super();
        this.activity = activity;
        this.arrayItems = items;
        this.adaptador=this;
    }

    // Retorna el número de items de la lista
    @Override
    public int getCount() {
        return arrayItems.size();
    }
    // Retorna el objeto TitularItems de la lista
    @Override
    public Object getItem(int position) {
        return arrayItems.get(position);
    }
    // Retorna la posición del item en la lista
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
   Clase estática que contiene los elementos de la lista
    */
    public static class Fila   {

        public RelativeLayout ly_cliente;
        public ImageView anular;
        public ImageView factura;
        public TextView txtRecibo,txtFecha,txtCliente;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        context = parent.getContext();

        Fila view = new Fila();
        LayoutInflater inflator = activity.getLayoutInflater();
        final FacturaXDia itm = arrayItems.get(position);
        final View convertViewAux;
        /*
        Condicional para recrear la vista y no distorcionar el número de elementos
         */

        if (convertView == null) {
            convertView = inflator.inflate(R.layout.item_generados, parent, false);

            view.ly_cliente = (RelativeLayout) convertView.findViewById(R.id.ly_cliente);
            //iew.txtTotal = (TextView) convertView.findViewById(R.id.txtTotal);
            view.anular = (ImageView) convertView.findViewById(R.id.anular);
            view.factura = (ImageView) convertView.findViewById(R.id.factura);

            view.txtCliente = (TextView) convertView.findViewById(R.id.txtCliente);
            view.txtRecibo = (TextView) convertView.findViewById(R.id.txtRecibo);
            view.txtFecha = (TextView) convertView.findViewById(R.id.txtFecha);

            convertView.setTag(view);
        } else {
            view = (Fila) convertView.getTag();
        }
        convertViewAux = convertView;
        // Se asigna el dato proveniente del objeto
        final int order = position + 1;

        view.txtCliente.setText("Cliente: " + itm.getNom_Tit());
        view.txtRecibo.setText("Recibo: " + itm.getNro_Docum());
        view.txtFecha.setText("Fecha rec.: " + itm.getHora());

        if(itm.getCalcula_interes().equals("N")){
            view.factura.setVisibility(View.GONE);
        }

        view.ly_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (WebService.configuracion.getTipo_impresora().equals("T")) {
                        FacturaXDia ri = new FacturaXDia();
                        ri = itm;

                        PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
                        Printer pr = new Printer();
                        pr.setPrMang(printManager);
                        pr.setContx(context);
                        pr.setValor(ri.getNro_Trans().trim());
                        pr.setTipo("RE");
                        pr.genarPdf(pr);
                    } else {
                        contextoImpre = new ApplicationContext();
                        contextoImpre = (ApplicationContext) context.getApplicationContext();
                        contextoImpre.setObject();
                        params4 = new RequestParams();
                        nom_copia = "Copia";
                        params4.add("nom_copia", nom_copia);
                        params4.put("nrotrans", itm.getNro_Trans());
                        Conectar();

                        TraerDatoImpresion task = new TraerDatoImpresion(new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) {
                                if (!WebService.errToken.equals("")) {
                                    Intent myIntent = new Intent(context, Login.class);
                                    context.startActivity(myIntent);
                                }else {
                                    Impr();
                                }
                            }
                        });
                        task.execute();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        view.anular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    displayAlertEliminar(itm);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        view.factura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    params1.put("cod_tit", itm.getCod_Tit().trim());
                    params1.put("cod_emp", itm.getCod_emp().trim());
                    CalculaInteres task = new CalculaInteres(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            if (!WebService.errToken.equals("")) {
                                Intent myIntent = new Intent(context, Login.class);
                                context.startActivity(myIntent);
                            } else {
                                params1 = new RequestParams();
                                params1.put("username", WebService.USUARIOLOGEADO);
                                params1.put("cod_emp", itm.getCod_emp().trim());
                                ObtenerNumRecomendado task2 = new ObtenerNumRecomendado(new AsyncResponse() {
                                    @Override
                                    public void processFinish(Object output) {
                                        if (!WebService.errToken.equals("")) {
                                            Intent myIntent = new Intent(context, Login.class);
                                            context.startActivity(myIntent);
                                        } else {
                                            WebService.usuarioActual.setEmpresa(itm.getCod_emp());
                                            WebService.clienteActual.setCodEmp(itm.getCod_emp());
                                            WebService.clienteActual.setCod_Tit_Gestion(itm.getCod_Tit());
                                            WebService.clienteActual.setNomEmp(itm.getNom_emp());
                                            WebService.clienteActual.setNom_Tit(itm.getNom_Tit());
                                            WebService.clienteActual.setCod_Moneda(itm.getCod_moneda());
                                            Intent myIntent = new Intent(context, FacturaCobranza.class);
                                            myIntent.putExtra("intent", "Generados");
                                            context.startActivity(myIntent);
                                        }
                                    }
                                });
                                task2.execute();
                            }
                        }
                    });
                    task.execute();

                    activity.refreshList();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        return convertView;
    }

    protected void displayAlertEliminar(final FacturaXDia itm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, myDialog));
        builder.setMessage("Desea anular el recibo?").setCancelable(
                false).setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            params1 = new RequestParams();
                            params1.put("nrotrans", itm.getNro_Trans());
                            params1.put("username", WebService.USUARIOLOGEADO);

                            AnularRecibo task = new AnularRecibo(new AsyncResponse() {
                                public void processFinish(Object output) {
                                    if (!WebService.errToken.equals("")) {
                                        Intent myIntent = new Intent(context, Login.class);
                                        context.startActivity(myIntent);
                                    } else {
                                        if (WebService.banderaAnuladas == false) {
                                            Toast.makeText(context, "El recibo no se pudo eliminar", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(context, "El recibo ha sido eliminado", Toast.LENGTH_LONG).show();
                                            for (int i = 0; WebService.listarecibos.size() > i; i++) {
                                                FacturaXDia fac = WebService.listarecibos.get(i);
                                                if (fac.getNro_Trans() == itm.getNro_Trans()) {
                                                    WebService.listarecibos.remove(i);
                                                    break;
                                                }
                                            }
                                        }
                                        activity.refreshList();
                                    }
                                }
                            });
                            task.execute();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }).setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alerta = builder.create();
        alerta.show();
    }

    public interface AsyncResponse {
        void processFinish(Object output);
    }

    private class AnularRecibo extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog( context );
        public AsyncResponse delegate = null;//Call back interface

        public AnularRecibo(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {

            WebService.AnularRecibo(params1, "Cobranzas/AnularRecibo.php");
            return null;

        }
        @Override
        public void onPreExecute() {
        }
        @Override
        protected void onPostExecute(Void result) {
            delegate.processFinish( WebService.logueado);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class CalculaInteres extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog(context);
        public AsyncResponse delegate = null;//Call back interface

        public CalculaInteres(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {
           // if (Utilidad.isNetworkAvailable()) {
                WebService.CalculaInteres("Facturas/CalculoInteres.php", params1);
                return null;
           /* }
            return null;*/
        }
        @Override
        public void onPreExecute() {
           // dialog1.setMessage(getResources().getString( R.string.cargando_dialog));
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
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class ObtenerNumRecomendado extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog1 = new ProgressDialog(context);
        public AsyncResponse delegate = null;//Call back interface

        public ObtenerNumRecomendado(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Void doInBackground(String... params) {
           // if (Utilidad.isNetworkAvailable()) {
                WebService.RecomendarNumeroFactura("Facturas/RecomendarNumero.php", params1);
                return null;
           /* }
            return null;*/
        }

        @Override
        public void onPreExecute() {
           // dialog1.setMessage(getResources().getString( R.string.cargando_dialog));
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
        protected void onProgressUpdate(Void... values) {
        }
    }

    public void Imprimir(String datos){
        contextoImpre.getObject().CON_PageStart(contextoImpre.getState(), false, 0, 0);
        contextoImpre.getObject().ASCII_CtrlOppositeColor(contextoImpre.getState(), false);
        contextoImpre.getObject().ASCII_CtrlAlignType(contextoImpre.getState(), 0);
        contextoImpre.getObject().ASCII_PrintString(contextoImpre.getState(), 0,  0,
                0,  0, 0, datos, "gb2312");
        contextoImpre.getObject().ASCII_CtrlFeedLines(contextoImpre.getState(), 1);
        contextoImpre.getObject().ASCII_CtrlPrintCRLF(contextoImpre.getState(), 1);
        contextoImpre.getObject().CON_PageEnd(contextoImpre.getState(), contextoImpre.getPrintway());

        contextoImpre.getObject().CON_CloseDevices(contextoImpre.getState());
    }

    public void Conectar(){
        getbtNM = new ArrayList<>();
        getbtName = new ArrayList<>();
        getbtMax = new ArrayList<>();

        getbtNM = (ArrayList<String>) contextoImpre.getObject().CON_GetWirelessDevices(0);
        for (int i = 0; i < getbtNM.size(); i++) {
            getbtName.add(getbtNM.get(i).split(",")[0]);
            getbtMax.add(getbtNM.get(i).split(",")[1].substring(0, 17));
        }

        connect(getbtMax.get(0),200);
        contextoImpre.getObject().CON_QueryStatus(contextoImpre.getState());
    }

    public void connect(String port,int tiempo_conexion) {
        if (!mBconnect) {
            state = contextoImpre.getObject().CON_ConnectDevices(getbtName.get(0), port, tiempo_conexion);
            if (state > 0) {
                Toast.makeText(context, R.string.mes_consuccess, Toast.LENGTH_SHORT).show();
                mBconnect = true;
                contextoImpre.setState(state);
                contextoImpre.setName(getbtName.get(0));
                contextoImpre.setPrintway(0);
                mBconnect = false;
            } else {
                Toast.makeText(context, R.string.mes_confail, Toast.LENGTH_SHORT).show();
                mBconnect = false;
            }
        }
    }

    private class  TraerDatoImpresion extends AsyncTask<Void, Void, Void> {
        public AsyncResponse delegate = null;//Call back interface
        public TraerDatoImpresion(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected Void doInBackground(Void... voids) {
            WebService.BuscarDatosImpresora("Impresion/printRecibo.php", params4);
            return null;
        }
        @Override
        public void onPreExecute() {
           // Utilidad.showLoadingMessage();
        }
        @Override
        protected void onPostExecute(Void result) {
            delegate.processFinish( WebService.logueado);
            //Utilidad.deleteLoadingMessage();
        }
        @Override
        protected void onProgressUpdate(Void... values) { }
    }

    public void Impr() {
        try {
            String str = WebService.respuestaWSImpresora;
            System.out.println(str);
            Imprimir(str);
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText( getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG ).show();
        }
    }

}
