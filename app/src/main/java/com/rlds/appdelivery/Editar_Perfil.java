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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Editar_Perfil extends AppCompatActivity {
    private CircleImageView fotousuarioEditar;
    private EditText editTextNomeEditar;
    private Button buttonSeleionarFotoEditar, buttonAtualizar;
    private Uri mSelecionarUri;
    private  String usuarioId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        // configuracoes iniciais
        inicializrComponentes();
        buttonSeleionarFotoEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionarFotoGaleria();

            }
        });
        buttonAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = editTextNomeEditar.getText().toString();
                if (nome.isEmpty()){
                    Snackbar snackbar = Snackbar.make(view, "Preencha o nome!",
                            Snackbar.LENGTH_SHORT

                    );
                    snackbar.show();

                }else {
                    // atualizar dados perfil
                    atualizarDados(view);


                }
            }
        });


    }

    private  void inicializrComponentes(){
        fotousuarioEditar = findViewById(R.id.foto_usuario_editar);
        editTextNomeEditar = findViewById(R.id.editNomeEditar);
        buttonAtualizar = findViewById(R.id.bt_atualizar);
        buttonSeleionarFotoEditar = findViewById(R.id.bt_editar_selecionar_foro);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        mSelecionarUri = data.getData();

                        try {
                            fotousuarioEditar.setImageURI(mSelecionarUri);

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
    public  void atualizarDados(View v){

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
                        String nome = editTextNomeEditar.getText().toString();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        Map<String, Object> usuarios = new HashMap<>();
                        usuarios.put("nome", nome);
                        usuarios.put("foto",foto);
                        // pegando id usuario
                        usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                         db.collection("usuarios")
                                .document(usuarioId).update("nome", nome, "foto", foto).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Snackbar snackbar = Snackbar.make(v,"Sucesso ao atualizar Dados",Snackbar.LENGTH_INDEFINITE)
                                                .setAction("ok", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        finish();
                                                    }

                                                });
                                        snackbar.show();

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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}