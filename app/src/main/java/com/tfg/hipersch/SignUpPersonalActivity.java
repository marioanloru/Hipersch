package com.tfg.hipersch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SignUpPersonalActivity extends AppCompatActivity {
    private String password;
    private String email;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_sign_up_personal);

        Intent intent = getIntent();
        name = intent.getStringExtra(SignUpActivity.NAME);
        email = intent.getStringExtra(SignUpActivity.EMAIL);
        password = intent.getStringExtra(SignUpActivity.PASSWORD);
    }

    public void register() {
        //Mandar peticion de registro
    }
}
