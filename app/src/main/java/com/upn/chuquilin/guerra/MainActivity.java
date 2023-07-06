package com.upn.chuquilin.guerra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.upn.chuquilin.guerra.db.AppDatabase;
import com.upn.chuquilin.guerra.entities.Duelista;
import com.upn.chuquilin.guerra.repositories.DuelistaRepository;
import com.upn.chuquilin.guerra.services.DuelistaService;
import com.upn.chuquilin.guerra.utils.RetrofitBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    EditText nameDuelista;
    Button btRegistrarDuelista;
    Button btListarDuelista;

    Retrofit mRetrofit;

    int cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRetrofit = RetrofitBuilder.build();

        nameDuelista = findViewById(R.id.nameDuelista);
        btRegistrarDuelista = findViewById(R.id.btRegistrarDuelista);
        btListarDuelista = findViewById(R.id.btListarDuelista);

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        DuelistaRepository repositoryD = db.duelistaRepository();

        btRegistrarDuelista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nameDuelista.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getBaseContext(), "Llenar Datos", Toast.LENGTH_SHORT).show();
                } else {
                    Duelista duelista = new Duelista();
                    duelista.nameDuelista = nameDuelista.getText().toString();
                    duelista.sincronizadoDuelista = false;
                    repositoryD.createDuelista(duelista);
                    nameDuelista.setText("");
                    Log.i("MAIN_APP: Guarda en DB", new Gson().toJson(duelista));
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });

        btListarDuelista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DuelistaListActivity.class);
                startActivity(intent);
            }
        });

        DuelistaService serviceD = mRetrofit.create(DuelistaService.class);

        if (isNetworkConnected()) {
            List<Duelista> SinSicroDuelistas = repositoryD.searchDuelista(false);
            for (Duelista duelista :SinSicroDuelistas) {
                Log.d("MAIN_APP: DB SSincro", new Gson().toJson(duelista));
                duelista.sincronizadoDuelista = true;
                repositoryD.updateDuelista(duelista);
                //*****SINCRO*************************
                SincronizacionDuelista(serviceD,duelista);

            }
            List<Duelista> EliminarBDDuelista= repositoryD.getAllDuelista();
            downloadingMockAPIDuelista(serviceD,repositoryD,EliminarBDDuelista);

            Toast.makeText(getBaseContext(), "SINCRONIZADO", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getBaseContext(), "NO HAY INTERNET", Toast.LENGTH_SHORT).show();

        }
    }

    private void SincronizacionDuelista(DuelistaService duelistaService, Duelista duelista){
        Call<Duelista> call= duelistaService.create(duelista);
        call.enqueue(new Callback<Duelista>() {
            @Override
            public void onResponse(Call<Duelista> call, Response<Duelista> response) {
                if (response.isSuccessful()) {
                    Duelista data = response.body();
                    Log.i("MAIN_APP: MockAPI", new Gson().toJson(data));
                }
            }

            @Override
            public void onFailure(Call<Duelista> call, Throwable t) {

            }
        });


    }
    private void downloadingMockAPIDuelista(DuelistaService duelistaService,DuelistaRepository duelistaRepository , List<Duelista> EliminarDuelista){
        //***Eleminar datos de BD
        duelistaRepository.deleteList(EliminarDuelista);
        //Cargar datos de MockAPI
        Call<List<Duelista>> call = duelistaService.getAllUser();
        call.enqueue(new Callback<List<Duelista>>() {
            @Override
            public void onResponse(Call<List<Duelista>> call, Response<List<Duelista>> response) {
                List<Duelista> data = response.body();
                Log.i("MAIN_APP", new Gson().toJson(data));
                for (Duelista cuenta : data) {
                    duelistaRepository.createDuelista(cuenta);
                }
            }

            @Override
            public void onFailure(Call<List<Duelista>> call, Throwable t) {

            }
        });
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}