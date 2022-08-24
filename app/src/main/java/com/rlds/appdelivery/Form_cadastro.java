package com.rlds.appdelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.ktx.Firebase;

import de.hdodenhof.circleimageview.CircleImageView;

public class Form_cadastro extends AppCompatActivity {
    private EditText editTextNome, editTextEmail, editTextSenha;
    private CircleImageView fotoUsuario;
    private Button buttonCadastrar, buttonSelecionarFoto;
    private TextView txt_mensagemErro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        // configuracões iniciais
        inicializarComponentes();
        editTextNome.addTextChangedListener(cadastroTextWatcher);
        editTextEmail.addTextChangedListener(cadastroTextWatcher);
        editTextSenha.addTextChangedListener(cadastroTextWatcher);
        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarUsuario(view);
            }
        });

    }

    private void inicializarComponentes(){
        editTextNome = findViewById(R.id.edit_nome);
        editTextEmail = findViewById(R.id.txt_email);
        editTextSenha = findViewById(R.id.txt_senha);
        buttonCadastrar = findViewById(R.id.bt_cadastrar);
        buttonSelecionarFoto = findViewById(R.id.bt_selecionar_foto);
        txt_mensagemErro = findViewById(R.id.txt_mensagemErro);
        fotoUsuario = findViewById(R.id.fotoUsuario);
    }
    public  void cadastrarUsuario(View view){
        String email = editTextEmail.getText().toString();
        String senha =  editTextSenha.getText().toString();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                email, senha
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Snackbar snackbar = Snackbar.make(
                            view, "Cadstro realizado com sucesso!", Snackbar.LENGTH_INDEFINITE
                    ).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });
                    snackbar.show();
                }else {
                    String erro;
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Digite uma senha com no mínimo 6 caracteres!";
                    }catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = " Digite um email válido!";
                    }catch (FirebaseAuthUserCollisionException e) {
                        erro = "Usuário já cadastrado!";
                    }catch (FirebaseNetworkException e){
                        erro = "SEm conecção com a internet!";

                    }catch (Exception e){
                        erro = "Erro cadastrar o usuário!";

                    }
                    txt_mensagemErro.setText(erro);

                }
            }
        });
    }

    TextWatcher cadastroTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String nome = editTextNome.getText().toString();
            String email = editTextEmail.getText().toString();
            String senha = editTextSenha.getText().toString();
            if(!nome.isEmpty() && !email.isEmpty() && !senha.isEmpty()){
                buttonCadastrar.setEnabled(true);
                buttonCadastrar.setBackgroundColor(getResources().getColor(R.color.dark_red));

            }else {
                buttonCadastrar.setEnabled(false);
                buttonCadastrar.setBackgroundColor(getResources().getColor(R.color.gray));

            }


        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}