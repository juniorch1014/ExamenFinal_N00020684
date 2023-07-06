package com.upn.chuquilin.guerra.services;

import com.upn.chuquilin.guerra.entities.Carta;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CartaService {

    @GET("cartas")
    Call<List<Carta>> getAllUser();

    @GET("cartas")
    Call<List<Carta>> getAllUser(@Query("limit") int limit, @Query("page") int page);

    @GET("cartas")
    Call<List<Carta>> getBuscar(@Query("search") String nombre);

    @GET("cartas")
    Call<List<Carta>> getCartasBySincro(@Query("sincronizadoCartas") boolean sincronizar);

    @GET("cartas/{id}")
    Call<Carta> findUser(@Path("id") int id);

    // users
    @POST("cartas")
    Call<Carta> create(@Body Carta user);

    @PUT("cartas/{id}")
    Call<Carta> update(@Path("id") int id, @Body Carta user);

    @DELETE("cartas/{id}")
    Call<Void> delete(@Path("id") int id);
}
