package com.example.motour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Perfilinformacion extends AppCompatActivity {

    private EditText nombrerec;
    private EditText apellidorec;
    private EditText telefonorec;
    private EditText rhrec;
    private EditText placarec;
    private EditText cilindrajerec;
    private EditText modelorec;
    private EditText marcarec;
    private EditText nombre_emer_rec;
    private EditText apellido_emer_rec;
    private EditText telefono_emer_rec;
    private EditText parentesco_emer_rec;
    private Button btnactdatos;

    private ImageView fotoperfil;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;

    private String nombre="";
    private String apellido="";
    private String telefono="";
    private String rh="";
    private String urlfotoperfil ="";

    private String placa ="";
    private String marca ="";
    private String modelo ="";
    private String cilindraje ="";
    private String nombre_emerg ="";
    private String apellido_emerg ="";
    private String telefono_emerg ="";
    private String parentesco_emerg ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilinformacion);

        nombrerec = findViewById(R.id.nombrerec);
        apellidorec = findViewById(R.id.apellidorec);
        telefonorec = findViewById(R.id.telefonorec);
        rhrec = findViewById(R.id.rhrec);
        fotoperfil = findViewById(R.id.fotoperfil);
        btnactdatos = findViewById(R.id.btnactdatos);

        placarec = findViewById(R.id.placarec);
        cilindrajerec = findViewById(R.id.cilindrajerec);
        modelorec = findViewById(R.id.modelorec);
        marcarec = findViewById(R.id.marcarec);
        nombre_emer_rec = findViewById(R.id.nombre_emer_rec);
        apellido_emer_rec = findViewById(R.id.apellido_emer_rec);
        telefono_emer_rec = findViewById(R.id.telefono_emer_rec);
        parentesco_emer_rec = findViewById(R.id.parentesco_emer_rec);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        obtenerinformacion();

        btnactdatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizardatos();
                startActivity(new Intent(Perfilinformacion.this, Perfil.class));
                finish();
            }
        });

    }

    private void obtenerinformacion(){


        String id = mAuth.getCurrentUser().getUid(); // Me trae el id cuando se crea el usuario
        mDatabase.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    nombre = dataSnapshot.child("nombre").getValue().toString();
                    apellido = dataSnapshot.child("apellido").getValue().toString();
                    telefono = dataSnapshot.child("telefono").getValue().toString();
                    rh = dataSnapshot.child("tipo_de_sangre").getValue().toString();


                    placa = dataSnapshot.child("datos_moto").child("placa").getValue().toString();
                    marca = dataSnapshot.child("datos_moto").child("marca").getValue().toString();
                    modelo = dataSnapshot.child("datos_moto").child("modelo").getValue().toString();
                    cilindraje = dataSnapshot.child("datos_moto").child("cilindraje").getValue().toString();

                    nombre_emerg = dataSnapshot.child("emergencia").child("nombre_emerg").getValue().toString();
                    apellido_emerg = dataSnapshot.child("emergencia").child("apellido_emerg").getValue().toString();
                    telefono_emerg = dataSnapshot.child("emergencia").child("telefono_emerg").getValue().toString();
                    parentesco_emerg = dataSnapshot.child("emergencia").child("parentesco_emerg").getValue().toString();

                    nombrerec.setText(nombre);
                    apellidorec.setText(apellido);
                    telefonorec.setText(telefono);
                    rhrec.setText(rh);
                    placarec.setText(placa);
                    cilindrajerec.setText(marca);
                    modelorec.setText(modelo);
                    marcarec.setText(cilindraje);
                    nombre_emer_rec.setText(nombre_emerg);
                    apellido_emer_rec.setText(apellido_emerg);
                    telefono_emer_rec.setText(telefono_emerg);
                    parentesco_emer_rec.setText(parentesco_emerg);
                    urlfotoperfil = dataSnapshot.child("url").getValue().toString();
                    if (!urlfotoperfil.isEmpty()){
                        Glide.with(Perfilinformacion.this)
                                .load(urlfotoperfil)
                                .fitCenter()
                                .centerCrop()
                                .into(fotoperfil);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void actualizardatos(){

        nombre = nombrerec.getText().toString();
        apellido = apellidorec.getText().toString();
        telefono = telefonorec.getText().toString();
        rh = rhrec.getText().toString();
        placa = placarec.getText().toString();
        marca = cilindrajerec.getText().toString();
        modelo = modelorec.getText().toString();
        cilindraje = marcarec.getText().toString();
        nombre_emerg = nombre_emer_rec.getText().toString();
        apellido_emerg = apellido_emer_rec.getText().toString();
        telefono_emerg = telefono_emer_rec.getText().toString();
        parentesco_emerg = parentesco_emer_rec.getText().toString();


        final String id = mAuth.getCurrentUser().getUid(); // Me trae el id cuando se crea el usuario
        mDatabase.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDatabase.child("Usuarios").child(id).child("nombre").setValue(nombre);
                mDatabase.child("Usuarios").child(id).child("apellido").setValue(apellido);
                mDatabase.child("Usuarios").child(id).child("telefono").setValue(telefono);
                mDatabase.child("Usuarios").child(id).child("rh").setValue(rh);

                mDatabase.child("Usuarios").child(id).child("datos_moto").child("placa").setValue(placa);
                mDatabase.child("Usuarios").child(id).child("datos_moto").child("marca").setValue(marca);
                mDatabase.child("Usuarios").child(id).child("datos_moto").child("cilindraje").setValue(cilindraje);
                mDatabase.child("Usuarios").child(id).child("datos_moto").child("modelo").setValue(modelo);

                mDatabase.child("Usuarios").child(id).child("emergencia").child("nombre_emerg").setValue(nombre_emerg);
                mDatabase.child("Usuarios").child(id).child("emergencia").child("apellido_emerg").setValue(apellido_emerg);
                mDatabase.child("Usuarios").child(id).child("emergencia").child("telefono_emerg").setValue(telefono_emerg);
                mDatabase.child("Usuarios").child(id).child("emergencia").child("parentesco_emerg").setValue(parentesco_emerg);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

