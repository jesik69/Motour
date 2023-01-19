package com.example.motour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class olvidopass extends AppCompatActivity {

    private EditText txtusuariopass;
    private Button btnreestablecer;

    private String email = "";
    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvidopass);

        mAuth = FirebaseAuth.getInstance();
        txtusuariopass = findViewById(R.id.txtusuariopass);
        btnreestablecer = findViewById(R.id.btnreestablecer);
        mDialog = new ProgressDialog(this);

        // Inicio evento boton reestablecer
            btnreestablecer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    email = txtusuariopass.getText().toString();
                    if (!email.isEmpty()){
                        if (!validarEmail(email)){
                            txtusuariopass.setError("Email Invalido. example@motour.com.co");
                            return;
                        }
                        mDialog.setMessage("Espere un momento");
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.show();
                        resetPassword();
                    }
                    else{
                        txtusuariopass.setError("Casilla Vacia");
                        return;
                    }

                }
            });
        // FIN evento boton reestablecer

    }

    private void resetPassword() {
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(olvidopass.this, "Se envio correo para reestablecer la contraseña", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(olvidopass.this, "No se logro enviar el correo de reestablecer contraseña, verifique sus datos", Toast.LENGTH_SHORT).show();
                }

                mDialog.dismiss();
            }
        });
    }

    // metodo validar email
    public boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    // fin metodo validar email
}
