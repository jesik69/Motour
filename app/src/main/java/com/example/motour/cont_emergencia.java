package com.example.motour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class cont_emergencia extends AppCompatActivity {

    private EditText txtnombre;
    private EditText txtapellido;
    private EditText txttelefono;
    private EditText txtparentesco;
    private EditText txtplaca;
    private EditText txtcilindraje;
    private EditText txtmarca;
    private EditText txtmodelo;

    private Button btnregistrar;
    private Button btnvolver;
    private Button btnlimpiar;

    private String nombre_emergencia="";
    private String apellido_emergencia ="";
    private String telefono_emergencia="";
    private String parentesco_emergencia="";

    private String placa="";
    private String cilindraje="";
    private String modelo="";
    private String marca="";
    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cont_emergencia);

        txtnombre = findViewById(R.id.txtNombre);
        txtapellido = findViewById(R.id.txtapellido);
        txttelefono = findViewById(R.id.txttelefono2);
        txtparentesco = findViewById(R.id.txtparentesco);
        txtplaca = findViewById(R.id.txtplaca);
        txtcilindraje = findViewById(R.id.txtcilindraje);
        txtmarca = findViewById(R.id.txtmarca);
        txtmodelo = findViewById(R.id.txtmodelo);

        btnregistrar = findViewById(R.id.btnregistrar);
        btnvolver = findViewById(R.id.btnvolver);
        btnlimpiar = findViewById(R.id.btnlimpiar);

        //instanciando a firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference(); //Referencia nodo principal de base de datos

        btnlimpiar=(Button)findViewById(R.id.btnlimpiar);
        /*btnlimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtnombre.setText("");
                txtapellido.setText("");
                txttelefono.setText("");
                txtparentesco.setText("");
                txtplaca.setText("");
                txtcilindraje.setText("");
                txtmarca.setText("");
                txtmodelo.setText("");
            }
        });*/
        // FIn Evento boton limpiar

        // Evento de boton registrar
        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre_emergencia = txtnombre.getText().toString();
                apellido_emergencia = txtapellido.getText().toString();
                telefono_emergencia = txttelefono.getText().toString();
                parentesco_emergencia = txtparentesco.getText().toString();

                placa = txtplaca.getText().toString();
                cilindraje = txtcilindraje.getText().toString();
                marca = txtmarca.getText().toString();
                modelo = txtmodelo.getText().toString();


                if (telefono_emergencia.length()>=1 && telefono_emergencia.length()<=6){
                    txttelefono.setError("Teléfono erroneo. Minimo 7 caracteres");
                    return;
                }else if(telefono_emergencia.length()>10){
                    txttelefono.setError("Teléfono erroneo. Maximo 10 caracteres");
                    return;
                }

                if (placa.length()>7){
                    txttelefono.setError("Placa erronea.Maximo 7 caracteres");
                    return;
                }

                registrarusuario_emer();

            }
        });

        // Fin evento registrar

        btnvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(cont_emergencia.this, Perfil.class));

            }
        });

    }
    private void registrarusuario_emer(){
        final String id = mAuth.getCurrentUser().getUid(); // Me trae el id cuando se crea el usuario
        mDatabase.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDatabase.child("Usuarios").child(id).child("emergencia").child("nombre_emerg").setValue(nombre_emergencia);
                mDatabase.child("Usuarios").child(id).child("emergencia").child("apellido_emerg").setValue(apellido_emergencia);
                mDatabase.child("Usuarios").child(id).child("emergencia").child("telefono_emerg").setValue(telefono_emergencia);
                mDatabase.child("Usuarios").child(id).child("emergencia").child("parentesco_emerg").setValue(parentesco_emergencia);

                mDatabase.child("Usuarios").child(id).child("datos_moto").child("placa").setValue(placa);
                mDatabase.child("Usuarios").child(id).child("datos_moto").child("cilindraje").setValue(cilindraje);
                mDatabase.child("Usuarios").child(id).child("datos_moto").child("marca").setValue(marca);
                mDatabase.child("Usuarios").child(id).child("datos_moto").child("modelo").setValue(modelo);

                startActivity(new Intent(cont_emergencia.this, Perfil.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
