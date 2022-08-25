package com.rlds.appdelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Adapter.AdapterProduto;
import Model.Produtos;
import RecyclerItemClickListener.RecyclerItemClickListener;

public class Lista_Produtos extends AppCompatActivity {
    private RecyclerView recyclerViewProdutos;
    private AdapterProduto adapterProduto;
    private List<Produtos>listaProdutos = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);
        recyclerViewProdutos = findViewById(R.id.recyclerViewProdutos);
        adapterProduto = new AdapterProduto(getApplicationContext(),listaProdutos);
        recyclerViewProdutos.setLayoutManager( new LinearLayoutManager(getApplicationContext()));
        recyclerViewProdutos.setHasFixedSize(true);
        recyclerViewProdutos.setAdapter(adapterProduto);
        // evendo de clique REcyclerViewr
        recyclerViewProdutos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerViewProdutos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(Lista_Produtos.this, Detalhes_Produtos.class);
                                startActivity(intent);


                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );

        db = FirebaseFirestore.getInstance();
        db.collection("produtos")
                .orderBy("nome").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot queryDocumentSnapshots: task.getResult()){
                        Produtos produtos = queryDocumentSnapshots.toObject(Produtos.class);
                        listaProdutos.add(produtos);

                        adapterProduto.notifyDataSetChanged();

                    }
                }

            }
        });




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