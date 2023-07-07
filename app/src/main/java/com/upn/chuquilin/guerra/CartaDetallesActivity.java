package com.upn.chuquilin.guerra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.upn.chuquilin.guerra.db.AppDatabase;
import com.upn.chuquilin.guerra.entities.Carta;
import com.upn.chuquilin.guerra.entities.Duelista;
import com.upn.chuquilin.guerra.mapasController.MapaMostrar;
import com.upn.chuquilin.guerra.repositories.CartaRepository;
import com.upn.chuquilin.guerra.repositories.DuelistaRepository;

public class CartaDetallesActivity extends AppCompatActivity {
    TextView nombreDetCart;
    TextView AtaqueCart;
    TextView defenzaCart;
    TextView LatitudCart;
    TextView longitudCart;
    Button btMapas;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carta_detalles);

        nombreDetCart = findViewById(R.id.nombreDetCart);
        AtaqueCart    = findViewById(R.id.AtaqueCart);
        defenzaCart   = findViewById(R.id.defenzaCart);
        LatitudCart   = findViewById(R.id.LatitudCart);
        longitudCart  = findViewById(R.id.longitudCart);
        btMapas = findViewById(R.id.btMapastas);



        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        CartaRepository repositoryC = db.cartaRepository();


        int idObtener;
        idObtener = getIntent().getIntExtra("idCD",0);
        Log.d("APP_MAIN: idCartaC", String.valueOf(idObtener));


        Carta carta = repositoryC.searchCartaID2(idObtener);

        nombreDetCart.setText(String.valueOf(carta.nameCarta));
        AtaqueCart.setText(String.valueOf(carta.puntosAtaque));
        defenzaCart.setText(String.valueOf(carta.puntosDefenza));
        LatitudCart.setText(String.valueOf(carta.latitud));
        longitudCart.setText(String.valueOf(carta.longitud));

        btMapas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapaMostrar.class);
                intent.putExtra("paisaje",carta.nameCarta);
                intent.putExtra("idL", carta.idP);
                startActivity(intent);
            }
        });



    }
}