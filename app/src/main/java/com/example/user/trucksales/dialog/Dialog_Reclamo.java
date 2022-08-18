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
import android.widget.TextView;

import com.example.user.trucksales.Datos.WebService;
import com.example.user.trucksales.Encapsuladoras.MotivoPausa;
import com.example.user.trucksales.R;

import java.util.ArrayList;
import java.util.List;

public class Dialog_Reclamo extends DialogFragment implements AdapterView.OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public int cantidadIngresada = 0;
    public int cod = 0;
    public String repo = "";
    public String observ = "";
    public boolean pide_cant = false;
    Spinner spReclamos;
    public List<String> spinnerReclamos = new ArrayList<>(  );


    Spinner spRepo;
    List<String> spinnerRepoArray = new ArrayList<>(  );

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

        View view = inflater.inflate(R.layout.dialog_reclamo, null);

        Button buttonCancelar = (Button) view.findViewById(R.id.buttonCancelar);
        Button buttonGuardar = (Button) view.findViewById(R.id.buttonGuardar);
        final EditText editText = (EditText) view.findViewById(R.id.editText);
        final TextView textViewCant = (TextView) view.findViewById(R.id.textViewCant);
        final TextView textViewRepo = (TextView) view.findViewById(R.id.textViewRepo);
        spReclamos = (Spinner) view.findViewById(R.id.spReclamos);
        final EditText editTextObservaciones = (EditText) view.findViewById(R.id.editTextObservaciones);
        spRepo = (Spinner) view.findViewById(R.id.spRepo);

        editText.setVisibility(View.GONE);
        textViewCant.setVisibility(View.GONE);
        spRepo.setVisibility(View.GONE);
        textViewRepo.setVisibility(View.GONE);

        try {
            spReclamos.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
            final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, spinnerReclamos);

            for (int i = 0; i < WebService.ArrayReclamos.size(); i++) {
                String nombreAgregar1 = WebService.ArrayReclamos.get(i).getNom_Pausa();
                spinnerReclamos.add(nombreAgregar1);
            }
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spReclamos.setAdapter(dataAdapter);

            spRepo.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
            final ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, spinnerRepoArray);

            spinnerRepoArray.add("Si");
            spinnerRepoArray.add("No");

            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRepo.setAdapter(dataAdapter2);
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
                            if(instanciaRec.getPide_cant().equals("S")){
                                editText.setVisibility(View.VISIBLE);
                                textViewCant.setVisibility(View.VISIBLE);
                                spRepo.setVisibility(View.VISIBLE);
                                textViewRepo.setVisibility(View.VISIBLE);
                                pide_cant = true;
                            }else {
                                editText.setVisibility(View.GONE);
                                textViewCant.setVisibility(View.GONE);
                                spRepo.setVisibility(View.GONE);
                                textViewRepo.setVisibility(View.GONE);
                                pide_cant = false;
                            }
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

        spRepo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    repo = spRepo.getSelectedItem().toString().trim();
                    repo = String.valueOf(repo.charAt(0));

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
                String text = "0";
                if(pide_cant){
                    text = editText.getText().toString();
                }

                String obs = editTextObservaciones.getText().toString();
                cantidadIngresada = 0;
                //editText.setError(null);
                if( !obs.trim().isEmpty()) {
                    try {
                        cantidadIngresada = Integer.parseInt(text);
                        observ = obs;

                       /* WebService.instaItem.setCant(Integer.valueOf(text));
                        WebService.instaItem.setCod_rec(Integer.valueOf(cod));
                        WebService.instaItem.setObservacion(observ);*/

                        mListener.onDialogNormalGuardarClick(Dialog_Reclamo.this);
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
