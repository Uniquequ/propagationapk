package com.example.propagationapk;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText d2DInput, hBSInput, hUTInput, hEInput, freqInput, wInput, hInput;
    private Spinner scenarioSpinner, losNlosSpinner, infNlosSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicjalizacja pól
        d2DInput = findViewById(R.id.d2D_input);
        hBSInput = findViewById(R.id.hBS_input);
        hUTInput = findViewById(R.id.hUT_input);
        hEInput = findViewById(R.id.hE_input);
        freqInput = findViewById(R.id.freq_input);
        wInput = findViewById(R.id.w_input);
        hInput = findViewById(R.id.h_input);

        scenarioSpinner = findViewById(R.id.scenario_spinner);
        losNlosSpinner = findViewById(R.id.los_nlos_spinner);
        infNlosSpinner = findViewById(R.id.inf_nlos_spinner);

        Button calculateButton = findViewById(R.id.calculate_button);
        TextView resultText = findViewById(R.id.result_text);

        // Ustawienie adapterów dla spinnerów
        String[] scenarios = {"RMa", "UMa", "UMi", "InH", "InF"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, scenarios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scenarioSpinner.setAdapter(adapter);

        String[] losNlosOptions = {"LoS", "NLoS"};
        ArrayAdapter<String> losNlosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, losNlosOptions);
        losNlosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        losNlosSpinner.setAdapter(losNlosAdapter);

        String[] infNlosOptions = {"SL", "DL", "SH", "DH"};
        ArrayAdapter<String> infNlosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, infNlosOptions);
        infNlosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        infNlosSpinner.setAdapter(infNlosAdapter);

        // Początkowo ukryj wszystkie pola i infNlosSpinner
        hideAllFields();
        infNlosSpinner.setVisibility(View.GONE);

        // Listener dla wyboru scenariusza
        scenarioSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                updateFieldVisibility();
                if (scenarioSpinner.getSelectedItem().toString().equals("InF") && losNlosSpinner.getSelectedItem().toString().equals("NLoS")) {
                    infNlosSpinner.setVisibility(View.VISIBLE);
                } else {
                    infNlosSpinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // Listener dla wyboru LoS/NLoS
        losNlosSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                updateFieldVisibility();
                if (scenarioSpinner.getSelectedItem().toString().equals("InF") && losNlosSpinner.getSelectedItem().toString().equals("NLoS")) {
                    infNlosSpinner.setVisibility(View.VISIBLE);
                } else {
                    infNlosSpinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // Przycisk obliczania
        calculateButton.setOnClickListener(v -> {
            try {
                String scenario = scenarioSpinner.getSelectedItem().toString();
                int losNlosChoice = losNlosSpinner.getSelectedItem().toString().equals("LoS") ? 1 : 2;
                String result;

                double d2D = Double.parseDouble(d2DInput.getText().toString()); //d2d i inne przenoszone wyżej poza switcha
                double hBS = Double.parseDouble(hBSInput.getText().toString());
                double hUT = Double.parseDouble(hUTInput.getText().toString());

                switch (scenario) {
                    case "RMa":
                        double freq_rma = Double.parseDouble(freqInput.getText().toString());
                        double h_rma = Double.parseDouble(hInput.getText().toString());
                        double w_rma = 0;
                        if (losNlosChoice==2){
                            w_rma = Double.parseDouble(wInput.getText().toString());
                        }
                        PropagationLossCalculator calculator_rma = new PropagationLossCalculator(d2D, hBS, hUT, 0, freq_rma, w_rma, h_rma); // hE i W niepotrzebne
                        result = calculator_rma.calculateRMa(losNlosChoice);
                        break;

                    case "UMa":
                        double hE_uma = Double.parseDouble(hEInput.getText().toString());
                        double freq_uma = Double.parseDouble(freqInput.getText().toString());
                        PropagationLossCalculator calculator_uma = new PropagationLossCalculator(d2D, hBS, hUT, hE_uma, freq_uma, 0, 0); // hE i W niepotrzebne
                        result = calculator_uma.calculateUMa(losNlosChoice);
                        break;

                    case "UMi":

                        double hE_um = Double.parseDouble(hEInput.getText().toString());
                        double freq_um = Double.parseDouble(freqInput.getText().toString());
                        PropagationLossCalculator calculator_um = new PropagationLossCalculator(d2D, hBS, hUT, hE_um, freq_um, 0, 0);
                        result = calculator_um.calculateUMi(losNlosChoice);
                        break;

                    case "InH":
                        double freq_inh = Double.parseDouble(freqInput.getText().toString());
                        PropagationLossCalculator calculator_inh = new PropagationLossCalculator(d2D, hBS, hUT, 0, freq_inh, 0, 0);
                        result = calculator_inh.calculateInH(losNlosChoice);
                        break;

                    case "InF":
                        double freq_inf = Double.parseDouble(freqInput.getText().toString());
                        PropagationLossCalculator calculator_inf = new PropagationLossCalculator(d2D, hBS, hUT, 0, freq_inf, 0, 0); // hE, W, h niepotrzebne
                        int nlosOption = infNlosSpinner.getSelectedItemPosition() + 1;
                        result = calculator_inf.calculateInF(losNlosChoice, losNlosChoice == 2 ? nlosOption : 0);
                        break;

                    default:
                        result = "Nieprawidłowy scenariusz!";
                }
                resultText.setText(result);
            } catch (NumberFormatException e) {
                resultText.setText("Wprowadź poprawne wartości liczbowe!");
            }
        });
    }

    // Metoda do ukrywania wszystkich pól
    private void hideAllFields() {
        d2DInput.setVisibility(View.GONE);
        hBSInput.setVisibility(View.GONE);
        hUTInput.setVisibility(View.GONE);
        hEInput.setVisibility(View.GONE);
        freqInput.setVisibility(View.GONE);
        wInput.setVisibility(View.GONE);
        hInput.setVisibility(View.GONE);
    }

    // Metoda do aktualizacji widoczności pól w zależności od scenariusza
    private void updateFieldVisibility() {
        hideAllFields();
        String scenario = scenarioSpinner.getSelectedItem().toString();
        int losNlosChoice = losNlosSpinner.getSelectedItem().toString().equals("LoS") ? 1 : 2;

        switch (scenario) {
            case "RMa":
                d2DInput.setVisibility(View.VISIBLE);
                hBSInput.setVisibility(View.VISIBLE);
                hUTInput.setVisibility(View.VISIBLE);
                freqInput.setVisibility(View.VISIBLE);
                hInput.setVisibility(View.VISIBLE);
                if (losNlosChoice == 2){
                    wInput.setVisibility(View.VISIBLE);
                }
                break;
            case "UMa":
                d2DInput.setVisibility(View.VISIBLE);
                hBSInput.setVisibility(View.VISIBLE);
                hUTInput.setVisibility(View.VISIBLE);
                hEInput.setVisibility(View.VISIBLE);
                freqInput.setVisibility(View.VISIBLE);
                break;
            case "UMi":
                d2DInput.setVisibility(View.VISIBLE);
                hBSInput.setVisibility(View.VISIBLE);
                hUTInput.setVisibility(View.VISIBLE);
                hEInput.setVisibility(View.VISIBLE);
                freqInput.setVisibility(View.VISIBLE);
                break;
            case "InH":
                d2DInput.setVisibility(View.VISIBLE);
                hBSInput.setVisibility(View.VISIBLE);
                hUTInput.setVisibility(View.VISIBLE);
                freqInput.setVisibility(View.VISIBLE);
                break;
            case "InF":
                d2DInput.setVisibility(View.VISIBLE);
                hBSInput.setVisibility(View.VISIBLE);
                hUTInput.setVisibility(View.VISIBLE);
                freqInput.setVisibility(View.VISIBLE);
                break;
        }
    }
}