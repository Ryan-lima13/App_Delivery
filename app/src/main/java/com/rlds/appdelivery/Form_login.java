package com.rlds.appdelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Form_login extends AppCompatActivity {
    private TextView txt_criar_conta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        getSupportActionBar().hide();
        // configuracões iniciais
        iniciarComponentes();
        txt_criar_conta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaCadastro = new Intent(Form_login.this, Form_cadastro.class);
                startActivity(telaCadastro);
            }
        });

    }
    public void iniciarComponentes(){
        txt_criar_conta = findViewById(R.id.txt_craiar_conta);
    }
}