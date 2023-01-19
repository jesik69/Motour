package com.example.motour;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Perfil extends AppCompatActivity {

    private TextView txtnombrerec;
    private TextView txtusuariorec;
    private TextView txtapellidorec;
    private TextView txttelefonorecibido;

    private Button btncerrar;
    private Button btnsubirfoto;
    private Button btnactualizar;
    private Button btncrear;
    private Button btnperfil;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;

    private ImageView imageView2;

    private String urlmodf = "";

    // variables camara
    private static final int GALLERY_INTENT = 1;
    private ProgressDialog mProgressDialog;

    private String urlfotoperfil="";
    private String nombre ="";
    private String usuario ="";

    private String telefono ="";


    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        txtnombrerec = findViewById(R.id.txtnombrerec);
        txtusuariorec =  findViewById(R.id.txtusuariorec);


        btncerrar = findViewById(R.id.btncerrar);
        btnsubirfoto = findViewById(R.id.btnsubirfoto);
        btnactualizar = findViewById(R.id.btnactualizar);
        btncrear = findViewById(R.id.btncrear);
        btnperfil = findViewById(R.id.btnperfil);
        imageView2 = findViewById(R.id.imageView2);

        mProgressDialog = new ProgressDialog(this);

        // Boton acerca de
        btnactualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Perfil.this, acercade.class));
            }
        });

        //Inicio boton perfil
        btnperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Perfil.this, Perfilinformacion.class));
            }
        });
        // Fin boton perfil
        // Inicio Evento boton Cerrar Sesion
        btncerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(Perfil.this, MainActivity.class));
                finish();
            }
        });
        // FIN Evento boton Cerrar Sesion
        obtenerinformacion();

        // Inicio Evento boton tomar foto
        btnsubirfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
        // FIN Evento boton tomar foto



        //Inicio evento boton crear ruta
        btncrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Perfil.this, mapa.class));
            }
        });
        //FIN evento boton crear ruta

    }

    // metodo para obtener informacion del usuario
    private void obtenerinformacion(){


        String id = mAuth.getCurrentUser().getUid(); // Me trae el id cuando se crea el usuario

        mDatabase.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    nombre = dataSnapshot.child("nombre").getValue().toString();
                    usuario = dataSnapshot.child("email").getValue().toString();


                        txtnombrerec.setText(nombre);
                        txtusuariorec.setText(usuario);
                        urlfotoperfil = dataSnapshot.child("url").getValue().toString();
                        if (!urlfotoperfil.isEmpty()){
                            Glide.with(Perfil.this)
                                    .load(urlfotoperfil)
                                    .fitCenter()
                                    .centerCrop()
                                    .into(imageView2);
                        }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== GALLERY_INTENT && resultCode == RESULT_OK){
            //mostrar que esta procesando la subida de la foto
                mProgressDialog.setTitle("Por favor espere");
                mProgressDialog.setMessage("Subiendo Foto de perfil");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            //
            Uri uri = data.getData();
            StorageReference filePath = mStorageRef.child("foto_per").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();

                    //Uri urldefoto = ref.getDownloadUrl();
                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uri.isComplete());
                    Uri url = uri.getResult();

                    //Toast.makeText(Perfil.this, "Upload Success, download URL " +url.toString(), Toast.LENGTH_LONG).show();
                    //Log.i("FBApp1 URL ", url.toString());
                    urlmodf = url.toString();

                    final String id = mAuth.getCurrentUser().getUid(); // Me trae el id cuando se crea el usuario
                    mDatabase.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            mDatabase.child("Usuarios").child(id).child("url").setValue(urlmodf);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    Toast.makeText(Perfil.this, "Se subio la foto perfectamente", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
