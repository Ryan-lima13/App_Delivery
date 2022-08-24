package com.rlds.appdelivery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Perfil_Usuario extends AppCompatActivity {
    private CircleImageView fotoUsuario;
    private TextView txtEmail, txtNome;
    private Button buttonEditarPerfil;
    private  String usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        // configurações iniciais
        inicializarComponentes();
        buttonEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Perfil_Usuario.this, Editar_Perfil.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference documentReference = db.collection("usuarios").document(usuarioId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot != null){
                    Glide.with(getApplicationContext()).load(documentSnapshot.getString("foto")).into(fotoUsuario);
                    txtNome.setText(documentSnapshot.getString("nome"));
                    txtEmail.setText(email);


                }
            }
        });

    }

    private void inicializarComponentes(){
        fotoUsuario = findViewById(R.id.foto_usuario);
        txtEmail = findViewById(R.id.nome_usuario);
        txtNome = findViewById(R.id.email_usuario);
        buttonEditarPerfil = findViewById(R.id.bt_editar_perfil);
    }
}