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

    TextView tvNroDetallesC;
    TextView tvNameDetallesC;
    Button btRegistrarMov;
    Button btListarMov;
    Button btSincroMov;
    Retrofit mRetrofit;

    String urlImage = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duelista_detalles);

//        Intent intent =  new Intent(getApplicationContext(), MapsActivity.class);
//        startActivity(intent);

        mRetrofit = RetrofitBuilder.build();

        tvNroDetallesC  = findViewById(R.id.tvNroDetalles);
        tvNameDetallesC = findViewById(R.id.tvNameDetalles);
        btRegistrarMov  = findViewById(R.id.btDetalleRegistrarDue);
        btListarMov     = findViewById(R.id.btListarDue);


        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        DuelistaRepository repositoryD = db.duelistaRepository();

        CartaRepository repositoryC = db.cartaRepository();

        int idObtener;
        idObtener = getIntent().getIntExtra("id",0);
        Log.d("APP_MAIN: idRec", String.valueOf(idObtener));


        Duelista duelista = repositoryD.searchDuelistaID(idObtener);

        tvNroDetallesC.setText(String.valueOf(duelista.id));
        tvNameDetallesC.setText(duelista.nameDuelista);





        btRegistrarMov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MovimientoRegistrarActivity.class);
//                intent.putExtra("id", idObtener);
//                Log.i("APP_MAIN: id", String.valueOf(idObtener));
//                startActivity(intent);
            }
        });

        btListarMov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), ListMovimientoActivity.class);
//                intent.putExtra("id2", idObtener);
//                Log.i("APP_MAIN: id2", String.valueOf(idObtener));
//                startActivity(intent);
            }
        });

        CartaService serviceM = mRetrofit.create(CartaService.class);

        if (isNetworkConnected()) {
                    List<Carta> SinSincroMovimientos = repositoryM.searchMovimiento(false);
                    for (Movimientos movimientos : SinSincroMovimientos) {
                        Log.d("MAIN_APP: DB SSincro", new Gson().toJson(movimientos));
                        movimientos.sincronizadoMovimientos = true;
//                        base64toLink(movimientos.imagenBase64);
//                        movimientos.urlimagen = urlImage;
                        double Latitud = LocationData.getInstance().getLatitude();
                        double Longitud = LocationData.getInstance().getLongitude();
                        movimientos.latitud = String.valueOf(Latitud);
                        movimientos.longitud= String.valueOf(Longitud);

                        repositoryM.updateMovimiento(movimientos);
                        //*****SINCRO*************************
                        SincronizacionMovimientos(serviceM,movimientos);
                    }
                    List<Movimientos> EliminarDBMovimiento = repositoryM.getAllMovimientos();
                    downloadingMockAPIMovimientos(serviceM,repositoryM,EliminarDBMovimiento);
                    Toast.makeText(getBaseContext(), "SINCRONIZADO", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getBaseContext(), "NO HAY INTERNET", Toast.LENGTH_SHORT).show();

                }


//        btSincroMov.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isNetworkConnected()) {
//                    List<Carta> SinSincroMovimientos = repositoryM.searchMovimiento(false);
//                    for (Movimientos movimientos : SinSincroMovimientos) {
//                        Log.d("MAIN_APP: DB SSincro", new Gson().toJson(movimientos));
//                        movimientos.sincronizadoMovimientos = true;
////                        base64toLink(movimientos.imagenBase64);
////                        movimientos.urlimagen = urlImage;
//                        double Latitud = LocationData.getInstance().getLatitude();
//                        double Longitud = LocationData.getInstance().getLongitude();
//                        movimientos.latitud = String.valueOf(Latitud);
//                        movimientos.longitud= String.valueOf(Longitud);
//
//                        repositoryM.updateMovimiento(movimientos);
//                        //*****SINCRO*************************
//                        SincronizacionMovimientos(serviceM,movimientos);
//                    }
//                    List<Movimientos> EliminarDBMovimiento = repositoryM.getAllMovimientos();
//                    downloadingMockAPIMovimientos(serviceM,repositoryM,EliminarDBMovimiento);
//                    Toast.makeText(getBaseContext(), "SINCRONIZADO", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(getBaseContext(), "NO HAY INTERNET", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
//    }



//    private void SincronizacionMovimientos(MovimientoService movimientoService, Movimientos movimientos) {
//        Call<Movimientos> call = movimientoService.create(movimientos);
//        call.enqueue(new Callback<Movimientos>() {
//            @Override
//            public void onResponse(Call<Movimientos> call, Response<Movimientos> response) {
//                if(response.isSuccessful()){
//                    Movimientos data = response.body();
//                    Log.i("MAIN_APP: MovMockAPI", new Gson().toJson(data));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Movimientos> call, Throwable t) {
//
//            }
//        });
//    }
//    private void downloadingMockAPIMovimientos(MovimientoService movimientoService, MovimientoRepository movimientoRepository, List<Movimientos> eliminarDBMovimiento) {
//        //***Eleminar datos de BD
//        movimientoRepository.deleteList(eliminarDBMovimiento);
//        //Cargar datos de MockAPI
//        Call<List<Movimientos>> call = movimientoService.getAllUser();
//        call.enqueue(new Callback<List<Movimientos>>() {
//            @Override
//            public void onResponse(Call<List<Movimientos>> call, Response<List<Movimientos>> response) {
//                List<Movimientos> data = response.body();
//                Log.i("MAIN_APP", new Gson().toJson(data));
//
//                for (Movimientos movimientos : data) {
//                    movimientoRepository.createMovimientos(movimientos);
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Movimientos>> call, Throwable t) {
//
//            }
//        });
//
//    }
//
//
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

//    private void base64toLink(String base64) {
//        Retrofit retrofit1 = new Retrofit.Builder()
//                .baseUrl("https://demo-upn.bit2bittest.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        MovimientoService service = retrofit1.create(MovimientoService.class);
//        Call<MovimientoService.ImagenResponse> call = service.guardarImage(new MovimientoService.ImagenToSave(base64));
//        call.enqueue(new Callback<MovimientoService.ImagenResponse>() {
//            @Override
//            public void onResponse(Call<MovimientoService.ImagenResponse> call, Response<MovimientoService.ImagenResponse> response) {
//                if (response.isSuccessful()) {
//                    MovimientoService.ImagenResponse imageResponse = response.body();
//                    Log.i("Respues", response.toString());
//                    urlImage = "https://demo-upn.bit2bittest.com/" + imageResponse.getUrl();
//                    Toast.makeText(getBaseContext(), "Link GENERADO", Toast.LENGTH_SHORT).show();
//                    Log.i("Imagen url:", urlImage);
//
//                } else {
//
//                    Log.e("Error cargar imagen",response.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovimientoService.ImagenResponse> call, Throwable t) {
//
//            }
//        });

    }
}