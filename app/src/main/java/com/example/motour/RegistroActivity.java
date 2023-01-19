package com.example.motour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    private EditText txtnombre;
    private EditText txtusuario;
    private EditText txtcontraseña;
    private EditText txtapellido;
    private EditText txttelefono;

    private Button btnregistrar;
    private Button btnlimpiar;
    private Button btnvolver;

    // Variables para registro manual
    private String nombre="";
    private String email="";
    private String contraseña="";
    private String rh2 ="";
    private String apellido="";
    private String telefono="";

    // Cosas de bases de datos
    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    // variables spinner rh

    private Spinner  sptiposdesangre;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);



        /*
         Se instancia cada uno de los modulos
        */

        /** Suma al complejo de este objeto otro complejo.

         * @btbingresar v el complejo que sumamos

         */
        // instanciando a firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference(); //Referencia nodo principal de base de datos

        txtnombre = findViewById(R.id.txtNombre);
        txtusuario = findViewById(R.id.txtusuario);
        txtcontraseña = findViewById(R.id.txtcontraseña);
        txtapellido = findViewById(R.id.txtapellido);
        txttelefono = findViewById(R.id.txttelefono2);
        btnregistrar = findViewById(R.id.btnregistrar);
        btnlimpiar = findViewById(R.id.btnlimpiar);
        btnvolver = findViewById(R.id.btnvolver);
        sptiposdesangre = findViewById(R.id.sptiposdesangre);// spinner
        mProgressDialog = new ProgressDialog(this);


        /*
         Fin de instancia cada uno de los modulos
        */

        // Evento de Spinner RH
        sptiposdesangre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Log.d("TIPO ELEGIDO",parent.getItemAtPosition(position).toString());
                rh2 = sptiposdesangre.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
        // FIN Evento de Spinner RH

        // Evento de boton registrar
                btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = txtnombre.getText().toString();
                email = txtusuario.getText().toString();
                contraseña = txtcontraseña.getText().toString();
                apellido = txtapellido.getText().toString();
                telefono = txttelefono.getText().toString();

                // validacion de datos


                if (email.isEmpty() && contraseña.isEmpty() && nombre.isEmpty() && sptiposdesangre.getSelectedItemPosition()==0 && apellido.isEmpty()){
                    txtnombre.setError("Casilla Vacia");
                    txtusuario.setError("Casilla Vacia");
                    txtcontraseña.setError("Casilla Vacia");
                    txtapellido.setError("Casilla Vacia");
                    if(sptiposdesangre.getSelectedItemPosition()==0){
                        TextView errorText=(TextView)sptiposdesangre.getSelectedView();
                        errorText.setError("");
                        errorText.setTextColor(Color.RED);
                        errorText.setText("!!");
                    }
                    return;
                }
                else{
                    if(sptiposdesangre.getSelectedItemPosition()==0){
                        TextView errorText=(TextView)sptiposdesangre.getSelectedView();
                        errorText.setError("Seleccione su tipo de sangre");
                        errorText.setTextColor(Color.RED);
                        errorText.setText("!");
                        return;
                    }
                    if (!email.isEmpty()){
                        if (!validarEmail(email)){
                            txtusuario.setError("Email Invalido. example@motour.com.co");
                            return;
                        }
                    }
                    else{
                        txtusuario.setError("Casilla Vacia");
                        return;
                    }
                    if (!contraseña.isEmpty()){
                        if (contraseña.length()<=6){
                            txtcontraseña.setError("Minimo 7 caracteres");
                            return;
                        }
                    }
                    else{
                        txtcontraseña.setError("Casilla Vacia");
                        return;
                    }
                    if  (nombre.length()>20){
                        txtnombre.setError("Maximo 20 caracteres");
                        return;
                    }
                    if (telefono.length()<=6){
                        txttelefono.setError("Teléfono erroneo. Minimo 7 caracteres");
                        return;
                    }else if(telefono.length()>10){
                        txttelefono.setError("Teléfono erroneo. Maximo 10 caracteres");
                    }
                }

                // FIN validacion de datos
                mProgressDialog.setTitle("Por favor espere");
                mProgressDialog.setMessage("Verificando sus datos");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                registrarusuario();
            }
        });
        // FIN Evento de boton registrar

        // Inicio Evento boton limpiar
        btnlimpiar=(Button)findViewById(R.id.btnlimpiar);
        btnlimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtnombre.setText("");
                txtapellido.setText("");
                txtcontraseña.setText("");
                txtusuario.setText("");
                txttelefono.setText("");
            }
        });
        // FIn Evento boton limpiar

        btnvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistroActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    // Metodo registo de usuario en firebase
    private void registrarusuario(){
        mAuth.createUserWithEmailAndPassword(email, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // validacion de creacion de datos
                if(task.isSuccessful()){
                    // Creacion de nodo en base de datos
                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre", nombre);
                    map.put("apellido", apellido);
                    map.put("email", email);
                    map.put("contraseña", contraseña);
                    map.put("tipo_de_sangre",rh2);
                    map.put("telefono",telefono);
                    map.put("url","");
                    String id = mAuth.getCurrentUser().getUid(); // Me trae el id cuando se crea el usuario
                    mDatabase.child("Usuarios").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                mProgressDialog.dismiss();
                                startActivity(new Intent(RegistroActivity.this, cont_emergencia.class));
                                finish();
                            }
                            else{
                                mProgressDialog.dismiss();
                                Toast.makeText(RegistroActivity.this, "No se creo los datos correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    mProgressDialog.dismiss();
                    Toast.makeText(RegistroActivity.this, "Usuario ya registrado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // FIN Metodo registo de usuario en firebase

    // metodo validar email
    public boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    // fin metodo validar email
}
