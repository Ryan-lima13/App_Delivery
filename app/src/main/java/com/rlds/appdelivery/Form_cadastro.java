package com.rlds.appdelivery;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Form_cadastro extends AppCompatActivity {
    private EditText editTextNome, editTextEmail, editTextSenha;
    private CircleImageView fotoUsuario;
    private Button buttonCadastrar, buttonSelecionarFoto;
    private TextView txt_mensagemErro;
    private Uri mSelecionarUri;
    private  String usuarioId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        // configuracões iniciais
        inicializarComponentes();
        editTextNome.addTextChangedListener(cadastroTextWatcher);
        editTextEmail.addTextChangedListener(cadastroTextWatcher);
        editTextSenha.addTextChangedListener(cadastroTextWatcher);
        // cadastrar usuario
        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarUsuario(view);
            }
        });
        // selecionar foto
        buttonSelecionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionarFotoGaleria();
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

                    salvarDadosUsuario();
                    Snackbar snackbar = Snackbar.make(
                            view, "Cadastro realizado com sucesso!", Snackbar.LENGTH_INDEFINITE
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
    ActivityResultLauncher<Intent>activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        mSelecionarUri = data.getData();

                        try {
                            fotoUsuario.setImageURI(mSelecionarUri);

                        }catch (Exception e){
                            e.printStackTrace();

                        }


                    }

                }
            }
    );
    public  void selecionarFotoGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activityResultLauncher.launch(intent);


    }
    public void salvarDadosUsuario(){
        String nomeArquivo = UUID.randomUUID().toString();
        final StorageReference reference = FirebaseStorage.getInstance().getReference("/imagens/" + nomeArquivo);
        reference.putFile(mSelecionarUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String foto = uri.toString();
                        // iniciar banco de dados - firestorage
                        String nome = editTextNome.getText().toString();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        Map<String, Object> usuarios = new HashMap<>();
                        usuarios.put("nome", nome);
                        usuarios.put("foto",foto);
                        // pegando id usuario
                        usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        DocumentReference documentReference = db.collection("usuarios")
                                .document(usuarioId);
                        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.i("db", "sucesso ao salvar dados");

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("erro", "erro ao salvar dados " + e.toString());

                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

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