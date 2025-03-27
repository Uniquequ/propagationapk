package com.example.propagationapk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Inicjalizacja elementów UI
        TextView welcomeText = findViewById(R.id.welcome_text); // Nadal inicjalizujemy, ale nie zmieniamy widoczności
        TextView sampleText = findViewById(R.id.sample_text);
        Button showTextButton = findViewById(R.id.show_text_button);
        Button goToCalculatorButton = findViewById(R.id.go_to_calculator_button);
        Button authorsButton = findViewById(R.id.authors_button);

        // Listener dla przycisku pokazującego przykładowy tekst
        showTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sampleText.setText("Przykładowe obliczenie: dla RMa LoS, d2D=100m, hBS=35m, hUT=1.5m, freq=2GHz, h=5m, tłumienie wynosi około 80.74 dB.");
            }
        });

        // Listener dla przycisku przechodzącego do ekranu obliczeń
        goToCalculatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Listener dla przycisku "Autorzy"
        authorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sampleText.setText("Autorzy aplikacji:\nBartosz Nowak 193631\nOlaf Oleksiak\nAndrzej Czechowski\nFilip Cybulski\nMaciej Demski");
            }
        });
    }
}