package com.example.user.trucksales.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.MotivoPausa;
import com.example.user.trucksales.R;

import java.util.ArrayList;
import java.util.List;

public class Dialog_Reclamo_Fact extends DialogFragment implements AdapterView.OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public int cod = 0;
    public String observ = "";
    Spinner spReclamos;
    public List<String> spinnerReclamos = new ArrayList<>(  );

    public interface NoticeDialogListenerNormal {
        void onDialogNormalGuardarClick(DialogFragment dialog);
    }

    NoticeDialogListenerNormal mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListenerExcepcionCuatro so we can send events to the host
            mListener = (NoticeDialogListenerNormal) getActivity();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString() + " must implement NoticeDialogListenerExcepcionCuatro");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        // Get the layout inflater
        LayoutInflater inflater = this.getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_reclamo_fact, null);

        Button buttonCancelar = (Button) view.findViewById(R.id.buttonCancelar);
        Button buttonGuardar = (Button) view.findViewById(R.id.buttonGuardar);
        spReclamos = (Spinner) view.findViewById(R.id.spReclamos);
        final EditText editTextObservaciones = (EditText) view.findViewById(R.id.editTextObservaciones);

        try{
            spReclamos.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
            final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, spinnerReclamos);

            for (int i = 0; i < WebService.ArrayReclamos.size(); i++) {
                String nombreAgregar1 = WebService.ArrayReclamos.get(i).getNom_Pausa();
                spinnerReclamos.add(nombreAgregar1);
            }
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spReclamos.setAdapter(dataAdapter);

        }catch (Exception ex){
            ex.printStackTrace();
        }

        spReclamos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String rec = spReclamos.getSelectedItem().toString().trim();
                    MotivoPausa instanciaRec = new MotivoPausa();
                    for (int x = 0; x < WebService.ArrayReclamos.size(); x++) {
                        instanciaRec = WebService.ArrayReclamos.get(x);
                        if (instanciaRec.getNom_Pausa().trim().equals(rec.trim())) {
                            cod = Integer.valueOf(instanciaRec.getCod_Pausa().trim());
                            break;
                        }
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setView(view);

        final AlertDialog dialog = builder.create();

        buttonCancelar.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        buttonGuardar.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                String obs = editTextObservaciones.getText().toString();
                if( !obs.trim().isEmpty()) {
                    try {
                        observ = obs;
                        mListener.onDialogNormalGuardarClick(Dialog_Reclamo_Fact.this);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    editTextObservaciones.setError("Debe ingresar los valores");
                }
            }
        });

        return dialog;
    }
}
