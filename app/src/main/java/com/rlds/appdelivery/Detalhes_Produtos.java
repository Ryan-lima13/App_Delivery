package com.rlds.appdelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Detalhes_Produtos extends AppCompatActivity {
    private ImageView imageViewProduto;
    private TextView textViewNome, textViewDescricao, textViewValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produtos);
        iniciarComponentes();
    }
    private void iniciarComponentes(){
        imageViewProduto = findViewById(R.id.fotoDetahes);
        textViewDescricao = findViewById(R.id.descricao);
        textViewNome = findViewById(R.id.nomeProdutos);
        textViewValor = findViewById(R.id.valor);
    }
}