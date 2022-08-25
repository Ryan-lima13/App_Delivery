package com.rlds.appdelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Lista_Produtos extends AppCompatActivity {
    private RecyclerView recyclerViewProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);
        recyclerViewProdutos = findViewById(R.id.recyclerViewProdutos);

    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.perfil){
            Intent intent = new Intent(Lista_Produtos.this, Perfil_Usuario.class);
            startActivity(intent);

        }else  if(itemId == R.id.pedidos){

        }else if(itemId == R.id.deslogar){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(Lista_Produtos.this, " Usuario Deslogado", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Lista_Produtos.this, Form_login.class);
            startActivity(intent);
            finish();

            

        }
        return super.onOptionsItemSelected(item);
    }
}