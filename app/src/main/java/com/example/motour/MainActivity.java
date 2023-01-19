package com.example.motour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText txtusuario;
    private EditText txtcontraseña;

    private Button btnregistrar;
    private Button btnlogin;
    private Button btnolvido;
    private ImageButton btnfacebook;
    private ImageButton btngoogle;

    // Variables para registro manual
    private String email="";
    private String contraseña="";

    // Cosas de bases de datos
    private FirebaseAuth mAuth;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         Se instancia cada uno de los modulos
        */

        // instanciando a firebase
        mAuth = FirebaseAuth.getInstance();

        txtusuario = findViewById(R.id.txtusuario);
        txtcontraseña = findViewById(R.id.txtcontraseña);

        btnregistrar = findViewById(R.id.btnregistrar);
        btnlogin = findViewById(R.id.btningresar);
        btnolvido = findViewById(R.id.btnolvido);
        btnfacebook = findViewById(R.id.btnfacebook);
        btngoogle = findViewById(R.id.btngoogle);
        mProgressDialog = new ProgressDialog(this);

        btngoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, oops.class ));
            }
        });

        btnfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, oops.class ));
            }
        });


        // Inicio Evento boton Ingresar
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = txtusuario.getText().toString();
                contraseña = txtcontraseña.getText().toString();

                // Valdiacion de datos
                if (email.isEmpty() && contraseña.isEmpty()){
                    txtusuario.setError("Casilla Vacia");
                    txtcontraseña.setError("Casilla Vacia");
                    return;
                }
                else{
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
                }

                mProgressDialog.setTitle("Por favor espere");
                mProgressDialog.setMessage("Verificando sus datos");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            loginusuario();
            }
        });

        // FIN Evento boton Ingresar

        // Evento de boton registrar
        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistroActivity.class ));
            }
        });
        // FIN Evento de boton registrar
        // Incio boton reestablecer contraseña
        btnolvido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, olvidopass.class));
            }
        });
        // Fin boton reestablecer contraseña

    }


    // metodo login
    private void loginusuario(){
        //mostrar que esta procesando la subida de la foto

        mAuth.signInWithEmailAndPassword(email, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    mProgressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Inicio sesion", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, Perfil.class));
                    finish();
                }
                else{
                    mProgressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos. Compruebe sus datos", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();

        // validar si ya inicio sesion
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(MainActivity.this, Perfil.class));
            finish();
        }
    }

    public boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

}
