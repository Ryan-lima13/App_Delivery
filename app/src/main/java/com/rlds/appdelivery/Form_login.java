package com.rlds.appdelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Form_login extends AppCompatActivity {
    private TextView txt_criar_conta ,txt_mensagemErro;
    private Button buttonEntrar;
    private EditText editTextEmail, editTextSenha;
    private ProgressBar progressBar;

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
        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String senha = editTextSenha.getText().toString();
                if(email.isEmpty() || senha.isEmpty()){
                    txt_mensagemErro.setText("Preencha todos os campos!");

                }else {
                    txt_mensagemErro.setText("");
                    // autenticar usuario
                    autenticarUsuario();

                }
            }
        });

    }
    public void iniciarComponentes(){
        txt_criar_conta = findViewById(R.id.txt_craiar_conta);
        editTextEmail = findViewById(R.id.edit_email);
        editTextSenha = findViewById(R.id.edit_senha);
        buttonEntrar = findViewById(R.id.bt_entrar);
        progressBar = findViewById(R.id.progressBar);
        txt_mensagemErro = findViewById(R.id.txt_mensagemErro);
    }
    private  void autenticarUsuario(){
        String email = editTextEmail.getText().toString();
        String senha = editTextSenha.getText().toString();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                email, senha
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.VISIBLE);
                    // ficar progressBar 3 segundo na tela
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            iniciarTelaProdutos();

                        }
                    },3000);


                }else{
                    String erro;
                    try {
                        throw  task.getException();

                    }catch (Exception e){
                        erro = "Erro ao fazer Login!";

                    }
                    txt_mensagemErro.setText(erro);
                }

            }
        });
    }
    public  void iniciarTelaProdutos(){
        Intent intent = new Intent(Form_login.this, Lista_Produtos.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if(usuarioAtual != null){
            iniciarTelaProdutos();
        }
    }
}