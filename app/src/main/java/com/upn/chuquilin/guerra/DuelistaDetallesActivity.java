package com.upn.chuquilin.guerra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.upn.chuquilin.guerra.db.AppDatabase;
import com.upn.chuquilin.guerra.entities.Carta;
import com.upn.chuquilin.guerra.entities.Duelista;
import com.upn.chuquilin.guerra.entities.LocationData;
import com.upn.chuquilin.guerra.mapasController.MapsActivity;
import com.upn.chuquilin.guerra.repositories.CartaRepository;
import com.upn.chuquilin.guerra.repositories.DuelistaRepository;
import com.upn.chuquilin.guerra.services.CartaService;
import com.upn.chuquilin.guerra.utils.RetrofitBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DuelistaDetallesActivity extends AppCompatActivity {

    TextView tvNroDetalles;
    TextView tvNameDetalles;
    Button btDetalleRegistrarDue;
    Button btListarDue;
    Retrofit mRetrofit;

    String urlImage = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duelista_detalles);

        Intent intent =  new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);

        mRetrofit = RetrofitBuilder.build();

        tvNroDetalles  = findViewById(R.id.tvNroDetalles);
        tvNameDetalles = findViewById(R.id.tvNameDetalles);
        btDetalleRegistrarDue  = findViewById(R.id.btDetalleRegistrarDue);
        btListarDue     = findViewById(R.id.btListarDue);


        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        DuelistaRepository repositoryD = db.duelistaRepository();

        CartaRepository repositoryC = db.cartaRepository();

        int idObtener;
        idObtener = getIntent().getIntExtra("id",0);
        Log.d("APP_MAIN: idRec", String.valueOf(idObtener));


        Duelista duelista = repositoryD.searchDuelistaID(idObtener);

        tvNroDetalles.setText(String.valueOf(duelista.id));
        tvNameDetalles.setText(duelista.nameDuelista);





        btDetalleRegistrarDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CartasRegistrarActivity.class);
                intent.putExtra("id", idObtener);
                Log.i("APP_MAIN: id", String.valueOf(idObtener));
                startActivity(intent);
            }
        });

        btListarDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CartaListActivity.class);
                intent.putExtra("id2", idObtener);
                Log.i("APP_MAIN: id2", String.valueOf(idObtener));
                startActivity(intent);
            }
        });

        CartaService serviceM = mRetrofit.create(CartaService.class);

        if (isNetworkConnected()) {
                    List<Carta> SinSincroCarta = repositoryC.searchCarta(false);
                    for (Carta carta : SinSincroCarta) {
                        Log.d("MAIN_APP: DB SSincro", new Gson().toJson(carta));
                        carta.sincronizadoCartas = true;
//                        base64toLink(movimientos.imagenBase64);
//                        movimientos.urlimagen = urlImage;
                        double Latitud = LocationData.getInstance().getLatitude();
                        double Longitud = LocationData.getInstance().getLongitude();
                        carta.latitud = String.valueOf(Latitud);
                        carta.longitud= String.valueOf(Longitud);

                        repositoryC.updateCartas(carta);
                        //*****SINCRO*************************
                        SincronizacionCarta(serviceM,carta);
                    }
                    List<Carta> EliminarDBMovimiento = repositoryC.getAllCarta();
                    downloadingMockAPICarta(serviceM,repositoryC,EliminarDBMovimiento);
                    Toast.makeText(getBaseContext(), "SINCRONIZADO", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getBaseContext(), "NO HAY INTERNET", Toast.LENGTH_SHORT).show();

                }

   }



    private void SincronizacionCarta(CartaService cartaService, Carta carta) {
        Call<Carta> call = cartaService.create(carta);
        call.enqueue(new Callback<Carta>() {
            @Override
            public void onResponse(Call<Carta> call, Response<Carta> response) {
                if(response.isSuccessful()){
                    Carta data = response.body();
                    Log.i("MAIN_APP: MovMockAPI", new Gson().toJson(data));
                }
            }

            @Override
            public void onFailure(Call<Carta> call, Throwable t) {

            }
        });
    }
    private void downloadingMockAPICarta(CartaService cartaService, CartaRepository cartaRepository, List<Carta> eliminarDBCarta) {
        //***Eleminar datos de BD
        cartaRepository.deleteList(eliminarDBCarta);
        //Cargar datos de MockAPI
        Call<List<Carta>> call = cartaService.getAllUser();
        call.enqueue(new Callback<List<Carta>>() {
            @Override
            public void onResponse(Call<List<Carta>> call, Response<List<Carta>> response) {
                List<Carta> data = response.body();
                Log.i("MAIN_APP", new Gson().toJson(data));

                for (Carta carta : data) {
                    cartaRepository.createCarta(carta);

                }
            }

            @Override
            public void onFailure(Call<List<Carta>> call, Throwable t) {

            }
        });

    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

//    private void base64toLink(String base64) {
//        Retrofit retrofit1 = new Retrofit.Builder()
//                .baseUrl("https://demo-upn.bit2bittest.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        CartaService service = retrofit1.create(CartaService.class);
//        Call<CartaService.ImagenResponse> call = service.guardarImage(new CartaService.ImagenToSave(base64));
//        call.enqueue(new Callback<CartaService.ImagenResponse>() {
//            @Override
//            public void onResponse(Call<CartaService.ImagenResponse> call, Response<CartaService.ImagenResponse> response) {
//                if (response.isSuccessful()) {
//                    CartaService.ImagenResponse imageResponse = response.body();
//                    Log.i("Respues", response.toString());
//                    urlImage = "https://demo-upn.bit2bittest.com/" + imageResponse.getUrl();
//                    Toast.makeText(getBaseContext(), "Link GENERADO", Toast.LENGTH_SHORT).show();
//                    Log.i("Imagen url:", urlImage);
//
//                } else {
//
//                    Log.e("Error cargar imagen", response.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CartaService.ImagenResponse> call, Throwable t) {
//
//            }
//        });
//
//    }
}