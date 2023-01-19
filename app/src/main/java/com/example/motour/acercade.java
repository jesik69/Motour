package com.example.motour;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class acercade extends AppCompatActivity {
    ImageView fundadoruno;
    ImageView fundadordos;
    ImageView fundadortres;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acercade);

        fundadoruno = findViewById(R.id.fundadoruno);
        fundadordos = findViewById(R.id.fundadordos);


        String urluno ="https://firebasestorage.googleapis.com/v0/b/pruebalogin-6a58a.appspot.com/o/foto_fundadores%2Fjeisson.jpg?alt=media&token=0dc25d2b-6ff5-4a84-8507-053430889607";
        Glide.with(acercade.this)
                .load(urluno)
                .fitCenter()
                .centerCrop()
                .into(fundadoruno);
        String urldos ="https://firebasestorage.googleapis.com/v0/b/pruebalogin-6a58a.appspot.com/o/foto_fundadores%2Fjessica.jpg?alt=media&token=637d5646-156a-47c0-b15f-ba1d65967c0a";
        Glide.with(acercade.this)
                .load(urldos)
                .fitCenter()
                .centerCrop()
                .into(fundadordos);
        //String urltres ="https://firebasestorage.googleapis.com/v0/b/pruebalogin-6a58a.appspot.com/o/foto_fundadores%2Frichard.jpg?alt=media&token=fe22d420-85c1-4dce-bd44-af487e1bb1c6";
        //Glide.with(acercade.this)
                //.load(urltres)
                //.fitCenter()
                //.centerCrop()
                //.into(fundadortres);

    }
}
