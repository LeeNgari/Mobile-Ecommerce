package com.example.exam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



public class welcome extends AppCompatActivity {

    // UI elements
    Button btn_login;      // Button for login
    Button btn_register;   // Button for registration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enable edge-to-edge layout
        setContentView(R.layout.activity_welcome); // Set the content view

        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        // Click listener for the login button
        btn_login.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Login.class); // Intent to open Login activity
            startActivity(intent);
            finish(); // Close the current activity
        });

        // Click listener for the registration button
        btn_register.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Registration.class); // Intent to open Registration activity
            startActivity(intent);
            finish(); // Close the current activity
        });
    }
}
