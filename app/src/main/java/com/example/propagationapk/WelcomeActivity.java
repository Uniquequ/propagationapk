package com.example.propagationapk;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class WelcomeActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
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
        LinearLayout linearLayout = findViewById(R.id.buttons_container);


        // Listener dla przycisku pokazującego przykładowy tekst


        showTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) linearLayout.getLayoutParams();

                // Usuń istniejące ograniczenie do góry i środka
                params.topToTop = -1; // Usuwa app:layout_constraintTop_toTopOf="parent"
                params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID; // Przyklej do dołu rodzica
                params.verticalBias = 1.0f; // Upewnij się, że jest na dole

                // Zastosuj nowe parametry
                linearLayout.setLayoutParams(params);
                sampleText.setText("Przechodząc do obliczeń należy podać wybrane parametry w zależności od wybranego środowiska:\n" +
                        "Rodzaje dostępnych środowisk:\n" +
                        "RMa - makrokomórka w środowisku wiejskim\n" +
                        "UMi - mikrokomórka w środowisku zurbanizowanym\n" +
                        "UMa - makrokomórka w środowisku zurbanizowanym\n" +
                        "InH - środowisko biurowe\n" +
                        "InF - środowisko przemysłowe\n\n" +
                        "Odległość między stacjami - należy podać odległość w metrach\n" +
                        "Wysokość zawieszenia anteny nadawczej - wysokość podana w metrach\n" +
                        "Wysokość zawieszenia terminala użytkownika - wysokość w metrach\n" +
                        "Efektywna wysokość środowiska - wysokość w metrach\n" +
                        "Częstotliwość - należy podać używaną częstotliwość w GHz\n" +
                        "Średnia szerokość ulic - przyjęta szerokość w metrach\n" +
                        "Średnia wysokość budynków - przyjęta wysokość w metrach");
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
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) linearLayout.getLayoutParams();
                params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
                params.verticalBias = 0.5f; // Wyśrodkowanie pionowe
                linearLayout.setLayoutParams(params);
                sampleText.setText("Autorzy aplikacji:\nBartosz Nowak 193631\nOlaf Oleksiak\nAndrzej Czechowski\nFilip Cybulski\nMaciej Demski");
            }
        });
    }
}