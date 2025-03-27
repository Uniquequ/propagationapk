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

    private TextView d2DText, hBSText, hUTText, hEText, freqText, wText, hText;
    private EditText d2DInput, hBSInput, hUTInput, hEInput, freqInput, wInput, hInput;
    private Spinner scenarioSpinner, losNlosSpinner, infNlosSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicjalizacja pól
        d2DText = findViewById(R.id.d2D_text);
        d2DInput = findViewById(R.id.d2D_input);
        hBSText = findViewById(R.id.hBS_text);
        hBSInput = findViewById(R.id.hBS_input);
        hUTText = findViewById(R.id.hUT_text);
        hUTInput = findViewById(R.id.hUT_input);
        hEText = findViewById(R.id.hE_text);
        hEInput = findViewById(R.id.hE_input);
        freqText = findViewById(R.id.freq_text);
        freqInput = findViewById(R.id.freq_input);
        wText = findViewById(R.id.w_text);
        wInput = findViewById(R.id.w_input);
        hText = findViewById(R.id.h_text);
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
                double freq = Double.parseDouble(freqInput.getText().toString());

                switch (scenario) {
                    case "RMa":
                        double h_rma = Double.parseDouble(hInput.getText().toString());
                        double w_rma = 0;
                        if (losNlosChoice==2){
                            w_rma = Double.parseDouble(wInput.getText().toString());
                        }
                        PropagationLossCalculator calculator_rma = new PropagationLossCalculator(d2D, hBS, hUT, 0, freq, w_rma, h_rma); // hE i W niepotrzebne
                        result = calculator_rma.calculateRMa(losNlosChoice);
                        break;

                    case "UMa":
                        double hE_uma = Double.parseDouble(hEInput.getText().toString());
                        PropagationLossCalculator calculator_uma = new PropagationLossCalculator(d2D, hBS, hUT, hE_uma, freq, 0, 0); // hE i W niepotrzebne
                        result = calculator_uma.calculateUMa(losNlosChoice);
                        break;

                    case "UMi":

                        double hE_um = Double.parseDouble(hEInput.getText().toString());
                        PropagationLossCalculator calculator_um = new PropagationLossCalculator(d2D, hBS, hUT, hE_um, freq, 0, 0);
                        result = calculator_um.calculateUMi(losNlosChoice);
                        break;

                    case "InH":
                        PropagationLossCalculator calculator_inh = new PropagationLossCalculator(d2D, hBS, hUT, 0, freq, 0, 0);
                        result = calculator_inh.calculateInH(losNlosChoice);
                        break;

                    case "InF":
                        PropagationLossCalculator calculator_inf = new PropagationLossCalculator(d2D, hBS, hUT, 0, freq, 0, 0); // hE, W, h niepotrzebne
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
        d2DText.setVisibility(View.GONE);
        d2DInput.setVisibility(View.GONE);
        hBSText.setVisibility(View.GONE);
        hBSInput.setVisibility(View.GONE);
        hUTText.setVisibility(View.GONE);
        hUTInput.setVisibility(View.GONE);
        hEText.setVisibility(View.GONE);
        hEInput.setVisibility(View.GONE);
        freqText.setVisibility(View.GONE);
        freqInput.setVisibility(View.GONE);
        wText.setVisibility(View.GONE);
        wInput.setVisibility(View.GONE);
        hText.setVisibility(View.GONE);
        hInput.setVisibility(View.GONE);
    }

    // Metoda do aktualizacji widoczności pól w zależności od scenariusza
    private void updateFieldVisibility() {
        hideAllFields();
        String scenario = scenarioSpinner.getSelectedItem().toString();
        int losNlosChoice = losNlosSpinner.getSelectedItem().toString().equals("LoS") ? 1 : 2;

        d2DText.setVisibility(View.VISIBLE);
        d2DInput.setVisibility(View.VISIBLE);
        hBSText.setVisibility(View.VISIBLE);
        hBSInput.setVisibility(View.VISIBLE);
        hUTText.setVisibility(View.VISIBLE);
        hUTInput.setVisibility(View.VISIBLE);
        freqText.setVisibility(View.VISIBLE);
        freqInput.setVisibility(View.VISIBLE);


        switch (scenario) {
            case "RMa":
                hText.setVisibility(View.VISIBLE);
                hBSInput.setHint("Domyślna wartość to 35");
                hUTInput.setHint("Domyślna wartość to 1.5");
                hInput.setVisibility(View.VISIBLE);
                if (losNlosChoice == 2){
                    wText.setVisibility(View.VISIBLE);
                    wInput.setVisibility(View.VISIBLE);
                }
                break;
            case "UMa":
                hBSInput.setHint("Domyślna wartość to 25");
                hUTInput.setHint("Podaj wartość [1.5, 22.5]");
                hEText.setVisibility(View.VISIBLE);
                hEInput.setVisibility(View.VISIBLE);
                break;
            case "UMi":
                hBSInput.setHint("Domyślna wartość to 10");
                hUTInput.setHint("Podaj wartość [1.5, 22.5]");
                hEText.setVisibility(View.VISIBLE);
                hEInput.setVisibility(View.VISIBLE);
                break;
        }
    }
}