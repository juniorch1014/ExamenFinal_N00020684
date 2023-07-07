package com.upn.chuquilin.guerra.mapasController;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.upn.chuquilin.guerra.R;
import com.upn.chuquilin.guerra.databinding.ActivityMapaMostrarBinding;
import com.upn.chuquilin.guerra.db.AppDatabase;
import com.upn.chuquilin.guerra.entities.Carta;
import com.upn.chuquilin.guerra.entities.LocationData;
import com.upn.chuquilin.guerra.repositories.CartaRepository;

public class MapaMostrar extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapaMostrarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapaMostrarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        CartaRepository repositoryC = db.cartaRepository();

        int idObtener;
        idObtener = getIntent().getIntExtra("idL",0);
        Log.d("APP_MAIN: idMaps", String.valueOf(idObtener));

        Carta carta = repositoryC.searchCartaID2(idObtener);

        double Latitud = Double.parseDouble(carta.latitud);
        double Longitud = Double.parseDouble(carta.longitud);
        String nombre;
        nombre = getIntent().getStringExtra("paisaje");
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(Latitud, Longitud );
        mMap.addMarker(new MarkerOptions().position(sydney).title(nombre));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        Log.i("MAIN_APPMapMos: Location - ",  "Latitude: " + Latitud);
        Log.i("MAIN_APPMapMos: Location - ",  "Longitude: " + Longitud);
    }
}