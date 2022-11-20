package com.example.homebudgetpollub;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class AccountActivity extends AppCompatActivity {

    private TextView userEmail;
    private Button logoutButton;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        userEmail = findViewById(R.id.userEmail);
        logoutButton = findViewById(R.id.logoutButton);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account info");

        userEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(AccountActivity.this)
                        .setTitle("Home budget pollub")
                        .setMessage("Are you suer you want to log out?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> {
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent( AccountActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

    }

}