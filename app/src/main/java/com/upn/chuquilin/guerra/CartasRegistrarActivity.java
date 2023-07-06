package com.upn.chuquilin.guerra;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.upn.chuquilin.guerra.db.AppDatabase;
import com.upn.chuquilin.guerra.entities.Carta;
import com.upn.chuquilin.guerra.entities.LocationData;
import com.upn.chuquilin.guerra.repositories.CartaRepository;
import com.upn.chuquilin.guerra.services.CartaService;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartasRegistrarActivity extends AppCompatActivity {


    EditText edNombreCart;
    EditText edAtaqueCart;
    EditText edDefenzaCart;
    TextView tvLatitudCart;
    TextView tvLongitudCart;
    TextView tvUrlImagenMov;

    Button btCamaraMov;
    Button btGaleriaCart;
    Button btRegistrarCart;

    String seleccionSpinner;
    ImageView ivImagenCart;
    String imagenBase64 = "";
    String urlImage = "";
    private static final int OPEN_CAMERA_REQUEST = 1001;
    private static final int OPEN_GALLERY_REQUEST = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartas_registrar);

        int idObtener;
        idObtener = getIntent().getIntExtra("id",0);
        Log.d("APP_MAIN: idRM", String.valueOf(idObtener));

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        CartaRepository repositoryM = db.cartaRepository();

        edNombreCart   = findViewById(R.id.edNombreCart);
        edAtaqueCart   = findViewById(R.id.edAtaqueCart);
        edDefenzaCart  = findViewById(R.id.edDefenzaCart);
        tvLatitudCart  = findViewById(R.id.tvLatitudCart);
        tvLongitudCart = findViewById(R.id.tvLongitudCart);
        ivImagenCart  = findViewById(R.id.ivImagenCart);
        btGaleriaCart  = findViewById(R.id.btGaleriaCart);
        btRegistrarCart= findViewById(R.id.btRegistrarCart);


//************************************************
//        Intent intent =  new Intent(getApplicationContext(), MapsActivity.class);
//        startActivity(intent);

        double Latitud = LocationData.getInstance().getLatitude();
        double Longitud = LocationData.getInstance().getLongitude();
        Log.d("MAIN_APP3-Lat", String.valueOf(Latitud));
        Log.d("MAIN_APP3-Long", String.valueOf(Longitud));

        tvLatitudCart.setText(String.valueOf(Latitud));
        tvLongitudCart.setText(String.valueOf(Longitud));

        btGaleriaCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btRegistrarCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edNombreCart.getText().toString().trim().isEmpty() || edAtaqueCart.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getBaseContext(), "Llenar Datos", Toast.LENGTH_SHORT).show();
                } else {
                    Carta carta = new Carta();
                    carta.duelistaID     = idObtener;
                    carta.puntosAtaque   = Integer.parseInt(String.valueOf(edAtaqueCart.getText()));
                    carta.puntosDefenza  = Integer.parseInt(String.valueOf(edDefenzaCart.getText()));
                    carta.latitud        = String.valueOf(Latitud);
                    carta.longitud       = String.valueOf(Longitud);
                    carta.imagenBase64   = imagenBase64;
                    carta.urlimagen      = urlImage;
                    carta.sincronizadoCartas = false;
                    carta.nameCarta      = String.valueOf(edNombreCart.getText());

                    //movimientos.tipoMovimiento = seleccionSpinner;

                    repositoryM.createCarta(carta);
                    Log.i("MAIN_APP: GuardaM en DB", new Gson().toJson(carta));
                }
                Intent intent = new Intent(getApplicationContext(), DuelistaDetallesActivity.class);
                intent.putExtra("id", idObtener);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OPEN_CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ivImagenCart.setImageBitmap(photo);

            imagenBase64 = BitmaptoBase64(photo);

            if (isNetworkConnected()) {
                base64toLink(imagenBase64);

            }

        }
        if(requestCode == OPEN_GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close(); // close cursor

            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            ivImagenCart.setImageBitmap(bitmap);

            imagenBase64 = BitmaptoBase64(bitmap);

            if (isNetworkConnected()) {
                base64toLink(imagenBase64);

            }

//            base64toLink(base64);
        }
    }


    private void handleOpenCamera() {
        if(checkSelfPermission(android.Manifest.permission.CAMERA)  == PackageManager.PERMISSION_GRANTED)
        {
            // abrir camara
            Log.i("MAIN_APP", "Tiene permisos para abrir la camara");
            openCamara();
        } else {
            // solicitar el permiso
            Log.i("MAIN_APP", "No tiene permisos para abrir la camara, solicitando");
            String[] permissions = new String[] {Manifest.permission.CAMERA};
            requestPermissions(permissions, 1001);
        }
    }

    private void openCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, OPEN_CAMERA_REQUEST);
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_GALLERY_REQUEST);
    }


    private String BitmaptoBase64 (Bitmap imagenBitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imagenBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        String Resulbase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Log.i("APP_MAIN", Resulbase64);
        return Resulbase64;
    }

    private Bitmap Base64toBitmap(String imagenBase64){
        byte[] decodedBytes = Base64.decode(imagenBase64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes,0,decodedBytes.length);

    }
    private void base64toLink(String base64) {
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl("https://demo-upn.bit2bittest.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CartaService service = retrofit1.create(CartaService.class);
        Call<CartaService.ImagenResponse> call = service.guardarImage(new CartaService.ImagenToSave(base64));
        call.enqueue(new Callback<CartaService.ImagenResponse>() {
            @Override
            public void onResponse(Call<CartaService.ImagenResponse> call, Response<CartaService.ImagenResponse> response) {
                if (response.isSuccessful()) {
                    CartaService.ImagenResponse imageResponse = response.body();
                    Log.i("Respues", response.toString());
                    urlImage = "https://demo-upn.bit2bittest.com/" + imageResponse.getUrl();
                    tvUrlImagenMov.setText(urlImage);
                    Toast.makeText(getBaseContext(), "Link GENERADO", Toast.LENGTH_SHORT).show();
                    Log.i("Imagen url:", urlImage);

                } else {

                    Log.e("Error cargar imagen",response.toString());
                }
            }

            @Override
            public void onFailure(Call<CartaService.ImagenResponse> call, Throwable t) {

            }
        });

    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}