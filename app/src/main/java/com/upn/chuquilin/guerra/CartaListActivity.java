package com.upn.chuquilin.guerra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.upn.chuquilin.guerra.Adapters.CartaAdapter;
import com.upn.chuquilin.guerra.db.AppDatabase;
import com.upn.chuquilin.guerra.entities.Carta;
import com.upn.chuquilin.guerra.repositories.CartaRepository;

import java.util.List;

public class CartaListActivity extends AppCompatActivity {

    RecyclerView rvListaCarta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carta_list);

        Log.d("MAIN_APP", "ListasaaaaMovimieeentos");

        int idObtener;
        idObtener = getIntent().getIntExtra("id2",0);
        Log.d("APP_MAIN: idListM", String.valueOf(idObtener));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvListaCarta = findViewById(R.id.rvListaCarta);
        rvListaCarta.setLayoutManager(layoutManager);

        //**LISTAR***
        LoadingDBMovimiento(idObtener);
    }

    private void LoadingDBMovimiento(int idObtenido) {
        AppDatabase db = AppDatabase.getInstance(this);
        CartaRepository repository = db.cartaRepository();
        List<Carta> mdataCarta = repository.searchCartaID(idObtenido);

        CartaAdapter mAdapter = new CartaAdapter(mdataCarta);
        rvListaCarta.setAdapter(mAdapter);
        Log.i("MAIN_APP: DBMovi", new Gson().toJson(mdataCarta));
        Toast.makeText(getBaseContext(), "MOSTRANDO DATOS", Toast.LENGTH_SHORT).show();

    }
}