package com.upn.chuquilin.guerra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.upn.chuquilin.guerra.Adapters.DuelistaAdapter;
import com.upn.chuquilin.guerra.db.AppDatabase;
import com.upn.chuquilin.guerra.entities.Duelista;
import com.upn.chuquilin.guerra.repositories.DuelistaRepository;

import java.util.List;

public class DuelistaListActivity extends AppCompatActivity {

    RecyclerView rvListaDuelista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duelista_list);

        Log.d("APP_MAIN", "ListasaaaassCuenta");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvListaDuelista = findViewById(R.id.rvListaDuelista);
        rvListaDuelista.setLayoutManager(layoutManager);

        //**LISTAR***
        LoadingDBCuenta();
        //***********
    }

    private void LoadingDBCuenta() {
        AppDatabase db = AppDatabase.getInstance(this);
        DuelistaRepository repository = db.duelistaRepository();
        List<Duelista> mdataDuelista = repository.getAllDuelista();

        DuelistaAdapter mAdapter = new DuelistaAdapter(mdataDuelista);
        rvListaDuelista.setAdapter(mAdapter);
        Log.i("MAIN_APP: DB", new Gson().toJson(mdataDuelista));
        Toast.makeText(getBaseContext(), "MOSTRANDO DATOS", Toast.LENGTH_SHORT).show();

    }
}