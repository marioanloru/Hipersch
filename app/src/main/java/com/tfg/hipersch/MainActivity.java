package com.tfg.hipersch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  get token from app shared info
        System.out.println("IMPRIMO EL TOKEN!!!" + TokenSaver.getToken(this).toString());
    }
}
